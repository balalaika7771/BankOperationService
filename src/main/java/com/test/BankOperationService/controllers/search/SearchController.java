package com.test.BankOperationService.controllers.search;


import com.test.BankOperationService.model.user.Person;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchController {

    private final PersonSearchService personSearchService;

    public SearchController(PersonSearchService personSearchService) {
        this.personSearchService = personSearchService;
    }

    @GetMapping("/persons")
    public List<Person> searchPersons(
            @RequestParam(required = false) String dateOfBirth,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String email
    ) {
        return personSearchService.searchPersons(dateOfBirth, phone, fullName, email);
    }
}