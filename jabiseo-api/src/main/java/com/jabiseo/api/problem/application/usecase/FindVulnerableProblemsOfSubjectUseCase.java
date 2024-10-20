package com.jabiseo.api.problem.application.usecase;

import com.jabiseo.domain.analysis.service.AnalysisService;
import com.jabiseo.api.problem.dto.CertificateResponse;
import com.jabiseo.api.problem.dto.FindProblemsResponse;
import com.jabiseo.api.problem.dto.ProblemsDetailResponse;
import com.jabiseo.domain.certificate.domain.Certificate;
import com.jabiseo.domain.member.domain.Member;
import com.jabiseo.domain.member.domain.MemberRepository;
import com.jabiseo.domain.problem.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindVulnerableProblemsOfSubjectUseCase {

    private final MemberRepository memberRepository;
    private final ProblemService problemService;
    private final AnalysisService analysisService;

    public FindProblemsResponse execute(Long memberId, Long subjectId) {
        Member member = memberRepository.getReferenceById(memberId);
        member.validateCurrentCertificate();
        Certificate certificate = member.getCurrentCertificate();

        List<Long> vulnerableProblemIdsOfSubject = analysisService.findVulnerableProblemIdsOfSubject(member, certificate, subjectId);

        CertificateResponse certificateResponse = CertificateResponse.from(certificate);
        List<ProblemsDetailResponse> problemsDetailResponses = problemService.findProblemsById(memberId, vulnerableProblemIdsOfSubject).stream()
                .map(ProblemsDetailResponse::from)
                .toList();

        return FindProblemsResponse.of(certificateResponse, problemsDetailResponses);
    }

}
