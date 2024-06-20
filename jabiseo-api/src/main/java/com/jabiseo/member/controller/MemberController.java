package com.jabiseo.member.controller;

import com.jabiseo.member.dto.FindMyCertificateStatusResponse;
import com.jabiseo.member.dto.FindMyInfoResponse;
import com.jabiseo.member.dto.UpdateMyCertificateStatusRequest;
import com.jabiseo.member.usecase.FindMyCertificateStatusUseCase;
import com.jabiseo.member.usecase.FindMyInfoUseCase;
import com.jabiseo.member.usecase.UpdateMyCertificateStatusUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members/myinfo")
public class MemberController {

    private final FindMyInfoUseCase findMyInfoUseCase;

    private final FindMyCertificateStatusUseCase findMyCertificateStatusUseCase;

    private final UpdateMyCertificateStatusUseCase updateMyCertificateStatusUseCase;

    @GetMapping
    public ResponseEntity<FindMyInfoResponse> findMyInfo() {
        FindMyInfoResponse result = findMyInfoUseCase.execute();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/certificates")
    public ResponseEntity<FindMyCertificateStatusResponse> findMyCertificateStatus() {
        FindMyCertificateStatusResponse result = findMyCertificateStatusUseCase.execute();
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/certificates")
    public ResponseEntity<Void> updateMyCertificateStatus(
            @RequestParam UpdateMyCertificateStatusRequest request
    ) {
        updateMyCertificateStatusUseCase.execute(request);

        return ResponseEntity.ok().build();
    }

}
