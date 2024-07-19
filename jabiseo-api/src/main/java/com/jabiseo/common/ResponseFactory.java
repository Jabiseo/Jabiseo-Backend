package com.jabiseo.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jabiseo.exception.ErrorCode;
import com.jabiseo.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

import java.io.IOException;

public class ResponseFactory {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void fail(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        ErrorResponse failResponse = ErrorResponse.of(errorCode);
        response.setStatus(errorCode.getStatusCode());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter()
                .write(mapper.writeValueAsString(failResponse));
    }

}
