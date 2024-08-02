package com.jabiseo.member.application.usecase;

import com.jabiseo.member.domain.Member;
import com.jabiseo.member.domain.MemberRepository;
import com.jabiseo.member.dto.UpdateProfileImageRequest;
import com.jabiseo.s3.S3Uploader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import static fixture.MemberFixture.createMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("profileImage 변경 usecase 테스트")
class UpdateProfileImageUseCaseTest {
    @InjectMocks
    UpdateProfileImageUseCase useCase;

    @Mock
    MemberRepository memberRepository;

    @Mock
    S3Uploader s3Uploader;

    UpdateProfileImageRequest request;

    @BeforeEach
    void setUp() {
        request = new UpdateProfileImageRequest(new MockMultipartFile("image", "image.jpg", "image/jpeg", new byte[1024]));
    }

    @Test
    @DisplayName("프로필 이미지 변경 시 s3업로더에 업로드 후 회원 정보를 업데이트 한다.")
    void whenUpdateProfileImageThenUpdateImageInfo(){
        //given
        Member member = createMember(1L);
        String newImageUrl = "new-image-url";
        given(memberRepository.getReferenceById(member.getId())).willReturn(member);
        given(s3Uploader.upload(request.image(), "profile/")).willReturn(newImageUrl);

        //when
        useCase.execute(member.getId(), request);

        //then
        assertThat(member.getProfileImage()).isEqualTo(newImageUrl);
    }

}