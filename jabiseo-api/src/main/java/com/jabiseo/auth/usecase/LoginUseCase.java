package com.jabiseo.auth.usecase;

import com.jabiseo.auth.dto.LoginResponse;
import org.springframework.stereotype.Service;

@Service
public class LoginUseCase {
    public LoginResponse execute(String idToken) {
        return new LoginResponse("access_token", "refresh_token");
    }
}
