package com.jabiseo.member.dto;

import com.jabiseo.member.domain.Member;

public record FindMyCertificateStatusResponse(
        String memberId,
        String certificateId
) {
    public static FindMyCertificateStatusResponse from(Member member) {
        return new FindMyCertificateStatusResponse(
                member.getId(),
                member.getCertificateState().getId()
        );
    }
}
