package android.filterfw.core;

import android.filterfw.format.ObjectFormat;
import android.graphics.Bitmap;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class SerializedFrame extends Frame {
    private static final int INITIAL_CAPACITY = 64;
    private DirectByteOutputStream mByteOutputStream;
    private ObjectOutputStream mObjectOut;

    private class DirectByteInputStream extends InputStream {
        private byte[] mBuffer;
        private int mPos = 0;
        private int mSize;

        public DirectByteInputStream(byte[] buffer, int size) {
            this.mBuffer = buffer;
            this.mSize = size;
        }

        public final int available() {
            return this.mSize - this.mPos;
        }

        public final int read() {
            int i = this.mPos;
            if (i >= this.mSize) {
                return -1;
            }
            byte[] bArr = this.mBuffer;
            this.mPos = i + 1;
            return bArr[i] & 255;
        }

        public final int read(byte[] b, int off, int len) {
            int i = this.mPos;
            int i2 = this.mSize;
            if (i >= i2) {
                return -1;
            }
            if (i + len > i2) {
                len = i2 - i;
            }
            System.arraycopy(this.mBuffer, this.mPos, b, off, len);
            this.mPos += len;
            return len;
        }

        public final long skip(long n) {
            int i = this.mPos;
            long j = ((long) i) + n;
            int i2 = this.mSize;
            if (j > ((long) i2)) {
                n = (long) (i2 - i);
            }
            if (n < 0) {
                return 0;
            }
            this.mPos = (int) (((long) this.mPos) + n);
            return n;
        }
    }

    private class DirectByteOutputStream extends OutputStream {
        private byte[] mBuffer = null;
        private int mDataOffset = 0;
        private int mOffset = 0;

        public DirectByteOutputStream(int size) {
            this.mBuffer = new byte[size];
        }

        private final void ensureFit(int bytesToWrite) {
            int i = this.mOffset;
            int i2 = i + bytesToWrite;
            byte[] bArr = this.mBuffer;
            if (i2 > bArr.length) {
                byte[] oldBuffer = this.mBuffer;
                this.mBuffer = new byte[Math.max(i + bytesToWrite, bArr.length * 2)];
                System.arraycopy(oldBuffer, 0, this.mBuffer, 0, this.mOffset);
            }
        }

        public final void markHeaderEnd() {
            this.mDataOffset = this.mOffset;
        }

        public final int getSize() {
            return this.mOffset;
        }

        public byte[] getByteArray() {
            return this.mBuffer;
        }

        public final void write(byte[] b) {
            write(b, 0, b.length);
        }

        public final void write(byte[] b, int off, int len) {
            ensureFit(len);
            System.arraycopy(b, off, this.mBuffer, this.mOffset, len);
            this.mOffset += len;
        }

        public final void write(int b) {
            ensureFit(1);
            byte[] bArr = this.mBuffer;
            int i = this.mOffset;
            this.mOffset = i + 1;
            bArr[i] = (byte) b;
        }

        public final void reset() {
            this.mOffset = this.mDataOffset;
        }

        public final DirectByteInputStream getInputStream() {
            return new DirectByteInputStream(this.mBuffer, this.mOffset);
        }
    }

    SerializedFrame(FrameFormat format, FrameManager frameManager) {
        super(format, frameManager);
        setReusable(false);
        try {
            this.mByteOutputStream = new DirectByteOutputStream(64);
            this.mObjectOut = new ObjectOutputStream(this.mByteOutputStream);
            this.mByteOutputStream.markHeaderEnd();
        } catch (IOException e) {
            throw new RuntimeException("Could not create serialization streams for SerializedFrame!", e);
        }
    }

    static SerializedFrame wrapObject(Object object, FrameManager frameManager) {
        SerializedFrame result = new SerializedFrame(ObjectFormat.fromObject(object, 1), frameManager);
        result.setObjectValue(object);
        return result;
    }

    /* Access modifiers changed, original: protected */
    public boolean hasNativeAllocation() {
        return false;
    }

    /* Access modifiers changed, original: protected */
    public void releaseNativeAllocation() {
    }

    public Object getObjectValue() {
        return deserializeObjectValue();
    }

    public void setInts(int[] ints) {
        assertFrameMutable();
        setGenericObjectValue(ints);
    }

    public int[] getInts() {
        Object result = deserializeObjectValue();
        return result instanceof int[] ? (int[]) result : null;
    }

    public void setFloats(float[] floats) {
        assertFrameMutable();
        setGenericObjectValue(floats);
    }

    public float[] getFloats() {
        Object result = deserializeObjectValue();
        return result instanceof float[] ? (float[]) result : null;
    }

    public void setData(ByteBuffer buffer, int offset, int length) {
        assertFrameMutable();
        setGenericObjectValue(ByteBuffer.wrap(buffer.array(), offset, length));
    }

    public ByteBuffer getData() {
        Object result = deserializeObjectValue();
        return result instanceof ByteBuffer ? (ByteBuffer) result : null;
    }

    public void setBitmap(Bitmap bitmap) {
        assertFrameMutable();
        setGenericObjectValue(bitmap);
    }

    public Bitmap getBitmap() {
        Object result = deserializeObjectValue();
        return result instanceof Bitmap ? (Bitmap) result : null;
    }

    /* Access modifiers changed, original: protected */
    public void setGenericObjectValue(Object object) {
        serializeObjectValue(object);
    }

    private final void serializeObjectValue(Object object) {
        try {
            this.mByteOutputStream.reset();
            this.mObjectOut.writeObject(object);
            this.mObjectOut.flush();
            this.mObjectOut.close();
        } catch (IOException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Could not serialize object ");
            stringBuilder.append(object);
            stringBuilder.append(" in ");
            stringBuilder.append(this);
            stringBuilder.append("!");
            throw new RuntimeException(stringBuilder.toString(), e);
        }
    }

    private final Object deserializeObjectValue() {
        StringBuilder stringBuilder;
        String str = "!";
        try {
            str = new ObjectInputStream(this.mByteOutputStream.getInputStream()).readObject();
            return str;
        } catch (IOException e) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Could not deserialize object in ");
            stringBuilder.append(this);
            stringBuilder.append(str);
            throw new RuntimeException(stringBuilder.toString(), e);
        } catch (ClassNotFoundException e2) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to deserialize object of unknown class in ");
            stringBuilder.append(this);
            stringBuilder.append(str);
            throw new RuntimeException(stringBuilder.toString(), e2);
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SerializedFrame (");
        stringBuilder.append(getFormat());
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
