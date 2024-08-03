package fixture;

import com.jabiseo.problem.domain.Problem;
import com.jabiseo.problem.dto.ProblemWithBookmarkDetailDto;

public class ProblemWithBookmarkDetailDtoFixture {
    public static ProblemWithBookmarkDetailDto createProblemWithBookmarkDetailDto(Problem problem, boolean isBookmark) {
        return new ProblemWithBookmarkDetailDto(
                problem.getId(),
                problem.getDescription(),
                problem.getChoice1(),
                problem.getChoice2(),
                problem.getChoice3(),
                problem.getChoice4(),
                problem.getAnswerNumber(),
                problem.getSolution(),
                isBookmark,
                problem.getExam().getId(),
                problem.getExam().getDescription(),
                problem.getSubject().getId(),
                problem.getSubject().getName(),
                problem.getSubject().getSequence()
        );
    }
}
