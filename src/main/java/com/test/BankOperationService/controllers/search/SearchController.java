package com.test.BankOperationService.controllers.search;


import com.test.BankOperationService.controllers.personController.PersonController;
import com.test.BankOperationService.model.Account.AccountResponse;
import com.test.BankOperationService.model.user.Person;
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

    @GetMapping("/persons")
    public Page<PersonDTO> searchPersons(
            @RequestParam(required = false,defaultValue = "01.01.1700") String dateOfBirth,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "firstName,asc") String sort,
            Pageable pageable
    ) {
        logger.info("search /persons:");
        List<Sort.Order> orders = new ArrayList<>();
        String[] properties = sort.split(",");
        for (int i = 0; i < properties.length; i += 2) {
            Sort.Direction direction = Sort.Direction.fromString(properties[i + 1]);
            orders.add(new Sort.Order(direction, properties[i]));
        }
        Sort sortObj = Sort.by(orders);

        List<PersonDTO> resultList = personSearchService.searchPersons(
                        LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                        phone,
                        fullName,
                        email,
                        sortObj,
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