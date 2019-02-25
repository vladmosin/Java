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
    }

    private static List<Method> getDifferentMethods(Class<?> first, Class<?> second) {
        var firstMethods = first.getDeclaredMethods();
        var secondMethods = second.getDeclaredMethods();

        return getDifferentMethods(firstMethods, secondMethods);
    }

    private static List<Method> getDifferentMethods(Method[] firstMethods, Method[] secondMethods) {
        return Stream.concat(
                Arrays.stream(firstMethods)
                      .filter(method -> containsMethod(secondMethods, method)),
                Arrays.stream(firstMethods)
                      .filter(method -> containsMethod(secondMethods, method)))
                .collect(Collectors.toList());
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

    private static List<Field> getDifferentFields(Class<?> first, Class<?> second) {
        var firstFields = first.getDeclaredFields();
        var secondFields = second.getDeclaredFields();

        return getDifferentFields(firstFields, secondFields);
    }

    private static List<Field> getDifferentFields(Field[] firstFields, Field[] secondFields) {
        return Stream.concat(
                Arrays.stream(firstFields)
                        .filter(field -> containsField(secondFields, field)),
                Arrays.stream(firstFields)
                        .filter(field -> containsField(secondFields, field)))
                .collect(Collectors.toList());
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
        var ownerType = parameterizedType.getOwnerType();

        if (ownerType != null) {
            printDependency(ownerType, out, Definition.NO);
            out.print(".");
        }

        printDependency(parameterizedType, out, Definition.NO);
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

    private static void printTabs(int tabs, PrintWriter out) {
        for (int i = 0; i < tabs; i++) {
            for (int j = 0; j < SPACES_PER_TAB; j++) {
                out.print(" ");
            }
        }
    }

    private static void printMethodBody(PrintWriter out) {
        out.print("{ throw new NotImplementedException() };\n");
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
            out.print("var" + counter);
            counter++;
        }
    }
}
