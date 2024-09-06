package com.jabiseo.plan.application.usecase;

import com.jabiseo.plan.dto.calender.PlanCalenderSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanCalenderSearchUseCase {


    public PlanCalenderSearchResponse execute(Long planId, int year, int month) {


        return null;
    }


}
