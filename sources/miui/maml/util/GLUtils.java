package miui.maml.util;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

public class GLUtils {
    public static Buffer buildBuffer(int[] arr) {
        ByteBuffer qbb = ByteBuffer.allocateDirect(arr.length * 4);
        qbb.order(ByteOrder.nativeOrder());
        IntBuffer buffer = qbb.asIntBuffer();
        buffer.put(arr);
        buffer.position(0);
        return buffer;
    }

    public static Buffer buildBuffer(float[] arr) {
        ByteBuffer qbb = ByteBuffer.allocateDirect(arr.length * 4);
        qbb.order(ByteOrder.nativeOrder());
        FloatBuffer buffer = qbb.asFloatBuffer();
        buffer.put(arr);
        buffer.position(0);
        return buffer;
    }

    public static Buffer buildBuffer(short[] arr) {
        ByteBuffer qbb = ByteBuffer.allocateDirect(arr.length * 2);
        qbb.order(ByteOrder.nativeOrder());
        ShortBuffer buffer = qbb.asShortBuffer();
        buffer.put(arr);
        buffer.position(0);
        return buffer;
    }

    public static Buffer buildBuffer(char[] arr) {
        ByteBuffer qbb = ByteBuffer.allocateDirect(arr.length);
        qbb.order(ByteOrder.nativeOrder());
        CharBuffer buffer = qbb.asCharBuffer();
        buffer.put(arr);
        buffer.position(0);
        return buffer;
    }

    public static Buffer buildBuffer(byte[] arr) {
        ByteBuffer qbb = ByteBuffer.allocateDirect(arr.length);
        qbb.order(ByteOrder.nativeOrder());
        qbb.put(arr);
        qbb.position(0);
        return qbb;
    }

    public static Buffer buildBuffer(long[] arr) {
        ByteBuffer qbb = ByteBuffer.allocateDirect(arr.length * 8);
        qbb.order(ByteOrder.nativeOrder());
        LongBuffer buffer = qbb.asLongBuffer();
        buffer.put(arr);
        buffer.position(0);
        return buffer;
    }

    public static Buffer buildBuffer(double[] arr) {
        ByteBuffer qbb = ByteBuffer.allocateDirect(arr.length * 8);
        qbb.order(ByteOrder.nativeOrder());
        DoubleBuffer buffer = qbb.asDoubleBuffer();
        buffer.put(arr);
        buffer.position(0);
        return buffer;
    }
}
