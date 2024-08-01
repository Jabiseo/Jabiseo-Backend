package com.jabiseo.problem.domain;

import com.jabiseo.certificate.domain.Certificate;
import com.jabiseo.certificate.domain.Exam;
import com.jabiseo.certificate.domain.Subject;
import com.jabiseo.common.config.JpaConfig;
import com.jabiseo.common.config.QueryDslConfig;
import com.jabiseo.member.domain.Member;
import com.jabiseo.problem.dto.ProblemWithBookmarkDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static fixture.CertificateFixture.createCertificate;
import static fixture.ExamFixture.createExam;
import static fixture.MemberFixture.createMember;
import static fixture.ProblemFixture.createProblem;
import static fixture.SubjectFixture.createSubject;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import({JpaConfig.class, QueryDslConfig.class})
@DisplayName("문제 세트에 대한 Problem Repository 테스트")
class ProblemRepositoryTest {

    @Autowired
    private ProblemRepository problemRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Long memberId;
    private List<Long> examIds;
    private List<Long> subjectIds;
    private Certificate certificate;
    private List<Exam> exams;
    private List<Subject> subjects;
    private int count;

    @BeforeEach
    void setUp() {
        //given
        Member member = createMember();
        certificate = createCertificate();
        member.updateCurrentCertificate(certificate);
        exams = new ArrayList<>();
        IntStream.range(0, 2).forEach(i -> exams.add(createExam(certificate)));
        subjects = new ArrayList<>();
        IntStream.range(0, 3).forEach(i -> subjects.add(createSubject(certificate)));
        List<Problem> requestProblems = List.of(
                createProblem(certificate, exams.get(0), subjects.get(0)),
                createProblem(certificate, exams.get(0), subjects.get(0)),
                createProblem(certificate, exams.get(0), subjects.get(2)),
                createProblem(certificate, exams.get(1), subjects.get(0))
        );
        List<Bookmark> bookmarks = List.of(
                Bookmark.of(member, requestProblems.get(0)),
                Bookmark.of(member, requestProblems.get(3))
        );

        entityManager.persist(member);
        entityManager.persist(certificate);
        exams.forEach(entityManager::persist);
        subjects.forEach(entityManager::persist);
        requestProblems.forEach(entityManager::persist);
        bookmarks.forEach(entityManager::persist);

        examIds = exams.stream().map(Exam::getId).toList();
        subjectIds = subjects.stream().map(Subject::getId).toList();
        memberId = member.getId();

        count = 10;
    }

    @Test
    @DisplayName("로그인한 유저가 시험, 과목 조건에 따라 문제 세트를 조회하는 쿼리가 정상적으로 동작한다.")
    void givenLoginMemberWithExamAndSubjectConditions_whenFindingBookmarkedProblems_thenFindBookmarkedProblems() {
        //when
        List<ProblemWithBookmarkDto> problems = problemRepository.findRandomByExamIdAndSubjectId(
                memberId, examIds.get(0), subjectIds.get(0), count
        );

        //then
        assertThat(problems).hasSize(2);
        long trueCount = problems.stream().filter(ProblemWithBookmarkDto::isBookmark).count();
        assertThat(trueCount).isEqualTo(1);
    }

    @Test
    @DisplayName("로그인한 유저가 시험을 제외한 과목 조건에 따라 문제 세트를 조회하는 쿼리가 정상적으로 동작한다.")
    void givenLoginMemberWithSubjectConditions_whenFindingBookmarkedProblems_thenFindBookmarkedProblems() {
        //when
        List<ProblemWithBookmarkDto> problems = problemRepository.findRandomByExamIdAndSubjectId(
                memberId, null, subjectIds.get(0), count
        );

        //then
        assertThat(problems).hasSize(3);
        long trueCount = problems.stream().filter(ProblemWithBookmarkDto::isBookmark).count();
        assertThat(trueCount).isEqualTo(2);
    }

    @Test
    @DisplayName("비로그인 유저가 시험, 과목 조건에 따라 문제 세트를 조회하는 쿼리가 정상적으로 동작한다.")
    void givenNonLoginMemberWithExamAndSubjectConditions_whenFindingBookmarkedProblems_thenFindBookmarkedProblems() {
        //when
        List<ProblemWithBookmarkDto> problems = problemRepository.findRandomByExamIdAndSubjectId(
                null, examIds.get(0), subjectIds.get(0), count
        );

        //then
        assertThat(problems).hasSize(2);
        long trueCount = problems.stream().filter(ProblemWithBookmarkDto::isBookmark).count();
        assertThat(trueCount).isEqualTo(0);
    }

    @Test
    @DisplayName("비로그인 유저가 시험을 제외한 과목 조건에 따라 문제 세트를 조회하는 쿼리가 정상적으로 동작한다.")
    void givenNonLoginMemberWithSubjectConditions_whenFindingBookmarkedProblems_thenFindBookmarkedProblems() {
        //when
        List<ProblemWithBookmarkDto> problems = problemRepository.findRandomByExamIdAndSubjectId(
                null, null, subjectIds.get(0), count
        );

        //then
        assertThat(problems).hasSize(3);
        long trueCount = problems.stream().filter(ProblemWithBookmarkDto::isBookmark).count();
        assertThat(trueCount).isEqualTo(0);
    }

}
