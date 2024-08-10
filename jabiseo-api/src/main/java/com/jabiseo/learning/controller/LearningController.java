package com.jabiseo.learning.controller;

import com.jabiseo.config.auth.AuthMember;
import com.jabiseo.config.auth.AuthenticatedMember;
import com.jabiseo.learning.dto.CreateLearningRequest;
import com.jabiseo.learning.application.usecase.CreateLearningUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/learning")
public class LearningController {

    private final CreateLearningUseCase createLearningUseCase;

    @PostMapping
    public ResponseEntity<Void> createLearning(
            @AuthenticatedMember AuthMember member,
            @RequestBody @Valid CreateLearningRequest request
    ) {
        Long learningId = createLearningUseCase.execute(member.getMemberId(), request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(learningId)
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
