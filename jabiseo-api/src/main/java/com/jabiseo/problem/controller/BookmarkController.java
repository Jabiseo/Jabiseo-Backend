package com.jabiseo.problem.controller;

import com.jabiseo.problem.dto.CreateBookmarkRequest;
import com.jabiseo.problem.dto.DeleteBookmarkRequest;
import com.jabiseo.problem.usecase.CreateBookmarkUseCase;
import com.jabiseo.problem.usecase.DeleteBookmarkUseCase;
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
            @RequestBody CreateBookmarkRequest request
    ) {
        String memberId = "1"; // TODO : 로그인 기능 구현 후 로그인한 사용자의 ID로 변경
        String bookmarkId = createBookmarkUseCase.execute(memberId, request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(bookmarkId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteBookmark(
            @RequestBody DeleteBookmarkRequest request
    ) {
        String memberId = "1"; // TODO : 로그인 기능 구현 후 로그인한 사용자의 ID로 변경
        deleteBookmarkUseCase.execute(memberId, request);
        return ResponseEntity.noContent().build();
    }
}
