package com.jabiseo.learning.dto;

import com.jabiseo.learning.domain.LearningMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LearningWithSolvingCountQueryDto {

    private LearningMode mode;
    private Long learningTime;
    private LocalDateTime createdAt;
    private Long solvingCount;

}
