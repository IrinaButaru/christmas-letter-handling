package com.christmas.letter.service;

import com.christmas.letter.model.dto.Letter;
import com.christmas.letter.model.mapper.DynamoDbLetterMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import jakarta.validation.Valid;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

@Service
@Validated
public class LetterSqsListener {

    private final LetterProcessorService letterProcessorService;

    public LetterSqsListener(LetterProcessorService letterProcessorService) {
        this.letterProcessorService = letterProcessorService;
    }

    @SqsListener("${com.christmas.letter.aws.sqs.queue-url}")
    public Letter processLetter(@Valid @Payload Letter letter) {
        return letterProcessorService.saveLetter(Objects.requireNonNull(DynamoDbLetterMapper.INSTANCE.objectToEntity(letter)));
    }
}
