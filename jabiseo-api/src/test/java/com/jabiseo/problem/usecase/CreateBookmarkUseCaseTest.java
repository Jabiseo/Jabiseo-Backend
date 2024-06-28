package com.jabiseo.problem.usecase;

import com.jabiseo.member.domain.Member;
import com.jabiseo.member.domain.MemberRepository;
import com.jabiseo.problem.domain.Bookmark;
import com.jabiseo.problem.domain.BookmarkRepository;
import com.jabiseo.problem.domain.Problem;
import com.jabiseo.problem.domain.ProblemRepository;
import com.jabiseo.problem.dto.CreateBookmarkRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.jabiseo.fixture.MemberFixture.createMember;
import static com.jabiseo.fixture.ProblemFixture.createProblem;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@DisplayName("북마크 생성 테스트")
@ExtendWith(MockitoExtension.class)
class CreateBookmarkUseCaseTest {

    @InjectMocks
    CreateBookmarkUseCase sut;

    @Mock
    MemberRepository memberRepository;

    @Mock
    ProblemRepository problemRepository;

    @Mock
    BookmarkRepository bookmarkRepository;

    @Test
    @DisplayName("북마크 생성 테스트 성공 케이스")        
    void givenMemberIdAndProblemId_whenCreatingBookmark_thenCreateBookmark() throws Exception {
        //given
        String memberId = "1";
        String problemId = "2";
        Member member = createMember(memberId);
        Problem problem = createProblem(problemId);
        given(memberRepository.getReferenceById(memberId)).willReturn(member);
        given(problemRepository.findById(problemId)).willReturn(Optional.of(problem));
        given(bookmarkRepository.existsByMemberIdAndProblemId(memberId, problemId)).willReturn(false);
        given(bookmarkRepository.save(any())).willReturn(Bookmark.of(member, problem));

        
        //when
        sut.execute(memberId, new CreateBookmarkRequest(problemId));
        
        //then
        ArgumentCaptor<Bookmark> bookmarkCaptor = ArgumentCaptor.forClass(Bookmark.class);
        verify(bookmarkRepository).save(bookmarkCaptor.capture());
        Bookmark savedBookmark = bookmarkCaptor.getValue();

        assertThat(savedBookmark).isNotNull();
        assertThat(savedBookmark.getMember().getId()).isEqualTo(memberId);
        assertThat(savedBookmark.getProblem().getId()).isEqualTo(problemId);
    }

}
