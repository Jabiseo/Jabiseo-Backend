package com.jabiseo.problem.application.usecase;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.member.domain.Member;
import com.jabiseo.member.domain.MemberRepository;
import com.jabiseo.problem.domain.Problem;
import com.jabiseo.problem.domain.ProblemRepository;
import com.jabiseo.problem.dto.FindProblemsRequest;
import com.jabiseo.problem.dto.FindProblemsResponse;
import com.jabiseo.problem.exception.ProblemBusinessException;
import com.jabiseo.problem.exception.ProblemErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static fixture.CertificateFixture.createCertificate;
import static fixture.MemberFixture.createMember;
import static fixture.ProblemFixture.createProblem;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@DisplayName("문제 세트 조회 테스트")
@ExtendWith(MockitoExtension.class)
class FindProblemsByIdUseCaseTest {

    @InjectMocks
    FindProblemsByIdUseCase sut;

    @Mock
    ProblemRepository problemRepository;

    @Mock
    MemberRepository memberRepository;

    @Test
    @DisplayName("북마크를 통한 문제 세트 조회 테스트를 성공한다.")
    void givenProblemIds_whenFindingProblems_thenFindProblems() {
        //given
        Long memberId = 1L;
        Long certificateId = 2L;
        List<Long> problemIds = List.of(3L, 4L, 5L);
        Member member = createMember(memberId);
        Certificate certificate = createCertificate(certificateId);
        member.updateCurrentCertificate(certificate);
        List<Problem> problems = List.of(
                createProblem(problemIds.get(0), certificate),
                createProblem(problemIds.get(1), certificate),
                createProblem(problemIds.get(2), certificate)
        );
        FindProblemsRequest request = new FindProblemsRequest(problemIds);
        given(memberRepository.getReferenceById(memberId)).willReturn(member);
        given(problemRepository.findById(problemIds.get(0))).willReturn(Optional.of(problems.get(0)));
        given(problemRepository.findById(problemIds.get(1))).willReturn(Optional.of(problems.get(1)));
        given(problemRepository.findById(problemIds.get(2))).willReturn(Optional.of(problems.get(2)));

        //when
        FindProblemsResponse result = sut.execute(member.getId(), request);

        //then
        assertThat(result.certificateInfo().certificateId()).isEqualTo(certificateId);
        assertThat(result.problems().get(0).problemId()).isEqualTo(problemIds.get(0));
        assertThat(result.problems().get(1).problemId()).isEqualTo(problemIds.get(1));
        assertThat(result.problems().get(2).problemId()).isEqualTo(problemIds.get(2));
    }

    @Test
    @DisplayName("존재하지 않는 문제로 북마크를 통한 문제 세트 조회를 하면 예외처리한다.")
    void givenNonExistedProblemIds_whenFindingProblems_thenReturnError() {
        //given
        Long memberId = 1L;
        Long certificateId = 2L;
        List<Long> problemIds = List.of(3L, 4L, 5L);
        Member member = createMember(memberId);
        Certificate certificate = createCertificate(certificateId);
        member.updateCurrentCertificate(certificate);
        problemIds.forEach(problemId -> createProblem(problemId, certificate));
        FindProblemsRequest request = new FindProblemsRequest(problemIds);
        given(memberRepository.getReferenceById(memberId)).willReturn(member);
        given(problemRepository.findById(problemIds.get(0))).willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> sut.execute(member.getId(), request))
                .isInstanceOf(ProblemBusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ProblemErrorCode.PROBLEM_NOT_FOUND);
    }

}
