package com.jabiseo.problem.domain;

import com.jabiseo.problem.domain.querydsl.ProblemRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProblemRepository extends ProblemRepositoryCustom, JpaRepository<Problem, Long> {

    @Query(value = "select p from Bookmark b join b.problem p where b.member.id = :memberId and p.exam.id = :examId and p.subject.id in :subjectIds")
    Page<Problem> findBookmarkedByExamIdAndSubjectIdIn(Long memberId, Long examId, List<Long> subjectIds, Pageable pageable);

    @Query(value = "select p from Bookmark b join b.problem p where b.member.id = :memberId and p.subject.id in :subjectIds")
    Page<Problem> findBookmarkedBySubjectIdIn(Long memberId, List<Long> subjectIds, Pageable pageable);
}
