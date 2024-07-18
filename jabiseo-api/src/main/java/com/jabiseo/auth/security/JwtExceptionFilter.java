package com.jabiseo.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
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
            send(response, e.getErrorCode());
        } catch (Exception e) {
            send(response, CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private void send(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getMessage(), errorCode.getErrorCode());
        response.setStatus(errorCode.getStatusCode());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter()
                .write(objectMapper.writeValueAsString(errorResponse));
    }


}
