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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @Test
    @DisplayName("존재하지 않는 회원의 자격증 상태 변경")
    void givenCertificateIdAndNonExistedMemberId_whenUpdatingCertificateState_thenReturnError() {
        //given
        String nonExistedMemberId = "1";
        String certificateId = "2";
        given(memberRepository.findById(nonExistedMemberId)).willReturn(Optional.empty());

        //when & then
        UpdateMyCertificateStateRequest request = new UpdateMyCertificateStateRequest(certificateId);
        assertThatThrownBy(() -> sut.execute(nonExistedMemberId, request))
                .isInstanceOf(MemberBusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", MemberErrorCode.MEMBER_NOT_FOUND);

    }

    @Test
    @DisplayName("존재하지 않는 자격증 상태 변경")
    void givenMemberIdAndNonExistedCertificateId_whenUpdatingCertificateState_thenReturnError() throws Exception {
        //given
        String memberId = "1";
        String nonExistedCertificateId = "2";
        Member member = createMember(memberId);
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(certificateRepository.findById(nonExistedCertificateId)).willReturn(Optional.empty());

        //when & then
        UpdateMyCertificateStateRequest request = new UpdateMyCertificateStateRequest(nonExistedCertificateId);
        assertThatThrownBy(() -> sut.execute(memberId, request))
                .isInstanceOf(CertificateBusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", CertificateErrorCode.CERTIFICATE_NOT_FOUND);
    }

}
