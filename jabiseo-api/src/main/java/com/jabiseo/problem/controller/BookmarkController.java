package com.jabiseo.problem.controller;

import com.jabiseo.config.auth.AuthMember;
import com.jabiseo.config.auth.AuthenticatedMember;
import com.jabiseo.problem.application.usecase.CreateBookmarkUseCase;
import com.jabiseo.problem.application.usecase.DeleteBookmarkUseCase;
import com.jabiseo.problem.dto.CreateBookmarkRequest;
import com.jabiseo.problem.dto.DeleteBookmarkRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    private final CreateBookmarkUseCase createBookmarkUseCase;

    private final DeleteBookmarkUseCase deleteBookmarkUseCase;

    @PostMapping
    public ResponseEntity<Void> createBookmark(
            @RequestBody CreateBookmarkRequest request,
            @AuthenticatedMember AuthMember member
    ) {
        String bookmarkId = createBookmarkUseCase.execute(member.getMemberId(), request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(bookmarkId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteBookmark(
            @RequestBody DeleteBookmarkRequest request,
            @AuthenticatedMember AuthMember member
    ) {
        deleteBookmarkUseCase.execute(member.getMemberId(), request);
        return ResponseEntity.noContent().build();
    }
}
