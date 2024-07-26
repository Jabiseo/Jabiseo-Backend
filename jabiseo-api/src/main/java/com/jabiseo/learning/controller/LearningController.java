package com.jabiseo.learning.controller;

import com.jabiseo.learning.dto.CreateLearningRequest;
import com.jabiseo.learning.application.usecase.CreateLearningUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/learning")
public class LearningController {

    private final CreateLearningUseCase createLearningUseCase;

    @PostMapping
    public ResponseEntity<Void> createLearning(
            @RequestBody CreateLearningRequest request
    ) {
        createLearningUseCase.execute(request);
        return ResponseEntity.noContent().build();
    }
}
