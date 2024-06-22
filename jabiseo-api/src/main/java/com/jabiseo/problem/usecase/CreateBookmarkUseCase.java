package com.jabiseo.problem.usecase;

import com.jabiseo.problem.dto.CreateBookmarkRequest;
import org.springframework.stereotype.Service;

@Service
public class CreateBookmarkUseCase {

    public String execute(CreateBookmarkRequest request) {
        return "bookmarkId";
    }
}
