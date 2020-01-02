package com.android.framework.protobuf.nano;

import java.io.IOException;

public abstract class MessageNano {
    protected volatile int cachedSize = -1;

    public abstract MessageNano mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException;

    public int getCachedSize() {
        if (this.cachedSize < 0) {
            getSerializedSize();
        }
        return this.cachedSize;
    }

    public int getSerializedSize() {
        int size = computeSerializedSize();
        this.cachedSize = size;
        return size;
    }

    /* Access modifiers changed, original: protected */
    public int computeSerializedSize() {
        return 0;
    }

    public void writeTo(CodedOutputByteBufferNano output) throws IOException {
    }

    public static final byte[] toByteArray(MessageNano msg) {
        byte[] result = new byte[msg.getSerializedSize()];
        toByteArray(msg, result, 0, result.length);
        return result;
    }

    public static final void toByteArray(MessageNano msg, byte[] data, int offset, int length) {
        try {
            CodedOutputByteBufferNano output = CodedOutputByteBufferNano.newInstance(data, offset, length);
            msg.writeTo(output);
            output.checkNoSpaceLeft();
        } catch (IOException e) {
            throw new RuntimeException("Serializing to a byte array threw an IOException (should never happen).", e);
        }
    }

    public static final <T extends MessageNano> T mergeFrom(T msg, byte[] data) throws InvalidProtocolBufferNanoException {
        return mergeFrom(msg, data, 0, data.length);
    }

    public static final <T extends MessageNano> T mergeFrom(T msg, byte[] data, int off, int len) throws InvalidProtocolBufferNanoException {
        try {
            CodedInputByteBufferNano input = CodedInputByteBufferNano.newInstance(data, off, len);
            msg.mergeFrom(input);
            input.checkLastTagWas(0);
            return msg;
        } catch (InvalidProtocolBufferNanoException e) {
            throw e;
        } catch (IOException e2) {
            throw new RuntimeException("Reading from a byte array threw an IOException (should never happen).");
        }
    }

    /* JADX WARNING: Missing block: B:14:0x002f, code skipped:
            return false;
     */
    public static final boolean messageNanoEquals(com.android.framework.protobuf.nano.MessageNano r4, com.android.framework.protobuf.nano.MessageNano r5) {
        /*
        if (r4 != r5) goto L_0x0004;
    L_0x0002:
        r0 = 1;
        return r0;
    L_0x0004:
        r0 = 0;
        if (r4 == 0) goto L_0x002f;
    L_0x0007:
        if (r5 != 0) goto L_0x000a;
    L_0x0009:
        goto L_0x002f;
    L_0x000a:
        r1 = r4.getClass();
        r2 = r5.getClass();
        if (r1 == r2) goto L_0x0015;
    L_0x0014:
        return r0;
    L_0x0015:
        r1 = r4.getSerializedSize();
        r2 = r5.getSerializedSize();
        if (r2 == r1) goto L_0x0020;
    L_0x001f:
        return r0;
    L_0x0020:
        r2 = new byte[r1];
        r3 = new byte[r1];
        toByteArray(r4, r2, r0, r1);
        toByteArray(r5, r3, r0, r1);
        r0 = java.util.Arrays.equals(r2, r3);
        return r0;
    L_0x002f:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.framework.protobuf.nano.MessageNano.messageNanoEquals(com.android.framework.protobuf.nano.MessageNano, com.android.framework.protobuf.nano.MessageNano):boolean");
    }

    public String toString() {
        return MessageNanoPrinter.print(this);
    }

    public MessageNano clone() throws CloneNotSupportedException {
        return (MessageNano) super.clone();
    }
}
