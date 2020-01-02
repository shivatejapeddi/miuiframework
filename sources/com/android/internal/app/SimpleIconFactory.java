package com.android.internal.app;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.os.UserHandle;
import android.util.AttributeSet;
import android.util.Pools.SynchronizedPool;
import com.android.internal.R;
import org.xmlpull.v1.XmlPullParser;

@Deprecated
public class SimpleIconFactory {
    private static final int AMBIENT_SHADOW_ALPHA = 30;
    private static final float BLUR_FACTOR = 0.010416667f;
    private static final float CIRCLE_AREA_BY_RECT = 0.7853982f;
    private static final int DEFAULT_WRAPPER_BACKGROUND = -1;
    private static final int KEY_SHADOW_ALPHA = 61;
    private static final float KEY_SHADOW_DISTANCE = 0.020833334f;
    private static final float LINEAR_SCALE_SLOPE = 0.040449437f;
    private static final float MAX_CIRCLE_AREA_FACTOR = 0.6597222f;
    private static final float MAX_SQUARE_AREA_FACTOR = 0.6510417f;
    private static final int MIN_VISIBLE_ALPHA = 40;
    private static final float SCALE_NOT_INITIALIZED = 0.0f;
    private static final SynchronizedPool<SimpleIconFactory> sPool = new SynchronizedPool(Runtime.getRuntime().availableProcessors());
    private final Rect mAdaptiveIconBounds;
    private float mAdaptiveIconScale;
    private int mBadgeBitmapSize;
    private final Bitmap mBitmap;
    private Paint mBlurPaint = new Paint(3);
    private final Rect mBounds;
    private Canvas mCanvas;
    private Context mContext;
    private BlurMaskFilter mDefaultBlurMaskFilter;
    private Paint mDrawPaint = new Paint(3);
    private int mFillResIconDpi;
    private int mIconBitmapSize;
    private final float[] mLeftBorder;
    private final int mMaxSize;
    private final Rect mOldBounds = new Rect();
    private final byte[] mPixels;
    private PackageManager mPm;
    private final float[] mRightBorder;
    private final Canvas mScaleCheckCanvas;
    private int mWrapperBackgroundColor;
    private Drawable mWrapperIcon;

    public static class FixedScaleDrawable extends DrawableWrapper {
        private static final float LEGACY_ICON_SCALE = 0.46669f;
        private float mScaleX = LEGACY_ICON_SCALE;
        private float mScaleY = LEGACY_ICON_SCALE;

        public FixedScaleDrawable() {
            super(new ColorDrawable());
        }

        public void draw(Canvas canvas) {
            int saveCount = canvas.save();
            canvas.scale(this.mScaleX, this.mScaleY, getBounds().exactCenterX(), getBounds().exactCenterY());
            super.draw(canvas);
            canvas.restoreToCount(saveCount);
        }

        public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs) {
        }

        public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs, Theme theme) {
        }

        public void setScale(float scale) {
            float h = (float) getIntrinsicHeight();
            float w = (float) getIntrinsicWidth();
            this.mScaleX = scale * LEGACY_ICON_SCALE;
            this.mScaleY = LEGACY_ICON_SCALE * scale;
            if (h > w && w > 0.0f) {
                this.mScaleX *= w / h;
            } else if (w > h && h > 0.0f) {
                this.mScaleY *= h / w;
            }
        }
    }

    private static class FixedSizeBitmapDrawable extends BitmapDrawable {
        FixedSizeBitmapDrawable(Bitmap bitmap) {
            super(null, bitmap);
        }

        public int getIntrinsicHeight() {
            return getBitmap().getWidth();
        }

        public int getIntrinsicWidth() {
            return getBitmap().getWidth();
        }
    }

    @Deprecated
    public static SimpleIconFactory obtain(Context ctx) {
        SimpleIconFactory instance = (SimpleIconFactory) sPool.acquire();
        if (instance != null) {
            return instance;
        }
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        int iconDpi = am == null ? 0 : am.getLauncherLargeIconDensity();
        Resources r = ctx.getResources();
        instance = new SimpleIconFactory(ctx, iconDpi, r.getDimensionPixelSize(R.dimen.resolver_icon_size), r.getDimensionPixelSize(R.dimen.resolver_badge_size));
        instance.setWrapperBackgroundColor(-1);
        return instance;
    }

    @Deprecated
    public void recycle() {
        setWrapperBackgroundColor(-1);
        sPool.release(this);
    }

    @Deprecated
    private SimpleIconFactory(Context context, int fillResIconDpi, int iconBitmapSize, int badgeBitmapSize) {
        this.mContext = context.getApplicationContext();
        this.mPm = this.mContext.getPackageManager();
        this.mIconBitmapSize = iconBitmapSize;
        this.mBadgeBitmapSize = badgeBitmapSize;
        this.mFillResIconDpi = fillResIconDpi;
        this.mCanvas = new Canvas();
        this.mCanvas.setDrawFilter(new PaintFlagsDrawFilter(4, 2));
        this.mMaxSize = iconBitmapSize * 2;
        int i = this.mMaxSize;
        this.mBitmap = Bitmap.createBitmap(i, i, Config.ALPHA_8);
        this.mScaleCheckCanvas = new Canvas(this.mBitmap);
        i = this.mMaxSize;
        this.mPixels = new byte[(i * i)];
        this.mLeftBorder = new float[i];
        this.mRightBorder = new float[i];
        this.mBounds = new Rect();
        this.mAdaptiveIconBounds = new Rect();
        this.mAdaptiveIconScale = 0.0f;
        this.mDefaultBlurMaskFilter = new BlurMaskFilter(((float) iconBitmapSize) * BLUR_FACTOR, Blur.NORMAL);
    }

    /* Access modifiers changed, original: 0000 */
    @Deprecated
    public void setWrapperBackgroundColor(int color) {
        this.mWrapperBackgroundColor = Color.alpha(color) < 255 ? -1 : color;
    }

    /* Access modifiers changed, original: 0000 */
    @Deprecated
    public Bitmap createUserBadgedIconBitmap(Drawable icon, UserHandle user) {
        float[] scale = new float[1];
        if (icon == null) {
            icon = getFullResDefaultActivityIcon(this.mFillResIconDpi);
        }
        icon = normalizeAndWrapToAdaptiveIcon(icon, null, scale);
        Bitmap bitmap = createIconBitmap(icon, scale[0]);
        if (icon instanceof AdaptiveIconDrawable) {
            this.mCanvas.setBitmap(bitmap);
            recreateIcon(Bitmap.createBitmap(bitmap), this.mCanvas);
            this.mCanvas.setBitmap(null);
        }
        if (user == null) {
            return bitmap;
        }
        Drawable badged = this.mPm.getUserBadgedIcon(new FixedSizeBitmapDrawable(bitmap), user);
        if (badged instanceof BitmapDrawable) {
            return ((BitmapDrawable) badged).getBitmap();
        }
        return createIconBitmap(badged, 1.0f);
    }

    /* Access modifiers changed, original: 0000 */
    @Deprecated
    public Bitmap createAppBadgedIconBitmap(Drawable icon, Bitmap renderedAppIcon) {
        if (icon == null) {
            icon = getFullResDefaultActivityIcon(this.mFillResIconDpi);
        }
        int w = icon.getIntrinsicWidth();
        int h = icon.getIntrinsicHeight();
        float scale = 1.0f;
        if (h > w && w > 0) {
            scale = ((float) h) / ((float) w);
        } else if (w > h && h > 0) {
            scale = ((float) w) / ((float) h);
        }
        icon = new BitmapDrawable(this.mContext.getResources(), maskBitmapToCircle(createIconBitmap(icon, scale)));
        Bitmap bitmap = createIconBitmap(icon, getScale(icon, null));
        this.mCanvas.setBitmap(bitmap);
        recreateIcon(Bitmap.createBitmap(bitmap), this.mCanvas);
        if (renderedAppIcon != null) {
            int i = this.mBadgeBitmapSize;
            renderedAppIcon = Bitmap.createScaledBitmap(renderedAppIcon, i, i, false);
            Canvas canvas = this.mCanvas;
            int i2 = this.mIconBitmapSize;
            int i3 = this.mBadgeBitmapSize;
            canvas.drawBitmap(renderedAppIcon, (float) (i2 - i3), (float) (i2 - i3), null);
        }
        this.mCanvas.setBitmap(null);
        return bitmap;
    }

    private Bitmap maskBitmapToCircle(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(-1);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(((float) bitmap.getWidth()) / 2.0f, ((float) bitmap.getHeight()) / 2.0f, (((float) bitmap.getWidth()) / 2.0f) - 1.0f, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    private static Drawable getFullResDefaultActivityIcon(int iconDpi) {
        return Resources.getSystem().getDrawableForDensity(17629184, iconDpi);
    }

    private Bitmap createIconBitmap(Drawable icon, float scale) {
        return createIconBitmap(icon, scale, this.mIconBitmapSize);
    }

    private Bitmap createIconBitmap(Drawable icon, float scale, int size) {
        Bitmap bitmap = Bitmap.createBitmap(size, size, Config.ARGB_8888);
        this.mCanvas.setBitmap(bitmap);
        this.mOldBounds.set(icon.getBounds());
        int offset;
        if (icon instanceof AdaptiveIconDrawable) {
            offset = Math.max((int) Math.ceil((double) (((float) size) * BLUR_FACTOR)), Math.round((((float) size) * (1.0f - scale)) / 2.0f));
            icon.setBounds(offset, offset, size - offset, size - offset);
            icon.draw(this.mCanvas);
        } else {
            if (icon instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) icon;
                Bitmap b = bitmapDrawable.getBitmap();
                if (bitmap != null && b.getDensity() == 0) {
                    bitmapDrawable.setTargetDensity(this.mContext.getResources().getDisplayMetrics());
                }
            }
            offset = size;
            int height = size;
            int intrinsicWidth = icon.getIntrinsicWidth();
            int intrinsicHeight = icon.getIntrinsicHeight();
            if (intrinsicWidth > 0 && intrinsicHeight > 0) {
                float ratio = ((float) intrinsicWidth) / ((float) intrinsicHeight);
                if (intrinsicWidth > intrinsicHeight) {
                    height = (int) (((float) offset) / ratio);
                } else if (intrinsicHeight > intrinsicWidth) {
                    offset = (int) (((float) height) * ratio);
                }
            }
            int left = (size - offset) / 2;
            int top = (size - height) / 2;
            icon.setBounds(left, top, left + offset, top + height);
            this.mCanvas.save();
            this.mCanvas.scale(scale, scale, (float) (size / 2), (float) (size / 2));
            icon.draw(this.mCanvas);
            this.mCanvas.restore();
        }
        icon.setBounds(this.mOldBounds);
        this.mCanvas.setBitmap(null);
        return bitmap;
    }

    private Drawable normalizeAndWrapToAdaptiveIcon(Drawable icon, RectF outIconBounds, float[] outScale) {
        if (this.mWrapperIcon == null) {
            this.mWrapperIcon = this.mContext.getDrawable(R.drawable.iconfactory_adaptive_icon_drawable_wrapper).mutate();
        }
        Drawable dr = this.mWrapperIcon;
        dr.setBounds(0, 0, 1, 1);
        float scale = getScale(icon, outIconBounds);
        if (!(icon instanceof AdaptiveIconDrawable)) {
            FixedScaleDrawable fsd = (FixedScaleDrawable) dr.getForeground();
            fsd.setDrawable(icon);
            fsd.setScale(scale);
            icon = dr;
            scale = getScale(icon, outIconBounds);
            ((ColorDrawable) dr.getBackground()).setColor(this.mWrapperBackgroundColor);
        }
        outScale[0] = scale;
        return icon;
    }

    /* JADX WARNING: Removed duplicated region for block: B:39:0x0084  */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x01a9  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x00db  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0084  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x00db  */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x01a9  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x004f  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0084  */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x01a9  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x00db  */
    /* JADX WARNING: Missing block: B:84:0x01a8, code skipped:
            return r7;
     */
    /* JADX WARNING: Missing block: B:88:0x01b2, code skipped:
            return 1.0f;
     */
    private synchronized float getScale(android.graphics.drawable.Drawable r24, android.graphics.RectF r25) {
        /*
        r23 = this;
        r1 = r23;
        r0 = r24;
        r2 = r25;
        monitor-enter(r23);
        r3 = r0 instanceof android.graphics.drawable.AdaptiveIconDrawable;	 Catch:{ all -> 0x01b3 }
        r4 = 0;
        if (r3 == 0) goto L_0x001d;
    L_0x000c:
        r3 = r1.mAdaptiveIconScale;	 Catch:{ all -> 0x01b3 }
        r3 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1));
        if (r3 == 0) goto L_0x001d;
    L_0x0012:
        if (r2 == 0) goto L_0x0019;
    L_0x0014:
        r3 = r1.mAdaptiveIconBounds;	 Catch:{ all -> 0x01b3 }
        r2.set(r3);	 Catch:{ all -> 0x01b3 }
    L_0x0019:
        r3 = r1.mAdaptiveIconScale;	 Catch:{ all -> 0x01b3 }
        monitor-exit(r23);
        return r3;
    L_0x001d:
        r3 = r24.getIntrinsicWidth();	 Catch:{ all -> 0x01b3 }
        r5 = r24.getIntrinsicHeight();	 Catch:{ all -> 0x01b3 }
        if (r3 <= 0) goto L_0x0041;
    L_0x0027:
        if (r5 > 0) goto L_0x002a;
    L_0x0029:
        goto L_0x0041;
    L_0x002a:
        r6 = r1.mMaxSize;	 Catch:{ all -> 0x01b3 }
        if (r3 > r6) goto L_0x0032;
    L_0x002e:
        r6 = r1.mMaxSize;	 Catch:{ all -> 0x01b3 }
        if (r5 <= r6) goto L_0x0059;
    L_0x0032:
        r6 = java.lang.Math.max(r3, r5);	 Catch:{ all -> 0x01b3 }
        r7 = r1.mMaxSize;	 Catch:{ all -> 0x01b3 }
        r7 = r7 * r3;
        r7 = r7 / r6;
        r3 = r7;
        r7 = r1.mMaxSize;	 Catch:{ all -> 0x01b3 }
        r7 = r7 * r5;
        r7 = r7 / r6;
        r5 = r7;
        goto L_0x0059;
    L_0x0041:
        if (r3 <= 0) goto L_0x004a;
    L_0x0043:
        r6 = r1.mMaxSize;	 Catch:{ all -> 0x01b3 }
        if (r3 <= r6) goto L_0x0048;
    L_0x0047:
        goto L_0x004a;
    L_0x0048:
        r6 = r3;
        goto L_0x004c;
    L_0x004a:
        r6 = r1.mMaxSize;	 Catch:{ all -> 0x01b3 }
    L_0x004c:
        r3 = r6;
        if (r5 <= 0) goto L_0x0056;
    L_0x004f:
        r6 = r1.mMaxSize;	 Catch:{ all -> 0x01b3 }
        if (r5 <= r6) goto L_0x0054;
    L_0x0053:
        goto L_0x0056;
    L_0x0054:
        r6 = r5;
        goto L_0x0058;
    L_0x0056:
        r6 = r1.mMaxSize;	 Catch:{ all -> 0x01b3 }
    L_0x0058:
        r5 = r6;
    L_0x0059:
        r6 = r1.mBitmap;	 Catch:{ all -> 0x01b3 }
        r7 = 0;
        r6.eraseColor(r7);	 Catch:{ all -> 0x01b3 }
        r0.setBounds(r7, r7, r3, r5);	 Catch:{ all -> 0x01b3 }
        r6 = r1.mScaleCheckCanvas;	 Catch:{ all -> 0x01b3 }
        r0.draw(r6);	 Catch:{ all -> 0x01b3 }
        r6 = r1.mPixels;	 Catch:{ all -> 0x01b3 }
        r6 = java.nio.ByteBuffer.wrap(r6);	 Catch:{ all -> 0x01b3 }
        r6.rewind();	 Catch:{ all -> 0x01b3 }
        r7 = r1.mBitmap;	 Catch:{ all -> 0x01b3 }
        r7.copyPixelsToBuffer(r6);	 Catch:{ all -> 0x01b3 }
        r7 = -1;
        r8 = -1;
        r9 = r1.mMaxSize;	 Catch:{ all -> 0x01b3 }
        r10 = 1;
        r9 = r9 + r10;
        r11 = -1;
        r12 = 0;
        r13 = r1.mMaxSize;	 Catch:{ all -> 0x01b3 }
        r13 = r13 - r3;
        r14 = 0;
    L_0x0081:
        r15 = -1;
        if (r14 >= r5) goto L_0x00d4;
    L_0x0084:
        r16 = r15;
        r17 = r15;
        r18 = 0;
        r10 = r16;
        r4 = r17;
        r16 = r12;
        r12 = r18;
    L_0x0092:
        if (r12 >= r3) goto L_0x00ae;
    L_0x0094:
        r15 = r1.mPixels;	 Catch:{ all -> 0x01b3 }
        r15 = r15[r16];	 Catch:{ all -> 0x01b3 }
        r15 = r15 & 255;
        r20 = r6;
        r6 = 40;
        if (r15 <= r6) goto L_0x00a6;
    L_0x00a0:
        r6 = -1;
        if (r4 != r6) goto L_0x00a4;
    L_0x00a3:
        r4 = r12;
    L_0x00a4:
        r6 = r12;
        r10 = r6;
    L_0x00a6:
        r16 = r16 + 1;
        r12 = r12 + 1;
        r6 = r20;
        r15 = -1;
        goto L_0x0092;
    L_0x00ae:
        r20 = r6;
        r12 = r16 + r13;
        r6 = r1.mLeftBorder;	 Catch:{ all -> 0x01b3 }
        r15 = (float) r4;	 Catch:{ all -> 0x01b3 }
        r6[r14] = r15;	 Catch:{ all -> 0x01b3 }
        r6 = r1.mRightBorder;	 Catch:{ all -> 0x01b3 }
        r15 = (float) r10;	 Catch:{ all -> 0x01b3 }
        r6[r14] = r15;	 Catch:{ all -> 0x01b3 }
        r6 = -1;
        if (r4 == r6) goto L_0x00cd;
    L_0x00bf:
        r8 = r14;
        if (r7 != r6) goto L_0x00c3;
    L_0x00c2:
        r7 = r14;
    L_0x00c3:
        r6 = java.lang.Math.min(r9, r4);	 Catch:{ all -> 0x01b3 }
        r9 = java.lang.Math.max(r11, r10);	 Catch:{ all -> 0x01b3 }
        r11 = r9;
        r9 = r6;
    L_0x00cd:
        r14 = r14 + 1;
        r6 = r20;
        r4 = 0;
        r10 = 1;
        goto L_0x0081;
    L_0x00d4:
        r20 = r6;
        r4 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = -1;
        if (r7 == r6) goto L_0x01a9;
    L_0x00db:
        if (r11 != r6) goto L_0x00e5;
    L_0x00dd:
        r18 = r7;
        r19 = r8;
        r21 = r9;
        goto L_0x01af;
    L_0x00e5:
        r6 = r1.mLeftBorder;	 Catch:{ all -> 0x01b3 }
        r10 = 1;
        convertToConvexArray(r6, r10, r7, r8);	 Catch:{ all -> 0x01b3 }
        r6 = r1.mRightBorder;	 Catch:{ all -> 0x01b3 }
        r10 = -1;
        convertToConvexArray(r6, r10, r7, r8);	 Catch:{ all -> 0x01b3 }
        r6 = 0;
        r10 = 0;
    L_0x00f3:
        if (r10 >= r5) goto L_0x010e;
    L_0x00f5:
        r14 = r1.mLeftBorder;	 Catch:{ all -> 0x01b3 }
        r14 = r14[r10];	 Catch:{ all -> 0x01b3 }
        r15 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r14 = (r14 > r15 ? 1 : (r14 == r15 ? 0 : -1));
        if (r14 > 0) goto L_0x0100;
    L_0x00ff:
        goto L_0x010b;
    L_0x0100:
        r14 = r1.mRightBorder;	 Catch:{ all -> 0x01b3 }
        r14 = r14[r10];	 Catch:{ all -> 0x01b3 }
        r15 = r1.mLeftBorder;	 Catch:{ all -> 0x01b3 }
        r15 = r15[r10];	 Catch:{ all -> 0x01b3 }
        r14 = r14 - r15;
        r14 = r14 + r4;
        r6 = r6 + r14;
    L_0x010b:
        r10 = r10 + 1;
        goto L_0x00f3;
    L_0x010e:
        r10 = r8 + 1;
        r10 = r10 - r7;
        r14 = r11 + 1;
        r14 = r14 - r9;
        r10 = r10 * r14;
        r10 = (float) r10;	 Catch:{ all -> 0x01b3 }
        r14 = r6 / r10;
        r15 = 1061752795; // 0x3f490fdb float:0.7853982 double:5.245755804E-315;
        r15 = (r14 > r15 ? 1 : (r14 == r15 ? 0 : -1));
        if (r15 >= 0) goto L_0x0123;
    L_0x011f:
        r15 = 1059644302; // 0x3f28e38e float:0.6597222 double:5.235338464E-315;
        goto L_0x0131;
    L_0x0123:
        r15 = 1059498667; // 0x3f26aaab float:0.6510417 double:5.23461893E-315;
        r16 = 1025879631; // 0x3d25ae4f float:0.040449437 double:5.068518824E-315;
        r18 = r4 - r14;
        r18 = r18 * r16;
        r18 = r18 + r15;
        r15 = r18;
    L_0x0131:
        r4 = r1.mBounds;	 Catch:{ all -> 0x01b3 }
        r4.left = r9;	 Catch:{ all -> 0x01b3 }
        r4 = r1.mBounds;	 Catch:{ all -> 0x01b3 }
        r4.right = r11;	 Catch:{ all -> 0x01b3 }
        r4 = r1.mBounds;	 Catch:{ all -> 0x01b3 }
        r4.top = r7;	 Catch:{ all -> 0x01b3 }
        r4 = r1.mBounds;	 Catch:{ all -> 0x01b3 }
        r4.bottom = r8;	 Catch:{ all -> 0x01b3 }
        if (r2 == 0) goto L_0x0173;
    L_0x0143:
        r4 = r1.mBounds;	 Catch:{ all -> 0x01b3 }
        r4 = r4.left;	 Catch:{ all -> 0x01b3 }
        r4 = (float) r4;	 Catch:{ all -> 0x01b3 }
        r18 = r7;
        r7 = (float) r3;	 Catch:{ all -> 0x01b3 }
        r4 = r4 / r7;
        r7 = r1.mBounds;	 Catch:{ all -> 0x01b3 }
        r7 = r7.top;	 Catch:{ all -> 0x01b3 }
        r7 = (float) r7;	 Catch:{ all -> 0x01b3 }
        r19 = r8;
        r8 = (float) r5;	 Catch:{ all -> 0x01b3 }
        r7 = r7 / r8;
        r8 = r1.mBounds;	 Catch:{ all -> 0x01b3 }
        r8 = r8.right;	 Catch:{ all -> 0x01b3 }
        r8 = (float) r8;	 Catch:{ all -> 0x01b3 }
        r21 = r9;
        r9 = (float) r3;	 Catch:{ all -> 0x01b3 }
        r8 = r8 / r9;
        r9 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r8 = r9 - r8;
        r9 = r1.mBounds;	 Catch:{ all -> 0x01b3 }
        r9 = r9.bottom;	 Catch:{ all -> 0x01b3 }
        r9 = (float) r9;	 Catch:{ all -> 0x01b3 }
        r22 = r10;
        r10 = (float) r5;	 Catch:{ all -> 0x01b3 }
        r9 = r9 / r10;
        r10 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r9 = r10 - r9;
        r2.set(r4, r7, r8, r9);	 Catch:{ all -> 0x01b3 }
        goto L_0x017b;
    L_0x0173:
        r18 = r7;
        r19 = r8;
        r21 = r9;
        r22 = r10;
    L_0x017b:
        r4 = r3 * r5;
        r4 = (float) r4;	 Catch:{ all -> 0x01b3 }
        r4 = r6 / r4;
        r7 = (r4 > r15 ? 1 : (r4 == r15 ? 0 : -1));
        if (r7 <= 0) goto L_0x018f;
    L_0x0184:
        r7 = r15 / r4;
        r7 = (double) r7;	 Catch:{ all -> 0x01b3 }
        r7 = java.lang.Math.sqrt(r7);	 Catch:{ all -> 0x01b3 }
        r7 = (float) r7;	 Catch:{ all -> 0x01b3 }
        r16 = r7;
        goto L_0x0191;
    L_0x018f:
        r16 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
    L_0x0191:
        r7 = r16;
        r8 = r0 instanceof android.graphics.drawable.AdaptiveIconDrawable;	 Catch:{ all -> 0x01b3 }
        if (r8 == 0) goto L_0x01a7;
    L_0x0197:
        r8 = r1.mAdaptiveIconScale;	 Catch:{ all -> 0x01b3 }
        r9 = 0;
        r8 = (r8 > r9 ? 1 : (r8 == r9 ? 0 : -1));
        if (r8 != 0) goto L_0x01a7;
    L_0x019e:
        r1.mAdaptiveIconScale = r7;	 Catch:{ all -> 0x01b3 }
        r8 = r1.mAdaptiveIconBounds;	 Catch:{ all -> 0x01b3 }
        r9 = r1.mBounds;	 Catch:{ all -> 0x01b3 }
        r8.set(r9);	 Catch:{ all -> 0x01b3 }
    L_0x01a7:
        monitor-exit(r23);
        return r7;
    L_0x01a9:
        r18 = r7;
        r19 = r8;
        r21 = r9;
    L_0x01af:
        monitor-exit(r23);
        r4 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        return r4;
    L_0x01b3:
        r0 = move-exception;
        monitor-exit(r23);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.app.SimpleIconFactory.getScale(android.graphics.drawable.Drawable, android.graphics.RectF):float");
    }

    private static void convertToConvexArray(float[] xCoordinates, int direction, int topY, int bottomY) {
        float[] angles = new float[(xCoordinates.length - 1)];
        int first = topY;
        int last = -1;
        float lastAngle = Float.MAX_VALUE;
        for (int i = topY + 1; i <= bottomY; i++) {
            if (xCoordinates[i] > -1.0f) {
                int start;
                if (lastAngle == Float.MAX_VALUE) {
                    start = first;
                } else {
                    start = (xCoordinates[i] - xCoordinates[last]) / ((float) (i - last));
                    int start2 = last;
                    if ((start - lastAngle) * ((float) direction) < 0.0f) {
                        int i2 = start2;
                        start2 = start;
                        start = i2;
                        while (start > first) {
                            start--;
                            if ((((xCoordinates[i] - xCoordinates[start]) / ((float) (i - start))) - angles[start]) * ((float) direction) >= 0.0f) {
                                break;
                            }
                        }
                    }
                    start = start2;
                }
                float lastAngle2 = (xCoordinates[i] - xCoordinates[start]) / ((float) (i - start));
                for (int j = start; j < i; j++) {
                    angles[j] = lastAngle2;
                    xCoordinates[j] = xCoordinates[start] + (((float) (j - start)) * lastAngle2);
                }
                last = i;
                lastAngle = lastAngle2;
            }
        }
    }

    private synchronized void recreateIcon(Bitmap icon, Canvas out) {
        recreateIcon(icon, this.mDefaultBlurMaskFilter, 30, 61, out);
    }

    private synchronized void recreateIcon(Bitmap icon, BlurMaskFilter blurMaskFilter, int ambientAlpha, int keyAlpha, Canvas out) {
        int[] offset = new int[2];
        this.mBlurPaint.setMaskFilter(blurMaskFilter);
        Bitmap shadow = icon.extractAlpha(this.mBlurPaint, offset);
        this.mDrawPaint.setAlpha(ambientAlpha);
        out.drawBitmap(shadow, (float) offset[0], (float) offset[1], this.mDrawPaint);
        this.mDrawPaint.setAlpha(keyAlpha);
        out.drawBitmap(shadow, (float) offset[0], ((float) offset[1]) + (((float) this.mIconBitmapSize) * KEY_SHADOW_DISTANCE), this.mDrawPaint);
        this.mDrawPaint.setAlpha(255);
        out.drawBitmap(icon, 0.0f, 0.0f, this.mDrawPaint);
    }
}
