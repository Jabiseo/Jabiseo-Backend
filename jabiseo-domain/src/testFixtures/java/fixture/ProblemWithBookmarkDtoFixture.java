package fixture;

import com.jabiseo.problem.domain.Problem;
import com.jabiseo.problem.dto.ProblemWithBookmarkDto;

public class ProblemWithBookmarkDtoFixture {
    public static ProblemWithBookmarkDto createProblemWithBookmarkDto(Problem problem, boolean isBookmark) {
        return new ProblemWithBookmarkDto(
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
                problem.getExam().getExamYear(),
                problem.getExam().getYearRound(),
                problem.getSubject().getId(),
                problem.getSubject().getName(),
                problem.getSubject().getSequence()
        );
    }
}
