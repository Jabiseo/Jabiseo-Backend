package com.jabiseo.problem.dto;

import java.util.List;

public record FindProblemsRequest(
        List<String> problemIds
) {
}
