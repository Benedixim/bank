package org.example.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankTest {

    static Bank bank1;
    static Bank bank2;

    @BeforeEach
    void prepareData(){
        bank1 = new Bank(1, "Clever-Bank", "Minsk");
        bank2 = new Bank(2, "Alfa-Bank", "Gomel");
    }

    @Test
    void getId() {
        assertEquals(1, bank1.getId());
    }

    @Test
    void getName() {
        assertEquals("Clever-Bank", bank1.getName());
    }

    @Test
    void getAddress() {
        assertEquals("Minsk", bank1.getAddress());
    }

    @Test
    void setId() {
        bank1.setId(2);
        assertEquals(2, bank1.getId());
    }

    @Test
    void setName() {
        bank1.setName("Belarus-Bank");
        assertEquals("Belarus-Bank", bank1.getName());
    }

    @Test
    void setAddress() {
        bank1.setAddress("Brest");
        assertEquals("Brest", bank1.getAddress());
    }

    @Test
    void testEquals() {
        Bank bank3 = new Bank(1, "Clever-Bank", "Minsk");

        assertEquals(bank1, bank3);

        assertEquals(bank1, bank1);

        assertNotEquals(bank1, bank2);

        Bank user5 = new Bank(1, "Sberbank", "Grodno");;
        assertNotEquals(bank1, user5);

        assertNotEquals("John", bank1);

        assertNotEquals(null, bank1); // Проверяем, что метод equals возвращает false для объекта null
    }

    @Test
    void canEqual() {
        assertTrue(bank1.canEqual(bank2));
    }

    @Test
    void testHashCode() {
        assertEquals(bank2.hashCode(), bank2.hashCode());
    }

    @Test
    void testToString() {
        String result = bank1.toString();
        assertEquals("Bank(id=1, name=Clever-Bank, address=Minsk)", result);
    }
}