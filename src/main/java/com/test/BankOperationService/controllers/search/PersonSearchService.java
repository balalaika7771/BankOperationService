package com.test.BankOperationService.controllers.search;

import com.test.BankOperationService.model.user.Person;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonSearchService {

    private final PersonElasticsearchRepository personElasticsearchRepository;

    public PersonSearchService(PersonElasticsearchRepository personElasticsearchRepository) {
        this.personElasticsearchRepository = personElasticsearchRepository;
    }

    public List<Person> searchPersons(String dateOfBirth, String phone, String fullName, String email) {
        // Выполняем поиск в Elasticsearch с учетом переданных параметров
        return personElasticsearchRepository.searchPersons(dateOfBirth, phone, fullName, email);
    }
}
