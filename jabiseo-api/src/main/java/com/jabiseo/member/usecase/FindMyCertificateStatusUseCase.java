package com.jabiseo.member.usecase;

import com.jabiseo.member.dto.FindMyCertificateStatusResponse;
import com.jabiseo.member.dto.FindMyInfoResponse;
import org.springframework.stereotype.Service;

@Service
public class FindMyCertificateStatusUseCase {

    public FindMyCertificateStatusResponse execute() {
        return new FindMyCertificateStatusResponse("memberId", "certificateId");
    }
}
