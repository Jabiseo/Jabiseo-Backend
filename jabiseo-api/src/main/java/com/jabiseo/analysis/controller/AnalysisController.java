package com.jabiseo.analysis.controller;

import com.jabiseo.analysis.application.usecase.FindTodayLearningUseCase;
import com.jabiseo.analysis.application.usecase.FindVulnerableSubjectsUseCase;
import com.jabiseo.analysis.application.usecase.FindVulnerableTagsUseCase;
import com.jabiseo.analysis.dto.FindTodayLearningResponse;
import com.jabiseo.analysis.dto.FindVulnerableSubjectResponse;
import com.jabiseo.analysis.dto.FindVulnerableTagResponse;
import com.jabiseo.config.auth.AuthMember;
import com.jabiseo.config.auth.AuthenticatedMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/analyses")
public class AnalysisController {

    private final FindTodayLearningUseCase findTodayLearningUseCase;
    private final FindVulnerableSubjectsUseCase findVulnerableSubjectsUseCase;
    private final FindVulnerableTagsUseCase findVulnerableTagsUseCase;

    @GetMapping("/today")
    public ResponseEntity<FindTodayLearningResponse> findTodayLearning(
            @AuthenticatedMember AuthMember member
    ) {
        FindTodayLearningResponse result = findTodayLearningUseCase.execute(member.getMemberId());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/vulnerable-subjects")
    public ResponseEntity<List<FindVulnerableSubjectResponse>> findVulnerableSubjects(
            @AuthenticatedMember AuthMember member
    ) {
        List<FindVulnerableSubjectResponse> result = findVulnerableSubjectsUseCase.execute(member.getMemberId());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/vulnerable-tags")
    public ResponseEntity<List<FindVulnerableTagResponse>> findVulnerableTags(
            @AuthenticatedMember AuthMember member
    ) {
        List<FindVulnerableTagResponse> result = findVulnerableTagsUseCase.execute(member.getMemberId());
        return ResponseEntity.ok(result);
    }

}
