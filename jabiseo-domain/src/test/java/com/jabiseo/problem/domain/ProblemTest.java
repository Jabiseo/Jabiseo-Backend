package com.jabiseo.problem.domain;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.certificate.exception.CertificateBusinessException;
import com.jabiseo.certificate.exception.CertificateErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static fixture.CertificateFixture.createCertificate;
import static fixture.ProblemFixture.createProblem;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
@DisplayName("문제 도메인 테스트")
class ProblemTest {

    @Test
    @DisplayName("문제 검사 시 파라미터로 들어온 자격증이 문제의 자격증에 해당하면 예외가 발생하지 않는다.")
    void givenCertificateAndProblem_whenValidateProblem_thenDoesNotReturnError() {
        //given
        Certificate certificate = createCertificate("1");
        Problem problem = createProblem("2", certificate);

        //when & then
        assertDoesNotThrow(() -> problem.validateProblemInCertificate(certificate));
    }

    @Test
    @DisplayName("문제 검사 시 파라미터로 들어온 자격증이 문제의 자격증에 해당하지 않으면 예외가 발생한다.")
    void givenInvalidCertificateAndProblem_whenValidateProblem_thenReturnError() {
        //given
        Certificate certificate = createCertificate("1");
        Certificate invalidCertificate = createCertificate("100");
        Problem problem = createProblem("2", certificate);

        //when & then
        assertThatThrownBy(() -> problem.validateProblemInCertificate(invalidCertificate))
                .isInstanceOf(CertificateBusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", CertificateErrorCode.PROBLEM_NOT_FOUND_IN_CERTIFICATE);
    }


}
