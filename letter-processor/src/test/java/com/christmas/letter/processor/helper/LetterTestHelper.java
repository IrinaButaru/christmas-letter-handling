package com.christmas.letter.processor.helper;

import com.christmas.letter.model.Letter;
import lombok.experimental.UtilityClass;
import net.bytebuddy.utility.RandomString;

import java.util.ArrayList;
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

    public static List<Letter> createLetters(int itemNum) {
        ArrayList<Letter> letters = new ArrayList();
        letters.add(createLetter("ok@email.com", "Existing Name"));
        for (int iterator = 1; iterator < itemNum; iterator++) {
            letters.add(createLetter(String.format("%s@email.com", RandomString.make()), RandomString.make()));
        }
        return letters;
    }
}
