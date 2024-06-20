package com.jabiseo.certificate.usecase;

import com.jabiseo.certificate.domain.CertificateRepository;
import com.jabiseo.certificate.dto.ExamDto;
import com.jabiseo.certificate.dto.FindCertificateResponse;
import com.jabiseo.certificate.dto.SubjectDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FindCertificateUseCase {

    public FindCertificateResponse execute(String id) {
        return new FindCertificateResponse(
                "certificateId",
                "name",
                new ArrayList<>(List.of(
                        new ExamDto("examId", "description")
                )),
                new ArrayList<>(List.of(
                        new SubjectDto("subjectId", 1, "subjectName")
                ))
        );
    }
}
