package com.jabiseo.member.controller;

import com.jabiseo.config.auth.AuthMember;
import com.jabiseo.config.auth.AuthenticatedMember;
import com.jabiseo.member.application.usecase.UpdateNicknameUseCase;
import com.jabiseo.member.dto.*;
import com.jabiseo.member.application.usecase.FindMyCurrentCertificateUseCase;
import com.jabiseo.member.application.usecase.FindMyInfoUseCase;
import com.jabiseo.member.application.usecase.UpdateMyCurrentCertificateUseCase;
import jakarta.validation.Valid;
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
    private final UpdateNicknameUseCase updateNicknameUseCase;

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

    @PatchMapping("/nickname")
    public ResponseEntity<UpdateNicknameResponse> updateMyNickname(@AuthenticatedMember AuthMember member, @Valid @RequestBody UpdateNicknameRequest request) {
        UpdateNicknameResponse result = updateNicknameUseCase.updateNickname(member.getMemberId(), request);
        return ResponseEntity.ok(result);
    }



}
