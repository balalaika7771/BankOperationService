package com.test.BankOperationService.schedulers;

import com.test.BankOperationService.model.Account.Account;
import com.test.BankOperationService.model.Account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BalanceService {

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public void increaseBalances() {
        List<Account> accounts = accountRepository.findAll();
        for (Account account : accounts) {
            BigDecimal currentBalance = new BigDecimal(account.getCurrentBalance());
            BigDecimal newBalance = currentBalance.multiply(BigDecimal.valueOf(1.05));
            BigDecimal maxBalance = BigDecimal.valueOf(account.getInitialDeposit()).multiply(BigDecimal.valueOf(2.07));
            if (newBalance.compareTo(maxBalance) <= 0) {
                account.setCurrentBalance(newBalance.doubleValue());
                accountRepository.save(account);
            }
        }
    }
}
