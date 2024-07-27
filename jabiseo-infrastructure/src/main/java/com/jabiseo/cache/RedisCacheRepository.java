package com.jabiseo.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jabiseo.client.OidcPublicKey;
import com.jabiseo.exception.BusinessException;
import com.jabiseo.exception.CommonErrorCode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class RedisCacheRepository {

    private final RedisTemplate<String, String> redisStringTemplate;
    private final ValueOperations<String, String> operation;
    private static final String MEMBER_TOKEN_PREFIX = "member_token:";
    private final ObjectMapper mapper = new ObjectMapper();

    public RedisCacheRepository(RedisTemplate<String, String> redisStringTemplate) {
        this.redisStringTemplate = redisStringTemplate;
        this.operation = redisStringTemplate.opsForValue();
    }


    public void saveToken(String key, String value) {
        operation.set(MEMBER_TOKEN_PREFIX + key, value);
    }

    public String getToken(String key) {
        return operation.get(MEMBER_TOKEN_PREFIX + key);
    }

    public void savePublicKey(String key, List<OidcPublicKey> publicKeys) {
        try {
            String publicKeyString = mapper.writeValueAsString(publicKeys);
            // TODO: timeout 값 논의 필요
            operation.set(key, publicKeyString, 1, TimeUnit.DAYS);
        } catch (JsonProcessingException e) {
            throw new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public List<OidcPublicKey> getPublicKeys(String key) {
        String values = operation.get(key);
        if (values == null) {
            return null;
        }
        try {
            return Arrays.asList(mapper.readValue(values, OidcPublicKey[].class));
        } catch (JsonProcessingException e) {
            throw new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }


}
