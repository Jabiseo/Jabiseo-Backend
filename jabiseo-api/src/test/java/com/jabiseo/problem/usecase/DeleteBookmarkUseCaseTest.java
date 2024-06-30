package com.jabiseo.problem.usecase;

import com.jabiseo.member.domain.Member;
import com.jabiseo.problem.domain.Bookmark;
import com.jabiseo.problem.domain.BookmarkRepository;
import com.jabiseo.problem.domain.Problem;
import com.jabiseo.problem.dto.DeleteBookmarkRequest;
import com.jabiseo.problem.exception.ProblemBusinessException;
import com.jabiseo.problem.exception.ProblemErrorCode;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@DisplayName("북마크 삭제 테스트")
@ExtendWith(MockitoExtension.class)
class DeleteBookmarkUseCaseTest {

    @InjectMocks
    DeleteBookmarkUseCase sut;

    @Mock
    BookmarkRepository bookmarkRepository;

    @Test
    @DisplayName("북마크 삭제 테스트 성공 케이스")
    void givenMemberIdAndProblemId_whenDeletingBookmark_thenDeleteBookmark() {
        //given
        String memberId = "1";
        String problemId = "2";
        Member member = createMember(memberId);
        Problem problem = createProblem(problemId);
        Bookmark bookmark = Bookmark.of(member, problem);
        given(bookmarkRepository.findByMemberIdAndProblemId(memberId, problemId)).willReturn(Optional.of(bookmark));

        //when
        sut.execute(memberId, new DeleteBookmarkRequest(problemId));

        //then
        ArgumentCaptor<Bookmark> bookmarkCaptor = ArgumentCaptor.forClass(Bookmark.class);
        verify(bookmarkRepository).delete(bookmarkCaptor.capture());
        Bookmark deletedBookmark = bookmarkCaptor.getValue();
        assertThat(deletedBookmark).isEqualTo(bookmark);
    }

    @Test
    @DisplayName("존재하지 않는 북마크를 삭제하는 경우 테스트")
    void givenNonExistBookmarkWithMemberIdAndProblemId_whenDeletingBookmark_thenReturnError() {
        //given
        String memberId = "1";
        String problemId = "2";
        given(bookmarkRepository.findByMemberIdAndProblemId(memberId, problemId)).willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> sut.execute(memberId, new DeleteBookmarkRequest(problemId)))
                .isInstanceOf(ProblemBusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ProblemErrorCode.BOOKMARK_NOT_FOUND);
    }

}
