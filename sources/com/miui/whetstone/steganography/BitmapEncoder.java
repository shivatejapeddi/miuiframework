package com.miui.whetstone.steganography;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class BitmapEncoder {
    public static final int HEADER_SIZE = 12;

    public static byte[] createHeader(long size) {
        byte[] header = new byte[12];
        int i = 0 + 1;
        header[0] = (byte) 91;
        int i2 = i + 1;
        header[i] = (byte) 91;
        byte[] longToBytes = longToBytes(size);
        int length = longToBytes.length;
        int i3 = 0;
        while (i3 < length) {
            int i4 = i2 + 1;
            header[i2] = longToBytes[i3];
            i3++;
            i2 = i4;
        }
        i = i2 + 1;
        header[i2] = (byte) 93;
        i2 = i + 1;
        header[i] = (byte) 93;
        return header;
    }

    private static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getLong();
    }

    public static Bitmap encode(Bitmap inBitmap, byte[] bytes) {
        byte[] header = createHeader((long) bytes.length);
        if (bytes.length % 24 != 0) {
            bytes = Arrays.copyOf(bytes, bytes.length + (24 - (bytes.length % 24)));
        }
        return encodeByteArrayIntoBitmap(inBitmap, header, bytes);
    }

    public static byte[] decode(Bitmap inBitmap) {
        return decodeBitmapToByteArray(inBitmap, 12, (int) bytesToLong(Arrays.copyOfRange(decodeBitmapToByteArray(inBitmap, null, 12), 2, 10)));
    }

    private static Bitmap encodeByteArrayIntoBitmap(Bitmap inBitmap, byte[] header, byte[] bytes) {
        Bitmap bitmap = inBitmap;
        byte[] bArr = header;
        byte[] bArr2 = bytes;
        Bitmap outBitmap = bitmap.copy(Config.ARGB_8888, true);
        int x = 0;
        int y = 0;
        int width = inBitmap.getWidth();
        int height = inBitmap.getHeight();
        int bufferPos = 0;
        int[] buffer = new int[]{0, 0, 0};
        int i = 0;
        while (i < bArr.length + bArr2.length) {
            int j = 0;
            while (j < 8) {
                if (i < bArr.length) {
                    buffer[bufferPos] = (bArr[i] >> j) & 1;
                } else {
                    buffer[bufferPos] = (bArr2[i - bArr.length] >> j) & 1;
                }
                if (bufferPos == 2) {
                    int color = bitmap.getPixel(x, y);
                    int r = Color.red(color);
                    int g = Color.green(color);
                    int b = Color.blue(color);
                    int r2 = r % 2 == 1 - buffer[0] ? r + 1 : r;
                    int g2 = g % 2 == 1 - buffer[1] ? g + 1 : g;
                    int b2 = b % 2 == 1 - buffer[2] ? b + 1 : b;
                    if (r2 == 256) {
                        r2 = 254;
                    }
                    if (g2 == 256) {
                        g2 = 254;
                    }
                    if (b2 == 256) {
                        b2 = 254;
                    }
                    outBitmap.setPixel(x, y, Color.argb(255, r2, g2, b2));
                    x++;
                    if (x == width) {
                        x = 0;
                        y++;
                    }
                    bufferPos = 0;
                } else {
                    bufferPos++;
                }
                j++;
                bitmap = inBitmap;
                bArr = header;
            }
            i++;
            bitmap = inBitmap;
            bArr = header;
        }
        return outBitmap;
    }

    private static byte[] decodeBitmapToByteArray(Bitmap inBitmap, int offset, int length) {
        Bitmap bitmap;
        int i = offset;
        byte[] bytes = new byte[length];
        int width = inBitmap.getWidth();
        int height = inBitmap.getHeight();
        int bitNo = 0;
        int byteNo = 0;
        int i2 = 3;
        int[] bitBuffer = new int[3];
        int y = 0;
        while (y < height) {
            int i3;
            int x = 0;
            while (x < width) {
                int color = inBitmap.getPixel(x, y);
                bitBuffer[0] = Color.red(color) % 2;
                bitBuffer[1] = Color.green(color) % 2;
                bitBuffer[2] = Color.blue(color) % 2;
                int i4 = 0;
                for (i2 = 
/*
Method generation error in method: com.miui.whetstone.steganography.BitmapEncoder.decodeBitmapToByteArray(android.graphics.Bitmap, int, int):byte[], dex: classes3.dex
jadx.core.utils.exceptions.CodegenException: Error generate insn: PHI: (r7_2 'i2' int) = (r7_1 'i2' int), (r7_16 'i2' int) binds: {(r7_1 'i2' int)=B:2:0x0016, (r7_16 'i2' int)=B:20:0x0073} in method: com.miui.whetstone.steganography.BitmapEncoder.decodeBitmapToByteArray(android.graphics.Bitmap, int, int):byte[], dex: classes3.dex
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:228)
	at jadx.core.codegen.RegionGen.makeLoop(RegionGen.java:185)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:89)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:95)
	at jadx.core.codegen.RegionGen.makeLoop(RegionGen.java:220)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:89)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:95)
	at jadx.core.codegen.RegionGen.makeLoop(RegionGen.java:220)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:89)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:183)
	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:321)
	at jadx.core.codegen.ClassGen.addMethods(ClassGen.java:259)
	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:221)
	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:111)
	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:77)
	at jadx.core.codegen.CodeGen.visit(CodeGen.java:10)
	at jadx.core.ProcessClass.process(ProcessClass.java:38)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
Caused by: jadx.core.utils.exceptions.CodegenException: PHI can be used only in fallback mode
	at jadx.core.codegen.InsnGen.fallbackOnlyInsn(InsnGen.java:539)
	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:511)
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:222)
	... 24 more

*/

    public static String printBinaryString(byte[] bytes) {
        String s = "";
        for (byte b : bytes) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(s);
            stringBuilder.append("");
            stringBuilder.append(b);
            stringBuilder.append(",");
            s = stringBuilder.toString();
        }
        return s;
    }
}
