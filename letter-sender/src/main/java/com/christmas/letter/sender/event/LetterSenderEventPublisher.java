package com.christmas.letter.sender.event;

import com.christmas.letter.sender.configuration.AWSSNSConfig;
import com.christmas.letter.sender.dto.ChristmasLetter;
import io.awspring.cloud.sns.core.SnsTemplate;
import org.springframework.stereotype.Component;

@Component
public class LetterSenderEventPublisher {

    private final SnsTemplate snsTemplate;
    private final AWSSNSConfig awsConfig;


    public LetterSenderEventPublisher(SnsTemplate snsTemplate, AWSSNSConfig awsConfig) {
        this.snsTemplate = snsTemplate;
        this.awsConfig = awsConfig;
    }

    public void publish(ChristmasLetter letterEvent) {
        snsTemplate.convertAndSend(awsConfig.getTopicArn(), letterEvent);
    }
}
