package com.jabiseo.member.controller;

import com.jabiseo.config.auth.AuthMember;
import com.jabiseo.config.auth.AuthenticatedMember;
import com.jabiseo.member.dto.FindMyCertificateStateResponse;
import com.jabiseo.member.dto.FindMyInfoResponse;
import com.jabiseo.member.dto.UpdateMyCertificateStateRequest;
import com.jabiseo.member.application.usecase.FindMyCertificateStateUseCase;
import com.jabiseo.member.application.usecase.FindMyInfoUseCase;
import com.jabiseo.member.application.usecase.UpdateMyCertificateStateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members/myinfo")
public class MemberController {

    private final FindMyInfoUseCase findMyInfoUseCase;

    private final FindMyCertificateStateUseCase findMyCertificateStateUseCase;

    private final UpdateMyCertificateStateUseCase updateMyCertificateStateUseCase;

    @GetMapping
    public ResponseEntity<FindMyInfoResponse> findMyInfo(
            @AuthenticatedMember AuthMember member
    ) {
        FindMyInfoResponse result = findMyInfoUseCase.execute(member.getMemberId());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/certificates")
    public ResponseEntity<FindMyCertificateStateResponse> findMyCertificateStatus(
            @AuthenticatedMember AuthMember member
    ) {
        FindMyCertificateStateResponse result = findMyCertificateStateUseCase.execute(member.getMemberId());
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/certificates")
    public ResponseEntity<Void> updateMyCertificateStatus(
            @RequestBody UpdateMyCertificateStateRequest request,
            @AuthenticatedMember AuthMember member
    ) {
        updateMyCertificateStateUseCase.execute(member.getMemberId(), request);
        return ResponseEntity.ok().build();
    }

}
