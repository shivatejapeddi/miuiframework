package android.media;

import java.io.IOException;
import java.io.InputStream;

public final class ResampleInputStream extends InputStream {
    private static final String TAG = "ResampleInputStream";
    private static final int mFirLength = 29;
    private byte[] mBuf;
    private int mBufCount;
    private InputStream mInputStream;
    private final byte[] mOneByte = new byte[1];
    private final int mRateIn;
    private final int mRateOut;

    private static native void fir21(byte[] bArr, int i, byte[] bArr2, int i2, int i3);

    static {
        System.loadLibrary("media_jni");
    }

    public ResampleInputStream(InputStream inputStream, int rateIn, int rateOut) {
        if (rateIn == rateOut * 2) {
            this.mInputStream = inputStream;
            this.mRateIn = 2;
            this.mRateOut = 1;
            return;
        }
        throw new IllegalArgumentException("only support 2:1 at the moment");
    }

    public int read() throws IOException {
        return read(this.mOneByte, 0, 1) == 1 ? this.mOneByte[0] & 255 : -1;
    }

    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    public int read(byte[] b, int offset, int length) throws IOException {
        if (this.mInputStream != null) {
            int nIn = ((((length / 2) * this.mRateIn) / this.mRateOut) + 29) * 2;
            byte[] bArr = this.mBuf;
            if (bArr == null) {
                this.mBuf = new byte[nIn];
            } else if (nIn > bArr.length) {
                byte[] bf = new byte[nIn];
                System.arraycopy(bArr, 0, bf, 0, this.mBufCount);
                this.mBuf = bf;
            }
            while (true) {
                int n = this.mBufCount;
                int len = ((((n / 2) - 29) * this.mRateOut) / this.mRateIn) * 2;
                if (len > 0) {
                    length = len < length ? len : (length / 2) * 2;
                    fir21(this.mBuf, 0, b, offset, length / 2);
                    n = (this.mRateIn * length) / this.mRateOut;
                    this.mBufCount -= n;
                    len = this.mBufCount;
                    if (len > 0) {
                        byte[] bArr2 = this.mBuf;
                        System.arraycopy(bArr2, n, bArr2, 0, len);
                    }
                    return length;
                }
                InputStream inputStream = this.mInputStream;
                byte[] bArr3 = this.mBuf;
                n = inputStream.read(bArr3, n, bArr3.length - n);
                if (n == -1) {
                    return -1;
                }
                this.mBufCount += n;
            }
        } else {
            throw new IllegalStateException("not open");
        }
    }

    public void close() throws IOException {
        try {
            if (this.mInputStream != null) {
                this.mInputStream.close();
            }
            this.mInputStream = null;
        } catch (Throwable th) {
            this.mInputStream = null;
        }
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        if (this.mInputStream != null) {
            close();
            throw new IllegalStateException("someone forgot to close ResampleInputStream");
        }
    }
}
