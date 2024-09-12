package com.jabiseo.analysis.application.usecase;

import com.jabiseo.analysis.dto.FindVulnerableSubjectResponse;
import com.jabiseo.analysis.service.AnalysisService;
import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.member.domain.Member;
import com.jabiseo.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindVulnerableSubjectsUseCase {

    private final MemberRepository memberRepository;
    private final AnalysisService analysisService;

    public List<FindVulnerableSubjectResponse> execute(Long memberId) {

        Member member = memberRepository.getReferenceById(memberId);
        member.validateCurrentCertificate();
        Certificate certificate = member.getCurrentCertificate();

        return analysisService.findVulnerableSubjects(member, certificate).stream()
                .map(FindVulnerableSubjectResponse::from)
                .toList();
    }

}
