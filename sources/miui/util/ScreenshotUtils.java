package miui.util;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.miui.R;
import android.os.FileUtils;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.provider.MiuiSettings.Global;
import android.view.Display;
import android.view.WindowManager;
import java.io.File;
import java.lang.ref.SoftReference;
import miui.graphics.BitmapFactory;
import miui.os.Build;

public class ScreenshotUtils {
    private static final File ACTIVITY_SCREENSHOT_FOLDER = new File(ACTIVITY_SCREENSHOT_FOLDER_PATH);
    private static final String ACTIVITY_SCREENSHOT_FOLDER_PATH = "/data/system/app_screenshot";
    public static final float BLUR_SCALE_RATIO = 0.33333334f;
    public static final int DEFAULT_SCREENSHOT_COLOR = -1426063361;
    public static final int DEFAULT_SCREEN_BLUR_RADIUS = Resources.getSystem().getDimensionPixelSize(R.dimen.window_background_blur_radius);
    public static final float REAL_BLUR_BLACK = (((float) SystemProperties.getInt("persist.sys.real_blur_black", 0)) / 100.0f);
    public static final int REAL_BLUR_MINIFY = SystemProperties.getInt("persist.sys.real_blur_minify", 4);
    public static final int REAL_BLUR_RADIUS = SystemProperties.getInt("persist.sys.real_blur_radius", 8);
    private static final String TAG = "ScreenshotUtils";
    private static SoftReference<Bitmap> sCacheBitmap;
    private static SoftReference<Bitmap> sCacheBitmapWithNavigationBarHide;
    private static SoftReference<Bitmap> sCacheBitmapWithNavigationBarShow;
    private static Display sDisplay;
    private static Handler sHandler;
    private static HandlerThread sHandlerThread;
    private static KeyguardManager sKeyguardManager;
    private static Paint sPaint;
    private static int sScreenHeight;
    private static int sScreenWidth;
    private static Point sSizeBuf = new Point();

    private static void initializeIfNeed(Context context) {
        if (!ACTIVITY_SCREENSHOT_FOLDER.exists() && ACTIVITY_SCREENSHOT_FOLDER.mkdir()) {
            FileUtils.setPermissions(ACTIVITY_SCREENSHOT_FOLDER.getAbsolutePath(), 509, -1, -1);
        }
        if (sDisplay == null) {
            sDisplay = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        }
        if (sKeyguardManager == null) {
            sKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        }
        int degree = sDisplay;
        if (degree != 0) {
            degree = degree.getRotation();
            boolean isPortrait = degree == 0 || degree == 2;
            CustomizeUtil.getRealSize(sDisplay, sSizeBuf);
            Point point = sSizeBuf;
            sScreenWidth = isPortrait ? point.x : point.y;
            point = sSizeBuf;
            sScreenHeight = isPortrait ? point.y : point.x;
        }
    }

    public static void getActivityScreenshotSize(Context context, Point size) {
        initializeIfNeed(context);
        sDisplay.getRealSize(size);
        float scale = Resources.getSystem().getFloat(R.dimen.config_screenshot_scale);
        size.x = (int) ((((float) size.x) * scale) + 0.5f);
        size.y = (int) ((((float) size.y) * scale) + 0.5f);
    }

    public static boolean disallowTaskManagerScreenshotMode(Context context) {
        if (MiuiFeatureUtils.isLiteMode()) {
            return FeatureParser.getBoolean("enable_miui_lite", false);
        }
        return false;
    }

    public static void captureActivityScreenshot(Context context, String shortComponentName) {
        captureActivityScreenshot(context, shortComponentName, Boolean.valueOf(true));
    }

    public static void captureActivityScreenshot(Context context, final String shortComponentName, Boolean isAsync) {
        initializeIfNeed(context);
        if (!sKeyguardManager.isKeyguardLocked()) {
            boolean isPort = true;
            if ((!disallowTaskManagerScreenshotMode(context) && SystemProperties.getBoolean("persist.sys.screenshot_mode", false)) || (!getActivityScreenshotFile(shortComponentName, true).exists() && !getActivityScreenshotFile(shortComponentName, false).exists())) {
                final int rotation = sDisplay.getRotation();
                if (!(rotation == 0 || rotation == 2)) {
                    isPort = false;
                }
                if (sHandler == null) {
                    synchronized (ScreenshotUtils.class) {
                        if (sHandler == null) {
                            sHandlerThread = new HandlerThread(TAG);
                            sHandlerThread.start();
                            sHandler = new Handler(sHandlerThread.getLooper());
                        }
                    }
                }
                boolean hasNavigationBar = false;
                try {
                    hasNavigationBar = CompatibilityHelper.hasNavigationBar(sDisplay.getDisplayId());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                if (isAsync.booleanValue()) {
                    final boolean finalHasNavigationBar = hasNavigationBar;
                    sHandler.post(new Runnable() {
                        public void run() {
                            ScreenshotUtils.screenShotAndSave(shortComponentName, rotation, isPort, finalHasNavigationBar);
                        }
                    });
                } else {
                    screenShotAndSave(shortComponentName, rotation, isPort, hasNavigationBar);
                }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:50:0x00eb A:{Catch:{ Exception -> 0x013b }} */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00d0 A:{Catch:{ Exception -> 0x013b }} */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x0109 A:{Catch:{ Exception -> 0x013b }} */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x0138  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x0114 A:{Catch:{ Exception -> 0x013b }} */
    private static void screenShotAndSave(java.lang.String r19, int r20, boolean r21, boolean r22) {
        /*
        r1 = r19;
        r2 = r20;
        r3 = r21;
        r4 = r22;
        r5 = "ScreenshotUtils";
        r0 = 30000; // 0x7530 float:4.2039E-41 double:1.4822E-319;
        r6 = sScreenWidth;	 Catch:{ Exception -> 0x013b }
        r7 = sScreenHeight;	 Catch:{ Exception -> 0x013b }
        r8 = 0;
        r6 = miui.util.CompatibilityHelper.screenshot(r6, r7, r8, r0);	 Catch:{ Exception -> 0x013b }
        r7 = new android.graphics.Point;	 Catch:{ Exception -> 0x013b }
        r7.<init>();	 Catch:{ Exception -> 0x013b }
        r9 = sDisplay;	 Catch:{ Exception -> 0x013b }
        r9.getSize(r7);	 Catch:{ Exception -> 0x013b }
        if (r3 == 0) goto L_0x0024;
    L_0x0021:
        r9 = r7.x;	 Catch:{ Exception -> 0x013b }
        goto L_0x0026;
    L_0x0024:
        r9 = r7.y;	 Catch:{ Exception -> 0x013b }
    L_0x0026:
        if (r3 == 0) goto L_0x002b;
    L_0x0028:
        r10 = r7.y;	 Catch:{ Exception -> 0x013b }
        goto L_0x002d;
    L_0x002b:
        r10 = r7.x;	 Catch:{ Exception -> 0x013b }
    L_0x002d:
        r11 = android.content.res.Resources.getSystem();	 Catch:{ Exception -> 0x013b }
        r12 = 285605914; // 0x1106001a float:1.0570767E-28 double:1.411080704E-315;
        r11 = r11.getFloat(r12);	 Catch:{ Exception -> 0x013b }
        r12 = 2;
        r13 = 3;
        if (r4 == 0) goto L_0x0052;
    L_0x003c:
        if (r2 == r13) goto L_0x0047;
    L_0x003e:
        if (r2 != r12) goto L_0x0041;
    L_0x0040:
        goto L_0x0047;
    L_0x0041:
        r14 = new android.graphics.Rect;	 Catch:{ Exception -> 0x013b }
        r14.<init>(r8, r8, r9, r10);	 Catch:{ Exception -> 0x013b }
        goto L_0x005c;
    L_0x0047:
        r14 = new android.graphics.Rect;	 Catch:{ Exception -> 0x013b }
        r15 = sScreenHeight;	 Catch:{ Exception -> 0x013b }
        r15 = r15 - r10;
        r12 = sScreenHeight;	 Catch:{ Exception -> 0x013b }
        r14.<init>(r8, r15, r9, r12);	 Catch:{ Exception -> 0x013b }
        goto L_0x005c;
    L_0x0052:
        r12 = new android.graphics.Rect;	 Catch:{ Exception -> 0x013b }
        r14 = sScreenWidth;	 Catch:{ Exception -> 0x013b }
        r15 = sScreenHeight;	 Catch:{ Exception -> 0x013b }
        r12.<init>(r8, r8, r14, r15);	 Catch:{ Exception -> 0x013b }
        r14 = r12;
    L_0x005c:
        r12 = getCacheBitmap(r22);	 Catch:{ Exception -> 0x013b }
        if (r12 != 0) goto L_0x0064;
    L_0x0062:
        r12 = 0;
        goto L_0x006e;
    L_0x0064:
        r12 = getCacheBitmap(r22);	 Catch:{ Exception -> 0x013b }
        r12 = r12.get();	 Catch:{ Exception -> 0x013b }
        r12 = (android.graphics.Bitmap) r12;	 Catch:{ Exception -> 0x013b }
    L_0x006e:
        if (r12 != 0) goto L_0x0099;
    L_0x0070:
        if (r4 == 0) goto L_0x0074;
    L_0x0072:
        r15 = r9;
        goto L_0x0076;
    L_0x0074:
        r15 = sScreenWidth;	 Catch:{ Exception -> 0x013b }
    L_0x0076:
        r15 = (float) r15;	 Catch:{ Exception -> 0x013b }
        r15 = r15 * r11;
        r17 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r15 = r15 + r17;
        r15 = (int) r15;	 Catch:{ Exception -> 0x013b }
        if (r4 == 0) goto L_0x0081;
    L_0x007f:
        r8 = r10;
        goto L_0x0085;
    L_0x0081:
        r18 = sScreenHeight;	 Catch:{ Exception -> 0x013b }
        r8 = r18;
    L_0x0085:
        r8 = (float) r8;	 Catch:{ Exception -> 0x013b }
        r8 = r8 * r11;
        r8 = r8 + r17;
        r8 = (int) r8;	 Catch:{ Exception -> 0x013b }
        r13 = android.graphics.Bitmap.Config.ARGB_8888;	 Catch:{ Exception -> 0x013b }
        r8 = android.graphics.Bitmap.createBitmap(r15, r8, r13);	 Catch:{ Exception -> 0x013b }
        r12 = r8;
        r8 = new java.lang.ref.SoftReference;	 Catch:{ Exception -> 0x013b }
        r8.<init>(r12);	 Catch:{ Exception -> 0x013b }
        setCacheBitmap(r8, r4);	 Catch:{ Exception -> 0x013b }
    L_0x0099:
        r8 = sPaint;	 Catch:{ Exception -> 0x013b }
        if (r8 != 0) goto L_0x00a5;
    L_0x009d:
        r8 = new android.graphics.Paint;	 Catch:{ Exception -> 0x013b }
        r13 = 3;
        r8.<init>(r13);	 Catch:{ Exception -> 0x013b }
        sPaint = r8;	 Catch:{ Exception -> 0x013b }
    L_0x00a5:
        r8 = new android.graphics.Canvas;	 Catch:{ Exception -> 0x013b }
        r8.<init>(r12);	 Catch:{ Exception -> 0x013b }
        r13 = 1;
        if (r2 == r13) goto L_0x00b4;
    L_0x00ad:
        r15 = 2;
        if (r2 != r15) goto L_0x00b1;
    L_0x00b0:
        goto L_0x00b4;
    L_0x00b1:
        r17 = r0;
        goto L_0x00cb;
    L_0x00b4:
        r13 = r12.getWidth();	 Catch:{ Exception -> 0x013b }
        r13 = (float) r13;	 Catch:{ Exception -> 0x013b }
        r17 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r13 = r13 / r17;
        r15 = r12.getHeight();	 Catch:{ Exception -> 0x013b }
        r15 = (float) r15;	 Catch:{ Exception -> 0x013b }
        r15 = r15 / r17;
        r17 = r0;
        r0 = 1127481344; // 0x43340000 float:180.0 double:5.570497984E-315;
        r8.rotate(r0, r13, r15);	 Catch:{ Exception -> 0x013b }
    L_0x00cb:
        r8.scale(r11, r11);	 Catch:{ Exception -> 0x013b }
        if (r6 == 0) goto L_0x00eb;
    L_0x00d0:
        r0 = new android.graphics.Rect;	 Catch:{ Exception -> 0x013b }
        if (r4 == 0) goto L_0x00d6;
    L_0x00d4:
        r13 = r9;
        goto L_0x00d8;
    L_0x00d6:
        r13 = sScreenWidth;	 Catch:{ Exception -> 0x013b }
    L_0x00d8:
        if (r4 == 0) goto L_0x00dc;
    L_0x00da:
        r15 = r10;
        goto L_0x00de;
    L_0x00dc:
        r15 = sScreenHeight;	 Catch:{ Exception -> 0x013b }
    L_0x00de:
        r2 = 0;
        r0.<init>(r2, r2, r13, r15);	 Catch:{ Exception -> 0x013b }
        r13 = sPaint;	 Catch:{ Exception -> 0x013b }
        r8.drawBitmap(r6, r14, r0, r13);	 Catch:{ Exception -> 0x013b }
        r6.recycle();	 Catch:{ Exception -> 0x013b }
        goto L_0x00f4;
    L_0x00eb:
        r2 = 0;
        r0 = -1426063361; // 0xffffffffaaffffff float:-4.5474732E-13 double:NaN;
        r13 = android.graphics.PorterDuff.Mode.SRC;	 Catch:{ Exception -> 0x013b }
        r8.drawColor(r0, r13);	 Catch:{ Exception -> 0x013b }
    L_0x00f4:
        r0 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x013b }
        r13 = getActivityScreenshotFile(r1, r3);	 Catch:{ Exception -> 0x013b }
        r0.<init>(r13);	 Catch:{ Exception -> 0x013b }
        r13 = android.graphics.Bitmap.CompressFormat.JPEG;	 Catch:{ Exception -> 0x013b }
        r15 = 90;
        r12.compress(r13, r15, r0);	 Catch:{ Exception -> 0x013b }
        r0.close();	 Catch:{ Exception -> 0x013b }
        if (r3 != 0) goto L_0x010a;
    L_0x0109:
        r2 = 1;
    L_0x010a:
        r2 = getActivityScreenshotFile(r1, r2);	 Catch:{ Exception -> 0x013b }
        r13 = r2.exists();	 Catch:{ Exception -> 0x013b }
        if (r13 == 0) goto L_0x0138;
    L_0x0114:
        r13 = r2.delete();	 Catch:{ Exception -> 0x013b }
        if (r13 == 0) goto L_0x0135;
    L_0x011a:
        r15 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x013b }
        r15.<init>();	 Catch:{ Exception -> 0x013b }
        r16 = r0;
        r0 = r2.getAbsolutePath();	 Catch:{ Exception -> 0x013b }
        r15.append(r0);	 Catch:{ Exception -> 0x013b }
        r0 = "delete failed";
        r15.append(r0);	 Catch:{ Exception -> 0x013b }
        r0 = r15.toString();	 Catch:{ Exception -> 0x013b }
        miui.util.Log.d(r5, r0);	 Catch:{ Exception -> 0x013b }
        goto L_0x013a;
    L_0x0135:
        r16 = r0;
        goto L_0x013a;
    L_0x0138:
        r16 = r0;
    L_0x013a:
        goto L_0x0141;
    L_0x013b:
        r0 = move-exception;
        r2 = "screenShotAndSave";
        miui.util.Log.d(r5, r2, r0);
    L_0x0141:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.util.ScreenshotUtils.screenShotAndSave(java.lang.String, int, boolean, boolean):void");
    }

    public static File getActivityScreenshotFile(String shortComponentName, boolean isPort) {
        File file = ACTIVITY_SCREENSHOT_FOLDER;
        Object[] objArr = new Object[2];
        objArr[0] = shortComponentName.replace('/', '-');
        objArr[1] = isPort ? "p" : "l";
        return new File(file, String.format("%s--%s", objArr));
    }

    public static Bitmap getScreenshot(Context context) {
        return getScreenshot(context, 1.0f, 0, 0, true);
    }

    public static Bitmap getScreenshot(Context context, float scale, int minLayer, int maxLayer, boolean includeNavigationBar) {
        Bitmap screenshot;
        int i = minLayer;
        int i2 = maxLayer;
        initializeIfNeed(context);
        int screenshotWidth = (int) ((((float) sScreenWidth) * scale) + 0.5f);
        int screenshotHeight = (int) ((((float) sScreenHeight) * scale) + 0.5f);
        if (i == 0 && i2 == 0) {
            screenshot = CompatibilityHelper.screenshot(screenshotWidth, screenshotHeight);
        } else {
            screenshot = CompatibilityHelper.screenshot(screenshotWidth, screenshotHeight, i, i2);
        }
        if (screenshot == null) {
            return screenshot;
        }
        int resourceId;
        int degree = sDisplay.getRotation();
        boolean z = degree == 0 || degree == 2;
        boolean isPortrait = z;
        if (includeNavigationBar) {
            sDisplay.getRealSize(sSizeBuf);
        } else {
            int navigationBarHeight = 0;
            try {
                boolean hasNavigationBar = CompatibilityHelper.hasNavigationBar(sDisplay.getDisplayId());
                if (hasNavigationBar && Global.getBoolean(context.getContentResolver(), Global.FORCE_FSG_NAV_BAR)) {
                    hasNavigationBar = false;
                }
                if (hasNavigationBar) {
                    resourceId = context.getResources().getIdentifier("navigation_bar_size", "dimen", "android");
                    if (resourceId > 0) {
                        navigationBarHeight = context.getResources().getDimensionPixelSize(resourceId);
                    }
                }
            } catch (RemoteException e) {
            }
            sDisplay.getRealSize(sSizeBuf);
            Point point;
            if (Build.IS_TABLET) {
                point = sSizeBuf;
                point.y -= navigationBarHeight;
            } else if (isPortrait) {
                point = sSizeBuf;
                point.y -= navigationBarHeight;
            } else {
                point = sSizeBuf;
                point.x -= navigationBarHeight;
            }
        }
        resourceId = (int) ((((float) sSizeBuf.x) * scale) + 1056964608);
        int targetHeight = (int) ((((float) sSizeBuf.y) * scale) + 0.5f);
        if (screenshotWidth == resourceId && screenshotHeight == targetHeight && degree == 0) {
            return screenshot;
        }
        Matrix matrix = new Matrix();
        if (degree != 0) {
            matrix.postTranslate(((float) (-screenshotWidth)) / 2.0f, ((float) (-screenshotHeight)) / 2.0f);
            matrix.postRotate((float) (360 - (degree * 90)));
            matrix.postTranslate((isPortrait ? (float) screenshotWidth : (float) screenshotHeight) / 2.0f, (isPortrait ? (float) screenshotHeight : (float) screenshotWidth) / 2.0f);
        }
        Bitmap temp = Bitmap.createBitmap(resourceId, targetHeight, Config.ARGB_8888);
        new Canvas(temp).drawBitmap(screenshot, matrix, new Paint());
        screenshot.recycle();
        return temp;
    }

    public static Bitmap getBlurBackground(Context context, Bitmap cache) {
        Bitmap screenshot = getScreenshot(context, 0.33333334f, 0, 0, false);
        Bitmap bluredBitmap = getBlurBackground(screenshot, cache);
        screenshot.recycle();
        return bluredBitmap;
    }

    public static Bitmap getBlurBackground(Bitmap screenshot, Bitmap cache) {
        if (screenshot != null) {
            cache = BitmapFactory.fastBlur(screenshot, cache, Resources.getSystem().getDimensionPixelSize(R.dimen.window_background_blur_radius));
        }
        if (cache != null) {
            new Canvas(cache).drawColor(Resources.getSystem().getColor(miui.system.R.color.blur_background_mask));
        }
        return cache;
    }

    private static SoftReference<Bitmap> getCacheBitmap(boolean hasNavigationBar) {
        if (!hasNavigationBar) {
            return sCacheBitmap;
        }
        Point displaySize = new Point();
        sDisplay.getSize(displaySize);
        int rotation = sDisplay.getRotation();
        boolean isPort = rotation == 0 || rotation == 2;
        return (isPort ? displaySize.y : displaySize.x) == sScreenHeight ? sCacheBitmapWithNavigationBarHide : sCacheBitmapWithNavigationBarShow;
    }

    private static void setCacheBitmap(SoftReference<Bitmap> cacheBitmap, boolean hasNavigationBar) {
        if (hasNavigationBar) {
            Point displaySize = new Point();
            sDisplay.getSize(displaySize);
            int rotation = sDisplay.getRotation();
            boolean isPort = rotation == 0 || rotation == 2;
            if ((isPort ? displaySize.y : displaySize.x) == sScreenHeight) {
                sCacheBitmapWithNavigationBarHide = cacheBitmap;
            } else {
                sCacheBitmapWithNavigationBarShow = cacheBitmap;
            }
            return;
        }
        sCacheBitmap = cacheBitmap;
    }
}
