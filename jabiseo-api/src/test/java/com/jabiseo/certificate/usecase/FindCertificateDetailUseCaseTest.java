package com.jabiseo.certificate.usecase;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.certificate.domain.CertificateRepository;
import com.jabiseo.certificate.dto.FindCertificateDetailResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.jabiseo.fixture.CertificateFixture.createCertificate;
import static com.jabiseo.fixture.ExamFixture.createExam;
import static com.jabiseo.fixture.SubjectFixture.createSubject;
import static org.assertj.core.api.Assertions.assertThat;
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

}
