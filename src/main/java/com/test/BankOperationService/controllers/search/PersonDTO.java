package com.test.BankOperationService.controllers.search;

import com.test.BankOperationService.model.Account.Account;
import com.test.BankOperationService.model.Account.AccountResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {

    private List<String> phones;

    private String firstName;

    private String lastName;

    private List<String> emails;

    private LocalDate dateOfBirth;

    private List<AccountResponse> accounts;

}
