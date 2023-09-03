package org.example.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    static Account account1;
    static Account account2;

    @BeforeEach
    void prepareData() {
        account1 = new Account(1, "123456789", "BYN", 10, new Date(), new Date(), 1,1);
        account2 = new Account(2, "987654321", "USD", 20, new Date() ,new Date(), 2, 2);
    }

    @Test
    void getId() {
        assertEquals(1, account1.getId());
    }

    @Test
    void getName() {
        assertEquals("123456789", account1.getName());
    }

    @Test
    void getCurrency() {
        assertEquals("BYN", account1.getCurrency());
    }

    @Test
    void getBalance() {
        assertEquals(10, account1.getBalance());
    }

    @Test
    void getOpeningDate() {
        assertEquals(new Date(), account1.getOpeningDate());
    }

    @Test
    void getLastInterestDate() {
        assertEquals(new Date(), account1.getLastInterestDate());
    }

    @Test
    void getBank_id() {
        assertEquals(1, account1.getBank_id());
    }

    @Test
    void getUser_id() {
        assertEquals(1, account1.getUser_id());
    }

    @Test
    void setId() {
        account1.setId(2);
        assertEquals(2, account1.getId());
    }

    @Test
    void setName() {
        account1.setName("135792468");
        assertEquals("135792468", account1.getName());
    }

    @Test
    void setCurrency() {
        account1.setCurrency("EUR");
        assertEquals("EUR", account1.getCurrency());
    }

    @Test
    void setBalance() {
        account1.setBalance(44);
        assertEquals(44, account1.getBalance());
    }

    @Test
    void setOpeningDate() {
        account1.setOpeningDate(new Date(2000, Calendar.FEBRUARY,8));
        assertEquals(new Date(2000, Calendar.FEBRUARY,8), account1.getOpeningDate());
    }

    @Test
    void setLastInterestDate() {
        account1.setLastInterestDate(new Date(1999, Calendar.MARCH,3));
        assertEquals(new Date(1999, Calendar.MARCH,3), account1.getLastInterestDate());
    }

    @Test
    void setBank_id() {
        account1.setBank_id(44);
        assertEquals(44, account1.getBank_id());
    }

    @Test
    void setUser_id() {
        account1.setUser_id(56);
        assertEquals(56, account1.getUser_id());
    }

    @Test
    void testEquals() {
        Account account3 = new Account(1, "123456789", "BYN", 10, new Date(), new Date(), 1,1);
        assertEquals(account1, account3);

        assertEquals(account1, account1);

        assertNotEquals(account1, account2);

        Account account5 = new Account(3, "0000004321", "RUB", 120, new Date(2010) ,new Date(), 5, 5);;
        assertNotEquals(account1, account5);

        assertNotEquals("John", account1);

        assertNotEquals(null, account1); // Проверяем, что метод equals возвращает false для объекта null

    }

    @Test
    void canEqual() {
        assertTrue(account1.canEqual(account2));
    }

    @Test
    void testHashCode() {
        assertEquals(account1.hashCode(), account1.hashCode());
    }

    @Test
    void testToString() {
        String result = account1.toString();
        assertEquals("Account(id=1, name=123456789, currency=BYN, balance=10.0, openingDate=" + account1.getOpeningDate() + ", lastInterestDate=" + account1.getLastInterestDate() + ", bank_id=1, user_id=1)", result);
    }
}