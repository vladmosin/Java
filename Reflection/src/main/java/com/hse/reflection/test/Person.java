package com.hse.reflection.test;

import java.util.List;

public class Person {
    public static float planet;
    private int age;
    protected final String name = null;

    public Person() {}

    protected Person(float planet) {}

    private static void someMethod(List<Object> list) {}

    public int methodWithReturnValue() {
        return 123;
    }
}
