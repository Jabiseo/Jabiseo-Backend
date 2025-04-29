package com.jabiseo.api.helper.fake;

import java.util.Set;

public class FailForSpecificIdStrategy implements FailStrategy {

    private final Set<Long> ids;

    public FailForSpecificIdStrategy(Set<Long> ids) {
        this.ids = ids;
    }

    @Override
    public boolean shouldFail(int count, Long id) {
        return ids.contains(id);
    }
}
