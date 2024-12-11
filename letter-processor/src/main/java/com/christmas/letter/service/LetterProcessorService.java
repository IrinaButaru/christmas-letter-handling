package com.christmas.letter.service;

import com.christmas.letter.event.LetterProcessorListener;
import org.springframework.stereotype.Service;

@Service
public class LetterProcessorService {

    private final LetterProcessorListener letterProcessorListener;

    public LetterProcessorService(LetterProcessorListener letterProcessorListener) {
        this.letterProcessorListener = letterProcessorListener;
    }

    public void processChristmasLetter() {

    }
}
