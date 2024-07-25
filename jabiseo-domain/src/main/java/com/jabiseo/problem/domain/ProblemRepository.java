package com.jabiseo.problem.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProblemRepository extends JpaRepository<Problem, String> {

    // TODO: rand() 쿼리를 native로 하는 것은 성능상 좋지 않음. 추후 수정 필요
    @Query(value = "select * from problem where exam_id = :examId and subject_id = :subjectId order by rand() limit :count", nativeQuery = true)
    List<Problem> findRandomByExamIdAndSubjectId(String examId, String subjectId, int count);

    // TODO: rand() 쿼리를 native로 하는 것은 성능상 좋지 않음. 추후 수정 필요
    @Query(value = "select * from problem where subject_id = :subjectId order by rand() limit :count", nativeQuery = true)
    List<Problem> findRandomBySubjectId(String subjectId, int count);

    @Query(value = "select p from Bookmark b join b.problem p where b.member.id = :memberId and p.exam.id = :examId and p.subject.id in :subjectIds")
    List<Problem> findBookmarkedByExamIdAndSubjectIdIn(String memberId, Optional<String> examId, List<String> subjectIds, Pageable pageable);
}
