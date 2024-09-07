package fixture;

import com.jabiseo.plan.domain.ActivityType;
import com.jabiseo.plan.domain.GoalType;
import com.jabiseo.plan.domain.Plan;
import com.jabiseo.plan.domain.PlanItem;
import org.springframework.test.util.ReflectionTestUtils;

public class PlanItemFixture {

    public static PlanItem createPlanItem(ActivityType activityType, GoalType goalType) {
        Plan plan = PlanFixture.createPlan(MemberFixture.createMember(), 1L);
        PlanItem planItem = new PlanItem(plan, activityType, goalType, 0);
        ReflectionTestUtils.setField(planItem, "id", 1L);
        return planItem;
    }
}
