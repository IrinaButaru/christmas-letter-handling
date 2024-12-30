package com.christmas.letter.model.mapper;

import com.christmas.letter.exception.SqsMessageDeserializationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.support.converter.SqsMessagingMessageConverter;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.sqs.model.Message;

@RequiredArgsConstructor
public class SqsMessageMapper extends SqsMessagingMessageConverter {

    private final ObjectMapper objectMapper;

    @Override
    protected Object getPayloadToDeserialize(Message message) {
        String body = message.body();
        return getMessageFromBody(body);
    }

    public String getMessageFromBody(String message) {
        try {
            JsonNode node = objectMapper.readTree(message);

            return node.path("Message").asText();
        } catch (JsonProcessingException ex) {
            throw new SqsMessageDeserializationException("Could not process letter from SQS message");
        }
    }
}
