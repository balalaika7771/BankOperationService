package com.test.BankOperationService.model.user;

import com.test.BankOperationService.model.Account.Account;
import com.test.BankOperationService.model.Account.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
        try {
        Person savedPerson = personRepository.save(person);
        account.setOwner(savedPerson);
        accountRepository.save(account);
        return savedPerson;
    } catch (Throwable throwable){

    }
        return person;
    }

    public List<String> getPersonEmails(Long personId) {
        try {
        Person person = getPersonById(personId);
        return person.getEmails();
    } catch (Throwable throwable){

    }
        return null;
    }

    public List<String> getPersonPhones(Long personId) {
        try {
        Person person = getPersonById(personId);
        return person.getPhones();
    } catch (Throwable throwable){

    }
        return null;
    }

    public List<Account> getPersonAccounts(Long personId) {
        try {
        Person person = getPersonById(personId);
        return person.getAccounts();
    } catch (Throwable throwable){

    }
        return null;
    }

    public void addEmail(Long personId, String email) {
        try {
        Person person = getPersonById(personId);
        person.getEmails().add(email);
        personRepository.save(person);
        } catch (Throwable throwable){

        }
    }

    public void addPhone(Long personId, String phone) {
        try {
        Person person = getPersonById(personId);
        person.getPhones().add(phone);
        personRepository.save(person);
        } catch (Throwable throwable){

        }
    }

    public void deleteEmail(Long personId, String email) {
        try {
        Person person = getPersonById(personId);
        person.getEmails().remove(email);
        personRepository.save(person);
        } catch (Throwable throwable){

        }
    }

    public void deletePhone(Long personId, String phone) {
        try {
            Person person = getPersonById(personId);
            person.getPhones().remove(phone);
            personRepository.save(person);
        } catch (Throwable throwable){

        }

    }

    private Person getPersonById(Long personId) throws Throwable {
        return personRepository.findById(personId)
                .orElseThrow(() -> new Throwable("Person with id " + personId + " not found"));
    }
}
