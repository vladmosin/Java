package com.hse.db;

import java.sql.SQLException;
import java.util.Scanner;

/**Interactive program to work with database*/
public class Phonebook {
    private static void outputHelp() {
        System.out.println("Help menu");
        System.out.println("0 - exit");
        System.out.println("1 - insert");
        System.out.println("2 - find people by phone");
        System.out.println("3 - find phones by person");
        System.out.println("4 - delete record");
        System.out.println("5 - change name in record");
        System.out.println("6 - change phone in record");
        System.out.println("7 - get all records");
        System.out.println();
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Scanner in = new Scanner(System.in);
        var dbManager = new WorkWithDB("phonebook");

        outputHelp();

        while (true) {
            int command;

            System.out.print("Input command (0 - 7): ");
            command = in.nextInt();
            if (command == 0) {
                break;
            } else if (command == 1) {
                System.out.print("Input name: ");
                String name = in.next();

                System.out.print("Input phone: ");
                String phone = in.next();

                dbManager.insert(name, phone);
            } else if (command == 2) {
                System.out.print("Input phone: ");
                String phone = in.next();

                dbManager.findByPhone(phone).forEach(System.out::println);
            } else if (command == 3) {
                System.out.print("Input name: ");
                String name = in.next();

                dbManager.findByName(name).forEach(System.out::println);
            } else if (command == 4) {
                System.out.print("Input name: ");
                String name = in.next();

                System.out.print("Input phone: ");
                String phone = in.next();

                dbManager.delete(name, phone);
            } else if (command == 5) {
                System.out.print("Input name: ");
                String name = in.next();

                System.out.print("Input phone: ");
                String phone = in.next();

                System.out.print("Input new name: ");
                String newName = in.next();

                dbManager.updateName(name, phone, newName);
            } else if (command == 6) {
                System.out.print("Input name: ");
                String name = in.next();

                System.out.print("Input phone: ");
                String phone = in.next();

                System.out.print("Input new phone: ");
                String newPhone = in.next();

                dbManager.updateName(name, phone, newPhone);
            } else if (command == 7) {
                dbManager.getAllRecords().forEach(e -> System.out.println(e.toString()));
            }
        }
    }
}
