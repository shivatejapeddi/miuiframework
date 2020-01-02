package com.android.internal.util;

import android.annotation.UnsupportedAppUsage;
import android.util.ArraySet;
import dalvik.system.VMRuntime;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import libcore.util.EmptyArray;

public class ArrayUtils {
    private static final int CACHE_SIZE = 73;
    public static final File[] EMPTY_FILE = new File[0];
    private static Object[] sCache = new Object[73];

    private ArrayUtils() {
    }

    public static byte[] newUnpaddedByteArray(int minLen) {
        return (byte[]) VMRuntime.getRuntime().newUnpaddedArray(Byte.TYPE, minLen);
    }

    public static char[] newUnpaddedCharArray(int minLen) {
        return (char[]) VMRuntime.getRuntime().newUnpaddedArray(Character.TYPE, minLen);
    }

    @UnsupportedAppUsage
    public static int[] newUnpaddedIntArray(int minLen) {
        return (int[]) VMRuntime.getRuntime().newUnpaddedArray(Integer.TYPE, minLen);
    }

    public static boolean[] newUnpaddedBooleanArray(int minLen) {
        return (boolean[]) VMRuntime.getRuntime().newUnpaddedArray(Boolean.TYPE, minLen);
    }

    public static long[] newUnpaddedLongArray(int minLen) {
        return (long[]) VMRuntime.getRuntime().newUnpaddedArray(Long.TYPE, minLen);
    }

    public static float[] newUnpaddedFloatArray(int minLen) {
        return (float[]) VMRuntime.getRuntime().newUnpaddedArray(Float.TYPE, minLen);
    }

    public static Object[] newUnpaddedObjectArray(int minLen) {
        return (Object[]) VMRuntime.getRuntime().newUnpaddedArray(Object.class, minLen);
    }

    @UnsupportedAppUsage
    public static <T> T[] newUnpaddedArray(Class<T> clazz, int minLen) {
        return (Object[]) VMRuntime.getRuntime().newUnpaddedArray(clazz, minLen);
    }

    public static boolean equals(byte[] array1, byte[] array2, int length) {
        if (length < 0) {
            throw new IllegalArgumentException();
        } else if (array1 == array2) {
            return true;
        } else {
            if (array1 == null || array2 == null || array1.length < length || array2.length < length) {
                return false;
            }
            for (int i = 0; i < length; i++) {
                if (array1[i] != array2[i]) {
                    return false;
                }
            }
            return true;
        }
    }

    @UnsupportedAppUsage
    public static <T> T[] emptyArray(Class<T> kind) {
        if (kind == Object.class) {
            return EmptyArray.OBJECT;
        }
        int bucket = (kind.hashCode() & Integer.MAX_VALUE) % 73;
        Object cache = sCache[bucket];
        if (cache == null || cache.getClass().getComponentType() != kind) {
            cache = Array.newInstance(kind, 0);
            sCache[bucket] = cache;
        }
        return (Object[]) cache;
    }

    public static boolean isEmpty(Collection<?> array) {
        return array == null || array.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    @UnsupportedAppUsage
    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(long[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(byte[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(boolean[] array) {
        return array == null || array.length == 0;
    }

    public static int size(Object[] array) {
        return array == null ? 0 : array.length;
    }

    public static int size(Collection<?> collection) {
        return collection == null ? 0 : collection.size();
    }

    @UnsupportedAppUsage
    public static <T> boolean contains(T[] array, T value) {
        return indexOf(array, value) != -1;
    }

    @UnsupportedAppUsage
    public static <T> int indexOf(T[] array, T value) {
        if (array == null) {
            return -1;
        }
        for (int i = 0; i < array.length; i++) {
            if (Objects.equals(array[i], value)) {
                return i;
            }
        }
        return -1;
    }

    public static <T> boolean containsAll(T[] array, T[] check) {
        if (check == null) {
            return true;
        }
        for (Object checkItem : check) {
            if (!contains((Object[]) array, checkItem)) {
                return false;
            }
        }
        return true;
    }

    public static <T> boolean containsAny(T[] array, T[] check) {
        if (check == null) {
            return false;
        }
        for (Object checkItem : check) {
            if (contains((Object[]) array, checkItem)) {
                return true;
            }
        }
        return false;
    }

    @UnsupportedAppUsage
    public static boolean contains(int[] array, int value) {
        if (array == null) {
            return false;
        }
        for (int element : array) {
            if (element == value) {
                return true;
            }
        }
        return false;
    }

    public static boolean contains(long[] array, long value) {
        if (array == null) {
            return false;
        }
        for (long element : array) {
            if (element == value) {
                return true;
            }
        }
        return false;
    }

    public static boolean contains(char[] array, char value) {
        if (array == null) {
            return false;
        }
        for (char element : array) {
            if (element == value) {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean containsAll(char[] array, char[] check) {
        if (check == null) {
            return true;
        }
        for (char checkItem : check) {
            if (!contains(array, checkItem)) {
                return false;
            }
        }
        return true;
    }

    public static long total(long[] array) {
        long total = 0;
        if (array != null) {
            for (long value : array) {
                total += value;
            }
        }
        return total;
    }

    public static int[] convertToIntArray(List<Integer> list) {
        int[] array = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = ((Integer) list.get(i)).intValue();
        }
        return array;
    }

    public static long[] convertToLongArray(int[] intArray) {
        if (intArray == null) {
            return null;
        }
        long[] array = new long[intArray.length];
        for (int i = 0; i < intArray.length; i++) {
            array[i] = (long) intArray[i];
        }
        return array;
    }

    public static <T> T[] concatElements(Class<T> kind, T[] a, T[] b) {
        int an = a != null ? a.length : 0;
        int bn = b != null ? b.length : 0;
        if (an == 0 && bn == 0) {
            if (kind == String.class) {
                return (Object[]) EmptyArray.STRING;
            }
            if (kind == Object.class) {
                return EmptyArray.OBJECT;
            }
        }
        Object[] res = (Object[]) Array.newInstance(kind, an + bn);
        if (an > 0) {
            System.arraycopy(a, 0, res, 0, an);
        }
        if (bn > 0) {
            System.arraycopy(b, 0, res, an, bn);
        }
        return res;
    }

    @UnsupportedAppUsage
    public static <T> T[] appendElement(Class<T> kind, T[] array, T element) {
        return appendElement(kind, array, element, false);
    }

    public static <T> T[] appendElement(Class<T> kind, T[] array, T element, boolean allowDuplicates) {
        int end;
        T[] result;
        if (array == null) {
            end = 0;
            result = (Object[]) Array.newInstance(kind, 1);
        } else if (!allowDuplicates && contains((Object[]) array, (Object) element)) {
            return array;
        } else {
            end = array.length;
            result = (Object[]) Array.newInstance(kind, end + 1);
            System.arraycopy(array, 0, result, 0, end);
        }
        result[end] = element;
        return result;
    }

    @UnsupportedAppUsage
    public static <T> T[] removeElement(Class<T> kind, T[] array, T element) {
        if (array == null || !contains((Object[]) array, (Object) element)) {
            return array;
        }
        int length = array.length;
        int i = 0;
        while (i < length) {
            if (!Objects.equals(array[i], element)) {
                i++;
            } else if (length == 1) {
                return null;
            } else {
                Object[] result = (Object[]) Array.newInstance(kind, length - 1);
                System.arraycopy(array, 0, result, 0, i);
                System.arraycopy(array, i + 1, result, i, (length - i) - 1);
                return result;
            }
        }
        return array;
    }

    public static int[] appendInt(int[] cur, int val, boolean allowDuplicates) {
        if (cur == null) {
            return new int[]{val};
        }
        if (!allowDuplicates) {
            for (int i : cur) {
                if (i == val) {
                    return cur;
                }
            }
        }
        int[] ret = new int[(N + 1)];
        System.arraycopy(cur, 0, ret, 0, N);
        ret[N] = val;
        return ret;
    }

    @UnsupportedAppUsage
    public static int[] appendInt(int[] cur, int val) {
        return appendInt(cur, val, false);
    }

    public static int[] removeInt(int[] cur, int val) {
        if (cur == null) {
            return null;
        }
        int N = cur.length;
        for (int i = 0; i < N; i++) {
            if (cur[i] == val) {
                int[] ret = new int[(N - 1)];
                if (i > 0) {
                    System.arraycopy(cur, 0, ret, 0, i);
                }
                if (i < N - 1) {
                    System.arraycopy(cur, i + 1, ret, i, (N - i) - 1);
                }
                return ret;
            }
        }
        return cur;
    }

    public static String[] removeString(String[] cur, String val) {
        if (cur == null) {
            return null;
        }
        int N = cur.length;
        for (int i = 0; i < N; i++) {
            if (Objects.equals(cur[i], val)) {
                String[] ret = new String[(N - 1)];
                if (i > 0) {
                    System.arraycopy(cur, 0, ret, 0, i);
                }
                if (i < N - 1) {
                    System.arraycopy(cur, i + 1, ret, i, (N - i) - 1);
                }
                return ret;
            }
        }
        return cur;
    }

    public static long[] appendLong(long[] cur, long val, boolean allowDuplicates) {
        if (cur == null) {
            return new long[]{val};
        }
        if (!allowDuplicates) {
            for (long j : cur) {
                if (j == val) {
                    return cur;
                }
            }
        }
        long[] ret = new long[(N + 1)];
        System.arraycopy(cur, 0, ret, 0, N);
        ret[N] = val;
        return ret;
    }

    public static long[] appendLong(long[] cur, long val) {
        return appendLong(cur, val, false);
    }

    public static long[] removeLong(long[] cur, long val) {
        if (cur == null) {
            return null;
        }
        int N = cur.length;
        for (int i = 0; i < N; i++) {
            if (cur[i] == val) {
                long[] ret = new long[(N - 1)];
                if (i > 0) {
                    System.arraycopy(cur, 0, ret, 0, i);
                }
                if (i < N - 1) {
                    System.arraycopy(cur, i + 1, ret, i, (N - i) - 1);
                }
                return ret;
            }
        }
        return cur;
    }

    public static long[] cloneOrNull(long[] array) {
        return array != null ? (long[]) array.clone() : null;
    }

    public static <T> T[] cloneOrNull(T[] array) {
        return array != null ? (Object[]) array.clone() : null;
    }

    public static <T> ArraySet<T> cloneOrNull(ArraySet<T> array) {
        return array != null ? new ArraySet((ArraySet) array) : null;
    }

    public static <T> ArraySet<T> add(ArraySet<T> cur, T val) {
        if (cur == null) {
            cur = new ArraySet();
        }
        cur.add(val);
        return cur;
    }

    public static <T> ArraySet<T> remove(ArraySet<T> cur, T val) {
        if (cur == null) {
            return null;
        }
        cur.remove(val);
        if (cur.isEmpty()) {
            return null;
        }
        return cur;
    }

    public static <T> ArrayList<T> add(ArrayList<T> cur, T val) {
        if (cur == null) {
            cur = new ArrayList();
        }
        cur.add(val);
        return cur;
    }

    public static <T> ArrayList<T> remove(ArrayList<T> cur, T val) {
        if (cur == null) {
            return null;
        }
        cur.remove(val);
        if (cur.isEmpty()) {
            return null;
        }
        return cur;
    }

    public static <T> boolean contains(Collection<T> cur, T val) {
        return cur != null ? cur.contains(val) : false;
    }

    public static <T> T[] trimToSize(T[] array, int size) {
        if (array == null || size == 0) {
            return null;
        }
        if (array.length == size) {
            return array;
        }
        return Arrays.copyOf(array, size);
    }

    public static <T> boolean referenceEquals(ArrayList<T> a, ArrayList<T> b) {
        boolean z = true;
        if (a == b) {
            return true;
        }
        int sizeA = a.size();
        if (sizeA != b.size()) {
            return false;
        }
        boolean diff = false;
        for (int i = 0; i < sizeA && !diff; i++) {
            diff |= a.get(i) != b.get(i) ? 1 : 0;
        }
        if (diff) {
            z = false;
        }
        return z;
    }

    public static <T> int unstableRemoveIf(ArrayList<T> collection, Predicate<T> predicate) {
        if (collection == null) {
            return 0;
        }
        int size = collection.size();
        int leftIdx = 0;
        int rightIdx = size - 1;
        while (leftIdx <= rightIdx) {
            while (leftIdx < size && !predicate.test(collection.get(leftIdx))) {
                leftIdx++;
            }
            while (rightIdx > leftIdx && predicate.test(collection.get(rightIdx))) {
                rightIdx--;
            }
            if (leftIdx >= rightIdx) {
                break;
            }
            Collections.swap(collection, leftIdx, rightIdx);
            leftIdx++;
            rightIdx--;
        }
        for (int i = size - 1; i >= leftIdx; i--) {
            collection.remove(i);
        }
        return size - leftIdx;
    }

    public static int[] defeatNullable(int[] val) {
        return val != null ? val : EmptyArray.INT;
    }

    public static String[] defeatNullable(String[] val) {
        return val != null ? val : EmptyArray.STRING;
    }

    public static File[] defeatNullable(File[] val) {
        return val != null ? val : EMPTY_FILE;
    }

    public static void checkBounds(int len, int index) {
        if (index < 0 || len <= index) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("length=");
            stringBuilder.append(len);
            stringBuilder.append("; index=");
            stringBuilder.append(index);
            throw new ArrayIndexOutOfBoundsException(stringBuilder.toString());
        }
    }

    public static <T> T[] filterNotNull(T[] val, IntFunction<T[]> arrayConstructor) {
        int nullCount = 0;
        int size = size((Object[]) val);
        for (int i = 0; i < size; i++) {
            if (val[i] == null) {
                nullCount++;
            }
        }
        if (nullCount == 0) {
            return val;
        }
        Object[] result = (Object[]) arrayConstructor.apply(size - nullCount);
        int outIdx = 0;
        for (int i2 = 0; i2 < size; i2++) {
            if (val[i2] != null) {
                int outIdx2 = outIdx + 1;
                result[outIdx] = val[i2];
                outIdx = outIdx2;
            }
        }
        return result;
    }

    /* JADX WARNING: Missing block: B:15:0x001b, code skipped:
            return false;
     */
    public static boolean startsWith(byte[] r4, byte[] r5) {
        /*
        r0 = 0;
        if (r4 == 0) goto L_0x001b;
    L_0x0003:
        if (r5 != 0) goto L_0x0006;
    L_0x0005:
        goto L_0x001b;
    L_0x0006:
        r1 = r4.length;
        r2 = r5.length;
        if (r1 >= r2) goto L_0x000b;
    L_0x000a:
        return r0;
    L_0x000b:
        r1 = 0;
    L_0x000c:
        r2 = r5.length;
        if (r1 >= r2) goto L_0x0019;
    L_0x000f:
        r2 = r4[r1];
        r3 = r5[r1];
        if (r2 == r3) goto L_0x0016;
    L_0x0015:
        return r0;
    L_0x0016:
        r1 = r1 + 1;
        goto L_0x000c;
    L_0x0019:
        r0 = 1;
        return r0;
    L_0x001b:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.util.ArrayUtils.startsWith(byte[], byte[]):boolean");
    }

    public static <T> T find(T[] items, Predicate<T> predicate) {
        if (isEmpty((Object[]) items)) {
            return null;
        }
        for (T item : items) {
            if (predicate.test(item)) {
                return item;
            }
        }
        return null;
    }

    public static String deepToString(Object value) {
        if (value == null || !value.getClass().isArray()) {
            return String.valueOf(value);
        }
        if (value.getClass() == boolean[].class) {
            return Arrays.toString((boolean[]) value);
        }
        if (value.getClass() == byte[].class) {
            return Arrays.toString((byte[]) value);
        }
        if (value.getClass() == char[].class) {
            return Arrays.toString((char[]) value);
        }
        if (value.getClass() == double[].class) {
            return Arrays.toString((double[]) value);
        }
        if (value.getClass() == float[].class) {
            return Arrays.toString((float[]) value);
        }
        if (value.getClass() == int[].class) {
            return Arrays.toString((int[]) value);
        }
        if (value.getClass() == long[].class) {
            return Arrays.toString((long[]) value);
        }
        if (value.getClass() == short[].class) {
            return Arrays.toString((short[]) value);
        }
        return Arrays.deepToString((Object[]) value);
    }

    public static <T> T firstOrNull(T[] items) {
        return items.length > 0 ? items[0] : null;
    }
}
