package com.jabiseo.domain.member.dto;

import com.jabiseo.domain.member.domain.DeviceToken;
import com.jabiseo.domain.member.domain.Member;
import lombok.Getter;

import java.util.List;

@Getter
public class MemberDeviceDto {

    private Member member;
    private List<DeviceToken> deviceTokenList;

    public MemberDeviceDto(Member member, List<DeviceToken> deviceTokenList) {
        this.member = member;
        this.deviceTokenList = deviceTokenList;
    }
}
