package com.hse.reflection;

import org.jetbrains.annotations.NotNull;
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
import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.*;

class TestPrimitiveTypes {
    int a;
    char b;
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
    @Override
    public int compareTo(@NotNull Object o) {
        return 0;
    }

    @Override
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
             var out = new PrintWriter(byteArrayOutputStream)) {
            Reflector.diffClasses(targetClass, loadedClass, out);
            assertEquals(0, byteArrayOutputStream.size());
        }

        var binaryFile = new File(targetClass.getSimpleName() + ".class");

        binaryFile.delete();
        sourceFile.delete();
    }
}