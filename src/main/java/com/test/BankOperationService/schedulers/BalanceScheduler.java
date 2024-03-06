package com.test.BankOperationService.schedulers;

import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Profile("!test") // Активация только при отсутствии профиля "test"
public class BalanceScheduler {

    private final BalanceService balanceService;

    public BalanceScheduler(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @Scheduled(fixedRate = 60000) // Раз в минуту
    public void increaseBalances() {
        balanceService.increaseBalances();
    }
}

