package com.jabiseo.problem.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem, String> {

    // TODO: rand() 쿼리를 native로 하는 것은 성능상 좋지 않음. 추후 수정 필요
    @Query(value = "select * from problem where exam_id = :examId and subject_id = :subjectId order by rand() limit :count", nativeQuery = true)
    List<Problem> findRandomByExamIdAndSubjectId(String examId, String subjectId, int count);

    // TODO: rand() 쿼리를 native로 하는 것은 성능상 좋지 않음. 추후 수정 필요
    @Query(value = "select * from problem where subject_id = :subjectId order by rand() limit :count", nativeQuery = true)
    List<Problem> findRandomBySubjectId(String subjectId, int count);

    Page<Problem> findByIdInAndExamIdAndSubjectIdIn(List<String> ids, String examId, List<String> subjectIds, Pageable pageable);

    Page<Problem> findByIdInAndSubjectIdIn(List<String> ids, List<String> subjectIds, Pageable pageable);

}
