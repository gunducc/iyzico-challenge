package com.iyzico.challenge.dto;

import lombok.Data;

import javax.validation.constraints.Size;
import javax.validation.constraints.Email;

@Data
public class PassengerDto {
    private Long Id;

    @Size(min=2, max = 100, message = "Firstname length should be between 2 and 100 chars")
    private String name;

    @Size(min=2, max = 100, message = "Lastname length should be between 2 and 100 chars")
    private String surname;

    @Email(message = "Email address format is not correct")
    private String email;

    private int age;
}
