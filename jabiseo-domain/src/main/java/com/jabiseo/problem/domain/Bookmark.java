package com.jabiseo.problem.domain;

import com.jabiseo.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark {

    @Id
    @Column(name = "bookmark_id")
    private String id;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Problem problem;

    private Bookmark(String id, Member member, Problem problem) {
        this.id = id;
        this.member = member;
        this.problem = problem;
    }

    public static Bookmark of(Member member, Problem problem) {
        String id = UUID.randomUUID().toString(); //TODO: PK 생성 전략 변경 필요
        Bookmark bookmark = new Bookmark(id, member, problem);
        member.addBookmark(bookmark);
        problem.addBookmark(bookmark);
        return bookmark;
    }

}
