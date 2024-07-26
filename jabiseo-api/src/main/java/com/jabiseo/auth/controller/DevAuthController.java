package com.jabiseo.auth.controller;

import com.jabiseo.auth.application.DevLoginHelper;
import com.jabiseo.auth.dto.LoginResponse;
import com.jabiseo.exception.CommonErrorCode;
import com.jabiseo.exception.ErrorResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DevAuthController {

    private final Environment environment;
    private final DevLoginHelper loginHelper;
    private static final String LIMIT_PROFILE = "local";

    @GetMapping("/dev/auth")
    public ResponseEntity<?> devAuth(@RequestParam(value = "member-id") @NotBlank String memberId) {
        if (!isLocalProfiles(environment.getActiveProfiles())) {
            return ResponseEntity.status(CommonErrorCode.FORBIDDEN.getStatusCode()).body(ErrorResponse.of(CommonErrorCode.FORBIDDEN));
        }

        LoginResponse result = loginHelper.login(memberId);
        return ResponseEntity.ok(result);
    }


    private boolean isLocalProfiles(String[] profiles) {
        return Arrays.asList(profiles).contains(LIMIT_PROFILE);
    }

}
