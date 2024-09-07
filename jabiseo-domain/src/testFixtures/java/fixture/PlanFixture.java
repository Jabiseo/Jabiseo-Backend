package fixture;

import com.jabiseo.member.domain.Member;
import com.jabiseo.plan.domain.Plan;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;

public class PlanFixture {
    public static Plan createPlan(Member member, Long planId) {
        Plan plan = Plan.create(member, LocalDate.now());

        ReflectionTestUtils.setField(plan, "id", planId);
        return plan;
    }
}
