package com.christmas.letter.sender.controller;

import com.christmas.letter.sender.dto.ChristmasLetter;
import com.christmas.letter.sender.service.LetterSenderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/sender/letters")
public class LetterSenderController {

  private final LetterSenderService letterSenderService;

  @PostMapping()
  public ResponseEntity publishLetter( @Valid @RequestBody ChristmasLetter letter) {

    letterSenderService.publishLetter(letter);
    return ResponseEntity.ok().build();
  }

}
