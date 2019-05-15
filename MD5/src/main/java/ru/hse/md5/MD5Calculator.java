package ru.hse.md5;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;

public class MD5Calculator {
    public static byte[] calculateSingleThreaded(@NotNull File directory)
            throws FileNotFoundException, NoSuchAlgorithmException, MD5Exception {
        var md5Calculator = new MD5CalculatorSingleThreaded();

        return md5Calculator.calculate(directory);
    }

    public static byte[] calculateMultiThreaded(@NotNull File directory, int numberOfThreads)
            throws FileNotFoundException, NoSuchAlgorithmException, MD5Exception {
        var md5Calculator = new MD5CalculatorMultiThreaded(numberOfThreads);

        return md5Calculator.calculate(directory);
    }
}
