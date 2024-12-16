package com.christmas.letter.repository;

import com.christmas.letter.model.ChristmasLetterEntity;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ChristmasLetterRepository
{
    private final DynamoDbTemplate dynamoDbTemplate;

    public ChristmasLetterRepository(DynamoDbTemplate dynamoDbTemplate) {
        this.dynamoDbTemplate = dynamoDbTemplate;
    }

    public ChristmasLetterEntity save(ChristmasLetterEntity entity) {
        return dynamoDbTemplate.save(entity);
    }
}
