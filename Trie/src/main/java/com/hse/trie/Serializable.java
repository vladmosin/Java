package com.hse.trie;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Interface to convert byte sequence to object and conversely
 * */
public interface Serializable {
    /**
     * Converts object to byte sequence in OutputStream
     * */
    void serialize(OutputStream out) throws IOException;

    /**
     * Creates object from data in InputStream and replace current object with it
     * */
    void deserialize(InputStream in) throws IOException;
}
