package com.jabiseo.problem.controller;

import com.jabiseo.config.auth.AuthMember;
import com.jabiseo.config.auth.AuthenticatedMember;
import com.jabiseo.problem.application.usecase.FindProblemsByIdUseCase;
import com.jabiseo.problem.dto.*;
import com.jabiseo.problem.application.usecase.CreateReportUseCase;
import com.jabiseo.problem.application.usecase.FindBookmarkedProblemsUseCase;
import com.jabiseo.problem.application.usecase.FindProblemsUseCase;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/problems")
public class ProblemController {

    private final FindProblemsUseCase findProblemsUseCase;

    private final FindProblemsByIdUseCase findProblemsByIdUseCase;

    private final CreateReportUseCase createReportUseCase;

    private final FindBookmarkedProblemsUseCase findBookmarkedProblemsUseCase;

    @GetMapping("/set")
    public ResponseEntity<FindProblemsResponse> findProblems(
            @AuthenticatedMember AuthMember member,
            // TODO: Valid에 대한 테스트
            @RequestParam(name = "certificate-id") String certificateId,
            @RequestParam(name = "subject-id") List<String> subjectIds,
            @RequestParam(name = "exam-id", required = false) Optional<String> examId,
            @RequestParam
            @Min(value = 1, message = "과목 당 문제 수는 0보다 커야 합니다.")
            @Max(value = 20, message = "과목 당 문제 수는 20보다 작거나 같아야 합니다.")
            int count
    ) {
        FindProblemsResponse result =
                findProblemsUseCase.execute(certificateId, subjectIds, examId, count);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/set/query")
    public ResponseEntity<FindProblemsResponse> findProblems(
            @AuthenticatedMember AuthMember member,
            @RequestBody FindProblemsRequest request
    ) {
        FindProblemsResponse result = findProblemsByIdUseCase.execute(member.getMemberId(), request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/reports")
    public ResponseEntity<Void> reportProblem(
            @RequestBody CreateReportRequest request
    ) {
        String reportId = createReportUseCase.execute(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(reportId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/bookmarked")
    public ResponseEntity<FindBookmarkedProblemsResponse> findBookmarkedProblems(
            @AuthenticatedMember AuthMember member,
            // TODO: DTO 기반으로 변경
            @RequestParam(name = "exam-id") Optional<String> examId,
            @RequestParam(name = "subject-id") List<String> subjectIds,
            int page
    ) {
        FindBookmarkedProblemsResponse result = findBookmarkedProblemsUseCase.execute(member.getMemberId(), examId, subjectIds, page);
        return ResponseEntity.ok(result);
    }
}
