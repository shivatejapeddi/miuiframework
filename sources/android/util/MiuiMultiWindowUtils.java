package android.util;

import android.app.ActivityManager.StackInfo;
import android.app.ActivityOptions;
import android.app.ActivityTaskManager;
import android.app.IActivityTaskManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Binder;
import android.os.RemoteException;
import android.provider.MiuiSettings;
import android.provider.MiuiSettings.XSpace;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import com.android.internal.policy.DecorView;
import java.lang.reflect.Method;
import java.util.HashMap;

public class MiuiMultiWindowUtils {
    public static final float DECOR_CAPTIONVIEW_HEIGHT = 36.36f;
    static final int FREEFORM_TO_LEFT = 100;
    static final int FREEFORM_TO_TOP = 300;
    public static final String FREEFORM_WINDOW_SCALE = "freeform_window_scale";
    public static final String KEY_QUICKREPLY = "quick_reply";
    static final String TAG = "FreeformWindow";
    private static HashMap<String, Integer> sAppList = new HashMap();
    public static float sScale = 0.75f;
    public static float sWidthHeightScale = 0.75f;

    static {
        sAppList.put("com.tencent.mm", Integer.valueOf(1));
        sAppList.put(XSpace.QQ_PACKAGE_NAME, Integer.valueOf(2));
        sAppList.put(XSpace.WHATSAPP_PACKAGE_NAME, Integer.valueOf(3));
    }

    public static boolean supportFreeFromWindow(Context context, String pkgName) {
        boolean z = true;
        if (context != null) {
            if ("com.android.systemui".equals(context.getPackageName())) {
                if (sAppList.get(pkgName) == null) {
                    z = false;
                }
                return z;
            }
        }
        return true;
    }

    public static ActivityOptions getActivityOptions(Context context, String freeformPkg) {
        int gameKey = Secure.getIntForUser(context.getContentResolver(), KEY_QUICKREPLY, 0, -2);
        int isScreenProjectionPrivace = Secure.getInt(context.getContentResolver(), MiuiSettings.Secure.SCREEN_PROJECT_PRIVACY_ON, 0);
        int screenProjectionState = Secure.getInt(context.getContentResolver(), MiuiSettings.Secure.SCREEN_PROJECT_IN_SCREENING, 0);
        boolean isLaunchMultiWindow = true;
        if (!((gameKey == 1 || (isScreenProjectionPrivace == 1 && screenProjectionState == 1)) && supportFreeFromWindow(context, freeformPkg))) {
            isLaunchMultiWindow = false;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("isLaunchMultiWindow:");
        stringBuilder.append(isLaunchMultiWindow);
        stringBuilder.append(" gameKey:");
        stringBuilder.append(gameKey);
        stringBuilder.append("isScreenProjectionPrivace:");
        stringBuilder.append(isScreenProjectionPrivace);
        stringBuilder.append("screenProjectionState:");
        stringBuilder.append(screenProjectionState);
        String stringBuilder2 = stringBuilder.toString();
        String str = TAG;
        Log.d(str, stringBuilder2);
        if (!isLaunchMultiWindow) {
            return null;
        }
        try {
            StackInfo stackInfo = ActivityTaskManager.getService().getStackInfo(3, 0);
            if (!(stackInfo == null || stackInfo.taskIds == null || stackInfo.taskIds.length <= 0)) {
                Log.d(str, "current focusStack is DOCKED and will cancel freeform");
                isLaunchMultiWindow = false;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        ActivityOptions options = null;
        if (isLaunchMultiWindow) {
            options = ActivityOptions.makeBasic();
            setMiuiConfigFlag(options, 2);
            options.setLaunchWindowingMode(5);
            options.setLaunchBounds(getFreeformRect(context, false));
        }
        return options;
    }

    private static boolean setMiuiConfigFlag(ActivityOptions object, int miuiConfigFlag) {
        try {
            Method method = ActivityOptions.class.getDeclaredMethod("setMiuiConfigFlag", new Class[]{Integer.TYPE});
            method.setAccessible(true);
            method.invoke(object, new Object[]{Integer.valueOf(miuiConfigFlag)});
            return true;
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("setMiuiConfigFlag:");
            stringBuilder.append(e.toString());
            Log.d(TAG, stringBuilder.toString());
            return false;
        }
    }

    public static int moveTaskToStack(int taskId, int stackId, boolean toTop) {
        IActivityTaskManager mInterface = ActivityTaskManager.getService();
        long identity = Binder.clearCallingIdentity();
        int windowingMode = 1;
        int i = 2;
        if (stackId == 2) {
            windowingMode = 5;
        }
        try {
            mInterface.setTaskWindowingMode(taskId, windowingMode, toTop);
            i = 1;
            return i;
        } catch (RemoteException exception) {
            i = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("setTaskWindowingMode failed: ");
            stringBuilder.append(exception);
            Log.w((String) i, stringBuilder.toString());
            return -1;
        } finally {
            Binder.restoreCallingIdentity(identity);
        }
    }

    public static int resizeTask(int taskId, Rect bounds, int resizeMode) {
        return -1;
    }

    public static Rect getFreeformRect(Context context) {
        return getFreeformRect(context, false);
    }

    public static Rect getFreeformRect(Context context, boolean needDisplayContentRotation) {
        return getFreeformRect(context, needDisplayContentRotation, false);
    }

    /* JADX WARNING: Missing block: B:23:0x0097, code skipped:
            if (r8.left <= (r7 - r2)) goto L_0x00a5;
     */
    /* JADX WARNING: Missing block: B:35:0x00ce, code skipped:
            if (r8.left <= (r6 - r2)) goto L_0x00de;
     */
    public static android.graphics.Rect getFreeformRect(android.content.Context r19, boolean r20, boolean r21) {
        /*
        r0 = r19;
        r1 = r21;
        if (r0 != 0) goto L_0x0008;
    L_0x0006:
        r2 = 0;
        return r2;
    L_0x0008:
        r2 = "window";
        r2 = r0.getSystemService(r2);
        r2 = (android.view.WindowManager) r2;
        r3 = new android.util.DisplayMetrics;
        r3.<init>();
        r4 = r2.getDefaultDisplay();
        r4.getRealMetrics(r3);
        r4 = r3.widthPixels;
        r5 = r3.heightPixels;
        r6 = java.lang.Math.min(r4, r5);
        r7 = java.lang.Math.max(r4, r5);
        r8 = new android.graphics.Rect;
        r8.<init>();
        r9 = 1108439204; // 0x421170a4 float:36.36 double:5.47641731E-315;
        r10 = r19.getResources();
        r10 = r10.getDisplayMetrics();
        r10 = r10.density;
        r10 = r10 * r9;
        r9 = (int) r10;
        r10 = 0;
        r11 = 0;
        r12 = 0;
        r13 = 0;
        r14 = 0;
        if (r20 == 0) goto L_0x0048;
    L_0x0044:
        getFreeFormWindowPositionOrientation(r0, r8, r1);
        goto L_0x004b;
    L_0x0048:
        getFreeFormWindowPosition(r0, r8);
    L_0x004b:
        if (r20 == 0) goto L_0x004f;
    L_0x004d:
        if (r1 == 0) goto L_0x006d;
    L_0x004f:
        if (r20 != 0) goto L_0x00a8;
    L_0x0051:
        r15 = r19.getDisplay();
        r15 = r15.getRotation();
        r0 = 1;
        if (r15 == r0) goto L_0x006d;
    L_0x005c:
        r0 = r19.getDisplay();
        r0 = r0.getRotation();
        r15 = 3;
        if (r0 != r15) goto L_0x0068;
    L_0x0067:
        goto L_0x006d;
    L_0x0068:
        r16 = r2;
        r17 = r3;
        goto L_0x00ac;
    L_0x006d:
        r0 = (float) r5;
        r15 = sScale;
        r0 = r0 / r15;
        r0 = (int) r0;
        r1 = (float) r0;
        r16 = sWidthHeightScale;
        r1 = r1 * r16;
        r1 = (int) r1;
        r16 = r2;
        r2 = (float) r1;
        r2 = r2 * r15;
        r2 = (int) r2;
        r15 = r8.top;
        if (r15 < 0) goto L_0x009a;
    L_0x0081:
        r15 = r8.top;
        r17 = r3;
        r3 = (float) r9;
        r18 = sScale;
        r3 = r3 * r18;
        r3 = (int) r3;
        r3 = r6 - r3;
        if (r15 > r3) goto L_0x009c;
    L_0x008f:
        r3 = r8.left;
        if (r3 < 0) goto L_0x009c;
    L_0x0093:
        r3 = r8.left;
        r15 = r7 - r2;
        if (r3 <= r15) goto L_0x00a5;
    L_0x0099:
        goto L_0x009c;
    L_0x009a:
        r17 = r3;
    L_0x009c:
        r3 = 1;
        r13 = 0;
        r14 = r13 + r0;
        r11 = 100;
        r12 = r11 + r1;
        r10 = r3;
    L_0x00a5:
        r18 = r9;
        goto L_0x00de;
    L_0x00a8:
        r16 = r2;
        r17 = r3;
    L_0x00ac:
        r0 = r4;
        r1 = (float) r0;
        r2 = sWidthHeightScale;
        r1 = r1 / r2;
        r1 = (int) r1;
        r2 = (float) r0;
        r3 = sScale;
        r2 = r2 * r3;
        r2 = (int) r2;
        r15 = (float) r1;
        r15 = r15 * r3;
        r3 = (int) r15;
        r15 = r8.top;
        if (r15 < 0) goto L_0x00d1;
    L_0x00be:
        r15 = r8.top;
        r18 = r9;
        r9 = r7 - r3;
        if (r15 > r9) goto L_0x00d3;
    L_0x00c6:
        r9 = r8.left;
        if (r9 < 0) goto L_0x00d3;
    L_0x00ca:
        r9 = r8.left;
        r15 = r6 - r2;
        if (r9 <= r15) goto L_0x00de;
    L_0x00d0:
        goto L_0x00d3;
    L_0x00d1:
        r18 = r9;
    L_0x00d3:
        r10 = 1;
        r13 = 300; // 0x12c float:4.2E-43 double:1.48E-321;
        r14 = r13 + r1;
        r9 = r6 - r2;
        r11 = r9 / 2;
        r12 = r11 + r0;
    L_0x00de:
        r0 = "x";
        r1 = "freeform window screen (";
        r2 = "FreeformWindow";
        if (r10 != 0) goto L_0x011a;
    L_0x00e7:
        r3 = r8.width();
        if (r3 == 0) goto L_0x011a;
    L_0x00ed:
        r3 = r8.height();
        if (r3 != 0) goto L_0x00f4;
    L_0x00f3:
        goto L_0x011a;
    L_0x00f4:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r3.append(r1);
        r3.append(r4);
        r3.append(r0);
        r3.append(r5);
        r0 = ") positionPoint:";
        r3.append(r0);
        r3.append(r8);
        r0 = r3.toString();
        android.util.Log.i(r2, r0);
        r0 = new android.graphics.Rect;
        r0.<init>(r8);
        return r0;
    L_0x011a:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r3.append(r1);
        r3.append(r4);
        r3.append(r0);
        r3.append(r5);
        r0 = ") rect[";
        r3.append(r0);
        r3.append(r11);
        r0 = ",";
        r3.append(r0);
        r3.append(r13);
        r1 = ";";
        r3.append(r1);
        r3.append(r12);
        r3.append(r0);
        r3.append(r14);
        r0 = "]";
        r3.append(r0);
        r0 = r3.toString();
        android.util.Log.i(r2, r0);
        r0 = new android.graphics.Rect;
        r0.<init>(r11, r13, r12, r14);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.util.MiuiMultiWindowUtils.getFreeformRect(android.content.Context, boolean, boolean):android.graphics.Rect");
    }

    public static void updatewindowConfiguration(DecorView decorView, Resources resources) {
        if (!(decorView.getAttachedActivity() == null || decorView.getAttachedActivity().getBaseContext() == null || resources == null)) {
            int windowMode = decorView.getAttachedActivity().getWindowingMode();
            if (windowMode != 0) {
                resources.getConfiguration().windowConfiguration.setWindowingMode(windowMode);
            }
        }
    }

    public static void exitFreeFormWindowIfNeeded() {
        try {
            if (ActivityTaskManager.getService().getStackInfo(5, 0) != null) {
                for (StackInfo stackInfo : ActivityTaskManager.getService().getAllStackInfos()) {
                    if (stackInfo.configuration.windowConfiguration.getWindowingMode() == 5) {
                        ActivityTaskManager.getService().setTaskWindowingMode(stackInfo.taskIds[0], 1, false);
                    }
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static boolean supportPinFreeFormApp() {
        return false;
    }

    public static boolean supportQuickReply() {
        return true;
    }

    public static void saveFreeFormWindowPosition(Context context, Rect position) {
        boolean verticalScreen = true;
        if (context.getDisplay().getRotation() == 1 || context.getDisplay().getRotation() == 3) {
            verticalScreen = false;
        }
        ContentResolver contentResolver = context.getContentResolver();
        if (verticalScreen) {
            System.putIntForUser(contentResolver, "VFreeForm_Position_Left", position.left, -2);
            System.putIntForUser(contentResolver, "VFreeForm_Position_Top", position.top, -2);
            System.putIntForUser(contentResolver, "VFreeForm_Position_Right", position.right, -2);
            System.putIntForUser(contentResolver, "VFreeForm_Position_Bottom", position.bottom, -2);
            return;
        }
        System.putIntForUser(contentResolver, "HFreeForm_Position_Left", position.left, -2);
        System.putIntForUser(contentResolver, "HFreeForm_Position_Top", position.top, -2);
        System.putIntForUser(contentResolver, "HFreeForm_Position_Right", position.right, -2);
        System.putIntForUser(contentResolver, "HFreeForm_Position_Bottom", position.bottom, -2);
    }

    public static void getFreeFormWindowPosition(Context context, Rect position) {
        boolean verticalScreen = true;
        if (context.getDisplay().getRotation() == 1 || context.getDisplay().getRotation() == 3) {
            verticalScreen = false;
        }
        getFreeFormWindowPositionOrientation(context, position, verticalScreen);
    }

    public static void getFreeFormWindowPositionOrientation(Context context, Rect position, boolean isVertical) {
        ContentResolver contentResolver = context.getContentResolver();
        if (isVertical) {
            position.left = System.getIntForUser(contentResolver, "VFreeForm_Position_Left", -1, -2);
            position.top = System.getIntForUser(contentResolver, "VFreeForm_Position_Top", -1, -2);
            position.right = System.getIntForUser(contentResolver, "VFreeForm_Position_Right", -1, -2);
            position.bottom = System.getIntForUser(contentResolver, "VFreeForm_Position_Bottom", -1, -2);
            return;
        }
        position.left = System.getIntForUser(contentResolver, "HFreeForm_Position_Left", -1, -2);
        position.top = System.getIntForUser(contentResolver, "HFreeForm_Position_Top", -1, -2);
        position.right = System.getIntForUser(contentResolver, "HFreeForm_Position_Right", -1, -2);
        position.bottom = System.getIntForUser(contentResolver, "HFreeForm_Position_Bottom", -1, -2);
    }
}
