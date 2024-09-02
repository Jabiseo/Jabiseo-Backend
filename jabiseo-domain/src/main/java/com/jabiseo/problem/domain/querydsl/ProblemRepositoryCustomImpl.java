package com.jabiseo.problem.domain.querydsl;

import com.jabiseo.problem.dto.ProblemWithBookmarkDetailQueryDto;
import com.jabiseo.problem.dto.ProblemWithBookmarkSummaryQueryDto;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.jabiseo.certificate.domain.QExam.exam;
import static com.jabiseo.certificate.domain.QSubject.subject;
import static com.jabiseo.problem.domain.QBookmark.bookmark;
import static com.jabiseo.problem.domain.QProblem.problem;

@RequiredArgsConstructor
public class ProblemRepositoryCustomImpl implements ProblemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ProblemWithBookmarkDetailQueryDto> findDetailRandomByExamIdAndSubjectIdWithBookmark(Long memberId, Long examId, Long subjectId, int count) {
        return queryFactory
                .select(
                        Projections.constructor(
                                ProblemWithBookmarkDetailQueryDto.class,
                                problem.id.as("problemId"),
                                problem.description,
                                problem.choice1,
                                problem.choice2,
                                problem.choice3,
                                problem.choice4,
                                problem.answerNumber,
                                problem.solution,
                                Expressions.cases()
                                        .when(isBookmarkedByMember(memberId, problem.id))
                                        .then(true)
                                        .otherwise(false)
                                        .as("isBookmark"),
                                exam.id.as("examId"),
                                exam.description.as("examDescription"),
                                subject.id.as("subjectId"),
                                subject.name.as("subjectName"),
                                subject.sequence.as("subjectSequence")
                        )
                )
                .from(problem)
                .join(problem.exam, exam)
                .join(problem.subject, subject)
                .where(examIdEq(examId), subjectIdEq(subjectId))
                .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
                .limit(count)
                .fetch();
    }

    @Override
    public List<ProblemWithBookmarkDetailQueryDto> findDetailByIdsInWithBookmark(Long memberId, List<Long> problemIds) {
        return queryFactory
                .select(
                        Projections.constructor(
                                ProblemWithBookmarkDetailQueryDto.class,
                                problem.id.as("problemId"),
                                problem.description,
                                problem.choice1,
                                problem.choice2,
                                problem.choice3,
                                problem.choice4,
                                problem.answerNumber,
                                problem.solution,
                                Expressions.cases()
                                        .when(isBookmarkedByMember(memberId, problem.id))
                                        .then(true)
                                        .otherwise(false)
                                        .as("isBookmark"),
                                exam.id.as("examId"),
                                exam.description.as("examDescription"),
                                subject.id.as("subjectId"),
                                subject.name.as("subjectName"),
                                subject.sequence.as("subjectSequence")
                        )
                )
                .from(problem)
                .where(problem.id.in(problemIds))
                .fetch();
    }

    @Override
    public Page<ProblemWithBookmarkSummaryQueryDto> findBookmarkedSummaryByExamIdAndSubjectIdsInWithBookmark(Long memberId, Long examId, List<Long> subjectIds, Pageable pageable) {
        List<ProblemWithBookmarkSummaryQueryDto> content = queryFactory
                .select(
                        Projections.constructor(
                                ProblemWithBookmarkSummaryQueryDto.class,
                                problem.id.as("problemId"),
                                problem.description,
                                Expressions.cases()
                                        .when(isBookmarkedByMember(memberId, problem.id))
                                        .then(true)
                                        .otherwise(false)
                                        .as("isBookmark"),
                                exam.id.as("examId"),
                                exam.description.as("examDescription"),
                                subject.id.as("subjectId"),
                                subject.name.as("subjectName"),
                                subject.sequence.as("subjectSequence")
                        )
                )
                .from(problem)
                .join(problem.exam, exam)
                .join(problem.subject, subject)
                .join(bookmark).on(bookmark.problem.id.eq(problem.id))
                .where(examIdEq(examId), subjectIdsIn(subjectIds))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(problem.count())
                .from(problem)
                .join(problem.exam, exam)
                .join(problem.subject, subject)
                .join(bookmark).on(bookmark.problem.id.eq(problem.id))
                .where(examIdEq(examId), subjectIdsIn(subjectIds));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public ProblemWithBookmarkDetailQueryDto findDetailByIdWithBookmark(Long memberId, Long problemId) {
        return queryFactory
                .select(
                        Projections.constructor(
                                ProblemWithBookmarkDetailQueryDto.class,
                                problem.id.as("problemId"),
                                problem.description,
                                problem.choice1,
                                problem.choice2,
                                problem.choice3,
                                problem.choice4,
                                problem.answerNumber,
                                problem.solution,
                                Expressions.cases()
                                        .when(isBookmarkedByMember(memberId, problem.id))
                                        .then(true)
                                        .otherwise(false)
                                        .as("isBookmark"),
                                exam.id.as("examId"),
                                exam.description.as("examDescription"),
                                subject.id.as("subjectId"),
                                subject.name.as("subjectName"),
                                subject.sequence.as("subjectSequence")
                        )
                )
                .from(problem)
                .where(problem.id.eq(problemId))
                .fetchOne();
    }

    @Override
    public List<ProblemWithBookmarkSummaryQueryDto> findSummaryByIdsInWithBookmark(Long memberId, List<Long> problemIds) {
        return queryFactory
                .select(
                        Projections.constructor(
                                ProblemWithBookmarkSummaryQueryDto.class,
                                problem.id.as("problemId"),
                                problem.description,
                                Expressions.cases()
                                        .when(isBookmarkedByMember(memberId, problem.id))
                                        .then(true)
                                        .otherwise(false)
                                        .as("isBookmark"),
                                exam.id.as("examId"),
                                exam.description.as("examDescription"),
                                subject.id.as("subjectId"),
                                subject.name.as("subjectName"),
                                subject.sequence.as("subjectSequence")
                        )
                )
                .from(problem)
                .join(problem.exam, exam)
                .join(problem.subject, subject)
                .where(problem.id.in(problemIds))
                .fetch();
    }

    private Predicate subjectIdsIn(List<Long> subjectIds) {
        return subjectIds != null ? subject.id.in(subjectIds) : null;
    }

    private static BooleanExpression subjectIdEq(Long subjectId) {
        return subjectId != null ? subject.id.eq(subjectId) : null;
    }

    private BooleanExpression examIdEq(Long examId) {
        return examId != null ? exam.id.eq(examId) : null;
    }

    private Predicate isBookmarkedByMember(Long memberId, NumberPath<Long> id) {
        if (memberId == null) {
            return Expressions.FALSE;
        }
        return JPAExpressions.selectOne()
                .from(bookmark)
                .where(bookmark.member.id.eq(memberId), bookmark.problem.id.eq(id))
                .exists();
    }
}
