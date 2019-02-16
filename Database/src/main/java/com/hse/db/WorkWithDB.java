package com.hse.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkWithDB {
    private Connection connection;

    public WorkWithDB() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:database.db");
    }

    public void insert(String name, String phone) throws SQLException {
        if (isInjection(name) || isInjection(phone)) {
            throw new IllegalArgumentException("SQL-injection given");
        }

        String query = "INSERT INTO phonebook (name, phone) " +
                       "VALUES ('" + name + "', '" + phone + "')";
        executeUpdateQuery(query);
    }

    public List<String> findByName(String name) throws SQLException {
        if (isInjection(name)) {
            throw new IllegalArgumentException("SQL-injection given");
        }

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

    public List<String> findByPhone(String phone) throws SQLException {
        if (isInjection(phone)) {
            throw new IllegalArgumentException("SQL-injection given");
        }

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

    private boolean isInjection(String data) {
        return data.contains(")") && data.contains("'");
    }

    public void delete(String name, String phone) throws SQLException {
        if (isInjection(name) || isInjection(phone)) {
            throw new IllegalArgumentException("SQL-injection given");
        }

        String query = "DELETE FROM phonebook " +
                       "WHERE phone = '" + phone + "' AND name = '" + name + "';";
        executeUpdateQuery(query);
    }

    public void updateName(String name, String phone, String newName) throws SQLException {
        if (isInjection(name) || isInjection(phone)) {
            throw new IllegalArgumentException("SQL-injection given");
        }

        String query = "UPDATE phonebook SET " +
                       "name = '" + newName + "' " +
                       "WHERE phone = '" + phone + "' AND name = '" + name + "';";
        executeUpdateQuery(query);
    }

    public void updatePhone(String name, String phone, String newPhone) throws SQLException {
        if (isInjection(name) || isInjection(phone)) {
            throw new IllegalArgumentException("SQL-injection given");
        }

        String query = "UPDATE phonebook SET " +
                "phone = '" + newPhone + "' " +
                "WHERE phone = '" + phone + "' AND name = '" + name + "';";
        executeUpdateQuery(query);
    }

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

    public void clear() throws SQLException {
        String query = "DELETE FROM phonebook";
        executeUpdateQuery(query);
    }

    private void executeUpdateQuery(String query) throws SQLException {
        Statement statement = connection.createStatement();

        statement.executeUpdate(query);
        statement.close();
    }
}
