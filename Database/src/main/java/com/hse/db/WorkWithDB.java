package com.hse.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Stores methods to work with database */
public class WorkWithDB {
    private Connection connection;

    /**Uses sqlite as database*/
    public WorkWithDB() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:database.db");
    }

    public void insert(String name, String phone) throws SQLException {
        String query = "INSERT INTO phonebook (name, phone) " +
                       "VALUES ('" + name + "', '" + phone + "')";
        executeUpdateQuery(query);
    }

    /**Finds all phones by given name*/
    public List<String> findByName(String name) throws SQLException {
        String query = "SELECT phone FROM phonebook " +
                       "WHERE name = '" + name + "';";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        var list = new ArrayList<String>();

        while (resultSet.next()) {
            list.add(resultSet.getString("phone"));
        }

        statement.close();
        return list;
    }

    /**Finds all names by given phone*/
    public List<String> findByPhone(String phone) throws SQLException {
        String query = "SELECT name FROM phonebook " +
                "WHERE phone = '" + phone + "';";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        var list = new ArrayList<String>();

        while (resultSet.next()) {
            list.add(resultSet.getString("name"));
        }

        statement.close();
        return list;
    }

    /**Delete all pairs (name, phone)*/
    public void delete(String name, String phone) throws SQLException {
        String query = "DELETE FROM phonebook " +
                       "WHERE phone = '" + phone + "' AND name = '" + name + "';";
        executeUpdateQuery(query);
    }

    /**Changes pairs (name, phone) on (newName, phone)*/
    public void updateName(String name, String phone, String newName) throws SQLException {
        String query = "UPDATE phonebook SET " +
                       "name = '" + newName + "' " +
                       "WHERE phone = '" + phone + "' AND name = '" + name + "';";
        executeUpdateQuery(query);
    }

    /**Changes pairs (name, phone) on (name, newPhone)*/
    public void updatePhone(String name, String phone, String newPhone) throws SQLException {
        String query = "UPDATE phonebook SET " +
                "phone = '" + newPhone + "' " +
                "WHERE phone = '" + phone + "' AND name = '" + name + "';";
        executeUpdateQuery(query);
    }

    /**Returns all records in database, which consists of name and phone*/
    public List<PhonebookNode> getAllRecords() throws SQLException {
        String query = "SELECT name, phone FROM phonebook;";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        var list = new ArrayList<PhonebookNode>();

        while (resultSet.next()) {
            list.add(new PhonebookNode(
                    resultSet.getString("name"),
                    resultSet.getString("phone")));
        }

        statement.close();
        return list;
    }

    /**Deletes all elements from phonebook*/
    public void clear() throws SQLException {
        String query = "DELETE FROM phonebook";
        executeUpdateQuery(query);
    }

    /**Execute given query*/
    private void executeUpdateQuery(String query) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        statement.execute();
        statement.close();
    }
}
