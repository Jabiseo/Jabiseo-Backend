package com.jabiseo.auth.controller;

import com.jabiseo.auth.application.DevLoginHelper;
import com.jabiseo.auth.dto.LoginResponse;
import com.jabiseo.common.security.JwtAuthenticationFilter;
import com.jabiseo.common.security.JwtExceptionFilter;
import com.jabiseo.common.security.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = DevAuthController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtExceptionFilter.class)
})
@WithMockUser
class DevAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    DevLoginHelper devLoginHelper;

    @Test
    @DisplayName("개발용 로그인 요청")
    void devLoginSuccess() throws Exception {
        //given
        Long memberId = 1234L;
        given(devLoginHelper.login(memberId)).willReturn(new LoginResponse("accc", "refresh"));

        //when
        ResultActions perform = mockMvc.perform(get("/api/dev/auth?member-id=" + memberId));

        //then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("accc"))
                .andExpect(jsonPath("$.refreshToken").value("refresh"));
    }
}