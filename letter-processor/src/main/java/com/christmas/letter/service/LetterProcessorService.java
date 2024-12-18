package com.christmas.letter.service;

import com.christmas.letter.model.ChristmasLetter;
import com.christmas.letter.model.ChristmasLetterEntity;
import com.christmas.letter.model.PaginatedRequest;
import com.christmas.letter.model.PaginatedResponse;
import com.christmas.letter.model.mapper.ChristmasLetterMapper;
import com.christmas.letter.repository.ChristmasLetterRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.List;
import java.util.Objects;

@Service
public class LetterProcessorService {

    private final ChristmasLetterRepository christmasLetterRepository;

    public LetterProcessorService(ChristmasLetterRepository christmasLetterRepository) {
        this.christmasLetterRepository = christmasLetterRepository;
    }

    //    @SqsListener("${com.christmas.letter.aws.sqs.queue-url}")
    public ChristmasLetter processLetter(Message message) throws JsonProcessingException {
        //TODO: remove test code
        Message message1 = getRandomLetterMessage();

        ChristmasLetterEntity letter = Objects.requireNonNull(ChristmasLetterMapper.INSTANCE.objectToEntity(getMessageChristmasLetter(message1)));
        return ChristmasLetterMapper.INSTANCE.entitytoObject(christmasLetterRepository.save(letter));
    }

    public ChristmasLetter getLetterByEmail(String email) {
        return ChristmasLetterMapper.INSTANCE.entitytoObject(christmasLetterRepository.getLetterByEmail(email));
    }

    public PaginatedResponse getLetters(PaginatedRequest paginatedRequest) {
        ScanResponse response = christmasLetterRepository.getLetters(paginatedRequest);
        List<ChristmasLetter> letters = response
                .items()
                .stream()
                .map(ChristmasLetterEntity::new)
                .map(ChristmasLetterMapper.INSTANCE::entitytoObject)
                .toList();
        String lastReturnedEmail = response.lastEvaluatedKey().isEmpty() ? "" : response.lastEvaluatedKey().get(ChristmasLetterEntity.EMAIL_KEY).s();

        return PaginatedResponse.builder()
                .letters(letters)
                .lastReturnedEmail(lastReturnedEmail)
                .build();
    }

    public ChristmasLetter getMessageChristmasLetter(Message message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(message.body(), ChristmasLetter.class);
    }

    //TODO: remove test method
    public Message getRandomLetterMessage() {
        return Message.builder()
                .body("{\"email\":\"maria@outlook.com\",\"name\":\"Maria\",\"wishes\":[\"puppy\",\"smartwatch\"],\"deliveryAddress\":\"xstreet no. 9\"}")
                .build();
    }
}
