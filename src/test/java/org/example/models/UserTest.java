package org.example.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    static User user1;
    static User user2;

    @BeforeEach
    void prepareData() {
        user1 = new User(1, "Alex");
        user2 = new User(2, "Bob");
    }

    @Test
    void getId() {
        assertEquals(1, user1.getId());
    }

    @Test
    void getName() {
        assertEquals("Alex", user1.getName());
    }

    @Test
    void setId() {
        user1.setId(2);
        assertEquals(2, user1.getId());
    }

    @Test
    void setName() {
        user1.setName("Bob");
        assertEquals("Bob", user1.getName());
    }

    @Test
    void testEquals() {
        User user3 = new User(1, "Alex");

        assertEquals(user1, user3);

        assertEquals(user1, user1);

        assertNotEquals(user1, user2);

        User user5 = new User(1, "Jane");
        assertNotEquals(user1, user5);

        assertNotEquals("John", user1);

        assertNotEquals(null, user1); // Проверяем, что метод equals возвращает false для объекта null
    }

    @Test
    void canEqual() {
        assertTrue(user1.canEqual(user2));
    }


    @Test
    void testHashCode() {
        assertEquals(user1.hashCode(), user1.hashCode());
    }

    @Test
    void testToString() {
        String result = user1.toString();
        assertEquals("User(id=1, name=Alex)", result);
    }
}