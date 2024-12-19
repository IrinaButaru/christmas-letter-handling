package com.christmas.letter.model.mapper;

import com.christmas.letter.model.Letter;
import com.christmas.letter.model.LetterEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ChristmasLetterMapper {

    ChristmasLetterMapper INSTANCE = Mappers.getMapper(ChristmasLetterMapper.class);

    LetterEntity objectToEntity(Letter letter);
    Letter entitytoObject(LetterEntity letterEntity);
}
