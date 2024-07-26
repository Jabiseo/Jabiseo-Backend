package com.jabiseo.problem.application.usecase;

import com.jabiseo.problem.domain.Bookmark;
import com.jabiseo.problem.domain.BookmarkRepository;
import com.jabiseo.problem.dto.DeleteBookmarkRequest;
import com.jabiseo.problem.exception.ProblemBusinessException;
import com.jabiseo.problem.exception.ProblemErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteBookmarkUseCase {

    private final BookmarkRepository bookmarkRepository;

    public void execute(String memberId, DeleteBookmarkRequest request) {
        Bookmark bookmark = bookmarkRepository.findByMemberIdAndProblemId(memberId, request.problemId())
                .orElseThrow(() -> new ProblemBusinessException(ProblemErrorCode.BOOKMARK_NOT_FOUND));

        bookmarkRepository.delete(bookmark);
    }
}
