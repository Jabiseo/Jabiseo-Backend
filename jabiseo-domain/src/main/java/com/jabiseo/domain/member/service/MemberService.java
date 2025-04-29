package com.jabiseo.domain.member.service;

import com.jabiseo.domain.certificate.domain.Certificate;
import com.jabiseo.domain.member.domain.DeviceToken;
import com.jabiseo.domain.member.domain.Member;
import com.jabiseo.domain.member.dto.MemberDeviceDto;
import com.jabiseo.domain.member.repository.DeviceTokenRepository;
import com.jabiseo.domain.member.repository.MemberRepository;
import com.jabiseo.domain.member.domain.OauthMemberInfo;
import com.jabiseo.domain.member.exception.MemberBusinessException;
import com.jabiseo.domain.member.exception.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberFactory memberFactory;
    private final DeviceTokenRepository deviceTokenRepository;

    public Member getById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberBusinessException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    public Member getByIdWithCertificate(Long memberId) {
        return memberRepository.findByIdWithCertificate(memberId)
                .orElseThrow(() -> new MemberBusinessException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    public List<MemberDeviceDto> findAllWithDeviceTokensByPaging(int page, int pageSize) {
        Page<Member> paging = memberRepository.findAll(PageRequest.of(page, pageSize));
        List<Member> members = paging.getContent();

        List<Long> ids = members.stream().map(Member::getId).toList();
        if(ids.isEmpty()) {
            return members.stream().map((it)-> new MemberDeviceDto(it, Collections.emptyList())).toList();
        }


        List<MemberDeviceDto> result = new ArrayList<>();
        List<DeviceToken> deviceTokens = deviceTokenRepository.findByMemberIdIn(ids);
        Map<Long, List<DeviceToken>> collect = deviceTokens.stream().collect(Collectors.groupingBy(token -> token.getMember().getId()));

        members.forEach(member -> {
            result.add(new MemberDeviceDto(member, collect.getOrDefault(member.getId(), Collections.emptyList())));
        });

        return result;
    }

    public List<MemberDeviceDto> findAllWithDeviceTokensByLastIndex(long lastIndex, long size) {
        // lastIndex가 0 이상인 경우만
        List<Member> members = memberRepository.findAllByLastIndex(lastIndex, size);

        if (members.isEmpty()) {
            return Collections.emptyList();
        }

        // 해당 멤버들의 ID 목록을 추출
        List<Long> ids = members.stream().map(Member::getId).toList();

        List<MemberDeviceDto> result = new ArrayList<>();
        if (!ids.isEmpty()) {
            // DeviceToken을 멤버 ID로 그룹화
            List<DeviceToken> deviceTokens = deviceTokenRepository.findByMemberIdIn(ids);
            Map<Long, List<DeviceToken>> collect = deviceTokens.stream()
                    .collect(Collectors.groupingBy(token -> token.getMember().getId()));

            // 멤버에 맞게 DeviceToken을 조합하여 결과에 추가
            members.forEach(member -> {
                result.add(new MemberDeviceDto(member, collect.getOrDefault(member.getId(), Collections.emptyList())));
            });
        } else {
            // 멤버가 없으면 빈 리스트로 반환
            result.addAll(members.stream()
                    .map(it -> new MemberDeviceDto(it, Collections.emptyList()))
                    .toList());
        }

        // 마지막 멤버의 ID를 반환하여 이후 조회에 사용할 수 있게 함
        return result;
    }

    @Transactional
    public Member getByOauthIdAndOauthServerOrCreateMember(OauthMemberInfo oauthMemberInfo) {
        return memberRepository.findByOauthIdAndOauthServer(oauthMemberInfo.getOauthId(), oauthMemberInfo.getOauthServer())
                .orElseGet(() -> {
                    Member newMember = memberFactory.createNew(oauthMemberInfo);
                    return memberRepository.save(newMember);
                });
    }

    @Transactional
    public void updateCurrentCertificate(Long memberId, Certificate certificate) {
        Member member = getById(memberId);
        member.updateCurrentCertificate(certificate);
    }

    @Transactional
    public Member updateNickname(Long memberId, String nickname) {
        Member member = getById(memberId);
        member.updateNickname(nickname);
        return member;
    }

    @Transactional
    public Member updateProfileImage(Member member, String profileUrl) {
        member.updateProfileImage(profileUrl);
        return memberRepository.save(member);
    }
}
