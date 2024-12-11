package com.christmas.letter.sender.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChristmasLetter {

    @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "email.must.be.valid")
    private String email;

    @NotEmpty(message = "name.cannot.be.empty")
    private String name;

    @NotEmpty(message = "must.add.at.least.one.wish")
    private List<String> wishes;

    @NotEmpty(message = "address.cannot.be.empty")
    private String deliveryAddress;


}
