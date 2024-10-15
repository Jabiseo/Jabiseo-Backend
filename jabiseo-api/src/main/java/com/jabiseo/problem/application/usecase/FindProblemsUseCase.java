package com.jabiseo.problem.application.usecase;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.certificate.domain.Exam;
import com.jabiseo.certificate.service.CertificateService;
import com.jabiseo.problem.dto.CertificateResponse;
import com.jabiseo.problem.dto.FindProblemsResponse;
import com.jabiseo.problem.dto.ProblemWithBookmarkDetailQueryDto;
import com.jabiseo.problem.dto.ProblemsDetailResponse;
import com.jabiseo.problem.service.ProblemService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindProblemsUseCase {

    private final CertificateService certificateService;
    private final ProblemService problemService;

    //memberId가 null일 경우 비회원이므로 bookmark 유무가 모두 false로 응답된다.
    //examId가 null일 경우 전체 시험을 대상으로 조회한다.
    public FindProblemsResponse execute(@Nullable Long memberId, Long certificateId,
                                        @Nullable Long examId, List<Long> subjectIds, int count) {

        Certificate certificate = certificateService.getById(certificateId);
        certificateService.validateExamIdAndSubjectIds(certificate, examId, subjectIds);

        List<ProblemWithBookmarkDetailQueryDto> problemWithBookmarkDetailQueryDtos = getProblemWithBookmarkDetailQueryDtos(memberId, examId, subjectIds, count, certificate);

        List<ProblemsDetailResponse> problemsDetailResponses = problemWithBookmarkDetailQueryDtos.stream()
                .map(ProblemsDetailResponse::from)
                .toList();

        CertificateResponse certificateResponse = CertificateResponse.from(certificate);

        return FindProblemsResponse.of(certificateResponse, problemsDetailResponses);
    }

    private List<ProblemWithBookmarkDetailQueryDto> getProblemWithBookmarkDetailQueryDtos(Long memberId, Long examId, List<Long> subjectIds, int count, Certificate certificate) {
        if (examId == null) {
            List<Long> examIds = certificate.getExams().stream().map(Exam::getId).toList();
            return problemService.findProblemsBySubjectId(memberId, examIds, subjectIds, count);
        } else {
            return problemService.findProblemsByExamIdAndSubjectIds(memberId, examId, subjectIds, count);
        }
    }
}
