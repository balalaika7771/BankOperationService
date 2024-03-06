package com.test.BankOperationService.controllers.search;

import com.test.BankOperationService.model.user.Person;
import com.test.BankOperationService.model.user.PersonRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PersonSearchService {

    private final PersonRepository personRepository;

    public PersonSearchService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Page<Person> searchPersons(LocalDate birthDate, String phone, String fullName, String email,
                                       Pageable pageable) {
        Specification<Person> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (birthDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dateOfBirth"), birthDate));
            }
            if (phone != null && !phone.isEmpty()) {
                predicates.add(cb.isMember(phone, root.get("phones")));
            }
            if (fullName != null && !fullName.isEmpty()) {
                predicates.add(cb.like(root.get("firstName"), fullName + "%"));
            }
            if (email != null && !email.isEmpty()) {
                predicates.add(cb.isMember(email, root.get("emails")));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return personRepository.findAll(spec, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()));
    }
}

