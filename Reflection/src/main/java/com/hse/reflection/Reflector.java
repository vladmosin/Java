package com.hse.reflection;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** Class for comparing classes and printing their inner structure */
public class Reflector {

    /** Number of spaces per one tab */
    private static final int SPACES_PER_TAB = 4;

    private static ArrayList<String> imports = new ArrayList<>();

    /** Current package */
    @NotNull private static String currentPackage = "";

    /** Enum for describing is given type definition */
    private enum Definition { YES, NO }

    /** Structure for storing information for printing types */
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

    /** Prints all different methods and fields in classes
     * @param out PrintStream to print information */
    public static void diffClasses(@NotNull Class<?> first, @NotNull Class<?> second, @NotNull PrintStream out) {
        var differentMethods = getDifferentMethods(first, second);
        var differentFields = getDifferentFields(first, second);
        printMethods(differentMethods, out, 0);
        printFields(differentFields, out, 0);
    }

    /** Prints all different methods and fields in classes to console */
    public static void diffClasses(@NotNull Class<?> first, @NotNull Class<?> second) {
        try (var byteArrayOutputStream =  new ByteArrayOutputStream();
             var out = new PrintStream(System.out)) {
            diffClasses(first, second, out);
            System.out.println(byteArrayOutputStream.toString());
        } catch (IOException e) {
            System.out.println("Something went wrong");
        }
    }

    /** Returns array of methods which differ in classes */
    @NotNull private static List<Method> getDifferentMethods(@NotNull Class<?> first, @NotNull Class<?> second) {
        var firstMethods = Arrays.asList(first.getDeclaredMethods());
        var secondMethods = Arrays.asList(second.getDeclaredMethods());

        return getDifferentMethods(firstMethods, secondMethods);
    }

    /** Returns array of methods which differ in arrays */
    @NotNull private static List<Method> getDifferentMethods(@NotNull List<Method> firstMethods,
                                                         @NotNull List<Method> secondMethods) {
        return Stream.concat(
                firstMethods.stream()
                      .filter(method -> !containsMethod(secondMethods, method)),
                secondMethods.stream()
                      .filter(method -> !containsMethod(firstMethods, method)))
                .collect(Collectors.toList());
    }

    /** Checks if method contains in array */
    private static boolean containsMethod(@NotNull List<Method> methods, @NotNull Method givenMethod) {
        for (var method : methods) {
            if (methodsEqual(method, givenMethod)) {
                return true;
            }
        }

        return false;
    }

    /** Returns true if methods are the same */
    private static boolean methodsEqual(@NotNull Method first, @NotNull Method second) {
        return first.getName().equals(second.getName()) &&
               checkTypeEquality(first.getGenericReturnType(), second.getGenericReturnType()) &&
               first.getModifiers() == second.getModifiers() &&
               Arrays.equals(first.getParameterTypes(), second.getParameterTypes(), Reflector::compareTypes);
    }

    private static boolean checkTypeEquality(Type first, Type second) {
        try (var byteArrayOutputStream1 = new ByteArrayOutputStream();
             var byteArrayOutputStream2 = new ByteArrayOutputStream();
             var out1 = new PrintStream(byteArrayOutputStream1);
             var out2 = new PrintStream(byteArrayOutputStream2)) {
                 printDependency(first, out1, Definition.NO);
                 printDependency(second, out2, Definition.NO);

                 boolean fl = Arrays.equals(byteArrayOutputStream1.toByteArray(), byteArrayOutputStream2.toByteArray());
                 return fl;
             } catch (IOException e) {
            return false;
        }
    }

    /** Compares types */
    private static int compareTypes(Type first, Type second) {
        if (checkTypeEquality(first, second)) {
            return 0;
        }

        return -1;
    }

    /** Returns array of fields which differ in classes */
    @NotNull private static List<Field> getDifferentFields(@NotNull Class<?> first, @NotNull Class<?> second) {
        var firstFields = Arrays.asList(first.getDeclaredFields());
        var secondFields = Arrays.asList(second.getDeclaredFields());

        return getDifferentFields(firstFields, secondFields);
    }

    /** Returns array of fields which differ in arrays */
    @NotNull private static List<Field> getDifferentFields(@NotNull List<Field> firstFields,
                                                           @NotNull List<Field> secondFields) {
        return Stream.concat(
                firstFields.stream()
                        .filter(field -> !containsField(secondFields, field)),
                secondFields.stream()
                        .filter(field -> !containsField(firstFields, field)))
                .collect(Collectors.toList());
    }

    /** Checks if field contains in array */
    private static boolean containsField(@NotNull List<Field> fields, @NotNull Field givenField) {
        for (var field : fields) {
            if (fieldsEqual(field, givenField)) {
                return true;
            }
        }

        return false;
    }

    /** Returns true if fields are the same */
    private static boolean fieldsEqual(@NotNull Field first, @NotNull Field second) {
        return first.getName().equals(second.getName()) &&
               checkTypeEquality(first.getGenericType(), second.getGenericType()) &&
               first.getModifiers() == second.getModifiers();
    }

    /** Prints class structure to file "className.java" */
    public static void printStructure(@NotNull Class<?> clazz) throws FileNotFoundException {
        String className = clazz.getSimpleName();

        imports.clear();

        try (var byteArrayOutputStream = new ByteArrayOutputStream();
             var out = new PrintStream(byteArrayOutputStream)) {
            printFullClass(clazz, out, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints class to PrintStream
     * @param clazz Class to print
     * @param out PrintStream to print class
     * @param tabs Distance in tabs from left side of screen to class declaration
     * */
    private static void printFullClass(@NotNull Class<?> clazz, @NotNull PrintStream out, int tabs) {
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
        printFields(Arrays.asList(clazz.getDeclaredFields()), out, tabs + 1);
        printMethods(Arrays.asList(clazz.getDeclaredMethods()), out, tabs + 1);

        out.print("}");
    }

    /**
     * Prints type information to PrintStream
     * @param type Type information about which should be printed
     * @param isDefinition YES for definition and NO for usage
     * */
    private static void printDependency(@NotNull Type type, @NotNull PrintStream out,
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

    /** Prints instance of class */
    private static void printInstanceOfClass(@NotNull Class<?> clazz, @NotNull PrintStream out) {
        if (clazz.isArray()) {
            printDependency(clazz.getComponentType(), out, Definition.NO);
            out.print("[] ");
        } else {
            out.print(getSimpleName(clazz));
        }
    }

    /** Prints instance of WildcardType */
    private static void printInstanceOfWildCardType(@NotNull WildcardType wildcardType, @NotNull PrintStream out) {
        out.print("? ");
        printDependencies(wildcardType.getLowerBounds(), out,
                new Template("super ", " ", " & "), Definition.NO);
        printDependencies(wildcardType.getUpperBounds(), out,
                new Template("extends ", " ", " & "), Definition.NO);
    }

    /** Prints instance of ParameterizedType */
    private static void printInstanceOfParameterizedType(@NotNull ParameterizedType parameterizedType,
                                                         @NotNull PrintStream out) {
        printDependency(parameterizedType.getRawType(), out, Definition.NO);
        printDependencies(parameterizedType.getActualTypeArguments(), out,
                new Template("<", "> ", ", "), Definition.NO);
    }

    /** Prints instance of GenericArrayType */
    private static void printInstanceOfGenericArrayType(@NotNull GenericArrayType genericArrayType,
                                                        @NotNull Definition isDefinition, @NotNull PrintStream out) {
        printDependency(genericArrayType.getGenericComponentType(), out, isDefinition);
        out.print("[] ");
    }

    /** Prints instance of TypeVariable */
    private static void printInstanceOfTypeVariable(@NotNull TypeVariable<?> typeVariable,
                                                    @NotNull Definition isDefinition, @NotNull PrintStream out) {
        imports.add(typeVariable.getName());
        out.print(typeVariable.getName());
        if (isDefinition == Definition.YES) {
            printDependencies(typeVariable.getBounds(), out,
                    new Template("extends ", " ", " & "), Definition.NO);
        }
    }

    /**
     * Prints array of types
     * @param isDefinition Is it definition
     * @param types Array of types
     * @param template Stores beginning, separator between types and ending
     * */
    private static void printDependencies(@NotNull Type[] types, @NotNull PrintStream out,
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

    /** Prints constructor to PrintStream */
    private static void printConstructor(@NotNull Constructor constructor, @NotNull PrintStream out) {
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

    /** Prints array of constructors
     * @param tabs Distance in tabs between left side of screen and constructor
     * */
    private static void printConstructors(@NotNull Constructor[] constructors, @NotNull PrintStream out, int tabs) {
        for (var constructor : constructors) {
            printTabs(tabs, out);
            printConstructor(constructor, out);
            out.print("\n");
        }
    }

    /** Prints array of fields
     * @param tabs Distance in tabs between left side of screen and field
     * */
    private static void printFields(@NotNull List<Field> fields, @NotNull PrintStream out, int tabs) {
        for (var field : fields) {
            printTabs(tabs, out);
            printField(field, out);
            out.print("\n");
        }
    }

    /** Prints field */
    private static void printField(@NotNull Field field, @NotNull PrintStream out) {
        out.print(Modifier.toString(field.getModifiers()) + " ");
        printDependency(field.getGenericType(), out, Definition.NO);
        out.print(" " + field.getName());
        if (Modifier.isFinal(field.getModifiers())) {
            out.print(" = ");
            printDefaultValue(field.getGenericType(), out);
        }

        out.print(";\n");
    }

    /** Prints array of methods
     * @param tabs Distance in tabs between left side of screen and method
     * */
    private static void printMethods(@NotNull List<Method> methods, @NotNull PrintStream out, int tabs) {
        for (var method : methods) {
            printTabs(tabs, out);
            printMethod(method, out);
            out.print("\n");
        }
    }

    /** Prints method */
    private static void printMethod(@NotNull Method method, @NotNull PrintStream out) {
        out.print(Modifier.toString(method.getModifiers()) + " ");
        printDependencies(method.getTypeParameters(), out,
                new Template("<", "> ", ", "), Definition.NO);
        printDependency(method.getGenericReturnType(), out, Definition.NO);
        out.print(" " + method.getName());
        out.print(" ( ");
        printArguments(method.getGenericParameterTypes(), out);
        out.print(" ) ");
        printMethodBody(method, out);
    }

    /** Prints tabs */
    private static void printTabs(int tabs, @NotNull PrintStream out) {
        for (int i = 0; i < tabs; i++) {
            for (int j = 0; j < SPACES_PER_TAB; j++) {
                out.print(" ");
            }
        }
    }

    /** Prints body for method, given as argument.
     * For returning type void method body is empty, for others is default value */
    private static void printMethodBody(@NotNull Method method, @NotNull PrintStream out) {
        out.print("{ ");
        if (!method.getGenericReturnType().getTypeName().equals("void")) {
            out.print("return ");
            printDefaultValue(method.getGenericReturnType(), out);
            out.print(";");
        }

        out.print("}");
    }

    /** Prints function arguments */
    private static void printArguments(@NotNull Type[] types, @NotNull PrintStream out) {
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

    /** Prints default value */
    private static void printDefaultValue(@NotNull Type type, @NotNull PrintStream out) {
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

    /** Returns simple name without package */
    @NotNull private static String getSimpleName(@NotNull Class<?> clazz) {
        return getSimpleName(clazz.getName(), currentPackage, clazz.isMemberClass());
    }

    /** Returns simple name without package */
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
