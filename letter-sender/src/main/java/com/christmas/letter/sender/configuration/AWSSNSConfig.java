package com.christmas.letter.sender.configuration;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "com.christmas.letter.aws.sns.topic")
public class AWSSNSConfig {

    @NotBlank(message = "aws.sns.topic.must.be.configured")
    @Value("${AWS_SNS_TOPIC_ARN}")
    private String topicArn;
}
