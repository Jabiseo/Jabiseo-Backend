package com.jabiseo.problem.dto;

import java.util.List;

public record FindProblemsResponse(
        CertificateResponse certificateInfo,
        List<ProblemsResponse> problems
) {
    public static FindProblemsResponse of(CertificateResponse certificateInfo, List<ProblemsResponse> problems) {
        return new FindProblemsResponse(certificateInfo, problems);
    }
}
