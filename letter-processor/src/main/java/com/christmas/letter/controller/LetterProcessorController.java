package com.christmas.letter.controller;

import com.christmas.letter.service.LetterProcessorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/letter")
public class LetterProcessorController {

    private final LetterProcessorService letterProcessorService;

    public LetterProcessorController(LetterProcessorService letterProcessorService) {
        this.letterProcessorService = letterProcessorService;
    }

    @PostMapping()
    public ResponseEntity publishLetter() throws JsonProcessingException {

        //TODO: remove test endpoint
        letterProcessorService.processChristmasLetter();
        return ResponseEntity.ok().build();
    }


}
