package com.jabiseo.api.problem.application.usecase;

import com.jabiseo.domain.certificate.domain.Certificate;
import com.jabiseo.domain.certificate.domain.CertificateRepository;
import com.jabiseo.domain.certificate.exception.CertificateBusinessException;
import com.jabiseo.domain.certificate.exception.CertificateErrorCode;
import com.jabiseo.domain.problem.domain.ProblemRepository;
import com.jabiseo.api.problem.dto.CertificateResponse;
import com.jabiseo.api.problem.dto.FindProblemsResponse;
import com.jabiseo.domain.problem.dto.ProblemWithBookmarkDetailQueryDto;
import com.jabiseo.api.problem.dto.ProblemsDetailResponse;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindProblemsUseCase {

    private final CertificateRepository certificateRepository;
    private final ProblemRepository problemRepository;

    //memberId가 null일 경우 비회원이므로 bookmark 유무가 모두 false로 응답된다.
    //examId가 null일 경우 전체 시험을 대상으로 조회한다.
    public FindProblemsResponse execute(@Nullable Long memberId, Long certificateId,
                                        @Nullable Long examId, List<Long> subjectIds, int count) {

        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new CertificateBusinessException(CertificateErrorCode.CERTIFICATE_NOT_FOUND));
        certificate.validateExamIdAndSubjectIds(examId, subjectIds);

        // TODO: 과목별로 문제를 가져와서 쿼리를 5번 날리는 로직에서 1번의 쿼리로 변경해야 함. 하지만 최종적으로 과목 순서가 유지되어야 함
        List<ProblemWithBookmarkDetailQueryDto> problemWithBookmarkDetailQueryDtos = subjectIds.stream()
                .distinct()
                .map(subjectId -> problemRepository.findDetailRandomByExamIdAndSubjectIdWithBookmark(memberId, examId, subjectId, count))
                .flatMap(List::stream)
                .toList();

        List<ProblemsDetailResponse> problemsDetailResponses = problemWithBookmarkDetailQueryDtos.stream()
                .map(ProblemsDetailResponse::from)
                .toList();

        CertificateResponse certificateResponse = CertificateResponse.from(certificate);

        return FindProblemsResponse.of(certificateResponse, problemsDetailResponses);
    }
}
