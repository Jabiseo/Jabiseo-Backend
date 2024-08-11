package com.jabiseo.plan.controller;

import com.jabiseo.config.auth.AuthMember;
import com.jabiseo.config.auth.AuthenticatedMember;
import com.jabiseo.plan.application.usecase.CreatePlanUseCase;
import com.jabiseo.plan.dto.CreatePlanRequest;
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
@RequestMapping("/api/plans")
public class PlanController {

    private final CreatePlanUseCase createPlanUseCase;

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CreatePlanRequest request, @AuthenticatedMember AuthMember member) {
        Long planId = createPlanUseCase.execute(member.getMemberId(), request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(planId)
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
