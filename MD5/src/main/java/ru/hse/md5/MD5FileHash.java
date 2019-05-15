package ru.hse.md5;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** Utility to hash single file */
public class MD5FileHash {
    @SuppressWarnings("StatementWithEmptyBody")
    public static byte[] hashFile(@NotNull File file) throws NoSuchAlgorithmException, MD5Exception {
        var messageDigest = MessageDigest.getInstance("MD5");
        var buffer = new byte[4096];

        try (var encryptedInput = new DigestInputStream(new FileInputStream(file), messageDigest)) {
            while (encryptedInput.read(buffer) != -1);
            return messageDigest.digest();
        } catch (Exception e) {
            throw new MD5Exception("Problems with caching file");
        }
    }
}
