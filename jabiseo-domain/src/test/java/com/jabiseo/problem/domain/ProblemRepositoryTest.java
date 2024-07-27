package com.jabiseo.problem.domain;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.certificate.domain.Exam;
import com.jabiseo.certificate.domain.Subject;
import com.jabiseo.member.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

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

    private String memberId;
    private Member member;
    private List<Long> examId;
    private List<Long> subjectId;
    private List<Long> problemIds;
    private Certificate certificate;
    private List<Exam> exams;
    private List<Subject> subjects;

    @BeforeEach
    void setUp() {
        //given
        memberId = "memberId";
        Long certificateId = 100L;
        examId = List.of(200L, 300L);
        subjectId = List.of(400L, 500L, 600L);
        problemIds = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L);

        member = createMember(memberId);
        certificate = createCertificate(certificateId);
        member.updateCurrentCertificate(certificate);
        exams = examId.stream()
                .map(id -> createExam(id, certificate))
                .toList();
        subjects = subjectId.stream()
                .map(id -> createSubject(id, certificate))
                .toList();

        entityManager.persist(member);
        entityManager.persist(certificate);
        exams.forEach(entityManager::persist);
        subjects.forEach(entityManager::persist);
    }

    @Test
    @DisplayName("시험, 과목 조건에 따라 북마크된 문제를 조회하는 쿼리가 정상적으로 동작한다.")
    void givenExamAndSubjectConditions_whenFindingBookmarkedProblems_thenFindBookmarkedProblems() {
        //given
        List<Problem> requestProblems = List.of(
                createProblem(problemIds.get(0), certificate, exams.get(0), subjects.get(0)),
                createProblem(problemIds.get(1), certificate, exams.get(0), subjects.get(1)),
                createProblem(problemIds.get(2), certificate, exams.get(0), subjects.get(2)),
                createProblem(problemIds.get(3), certificate, exams.get(1), subjects.get(0))
        );
        List<Bookmark> bookmarks = requestProblems.stream()
                .map(problem -> Bookmark.of(member, problem))
                .toList();
        requestProblems.forEach(entityManager::persist);
        bookmarks.forEach(entityManager::persist);
        Pageable pageable = PageRequest.of(0, 10);

        //when
        Page<Problem> problems = problemRepository.findBookmarkedByExamIdAndSubjectIdIn(
                memberId, examId.get(0), List.of(subjectId.get(0), subjectId.get(1)), pageable
        );

        //then
        assertThat(problems).hasSize(2);
    }

    @Test
    @DisplayName("시험 조건은 없고 과목에 따라 북마크된 문제를 조회하는 쿼리가 정상적으로 동작한다.")
    void givenSubjectConditions_whenFindingBookmarkedProblems_thenFindBookmarkedProblems() {
        //given
        List<Problem> requestProblems = List.of(
                createProblem(problemIds.get(0), certificate, exams.get(0), subjects.get(0)),
                createProblem(problemIds.get(1), certificate, exams.get(0), subjects.get(1)),
                createProblem(problemIds.get(2), certificate, exams.get(0), subjects.get(2)),
                createProblem(problemIds.get(3), certificate, exams.get(1), subjects.get(0))
        );
        List<Bookmark> bookmarks = requestProblems.stream()
                .map(problem -> Bookmark.of(member, problem))
                .toList();
        requestProblems.forEach(entityManager::persist);
        bookmarks.forEach(entityManager::persist);
        Pageable pageable = PageRequest.of(0, 10);

        //when
        Page<Problem> problems = problemRepository.findBookmarkedBySubjectIdIn(
                memberId, List.of(subjectId.get(0), subjectId.get(1)), pageable
        );

        //then
        assertThat(problems).hasSize(3);
    }

    @ParameterizedTest
    @DisplayName("페이지 수가 2개 이상인 경우, 시험, 과목 조건에 따라 북마크된 문제가 정상적으로 동작한다.")
    @CsvSource({"0, 10", "1, 1"})
    void givenExamAndSubjectConditionsWithMultiplePage_whenFindingBookmarkedProblems_thenFindBookmarkedProblems(int page, int pageSize) {
        //given
        List<Problem> requestProblems = problemIds.stream()
                .map(id -> createProblem(id, certificate, exams.get(0), subjects.get(0)))
                .toList();
        List<Bookmark> bookmarks = requestProblems.stream()
                .map(problem -> Bookmark.of(member, problem))
                .toList();
        requestProblems.forEach(entityManager::persist);
        bookmarks.forEach(entityManager::persist);
        Pageable pageable = PageRequest.of(page, 10);

        //when
        Page<Problem> problems = problemRepository.findBookmarkedBySubjectIdIn(
                memberId, List.of(subjectId.get(0), subjectId.get(0)), pageable
        );

        //then
        assertThat(problems).hasSize(pageSize);
    }

}
