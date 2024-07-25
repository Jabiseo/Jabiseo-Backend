package com.jabiseo.problem.application.usecase;

import com.jabiseo.problem.dto.CreateReportRequest;
import org.springframework.stereotype.Service;

@Service
public class CreateReportUseCase {

    public String execute(CreateReportRequest request) {
        return "reportId";
    }
}
