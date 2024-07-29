package com.jabiseo.auth.application.usecase;

import com.jabiseo.auth.application.JwtHandler;
import com.jabiseo.auth.dto.ReissueRequest;
import com.jabiseo.auth.dto.ReissueResponse;
import com.jabiseo.auth.exception.AuthenticationBusinessException;
import com.jabiseo.auth.exception.AuthenticationErrorCode;
import com.jabiseo.cache.RedisCacheRepository;
import com.jabiseo.member.domain.Member;
import com.jabiseo.member.domain.MemberRepository;
import fixture.MemberFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ReissueUseCaseTest {

    @InjectMocks
    ReissueUseCase reissueUseCase;

    @Mock
    MemberRepository memberRepository;

    @Mock
    RedisCacheRepository redisCacheRepository;

    @Mock
    JwtHandler jwtHandler;

    ReissueRequest request;

    @BeforeEach
    void setUp() {
        request = new ReissueRequest("refresh");
    }

    @Test
    @DisplayName("저장된 토큰이 없는 경우 예외를 반환한다")
    void savedTokenIsNullThrownException() {
        //given
        String memberId = "id";
        given(memberRepository.getReferenceById(memberId)).willReturn(MemberFixture.createMember(memberId));
        given(redisCacheRepository.findToken(memberId)).willReturn(Optional.empty());

        //when then
        assertThatThrownBy(() -> reissueUseCase.execute(request, memberId))
                .isInstanceOf(AuthenticationBusinessException.class);
    }

    @Test
    @DisplayName("다른 refreshToken으로 요청하면 예외를 반환한다")
    void otherTokenRequestThrownException() {
        //given
        String memberId = "id";
        String otherToken = "tokens";
        given(memberRepository.getReferenceById(memberId)).willReturn(MemberFixture.createMember(memberId));
        given(redisCacheRepository.findToken(memberId)).willReturn(Optional.of(otherToken));

        //when then
        assertThatThrownBy(() -> reissueUseCase.execute(request, memberId))
                .isInstanceOf(AuthenticationBusinessException.class)
                .hasMessage(AuthenticationErrorCode.NOT_MATCH_REFRESH.getMessage());
    }

    @Test
    @DisplayName("정상 요청의 경우 새로운 access Token을 발급한다.")
    void requestSuccessReturnNewAccessToken(){
        //given
        String memberId = "id";
        Member member = MemberFixture.createMember(memberId);
        String newAccessToken = "accessToken";
        given(memberRepository.getReferenceById(memberId)).willReturn(member);
        given(redisCacheRepository.findToken(memberId)).willReturn(Optional.of(request.refreshToken()));
        given(jwtHandler.createAccessToken(member)).willReturn(newAccessToken);

        //when
        ReissueResponse execute = reissueUseCase.execute(request, memberId);

        //then
        assertThat(execute.accessToken()).isEqualTo(newAccessToken);
    }
}
