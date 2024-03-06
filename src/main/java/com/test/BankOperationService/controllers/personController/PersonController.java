package com.test.BankOperationService.controllers.personController;


import com.test.BankOperationService.controllers.personAuth.PersonAuthController;
import com.test.BankOperationService.model.Account.AccountResponse;
import com.test.BankOperationService.model.user.EmailAddValidator;
import com.test.BankOperationService.model.user.Person;
import com.test.BankOperationService.model.user.PersonService;
import com.test.BankOperationService.model.user.PhoneAddValidator;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);
    private final PersonService personService;
    private final EmailAddValidator emailAddValidator;

    private final PhoneAddValidator phoneAddValidator;
    @Autowired
    public PersonController(PersonService personService, EmailAddValidator emailAddValidator, PhoneAddValidator phoneAddValidator) {
        this.personService = personService;
        this.emailAddValidator = emailAddValidator;
        this.phoneAddValidator = phoneAddValidator;
    }

    @Operation(summary = "Get person emails")
    @GetMapping("/emails")
    public List<String> getPersonEmails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Person person = (Person)authentication.getPrincipal();
        logger.info("[ok] /emails:");
        return personService.getPersonEmails(person.getId());
    }

    @Operation(summary = "Get person phones")
    @GetMapping("/phones")
    public List<String> getPersonPhones() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Person person = (Person)authentication.getPrincipal();
        logger.info("[ok] /phones:");
        return personService.getPersonPhones(person.getId());
    }

    @Operation(summary = "Get person accounts")
    @GetMapping("/accounts")
    public List<AccountResponse> getPersonAccounts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Person person = (Person)authentication.getPrincipal();
        logger.info("[ok] /accounts:");
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
    @Operation(summary = "Add email to person")
    @PostMapping("/emails")
    public ResponseEntity<?> addEmail(@RequestBody String email, BindingResult bindingResult) {
        emailAddValidator.validate(email,bindingResult);
        if (bindingResult.hasErrors()) {
            logger.warn("[error]Post /emails:"+ bindingResult.toString());
            // Обработка ошибок
            return ResponseEntity.badRequest().build();
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Person person = (Person)authentication.getPrincipal();
        personService.addEmail(person.getId(), email);
        logger.info("[ok]Post /emails:");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @Operation(summary = "Add phone to person")
    @PostMapping("/phones")
    public ResponseEntity<?> addPhone(@RequestBody String phone, BindingResult bindingResult) {
        phoneAddValidator.validate(phone,bindingResult);
        if (bindingResult.hasErrors()) {
            logger.warn("[error]Post /phones:"+ bindingResult.toString());
            // Обработка ошибок
            return ResponseEntity.badRequest().build();
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Person person = (Person)authentication.getPrincipal();
        personService.addPhone(person.getId(), phone);
        logger.info("[ok]Post /phones:");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @Operation(summary = "Delete email from person")
    @DeleteMapping("/emails")
    public ResponseEntity<?> deleteEmail(@RequestParam String email) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Person person = (Person)authentication.getPrincipal();
        if(personService.getPersonEmails(person.getId()).size() <= 1){
            logger.warn("[error]Delete /emails:"+ email);
            return ResponseEntity.badRequest().build();
        }
        personService.deleteEmail(person.getId(), email);
        logger.info("[ok]Delete /emails:");
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "Delete phone from person")
    @DeleteMapping("/phones")
    public ResponseEntity<?> deletePhone(@RequestParam String phone) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Person person = (Person)authentication.getPrincipal();
        if(personService.getPersonPhones(person.getId()).size() <= 1){
            logger.warn("[error]Delete /phones:"+ phone);
            return ResponseEntity.badRequest().build();
        }
        personService.deletePhone(person.getId(), phone);
        logger.info("[ok]Delete /phones:");
        return ResponseEntity.ok().build();
    }
}
