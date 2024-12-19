package com.christmas.letter.service;

import com.christmas.letter.model.Letter;
import com.christmas.letter.model.LetterEntity;
import com.christmas.letter.model.PaginatedResponse;
import com.christmas.letter.model.mapper.ChristmasLetterMapper;
import com.christmas.letter.repository.LetterRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.List;
import java.util.Objects;

@Service
public class LetterProcessorService {

    private final LetterRepository letterRepository;

    public LetterProcessorService(LetterRepository letterRepository) {
        this.letterRepository = letterRepository;
    }

    //    @SqsListener("${com.christmas.letter.aws.sqs.queue-url}")
    public Letter processLetter(Message message) throws JsonProcessingException {
        //TODO: remove test code
        Message message1 = getRandomLetterMessage();

        LetterEntity letter = Objects.requireNonNull(ChristmasLetterMapper.INSTANCE.objectToEntity(getMessageChristmasLetter(message1)));
        return ChristmasLetterMapper.INSTANCE.entitytoObject(letterRepository.save(letter));
    }

    public Letter getLetterByEmail(String email) throws Exception {
        LetterEntity letterEntity = letterRepository.findById(email)
                .orElseThrow(Exception::new);

        return ChristmasLetterMapper.INSTANCE.entitytoObject(letterEntity);
    }

    public PaginatedResponse getAllLetters(Pageable pageable) {
        Page<LetterEntity> page = letterRepository.findAll(pageable);

        List<Letter> letters = page
                .getContent()
                .stream()
                .map(ChristmasLetterMapper.INSTANCE::entitytoObject)
                .toList();

        return PaginatedResponse.builder()
                .letters(letters)
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    public Letter getMessageChristmasLetter(Message message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(message.body(), Letter.class);
    }

    //TODO: remove test method
    public Message getRandomLetterMessage() {
        return Message.builder()
                .body("{\"email\":\"maria@outlook.com\",\"name\":\"Maria\",\"wishes\":[\"puppy\",\"smartwatch\"],\"deliveryAddress\":\"xstreet no. 9\"}")
                .build();
    }
}
