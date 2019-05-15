package ru.hse.md5;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** Singlethreaded implementation of MD5 */
public class MD5CalculatorSingleThreaded {
     /** Calculates hash */
    public byte[] calculate(@NotNull File directory) throws FileNotFoundException, MD5Exception, NoSuchAlgorithmException {
        if (!directory.exists()) {
            throw new FileNotFoundException("File was not found");
        }

        if (!directory.isDirectory()) {
            return MD5FileHash.hashFile(directory);
        }

        var encryptedMessage = MessageDigest.getInstance("MD5");
        encryptedMessage.update(directory.getName().getBytes());

        var files = directory.listFiles();
        if (files == null) {
            return encryptedMessage.digest();
        }

        for (var file : files) {
            encryptedMessage.update(calculate(file));
        }

        return encryptedMessage.digest();
    }
}
