package com.test.BankOperationService.controllers.personAuth;

import com.test.BankOperationService.model.user.EmailAddValidator;
import com.test.BankOperationService.model.user.PersonService;
import com.test.BankOperationService.model.user.PhoneAddValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class PersonAuthController {

    private static final Logger logger = LoggerFactory.getLogger(PersonAuthController.class);
    private final AuthenticationService service;
    private final PersonService personService;

    private final PhoneAddValidator phoneAddValidator;
    private final EmailAddValidator emailAddValidator;
    @Autowired
    public PersonAuthController(AuthenticationService service, PersonService personService, PhoneAddValidator phoneAddValidator, EmailAddValidator emailAddValidator) {
        this.service = service;
        this.personService = personService;
        this.phoneAddValidator = phoneAddValidator;
        this.emailAddValidator = emailAddValidator;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody CreateUserRequest request, BindingResult bindingResult
    ) {

        phoneAddValidator.validate(request.getPhone(), bindingResult);
        emailAddValidator.validate(request.getEmail(), bindingResult);
        if (bindingResult.hasErrors()) {
            logger.warn("[error] /register:" + request.toString()+"error\n"+bindingResult.toString());
            // Обработка ошибок валидации
            return ResponseEntity.badRequest().build();
        }
        logger.info("[ok] /register:" + request.toString());
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        logger.info("[ok] /authenticate:" + request.toString());
            return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        logger.info("[ok] /refresh-token:");
        service.refreshToken(request, response);
    }
}
