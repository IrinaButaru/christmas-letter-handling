package com.christmas.letter.repository;

import com.christmas.letter.model.ChristmasLetterEntity;
import com.christmas.letter.model.PaginatedRequest;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

@Repository
public class ChristmasLetterRepository
{
    private final DynamoDbTemplate dynamoDbTemplate;
    private final DynamoDbClient dynamoDbClient;

    public ChristmasLetterRepository(DynamoDbTemplate dynamoDbTemplate, DynamoDbClient dynamoDbClient) {
        this.dynamoDbTemplate = dynamoDbTemplate;
        this.dynamoDbClient = dynamoDbClient;
    }

    public ChristmasLetterEntity save(ChristmasLetterEntity entity) {
        return dynamoDbTemplate.save(entity);
    }

    public ChristmasLetterEntity getLetterByEmail(String email){
        return dynamoDbTemplate.load(Key.builder().partitionValue(email).build(), ChristmasLetterEntity.class);
    }

    public ScanResponse getLetters(PaginatedRequest paginatedRequest) {
        AttributeValue lastEmail = paginatedRequest.getLastEvaluatedEmail().get(ChristmasLetterEntity.EMAIL_KEY);
//        Map<String,String> expressionAttributesNames = Map.of("#email","email");
//        Map<String,AttributeValue> expressionAttributeValues = Map.of(":email", AttributeValue.fromS("a"));

//        QueryRequest.Builder queryRequestBuilder = QueryRequest.builder()
//                .tableName(ChristmasLetterEntity.TABLE_NAME)
//                .keyConditionExpression("#email = :email")
//                .expressionAttributeNames(expressionAttributesNames)
//                .expressionAttributeValues(expressionAttributeValues)
//                .limit(paginatedRequest.getPageLimit())
//                .scanIndexForward(paginatedRequest.isPreviousPage());

        ScanRequest.Builder requestBuilder = ScanRequest.builder()
                .tableName(ChristmasLetterEntity.TABLE_NAME)
                .limit(paginatedRequest.getPageLimit());

        if(lastEmail != null && !lastEmail.s().isEmpty())
            requestBuilder.exclusiveStartKey(paginatedRequest.getLastEvaluatedEmail());
//            queryRequestBuilder.exclusiveStartKey(paginatedRequest.getLastEvaluatedEmail());


        return dynamoDbClient.scan(requestBuilder.build());
//        return dynamoDbClient.query(queryRequestBuilder.build());
    }
}
