package com.jabiseo.member.usecase;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.certificate.domain.CertificateRepository;
import com.jabiseo.member.domain.Member;
import com.jabiseo.member.domain.MemberRepository;
import com.jabiseo.member.dto.UpdateMyCertificateStateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.jabiseo.fixture.CertificateFixture.createCertificate;
import static com.jabiseo.fixture.MemberFixture.createMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@DisplayName("자격증 상태 변경 테스트")
@ExtendWith(MockitoExtension.class)
class UpdateMyCertificateStateUseCaseTest {

    @InjectMocks
    UpdateMyCertificateStateUseCase sut;

    @Mock
    MemberRepository memberRepository;

    @Mock
    CertificateRepository certificateRepository;

    @Test
    @DisplayName("자격증 상태 변경")
    void givenMemberIdAndCertificateId_whenUpdatingCertificateState_thenUpdateCertificateState() {
        //given
        String memberId = "1";
        String certificateId = "2";
        Member member = createMember(memberId);
        Certificate certificate = createCertificate(certificateId);
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(certificateRepository.findById(certificateId)).willReturn(Optional.of(certificate));

        //when
        UpdateMyCertificateStateRequest request = new UpdateMyCertificateStateRequest(certificateId);
        sut.execute(memberId, request);

        //then
        assertThat(member.getCertificateState()).isEqualTo(certificate);
    }

}
