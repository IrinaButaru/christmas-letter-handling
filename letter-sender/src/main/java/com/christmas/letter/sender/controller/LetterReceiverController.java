package com.christmas.letter.sender.controller;

import com.christmas.letter.sender.dto.ChristmasLetter;
import com.christmas.letter.sender.service.LetterSenderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/letter")
public class LetterReceiverController {

  private final LetterSenderService letterSenderService;

  @Autowired
  public LetterReceiverController(LetterSenderService letterSenderService) {
    this.letterSenderService = letterSenderService;
  }

  @PostMapping()
  public ResponseEntity publishLetter(@Valid @RequestBody ChristmasLetter letter) {

    letterSenderService.publishLetter(letter);
    return ResponseEntity.ok().build();
  }

}
