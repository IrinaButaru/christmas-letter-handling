package com.christmas.letter.controller;

import com.christmas.letter.model.dto.Letter;
import com.christmas.letter.model.response.PaginatedResponse;
import com.christmas.letter.service.LetterProcessorService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("processor/letters")
public class LetterProcessorController {

    private final LetterProcessorService letterProcessorService;

    @PreAuthorize("hasAnyAuthority('SANTA','ELF')")
    @GetMapping("/{email}")
    public ResponseEntity<Letter> getLetterByEmail(@PathVariable String email) throws Exception {
        return ResponseEntity.ok(letterProcessorService.getLetterByEmail(email));
    }

    @PreAuthorize("hasAnyAuthority('SANTA','ELF')")
    @GetMapping
    ResponseEntity<PaginatedResponse> getAllLetters(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(letterProcessorService.getAllLetters(pageable));
    }

}
