package com.christmas.letter.sender.service;

import com.christmas.letter.sender.event.LetterSenderEventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LetterSenderServiceTest {

    @Mock
    private LetterSenderEventPublisher letterSenderEventPublisher;

    @InjectMocks
    private LetterSenderService letterSenderService;

    @Test
    public void testPublishLetter() {
        letterSenderService.publishLetter(any());

        verify(letterSenderEventPublisher, times(1)).publish(any());
    }
}
