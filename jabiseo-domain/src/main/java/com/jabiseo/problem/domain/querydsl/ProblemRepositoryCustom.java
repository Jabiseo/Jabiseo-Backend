package com.jabiseo.problem.domain.querydsl;

import com.jabiseo.problem.dto.ProblemWithBookmarkDto;

import java.util.List;

public interface ProblemRepositoryCustom {

    List<ProblemWithBookmarkDto> findRandomByExamIdAndSubjectIdWithBookmark(Long memberId, Long examId, Long subjectId, int count);

}
