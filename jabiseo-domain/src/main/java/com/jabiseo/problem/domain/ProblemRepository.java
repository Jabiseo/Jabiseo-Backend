package com.jabiseo.problem.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem, String> {

    @Query(value = "select * from problem where exam_id = :examId and subject_id = :subjectId order by rand() limit :count", nativeQuery = true)
    List<Problem> findRandomByExamIdAndSubjectId(String examId, String subjectId, int count);

    @Query(value = "select * from problem where subject_id = :subjectId order by rand() limit :count", nativeQuery = true)
    List<Problem> findRandomBySubjectId(String subjectId, int count);
}
