package com.jabiseo.problem.application.usecase;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.certificate.domain.CertificateRepository;
import com.jabiseo.certificate.exception.CertificateBusinessException;
import com.jabiseo.certificate.exception.CertificateErrorCode;
import com.jabiseo.problem.domain.Problem;
import com.jabiseo.problem.domain.ProblemRepository;
import com.jabiseo.problem.dto.CertificateResponse;
import com.jabiseo.problem.dto.FindProblemsResponse;
import com.jabiseo.problem.dto.ProblemsDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindProblemsUseCase {

    private final CertificateRepository certificateRepository;
    private final ProblemRepository problemRepository;

    // TODO: 문제에 북마크 되어 있는지 표시해야 함
    public FindProblemsResponse execute(Long certificateId, List<Long> subjectIds, Optional<Long> examId, int count) {
        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new CertificateBusinessException(CertificateErrorCode.CERTIFICATE_NOT_FOUND));

        certificate.validateExamIdAndSubjectIds(examId, subjectIds);

        // TODO: 과목별로 문제를 가져와서 쿼리를 5번 날리는 로직에서 1번의 쿼리로 변경해야 함. 하지만 최종적으로 과목 순서가 유지되어야 함
        List<Problem> problems = subjectIds.stream()
                .map(subjectId -> {
                    if (examId.isPresent()) {
                        return problemRepository.findRandomByExamIdAndSubjectId(examId.get(), subjectId, count);
                    }
                    return problemRepository.findRandomBySubjectId(subjectId, count);
                })
                .flatMap(List::stream)
                .toList();

        CertificateResponse certificateResponse = CertificateResponse.from(certificate);
        List<ProblemsDetailResponse> problemsDetailResponses = problems.stream()
                .map(ProblemsDetailResponse::from)
                .toList();

        return FindProblemsResponse.of(certificateResponse, problemsDetailResponses);
    }
}
