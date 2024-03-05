package com.test.BankOperationService.controllers.search;

import com.test.BankOperationService.model.user.Person;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonElasticsearchRepository extends ElasticsearchRepository<Person, String> {

    List<Person> searchPersons(String dateOfBirth, String phone, String fullName, String email);
}
