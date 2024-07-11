package com.jabiseo.auth.application.oidc;

import com.jabiseo.cache.RedisCacheRepository;
import com.jabiseo.client.KakaoKauthClient;
import com.jabiseo.client.OidcPublicKey;
import com.jabiseo.client.OidcPublicKeyResponse;
import com.jabiseo.auth.exception.AuthenticationBusinessException;
import com.jabiseo.auth.exception.AuthenticationErrorCode;
import com.jabiseo.auth.application.oidc.property.KakaoOidcProperty;
import com.jabiseo.exception.CommonErrorCode;
import com.jabiseo.member.domain.OauthServer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class KakaoIdTokenValidator extends AbstractIdTokenValidator {

    private final String KAKAO_ID_KEY = "sub";
    private final KakaoKauthClient kakaoKauthClient;
    private final RedisCacheRepository redisCacheRepository;
    private final String CACHE_KEY = "KAKAO_OIDC_PUBLIC_KEY";

    public KakaoIdTokenValidator(KakaoOidcProperty kakaoOidcProperty, IdTokenJwtHandler idTokenJwtHandler, KakaoKauthClient kakaoKauthClient, RedisCacheRepository redisCacheRepository) {
        super(kakaoOidcProperty.toIdTokenProperty(), idTokenJwtHandler);
        this.kakaoKauthClient = kakaoKauthClient;
        this.redisCacheRepository = redisCacheRepository;
    }

    @Override
    protected OidcPublicKey getOidcPublicKey(String kid) {

        List<OidcPublicKey> keys = redisCacheRepository.getPublicKeys(CACHE_KEY);
        if (keys == null) {
            OidcPublicKeyResponse publicKeys = kakaoKauthClient.getPublicKeys().getBody();
            keys = publicKeys.getKeys();
            redisCacheRepository.savePublicKey(CACHE_KEY, keys);
        }

        return keys.stream().filter((key) -> key.getKid().equals(kid))
                .findAny()
                .orElseThrow(() -> new AuthenticationBusinessException(AuthenticationErrorCode.INVALID_ID_TOKEN));
    }

    @Override
    protected OauthMemberInfo extractMemberInfoFromPayload(Map<String, Object> payload) {
        String oauthId = (String) payload.get(KAKAO_ID_KEY);

        if (requireValueIsNull(oauthId)) {
            throw new AuthenticationBusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }

        return OauthMemberInfo.builder()
                .oauthId(oauthId)
                .oauthServer(OauthServer.KAKAO)
                .email("email@email.com")
                .build();
    }

    @Override
    OauthServer getOauthServer() {
        return OauthServer.KAKAO;
    }

    /*
     *  해당 예외가 발생하는건 카카오에서 프로퍼티 key 값을 바꾸지 않는 이상은 발생하지 않는다.
     */
    private boolean requireValueIsNull(String oauthId) {
        return oauthId == null;
    }
}
