package com.jabiseo.problem.application.usecase;

import com.jabiseo.problem.domain.ProblemRepository;
import com.jabiseo.problem.dto.FindProblemDetailResponse;
import com.jabiseo.problem.dto.ProblemWithBookmarkDetailQueryDto;
import com.jabiseo.problem.dto.ProblemsDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindProblemDetailUseCase {

    private final ProblemRepository problemRepository;

    public FindProblemDetailResponse execute(Long memberId, Long problemId) {

        ProblemWithBookmarkDetailQueryDto dto = problemRepository.findDetailByIdWithBookmark(memberId, problemId);
        ProblemsDetailResponse problemDetailResponse = ProblemsDetailResponse.from(dto);

        return FindProblemDetailResponse.of(problemDetailResponse);
    }
}
