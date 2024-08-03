package fixture;

import com.jabiseo.problem.domain.Problem;
import com.jabiseo.problem.dto.ProblemWithBookmarkSummaryDto;

public class ProblemWithBookmarkSummaryDtoFixture {

    public static ProblemWithBookmarkSummaryDto createProblemWithBookmarkSummaryDto(Problem problem, boolean isBookmark) {
        return new ProblemWithBookmarkSummaryDto(
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
