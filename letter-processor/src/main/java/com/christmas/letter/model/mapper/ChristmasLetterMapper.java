package com.christmas.letter.model.mapper;

import com.christmas.letter.model.ChristmasLetter;
import com.christmas.letter.model.ChristmasLetterEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ChristmasLetterMapper {

    ChristmasLetterMapper INSTANCE = Mappers.getMapper(ChristmasLetterMapper.class);

    ChristmasLetterEntity objectToEntity(ChristmasLetter christmasLetter);
    ChristmasLetter entitytoObject(ChristmasLetterEntity christmasLetterEntity);
}
