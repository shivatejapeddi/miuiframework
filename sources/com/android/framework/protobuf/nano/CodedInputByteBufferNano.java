package com.android.framework.protobuf.nano;

import java.io.IOException;

public final class CodedInputByteBufferNano {
    private static final int DEFAULT_RECURSION_LIMIT = 64;
    private static final int DEFAULT_SIZE_LIMIT = 67108864;
    private final byte[] buffer;
    private int bufferPos;
    private int bufferSize;
    private int bufferSizeAfterLimit;
    private int bufferStart;
    private int currentLimit = Integer.MAX_VALUE;
    private int lastTag;
    private int recursionDepth;
    private int recursionLimit = 64;
    private int sizeLimit = 67108864;

    public static CodedInputByteBufferNano newInstance(byte[] buf) {
        return newInstance(buf, 0, buf.length);
    }

    public static CodedInputByteBufferNano newInstance(byte[] buf, int off, int len) {
        return new CodedInputByteBufferNano(buf, off, len);
    }

    public int readTag() throws IOException {
        if (isAtEnd()) {
            this.lastTag = 0;
            return 0;
        }
        this.lastTag = readRawVarint32();
        int i = this.lastTag;
        if (i != 0) {
            return i;
        }
        throw InvalidProtocolBufferNanoException.invalidTag();
    }

    public void checkLastTagWas(int value) throws InvalidProtocolBufferNanoException {
        if (this.lastTag != value) {
            throw InvalidProtocolBufferNanoException.invalidEndTag();
        }
    }

    public boolean skipField(int tag) throws IOException {
        int tagWireType = WireFormatNano.getTagWireType(tag);
        if (tagWireType == 0) {
            readInt32();
            return true;
        } else if (tagWireType == 1) {
            readRawLittleEndian64();
            return true;
        } else if (tagWireType == 2) {
            skipRawBytes(readRawVarint32());
            return true;
        } else if (tagWireType == 3) {
            skipMessage();
            checkLastTagWas(WireFormatNano.makeTag(WireFormatNano.getTagFieldNumber(tag), 4));
            return true;
        } else if (tagWireType == 4) {
            return false;
        } else {
            if (tagWireType == 5) {
                readRawLittleEndian32();
                return true;
            }
            throw InvalidProtocolBufferNanoException.invalidWireType();
        }
    }

    public void skipMessage() throws IOException {
        while (true) {
            int tag = readTag();
            if (tag == 0 || !skipField(tag)) {
                return;
            }
        }
    }

    public double readDouble() throws IOException {
        return Double.longBitsToDouble(readRawLittleEndian64());
    }

    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readRawLittleEndian32());
    }

    public long readUInt64() throws IOException {
        return readRawVarint64();
    }

    public long readInt64() throws IOException {
        return readRawVarint64();
    }

    public int readInt32() throws IOException {
        return readRawVarint32();
    }

    public long readFixed64() throws IOException {
        return readRawLittleEndian64();
    }

    public int readFixed32() throws IOException {
        return readRawLittleEndian32();
    }

    public boolean readBool() throws IOException {
        return readRawVarint32() != 0;
    }

    public String readString() throws IOException {
        int size = readRawVarint32();
        int i = this.bufferSize;
        int i2 = this.bufferPos;
        if (size > i - i2 || size <= 0) {
            return new String(readRawBytes(size), InternalNano.UTF_8);
        }
        String result = new String(this.buffer, i2, size, InternalNano.UTF_8);
        this.bufferPos += size;
        return result;
    }

    public void readGroup(MessageNano msg, int fieldNumber) throws IOException {
        int i = this.recursionDepth;
        if (i < this.recursionLimit) {
            this.recursionDepth = i + 1;
            msg.mergeFrom(this);
            checkLastTagWas(WireFormatNano.makeTag(fieldNumber, 4));
            this.recursionDepth--;
            return;
        }
        throw InvalidProtocolBufferNanoException.recursionLimitExceeded();
    }

    public void readMessage(MessageNano msg) throws IOException {
        int length = readRawVarint32();
        if (this.recursionDepth < this.recursionLimit) {
            int oldLimit = pushLimit(length);
            this.recursionDepth++;
            msg.mergeFrom(this);
            checkLastTagWas(0);
            this.recursionDepth--;
            popLimit(oldLimit);
            return;
        }
        throw InvalidProtocolBufferNanoException.recursionLimitExceeded();
    }

    public byte[] readBytes() throws IOException {
        int size = readRawVarint32();
        int i = this.bufferSize;
        int i2 = this.bufferPos;
        if (size <= i - i2 && size > 0) {
            byte[] result = new byte[size];
            System.arraycopy(this.buffer, i2, result, 0, size);
            this.bufferPos += size;
            return result;
        } else if (size == 0) {
            return WireFormatNano.EMPTY_BYTES;
        } else {
            return readRawBytes(size);
        }
    }

    public int readUInt32() throws IOException {
        return readRawVarint32();
    }

    public int readEnum() throws IOException {
        return readRawVarint32();
    }

    public int readSFixed32() throws IOException {
        return readRawLittleEndian32();
    }

    public long readSFixed64() throws IOException {
        return readRawLittleEndian64();
    }

    public int readSInt32() throws IOException {
        return decodeZigZag32(readRawVarint32());
    }

    public long readSInt64() throws IOException {
        return decodeZigZag64(readRawVarint64());
    }

    public int readRawVarint32() throws IOException {
        byte tmp = readRawByte();
        if (tmp >= (byte) 0) {
            return tmp;
        }
        int result = tmp & 127;
        byte readRawByte = readRawByte();
        tmp = readRawByte;
        if (readRawByte >= (byte) 0) {
            result |= tmp << 7;
        } else {
            result |= (tmp & 127) << 7;
            readRawByte = readRawByte();
            tmp = readRawByte;
            if (readRawByte >= (byte) 0) {
                result |= tmp << 14;
            } else {
                result |= (tmp & 127) << 14;
                readRawByte = readRawByte();
                tmp = readRawByte;
                if (readRawByte >= (byte) 0) {
                    result |= tmp << 21;
                } else {
                    result |= (tmp & 127) << 21;
                    readRawByte = readRawByte();
                    result |= readRawByte << 28;
                    if (readRawByte < (byte) 0) {
                        for (int i = 0; i < 5; i++) {
                            if (readRawByte() >= (byte) 0) {
                                return result;
                            }
                        }
                        throw InvalidProtocolBufferNanoException.malformedVarint();
                    }
                }
            }
        }
        return result;
    }

    public long readRawVarint64() throws IOException {
        long result = 0;
        for (int shift = 0; shift < 64; shift += 7) {
            byte b = readRawByte();
            result |= ((long) (b & 127)) << shift;
            if ((b & 128) == 0) {
                return result;
            }
        }
        throw InvalidProtocolBufferNanoException.malformedVarint();
    }

    public int readRawLittleEndian32() throws IOException {
        return (((readRawByte() & 255) | ((readRawByte() & 255) << 8)) | ((readRawByte() & 255) << 16)) | ((readRawByte() & 255) << 24);
    }

    public long readRawLittleEndian64() throws IOException {
        return (((((((((long) readRawByte()) & 255) | ((((long) readRawByte()) & 255) << 8)) | ((((long) readRawByte()) & 255) << 16)) | ((((long) readRawByte()) & 255) << 24)) | ((((long) readRawByte()) & 255) << 32)) | ((((long) readRawByte()) & 255) << 40)) | ((((long) readRawByte()) & 255) << 48)) | ((255 & ((long) readRawByte())) << 56);
    }

    public static int decodeZigZag32(int n) {
        return (n >>> 1) ^ (-(n & 1));
    }

    public static long decodeZigZag64(long n) {
        return (n >>> 1) ^ (-(1 & n));
    }

    private CodedInputByteBufferNano(byte[] buffer, int off, int len) {
        this.buffer = buffer;
        this.bufferStart = off;
        this.bufferSize = off + len;
        this.bufferPos = off;
    }

    public int setRecursionLimit(int limit) {
        if (limit >= 0) {
            int oldLimit = this.recursionLimit;
            this.recursionLimit = limit;
            return oldLimit;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Recursion limit cannot be negative: ");
        stringBuilder.append(limit);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public int setSizeLimit(int limit) {
        if (limit >= 0) {
            int oldLimit = this.sizeLimit;
            this.sizeLimit = limit;
            return oldLimit;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Size limit cannot be negative: ");
        stringBuilder.append(limit);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public void resetSizeCounter() {
    }

    public int pushLimit(int byteLimit) throws InvalidProtocolBufferNanoException {
        if (byteLimit >= 0) {
            byteLimit += this.bufferPos;
            int oldLimit = this.currentLimit;
            if (byteLimit <= oldLimit) {
                this.currentLimit = byteLimit;
                recomputeBufferSizeAfterLimit();
                return oldLimit;
            }
            throw InvalidProtocolBufferNanoException.truncatedMessage();
        }
        throw InvalidProtocolBufferNanoException.negativeSize();
    }

    private void recomputeBufferSizeAfterLimit() {
        this.bufferSize += this.bufferSizeAfterLimit;
        int bufferEnd = this.bufferSize;
        int i = this.currentLimit;
        if (bufferEnd > i) {
            this.bufferSizeAfterLimit = bufferEnd - i;
            this.bufferSize -= this.bufferSizeAfterLimit;
            return;
        }
        this.bufferSizeAfterLimit = 0;
    }

    public void popLimit(int oldLimit) {
        this.currentLimit = oldLimit;
        recomputeBufferSizeAfterLimit();
    }

    public int getBytesUntilLimit() {
        int i = this.currentLimit;
        if (i == Integer.MAX_VALUE) {
            return -1;
        }
        return i - this.bufferPos;
    }

    public boolean isAtEnd() {
        return this.bufferPos == this.bufferSize;
    }

    public int getPosition() {
        return this.bufferPos - this.bufferStart;
    }

    public int getAbsolutePosition() {
        return this.bufferPos;
    }

    public byte[] getBuffer() {
        return this.buffer;
    }

    public byte[] getData(int offset, int length) {
        if (length == 0) {
            return WireFormatNano.EMPTY_BYTES;
        }
        byte[] copy = new byte[length];
        System.arraycopy(this.buffer, this.bufferStart + offset, copy, 0, length);
        return copy;
    }

    public void rewindToPosition(int position) {
        int i = this.bufferPos;
        int i2 = this.bufferStart;
        StringBuilder stringBuilder;
        if (position > i - i2) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Position ");
            stringBuilder.append(position);
            stringBuilder.append(" is beyond current ");
            stringBuilder.append(this.bufferPos - this.bufferStart);
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (position >= 0) {
            this.bufferPos = i2 + position;
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Bad position ");
            stringBuilder.append(position);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public byte readRawByte() throws IOException {
        int i = this.bufferPos;
        if (i != this.bufferSize) {
            byte[] bArr = this.buffer;
            this.bufferPos = i + 1;
            return bArr[i];
        }
        throw InvalidProtocolBufferNanoException.truncatedMessage();
    }

    public byte[] readRawBytes(int size) throws IOException {
        if (size >= 0) {
            int i = this.bufferPos;
            int i2 = i + size;
            int i3 = this.currentLimit;
            if (i2 > i3) {
                skipRawBytes(i3 - i);
                throw InvalidProtocolBufferNanoException.truncatedMessage();
            } else if (size <= this.bufferSize - i) {
                byte[] bytes = new byte[size];
                System.arraycopy(this.buffer, i, bytes, 0, size);
                this.bufferPos += size;
                return bytes;
            } else {
                throw InvalidProtocolBufferNanoException.truncatedMessage();
            }
        }
        throw InvalidProtocolBufferNanoException.negativeSize();
    }

    public void skipRawBytes(int size) throws IOException {
        if (size >= 0) {
            int i = this.bufferPos;
            int i2 = i + size;
            int i3 = this.currentLimit;
            if (i2 > i3) {
                skipRawBytes(i3 - i);
                throw InvalidProtocolBufferNanoException.truncatedMessage();
            } else if (size <= this.bufferSize - i) {
                this.bufferPos = i + size;
                return;
            } else {
                throw InvalidProtocolBufferNanoException.truncatedMessage();
            }
        }
        throw InvalidProtocolBufferNanoException.negativeSize();
    }

    /* Access modifiers changed, original: 0000 */
    public Object readPrimitiveField(int type) throws IOException {
        switch (type) {
            case 1:
                return Double.valueOf(readDouble());
            case 2:
                return Float.valueOf(readFloat());
            case 3:
                return Long.valueOf(readInt64());
            case 4:
                return Long.valueOf(readUInt64());
            case 5:
                return Integer.valueOf(readInt32());
            case 6:
                return Long.valueOf(readFixed64());
            case 7:
                return Integer.valueOf(readFixed32());
            case 8:
                return Boolean.valueOf(readBool());
            case 9:
                return readString();
            case 12:
                return readBytes();
            case 13:
                return Integer.valueOf(readUInt32());
            case 14:
                return Integer.valueOf(readEnum());
            case 15:
                return Integer.valueOf(readSFixed32());
            case 16:
                return Long.valueOf(readSFixed64());
            case 17:
                return Integer.valueOf(readSInt32());
            case 18:
                return Long.valueOf(readSInt64());
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown type ");
                stringBuilder.append(type);
                throw new IllegalArgumentException(stringBuilder.toString());
        }
    }
}
