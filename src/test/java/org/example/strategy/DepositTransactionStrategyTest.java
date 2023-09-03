package org.example.strategy;

import org.example.models.Account;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DepositTransactionStrategyTest {

    @Test
    void execute() {
        Account account1 = new Account();
        account1.setId(1);
        account1.setBalance(1000);
        account1.setCurrency("BYN");

        double amount = 200;
        String currency = "BYN";

        DepositTransactionStrategy strategy = new DepositTransactionStrategy(account1, amount, currency);

        boolean result = strategy.execute();

        assertTrue(result);
        assertEquals(1200, account1.getBalance(), 0.001);
    }
}