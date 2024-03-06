package com.test.BankOperationService.controllers.transfer;

import com.test.BankOperationService.model.Account.Account;
import com.test.BankOperationService.model.user.Person;
import com.test.BankOperationService.model.user.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

public class TransferController {

    private final TransferService transferService;
    private final PersonService personService;
    public TransferController(TransferService transferService, PersonService personService) {
        this.transferService = transferService;
        this.personService = personService;
    }

    @Operation(summary = "Transfer money between accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Money transferred successfully"),
            @ApiResponse(responseCode = "400", description = "Failed to transfer money")
    })
    @PostMapping("/transfer")
    public ResponseEntity<String> transferMoney(@RequestBody TransferRequest transferRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Person person = (Person)authentication.getPrincipal();
        List<Account> personAccounts = personService.getPersonAccounts(person.getId());

        if(transferRequest.getFromAccountId() == null){
            transferRequest.setFromAccountId(personAccounts.get(0).getId());
        }
        if(personAccounts.stream().noneMatch(account -> account.getId().equals(transferRequest.getFromAccountId()))){
            return ResponseEntity.badRequest().body("Failed to transfer money");
        }

        boolean success = transferService.transferMoney(transferRequest.getFromAccountId(),
                transferRequest.getToAccountId(),
                transferRequest.getAmount());

        if (success) {
            return ResponseEntity.ok("Money transferred successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to transfer money");
        }
    }
}
