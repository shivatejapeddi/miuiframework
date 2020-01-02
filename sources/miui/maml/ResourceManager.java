package miui.maml;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.AsyncTask;
import android.os.MemoryFile;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import org.w3c.dom.Element;

public class ResourceManager {
    private static final int DEF_CACHE_SIZE = 268435456;
    private static final int DENSITY_HIGH_R = 240;
    private static final int DENSITY_XHIGH_R = 360;
    private static final int DENSITY_XXHIGH_R = 540;
    private static final int DENSITY_XXXHIGH = 640;
    private static final int DENSITY_XXXHIGH_R = 720;
    private static final String LOG_TAG = "ResourceManager";
    private static final int RESOURCE_FALLBACK_DENSITY = 480;
    private static final String RESOURCE_FALLBACK_EXTRA_FOLDER = "den480/";
    protected final LruCache<String, BitmapInfo> mBitmapsCache;
    private int mDefaultResourceDensity;
    private int mExtraResourceDensity;
    private String mExtraResourceFolder;
    private final HashSet<String> mLoadingBitmaps = new HashSet();
    private final ResourceLoader mResourceLoader;
    private int mTargetDensity;
    protected final HashMap<String, WeakReference<BitmapInfo>> mWeakRefBitmapsCache;

    public interface AsyncLoadListener {
        void onLoadComplete(String str, BitmapInfo bitmapInfo);
    }

    public static class BitmapInfo {
        public final Bitmap mBitmap;
        public String mKey;
        public long mLastVisitTime;
        public boolean mLoading;
        public final NinePatch mNinePatch;
        public final Rect mPadding;
        public HashMap<String, WeakReference<BitmapInfo>> mWeakRefCache;

        public BitmapInfo() {
            this.mBitmap = null;
            this.mPadding = null;
            this.mNinePatch = null;
        }

        public BitmapInfo(Bitmap bm, Rect padding) {
            this.mBitmap = bm;
            this.mPadding = padding;
            if (bm == null || bm.getNinePatchChunk() == null) {
                this.mNinePatch = null;
            } else {
                this.mNinePatch = new NinePatch(bm, bm.getNinePatchChunk(), null);
            }
            this.mLastVisitTime = System.currentTimeMillis();
        }

        /* Access modifiers changed, original: protected */
        public void finalize() throws Throwable {
            synchronized (this.mWeakRefCache) {
                if (this.mWeakRefCache != null) {
                    this.mWeakRefCache.remove(this.mKey);
                    this.mWeakRefCache = null;
                }
            }
            super.finalize();
        }
    }

    private class LoadBitmapAsyncTask extends AsyncTask<String, Object, BitmapInfo> {
        private AsyncLoadListener mLoadListener;
        private String mSrc;

        public LoadBitmapAsyncTask(AsyncLoadListener l) {
            this.mLoadListener = l;
        }

        /* Access modifiers changed, original: protected|varargs */
        public BitmapInfo doInBackground(String... params) {
            this.mSrc = params[0];
            String str = this.mSrc;
            if (str != null) {
                return ResourceManager.this.loadBitmap(str);
            }
            return null;
        }

        /* Access modifiers changed, original: protected */
        public void onPostExecute(BitmapInfo result) {
            synchronized (ResourceManager.this.mLoadingBitmaps) {
                this.mLoadListener.onLoadComplete(this.mSrc, result);
                ResourceManager.this.mLoadingBitmaps.remove(this.mSrc);
            }
        }
    }

    public ResourceManager(ResourceLoader resourceLoader) {
        this.mResourceLoader = resourceLoader;
        this.mBitmapsCache = new LruCache<String, BitmapInfo>(268435456) {
            /* Access modifiers changed, original: protected */
            public int sizeOf(String key, BitmapInfo bi) {
                if (bi == null || bi.mBitmap == null) {
                    return 0;
                }
                return bi.mBitmap.getAllocationByteCount();
            }
        };
        this.mWeakRefBitmapsCache = new HashMap();
    }

    public MemoryFile getFile(String src) {
        return this.mResourceLoader.getFile(src);
    }

    public Bitmap getBitmap(String src) {
        BitmapInfo info = getBitmapInfo(src);
        return info != null ? info.mBitmap : null;
    }

    public Drawable getDrawable(Resources res, String src) {
        BitmapInfo info = getBitmapInfo(src);
        if (info == null || info.mBitmap == null) {
            return null;
        }
        Bitmap bm = info.mBitmap;
        if (info.mNinePatch != null) {
            NinePatchDrawable ninePatchDrawable = new NinePatchDrawable(res, bm, bm.getNinePatchChunk(), info.mPadding, src);
            ninePatchDrawable.setTargetDensity(this.mTargetDensity);
            return ninePatchDrawable;
        }
        BitmapDrawable d = new BitmapDrawable(res, bm);
        d.setTargetDensity(this.mTargetDensity);
        return d;
    }

    public NinePatch getNinePatch(String src) {
        BitmapInfo info = getBitmapInfo(src);
        return info != null ? info.mNinePatch : null;
    }

    public Element getManifestRoot() {
        return this.mResourceLoader.getManifestRoot();
    }

    public Element getConfigRoot() {
        return this.mResourceLoader.getConfigRoot();
    }

    public void clear() {
        synchronized (this.mBitmapsCache) {
            this.mBitmapsCache.evictAll();
        }
    }

    public void clear(String bitmapName) {
        synchronized (this.mBitmapsCache) {
            this.mBitmapsCache.remove(bitmapName);
        }
    }

    public BitmapInfo getBitmapInfo(String src) {
        if (TextUtils.isEmpty(src)) {
            return null;
        }
        BitmapInfo info = getCache(src);
        if (info != null) {
            return info;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("load image ");
        stringBuilder.append(src);
        Log.i(LOG_TAG, stringBuilder.toString());
        return loadBitmap(src);
    }

    /* JADX WARNING: Missing block: B:18:0x004d, code skipped:
            r0 = new miui.maml.ResourceManager.BitmapInfo();
            r0.mLoading = true;
     */
    /* JADX WARNING: Missing block: B:19:0x0055, code skipped:
            return r0;
     */
    public miui.maml.ResourceManager.BitmapInfo getBitmapInfoAsync(java.lang.String r7, miui.maml.ResourceManager.AsyncLoadListener r8) {
        /*
        r6 = this;
        r0 = android.text.TextUtils.isEmpty(r7);
        if (r0 == 0) goto L_0x0008;
    L_0x0006:
        r0 = 0;
        return r0;
    L_0x0008:
        r0 = r6.getCache(r7);
        if (r0 == 0) goto L_0x000f;
    L_0x000e:
        return r0;
    L_0x000f:
        r1 = r6.mLoadingBitmaps;
        monitor-enter(r1);
        r2 = r6.mLoadingBitmaps;	 Catch:{ all -> 0x0056 }
        r2 = r2.contains(r7);	 Catch:{ all -> 0x0056 }
        r3 = 1;
        if (r2 != 0) goto L_0x004c;
    L_0x001b:
        r2 = r6.getCache(r7);	 Catch:{ all -> 0x0056 }
        r0 = r2;
        if (r0 == 0) goto L_0x0024;
    L_0x0022:
        monitor-exit(r1);	 Catch:{ all -> 0x0056 }
        return r0;
    L_0x0024:
        r2 = r6.mLoadingBitmaps;	 Catch:{ all -> 0x0056 }
        r2.add(r7);	 Catch:{ all -> 0x0056 }
        r2 = "ResourceManager";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0056 }
        r4.<init>();	 Catch:{ all -> 0x0056 }
        r5 = "load image async: ";
        r4.append(r5);	 Catch:{ all -> 0x0056 }
        r4.append(r7);	 Catch:{ all -> 0x0056 }
        r4 = r4.toString();	 Catch:{ all -> 0x0056 }
        android.util.Log.i(r2, r4);	 Catch:{ all -> 0x0056 }
        r2 = new miui.maml.ResourceManager$LoadBitmapAsyncTask;	 Catch:{ all -> 0x0056 }
        r2.<init>(r8);	 Catch:{ all -> 0x0056 }
        r4 = new java.lang.String[r3];	 Catch:{ all -> 0x0056 }
        r5 = 0;
        r4[r5] = r7;	 Catch:{ all -> 0x0056 }
        r2.execute(r4);	 Catch:{ all -> 0x0056 }
    L_0x004c:
        monitor-exit(r1);	 Catch:{ all -> 0x0056 }
        r1 = new miui.maml.ResourceManager$BitmapInfo;
        r1.<init>();
        r0 = r1;
        r0.mLoading = r3;
        return r0;
    L_0x0056:
        r2 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0056 }
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.maml.ResourceManager.getBitmapInfoAsync(java.lang.String, miui.maml.ResourceManager$AsyncLoadListener):miui.maml.ResourceManager$BitmapInfo");
    }

    private BitmapInfo loadBitmap(String src) {
        StringBuilder stringBuilder;
        String path;
        BitmapInfo info = null;
        boolean useDefaultResource = true;
        Options opts = new Options();
        opts.inScaled = true;
        opts.inTargetDensity = this.mTargetDensity;
        if (this.mExtraResourceFolder != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("try to load resource from extra resource: ");
            stringBuilder.append(this.mExtraResourceFolder);
            stringBuilder.append(" of ");
            stringBuilder.append(src);
            Log.i(LOG_TAG, stringBuilder.toString());
            opts.inDensity = this.mExtraResourceDensity;
            if (TextUtils.isEmpty(this.mExtraResourceFolder)) {
                path = src;
            } else {
                path = new StringBuilder();
                path.append(this.mExtraResourceFolder);
                path.append("/");
                path.append(src);
                path = path.toString();
            }
            info = this.mResourceLoader.getBitmapInfo(path, opts);
            if (info != null) {
                useDefaultResource = false;
            }
        }
        if (info == null) {
            opts.inDensity = this.mDefaultResourceDensity;
            info = this.mResourceLoader.getBitmapInfo(src, opts);
        }
        if (info == null) {
            opts.inDensity = 480;
            path = new StringBuilder();
            path.append(RESOURCE_FALLBACK_EXTRA_FOLDER);
            path.append(src);
            info = this.mResourceLoader.getBitmapInfo(path.toString(), opts);
        }
        if (info != null) {
            if (!useDefaultResource) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("load image from extra resource: ");
                stringBuilder.append(this.mExtraResourceFolder);
                stringBuilder.append(" of ");
                stringBuilder.append(src);
                Log.i(LOG_TAG, stringBuilder.toString());
            }
            info.mKey = src;
            info.mWeakRefCache = this.mWeakRefBitmapsCache;
            info.mBitmap.setDensity(this.mTargetDensity);
            info.mLastVisitTime = System.currentTimeMillis();
            synchronized (this.mBitmapsCache) {
                this.mBitmapsCache.put(src, info);
            }
            synchronized (this.mWeakRefBitmapsCache) {
                this.mWeakRefBitmapsCache.put(src, new WeakReference(info));
            }
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("fail to load image: ");
            stringBuilder.append(src);
            Log.e(LOG_TAG, stringBuilder.toString());
        }
        return info;
    }

    private BitmapInfo getCache(String src) {
        BitmapInfo info;
        WeakReference<BitmapInfo> bi;
        synchronized (this.mBitmapsCache) {
            info = (BitmapInfo) this.mBitmapsCache.get(src);
        }
        synchronized (this.mWeakRefBitmapsCache) {
            bi = (WeakReference) this.mWeakRefBitmapsCache.get(src);
        }
        if (info != null) {
            info.mLastVisitTime = System.currentTimeMillis();
            if (bi != null && bi.get() != null) {
                return info;
            }
            synchronized (this.mWeakRefBitmapsCache) {
                this.mWeakRefBitmapsCache.put(src, new WeakReference(info));
            }
            return info;
        } else if (bi == null) {
            return info;
        } else {
            BitmapInfo info2 = (BitmapInfo) bi.get();
            if (info2 != null) {
                info2.mLastVisitTime = System.currentTimeMillis();
                synchronized (this.mBitmapsCache) {
                    this.mBitmapsCache.put(src, info2);
                }
            } else {
                synchronized (this.mWeakRefBitmapsCache) {
                    this.mWeakRefBitmapsCache.remove(src);
                }
            }
            return info2;
        }
    }

    public void setDefaultResourceDensity(int density) {
        this.mDefaultResourceDensity = density;
    }

    public void setTargetDensity(int density) {
        this.mTargetDensity = density;
    }

    public void setExtraResource(String resDir, int den) {
        this.mExtraResourceFolder = resDir;
        this.mExtraResourceDensity = den;
    }

    public void setExtraResource(String resDir) {
        this.mExtraResourceFolder = resDir;
    }

    public void setExtraResourceDensity(int den) {
        this.mExtraResourceDensity = den;
    }

    public static int translateDensity(int density) {
        if (density > 240 && density <= 320) {
            return ((int) (((double) (density - 240)) * 1.5d)) + 240;
        }
        if (density > 320 && density <= 480) {
            return ((int) (((double) (density - 320)) * 1.125d)) + 360;
        }
        if (density <= 480 || density > 640) {
            return density;
        }
        return ((int) (((double) (density - 480)) * 1.125d)) + 540;
    }

    public static int retranslateDensity(int density) {
        if (density > 240 && density <= 360) {
            return ((int) (((double) (density - 240)) * 0.6666666666666666d)) + 240;
        }
        if (density > 360 && density <= 540) {
            return ((int) (((double) (density - 360)) * 0.8888888888888888d)) + 320;
        }
        if (density <= 540 || density > 720) {
            return density;
        }
        return ((int) (((double) (density - 540)) * 0.8888888888888888d)) + 480;
    }

    public void pause() {
    }

    public void resume() {
    }

    public void init() {
        this.mResourceLoader.init();
    }

    public void finish(boolean keepResource) {
        if (!keepResource) {
            synchronized (this.mBitmapsCache) {
                this.mBitmapsCache.evictAll();
            }
            synchronized (this.mWeakRefBitmapsCache) {
                this.mWeakRefBitmapsCache.clear();
            }
        }
        synchronized (this.mLoadingBitmaps) {
            this.mLoadingBitmaps.clear();
        }
        this.mResourceLoader.finish();
    }

    public final boolean resourceExists(String path) {
        return this.mResourceLoader.resourceExists(path);
    }

    public final InputStream getInputStream(String path) {
        return this.mResourceLoader.getInputStream(path);
    }

    public final InputStream getInputStream(String path, long[] size) {
        return this.mResourceLoader.getInputStream(path, size);
    }

    public String getPathForLanguage(String file) {
        return this.mResourceLoader.getPathForLanguage(file);
    }

    public void setCacheSize(int size) {
        synchronized (this.mBitmapsCache) {
            this.mBitmapsCache.resize(size);
        }
    }

    public void setLocal(Locale locale) {
        if (!(locale == null || locale.equals(this.mResourceLoader.getLocale()))) {
            this.mResourceLoader.setLocal(locale);
            finish(false);
        }
    }
}
