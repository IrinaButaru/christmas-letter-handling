package com.christmas.letter.controller;

import com.christmas.letter.service.LetterProcessorService;
import org.springframework.stereotype.Controller;

@Controller
public class LetterProcessorController {

    private final LetterProcessorService letterProcessorService;

    public LetterProcessorController(LetterProcessorService letterProcessorService) {
        this.letterProcessorService = letterProcessorService;
    }


}
