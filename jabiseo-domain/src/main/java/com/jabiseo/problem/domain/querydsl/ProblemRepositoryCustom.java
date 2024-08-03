package com.jabiseo.problem.domain.querydsl;

import com.jabiseo.problem.dto.ProblemWithBookmarkDetailDto;
import com.jabiseo.problem.dto.ProblemWithBookmarkSummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProblemRepositoryCustom {

    List<ProblemWithBookmarkDetailDto> findDetailRandomByExamIdAndSubjectIdWithBookmark(Long memberId, Long examId, Long subjectId, int count);

    List<ProblemWithBookmarkDetailDto> findDetailByIdsInWithBookmark(Long memberId, List<Long> problemIds);

    Page<ProblemWithBookmarkSummaryDto> findBookmarkedSummaryByExamIdAndSubjectIdsInWithBookmark(Long memberId, Long examId, List<Long> subjectIds, Pageable pageable);
}
