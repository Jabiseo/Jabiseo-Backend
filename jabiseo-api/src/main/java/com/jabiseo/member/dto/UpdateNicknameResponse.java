package com.jabiseo.member.dto;

import com.jabiseo.member.domain.Member;

public record UpdateNicknameResponse(String nickname,
                                     String email,
                                     String profileImage,
                                     String memberId) {

    public static UpdateNicknameResponse of(Member member) {
        return new UpdateNicknameResponse(member.getNickname(), member.getEmail(), member.getProfileImage(), member.getId().toString());
    }

}
