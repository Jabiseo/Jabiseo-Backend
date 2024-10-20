package com.jabiseo.infra.opensearch.helper;

import jakarta.json.JsonValue;

public record OpenSearchResultDto(
        String id,
        double score,
        JsonValue source
) {
    public static OpenSearchResultDto of(String id, double score, JsonValue source) {
        return new OpenSearchResultDto(id, score, source);
    }
}
