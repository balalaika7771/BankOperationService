package com.test.BankOperationService.controllers.search;


import com.test.BankOperationService.model.Account.AccountResponse;
import com.test.BankOperationService.model.user.Person;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
public class SearchController {

    private final PersonSearchService personSearchService;

    public SearchController(PersonSearchService personSearchService) {
        this.personSearchService = personSearchService;
    }

    @GetMapping("/persons")
    public List<PersonDTO> searchPersons(
            @RequestParam(required = false) String dateOfBirth,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String email,
            Pageable pageable
    ) {
        return personSearchService.searchPersons(
                LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                        phone,
                        fullName,
                        email,
                        pageable)
                .stream().map(
                person -> PersonDTO.builder()
                        .emails(person.getEmails())
                        .phones(person.getPhones())
                        .lastName(person.getLastName())
                        .firstName(person.getFirstName())
                        .accounts(person.getAccounts().stream().map(
                                account -> AccountResponse.builder()
                                        .number(account.getId())
                                        .build()).toList())
                        .build()).toList();
    }
}