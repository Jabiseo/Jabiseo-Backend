package com.jabiseo.auth.application.usecase;

import com.jabiseo.cache.RedisCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutUseCase {

    private final RedisCacheRepository redisCacheRepository;

    public void execute(String memberId) {
        redisCacheRepository.deleteToken(memberId);
    }

}
