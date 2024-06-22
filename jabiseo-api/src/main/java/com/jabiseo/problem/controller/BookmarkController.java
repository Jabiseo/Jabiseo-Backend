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
        String bookmarkId = createBookmarkUseCase.execute(request);

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
        deleteBookmarkUseCase.execute(request);
        return ResponseEntity.noContent().build();
    }
}
