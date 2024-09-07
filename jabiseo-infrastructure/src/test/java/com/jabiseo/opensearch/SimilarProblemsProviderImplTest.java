package com.jabiseo.opensearch;

import com.jabiseo.client.NetworkApiErrorCode;
import com.jabiseo.client.NetworkApiException;
import com.jabiseo.opensearch.redis.SimilarProblemIdCache;
import com.jabiseo.opensearch.redis.SimilarProblemIdCacheRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.core.GetRequest;
import org.opensearch.client.opensearch.core.SearchRequest;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@DisplayName("유사 문제 기능 테스트")
@ExtendWith(MockitoExtension.class)
class SimilarProblemsProviderImplTest {

    @InjectMocks
    SimilarProblemsProviderImpl sut;

    @Mock
    SimilarProblemIdCacheRepository similarProblemIdCacheRepository;

    @Mock
    OpenSearchClient openSearchClient;

    @Test
    @DisplayName("redis 캐시에 유사 문제 id가 저장되어 있는 경우 Opensearch와 통신하지 않는다.")
    void givenCachedProblemId_whenFindSimilarProblemIds_thenDontConnectWithOpenSearch() throws Exception {
        //given
        Long certificateId = 1L; //정보처리기사
        Long problemId = 2L;
        int similarProblemsSize = 3;
        given(similarProblemIdCacheRepository.findById(problemId))
                .willReturn(Optional.of(new SimilarProblemIdCache(problemId, List.of(2L, 3L, 4L))));

        //when
        sut.findSimilarProblems(problemId, certificateId, similarProblemsSize);

        //then
        verify(openSearchClient, never()).get((GetRequest) any(), any());
        verify(openSearchClient, never()).search((SearchRequest) any(), any());
    }

    @Test
    @DisplayName("opensearch에 문제 벡터 조회 시 오류가 발생하면 NetworkApiException을 던진다.")
    void givenErrorInOpenSearchConnection_whenFindSimilarProblems_thenThrowNetworkApiException() throws Exception {
        //given
        Long certificateId = 1L; //정보처리기사
        Long problemId = 2L;
        int similarProblemsSize = 3;

        given(similarProblemIdCacheRepository.findById(problemId)).willReturn(Optional.empty());
        given(openSearchClient.get((GetRequest) any(), any())).willThrow(new IOException());

        //when & then
        assertThatThrownBy(() -> sut.findSimilarProblems(problemId, certificateId, similarProblemsSize))
                .isInstanceOf(NetworkApiException.class)
                .hasFieldOrPropertyWithValue("errorCode", NetworkApiErrorCode.OPENSEARCH_API_FAIL);
    }

}
