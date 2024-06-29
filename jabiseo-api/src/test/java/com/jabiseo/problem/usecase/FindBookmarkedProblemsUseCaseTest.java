package com.jabiseo.problem.usecase;

import com.jabiseo.member.domain.Member;
import com.jabiseo.member.domain.MemberRepository;
import com.jabiseo.problem.domain.Bookmark;
import com.jabiseo.problem.domain.Problem;
import com.jabiseo.problem.dto.FindBookmarkedProblemsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.jabiseo.fixture.MemberFixture.createMember;
import static com.jabiseo.fixture.ProblemFixture.createProblem;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@DisplayName("북마크 목록 조회 테스트")
@ExtendWith(MockitoExtension.class)
class FindBookmarkedProblemsUseCaseTest {

    @InjectMocks
    FindBookmarkedProblemsUseCase sut;

    @Mock
    MemberRepository memberRepository;

    @Test
    @DisplayName("북마크 목록 조회 성공 케이스")
    void givenMemberId_whenFindingBookmarkedProblems_thenFindBookmarkedProblems() {
        //given
        String memberId = "1";
        String problemId1 = "2";
        String problemId2 = "3";
        Member member = createMember(memberId);
        Problem problem1 = createProblem(problemId1);
        Problem problem2 = createProblem(problemId2);
        Bookmark.of(member, problem1);
        Bookmark.of(member, problem2);
        given(memberRepository.getReferenceById(memberId)).willReturn(member);

        //when
        List<FindBookmarkedProblemsResponse> results = sut.execute(memberId);

        //then
        assertThat(results.get(0)).isEqualTo(FindBookmarkedProblemsResponse.from(problem1));
        assertThat(results.get(1)).isEqualTo(FindBookmarkedProblemsResponse.from(problem2));
    }

}
