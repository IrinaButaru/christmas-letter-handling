package com.christmas.letter.service;

import com.christmas.letter.model.Letter;
import com.christmas.letter.model.mapper.DynamoDbLetterMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.awspring.cloud.sqs.annotation.SqsListener;
import jakarta.validation.Valid;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class LetterSqsListener {

    private final LetterProcessorService letterProcessorService;

    public LetterSqsListener(LetterProcessorService letterProcessorService) {
        this.letterProcessorService = letterProcessorService;
    }

    @SqsListener("${com.christmas.letter.aws.sqs.queue-url}")
    public Letter processLetter(@Valid @Payload Letter letter) throws JsonProcessingException {
        return letterProcessorService.saveLetter(Objects.requireNonNull(DynamoDbLetterMapper.INSTANCE.objectToEntity(letter)));
    }
}
