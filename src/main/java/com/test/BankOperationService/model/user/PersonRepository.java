package com.test.BankOperationService.model.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {
  @Query("SELECT u FROM Person u JOIN u.emails e WHERE e = :email")
  Optional<Person> findByAnyEmail(String email);
  boolean existsByEmails(String email);
  boolean existsByPhonesContains(String phone);

}
