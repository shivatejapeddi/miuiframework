package android.content.res;

import android.annotation.UnsupportedAppUsage;
import android.content.res.Resources.Theme;
import android.util.ArrayMap;
import android.util.LongSparseArray;
import java.lang.ref.WeakReference;

abstract class ThemedResourceCache<T> {
    private LongSparseArray<WeakReference<T>> mNullThemedEntries;
    @UnsupportedAppUsage
    private ArrayMap<ThemeKey, LongSparseArray<WeakReference<T>>> mThemedEntries;
    private LongSparseArray<WeakReference<T>> mUnthemedEntries;

    public abstract boolean shouldInvalidateEntry(T t, int i);

    ThemedResourceCache() {
    }

    public void put(long key, Theme theme, T entry) {
        put(key, theme, entry, true);
    }

    public void put(long key, Theme theme, T entry, boolean usesTheme) {
        if (entry != null) {
            synchronized (this) {
                LongSparseArray<WeakReference<T>> entries;
                if (usesTheme) {
                    entries = getThemedLocked(theme, true);
                } else {
                    entries = getUnthemedLocked(true);
                }
                if (entries != null) {
                    entries.put(key, new WeakReference(entry));
                }
            }
        }
    }

    /* JADX WARNING: Missing block: B:19:0x002c, code skipped:
            return null;
     */
    public T get(long r5, android.content.res.Resources.Theme r7) {
        /*
        r4 = this;
        monitor-enter(r4);
        r0 = 0;
        r1 = r4.getThemedLocked(r7, r0);	 Catch:{ all -> 0x002d }
        if (r1 == 0) goto L_0x0016;
    L_0x0008:
        r2 = r1.get(r5);	 Catch:{ all -> 0x002d }
        r2 = (java.lang.ref.WeakReference) r2;	 Catch:{ all -> 0x002d }
        if (r2 == 0) goto L_0x0016;
    L_0x0010:
        r0 = r2.get();	 Catch:{ all -> 0x002d }
        monitor-exit(r4);	 Catch:{ all -> 0x002d }
        return r0;
    L_0x0016:
        r0 = r4.getUnthemedLocked(r0);	 Catch:{ all -> 0x002d }
        if (r0 == 0) goto L_0x002a;
    L_0x001c:
        r2 = r0.get(r5);	 Catch:{ all -> 0x002d }
        r2 = (java.lang.ref.WeakReference) r2;	 Catch:{ all -> 0x002d }
        if (r2 == 0) goto L_0x002a;
    L_0x0024:
        r3 = r2.get();	 Catch:{ all -> 0x002d }
        monitor-exit(r4);	 Catch:{ all -> 0x002d }
        return r3;
    L_0x002a:
        monitor-exit(r4);	 Catch:{ all -> 0x002d }
        r0 = 0;
        return r0;
    L_0x002d:
        r0 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x002d }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.res.ThemedResourceCache.get(long, android.content.res.Resources$Theme):java.lang.Object");
    }

    @UnsupportedAppUsage
    public void onConfigurationChange(int configChanges) {
        prune(configChanges);
    }

    private LongSparseArray<WeakReference<T>> getThemedLocked(Theme t, boolean create) {
        if (t == null) {
            if (this.mNullThemedEntries == null && create) {
                this.mNullThemedEntries = new LongSparseArray(1);
            }
            return this.mNullThemedEntries;
        }
        if (this.mThemedEntries == null) {
            if (!create) {
                return null;
            }
            this.mThemedEntries = new ArrayMap(1);
        }
        ThemeKey key = t.getKey();
        LongSparseArray<WeakReference<T>> cache = (LongSparseArray) this.mThemedEntries.get(key);
        if (cache == null && create) {
            cache = new LongSparseArray(1);
            this.mThemedEntries.put(key.clone(), cache);
        }
        return cache;
    }

    private LongSparseArray<WeakReference<T>> getUnthemedLocked(boolean create) {
        if (this.mUnthemedEntries == null && create) {
            this.mUnthemedEntries = new LongSparseArray(1);
        }
        return this.mUnthemedEntries;
    }

    private boolean prune(int configChanges) {
        boolean z;
        synchronized (this) {
            z = true;
            if (this.mThemedEntries != null) {
                for (int i = this.mThemedEntries.size() - 1; i >= 0; i--) {
                    if (pruneEntriesLocked((LongSparseArray) this.mThemedEntries.valueAt(i), configChanges)) {
                        this.mThemedEntries.removeAt(i);
                    }
                }
            }
            pruneEntriesLocked(this.mNullThemedEntries, configChanges);
            pruneEntriesLocked(this.mUnthemedEntries, configChanges);
            if (this.mThemedEntries != null || this.mNullThemedEntries != null || this.mUnthemedEntries != null) {
                z = false;
            }
        }
        return z;
    }

    private boolean pruneEntriesLocked(LongSparseArray<WeakReference<T>> entries, int configChanges) {
        boolean z = true;
        if (entries == null) {
            return true;
        }
        for (int i = entries.size() - 1; i >= 0; i--) {
            WeakReference<T> ref = (WeakReference) entries.valueAt(i);
            if (ref == null || pruneEntryLocked(ref.get(), configChanges)) {
                entries.removeAt(i);
            }
        }
        if (entries.size() != 0) {
            z = false;
        }
        return z;
    }

    private boolean pruneEntryLocked(T entry, int configChanges) {
        return entry == null || (configChanges != 0 && shouldInvalidateEntry(entry, configChanges));
    }
}
