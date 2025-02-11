package com.christmas.letter.model.mapper;

import com.christmas.letter.model.dto.Letter;
import com.christmas.letter.model.entity.LetterEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DynamoDbLetterMapper {

    DynamoDbLetterMapper INSTANCE = Mappers.getMapper(DynamoDbLetterMapper.class);

    LetterEntity objectToEntity(Letter letter);
    Letter entitytoObject(LetterEntity letterEntity);
}
