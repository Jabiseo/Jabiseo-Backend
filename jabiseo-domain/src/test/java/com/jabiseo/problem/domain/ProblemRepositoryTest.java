package com.jabiseo.problem.domain;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.certificate.domain.Exam;
import com.jabiseo.certificate.domain.Subject;
import com.jabiseo.member.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static fixture.CertificateFixture.createCertificate;
import static fixture.ExamFixture.createExam;
import static fixture.MemberFixture.createMember;
import static fixture.ProblemFixture.createProblem;
import static fixture.SubjectFixture.createSubject;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@DisplayName("Problem Repository 테스트")
class ProblemRepositoryTest {

    @Autowired
    private ProblemRepository problemRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @DisplayName("북마크 목록 조회 JPA 테스트")
    void givenProblemConditions_whenFindingBookmarkedProblems_thenFindBookmarkedProblems() {
        //given
        String memberId = "memberId";
        String certificateId = "certificateId";
        List<String> examId = List.of("examId1", "examId2");
        List<String> subjectId =  List.of("subjectId1", "subjectId2", "subjectId3");
        List<String> problemId =  List.of("problemId1", "problemId2", "problemId3", "problemId4");

        Member member = createMember(memberId);
        Certificate certificate = createCertificate(certificateId);
        member.updateCertificateState(certificate);
        List<Exam> exams = examId.stream()
                .map(id -> createExam(id, certificate))
                .toList();
        List<Subject> subjects = subjectId.stream()
                .map(id -> createSubject(id, certificate))
                .toList();
        List<Problem> requestProblems = List.of(
                createProblem(problemId.get(0), certificate, exams.get(0), subjects.get(0)),
                createProblem(problemId.get(1), certificate, exams.get(0), subjects.get(1)),
                createProblem(problemId.get(2), certificate, exams.get(0), subjects.get(2)),
                createProblem(problemId.get(3), certificate, exams.get(1), subjects.get(0))
        );
        List<Bookmark> bookmarks = requestProblems.stream()
                .map(problem -> Bookmark.of(member, problem))
                .toList();

        entityManager.persist(member);
        entityManager.persist(certificate);
        exams.forEach(entityManager::persist);
        subjects.forEach(entityManager::persist);
        requestProblems.forEach(entityManager::persist);
        bookmarks.forEach(entityManager::persist);

        Pageable pageable = PageRequest.of(0, 10);

        //when
        List<Problem> problems = problemRepository.findBookmarkedByExamIdAndSubjectIdIn(
                memberId, Optional.of(examId.get(0)), List.of(subjectId.get(0), subjectId.get(1)), pageable
        );

        //then
        assertThat(problems).hasSize(2);
    }

}
