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

    private static final String INDEX_NAME = "jabiseo_problems";
    private static final int KNN_K = 3;

    private final SimilarProblemIdCacheRepository similarProblemIdCacheRepository;
    private final OpenSearchClient openSearchClient;

    public List<Long> getSimilarProblems(Long problemId, int similarProblemsSize) {
        return similarProblemIdCacheRepository.findById(problemId)
                .orElseGet(() -> fetchAndCacheSimilarProblems(problemId, similarProblemsSize))
                .getSimilarProblemIds();
    }

    private SimilarProblemIdCache fetchAndCacheSimilarProblems(Long problemId, int similarProblemsSize) {
        float[] vector = fetchProblemVector(problemId);
        List<Long> similarProblemIds = searchSimilarProblems(vector, similarProblemsSize);

        // Save to cache and return
        SimilarProblemIdCache cache = new SimilarProblemIdCache(problemId, similarProblemIds);
        return similarProblemIdCacheRepository.save(cache);
    }

    private float[] fetchProblemVector(Long problemId) {
        GetResponse<JsonData> getResponse = getProblemFromOpenSearch(problemId);
        double[] doubleVector = parseVectorFromResponse(getResponse);

        return convertToFloatArray(doubleVector);
    }

    private GetResponse<JsonData> getProblemFromOpenSearch(Long problemId) {
        try {
            return openSearchClient.get(GetRequest.of(getReq ->
                    getReq.index(INDEX_NAME).id(String.valueOf(problemId))
            ), JsonData.class);
        } catch (IOException e) {
            throw new NetworkApiException(NetworkApiErrorCode.OPENSEARCH_API_FAIL);
        }
    }

    private double[] parseVectorFromResponse(GetResponse<JsonData> getResponse) {
        return getResponse.source()
                .toJson()
                .asJsonObject()
                .get("problem_vector")
                .asJsonArray()
                .stream()
                .map(JsonNumber.class::cast)
                .mapToDouble(JsonNumber::doubleValue)
                .toArray();
    }

    private float[] convertToFloatArray(double[] doubleVector) {
        float[] vector = new float[doubleVector.length];
        IntStream.range(0, doubleVector.length)
                .forEach(i -> vector[i] = (float) doubleVector[i]);
        return vector;
    }

    private List<Long> searchSimilarProblems(float[] vector, int similarProblemsSize) {
        SearchRequest searchRequest = createSearchRequest(vector, similarProblemsSize);
        SearchResponse<JsonData> searchResponse = executeSearch(searchRequest);
        return extractSimilarProblemIds(searchResponse);
    }

    private SearchRequest createSearchRequest(float[] vector, int similarProblemsSize) {
        return SearchRequest.of(searchRequest ->
                searchRequest.index(INDEX_NAME)
                        .size(similarProblemsSize + 1)
                        .source(source -> source
                                // id만 필요하므로 나머지 필드는 제외
                                .filter(filter -> filter.includes("")))
                        .query(query -> query
                                .knn(knn -> knn
                                        .field("problem_vector")
                                        .vector(vector)
                                        .k(KNN_K)
                                )
                        )
        );
    }

    private SearchResponse<JsonData> executeSearch(SearchRequest searchRequest) {
        try {
            return openSearchClient.search(searchRequest, JsonData.class);
        } catch (IOException e) {
            throw new NetworkApiException(NetworkApiErrorCode.OPENSEARCH_API_FAIL);
        }
    }

    private List<Long> extractSimilarProblemIds(SearchResponse<JsonData> searchResponse) {
        return searchResponse.hits().hits().stream()
                .map(Hit::id)
                .map(Long::parseLong)
                .skip(1) // 첫 번쨰 원소는 원래 문제 자체이므로 스킵
                .limit(KNN_K)
                .toList();
    }
}
