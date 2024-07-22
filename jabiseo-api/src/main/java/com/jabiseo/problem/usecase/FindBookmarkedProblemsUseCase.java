package com.jabiseo.problem.usecase;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.member.domain.Member;
import com.jabiseo.member.domain.MemberRepository;
import com.jabiseo.member.exception.MemberBusinessException;
import com.jabiseo.member.exception.MemberErrorCode;
import com.jabiseo.problem.domain.Bookmark;
import com.jabiseo.problem.domain.Problem;
import com.jabiseo.problem.dto.FindBookmarkedProblemsResponse;
import com.jabiseo.problem.service.BookmarkedProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindBookmarkedProblemsUseCase {

    private final MemberRepository memberRepository;
    private final BookmarkedProblemService bookmarkedProblemService;


    public List<FindBookmarkedProblemsResponse> execute(String memberId, Optional<String> examId, List<String> subjectIds, Pageable pageable) {

        Member member = memberRepository.getReferenceById(memberId);

        if (!member.containsCertificate()) {
            throw new MemberBusinessException(MemberErrorCode.CURRENT_CERTIFICATE_NOT_EXIST);
        }
        Certificate certificate = member.getCertificateState();

        certificate.validateSubjectIds(subjectIds);
        certificate.validateExamId(examId);

        List<Bookmark> bookmarks = member.getBookmarks();
        List<String> problemIds = bookmarks.stream()
                .map(Bookmark::getProblem)
                .map(Problem::getId)
                .toList();

        List<Problem> problems = bookmarkedProblemService.findBookmarkedProblems(problemIds, examId, subjectIds, pageable);

        return problems.stream()
                .map(FindBookmarkedProblemsResponse::from)
                .toList();
    }

}
