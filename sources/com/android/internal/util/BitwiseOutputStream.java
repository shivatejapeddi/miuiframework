package com.android.internal.util;

import android.annotation.UnsupportedAppUsage;

public class BitwiseOutputStream {
    private byte[] mBuf;
    private int mEnd;
    private int mPos = 0;

    public static class AccessException extends Exception {
        public AccessException(String s) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("BitwiseOutputStream access failed: ");
            stringBuilder.append(s);
            super(stringBuilder.toString());
        }
    }

    @UnsupportedAppUsage
    public BitwiseOutputStream(int startingLength) {
        this.mBuf = new byte[startingLength];
        this.mEnd = startingLength << 3;
    }

    @UnsupportedAppUsage
    public byte[] toByteArray() {
        int i = this.mPos;
        int len = (i >>> 3) + ((i & 7) > 0 ? 1 : 0);
        byte[] newBuf = new byte[len];
        System.arraycopy(this.mBuf, 0, newBuf, 0, len);
        return newBuf;
    }

    private void possExpand(int bits) {
        int i = this.mPos;
        int i2 = i + bits;
        int i3 = this.mEnd;
        if (i2 >= i3) {
            byte[] newBuf = new byte[((i + bits) >>> 2)];
            System.arraycopy(this.mBuf, 0, newBuf, 0, i3 >>> 3);
            this.mBuf = newBuf;
            this.mEnd = newBuf.length << 3;
        }
    }

    @UnsupportedAppUsage
    public void write(int bits, int data) throws AccessException {
        if (bits < 0 || bits > 8) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("illegal write (");
            stringBuilder.append(bits);
            stringBuilder.append(" bits)");
            throw new AccessException(stringBuilder.toString());
        }
        possExpand(bits);
        data &= -1 >>> (32 - bits);
        int i = this.mPos;
        int index = i >>> 3;
        int offset = (16 - (i & 7)) - bits;
        data <<= offset;
        this.mPos = i + bits;
        byte[] bArr = this.mBuf;
        bArr[index] = (byte) (bArr[index] | (data >>> 8));
        if (offset < 8) {
            int i2 = index + 1;
            bArr[i2] = (byte) (bArr[i2] | (data & 255));
        }
    }

    @UnsupportedAppUsage
    public void writeByteArray(int bits, byte[] arr) throws AccessException {
        for (int i = 0; i < arr.length; i++) {
            int increment = Math.min(8, bits - (i << 3));
            if (increment > 0) {
                write(increment, (byte) (arr[i] >>> (8 - increment)));
            }
        }
    }

    public void skip(int bits) {
        possExpand(bits);
        this.mPos += bits;
    }
}
