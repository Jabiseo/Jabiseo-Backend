package com.jabiseo.problem.application.usecase;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.member.domain.Member;
import com.jabiseo.member.domain.MemberRepository;
import com.jabiseo.member.exception.MemberBusinessException;
import com.jabiseo.member.exception.MemberErrorCode;
import com.jabiseo.problem.domain.Problem;
import com.jabiseo.problem.domain.ProblemRepository;
import com.jabiseo.problem.dto.FindBookmarkedProblemsResponse;
import com.jabiseo.problem.dto.ProblemsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static fixture.CertificateFixture.createCertificate;
import static fixture.ExamFixture.createExam;
import static fixture.MemberFixture.createMember;
import static fixture.ProblemFixture.createProblem;
import static fixture.SubjectFixture.createSubject;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@DisplayName("북마크 목록 조회 테스트")
@ExtendWith(MockitoExtension.class)
class FindBookmarkedProblemsUseCaseTest {

    @InjectMocks
    FindBookmarkedProblemsUseCase sut;

    @Mock
    MemberRepository memberRepository;

    @Mock
    ProblemRepository problemRepository;

    @Test
    @DisplayName("examId가 있을 경우 북마크 목록 조회를 성공한다.")
    void givenProblemConditionsContainsExamId_whenFindingBookmarkedProblems_thenFindBookmarkedProblems() {
        //given
        String memberId = "1";
        Long certificateId = 2L;
        Long examId = 3L;
        Long subjectId = 4L;
        Long problemId1 = 5L;
        Long problemId2 = 6L;

        Member member = createMember(memberId);
        Certificate certificate = createCertificate(certificateId);
        member.updateCurrentCertificate(certificate);
        createExam(examId, certificate);
        createSubject(subjectId, certificate);
        Problem problem1 = createProblem(problemId1);
        Problem problem2 = createProblem(problemId2);
        Pageable pageable = PageRequest.of(0, 10);
        given(memberRepository.getReferenceById(memberId)).willReturn(member);
        given(problemRepository.findBookmarkedByExamIdAndSubjectIdIn(memberId, examId, List.of(subjectId), pageable))
                .willReturn(new PageImpl<>(List.of(problem1, problem2), pageable, 2));

        //when
        FindBookmarkedProblemsResponse results = sut.execute(memberId, Optional.of(examId), List.of(subjectId), 0);

        //then
        assertThat(results.totalCount()).isEqualTo(2);
        assertThat(results.totalPage()).isEqualTo(1);
        assertThat(results.problems().get(0)).isEqualTo(ProblemsResponse.from(problem1));
        assertThat(results.problems().get(1)).isEqualTo(ProblemsResponse.from(problem2));
    }

    @Test
    @DisplayName("examId가 없을 경우 북마크 목록 조회를 성공한다.")
    void givenProblemConditionsExceptExamId_whenFindingBookmarkedProblems_thenFindBookmarkedProblems() {
        //given
        String memberId = "1";
        Long certificateId = 2L;
        Long subjectId = 4L;
        Long problemId1 = 5L;
        Long problemId2 = 6L;

        Member member = createMember(memberId);
        Certificate certificate = createCertificate(certificateId);
        member.updateCurrentCertificate(certificate);
        createSubject(subjectId, certificate);
        Problem problem1 = createProblem(problemId1);
        Problem problem2 = createProblem(problemId2);
        Pageable pageable = PageRequest.of(0, 10);
        given(memberRepository.getReferenceById(memberId)).willReturn(member);
        given(problemRepository.findBookmarkedBySubjectIdIn(memberId, List.of(subjectId), pageable))
                .willReturn(new PageImpl<>(List.of(problem1, problem2), pageable, 2));

        //when
        FindBookmarkedProblemsResponse results = sut.execute(memberId, Optional.empty(), List.of(subjectId), 0);

        //then
        assertThat(results.totalCount()).isEqualTo(2);
        assertThat(results.totalPage()).isEqualTo(1);
        assertThat(results.problems().get(0)).isEqualTo(ProblemsResponse.from(problem1));
        assertThat(results.problems().get(1)).isEqualTo(ProblemsResponse.from(problem2));
    }

    @Test
    @DisplayName("현재 자격증이 없는 회원의 북마크 목록 조회를 시도하면 예외처리한다.")
    void givenMemberWithNoCurrentCertificate_whenFindingBookmarkedProblems_thenReturnError() {
        //given
        String memberId = "1";
        Long examId = 3L;
        Long subjectId = 4L;

        Member member = createMember(memberId);
        given(memberRepository.getReferenceById(memberId)).willReturn(member);

        //when & then
        assertThatThrownBy(() -> sut.execute(memberId, Optional.of(examId), List.of(subjectId), 0))
                .isInstanceOf(MemberBusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", MemberErrorCode.CURRENT_CERTIFICATE_NOT_EXIST);
    }
}
