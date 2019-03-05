package com.hse.reflection;

import jdk.jshell.spi.ExecutionControl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class Reflector {
    private static final int SPACES_PER_TAB = 4;
    @NotNull private static String currentPackage = "";

    private enum Definition { YES, NO }

    private static class Template {
        @NotNull private String prefix;
        @NotNull private String suffix;
        @NotNull private String separator;

        private Template(@NotNull String prefix, @NotNull String suffix, @NotNull String separator) {
            this.prefix = prefix;
            this.suffix = suffix;
            this.separator = separator;
        }
    }

    public static void diffClasses(@NotNull Class<?> first, @NotNull Class<?> second, @NotNull PrintWriter out) {
        var differentMethods = getDifferentMethods(first, second);
        var differentFields = getDifferentFields(first, second);
        printMethods(differentMethods, out, 0);
        printFields(differentFields, out, 0);
    }

    public static void diffClasses(@NotNull Class<?> first, @NotNull Class<?> second) {
        try (var out = new PrintWriter(System.out)) {
            diffClasses(first, second, out);
        }
    }

    @NotNull private static Method[] getDifferentMethods(@NotNull Class<?> first, @NotNull Class<?> second) {
        var firstMethods = first.getDeclaredMethods();
        var secondMethods = second.getDeclaredMethods();

        return getDifferentMethods(firstMethods, secondMethods);
    }

    @NotNull private static Method[] getDifferentMethods(@NotNull Method[] firstMethods,
                                                         @NotNull Method[] secondMethods) {
        return Stream.concat(
                Arrays.stream(firstMethods)
                      .filter(method -> !containsMethod(secondMethods, method)),
                Arrays.stream(firstMethods)
                      .filter(method -> !containsMethod(secondMethods, method)))
                .toArray(Method[]::new);
    }

    private static boolean containsMethod(@NotNull Method[] methods, @NotNull Method givenMethod) {
        for (var method : methods) {
            if (methodsEqual(method, givenMethod)) {
                return true;
            }
        }

        return false;
    }

    private static boolean methodsEqual(@NotNull Method first, @NotNull Method second) {
        return first.getName().equals(second.getName()) &&
               first.getGenericReturnType().equals(second.getGenericReturnType()) &&
               first.getModifiers() == second.getModifiers() &&
               Arrays.equals(first.getParameterTypes(), second.getParameterTypes());
    }

    @NotNull private static Field[] getDifferentFields(@NotNull Class<?> first, @NotNull Class<?> second) {
        var firstFields = first.getDeclaredFields();
        var secondFields = second.getDeclaredFields();

        return getDifferentFields(firstFields, secondFields);
    }

    @NotNull private static Field[] getDifferentFields(@NotNull Field[] firstFields, @NotNull Field[] secondFields) {
        return Stream.concat(
                Arrays.stream(firstFields)
                        .filter(field -> !containsField(secondFields, field)),
                Arrays.stream(firstFields)
                        .filter(field -> !containsField(secondFields, field)))
                .toArray(Field[]::new);
    }

    private static boolean containsField(@NotNull Field[] fields, @NotNull Field givenField) {
        for (var field : fields) {
            if (fieldsEqual(field, givenField)) {
                return true;
            }
        }

        return false;
    }

    private static boolean fieldsEqual(@NotNull Field first, @NotNull Field second) {
        return first.getName().equals(second.getName()) &&
               first.getGenericType().equals(second.getGenericType()) &&
               first.getModifiers() == second.getModifiers();
    }

    public static void printStructure(@NotNull Class<?> clazz) throws FileNotFoundException {
        String className = clazz.getSimpleName();

        try (var out = new PrintWriter(new File(className + ".java"))) {
            printFullClass(clazz, out, 0);
        }
    }

    private static void printFullClass(@NotNull Class<?> clazz, @NotNull PrintWriter out, int tabs) {
        currentPackage = clazz.getPackageName();
        printTabs(tabs, out);
        out.print(Modifier.toString(clazz.getModifiers()) + " class " + getSimpleName(clazz));
        printDependencies(clazz.getTypeParameters(), out,
                new Template("<", ">", ", "), Definition.NO);
        if (clazz.getGenericSuperclass() != null && clazz.getGenericSuperclass() != Object.class) {
            out.print(" extends ");
            printDependency(clazz.getGenericSuperclass(), out, Definition.NO);
        }

        if (clazz.getGenericInterfaces().length > 0) {
            printDependencies(clazz.getGenericInterfaces(), out,
                    new Template(" implements ", "", ", "), Definition.NO);
        }

        out.print(" {\n");
        for (var innerOrNestedClass : clazz.getDeclaredClasses()) {
            printFullClass(innerOrNestedClass, out, tabs + 1);
        }

        printConstructors(clazz.getDeclaredConstructors(), out, tabs + 1);
        printFields(clazz.getDeclaredFields(), out, tabs + 1);
        printMethods(clazz.getDeclaredMethods(), out, tabs + 1);

        out.print("}");
    }

    private static void printDependency(@NotNull Type type, @NotNull PrintWriter out,
                                        @NotNull Definition isDefinition) {
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

    private static void printInstanceOfClass(@NotNull Class<?> clazz, @NotNull PrintWriter out) {
        if (clazz.isArray()) {
            printDependency(clazz.getComponentType(), out, Definition.NO);
            out.print("[] ");
        } else {
            out.print(getSimpleName(clazz));
        }
    }

    private static void printInstanceOfWildCardType(@NotNull WildcardType wildcardType, @NotNull PrintWriter out) {
        out.print("? ");
        printDependencies(wildcardType.getLowerBounds(), out,
                new Template("super ", " ", " & "), Definition.NO);
        printDependencies(wildcardType.getUpperBounds(), out,
                new Template("extends ", " ", " & "), Definition.NO);
    }

    private static void printInstanceOfParameterizedType(@NotNull ParameterizedType parameterizedType,
                                                         @NotNull PrintWriter out) {
        printDependency(parameterizedType.getRawType(), out, Definition.NO);
        printDependencies(parameterizedType.getActualTypeArguments(), out,
                new Template("<", "> ", ", "), Definition.NO);
    }

    private static void printInstanceOfGenericArrayType(@NotNull GenericArrayType genericArrayType,
                                                        @NotNull Definition isDefinition, @NotNull PrintWriter out) {
        printDependency(genericArrayType.getGenericComponentType(), out, isDefinition);
        out.print("[] ");
    }

    private static void printInstanceOfTypeVariable(@NotNull TypeVariable<?> typeVariable,
                                                    @NotNull Definition isDefinition, @NotNull PrintWriter out) {
        out.print(typeVariable.getName());
        if (isDefinition == Definition.YES) {
            printDependencies(typeVariable.getBounds(), out,
                    new Template("extends ", " ", " & "), Definition.NO);
        }
    }

    private static void printDependencies(@NotNull Type[] types, @NotNull PrintWriter out,
                                          @NotNull Template template, @NotNull Definition isDefinition) {
        var listOfTypes = new ArrayList<Type>();

        for (var type : types) {
            if (type != Object.class) {
                listOfTypes.add(type);
            }
        }

        if (listOfTypes.size() == 0) {
            return;
        }

        boolean wasPrinted = false;

        out.print(template.prefix);
        for (var type : listOfTypes) {
            if (wasPrinted) {
                out.print(template.separator);
            }

            wasPrinted = true;
            printDependency(type, out, isDefinition);
        }

        out.print(template.suffix);
    }

    private static void printConstructor(@NotNull Constructor constructor, @NotNull PrintWriter out) {
        out.print(Modifier.toString(constructor.getModifiers()) + " ");
        printDependencies(constructor.getTypeParameters(), out,
                new Template("<", "> ", ", "), Definition.YES);
        out.print(getSimpleName(constructor.getDeclaringClass()) + " (");
        printArguments(constructor.getGenericParameterTypes(), out);
        out.print(") ");
        printDependencies(constructor.getGenericExceptionTypes(), out,
                new Template("throws ", " ",", "), Definition.NO);
        out.print(" {}\n");
    }

    private static void printConstructors(@NotNull Constructor[] constructors, @NotNull PrintWriter out, int tabs) {
        for (var constructor : constructors) {
            printTabs(tabs, out);
            printConstructor(constructor, out);
            out.print("\n");
        }
    }

    private static void printFields(@NotNull Field[] fields, @NotNull PrintWriter out, int tabs) {
        for (var field : fields) {
            printTabs(tabs, out);
            printField(field, out);
            out.print("\n");
        }
    }

    private static void printField(@NotNull Field field, @NotNull PrintWriter out) {
        out.print(Modifier.toString(field.getModifiers()) + " ");
        printDependency(field.getGenericType(), out, Definition.NO);
        out.print(" " + field.getName());
        if (Modifier.isFinal(field.getModifiers())) {
            out.print(" = ");
            printDefaultValue(field.getGenericType(), out);
        }

        out.print(";\n");
    }

    private static void printMethods(@NotNull Method[] methods, @NotNull PrintWriter out, int tabs) {
        for (var method : methods) {
            printTabs(tabs, out);
            printMethod(method, out);
            out.print("\n");
        }
    }

    private static void printMethod(@NotNull Method method, @NotNull PrintWriter out) {
        out.print(Modifier.toString(method.getModifiers()) + " ");
        printDependencies(method.getTypeParameters(), out,
                new Template("<", "> ", ", "), Definition.NO);
        printDependency(method.getReturnType(), out, Definition.NO);
        out.print(" " + method.getName());
        out.print(" ( ");
        printArguments(method.getGenericParameterTypes(), out);
        out.print(" ) ");
        printMethodBody(method, out);
    }

    private static void printTabs(int tabs, @NotNull PrintWriter out) {
        for (int i = 0; i < tabs; i++) {
            for (int j = 0; j < SPACES_PER_TAB; j++) {
                out.print(" ");
            }
        }
    }

    private static void printMethodBody(@NotNull Method method, @NotNull PrintWriter out) {
        out.print("{ ");
        if (!method.getGenericReturnType().getTypeName().equals("void")) {
            out.print("return ");
            printDefaultValue(method.getGenericReturnType(), out);
            out.print(";");
        }

        out.print("}");
    }

    private static void printArguments(@NotNull Type[] types, @NotNull PrintWriter out) {
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

    private static void printDefaultValue(@NotNull Type type, @NotNull PrintWriter out) {
        if (!(type instanceof Class)) {
            out.print("null");
            return;
        }

        var clazz = (Class<?>) type;

        if (!clazz.isPrimitive()) {
            out.print("null");
            return;
        }

        if (clazz == char.class) {
            out.print('0');
            return;
        }

        if (clazz == float.class) {
            out.print('0');
            return;
        }

        var arrayWithOneElement = Array.newInstance(clazz, 1);

        out.print(Array.get(arrayWithOneElement, 0).toString());
    }

    @NotNull private static String getSimpleName(@NotNull Class<?> clazz) {
        return getSimpleName(clazz.getName(), currentPackage, clazz.isMemberClass());
    }

    @NotNull private static String getSimpleName(@NotNull String fullName,
                                                 @NotNull String packageName, boolean isClassMember) {
        String name = fullName;

        if (!packageName.equals("") && fullName.contains(packageName)) {
            name = fullName.substring(packageName.length() + 1);
        }

        if (isClassMember) {
            name = name.substring(name.lastIndexOf('$') + 1);
        }
        return name;
    }
}
