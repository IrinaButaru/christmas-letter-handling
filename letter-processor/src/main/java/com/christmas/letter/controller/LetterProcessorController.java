package com.christmas.letter.controller;

import com.christmas.letter.model.Letter;
import com.christmas.letter.model.PaginatedResponse;
import com.christmas.letter.service.LetterProcessorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/letter")
public class LetterProcessorController {

    private final LetterProcessorService letterProcessorService;

    public LetterProcessorController(LetterProcessorService letterProcessorService) {
        this.letterProcessorService = letterProcessorService;
    }

    @PostMapping()
    public ResponseEntity publishLetter() throws JsonProcessingException {

        //TODO: remove test endpoint
        letterProcessorService.processLetter(null);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{email}")
    public ResponseEntity<Letter> getLetterByEmail(@PathVariable String email) throws Exception {
        return ResponseEntity.ok(letterProcessorService.getLetterByEmail(email));
    }
    @GetMapping
    ResponseEntity<PaginatedResponse> getAllLetters(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(letterProcessorService.getAllLetters(pageable));
    }

}
