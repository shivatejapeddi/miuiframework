package android.bluetooth.le;

import android.bluetooth.BluetoothAdapter;
import android.util.SparseArray;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class BluetoothLeUtils {
    static String toString(SparseArray<byte[]> array) {
        if (array == null) {
            return "null";
        }
        if (array.size() == 0) {
            return "{}";
        }
        StringBuilder buffer = new StringBuilder();
        buffer.append('{');
        for (int i = 0; i < array.size(); i++) {
            buffer.append(array.keyAt(i));
            buffer.append("=");
            buffer.append(Arrays.toString((byte[]) array.valueAt(i)));
        }
        buffer.append('}');
        return buffer.toString();
    }

    static <T> String toString(Map<T, byte[]> map) {
        if (map == null) {
            return "null";
        }
        if (map.isEmpty()) {
            return "{}";
        }
        StringBuilder buffer = new StringBuilder();
        buffer.append('{');
        Iterator<Entry<T, byte[]>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Object key = ((Entry) it.next()).getKey();
            buffer.append(key);
            buffer.append("=");
            buffer.append(Arrays.toString((byte[]) map.get(key)));
            if (it.hasNext()) {
                buffer.append(", ");
            }
        }
        buffer.append('}');
        return buffer.toString();
    }

    static <T> String toString(byte[] data) {
        if (data == null) {
            return "null";
        }
        if (data.length == 0) {
            return "{}";
        }
        StringBuilder buffer = new StringBuilder();
        buffer.append('{');
        for (int i = 0; i < data.length; i++) {
            buffer.append(data[i]);
            if (i + 1 < data.length) {
                buffer.append(", ");
            }
        }
        buffer.append('}');
        return buffer.toString();
    }

    /* JADX WARNING: Missing block: B:19:0x003e, code skipped:
            return false;
     */
    static boolean equals(android.util.SparseArray<byte[]> r5, android.util.SparseArray<byte[]> r6) {
        /*
        r0 = 1;
        if (r5 != r6) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = 0;
        if (r5 == 0) goto L_0x003e;
    L_0x0007:
        if (r6 != 0) goto L_0x000a;
    L_0x0009:
        goto L_0x003e;
    L_0x000a:
        r2 = r5.size();
        r3 = r6.size();
        if (r2 == r3) goto L_0x0015;
    L_0x0014:
        return r1;
    L_0x0015:
        r2 = 0;
    L_0x0016:
        r3 = r5.size();
        if (r2 >= r3) goto L_0x003d;
    L_0x001c:
        r3 = r5.keyAt(r2);
        r4 = r6.keyAt(r2);
        if (r3 != r4) goto L_0x003c;
    L_0x0026:
        r3 = r5.valueAt(r2);
        r3 = (byte[]) r3;
        r4 = r6.valueAt(r2);
        r4 = (byte[]) r4;
        r3 = java.util.Arrays.equals(r3, r4);
        if (r3 != 0) goto L_0x0039;
    L_0x0038:
        goto L_0x003c;
    L_0x0039:
        r2 = r2 + 1;
        goto L_0x0016;
    L_0x003c:
        return r1;
    L_0x003d:
        return r0;
    L_0x003e:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.bluetooth.le.BluetoothLeUtils.equals(android.util.SparseArray, android.util.SparseArray):boolean");
    }

    /* JADX WARNING: Missing block: B:20:0x0043, code skipped:
            return false;
     */
    static <T> boolean equals(java.util.Map<T, byte[]> r7, java.util.Map<T, byte[]> r8) {
        /*
        r0 = 1;
        if (r7 != r8) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = 0;
        if (r7 == 0) goto L_0x0043;
    L_0x0007:
        if (r8 != 0) goto L_0x000a;
    L_0x0009:
        goto L_0x0043;
    L_0x000a:
        r2 = r7.size();
        r3 = r8.size();
        if (r2 == r3) goto L_0x0015;
    L_0x0014:
        return r1;
    L_0x0015:
        r2 = r7.keySet();
        r3 = r8.keySet();
        r3 = r2.equals(r3);
        if (r3 != 0) goto L_0x0024;
    L_0x0023:
        return r1;
    L_0x0024:
        r3 = r2.iterator();
    L_0x0028:
        r4 = r3.hasNext();
        if (r4 == 0) goto L_0x0042;
    L_0x002e:
        r4 = r3.next();
        r5 = r7.get(r4);
        r6 = r8.get(r4);
        r5 = java.util.Objects.deepEquals(r5, r6);
        if (r5 != 0) goto L_0x0041;
    L_0x0040:
        return r1;
    L_0x0041:
        goto L_0x0028;
    L_0x0042:
        return r0;
    L_0x0043:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.bluetooth.le.BluetoothLeUtils.equals(java.util.Map, java.util.Map):boolean");
    }

    /* JADX WARNING: Missing block: B:13:0x0017, code skipped:
            return false;
     */
    static <T> boolean equals(byte[] r4, byte[] r5) {
        /*
        r0 = 1;
        if (r4 != r5) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = 0;
        if (r4 == 0) goto L_0x0017;
    L_0x0007:
        if (r5 != 0) goto L_0x000a;
    L_0x0009:
        goto L_0x0017;
    L_0x000a:
        r2 = r4.length;
        r3 = r5.length;
        if (r2 == r3) goto L_0x000f;
    L_0x000e:
        return r1;
    L_0x000f:
        r2 = java.util.Objects.deepEquals(r4, r5);
        if (r2 != 0) goto L_0x0016;
    L_0x0015:
        return r1;
    L_0x0016:
        return r0;
    L_0x0017:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.bluetooth.le.BluetoothLeUtils.equals(byte[], byte[]):boolean");
    }

    static void checkAdapterStateOn(BluetoothAdapter adapter) {
        if (adapter == null || !adapter.isLeEnabled()) {
            throw new IllegalStateException("BT Adapter is not turned ON");
        }
    }
}
