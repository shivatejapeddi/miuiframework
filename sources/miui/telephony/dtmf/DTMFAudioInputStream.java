package miui.telephony.dtmf;

import android.util.Log;
import java.io.IOException;
import java.io.InputStream;

public class DTMFAudioInputStream extends InputStream {
    private static final String LOG_TAG = "DTMFAudioInputStream";
    private byte[] mByteBuff;
    private int mReadedCnt;

    public DTMFAudioInputStream(byte[] bytes) {
        if (bytes == null) {
            Log.i(LOG_TAG, "parameter error");
            return;
        }
        this.mByteBuff = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            this.mByteBuff[i] = bytes[i];
        }
        this.mReadedCnt = 0;
    }

    public int read(byte[] buffer, int len) throws IOException {
        byte[] bArr = this.mByteBuff;
        if (bArr == null || len <= 0) {
            Log.i(LOG_TAG, "paramenter error:fail to get subdatalist");
            return -1;
        }
        int _len = len;
        int length = bArr.length;
        int i = this.mReadedCnt;
        if (length - i < len) {
            _len = bArr.length - i;
        }
        for (int i2 = 0; i2 < _len; i2++) {
            buffer[i2] = (byte) read();
        }
        return len;
    }

    public int read() throws IOException {
        int i = this.mReadedCnt;
        byte[] bArr = this.mByteBuff;
        if (i >= bArr.length) {
            return -1;
        }
        this.mReadedCnt = i + 1;
        return bArr[i];
    }
}
