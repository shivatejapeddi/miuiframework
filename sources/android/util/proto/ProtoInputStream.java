package android.util.proto;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public final class ProtoInputStream extends ProtoStream {
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    public static final int NO_MORE_FIELDS = -1;
    private static final byte STATE_FIELD_MISS = (byte) 4;
    private static final byte STATE_READING_PACKED = (byte) 2;
    private static final byte STATE_STARTED_FIELD_READ = (byte) 1;
    private byte[] mBuffer;
    private final int mBufferSize;
    private int mDepth;
    private int mDiscardedBytes;
    private int mEnd;
    private ArrayList<Long> mExpectedObjectTokenStack;
    private int mFieldNumber;
    private int mOffset;
    private int mPackedEnd;
    private byte mState;
    private InputStream mStream;
    private int mWireType;

    public ProtoInputStream(InputStream stream, int bufferSize) {
        this.mState = (byte) 0;
        this.mExpectedObjectTokenStack = null;
        this.mDepth = -1;
        this.mDiscardedBytes = 0;
        this.mOffset = 0;
        this.mEnd = 0;
        this.mPackedEnd = 0;
        this.mStream = stream;
        if (bufferSize > 0) {
            this.mBufferSize = bufferSize;
        } else {
            this.mBufferSize = 8192;
        }
        this.mBuffer = new byte[this.mBufferSize];
    }

    public ProtoInputStream(InputStream stream) {
        this(stream, 8192);
    }

    public ProtoInputStream(byte[] buffer) {
        this.mState = (byte) 0;
        this.mExpectedObjectTokenStack = null;
        this.mDepth = -1;
        this.mDiscardedBytes = 0;
        this.mOffset = 0;
        this.mEnd = 0;
        this.mPackedEnd = 0;
        this.mBufferSize = buffer.length;
        this.mEnd = buffer.length;
        this.mBuffer = buffer;
        this.mStream = null;
    }

    public int getFieldNumber() {
        return this.mFieldNumber;
    }

    public int getWireType() {
        if ((this.mState & 2) == 2) {
            return 2;
        }
        return this.mWireType;
    }

    public int getOffset() {
        return this.mOffset + this.mDiscardedBytes;
    }

    public int nextField() throws IOException {
        byte b = this.mState;
        if ((b & 4) == 4) {
            this.mState = (byte) (b & -5);
            return this.mFieldNumber;
        }
        if ((b & 1) == 1) {
            skip();
            this.mState = (byte) (this.mState & -2);
        }
        if ((this.mState & 2) == 2) {
            if (getOffset() < this.mPackedEnd) {
                this.mState = (byte) (this.mState | 1);
                return this.mFieldNumber;
            } else if (getOffset() == this.mPackedEnd) {
                this.mState = (byte) (this.mState & -3);
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unexpectedly reached end of packed field at offset 0x");
                stringBuilder.append(Integer.toHexString(this.mPackedEnd));
                stringBuilder.append(dumpDebugData());
                throw new ProtoParseException(stringBuilder.toString());
            }
        }
        if (this.mDepth < 0 || getOffset() != ProtoStream.getOffsetFromToken(((Long) this.mExpectedObjectTokenStack.get(this.mDepth)).longValue())) {
            readTag();
        } else {
            this.mFieldNumber = -1;
        }
        return this.mFieldNumber;
    }

    public boolean isNextField(long fieldId) throws IOException {
        if (nextField() == ((int) fieldId)) {
            return true;
        }
        this.mState = (byte) (this.mState | 4);
        return false;
    }

    public double readDouble(long fieldId) throws IOException {
        assertFreshData();
        assertFieldNumber(fieldId);
        checkPacked(fieldId);
        if (((int) ((ProtoStream.FIELD_TYPE_MASK & fieldId) >>> 32)) == 1) {
            assertWireType(1);
            double value = Double.longBitsToDouble(readFixed64());
            this.mState = (byte) (this.mState & -2);
            return value;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Requested field id (");
        stringBuilder.append(ProtoStream.getFieldIdString(fieldId));
        stringBuilder.append(") cannot be read as a double");
        stringBuilder.append(dumpDebugData());
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public float readFloat(long fieldId) throws IOException {
        assertFreshData();
        assertFieldNumber(fieldId);
        checkPacked(fieldId);
        if (((int) ((ProtoStream.FIELD_TYPE_MASK & fieldId) >>> 32)) == 2) {
            assertWireType(5);
            float value = Float.intBitsToFloat(readFixed32());
            this.mState = (byte) (this.mState & -2);
            return value;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Requested field id (");
        stringBuilder.append(ProtoStream.getFieldIdString(fieldId));
        stringBuilder.append(") is not a float");
        stringBuilder.append(dumpDebugData());
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public int readInt(long fieldId) throws IOException {
        assertFreshData();
        assertFieldNumber(fieldId);
        checkPacked(fieldId);
        int i = (int) ((ProtoStream.FIELD_TYPE_MASK & fieldId) >>> 32);
        if (i != 5) {
            if (i != 7) {
                if (i != 17) {
                    switch (i) {
                        case 13:
                        case 14:
                            break;
                        case 15:
                            break;
                        default:
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Requested field id (");
                            stringBuilder.append(ProtoStream.getFieldIdString(fieldId));
                            stringBuilder.append(") is not an int");
                            stringBuilder.append(dumpDebugData());
                            throw new IllegalArgumentException(stringBuilder.toString());
                    }
                }
                assertWireType(0);
                i = decodeZigZag32((int) readVarint());
                this.mState = (byte) (this.mState & -2);
                return i;
            }
            assertWireType(5);
            i = readFixed32();
            this.mState = (byte) (this.mState & -2);
            return i;
        }
        assertWireType(0);
        i = (int) readVarint();
        this.mState = (byte) (this.mState & -2);
        return i;
    }

    public long readLong(long fieldId) throws IOException {
        long value;
        assertFreshData();
        assertFieldNumber(fieldId);
        checkPacked(fieldId);
        int i = (int) ((ProtoStream.FIELD_TYPE_MASK & fieldId) >>> 32);
        if (i == 3 || i == 4) {
            assertWireType(0);
            value = readVarint();
        } else if (i == 6 || i == 16) {
            assertWireType(1);
            value = readFixed64();
        } else if (i == 18) {
            assertWireType(0);
            value = decodeZigZag64(readVarint());
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Requested field id (");
            stringBuilder.append(ProtoStream.getFieldIdString(fieldId));
            stringBuilder.append(") is not an long");
            stringBuilder.append(dumpDebugData());
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        this.mState = (byte) (this.mState & -2);
        return value;
    }

    public boolean readBoolean(long fieldId) throws IOException {
        assertFreshData();
        assertFieldNumber(fieldId);
        checkPacked(fieldId);
        if (((int) ((ProtoStream.FIELD_TYPE_MASK & fieldId) >>> 32)) == 8) {
            boolean value = false;
            assertWireType(0);
            if (readVarint() != 0) {
                value = true;
            }
            this.mState = (byte) (this.mState & -2);
            return value;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Requested field id (");
        stringBuilder.append(ProtoStream.getFieldIdString(fieldId));
        stringBuilder.append(") is not an boolean");
        stringBuilder.append(dumpDebugData());
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public String readString(long fieldId) throws IOException {
        assertFreshData();
        assertFieldNumber(fieldId);
        if (((int) ((ProtoStream.FIELD_TYPE_MASK & fieldId) >>> 32)) == 9) {
            assertWireType(2);
            String value = readRawString((int) readVarint());
            this.mState = (byte) (this.mState & -2);
            return value;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Requested field id(");
        stringBuilder.append(ProtoStream.getFieldIdString(fieldId));
        stringBuilder.append(") is not an string");
        stringBuilder.append(dumpDebugData());
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public byte[] readBytes(long fieldId) throws IOException {
        assertFreshData();
        assertFieldNumber(fieldId);
        int i = (int) ((ProtoStream.FIELD_TYPE_MASK & fieldId) >>> 32);
        if (i == 11 || i == 12) {
            assertWireType(2);
            byte[] value = readRawBytes((int) readVarint());
            this.mState = (byte) (this.mState & -2);
            return value;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Requested field type (");
        stringBuilder.append(ProtoStream.getFieldIdString(fieldId));
        stringBuilder.append(") cannot be read as raw bytes");
        stringBuilder.append(dumpDebugData());
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public long start(long fieldId) throws IOException {
        assertFreshData();
        assertFieldNumber(fieldId);
        assertWireType(2);
        int messageSize = (int) readVarint();
        if (this.mExpectedObjectTokenStack == null) {
            this.mExpectedObjectTokenStack = new ArrayList();
        }
        int i = this.mDepth + 1;
        this.mDepth = i;
        if (i == this.mExpectedObjectTokenStack.size()) {
            this.mExpectedObjectTokenStack.add(Long.valueOf(ProtoStream.makeToken(0, (fieldId & ProtoStream.FIELD_COUNT_REPEATED) == ProtoStream.FIELD_COUNT_REPEATED, this.mDepth, (int) fieldId, getOffset() + messageSize)));
        } else {
            this.mExpectedObjectTokenStack.set(this.mDepth, Long.valueOf(ProtoStream.makeToken(0, (fieldId & ProtoStream.FIELD_COUNT_REPEATED) == ProtoStream.FIELD_COUNT_REPEATED, this.mDepth, (int) fieldId, getOffset() + messageSize)));
        }
        i = this.mDepth;
        if (i <= 0 || ProtoStream.getOffsetFromToken(((Long) this.mExpectedObjectTokenStack.get(i)).longValue()) <= ProtoStream.getOffsetFromToken(((Long) this.mExpectedObjectTokenStack.get(this.mDepth - 1)).longValue())) {
            this.mState = (byte) (this.mState & -2);
            return ((Long) this.mExpectedObjectTokenStack.get(this.mDepth)).longValue();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Embedded Object (");
        stringBuilder.append(ProtoStream.token2String(((Long) this.mExpectedObjectTokenStack.get(this.mDepth)).longValue()));
        stringBuilder.append(") ends after of parent Objects's (");
        stringBuilder.append(ProtoStream.token2String(((Long) this.mExpectedObjectTokenStack.get(this.mDepth - 1)).longValue()));
        stringBuilder.append(") end");
        stringBuilder.append(dumpDebugData());
        throw new ProtoParseException(stringBuilder.toString());
    }

    public void end(long token) {
        if (((Long) this.mExpectedObjectTokenStack.get(this.mDepth)).longValue() == token) {
            if (ProtoStream.getOffsetFromToken(((Long) this.mExpectedObjectTokenStack.get(this.mDepth)).longValue()) > getOffset()) {
                incOffset(ProtoStream.getOffsetFromToken(((Long) this.mExpectedObjectTokenStack.get(this.mDepth)).longValue()) - getOffset());
            }
            this.mDepth--;
            this.mState = (byte) (this.mState & -2);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("end token ");
        stringBuilder.append(token);
        stringBuilder.append(" does not match current message token ");
        stringBuilder.append(this.mExpectedObjectTokenStack.get(this.mDepth));
        stringBuilder.append(dumpDebugData());
        throw new ProtoParseException(stringBuilder.toString());
    }

    private void readTag() throws IOException {
        fillBuffer();
        if (this.mOffset >= this.mEnd) {
            this.mFieldNumber = -1;
            return;
        }
        int tag = (int) readVarint();
        this.mFieldNumber = tag >>> 3;
        this.mWireType = tag & 7;
        this.mState = (byte) (this.mState | 1);
    }

    public int decodeZigZag32(int n) {
        return (n >>> 1) ^ (-(n & 1));
    }

    public long decodeZigZag64(long n) {
        return (n >>> 1) ^ (-(1 & n));
    }

    private long readVarint() throws IOException {
        long value = 0;
        int shift = 0;
        while (true) {
            fillBuffer();
            int fragment = this.mEnd - this.mOffset;
            int i = 0;
            while (i < fragment) {
                byte b = this.mBuffer[this.mOffset + i];
                value |= (((long) b) & 127) << shift;
                if ((b & 128) == 0) {
                    incOffset(i + 1);
                    return value;
                }
                shift += 7;
                if (shift <= 63) {
                    i++;
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Varint is too large at offset 0x");
                    stringBuilder.append(Integer.toHexString(getOffset() + i));
                    stringBuilder.append(dumpDebugData());
                    throw new ProtoParseException(stringBuilder.toString());
                }
            }
            incOffset(fragment);
        }
    }

    private int readFixed32() throws IOException {
        int i;
        if (this.mOffset + 4 <= this.mEnd) {
            incOffset(4);
            byte[] bArr = this.mBuffer;
            i = this.mOffset;
            return ((bArr[i - 1] & 255) << 24) | (((bArr[i - 4] & 255) | ((bArr[i - 3] & 255) << 8)) | ((bArr[i - 2] & 255) << 16));
        }
        int value = 0;
        i = 0;
        int bytesLeft = 4;
        while (bytesLeft > 0) {
            fillBuffer();
            int i2 = this.mEnd;
            int i3 = this.mOffset;
            i2 = i2 - i3 < bytesLeft ? i2 - i3 : bytesLeft;
            incOffset(i2);
            bytesLeft -= i2;
            while (i2 > 0) {
                value |= (this.mBuffer[this.mOffset - i2] & 255) << i;
                i2--;
                i += 8;
            }
        }
        return value;
    }

    private long readFixed64() throws IOException {
        int i;
        if (this.mOffset + 8 <= this.mEnd) {
            incOffset(8);
            byte[] bArr = this.mBuffer;
            i = this.mOffset;
            return ((((long) bArr[i - 1]) & 255) << 56) | (((((((((long) bArr[i - 8]) & 255) | ((((long) bArr[i - 7]) & 255) << 8)) | ((((long) bArr[i - 6]) & 255) << 16)) | ((((long) bArr[i - 5]) & 255) << 24)) | ((((long) bArr[i - 4]) & 255) << 32)) | ((((long) bArr[i - 3]) & 255) << 40)) | ((((long) bArr[i - 2]) & 255) << 48));
        }
        long value = 0;
        i = 0;
        int bytesLeft = 8;
        while (bytesLeft > 0) {
            fillBuffer();
            int i2 = this.mEnd;
            int i3 = this.mOffset;
            i2 = i2 - i3 < bytesLeft ? i2 - i3 : bytesLeft;
            incOffset(i2);
            bytesLeft -= i2;
            while (i2 > 0) {
                value |= (((long) this.mBuffer[this.mOffset - i2]) & 255) << i;
                i2--;
                i += 8;
            }
        }
        return value;
    }

    private byte[] readRawBytes(int n) throws IOException {
        byte[] buffer = new byte[n];
        int pos = 0;
        while (true) {
            int i = this.mOffset;
            int i2 = (i + n) - pos;
            int fragment = this.mEnd;
            if (i2 > fragment) {
                fragment -= i;
                if (fragment > 0) {
                    System.arraycopy(this.mBuffer, i, buffer, pos, fragment);
                    incOffset(fragment);
                    pos += fragment;
                }
                fillBuffer();
                if (this.mOffset >= this.mEnd) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unexpectedly reached end of the InputStream at offset 0x");
                    stringBuilder.append(Integer.toHexString(this.mEnd));
                    stringBuilder.append(dumpDebugData());
                    throw new ProtoParseException(stringBuilder.toString());
                }
            } else {
                System.arraycopy(this.mBuffer, i, buffer, pos, n - pos);
                incOffset(n - pos);
                return buffer;
            }
        }
    }

    private String readRawString(int n) throws IOException {
        fillBuffer();
        int i = this.mOffset;
        int i2 = i + n;
        int stringHead = this.mEnd;
        String value;
        if (i2 <= stringHead) {
            value = new String(this.mBuffer, i, n, StandardCharsets.UTF_8);
            incOffset(n);
            return value;
        } else if (n > this.mBufferSize) {
            return new String(readRawBytes(n), 0, n, StandardCharsets.UTF_8);
        } else {
            stringHead -= i;
            byte[] bArr = this.mBuffer;
            System.arraycopy(bArr, i, bArr, 0, stringHead);
            this.mEnd = this.mStream.read(this.mBuffer, stringHead, n - stringHead) + stringHead;
            this.mDiscardedBytes += this.mOffset;
            this.mOffset = 0;
            value = new String(this.mBuffer, this.mOffset, n, StandardCharsets.UTF_8);
            incOffset(n);
            return value;
        }
    }

    private void fillBuffer() throws IOException {
        int i = this.mOffset;
        int i2 = this.mEnd;
        if (i >= i2) {
            InputStream inputStream = this.mStream;
            if (inputStream != null) {
                this.mOffset = i - i2;
                this.mDiscardedBytes += i2;
                i = this.mOffset;
                i2 = this.mBufferSize;
                if (i >= i2) {
                    i = (int) inputStream.skip((long) ((i / i2) * i2));
                    this.mDiscardedBytes += i;
                    this.mOffset -= i;
                }
                this.mEnd = this.mStream.read(this.mBuffer);
            }
        }
    }

    public void skip() throws IOException {
        if ((this.mState & 2) == 2) {
            incOffset(this.mPackedEnd - getOffset());
        } else {
            int i = this.mWireType;
            if (i == 0) {
                while (true) {
                    fillBuffer();
                    byte b = this.mBuffer[this.mOffset];
                    incOffset(1);
                    if ((b & 128) == 0) {
                        break;
                    }
                }
            } else if (i == 1) {
                incOffset(8);
            } else if (i == 2) {
                fillBuffer();
                incOffset((int) readVarint());
            } else if (i == 5) {
                incOffset(4);
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unexpected wire type: ");
                stringBuilder.append(this.mWireType);
                stringBuilder.append(" at offset 0x");
                stringBuilder.append(Integer.toHexString(this.mOffset));
                stringBuilder.append(dumpDebugData());
                throw new ProtoParseException(stringBuilder.toString());
            }
        }
        this.mState = (byte) (this.mState & -2);
    }

    private void incOffset(int n) {
        this.mOffset += n;
        if (this.mDepth >= 0 && getOffset() > ProtoStream.getOffsetFromToken(((Long) this.mExpectedObjectTokenStack.get(this.mDepth)).longValue())) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unexpectedly reached end of embedded object.  ");
            stringBuilder.append(ProtoStream.token2String(((Long) this.mExpectedObjectTokenStack.get(this.mDepth)).longValue()));
            stringBuilder.append(dumpDebugData());
            throw new ProtoParseException(stringBuilder.toString());
        }
    }

    private void checkPacked(long fieldId) throws IOException {
        if (this.mWireType == 2) {
            int length = (int) readVarint();
            this.mPackedEnd = getOffset() + length;
            this.mState = (byte) (2 | this.mState);
            String str = ") packed length ";
            String str2 = "Requested field id (";
            StringBuilder stringBuilder;
            switch ((int) ((ProtoStream.FIELD_TYPE_MASK & fieldId) >>> 32)) {
                case 1:
                case 6:
                case 16:
                    if (length % 8 == 0) {
                        this.mWireType = 1;
                        return;
                    }
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str2);
                    stringBuilder.append(ProtoStream.getFieldIdString(fieldId));
                    stringBuilder.append(str);
                    stringBuilder.append(length);
                    stringBuilder.append(" is not aligned for fixed64");
                    stringBuilder.append(dumpDebugData());
                    throw new IllegalArgumentException(stringBuilder.toString());
                case 2:
                case 7:
                case 15:
                    if (length % 4 == 0) {
                        this.mWireType = 5;
                        return;
                    }
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str2);
                    stringBuilder.append(ProtoStream.getFieldIdString(fieldId));
                    stringBuilder.append(str);
                    stringBuilder.append(length);
                    stringBuilder.append(" is not aligned for fixed32");
                    stringBuilder.append(dumpDebugData());
                    throw new IllegalArgumentException(stringBuilder.toString());
                case 3:
                case 4:
                case 5:
                case 8:
                case 13:
                case 14:
                case 17:
                case 18:
                    this.mWireType = 0;
                    return;
                default:
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(str2);
                    stringBuilder2.append(ProtoStream.getFieldIdString(fieldId));
                    stringBuilder2.append(") is not a packable field");
                    stringBuilder2.append(dumpDebugData());
                    throw new IllegalArgumentException(stringBuilder2.toString());
            }
        }
    }

    private void assertFieldNumber(long fieldId) {
        if (((int) fieldId) != this.mFieldNumber) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Requested field id (");
            stringBuilder.append(ProtoStream.getFieldIdString(fieldId));
            stringBuilder.append(") does not match current field number (0x");
            stringBuilder.append(Integer.toHexString(this.mFieldNumber));
            stringBuilder.append(") at offset 0x");
            stringBuilder.append(Integer.toHexString(getOffset()));
            stringBuilder.append(dumpDebugData());
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    private void assertWireType(int wireType) {
        if (wireType != this.mWireType) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Current wire type ");
            stringBuilder.append(ProtoStream.getWireTypeString(this.mWireType));
            stringBuilder.append(" does not match expected wire type ");
            stringBuilder.append(ProtoStream.getWireTypeString(wireType));
            stringBuilder.append(" at offset 0x");
            stringBuilder.append(Integer.toHexString(getOffset()));
            stringBuilder.append(dumpDebugData());
            throw new WireTypeMismatchException(stringBuilder.toString());
        }
    }

    private void assertFreshData() {
        if ((this.mState & 1) != 1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Attempting to read already read field at offset 0x");
            stringBuilder.append(Integer.toHexString(getOffset()));
            stringBuilder.append(dumpDebugData());
            throw new ProtoParseException(stringBuilder.toString());
        }
    }

    public String dumpDebugData() {
        StringBuilder sb = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nmFieldNumber : 0x");
        stringBuilder.append(Integer.toHexString(this.mFieldNumber));
        sb.append(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("\nmWireType : 0x");
        stringBuilder.append(Integer.toHexString(this.mWireType));
        sb.append(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("\nmState : 0x");
        stringBuilder.append(Integer.toHexString(this.mState));
        sb.append(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("\nmDiscardedBytes : 0x");
        stringBuilder.append(Integer.toHexString(this.mDiscardedBytes));
        sb.append(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("\nmOffset : 0x");
        stringBuilder.append(Integer.toHexString(this.mOffset));
        sb.append(stringBuilder.toString());
        sb.append("\nmExpectedObjectTokenStack : ");
        ArrayList arrayList = this.mExpectedObjectTokenStack;
        String str = "null";
        if (arrayList == null) {
            sb.append(str);
        } else {
            sb.append(arrayList);
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("\nmDepth : 0x");
        stringBuilder.append(Integer.toHexString(this.mDepth));
        sb.append(stringBuilder.toString());
        sb.append("\nmBuffer : ");
        byte[] bArr = this.mBuffer;
        if (bArr == null) {
            sb.append(str);
        } else {
            sb.append(bArr);
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("\nmBufferSize : 0x");
        stringBuilder.append(Integer.toHexString(this.mBufferSize));
        sb.append(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("\nmEnd : 0x");
        stringBuilder.append(Integer.toHexString(this.mEnd));
        sb.append(stringBuilder.toString());
        return sb.toString();
    }
}
