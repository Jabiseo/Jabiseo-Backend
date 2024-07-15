package com.jabiseo.fixture;

import com.jabiseo.member.domain.Member;
import com.jabiseo.member.domain.OauthServer;

public class MemberFixture {

    public static Member createMember(String memberId) {
        return Member.of(memberId, "email", "name",
                "oauth2Id", OauthServer.KAKAO, "profileImage");
    }

}
