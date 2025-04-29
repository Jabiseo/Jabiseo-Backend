package com.jabiseo.domain.member.repository;

import com.jabiseo.domain.member.domain.Member;
import com.jabiseo.domain.member.domain.OauthServer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByOauthIdAndOauthServer(String oauthId, OauthServer oauthServer);

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.currentCertificate WHERE m.id = :memberId")
    Optional<Member> findByIdWithCertificate(Long memberId);


    @Query("""
      SELECT m FROM Member m LEFT JOIN FETCH m.deviceTokens
    """)
    Page<Member> findAllWithDeviceTokens(Pageable pageable);

    @Query("SELECT m FROM Member m WHERE m.id > :lastIndex ORDER BY m.id ASC limit :limit ")
    List<Member> findAllByLastIndex(@Param("lastIndex") long lastIndex, @Param("limit") long limit);

}
