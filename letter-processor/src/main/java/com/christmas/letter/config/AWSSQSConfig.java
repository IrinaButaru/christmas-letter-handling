package com.christmas.letter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSSQSConfig {

    @Value("${spring.cloud.aws.credentials.access-key}")
    public String accessKey;

    @Value("${spring.cloud.aws.credentials.secret-key}")
    public String secretKey;

    @Value("${spring.cloud.aws.sqs.region}")
    public String region;

    @Value("${com.christmas.letter.aws.sqs.queue-name}")
    public String queueName;



}
