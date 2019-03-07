package com.hse.db;

import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Stores methods to work with database */
public class WorkWithDB {
    @NotNull private Connection connection;
    @NotNull private String tableName = "phonebook";

    /**Uses sqlite as database*/
    public WorkWithDB(@NotNull String tableName) throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        this.tableName = tableName;
        connection = DriverManager.getConnection("jdbc:sqlite:database.db");
        String query = "CREATE TABLE IF NOT EXISTS " + tableName + " ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name VARCHAR(50), " +
                "phone VARCHAR(50));";

        try (var statement = connection.prepareStatement(query)) {
            statement.executeUpdate();
        }
    }

    /**Inserts new person to table with given name and phone*/
    public void insert(@NotNull String name, @NotNull String phone) throws SQLException {
        delete(name, phone);
        String query = "INSERT INTO " + tableName + " (name, phone) " +
                       "VALUES (?, ?);";

        try (var statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, phone);
            statement.executeUpdate();
        }
    }

    /**Finds all phones by given name*/
    public List<String> findByName(@NotNull String name) throws SQLException {
        String query = "SELECT phone FROM " + tableName +
                       " WHERE name = ?;";

        return launchFind(name, query);
    }

    /**Finds all names by given phone*/
    public List<String> findByPhone(@NotNull String phone) throws SQLException {
        String query = "SELECT name FROM " + tableName +
                " WHERE phone = ?;";

        return launchFind(phone, query);
    }

    /**Launches find query*/
    private List<String> launchFind(@NotNull String element, @NotNull String query) throws SQLException {
        try (var statement = connection.prepareStatement(query)) {
            statement.setString(1, element);

            var resultSet = statement.executeQuery();
            var list = new ArrayList<String>();

            while (resultSet.next()) {
                list.add(resultSet.getString(1));
            }

            return list;
        }
    }

    /**Delete all pairs (name, phone)*/
    public void delete(@NotNull String name, @NotNull String phone) throws SQLException {
        String query = "DELETE FROM " + tableName +
                       " WHERE phone = ? AND name = ?;";

        try (var statement = connection.prepareStatement(query)) {
            statement.setString(1, phone);
            statement.setString(2, name);
            statement.execute();
        }
    }

    /**Changes pairs (name, phone) on (newName, phone)*/
    public void updateName(@NotNull String name, @NotNull String phone,
                           @NotNull String newName) throws SQLException {
        String query = "UPDATE " + tableName + " SET " +
                       "name = ? " +
                       "WHERE phone = ? AND name = ?;";

        launchUpdate(name, phone, newName, query);
    }

    /**Changes pairs (name, phone) on (name, newPhone)*/
    public void updatePhone(@NotNull String name, @NotNull String phone,
                            @NotNull String newPhone) throws SQLException {
        String query = "UPDATE " + tableName + " SET " +
                "phone = ? " +
                "WHERE phone = ? AND name = ?;";

        launchUpdate(name, phone, newPhone, query);
    }

    /**Launches update query*/
    private void launchUpdate(@NotNull String name, @NotNull String phone, @NotNull String newElement,
                             @NotNull String query) throws SQLException {
        try (var statement = connection.prepareStatement(query)) {
            statement.setString(1, newElement);
            statement.setString(2, phone);
            statement.setString(3, name);
            statement.execute();
        }
    }

    /**Returns all records in database, which consists of name and phone*/
    public List<PhonebookNode> getAllRecords() throws SQLException {
        String query = "SELECT name, phone FROM " + tableName + ";";

        try (var statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            var list = new ArrayList<PhonebookNode>();

            while (resultSet.next()) {
                list.add(new PhonebookNode(
                        resultSet.getString("name"),
                        resultSet.getString("phone")));
            }

            statement.close();
            return list;
        }
    }

    /**Deletes all elements from phonebook*/
    public void clear() throws SQLException {
        String query = "DELETE FROM " + tableName + ";";
        try (var statement = connection.prepareStatement(query)) {
            statement.execute();
        }
    }

    /**Deletes table from database*/
    public void dropTable() throws SQLException {
        String query = "DROP TABLE " + tableName + ";";
        try (var statement = connection.prepareStatement(query)) {
            statement.execute();
        }
    }
}
