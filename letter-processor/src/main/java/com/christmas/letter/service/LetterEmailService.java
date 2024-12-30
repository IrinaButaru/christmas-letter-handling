package com.christmas.letter.service;

import com.christmas.letter.model.LetterEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class LetterEmailService {

    @Value("${spring.mail.username}")
    private String sender;

    private JavaMailSender javaMailSender;

    public LetterEmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmail(LetterEmail letterEmail) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(sender);
            mailMessage.setTo(letterEmail.getRecipients().toArray(String[]::new));
            mailMessage.setText(letterEmail.getMessageBody());
            mailMessage.setSubject(letterEmail.getSubject());

            javaMailSender.send(mailMessage);
        } catch (Exception e) {
            throw new IllegalStateException("Could not send email");
        }
    }


}
