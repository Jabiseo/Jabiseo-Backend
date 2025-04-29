package com.jabiseo.domain.common;

public interface IdempotencyChecker {

    boolean check(String key, int ttl);
    void reset(String key);

}
