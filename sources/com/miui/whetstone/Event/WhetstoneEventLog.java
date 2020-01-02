package com.miui.whetstone.Event;

import android.net.wifi.WifiEnterpriseConfig;
import android.util.Log;
import java.io.UnsupportedEncodingException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class WhetstoneEventLog {
    private static final int DATA_START = 24;
    private static final byte INT_TYPE = (byte) 0;
    private static final int LENGTH_OFFSET = 0;
    private static final byte LIST_TYPE = (byte) 3;
    private static final byte LONG_TYPE = (byte) 1;
    private static final int NANOSECONDS_OFFSET = 16;
    private static final int PAYLOAD_START = 20;
    private static final int PROCESS_OFFSET = 4;
    private static final int SECONDS_OFFSET = 12;
    private static final byte STRING_TYPE = (byte) 2;
    private static final String TAG = "WhetstoneEventLog";
    private static final int TAG_OFFSET = 20;
    private static final int THREAD_OFFSET = 8;
    private final ByteBuffer mBuffer;
    private Object mData = getData();
    private int mLength = 0;

    public WhetstoneEventLog(byte[] data) {
        this.mBuffer = ByteBuffer.wrap(data);
        this.mBuffer.order(ByteOrder.nativeOrder());
        Object obj = this.mData;
        if (obj instanceof Object[]) {
            this.mLength = ((Object[]) obj).length;
        } else if (obj instanceof Object) {
            this.mLength = 1;
        }
    }

    public void print() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getProcessId());
        String str = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        stringBuilder.append(str);
        stringBuilder.append(getTimeNanos());
        stringBuilder.append(str);
        stringBuilder.append(getTag());
        stringBuilder.append(str);
        stringBuilder.append(getData());
        Log.i(TAG, stringBuilder.toString());
    }

    public String getString(int pos) {
        Object temp = getObject(pos);
        if (temp == null || !(temp instanceof String)) {
            return null;
        }
        return (String) temp;
    }

    @Deprecated
    private String getParameter(CharSequence msg, int pos) {
        int start = -1;
        if (msg == null || pos <= 0) {
            return null;
        }
        int n = 0;
        int i = 0;
        while (i < msg.length()) {
            if (msg.charAt(i) == '[') {
                start = i;
            } else if (msg.charAt(i) == ']') {
                int end = i;
                if (n + 1 == pos && i > start + 1) {
                    return msg.subSequence(start + 1, i - 1).toString();
                }
                return null;
            } else if (start > 0 && msg.charAt(i) == ',' && -1 == -1) {
                n++;
                if (n == pos && i > start + 1) {
                    return msg.subSequence(start + 1, i - 1).toString();
                }
                start = i;
            }
            i++;
        }
        return null;
    }

    public Integer getInteger(int pos) {
        Object temp = getObject(pos);
        if (temp == null || !(temp instanceof Integer)) {
            return null;
        }
        return (Integer) temp;
    }

    public Long getLong(int pos) {
        Object temp = getObject(pos);
        if (temp == null || !(temp instanceof Long)) {
            return null;
        }
        return (Long) temp;
    }

    private Object getObject(int pos) {
        if (pos < 0 || pos >= this.mLength) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("invalid pos ");
            stringBuilder.append(pos);
            Log.e(TAG, stringBuilder.toString());
            return null;
        }
        Object obj = this.mData;
        if (obj instanceof Object[]) {
            obj = ((Object[]) obj)[pos];
        } else {
            obj = this.mData;
        }
        return obj;
    }

    public int getProcessId() {
        return this.mBuffer.getInt(4);
    }

    public int getThreadId() {
        return this.mBuffer.getInt(8);
    }

    public long getTimeNanos() {
        return (((long) this.mBuffer.getInt(12)) * 1000000000) + ((long) this.mBuffer.getInt(16));
    }

    public int getTag() {
        return this.mBuffer.getInt(20);
    }

    public synchronized Object getData() {
        Object obj;
        String str;
        StringBuilder stringBuilder;
        obj = null;
        try {
            this.mBuffer.limit(this.mBuffer.getShort(0) + 20);
            this.mBuffer.position(24);
            obj = decodeObject();
        } catch (IllegalArgumentException e) {
            str = TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Illegal entry payload: tag=");
            stringBuilder.append(getTag());
            Log.wtf(str, stringBuilder.toString(), e);
            return obj;
        } catch (BufferUnderflowException e2) {
            str = TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Truncated entry payload: tag=");
            stringBuilder.append(getTag());
            Log.wtf(str, stringBuilder.toString(), e2);
            return obj;
        }
        return obj;
    }

    private Object decodeObject() {
        byte type = this.mBuffer.get();
        if (type == (byte) 0) {
            return Integer.valueOf(this.mBuffer.getInt());
        }
        if (type == (byte) 1) {
            return Long.valueOf(this.mBuffer.getLong());
        }
        int length;
        if (type == (byte) 2) {
            try {
                length = this.mBuffer.getInt();
                int start = this.mBuffer.position();
                this.mBuffer.position(start + length);
                return new String(this.mBuffer.array(), start, length, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                Log.wtf(TAG, "UTF-8 is not supported", e);
                return null;
            }
        } else if (type == (byte) 3) {
            length = this.mBuffer.get();
            if (length < 0) {
                length += 256;
            }
            Object[] array = new Object[length];
            this.mLength = length;
            for (int i = 0; i < length; i++) {
                array[i] = decodeObject();
            }
            return array;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unknown entry type: ");
            stringBuilder.append(type);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }
}
