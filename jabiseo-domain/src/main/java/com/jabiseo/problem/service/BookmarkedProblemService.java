package com.jabiseo.problem.service;

import com.jabiseo.problem.domain.Problem;
import com.jabiseo.problem.domain.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookmarkedProblemService {

    private final ProblemRepository problemRepository;

    public List<Problem> findBookmarkedProblems(List<String> problemIds, Optional<String> examId, List<String> subjectIds, Pageable pageable) {
        // examId의 존재 유무에 따라 다른 쿼리를 날려 문제를 가져옴
        return examId.map(rawExamId ->
                        problemRepository.findByIdInAndExamIdAndSubjectIdIn(problemIds, rawExamId, subjectIds, pageable))
                .orElseGet(() -> problemRepository.findByIdInAndSubjectIdIn(problemIds, subjectIds, pageable))
                .toList();
    }
}
