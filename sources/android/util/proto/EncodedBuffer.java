package android.util.proto;

import android.net.wifi.WifiEnterpriseConfig;
import android.os.BatteryStats;
import android.util.Log;
import java.util.ArrayList;
import miui.view.MiuiHapticFeedbackConstants;

public final class EncodedBuffer {
    private static final String TAG = "EncodedBuffer";
    private int mBufferCount;
    private final ArrayList<byte[]> mBuffers;
    private final int mChunkSize;
    private int mReadBufIndex;
    private byte[] mReadBuffer;
    private int mReadIndex;
    private int mReadLimit;
    private int mReadableSize;
    private int mWriteBufIndex;
    private byte[] mWriteBuffer;
    private int mWriteIndex;

    public EncodedBuffer() {
        this(0);
    }

    public EncodedBuffer(int chunkSize) {
        this.mBuffers = new ArrayList();
        this.mReadLimit = -1;
        this.mReadableSize = -1;
        if (chunkSize <= 0) {
            chunkSize = 8192;
        }
        this.mChunkSize = chunkSize;
        this.mWriteBuffer = new byte[this.mChunkSize];
        this.mBuffers.add(this.mWriteBuffer);
        this.mBufferCount = 1;
    }

    public void startEditing() {
        int i = this.mWriteBufIndex * this.mChunkSize;
        int i2 = this.mWriteIndex;
        this.mReadableSize = i + i2;
        this.mReadLimit = i2;
        this.mWriteBuffer = (byte[]) this.mBuffers.get(0);
        this.mWriteIndex = 0;
        this.mWriteBufIndex = 0;
        this.mReadBuffer = this.mWriteBuffer;
        this.mReadBufIndex = 0;
        this.mReadIndex = 0;
    }

    public void rewindRead() {
        this.mReadBuffer = (byte[]) this.mBuffers.get(0);
        this.mReadBufIndex = 0;
        this.mReadIndex = 0;
    }

    public int getReadableSize() {
        return this.mReadableSize;
    }

    public int getSize() {
        return ((this.mBufferCount - 1) * this.mChunkSize) + this.mWriteIndex;
    }

    public int getReadPos() {
        return (this.mReadBufIndex * this.mChunkSize) + this.mReadIndex;
    }

    public void skipRead(int amount) {
        if (amount < 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("skipRead with negative amount=");
            stringBuilder.append(amount);
            throw new RuntimeException(stringBuilder.toString());
        } else if (amount != 0) {
            int i = this.mChunkSize;
            int i2 = this.mReadIndex;
            if (amount <= i - i2) {
                this.mReadIndex = i2 + amount;
            } else {
                amount -= i - i2;
                this.mReadIndex = amount % i;
                if (this.mReadIndex == 0) {
                    this.mReadIndex = i;
                    this.mReadBufIndex += amount / i;
                } else {
                    this.mReadBufIndex += (amount / i) + 1;
                }
                this.mReadBuffer = (byte[]) this.mBuffers.get(this.mReadBufIndex);
            }
        }
    }

    public byte readRawByte() {
        int i = this.mReadBufIndex;
        int i2 = this.mBufferCount;
        if (i > i2 || (i == i2 - 1 && this.mReadIndex >= this.mReadLimit)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Trying to read too much data mReadBufIndex=");
            stringBuilder.append(this.mReadBufIndex);
            stringBuilder.append(" mBufferCount=");
            stringBuilder.append(this.mBufferCount);
            stringBuilder.append(" mReadIndex=");
            stringBuilder.append(this.mReadIndex);
            stringBuilder.append(" mReadLimit=");
            stringBuilder.append(this.mReadLimit);
            throw new IndexOutOfBoundsException(stringBuilder.toString());
        }
        if (this.mReadIndex >= this.mChunkSize) {
            this.mReadBufIndex++;
            this.mReadBuffer = (byte[]) this.mBuffers.get(this.mReadBufIndex);
            this.mReadIndex = 0;
        }
        byte[] bArr = this.mReadBuffer;
        i2 = this.mReadIndex;
        this.mReadIndex = i2 + 1;
        return bArr[i2];
    }

    public long readRawUnsigned() {
        int bits = 0;
        long result = 0;
        while (true) {
            byte b = readRawByte();
            result |= ((long) (b & 127)) << bits;
            if ((b & 128) == 0) {
                return result;
            }
            bits += 7;
            if (bits > 64) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Varint too long -- ");
                stringBuilder.append(getDebugString());
                throw new ProtoParseException(stringBuilder.toString());
            }
        }
    }

    public int readRawFixed32() {
        return (((readRawByte() & 255) | ((readRawByte() & 255) << 8)) | ((readRawByte() & 255) << 16)) | ((readRawByte() & 255) << 24);
    }

    private void nextWriteBuffer() {
        this.mWriteBufIndex++;
        int i = this.mWriteBufIndex;
        if (i >= this.mBufferCount) {
            this.mWriteBuffer = new byte[this.mChunkSize];
            this.mBuffers.add(this.mWriteBuffer);
            this.mBufferCount++;
        } else {
            this.mWriteBuffer = (byte[]) this.mBuffers.get(i);
        }
        this.mWriteIndex = 0;
    }

    public void writeRawByte(byte val) {
        if (this.mWriteIndex >= this.mChunkSize) {
            nextWriteBuffer();
        }
        byte[] bArr = this.mWriteBuffer;
        int i = this.mWriteIndex;
        this.mWriteIndex = i + 1;
        bArr[i] = val;
    }

    public static int getRawVarint32Size(int val) {
        if ((val & -128) == 0) {
            return 1;
        }
        if ((val & -16384) == 0) {
            return 2;
        }
        if ((-2097152 & val) == 0) {
            return 3;
        }
        if ((MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_MASK & val) == 0) {
            return 4;
        }
        return 5;
    }

    public void writeRawVarint32(int val) {
        while ((val & -128) != 0) {
            writeRawByte((byte) ((val & 127) | 128));
            val >>>= 7;
        }
        writeRawByte((byte) val);
    }

    public static int getRawZigZag32Size(int val) {
        return getRawVarint32Size(zigZag32(val));
    }

    public void writeRawZigZag32(int val) {
        writeRawVarint32(zigZag32(val));
    }

    public static int getRawVarint64Size(long val) {
        if ((-128 & val) == 0) {
            return 1;
        }
        if ((-16384 & val) == 0) {
            return 2;
        }
        if ((-2097152 & val) == 0) {
            return 3;
        }
        if ((-268435456 & val) == 0) {
            return 4;
        }
        if ((-34359738368L & val) == 0) {
            return 5;
        }
        if ((-4398046511104L & val) == 0) {
            return 6;
        }
        if ((-562949953421312L & val) == 0) {
            return 7;
        }
        if ((BatteryStats.STEP_LEVEL_MODIFIED_MODE_MASK & val) == 0) {
            return 8;
        }
        if ((Long.MIN_VALUE & val) == 0) {
            return 9;
        }
        return 10;
    }

    public void writeRawVarint64(long val) {
        while ((-128 & val) != 0) {
            writeRawByte((byte) ((int) ((127 & val) | 128)));
            val >>>= 7;
        }
        writeRawByte((byte) ((int) val));
    }

    public static int getRawZigZag64Size(long val) {
        return getRawVarint64Size(zigZag64(val));
    }

    public void writeRawZigZag64(long val) {
        writeRawVarint64(zigZag64(val));
    }

    public void writeRawFixed32(int val) {
        writeRawByte((byte) val);
        writeRawByte((byte) (val >> 8));
        writeRawByte((byte) (val >> 16));
        writeRawByte((byte) (val >> 24));
    }

    public void writeRawFixed64(long val) {
        writeRawByte((byte) ((int) val));
        writeRawByte((byte) ((int) (val >> 8)));
        writeRawByte((byte) ((int) (val >> 16)));
        writeRawByte((byte) ((int) (val >> 24)));
        writeRawByte((byte) ((int) (val >> 32)));
        writeRawByte((byte) ((int) (val >> 40)));
        writeRawByte((byte) ((int) (val >> 48)));
        writeRawByte((byte) ((int) (val >> 56)));
    }

    public void writeRawBuffer(byte[] val) {
        if (val != null && val.length > 0) {
            writeRawBuffer(val, 0, val.length);
        }
    }

    public void writeRawBuffer(byte[] val, int offset, int length) {
        if (val != null) {
            int i = this.mChunkSize;
            int i2 = this.mWriteIndex;
            i = length < i - i2 ? length : i - i2;
            if (i > 0) {
                System.arraycopy(val, offset, this.mWriteBuffer, this.mWriteIndex, i);
                this.mWriteIndex += i;
                length -= i;
                offset += i;
            }
            while (length > 0) {
                nextWriteBuffer();
                i2 = this.mChunkSize;
                if (length < i2) {
                    i2 = length;
                }
                i = i2;
                System.arraycopy(val, offset, this.mWriteBuffer, this.mWriteIndex, i);
                this.mWriteIndex += i;
                length -= i;
                offset += i;
            }
        }
    }

    public void writeFromThisBuffer(int srcOffset, int size) {
        if (this.mReadLimit >= 0) {
            int writePos = getWritePos();
            String str = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
            String str2 = " size=";
            StringBuilder stringBuilder;
            if (srcOffset < writePos) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Can only move forward in the buffer -- srcOffset=");
                stringBuilder.append(srcOffset);
                stringBuilder.append(str2);
                stringBuilder.append(size);
                stringBuilder.append(str);
                stringBuilder.append(getDebugString());
                throw new IllegalArgumentException(stringBuilder.toString());
            } else if (srcOffset + size > this.mReadableSize) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Trying to move more data than there is -- srcOffset=");
                stringBuilder.append(srcOffset);
                stringBuilder.append(str2);
                stringBuilder.append(size);
                stringBuilder.append(str);
                stringBuilder.append(getDebugString());
                throw new IllegalArgumentException(stringBuilder.toString());
            } else if (size != 0) {
                writePos = this.mWriteBufIndex;
                int i = this.mChunkSize;
                int i2 = writePos * i;
                int i3 = this.mWriteIndex;
                if (srcOffset != i2 + i3) {
                    writePos = srcOffset / i;
                    byte[] readBuffer = (byte[]) this.mBuffers.get(writePos);
                    i2 = srcOffset % this.mChunkSize;
                    while (size > 0) {
                        if (this.mWriteIndex >= this.mChunkSize) {
                            nextWriteBuffer();
                        }
                        if (i2 >= this.mChunkSize) {
                            writePos++;
                            readBuffer = (byte[]) this.mBuffers.get(writePos);
                            i2 = 0;
                        }
                        i3 = this.mChunkSize;
                        int amt = Math.min(size, Math.min(i3 - this.mWriteIndex, i3 - i2));
                        System.arraycopy(readBuffer, i2, this.mWriteBuffer, this.mWriteIndex, amt);
                        this.mWriteIndex += amt;
                        i2 += amt;
                        size -= amt;
                    }
                } else if (size <= i - i3) {
                    this.mWriteIndex = i3 + size;
                } else {
                    size -= i - i3;
                    this.mWriteIndex = size % i;
                    if (this.mWriteIndex == 0) {
                        this.mWriteIndex = i;
                        this.mWriteBufIndex = writePos + (size / i);
                    } else {
                        this.mWriteBufIndex = writePos + ((size / i) + 1);
                    }
                    this.mWriteBuffer = (byte[]) this.mBuffers.get(this.mWriteBufIndex);
                }
                return;
            } else {
                return;
            }
        }
        throw new IllegalStateException("writeFromThisBuffer before startEditing");
    }

    public int getWritePos() {
        return (this.mWriteBufIndex * this.mChunkSize) + this.mWriteIndex;
    }

    public void rewindWriteTo(int writePos) {
        if (writePos <= getWritePos()) {
            int i = this.mChunkSize;
            this.mWriteBufIndex = writePos / i;
            this.mWriteIndex = writePos % i;
            if (this.mWriteIndex == 0) {
                int i2 = this.mWriteBufIndex;
                if (i2 != 0) {
                    this.mWriteIndex = i;
                    this.mWriteBufIndex = i2 - 1;
                }
            }
            this.mWriteBuffer = (byte[]) this.mBuffers.get(this.mWriteBufIndex);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("rewindWriteTo only can go backwards");
        stringBuilder.append(writePos);
        throw new RuntimeException(stringBuilder.toString());
    }

    public int getRawFixed32At(int pos) {
        byte[] bArr = (byte[]) this.mBuffers.get(pos / this.mChunkSize);
        int i = this.mChunkSize;
        int i2 = bArr[pos % i] & 255;
        byte[] bArr2 = (byte[]) this.mBuffers.get((pos + 1) / i);
        int i3 = pos + 1;
        int i4 = this.mChunkSize;
        i2 |= (bArr2[i3 % i4] & 255) << 8;
        bArr2 = (byte[]) this.mBuffers.get((pos + 2) / i4);
        i3 = pos + 2;
        i4 = this.mChunkSize;
        return (i2 | ((bArr2[i3 % i4] & 255) << 16)) | ((((byte[]) this.mBuffers.get((pos + 3) / i4))[(pos + 3) % this.mChunkSize] & 255) << 24);
    }

    public void editRawFixed32(int pos, int val) {
        byte[] bArr = (byte[]) this.mBuffers.get(pos / this.mChunkSize);
        int i = this.mChunkSize;
        bArr[pos % i] = (byte) val;
        bArr = (byte[]) this.mBuffers.get((pos + 1) / i);
        i = pos + 1;
        int i2 = this.mChunkSize;
        bArr[i % i2] = (byte) (val >> 8);
        bArr = (byte[]) this.mBuffers.get((pos + 2) / i2);
        i = pos + 2;
        i2 = this.mChunkSize;
        bArr[i % i2] = (byte) (val >> 16);
        ((byte[]) this.mBuffers.get((pos + 3) / i2))[(pos + 3) % this.mChunkSize] = (byte) (val >> 24);
    }

    private static int zigZag32(int val) {
        return (val << 1) ^ (val >> 31);
    }

    private static long zigZag64(long val) {
        return (val << 1) ^ (val >> 63);
    }

    public byte[] getBytes(int size) {
        byte[] result = new byte[size];
        int bufCount = size / this.mChunkSize;
        int writeIndex = 0;
        int bufIndex = 0;
        while (bufIndex < bufCount) {
            System.arraycopy(this.mBuffers.get(bufIndex), 0, result, writeIndex, this.mChunkSize);
            writeIndex += this.mChunkSize;
            bufIndex++;
        }
        int lastSize = size - (this.mChunkSize * bufCount);
        if (lastSize > 0) {
            System.arraycopy(this.mBuffers.get(bufIndex), 0, result, writeIndex, lastSize);
        }
        return result;
    }

    public int getChunkCount() {
        return this.mBuffers.size();
    }

    public int getWriteIndex() {
        return this.mWriteIndex;
    }

    public int getWriteBufIndex() {
        return this.mWriteBufIndex;
    }

    public String getDebugString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("EncodedBuffer( mChunkSize=");
        stringBuilder.append(this.mChunkSize);
        stringBuilder.append(" mBuffers.size=");
        stringBuilder.append(this.mBuffers.size());
        stringBuilder.append(" mBufferCount=");
        stringBuilder.append(this.mBufferCount);
        stringBuilder.append(" mWriteIndex=");
        stringBuilder.append(this.mWriteIndex);
        stringBuilder.append(" mWriteBufIndex=");
        stringBuilder.append(this.mWriteBufIndex);
        stringBuilder.append(" mReadBufIndex=");
        stringBuilder.append(this.mReadBufIndex);
        stringBuilder.append(" mReadIndex=");
        stringBuilder.append(this.mReadIndex);
        stringBuilder.append(" mReadableSize=");
        stringBuilder.append(this.mReadableSize);
        stringBuilder.append(" mReadLimit=");
        stringBuilder.append(this.mReadLimit);
        stringBuilder.append(" )");
        return stringBuilder.toString();
    }

    public void dumpBuffers(String tag) {
        int N = this.mBuffers.size();
        int start = 0;
        for (int i = 0; i < N; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("{");
            stringBuilder.append(i);
            stringBuilder.append("} ");
            start += dumpByteString(tag, stringBuilder.toString(), start, (byte[]) this.mBuffers.get(i));
        }
    }

    public static void dumpByteString(String tag, String prefix, byte[] buf) {
        dumpByteString(tag, prefix, 0, buf);
    }

    private static int dumpByteString(String tag, String prefix, int start, byte[] buf) {
        StringBuffer sb = new StringBuffer();
        int length = buf.length;
        for (int i = 0; i < length; i++) {
            if (i % 16 == 0) {
                if (i != 0) {
                    Log.d(tag, sb.toString());
                    sb = new StringBuffer();
                }
                sb.append(prefix);
                sb.append('[');
                sb.append(start + i);
                sb.append(']');
                sb.append(' ');
            } else {
                sb.append(' ');
            }
            byte b = buf[i];
            byte c = (byte) ((b >> 4) & 15);
            if (c < (byte) 10) {
                sb.append((char) (c + 48));
            } else {
                sb.append((char) (c + 87));
            }
            byte d = (byte) (b & 15);
            if (d < (byte) 10) {
                sb.append((char) (d + 48));
            } else {
                sb.append((char) (d + 87));
            }
        }
        Log.d(tag, sb.toString());
        return length;
    }
}
