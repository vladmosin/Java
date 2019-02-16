package com.hse.db;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class WorkWithDBTest {
    private WorkWithDB dbManager = new WorkWithDB();

    WorkWithDBTest() throws SQLException, ClassNotFoundException {}

    @BeforeEach
    void clear() throws SQLException {
        dbManager.clear();
    }

    @Test
    void testInsert() throws SQLException {
        dbManager.insert("cat", "235");
        dbManager.insert("dog", "239042");
        dbManager.insert("elephant", "23420");
        dbManager.insert("mouse", "0238985");

        var list = dbManager.getAllRecords();

        assertEquals(4, list.size());
        assertTrue(list.contains(new PhonebookNode("cat", "235")));
        assertTrue(list.contains(new PhonebookNode("dog", "239042")));
        assertTrue(list.contains(new PhonebookNode("elephant", "23420")));
        assertTrue(list.contains(new PhonebookNode("mouse", "0238985")));
    }

    @Test
    void testDelete() throws SQLException {
        dbManager.insert("cat", "93284");
        dbManager.insert("dog", "32094");
        dbManager.delete("cat", "93284");

        var list = dbManager.getAllRecords();

        assertEquals(1, list.size());
        assertTrue(list.contains(new PhonebookNode("dog", "32094")));
        dbManager.delete("dog", "32094");
        assertEquals(0, dbManager.getAllRecords().size());
    }

    @Test
    void testfindByName() throws SQLException {
        dbManager.insert("cat", "93284");
        dbManager.insert("cat", "093");
        dbManager.insert("cat", "mkfwe32");

        var list = dbManager.findByName("cat");

        assertEquals(3, list.size());
        assertTrue(list.contains("93284"));
        assertTrue(list.contains("093"));
        assertTrue(list.contains("mkfwe32"));

        dbManager.delete("cat", "093");

        list = dbManager.findByName("cat");

        assertEquals(2, list.size());
        assertTrue(list.contains("93284"));
        assertTrue(list.contains("mkfwe32"));
    }

    @Test
    void testFindByPhone() throws SQLException {
        dbManager.insert("cat", "4");
        dbManager.insert("dog", "4");
        dbManager.insert("mouse", "4");

        var list = dbManager.findByPhone("4");

        assertEquals(3, list.size());
        assertTrue(list.contains("cat"));
        assertTrue(list.contains("dog"));
        assertTrue(list.contains("mouse"));
    }

    @Test
    void testUpdateName() throws SQLException {
        dbManager.insert("cat", "3421");
        dbManager.updateName("cat", "3421", "dog");

        assertEquals(0, dbManager.findByName("cat").size());
        assertEquals(1, dbManager.findByName("dog").size());

        dbManager.insert("dog", "3421");
        dbManager.updateName("dog", "3421", "mouse");

        var list = dbManager.getAllRecords();
        var node = new PhonebookNode("mouse", "3421");

        assertEquals(2, list.size());

        assertEquals(node, list.get(0));
        assertEquals(node, list.get(1));
    }

    @Test
    void testUpdatePhone() throws SQLException {
        dbManager.insert("first", "3");
        dbManager.insert("second", "3");
        dbManager.updatePhone("first", "3", "4");

        var list = dbManager.getAllRecords();

        assertTrue(list.contains(new PhonebookNode("second", "3")));
        assertTrue(list.contains(new PhonebookNode("first", "4")));
        assertFalse(list.contains(new PhonebookNode("first", "3")));
    }

    @Test
    void testEmptyDelete() throws SQLException {
        dbManager.delete("a", "1");
        dbManager.insert("a", "1");
        dbManager.delete("1", "2");
        assertEquals(1, dbManager.getAllRecords().size());
    }

    @Test
    void testNonexistentUpdateName() throws SQLException {
        dbManager.insert("a", "1");
        dbManager.updateName("1", "2", "a");
        assertEquals(1, dbManager.getAllRecords().size());
        assertEquals(new PhonebookNode("a", "1"),
                dbManager.getAllRecords().get(0));
    }

    @Test
    void testNonexistentUpdatePhone() throws SQLException {
        dbManager.insert("a", "1");
        dbManager.updatePhone("1", "2", "a");
        assertEquals(1, dbManager.getAllRecords().size());
        assertEquals(new PhonebookNode("a", "1"),
                dbManager.getAllRecords().get(0));
    }
}