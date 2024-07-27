package com.jabiseo.problem.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, String> {

    boolean existsByMemberIdAndProblemId(String memberId, Long problemId);

    Optional<Bookmark> findByMemberIdAndProblemId(String memberId, Long problemId);

}
