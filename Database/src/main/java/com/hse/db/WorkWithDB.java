package com.hse.db;

import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Stores methods to work with database */
public class WorkWithDB {
    @NotNull private Connection connection;

    /**Uses sqlite as database*/
    public WorkWithDB(@NotNull String databaseName) throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite: " + databaseName + ".db");
        try (var statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS People ( " +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name VARCHAR(50), " +
                    "UNIQUE (name));");
            statement.execute("CREATE TABLE IF NOT EXISTS Phones ( " +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "phone VARCHAR(50), " +
                    "UNIQUE (phone));");
            statement.execute("CREATE TABLE IF NOT EXISTS PeoplePhones ("
                    + "personId INTEGER,"
                    + "phoneId INTEGER,"
                    + "FOREIGN KEY(personId) REFERENCES People (id),"
                    + "FOREIGN KEY(phoneId) REFERENCES Phones (id),"
                    + "UNIQUE(personId, phoneId) ON CONFLICT REPLACE"
                    + ");");
        }
    }

    /**Inserts new person to table with given name and phone*/
    public void insert(@NotNull String name, @NotNull String phone) throws SQLException {
        insertName(name);
        insertPhone(phone);

        String query = "INSERT OR IGNORE INTO PeoplePhones(personId, phoneId)"
                     + "VALUES ((SELECT id FROM People WHERE name = ?),"
                     + " (SELECT id FROM Phones WHERE phone = ?));";

        try (var statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, phone);
            statement.executeUpdate();
        }
    }

    /**Inserts person into People if it does not exist*/
    private void insertName(@NotNull String name) throws SQLException {
            String query = "INSERT OR IGNORE INTO People(name) VALUES(?);";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, name);
                statement.executeUpdate();
        }
    }

    /**Inserts phone into Phones if it does not exist*/
    private void insertPhone(@NotNull String phone) throws SQLException {
        String query = "INSERT OR IGNORE INTO Phones(phone) VALUES(?);";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, phone);
            statement.executeUpdate();
        }
    }

    /**Finds all phones by given name*/
    public List<String> findByName(@NotNull String name) throws SQLException {
        String query = "SELECT Phones.phone FROM Phones, People, PeoplePhones "
                + "WHERE Phones.id = PeoplePhones.phoneId "
                + "AND People.id = PeoplePhones.personId "
                + "AND People.name = ?;";

        return launchFind(name, query);
    }

    /**Finds all names by given phone*/
    public List<String> findByPhone(@NotNull String phone) throws SQLException {
        String query = "SELECT People.name FROM Phones, People, PeoplePhones "
                + "WHERE Phones.id = PeoplePhones.phoneId "
                + "AND People.id = PeoplePhones.personId "
                + "AND Phones.phone = ?;";

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
        String query = "DELETE FROM PeoplePhones "
                     + "WHERE personId = (SELECT id FROM People WHERE name = ?) "
                     + "AND phoneId = (SELECT id FROM Phones WHERE phone = ?);";

        try (var statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, phone);
            statement.execute();
        }
    }

    /**Changes pairs (name, phone) on (newName, phone)*/
    public void updateName(@NotNull String name, @NotNull String phone,
                           @NotNull String newName) throws SQLException {
        insertName(newName);
        String query = "UPDATE PeoplePhones "
                + "SET personId = (SELECT id FROM People WHERE name = ?) "
                + "WHERE personId = "
                + "(SELECT id FROM People WHERE name = ?) "
                + "AND phoneId = "
                + "(SELECT id FROM Phones WHERE phone = ?);";

        launchUpdate(name, phone, newName, query);
    }

    /**Changes pairs (name, phone) on (name, newPhone)*/
    public void updatePhone(@NotNull String name, @NotNull String phone,
                            @NotNull String newPhone) throws SQLException {
        insertPhone(newPhone);
        String query = "UPDATE PeoplePhones "
                + "SET phoneId = (SELECT id FROM Phones WHERE phone = ?) "
                + "WHERE personId = "
                + "(SELECT id FROM People WHERE name = ?) "
                + "AND phoneId = "
                + "(SELECT id FROM Phones WHERE phone = ?);";

        launchUpdate(name, phone, newPhone, query);
    }

    /**Launches update query*/
    private void launchUpdate(@NotNull String name, @NotNull String phone, @NotNull String newElement,
                             @NotNull String query) throws SQLException {
        try (var statement = connection.prepareStatement(query)) {
            statement.setString(1, newElement);
            statement.setString(2, name);
            statement.setString(3, phone);
            statement.execute();
        }
    }

    /**Returns all records in database, which consists of name and phone*/
    public List<PhonebookNode> getAllRecords() throws SQLException {
        String query = "SELECT People.name, Phones.phone FROM Phones, People, PeoplePhones "
                + "WHERE Phones.id = PeoplePhones.phoneId "
                + "AND People.id = PeoplePhones.personId;";

        try (var statement = connection.createStatement()) {
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
    }

    /**Deletes all elements from phonebook*/
    public void clear() throws SQLException {
        try (var statement = connection.createStatement()) {
            statement.execute("DELETE FROM People;");
            statement.execute("DELETE FROM Phones;");
            statement.execute("DELETE FROM PeoplePhones;");
        }
    }

    /**Deletes table from database*/
    public void dropTable() throws SQLException {
        try (var statement = connection.createStatement()) {
            statement.execute("DROP TABLE People;");
            statement.execute("DROP TABLE Phones;");
            statement.execute("DROP TABLE PeoplePhones;");
        }
    }
}
