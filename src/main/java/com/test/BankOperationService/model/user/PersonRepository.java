package com.test.BankOperationService.model.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer> {
  @Query("SELECT u FROM Person u JOIN u.emails e WHERE e = :email")
  Optional<Person> findByAnyEmail(String email);
}
