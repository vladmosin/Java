package com.hse.reflection;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReflectorTest {
    @Test
    public void firstTest() throws FileNotFoundException {
        Reflector.printStructure(com.hse.reflection.test.OneFieldClass.class);
    }

    @Test
    public void secondTest() throws FileNotFoundException {
        Reflector.printStructure(com.hse.reflection.test.Person.class);
    }
}