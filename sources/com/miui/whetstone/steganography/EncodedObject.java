package com.miui.whetstone.steganography;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class EncodedObject {
    private final Bitmap bitmap;

    public EncodedObject(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap intoBitmap() {
        return this.bitmap;
    }

    public File intoFile(String path) throws IOException {
        return intoFile(new File(path));
    }

    public File intoFile(File file) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        this.bitmap.compress(CompressFormat.PNG, 100, bytes);
        file.createNewFile();
        FileOutputStream fo = new FileOutputStream(file);
        fo.write(bytes.toByteArray());
        fo.close();
        return file;
    }
}
