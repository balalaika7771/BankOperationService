package com.test.BankOperationService.controllers.personController;


import com.test.BankOperationService.model.Account.Account;
import com.test.BankOperationService.model.user.EmailAddValidator;
import com.test.BankOperationService.model.user.Person;
import com.test.BankOperationService.model.user.PersonService;
import com.test.BankOperationService.model.user.PhoneAddValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user")
public class PersonController {

    private final PersonService personService;
    private final EmailAddValidator emailAddValidator;

    private final PhoneAddValidator phoneAddValidator;
    @Autowired
    public PersonController(PersonService personService, EmailAddValidator emailAddValidator, PhoneAddValidator phoneAddValidator) {
        this.personService = personService;
        this.emailAddValidator = emailAddValidator;
        this.phoneAddValidator = phoneAddValidator;
    }


    @GetMapping("/emails")
    public List<String> getPersonEmails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Person person = (Person)authentication.getPrincipal();
        return personService.getPersonEmails(person.getId());
    }

    @GetMapping("/phones")
    public List<String> getPersonPhones() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Person person = (Person)authentication.getPrincipal();
        return personService.getPersonPhones(person.getId());
    }

    @GetMapping("/accounts")
    public List<AccountResponse> getPersonAccounts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Person person = (Person)authentication.getPrincipal();

        return personService.getPersonAccounts(
                person.getId())
                .stream().map(
                        account ->
                                AccountResponse.builder()
                                        .number(account.getId())
                                        .initialDeposit(account.getInitialDeposit())
                                        .currentBalance(account.getCurrentBalance())
                                        .build())
                .toList();
    }

    @PostMapping("/emails")
    public ResponseEntity<?> addEmail(@RequestBody String email, BindingResult bindingResult) {
        emailAddValidator.validate(email,bindingResult);
        if (bindingResult.hasErrors()) {
            // Обработка ошибок
            return ResponseEntity.badRequest().build();
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Person person = (Person)authentication.getPrincipal();
        personService.addEmail(person.getId(), email);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/phones")
    public ResponseEntity<?> addPhone(@RequestBody String phone, BindingResult bindingResult) {
        phoneAddValidator.validate(phone,bindingResult);
        if (bindingResult.hasErrors()) {
            // Обработка ошибок
            return ResponseEntity.badRequest().build();
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Person person = (Person)authentication.getPrincipal();
        personService.addPhone(person.getId(), phone);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/emails")
    public ResponseEntity<?> deleteEmail(@RequestParam String email) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Person person = (Person)authentication.getPrincipal();
        if(personService.getPersonEmails(person.getId()).size() <= 1){
            return ResponseEntity.badRequest().build();
        }
        personService.deleteEmail(person.getId(), email);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/phones")
    public ResponseEntity<?> deletePhone(@RequestParam String phone) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Person person = (Person)authentication.getPrincipal();
        if(personService.getPersonPhones(person.getId()).size() <= 1){
            return ResponseEntity.badRequest().build();
        }
        personService.deletePhone(person.getId(), phone);
        return ResponseEntity.ok().build();
    }
}
