package com.jabiseo.problem.service;

import com.jabiseo.problem.domain.Problem;
import com.jabiseo.problem.domain.ProblemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("북마크한 문제에 대한 서비스 테스트")
class BookmarkedProblemServiceTest {

    @InjectMocks
    BookmarkedProblemService sut;

    @Mock
    ProblemRepository problemRepository;

    @Test
    @DisplayName("북마크한 목록을 조회")
    void givenProblemConditions_whenFindingBookmarkedProblems_thenFindBookmarkedProblems() {
        //given
        List<Problem> problems = List.of(
                Problem.of("1", "문제1", "선지1", "선지2", "선지3", "선지4",
                        "선지5", 3, "이론", "해답", null, null, null)
        );
        given(problemRepository.findByIdInAndExamIdAndSubjectIdIn(any(), any(), any(), any()))
                .willReturn(new PageImpl<>(problems));

        //when
        List<Problem> bookmarkedProblems = sut.findBookmarkedProblems(List.of("1"), Optional.of("2"), List.of("3"), null);

        //then
        assertThat(bookmarkedProblems).isEqualTo(problems);
    }

}
