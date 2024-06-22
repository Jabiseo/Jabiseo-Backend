package com.jabiseo.problem.dto;

import com.jabiseo.problem.ReportType;

public record CreateReportRequest(
        String problemId,
        ReportType reportType,
        String content
) {
}
