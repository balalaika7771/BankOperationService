package com.test.BankOperationService.controllers.transfer;

import com.test.BankOperationService.controllers.search.SearchController;
import com.test.BankOperationService.model.Account.Account;
import com.test.BankOperationService.model.Account.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferService {

    private static final Logger logger = LoggerFactory.getLogger(TransferService.class);
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
            logger.warn("transfer ERROR- fromAccount:"+fromAccountId+", toAccount:"+toAccountId);
            return false;
        }

        // Выполняем перевод
        fromAccount.setCurrentBalance(new BigDecimal(fromAccount.getCurrentBalance()).subtract(amount).doubleValue());
        toAccount.setCurrentBalance(new BigDecimal(toAccount.getCurrentBalance()).add(amount).doubleValue());

        // Сохраняем изменения
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        logger.info("transfer ok- fromAccount:"+fromAccountId+", toAccount:"+toAccountId);
        return true;
    }
}

