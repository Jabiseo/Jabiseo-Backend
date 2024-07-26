package com.jabiseo.member.dto;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.member.domain.Member;

public record FindMyCertificateStateResponse(
        String memberId,
        String certificateId
) {
    public static FindMyCertificateStateResponse of(Member member, Certificate certificate) {
        return new FindMyCertificateStateResponse(member.getId(), certificate.getId());
    }

    public static FindMyCertificateStateResponse from(Member member) {
        return new FindMyCertificateStateResponse(member.getId(), null);
    }
}
