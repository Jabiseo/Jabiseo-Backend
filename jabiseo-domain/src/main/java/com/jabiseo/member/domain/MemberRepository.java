package com.jabiseo.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findByOauthIdAndOauthServer(String oauthId, OauthServer oauthServer);
}
