package fixture;

import com.jabiseo.domain.member.domain.DeviceToken;
import com.jabiseo.domain.member.domain.Member;

import java.util.UUID;

public class DeviceTokenFixture {

    public static DeviceToken createDeviceToken() {
        return DeviceToken.create(UUID.randomUUID().toString(), UUID.randomUUID().toString(), MemberFixture.createMember());
    }


    public static DeviceToken createDeviceToken(Member member) {
        return DeviceToken.create(UUID.randomUUID().toString(), UUID.randomUUID().toString(), member);
    }
}
