package com.jabiseo.member.controller;

import com.jabiseo.config.auth.AuthMember;
import com.jabiseo.config.auth.AuthenticatedMember;
import com.jabiseo.member.dto.FindMyCurrentCertificateResponse;
import com.jabiseo.member.dto.FindMyInfoResponse;
import com.jabiseo.member.dto.UpdateMyCurrentCertificateRequest;
import com.jabiseo.member.application.usecase.FindMyCurrentCertificateUseCase;
import com.jabiseo.member.application.usecase.FindMyInfoUseCase;
import com.jabiseo.member.application.usecase.UpdateMyCurrentCertificateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members/myinfo")
public class MemberController {

    private final FindMyInfoUseCase findMyInfoUseCase;

    private final FindMyCurrentCertificateUseCase findMyCurrentCertificateUseCase;

    private final UpdateMyCurrentCertificateUseCase updateMyCurrentCertificateUseCase;

    @GetMapping
    public ResponseEntity<FindMyInfoResponse> findMyInfo(
            @AuthenticatedMember AuthMember member
    ) {
        FindMyInfoResponse result = findMyInfoUseCase.execute(member.getMemberId());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/certificates")
    public ResponseEntity<FindMyCurrentCertificateResponse> findMyCertificateStatus(
            @AuthenticatedMember AuthMember member
    ) {
        FindMyCurrentCertificateResponse result = findMyCurrentCertificateUseCase.execute(member.getMemberId());
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/certificates")
    public ResponseEntity<Void> updateMyCertificateStatus(
            @RequestBody UpdateMyCurrentCertificateRequest request,
            @AuthenticatedMember AuthMember member
    ) {
        updateMyCurrentCertificateUseCase.execute(member.getMemberId(), request);
        return ResponseEntity.ok().build();
    }

}
