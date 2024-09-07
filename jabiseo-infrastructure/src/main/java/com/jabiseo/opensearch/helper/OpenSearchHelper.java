package com.jabiseo.opensearch.helper;

import com.jabiseo.client.NetworkApiErrorCode;
import com.jabiseo.client.NetworkApiException;
import jakarta.json.JsonNumber;
import lombok.RequiredArgsConstructor;
import org.opensearch.client.json.JsonData;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.FieldValue;
import org.opensearch.client.opensearch._types.query_dsl.TermQuery;
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
public class OpenSearchHelper {

    private static final int KNN_K = 100;

    private final OpenSearchClient openSearchClient;

    public List<Float> fetchVector(Long id, String indexName, String vectorName) {
        GetResponse<JsonData> getResponse = fetchFromOpenSearch(id, indexName, vectorName);
        return parseVectorFromResponse(getResponse, vectorName);
    }

    private GetResponse<JsonData> fetchFromOpenSearch(Long id, String indexName, String vectorName) {
        try {
            return openSearchClient.get(GetRequest.of(getReq -> getReq
                    .index(indexName)
                    .id(String.valueOf(id))
                    .sourceIncludes(vectorName)
            ), JsonData.class);
        } catch (IOException e) {
            throw new NetworkApiException(NetworkApiErrorCode.OPEN_SEARCH_API_FAIL);
        }
    }

    private List<Float> parseVectorFromResponse(GetResponse<JsonData> getResponse, String vectorName) {
        return getResponse.source()
                .toJson()
                .asJsonObject()
                .get(vectorName)
                .asJsonArray()
                .stream()
                .map(JsonNumber.class::cast)
                .map(jsonNumber -> (float) jsonNumber.doubleValue())
                .toList();
    }

    public List<Long> searchSimilarIds(List<Float> vector, String indexName, String vectorName, int targetSize) {
        SearchRequest searchRequest = createKnnSearchRequestForIds(vector, indexName, vectorName, targetSize);
        SearchResponse<JsonData> searchResponse = executeSearch(searchRequest);
        return extractIds(searchResponse, targetSize);
    }

    public List<OpenSearchResultDto> searchSimilarItems(List<Float> vector, String indexName, String vectorName) {
        SearchRequest searchRequest = createKnnSearchRequest(vector, indexName, vectorName);
        SearchResponse<JsonData> searchResponse = executeSearch(searchRequest);
        return extractResult(searchResponse);
    }

    public List<OpenSearchResultDto> searchSimilarItems(List<Float> vector, String indexName, String vectorName, int targetSize) {
        SearchRequest searchRequest = createKnnSearchRequest(vector, indexName, vectorName, targetSize);
        SearchResponse<JsonData> searchResponse = executeSearch(searchRequest);
        return extractResult(searchResponse);
    }

    public List<Long> searchSimilarIdsWithFiltering(List<Float> vector, String indexName, String vectorName, int targetSize, String filterName, Long filterValue) {
        SearchRequest searchRequest = createKnnSearchRequestForIdsWithFiltering(vector, indexName, vectorName, targetSize, filterName, filterValue);
        SearchResponse<JsonData> searchResponse = executeSearch(searchRequest);
        return extractIds(searchResponse, targetSize);
    }

    private SearchRequest createKnnSearchRequestForIds(List<Float> vector, String indexName, String vectorName, int size) {
        // id만 필요한 경우, source 필드는 제외
        return SearchRequest.of(searchRequest ->
                searchRequest.index(indexName)
                        .size(size)
                        .source(source -> source
                                // id만 필요하므로 나머지 필드는 제외
                                .filter(filter -> filter.includes("")))
                        .query(query -> query
                                .knn(knn -> knn
                                        .field(vectorName)
                                        .vector(convertToArray(vector))
                                        .k(KNN_K)
                                )
                        )
        );
    }

    private SearchRequest createKnnSearchRequest(List<Float> vector, String indexName, String vectorName) {
        return SearchRequest.of(searchRequest ->
                searchRequest.index(indexName)
                        .query(query -> query
                                .knn(knn -> knn
                                        .field(vectorName)
                                        .vector(convertToArray(vector))
                                        .k(KNN_K)
                                )
                        )
        );
    }

    private SearchRequest createKnnSearchRequest(List<Float> vector, String indexName, String vectorName, int size) {
        return SearchRequest.of(searchRequest ->
                searchRequest.index(indexName)
                        .size(size)
                        .query(query -> query
                                .knn(knn -> knn
                                        .field(vectorName)
                                        .vector(convertToArray(vector))
                                        .k(KNN_K)
                                )
                        )
        );
    }

    private SearchRequest createKnnSearchRequestForIdsWithFiltering(List<Float> vector, String indexName, String vectorName, int size, String filterName, Long filterValue) {
        return SearchRequest.of(searchRequest ->
                searchRequest.index(indexName)
                        .size(size)
                        .query(query -> query
                                .knn(knn -> knn
                                        .field(vectorName)
                                        .vector(convertToArray(vector))
                                        .k(KNN_K)
                                        .filter(TermQuery.of(term -> term
                                                        .field(filterName)
                                                        .value(FieldValue.of(filterValue)))
                                                ._toQuery()
                                        )
                                )
                        )
        );
    }

    private SearchResponse<JsonData> executeSearch(SearchRequest searchRequest) {
        try {
            return openSearchClient.search(searchRequest, JsonData.class);
        } catch (IOException e) {
            throw new NetworkApiException(NetworkApiErrorCode.OPEN_SEARCH_API_FAIL);
        }
    }

    private List<Long> extractIds(SearchResponse<JsonData> searchResponse, int size) {
        // 검색 결과에서 유사 문제 ID 추출
        try {
            return searchResponse.hits().hits().stream()
                    .map(Hit::id)
                    .map(Long::parseLong)
                    .limit(size)
                    .toList();
        } catch (Exception e) {
            throw new NetworkApiException(NetworkApiErrorCode.OPEN_SEARCH_API_FAIL);
        }
    }

    private List<OpenSearchResultDto> extractResult(SearchResponse<JsonData> searchResponse) {
        try {
            return searchResponse.hits().hits().stream()
                    .map(hit -> OpenSearchResultDto.of(
                            hit.id(),
                            hit.score(),
                            hit.source().toJson().asJsonObject()
                    ))
                    .toList();
        } catch (Exception e) {
            throw new NetworkApiException(NetworkApiErrorCode.OPEN_SEARCH_API_FAIL);
        }
    }

    public static float[] convertToArray(List<Float> list) {
        float[] array = new float[list.size()];
        IntStream.range(0, list.size()).forEach(i -> array[i] = list.get(i));
        return array;
    }
}
