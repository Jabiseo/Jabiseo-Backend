package com.jabiseo.certificate.usecase;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.certificate.domain.CertificateRepository;
import com.jabiseo.certificate.dto.FindCertificateDetailResponse;
import com.jabiseo.certificate.exception.CertificateBusinessException;
import com.jabiseo.certificate.exception.CertificateErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static fixture.CertificateFixture.createCertificate;
import static fixture.ExamFixture.createExam;
import static fixture.SubjectFixture.createSubject;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@DisplayName("자격증 정보 조회 테스트")
@ExtendWith(MockitoExtension.class)
class FindCertificateDetailUseCaseTest {

    @InjectMocks
    FindCertificateDetailUseCase sut;

    @Mock
    CertificateRepository certificateRepository;

    @Test
    @DisplayName("자격증 정보 조회")
    void givenCertificateId_whenFindingCertificate_thenFindCertificate() {
        //given
        String certificateId = "1";
        String examId = "2";
        String subjectId = "3";
        Certificate certificate = createCertificate(certificateId);
        createExam(examId, certificate);
        createSubject(subjectId, certificate);
        given(certificateRepository.findById(certificateId)).willReturn(Optional.of(certificate));


        //when
        FindCertificateDetailResponse response = sut.execute(certificateId);

        //then
        assertThat(response.certificateId()).isEqualTo(certificateId);
        assertThat(response.exams().get(0).examId()).isEqualTo(examId);
        assertThat(response.subjects().get(0).subjectId()).isEqualTo(subjectId);
    }

    @Test
    @DisplayName("존재하지 않는 자격증 정보 조회")
    void givenNonExistedCertificateId_whenFindingCertificate_thenReturnException() {
        //given
        String nonExistedCertificateId = "1";
        given(certificateRepository.findById(nonExistedCertificateId)).willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> sut.execute(nonExistedCertificateId))
                .isInstanceOf(CertificateBusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", CertificateErrorCode.CERTIFICATE_NOT_FOUND);
    }

}
