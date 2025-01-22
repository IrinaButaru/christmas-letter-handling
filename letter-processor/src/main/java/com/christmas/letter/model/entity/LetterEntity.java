package com.christmas.letter.model.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "christmas_letter")
public class LetterEntity {
    public static final String TABLE_NAME = "christmas_letter";
    public static final String EMAIL_KEY = "email";
    public static final String NAME_KEY = "name";
    public static final String WISHES_KEY = "wishes";
    public static final String DELIVERY_ADDRESS_KEY = "deliveryAddress";

    @DynamoDBHashKey(attributeName = "email")
    private String email;

    @DynamoDBAttribute(attributeName = "name")
    private String name;

    @DynamoDBAttribute(attributeName = "wishes")
    private List<String> wishes;

    @DynamoDBAttribute(attributeName = "delivery_address")
    private String deliveryAddress;
}
