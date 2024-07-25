package com.jabiseo.problem.usecase;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.member.domain.Member;
import com.jabiseo.member.domain.MemberRepository;
import com.jabiseo.problem.domain.Bookmark;
import com.jabiseo.problem.domain.Problem;
import com.jabiseo.problem.dto.FindBookmarkedProblemsResponse;
import com.jabiseo.problem.service.BookmarkedProblemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import static org.mockito.BDDMockito.given;

@DisplayName("북마크 목록 조회 테스트")
@ExtendWith(MockitoExtension.class)
class FindBookmarkedProblemsUseCaseTest {

    @InjectMocks
    FindBookmarkedProblemsUseCase sut;

    @Mock
    MemberRepository memberRepository;

    @Mock
    BookmarkedProblemService bookmarkedProblemService;

    @Test
    @DisplayName("북마크 목록 조회 성공 케이스")
    void givenProblemConditions_whenFindingBookmarkedProblems_thenFindBookmarkedProblems() {
        //given
        String memberId = "1";
        String certificateId = "2";
        String examId = "3";
        String subjectId = "4";
        String problemId1 = "5";
        String problemId2 = "6";
        Member member = createMember(memberId);
        Certificate certificate = createCertificate(certificateId);
        member.updateCertificateState(certificate);
        createExam(examId, certificate);
        createSubject(subjectId, certificate);
        Problem problem1 = createProblem(problemId1);
        Problem problem2 = createProblem(problemId2);
        Bookmark.of(member, problem1);
        Bookmark.of(member, problem2);
        Pageable pageable = PageRequest.of(0, 10);
        given(memberRepository.getReferenceById(memberId)).willReturn(member);
        given(bookmarkedProblemService.findBookmarkedProblems(List.of(problemId1, problemId2), Optional.of(examId), List.of(subjectId), pageable))
                .willReturn(List.of(problem1, problem2));

        //when
        List<FindBookmarkedProblemsResponse> results = sut.execute(memberId, Optional.of(examId), List.of(subjectId), pageable);

        //then
        assertThat(results.get(0)).isEqualTo(FindBookmarkedProblemsResponse.from(problem1));
        assertThat(results.get(1)).isEqualTo(FindBookmarkedProblemsResponse.from(problem2));
    }

}
