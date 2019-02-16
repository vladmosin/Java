package com.hse.db;

public class PhonebookNode {
    private String phone;
    private String name;

    public PhonebookNode(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " | " + phone;
    }
}
