package com.jabiseo.member.dto;

public record FindMyCertificateStateResponse(
        String memberId,
        String certificateId
) {
    public static FindMyCertificateStateResponse of(String memberId, String certificateId) {
        return new FindMyCertificateStateResponse(memberId, certificateId);
    }
}
