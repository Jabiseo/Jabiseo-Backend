package com.jabiseo.member.repository;

import com.jabiseo.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMemberRepository extends JpaRepository<Member, String> {
}
