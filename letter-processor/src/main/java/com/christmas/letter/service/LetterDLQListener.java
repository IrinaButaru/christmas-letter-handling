package com.christmas.letter.service;

import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.model.Message;

@Log4j2
@Service
public class LetterDLQListener {

    private LetterEmailService letterEmailService;

    public LetterDLQListener(LetterEmailService letterEmailService) {
        this.letterEmailService = letterEmailService;
    }

    @SqsListener("${com.christmas.letter.aws.sqs.dlq-url}")
    public void processErrorLetter(Message message) {
        log.error("Got a christmas letter that could not be processed");

        letterEmailService.sendEmail(message);
    }

}
