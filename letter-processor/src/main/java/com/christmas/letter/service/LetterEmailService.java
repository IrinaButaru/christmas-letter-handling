package com.christmas.letter.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.List;

@Log4j2
@Service
public class LetterEmailService {
    public static final  String EMAIL_BODY_TEMPLATE = "Hello, Santa!\n\n"
            + "You have received a letter that we could not process and it requires your attention. Here are the details:\n"
            + "%s";
    public static final String EMAIL_SUBJECT = "Wishes Letter";
    public static final String SENT_EMAIL_LOG = "Message sent via email";

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${com.christmas.letter.email.recipients}")
    private List<String> recipients;

    private final JavaMailSender javaMailSender;

    public LetterEmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmail(Message message) {
        try {

            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(sender);
            mailMessage.setTo(recipients.toArray(String[]::new));
            mailMessage.setText(createText(message));
            mailMessage.setSubject(EMAIL_SUBJECT);

            javaMailSender.send(mailMessage);
            log.info(SENT_EMAIL_LOG);
        } catch (Exception e) {
            log.error("Could not send email: " + e.getStackTrace());
            throw new IllegalStateException("Could not send email");
        }
    }

    private String createText(Message sqsMessage) {
        return  String.format(EMAIL_BODY_TEMPLATE, sqsMessage.body());
    }


}
