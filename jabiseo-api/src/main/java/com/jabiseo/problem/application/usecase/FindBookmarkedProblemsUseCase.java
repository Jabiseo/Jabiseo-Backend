package com.jabiseo.problem.application.usecase;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.member.domain.Member;
import com.jabiseo.member.domain.MemberRepository;
import com.jabiseo.problem.domain.Problem;
import com.jabiseo.problem.domain.ProblemRepository;
import com.jabiseo.problem.dto.FindBookmarkedProblemsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindBookmarkedProblemsUseCase {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private final MemberRepository memberRepository;

    private final ProblemRepository problemRepository;

    public FindBookmarkedProblemsResponse execute(Long memberId, Long examId, List<Long> subjectIds, int page) {

        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE);

        Member member = memberRepository.getReferenceById(memberId);
        member.validateCurrentCertificate();

        Certificate certificate = member.getCurrentCertificate();
        certificate.validateExamIdAndSubjectIds(examId, subjectIds);

        Page<Problem> problems;
        if (examId != null) {
            problems = problemRepository.findBookmarkedByExamIdAndSubjectIdIn(memberId, examId, subjectIds, pageable);
        } else {
            problems = problemRepository.findBookmarkedBySubjectIdIn(memberId, subjectIds, pageable);
        }

        return FindBookmarkedProblemsResponse.of(
                problems.getTotalElements(),
                problems.getTotalPages(),
                problems.getContent()
        );
    }
}
