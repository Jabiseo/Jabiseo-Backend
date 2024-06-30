package com.jabiseo.config;

import com.jabiseo.auth.oidc.KakaoKauthClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {


    @Bean
    public KakaoKauthClient kakaoKauthClient() {
        RestClient client = RestClient.builder()
                .baseUrl("https://kauth.kakao.com")
                .build();

        return HttpServiceProxyFactory
                .builder()
                .exchangeAdapter(RestClientAdapter.create(client))
                .build()
                .createClient(KakaoKauthClient.class);
    }
}
