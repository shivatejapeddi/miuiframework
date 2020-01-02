package android.view;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.os.SystemProperties;
import android.util.Log;
import java.util.HashSet;
import java.util.Set;
import org.apache.miui.commons.lang3.ClassUtils;

public class ViewRootImplInjector {
    private static final int AMOTION_EVENT_FLAG_DEBUGINPUT_DETAIL = 4194304;
    private static final int AMOTION_EVENT_FLAG_DEBUGINPUT_MAJAR = 2097152;
    private static final int DEBUG_INPUT_DETAIL = 2;
    private static final int DEBUG_INPUT_MAJAR = 1;
    private static final int DEBUG_INPUT_NO = 0;
    private static final int GESTURE_FINGER_COUNT = 3;
    private static final Set<String> PACKAGE_ALLOW_DRAW_IF_ANIMATING = new HashSet<String>() {
        {
            add(ViewRootImplInjector.PACKAGE_NAME_HOME);
            add(ViewRootImplInjector.PACKAGE_NAME_SYSTEMUI);
        }
    };
    private static final String PACKAGE_NAME_HOME = "com.miui.home";
    private static final String PACKAGE_NAME_SYSTEMUI = "com.android.systemui";
    private static final String TAG = "ViewRootImpl";
    private static int mDebugInput = 0;
    private static int mMoveCount = 0;
    static ComponentName sLauncher = new ComponentName(PACKAGE_NAME_HOME, "com.miui.home.launcher.Launcher");

    /* JADX WARNING: Missing block: B:21:0x006a, code skipped:
            return;
     */
    public static void transformWindowType(android.view.View r5, android.view.WindowManager.LayoutParams r6) {
        /*
        r0 = miui.os.Build.IS_INTERNATIONAL_BUILD;
        if (r0 != 0) goto L_0x006a;
    L_0x0004:
        r0 = android.miui.AppOpsUtils.isXOptMode();
        if (r0 == 0) goto L_0x000b;
    L_0x000a:
        goto L_0x006a;
    L_0x000b:
        r0 = r6.type;
        r1 = 2005; // 0x7d5 float:2.81E-42 double:9.906E-321;
        if (r0 != r1) goto L_0x0069;
    L_0x0011:
        r0 = new java.util.ArrayList;
        r0.<init>();
        r1 = "android.view.ViewRootImplInjector.transformWindowType";
        r0.add(r1);
        r1 = "android.view.ViewRootImpl.setView";
        r0.add(r1);
        r1 = "android.view.WindowManagerGlobal.addView";
        r0.add(r1);
        r1 = "android.view.WindowManagerImpl.addView";
        r0.add(r1);
        r1 = "android.widget.Toast$TN.handleShow";
        r0.add(r1);
        r1 = new java.lang.Exception;	 Catch:{ Exception -> 0x0061 }
        r1.<init>();	 Catch:{ Exception -> 0x0061 }
        r1 = r1.getStackTrace();	 Catch:{ Exception -> 0x0061 }
        r2 = r1.length;	 Catch:{ Exception -> 0x0061 }
        r3 = r0.size();	 Catch:{ Exception -> 0x0061 }
        if (r2 <= r3) goto L_0x0060;
    L_0x003f:
        r2 = 0;
    L_0x0040:
        r3 = r0.size();	 Catch:{ Exception -> 0x0061 }
        if (r2 >= r3) goto L_0x0060;
    L_0x0046:
        r3 = r0.get(r2);	 Catch:{ Exception -> 0x0061 }
        r3 = (java.lang.String) r3;	 Catch:{ Exception -> 0x0061 }
        r4 = r1[r2];	 Catch:{ Exception -> 0x0061 }
        r4 = elementToString(r4);	 Catch:{ Exception -> 0x0061 }
        r3 = r3.equals(r4);	 Catch:{ Exception -> 0x0061 }
        if (r3 != 0) goto L_0x005d;
    L_0x0058:
        r3 = 2003; // 0x7d3 float:2.807E-42 double:9.896E-321;
        r6.type = r3;	 Catch:{ Exception -> 0x0061 }
        return;
    L_0x005d:
        r2 = r2 + 1;
        goto L_0x0040;
    L_0x0060:
        goto L_0x0069;
    L_0x0061:
        r1 = move-exception;
        r2 = "ViewRootImpl";
        r3 = " transformWindowTye error ";
        android.util.Log.e(r2, r3, r1);
    L_0x0069:
        return;
    L_0x006a:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.ViewRootImplInjector.transformWindowType(android.view.View, android.view.WindowManager$LayoutParams):void");
    }

    private static String elementToString(StackTraceElement element) {
        StringBuilder buf = new StringBuilder(80);
        buf.append(element.getClassName());
        buf.append(ClassUtils.PACKAGE_SEPARATOR_CHAR);
        buf.append(element.getMethodName());
        return buf.toString();
    }

    static boolean needUpdateWindowState(ViewRootImpl root, boolean stopped) {
        if (root == null || !stopped) {
            return true;
        }
        Context ctx = root.mContext;
        if (ctx == null) {
            return true;
        }
        if (sLauncher.getPackageName().equals(ctx.getPackageName())) {
            try {
                ComponentName cn = ((RunningTaskInfo) ((ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1).get(0)).topActivity;
                if (cn == null || !sLauncher.getClassName().equals(cn.getClassName())) {
                    return true;
                }
                return false;
            } catch (Exception e) {
            }
        }
        return true;
    }

    public static void checkForThreeGesture(MotionEvent event) {
        if (event.getPointerCount() == 3 && SystemProperties.getBoolean("sys.miui.screenshot", false)) {
            event.setAction(3);
            Log.d(TAG, "cancle motionEvent because of threeGesture detecting");
        }
    }

    static boolean allowDrawIfAnimating(String packageName) {
        return PACKAGE_ALLOW_DRAW_IF_ANIMATING.contains(packageName);
    }

    static void logOnInputEvent(InputEvent event) {
        boolean z = event instanceof KeyEvent;
        String str = "[TouchInput][ViewRootImpl] ";
        String str2 = TAG;
        if (z) {
            int i = mDebugInput;
            if (i == 1 || i == 2) {
                KeyEvent keyEvent = (KeyEvent) event;
                int keyCode = keyEvent.getKeyCode();
                if (keyEvent.getAction() == 0) {
                    boolean down = true;
                }
                if (keyCode == 25 || keyCode == 24 || keyCode == 26 || keyCode == 4 || keyCode == 3) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(str);
                    stringBuilder.append(event.toString());
                    Log.d(str2, stringBuilder.toString());
                }
            }
        } else if (event instanceof MotionEvent) {
            MotionEvent motionEvent = (MotionEvent) event;
            checkTouchInputLevel(motionEvent.getFlags());
            int i2 = mDebugInput;
            if (i2 == 1) {
                int maskedAction = motionEvent.getActionMasked();
                if (maskedAction == 2) {
                    mMoveCount++;
                }
                if (maskedAction == 0 || maskedAction == 1 || maskedAction == 5 || maskedAction == 6) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(getMotionStr(motionEvent));
                    stringBuilder2.append(" moveCount:");
                    stringBuilder2.append(mMoveCount);
                    Log.d(str2, stringBuilder2.toString());
                    if (maskedAction == 1) {
                        mMoveCount = 0;
                    }
                }
            } else if (i2 == 2) {
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append(str);
                stringBuilder3.append(event.toString());
                Log.d(str2, stringBuilder3.toString());
            }
        }
    }

    private static void checkTouchInputLevel(int eventFlags) {
        if ((2097152 & eventFlags) != 0) {
            mDebugInput = 1;
        } else if ((4194304 & eventFlags) != 0) {
            mDebugInput = 2;
        } else {
            mDebugInput = 0;
        }
    }

    private static String getMotionStr(MotionEvent event) {
        StringBuilder msg = new StringBuilder();
        msg.append("[TouchInput][ViewRootImpl] ");
        msg.append("MotionEvent { action=");
        msg.append(MotionEvent.actionToString(event.getAction()));
        int pointerCount = event.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(", id[");
            stringBuilder.append(i);
            stringBuilder.append("]=");
            msg.append(stringBuilder.toString());
            msg.append(event.getPointerId(i));
        }
        msg.append(", pointerCount=");
        msg.append(pointerCount);
        msg.append(", eventTime=");
        msg.append(event.getEventTime());
        msg.append(", downTime=");
        msg.append(event.getDownTime());
        msg.append(" }");
        return msg.toString();
    }
}
