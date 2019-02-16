package com.hse.db;

public class PhonebookNode {
    private String phone;
    private String name;

    public PhonebookNode(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return name + " | " + phone;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof PhonebookNode) {
            return ((PhonebookNode)other).phone.equals(phone) &&
                   ((PhonebookNode)other).name.equals(name);
        } else {
            throw new IllegalArgumentException("other is not PhonebookNode");
        }
    }
}
