package com.jabiseo.member.usecase;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.member.domain.Member;
import com.jabiseo.member.domain.MemberRepository;
import com.jabiseo.member.dto.FindMyCertificateStateResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.jabiseo.fixture.CertificateFixture.createCertificate;
import static com.jabiseo.fixture.MemberFixture.createMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@DisplayName("자격증 상태 조회 테스트")
@ExtendWith(MockitoExtension.class)
class FindMyCertificateStateUseCaseTest {

    @InjectMocks
    FindMyCertificateStateUseCase sut;

    @Mock
    MemberRepository memberRepository;

    @Test
    @DisplayName("자격증 상태 조회")
    void givenMemberId_whenFindingCertificateStatus_thenFindCertificateStatus() throws Exception {
        //given
        String memberId = "1";
        String certificateId = "2";
        Certificate certificate = createCertificate(certificateId);
        Member member = createMember(memberId);
        member.updateCertificateState(certificate);
        given(memberRepository.findCertificateStateById(memberId)).willReturn(certificate);

        //when
        FindMyCertificateStateResponse response = sut.execute(memberId);

        //then
        assertThat(response.memberId()).isEqualTo(memberId);
        assertThat(response.certificateId()).isEqualTo(certificateId);
    }

}
