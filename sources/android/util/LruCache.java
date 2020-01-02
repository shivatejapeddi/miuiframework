package android.util;

import android.annotation.UnsupportedAppUsage;
import java.util.LinkedHashMap;
import java.util.Map;

public class LruCache<K, V> {
    private int createCount;
    private int evictionCount;
    private int hitCount;
    @UnsupportedAppUsage
    private final LinkedHashMap<K, V> map;
    private int maxSize;
    private int missCount;
    private int putCount;
    private int size;

    public LruCache(int maxSize) {
        if (maxSize > 0) {
            this.maxSize = maxSize;
            this.map = new LinkedHashMap(0, 0.75f, true);
            return;
        }
        throw new IllegalArgumentException("maxSize <= 0");
    }

    public void resize(int maxSize) {
        if (maxSize > 0) {
            synchronized (this) {
                this.maxSize = maxSize;
            }
            trimToSize(maxSize);
            return;
        }
        throw new IllegalArgumentException("maxSize <= 0");
    }

    /* JADX WARNING: Missing block: B:10:0x001a, code skipped:
            r1 = create(r5);
     */
    /* JADX WARNING: Missing block: B:11:0x001e, code skipped:
            if (r1 != null) goto L_0x0022;
     */
    /* JADX WARNING: Missing block: B:13:0x0021, code skipped:
            return null;
     */
    /* JADX WARNING: Missing block: B:14:0x0022, code skipped:
            monitor-enter(r4);
     */
    /* JADX WARNING: Missing block: B:16:?, code skipped:
            r4.createCount++;
            r0 = r4.map.put(r5, r1);
     */
    /* JADX WARNING: Missing block: B:17:0x0030, code skipped:
            if (r0 == null) goto L_0x0038;
     */
    /* JADX WARNING: Missing block: B:18:0x0032, code skipped:
            r4.map.put(r5, r0);
     */
    /* JADX WARNING: Missing block: B:19:0x0038, code skipped:
            r4.size += safeSizeOf(r5, r1);
     */
    /* JADX WARNING: Missing block: B:20:0x0041, code skipped:
            monitor-exit(r4);
     */
    /* JADX WARNING: Missing block: B:21:0x0042, code skipped:
            if (r0 == null) goto L_0x0049;
     */
    /* JADX WARNING: Missing block: B:22:0x0044, code skipped:
            entryRemoved(false, r5, r1, r0);
     */
    /* JADX WARNING: Missing block: B:23:0x0048, code skipped:
            return r0;
     */
    /* JADX WARNING: Missing block: B:24:0x0049, code skipped:
            trimToSize(r4.maxSize);
     */
    /* JADX WARNING: Missing block: B:25:0x004e, code skipped:
            return r1;
     */
    public final V get(K r5) {
        /*
        r4 = this;
        if (r5 == 0) goto L_0x0055;
    L_0x0002:
        monitor-enter(r4);
        r0 = r4.map;	 Catch:{ all -> 0x0052 }
        r0 = r0.get(r5);	 Catch:{ all -> 0x0052 }
        if (r0 == 0) goto L_0x0013;
    L_0x000b:
        r1 = r4.hitCount;	 Catch:{ all -> 0x0052 }
        r1 = r1 + 1;
        r4.hitCount = r1;	 Catch:{ all -> 0x0052 }
        monitor-exit(r4);	 Catch:{ all -> 0x0052 }
        return r0;
    L_0x0013:
        r1 = r4.missCount;	 Catch:{ all -> 0x0052 }
        r1 = r1 + 1;
        r4.missCount = r1;	 Catch:{ all -> 0x0052 }
        monitor-exit(r4);	 Catch:{ all -> 0x0052 }
        r1 = r4.create(r5);
        if (r1 != 0) goto L_0x0022;
    L_0x0020:
        r2 = 0;
        return r2;
    L_0x0022:
        monitor-enter(r4);
        r2 = r4.createCount;	 Catch:{ all -> 0x004f }
        r2 = r2 + 1;
        r4.createCount = r2;	 Catch:{ all -> 0x004f }
        r2 = r4.map;	 Catch:{ all -> 0x004f }
        r2 = r2.put(r5, r1);	 Catch:{ all -> 0x004f }
        r0 = r2;
        if (r0 == 0) goto L_0x0038;
    L_0x0032:
        r2 = r4.map;	 Catch:{ all -> 0x004f }
        r2.put(r5, r0);	 Catch:{ all -> 0x004f }
        goto L_0x0041;
    L_0x0038:
        r2 = r4.size;	 Catch:{ all -> 0x004f }
        r3 = r4.safeSizeOf(r5, r1);	 Catch:{ all -> 0x004f }
        r2 = r2 + r3;
        r4.size = r2;	 Catch:{ all -> 0x004f }
    L_0x0041:
        monitor-exit(r4);	 Catch:{ all -> 0x004f }
        if (r0 == 0) goto L_0x0049;
    L_0x0044:
        r2 = 0;
        r4.entryRemoved(r2, r5, r1, r0);
        return r0;
    L_0x0049:
        r2 = r4.maxSize;
        r4.trimToSize(r2);
        return r1;
    L_0x004f:
        r2 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x004f }
        throw r2;
    L_0x0052:
        r0 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0052 }
        throw r0;
    L_0x0055:
        r0 = new java.lang.NullPointerException;
        r1 = "key == null";
        r0.<init>(r1);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.util.LruCache.get(java.lang.Object):java.lang.Object");
    }

    public final V put(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException("key == null || value == null");
        }
        V previous;
        synchronized (this) {
            this.putCount++;
            this.size += safeSizeOf(key, value);
            previous = this.map.put(key, value);
            if (previous != null) {
                this.size -= safeSizeOf(key, previous);
            }
        }
        if (previous != null) {
            entryRemoved(false, key, previous, value);
        }
        trimToSize(this.maxSize);
        return previous;
    }

    /* JADX WARNING: Missing block: B:19:?, code skipped:
            r1 = new java.lang.StringBuilder();
            r1.append(getClass().getName());
            r1.append(".sizeOf() is reporting inconsistent results!");
     */
    /* JADX WARNING: Missing block: B:20:0x0061, code skipped:
            throw new java.lang.IllegalStateException(r1.toString());
     */
    public void trimToSize(int r6) {
        /*
        r5 = this;
    L_0x0000:
        monitor-enter(r5);
        r0 = r5.size;	 Catch:{ all -> 0x0062 }
        if (r0 < 0) goto L_0x0043;
    L_0x0005:
        r0 = r5.map;	 Catch:{ all -> 0x0062 }
        r0 = r0.isEmpty();	 Catch:{ all -> 0x0062 }
        if (r0 == 0) goto L_0x0011;
    L_0x000d:
        r0 = r5.size;	 Catch:{ all -> 0x0062 }
        if (r0 != 0) goto L_0x0043;
    L_0x0011:
        r0 = r5.size;	 Catch:{ all -> 0x0062 }
        if (r0 > r6) goto L_0x0017;
    L_0x0015:
        monitor-exit(r5);	 Catch:{ all -> 0x0062 }
        goto L_0x0020;
    L_0x0017:
        r0 = r5.map;	 Catch:{ all -> 0x0062 }
        r0 = r0.eldest();	 Catch:{ all -> 0x0062 }
        if (r0 != 0) goto L_0x0021;
    L_0x001f:
        monitor-exit(r5);	 Catch:{ all -> 0x0062 }
    L_0x0020:
        return;
    L_0x0021:
        r1 = r0.getKey();	 Catch:{ all -> 0x0062 }
        r2 = r0.getValue();	 Catch:{ all -> 0x0062 }
        r3 = r5.map;	 Catch:{ all -> 0x0062 }
        r3.remove(r1);	 Catch:{ all -> 0x0062 }
        r3 = r5.size;	 Catch:{ all -> 0x0062 }
        r4 = r5.safeSizeOf(r1, r2);	 Catch:{ all -> 0x0062 }
        r3 = r3 - r4;
        r5.size = r3;	 Catch:{ all -> 0x0062 }
        r3 = r5.evictionCount;	 Catch:{ all -> 0x0062 }
        r4 = 1;
        r3 = r3 + r4;
        r5.evictionCount = r3;	 Catch:{ all -> 0x0062 }
        monitor-exit(r5);	 Catch:{ all -> 0x0062 }
        r0 = 0;
        r5.entryRemoved(r4, r1, r2, r0);
        goto L_0x0000;
    L_0x0043:
        r0 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x0062 }
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0062 }
        r1.<init>();	 Catch:{ all -> 0x0062 }
        r2 = r5.getClass();	 Catch:{ all -> 0x0062 }
        r2 = r2.getName();	 Catch:{ all -> 0x0062 }
        r1.append(r2);	 Catch:{ all -> 0x0062 }
        r2 = ".sizeOf() is reporting inconsistent results!";
        r1.append(r2);	 Catch:{ all -> 0x0062 }
        r1 = r1.toString();	 Catch:{ all -> 0x0062 }
        r0.<init>(r1);	 Catch:{ all -> 0x0062 }
        throw r0;	 Catch:{ all -> 0x0062 }
    L_0x0062:
        r0 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x0062 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.util.LruCache.trimToSize(int):void");
    }

    public final V remove(K key) {
        if (key != null) {
            V previous;
            synchronized (this) {
                previous = this.map.remove(key);
                if (previous != null) {
                    this.size -= safeSizeOf(key, previous);
                }
            }
            if (previous != null) {
                entryRemoved(false, key, previous, null);
            }
            return previous;
        }
        throw new NullPointerException("key == null");
    }

    /* Access modifiers changed, original: protected */
    public void entryRemoved(boolean evicted, K k, V v, V v2) {
    }

    /* Access modifiers changed, original: protected */
    public V create(K k) {
        return null;
    }

    private int safeSizeOf(K key, V value) {
        int result = sizeOf(key, value);
        if (result >= 0) {
            return result;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Negative size: ");
        stringBuilder.append(key);
        stringBuilder.append("=");
        stringBuilder.append(value);
        throw new IllegalStateException(stringBuilder.toString());
    }

    /* Access modifiers changed, original: protected */
    public int sizeOf(K k, V v) {
        return 1;
    }

    public final void evictAll() {
        trimToSize(-1);
    }

    public final synchronized int size() {
        return this.size;
    }

    public final synchronized int maxSize() {
        return this.maxSize;
    }

    public final synchronized int hitCount() {
        return this.hitCount;
    }

    public final synchronized int missCount() {
        return this.missCount;
    }

    public final synchronized int createCount() {
        return this.createCount;
    }

    public final synchronized int putCount() {
        return this.putCount;
    }

    public final synchronized int evictionCount() {
        return this.evictionCount;
    }

    public final synchronized Map<K, V> snapshot() {
        return new LinkedHashMap(this.map);
    }

    public final synchronized String toString() {
        int hitPercent;
        int accesses = this.hitCount + this.missCount;
        hitPercent = accesses != 0 ? (this.hitCount * 100) / accesses : 0;
        return String.format("LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", new Object[]{Integer.valueOf(this.maxSize), Integer.valueOf(this.hitCount), Integer.valueOf(this.missCount), Integer.valueOf(hitPercent)});
    }
}
