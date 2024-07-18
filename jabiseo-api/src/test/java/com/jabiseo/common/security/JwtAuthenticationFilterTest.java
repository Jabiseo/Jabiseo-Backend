package com.jabiseo.common.security;

import com.jabiseo.auth.application.JwtClaim;
import com.jabiseo.auth.application.JwtHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("Jwt 필터 테스트")
@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @InjectMocks
    JwtAuthenticationFilter jwtAuthenticationFilter;
    @Mock
    JwtHandler jwtHandler;

    MockHttpServletRequest request;
    MockHttpServletResponse response;
    FilterChain chain;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        chain = mock(FilterChain.class);
    }

    @AfterEach
    void tearDown() throws Exception {
        SecurityContextHolder.clearContext();
    }

    @ParameterizedTest
    @DisplayName("요청 헤더 중 Authorization 값이 잘못된 값이 들어가 있다면 jwt 검증을 처리하지 않고 진행한다")
    @ValueSource(strings = {"tokewq", "eqweq", "eqweqwe", "Bearer",})
    void containsInvalidTokenPassProcess(String headerValue) throws ServletException, IOException {
        //given
        request.addHeader("Authorization", headerValue);

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, chain);

        //then
        verify(jwtHandler, never()).validateAccessToken(any());
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("Authorization 값에 토큰형식의 값이 있다면 jwt검증을 요청한다.")
    void containsTokenStartJwtValidating() throws ServletException, IOException {
        //given
        String token = "tokens";
        String headerValue = "Bearer " + token;
        request.addHeader("Authorization", headerValue);

        given(jwtHandler.validateAccessToken(token)).willReturn(true);
        given(jwtHandler.getClaimsFromAccessToken(token)).willReturn(new JwtClaim("memberId"));

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, chain);

        //then
        verify(jwtHandler, times(1)).validateAccessToken(token);
    }

    @Test
    @DisplayName("jwt 검증 성공시 SecurityContextHolder 에 memberId가 포함된 인증 정보를 저장한다.")
    void jwtValidSuccessSaveAuthenticationInfo() throws ServletException, IOException {
        //given
        String token = "tokens";
        String headerValue = "Bearer " + token;
        request.addHeader("Authorization", headerValue);
        String memberId = "memberId";


        given(jwtHandler.validateAccessToken(token)).willReturn(true);
        given(jwtHandler.getClaimsFromAccessToken(token)).willReturn(new JwtClaim(memberId));

        //when
        jwtAuthenticationFilter.doFilterInternal(request, response, chain);

        //then
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
        assertThat(authentication.getPrincipal().toString()).isEqualTo(memberId);
    }


}