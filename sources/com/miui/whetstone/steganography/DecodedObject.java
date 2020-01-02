package com.miui.whetstone.steganography;

import java.io.File;
import java.io.IOException;

public class DecodedObject {
    private final byte[] bytes;

    public DecodedObject(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] intoByteArray() {
        return this.bytes;
    }

    public String intoString() {
        return new String(this.bytes);
    }

    public File intoFile(String path) throws IOException {
        return intoFile(new File(path));
    }

    public File intoFile(File file) {
        throw new RuntimeException("Not implemented yet");
    }
}
