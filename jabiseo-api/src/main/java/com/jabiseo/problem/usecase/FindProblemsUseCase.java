package com.jabiseo.problem.usecase;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.certificate.domain.CertificateRepository;
import com.jabiseo.certificate.dto.ExamResponse;
import com.jabiseo.certificate.dto.SubjectResponse;
import com.jabiseo.certificate.exception.CertificateBusinessException;
import com.jabiseo.certificate.exception.CertificateErrorCode;
import com.jabiseo.problem.domain.ProblemRepository;
import com.jabiseo.problem.dto.ChoiceResponse;
import com.jabiseo.problem.dto.FindProblemsRequest;
import com.jabiseo.problem.dto.FindProblemsResponse;
import com.jabiseo.problem.exception.ProblemBusinessException;
import com.jabiseo.problem.exception.ProblemErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindProblemsUseCase {

    private static final int MIN_PROBLEM_PER_SUBJECT_COUNT = 0;
    private static final int MAX_PROBLEM_PER_SUBJECT_COUNT = 20;


    private final CertificateRepository certificateRepository;

    private final ProblemRepository problemRepository;

    public List<FindProblemsResponse> execute(String certificateId, List<String> subjectIds, String examId, int count) {
        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new CertificateBusinessException(CertificateErrorCode.CERTIFICATE_NOT_FOUND));

        // TODO: 검증을 어디서 어떻게 할 것인지 논의 필요
        validateSubjectIds(subjectIds, certificate);
        validateExamId(examId, certificate);
        validateProblemCount(count);

        if (examId != null) {
            return subjectIds.stream()
                    .map(subjectId -> problemRepository.findRandomByExamIdAndSubjectId(examId, subjectId, count))
                    .flatMap(List::stream)
                    .map(FindProblemsResponse::from)
                    .toList();
        }
        return subjectIds.stream()
                .map(subjectId -> problemRepository.findRandomBySubjectId(subjectId, count))
                .flatMap(List::stream)
                .map(FindProblemsResponse::from)
                .toList();
    }

    public List<FindProblemsResponse> execute(FindProblemsRequest request) {
        return new ArrayList<>(List.of(
                new FindProblemsResponse(
                        "1",
                        new ExamResponse("examId", "examDescription"),
                        new SubjectResponse("subjectId", 3, "subjectName"),
                        true,
                        "description",
                        List.of(new ChoiceResponse("choice")),
                        1,
                        "theory",
                        "solution"
                )
        ));
    }

    private static void validateSubjectIds(List<String> subjectIds, Certificate certificate) {
        // 자격증에 해당하는 과목들이 모두 있는지 검사
        subjectIds.forEach(subjectId -> {
            if (!certificate.containsSubject(subjectId)) {
                throw new CertificateBusinessException(CertificateErrorCode.SUBJECT_NOT_FOUND_IN_CERTIFICATE);
            }
        });
    }

    private static void validateProblemCount(int count) {
        // 문제의 개수가 올바른지 검사
        if (count < MIN_PROBLEM_PER_SUBJECT_COUNT || count > MAX_PROBLEM_PER_SUBJECT_COUNT) {
            throw new ProblemBusinessException(ProblemErrorCode.INVALID_PROBLEM_COUNT);
        }
    }

    private static void validateExamId(String examId, Certificate certificate) {
        // 자격증에 해당하는 시험이 있는지 검사
        if (examId != null && !certificate.containsExam(examId)) {
            throw new CertificateBusinessException(CertificateErrorCode.EXAM_NOT_FOUND_IN_CERTIFICATE);
        }
    }
}
