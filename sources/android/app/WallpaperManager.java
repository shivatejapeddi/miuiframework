package android.app;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.app.IWallpaperManagerCallback.Stub;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.DeadSystemException;
import android.os.FileUtils;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.ParcelFileDescriptor.AutoCloseOutputStream;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.WindowManagerGlobal;
import com.android.internal.R;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import libcore.io.IoUtils;

public class WallpaperManager {
    public static final String ACTION_CHANGE_LIVE_WALLPAPER = "android.service.wallpaper.CHANGE_LIVE_WALLPAPER";
    public static final String ACTION_CROP_AND_SET_WALLPAPER = "android.service.wallpaper.CROP_AND_SET_WALLPAPER";
    public static final String ACTION_LIVE_WALLPAPER_CHOOSER = "android.service.wallpaper.LIVE_WALLPAPER_CHOOSER";
    public static final String COMMAND_DROP = "android.home.drop";
    public static final String COMMAND_SECONDARY_TAP = "android.wallpaper.secondaryTap";
    public static final String COMMAND_TAP = "android.wallpaper.tap";
    private static boolean DEBUG = false;
    public static final String EXTRA_LIVE_WALLPAPER_COMPONENT = "android.service.wallpaper.extra.LIVE_WALLPAPER_COMPONENT";
    public static final String EXTRA_NEW_WALLPAPER_ID = "android.service.wallpaper.extra.ID";
    public static final int FLAG_LOCK = 2;
    public static final int FLAG_SYSTEM = 1;
    private static final String PROP_LOCK_WALLPAPER = "ro.config.lock_wallpaper";
    private static final String PROP_WALLPAPER = "ro.config.wallpaper";
    private static final String PROP_WALLPAPER_COMPONENT = "ro.config.wallpaper_component";
    private static String TAG = "WallpaperManager";
    public static final String WALLPAPER_PREVIEW_META_DATA = "android.wallpaper.preview";
    @UnsupportedAppUsage
    private static Globals sGlobals;
    private static final Object sSync = new Object[0];
    private final Context mContext;
    private float mWallpaperXStep = -1.0f;
    private float mWallpaperYStep = -1.0f;

    static class FastBitmapDrawable extends Drawable {
        private final Bitmap mBitmap;
        private int mDrawLeft;
        private int mDrawTop;
        private final int mHeight;
        private final Paint mPaint;
        private final int mWidth;

        private FastBitmapDrawable(Bitmap bitmap) {
            this.mBitmap = bitmap;
            this.mWidth = bitmap.getWidth();
            this.mHeight = bitmap.getHeight();
            setBounds(0, 0, this.mWidth, this.mHeight);
            this.mPaint = new Paint();
            this.mPaint.setXfermode(new PorterDuffXfermode(Mode.SRC));
        }

        public void draw(Canvas canvas) {
            canvas.drawBitmap(this.mBitmap, (float) this.mDrawLeft, (float) this.mDrawTop, this.mPaint);
        }

        public int getOpacity() {
            return -1;
        }

        public void setBounds(int left, int top, int right, int bottom) {
            this.mDrawLeft = (((right - left) - this.mWidth) / 2) + left;
            this.mDrawTop = (((bottom - top) - this.mHeight) / 2) + top;
        }

        public void setAlpha(int alpha) {
            throw new UnsupportedOperationException("Not supported with this drawable");
        }

        public void setColorFilter(ColorFilter colorFilter) {
            throw new UnsupportedOperationException("Not supported with this drawable");
        }

        public void setDither(boolean dither) {
            throw new UnsupportedOperationException("Not supported with this drawable");
        }

        public void setFilterBitmap(boolean filter) {
            throw new UnsupportedOperationException("Not supported with this drawable");
        }

        public int getIntrinsicWidth() {
            return this.mWidth;
        }

        public int getIntrinsicHeight() {
            return this.mHeight;
        }

        public int getMinimumWidth() {
            return this.mWidth;
        }

        public int getMinimumHeight() {
            return this.mHeight;
        }
    }

    private static class Globals extends Stub {
        private Bitmap mCachedWallpaper;
        private int mCachedWallpaperUserId;
        private boolean mColorCallbackRegistered;
        private final ArrayList<Pair<OnColorsChangedListener, Handler>> mColorListeners = new ArrayList();
        private Bitmap mDefaultWallpaper;
        private Handler mMainLooperHandler;
        private final IWallpaperManager mService;

        Globals(IWallpaperManager service, Looper looper) {
            this.mService = service;
            this.mMainLooperHandler = new Handler(looper);
            forgetLoadedWallpaper();
        }

        public void onWallpaperChanged() {
            forgetLoadedWallpaper();
        }

        public void addOnColorsChangedListener(OnColorsChangedListener callback, Handler handler, int userId, int displayId) {
            synchronized (this) {
                if (!this.mColorCallbackRegistered) {
                    try {
                        this.mService.registerWallpaperColorsCallback(this, userId, displayId);
                        this.mColorCallbackRegistered = true;
                    } catch (RemoteException e) {
                        Log.w(WallpaperManager.TAG, "Can't register for color updates", e);
                    }
                }
                this.mColorListeners.add(new Pair(callback, handler));
            }
        }

        public void removeOnColorsChangedListener(OnColorsChangedListener callback, int userId, int displayId) {
            synchronized (this) {
                this.mColorListeners.removeIf(new -$$Lambda$WallpaperManager$Globals$2yG7V1sbMECCnlFTLyjKWKqNoYI(callback));
                if (this.mColorListeners.size() == 0 && this.mColorCallbackRegistered) {
                    this.mColorCallbackRegistered = false;
                    try {
                        this.mService.unregisterWallpaperColorsCallback(this, userId, displayId);
                    } catch (RemoteException e) {
                        Log.w(WallpaperManager.TAG, "Can't unregister color updates", e);
                    }
                }
            }
        }

        static /* synthetic */ boolean lambda$removeOnColorsChangedListener$0(OnColorsChangedListener callback, Pair pair) {
            return pair.first == callback;
        }

        public void onWallpaperColorsChanged(WallpaperColors colors, int which, int userId) {
            synchronized (this) {
                Iterator it = this.mColorListeners.iterator();
                while (it.hasNext()) {
                    Handler handler;
                    Pair<OnColorsChangedListener, Handler> listener = (Pair) it.next();
                    Handler handler2 = listener.second;
                    if (listener.second == null) {
                        handler = this.mMainLooperHandler;
                    } else {
                        handler = handler2;
                    }
                    handler.post(new -$$Lambda$WallpaperManager$Globals$1AcnQUORvPlCjJoNqdxfQT4o4Nw(this, listener, colors, which, userId));
                }
            }
        }

        public /* synthetic */ void lambda$onWallpaperColorsChanged$1$WallpaperManager$Globals(Pair listener, WallpaperColors colors, int which, int userId) {
            boolean stillExists;
            synchronized (WallpaperManager.sGlobals) {
                stillExists = this.mColorListeners.contains(listener);
            }
            if (stillExists) {
                ((OnColorsChangedListener) listener.first).onColorsChanged(colors, which, userId);
            }
        }

        /* Access modifiers changed, original: 0000 */
        public WallpaperColors getWallpaperColors(int which, int userId, int displayId) {
            if (which == 2 || which == 1) {
                try {
                    return this.mService.getWallpaperColors(which, userId, displayId);
                } catch (RemoteException e) {
                    return null;
                }
            }
            throw new IllegalArgumentException("Must request colors for exactly one kind of wallpaper");
        }

        /* Access modifiers changed, original: 0000 */
        public WallpaperColors getWallpaperColors(int which, int userId, Rect rectOnScreen) {
            if (which == 2 || which == 1) {
                try {
                    return this.mService.getPartialWallpaperColors(which, userId, rectOnScreen);
                } catch (RemoteException e) {
                    return null;
                }
            }
            throw new IllegalArgumentException("Must request colors for exactly one kind of wallpaper");
        }

        public Bitmap peekWallpaperBitmap(Context context, boolean returnDefault, int which) {
            return peekWallpaperBitmap(context, returnDefault, which, context.getUserId(), false);
        }

        /* JADX WARNING: Missing block: B:38:0x0074, code skipped:
            if (r7 == false) goto L_0x0088;
     */
        /* JADX WARNING: Missing block: B:39:0x0076, code skipped:
            r0 = r5.mDefaultWallpaper;
     */
        /* JADX WARNING: Missing block: B:40:0x0078, code skipped:
            if (r0 != null) goto L_0x0087;
     */
        /* JADX WARNING: Missing block: B:41:0x007a, code skipped:
            r1 = getDefaultWallpaper(r6, r8);
     */
        /* JADX WARNING: Missing block: B:42:0x007e, code skipped:
            monitor-enter(r5);
     */
        /* JADX WARNING: Missing block: B:44:?, code skipped:
            r5.mDefaultWallpaper = r1;
     */
        /* JADX WARNING: Missing block: B:45:0x0081, code skipped:
            monitor-exit(r5);
     */
        /* JADX WARNING: Missing block: B:46:0x0082, code skipped:
            r0 = r1;
     */
        /* JADX WARNING: Missing block: B:50:0x0087, code skipped:
            return r0;
     */
        /* JADX WARNING: Missing block: B:51:0x0088, code skipped:
            return null;
     */
        public android.graphics.Bitmap peekWallpaperBitmap(android.content.Context r6, boolean r7, int r8, int r9, boolean r10) {
            /*
            r5 = this;
            r0 = r5.mService;
            r1 = 0;
            if (r0 == 0) goto L_0x0017;
        L_0x0005:
            r2 = r6.getOpPackageName();	 Catch:{ RemoteException -> 0x0011 }
            r0 = r0.isWallpaperSupported(r2);	 Catch:{ RemoteException -> 0x0011 }
            if (r0 != 0) goto L_0x0010;
        L_0x000f:
            return r1;
        L_0x0010:
            goto L_0x0017;
        L_0x0011:
            r0 = move-exception;
            r1 = r0.rethrowFromSystemServer();
            throw r1;
        L_0x0017:
            monitor-enter(r5);
            r0 = r5.mCachedWallpaper;	 Catch:{ all -> 0x0089 }
            if (r0 == 0) goto L_0x002c;
        L_0x001c:
            r0 = r5.mCachedWallpaperUserId;	 Catch:{ all -> 0x0089 }
            if (r0 != r9) goto L_0x002c;
        L_0x0020:
            r0 = r5.mCachedWallpaper;	 Catch:{ all -> 0x0089 }
            r0 = r0.isRecycled();	 Catch:{ all -> 0x0089 }
            if (r0 != 0) goto L_0x002c;
        L_0x0028:
            r0 = r5.mCachedWallpaper;	 Catch:{ all -> 0x0089 }
            monitor-exit(r5);	 Catch:{ all -> 0x0089 }
            return r0;
        L_0x002c:
            r5.mCachedWallpaper = r1;	 Catch:{ all -> 0x0089 }
            r0 = 0;
            r5.mCachedWallpaperUserId = r0;	 Catch:{ all -> 0x0089 }
            r0 = r5.getCurrentWallpaperLocked(r6, r9, r10);	 Catch:{ OutOfMemoryError -> 0x0051, SecurityException -> 0x003a }
            r5.mCachedWallpaper = r0;	 Catch:{ OutOfMemoryError -> 0x0051, SecurityException -> 0x003a }
            r5.mCachedWallpaperUserId = r9;	 Catch:{ OutOfMemoryError -> 0x0051, SecurityException -> 0x003a }
            goto L_0x006b;
        L_0x003a:
            r0 = move-exception;
            r2 = r6.getApplicationInfo();	 Catch:{ all -> 0x0089 }
            r2 = r2.targetSdkVersion;	 Catch:{ all -> 0x0089 }
            r3 = 27;
            if (r2 >= r3) goto L_0x004f;
        L_0x0045:
            r2 = android.app.WallpaperManager.TAG;	 Catch:{ all -> 0x0089 }
            r3 = "No permission to access wallpaper, suppressing exception to avoid crashing legacy app.";
            android.util.Log.w(r2, r3);	 Catch:{ all -> 0x0089 }
            goto L_0x006b;
            throw r0;	 Catch:{ all -> 0x0089 }
        L_0x0051:
            r0 = move-exception;
            r2 = android.app.WallpaperManager.TAG;	 Catch:{ all -> 0x0089 }
            r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0089 }
            r3.<init>();	 Catch:{ all -> 0x0089 }
            r4 = "Out of memory loading the current wallpaper: ";
            r3.append(r4);	 Catch:{ all -> 0x0089 }
            r3.append(r0);	 Catch:{ all -> 0x0089 }
            r3 = r3.toString();	 Catch:{ all -> 0x0089 }
            android.util.Log.w(r2, r3);	 Catch:{ all -> 0x0089 }
        L_0x006b:
            r0 = r5.mCachedWallpaper;	 Catch:{ all -> 0x0089 }
            if (r0 == 0) goto L_0x0073;
        L_0x006f:
            r0 = r5.mCachedWallpaper;	 Catch:{ all -> 0x0089 }
            monitor-exit(r5);	 Catch:{ all -> 0x0089 }
            return r0;
        L_0x0073:
            monitor-exit(r5);	 Catch:{ all -> 0x0089 }
            if (r7 == 0) goto L_0x0088;
        L_0x0076:
            r0 = r5.mDefaultWallpaper;
            if (r0 != 0) goto L_0x0087;
        L_0x007a:
            r1 = r5.getDefaultWallpaper(r6, r8);
            monitor-enter(r5);
            r5.mDefaultWallpaper = r1;	 Catch:{ all -> 0x0084 }
            monitor-exit(r5);	 Catch:{ all -> 0x0084 }
            r0 = r1;
            goto L_0x0087;
        L_0x0084:
            r0 = move-exception;
            monitor-exit(r5);	 Catch:{ all -> 0x0084 }
            throw r0;
        L_0x0087:
            return r0;
        L_0x0088:
            return r1;
        L_0x0089:
            r0 = move-exception;
            monitor-exit(r5);	 Catch:{ all -> 0x0089 }
            throw r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.app.WallpaperManager$Globals.peekWallpaperBitmap(android.content.Context, boolean, int, int, boolean):android.graphics.Bitmap");
        }

        /* Access modifiers changed, original: 0000 */
        public void forgetLoadedWallpaper() {
            synchronized (this) {
                this.mCachedWallpaper = null;
                this.mCachedWallpaperUserId = 0;
                this.mDefaultWallpaper = null;
            }
        }

        private Bitmap getCurrentWallpaperLocked(Context context, int userId, boolean hardware) {
            if (this.mService == null) {
                Log.w(WallpaperManager.TAG, "WallpaperService not running");
                return null;
            }
            ParcelFileDescriptor fd;
            try {
                fd = this.mService.getWallpaper(context.getOpPackageName(), this, 1, new Bundle(), userId);
                if (fd != null) {
                    try {
                        Options options = new Options();
                        if (hardware) {
                            options.inPreferredConfig = Config.HARDWARE;
                        }
                        Bitmap decodeFileDescriptor = BitmapFactory.decodeFileDescriptor(fd.getFileDescriptor(), null, options);
                        IoUtils.closeQuietly(fd);
                        return decodeFileDescriptor;
                    } catch (OutOfMemoryError e) {
                        Log.w(WallpaperManager.TAG, "Can't decode file", e);
                        IoUtils.closeQuietly(fd);
                    }
                }
                return null;
            } catch (RemoteException e2) {
                throw e2.rethrowFromSystemServer();
            } catch (Throwable th) {
                IoUtils.closeQuietly(fd);
            }
        }

        private Bitmap getDefaultWallpaper(Context context, int which) {
            InputStream is = WallpaperManager.openDefaultWallpaper(context, which);
            Bitmap bitmap = null;
            if (is != null) {
                try {
                    bitmap = BitmapFactory.decodeStream(is, null, new Options());
                    return bitmap;
                } catch (OutOfMemoryError e) {
                    Log.w(WallpaperManager.TAG, "Can't decode stream", e);
                } finally {
                    IoUtils.closeQuietly(is);
                }
            }
            return bitmap;
        }
    }

    public interface OnColorsChangedListener {
        void onColorsChanged(WallpaperColors wallpaperColors, int i);

        void onColorsChanged(WallpaperColors colors, int which, int userId) {
            onColorsChanged(colors, which);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SetWallpaperFlags {
    }

    private class WallpaperSetCompletion extends Stub {
        final CountDownLatch mLatch = new CountDownLatch(1);

        public void waitForCompletion() {
            try {
                this.mLatch.await(30, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
            }
        }

        public void onWallpaperChanged() throws RemoteException {
            this.mLatch.countDown();
        }

        public void onWallpaperColorsChanged(WallpaperColors colors, int which, int userId) throws RemoteException {
            WallpaperManager.sGlobals.onWallpaperColorsChanged(colors, which, userId);
        }
    }

    static void initGlobals(IWallpaperManager service, Looper looper) {
        synchronized (sSync) {
            if (sGlobals == null) {
                sGlobals = new Globals(service, looper);
            }
        }
    }

    WallpaperManager(IWallpaperManager service, Context context, Handler handler) {
        this.mContext = context;
        initGlobals(service, context.getMainLooper());
    }

    public static WallpaperManager getInstance(Context context) {
        return (WallpaperManager) context.getSystemService(Context.WALLPAPER_SERVICE);
    }

    @UnsupportedAppUsage
    public IWallpaperManager getIWallpaperManager() {
        return sGlobals.mService;
    }

    public Drawable getDrawable() {
        Bitmap bm = sGlobals.peekWallpaperBitmap(this.mContext, true, 1);
        if (bm == null) {
            return null;
        }
        Drawable dr = new BitmapDrawable(this.mContext.getResources(), bm);
        dr.setDither(false);
        return dr;
    }

    public Drawable getBuiltInDrawable() {
        return getBuiltInDrawable(0, 0, false, 0.0f, 0.0f, 1);
    }

    public Drawable getBuiltInDrawable(int which) {
        return getBuiltInDrawable(0, 0, false, 0.0f, 0.0f, which);
    }

    public Drawable getBuiltInDrawable(int outWidth, int outHeight, boolean scaleToFit, float horizontalAlignment, float verticalAlignment) {
        return getBuiltInDrawable(outWidth, outHeight, scaleToFit, horizontalAlignment, verticalAlignment, 1);
    }

    public Drawable getBuiltInDrawable(int outWidth, int outHeight, boolean scaleToFit, float horizontalAlignment, float verticalAlignment, int which) {
        int i = outWidth;
        int outHeight2 = outHeight;
        int i2 = which;
        float f;
        if (sGlobals.mService == null) {
            f = horizontalAlignment;
            float f2 = verticalAlignment;
            Log.w(TAG, "WallpaperService not running");
            throw new RuntimeException(new DeadSystemException());
        } else if (i2 == 1 || i2 == 2) {
            Resources resources = this.mContext.getResources();
            f = Math.max(0.0f, Math.min(1.0f, horizontalAlignment));
            float verticalAlignment2 = Math.max(0.0f, Math.min(1.0f, verticalAlignment));
            InputStream wpStream = openDefaultWallpaper(this.mContext, i2);
            if (wpStream == null) {
                if (DEBUG) {
                    String str = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("default wallpaper stream ");
                    stringBuilder.append(i2);
                    stringBuilder.append(" is null");
                    Log.w(str, stringBuilder.toString());
                }
                return null;
            }
            Bitmap fullSize;
            InputStream is = new BufferedInputStream(wpStream);
            float f3;
            if (i <= 0) {
                f3 = f;
                fullSize = null;
            } else if (outHeight2 <= 0) {
                horizontalAlignment = verticalAlignment2;
                f3 = f;
                fullSize = null;
            } else {
                Options options = new Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(is, null, options);
                if (options.outWidth == 0 || options.outHeight == 0) {
                    f3 = f;
                    Drawable drawable = null;
                    Log.e(TAG, "default wallpaper dimensions are 0");
                    return drawable;
                }
                InputStream is2;
                int inWidth = options.outWidth;
                int inHeight = options.outHeight;
                InputStream is3 = new BufferedInputStream(openDefaultWallpaper(this.mContext, i2));
                int outWidth2 = Math.min(inWidth, i);
                outHeight2 = Math.min(inHeight, outHeight2);
                if (scaleToFit) {
                    outWidth = outWidth2;
                    is2 = is3;
                    is = outWidth;
                    options = getMaxCropRect(inWidth, inHeight, outWidth, outHeight2, f, verticalAlignment2);
                } else {
                    is2 = is3;
                    is = outWidth2;
                    float left = ((float) (inWidth - is)) * f;
                    float top = ((float) (inHeight - outHeight2)) * verticalAlignment2;
                    options = new RectF(left, top, ((float) is) + left, ((float) outHeight2) + top);
                }
                Rect roundedTrueCrop = new Rect();
                options.roundOut(roundedTrueCrop);
                if (roundedTrueCrop.width() <= 0) {
                    outWidth = outHeight2;
                    horizontalAlignment = verticalAlignment2;
                    f3 = f;
                } else if (roundedTrueCrop.height() <= 0) {
                    outWidth = outHeight2;
                    horizontalAlignment = verticalAlignment2;
                    f3 = f;
                } else {
                    inHeight = Math.min(roundedTrueCrop.width() / is, roundedTrueCrop.height() / outHeight2);
                    BitmapRegionDecoder decoder = null;
                    try {
                        decoder = BitmapRegionDecoder.newInstance(is2, true);
                    } catch (IOException e) {
                        Log.w(TAG, "cannot open region decoder for default wallpaper");
                    }
                    Bitmap crop = null;
                    if (decoder != null) {
                        Options options2 = new Options();
                        if (inHeight > 1) {
                            options2.inSampleSize = inHeight;
                        }
                        crop = decoder.decodeRegion(roundedTrueCrop, options2);
                        decoder.recycle();
                    }
                    if (crop == null) {
                        InputStream is4 = new BufferedInputStream(openDefaultWallpaper(this.mContext, i2));
                        Options options3 = new Options();
                        if (inHeight > 1) {
                            options3.inSampleSize = inHeight;
                        }
                        Bitmap fullSize2 = BitmapFactory.decodeStream(is4, null, options3);
                        if (fullSize2 != null) {
                            InputStream is5 = is4;
                            crop = Bitmap.createBitmap(fullSize2, roundedTrueCrop.left, roundedTrueCrop.top, roundedTrueCrop.width(), roundedTrueCrop.height());
                            is2 = is5;
                        } else {
                            Bitmap bitmap = crop;
                        }
                    }
                    if (crop == null) {
                        Log.w(TAG, "cannot decode default wallpaper");
                        return null;
                    }
                    if (is <= null || outHeight2 <= 0) {
                        horizontalAlignment = verticalAlignment2;
                        f3 = f;
                    } else if (crop.getWidth() == is && crop.getHeight() == outHeight2) {
                        outWidth = outHeight2;
                        horizontalAlignment = verticalAlignment2;
                        f3 = f;
                    } else {
                        Matrix m = new Matrix();
                        RectF cropRect = new RectF(0.0f, 0.0f, (float) crop.getWidth(), (float) crop.getHeight());
                        RectF returnRect = new RectF(0.0f, 0.0f, (float) is, (float) outHeight2);
                        m.setRectToRect(cropRect, returnRect, ScaleToFit.FILL);
                        Bitmap verticalAlignment3 = Bitmap.createBitmap((int) returnRect.width(), (int) returnRect.height(), Config.ARGB_8888);
                        if (verticalAlignment3 != null) {
                            f = new Canvas(verticalAlignment3);
                            Paint p = new Paint();
                            p.setFilterBitmap(1);
                            f.drawBitmap(crop, m, p);
                            crop = verticalAlignment3;
                        }
                    }
                    return new BitmapDrawable(resources, crop);
                }
                Log.w(TAG, "crop has bad values for full size image");
                return null;
            }
            return new BitmapDrawable(resources, BitmapFactory.decodeStream(is, fullSize, fullSize));
        } else {
            throw new IllegalArgumentException("Must request exactly one kind of wallpaper");
        }
    }

    private static RectF getMaxCropRect(int inWidth, int inHeight, int outWidth, int outHeight, float horizontalAlignment, float verticalAlignment) {
        RectF cropRect = new RectF();
        float cropWidth;
        if (((float) inWidth) / ((float) inHeight) > ((float) outWidth) / ((float) outHeight)) {
            cropRect.top = 0.0f;
            cropRect.bottom = (float) inHeight;
            cropWidth = ((float) outWidth) * (((float) inHeight) / ((float) outHeight));
            cropRect.left = (((float) inWidth) - cropWidth) * horizontalAlignment;
            cropRect.right = cropRect.left + cropWidth;
        } else {
            cropRect.left = 0.0f;
            cropRect.right = (float) inWidth;
            cropWidth = ((float) outHeight) * (((float) inWidth) / ((float) outWidth));
            cropRect.top = (((float) inHeight) - cropWidth) * verticalAlignment;
            cropRect.bottom = cropRect.top + cropWidth;
        }
        return cropRect;
    }

    public Drawable peekDrawable() {
        Bitmap bm = sGlobals.peekWallpaperBitmap(this.mContext, false, 1);
        if (bm == null) {
            return null;
        }
        Drawable dr = new BitmapDrawable(this.mContext.getResources(), bm);
        dr.setDither(false);
        return dr;
    }

    public Drawable getFastDrawable() {
        Bitmap bm = sGlobals.peekWallpaperBitmap(this.mContext, true, 1);
        if (bm != null) {
            return new FastBitmapDrawable(bm);
        }
        return null;
    }

    public Drawable peekFastDrawable() {
        Bitmap bm = sGlobals.peekWallpaperBitmap(this.mContext, false, 1);
        if (bm != null) {
            return new FastBitmapDrawable(bm);
        }
        return null;
    }

    @UnsupportedAppUsage
    public Bitmap getBitmap() {
        return getBitmap(false);
    }

    @UnsupportedAppUsage
    public Bitmap getBitmap(boolean hardware) {
        return getBitmapAsUser(this.mContext.getUserId(), hardware);
    }

    public Bitmap getBitmapAsUser(int userId, boolean hardware) {
        return sGlobals.peekWallpaperBitmap(this.mContext, true, 1, userId, hardware);
    }

    public ParcelFileDescriptor getWallpaperFile(int which) {
        return getWallpaperFile(which, this.mContext.getUserId());
    }

    public void addOnColorsChangedListener(OnColorsChangedListener listener, Handler handler) {
        addOnColorsChangedListener(listener, handler, this.mContext.getUserId());
    }

    @UnsupportedAppUsage
    public void addOnColorsChangedListener(OnColorsChangedListener listener, Handler handler, int userId) {
        sGlobals.addOnColorsChangedListener(listener, handler, userId, this.mContext.getDisplayId());
    }

    public void removeOnColorsChangedListener(OnColorsChangedListener callback) {
        removeOnColorsChangedListener(callback, this.mContext.getUserId());
    }

    public void removeOnColorsChangedListener(OnColorsChangedListener callback, int userId) {
        sGlobals.removeOnColorsChangedListener(callback, userId, this.mContext.getDisplayId());
    }

    public WallpaperColors getWallpaperColors(int which) {
        return getWallpaperColors(which, this.mContext.getUserId());
    }

    public WallpaperColors getWallpaperColors(int which, Rect rectOnScreen) {
        validateRect(rectOnScreen);
        return sGlobals.getWallpaperColors(which, this.mContext.getUserId(), rectOnScreen);
    }

    @UnsupportedAppUsage
    public WallpaperColors getWallpaperColors(int which, int userId) {
        return sGlobals.getWallpaperColors(which, userId, this.mContext.getDisplayId());
    }

    @UnsupportedAppUsage
    public ParcelFileDescriptor getWallpaperFile(int which, int userId) {
        if (which != 1 && which != 2) {
            throw new IllegalArgumentException("Must request exactly one kind of wallpaper");
        } else if (sGlobals.mService != null) {
            try {
                return sGlobals.mService.getWallpaper(this.mContext.getOpPackageName(), null, which, new Bundle(), userId);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            } catch (SecurityException e2) {
                if (this.mContext.getApplicationInfo().targetSdkVersion < 27) {
                    Log.w(TAG, "No permission to access wallpaper, suppressing exception to avoid crashing legacy app.");
                    return null;
                }
                throw e2;
            }
        } else {
            Log.w(TAG, "WallpaperService not running");
            throw new RuntimeException(new DeadSystemException());
        }
    }

    public void forgetLoadedWallpaper() {
        sGlobals.forgetLoadedWallpaper();
    }

    public WallpaperInfo getWallpaperInfo() {
        return getWallpaperInfo(this.mContext.getUserId());
    }

    public WallpaperInfo getWallpaperInfo(int userId) {
        try {
            if (sGlobals.mService != null) {
                return sGlobals.mService.getWallpaperInfo(userId);
            }
            Log.w(TAG, "WallpaperService not running");
            throw new RuntimeException(new DeadSystemException());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getWallpaperId(int which) {
        return getWallpaperIdForUser(which, this.mContext.getUserId());
    }

    public int getWallpaperIdForUser(int which, int userId) {
        try {
            if (sGlobals.mService != null) {
                return sGlobals.mService.getWallpaperIdForUser(which, userId);
            }
            Log.w(TAG, "WallpaperService not running");
            throw new RuntimeException(new DeadSystemException());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public Intent getCropAndSetWallpaperIntent(Uri imageUri) {
        if (imageUri != null) {
            if ("content".equals(imageUri.getScheme())) {
                PackageManager packageManager = this.mContext.getPackageManager();
                Intent cropAndSetWallpaperIntent = new Intent(ACTION_CROP_AND_SET_WALLPAPER, imageUri);
                cropAndSetWallpaperIntent.addFlags(1);
                ResolveInfo resolvedHome = packageManager.resolveActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), 65536);
                if (resolvedHome != null) {
                    cropAndSetWallpaperIntent.setPackage(resolvedHome.activityInfo.packageName);
                    if (packageManager.queryIntentActivities(cropAndSetWallpaperIntent, 0).size() > 0) {
                        return cropAndSetWallpaperIntent;
                    }
                }
                cropAndSetWallpaperIntent.setPackage(this.mContext.getString(R.string.config_wallpaperCropperPackage));
                if (packageManager.queryIntentActivities(cropAndSetWallpaperIntent, 0).size() > 0) {
                    return cropAndSetWallpaperIntent;
                }
                throw new IllegalArgumentException("Cannot use passed URI to set wallpaper; check that the type returned by ContentProvider matches image/*");
            }
            throw new IllegalArgumentException("Image URI must be of the content scheme type");
        }
        throw new IllegalArgumentException("Image URI must not be null");
    }

    public void setResource(int resid) throws IOException {
        setResource(resid, 3);
    }

    public int setResource(int resid, int which) throws IOException {
        if (sGlobals.mService != null) {
            Bundle result = new Bundle();
            WallpaperSetCompletion completion = new WallpaperSetCompletion();
            FileOutputStream fos;
            try {
                Resources resources = this.mContext.getResources();
                ParcelFileDescriptor fd = sGlobals.mService;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("res:");
                stringBuilder.append(resources.getResourceName(resid));
                fd = fd.setWallpaper(stringBuilder.toString(), this.mContext.getOpPackageName(), null, false, result, which, completion, this.mContext.getUserId());
                if (fd != null) {
                    fos = null;
                    fos = new AutoCloseOutputStream(fd);
                    copyStreamToWallpaperFile(resources.openRawResource(resid), fos);
                    fos.close();
                    completion.waitForCompletion();
                    IoUtils.closeQuietly(fos);
                }
                return result.getInt(EXTRA_NEW_WALLPAPER_ID, 0);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            } catch (Throwable th) {
                IoUtils.closeQuietly(fos);
            }
        }
        Log.w(TAG, "WallpaperService not running");
        throw new RuntimeException(new DeadSystemException());
    }

    public void setBitmap(Bitmap bitmap) throws IOException {
        setBitmap(bitmap, null, true);
    }

    public int setBitmap(Bitmap fullImage, Rect visibleCropHint, boolean allowBackup) throws IOException {
        return setBitmap(fullImage, visibleCropHint, allowBackup, 3);
    }

    public int setBitmap(Bitmap fullImage, Rect visibleCropHint, boolean allowBackup, int which) throws IOException {
        return setBitmap(fullImage, visibleCropHint, allowBackup, which, this.mContext.getUserId());
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public int setBitmap(Bitmap fullImage, Rect visibleCropHint, boolean allowBackup, int which, int userId) throws IOException {
        validateRect(visibleCropHint);
        if (sGlobals.mService != null) {
            Bundle result = new Bundle();
            WallpaperSetCompletion completion = new WallpaperSetCompletion();
            FileOutputStream fos;
            try {
                ParcelFileDescriptor fd = sGlobals.mService.setWallpaper(null, this.mContext.getOpPackageName(), visibleCropHint, allowBackup, result, which, completion, userId);
                if (fd != null) {
                    fos = null;
                    fos = new AutoCloseOutputStream(fd);
                    fullImage.compress(CompressFormat.PNG, 90, fos);
                    fos.close();
                    completion.waitForCompletion();
                    IoUtils.closeQuietly(fos);
                }
                return result.getInt(EXTRA_NEW_WALLPAPER_ID, 0);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            } catch (Throwable th) {
                IoUtils.closeQuietly(fos);
            }
        }
        Log.w(TAG, "WallpaperService not running");
        throw new RuntimeException(new DeadSystemException());
    }

    private final void validateRect(Rect rect) {
        if (rect != null && rect.isEmpty()) {
            throw new IllegalArgumentException("visibleCrop rectangle must be valid and non-empty");
        }
    }

    public void setStream(InputStream bitmapData) throws IOException {
        setStream(bitmapData, null, true);
    }

    private void copyStreamToWallpaperFile(InputStream data, FileOutputStream fos) throws IOException {
        FileUtils.copy(data, (OutputStream) fos);
    }

    public int setStream(InputStream bitmapData, Rect visibleCropHint, boolean allowBackup) throws IOException {
        return setStream(bitmapData, visibleCropHint, allowBackup, 3);
    }

    public int setStream(InputStream bitmapData, Rect visibleCropHint, boolean allowBackup, int which) throws IOException {
        validateRect(visibleCropHint);
        if (sGlobals.mService != null) {
            Bundle result = new Bundle();
            WallpaperSetCompletion completion = new WallpaperSetCompletion();
            FileOutputStream fos;
            try {
                ParcelFileDescriptor fd = sGlobals.mService.setWallpaper(null, this.mContext.getOpPackageName(), visibleCropHint, allowBackup, result, which, completion, this.mContext.getUserId());
                if (fd != null) {
                    fos = null;
                    fos = new AutoCloseOutputStream(fd);
                    copyStreamToWallpaperFile(bitmapData, fos);
                    fos.close();
                    completion.waitForCompletion();
                    IoUtils.closeQuietly(fos);
                }
                return result.getInt(EXTRA_NEW_WALLPAPER_ID, 0);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            } catch (Throwable th) {
                IoUtils.closeQuietly(fos);
            }
        }
        Log.w(TAG, "WallpaperService not running");
        throw new RuntimeException(new DeadSystemException());
    }

    public boolean hasResourceWallpaper(int resid) {
        if (sGlobals.mService != null) {
            try {
                Resources resources = this.mContext.getResources();
                String name = new StringBuilder();
                name.append("res:");
                name.append(resources.getResourceName(resid));
                return sGlobals.mService.hasNamedWallpaper(name.toString());
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        Log.w(TAG, "WallpaperService not running");
        throw new RuntimeException(new DeadSystemException());
    }

    public int getDesiredMinimumWidth() {
        if (sGlobals.mService != null) {
            try {
                return sGlobals.mService.getWidthHint(this.mContext.getDisplayId());
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        Log.w(TAG, "WallpaperService not running");
        throw new RuntimeException(new DeadSystemException());
    }

    public int getDesiredMinimumHeight() {
        if (sGlobals.mService != null) {
            try {
                return sGlobals.mService.getHeightHint(this.mContext.getDisplayId());
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        Log.w(TAG, "WallpaperService not running");
        throw new RuntimeException(new DeadSystemException());
    }

    public void suggestDesiredDimensions(int minimumWidth, int minimumHeight) {
        RemoteException e;
        try {
            e = SystemProperties.getInt("sys.max_texture_size", 0);
        } catch (Exception e2) {
            e = null;
        }
        if (e > null && (minimumWidth > e || minimumHeight > e)) {
            float aspect = ((float) minimumHeight) / ((float) minimumWidth);
            if (minimumWidth > minimumHeight) {
                minimumWidth = e;
                minimumHeight = (int) (((double) (((float) minimumWidth) * aspect)) + 0.5d);
            } else {
                minimumHeight = e;
                minimumWidth = (int) (((double) (((float) minimumHeight) / aspect)) + 0.5d);
            }
        }
        try {
            if (sGlobals.mService != null) {
                sGlobals.mService.setDimensionHints(minimumWidth, minimumHeight, this.mContext.getOpPackageName(), this.mContext.getDisplayId());
            } else {
                Log.w(TAG, "WallpaperService not running");
                throw new RuntimeException(new DeadSystemException());
            }
        } catch (RemoteException e3) {
            throw e3.rethrowFromSystemServer();
        }
    }

    public void setDisplayPadding(Rect padding) {
        try {
            if (sGlobals.mService != null) {
                sGlobals.mService.setDisplayPadding(padding, this.mContext.getOpPackageName(), this.mContext.getDisplayId());
            } else {
                Log.w(TAG, "WallpaperService not running");
                throw new RuntimeException(new DeadSystemException());
            }
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void setDisplayOffset(IBinder windowToken, int x, int y) {
        try {
            WindowManagerGlobal.getWindowSession().setWallpaperDisplayOffset(windowToken, x, y);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void clearWallpaper() {
        clearWallpaper(2, this.mContext.getUserId());
        clearWallpaper(1, this.mContext.getUserId());
    }

    @SystemApi
    public void clearWallpaper(int which, int userId) {
        if (sGlobals.mService != null) {
            try {
                sGlobals.mService.clearWallpaper(this.mContext.getOpPackageName(), which, userId);
                return;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        Log.w(TAG, "WallpaperService not running");
        throw new RuntimeException(new DeadSystemException());
    }

    @SystemApi
    public boolean setWallpaperComponent(ComponentName name) {
        return setWallpaperComponent(name, this.mContext.getUserId());
    }

    @UnsupportedAppUsage
    public boolean setWallpaperComponent(ComponentName name, int userId) {
        if (sGlobals.mService != null) {
            try {
                sGlobals.mService.setWallpaperComponentChecked(name, this.mContext.getOpPackageName(), userId);
                return true;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        Log.w(TAG, "WallpaperService not running");
        throw new RuntimeException(new DeadSystemException());
    }

    public void setWallpaperOffsets(IBinder windowToken, float xOffset, float yOffset) {
        try {
            WindowManagerGlobal.getWindowSession().setWallpaperPosition(windowToken, xOffset, yOffset, this.mWallpaperXStep, this.mWallpaperYStep);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setWallpaperOffsetSteps(float xStep, float yStep) {
        this.mWallpaperXStep = xStep;
        this.mWallpaperYStep = yStep;
    }

    public void sendWallpaperCommand(IBinder windowToken, String action, int x, int y, int z, Bundle extras) {
        try {
            WindowManagerGlobal.getWindowSession().sendWallpaperCommand(windowToken, action, x, y, z, extras, false);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isWallpaperSupported() {
        if (sGlobals.mService != null) {
            try {
                return sGlobals.mService.isWallpaperSupported(this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        Log.w(TAG, "WallpaperService not running");
        throw new RuntimeException(new DeadSystemException());
    }

    public boolean isSetWallpaperAllowed() {
        if (sGlobals.mService != null) {
            try {
                return sGlobals.mService.isSetWallpaperAllowed(this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        Log.w(TAG, "WallpaperService not running");
        throw new RuntimeException(new DeadSystemException());
    }

    public void clearWallpaperOffsets(IBinder windowToken) {
        try {
            WindowManagerGlobal.getWindowSession().setWallpaperPosition(windowToken, -1.0f, -1.0f, -1.0f, -1.0f);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void clear() throws IOException {
        setStream(openDefaultWallpaper(this.mContext, 1), null, false);
    }

    public void clear(int which) throws IOException {
        if ((which & 1) != 0) {
            clear();
        }
        if ((which & 2) != 0) {
            clearWallpaper(2, this.mContext.getUserId());
        }
    }

    @UnsupportedAppUsage
    public static InputStream openDefaultWallpaper(Context context, int which) {
        InputStream inputStream = null;
        if (which == 2) {
            return null;
        }
        String path = SystemProperties.get(PROP_WALLPAPER);
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            if (file.exists()) {
                try {
                    return new FileInputStream(file);
                } catch (IOException e) {
                }
            }
        }
        try {
            inputStream = context.getResources().openRawResource(R.drawable.default_wallpaper);
            return inputStream;
        } catch (NotFoundException e2) {
            return inputStream;
        }
    }

    public static ComponentName getDefaultWallpaperComponent(Context context) {
        ComponentName cn = null;
        String flat = SystemProperties.get(PROP_WALLPAPER_COMPONENT);
        if (!TextUtils.isEmpty(flat)) {
            cn = ComponentName.unflattenFromString(flat);
        }
        if (cn == null) {
            flat = context.getString(R.string.default_wallpaper_component);
            if (!TextUtils.isEmpty(flat)) {
                cn = ComponentName.unflattenFromString(flat);
            }
        }
        if (cn == null) {
            return cn;
        }
        try {
            context.getPackageManager().getPackageInfo(cn.getPackageName(), 786432);
            return cn;
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    public boolean setLockWallpaperCallback(IWallpaperManagerCallback callback) {
        if (sGlobals.mService != null) {
            try {
                return sGlobals.mService.setLockWallpaperCallback(callback);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        Log.w(TAG, "WallpaperService not running");
        throw new RuntimeException(new DeadSystemException());
    }

    public boolean isWallpaperBackupEligible(int which) {
        if (sGlobals.mService != null) {
            try {
                return sGlobals.mService.isWallpaperBackupEligible(which, this.mContext.getUserId());
            } catch (RemoteException e) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Exception querying wallpaper backup eligibility: ");
                stringBuilder.append(e.getMessage());
                Log.e(str, stringBuilder.toString());
                return false;
            }
        }
        Log.w(TAG, "WallpaperService not running");
        throw new RuntimeException(new DeadSystemException());
    }
}
