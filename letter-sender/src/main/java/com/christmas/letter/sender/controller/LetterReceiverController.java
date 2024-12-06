package com.christmas.letter.sender.controller;

import com.christmas.letter.sender.dto.ChristmasLetterRequest;
import com.christmas.letter.sender.service.LetterReceiverService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/letter")
public class LetterReceiverController {

  private final LetterReceiverService letterReceiverService;

  @Autowired
  public LetterReceiverController(LetterReceiverService letterReceiverService) {
    this.letterReceiverService = letterReceiverService;
  }

  @PostMapping()
  public ResponseEntity publishLetter(@Valid @RequestBody ChristmasLetterRequest letter) {
    return ResponseEntity.ok().build();
  }

}
