package com.christmas.letter.service;

import com.christmas.letter.model.LetterEmail;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.List;

@Log4j2
@Service
public class LetterDLQListener {

    @Value("${com.christmas.letter.email.recipients}")
    private List<String> recipients;

    private LetterEmailService letterEmailService;

    public LetterDLQListener(LetterEmailService letterEmailService) {
        this.letterEmailService = letterEmailService;
    }

    @SqsListener("${com.christmas.letter.aws.sqs.dlq-url}")
    public void processErrorLetter(Message message) {
        log.error("Got a christmas letter that could not be processed");

        LetterEmail letterEmail = LetterEmail.builder()
                .recipients(recipients)
                .subject("Wishes letter")
                .messageBody(createText(message))
                .build();

        letterEmailService.sendEmail(letterEmail);
    }

    private String createText(Message sqsMessage) {
        return  String.format("Hello, Santa!\n\n"
                + "You have received a letter that we could not process and it requires your attention. Here are the details:\n"
                + "%s", sqsMessage.body());
    }
}
