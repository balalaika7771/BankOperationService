package com.test.BankOperationService.controllers.personAuth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CreateUserRequest {

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    private String dateOfBirth;

    @NotEmpty
    private String phone;

    @NotEmpty
    private String password;

    @NotEmpty
    @Email
    private String email;

    @Positive
    private double initialDeposit;
}
