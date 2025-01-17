package com.christmas.letter.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Letter {

    @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "email.must.be.valid")
    private String email;

    @NotEmpty(message = "name.cannot.be.empty")
    private String name;

    @NotEmpty(message = "must.add.at.least.one.wish")
    private List<String> wishes;

    @NotEmpty(message = "address.cannot.be.empty")
    private String deliveryAddress;

}
