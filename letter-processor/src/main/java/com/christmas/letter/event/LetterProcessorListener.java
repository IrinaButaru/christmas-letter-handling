package com.christmas.letter.event;

import com.christmas.letter.model.ChristmasLetter;
import com.christmas.letter.model.ChristmasLetterEntity;
import com.christmas.letter.model.mapper.ChristmasLetterMapper;
import com.christmas.letter.repository.ChristmasLetterRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.Objects;

@Service
public class LetterProcessorListener {

    private final ChristmasLetterRepository christmasLetterRepository;

    public LetterProcessorListener(ChristmasLetterRepository christmasLetterRepository) {
        this.christmasLetterRepository = christmasLetterRepository;
    }

    @SqsListener("${com.christmas.letter.aws.sqs.queue-url}")
    public ChristmasLetter processLetter(Message message) throws JsonProcessingException {

        return saveChristmasLetter(ChristmasLetterMapper.INSTANCE.objectToEntity(getMessageChristmasLetter(message)));

    }

    public ChristmasLetter getMessageChristmasLetter(Message message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(message.body(), ChristmasLetter.class);
    }

    public ChristmasLetter saveChristmasLetter(ChristmasLetterEntity letter) {
        return ChristmasLetterMapper.INSTANCE.entitytoObject(christmasLetterRepository.save(Objects.requireNonNull(letter)));
    }
}
