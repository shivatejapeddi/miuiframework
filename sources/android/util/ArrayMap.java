package android.util;

import android.annotation.UnsupportedAppUsage;
import com.android.internal.util.ArrayUtils;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import libcore.util.EmptyArray;

public final class ArrayMap<K, V> implements Map<K, V> {
    private static final int BASE_SIZE = 4;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private static final int CACHE_SIZE = 10;
    private static final boolean CONCURRENT_MODIFICATION_EXCEPTIONS = true;
    private static final boolean DEBUG = false;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public static final ArrayMap EMPTY = new ArrayMap(-1);
    @UnsupportedAppUsage(maxTargetSdk = 28)
    static final int[] EMPTY_IMMUTABLE_INTS = new int[0];
    private static final String TAG = "ArrayMap";
    @UnsupportedAppUsage(maxTargetSdk = 28)
    static Object[] mBaseCache;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    static int mBaseCacheSize;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    static Object[] mTwiceBaseCache;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    static int mTwiceBaseCacheSize;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    Object[] mArray;
    MapCollections<K, V> mCollections;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    int[] mHashes;
    final boolean mIdentityHashCode;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    int mSize;

    private static int binarySearchHashes(int[] hashes, int N, int hash) {
        try {
            return ContainerHelpers.binarySearch(hashes, N, hash);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ConcurrentModificationException();
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public int indexOf(Object key, int hash) {
        int N = this.mSize;
        if (N == 0) {
            return -1;
        }
        int index = binarySearchHashes(this.mHashes, N, hash);
        if (index < 0 || key.equals(this.mArray[index << 1])) {
            return index;
        }
        int end = index + 1;
        while (end < N && this.mHashes[end] == hash) {
            if (key.equals(this.mArray[end << 1])) {
                return end;
            }
            end++;
        }
        int i = index - 1;
        while (i >= 0 && this.mHashes[i] == hash) {
            if (key.equals(this.mArray[i << 1])) {
                return i;
            }
            i--;
        }
        return ~end;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public int indexOfNull() {
        int N = this.mSize;
        if (N == 0) {
            return -1;
        }
        int index = binarySearchHashes(this.mHashes, N, 0);
        if (index < 0 || this.mArray[index << 1] == null) {
            return index;
        }
        int end = index + 1;
        while (end < N && this.mHashes[end] == 0) {
            if (this.mArray[end << 1] == null) {
                return end;
            }
            end++;
        }
        int i = index - 1;
        while (i >= 0 && this.mHashes[i] == 0) {
            if (this.mArray[i << 1] == null) {
                return i;
            }
            i--;
        }
        return ~end;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    private void allocArrays(int size) {
        if (this.mHashes != EMPTY_IMMUTABLE_INTS) {
            Object[] array;
            if (size == 8) {
                synchronized (ArrayMap.class) {
                    if (mTwiceBaseCache != null) {
                        array = mTwiceBaseCache;
                        mTwiceBaseCache = (Object[]) array[0];
                        this.mHashes = (int[]) array[1];
                        this.mArray = array;
                        array[1] = null;
                        array[0] = null;
                        mTwiceBaseCacheSize--;
                        return;
                    }
                }
            } else if (size == 4) {
                synchronized (ArrayMap.class) {
                    if (mBaseCache != null) {
                        array = mBaseCache;
                        mBaseCache = (Object[]) array[0];
                        this.mHashes = (int[]) array[1];
                        this.mArray = array;
                        array[1] = null;
                        array[0] = null;
                        mBaseCacheSize--;
                        return;
                    }
                }
            }
            this.mHashes = new int[size];
            this.mArray = new Object[(size << 1)];
            return;
        }
        throw new UnsupportedOperationException("ArrayMap is immutable");
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    private static void freeArrays(int[] hashes, Object[] array, int size) {
        int i;
        if (hashes.length == 8) {
            synchronized (ArrayMap.class) {
                if (mTwiceBaseCacheSize < 10) {
                    array[0] = mTwiceBaseCache;
                    array[1] = hashes;
                    for (i = (size << 1) - 1; i >= 2; i--) {
                        array[i] = null;
                    }
                    mTwiceBaseCache = array;
                    mTwiceBaseCacheSize++;
                }
            }
        } else if (hashes.length == 4) {
            synchronized (ArrayMap.class) {
                if (mBaseCacheSize < 10) {
                    array[0] = mBaseCache;
                    array[1] = hashes;
                    for (i = (size << 1) - 1; i >= 2; i--) {
                        array[i] = null;
                    }
                    mBaseCache = array;
                    mBaseCacheSize++;
                }
            }
        }
    }

    public ArrayMap() {
        this(0, false);
    }

    public ArrayMap(int capacity) {
        this(capacity, false);
    }

    public ArrayMap(int capacity, boolean identityHashCode) {
        this.mIdentityHashCode = identityHashCode;
        if (capacity < 0) {
            this.mHashes = EMPTY_IMMUTABLE_INTS;
            this.mArray = EmptyArray.OBJECT;
        } else if (capacity == 0) {
            this.mHashes = EmptyArray.INT;
            this.mArray = EmptyArray.OBJECT;
        } else {
            allocArrays(capacity);
        }
        this.mSize = 0;
    }

    public ArrayMap(ArrayMap<K, V> map) {
        this();
        if (map != null) {
            putAll((ArrayMap) map);
        }
    }

    public void clear() {
        if (this.mSize > 0) {
            int[] ohashes = this.mHashes;
            Object[] oarray = this.mArray;
            int osize = this.mSize;
            this.mHashes = EmptyArray.INT;
            this.mArray = EmptyArray.OBJECT;
            this.mSize = 0;
            freeArrays(ohashes, oarray, osize);
        }
        if (this.mSize > 0) {
            throw new ConcurrentModificationException();
        }
    }

    public void erase() {
        int N = this.mSize;
        if (N > 0) {
            N <<= 1;
            Object[] array = this.mArray;
            for (int i = 0; i < N; i++) {
                array[i] = null;
            }
            this.mSize = 0;
        }
    }

    public void ensureCapacity(int minimumCapacity) {
        int osize = this.mSize;
        if (this.mHashes.length < minimumCapacity) {
            int[] ohashes = this.mHashes;
            Object[] oarray = this.mArray;
            allocArrays(minimumCapacity);
            if (this.mSize > 0) {
                System.arraycopy(ohashes, 0, this.mHashes, 0, osize);
                System.arraycopy(oarray, 0, this.mArray, 0, osize << 1);
            }
            freeArrays(ohashes, oarray, osize);
        }
        if (this.mSize != osize) {
            throw new ConcurrentModificationException();
        }
    }

    public boolean containsKey(Object key) {
        return indexOfKey(key) >= 0;
    }

    public int indexOfKey(Object key) {
        if (key == null) {
            return indexOfNull();
        }
        return indexOf(key, this.mIdentityHashCode ? System.identityHashCode(key) : key.hashCode());
    }

    public int indexOfValue(Object value) {
        int N = this.mSize * 2;
        Object[] array = this.mArray;
        int i;
        if (value == null) {
            for (i = 1; i < N; i += 2) {
                if (array[i] == null) {
                    return i >> 1;
                }
            }
        } else {
            for (i = 1; i < N; i += 2) {
                if (value.equals(array[i])) {
                    return i >> 1;
                }
            }
        }
        return -1;
    }

    public boolean containsValue(Object value) {
        return indexOfValue(value) >= 0;
    }

    public V get(Object key) {
        int index = indexOfKey(key);
        return index >= 0 ? this.mArray[(index << 1) + 1] : null;
    }

    public K keyAt(int index) {
        if (index < this.mSize || !UtilConfig.sThrowExceptionForUpperArrayOutOfBounds) {
            return this.mArray[index << 1];
        }
        throw new ArrayIndexOutOfBoundsException(index);
    }

    public V valueAt(int index) {
        if (index < this.mSize || !UtilConfig.sThrowExceptionForUpperArrayOutOfBounds) {
            return this.mArray[(index << 1) + 1];
        }
        throw new ArrayIndexOutOfBoundsException(index);
    }

    public V setValueAt(int index, V value) {
        if (index < this.mSize || !UtilConfig.sThrowExceptionForUpperArrayOutOfBounds) {
            int index2 = (index << 1) + 1;
            Object[] objArr = this.mArray;
            V old = objArr[index2];
            objArr[index2] = value;
            return old;
        }
        throw new ArrayIndexOutOfBoundsException(index);
    }

    public boolean isEmpty() {
        return this.mSize <= 0;
    }

    public V put(K key, V value) {
        int hash;
        int index;
        int osize = this.mSize;
        if (key == null) {
            hash = 0;
            index = indexOfNull();
        } else {
            hash = this.mIdentityHashCode ? System.identityHashCode(key) : key.hashCode();
            index = indexOf(key, hash);
        }
        int index2;
        if (index >= 0) {
            index2 = (index << 1) + 1;
            Object[] objArr = this.mArray;
            V old = objArr[index2];
            objArr[index2] = value;
            return old;
        }
        int[] ohashes;
        index = ~index;
        if (osize >= this.mHashes.length) {
            index2 = 4;
            if (osize >= 8) {
                index2 = (osize >> 1) + osize;
            } else if (osize >= 4) {
                index2 = 8;
            }
            ohashes = this.mHashes;
            Object[] oarray = this.mArray;
            allocArrays(index2);
            if (osize == this.mSize) {
                int[] iArr = this.mHashes;
                if (iArr.length > 0) {
                    System.arraycopy(ohashes, 0, iArr, 0, ohashes.length);
                    System.arraycopy(oarray, 0, this.mArray, 0, oarray.length);
                }
                freeArrays(ohashes, oarray, osize);
            } else {
                throw new ConcurrentModificationException();
            }
        }
        if (index < osize) {
            int[] iArr2 = this.mHashes;
            System.arraycopy(iArr2, index, iArr2, index + 1, osize - index);
            Object[] objArr2 = this.mArray;
            System.arraycopy(objArr2, index << 1, objArr2, (index + 1) << 1, (this.mSize - index) << 1);
        }
        index2 = this.mSize;
        if (osize == index2) {
            ohashes = this.mHashes;
            if (index < ohashes.length) {
                ohashes[index] = hash;
                Object[] objArr3 = this.mArray;
                objArr3[index << 1] = key;
                objArr3[(index << 1) + 1] = value;
                this.mSize = index2 + 1;
                return null;
            }
        }
        throw new ConcurrentModificationException();
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public void append(K key, V value) {
        int index = this.mSize;
        int hash = key == null ? 0 : this.mIdentityHashCode ? System.identityHashCode(key) : key.hashCode();
        int[] iArr = this.mHashes;
        if (index >= iArr.length) {
            throw new IllegalStateException("Array is full");
        } else if (index <= 0 || iArr[index - 1] <= hash) {
            this.mSize = index + 1;
            this.mHashes[index] = hash;
            index <<= 1;
            Object[] objArr = this.mArray;
            objArr[index] = key;
            objArr[index + 1] = value;
        } else {
            RuntimeException e = new RuntimeException("here");
            e.fillInStackTrace();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("New hash ");
            stringBuilder.append(hash);
            stringBuilder.append(" is before end of array hash ");
            stringBuilder.append(this.mHashes[index - 1]);
            stringBuilder.append(" at index ");
            stringBuilder.append(index);
            stringBuilder.append(" key ");
            stringBuilder.append(key);
            Log.w(TAG, stringBuilder.toString(), e);
            put(key, value);
        }
    }

    public void validate() {
        int N = this.mSize;
        if (N > 1) {
            int basehash = this.mHashes[0];
            int basei = 0;
            for (int i = 1; i < N; i++) {
                int hash = this.mHashes[i];
                if (hash != basehash) {
                    basehash = hash;
                    basei = i;
                } else {
                    Object cur = this.mArray[i << 1];
                    int j = i - 1;
                    while (j >= basei) {
                        Object prev = this.mArray[j << 1];
                        String str = "Duplicate key in ArrayMap: ";
                        StringBuilder stringBuilder;
                        if (cur == prev) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append(str);
                            stringBuilder.append(cur);
                            throw new IllegalArgumentException(stringBuilder.toString());
                        } else if (cur == null || prev == null || !cur.equals(prev)) {
                            j--;
                        } else {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append(str);
                            stringBuilder.append(cur);
                            throw new IllegalArgumentException(stringBuilder.toString());
                        }
                    }
                    continue;
                }
            }
        }
    }

    public void putAll(ArrayMap<? extends K, ? extends V> array) {
        int N = array.mSize;
        ensureCapacity(this.mSize + N);
        if (this.mSize != 0) {
            for (int i = 0; i < N; i++) {
                put(array.keyAt(i), array.valueAt(i));
            }
        } else if (N > 0) {
            System.arraycopy(array.mHashes, 0, this.mHashes, 0, N);
            System.arraycopy(array.mArray, 0, this.mArray, 0, N << 1);
            this.mSize = N;
        }
    }

    public V remove(Object key) {
        int index = indexOfKey(key);
        if (index >= 0) {
            return removeAt(index);
        }
        return null;
    }

    public V removeAt(int index) {
        if (index < this.mSize || !UtilConfig.sThrowExceptionForUpperArrayOutOfBounds) {
            int nsize;
            Object old = this.mArray[(index << 1) + 1];
            int osize = this.mSize;
            if (osize <= 1) {
                int[] ohashes = this.mHashes;
                Object[] oarray = this.mArray;
                this.mHashes = EmptyArray.INT;
                this.mArray = EmptyArray.OBJECT;
                freeArrays(ohashes, oarray, osize);
                nsize = 0;
            } else {
                int nsize2 = osize - 1;
                int[] iArr = this.mHashes;
                int i = 8;
                if (iArr.length <= 8 || this.mSize >= iArr.length / 3) {
                    Object[] objArr;
                    if (index < nsize2) {
                        iArr = this.mHashes;
                        System.arraycopy(iArr, index + 1, iArr, index, nsize2 - index);
                        objArr = this.mArray;
                        System.arraycopy(objArr, (index + 1) << 1, objArr, index << 1, (nsize2 - index) << 1);
                    }
                    objArr = this.mArray;
                    objArr[nsize2 << 1] = null;
                    objArr[(nsize2 << 1) + 1] = null;
                } else {
                    if (osize > 8) {
                        i = osize + (osize >> 1);
                    }
                    int n = i;
                    int[] ohashes2 = this.mHashes;
                    Object[] oarray2 = this.mArray;
                    allocArrays(n);
                    if (osize == this.mSize) {
                        if (index > 0) {
                            System.arraycopy(ohashes2, 0, this.mHashes, 0, index);
                            System.arraycopy(oarray2, 0, this.mArray, 0, index << 1);
                        }
                        if (index < nsize2) {
                            System.arraycopy(ohashes2, index + 1, this.mHashes, index, nsize2 - index);
                            System.arraycopy(oarray2, (index + 1) << 1, this.mArray, index << 1, (nsize2 - index) << 1);
                        }
                    } else {
                        throw new ConcurrentModificationException();
                    }
                }
                nsize = nsize2;
            }
            if (osize == this.mSize) {
                this.mSize = nsize;
                return old;
            }
            throw new ConcurrentModificationException();
        }
        throw new ArrayIndexOutOfBoundsException(index);
    }

    public int size() {
        return this.mSize;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Map)) {
            return false;
        }
        Map<?, ?> map = (Map) object;
        if (size() != map.size()) {
            return false;
        }
        int i = 0;
        while (i < this.mSize) {
            try {
                K key = keyAt(i);
                V mine = valueAt(i);
                Object theirs = map.get(key);
                if (mine == null) {
                    if (theirs != null || !map.containsKey(key)) {
                        return false;
                    }
                } else if (!mine.equals(theirs)) {
                    return false;
                }
                i++;
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e2) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int[] hashes = this.mHashes;
        Object[] array = this.mArray;
        int result = 0;
        int i = 0;
        int v = 1;
        int s = this.mSize;
        while (i < s) {
            Object value = array[v];
            result += hashes[i] ^ (value == null ? 0 : value.hashCode());
            i++;
            v += 2;
        }
        return result;
    }

    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder buffer = new StringBuilder(this.mSize * 28);
        buffer.append('{');
        for (int i = 0; i < this.mSize; i++) {
            if (i > 0) {
                buffer.append(", ");
            }
            ArrayMap key = keyAt(i);
            String str = "(this Map)";
            if (key != this) {
                buffer.append(key);
            } else {
                buffer.append(str);
            }
            buffer.append('=');
            ArrayMap value = valueAt(i);
            if (value != this) {
                buffer.append(ArrayUtils.deepToString(value));
            } else {
                buffer.append(str);
            }
        }
        buffer.append('}');
        return buffer.toString();
    }

    private MapCollections<K, V> getCollection() {
        if (this.mCollections == null) {
            this.mCollections = new MapCollections<K, V>() {
                /* Access modifiers changed, original: protected */
                public int colGetSize() {
                    return ArrayMap.this.mSize;
                }

                /* Access modifiers changed, original: protected */
                public Object colGetEntry(int index, int offset) {
                    return ArrayMap.this.mArray[(index << 1) + offset];
                }

                /* Access modifiers changed, original: protected */
                public int colIndexOfKey(Object key) {
                    return ArrayMap.this.indexOfKey(key);
                }

                /* Access modifiers changed, original: protected */
                public int colIndexOfValue(Object value) {
                    return ArrayMap.this.indexOfValue(value);
                }

                /* Access modifiers changed, original: protected */
                public Map<K, V> colGetMap() {
                    return ArrayMap.this;
                }

                /* Access modifiers changed, original: protected */
                public void colPut(K key, V value) {
                    ArrayMap.this.put(key, value);
                }

                /* Access modifiers changed, original: protected */
                public V colSetValue(int index, V value) {
                    return ArrayMap.this.setValueAt(index, value);
                }

                /* Access modifiers changed, original: protected */
                public void colRemoveAt(int index) {
                    ArrayMap.this.removeAt(index);
                }

                /* Access modifiers changed, original: protected */
                public void colClear() {
                    ArrayMap.this.clear();
                }
            };
        }
        return this.mCollections;
    }

    public boolean containsAll(Collection<?> collection) {
        return MapCollections.containsAllHelper(this, collection);
    }

    public void putAll(Map<? extends K, ? extends V> map) {
        ensureCapacity(this.mSize + map.size());
        for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    public boolean removeAll(Collection<?> collection) {
        return MapCollections.removeAllHelper(this, collection);
    }

    public boolean retainAll(Collection<?> collection) {
        return MapCollections.retainAllHelper(this, collection);
    }

    public Set<Entry<K, V>> entrySet() {
        return getCollection().getEntrySet();
    }

    public Set<K> keySet() {
        return getCollection().getKeySet();
    }

    public Collection<V> values() {
        return getCollection().getValues();
    }
}
