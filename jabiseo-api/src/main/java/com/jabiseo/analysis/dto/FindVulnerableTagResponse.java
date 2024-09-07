package com.jabiseo.analysis.dto;

public record FindVulnerableTagResponse(
    Long tagId,
    String tagName,
    int vulnerableRate
) {
    public static FindVulnerableTagResponse from(VulnerableTagResponse vulnerableTagResponse) {
        return new FindVulnerableTagResponse(
            vulnerableTagResponse.tagId(),
            vulnerableTagResponse.tagName(),
            vulnerableTagResponse.vulnerableRate()
        );
    }
}
