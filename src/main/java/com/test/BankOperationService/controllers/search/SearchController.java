package com.test.BankOperationService.controllers.search;


import com.test.BankOperationService.controllers.personController.PersonController;
import com.test.BankOperationService.model.Account.AccountResponse;
import com.test.BankOperationService.model.user.Person;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SearchController {

    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
    private final PersonSearchService personSearchService;

    public SearchController(PersonSearchService personSearchService) {
        this.personSearchService = personSearchService;
    }

    @Operation(summary = "Search persons with optional filtering and pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation")
    })
    @GetMapping("/persons")
    public Page<PersonDTO> searchPersons(
            @Parameter(in = ParameterIn.QUERY, name = "dateOfBirth", description = "Date of birth in format 'dd.MM.yyyy'")
            @RequestParam(required = false, defaultValue = "01.01.1700") String dateOfBirth,
            @Parameter(in = ParameterIn.QUERY, name = "phone", description = "Phone number")
            @RequestParam(required = false) String phone,
            @Parameter(in = ParameterIn.QUERY, name = "fullName", description = "Full name")
            @RequestParam(required = false) String fullName,
            @Parameter(in = ParameterIn.QUERY, name = "email", description = "Email address")
            @RequestParam(required = false) String email,
            Pageable pageable
    ) {
        logger.info("search /persons:");


        List<PersonDTO> resultList = personSearchService.searchPersons(
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
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), resultList.size());
        Page<PersonDTO> page = new PageImpl<>(resultList.subList(start, end), pageable, resultList.size());
        return page;
    }


}