package com.christmas.letter.processor.helper;

import com.christmas.letter.model.Letter;
import lombok.experimental.UtilityClass;
import net.bytebuddy.utility.RandomString;

import java.util.List;

@UtilityClass
public class LetterTestHelper {

    public static Letter createDefaultLetter(){
        return createLetter("existing@email.com", "Existing Test Letter");
    }

    public static Letter createLetter(String email, String name){
        return Letter.builder()
                .email(email)
                .name(name)
                .wishes(List.of("gift", "another gift"))
                .deliveryAddress("test street no. 7")
                .build();
    }

    public static List<Letter> createLetters() {
        return List.of(createLetter(String.format("%s@email.com", RandomString.make()), RandomString.make()),
                createLetter(String.format("%s@email.com", RandomString.make()), RandomString.make()),
                createLetter(String.format("%s@gmail.com", RandomString.make()), RandomString.make()),
                createLetter(String.format("%s@outlook.com", RandomString.make()), RandomString.make()),
                createLetter(String.format("%s@example.com", RandomString.make()), RandomString.make()),
                createLetter(String.format("%s@yahoo.com", RandomString.make()), RandomString.make()),
                createLetter(String.format("%s@email.com", RandomString.make()), RandomString.make()),
                createLetter(String.format("%s@email.com", RandomString.make()), RandomString.make()));
    }

}
