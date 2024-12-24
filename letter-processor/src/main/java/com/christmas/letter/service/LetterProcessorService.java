package com.christmas.letter.service;

import com.christmas.letter.exception.NotFoundException;
import com.christmas.letter.model.Letter;
import com.christmas.letter.model.LetterEntity;
import com.christmas.letter.model.mapper.DynamoDbLetterMapper;
import com.christmas.letter.model.response.PaginatedResponse;
import com.christmas.letter.repository.LetterRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LetterProcessorService {

    private final LetterRepository letterRepository;

    public LetterProcessorService(LetterRepository letterRepository) {
        this.letterRepository = letterRepository;
    }

    public Letter saveLetter(LetterEntity letterEntity) {
        return DynamoDbLetterMapper.INSTANCE.entitytoObject(letterRepository.save(letterEntity));
    }

    public Letter getLetterByEmail(String email) {
        LetterEntity letterEntity = letterRepository.findById(email)
                .orElseThrow(() -> new NotFoundException(String.format("Could not find letter for email %s ", email)));

        return DynamoDbLetterMapper.INSTANCE.entitytoObject(letterEntity);
    }

    public PaginatedResponse getAllLetters(Pageable pageable) {
        Page<LetterEntity> page = letterRepository.findAll(pageable);

        List<Letter> letters = page
                .getContent()
                .stream()
                .map(DynamoDbLetterMapper.INSTANCE::entitytoObject)
                .toList();

        return PaginatedResponse.builder()
                .letters(letters)
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}
