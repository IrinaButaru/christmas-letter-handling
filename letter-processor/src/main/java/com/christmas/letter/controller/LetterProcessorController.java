package com.christmas.letter.controller;

import com.christmas.letter.model.ChristmasLetter;
import com.christmas.letter.model.PaginatedRequest;
import com.christmas.letter.service.LetterProcessorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        letterProcessorService.processLetter(null);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{email}")
    public ResponseEntity<ChristmasLetter> getLetterByEmail(@PathVariable String email) {
        return ResponseEntity.ok(letterProcessorService.getLetterByEmail(email));
    }

    @GetMapping
    public ResponseEntity getLetters(@RequestParam(required = false) Integer pageSize,
                                     @RequestParam(required = false) String lastEvaluatedEmail,
                                     @RequestParam(required = false) boolean previousPage) {

        PaginatedRequest paginatedRequest = new PaginatedRequest(pageSize, lastEvaluatedEmail, previousPage);
        return ResponseEntity.ok(letterProcessorService.getLetters(paginatedRequest));
    }

}
