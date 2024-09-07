package com.jabiseo.problem.service;

import java.util.List;

public interface SimilarProblemsProvider {

    List<Long> findSimilarProblems(Long problemId, Long certificateId, int similarProblemsCount);

}
