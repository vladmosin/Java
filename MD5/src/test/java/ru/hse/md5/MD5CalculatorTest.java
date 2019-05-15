package ru.hse.md5;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;


public class MD5CalculatorTest {

    // 0 means that single threaded version should be used
    private void checkEmptyFile(int numberOfThreads) throws IOException, MD5Exception, NoSuchAlgorithmException {
        var temporaryFile = File.createTempFile("tempFile", ".tmp");

        temporaryFile.deleteOnExit();
        checkFile(temporaryFile, numberOfThreads);
    }

    private void checkFile(@NotNull File file, int numberOfThreads) throws MD5Exception,
            NoSuchAlgorithmException, FileNotFoundException {
        byte[] hashWithCalculator;
        byte[] rightHash = MD5FileHash.hashFile(file);

        if (numberOfThreads == 0) {
            hashWithCalculator = MD5Calculator.calculateSingleThreaded(file);
        } else {
            hashWithCalculator = MD5Calculator.calculateMultiThreaded(file, numberOfThreads);
        }

        assertArrayEquals(hashWithCalculator, rightHash);
    }

    @Test
    public void testEmptyFileSingleThreaded() throws MD5Exception, NoSuchAlgorithmException, IOException {
        checkEmptyFile(0);
    }

    @Test
    public void testEmptyFileMultiThreaded() throws MD5Exception, NoSuchAlgorithmException, IOException {
        checkEmptyFile(4);
    }

    @Test
    public void testNotEmptyFileSingleThreaded() throws FileNotFoundException, NoSuchAlgorithmException, MD5Exception {
        checkFile(new File("src/test/TestDirectory/TestDirectory1/file1.txt"),0);
    }

    @Test
    public void testNotEmptyFileMultiThreaded() throws FileNotFoundException, NoSuchAlgorithmException, MD5Exception {
        checkFile(new File("src/test/TestDirectory/TestDirectory1/file1.txt"),5);
    }

    public void testDirectoryWithOneFile(int numberOfThreads) throws NoSuchAlgorithmException, MD5Exception, FileNotFoundException {
        var directory = new File("src/test/TestDirectory/TestDirectory2");
        var file = new File("src/test/TestDirectory/TestDirectory2/file3.txt");

        var messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(directory.getName().getBytes());
        messageDigest.update(MD5FileHash.hashFile(file));

        var rightHash = messageDigest.digest();
        byte[] hashWithCalculator;

        if (numberOfThreads == 0) {
            hashWithCalculator = MD5Calculator.calculateSingleThreaded(directory);
        } else {
            hashWithCalculator = MD5Calculator.calculateMultiThreaded(directory, numberOfThreads);
        }

        assertArrayEquals(rightHash, hashWithCalculator);
    }

    @Test
    public void testDirectoryWithOneFileSingleThreaded() throws FileNotFoundException, NoSuchAlgorithmException, MD5Exception {
        testDirectoryWithOneFile(0);
    }

    @Test
    public void testDirectoryWithOneFileMultiThreaded() throws FileNotFoundException, NoSuchAlgorithmException, MD5Exception {
        testDirectoryWithOneFile(3);
    }

    private void testBigFile(int numberOfThreads) throws IOException, MD5Exception, NoSuchAlgorithmException {
        var temporaryFile = File.createTempFile("tempFile", ".tmp");
        var printer = new PrintWriter(temporaryFile);

        for (int i = 0; i < 10000; i++) {
            printer.print("goenrgbnrgvekgmo");
        }

        temporaryFile.deleteOnExit();
        checkFile(temporaryFile, numberOfThreads);
    }

    @Test
    public void testBigFileSingleThreaded() throws FileNotFoundException, NoSuchAlgorithmException, MD5Exception {
        testDirectoryWithOneFile(0);
    }

    @Test
    public void testBigFileMultiThreaded() throws FileNotFoundException, NoSuchAlgorithmException, MD5Exception {
        testDirectoryWithOneFile(3);
    }

    @Test
    public void testEqualBehaviour() throws FileNotFoundException, NoSuchAlgorithmException, MD5Exception {
        assertArrayEquals(MD5Calculator.calculateMultiThreaded(new File("src/test/TestDirectory"), 4),
                          MD5Calculator.calculateSingleThreaded(new File("src/test/TestDirectory")));
    }
}
