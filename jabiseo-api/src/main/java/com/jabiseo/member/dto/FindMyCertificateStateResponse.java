package com.jabiseo.member.dto;

import com.jabiseo.member.domain.Member;

public record FindMyCertificateStateResponse(
        String memberId,
        String certificateId
) {
    public static FindMyCertificateStateResponse from(Member member) {
        return new FindMyCertificateStateResponse(
                member.getId(),
                member.getCertificateState().getId()
        );
    }
}
