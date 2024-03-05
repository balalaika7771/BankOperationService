package com.test.BankOperationService.controllers.personController;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {

    private Long number;

    private double initialDeposit;

    private double currentBalance;
}
