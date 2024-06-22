package com.jabiseo.member.dto;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.member.domain.Member;

public record FindMyCertificateStateResponse(
        String memberId,
        String certificateId
) {
    public static FindMyCertificateStateResponse of(Member member, Certificate certificate) {
        if (certificate == null) {
            return new FindMyCertificateStateResponse(member.getId(), null);
        }
        return new FindMyCertificateStateResponse(member.getId(), certificate.getId());
    }
}
