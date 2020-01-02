package android.hardware.camera2.marshal.impl;

import android.hardware.camera2.marshal.MarshalHelpers;
import android.hardware.camera2.marshal.MarshalQueryable;
import android.hardware.camera2.marshal.Marshaler;
import android.hardware.camera2.utils.TypeReference;
import android.util.Log;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class MarshalQueryableEnum<T extends Enum<T>> implements MarshalQueryable<T> {
    private static final boolean DEBUG = false;
    private static final String TAG = MarshalQueryableEnum.class.getSimpleName();
    private static final int UINT8_MASK = 255;
    private static final int UINT8_MAX = 255;
    private static final int UINT8_MIN = 0;
    private static final HashMap<Class<? extends Enum>, int[]> sEnumValues = new HashMap();

    private class MarshalerEnum extends Marshaler<T> {
        private final Class<T> mClass;

        protected MarshalerEnum(TypeReference<T> typeReference, int nativeType) {
            super(MarshalQueryableEnum.this, typeReference, nativeType);
            this.mClass = typeReference.getRawType();
        }

        public void marshal(T value, ByteBuffer buffer) {
            int enumValue = MarshalQueryableEnum.getEnumValue(value);
            if (this.mNativeType == 1) {
                buffer.putInt(enumValue);
            } else if (this.mNativeType != 0) {
                throw new AssertionError();
            } else if (enumValue < 0 || enumValue > 255) {
                throw new UnsupportedOperationException(String.format("Enum value %x too large to fit into unsigned byte", new Object[]{Integer.valueOf(enumValue)}));
            } else {
                buffer.put((byte) enumValue);
            }
        }

        public T unmarshal(ByteBuffer buffer) {
            int i = this.mNativeType;
            if (i == 0) {
                i = buffer.get() & 255;
            } else if (i == 1) {
                i = buffer.getInt();
            } else {
                throw new AssertionError("Unexpected native type; impossible since its not supported");
            }
            return MarshalQueryableEnum.getEnumFromValue(this.mClass, i);
        }

        public int getNativeSize() {
            return MarshalHelpers.getPrimitiveTypeSize(this.mNativeType);
        }
    }

    public Marshaler<T> createMarshaler(TypeReference<T> managedType, int nativeType) {
        return new MarshalerEnum(managedType, nativeType);
    }

    public boolean isTypeMappingSupported(TypeReference<T> managedType, int nativeType) {
        String str;
        StringBuilder stringBuilder;
        String str2 = "Can't marshal class ";
        if ((nativeType == 1 || nativeType == 0) && (managedType.getType() instanceof Class)) {
            Class<?> typeClass = (Class) managedType.getType();
            if (typeClass.isEnum()) {
                try {
                    typeClass.getDeclaredConstructor(new Class[]{String.class, Integer.TYPE});
                    return true;
                } catch (NoSuchMethodException e) {
                    str = TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str2);
                    stringBuilder.append(typeClass);
                    stringBuilder.append("; no default constructor");
                    Log.e(str, stringBuilder.toString());
                } catch (SecurityException e2) {
                    str = TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str2);
                    stringBuilder.append(typeClass);
                    stringBuilder.append("; not accessible");
                    Log.e(str, stringBuilder.toString());
                }
            }
        }
        return false;
    }

    public static <T extends Enum<T>> void registerEnumValues(Class<T> enumType, int[] values) {
        if (((Enum[]) enumType.getEnumConstants()).length == values.length) {
            sEnumValues.put(enumType, values);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Expected values array to be the same size as the enumTypes values ");
        stringBuilder.append(values.length);
        stringBuilder.append(" for type ");
        stringBuilder.append(enumType);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    private static <T extends Enum<T>> int getEnumValue(T enumValue) {
        int[] values = (int[]) sEnumValues.get(enumValue.getClass());
        int ordinal = enumValue.ordinal();
        if (values != null) {
            return values[ordinal];
        }
        return ordinal;
    }

    private static <T extends Enum<T>> T getEnumFromValue(Class<T> enumType, int value) {
        int ordinal;
        int[] registeredValues = (int[]) sEnumValues.get(enumType);
        if (registeredValues != null) {
            ordinal = -1;
            for (int i = 0; i < registeredValues.length; i++) {
                if (registeredValues[i] == value) {
                    ordinal = i;
                    break;
                }
            }
        } else {
            ordinal = value;
        }
        Enum[] values = (Enum[]) enumType.getEnumConstants();
        if (ordinal >= 0 && ordinal < values.length) {
            return values[ordinal];
        }
        r4 = new Object[3];
        boolean z = true;
        r4[1] = enumType;
        if (registeredValues == null) {
            z = false;
        }
        r4[2] = Boolean.valueOf(z);
        throw new IllegalArgumentException(String.format("Argument 'value' (%d) was not a valid enum value for type %s (registered? %b)", r4));
    }
}
