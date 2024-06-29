package com.jabiseo.problem.usecase;

import com.jabiseo.member.domain.Member;
import com.jabiseo.member.domain.MemberRepository;
import com.jabiseo.problem.domain.Bookmark;
import com.jabiseo.problem.dto.FindBookmarkedProblemsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindBookmarkedProblemsUseCase {

    private final MemberRepository memberRepository;

    public List<FindBookmarkedProblemsResponse> execute(String memberId) {
        Member member = memberRepository.getReferenceById(memberId);
        List<Bookmark> bookmarks = member.getBookmarks();
        return bookmarks.stream()
                .map(Bookmark::getProblem)
                .map(FindBookmarkedProblemsResponse::from)
                .toList();
    }
}
