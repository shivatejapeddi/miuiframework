package com.miui.whetstone.steganography;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.File;

public class Steg {
    private final int PASS_NONE = 0;
    private final int PASS_SIMPLE_XOR = 1;
    private Bitmap inBitmap = null;
    private String key = null;
    private int passmode = 0;

    public static Steg withInput(String filePath) {
        Steg steg = new Steg();
        steg.setInputBitmap(BitmapFactory.decodeFile(filePath));
        return steg;
    }

    public static Steg withInput(File file) {
        Steg steg = new Steg();
        steg.setInputBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
        return steg;
    }

    public static Steg withInput(Bitmap bitmap) {
        Steg steg = new Steg();
        steg.setInputBitmap(bitmap);
        return steg;
    }

    private void setInputBitmap(Bitmap bitmap) {
        this.inBitmap = bitmap;
    }

    public Steg withPassword(String key) {
        withPassword(key, 1);
        return this;
    }

    public Steg withPassword(String key, int mode) {
        this.key = key;
        this.passmode = mode;
        throw new RuntimeException("Not implemented yet");
    }

    public DecodedObject decode() throws Exception {
        return new DecodedObject(BitmapEncoder.decode(this.inBitmap));
    }

    public EncodedObject encode(File file) throws Exception {
        throw new RuntimeException("Not implemented yet");
    }

    public EncodedObject encode(String string) throws Exception {
        return encode(string.getBytes());
    }

    public EncodedObject encode(byte[] bytes) throws Exception {
        if (bytes.length <= bytesAvaliableInBitmap()) {
            return new EncodedObject(BitmapEncoder.encode(this.inBitmap, bytes));
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Not enough space in bitmap to hold data (max:");
        stringBuilder.append(bytesAvaliableInBitmap());
        stringBuilder.append(")");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    private int bytesAvaliableInBitmap() {
        Bitmap bitmap = this.inBitmap;
        if (bitmap == null) {
            return 0;
        }
        return (((bitmap.getWidth() * this.inBitmap.getHeight()) * 3) / 8) - 12;
    }
}
