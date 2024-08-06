package fixture;

import com.jabiseo.problem.domain.Problem;
import com.jabiseo.problem.dto.ProblemWithBookmarkSummaryQueryDto;

public class ProblemWithBookmarkSummaryDtoFixture {

    public static ProblemWithBookmarkSummaryQueryDto createProblemWithBookmarkSummaryQueryDto(Problem problem, boolean isBookmark) {
        return new ProblemWithBookmarkSummaryQueryDto(
                problem.getId(),
                problem.getDescription(),
                isBookmark,
                problem.getExam().getId(),
                problem.getExam().getDescription(),
                problem.getSubject().getId(),
                problem.getSubject().getName(),
                problem.getSubject().getSequence()
        );
    }

}
