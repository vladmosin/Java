package ru.hse.md5;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5CalculatorSingleThreaded {
    public byte[] calculate(@NotNull String directoryName) throws MD5Exception, NoSuchAlgorithmException, FileNotFoundException {
        var directory = new File(directoryName);
        return calculate(directory);
    }

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
