package me.tabinol.simple.database;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

@TestMethodOrder(OrderAnnotation.class)
final class SimpleDatabaseTest {

    private static final SimpleDatabase simpleDatabase = SimpleDatabase.newInstance();

    @Test
    @Order(1)
    void putGetDeleteTest() {
        simpleDatabase.put("example", "foo");
        assertEquals("foo", simpleDatabase.get("example"));
        simpleDatabase.delete("example");
        assertNull(simpleDatabase.get("example"));
        simpleDatabase.delete("example");
    }

    @Test
    @Order(2)
    void createTransactionTest() throws SimpleDatabaseException {
        simpleDatabase.createTransaction("abc");
        simpleDatabase.put("a", "foo", "abc");
        assertEquals("foo", simpleDatabase.get("a", "abc"));
        assertNull(simpleDatabase.get("a"));
    }

    @Test
    @Order(3)
    void commitTransactionTest() throws SimpleDatabaseException {
        simpleDatabase.createTransaction("xyz");
        simpleDatabase.put("a", "bar", "xyz");
        assertEquals("bar", simpleDatabase.get("a", "xyz"));
        simpleDatabase.commitTransaction("xyz");
        assertEquals("bar", simpleDatabase.get("a"));
    }

    @Test
    @Order(4)
    void commitWithPreviousValueMutatedAfterTest() {
        assertThrows(SimpleDatabaseException.class, () -> simpleDatabase.commitTransaction("abc"));
    }

    @Test
    @Order(5)
    void getFromCommittedTransactionTest() {
        assertEquals("bar", simpleDatabase.get("a"));
    }

    @Test
    @Order(6)
    void rollbackTransactionPutTest() throws SimpleDatabaseException {
        simpleDatabase.createTransaction("abc");
        simpleDatabase.put("a", "foo", "abc");
        assertEquals("bar", simpleDatabase.get("a"));
        simpleDatabase.rollbackTransaction("abc");
        assertThrows(SimpleDatabaseException.class, () -> simpleDatabase.put("a", "foo", "abc"));
        assertEquals("bar", simpleDatabase.get("a"));
    }

    @Test
    @Order(7)
    void rollbackTransactionGetTest() throws SimpleDatabaseException {
        simpleDatabase.createTransaction("def");
        simpleDatabase.put("b", "foo", "def");
        assertEquals("bar", simpleDatabase.get("a", "def"));
        assertEquals("foo", simpleDatabase.get("b", "def"));
        simpleDatabase.rollbackTransaction("def");
        assertNull(simpleDatabase.get("b"));
    }

    @Test
    @Order(8)
    void transactionDeleteTest() throws SimpleDatabaseException {
        simpleDatabase.createTransaction("hij");
        simpleDatabase.put("c", "hello", "hij");
        simpleDatabase.delete("c", "hij");
        assertNull(simpleDatabase.get("c", "hij"));
    }
}