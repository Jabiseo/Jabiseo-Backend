package com.jabiseo.client;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {


    @Bean
    public KakaoKauthClient kakaoKauthClient() {
        RestClient client = RestClient.builder()
                .baseUrl("https://kauth.kakao.com")
                .defaultStatusHandler(HttpStatusCode::isError, ((request, response) -> {
                    throw new NetworkApiException(NetworkApiErrorCode.KAKAO_JWK_API_FAIL);
                }))
                .build();

        return HttpServiceProxyFactory
                .builder()
                .exchangeAdapter(RestClientAdapter.create(client))
                .build()
                .createClient(KakaoKauthClient.class);
    }
}
