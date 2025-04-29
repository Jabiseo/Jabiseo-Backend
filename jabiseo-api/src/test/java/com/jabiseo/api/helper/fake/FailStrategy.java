package com.jabiseo.api.helper.fake;

public interface FailStrategy {

    boolean shouldFail(int count, Long id);

}
