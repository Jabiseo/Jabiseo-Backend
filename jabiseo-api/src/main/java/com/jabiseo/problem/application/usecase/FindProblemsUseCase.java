package com.jabiseo.problem.application.usecase;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.certificate.domain.CertificateRepository;
import com.jabiseo.certificate.exception.CertificateBusinessException;
import com.jabiseo.certificate.exception.CertificateErrorCode;
import com.jabiseo.problem.domain.ProblemRepository;
import com.jabiseo.problem.dto.CertificateResponse;
import com.jabiseo.problem.dto.FindProblemsResponse;
import com.jabiseo.problem.dto.ProblemWithBookmarkDetailQueryDto;
import com.jabiseo.problem.dto.ProblemsDetailResponse;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindProblemsUseCase {

    private static final int MAX_PROBLEM_COUNT = 20;

    private final CertificateRepository certificateRepository;
    private final ProblemRepository problemRepository;

    //memberId가 null일 경우 비회원이므로 bookmark 유무가 모두 false로 응답된다.
    //examId가 null일 경우 전체 시험을 대상으로 조회한다.
    public FindProblemsResponse execute(@Nullable Long memberId, Long certificateId,
                                        @Nullable Long examId, List<Long> subjectIds, int count) {

        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new CertificateBusinessException(CertificateErrorCode.CERTIFICATE_NOT_FOUND));
        certificate.validateExamIdAndSubjectIds(examId, subjectIds);

        List<ProblemWithBookmarkDetailQueryDto> problemWithBookmarkDetailQueryDtos = subjectIds.stream()
                .distinct()
                .flatMap(subjectId -> {
                    List<ProblemWithBookmarkDetailQueryDto> problems = problemRepository.findDetailByExamIdAndSubjectIdWithBookmark(memberId, examId, subjectId, MAX_PROBLEM_COUNT);
                    Collections.shuffle(problems); // 문제 리스트를 랜덤으로 섞음
                    return problems.stream().limit(count);
                })
                .toList();

        List<ProblemsDetailResponse> problemsDetailResponses = problemWithBookmarkDetailQueryDtos.stream()
                .map(ProblemsDetailResponse::from)
                .toList();

        CertificateResponse certificateResponse = CertificateResponse.from(certificate);

        return FindProblemsResponse.of(certificateResponse, problemsDetailResponses);
    }
}
