package com.jabiseo.member.repository;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.member.domain.Member;
import com.jabiseo.member.domain.MemberRepository;
import com.jabiseo.member.exception.MemberBusinessException;
import com.jabiseo.member.exception.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final JpaMemberRepository jpaMemberRepository;

    @Override
    public Certificate findCertificateStateById(String id) {
        Member member = jpaMemberRepository.findById(id)
                .orElseThrow(() -> new MemberBusinessException(MemberErrorCode.MEMBER_NOT_FOUND));
        if (member.getCertificateState() == null) {
            throw new MemberBusinessException(MemberErrorCode.CERTIFICATE_STATE_NOT_FOUND);
        }
        return member.getCertificateState();
    }
}
