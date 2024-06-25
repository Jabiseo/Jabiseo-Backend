package com.jabiseo.problem.controller;

import com.jabiseo.problem.dto.CreateReportRequest;
import com.jabiseo.problem.dto.FindBookmarkedProblemsResponse;
import com.jabiseo.problem.dto.FindProblemsRequest;
import com.jabiseo.problem.dto.FindProblemsResponse;
import com.jabiseo.problem.usecase.CreateReportUseCase;
import com.jabiseo.problem.usecase.FindBookmarkedProblemsUseCase;
import com.jabiseo.problem.usecase.FindProblemsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/problems")
public class ProblemController {

    private final FindProblemsUseCase findProblemsUseCase;

    private final CreateReportUseCase createReportUseCase;

    private final FindBookmarkedProblemsUseCase findBookmarkedProblemsUseCase;

    @GetMapping("/set")
    public ResponseEntity<List<FindProblemsResponse>> findProblems(
            @RequestParam(name = "certificate-id") String certificateId,
            @RequestParam(name = "subject-id") List<String> subjectIds,
            @RequestParam(name = "exam-id", required = false) String examId,
            @RequestParam(required = false) int count
    ) {
        List<FindProblemsResponse> result =
                findProblemsUseCase.execute(certificateId, subjectIds, examId, count);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/set/query")
    public ResponseEntity<List<FindProblemsResponse>> findProblems(
            @RequestBody FindProblemsRequest request
    ) {
        List<FindProblemsResponse> result = findProblemsUseCase.execute(request);
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
    public ResponseEntity<List<FindBookmarkedProblemsResponse>> findBookmarkedProblems() {
        List<FindBookmarkedProblemsResponse> result = findBookmarkedProblemsUseCase.execute();
        return ResponseEntity.ok(result);
    }
}
