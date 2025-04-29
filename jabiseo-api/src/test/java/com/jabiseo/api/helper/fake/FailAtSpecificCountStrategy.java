package com.jabiseo.api.helper.fake;

public class FailAtSpecificCountStrategy implements FailStrategy {

    private final int failAt;

    public FailAtSpecificCountStrategy(int failAt) {
        this.failAt = failAt;
    }


    @Override
    public boolean shouldFail(int count, Long id) {
        return count == failAt;
    }
}
