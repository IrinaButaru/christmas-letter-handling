package com.christmas.letter.service;

import com.christmas.letter.event.LetterProcessorListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

@Service
public class LetterProcessorService {

    private final LetterProcessorListener letterProcessorListener;

    public LetterProcessorService(LetterProcessorListener letterProcessorListener) {
        this.letterProcessorListener = letterProcessorListener;
    }

    //TODO: remove test method
    public void processChristmasLetter() throws JsonProcessingException {
        letterProcessorListener.processLetter(null);
    }
}
