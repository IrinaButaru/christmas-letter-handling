package com.christmas.letter.sender.service;

import com.christmas.letter.sender.dto.ChristmasLetterRequest;
import com.christmas.letter.sender.dto.LetterEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LetterReceiverService {

    private final LetterReceiverEventPublisher publisher;

    @Autowired
    public LetterReceiverService(LetterReceiverEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publishLetter(ChristmasLetterRequest christmasLetterRequest) {
        publisher.publish(new LetterEvent(christmasLetterRequest));
    }
}
