package com.christmas.letter.event;

import com.christmas.letter.model.ChristmasLetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.model.Message;

@Component
public class LetterProcessorListener {

    @SqsListener("${com.christmas.letter.aws.sqs.queue-url}")
    public ChristmasLetter processLetter(Message message) throws JsonProcessingException {
        //TODO: save letter to DB

        return getMessageChristmasLetter(message);
    }

    public ChristmasLetter getMessageChristmasLetter(Message message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(message.body(), ChristmasLetter.class);
    }
}
