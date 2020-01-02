package miui.maml.data;

import android.util.Log;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import miui.maml.util.Utils;

public class Variables {
    private static boolean DBG = false;
    private static final String LOG_TAG = "Variables";
    public static final int MAX_ARRAY_SIZE = 10000;
    private DoubleBucket mDoubleBucket = new DoubleBucket();
    private VarBucket<Object> mObjectBucket = new VarBucket();

    private static abstract class BaseVarBucket {
        private HashMap<String, Integer> mIndices;
        private int mNextIndex;

        public abstract void onAddItem(int i);

        private BaseVarBucket() {
            this.mIndices = new HashMap();
            this.mNextIndex = 0;
        }

        public synchronized int registerVariable(String name) {
            Integer index;
            index = (Integer) this.mIndices.get(name);
            if (index == null) {
                index = Integer.valueOf(this.mNextIndex);
                this.mIndices.put(name, index);
                onAddItem(this.mNextIndex);
            }
            if (index.intValue() == this.mNextIndex) {
                this.mNextIndex++;
            }
            if (Variables.DBG) {
                String str = Variables.LOG_TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("registerVariable: ");
                stringBuilder.append(name);
                stringBuilder.append("  index:");
                stringBuilder.append(index);
                Log.d(str, stringBuilder.toString());
            }
            return index.intValue();
        }

        public boolean exists(String name) {
            return this.mIndices.containsKey(name);
        }
    }

    private static class DoubleBucket extends BaseVarBucket {
        private ArrayList<DoubleInfo> mArray;

        private DoubleBucket() {
            super();
            this.mArray = new ArrayList();
        }

        /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x0012 */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing block: B:10:0x000f, code skipped:
            return r0;
     */
        public final synchronized boolean exists(int r3) {
            /*
            r2 = this;
            monitor-enter(r2);
            r0 = 0;
            if (r3 >= 0) goto L_0x0005;
        L_0x0004:
            goto L_0x000e;
        L_0x0005:
            r1 = r2.mArray;	 Catch:{ IndexOutOfBoundsException -> 0x0015, all -> 0x0012 }
            r1 = r1.get(r3);	 Catch:{ IndexOutOfBoundsException -> 0x0010, all -> 0x0012 }
            if (r1 == 0) goto L_0x0004;
        L_0x000d:
            r0 = 1;
        L_0x000e:
            monitor-exit(r2);
            return r0;
        L_0x0010:
            r1 = move-exception;
            goto L_0x0016;
        L_0x0012:
            r3 = move-exception;
            monitor-exit(r2);
            throw r3;
        L_0x0015:
            r1 = move-exception;
        L_0x0016:
            monitor-exit(r2);
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: miui.maml.data.Variables$DoubleBucket.exists(int):boolean");
        }

        /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x0022 */
        /* JADX WARNING: Failed to process nested try/catch */
        public final synchronized void put(int r4, double r5) {
            /*
            r3 = this;
            monitor-enter(r3);
            if (r4 >= 0) goto L_0x0005;
        L_0x0003:
            monitor-exit(r3);
            return;
        L_0x0005:
            r0 = r3.mArray;	 Catch:{ IndexOutOfBoundsException -> 0x0025, all -> 0x0022 }
            r0 = r0.get(r4);	 Catch:{ IndexOutOfBoundsException -> 0x0020, all -> 0x0022 }
            r0 = (miui.maml.data.Variables.DoubleInfo) r0;	 Catch:{ IndexOutOfBoundsException -> 0x0020, all -> 0x0022 }
            if (r0 != 0) goto L_0x001c;
        L_0x000f:
            r1 = new miui.maml.data.Variables$DoubleInfo;	 Catch:{ IndexOutOfBoundsException -> 0x0020, all -> 0x0022 }
            r2 = 0;
            r1.<init>(r5, r2);	 Catch:{ IndexOutOfBoundsException -> 0x0020, all -> 0x0022 }
            r0 = r1;
            r1 = r3.mArray;	 Catch:{ IndexOutOfBoundsException -> 0x0020, all -> 0x0022 }
            r1.set(r4, r0);	 Catch:{ IndexOutOfBoundsException -> 0x0020, all -> 0x0022 }
            goto L_0x001f;
        L_0x001c:
            r0.setValue(r5);	 Catch:{ IndexOutOfBoundsException -> 0x0020, all -> 0x0022 }
        L_0x001f:
            goto L_0x0026;
        L_0x0020:
            r0 = move-exception;
            goto L_0x0026;
        L_0x0022:
            r4 = move-exception;
            monitor-exit(r3);
            throw r4;
        L_0x0025:
            r0 = move-exception;
        L_0x0026:
            monitor-exit(r3);
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: miui.maml.data.Variables$DoubleBucket.put(int, double):void");
        }

        /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x0014 */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing block: B:10:0x0011, code skipped:
            return r0;
     */
        public synchronized double get(int r4) {
            /*
            r3 = this;
            monitor-enter(r3);
            r0 = 0;
            r2 = r3.mArray;	 Catch:{ IndexOutOfBoundsException -> 0x0017, all -> 0x0014 }
            r2 = r2.get(r4);	 Catch:{ IndexOutOfBoundsException -> 0x0012, all -> 0x0014 }
            r2 = (miui.maml.data.Variables.DoubleInfo) r2;	 Catch:{ IndexOutOfBoundsException -> 0x0012, all -> 0x0014 }
            if (r2 != 0) goto L_0x000e;
        L_0x000d:
            goto L_0x0010;
        L_0x000e:
            r0 = r2.mValue;	 Catch:{ IndexOutOfBoundsException -> 0x0012, all -> 0x0014 }
        L_0x0010:
            monitor-exit(r3);
            return r0;
        L_0x0012:
            r2 = move-exception;
            goto L_0x0018;
        L_0x0014:
            r4 = move-exception;
            monitor-exit(r3);
            throw r4;
        L_0x0017:
            r2 = move-exception;
        L_0x0018:
            monitor-exit(r3);
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: miui.maml.data.Variables$DoubleBucket.get(int):double");
        }

        /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x0013 */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing block: B:10:0x0010, code skipped:
            return r0;
     */
        public synchronized int getVer(int r3) {
            /*
            r2 = this;
            monitor-enter(r2);
            r0 = -1;
            r1 = r2.mArray;	 Catch:{ IndexOutOfBoundsException -> 0x0016, all -> 0x0013 }
            r1 = r1.get(r3);	 Catch:{ IndexOutOfBoundsException -> 0x0011, all -> 0x0013 }
            r1 = (miui.maml.data.Variables.DoubleInfo) r1;	 Catch:{ IndexOutOfBoundsException -> 0x0011, all -> 0x0013 }
            if (r1 != 0) goto L_0x000d;
        L_0x000c:
            goto L_0x000f;
        L_0x000d:
            r0 = r1.mVersion;	 Catch:{ IndexOutOfBoundsException -> 0x0011, all -> 0x0013 }
        L_0x000f:
            monitor-exit(r2);
            return r0;
        L_0x0011:
            r1 = move-exception;
            goto L_0x0017;
        L_0x0013:
            r3 = move-exception;
            monitor-exit(r2);
            throw r3;
        L_0x0016:
            r1 = move-exception;
        L_0x0017:
            monitor-exit(r2);
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: miui.maml.data.Variables$DoubleBucket.getVer(int):int");
        }

        public void reset() {
            int M = this.mArray.size();
            for (int i = 0; i < M; i++) {
                DoubleInfo info = (DoubleInfo) this.mArray.get(i);
                if (info != null) {
                    info.setValue(0.0d);
                }
            }
        }

        /* Access modifiers changed, original: protected */
        public void onAddItem(int index) {
            while (this.mArray.size() <= index) {
                this.mArray.add(null);
            }
        }
    }

    private static class DoubleInfo {
        double mValue;
        int mVersion;

        public DoubleInfo(double value, int version) {
            this.mValue = value;
            this.mVersion = version;
        }

        public void setValue(double value) {
            this.mValue = value;
            this.mVersion++;
        }
    }

    private static class ValueInfo<T> {
        T mValue;
        int mVersion;

        public ValueInfo(T value, int version) {
            this.mValue = value;
            this.mVersion = version;
        }

        public void setValue(T value) {
            this.mValue = value;
            this.mVersion++;
        }

        public void reset() {
            Object[] value = this.mValue;
            int i;
            if (value instanceof double[]) {
                double[] value2 = (double[]) value;
                for (i = 0; i < value2.length; i++) {
                    value2[i] = 0.0d;
                }
            } else if (value instanceof float[]) {
                float[] value3 = (float[]) value;
                for (i = 0; i < value3.length; i++) {
                    value3[i] = 0.0f;
                }
            } else if (value instanceof int[]) {
                int[] value4 = (int[]) value;
                for (i = 0; i < value4.length; i++) {
                    value4[i] = 0;
                }
            } else if (value instanceof Object[]) {
                value = value;
                for (i = 0; i < value.length; i++) {
                    value[i] = null;
                }
            } else {
                setValue(null);
            }
        }
    }

    private static class VarBucket<T> extends BaseVarBucket {
        private ArrayList<ValueInfo<T>> mArray;

        private VarBucket() {
            super();
            this.mArray = new ArrayList();
        }

        /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x0022 */
        /* JADX WARNING: Failed to process nested try/catch */
        public final synchronized void put(int r4, T r5) {
            /*
            r3 = this;
            monitor-enter(r3);
            if (r4 >= 0) goto L_0x0005;
        L_0x0003:
            monitor-exit(r3);
            return;
        L_0x0005:
            r0 = r3.mArray;	 Catch:{ IndexOutOfBoundsException -> 0x0025, all -> 0x0022 }
            r0 = r0.get(r4);	 Catch:{ IndexOutOfBoundsException -> 0x0020, all -> 0x0022 }
            r0 = (miui.maml.data.Variables.ValueInfo) r0;	 Catch:{ IndexOutOfBoundsException -> 0x0020, all -> 0x0022 }
            if (r0 != 0) goto L_0x001c;
        L_0x000f:
            r1 = new miui.maml.data.Variables$ValueInfo;	 Catch:{ IndexOutOfBoundsException -> 0x0020, all -> 0x0022 }
            r2 = 0;
            r1.<init>(r5, r2);	 Catch:{ IndexOutOfBoundsException -> 0x0020, all -> 0x0022 }
            r0 = r1;
            r1 = r3.mArray;	 Catch:{ IndexOutOfBoundsException -> 0x0020, all -> 0x0022 }
            r1.set(r4, r0);	 Catch:{ IndexOutOfBoundsException -> 0x0020, all -> 0x0022 }
            goto L_0x001f;
        L_0x001c:
            r0.setValue(r5);	 Catch:{ IndexOutOfBoundsException -> 0x0020, all -> 0x0022 }
        L_0x001f:
            goto L_0x0026;
        L_0x0020:
            r0 = move-exception;
            goto L_0x0026;
        L_0x0022:
            r4 = move-exception;
            monitor-exit(r3);
            throw r4;
        L_0x0025:
            r0 = move-exception;
        L_0x0026:
            monitor-exit(r3);
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: miui.maml.data.Variables$VarBucket.put(int, java.lang.Object):void");
        }

        /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x0013 */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing block: B:10:0x0010, code skipped:
            return r0;
     */
        public synchronized T get(int r3) {
            /*
            r2 = this;
            monitor-enter(r2);
            r0 = 0;
            r1 = r2.mArray;	 Catch:{ IndexOutOfBoundsException -> 0x0016, all -> 0x0013 }
            r1 = r1.get(r3);	 Catch:{ IndexOutOfBoundsException -> 0x0011, all -> 0x0013 }
            r1 = (miui.maml.data.Variables.ValueInfo) r1;	 Catch:{ IndexOutOfBoundsException -> 0x0011, all -> 0x0013 }
            if (r1 != 0) goto L_0x000d;
        L_0x000c:
            goto L_0x000f;
        L_0x000d:
            r0 = r1.mValue;	 Catch:{ IndexOutOfBoundsException -> 0x0011, all -> 0x0013 }
        L_0x000f:
            monitor-exit(r2);
            return r0;
        L_0x0011:
            r1 = move-exception;
            goto L_0x0017;
        L_0x0013:
            r3 = move-exception;
            monitor-exit(r2);
            throw r3;
        L_0x0016:
            r1 = move-exception;
        L_0x0017:
            monitor-exit(r2);
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: miui.maml.data.Variables$VarBucket.get(int):java.lang.Object");
        }

        /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x0013 */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing block: B:10:0x0010, code skipped:
            return r0;
     */
        public synchronized int getVer(int r3) {
            /*
            r2 = this;
            monitor-enter(r2);
            r0 = -1;
            r1 = r2.mArray;	 Catch:{ IndexOutOfBoundsException -> 0x0016, all -> 0x0013 }
            r1 = r1.get(r3);	 Catch:{ IndexOutOfBoundsException -> 0x0011, all -> 0x0013 }
            r1 = (miui.maml.data.Variables.ValueInfo) r1;	 Catch:{ IndexOutOfBoundsException -> 0x0011, all -> 0x0013 }
            if (r1 != 0) goto L_0x000d;
        L_0x000c:
            goto L_0x000f;
        L_0x000d:
            r0 = r1.mVersion;	 Catch:{ IndexOutOfBoundsException -> 0x0011, all -> 0x0013 }
        L_0x000f:
            monitor-exit(r2);
            return r0;
        L_0x0011:
            r1 = move-exception;
            goto L_0x0017;
        L_0x0013:
            r3 = move-exception;
            monitor-exit(r2);
            throw r3;
        L_0x0016:
            r1 = move-exception;
        L_0x0017:
            monitor-exit(r2);
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: miui.maml.data.Variables$VarBucket.getVer(int):int");
        }

        public void reset() {
            int M = this.mArray.size();
            for (int i = 0; i < M; i++) {
                ValueInfo<T> info = (ValueInfo) this.mArray.get(i);
                if (info != null) {
                    info.reset();
                }
            }
        }

        /* Access modifiers changed, original: protected */
        public void onAddItem(int index) {
            while (this.mArray.size() <= index) {
                this.mArray.add(null);
            }
        }
    }

    public int registerDoubleVariable(String name) {
        return this.mDoubleBucket.registerVariable(name);
    }

    public int registerVariable(String name) {
        return this.mObjectBucket.registerVariable(name);
    }

    public boolean existsDouble(String name) {
        return this.mDoubleBucket.exists(name);
    }

    public boolean existsDouble(int index) {
        return this.mDoubleBucket.exists(index);
    }

    public boolean existsObj(String name) {
        return this.mObjectBucket.exists(name);
    }

    public boolean existsArrItem(int index, int arrIndex) {
        Object arr = get(index);
        boolean z = false;
        if (arr == null) {
            return false;
        }
        if (arrIndex >= 0) {
            try {
                if (arrIndex < Array.getLength(arr)) {
                    z = true;
                }
            } catch (RuntimeException e) {
                return false;
            }
        }
        return z;
    }

    public boolean createArray(String name, int size, Class<?> type) {
        boolean array = false;
        if (type == null || size <= 0 || size > 10000) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("createArray failed: name= ");
            stringBuilder.append(name);
            stringBuilder.append("  size=");
            stringBuilder.append(size);
            Log.e(LOG_TAG, stringBuilder.toString());
            return false;
        }
        int id = registerVariable(name);
        if (get(id) != null) {
            return false;
        }
        try {
            array = Array.newInstance(type, size);
            put(id, (Object) array);
            return true;
        } catch (Exception e) {
            return array;
        }
    }

    @Deprecated
    public final void putNum(String name, double value) {
        put(name, value);
    }

    public final void put(String name, double value) {
        put(registerDoubleVariable(name), value);
    }

    public final void put(int index, double value) {
        this.mDoubleBucket.put(index, value);
    }

    public final boolean putDouble(int index, Object value) {
        if (value instanceof Number) {
            put(index, ((Number) value).doubleValue());
            return true;
        } else if (value instanceof Boolean) {
            put(index, ((Boolean) value).booleanValue() ? 1.0d : 0.0d);
            return true;
        } else {
            if (value instanceof String) {
                try {
                    put(index, Double.parseDouble((String) value));
                    return true;
                } catch (NumberFormatException e) {
                }
            }
            return false;
        }
    }

    public void put(String name, Object value) {
        put(registerVariable(name), value);
    }

    public final void put(int index, Object value) {
        this.mObjectBucket.put(index, value);
    }

    public Object get(String name) {
        return get(registerVariable(name));
    }

    public Object get(int index) {
        return this.mObjectBucket.get(index);
    }

    public double getDouble(int index) {
        return this.mDoubleBucket.get(index);
    }

    public double getDouble(String name) {
        return getDouble(registerDoubleVariable(name));
    }

    public String getString(int index) {
        try {
            return (String) get(index);
        } catch (ClassCastException e) {
            return null;
        }
    }

    public String getString(String name) {
        return getString(registerVariable(name));
    }

    public Object getArr(int index, int arrIndex) {
        return getArrInner(index, arrIndex);
    }

    public double getArrDouble(int index, int arrIndex) {
        double d = 0.0d;
        StringBuilder stringBuilder;
        try {
            Object arr = get(index);
            if (arr == null) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("getArrDouble: designated array does not exist. index:");
                stringBuilder.append(index);
                dbglog(stringBuilder.toString());
                return 0.0d;
            } else if (!(arr instanceof boolean[])) {
                return Array.getDouble(arr, arrIndex);
            } else {
                if (((boolean[]) arr)[index]) {
                    d = 1.0d;
                }
                return d;
            }
        } catch (Exception e) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("getArrDouble: designated index is invalid. index:");
            stringBuilder.append(index);
            stringBuilder.append(" arrIndex:");
            stringBuilder.append(arrIndex);
            dbglog(stringBuilder.toString());
        }
    }

    public String getArrString(int index, int arrIndex) {
        return (String) getArrInner(index, arrIndex);
    }

    public boolean putArr(int index, int arrIndex, Object value) {
        StringBuilder stringBuilder;
        try {
            Object arr = (Object[]) get(index);
            if (arr == null) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("putArr: designated array does not exist. index:");
                stringBuilder.append(index);
                dbglog(stringBuilder.toString());
                return false;
            }
            arr[arrIndex] = value;
            put(index, arr);
            return true;
        } catch (ClassCastException e) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("putArr: designated object is not an object array. index:");
            stringBuilder.append(index);
            dbglog(stringBuilder.toString());
        } catch (IndexOutOfBoundsException e2) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("putArr: designated array index is invalid. index:");
            stringBuilder.append(index);
            stringBuilder.append(" arrIndex:");
            stringBuilder.append(arrIndex);
            dbglog(stringBuilder.toString());
        }
    }

    public boolean putArrDouble(int index, int arrIndex, Object value) {
        if (value instanceof Number) {
            return putArr(index, arrIndex, ((Number) value).doubleValue());
        }
        if (value instanceof String) {
            try {
                return putArr(index, arrIndex, Utils.parseDouble((String) value));
            } catch (NumberFormatException e) {
            }
        }
        return false;
    }

    public boolean putArr(int index, int arrIndex, double value) {
        StringBuilder stringBuilder;
        try {
            Object array = get(index);
            if (array == null) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("putArr: designated array does not exist. index:");
                stringBuilder.append(index);
                dbglog(stringBuilder.toString());
                return false;
            }
            if (array instanceof double[]) {
                ((double[]) array)[arrIndex] = value;
            } else if (array instanceof byte[]) {
                ((byte[]) array)[arrIndex] = (byte) ((int) ((long) value));
            } else if (array instanceof char[]) {
                ((char[]) array)[arrIndex] = (char) ((int) ((long) value));
            } else if (array instanceof float[]) {
                ((float[]) array)[arrIndex] = (float) value;
            } else if (array instanceof int[]) {
                ((int[]) array)[arrIndex] = (int) ((long) value);
            } else if (array instanceof long[]) {
                ((long[]) array)[arrIndex] = (long) value;
            } else if (array instanceof short[]) {
                ((short[]) array)[arrIndex] = (short) ((int) ((long) value));
            } else if (array instanceof boolean[]) {
                ((boolean[]) array)[arrIndex] = value > 0.0d;
            }
            put(index, array);
            return true;
        } catch (Exception e) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("putArr: failed. index:");
            stringBuilder.append(index);
            stringBuilder.append(" arrIndex:");
            stringBuilder.append(arrIndex);
            stringBuilder.append("\n");
            stringBuilder.append(e.toString());
            dbglog(stringBuilder.toString());
            return false;
        }
    }

    private <T> T getArrInner(int index, int arrIndex) {
        StringBuilder stringBuilder;
        try {
            Object[] arr = (Object[]) get(index);
            if (arr != null) {
                return arr[arrIndex];
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("getArrInner: designated object is not an array. index:");
            stringBuilder.append(index);
            dbglog(stringBuilder.toString());
            return null;
        } catch (ClassCastException e) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("getArrInner: designated object type is not correct. index:");
            stringBuilder.append(index);
            dbglog(stringBuilder.toString());
        } catch (IndexOutOfBoundsException e2) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("getArrInner: designated index is invalid. index:");
            stringBuilder.append(index);
            stringBuilder.append(" arrIndex:");
            stringBuilder.append(arrIndex);
            dbglog(stringBuilder.toString());
        }
    }

    private static void dbglog(String info) {
        if (DBG) {
            Log.d(LOG_TAG, info);
        }
    }

    public int getVer(int index, boolean isNumber) {
        return isNumber ? this.mDoubleBucket.getVer(index) : this.mObjectBucket.getVer(index);
    }

    public void reset() {
        this.mDoubleBucket.reset();
        this.mObjectBucket.reset();
    }
}
