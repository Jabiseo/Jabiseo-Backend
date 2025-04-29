package com.jabiseo.api.helper.fake;

public class AlwaysSuccessStrategy implements FailStrategy {

    @Override
    public boolean shouldFail(int count, Long id) {
        return false; // 항상 성공
    }

}
