package com.jabiseo.plan.controller;

import com.jabiseo.config.auth.AuthMember;
import com.jabiseo.config.auth.AuthenticatedMember;
import com.jabiseo.plan.application.usecase.CreatePlanUseCase;
import com.jabiseo.plan.application.usecase.FindActivePlanUseCase;
import com.jabiseo.plan.application.usecase.SearchPlanCalenderUseCase;
import com.jabiseo.plan.dto.ActivePlanResponse;
import com.jabiseo.plan.dto.CreatePlanRequest;
import com.jabiseo.plan.dto.calender.PlanCalenderSearchResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plans")
public class PlanController {

    private final CreatePlanUseCase createPlanUseCase;
    private final FindActivePlanUseCase findActivePlanUseCase;
    private final SearchPlanCalenderUseCase searchPlanCalenderUseCase;

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

    @GetMapping("/active")
    public ResponseEntity<ActivePlanResponse> getActive(@AuthenticatedMember AuthMember member) {
        ActivePlanResponse result = findActivePlanUseCase.execute(member.getMemberId());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}/calender")
    public ResponseEntity<PlanCalenderSearchResponse> getPlanCalender(@AuthenticatedMember AuthMember member,
                                                                      @PathVariable("id") Long planId,
                                                                      @RequestParam(name = "year") int year,
                                                                      @RequestParam(name = "month") int month) {
        PlanCalenderSearchResponse result = searchPlanCalenderUseCase.execute(member.getMemberId(), planId, year, month);
        return ResponseEntity.ok(result);
    }
}
