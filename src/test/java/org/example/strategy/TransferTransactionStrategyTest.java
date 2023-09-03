package org.example.strategy;

import org.example.models.Account;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransferTransactionStrategyTest {

    @Test
    public void testExecute() {
        Account account1 = new Account();
        account1.setId(1);
        account1.setBalance(1000);
        account1.setCurrency("BYN");

        Account account2 = new Account();
        account2.setId(2);
        account2.setBalance(500);
        account2.setCurrency("BYN");

        double amount = 200;
        String currency = "BYN";

        TransferTransactionStrategy strategy = new TransferTransactionStrategy(account1, account2, amount, currency);

        boolean result = strategy.execute();

        assertTrue(result);
        assertEquals(800, account1.getBalance(), 0.001);
        assertEquals(700, account2.getBalance(), 0.001);
    }
}