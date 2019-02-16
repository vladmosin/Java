package com.hse.db;

import java.sql.SQLException;
import java.util.Scanner;

public class Phonebook {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        int command;
        Scanner in = new Scanner(System.in);
        var dbManager = new WorkWithDB();

        while (true) {
            command = in.nextInt();

            if (command == 0) {
                break;
            } else if (command == 1) {
                String name = in.next();
                String phone = in.next();

                dbManager.insert(name, phone);
            } else if (command == 2) {
                String phone = in.next();

                dbManager.findByPhone(phone).forEach(System.out::println);
            } else if (command == 3) {
                String name = in.next();

                dbManager.findByName(name).forEach(System.out::println);
            } else if (command == 4) {
                String name = in.next();
                String phone = in.next();

                dbManager.delete(name, phone);
            } else if (command == 5) {
                String name = in.next();
                String phone = in.next();
                String newName = in.next();

                dbManager.updateName(name, phone, newName);
            } else if (command == 6) {
                String name = in.next();
                String phone = in.next();
                String newPhone = in.next();

                dbManager.updateName(name, phone, newPhone);
            } else if (command == 7) {
                dbManager.getAllRecords().forEach(e -> System.out.println(e.toString()));
            }
        }
    }
}
