package com.test.BankOperationService.model.user;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PhoneAddValidator implements Validator {

    private final PersonRepository personRepository;

    public PhoneAddValidator(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return String.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        String phone = (String) target;
        // Проверяем уникальность телефонных номеров
        if (personRepository.existsByPhonesContains(phone)) {
            errors.rejectValue("phones", "phone.duplicate", "Телефонный номер '" + phone + "' уже занят");
        }
    }
}