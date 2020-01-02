package miui.maml.util;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import java.util.HashMap;
import miui.maml.RenderThread;
import miui.maml.RendererCore;
import miui.maml.RendererCore.OnReleaseListener;
import miui.maml.ResourceLoader;
import miui.maml.ScreenElementRoot;
import miui.maml.ScreenElementRootFactory;
import miui.maml.ScreenElementRootFactory.Parameter;

public class RendererCoreCache implements OnReleaseListener {
    private static final String LOG_TAG = "RendererCoreCache";
    public static final int TIME_DAY = 86400000;
    public static final int TIME_HOUR = 3600000;
    public static final int TIME_MIN = 60000;
    private HashMap<Object, RendererCoreInfo> mCaches;
    private Handler mHandler;

    public interface OnCreateRootCallback {
        void onCreateRoot(ScreenElementRoot screenElementRoot);
    }

    protected class CheckCacheRunnable implements Runnable {
        private Object mKey;

        public CheckCacheRunnable(Object key) {
            this.mKey = key;
        }

        public void run() {
            RendererCoreCache.this.checkCache(this.mKey);
        }
    }

    public static class RendererCoreInfo {
        public long accessTime = Long.MAX_VALUE;
        public long cacheTime;
        public CheckCacheRunnable checkCache;
        public RendererCore r;

        public RendererCoreInfo(RendererCore rc) {
            this.r = rc;
        }
    }

    public RendererCoreCache() {
        this.mCaches = new HashMap();
        this.mHandler = new Handler();
    }

    public RendererCoreCache(Handler h) {
        this.mCaches = new HashMap();
        this.mHandler = h;
    }

    public synchronized RendererCoreInfo get(Object key, long cacheTime) {
        RendererCoreInfo ri = (RendererCoreInfo) this.mCaches.get(key);
        if (ri == null) {
            return null;
        }
        ri.accessTime = Long.MAX_VALUE;
        ri.cacheTime = cacheTime;
        this.mHandler.removeCallbacks(ri.checkCache);
        return ri;
    }

    public synchronized RendererCoreInfo get(Object key, Context context, long cacheTime, String path, OnCreateRootCallback callback) {
        return get(key, context, cacheTime, null, path, callback);
    }

    public synchronized RendererCoreInfo get(Object key, Context context, long cacheTime, ResourceLoader loader, OnCreateRootCallback callback) {
        return get(key, context, cacheTime, loader, null, callback);
    }

    private RendererCoreInfo get(Object key, Context context, long cacheTime, ResourceLoader loader, String path, OnCreateRootCallback callback) {
        RendererCoreInfo ri = get(key, cacheTime);
        if (ri != null) {
            return ri;
        }
        ScreenElementRoot root;
        if (loader != null) {
            root = ScreenElementRootFactory.create(new Parameter(context, loader));
        } else {
            root = ScreenElementRootFactory.create(new Parameter(context, path));
        }
        if (root == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("fail to get RendererCoreInfo");
            stringBuilder.append(key);
            Log.e(LOG_TAG, stringBuilder.toString());
            return null;
        }
        if (callback != null) {
            callback.onCreateRoot(root);
        }
        root.setDefaultFramerate(0.0f);
        RendererCore r = null;
        if (root.load()) {
            r = new RendererCore(root, RenderThread.globalThread(true));
        }
        ri = new RendererCoreInfo(r);
        ri.accessTime = Long.MAX_VALUE;
        ri.cacheTime = cacheTime;
        if (r != null) {
            r.setOnReleaseListener(this);
            ri.checkCache = new CheckCacheRunnable(key);
        }
        this.mCaches.put(key, ri);
        return ri;
    }

    public synchronized void release(Object key) {
        String str = LOG_TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("release: ");
        stringBuilder.append(key);
        Log.d(str, stringBuilder.toString());
        RendererCoreInfo ri = (RendererCoreInfo) this.mCaches.get(key);
        if (ri != null) {
            ri.accessTime = System.currentTimeMillis();
            String str2;
            StringBuilder stringBuilder2;
            if (ri.cacheTime == 0) {
                this.mCaches.remove(key);
                str2 = LOG_TAG;
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("removed: ");
                stringBuilder2.append(key);
                Log.d(str2, stringBuilder2.toString());
            } else {
                str2 = LOG_TAG;
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("scheduled release: ");
                stringBuilder2.append(key);
                stringBuilder2.append(" after ");
                stringBuilder2.append(ri.cacheTime);
                Log.d(str2, stringBuilder2.toString());
                this.mHandler.removeCallbacks(ri.checkCache);
                this.mHandler.postDelayed(ri.checkCache, ri.cacheTime);
            }
        }
    }

    public synchronized void clear() {
        this.mCaches.clear();
    }

    /* JADX WARNING: Missing block: B:22:0x00a9, code skipped:
            return;
     */
    private synchronized void checkCache(java.lang.Object r8) {
        /*
        r7 = this;
        monitor-enter(r7);
        r0 = "RendererCoreCache";
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00aa }
        r1.<init>();	 Catch:{ all -> 0x00aa }
        r2 = "checkCache: ";
        r1.append(r2);	 Catch:{ all -> 0x00aa }
        r1.append(r8);	 Catch:{ all -> 0x00aa }
        r1 = r1.toString();	 Catch:{ all -> 0x00aa }
        android.util.Log.d(r0, r1);	 Catch:{ all -> 0x00aa }
        r0 = r7.mCaches;	 Catch:{ all -> 0x00aa }
        r0 = r0.get(r8);	 Catch:{ all -> 0x00aa }
        r0 = (miui.maml.util.RendererCoreCache.RendererCoreInfo) r0;	 Catch:{ all -> 0x00aa }
        if (r0 != 0) goto L_0x0039;
    L_0x0021:
        r1 = "RendererCoreCache";
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00aa }
        r2.<init>();	 Catch:{ all -> 0x00aa }
        r3 = "checkCache: the key does not exist, ";
        r2.append(r3);	 Catch:{ all -> 0x00aa }
        r2.append(r8);	 Catch:{ all -> 0x00aa }
        r2 = r2.toString();	 Catch:{ all -> 0x00aa }
        android.util.Log.d(r1, r2);	 Catch:{ all -> 0x00aa }
        monitor-exit(r7);
        return;
    L_0x0039:
        r1 = r0.accessTime;	 Catch:{ all -> 0x00aa }
        r3 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1));
        if (r1 != 0) goto L_0x0046;
    L_0x0044:
        monitor-exit(r7);
        return;
    L_0x0046:
        r1 = java.lang.System.currentTimeMillis();	 Catch:{ all -> 0x00aa }
        r3 = r0.accessTime;	 Catch:{ all -> 0x00aa }
        r1 = r1 - r3;
        r3 = r0.cacheTime;	 Catch:{ all -> 0x00aa }
        r3 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1));
        if (r3 < 0) goto L_0x006f;
    L_0x0053:
        r3 = r7.mCaches;	 Catch:{ all -> 0x00aa }
        r3.remove(r8);	 Catch:{ all -> 0x00aa }
        r3 = "RendererCoreCache";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00aa }
        r4.<init>();	 Catch:{ all -> 0x00aa }
        r5 = "checkCache removed: ";
        r4.append(r5);	 Catch:{ all -> 0x00aa }
        r4.append(r8);	 Catch:{ all -> 0x00aa }
        r4 = r4.toString();	 Catch:{ all -> 0x00aa }
        android.util.Log.d(r3, r4);	 Catch:{ all -> 0x00aa }
        goto L_0x00a8;
    L_0x006f:
        r3 = 0;
        r3 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1));
        if (r3 >= 0) goto L_0x007d;
    L_0x0075:
        r3 = java.lang.System.currentTimeMillis();	 Catch:{ all -> 0x00aa }
        r0.accessTime = r3;	 Catch:{ all -> 0x00aa }
        r1 = 0;
    L_0x007d:
        r3 = r7.mHandler;	 Catch:{ all -> 0x00aa }
        r4 = r0.checkCache;	 Catch:{ all -> 0x00aa }
        r5 = r0.cacheTime;	 Catch:{ all -> 0x00aa }
        r5 = r5 - r1;
        r3.postDelayed(r4, r5);	 Catch:{ all -> 0x00aa }
        r3 = "RendererCoreCache";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00aa }
        r4.<init>();	 Catch:{ all -> 0x00aa }
        r5 = "checkCache resheduled: ";
        r4.append(r5);	 Catch:{ all -> 0x00aa }
        r4.append(r8);	 Catch:{ all -> 0x00aa }
        r5 = " after ";
        r4.append(r5);	 Catch:{ all -> 0x00aa }
        r5 = r0.cacheTime;	 Catch:{ all -> 0x00aa }
        r5 = r5 - r1;
        r4.append(r5);	 Catch:{ all -> 0x00aa }
        r4 = r4.toString();	 Catch:{ all -> 0x00aa }
        android.util.Log.d(r3, r4);	 Catch:{ all -> 0x00aa }
    L_0x00a8:
        monitor-exit(r7);
        return;
    L_0x00aa:
        r8 = move-exception;
        monitor-exit(r7);
        throw r8;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.maml.util.RendererCoreCache.checkCache(java.lang.Object):void");
    }

    /* JADX WARNING: Missing block: B:12:0x0045, code skipped:
            return r2;
     */
    public synchronized boolean OnRendererCoreReleased(miui.maml.RendererCore r9) {
        /*
        r8 = this;
        monitor-enter(r8);
        r0 = "RendererCoreCache";
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0049 }
        r1.<init>();	 Catch:{ all -> 0x0049 }
        r2 = "OnRendererCoreReleased: ";
        r1.append(r2);	 Catch:{ all -> 0x0049 }
        r1.append(r9);	 Catch:{ all -> 0x0049 }
        r1 = r1.toString();	 Catch:{ all -> 0x0049 }
        android.util.Log.d(r0, r1);	 Catch:{ all -> 0x0049 }
        r0 = r8.mCaches;	 Catch:{ all -> 0x0049 }
        r0 = r0.keySet();	 Catch:{ all -> 0x0049 }
        r0 = r0.iterator();	 Catch:{ all -> 0x0049 }
    L_0x0021:
        r1 = r0.hasNext();	 Catch:{ all -> 0x0049 }
        r2 = 0;
        if (r1 == 0) goto L_0x0047;
    L_0x0028:
        r1 = r0.next();	 Catch:{ all -> 0x0049 }
        r3 = r8.mCaches;	 Catch:{ all -> 0x0049 }
        r3 = r3.get(r1);	 Catch:{ all -> 0x0049 }
        r3 = (miui.maml.util.RendererCoreCache.RendererCoreInfo) r3;	 Catch:{ all -> 0x0049 }
        r4 = r3.r;	 Catch:{ all -> 0x0049 }
        if (r4 != r9) goto L_0x0046;
    L_0x0038:
        r8.release(r1);	 Catch:{ all -> 0x0049 }
        r4 = r3.cacheTime;	 Catch:{ all -> 0x0049 }
        r6 = 0;
        r0 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r0 != 0) goto L_0x0044;
    L_0x0043:
        r2 = 1;
    L_0x0044:
        monitor-exit(r8);
        return r2;
    L_0x0046:
        goto L_0x0021;
    L_0x0047:
        monitor-exit(r8);
        return r2;
    L_0x0049:
        r9 = move-exception;
        monitor-exit(r8);
        throw r9;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.maml.util.RendererCoreCache.OnRendererCoreReleased(miui.maml.RendererCore):boolean");
    }
}
