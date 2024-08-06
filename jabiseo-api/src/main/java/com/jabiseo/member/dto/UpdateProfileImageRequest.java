package com.jabiseo.member.dto;

import com.jabiseo.common.validator.ImageValid;
import org.springframework.web.multipart.MultipartFile;

public record UpdateProfileImageRequest(
        @ImageValid MultipartFile image
) {
}
