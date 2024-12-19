package com.christmas.letter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Letter {

    private String email;

    private String name;

    private List<String> wishes;

    private String deliveryAddress;

}
