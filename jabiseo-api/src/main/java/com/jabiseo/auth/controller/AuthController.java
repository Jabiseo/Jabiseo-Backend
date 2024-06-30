package com.jabiseo.auth.controller;

import com.jabiseo.auth.dto.LoginRequest;
import com.jabiseo.auth.dto.LoginResponse;
import com.jabiseo.auth.usecase.LoginUseCase;
import com.jabiseo.auth.usecase.LogoutUseCase;
import com.jabiseo.auth.usecase.ReissueUseCase;
import com.jabiseo.auth.usecase.WithdrawUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final LoginUseCase loginUseCase;

    private final ReissueUseCase reissueUseCase;

    private final LogoutUseCase logoutUseCase;

    private final WithdrawUseCase withdrawUseCase;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse result = loginUseCase.execute(loginRequest);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/reissue")
    public ResponseEntity<LoginResponse> reissue(String refreshToken) {
        LoginResponse result = reissueUseCase.reissue(refreshToken);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        logoutUseCase.execute();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdraw() {
        withdrawUseCase.execute();
        return ResponseEntity.noContent().build();
    }
}
