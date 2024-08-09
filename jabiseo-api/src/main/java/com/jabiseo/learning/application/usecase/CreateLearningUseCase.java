package com.jabiseo.learning.application.usecase;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.certificate.domain.CertificateRepository;
import com.jabiseo.certificate.exception.CertificateBusinessException;
import com.jabiseo.certificate.exception.CertificateErrorCode;
import com.jabiseo.learning.domain.Learning;
import com.jabiseo.learning.domain.LearningRepository;
import com.jabiseo.learning.domain.ProblemSolving;
import com.jabiseo.learning.domain.ProblemSolvingRepository;
import com.jabiseo.learning.dto.CreateLearningRequest;
import com.jabiseo.learning.dto.ProblemResultRequest;
import com.jabiseo.member.domain.Member;
import com.jabiseo.member.domain.MemberRepository;
import com.jabiseo.member.exception.MemberBusinessException;
import com.jabiseo.member.exception.MemberErrorCode;
import com.jabiseo.problem.domain.Problem;
import com.jabiseo.problem.domain.ProblemRepository;
import com.jabiseo.problem.exception.ProblemBusinessException;
import com.jabiseo.problem.exception.ProblemErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateLearningUseCase {

    private final MemberRepository memberRepository;
    private final CertificateRepository certificateRepository;
    private final ProblemRepository problemRepository;
    private final LearningRepository learningRepository;
    private final ProblemSolvingRepository problemSolvingRepository;

    public Long execute(Long memberId, CreateLearningRequest request) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberBusinessException(MemberErrorCode.MEMBER_NOT_FOUND));

        Certificate certificate = certificateRepository.findById(request.certificateId())
                .orElseThrow(() -> new CertificateBusinessException(CertificateErrorCode.CERTIFICATE_NOT_FOUND));

        Learning learning = Learning.of(request.learningMode(), request.learningTime(), certificate);
        learningRepository.save(learning);

        //문제들의 id 리스트를 뽑아내 한 번의 쿼리로 찾아옴
        List<Long> solvedProblemIds = request.problems().stream()
                .map(ProblemResultRequest::problemId)
                .toList();
        List<Problem> solvedProblems = problemRepository.findAllById(solvedProblemIds);

        //요청 개수와 실제 데이터 개수가 다르면 옳지 않은 문제 ID가 요청되었다는 것
        if (solvedProblems.size() != request.problems().size()) {
            throw new ProblemBusinessException(ProblemErrorCode.PROBLEM_NOT_FOUND);
        }
        solvedProblems.forEach(problem -> problem.validateProblemInCertificate(certificate));

        //ProblemSolving 생성 및 저장
        List<ProblemSolving> problemSolvings = new ArrayList<>();
        for (int i = 0; i < solvedProblems.size(); i++) {
            boolean isCorrect = solvedProblems.get(i).checkAnswer(request.problems().get(i).choice());
            ProblemSolving problemSolving = ProblemSolving.of(member, solvedProblems.get(i), learning, request.problems().get(i).choice(), isCorrect);
            problemSolvings.add(problemSolving);
        }
        problemSolvingRepository.saveAll(problemSolvings);

        return learning.getId();
    }
}
