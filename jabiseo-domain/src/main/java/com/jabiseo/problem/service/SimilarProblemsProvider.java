package com.jabiseo.problem.service;

import java.util.List;

public interface SimilarProblemsProvider {

    List<Long> findSimilarProblemIds(Long problemId, Long certificateId, int similarProblemsCount);

}
