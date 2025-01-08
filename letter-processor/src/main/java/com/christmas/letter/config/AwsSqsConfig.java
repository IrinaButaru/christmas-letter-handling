package com.christmas.letter.config;

import com.christmas.letter.model.mapper.SqsMessageMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
public class AwsSqsConfig {

    @Value("${spring.cloud.aws.credentials.access-key}")
    public String accessKey;

    @Value("${spring.cloud.aws.credentials.secret-key}")
    public String secretKey;

    @Value("${spring.cloud.aws.sqs.region}")
    public String region;

    @Value("${com.christmas.letter.aws.sqs.queue-name}")
    public String queueName;

    @Bean
    public SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory(SqsAsyncClient sqsAsyncClient,
                                                                                         ObjectMapper mapper) {
        return SqsMessageListenerContainerFactory.builder()
                .sqsAsyncClient(sqsAsyncClient)
                .configure(options -> options.messageConverter(new SqsMessageMapper(mapper)))
                .build();
    }

    @Bean
    public ObjectMapper customObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }


}
