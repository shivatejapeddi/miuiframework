package android.os;

import android.annotation.UnsupportedAppUsage;
import android.util.ArrayMap;
import android.util.Log;
import android.util.MathUtils;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.IndentingPrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

public class BaseBundle {
    private static final int BUNDLE_MAGIC = 1279544898;
    private static final int BUNDLE_MAGIC_NATIVE = 1279544900;
    static final boolean DEBUG = false;
    static final int FLAG_DEFUSABLE = 1;
    private static final boolean LOG_DEFUSABLE = false;
    private static final String TAG = "Bundle";
    private static volatile boolean sShouldDefuse = false;
    private ClassLoader mClassLoader;
    @VisibleForTesting
    public int mFlags;
    @UnsupportedAppUsage
    ArrayMap<String, Object> mMap;
    private boolean mParcelledByNative;
    @UnsupportedAppUsage
    Parcel mParcelledData;

    static final class NoImagePreloadHolder {
        public static final Parcel EMPTY_PARCEL = Parcel.obtain();

        NoImagePreloadHolder() {
        }
    }

    public static void setShouldDefuse(boolean shouldDefuse) {
        sShouldDefuse = shouldDefuse;
    }

    BaseBundle(ClassLoader loader, int capacity) {
        this.mMap = null;
        this.mParcelledData = null;
        this.mMap = capacity > 0 ? new ArrayMap(capacity) : new ArrayMap();
        this.mClassLoader = loader == null ? getClass().getClassLoader() : loader;
    }

    BaseBundle() {
        this((ClassLoader) null, 0);
    }

    BaseBundle(Parcel parcelledData) {
        this.mMap = null;
        this.mParcelledData = null;
        readFromParcelInner(parcelledData);
    }

    BaseBundle(Parcel parcelledData, int length) {
        this.mMap = null;
        this.mParcelledData = null;
        readFromParcelInner(parcelledData, length);
    }

    BaseBundle(ClassLoader loader) {
        this(loader, 0);
    }

    BaseBundle(int capacity) {
        this((ClassLoader) null, capacity);
    }

    BaseBundle(BaseBundle b) {
        this.mMap = null;
        this.mParcelledData = null;
        copyInternal(b, false);
    }

    BaseBundle(boolean doInit) {
        this.mMap = null;
        this.mParcelledData = null;
    }

    public String getPairValue() {
        unparcel();
        int size = this.mMap.size();
        if (size > 1) {
            Log.w(TAG, "getPairValue() used on Bundle with multiple pairs.");
        }
        if (size == 0) {
            return null;
        }
        Object o = this.mMap.valueAt(0);
        try {
            return (String) o;
        } catch (ClassCastException e) {
            typeWarning("getPairValue()", o, "String", e);
            return null;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void setClassLoader(ClassLoader loader) {
        this.mClassLoader = loader;
    }

    /* Access modifiers changed, original: 0000 */
    public ClassLoader getClassLoader() {
        return this.mClassLoader;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void unparcel() {
        synchronized (this) {
            Parcel source = this.mParcelledData;
            if (source != null) {
                initializeFromParcelLocked(source, true, this.mParcelledByNative);
            }
        }
    }

    /* JADX WARNING: Missing block: B:21:0x0048, code skipped:
            if (r10 != false) goto L_0x004a;
     */
    /* JADX WARNING: Missing block: B:22:0x004a, code skipped:
            recycleParcel(r9);
     */
    /* JADX WARNING: Missing block: B:23:0x004d, code skipped:
            r8.mParcelledData = null;
            r8.mParcelledByNative = false;
     */
    /* JADX WARNING: Missing block: B:33:0x0069, code skipped:
            if (r10 != false) goto L_0x004a;
     */
    /* JADX WARNING: Missing block: B:41:0x007b, code skipped:
            if (r10 != false) goto L_0x004a;
     */
    /* JADX WARNING: Missing block: B:42:0x007e, code skipped:
            return;
     */
    private void initializeFromParcelLocked(android.os.Parcel r9, boolean r10, boolean r11) {
        /*
        r8 = this;
        r0 = isEmptyParcel(r9);
        r1 = 0;
        r2 = 0;
        if (r0 == 0) goto L_0x001d;
    L_0x0008:
        r0 = r8.mMap;
        if (r0 != 0) goto L_0x0015;
    L_0x000c:
        r0 = new android.util.ArrayMap;
        r3 = 1;
        r0.<init>(r3);
        r8.mMap = r0;
        goto L_0x0018;
    L_0x0015:
        r0.erase();
    L_0x0018:
        r8.mParcelledData = r2;
        r8.mParcelledByNative = r1;
        return;
    L_0x001d:
        r0 = r9.readInt();
        if (r0 >= 0) goto L_0x0024;
    L_0x0023:
        return;
    L_0x0024:
        r3 = r8.mMap;
        if (r3 != 0) goto L_0x002f;
    L_0x0028:
        r4 = new android.util.ArrayMap;
        r4.<init>(r0);
        r3 = r4;
        goto L_0x0035;
    L_0x002f:
        r3.erase();
        r3.ensureCapacity(r0);
    L_0x0035:
        r4 = "Failed to parse Bundle, but defusing quietly";
        r5 = "Bundle";
        if (r11 == 0) goto L_0x0041;
    L_0x003b:
        r6 = r8.mClassLoader;	 Catch:{ BadParcelableException -> 0x006e, RuntimeException -> 0x0054 }
        r9.readArrayMapSafelyInternal(r3, r0, r6);	 Catch:{ BadParcelableException -> 0x006e, RuntimeException -> 0x0054 }
        goto L_0x0046;
    L_0x0041:
        r6 = r8.mClassLoader;	 Catch:{ BadParcelableException -> 0x006e, RuntimeException -> 0x0054 }
        r9.readArrayMapInternal(r3, r0, r6);	 Catch:{ BadParcelableException -> 0x006e, RuntimeException -> 0x0054 }
    L_0x0046:
        r8.mMap = r3;
        if (r10 == 0) goto L_0x004d;
    L_0x004a:
        recycleParcel(r9);
    L_0x004d:
        r8.mParcelledData = r2;
        r8.mParcelledByNative = r1;
        goto L_0x007e;
    L_0x0052:
        r4 = move-exception;
        goto L_0x0081;
    L_0x0054:
        r6 = move-exception;
        r7 = sShouldDefuse;	 Catch:{ all -> 0x0052 }
        if (r7 == 0) goto L_0x006c;
    L_0x0059:
        r7 = r6.getCause();	 Catch:{ all -> 0x0052 }
        r7 = r7 instanceof java.lang.ClassNotFoundException;	 Catch:{ all -> 0x0052 }
        if (r7 == 0) goto L_0x006c;
    L_0x0061:
        android.util.Log.w(r5, r4, r6);	 Catch:{ all -> 0x0052 }
        r3.erase();	 Catch:{ all -> 0x0052 }
        r8.mMap = r3;
        if (r10 == 0) goto L_0x004d;
    L_0x006b:
        goto L_0x004a;
        throw r6;	 Catch:{ all -> 0x0052 }
    L_0x006e:
        r6 = move-exception;
        r7 = sShouldDefuse;	 Catch:{ all -> 0x0052 }
        if (r7 == 0) goto L_0x007f;
    L_0x0073:
        android.util.Log.w(r5, r4, r6);	 Catch:{ all -> 0x0052 }
        r3.erase();	 Catch:{ all -> 0x0052 }
        r8.mMap = r3;
        if (r10 == 0) goto L_0x004d;
    L_0x007d:
        goto L_0x004a;
    L_0x007e:
        return;
        throw r6;	 Catch:{ all -> 0x0052 }
    L_0x0081:
        r8.mMap = r3;
        if (r10 == 0) goto L_0x0088;
    L_0x0085:
        recycleParcel(r9);
    L_0x0088:
        r8.mParcelledData = r2;
        r8.mParcelledByNative = r1;
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.BaseBundle.initializeFromParcelLocked(android.os.Parcel, boolean, boolean):void");
    }

    @UnsupportedAppUsage
    public boolean isParcelled() {
        return this.mParcelledData != null;
    }

    public boolean isEmptyParcel() {
        return isEmptyParcel(this.mParcelledData);
    }

    private static boolean isEmptyParcel(Parcel p) {
        return p == NoImagePreloadHolder.EMPTY_PARCEL;
    }

    private static void recycleParcel(Parcel p) {
        if (p != null && !isEmptyParcel(p)) {
            p.recycle();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public ArrayMap<String, Object> getMap() {
        unparcel();
        return this.mMap;
    }

    public int size() {
        unparcel();
        return this.mMap.size();
    }

    public boolean isEmpty() {
        unparcel();
        return this.mMap.isEmpty();
    }

    public boolean maybeIsEmpty() {
        if (isParcelled()) {
            return isEmptyParcel();
        }
        return isEmpty();
    }

    public static boolean kindofEquals(BaseBundle a, BaseBundle b) {
        return a == b || (a != null && a.kindofEquals(b));
    }

    public boolean kindofEquals(BaseBundle other) {
        boolean z = false;
        if (other == null || isParcelled() != other.isParcelled()) {
            return false;
        }
        if (!isParcelled()) {
            return this.mMap.equals(other.mMap);
        }
        if (this.mParcelledData.compareData(other.mParcelledData) == 0) {
            z = true;
        }
        return z;
    }

    public void clear() {
        unparcel();
        this.mMap.clear();
    }

    /* Access modifiers changed, original: 0000 */
    public void copyInternal(BaseBundle from, boolean deep) {
        synchronized (from) {
            if (from.mParcelledData == null) {
                this.mParcelledData = null;
                this.mParcelledByNative = false;
            } else if (from.isEmptyParcel()) {
                this.mParcelledData = NoImagePreloadHolder.EMPTY_PARCEL;
                this.mParcelledByNative = false;
            } else {
                this.mParcelledData = Parcel.obtain();
                Parcel parcel = this.mParcelledData;
                Parcel parcel2 = from.mParcelledData;
                parcel.appendFrom(parcel2, 0, parcel2.dataSize());
                this.mParcelledData.setDataPosition(0);
                this.mParcelledByNative = from.mParcelledByNative;
            }
            ArrayMap fromMap = from.mMap;
            if (fromMap == null) {
                this.mMap = null;
            } else if (deep) {
                int N = fromMap.size();
                this.mMap = new ArrayMap(N);
                for (int i = 0; i < N; i++) {
                    this.mMap.append((String) fromMap.keyAt(i), deepCopyValue(fromMap.valueAt(i)));
                }
            } else {
                this.mMap = new ArrayMap(fromMap);
            }
            this.mClassLoader = from.mClassLoader;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public Object deepCopyValue(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Bundle) {
            return ((Bundle) value).deepCopy();
        }
        if (value instanceof PersistableBundle) {
            return ((PersistableBundle) value).deepCopy();
        }
        if (value instanceof ArrayList) {
            return deepcopyArrayList((ArrayList) value);
        }
        if (value.getClass().isArray()) {
            if (value instanceof int[]) {
                return ((int[]) value).clone();
            }
            if (value instanceof long[]) {
                return ((long[]) value).clone();
            }
            if (value instanceof float[]) {
                return ((float[]) value).clone();
            }
            if (value instanceof double[]) {
                return ((double[]) value).clone();
            }
            if (value instanceof Object[]) {
                return ((Object[]) value).clone();
            }
            if (value instanceof byte[]) {
                return ((byte[]) value).clone();
            }
            if (value instanceof short[]) {
                return ((short[]) value).clone();
            }
            if (value instanceof char[]) {
                return ((char[]) value).clone();
            }
        }
        return value;
    }

    /* Access modifiers changed, original: 0000 */
    public ArrayList deepcopyArrayList(ArrayList from) {
        int N = from.size();
        ArrayList out = new ArrayList(N);
        for (int i = 0; i < N; i++) {
            out.add(deepCopyValue(from.get(i)));
        }
        return out;
    }

    public boolean containsKey(String key) {
        unparcel();
        return this.mMap.containsKey(key);
    }

    public Object get(String key) {
        unparcel();
        return this.mMap.get(key);
    }

    public void remove(String key) {
        unparcel();
        this.mMap.remove(key);
    }

    public void putAll(PersistableBundle bundle) {
        unparcel();
        bundle.unparcel();
        this.mMap.putAll(bundle.mMap);
    }

    /* Access modifiers changed, original: 0000 */
    public void putAll(ArrayMap map) {
        unparcel();
        this.mMap.putAll(map);
    }

    public Set<String> keySet() {
        unparcel();
        return this.mMap.keySet();
    }

    public void putBoolean(String key, boolean value) {
        unparcel();
        this.mMap.put(key, Boolean.valueOf(value));
    }

    /* Access modifiers changed, original: 0000 */
    public void putByte(String key, byte value) {
        unparcel();
        this.mMap.put(key, Byte.valueOf(value));
    }

    /* Access modifiers changed, original: 0000 */
    public void putChar(String key, char value) {
        unparcel();
        this.mMap.put(key, Character.valueOf(value));
    }

    /* Access modifiers changed, original: 0000 */
    public void putShort(String key, short value) {
        unparcel();
        this.mMap.put(key, Short.valueOf(value));
    }

    public void putInt(String key, int value) {
        unparcel();
        this.mMap.put(key, Integer.valueOf(value));
    }

    public void putLong(String key, long value) {
        unparcel();
        this.mMap.put(key, Long.valueOf(value));
    }

    /* Access modifiers changed, original: 0000 */
    public void putFloat(String key, float value) {
        unparcel();
        this.mMap.put(key, Float.valueOf(value));
    }

    public void putDouble(String key, double value) {
        unparcel();
        this.mMap.put(key, Double.valueOf(value));
    }

    public void putString(String key, String value) {
        unparcel();
        this.mMap.put(key, value);
    }

    /* Access modifiers changed, original: 0000 */
    public void putCharSequence(String key, CharSequence value) {
        unparcel();
        this.mMap.put(key, value);
    }

    /* Access modifiers changed, original: 0000 */
    public void putIntegerArrayList(String key, ArrayList<Integer> value) {
        unparcel();
        this.mMap.put(key, value);
    }

    /* Access modifiers changed, original: 0000 */
    public void putStringArrayList(String key, ArrayList<String> value) {
        unparcel();
        this.mMap.put(key, value);
    }

    /* Access modifiers changed, original: 0000 */
    public void putCharSequenceArrayList(String key, ArrayList<CharSequence> value) {
        unparcel();
        this.mMap.put(key, value);
    }

    /* Access modifiers changed, original: 0000 */
    public void putSerializable(String key, Serializable value) {
        unparcel();
        this.mMap.put(key, value);
    }

    public void putBooleanArray(String key, boolean[] value) {
        unparcel();
        this.mMap.put(key, value);
    }

    /* Access modifiers changed, original: 0000 */
    public void putByteArray(String key, byte[] value) {
        unparcel();
        this.mMap.put(key, value);
    }

    /* Access modifiers changed, original: 0000 */
    public void putShortArray(String key, short[] value) {
        unparcel();
        this.mMap.put(key, value);
    }

    /* Access modifiers changed, original: 0000 */
    public void putCharArray(String key, char[] value) {
        unparcel();
        this.mMap.put(key, value);
    }

    public void putIntArray(String key, int[] value) {
        unparcel();
        this.mMap.put(key, value);
    }

    public void putLongArray(String key, long[] value) {
        unparcel();
        this.mMap.put(key, value);
    }

    /* Access modifiers changed, original: 0000 */
    public void putFloatArray(String key, float[] value) {
        unparcel();
        this.mMap.put(key, value);
    }

    public void putDoubleArray(String key, double[] value) {
        unparcel();
        this.mMap.put(key, value);
    }

    public void putStringArray(String key, String[] value) {
        unparcel();
        this.mMap.put(key, value);
    }

    /* Access modifiers changed, original: 0000 */
    public void putCharSequenceArray(String key, CharSequence[] value) {
        unparcel();
        this.mMap.put(key, value);
    }

    public boolean getBoolean(String key) {
        unparcel();
        return getBoolean(key, false);
    }

    /* Access modifiers changed, original: 0000 */
    public void typeWarning(String key, Object value, String className, Object defaultValue, ClassCastException e) {
        StringBuilder sb = new StringBuilder();
        sb.append("Key ");
        sb.append(key);
        sb.append(" expected ");
        sb.append(className);
        sb.append(" but value was a ");
        sb.append(value.getClass().getName());
        sb.append(".  The default value ");
        sb.append(defaultValue);
        sb.append(" was returned.");
        String stringBuilder = sb.toString();
        String str = TAG;
        Log.w(str, stringBuilder);
        Log.w(str, "Attempt to cast generated internal exception:", e);
    }

    /* Access modifiers changed, original: 0000 */
    public void typeWarning(String key, Object value, String className, ClassCastException e) {
        typeWarning(key, value, className, "<null>", e);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        unparcel();
        Object o = this.mMap.get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return ((Boolean) o).booleanValue();
        } catch (ClassCastException e) {
            ClassCastException e2 = e;
            String str = key;
            Object obj = o;
            typeWarning(str, obj, "Boolean", Boolean.valueOf(defaultValue), e2);
            return defaultValue;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public byte getByte(String key) {
        unparcel();
        return getByte(key, (byte) 0).byteValue();
    }

    /* Access modifiers changed, original: 0000 */
    public Byte getByte(String key, byte defaultValue) {
        unparcel();
        Object o = this.mMap.get(key);
        if (o == null) {
            return Byte.valueOf(defaultValue);
        }
        try {
            return (Byte) o;
        } catch (ClassCastException e) {
            String str = key;
            Object obj = o;
            typeWarning(str, obj, "Byte", Byte.valueOf(defaultValue), e);
            return Byte.valueOf(defaultValue);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public char getChar(String key) {
        unparcel();
        return getChar(key, 0);
    }

    /* Access modifiers changed, original: 0000 */
    public char getChar(String key, char defaultValue) {
        unparcel();
        Object o = this.mMap.get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return ((Character) o).charValue();
        } catch (ClassCastException e) {
            ClassCastException e2 = e;
            String str = key;
            Object obj = o;
            typeWarning(str, obj, "Character", Character.valueOf(defaultValue), e2);
            return defaultValue;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public short getShort(String key) {
        unparcel();
        return getShort(key, (short) 0);
    }

    /* Access modifiers changed, original: 0000 */
    public short getShort(String key, short defaultValue) {
        unparcel();
        Object o = this.mMap.get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return ((Short) o).shortValue();
        } catch (ClassCastException e) {
            ClassCastException e2 = e;
            String str = key;
            Object obj = o;
            typeWarning(str, obj, "Short", Short.valueOf(defaultValue), e2);
            return defaultValue;
        }
    }

    public int getInt(String key) {
        unparcel();
        return getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        unparcel();
        Object o = this.mMap.get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return ((Integer) o).intValue();
        } catch (ClassCastException e) {
            ClassCastException e2 = e;
            String str = key;
            Object obj = o;
            typeWarning(str, obj, "Integer", Integer.valueOf(defaultValue), e2);
            return defaultValue;
        }
    }

    public long getLong(String key) {
        unparcel();
        return getLong(key, 0);
    }

    public long getLong(String key, long defaultValue) {
        unparcel();
        Object o = this.mMap.get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return ((Long) o).longValue();
        } catch (ClassCastException e) {
            ClassCastException e2 = e;
            String str = key;
            Object obj = o;
            typeWarning(str, obj, "Long", Long.valueOf(defaultValue), e2);
            return defaultValue;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public float getFloat(String key) {
        unparcel();
        return getFloat(key, 0.0f);
    }

    /* Access modifiers changed, original: 0000 */
    public float getFloat(String key, float defaultValue) {
        unparcel();
        Object o = this.mMap.get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return ((Float) o).floatValue();
        } catch (ClassCastException e) {
            ClassCastException e2 = e;
            String str = key;
            Object obj = o;
            typeWarning(str, obj, "Float", Float.valueOf(defaultValue), e2);
            return defaultValue;
        }
    }

    public double getDouble(String key) {
        unparcel();
        return getDouble(key, 0.0d);
    }

    public double getDouble(String key, double defaultValue) {
        unparcel();
        Object o = this.mMap.get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return ((Double) o).doubleValue();
        } catch (ClassCastException e) {
            ClassCastException e2 = e;
            String str = key;
            Object obj = o;
            typeWarning(str, obj, "Double", Double.valueOf(defaultValue), e2);
            return defaultValue;
        }
    }

    public String getString(String key) {
        unparcel();
        Object o = this.mMap.get(key);
        try {
            return (String) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "String", e);
            return null;
        }
    }

    public String getString(String key, String defaultValue) {
        String s = getString(key);
        return s == null ? defaultValue : s;
    }

    /* Access modifiers changed, original: 0000 */
    public CharSequence getCharSequence(String key) {
        unparcel();
        Object o = this.mMap.get(key);
        try {
            return (CharSequence) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "CharSequence", e);
            return null;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public CharSequence getCharSequence(String key, CharSequence defaultValue) {
        CharSequence cs = getCharSequence(key);
        return cs == null ? defaultValue : cs;
    }

    /* Access modifiers changed, original: 0000 */
    public Serializable getSerializable(String key) {
        unparcel();
        Object o = this.mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (Serializable) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "Serializable", e);
            return null;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public ArrayList<Integer> getIntegerArrayList(String key) {
        unparcel();
        Object o = this.mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (ArrayList) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "ArrayList<Integer>", e);
            return null;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public ArrayList<String> getStringArrayList(String key) {
        unparcel();
        Object o = this.mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (ArrayList) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "ArrayList<String>", e);
            return null;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public ArrayList<CharSequence> getCharSequenceArrayList(String key) {
        unparcel();
        Object o = this.mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (ArrayList) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "ArrayList<CharSequence>", e);
            return null;
        }
    }

    public boolean[] getBooleanArray(String key) {
        unparcel();
        Object o = this.mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (boolean[]) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "byte[]", e);
            return null;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public byte[] getByteArray(String key) {
        unparcel();
        Object o = this.mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (byte[]) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "byte[]", e);
            return null;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public short[] getShortArray(String key) {
        unparcel();
        Object o = this.mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (short[]) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "short[]", e);
            return null;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public char[] getCharArray(String key) {
        unparcel();
        Object o = this.mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (char[]) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "char[]", e);
            return null;
        }
    }

    public int[] getIntArray(String key) {
        unparcel();
        Object o = this.mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (int[]) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "int[]", e);
            return null;
        }
    }

    public long[] getLongArray(String key) {
        unparcel();
        Object o = this.mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (long[]) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "long[]", e);
            return null;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public float[] getFloatArray(String key) {
        unparcel();
        Object o = this.mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (float[]) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "float[]", e);
            return null;
        }
    }

    public double[] getDoubleArray(String key) {
        unparcel();
        Object o = this.mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (double[]) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "double[]", e);
            return null;
        }
    }

    public String[] getStringArray(String key) {
        unparcel();
        Object o = this.mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (String[]) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "String[]", e);
            return null;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public CharSequence[] getCharSequenceArray(String key) {
        unparcel();
        Object o = this.mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (CharSequence[]) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "CharSequence[]", e);
            return null;
        }
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Missing block: B:15:0x0035, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:18:0x0039, code skipped:
            if (r0 == null) goto L_0x0064;
     */
    /* JADX WARNING: Missing block: B:20:0x003f, code skipped:
            if (r0.size() > 0) goto L_0x0042;
     */
    /* JADX WARNING: Missing block: B:21:0x0042, code skipped:
            r2 = r6.dataPosition();
            r6.writeInt(-1);
            r6.writeInt(BUNDLE_MAGIC);
            r1 = r6.dataPosition();
            r6.writeArrayMapInternal(r0);
            r3 = r6.dataPosition();
            r6.setDataPosition(r2);
            r6.writeInt(r3 - r1);
            r6.setDataPosition(r3);
     */
    /* JADX WARNING: Missing block: B:22:0x0063, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:23:0x0064, code skipped:
            r6.writeInt(0);
     */
    /* JADX WARNING: Missing block: B:24:0x0067, code skipped:
            return;
     */
    public void writeToParcelInner(android.os.Parcel r6, int r7) {
        /*
        r5 = this;
        r0 = r6.hasReadWriteHelper();
        if (r0 == 0) goto L_0x0009;
    L_0x0006:
        r5.unparcel();
    L_0x0009:
        monitor-enter(r5);
        r0 = r5.mParcelledData;	 Catch:{ all -> 0x0068 }
        r1 = 1279544898; // 0x4c444e42 float:5.146036E7 double:6.321791764E-315;
        r2 = 0;
        if (r0 == 0) goto L_0x0036;
    L_0x0012:
        r0 = r5.mParcelledData;	 Catch:{ all -> 0x0068 }
        r3 = android.os.BaseBundle.NoImagePreloadHolder.EMPTY_PARCEL;	 Catch:{ all -> 0x0068 }
        if (r0 != r3) goto L_0x001c;
    L_0x0018:
        r6.writeInt(r2);	 Catch:{ all -> 0x0068 }
        goto L_0x0034;
    L_0x001c:
        r0 = r5.mParcelledData;	 Catch:{ all -> 0x0068 }
        r0 = r0.dataSize();	 Catch:{ all -> 0x0068 }
        r6.writeInt(r0);	 Catch:{ all -> 0x0068 }
        r3 = r5.mParcelledByNative;	 Catch:{ all -> 0x0068 }
        if (r3 == 0) goto L_0x002c;
    L_0x0029:
        r1 = 1279544900; // 0x4c444e44 float:5.1460368E7 double:6.321791774E-315;
    L_0x002c:
        r6.writeInt(r1);	 Catch:{ all -> 0x0068 }
        r1 = r5.mParcelledData;	 Catch:{ all -> 0x0068 }
        r6.appendFrom(r1, r2, r0);	 Catch:{ all -> 0x0068 }
    L_0x0034:
        monitor-exit(r5);	 Catch:{ all -> 0x0068 }
        return;
    L_0x0036:
        r0 = r5.mMap;	 Catch:{ all -> 0x0068 }
        monitor-exit(r5);	 Catch:{ all -> 0x0068 }
        if (r0 == 0) goto L_0x0064;
    L_0x003b:
        r3 = r0.size();
        if (r3 > 0) goto L_0x0042;
    L_0x0041:
        goto L_0x0064;
    L_0x0042:
        r2 = r6.dataPosition();
        r3 = -1;
        r6.writeInt(r3);
        r6.writeInt(r1);
        r1 = r6.dataPosition();
        r6.writeArrayMapInternal(r0);
        r3 = r6.dataPosition();
        r6.setDataPosition(r2);
        r4 = r3 - r1;
        r6.writeInt(r4);
        r6.setDataPosition(r3);
        return;
    L_0x0064:
        r6.writeInt(r2);
        return;
    L_0x0068:
        r0 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x0068 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.BaseBundle.writeToParcelInner(android.os.Parcel, int):void");
    }

    /* Access modifiers changed, original: 0000 */
    public void readFromParcelInner(Parcel parcel) {
        readFromParcelInner(parcel, parcel.readInt());
    }

    private void readFromParcelInner(Parcel parcel, int length) {
        StringBuilder stringBuilder;
        if (length < 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Bad length in parcel: ");
            stringBuilder.append(length);
            throw new RuntimeException(stringBuilder.toString());
        } else if (length == 0) {
            this.mParcelledData = NoImagePreloadHolder.EMPTY_PARCEL;
            this.mParcelledByNative = false;
        } else if (length % 4 == 0) {
            int magic = parcel.readInt();
            boolean isNativeBundle = true;
            boolean isJavaBundle = magic == BUNDLE_MAGIC;
            if (magic != BUNDLE_MAGIC_NATIVE) {
                isNativeBundle = false;
            }
            if (!isJavaBundle && !isNativeBundle) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Bad magic number for Bundle: 0x");
                stringBuilder2.append(Integer.toHexString(magic));
                throw new IllegalStateException(stringBuilder2.toString());
            } else if (parcel.hasReadWriteHelper()) {
                synchronized (this) {
                    initializeFromParcelLocked(parcel, false, isNativeBundle);
                }
            } else {
                int offset = parcel.dataPosition();
                parcel.setDataPosition(MathUtils.addOrThrow(offset, length));
                Parcel p = Parcel.obtain();
                p.setDataPosition(0);
                p.appendFrom(parcel, offset, length);
                p.adoptClassCookies(parcel);
                p.setDataPosition(0);
                this.mParcelledData = p;
                this.mParcelledByNative = isNativeBundle;
            }
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Bundle length is not aligned by 4: ");
            stringBuilder.append(length);
            throw new IllegalStateException(stringBuilder.toString());
        }
    }

    public static void dumpStats(IndentingPrintWriter pw, String key, Object value) {
        Parcel tmp = Parcel.obtain();
        tmp.writeValue(value);
        int size = tmp.dataPosition();
        tmp.recycle();
        if (size > 1024) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(key);
            stringBuilder.append(" [size=");
            stringBuilder.append(size);
            stringBuilder.append("]");
            pw.println(stringBuilder.toString());
            if (value instanceof BaseBundle) {
                dumpStats(pw, (BaseBundle) value);
            } else if (value instanceof SparseArray) {
                dumpStats(pw, (SparseArray) value);
            }
        }
    }

    public static void dumpStats(IndentingPrintWriter pw, SparseArray array) {
        pw.increaseIndent();
        if (array == null) {
            pw.println("[null]");
            return;
        }
        for (int i = 0; i < array.size(); i++) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("0x");
            stringBuilder.append(Integer.toHexString(array.keyAt(i)));
            dumpStats(pw, stringBuilder.toString(), array.valueAt(i));
        }
        pw.decreaseIndent();
    }

    public static void dumpStats(IndentingPrintWriter pw, BaseBundle bundle) {
        pw.increaseIndent();
        if (bundle == null) {
            pw.println("[null]");
            return;
        }
        ArrayMap<String, Object> map = bundle.getMap();
        for (int i = 0; i < map.size(); i++) {
            dumpStats(pw, (String) map.keyAt(i), map.valueAt(i));
        }
        pw.decreaseIndent();
    }
}
