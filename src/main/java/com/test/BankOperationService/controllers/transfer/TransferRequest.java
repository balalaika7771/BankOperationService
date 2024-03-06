package com.test.BankOperationService.controllers.transfer;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@Builder
public class TransferRequest {

    @Positive
    private BigDecimal Amount;

    @NotEmpty
    private Long ToAccountId;

    private Long FromAccountId = null;
}
