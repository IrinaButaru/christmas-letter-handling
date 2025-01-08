package com.christmas.letter.processor.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@TestConfiguration
public class MailpitConfig {

    @Bean
    RestClient mailpitRestClient(RestClient.Builder builder, @Value("${spring.mail.host}") String host, @Value("${mailpit.web.port}") int port) {
        return builder
                .baseUrl("http://" + host + ":" + port + "/api/v1")
                .build();
    }

    @Bean
    MailpitClient mailpitClient(RestClient mailpitRestClient) {
        return new MailpitClient(mailpitRestClient);
    }
}
