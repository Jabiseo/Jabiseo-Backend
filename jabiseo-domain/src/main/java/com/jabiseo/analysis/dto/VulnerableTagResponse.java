package com.jabiseo.analysis.dto;

public record VulnerableTagResponse(
        Long tagId,
        String tagName,
        int vulnerableRate
) {
    public static VulnerableTagResponse of(Long tagId, String tagName, int vulnerableRate) {
        return new VulnerableTagResponse(tagId, tagName, vulnerableRate);
    }
}
