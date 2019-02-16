package com.hse.db;

import java.sql.*;

public class WorkWithDB {
    private Connection connection;

    public WorkWithDB(String databaseName) throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + databaseName);
    }

    public void dropTable(String tableName) throws SQLException {
        String query = "DROP TABLE " + tableName + ";";
        Statement statement = connection.createStatement();

        statement.executeUpdate(query);
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        var work = new WorkWithDB("database.db");
        work.dropTable("db");
    }
}
