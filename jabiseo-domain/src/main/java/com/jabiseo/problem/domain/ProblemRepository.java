package com.jabiseo.problem.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

    // TODO: rand() 쿼리를 native로 하는 것은 성능상 좋지 않음. 추후 수정 필요
    @Query(value = "select * from problem where exam_id = :examId and subject_id = :subjectId order by rand() limit :count", nativeQuery = true)
    List<Problem> findRandomByExamIdAndSubjectId(Long examId, Long subjectId, int count);

    // TODO: rand() 쿼리를 native로 하는 것은 성능상 좋지 않음. 추후 수정 필요
    @Query(value = "select * from problem where subject_id = :subjectId order by rand() limit :count", nativeQuery = true)
    List<Problem> findRandomBySubjectId(Long subjectId, int count);

    @Query(value = "select p from Bookmark b join b.problem p where b.member.id = :memberId and p.exam.id = :examId and p.subject.id in :subjectIds")
    Page<Problem> findBookmarkedByExamIdAndSubjectIdIn(String memberId, Long examId, List<Long> subjectIds, Pageable pageable);

    @Query(value = "select p from Bookmark b join b.problem p where b.member.id = :memberId and p.subject.id in :subjectIds")
    Page<Problem> findBookmarkedBySubjectIdIn(String memberId, List<Long> subjectIds, Pageable pageable);
}
