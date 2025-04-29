package com.jabiseo.infra.cache;

import com.jabiseo.domain.common.IdempotencyChecker;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisIdempotencyChecker implements IdempotencyChecker {
    private final RedisTemplate<String, String> redisStringTemplate;

    public RedisIdempotencyChecker(RedisTemplate<String, String> redisStringTemplate) {
        this.redisStringTemplate = redisStringTemplate;
    }
    @Override
    public boolean check(String key, int ttl) {
        Boolean result = redisStringTemplate.opsForValue().setIfAbsent(key, "1", ttl, TimeUnit.HOURS);
        return Boolean.TRUE.equals(result);
    }

    @Override
    public void reset(String key) {
        redisStringTemplate.opsForValue().getAndDelete(key);
    }
}
