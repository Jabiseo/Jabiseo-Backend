package com.jabiseo.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jabiseo.common.ResponseFactory;
import com.jabiseo.exception.BusinessException;
import com.jabiseo.exception.CommonErrorCode;
import com.jabiseo.exception.ErrorCode;
import com.jabiseo.exception.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (BusinessException e) {
            ResponseFactory.fail(response, e.getErrorCode());
        } catch (Exception e) {
            ResponseFactory.fail(response, CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

}
