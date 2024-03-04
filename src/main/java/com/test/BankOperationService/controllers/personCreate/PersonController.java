package com.test.BankOperationService.controllers.personCreate;

import com.test.BankOperationService.model.user.CreateUserRequest;
import com.test.BankOperationService.model.user.EmailValidator;
import com.test.BankOperationService.model.user.PersonService;
import com.test.BankOperationService.model.user.PhoneValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Validated
@RequestMapping("/auth")
public class PersonController {

    private final AuthenticationService service;
    private final PersonService personService;

    private final PhoneValidator phoneValidator;
    private final EmailValidator emailValidator;
    @Autowired
    public PersonController(AuthenticationService service, PersonService personService, PhoneValidator phoneValidator, EmailValidator emailValidator) {
        this.service = service;
        this.personService = personService;
        this.phoneValidator = phoneValidator;
        this.emailValidator = emailValidator;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody CreateUserRequest request, BindingResult bindingResult
    ) {
        phoneValidator.validate(request.getPhone(), bindingResult);
        emailValidator.validate(request.getEmail(), bindingResult);
        if (bindingResult.hasErrors()) {
            // Обработка ошибок валидации
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }
}
