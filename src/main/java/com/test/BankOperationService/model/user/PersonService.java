package com.test.BankOperationService.model.user;

import com.test.BankOperationService.model.Account.Account;
import com.test.BankOperationService.model.Account.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PasswordEncoder passwordEncoder;
    private final PersonRepository personRepository;
    private final AccountRepository accountRepository;
    @Autowired
    public PersonService(PersonRepository personRepository, AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public Person createUserWithAccount(Person person, Account account) {
        Person savedPerson = personRepository.save(person);
        account.setOwner(savedPerson);
        accountRepository.save(account);
        return savedPerson;
    }
}
