package com.jabiseo.problem.application.usecase;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.certificate.domain.CertificateRepository;
import com.jabiseo.certificate.exception.CertificateBusinessException;
import com.jabiseo.certificate.exception.CertificateErrorCode;
import com.jabiseo.problem.domain.ProblemRepository;
import com.jabiseo.problem.dto.CertificateResponse;
import com.jabiseo.problem.dto.FindProblemsResponse;
import com.jabiseo.problem.dto.ProblemWithBookmarkDto;
import com.jabiseo.problem.dto.ProblemsDetailResponse;
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

    public FindProblemsResponse execute(Long memberId, Long certificateId, List<Long> subjectIds, Long examId, int count) {

        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new CertificateBusinessException(CertificateErrorCode.CERTIFICATE_NOT_FOUND));
        certificate.validateExamIdAndSubjectIds(examId, subjectIds);

        // TODO: 과목별로 문제를 가져와서 쿼리를 5번 날리는 로직에서 1번의 쿼리로 변경해야 함. 하지만 최종적으로 과목 순서가 유지되어야 함
        List<ProblemWithBookmarkDto> problemWithBookmarkDtos = subjectIds.stream()
                .map(subjectId -> problemRepository.findRandomByExamIdAndSubjectIdWithBookmark(memberId, examId, subjectId, count))
                .flatMap(List::stream)
                .toList();

        List<ProblemsDetailResponse> problemsDetailResponses = problemWithBookmarkDtos.stream()
                .map(ProblemsDetailResponse::fromNonLogin)
                .toList();

        CertificateResponse certificateResponse = CertificateResponse.from(certificate);

        return FindProblemsResponse.of(certificateResponse, problemsDetailResponses);
    }
}
