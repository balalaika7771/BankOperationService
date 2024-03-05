package com.test.BankOperationService.controllers.search;

import com.test.BankOperationService.model.user.Person;
import com.test.BankOperationService.model.user.PersonRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PersonSearchService {

    private final EntityManager entityManager;
    private final PersonRepository personRepository;

    public PersonSearchService(EntityManager entityManager, PersonRepository personRepository) {
        this.entityManager = entityManager;
        this.personRepository = personRepository;
    }

    public Page<Person> searchPersons(LocalDate birthDate, String phone, String fullName, String email,
                                      Pageable pageable) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> query = cb.createQuery(Person.class);
        Root<Person> root = query.from(Person.class);

        List<Predicate> predicates = new ArrayList<>();

        if (birthDate != null) {
            predicates.add(cb.greaterThan(root.get("dateOfBirth"), birthDate));
        }

        if (phone != null) {
            predicates.add(cb.isMember(phone, root.get("phones")));
        }

        if (fullName != null) {
            predicates.add(cb.or(
                    cb.like(root.get("firstName"), fullName + "%"),
                    cb.like(root.get("lastName"), fullName + "%")
            ));
        }

        if (email != null) {
            predicates.add(cb.isMember(email, root.get("emails")));
        }

        query.where(predicates.toArray(new Predicate[0]));

        TypedQuery<Person> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Person> resultList = typedQuery.getResultList();
        long total = countTotalRecords(query);

        return new PageImpl<>(resultList, pageable, total);
    }

    private long countTotalRecords(CriteriaQuery<Person> query) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        countQuery.select(cb.count(countQuery.from(Person.class)));
        countQuery.where(query.getRestriction());
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
