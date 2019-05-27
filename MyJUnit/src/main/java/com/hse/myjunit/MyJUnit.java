package com.hse.myjunit;

import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class MyJUnit {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        if (args.length != 1) {
            System.out.println("Usage: <path to file with tests>");
            return;
        }

        var testLauncher = new TestLauncher();
        var path = Paths.get(args[0]);

        if (!path.toFile().exists()) {
            System.out.println("File not found");
        }

        List<Class<?>> loadedClasses = new ArrayList<>();
        try {
            if (args[0].endsWith(".class")) {
                loadedClasses = new ArrayList<>();
                loadedClasses.add(loadClass(path));
            } else if (args[0].endsWith(".jar")) {
                loadedClasses = loadClassesFromJar(path);
            } else {
                System.out.println("Illegal file name");
            }
            System.out.println(testLauncher.runTests(loadedClasses));
        } catch (MalformedURLException | ClassNotFoundException ignored) {
            System.out.println("Incorrect path");
        } catch (IOException ignored) {
            System.out.println("Problems with file reading");
        }
    }

    private static Class<?> loadClass(@NotNull Path path) throws MalformedURLException, ClassNotFoundException {
        var urlClassLoader = createClassLoader(path);
        var className = path.getFileName().toString();

        return urlClassLoader.loadClass(className);
    }

    private static List<Class<?>> loadClassesFromJar(@NotNull Path path) throws IOException, ClassNotFoundException {
        var urlClassLoader = createClassLoader(path);
        var loadedClasses = new ArrayList<Class<?>>();
        var jarInputStream = new JarInputStream(new FileInputStream(path.toFile()));
        JarEntry jarEntry;

        while ((jarEntry = jarInputStream.getNextJarEntry()) != null) {
            if (jarEntry.getName().endsWith(".class")) {
                loadedClasses.add(urlClassLoader.loadClass(jarEntry.getName()));
            }
        }

        return loadedClasses;
    }

    private static URLClassLoader createClassLoader(@NotNull Path path) throws MalformedURLException {
        return new URLClassLoader(new URL[] { path.getParent().toUri().toURL()});
    }
}
