package com.jabiseo.problem.usecase;

import com.jabiseo.member.domain.Member;
import com.jabiseo.member.domain.MemberRepository;
import com.jabiseo.problem.domain.Bookmark;
import com.jabiseo.problem.domain.BookmarkRepository;
import com.jabiseo.problem.domain.Problem;
import com.jabiseo.problem.domain.ProblemRepository;
import com.jabiseo.problem.dto.CreateBookmarkRequest;
import com.jabiseo.problem.exception.ProblemBusinessException;
import com.jabiseo.problem.exception.ProblemErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateBookmarkUseCase {

    private final MemberRepository memberRepository;

    private final ProblemRepository problemRepository;

    private final BookmarkRepository bookmarkRepository;

    public String execute(String memberId, CreateBookmarkRequest request) {
        if (bookmarkRepository.existsByMemberIdAndProblemId(memberId, request.problemId())) {
            throw new ProblemBusinessException(ProblemErrorCode.BOOKMARK_ALREADY_EXISTS);
        }

        Member member = memberRepository.getReferenceById(memberId);
        Problem problem = problemRepository.findById(request.problemId())
                .orElseThrow(() -> new ProblemBusinessException(ProblemErrorCode.PROBLEM_NOT_FOUND));


        Bookmark bookmark = Bookmark.of(member, problem);

        return bookmarkRepository.save(bookmark).getId();
    }
}
