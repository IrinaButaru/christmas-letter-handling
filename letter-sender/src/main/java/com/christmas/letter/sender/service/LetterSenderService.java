package com.christmas.letter.sender.service;

import com.christmas.letter.sender.dto.ChristmasLetter;
import com.christmas.letter.sender.dto.LetterEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LetterSenderService {

    private final LetterSenderEventPublisher publisher;

    @Autowired
    public LetterSenderService(LetterSenderEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publishLetter(ChristmasLetter christmasLetter) {
        publisher.publish(new LetterEvent(christmasLetter));
    }
}
