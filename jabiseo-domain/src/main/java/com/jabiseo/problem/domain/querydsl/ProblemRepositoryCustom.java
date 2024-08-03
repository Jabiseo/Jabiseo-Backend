package com.jabiseo.problem.domain.querydsl;

import com.jabiseo.problem.dto.ProblemWithBookmarkDetailDto;

import java.util.List;

public interface ProblemRepositoryCustom {

    List<ProblemWithBookmarkDetailDto> findDetailRandomByExamIdAndSubjectIdWithBookmark(Long memberId, Long examId, Long subjectId, int count);

    List<ProblemWithBookmarkDetailDto> findDetailByIdsInWithBookmark(Long memberId, List<Long> problemIds);
}
