package com.jabiseo.member.usecase;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.certificate.domain.CertificateRepository;
import com.jabiseo.certificate.exception.CertificateBusinessException;
import com.jabiseo.certificate.exception.CertificateErrorCode;
import com.jabiseo.member.domain.Member;
import com.jabiseo.member.domain.MemberRepository;
import com.jabiseo.member.dto.UpdateMyCertificateStateRequest;
import com.jabiseo.member.exception.MemberBusinessException;
import com.jabiseo.member.exception.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateMyCertificateStateUseCase {

    private final MemberRepository memberRepository;
    private final CertificateRepository certificateRepository;

    public void execute(String memberId, UpdateMyCertificateStateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberBusinessException(MemberErrorCode.MEMBER_NOT_FOUND));
        Certificate certificate = certificateRepository.findById(request.certificateId())
                .orElseThrow(() -> new MemberBusinessException(MemberErrorCode.CURRENT_CERTIFICATE_NOT_EXIST));
        member.updateCertificateState(certificate);
    }
}
