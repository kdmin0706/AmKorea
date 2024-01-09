package com.community.amkorea.soccer.config;

import com.community.amkorea.global.exception.CustomException;
import com.community.amkorea.global.exception.ErrorCode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
@Configuration
public class SoccerApiConfig {
  private final int MAX_IN_MEMORY_SIZE = 10 * 10 * 10 * 1024;   //10MB
  @Bean
  public SoccerApi apiSoccerProxy() {
    WebClient client = WebClient.builder()
        .baseUrl("https://apiv3.apifootball.com")
        .defaultStatusHandler(HttpStatusCode::isError,
            resp -> { throw new CustomException(ErrorCode.API_SERVER_ERROR); })
        .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(MAX_IN_MEMORY_SIZE))
        .build();

    HttpServiceProxyFactory factory = HttpServiceProxyFactory
        .builder(WebClientAdapter.forClient(client))
        .build();

    return factory.createClient(SoccerApi.class);
  }

}
