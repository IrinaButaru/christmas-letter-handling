package com.christmas.letter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LetterEmail {

    private List<String> recipients;

    private String subject;

    private String messageBody;

    private String attachement;

}
