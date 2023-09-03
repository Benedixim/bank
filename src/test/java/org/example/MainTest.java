package org.example;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {

    @Test
    void getAmount() {
        String input = "123,45\n";
        Scanner scanner = new Scanner(input);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        double amount = Main.getAmount(scanner);

        assertEquals(123.45, amount);
    }

    @Test
    public void testGetCurrency() {
        String input = "USD\n";
        Scanner scanner = new Scanner(input);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String currency = Main.getCurrency(scanner);
        assertEquals("USD", currency);
    }

    @Test
    void getInt() {
        String input = "1\n";
        Scanner scanner = new Scanner(input);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        double amount = Main.getInt(scanner, 5);

        assertEquals(1, amount);
    }
}