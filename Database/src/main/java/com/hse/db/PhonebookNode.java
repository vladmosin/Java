package com.hse.db;

import org.jetbrains.annotations.NotNull;

/**Stores phone and name*/
public class PhonebookNode {
    @NotNull private final String phone;
    @NotNull private final String name;

    public PhonebookNode(@NotNull String name, @NotNull String phone) {
        this.name = name;
        this.phone = phone;
    }

    @Override
    @NotNull
    public String toString() {
        return name + " | " + phone;
    }

    @Override
    public boolean equals(@NotNull Object other) {
        if (other instanceof PhonebookNode) {
            return ((PhonebookNode)other).phone.equals(phone) &&
                   ((PhonebookNode)other).name.equals(name);
        } else {
            throw new IllegalArgumentException("other is not PhonebookNode");
        }
    }
}
