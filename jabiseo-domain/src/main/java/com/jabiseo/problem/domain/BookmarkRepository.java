package com.jabiseo.problem.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, String> {

    boolean existsByMemberIdAndProblemId(String memberId, String problemId);

}
