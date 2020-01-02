package com.android.framework.protobuf;

import com.android.internal.midi.MidiConstants;
import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.misc.Unsafe;

final class Utf8 {
    private static final long ASCII_MASK_LONG = -9187201950435737472L;
    public static final int COMPLETE = 0;
    public static final int MALFORMED = -1;
    static final int MAX_BYTES_PER_CHAR = 3;
    private static final int UNSAFE_COUNT_ASCII_THRESHOLD = 16;
    private static final Logger logger = Logger.getLogger(Utf8.class.getName());
    private static final Processor processor = (UnsafeProcessor.isAvailable() ? new UnsafeProcessor() : new SafeProcessor());

    static abstract class Processor {
        public abstract int encodeUtf8(CharSequence charSequence, byte[] bArr, int i, int i2);

        public abstract void encodeUtf8Direct(CharSequence charSequence, ByteBuffer byteBuffer);

        public abstract int partialIsValidUtf8(int i, byte[] bArr, int i2, int i3);

        public abstract int partialIsValidUtf8Direct(int i, ByteBuffer byteBuffer, int i2, int i3);

        Processor() {
        }

        /* Access modifiers changed, original: final */
        public final boolean isValidUtf8(byte[] bytes, int index, int limit) {
            return partialIsValidUtf8(0, bytes, index, limit) == 0;
        }

        /* Access modifiers changed, original: final */
        public final boolean isValidUtf8(ByteBuffer buffer, int index, int limit) {
            return partialIsValidUtf8(0, buffer, index, limit) == 0;
        }

        /* Access modifiers changed, original: final */
        public final int partialIsValidUtf8(int state, ByteBuffer buffer, int index, int limit) {
            if (buffer.hasArray()) {
                int offset = buffer.arrayOffset();
                return partialIsValidUtf8(state, buffer.array(), offset + index, offset + limit);
            } else if (buffer.isDirect()) {
                return partialIsValidUtf8Direct(state, buffer, index, limit);
            } else {
                return partialIsValidUtf8Default(state, buffer, index, limit);
            }
        }

        /* Access modifiers changed, original: final */
        public final int partialIsValidUtf8Default(int state, ByteBuffer buffer, int index, int limit) {
            int index2;
            if (state == 0) {
                index2 = index;
            } else if (index >= limit) {
                return state;
            } else {
                byte byte1 = (byte) state;
                byte byte2;
                int index3;
                if (byte1 < MidiConstants.STATUS_PITCH_BEND) {
                    if (byte1 >= (byte) -62) {
                        index2 = index + 1;
                        if (buffer.get(index) > (byte) -65) {
                            index = index2;
                        }
                    }
                    return -1;
                } else if (byte1 < (byte) -16) {
                    byte2 = (byte) (~(state >> 8));
                    if (byte2 == (byte) 0) {
                        index3 = index + 1;
                        byte2 = buffer.get(index);
                        if (index3 >= limit) {
                            return Utf8.incompleteStateFor(byte1, byte2);
                        }
                        index = index3;
                    }
                    if (byte2 <= (byte) -65 && ((byte1 != MidiConstants.STATUS_PITCH_BEND || byte2 >= MidiConstants.STATUS_POLYPHONIC_AFTERTOUCH) && (byte1 != (byte) -19 || byte2 < MidiConstants.STATUS_POLYPHONIC_AFTERTOUCH))) {
                        index2 = index + 1;
                        if (buffer.get(index) > (byte) -65) {
                            index = index2;
                        }
                    }
                    return -1;
                } else {
                    byte byte22 = (byte) (~(state >> 8));
                    byte2 = (byte) 0;
                    if (byte22 == (byte) 0) {
                        index3 = index + 1;
                        byte22 = buffer.get(index);
                        if (index3 >= limit) {
                            return Utf8.incompleteStateFor(byte1, byte22);
                        }
                        index = index3;
                    } else {
                        byte2 = (byte) (state >> 16);
                    }
                    if (byte2 == (byte) 0) {
                        index3 = index + 1;
                        byte2 = buffer.get(index);
                        if (index3 >= limit) {
                            return Utf8.incompleteStateFor((int) byte1, (int) byte22, (int) byte2);
                        }
                        index = index3;
                    }
                    if (byte22 <= (byte) -65 && (((byte1 << 28) + (byte22 + 112)) >> 30) == 0 && byte3 <= (byte) -65) {
                        index3 = index + 1;
                        if (buffer.get(index) > (byte) -65) {
                            index = index3;
                        } else {
                            index2 = index3;
                        }
                    }
                    return -1;
                }
            }
            return partialIsValidUtf8(buffer, index2, limit);
        }

        private static int partialIsValidUtf8(ByteBuffer buffer, int index, int limit) {
            index += Utf8.estimateConsecutiveAscii(buffer, index, limit);
            while (index < limit) {
                int index2 = index + 1;
                byte b = buffer.get(index);
                byte byte1 = b;
                int index3;
                if (b >= (byte) 0) {
                    index = index2;
                } else if (byte1 < (byte) -32) {
                    if (index2 >= limit) {
                        return byte1;
                    }
                    if (byte1 < (byte) -62 || buffer.get(index2) > (byte) -65) {
                        return -1;
                    }
                    index = index2 + 1;
                } else if (byte1 < (byte) -16) {
                    if (index2 >= limit - 1) {
                        return Utf8.incompleteStateFor(buffer, byte1, index2, limit - index2);
                    }
                    index3 = index2 + 1;
                    index2 = buffer.get(index2);
                    if (index2 > -65 || ((byte1 == (byte) -32 && index2 < -96) || ((byte1 == (byte) -19 && index2 >= -96) || buffer.get(index3) > (byte) -65))) {
                        return -1;
                    }
                    index = index3 + 1;
                } else if (index2 >= limit - 2) {
                    return Utf8.incompleteStateFor(buffer, byte1, index2, limit - index2);
                } else {
                    index = index2 + 1;
                    index2 = buffer.get(index2);
                    if (index2 <= -65 && (((byte1 << 28) + (index2 + 112)) >> 30) == 0) {
                        index3 = index + 1;
                        if (buffer.get(index) <= (byte) -65) {
                            index = index3 + 1;
                            if (buffer.get(index3) > (byte) -65) {
                            }
                        }
                    }
                    return -1;
                }
            }
            return 0;
        }

        /* Access modifiers changed, original: final */
        public final void encodeUtf8(CharSequence in, ByteBuffer out) {
            if (out.hasArray()) {
                int offset = out.arrayOffset();
                out.position(Utf8.encode(in, out.array(), out.position() + offset, out.remaining()) - offset);
            } else if (out.isDirect()) {
                encodeUtf8Direct(in, out);
            } else {
                encodeUtf8Default(in, out);
            }
        }

        /* Access modifiers changed, original: final */
        public final void encodeUtf8Default(CharSequence in, ByteBuffer out) {
            char charAt;
            char c;
            int badWriteIndex;
            StringBuilder stringBuilder;
            int inLength = in.length();
            int outIx = out.position();
            int inIx = 0;
            while (inIx < inLength) {
                try {
                    charAt = in.charAt(inIx);
                    c = charAt;
                    if (charAt >= 128) {
                        break;
                    }
                    out.put(outIx + inIx, (byte) c);
                    inIx++;
                } catch (IndexOutOfBoundsException e) {
                    badWriteIndex = out.position() + Math.max(inIx, (outIx - out.position()) + 1);
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed writing ");
                    stringBuilder.append(in.charAt(inIx));
                    stringBuilder.append(" at index ");
                    stringBuilder.append(badWriteIndex);
                    throw new ArrayIndexOutOfBoundsException(stringBuilder.toString());
                }
            }
            if (inIx == inLength) {
                out.position(outIx + inIx);
                return;
            }
            outIx += inIx;
            while (inIx < inLength) {
                charAt = in.charAt(inIx);
                int outIx2;
                if (charAt < 128) {
                    out.put(outIx, (byte) charAt);
                } else if (charAt < 2048) {
                    outIx2 = outIx + 1;
                    try {
                        out.put(outIx, (byte) ((charAt >>> 6) | 192));
                        out.put(outIx2, (byte) ((charAt & 63) | 128));
                        outIx = outIx2;
                    } catch (IndexOutOfBoundsException e2) {
                        outIx = outIx2;
                        badWriteIndex = out.position() + Math.max(inIx, (outIx - out.position()) + 1);
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Failed writing ");
                        stringBuilder.append(in.charAt(inIx));
                        stringBuilder.append(" at index ");
                        stringBuilder.append(badWriteIndex);
                        throw new ArrayIndexOutOfBoundsException(stringBuilder.toString());
                    }
                } else if (charAt < 55296 || 57343 < charAt) {
                    outIx2 = outIx + 1;
                    out.put(outIx, (byte) ((charAt >>> 12) | 224));
                    outIx = outIx2 + 1;
                    out.put(outIx2, (byte) (((charAt >>> 6) & 63) | 128));
                    out.put(outIx, (byte) ((charAt & 63) | 128));
                } else {
                    if (inIx + 1 != inLength) {
                        inIx++;
                        c = in.charAt(inIx);
                        char low = c;
                        if (Character.isSurrogatePair(charAt, c)) {
                            outIx2 = Character.toCodePoint(charAt, low);
                            int outIx3 = outIx + 1;
                            try {
                                out.put(outIx, (byte) ((outIx2 >>> 18) | 240));
                                outIx = outIx3 + 1;
                                out.put(outIx3, (byte) (((outIx2 >>> 12) & 63) | 128));
                                outIx3 = outIx + 1;
                                out.put(outIx, (byte) (((outIx2 >>> 6) & 63) | 128));
                                out.put(outIx3, (byte) ((outIx2 & 63) | 128));
                                outIx = outIx3;
                            } catch (IndexOutOfBoundsException e3) {
                                outIx = outIx3;
                                badWriteIndex = out.position() + Math.max(inIx, (outIx - out.position()) + 1);
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("Failed writing ");
                                stringBuilder.append(in.charAt(inIx));
                                stringBuilder.append(" at index ");
                                stringBuilder.append(badWriteIndex);
                                throw new ArrayIndexOutOfBoundsException(stringBuilder.toString());
                            }
                        }
                    }
                    throw new UnpairedSurrogateException(inIx, inLength);
                }
                inIx++;
                outIx++;
            }
            out.position(outIx);
        }
    }

    static final class SafeProcessor extends Processor {
        SafeProcessor() {
        }

        /* Access modifiers changed, original: 0000 */
        public int partialIsValidUtf8(int state, byte[] bytes, int index, int limit) {
            int index2;
            if (state == 0) {
                index2 = index;
            } else if (index >= limit) {
                return state;
            } else {
                int byte1 = (byte) state;
                int byte2;
                int index3;
                if (byte1 < -32) {
                    if (byte1 >= -62) {
                        index2 = index + 1;
                        if (bytes[index] > (byte) -65) {
                            index = index2;
                        }
                    }
                    return -1;
                } else if (byte1 < -16) {
                    byte2 = (byte) (~(state >> 8));
                    if (byte2 == 0) {
                        index3 = index + 1;
                        byte2 = bytes[index];
                        if (index3 >= limit) {
                            return Utf8.incompleteStateFor(byte1, byte2);
                        }
                        index = index3;
                    }
                    if (byte2 <= -65 && ((byte1 != -32 || byte2 >= -96) && (byte1 != -19 || byte2 < -96))) {
                        index2 = index + 1;
                        if (bytes[index] > (byte) -65) {
                            index = index2;
                        }
                    }
                    return -1;
                } else {
                    index2 = (byte) (~(state >> 8));
                    byte2 = 0;
                    if (index2 == 0) {
                        index3 = index + 1;
                        index2 = bytes[index];
                        if (index3 >= limit) {
                            return Utf8.incompleteStateFor(byte1, index2);
                        }
                        index = index3;
                    } else {
                        byte2 = (byte) (state >> 16);
                    }
                    if (byte2 == 0) {
                        index3 = index + 1;
                        byte2 = bytes[index];
                        if (index3 >= limit) {
                            return Utf8.incompleteStateFor(byte1, index2, byte2);
                        }
                        index = index3;
                    }
                    if (index2 <= -65 && (((byte1 << 28) + (index2 + 112)) >> 30) == 0 && byte3 <= -65) {
                        index3 = index + 1;
                        if (bytes[index] > (byte) -65) {
                            index = index3;
                        } else {
                            index2 = index3;
                        }
                    }
                    return -1;
                }
            }
            return partialIsValidUtf8(bytes, index2, limit);
        }

        /* Access modifiers changed, original: 0000 */
        public int partialIsValidUtf8Direct(int state, ByteBuffer buffer, int index, int limit) {
            return partialIsValidUtf8Default(state, buffer, index, limit);
        }

        /* Access modifiers changed, original: 0000 */
        public int encodeUtf8(CharSequence in, byte[] out, int offset, int length) {
            char charAt;
            char c;
            int utf16Length = in.length();
            int j = offset;
            int i = 0;
            int limit = offset + length;
            while (i < utf16Length && i + j < limit) {
                charAt = in.charAt(i);
                c = charAt;
                if (charAt >= 128) {
                    break;
                }
                out[j + i] = (byte) c;
                i++;
            }
            if (i == utf16Length) {
                return j + utf16Length;
            }
            j += i;
            while (i < utf16Length) {
                charAt = in.charAt(i);
                int j2;
                if (charAt < 128 && j < limit) {
                    j2 = j + 1;
                    out[j] = (byte) charAt;
                    j = j2;
                } else if (charAt < 2048 && j <= limit - 2) {
                    j2 = j + 1;
                    out[j] = (byte) ((charAt >>> 6) | 960);
                    j = j2 + 1;
                    out[j2] = (byte) ((charAt & 63) | 128);
                } else if ((charAt < 55296 || 57343 < charAt) && j <= limit - 3) {
                    j2 = j + 1;
                    out[j] = (byte) ((charAt >>> 12) | 480);
                    j = j2 + 1;
                    out[j2] = (byte) (((charAt >>> 6) & 63) | 128);
                    j2 = j + 1;
                    out[j] = (byte) ((charAt & 63) | 128);
                    j = j2;
                } else if (j <= limit - 4) {
                    if (i + 1 != in.length()) {
                        i++;
                        c = in.charAt(i);
                        char low = c;
                        if (Character.isSurrogatePair(charAt, c)) {
                            j2 = Character.toCodePoint(charAt, low);
                            int i2 = j + 1;
                            out[j] = (byte) ((j2 >>> 18) | 240);
                            j = i2 + 1;
                            out[i2] = (byte) (((j2 >>> 12) & 63) | 128);
                            i2 = j + 1;
                            out[j] = (byte) (((j2 >>> 6) & 63) | 128);
                            j = i2 + 1;
                            out[i2] = (byte) ((j2 & 63) | 128);
                        }
                    }
                    throw new UnpairedSurrogateException(i - 1, utf16Length);
                } else if (55296 > charAt || charAt > 57343 || (i + 1 != in.length() && Character.isSurrogatePair(charAt, in.charAt(i + 1)))) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed writing ");
                    stringBuilder.append(charAt);
                    stringBuilder.append(" at index ");
                    stringBuilder.append(j);
                    throw new ArrayIndexOutOfBoundsException(stringBuilder.toString());
                } else {
                    throw new UnpairedSurrogateException(i, utf16Length);
                }
                i++;
            }
            return j;
        }

        /* Access modifiers changed, original: 0000 */
        public void encodeUtf8Direct(CharSequence in, ByteBuffer out) {
            encodeUtf8Default(in, out);
        }

        private static int partialIsValidUtf8(byte[] bytes, int index, int limit) {
            while (index < limit && bytes[index] >= (byte) 0) {
                index++;
            }
            return index >= limit ? 0 : partialIsValidUtf8NonAscii(bytes, index, limit);
        }

        private static int partialIsValidUtf8NonAscii(byte[] bytes, int index, int limit) {
            while (index < limit) {
                int index2 = index + 1;
                byte b = bytes[index];
                byte byte1 = b;
                byte b2;
                if (b >= (byte) 0) {
                    index = index2;
                } else if (byte1 < (byte) -32) {
                    if (index2 >= limit) {
                        return byte1;
                    }
                    if (byte1 >= (byte) -62) {
                        index = index2 + 1;
                        if (bytes[index2] > (byte) -65) {
                        }
                    }
                    return -1;
                } else if (byte1 < (byte) -16) {
                    if (index2 >= limit - 1) {
                        return Utf8.incompleteStateFor(bytes, index2, limit);
                    }
                    int index3 = index2 + 1;
                    b2 = bytes[index2];
                    byte byte2 = b2;
                    if (b2 > (byte) -65 || ((byte1 == (byte) -32 && byte2 < (byte) -96) || (byte1 == (byte) -19 && byte2 >= (byte) -96))) {
                    } else {
                        index = index3 + 1;
                        if (bytes[index3] > (byte) -65) {
                        }
                    }
                    return -1;
                } else if (index2 >= limit - 2) {
                    return Utf8.incompleteStateFor(bytes, index2, limit);
                } else {
                    index = index2 + 1;
                    b2 = bytes[index2];
                    byte byte22 = b2;
                    if (b2 <= (byte) -65 && (((byte1 << 28) + (byte22 + 112)) >> 30) == 0) {
                        index2 = index + 1;
                        if (bytes[index] <= (byte) -65) {
                            index = index2 + 1;
                            if (bytes[index2] > (byte) -65) {
                            }
                        }
                    }
                    return -1;
                }
            }
            return 0;
        }
    }

    static class UnpairedSurrogateException extends IllegalArgumentException {
        private UnpairedSurrogateException(int index, int length) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unpaired surrogate at index ");
            stringBuilder.append(index);
            stringBuilder.append(" of ");
            stringBuilder.append(length);
            super(stringBuilder.toString());
        }
    }

    static final class UnsafeProcessor extends Processor {
        private static final int ARRAY_BASE_OFFSET = byteArrayBaseOffset();
        private static final boolean AVAILABLE;
        private static final long BUFFER_ADDRESS_OFFSET = fieldOffset(field(Buffer.class, "address"));
        private static final Unsafe UNSAFE = getUnsafe();

        UnsafeProcessor() {
        }

        static {
            boolean z = BUFFER_ADDRESS_OFFSET != -1 && ARRAY_BASE_OFFSET % 8 == 0;
            AVAILABLE = z;
        }

        static boolean isAvailable() {
            return AVAILABLE;
        }

        /* Access modifiers changed, original: 0000 */
        public int partialIsValidUtf8(int state, byte[] bytes, int index, int limit) {
            int i = state;
            Object obj = bytes;
            if (((index | limit) | (obj.length - limit)) >= 0) {
                long offset;
                int i2 = ARRAY_BASE_OFFSET;
                long offset2 = (long) (i2 + index);
                long offsetLimit = (long) (i2 + limit);
                if (i == 0) {
                    offset = offset2;
                } else if (offset2 >= offsetLimit) {
                    return i;
                } else {
                    i2 = (byte) i;
                    int byte2;
                    long offset3;
                    if (i2 < -32) {
                        if (i2 >= -62) {
                            offset = 1 + offset2;
                            if (UNSAFE.getByte(obj, offset2) > (byte) -65) {
                                offset2 = offset;
                            }
                        }
                        return -1;
                    } else if (i2 < -16) {
                        byte2 = (byte) (~(i >> 8));
                        if (byte2 == 0) {
                            offset3 = offset2 + 1;
                            byte2 = UNSAFE.getByte(obj, offset2);
                            if (offset3 >= offsetLimit) {
                                return Utf8.incompleteStateFor(i2, byte2);
                            }
                        }
                        offset3 = offset2;
                        if (byte2 <= -65 && ((i2 != -32 || byte2 >= -96) && (i2 != -19 || byte2 < -96))) {
                            offset = 1 + offset3;
                            if (UNSAFE.getByte(obj, offset3) > (byte) -65) {
                                offset3 = offset;
                            }
                        }
                        return -1;
                    } else {
                        int byte22 = (byte) (~(i >> 8));
                        byte2 = 0;
                        if (byte22 == 0) {
                            offset3 = offset2 + 1;
                            byte22 = UNSAFE.getByte(obj, offset2);
                            if (offset3 >= offsetLimit) {
                                return Utf8.incompleteStateFor(i2, byte22);
                            }
                        }
                        byte2 = (byte) (i >> 16);
                        offset3 = offset2;
                        if (byte2 == 0) {
                            long offset4 = offset3 + 1;
                            byte2 = UNSAFE.getByte(obj, offset3);
                            if (offset4 >= offsetLimit) {
                                return Utf8.incompleteStateFor(i2, byte22, byte2);
                            }
                            offset3 = offset4;
                        }
                        if (byte22 <= -65 && (((i2 << 28) + (byte22 + 112)) >> 30) == 0 && byte3 <= -65) {
                            offset = 1 + offset3;
                            if (UNSAFE.getByte(obj, offset3) > (byte) -65) {
                                offset3 = offset;
                            }
                        }
                        return -1;
                    }
                }
                return partialIsValidUtf8(obj, offset, (int) (offsetLimit - offset));
            }
            throw new ArrayIndexOutOfBoundsException(String.format("Array length=%d, index=%d, limit=%d", new Object[]{Integer.valueOf(obj.length), Integer.valueOf(index), Integer.valueOf(limit)}));
        }

        /* Access modifiers changed, original: 0000 */
        public int partialIsValidUtf8Direct(int state, ByteBuffer buffer, int index, int limit) {
            int i = state;
            int i2 = index;
            if (((i2 | limit) | (buffer.limit() - limit)) >= 0) {
                long address;
                long address2 = addressOffset(buffer) + ((long) i2);
                long addressLimit = ((long) (limit - i2)) + address2;
                if (i == 0) {
                    address = address2;
                } else if (address2 >= addressLimit) {
                    return i;
                } else {
                    int byte1 = (byte) i;
                    int byte2;
                    long address3;
                    if (byte1 < -32) {
                        if (byte1 >= -62) {
                            address = 1 + address2;
                            if (UNSAFE.getByte(address2) > (byte) -65) {
                                address2 = address;
                            }
                        }
                        return -1;
                    } else if (byte1 < -16) {
                        byte2 = (byte) (~(i >> 8));
                        if (byte2 == 0) {
                            address3 = address2 + 1;
                            byte2 = UNSAFE.getByte(address2);
                            if (address3 >= addressLimit) {
                                return Utf8.incompleteStateFor(byte1, byte2);
                            }
                        }
                        address3 = address2;
                        if (byte2 <= -65 && ((byte1 != -32 || byte2 >= -96) && (byte1 != -19 || byte2 < -96))) {
                            address = 1 + address3;
                            if (UNSAFE.getByte(address3) > (byte) -65) {
                                address3 = address;
                            }
                        }
                        return -1;
                    } else {
                        int byte22 = (byte) (~(i >> 8));
                        byte2 = 0;
                        if (byte22 == 0) {
                            address3 = address2 + 1;
                            byte22 = UNSAFE.getByte(address2);
                            if (address3 >= addressLimit) {
                                return Utf8.incompleteStateFor(byte1, byte22);
                            }
                        }
                        byte2 = (byte) (i >> 16);
                        address3 = address2;
                        if (byte2 == 0) {
                            long address4 = address3 + 1;
                            byte2 = UNSAFE.getByte(address3);
                            if (address4 >= addressLimit) {
                                return Utf8.incompleteStateFor(byte1, byte22, byte2);
                            }
                            address3 = address4;
                        }
                        if (byte22 <= -65 && (((byte1 << 28) + (byte22 + 112)) >> 30) == 0 && byte3 <= -65) {
                            address = 1 + address3;
                            if (UNSAFE.getByte(address3) > (byte) -65) {
                                address3 = address;
                            }
                        }
                        return -1;
                    }
                }
                return partialIsValidUtf8(address, (int) (addressLimit - address));
            }
            throw new ArrayIndexOutOfBoundsException(String.format("buffer limit=%d, index=%d, limit=%d", new Object[]{Integer.valueOf(buffer.limit()), Integer.valueOf(index), Integer.valueOf(limit)}));
        }

        /* Access modifiers changed, original: 0000 */
        public int encodeUtf8(CharSequence in, byte[] out, int offset, int length) {
            CharSequence charSequence = in;
            Object obj = out;
            int i = offset;
            int i2 = length;
            long outIx = (long) (ARRAY_BASE_OFFSET + i);
            long outLimit = ((long) i2) + outIx;
            int inLimit = in.length();
            String str = " at index ";
            String str2 = "Failed writing ";
            long j;
            StringBuilder stringBuilder;
            if (inLimit > i2 || obj.length - i2 < i) {
                j = outLimit;
                String str3 = str;
                String str4 = str2;
                stringBuilder = new StringBuilder();
                stringBuilder.append(str4);
                stringBuilder.append(charSequence.charAt(inLimit - 1));
                stringBuilder.append(str3);
                stringBuilder.append(offset + length);
                throw new ArrayIndexOutOfBoundsException(stringBuilder.toString());
            }
            char c;
            long j2;
            char charAt;
            int inIx = 0;
            while (true) {
                c = 128;
                j2 = 1;
                if (inIx >= inLimit) {
                    break;
                }
                charAt = charSequence.charAt(inIx);
                char c2 = charAt;
                if (charAt >= 128) {
                    break;
                }
                charAt = c2;
                c2 = 1 + outIx;
                UNSAFE.putByte(obj, outIx, (byte) charAt);
                inIx++;
                outIx = c2;
            }
            if (inIx == inLimit) {
                return (int) (outIx - ((long) ARRAY_BASE_OFFSET));
            }
            while (inIx < inLimit) {
                String str5;
                long j3;
                String str6;
                charAt = charSequence.charAt(inIx);
                long outIx2;
                if (charAt < c && outIx < outLimit) {
                    outIx2 = outIx + j2;
                    UNSAFE.putByte(obj, outIx, (byte) charAt);
                    str5 = str;
                    outIx = outIx2;
                    j3 = 1;
                    j = outLimit;
                    str6 = str2;
                    outLimit = 128;
                } else if (charAt >= 2048 || outIx > outLimit - 2) {
                    long j4;
                    if (charAt >= 55296 && 57343 >= charAt) {
                        str5 = str;
                        str6 = str2;
                    } else if (outIx <= outLimit - 3) {
                        str5 = str;
                        str6 = str2;
                        j4 = outIx + 1;
                        UNSAFE.putByte(obj, outIx, (byte) ((charAt >>> 12) | 480));
                        long j5 = j4 + 1;
                        UNSAFE.putByte(obj, j4, (byte) (((charAt >>> 6) & 63) | 128));
                        long outIx3 = j5 + 1;
                        UNSAFE.putByte(obj, j5, (byte) ((charAt & 63) | 128));
                        j = outLimit;
                        outIx = outIx3;
                        outLimit = 128;
                        j3 = 1;
                    } else {
                        str5 = str;
                        str6 = str2;
                    }
                    if (outIx <= outLimit - 4) {
                        if (inIx + 1 != inLimit) {
                            inIx++;
                            char charAt2 = charSequence.charAt(inIx);
                            char low = charAt2;
                            if (Character.isSurrogatePair(charAt, charAt2)) {
                                i = Character.toCodePoint(charAt, low);
                                j = outLimit;
                                outLimit = outIx + 1;
                                UNSAFE.putByte(obj, outIx, (byte) ((i >>> 18) | 240));
                                j4 = outLimit + 1;
                                UNSAFE.putByte(obj, outLimit, (byte) (((i >>> 12) & 63) | 128));
                                long j6 = j4 + 1;
                                outLimit = 128;
                                UNSAFE.putByte(obj, j4, (byte) (((i >>> 6) & 63) | 128));
                                j3 = 1;
                                j4 = j6 + 1;
                                UNSAFE.putByte(obj, j6, (byte) ((i & 63) | 128));
                                outIx = j4;
                            }
                        }
                        throw new UnpairedSurrogateException(inIx - 1, inLimit);
                    }
                    if (55296 > charAt || charAt > 57343 || (inIx + 1 != inLimit && Character.isSurrogatePair(charAt, charSequence.charAt(inIx + 1)))) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(str6);
                        stringBuilder.append(charAt);
                        stringBuilder.append(str5);
                        stringBuilder.append(outIx);
                        throw new ArrayIndexOutOfBoundsException(stringBuilder.toString());
                    }
                    throw new UnpairedSurrogateException(inIx, inLimit);
                } else {
                    long j7 = outIx + 1;
                    UNSAFE.putByte(obj, outIx, (byte) ((charAt >>> 6) | 960));
                    outIx2 = j7 + 1;
                    UNSAFE.putByte(obj, j7, (byte) ((charAt & 63) | 128));
                    str5 = str;
                    outIx = outIx2;
                    j3 = 1;
                    j = outLimit;
                    str6 = str2;
                    outLimit = 128;
                }
                inIx++;
                i = offset;
                i2 = length;
                c = outLimit;
                str = str5;
                str2 = str6;
                outLimit = j;
                j2 = j3;
            }
            return (int) (outIx - ((long) ARRAY_BASE_OFFSET));
        }

        /* Access modifiers changed, original: 0000 */
        public void encodeUtf8Direct(CharSequence in, ByteBuffer out) {
            CharSequence charSequence = in;
            ByteBuffer byteBuffer = out;
            long address = addressOffset(out);
            long outIx = ((long) out.position()) + address;
            long outLimit = ((long) out.limit()) + address;
            int inLimit = in.length();
            String str = " at index ";
            String str2 = "Failed writing ";
            long outLimit2;
            if (((long) inLimit) <= outLimit - outIx) {
                char c;
                long j;
                char charAt;
                int inIx = 0;
                while (true) {
                    c = 128;
                    j = 1;
                    if (inIx >= inLimit) {
                        break;
                    }
                    charAt = charSequence.charAt(inIx);
                    char c2 = charAt;
                    if (charAt >= 128) {
                        break;
                    }
                    charAt = c2;
                    c2 = 1 + outIx;
                    UNSAFE.putByte(outIx, (byte) charAt);
                    inIx++;
                    outIx = c2;
                }
                if (inIx == inLimit) {
                    byteBuffer.position((int) (outIx - address));
                    return;
                }
                while (inIx < inLimit) {
                    long outIx2;
                    long j2;
                    charAt = charSequence.charAt(inIx);
                    if (charAt < c && outIx < outLimit) {
                        outIx2 = outIx + j;
                        UNSAFE.putByte(outIx, (byte) charAt);
                        outLimit2 = outLimit;
                        outIx = outIx2;
                        outLimit = 128;
                        j2 = 1;
                        outIx2 = address;
                    } else if (charAt >= 2048 || outIx > outLimit - 2) {
                        outIx2 = address;
                        if ((charAt < 55296 || 57343 < charAt) && outIx <= outLimit - 3) {
                            j = outIx + 1;
                            UNSAFE.putByte(outIx, (byte) ((charAt >>> 12) | 480));
                            outIx = j + 1;
                            UNSAFE.putByte(j, (byte) (((charAt >>> 6) & 63) | 128));
                            j2 = outIx + 1;
                            UNSAFE.putByte(outIx, (byte) ((charAt & 63) | 128));
                            outLimit2 = outLimit;
                            outIx = j2;
                            outLimit = 128;
                            j2 = 1;
                        } else if (outIx <= outLimit - 4) {
                            if (inIx + 1 != inLimit) {
                                inIx++;
                                char charAt2 = charSequence.charAt(inIx);
                                address = charAt2;
                                if (Character.isSurrogatePair(charAt, charAt2)) {
                                    int codePoint = Character.toCodePoint(charAt, address);
                                    outLimit2 = outLimit;
                                    outLimit = outIx + 1;
                                    UNSAFE.putByte(outIx, (byte) ((codePoint >>> 18) | 240));
                                    outIx = outLimit + 1;
                                    UNSAFE.putByte(outLimit, (byte) (((codePoint >>> 12) & 63) | 128));
                                    j = outIx + 1;
                                    outLimit = 128;
                                    UNSAFE.putByte(outIx, (byte) (((codePoint >>> 6) & 63) | 128));
                                    j2 = 1;
                                    outIx = j + 1;
                                    UNSAFE.putByte(j, (byte) ((codePoint & 63) | 128));
                                }
                            }
                            throw new UnpairedSurrogateException(inIx - 1, inLimit);
                        } else {
                            if (55296 > charAt || charAt > 57343 || (inIx + 1 != inLimit && Character.isSurrogatePair(charAt, charSequence.charAt(inIx + 1)))) {
                                address = new StringBuilder();
                                address.append(str2);
                                address.append(charAt);
                                address.append(str);
                                address.append(outIx);
                                throw new ArrayIndexOutOfBoundsException(address.toString());
                            }
                            throw new UnpairedSurrogateException(inIx, inLimit);
                        }
                    } else {
                        outIx2 = address;
                        long j3 = outIx + 1;
                        UNSAFE.putByte(outIx, (byte) ((charAt >>> 6) | 960));
                        outIx = j3 + 1;
                        UNSAFE.putByte(j3, (byte) ((charAt & 63) | 128));
                        outLimit2 = outLimit;
                        outLimit = 128;
                        j2 = 1;
                    }
                    inIx++;
                    byteBuffer = out;
                    c = outLimit;
                    address = outIx2;
                    outLimit = outLimit2;
                    j = j2;
                }
                out.position((int) (outIx - address));
                return;
            }
            outLimit2 = outLimit;
            address = byteBuffer;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str2);
            stringBuilder.append(charSequence.charAt(inLimit - 1));
            stringBuilder.append(str);
            stringBuilder.append(out.limit());
            throw new ArrayIndexOutOfBoundsException(stringBuilder.toString());
        }

        private static int unsafeEstimateConsecutiveAscii(byte[] bytes, long offset, int maxChars) {
            int remaining = maxChars;
            if (remaining < 16) {
                return 0;
            }
            int unaligned = ((int) offset) & 7;
            int j = unaligned;
            while (j > 0) {
                long offset2 = 1 + offset;
                if (UNSAFE.getByte(bytes, offset) < (byte) 0) {
                    return unaligned - j;
                }
                j--;
                offset = offset2;
            }
            remaining -= unaligned;
            while (remaining >= 8 && (UNSAFE.getLong(bytes, offset) & Utf8.ASCII_MASK_LONG) == 0) {
                offset += 8;
                remaining -= 8;
            }
            return maxChars - remaining;
        }

        private static int unsafeEstimateConsecutiveAscii(long address, int maxChars) {
            int remaining = maxChars;
            if (remaining < 16) {
                return 0;
            }
            int unaligned = ((int) address) & 7;
            int j = unaligned;
            while (j > 0) {
                long address2 = 1 + address;
                if (UNSAFE.getByte(address) < (byte) 0) {
                    return unaligned - j;
                }
                j--;
                address = address2;
            }
            remaining -= unaligned;
            while (remaining >= 8 && (UNSAFE.getLong(address) & Utf8.ASCII_MASK_LONG) == 0) {
                address += 8;
                remaining -= 8;
            }
            return maxChars - remaining;
        }

        /* JADX WARNING: Missing block: B:40:0x0073, code skipped:
            return -1;
     */
        /* JADX WARNING: Missing block: B:56:0x00ab, code skipped:
            return -1;
     */
        private static int partialIsValidUtf8(byte[] r11, long r12, int r14) {
            /*
            r0 = unsafeEstimateConsecutiveAscii(r11, r12, r14);
            r14 = r14 - r0;
            r1 = (long) r0;
            r12 = r12 + r1;
        L_0x0007:
            r1 = 0;
        L_0x0008:
            r2 = 1;
            if (r14 <= 0) goto L_0x001c;
        L_0x000c:
            r4 = UNSAFE;
            r5 = r12 + r2;
            r12 = r4.getByte(r11, r12);
            r1 = r12;
            if (r12 < 0) goto L_0x001b;
        L_0x0017:
            r14 = r14 + -1;
            r12 = r5;
            goto L_0x0008;
        L_0x001b:
            r12 = r5;
        L_0x001c:
            if (r14 != 0) goto L_0x0020;
        L_0x001e:
            r2 = 0;
            return r2;
        L_0x0020:
            r14 = r14 + -1;
            r4 = -32;
            r5 = -65;
            r6 = -1;
            if (r1 >= r4) goto L_0x0041;
        L_0x0029:
            if (r14 != 0) goto L_0x002c;
        L_0x002b:
            return r1;
        L_0x002c:
            r14 = r14 + -1;
            r4 = -62;
            if (r1 < r4) goto L_0x0040;
        L_0x0032:
            r4 = UNSAFE;
            r2 = r2 + r12;
            r12 = r4.getByte(r11, r12);
            if (r12 <= r5) goto L_0x003d;
        L_0x003b:
            r12 = r2;
            goto L_0x0040;
        L_0x003d:
            r12 = r2;
            goto L_0x00a8;
        L_0x0040:
            return r6;
        L_0x0041:
            r7 = -16;
            if (r1 >= r7) goto L_0x0074;
        L_0x0045:
            r7 = 2;
            if (r14 >= r7) goto L_0x004d;
        L_0x0048:
            r2 = unsafeIncompleteStateFor(r11, r1, r12, r14);
            return r2;
        L_0x004d:
            r14 = r14 + -2;
            r7 = UNSAFE;
            r8 = r12 + r2;
            r12 = r7.getByte(r11, r12);
            r13 = r12;
            if (r12 > r5) goto L_0x0072;
        L_0x005a:
            r12 = -96;
            if (r1 != r4) goto L_0x0060;
        L_0x005e:
            if (r13 < r12) goto L_0x0072;
        L_0x0060:
            r4 = -19;
            if (r1 != r4) goto L_0x0066;
        L_0x0064:
            if (r13 >= r12) goto L_0x0072;
        L_0x0066:
            r12 = UNSAFE;
            r2 = r2 + r8;
            r12 = r12.getByte(r11, r8);
            if (r12 <= r5) goto L_0x0070;
        L_0x006f:
            goto L_0x0073;
        L_0x0070:
            r12 = r2;
            goto L_0x00a8;
        L_0x0072:
            r2 = r8;
        L_0x0073:
            return r6;
        L_0x0074:
            r4 = 3;
            if (r14 >= r4) goto L_0x007c;
        L_0x0077:
            r2 = unsafeIncompleteStateFor(r11, r1, r12, r14);
            return r2;
        L_0x007c:
            r14 = r14 + -3;
            r4 = UNSAFE;
            r7 = r12 + r2;
            r12 = r4.getByte(r11, r12);
            r13 = r12;
            if (r12 > r5) goto L_0x00ab;
        L_0x0089:
            r12 = r1 << 28;
            r4 = r13 + 112;
            r12 = r12 + r4;
            r12 = r12 >> 30;
            if (r12 != 0) goto L_0x00ab;
        L_0x0092:
            r12 = UNSAFE;
            r9 = r7 + r2;
            r12 = r12.getByte(r11, r7);
            if (r12 > r5) goto L_0x00aa;
        L_0x009c:
            r12 = UNSAFE;
            r7 = r9 + r2;
            r12 = r12.getByte(r11, r9);
            if (r12 <= r5) goto L_0x00a7;
        L_0x00a6:
            goto L_0x00ab;
        L_0x00a7:
            r12 = r7;
        L_0x00a8:
            goto L_0x0007;
        L_0x00aa:
            r7 = r9;
        L_0x00ab:
            return r6;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.framework.protobuf.Utf8$UnsafeProcessor.partialIsValidUtf8(byte[], long, int):int");
        }

        /* JADX WARNING: Missing block: B:40:0x0072, code skipped:
            return -1;
     */
        /* JADX WARNING: Missing block: B:56:0x00a9, code skipped:
            return -1;
     */
        private static int partialIsValidUtf8(long r11, int r13) {
            /*
            r0 = unsafeEstimateConsecutiveAscii(r11, r13);
            r1 = (long) r0;
            r11 = r11 + r1;
            r13 = r13 - r0;
        L_0x0007:
            r1 = 0;
        L_0x0008:
            r2 = 1;
            if (r13 <= 0) goto L_0x001c;
        L_0x000c:
            r4 = UNSAFE;
            r5 = r11 + r2;
            r11 = r4.getByte(r11);
            r1 = r11;
            if (r11 < 0) goto L_0x001b;
        L_0x0017:
            r13 = r13 + -1;
            r11 = r5;
            goto L_0x0008;
        L_0x001b:
            r11 = r5;
        L_0x001c:
            if (r13 != 0) goto L_0x0020;
        L_0x001e:
            r2 = 0;
            return r2;
        L_0x0020:
            r13 = r13 + -1;
            r4 = -32;
            r5 = -65;
            r6 = -1;
            if (r1 >= r4) goto L_0x0041;
        L_0x0029:
            if (r13 != 0) goto L_0x002c;
        L_0x002b:
            return r1;
        L_0x002c:
            r13 = r13 + -1;
            r4 = -62;
            if (r1 < r4) goto L_0x0040;
        L_0x0032:
            r4 = UNSAFE;
            r2 = r2 + r11;
            r11 = r4.getByte(r11);
            if (r11 <= r5) goto L_0x003d;
        L_0x003b:
            r11 = r2;
            goto L_0x0040;
        L_0x003d:
            r11 = r2;
            goto L_0x00a6;
        L_0x0040:
            return r6;
        L_0x0041:
            r7 = -16;
            if (r1 >= r7) goto L_0x0073;
        L_0x0045:
            r7 = 2;
            if (r13 >= r7) goto L_0x004d;
        L_0x0048:
            r2 = unsafeIncompleteStateFor(r11, r1, r13);
            return r2;
        L_0x004d:
            r13 = r13 + -2;
            r7 = UNSAFE;
            r8 = r11 + r2;
            r11 = r7.getByte(r11);
            if (r11 > r5) goto L_0x0071;
        L_0x0059:
            r12 = -96;
            if (r1 != r4) goto L_0x005f;
        L_0x005d:
            if (r11 < r12) goto L_0x0071;
        L_0x005f:
            r4 = -19;
            if (r1 != r4) goto L_0x0065;
        L_0x0063:
            if (r11 >= r12) goto L_0x0071;
        L_0x0065:
            r12 = UNSAFE;
            r2 = r2 + r8;
            r12 = r12.getByte(r8);
            if (r12 <= r5) goto L_0x006f;
        L_0x006e:
            goto L_0x0072;
        L_0x006f:
            r11 = r2;
            goto L_0x00a6;
        L_0x0071:
            r2 = r8;
        L_0x0072:
            return r6;
        L_0x0073:
            r4 = 3;
            if (r13 >= r4) goto L_0x007b;
        L_0x0076:
            r2 = unsafeIncompleteStateFor(r11, r1, r13);
            return r2;
        L_0x007b:
            r13 = r13 + -3;
            r4 = UNSAFE;
            r7 = r11 + r2;
            r11 = r4.getByte(r11);
            if (r11 > r5) goto L_0x00a9;
        L_0x0087:
            r12 = r1 << 28;
            r4 = r11 + 112;
            r12 = r12 + r4;
            r12 = r12 >> 30;
            if (r12 != 0) goto L_0x00a9;
        L_0x0090:
            r12 = UNSAFE;
            r9 = r7 + r2;
            r12 = r12.getByte(r7);
            if (r12 > r5) goto L_0x00a8;
        L_0x009a:
            r12 = UNSAFE;
            r7 = r9 + r2;
            r12 = r12.getByte(r9);
            if (r12 <= r5) goto L_0x00a5;
        L_0x00a4:
            goto L_0x00a9;
        L_0x00a5:
            r11 = r7;
        L_0x00a6:
            goto L_0x0007;
        L_0x00a8:
            r7 = r9;
        L_0x00a9:
            return r6;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.framework.protobuf.Utf8$UnsafeProcessor.partialIsValidUtf8(long, int):int");
        }

        private static int unsafeIncompleteStateFor(byte[] bytes, int byte1, long offset, int remaining) {
            if (remaining == 0) {
                return Utf8.incompleteStateFor(byte1);
            }
            if (remaining == 1) {
                return Utf8.incompleteStateFor(byte1, UNSAFE.getByte(bytes, offset));
            }
            if (remaining == 2) {
                return Utf8.incompleteStateFor(byte1, (int) UNSAFE.getByte(bytes, offset), (int) UNSAFE.getByte(bytes, 1 + offset));
            }
            throw new AssertionError();
        }

        private static int unsafeIncompleteStateFor(long address, int byte1, int remaining) {
            if (remaining == 0) {
                return Utf8.incompleteStateFor(byte1);
            }
            if (remaining == 1) {
                return Utf8.incompleteStateFor(byte1, UNSAFE.getByte(address));
            }
            if (remaining == 2) {
                return Utf8.incompleteStateFor(byte1, (int) UNSAFE.getByte(address), (int) UNSAFE.getByte(1 + address));
            }
            throw new AssertionError();
        }

        private static Field field(Class<?> clazz, String fieldName) {
            Field field;
            try {
                field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
            } catch (Throwable th) {
                field = null;
            }
            Logger access$700 = Utf8.logger;
            Level level = Level.FINEST;
            Object[] objArr = new Object[3];
            objArr[0] = clazz.getName();
            objArr[1] = fieldName;
            objArr[2] = field != null ? "available" : "unavailable";
            access$700.log(level, "{0}.{1}: {2}", objArr);
            return field;
        }

        private static long fieldOffset(Field field) {
            if (field != null) {
                Unsafe unsafe = UNSAFE;
                if (unsafe != null) {
                    return unsafe.objectFieldOffset(field);
                }
            }
            return -1;
        }

        private static <T> int byteArrayBaseOffset() {
            Unsafe unsafe = UNSAFE;
            return unsafe == null ? -1 : unsafe.arrayBaseOffset(byte[].class);
        }

        private static long addressOffset(ByteBuffer buffer) {
            return UNSAFE.getLong(buffer, BUFFER_ADDRESS_OFFSET);
        }

        private static Unsafe getUnsafe() {
            Unsafe unsafe = null;
            try {
                unsafe = (Unsafe) AccessController.doPrivileged(new PrivilegedExceptionAction<Unsafe>() {
                    public Unsafe run() throws Exception {
                        Class<Unsafe> k = Unsafe.class;
                        UnsafeProcessor.checkRequiredMethods(k);
                        for (Field f : k.getDeclaredFields()) {
                            f.setAccessible(true);
                            Object x = f.get(null);
                            if (k.isInstance(x)) {
                                return (Unsafe) k.cast(x);
                            }
                        }
                        return null;
                    }
                });
            } catch (Throwable th) {
            }
            Utf8.logger.log(Level.FINEST, "sun.misc.Unsafe: {}", unsafe != null ? "available" : "unavailable");
            return unsafe;
        }

        private static void checkRequiredMethods(Class<Unsafe> clazz) throws NoSuchMethodException, SecurityException {
            clazz.getMethod("arrayBaseOffset", new Class[]{Class.class});
            String str = "getByte";
            clazz.getMethod(str, new Class[]{Object.class, Long.TYPE});
            String str2 = "putByte";
            clazz.getMethod(str2, new Class[]{Object.class, Long.TYPE, Byte.TYPE});
            String str3 = "getLong";
            clazz.getMethod(str3, new Class[]{Object.class, Long.TYPE});
            clazz.getMethod("objectFieldOffset", new Class[]{Field.class});
            clazz.getMethod(str, new Class[]{Long.TYPE});
            clazz.getMethod(str3, new Class[]{Object.class, Long.TYPE});
            clazz.getMethod(str2, new Class[]{Long.TYPE, Byte.TYPE});
            clazz.getMethod(str3, new Class[]{Long.TYPE});
        }
    }

    public static boolean isValidUtf8(byte[] bytes) {
        return processor.isValidUtf8(bytes, 0, bytes.length);
    }

    public static boolean isValidUtf8(byte[] bytes, int index, int limit) {
        return processor.isValidUtf8(bytes, index, limit);
    }

    public static int partialIsValidUtf8(int state, byte[] bytes, int index, int limit) {
        return processor.partialIsValidUtf8(state, bytes, index, limit);
    }

    private static int incompleteStateFor(int byte1) {
        return byte1 > -12 ? -1 : byte1;
    }

    private static int incompleteStateFor(int byte1, int byte2) {
        return (byte1 > -12 || byte2 > -65) ? -1 : (byte2 << 8) ^ byte1;
    }

    private static int incompleteStateFor(int byte1, int byte2, int byte3) {
        return (byte1 > -12 || byte2 > -65 || byte3 > -65) ? -1 : ((byte2 << 8) ^ byte1) ^ (byte3 << 16);
    }

    private static int incompleteStateFor(byte[] bytes, int index, int limit) {
        int byte1 = bytes[index - 1];
        int i = limit - index;
        if (i == 0) {
            return incompleteStateFor(byte1);
        }
        if (i == 1) {
            return incompleteStateFor(byte1, bytes[index]);
        }
        if (i == 2) {
            return incompleteStateFor(byte1, bytes[index], bytes[index + 1]);
        }
        throw new AssertionError();
    }

    private static int incompleteStateFor(ByteBuffer buffer, int byte1, int index, int remaining) {
        if (remaining == 0) {
            return incompleteStateFor(byte1);
        }
        if (remaining == 1) {
            return incompleteStateFor(byte1, buffer.get(index));
        }
        if (remaining == 2) {
            return incompleteStateFor(byte1, buffer.get(index), buffer.get(index + 1));
        }
        throw new AssertionError();
    }

    static int encodedLength(CharSequence sequence) {
        int utf16Length = sequence.length();
        int utf8Length = utf16Length;
        int i = 0;
        while (i < utf16Length && sequence.charAt(i) < 128) {
            i++;
        }
        while (i < utf16Length) {
            char c = sequence.charAt(i);
            if (c >= 2048) {
                utf8Length += encodedLengthGeneral(sequence, i);
                break;
            }
            utf8Length += (127 - c) >>> 31;
            i++;
        }
        if (utf8Length >= utf16Length) {
            return utf8Length;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UTF-8 length does not fit in int: ");
        stringBuilder.append(((long) utf8Length) + 4294967296L);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    private static int encodedLengthGeneral(CharSequence sequence, int start) {
        int utf16Length = sequence.length();
        int utf8Length = 0;
        int i = start;
        while (i < utf16Length) {
            char c = sequence.charAt(i);
            if (c < 2048) {
                utf8Length += (127 - c) >>> 31;
            } else {
                utf8Length += 2;
                if (55296 <= c && c <= 57343) {
                    if (Character.codePointAt(sequence, i) >= 65536) {
                        i++;
                    } else {
                        throw new UnpairedSurrogateException(i, utf16Length);
                    }
                }
            }
            i++;
        }
        return utf8Length;
    }

    static int encode(CharSequence in, byte[] out, int offset, int length) {
        return processor.encodeUtf8(in, out, offset, length);
    }

    static boolean isValidUtf8(ByteBuffer buffer) {
        return processor.isValidUtf8(buffer, buffer.position(), buffer.remaining());
    }

    static int partialIsValidUtf8(int state, ByteBuffer buffer, int index, int limit) {
        return processor.partialIsValidUtf8(state, buffer, index, limit);
    }

    static void encodeUtf8(CharSequence in, ByteBuffer out) {
        processor.encodeUtf8(in, out);
    }

    private static int estimateConsecutiveAscii(ByteBuffer buffer, int index, int limit) {
        int i = index;
        int lim = limit - 7;
        while (i < lim && (buffer.getLong(i) & ASCII_MASK_LONG) == 0) {
            i += 8;
        }
        return i - index;
    }

    private Utf8() {
    }
}
