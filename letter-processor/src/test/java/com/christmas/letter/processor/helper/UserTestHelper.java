package com.christmas.letter.processor.helper;

import com.christmas.letter.model.Role;
import com.christmas.letter.model.entity.LetterEntity;
import com.christmas.letter.model.entity.UserEntity;
import com.christmas.letter.model.mapper.DynamoDbLetterMapper;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class UserTestHelper {

    public static final String GUEST_EMAIL = "guest@email.com";
    public static final String SANTA_EMAIL = "santa@email.com";
    public static final String ELF_EMAIL = "elf@email.com";

    public static UserEntity createUserEntity(String email, List<Role> roles) {
        return UserEntity.builder()
                .email(email)
                .name("User")
                .password("test123!")
                .roles(roles.stream().map(role -> role.toString()).collect(Collectors.toList()))
                .build();
    }

    public static List<LetterEntity> getLetterEntities() {
        return LetterTestHelper.createLetters(9)
                .stream()
                .map(DynamoDbLetterMapper.INSTANCE::objectToEntity)
                .toList();
    }

    public static List<UserEntity> getUserEntities() {
        return List.of(UserTestHelper.createUserEntity(UserTestHelper.SANTA_EMAIL, List.of(Role.SANTA)),
                UserTestHelper.createUserEntity(UserTestHelper.GUEST_EMAIL,List.of(Role.GUEST)),
                UserTestHelper.createUserEntity(UserTestHelper.ELF_EMAIL,List.of(Role.ELF)));
    }
}
