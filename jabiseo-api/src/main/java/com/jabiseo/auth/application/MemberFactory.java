package com.jabiseo.auth.application;


import com.jabiseo.auth.application.oidc.OauthMemberInfo;
import com.jabiseo.member.domain.Member;
import com.jabiseo.member.domain.RandomNicknameGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MemberFactory {

    @Value("${jabiseo.default-image-url}")
    private String DEFAULT_IMAGE_URL;

    private final RandomNicknameGenerator randomNicknameGenerator;

    public Member createNew(OauthMemberInfo oauthMemberInfo) {
        String nickname = randomNicknameGenerator.generate();

        // TODO: ID 생성 전략을 통해 따로 생성해야 한다.
        String id = UUID.randomUUID().toString();
        return Member.of(id, oauthMemberInfo.getEmail(), nickname, oauthMemberInfo.getOauthId(), oauthMemberInfo.getOauthServer(), DEFAULT_IMAGE_URL);
    }
}
