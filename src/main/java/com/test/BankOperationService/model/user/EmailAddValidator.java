package com.test.BankOperationService.model.user;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class EmailAddValidator implements Validator {

    private final PersonRepository personRepository;

    public EmailAddValidator(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return String.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        String email = (String) target;
        // Проверяем уникальность email'ов
        if (personRepository.existsByEmails(email)) {
            errors.rejectValue("emails", "email.duplicate", "Email '" + email + "' уже занят");
        }
    }
}