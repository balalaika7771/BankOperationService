package com.test.BankOperationService.controllers.transfer;

import com.test.BankOperationService.model.Account.Account;
import com.test.BankOperationService.model.Account.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferService {

    private final AccountRepository accountRepository;

    public TransferService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public boolean transferMoney(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        Account fromAccount = accountRepository.findById(fromAccountId).orElse(null);
        Account toAccount = accountRepository.findById(toAccountId).orElse(null);

        // Проверяем наличие обоих счетов и достаточность средств для перевода
        if (fromAccount == null || toAccount == null || new BigDecimal(fromAccount.getCurrentBalance()).compareTo(amount) < 0) {
            return false;
        }

        // Выполняем перевод
        fromAccount.setCurrentBalance(new BigDecimal(fromAccount.getCurrentBalance()).subtract(amount).doubleValue());
        toAccount.setCurrentBalance(new BigDecimal(toAccount.getCurrentBalance()).add(amount).doubleValue());

        // Сохраняем изменения
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        return true;
    }
}

