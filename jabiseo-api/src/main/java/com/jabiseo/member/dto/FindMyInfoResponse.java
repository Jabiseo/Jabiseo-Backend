package com.jabiseo.member.dto;

public record FindMyInfoResponse(
        String memberId,
        String nickname,
        String email,
        String profileImage
) {

}
