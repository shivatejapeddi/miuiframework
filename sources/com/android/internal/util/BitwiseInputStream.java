package com.android.internal.util;

import android.annotation.UnsupportedAppUsage;

public class BitwiseInputStream {
    private byte[] mBuf;
    private int mEnd;
    private int mPos = 0;

    public static class AccessException extends Exception {
        public AccessException(String s) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("BitwiseInputStream access failed: ");
            stringBuilder.append(s);
            super(stringBuilder.toString());
        }
    }

    @UnsupportedAppUsage
    public BitwiseInputStream(byte[] buf) {
        this.mBuf = buf;
        this.mEnd = buf.length << 3;
    }

    @UnsupportedAppUsage
    public int available() {
        return this.mEnd - this.mPos;
    }

    @UnsupportedAppUsage
    public int read(int bits) throws AccessException {
        int i = this.mPos;
        int index = i >>> 3;
        int offset = (16 - (i & 7)) - bits;
        if (bits < 0 || bits > 8 || i + bits > this.mEnd) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("illegal read (pos ");
            stringBuilder.append(this.mPos);
            stringBuilder.append(", end ");
            stringBuilder.append(this.mEnd);
            stringBuilder.append(", bits ");
            stringBuilder.append(bits);
            stringBuilder.append(")");
            throw new AccessException(stringBuilder.toString());
        }
        byte[] bArr = this.mBuf;
        int data = (bArr[index] & 255) << 8;
        if (offset < 8) {
            data |= bArr[index + 1] & 255;
        }
        i = (data >>> offset) & (-1 >>> (32 - bits));
        this.mPos += bits;
        return i;
    }

    @UnsupportedAppUsage
    public byte[] readByteArray(int bits) throws AccessException {
        int bytes = (bits >>> 3) + ((bits & 7) > 0 ? 1 : 0);
        byte[] arr = new byte[bytes];
        for (int i = 0; i < bytes; i++) {
            int increment = Math.min(8, bits - (i << 3));
            arr[i] = (byte) (read(increment) << (8 - increment));
        }
        return arr;
    }

    @UnsupportedAppUsage
    public void skip(int bits) throws AccessException {
        int i = this.mPos;
        if (i + bits <= this.mEnd) {
            this.mPos = i + bits;
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("illegal skip (pos ");
        stringBuilder.append(this.mPos);
        stringBuilder.append(", end ");
        stringBuilder.append(this.mEnd);
        stringBuilder.append(", bits ");
        stringBuilder.append(bits);
        stringBuilder.append(")");
        throw new AccessException(stringBuilder.toString());
    }
}
