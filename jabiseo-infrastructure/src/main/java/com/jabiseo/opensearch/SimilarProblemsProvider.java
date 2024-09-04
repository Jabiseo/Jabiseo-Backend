package com.jabiseo.opensearch;

import com.jabiseo.client.NetworkApiErrorCode;
import com.jabiseo.client.NetworkApiException;
import com.jabiseo.opensearch.redis.SimilarProblemIdCache;
import com.jabiseo.opensearch.redis.SimilarProblemIdCacheRepository;
import jakarta.json.JsonNumber;
import lombok.RequiredArgsConstructor;
import org.opensearch.client.json.JsonData;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.core.GetRequest;
import org.opensearch.client.opensearch.core.GetResponse;
import org.opensearch.client.opensearch.core.SearchRequest;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.search.Hit;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class SimilarProblemsProvider {

    private static final String VECTOR_NAME = "problem_vector";
    private static final int KNN_K = 3;

    private final SimilarProblemIdCacheRepository similarProblemIdCacheRepository;
    private final OpenSearchClient openSearchClient;

    public List<Long> findSimilarProblems(Long problemId, Long certificateId, int similarProblemsSize) {
        // 캐시에 저장된 유사 문제 ID가 있으면 반환, 없으면 opensearch에서 검색 후 캐시에 저장
        return similarProblemIdCacheRepository.findById(problemId)
                .orElseGet(() -> fetchAndCacheSimilarProblems(problemId, certificateId, similarProblemsSize))
                .getSimilarProblemIds();
    }

    private SimilarProblemIdCache fetchAndCacheSimilarProblems(Long problemId, Long certificateId, int similarProblemsSize) {
        // 자격증에 해당하는 인덱스 이름
        String indexName = CertificateIndex.findByCertificateId(certificateId).getIndexName();

        List<Float> vector = fetchProblemVector(problemId, indexName);
        List<Long> similarProblemIds = searchSimilarProblems(vector, indexName, similarProblemsSize);

        // 캐시에 저장 후 반환
        SimilarProblemIdCache cache = new SimilarProblemIdCache(problemId, similarProblemIds);
        return similarProblemIdCacheRepository.save(cache);
    }

    private List<Float> fetchProblemVector(Long problemId, String indexName) {
        GetResponse<JsonData> getResponse = getProblemFromOpenSearch(problemId, indexName);
        return parseVectorFromResponse(getResponse);
    }

    private GetResponse<JsonData> getProblemFromOpenSearch(Long problemId, String indexName) {
        try {
            return openSearchClient.get(GetRequest.of(getReq -> getReq
                    .index(indexName)
                    .id(String.valueOf(problemId))
                    .sourceIncludes(VECTOR_NAME)
            ), JsonData.class);
        } catch (IOException e) {
            throw new NetworkApiException(NetworkApiErrorCode.OPENSEARCH_API_FAIL);
        }
    }

    private List<Float> parseVectorFromResponse(GetResponse<JsonData> getResponse) {
        return getResponse.source()
                .toJson()
                .asJsonObject()
                .get(VECTOR_NAME)
                .asJsonArray()
                .stream()
                .map(JsonNumber.class::cast)
                .map(jsonNumber -> (float) jsonNumber.doubleValue())
                .toList();

    }

    private List<Long> searchSimilarProblems(List<Float> vector, String indexName, int similarProblemsSize) {
        SearchRequest searchRequest = createSearchRequest(vector, indexName, similarProblemsSize);
        SearchResponse<JsonData> searchResponse = executeSearch(searchRequest);
        return extractSimilarProblemIds(searchResponse);
    }

    private SearchRequest createSearchRequest(List<Float> vector, String indexName, int similarProblemsSize) {
        // opensearch에 검색할 요청 생성
        return SearchRequest.of(searchRequest ->
                searchRequest.index(indexName)
                        .size(similarProblemsSize + 1)
                        .source(source -> source
                                // id만 필요하므로 나머지 필드는 제외
                                .filter(filter -> filter.includes("")))
                        .query(query -> query
                                .knn(knn -> knn
                                        .field(VECTOR_NAME)
                                        .vector(convertToArray(vector))
                                        .k(KNN_K)
                                )
                        )
        );
    }

    private SearchResponse<JsonData> executeSearch(SearchRequest searchRequest) {
        // opensearch에 검색
        try {
            return openSearchClient.search(searchRequest, JsonData.class);
        } catch (IOException e) {
            throw new NetworkApiException(NetworkApiErrorCode.OPENSEARCH_API_FAIL);
        }
    }

    private List<Long> extractSimilarProblemIds(SearchResponse<JsonData> searchResponse) {
        // 검색 결과에서 유사 문제 ID 추출
        return searchResponse.hits().hits().stream()
                .map(Hit::id)
                .map(Long::parseLong)
                .skip(1) // 첫 번쨰 원소는 원래 문제 자체이므로 스킵
                .limit(KNN_K)
                .toList();
    }

    private static float[] convertToArray(List<Float> list) {
        float[] array = new float[list.size()];
        IntStream.range(0, list.size()).forEach(i -> array[i] = list.get(i));
        return array;
    }
}
