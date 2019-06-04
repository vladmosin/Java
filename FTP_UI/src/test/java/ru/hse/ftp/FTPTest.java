package ru.hse.ftp;

import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FTPTest {

    @BeforeAll
    static void startServer() throws InterruptedException, IOException {
        var javaHome = System.getProperty("java.home");
        var javaBin = javaHome + File.separator + "bin" + File.separator + "java";
        var classpath = System.getProperty("java.class.path");
        var className = Server.class.getCanonicalName();

        var builder = new ProcessBuilder(javaBin, "-cp", classpath, className);
        builder.start();
        Thread.sleep(1000);
    }

    @Test
    void executeList() throws IOException {
        var client = new Client();
        var result = client.executeList("src");
        assertEquals("2 main 1 test 1", result);
    }

    @Test
    void testListWithFile() throws IOException {
        var client = new Client();
        var result = client.executeList("MainTest");
        assertEquals("3 file1.txt 0 file2.txt 0 TestDir 1", result);
    }

    @Test
    void testGetWithFile() throws IOException {
        var client = new Client();
        var result = client.executeGet("MainTest/file1.txt");
        assertEquals("3 122", result);
    }

    @Test
    void testMultiQueries() throws IOException {
        var client = new Client();
        var result = client.executeGet("MainTest/file1.txt");
        assertEquals("3 122", result);
        result = client.executeList("MainTest");
        assertEquals("3 file1.txt 0 file2.txt 0 TestDir 1", result);
    }

    @Test
    void testTwoClients() throws IOException, InterruptedException {
        var client1 = new Client("localhost");
        var client2 = new Client("localhost");

        var thread1 = new Thread(() -> {
            try {
                for (int i = 0; i < 100; i++) {
                    client1.executeList("MainTest");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        var thread2 = new Thread(() -> {
            try {
                for (int i = 0; i < 100; i++) {
                    client2.executeList("MainTest");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }

    @Test
    void testListFile() throws IOException {
        var client = new Client();
        var result = client.executeList("MainTest/file1.txt");
        assertEquals("-1", result);
    }
}
