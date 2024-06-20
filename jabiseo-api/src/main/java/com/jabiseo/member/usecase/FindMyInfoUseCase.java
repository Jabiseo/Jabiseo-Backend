package com.jabiseo.member.usecase;

import com.jabiseo.member.dto.FindMyInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindMyInfoUseCase {

    public FindMyInfoResponse execute() {
        return new FindMyInfoResponse("memberId", "name", "email", "phone");
    }
}
