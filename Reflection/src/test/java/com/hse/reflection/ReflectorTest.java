package com.hse.reflection;

import org.junit.jupiter.api.Test;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TestPrimitiveTypes {
    int a;
    char b;
}

class TestDifferenceInGenericReturnType1 {
    List<String> f() {return null;}
}

class TestDifferenceInGenericReturnType2 {
    List<Integer> f() {return null;}
}

class TestPrimitivesDifferenceInName {
    int c;
    char b;
}

class TestPrimitivesDifferenceInType {
    int a;
    boolean b;
}

class TestPrimitivesDifferenceInModifiers {
    public int a;
    char b;
}

class TestImports {
    List<String> list;
}

class TestFinalPrimitives {
    final short s = 1;
    final byte bt = 0;
    final int i = 13234;
    final long l = 0;
    final char c = 'i';
    final boolean b = true;
    final double d = 0.32;
    final float f = 13;
}

class TestMethods {
    Integer first(int a, Integer b) { return a; }
    void emptyFunction() {}
    double max(Object o) { return 21.23; }
}

class TestDifferenceInGenericType {
    List<Integer> list;
}

class TestArraysInFields {
    Object[] a;
    final int[] b = null;
}

class TestArraysAsFunctionArguments {
    void f1(Double[] d, Object[] objects) {}
    int f2(long[] l) { return 1; }
}

class TestArraysAsReturnValue {
    int[] f1() { return null; }
    Integer[] f2(Integer[] a) { return a; }
}

class TestConstructor {
    TestConstructor() {}
    TestConstructor(Integer i) {}
    TestConstructor(int a, double b) {}
}

class TestFieldsWithModifiers {
    public int a;
    private Object o;
    public static char c;
}

class TestMethodsWithModifiers {
    public static void f() {};
    private final Object g(int a) { return null; };
}

class TestNestedClasses {
    private static class Nested1 {
        private Nested1() {}
        private int x;
        protected void m() {}

        private static class Nested2 {}
    }
}

class TestInnerClasses {
    public class Inner {
        int a;
    }
}

class TestExceptions {
    TestExceptions() throws IllegalAccessError {}
    void f(int a) throws IOException, ClassNotFoundException, IllegalArgumentException {}
}

class TestExtends extends AbstractMap {
    @Override
    public Set<Entry> entrySet() {
        return null;
    }
}

class TestImplements implements Runnable, Comparable {
    public int compareTo(Object o) { return 0; }
    public void run() {}
}

class TestExtendsAndImplements extends AbstractMap implements Comparable {
    public int compareTo(Object o) {
        return 0;
    }
    
    public Set<Entry> entrySet() {
        return null;
    }
}

class TestGenericFields<T, S> {
    final S b = null;
    T[] a;
    S s;
    T t;
    List<S> list;
}

class TestGenericMethodsArgs<T, S> {
    TestGenericMethodsArgs(T t) {}
    void f1(T t, S s) {}
    <U extends Serializable & Runnable> void f2(Collection<U> pred) {}
}

class TestGenericReturnTypes<T, S> {
    List<T> f1() { return null; }
    S f2() { return null; }
}

class TestWildcardInFields<T> {
    List<?> l1;
    List<? extends T> l2;
    List<? super T> l3;
}

class TestWildcardInReturnTypes<T> {
    List<?> f1() {
        return null;
    }

    List<? extends T> f2() {
        return null;
    }

    List<? super T> f3() {
        return null;
    }
}

class TestWildcardInArgs<T> {
    void f1(List<?> l) {}
    void f2(List<? extends T> l) {}
    void f3(List<? super T> l) {}
}

class ReflectorTest {
    @Test
    void testPrimitiveTypes() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestPrimitiveTypes.class);
    }

    @Test
    void testImports() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestImports.class);
    }

    @Test
    void testDifferenceInGenericReturnType1() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestDifferenceInGenericReturnType1.class);
    }

    @Test
    void testFinalPrimitives() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestFinalPrimitives.class);
    }

    @Test
    void testMethods() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestMethods.class);
    }

    @Test
    void testConstructor() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestConstructor.class);
    }

    @Test
    void testArraysAsFunctionArguments() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestArraysAsFunctionArguments.class);
    }

    @Test
    void testArraysAsReturnValue() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestArraysAsReturnValue.class);
    }

    @Test
    void testArraysInFields() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestArraysInFields.class);
    }

    @Test
    void testFieldsWithModifiers() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestFieldsWithModifiers.class);
    }

    @Test
    void testMethodsWithModifiers() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestMethodsWithModifiers.class);
    }

    @Test
    void testExceptions() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestExceptions.class);
    }

    @Test
    void testExtends() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestExtends.class);
    }

    @Test
    void testInnerClasses() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestInnerClasses.class);
    }

    @Test
    void testImplements() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestImplements.class);
    }

    @Test
    void testExtendsAndImplements() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestExtendsAndImplements.class);
    }

    @Test
    void testNestedClasses() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestNestedClasses.class);
    }

    @Test
    void testGenericFields() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestGenericFields.class);
    }

    @Test
    void testGenericMethodsArgs() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestGenericMethodsArgs.class);
    }

    @Test
    void testGenericReturnTypes() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestGenericReturnTypes.class);
    }

    @Test
    void testWildcardInFields() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestWildcardInFields.class);
    }

    @Test
    void testWildcardInReturnTypes() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestWildcardInReturnTypes.class);
    }

    @Test
    void testWildcardInArgs() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestWildcardInArgs.class);
    }

    @Test
    void testDiffOnEqualClasses() throws IOException {
        try (var byteArrayOutputStream = new ByteArrayOutputStream();
             var out = new PrintStream(byteArrayOutputStream)) {
            Reflector.diffClasses(TestWildcardInArgs.class, TestWildcardInArgs.class, out);
            assertEquals(0, byteArrayOutputStream.size());
        }

        try (var byteArrayOutputStream = new ByteArrayOutputStream();
             var out = new PrintStream(byteArrayOutputStream)) {
            Reflector.diffClasses(TestPrimitiveTypes.class, TestPrimitiveTypes.class, out);
            assertEquals(0, byteArrayOutputStream.size());
        }
    }

    @Test
    void testFindDifference() throws IOException {
        try (var byteArrayOutputStream = new ByteArrayOutputStream();
             var out = new PrintStream(byteArrayOutputStream)) {
            Reflector.diffClasses(TestInnerClasses.class, TestMethods.class, out);
            assertTrue(byteArrayOutputStream.size() > 0);
        }
    }

    @Test
    void testDifferenceInPrimitiveFieldNames() throws IOException {
        try (var byteArrayOutputStream = new ByteArrayOutputStream();
             var out = new PrintStream(byteArrayOutputStream)) {
            Reflector.diffClasses(TestPrimitiveTypes.class, TestPrimitivesDifferenceInName.class, out);
            String difference = byteArrayOutputStream.toString();

            assertTrue(difference.contains("int a"));
            assertTrue(difference.contains("int c"));
            assertFalse(difference.contains("char b"));
        }
    }

    @Test
    void testDifferenceInPrimitiveType() throws IOException {
        try (var byteArrayOutputStream = new ByteArrayOutputStream();
             var out = new PrintStream(byteArrayOutputStream)) {
            Reflector.diffClasses(TestPrimitiveTypes.class, TestPrimitivesDifferenceInType.class, out);
            String difference = byteArrayOutputStream.toString();

            assertTrue(difference.contains("boolean b"));
            assertTrue(difference.contains("char b"));
            assertFalse(difference.contains("int a"));
        }
    }

    @Test
    void testDifferenceInPrimitiveModifiers() throws IOException {
        try (var byteArrayOutputStream = new ByteArrayOutputStream();
             var out = new PrintStream(byteArrayOutputStream)) {
            Reflector.diffClasses(TestPrimitiveTypes.class, TestPrimitivesDifferenceInModifiers.class, out);
            String difference = byteArrayOutputStream.toString();

            assertTrue(difference.contains("public int a"));
            assertFalse(difference.contains("char b"));
        }
    }

    @Test
    void testDifferenceInGenericType() throws IOException {
        try (var byteArrayOutputStream = new ByteArrayOutputStream();
             var out = new PrintStream(byteArrayOutputStream)) {
            Reflector.diffClasses(TestDifferenceInGenericType.class, TestImports.class, out);
            String difference = byteArrayOutputStream.toString();

            assertTrue(difference.contains("List<String>  list"));
            assertTrue(difference.contains("List<Integer>  list"));
        }
    }

    @Test
    void testDifferenceInGenericReturnType() throws IOException {
        try (var byteArrayOutputStream = new ByteArrayOutputStream();
             var out = new PrintStream(byteArrayOutputStream)) {
            Reflector.diffClasses(TestDifferenceInGenericReturnType1.class,
                                  TestDifferenceInGenericReturnType2.class, out);
            String difference = byteArrayOutputStream.toString();

            assertTrue(difference.contains("List<String>  f (  ) { return null;}"));
            assertTrue(difference.contains("List<Integer>  f (  ) { return null;}"));
        }
    }

    private void testCompiledSourceEqual(Class<?> targetClass) throws IOException,
            ClassNotFoundException {
        Reflector.printStructure(targetClass);

        var sourceFile = new File(targetClass.getSimpleName() + ".java");
        var currentDirectory = new File(".");

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, sourceFile.getPath());

        var classLoader = URLClassLoader.newInstance(new URL[] { currentDirectory.toURI().toURL() });
        var loadedClass = Class.forName(targetClass.getSimpleName(), true, classLoader);

        try (var byteArrayOutputStream = new ByteArrayOutputStream();
             var out = new PrintStream(byteArrayOutputStream)) {
            Reflector.diffClasses(targetClass, loadedClass, out);
            assertEquals(0, byteArrayOutputStream.size());
        }

        var binaryFile = new File(targetClass.getSimpleName() + ".class");

        binaryFile.delete();
        sourceFile.delete();
    }
}