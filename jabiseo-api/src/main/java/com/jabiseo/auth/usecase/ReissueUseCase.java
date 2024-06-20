package com.jabiseo.auth.usecase;

import com.jabiseo.auth.dto.LoginResponse;
import org.springframework.stereotype.Service;

@Service
public class ReissueUseCase {
    public LoginResponse reissue(String refreshToken) {
        return new LoginResponse("access_token", "refresh_token");
    }
}
