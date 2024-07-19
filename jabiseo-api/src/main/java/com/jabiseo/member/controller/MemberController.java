package com.jabiseo.member.controller;

import com.jabiseo.config.auth.AuthMember;
import com.jabiseo.config.auth.AuthenticatedMember;
import com.jabiseo.member.dto.FindMyCertificateStateResponse;
import com.jabiseo.member.dto.FindMyInfoResponse;
import com.jabiseo.member.dto.UpdateMyCertificateStateRequest;
import com.jabiseo.member.usecase.FindMyCertificateStateUseCase;
import com.jabiseo.member.usecase.FindMyInfoUseCase;
import com.jabiseo.member.usecase.UpdateMyCertificateStateUseCase;
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
    public ResponseEntity<FindMyInfoResponse> findMyInfo(@AuthenticatedMember AuthMember member) {
        FindMyInfoResponse result = findMyInfoUseCase.execute(member.getMemberId());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/certificates")
    public ResponseEntity<FindMyCertificateStateResponse> findMyCertificateStatus() {
        String memberId = "1"; // TODO: 로그인 기능 구현 후 JWT에서 memberId 가져오기
        FindMyCertificateStateResponse result = findMyCertificateStateUseCase.execute(memberId);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/certificates")
    public ResponseEntity<Void> updateMyCertificateStatus(
            @RequestBody UpdateMyCertificateStateRequest request
    ) {
        String memberId = "1"; // TODO: 로그인 기능 구현 후 JWT에서 memberId 가져오기
        updateMyCertificateStateUseCase.execute(memberId, request);
        return ResponseEntity.ok().build();
    }

}
