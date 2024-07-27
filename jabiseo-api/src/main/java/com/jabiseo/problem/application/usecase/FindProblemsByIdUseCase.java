package com.jabiseo.problem.application.usecase;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.member.domain.Member;
import com.jabiseo.member.domain.MemberRepository;
import com.jabiseo.problem.domain.ProblemRepository;
import com.jabiseo.problem.dto.CertificateResponse;
import com.jabiseo.problem.dto.FindProblemsRequest;
import com.jabiseo.problem.dto.FindProblemsResponse;
import com.jabiseo.problem.dto.ProblemsDetailResponse;
import com.jabiseo.problem.exception.ProblemBusinessException;
import com.jabiseo.problem.exception.ProblemErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindProblemsByIdUseCase {

    private final ProblemRepository problemRepository;
    private final MemberRepository memberRepository;

    // TODO: 문제에 북마크 되어 있는지 표시해야 함
    public FindProblemsResponse execute(String memberId, FindProblemsRequest request) {
        Member member = memberRepository.getReferenceById(memberId);
        member.validateCurrentCertificate();
        Certificate certificate = member.getCurrentCertificate();

        CertificateResponse certificateResponse = CertificateResponse.from(certificate);
        List<ProblemsDetailResponse> problemsDetailResponses = request.problemIds().stream()
                .map(problemId -> problemRepository.findById(problemId)
                        .orElseThrow(() -> new ProblemBusinessException(ProblemErrorCode.PROBLEM_NOT_FOUND)))
                .peek(problem -> problem.validateProblemInCertificate(certificate))
                .map(ProblemsDetailResponse::from)
                .toList();
        return FindProblemsResponse.of(certificateResponse, problemsDetailResponses);
    }
}
