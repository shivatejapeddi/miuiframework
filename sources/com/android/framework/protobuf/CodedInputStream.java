package com.android.framework.protobuf;

import com.android.framework.protobuf.MessageLite.Builder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CodedInputStream {
    private static final int BUFFER_SIZE = 4096;
    private static final int DEFAULT_RECURSION_LIMIT = 100;
    private static final int DEFAULT_SIZE_LIMIT = 67108864;
    private final byte[] buffer;
    private final boolean bufferIsImmutable;
    private int bufferPos;
    private int bufferSize;
    private int bufferSizeAfterLimit;
    private int currentLimit = Integer.MAX_VALUE;
    private boolean enableAliasing = false;
    private final InputStream input;
    private int lastTag;
    private int recursionDepth;
    private int recursionLimit = 100;
    private RefillCallback refillCallback = null;
    private int sizeLimit = 67108864;
    private int totalBytesRetired;

    private interface RefillCallback {
        void onRefill();
    }

    private class SkippedDataSink implements RefillCallback {
        private ByteArrayOutputStream byteArrayStream;
        private int lastPos = CodedInputStream.this.bufferPos;

        private SkippedDataSink() {
        }

        public void onRefill() {
            if (this.byteArrayStream == null) {
                this.byteArrayStream = new ByteArrayOutputStream();
            }
            this.byteArrayStream.write(CodedInputStream.this.buffer, this.lastPos, CodedInputStream.this.bufferPos - this.lastPos);
            this.lastPos = 0;
        }

        /* Access modifiers changed, original: 0000 */
        public ByteBuffer getSkippedData() {
            ByteArrayOutputStream byteArrayOutputStream = this.byteArrayStream;
            if (byteArrayOutputStream == null) {
                return ByteBuffer.wrap(CodedInputStream.this.buffer, this.lastPos, CodedInputStream.this.bufferPos - this.lastPos);
            }
            byteArrayOutputStream.write(CodedInputStream.this.buffer, this.lastPos, CodedInputStream.this.bufferPos);
            return ByteBuffer.wrap(this.byteArrayStream.toByteArray());
        }
    }

    public static CodedInputStream newInstance(InputStream input) {
        return new CodedInputStream(input, 4096);
    }

    static CodedInputStream newInstance(InputStream input, int bufferSize) {
        return new CodedInputStream(input, bufferSize);
    }

    public static CodedInputStream newInstance(byte[] buf) {
        return newInstance(buf, 0, buf.length);
    }

    public static CodedInputStream newInstance(byte[] buf, int off, int len) {
        return newInstance(buf, off, len, false);
    }

    static CodedInputStream newInstance(byte[] buf, int off, int len, boolean bufferIsImmutable) {
        CodedInputStream result = new CodedInputStream(buf, off, len, bufferIsImmutable);
        try {
            result.pushLimit(len);
            return result;
        } catch (InvalidProtocolBufferException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public static CodedInputStream newInstance(ByteBuffer buf) {
        if (buf.hasArray()) {
            return newInstance(buf.array(), buf.arrayOffset() + buf.position(), buf.remaining());
        }
        ByteBuffer temp = buf.duplicate();
        byte[] buffer = new byte[temp.remaining()];
        temp.get(buffer);
        return newInstance(buffer);
    }

    public int readTag() throws IOException {
        if (isAtEnd()) {
            this.lastTag = 0;
            return 0;
        }
        this.lastTag = readRawVarint32();
        if (WireFormat.getTagFieldNumber(this.lastTag) != 0) {
            return this.lastTag;
        }
        throw InvalidProtocolBufferException.invalidTag();
    }

    public void checkLastTagWas(int value) throws InvalidProtocolBufferException {
        if (this.lastTag != value) {
            throw InvalidProtocolBufferException.invalidEndTag();
        }
    }

    public int getLastTag() {
        return this.lastTag;
    }

    public boolean skipField(int tag) throws IOException {
        int tagWireType = WireFormat.getTagWireType(tag);
        if (tagWireType == 0) {
            skipRawVarint();
            return true;
        } else if (tagWireType == 1) {
            skipRawBytes(8);
            return true;
        } else if (tagWireType == 2) {
            skipRawBytes(readRawVarint32());
            return true;
        } else if (tagWireType == 3) {
            skipMessage();
            checkLastTagWas(WireFormat.makeTag(WireFormat.getTagFieldNumber(tag), 4));
            return true;
        } else if (tagWireType == 4) {
            return false;
        } else {
            if (tagWireType == 5) {
                skipRawBytes(4);
                return true;
            }
            throw InvalidProtocolBufferException.invalidWireType();
        }
    }

    public boolean skipField(int tag, CodedOutputStream output) throws IOException {
        int tagWireType = WireFormat.getTagWireType(tag);
        long value;
        if (tagWireType == 0) {
            value = readInt64();
            output.writeRawVarint32(tag);
            output.writeUInt64NoTag(value);
            return true;
        } else if (tagWireType == 1) {
            value = readRawLittleEndian64();
            output.writeRawVarint32(tag);
            output.writeFixed64NoTag(value);
            return true;
        } else if (tagWireType == 2) {
            ByteString value2 = readBytes();
            output.writeRawVarint32(tag);
            output.writeBytesNoTag(value2);
            return true;
        } else if (tagWireType == 3) {
            output.writeRawVarint32(tag);
            skipMessage(output);
            tagWireType = WireFormat.makeTag(WireFormat.getTagFieldNumber(tag), 4);
            checkLastTagWas(tagWireType);
            output.writeRawVarint32(tagWireType);
            return true;
        } else if (tagWireType == 4) {
            return false;
        } else {
            if (tagWireType == 5) {
                tagWireType = readRawLittleEndian32();
                output.writeRawVarint32(tag);
                output.writeFixed32NoTag(tagWireType);
                return true;
            }
            throw InvalidProtocolBufferException.invalidWireType();
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

    public void skipMessage(CodedOutputStream output) throws IOException {
        while (true) {
            int tag = readTag();
            if (tag == 0 || !skipField(tag, output)) {
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
        return readRawVarint64() != 0;
    }

    public String readString() throws IOException {
        int size = readRawVarint32();
        int i = this.bufferSize;
        int i2 = this.bufferPos;
        String result;
        if (size <= i - i2 && size > 0) {
            result = new String(this.buffer, i2, size, Internal.UTF_8);
            this.bufferPos += size;
            return result;
        } else if (size == 0) {
            return "";
        } else {
            if (size > this.bufferSize) {
                return new String(readRawBytesSlowPath(size), Internal.UTF_8);
            }
            refillBuffer(size);
            result = new String(this.buffer, this.bufferPos, size, Internal.UTF_8);
            this.bufferPos += size;
            return result;
        }
    }

    public String readStringRequireUtf8() throws IOException {
        byte[] bytes;
        int pos;
        int size = readRawVarint32();
        int oldPos = this.bufferPos;
        if (size <= this.bufferSize - oldPos && size > 0) {
            bytes = this.buffer;
            this.bufferPos = oldPos + size;
            pos = oldPos;
        } else if (size == 0) {
            return "";
        } else {
            if (size <= this.bufferSize) {
                refillBuffer(size);
                bytes = this.buffer;
                pos = 0;
                this.bufferPos = 0 + size;
            } else {
                bytes = readRawBytesSlowPath(size);
                pos = 0;
            }
        }
        if (Utf8.isValidUtf8(bytes, pos, pos + size)) {
            return new String(bytes, pos, size, Internal.UTF_8);
        }
        throw InvalidProtocolBufferException.invalidUtf8();
    }

    public void readGroup(int fieldNumber, Builder builder, ExtensionRegistryLite extensionRegistry) throws IOException {
        int i = this.recursionDepth;
        if (i < this.recursionLimit) {
            this.recursionDepth = i + 1;
            builder.mergeFrom(this, extensionRegistry);
            checkLastTagWas(WireFormat.makeTag(fieldNumber, 4));
            this.recursionDepth--;
            return;
        }
        throw InvalidProtocolBufferException.recursionLimitExceeded();
    }

    public <T extends MessageLite> T readGroup(int fieldNumber, Parser<T> parser, ExtensionRegistryLite extensionRegistry) throws IOException {
        int i = this.recursionDepth;
        if (i < this.recursionLimit) {
            this.recursionDepth = i + 1;
            MessageLite result = (MessageLite) parser.parsePartialFrom(this, extensionRegistry);
            checkLastTagWas(WireFormat.makeTag(fieldNumber, 4));
            this.recursionDepth--;
            return result;
        }
        throw InvalidProtocolBufferException.recursionLimitExceeded();
    }

    @Deprecated
    public void readUnknownGroup(int fieldNumber, Builder builder) throws IOException {
        readGroup(fieldNumber, builder, null);
    }

    public void readMessage(Builder builder, ExtensionRegistryLite extensionRegistry) throws IOException {
        int length = readRawVarint32();
        if (this.recursionDepth < this.recursionLimit) {
            int oldLimit = pushLimit(length);
            this.recursionDepth++;
            builder.mergeFrom(this, extensionRegistry);
            checkLastTagWas(0);
            this.recursionDepth--;
            popLimit(oldLimit);
            return;
        }
        throw InvalidProtocolBufferException.recursionLimitExceeded();
    }

    public <T extends MessageLite> T readMessage(Parser<T> parser, ExtensionRegistryLite extensionRegistry) throws IOException {
        int length = readRawVarint32();
        if (this.recursionDepth < this.recursionLimit) {
            int oldLimit = pushLimit(length);
            this.recursionDepth++;
            MessageLite result = (MessageLite) parser.parsePartialFrom(this, extensionRegistry);
            checkLastTagWas(0);
            this.recursionDepth--;
            popLimit(oldLimit);
            return result;
        }
        throw InvalidProtocolBufferException.recursionLimitExceeded();
    }

    public ByteString readBytes() throws IOException {
        int size = readRawVarint32();
        int i = this.bufferSize;
        int i2 = this.bufferPos;
        if (size <= i - i2 && size > 0) {
            ByteString result;
            if (this.bufferIsImmutable && this.enableAliasing) {
                result = ByteString.wrap(this.buffer, i2, size);
            } else {
                result = ByteString.copyFrom(this.buffer, this.bufferPos, size);
            }
            this.bufferPos += size;
            return result;
        } else if (size == 0) {
            return ByteString.EMPTY;
        } else {
            return ByteString.wrap(readRawBytesSlowPath(size));
        }
    }

    public byte[] readByteArray() throws IOException {
        int size = readRawVarint32();
        int i = this.bufferSize;
        int i2 = this.bufferPos;
        if (size > i - i2 || size <= 0) {
            return readRawBytesSlowPath(size);
        }
        byte[] result = Arrays.copyOfRange(this.buffer, i2, i2 + size);
        this.bufferPos += size;
        return result;
    }

    public ByteBuffer readByteBuffer() throws IOException {
        int size = readRawVarint32();
        int i = this.bufferSize;
        int i2 = this.bufferPos;
        if (size <= i - i2 && size > 0) {
            ByteBuffer result;
            if (this.input == null && !this.bufferIsImmutable && this.enableAliasing) {
                result = ByteBuffer.wrap(this.buffer, i2, size).slice();
            } else {
                byte[] bArr = this.buffer;
                i2 = this.bufferPos;
                result = ByteBuffer.wrap(Arrays.copyOfRange(bArr, i2, i2 + size));
            }
            this.bufferPos += size;
            return result;
        } else if (size == 0) {
            return Internal.EMPTY_BYTE_BUFFER;
        } else {
            return ByteBuffer.wrap(readRawBytesSlowPath(size));
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

    /* JADX WARNING: Missing block: B:28:0x0071, code skipped:
            if (r2[r1] < (byte) 0) goto L_0x0074;
     */
    public int readRawVarint32() throws java.io.IOException {
        /*
        r5 = this;
        r0 = r5.bufferPos;
        r1 = r5.bufferSize;
        if (r1 != r0) goto L_0x0008;
    L_0x0006:
        goto L_0x0074;
    L_0x0008:
        r2 = r5.buffer;
        r3 = r0 + 1;
        r0 = r2[r0];
        r4 = r0;
        if (r0 < 0) goto L_0x0014;
    L_0x0011:
        r5.bufferPos = r3;
        return r4;
    L_0x0014:
        r1 = r1 - r3;
        r0 = 9;
        if (r1 >= r0) goto L_0x001a;
    L_0x0019:
        goto L_0x0074;
    L_0x001a:
        r0 = r3 + 1;
        r1 = r2[r3];
        r1 = r1 << 7;
        r1 = r1 ^ r4;
        r3 = r1;
        if (r1 >= 0) goto L_0x0029;
    L_0x0024:
        r1 = r3 ^ -128;
        r3 = r1;
        r1 = r0;
        goto L_0x007b;
    L_0x0029:
        r1 = r0 + 1;
        r0 = r2[r0];
        r0 = r0 << 14;
        r0 = r0 ^ r3;
        r3 = r0;
        if (r0 < 0) goto L_0x0037;
    L_0x0033:
        r0 = r3 ^ 16256;
        r3 = r0;
        goto L_0x007b;
    L_0x0037:
        r0 = r1 + 1;
        r1 = r2[r1];
        r1 = r1 << 21;
        r1 = r1 ^ r3;
        r3 = r1;
        if (r1 >= 0) goto L_0x0048;
    L_0x0041:
        r1 = -2080896; // 0xffffffffffe03f80 float:NaN double:NaN;
        r1 = r1 ^ r3;
        r3 = r1;
        r1 = r0;
        goto L_0x007b;
    L_0x0048:
        r1 = r0 + 1;
        r0 = r2[r0];
        r4 = r0 << 28;
        r3 = r3 ^ r4;
        r4 = 266354560; // 0xfe03f80 float:2.2112565E-29 double:1.315966377E-315;
        r3 = r3 ^ r4;
        if (r0 >= 0) goto L_0x007b;
    L_0x0055:
        r4 = r1 + 1;
        r1 = r2[r1];
        if (r1 >= 0) goto L_0x007a;
    L_0x005b:
        r1 = r4 + 1;
        r4 = r2[r4];
        if (r4 >= 0) goto L_0x007b;
    L_0x0061:
        r4 = r1 + 1;
        r1 = r2[r1];
        if (r1 >= 0) goto L_0x007a;
    L_0x0067:
        r1 = r4 + 1;
        r4 = r2[r4];
        if (r4 >= 0) goto L_0x007b;
    L_0x006d:
        r4 = r1 + 1;
        r1 = r2[r1];
        if (r1 >= 0) goto L_0x007a;
    L_0x0074:
        r0 = r5.readRawVarint64SlowPath();
        r0 = (int) r0;
        return r0;
    L_0x007a:
        r1 = r4;
    L_0x007b:
        r5.bufferPos = r1;
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.framework.protobuf.CodedInputStream.readRawVarint32():int");
    }

    private void skipRawVarint() throws IOException {
        if (this.bufferSize - this.bufferPos >= 10) {
            byte[] buffer = this.buffer;
            int pos = this.bufferPos;
            int i = 0;
            while (i < 10) {
                int pos2 = pos + 1;
                if (buffer[pos] >= (byte) 0) {
                    this.bufferPos = pos2;
                    return;
                } else {
                    i++;
                    pos = pos2;
                }
            }
        }
        skipRawVarintSlowPath();
    }

    private void skipRawVarintSlowPath() throws IOException {
        int i = 0;
        while (i < 10) {
            if (readRawByte() < (byte) 0) {
                i++;
            } else {
                return;
            }
        }
        throw InvalidProtocolBufferException.malformedVarint();
    }

    static int readRawVarint32(InputStream input) throws IOException {
        int firstByte = input.read();
        if (firstByte != -1) {
            return readRawVarint32(firstByte, input);
        }
        throw InvalidProtocolBufferException.truncatedMessage();
    }

    public static int readRawVarint32(int firstByte, InputStream input) throws IOException {
        if ((firstByte & 128) == 0) {
            return firstByte;
        }
        int b;
        int result = firstByte & 127;
        int offset = 7;
        while (offset < 32) {
            b = input.read();
            if (b != -1) {
                result |= (b & 127) << offset;
                if ((b & 128) == 0) {
                    return result;
                }
                offset += 7;
            } else {
                throw InvalidProtocolBufferException.truncatedMessage();
            }
        }
        while (offset < 64) {
            b = input.read();
            if (b == -1) {
                throw InvalidProtocolBufferException.truncatedMessage();
            } else if ((b & 128) == 0) {
                return result;
            } else {
                offset += 7;
            }
        }
        throw InvalidProtocolBufferException.malformedVarint();
    }

    /* JADX WARNING: Missing block: B:32:0x00bd, code skipped:
            if (((long) r2[r1]) < 0) goto L_0x00c0;
     */
    public long readRawVarint64() throws java.io.IOException {
        /*
        r10 = this;
        r0 = r10.bufferPos;
        r1 = r10.bufferSize;
        if (r1 != r0) goto L_0x0008;
    L_0x0006:
        goto L_0x00c0;
    L_0x0008:
        r2 = r10.buffer;
        r3 = r0 + 1;
        r0 = r2[r0];
        r4 = r0;
        if (r0 < 0) goto L_0x0015;
    L_0x0011:
        r10.bufferPos = r3;
        r0 = (long) r4;
        return r0;
    L_0x0015:
        r1 = r1 - r3;
        r0 = 9;
        if (r1 >= r0) goto L_0x001c;
    L_0x001a:
        goto L_0x00c0;
    L_0x001c:
        r0 = r3 + 1;
        r1 = r2[r3];
        r1 = r1 << 7;
        r1 = r1 ^ r4;
        r3 = r1;
        if (r1 >= 0) goto L_0x002b;
    L_0x0026:
        r1 = r3 ^ -128;
        r4 = (long) r1;
        goto L_0x00c6;
    L_0x002b:
        r1 = r0 + 1;
        r0 = r2[r0];
        r0 = r0 << 14;
        r0 = r0 ^ r3;
        r3 = r0;
        if (r0 < 0) goto L_0x003b;
    L_0x0035:
        r0 = r3 ^ 16256;
        r4 = (long) r0;
        r0 = r1;
        goto L_0x00c6;
    L_0x003b:
        r0 = r1 + 1;
        r1 = r2[r1];
        r1 = r1 << 21;
        r1 = r1 ^ r3;
        r3 = r1;
        if (r1 >= 0) goto L_0x004c;
    L_0x0045:
        r1 = -2080896; // 0xffffffffffe03f80 float:NaN double:NaN;
        r1 = r1 ^ r3;
        r4 = (long) r1;
        goto L_0x00c6;
    L_0x004c:
        r4 = (long) r3;
        r1 = r0 + 1;
        r0 = r2[r0];
        r6 = (long) r0;
        r0 = 28;
        r6 = r6 << r0;
        r4 = r4 ^ r6;
        r6 = r4;
        r8 = 0;
        r0 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1));
        if (r0 < 0) goto L_0x0063;
    L_0x005d:
        r4 = 266354560; // 0xfe03f80 float:2.2112565E-29 double:1.315966377E-315;
        r4 = r4 ^ r6;
        r0 = r1;
        goto L_0x00c6;
    L_0x0063:
        r0 = r1 + 1;
        r1 = r2[r1];
        r4 = (long) r1;
        r1 = 35;
        r4 = r4 << r1;
        r4 = r4 ^ r6;
        r6 = r4;
        r1 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1));
        if (r1 >= 0) goto L_0x0078;
    L_0x0071:
        r4 = -34093383808; // 0xfffffff80fe03f80 float:2.2112565E-29 double:NaN;
        r4 = r4 ^ r6;
        goto L_0x00c6;
    L_0x0078:
        r1 = r0 + 1;
        r0 = r2[r0];
        r4 = (long) r0;
        r0 = 42;
        r4 = r4 << r0;
        r4 = r4 ^ r6;
        r6 = r4;
        r0 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1));
        if (r0 < 0) goto L_0x008e;
    L_0x0086:
        r4 = 4363953127296; // 0x3f80fe03f80 float:2.2112565E-29 double:2.1560793202584E-311;
        r4 = r4 ^ r6;
        r0 = r1;
        goto L_0x00c6;
    L_0x008e:
        r0 = r1 + 1;
        r1 = r2[r1];
        r4 = (long) r1;
        r1 = 49;
        r4 = r4 << r1;
        r4 = r4 ^ r6;
        r6 = r4;
        r1 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1));
        if (r1 >= 0) goto L_0x00a3;
    L_0x009c:
        r4 = -558586000294016; // 0xfffe03f80fe03f80 float:2.2112565E-29 double:NaN;
        r4 = r4 ^ r6;
        goto L_0x00c6;
    L_0x00a3:
        r1 = r0 + 1;
        r0 = r2[r0];
        r4 = (long) r0;
        r0 = 56;
        r4 = r4 << r0;
        r4 = r4 ^ r6;
        r6 = 71499008037633920; // 0xfe03f80fe03f80 float:2.2112565E-29 double:6.838959413692434E-304;
        r4 = r4 ^ r6;
        r0 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1));
        if (r0 >= 0) goto L_0x00c5;
    L_0x00b6:
        r0 = r1 + 1;
        r1 = r2[r1];
        r6 = (long) r1;
        r1 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r1 >= 0) goto L_0x00c6;
    L_0x00c0:
        r0 = r10.readRawVarint64SlowPath();
        return r0;
    L_0x00c5:
        r0 = r1;
    L_0x00c6:
        r10.bufferPos = r0;
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.framework.protobuf.CodedInputStream.readRawVarint64():long");
    }

    /* Access modifiers changed, original: 0000 */
    public long readRawVarint64SlowPath() throws IOException {
        long result = 0;
        for (int shift = 0; shift < 64; shift += 7) {
            byte b = readRawByte();
            result |= ((long) (b & 127)) << shift;
            if ((b & 128) == 0) {
                return result;
            }
        }
        throw InvalidProtocolBufferException.malformedVarint();
    }

    public int readRawLittleEndian32() throws IOException {
        int pos = this.bufferPos;
        if (this.bufferSize - pos < 4) {
            refillBuffer(4);
            pos = this.bufferPos;
        }
        byte[] buffer = this.buffer;
        this.bufferPos = pos + 4;
        return (((buffer[pos] & 255) | ((buffer[pos + 1] & 255) << 8)) | ((buffer[pos + 2] & 255) << 16)) | ((buffer[pos + 3] & 255) << 24);
    }

    public long readRawLittleEndian64() throws IOException {
        int pos = this.bufferPos;
        if (this.bufferSize - pos < 8) {
            refillBuffer(8);
            pos = this.bufferPos;
        }
        byte[] buffer = this.buffer;
        this.bufferPos = pos + 8;
        return (((((((((long) buffer[pos]) & 255) | ((((long) buffer[pos + 1]) & 255) << 8)) | ((((long) buffer[pos + 2]) & 255) << 16)) | ((((long) buffer[pos + 3]) & 255) << 24)) | ((((long) buffer[pos + 4]) & 255) << 32)) | ((((long) buffer[pos + 5]) & 255) << 40)) | ((((long) buffer[pos + 6]) & 255) << 48)) | ((((long) buffer[pos + 7]) & 255) << 56);
    }

    public static int decodeZigZag32(int n) {
        return (n >>> 1) ^ (-(n & 1));
    }

    public static long decodeZigZag64(long n) {
        return (n >>> 1) ^ (-(1 & n));
    }

    private CodedInputStream(byte[] buffer, int off, int len, boolean bufferIsImmutable) {
        this.buffer = buffer;
        this.bufferSize = off + len;
        this.bufferPos = off;
        this.totalBytesRetired = -off;
        this.input = null;
        this.bufferIsImmutable = bufferIsImmutable;
    }

    private CodedInputStream(InputStream input, int bufferSize) {
        this.buffer = new byte[bufferSize];
        this.bufferPos = 0;
        this.totalBytesRetired = 0;
        this.input = input;
        this.bufferIsImmutable = false;
    }

    public void enableAliasing(boolean enabled) {
        this.enableAliasing = enabled;
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
        this.totalBytesRetired = -this.bufferPos;
    }

    public int pushLimit(int byteLimit) throws InvalidProtocolBufferException {
        if (byteLimit >= 0) {
            byteLimit += this.totalBytesRetired + this.bufferPos;
            int oldLimit = this.currentLimit;
            if (byteLimit <= oldLimit) {
                this.currentLimit = byteLimit;
                recomputeBufferSizeAfterLimit();
                return oldLimit;
            }
            throw InvalidProtocolBufferException.truncatedMessage();
        }
        throw InvalidProtocolBufferException.negativeSize();
    }

    private void recomputeBufferSizeAfterLimit() {
        this.bufferSize += this.bufferSizeAfterLimit;
        int bufferEnd = this.totalBytesRetired;
        int i = this.bufferSize;
        bufferEnd += i;
        int i2 = this.currentLimit;
        if (bufferEnd > i2) {
            this.bufferSizeAfterLimit = bufferEnd - i2;
            this.bufferSize = i - this.bufferSizeAfterLimit;
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
        return i - (this.totalBytesRetired + this.bufferPos);
    }

    public boolean isAtEnd() throws IOException {
        return this.bufferPos == this.bufferSize && !tryRefillBuffer(1);
    }

    public int getTotalBytesRead() {
        return this.totalBytesRetired + this.bufferPos;
    }

    private void refillBuffer(int n) throws IOException {
        if (!tryRefillBuffer(n)) {
            throw InvalidProtocolBufferException.truncatedMessage();
        }
    }

    private boolean tryRefillBuffer(int n) throws IOException {
        int i = this.bufferPos;
        if (i + n <= this.bufferSize) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("refillBuffer() called when ");
            stringBuilder.append(n);
            stringBuilder.append(" bytes were already available in buffer");
            throw new IllegalStateException(stringBuilder.toString());
        } else if ((this.totalBytesRetired + i) + n > this.currentLimit) {
            return false;
        } else {
            RefillCallback refillCallback = this.refillCallback;
            if (refillCallback != null) {
                refillCallback.onRefill();
            }
            if (this.input != null) {
                int i2;
                byte[] bArr;
                i = this.bufferPos;
                if (i > 0) {
                    i2 = this.bufferSize;
                    if (i2 > i) {
                        bArr = this.buffer;
                        System.arraycopy(bArr, i, bArr, 0, i2 - i);
                    }
                    this.totalBytesRetired += i;
                    this.bufferSize -= i;
                    this.bufferPos = 0;
                }
                i2 = this.input;
                bArr = this.buffer;
                int i3 = this.bufferSize;
                i2 = i2.read(bArr, i3, bArr.length - i3);
                if (i2 == 0 || i2 < -1 || i2 > this.buffer.length) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("InputStream#read(byte[]) returned invalid result: ");
                    stringBuilder2.append(i2);
                    stringBuilder2.append("\nThe InputStream implementation is buggy.");
                    throw new IllegalStateException(stringBuilder2.toString());
                } else if (i2 > 0) {
                    this.bufferSize += i2;
                    if ((this.totalBytesRetired + n) - this.sizeLimit <= 0) {
                        recomputeBufferSizeAfterLimit();
                        return this.bufferSize >= n ? true : tryRefillBuffer(n);
                    }
                    throw InvalidProtocolBufferException.sizeLimitExceeded();
                }
            }
            return false;
        }
    }

    public byte readRawByte() throws IOException {
        if (this.bufferPos == this.bufferSize) {
            refillBuffer(1);
        }
        byte[] bArr = this.buffer;
        int i = this.bufferPos;
        this.bufferPos = i + 1;
        return bArr[i];
    }

    public byte[] readRawBytes(int size) throws IOException {
        int pos = this.bufferPos;
        if (size > this.bufferSize - pos || size <= 0) {
            return readRawBytesSlowPath(size);
        }
        this.bufferPos = pos + size;
        return Arrays.copyOfRange(this.buffer, pos, pos + size);
    }

    private byte[] readRawBytesSlowPath(int size) throws IOException {
        if (size > 0) {
            int i = this.totalBytesRetired;
            int i2 = this.bufferPos;
            int currentMessageSize = (i + i2) + size;
            if (currentMessageSize <= this.sizeLimit) {
                int i3 = this.currentLimit;
                if (currentMessageSize <= i3) {
                    InputStream inputStream = this.input;
                    if (inputStream != null) {
                        int originalBufferPos = this.bufferPos;
                        int i4 = this.bufferSize;
                        i2 = i4 - i2;
                        this.totalBytesRetired = i + i4;
                        this.bufferPos = 0;
                        this.bufferSize = 0;
                        i4 = size - i2;
                        int n;
                        if (i4 < 4096 || i4 <= inputStream.available()) {
                            byte[] bytes = new byte[size];
                            System.arraycopy(this.buffer, originalBufferPos, bytes, 0, i2);
                            i = i2;
                            while (i < bytes.length) {
                                n = this.input.read(bytes, i, size - i);
                                if (n != -1) {
                                    this.totalBytesRetired += n;
                                    i += n;
                                } else {
                                    throw InvalidProtocolBufferException.truncatedMessage();
                                }
                            }
                            return bytes;
                        }
                        List<byte[]> chunks = new ArrayList();
                        while (i4 > 0) {
                            byte[] chunk = new byte[Math.min(i4, 4096)];
                            int pos = 0;
                            while (pos < chunk.length) {
                                int n2 = this.input.read(chunk, pos, chunk.length - pos);
                                if (n2 != -1) {
                                    this.totalBytesRetired += n2;
                                    pos += n2;
                                } else {
                                    throw InvalidProtocolBufferException.truncatedMessage();
                                }
                            }
                            i4 -= chunk.length;
                            chunks.add(chunk);
                        }
                        byte[] bytes2 = new byte[size];
                        System.arraycopy(this.buffer, originalBufferPos, bytes2, 0, i2);
                        n = i2;
                        for (byte[] chunk2 : chunks) {
                            System.arraycopy(chunk2, 0, bytes2, n, chunk2.length);
                            n += chunk2.length;
                        }
                        return bytes2;
                    }
                    throw InvalidProtocolBufferException.truncatedMessage();
                }
                skipRawBytes((i3 - i) - i2);
                throw InvalidProtocolBufferException.truncatedMessage();
            }
            throw InvalidProtocolBufferException.sizeLimitExceeded();
        } else if (size == 0) {
            return Internal.EMPTY_BYTE_ARRAY;
        } else {
            throw InvalidProtocolBufferException.negativeSize();
        }
    }

    public void skipRawBytes(int size) throws IOException {
        int i = this.bufferSize;
        int i2 = this.bufferPos;
        if (size > i - i2 || size < 0) {
            skipRawBytesSlowPath(size);
        } else {
            this.bufferPos = i2 + size;
        }
    }

    private void skipRawBytesSlowPath(int size) throws IOException {
        if (size >= 0) {
            int i = this.totalBytesRetired;
            int i2 = this.bufferPos;
            int i3 = (i + i2) + size;
            int i4 = this.currentLimit;
            if (i3 <= i4) {
                i = this.bufferSize;
                i2 = i - i2;
                this.bufferPos = i;
                refillBuffer(1);
                while (true) {
                    i3 = size - i2;
                    i4 = this.bufferSize;
                    if (i3 > i4) {
                        i2 += i4;
                        this.bufferPos = i4;
                        refillBuffer(1);
                    } else {
                        this.bufferPos = size - i2;
                        return;
                    }
                }
            }
            skipRawBytes((i4 - i) - i2);
            throw InvalidProtocolBufferException.truncatedMessage();
        }
        throw InvalidProtocolBufferException.negativeSize();
    }
}
