package com.jabiseo.problem.service;

import com.jabiseo.problem.dto.ProblemSearchDto;

import java.util.List;

public interface ProblemSearchProvider {
    List<ProblemSearchDto> searchProblem(String query, Double lastScore, Long lastId, Long certificateId, int searchProblemCount);
}
