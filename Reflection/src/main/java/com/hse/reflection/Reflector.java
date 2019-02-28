package com.hse.reflection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Reflector {
    private static final int SPACES_PER_TAB = 4;

    private enum Definition { YES, NO }

    private static class Template {
        private String prefix;
        private String suffix;
        private String separator;

        private Template(String prefix, String suffix, String separator) {
            this.prefix = prefix;
            this.suffix = suffix;
            this.separator = separator;
        }
    }

    public static void diffClasses(Class<?> first, Class<?> second) {
        var differentMethods = getDifferentMethods(first, second);
        var differentFields = getDifferentFields(first, second);

        try (var out = new PrintWriter(System.out)) {
            printMethods(differentMethods, out, 0);
            printFields(differentFields, out, 0);
        }
    }

    private static Method[] getDifferentMethods(Class<?> first, Class<?> second) {
        var firstMethods = first.getDeclaredMethods();
        var secondMethods = second.getDeclaredMethods();

        return getDifferentMethods(firstMethods, secondMethods);
    }

    private static Method[] getDifferentMethods(Method[] firstMethods, Method[] secondMethods) {
        return Stream.concat(
                Arrays.stream(firstMethods)
                      .filter(method -> containsMethod(secondMethods, method)),
                Arrays.stream(firstMethods)
                      .filter(method -> containsMethod(secondMethods, method)))
                .toArray(Method[]::new);
    }

    private static boolean containsMethod(Method[] methods, Method givenMethod) {
        for (var method : methods) {
            if (methodsEqual(method, givenMethod)) {
                return true;
            }
        }

        return false;
    }

    private static boolean methodsEqual(Method first, Method second) {
        return first.getName().equals(second.getName()) &&
               first.getGenericReturnType().equals(second.getGenericReturnType()) &&
               first.getModifiers() == second.getModifiers() &&
               Arrays.equals(first.getParameterTypes(), second.getParameterTypes());
    }

    private static Field[] getDifferentFields(Class<?> first, Class<?> second) {
        var firstFields = first.getDeclaredFields();
        var secondFields = second.getDeclaredFields();

        return getDifferentFields(firstFields, secondFields);
    }

    private static Field[] getDifferentFields(Field[] firstFields, Field[] secondFields) {
        return Stream.concat(
                Arrays.stream(firstFields)
                        .filter(field -> containsField(secondFields, field)),
                Arrays.stream(firstFields)
                        .filter(field -> containsField(secondFields, field)))
                .toArray(Field[]::new);
    }

    private static boolean containsField(Field[] fields, Field givenField) {
        for (var field : fields) {
            if (fieldsEqual(field, givenField)) {
                return true;
            }
        }

        return false;
    }

    private static boolean fieldsEqual(Field first, Field second) {
        return first.getName().equals(second.getName()) &&
               first.getGenericType().equals(second.getGenericType()) &&
               first.getModifiers() == second.getModifiers();
    }

    public static void printStructure(Class<?> clazz) throws FileNotFoundException {
        String className = clazz.getSimpleName();

        try (var out = new PrintWriter(new File(className + ".java"))) {
            out.println("package " + clazz.getPackageName() + ";");
            out.println();
            printFullClass(clazz, out, 0);
        }
    }

    private static void printFullClass(Class<?> clazz, PrintWriter out, int tabs) {
        printTabs(tabs, out);
        out.print(Modifier.toString(clazz.getModifiers()) + " " + clazz.getName());
        printDependencies(clazz.getTypeParameters(), out,
                new Template("<", ">", ", "), Definition.NO);
        if (clazz.getGenericSuperclass() != null) {
            out.print(" extends ");
            printDependency(clazz.getGenericSuperclass(), out, Definition.NO);
        }

        if (clazz.getGenericInterfaces().length > 0) {
            printDependencies(clazz.getGenericInterfaces(), out,
                    new Template(" implements ", "", " & "), Definition.NO);
        }

        out.print(" {\n");
        for (var classes : clazz.getDeclaredClasses()) {
            printFullClass(clazz, out, tabs + 1);
        }

        printConstructors(clazz.getDeclaredConstructors(), out, clazz.getSimpleName(), tabs + 1);
        printFields(clazz.getDeclaredFields(), out, tabs + 1);
        printMethods(clazz.getDeclaredMethods(), out, tabs + 1);
    }

    private static void printDependency(Type type, PrintWriter out, Definition isDefinition) {
        if (type instanceof Class) {
            printInstanceOfClass((Class<?>) type, out);
        } else if (type instanceof WildcardType) {
            printInstanceOfWildCardType((WildcardType) type, out);
        } else if (type instanceof ParameterizedType) {
            printInstanceOfParameterizedType((ParameterizedType) type, out);
        } else if (type instanceof GenericArrayType) {
            printInstanceOfGenericArrayType((GenericArrayType) type, isDefinition, out);
        } else if (type instanceof TypeVariable) {
            printInstanceOfTypeVariable((TypeVariable<?>) type, isDefinition, out);
        }
    }

    private static void printInstanceOfClass(Class<?> clazz, PrintWriter out) {
        out.print(clazz.getName());
    }

    private static void printInstanceOfWildCardType(WildcardType wildcardType, PrintWriter out) {
        out.print("? ");
        printDependencies(wildcardType.getLowerBounds(), out,
                new Template("super ", "", " & "), Definition.NO);
        printDependencies(wildcardType.getUpperBounds(), out,
                new Template("extends ", "", " & "), Definition.NO);
    }

    private static void printInstanceOfParameterizedType(ParameterizedType parameterizedType, PrintWriter out) {
        printDependency(parameterizedType.getRawType(), out, Definition.NO);
        printDependencies(parameterizedType.getActualTypeArguments(), out,
                new Template("<", ">", ","), Definition.NO);
    }

    private static void printInstanceOfGenericArrayType(GenericArrayType genericArrayType,
                                                        Definition isDefinition, PrintWriter out) {
        printDependency(genericArrayType.getGenericComponentType(), out, isDefinition);
        out.print("[]");
    }

    private static void printInstanceOfTypeVariable(TypeVariable<?> typeVariable,
                                                    Definition isDefinition, PrintWriter out) {
        out.print(typeVariable.getName());
        if (isDefinition == Definition.YES) {
            printDependencies(typeVariable.getBounds(), out,
                    new Template("extends ", "", " & "), Definition.NO);
        }
    }

    private static void printDependencies(Type[] types, PrintWriter out, Template template, Definition isDefinition) {
        if (types.length == 0) {
            return;
        }

        boolean wasPrinted = false;

        out.print(template.prefix);
        for (var type : types) {
            if (wasPrinted) {
                out.print(template.separator);
            }

            wasPrinted = true;
            printDependency(type, out, isDefinition);
        }

        out.print(template.suffix);
    }

    private static void printConstructor(Constructor constructor, PrintWriter out, String className) {
        out.print(Modifier.toString(constructor.getModifiers()) + " ");
        printDependencies(constructor.getTypeParameters(), out,
                new Template("<", "> ", ", "), Definition.YES);
        out.print(className + " (");
        printArguments(constructor.getGenericParameterTypes(), out);
        out.print(") ");
        printDependencies(constructor.getGenericExceptionTypes(), out,
                new Template("throws ", "", ", "), Definition.NO);
        printMethodBody(out);
    }

    private static void printConstructors(Constructor[] constructors, PrintWriter out, String className, int tabs) {
        for (var constructor : constructors) {
            printTabs(tabs, out);
            printConstructor(constructor, out, className);
            out.print("\n");
        }
    }

    private static void printFields(Field[] fields, PrintWriter out, int tabs) {
        for (var field : fields) {
            printTabs(tabs, out);
            printField(field, out);
            out.print("\n");
        }
    }

    private static void printField(Field field, PrintWriter out) {
        out.print(Modifier.toString(field.getModifiers()) + " ");
        printDependency(field.getGenericType(), out, Definition.NO);
        out.print(" " + field.getName());
        if (Modifier.isFinal(field.getModifiers())) {
            out.print(" = ");
            printDefaultValue(field.getGenericType(), out);
        }

        out.print(";\n");
    }

    private static void printMethods(Method[] methods, PrintWriter out, int tabs) {
        for (var method : methods) {
            printTabs(tabs, out);
            printMethod(method, out);
            out.print("\n");
        }
    }

    private static void printMethod(Method method, PrintWriter out) {
        out.print(Modifier.toString(method.getModifiers()) + " ");
        printDependencies(method.getTypeParameters(), out,
                new Template("<", "> ", ", "), Definition.NO);
        printDependency(method.getReturnType(), out, Definition.NO);
        out.print(" " + method.getName());
        out.print(" ( ");
        printArguments(method.getGenericParameterTypes(), out);
        out.print(" ) ");
        printMethodBody(out);
    }

    private static void printTabs(int tabs, PrintWriter out) {
        for (int i = 0; i < tabs; i++) {
            for (int j = 0; j < SPACES_PER_TAB; j++) {
                out.print(" ");
            }
        }
    }

    private static void printMethodBody(PrintWriter out) {
        out.print("{ throw new NotImplementedException() }\n");
    }

    private static void printArguments(Type[] types, PrintWriter out) {
        boolean wasPrinted = false;
        int counter = 1;

        for (var type : types) {
            if (wasPrinted) {
                out.print(", ");
            }

            wasPrinted = true;
            printDependency(type, out, Definition.NO);
            out.print(" var" + counter);
            counter++;
        }
    }

    private static void printDefaultValue(Type type, PrintWriter out) {
        if (!(type instanceof Class)) {
            out.print("null");
            return;
        }

        var clazz = (Class<?>) type;

        if (!clazz.isPrimitive()) {
            out.print("null");
            return;
        }

        var arrayWithOneElement = Array.newInstance(clazz, 1);

        out.print(Array.get(arrayWithOneElement, 0).toString());
    }
}
