package com.jabiseo.domain.member.repository;

import com.jabiseo.domain.member.domain.DeviceToken;
import com.jabiseo.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {

    Optional<DeviceToken> findByMemberAndDeviceId(Member member, String deviceId);

    void deleteByMemberAndDeviceId(Member member, String deviceId);

    @Query("""
    SELECT d FROM DeviceToken d WHERE d.member.id IN :memberIds
    """)
    List<DeviceToken> findByMemberIdIn(@Param("memberIds") List<Long> memberIds);

    List<Long> id(Long id);
}
