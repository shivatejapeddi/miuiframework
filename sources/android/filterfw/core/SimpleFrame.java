package android.filterfw.core;

import android.filterfw.format.ObjectFormat;
import android.graphics.Bitmap;
import java.nio.ByteBuffer;

public class SimpleFrame extends Frame {
    private Object mObject;

    SimpleFrame(FrameFormat format, FrameManager frameManager) {
        super(format, frameManager);
        initWithFormat(format);
        setReusable(false);
    }

    static SimpleFrame wrapObject(Object object, FrameManager frameManager) {
        SimpleFrame result = new SimpleFrame(ObjectFormat.fromObject(object, 1), frameManager);
        result.setObjectValue(object);
        return result;
    }

    private void initWithFormat(FrameFormat format) {
        int count = format.getLength();
        int baseType = format.getBaseType();
        if (baseType == 2) {
            this.mObject = new byte[count];
        } else if (baseType == 3) {
            this.mObject = new short[count];
        } else if (baseType == 4) {
            this.mObject = new int[count];
        } else if (baseType == 5) {
            this.mObject = new float[count];
        } else if (baseType != 6) {
            this.mObject = null;
        } else {
            this.mObject = new double[count];
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean hasNativeAllocation() {
        return false;
    }

    /* Access modifiers changed, original: protected */
    public void releaseNativeAllocation() {
    }

    public Object getObjectValue() {
        return this.mObject;
    }

    public void setInts(int[] ints) {
        assertFrameMutable();
        setGenericObjectValue(ints);
    }

    public int[] getInts() {
        Object obj = this.mObject;
        return obj instanceof int[] ? (int[]) obj : null;
    }

    public void setFloats(float[] floats) {
        assertFrameMutable();
        setGenericObjectValue(floats);
    }

    public float[] getFloats() {
        Object obj = this.mObject;
        return obj instanceof float[] ? (float[]) obj : null;
    }

    public void setData(ByteBuffer buffer, int offset, int length) {
        assertFrameMutable();
        setGenericObjectValue(ByteBuffer.wrap(buffer.array(), offset, length));
    }

    public ByteBuffer getData() {
        Object obj = this.mObject;
        return obj instanceof ByteBuffer ? (ByteBuffer) obj : null;
    }

    public void setBitmap(Bitmap bitmap) {
        assertFrameMutable();
        setGenericObjectValue(bitmap);
    }

    public Bitmap getBitmap() {
        Object obj = this.mObject;
        return obj instanceof Bitmap ? (Bitmap) obj : null;
    }

    private void setFormatObjectClass(Class objectClass) {
        MutableFrameFormat format = getFormat().mutableCopy();
        format.setObjectClass(objectClass);
        setFormat(format);
    }

    /* Access modifiers changed, original: protected */
    public void setGenericObjectValue(Object object) {
        FrameFormat format = getFormat();
        if (format.getObjectClass() == null) {
            setFormatObjectClass(object.getClass());
        } else if (!format.getObjectClass().isAssignableFrom(object.getClass())) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Attempting to set object value of type '");
            stringBuilder.append(object.getClass());
            stringBuilder.append("' on SimpleFrame of type '");
            stringBuilder.append(format.getObjectClass());
            stringBuilder.append("'!");
            throw new RuntimeException(stringBuilder.toString());
        }
        this.mObject = object;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SimpleFrame (");
        stringBuilder.append(getFormat());
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
