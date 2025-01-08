package com.christmas.letter.controller;

import com.christmas.letter.model.Letter;
import com.christmas.letter.model.response.PaginatedResponse;
import com.christmas.letter.service.LetterProcessorService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/letter")
public class LetterProcessorController {

    private final LetterProcessorService letterProcessorService;

    public LetterProcessorController(LetterProcessorService letterProcessorService) {
        this.letterProcessorService = letterProcessorService;
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
