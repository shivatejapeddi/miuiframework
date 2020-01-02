package android.os;

import android.annotation.SystemApi;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

@SystemApi
public class HidlSupport {

    public static final class Mutable<E> {
        public E value;

        public Mutable() {
            this.value = null;
        }

        public Mutable(E value) {
            this.value = value;
        }
    }

    @SystemApi
    public static native int getPidIfSharable();

    @SystemApi
    public static boolean deepEquals(Object lft, Object rgt) {
        boolean z = true;
        if (lft == rgt) {
            return true;
        }
        if (lft == null || rgt == null) {
            return false;
        }
        Class<?> lftClazz = lft.getClass();
        Class<?> rgtClazz = rgt.getClass();
        if (lftClazz != rgtClazz) {
            return false;
        }
        if (lftClazz.isArray()) {
            Class<?> lftElementType = lftClazz.getComponentType();
            if (lftElementType != rgtClazz.getComponentType()) {
                return false;
            }
            if (lftElementType.isPrimitive()) {
                return Objects.deepEquals(lft, rgt);
            }
            Object[] lftArray = (Object[]) lft;
            Object[] rgtArray = (Object[]) rgt;
            if (!(lftArray.length == rgtArray.length && IntStream.range(0, lftArray.length).allMatch(new -$$Lambda$HidlSupport$4ktYtLCfMafhYI23iSXUQOH_hxo(lftArray, rgtArray)))) {
                z = false;
            }
            return z;
        } else if (lft instanceof List) {
            List<Object> lftList = (List) lft;
            List<Object> rgtList = (List) rgt;
            if (lftList.size() != rgtList.size()) {
                return false;
            }
            return rgtList.stream().allMatch(new -$$Lambda$HidlSupport$oV2DlGQSAfcavBj7TK20nYhwS0U(lftList.iterator()));
        } else {
            throwErrorIfUnsupportedType(lft);
            return lft.equals(rgt);
        }
    }

    @SystemApi
    public static int deepHashCode(Object o) {
        if (o == null) {
            return 0;
        }
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            if (clazz.getComponentType().isPrimitive()) {
                return primitiveArrayHashCode(o);
            }
            return Arrays.hashCode(Arrays.stream((Object[]) o).mapToInt(-$$Lambda$HidlSupport$GHxmwrIWiKN83tl6aMQt_nV5hiw.INSTANCE).toArray());
        } else if (o instanceof List) {
            return Arrays.hashCode(((List) o).stream().mapToInt(-$$Lambda$HidlSupport$CwwfmHPEvZaybUxpLzKdwrpQRfA.INSTANCE).toArray());
        } else {
            throwErrorIfUnsupportedType(o);
            return o.hashCode();
        }
    }

    private static void throwErrorIfUnsupportedType(Object o) {
        if ((o instanceof Collection) && !(o instanceof List)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Cannot check equality on collections other than lists: ");
            stringBuilder.append(o.getClass().getName());
            throw new UnsupportedOperationException(stringBuilder.toString());
        } else if (o instanceof Map) {
            throw new UnsupportedOperationException("Cannot check equality on maps");
        }
    }

    private static int primitiveArrayHashCode(Object o) {
        Class<?> elementType = o.getClass().getComponentType();
        if (elementType == Boolean.TYPE) {
            return Arrays.hashCode((boolean[]) o);
        }
        if (elementType == Byte.TYPE) {
            return Arrays.hashCode((byte[]) o);
        }
        if (elementType == Character.TYPE) {
            return Arrays.hashCode((char[]) o);
        }
        if (elementType == Double.TYPE) {
            return Arrays.hashCode((double[]) o);
        }
        if (elementType == Float.TYPE) {
            return Arrays.hashCode((float[]) o);
        }
        if (elementType == Integer.TYPE) {
            return Arrays.hashCode((int[]) o);
        }
        if (elementType == Long.TYPE) {
            return Arrays.hashCode((long[]) o);
        }
        if (elementType == Short.TYPE) {
            return Arrays.hashCode((short[]) o);
        }
        throw new UnsupportedOperationException();
    }

    /* JADX WARNING: Missing block: B:11:0x001f, code skipped:
            return false;
     */
    @android.annotation.SystemApi
    public static boolean interfacesEqual(android.os.IHwInterface r2, java.lang.Object r3) {
        /*
        if (r2 != r3) goto L_0x0004;
    L_0x0002:
        r0 = 1;
        return r0;
    L_0x0004:
        r0 = 0;
        if (r2 == 0) goto L_0x001f;
    L_0x0007:
        if (r3 != 0) goto L_0x000a;
    L_0x0009:
        goto L_0x001f;
    L_0x000a:
        r1 = r3 instanceof android.os.IHwInterface;
        if (r1 != 0) goto L_0x000f;
    L_0x000e:
        return r0;
    L_0x000f:
        r0 = r2.asBinder();
        r1 = r3;
        r1 = (android.os.IHwInterface) r1;
        r1 = r1.asBinder();
        r0 = java.util.Objects.equals(r0, r1);
        return r0;
    L_0x001f:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.HidlSupport.interfacesEqual(android.os.IHwInterface, java.lang.Object):boolean");
    }
}
