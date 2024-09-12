package com.jabiseo.problem.application.usecase;

import com.jabiseo.analysis.service.AnalysisService;
import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.member.domain.Member;
import com.jabiseo.member.domain.MemberRepository;
import com.jabiseo.problem.dto.CertificateResponse;
import com.jabiseo.problem.dto.FindProblemsResponse;
import com.jabiseo.problem.dto.ProblemsDetailResponse;
import com.jabiseo.problem.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindRecommendedProblemsUseCase {

    private final MemberRepository memberRepository;
    private final AnalysisService analysisService;
    private final ProblemService problemService;

    public FindProblemsResponse execute(Long memberId) {
        Member member = memberRepository.getReferenceById(memberId);
        member.validateCurrentCertificate();
        Certificate certificate = member.getCurrentCertificate();

        List<Long> vulnerableProblems = analysisService.findVulnerableProblems(member, certificate);

        CertificateResponse certificateResponse = CertificateResponse.from(certificate);
        List<ProblemsDetailResponse> problemsDetailResponses = problemService.findProblemsById(memberId, vulnerableProblems).stream()
                .map(ProblemsDetailResponse::from)
                .toList();

        return FindProblemsResponse.of(certificateResponse, problemsDetailResponses);
    }

}
