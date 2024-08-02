package com.jabiseo.member.dto;

import com.jabiseo.member.domain.Member;

public record UpdateProfileImageResponse(String nickname,
                                         String email,
                                         String profileImage,
                                         String memberId) {

    public static UpdateProfileImageResponse of(Member member) {
        return new UpdateProfileImageResponse(member.getNickname(), member.getEmail(), member.getProfileImage(), member.getId().toString());
    }
}
