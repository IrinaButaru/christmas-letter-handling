package com.christmas.letter.sender.service;

import com.christmas.letter.sender.configuration.AWSSNSConfig;
import com.christmas.letter.sender.dto.LetterEvent;
import io.awspring.cloud.sns.core.SnsTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LetterSenderEventPublisher {

    private final SnsTemplate snsTemplate;
    private final AWSSNSConfig awsConfig;

    @Autowired
    public LetterSenderEventPublisher(SnsTemplate snsTemplate, AWSSNSConfig awsConfig) {
        this.snsTemplate = snsTemplate;
        this.awsConfig = awsConfig;
    }

    public void publish(LetterEvent letterEvent) {
        snsTemplate.convertAndSend(awsConfig.getTopicArn(), letterEvent);
    }
}
