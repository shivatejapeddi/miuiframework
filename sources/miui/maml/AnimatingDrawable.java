package miui.maml;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.UserHandle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import miui.maml.MamlDrawable.MamlDrawableState;

public class AnimatingDrawable extends MamlDrawable {
    private static final String QUIET_IMAGE_NAME = "quietImage.png";
    private static final String TAG = "Maml.AnimatingDrawable";
    public static final int TIME_FANCY_CACHE = 0;
    private String mClassName;
    private Context mContext;
    private FancyDrawable mFancyDrawable;
    private final Object mLock = new Object();
    private String mPackageName;
    private Drawable mQuietDrawable;
    private ResourceManager mResourceManager;
    private UserHandle mUser;

    static final class AnimatingDrawableState extends MamlDrawableState {
        private String mClassName;
        private Context mContext;
        private String mPackageName;
        private ResourceManager mResourceManager;
        private UserHandle mUser;

        public AnimatingDrawableState(Context context, String packageName, String className, ResourceManager manager, UserHandle user) {
            this.mContext = context;
            this.mResourceManager = manager;
            this.mPackageName = packageName;
            this.mClassName = className;
            this.mUser = user;
        }

        /* Access modifiers changed, original: protected */
        public MamlDrawable createDrawable() {
            return new AnimatingDrawable(this.mContext, this.mPackageName, this.mClassName, this.mResourceManager, this.mUser);
        }
    }

    public AnimatingDrawable(Context context, String packageName, String className, ResourceManager manager, UserHandle user) {
        this.mContext = context;
        this.mResourceManager = manager;
        this.mPackageName = packageName;
        this.mClassName = className;
        this.mUser = user;
        init();
    }

    private void init() {
        this.mState = new AnimatingDrawableState(this.mContext, this.mPackageName, this.mClassName, this.mResourceManager, this.mUser);
        Display display = ((WindowManager) this.mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        ResourceManager resourceManager = this.mResourceManager;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("den");
        stringBuilder.append(outMetrics.densityDpi);
        resourceManager.setExtraResource(stringBuilder.toString(), outMetrics.densityDpi);
        this.mQuietDrawable = this.mResourceManager.getDrawable(this.mContext.getResources(), QUIET_IMAGE_NAME);
        Drawable drawable = this.mQuietDrawable;
        if (drawable != null) {
            setIntrinsicSize(drawable.getIntrinsicWidth(), this.mQuietDrawable.getIntrinsicHeight());
            this.mQuietDrawable = this.mQuietDrawable.mutate();
            drawable = this.mQuietDrawable;
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), this.mQuietDrawable.getIntrinsicHeight());
            return;
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("mQuietDrwable is null! package/class=");
        stringBuilder2.append(this.mPackageName);
        stringBuilder2.append("/");
        stringBuilder2.append(this.mClassName);
        Log.e(TAG, stringBuilder2.toString());
    }

    public Drawable getStartDrawable() {
        synchronized (this.mLock) {
            prepareFancyDrawable();
            if (this.mFancyDrawable != null) {
                Drawable startDrawable = this.mFancyDrawable.getStartDrawable();
                return startDrawable;
            }
            return null;
        }
    }

    /* JADX WARNING: Missing block: B:13:0x0030, code skipped:
            return;
     */
    public void prepareFancyDrawable() {
        /*
        r8 = this;
        r0 = r8.mLock;
        monitor-enter(r0);
        r1 = r8.mFancyDrawable;	 Catch:{ all -> 0x0031 }
        if (r1 == 0) goto L_0x0009;
    L_0x0007:
        monitor-exit(r0);	 Catch:{ all -> 0x0031 }
        return;
    L_0x0009:
        r2 = r8.mContext;	 Catch:{ all -> 0x0031 }
        r3 = r8.mPackageName;	 Catch:{ all -> 0x0031 }
        r4 = r8.mClassName;	 Catch:{ all -> 0x0031 }
        r5 = 0;
        r7 = r8.mUser;	 Catch:{ all -> 0x0031 }
        r1 = miui.maml.util.AppIconsHelper.getFancyIconDrawable(r2, r3, r4, r5, r7);	 Catch:{ all -> 0x0031 }
        r2 = r1 instanceof miui.maml.FancyDrawable;	 Catch:{ all -> 0x0031 }
        if (r2 == 0) goto L_0x002f;
    L_0x001b:
        r2 = r1;
        r2 = (miui.maml.FancyDrawable) r2;	 Catch:{ all -> 0x0031 }
        r8.mFancyDrawable = r2;	 Catch:{ all -> 0x0031 }
        r2 = r8.mQuietDrawable;	 Catch:{ all -> 0x0031 }
        if (r2 == 0) goto L_0x002f;
    L_0x0024:
        r2 = r8.mFancyDrawable;	 Catch:{ all -> 0x0031 }
        r3 = r8.mQuietDrawable;	 Catch:{ all -> 0x0031 }
        r3 = r3.getColorFilter();	 Catch:{ all -> 0x0031 }
        r2.setColorFilter(r3);	 Catch:{ all -> 0x0031 }
    L_0x002f:
        monitor-exit(r0);	 Catch:{ all -> 0x0031 }
        return;
    L_0x0031:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0031 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.maml.AnimatingDrawable.prepareFancyDrawable():void");
    }

    public Drawable getFancyDrawable() {
        return this.mFancyDrawable;
    }

    public void clear() {
        synchronized (this.mLock) {
            this.mFancyDrawable = null;
        }
    }

    public void sendCommand(String command) {
        FancyDrawable fancyDrawable = this.mFancyDrawable;
        if (fancyDrawable != null) {
            fancyDrawable.getRoot().onCommand(command);
        }
    }

    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
    }

    /* Access modifiers changed, original: protected */
    public void drawIcon(Canvas canvas) {
        String str = TAG;
        try {
            int sa = canvas.save();
            canvas.translate((float) getBounds().left, (float) getBounds().top);
            canvas.scale(((float) this.mWidth) / ((float) this.mIntrinsicWidth), ((float) this.mHeight) / ((float) this.mIntrinsicHeight), 0.0f, 0.0f);
            this.mQuietDrawable.draw(canvas);
            canvas.restoreToCount(sa);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(str, e.toString());
        } catch (OutOfMemoryError e2) {
            e2.printStackTrace();
            Log.e(str, e2.toString());
        }
    }

    public int getOpacity() {
        return -3;
    }

    public void setAlpha(int alpha) {
        Drawable drawable = this.mQuietDrawable;
        if (drawable != null) {
            drawable.setAlpha(alpha);
        }
    }

    public void setColorFilter(ColorFilter cf) {
        Drawable drawable = this.mQuietDrawable;
        if (drawable != null) {
            drawable.setColorFilter(cf);
        }
        if (this.mBadgeDrawable != null) {
            this.mBadgeDrawable.setColorFilter(cf);
        }
    }
}
