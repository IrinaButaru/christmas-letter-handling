package com.christmas.letter.processor.service;

import com.christmas.letter.model.LetterEmail;
import com.christmas.letter.service.LetterDLQListener;
import com.christmas.letter.service.LetterEmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class LetterDLQListenerTest {

    @Mock
    private LetterEmailService letterEmailService;

    @InjectMocks
    private LetterDLQListener letterDLQListener;

    @Test
    public void processErrorLetter_WhenMessageReceived_ShouldSendEmail() {
        Message message = Message.builder().body("letter").build();
        LetterEmail letterEmail = LetterEmail.builder()
                .recipients(List.of("santa@email.com", "elf@email.com"))
                .subject("Wishes letter")
                .messageBody("test message body")
                .build();


        letterDLQListener.processErrorLetter(message);

        verify(letterEmailService).sendEmail(any());
    }
}
