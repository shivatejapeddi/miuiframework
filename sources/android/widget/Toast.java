package android.widget;

import android.annotation.UnsupportedAppUsage;
import android.app.INotificationManager;
import android.app.ITransientNotification.Stub;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import com.android.internal.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Toast {
    public static final int LENGTH_LONG = 1;
    public static final int LENGTH_SHORT = 0;
    static final String TAG = "Toast";
    static final boolean localLOGV = false;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private static INotificationManager sService;
    final Context mContext;
    @UnsupportedAppUsage
    int mDuration;
    View mNextView;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    final TN mTN;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Duration {
    }

    private static class TN extends Stub {
        private static final int CANCEL = 2;
        private static final int HIDE = 1;
        static final long LONG_DURATION_TIMEOUT = 7000;
        static final long SHORT_DURATION_TIMEOUT = 4000;
        private static final int SHOW = 0;
        int mDuration;
        @UnsupportedAppUsage(maxTargetSdk = 28)
        int mGravity;
        final Handler mHandler;
        float mHorizontalMargin;
        @UnsupportedAppUsage(maxTargetSdk = 28)
        View mNextView;
        String mPackageName;
        @UnsupportedAppUsage(maxTargetSdk = 28)
        final LayoutParams mParams = new LayoutParams();
        float mVerticalMargin;
        @UnsupportedAppUsage(maxTargetSdk = 28)
        View mView;
        WindowManager mWM;
        int mX;
        @UnsupportedAppUsage(maxTargetSdk = 28)
        int mY;

        TN(String packageName, Looper looper) {
            LayoutParams params = this.mParams;
            params.height = -2;
            params.width = -2;
            params.format = -3;
            params.windowAnimations = 16973828;
            params.type = 2005;
            params.setTitle(Toast.TAG);
            params.flags = 152;
            this.mPackageName = packageName;
            if (looper == null) {
                looper = Looper.myLooper();
                if (looper == null) {
                    throw new RuntimeException("Can't toast on a thread that has not called Looper.prepare()");
                }
            }
            this.mHandler = new Handler(looper, null) {
                public void handleMessage(Message msg) {
                    int i = msg.what;
                    if (i == 0) {
                        TN.this.handleShow(msg.obj);
                    } else if (i == 1) {
                        TN.this.handleHide();
                        TN.this.mNextView = null;
                    } else if (i == 2) {
                        TN.this.handleHide();
                        TN.this.mNextView = null;
                        try {
                            Toast.getService().cancelToast(TN.this.mPackageName, TN.this);
                        } catch (RemoteException e) {
                        }
                    }
                }
            };
        }

        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
        public void show(IBinder windowToken) {
            this.mHandler.obtainMessage(0, windowToken).sendToTarget();
        }

        public void hide() {
            this.mHandler.obtainMessage(1).sendToTarget();
        }

        public void cancel() {
            this.mHandler.obtainMessage(2).sendToTarget();
        }

        /* JADX WARNING: Missing block: B:27:0x00da, code skipped:
            return;
     */
        public void handleShow(android.os.IBinder r10) {
            /*
            r9 = this;
            r0 = r9.mHandler;
            r1 = 2;
            r0 = r0.hasMessages(r1);
            if (r0 != 0) goto L_0x00da;
        L_0x0009:
            r0 = r9.mHandler;
            r1 = 1;
            r0 = r0.hasMessages(r1);
            if (r0 == 0) goto L_0x0014;
        L_0x0012:
            goto L_0x00da;
        L_0x0014:
            r0 = r9.mView;
            r2 = r9.mNextView;
            if (r0 == r2) goto L_0x00d9;
        L_0x001a:
            r9.handleHide();
            r0 = r9.mNextView;
            r9.mView = r0;
            r0 = r9.mView;
            r0 = r0.getContext();
            r0 = r0.getApplicationContext();
            r2 = r9.mView;
            r2 = r2.getContext();
            r2 = r2.getOpPackageName();
            if (r0 != 0) goto L_0x003d;
        L_0x0037:
            r3 = r9.mView;
            r0 = r3.getContext();
        L_0x003d:
            r3 = "window";
            r3 = r0.getSystemService(r3);
            r3 = (android.view.WindowManager) r3;
            r9.mWM = r3;
            r3 = r9.mView;
            r3 = r3.getContext();
            r3 = r3.getResources();
            r3 = r3.getConfiguration();
            r4 = r9.mGravity;
            r5 = r3.getLayoutDirection();
            r4 = android.view.Gravity.getAbsoluteGravity(r4, r5);
            r5 = r9.mParams;
            r5.gravity = r4;
            r6 = r4 & 7;
            r7 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
            r8 = 7;
            if (r6 != r8) goto L_0x006d;
        L_0x006b:
            r5.horizontalWeight = r7;
        L_0x006d:
            r5 = r4 & 112;
            r6 = 112; // 0x70 float:1.57E-43 double:5.53E-322;
            if (r5 != r6) goto L_0x0077;
        L_0x0073:
            r5 = r9.mParams;
            r5.verticalWeight = r7;
        L_0x0077:
            r5 = r9.mParams;
            r6 = r9.mX;
            r5.x = r6;
            r6 = r9.mY;
            r5.y = r6;
            r6 = r9.mVerticalMargin;
            r5.verticalMargin = r6;
            r6 = r9.mHorizontalMargin;
            r5.horizontalMargin = r6;
            r5.packageName = r2;
            r6 = r9.mDuration;
            if (r6 != r1) goto L_0x0092;
        L_0x008f:
            r6 = 7000; // 0x1b58 float:9.809E-42 double:3.4585E-320;
            goto L_0x0094;
        L_0x0092:
            r6 = 4000; // 0xfa0 float:5.605E-42 double:1.9763E-320;
        L_0x0094:
            r5.hideTimeoutMilliseconds = r6;
            r1 = r9.mParams;
            r1.token = r10;
            r1 = r9.mView;
            r1 = r1.getParent();
            if (r1 == 0) goto L_0x00a9;
        L_0x00a2:
            r1 = r9.mWM;
            r5 = r9.mView;
            r1.removeView(r5);
        L_0x00a9:
            r1 = r9.mWM;	 Catch:{ BadTokenException -> 0x00b6 }
            r5 = r9.mView;	 Catch:{ BadTokenException -> 0x00b6 }
            r6 = r9.mParams;	 Catch:{ BadTokenException -> 0x00b6 }
            r1.addView(r5, r6);	 Catch:{ BadTokenException -> 0x00b6 }
            r9.trySendAccessibilityEvent();	 Catch:{ BadTokenException -> 0x00b6 }
            goto L_0x00b7;
        L_0x00b6:
            r1 = move-exception;
        L_0x00b7:
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r5 = "Show toast from OpPackageName:";
            r1.append(r5);
            r1.append(r2);
            r5 = ", PackageName:";
            r1.append(r5);
            r5 = r0.getPackageName();
            r1.append(r5);
            r1 = r1.toString();
            r5 = "Toast";
            android.util.Log.i(r5, r1);
        L_0x00d9:
            return;
        L_0x00da:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.widget.Toast$TN.handleShow(android.os.IBinder):void");
        }

        private void trySendAccessibilityEvent() {
            AccessibilityManager accessibilityManager = AccessibilityManager.getInstance(this.mView.getContext());
            if (accessibilityManager.isEnabled()) {
                AccessibilityEvent event = AccessibilityEvent.obtain(64);
                event.setClassName(getClass().getName());
                event.setPackageName(this.mView.getContext().getPackageName());
                this.mView.dispatchPopulateAccessibilityEvent(event);
                accessibilityManager.sendAccessibilityEvent(event);
            }
        }

        @UnsupportedAppUsage
        public void handleHide() {
            View view = this.mView;
            if (view != null) {
                if (view.getParent() != null) {
                    this.mWM.removeViewImmediate(this.mView);
                }
                try {
                    Toast.getService().finishToken(this.mPackageName, this);
                } catch (RemoteException e) {
                }
                this.mView = null;
            }
        }
    }

    public Toast(Context context) {
        this(context, null);
    }

    public Toast(Context context, Looper looper) {
        this.mContext = context;
        this.mTN = new TN(context.getPackageName(), looper);
        this.mTN.mY = context.getResources().getDimensionPixelSize(R.dimen.toast_y_offset);
        this.mTN.mGravity = context.getResources().getInteger(R.integer.config_toastDefaultGravity);
    }

    public void show() {
        if (this.mNextView != null) {
            INotificationManager service = getService();
            String pkg = this.mContext.getOpPackageName();
            TN tn = this.mTN;
            tn.mNextView = this.mNextView;
            try {
                service.enqueueToast(pkg, tn, this.mDuration, this.mContext.getDisplayId());
                return;
            } catch (RemoteException e) {
                return;
            }
        }
        throw new RuntimeException("setView must have been called");
    }

    public void cancel() {
        this.mTN.cancel();
    }

    public void setView(View view) {
        this.mNextView = view;
    }

    public View getView() {
        return this.mNextView;
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
        this.mTN.mDuration = duration;
    }

    public int getDuration() {
        return this.mDuration;
    }

    public void setMargin(float horizontalMargin, float verticalMargin) {
        TN tn = this.mTN;
        tn.mHorizontalMargin = horizontalMargin;
        tn.mVerticalMargin = verticalMargin;
    }

    public float getHorizontalMargin() {
        return this.mTN.mHorizontalMargin;
    }

    public float getVerticalMargin() {
        return this.mTN.mVerticalMargin;
    }

    public void setGravity(int gravity, int xOffset, int yOffset) {
        TN tn = this.mTN;
        tn.mGravity = gravity;
        tn.mX = xOffset;
        tn.mY = yOffset;
    }

    public int getGravity() {
        return this.mTN.mGravity;
    }

    public int getXOffset() {
        return this.mTN.mX;
    }

    public int getYOffset() {
        return this.mTN.mY;
    }

    @UnsupportedAppUsage
    public LayoutParams getWindowParams() {
        return this.mTN.mParams;
    }

    public static Toast makeText(Context context, CharSequence text, int duration) {
        return makeText(context, null, text, duration);
    }

    public static Toast makeText(Context context, Looper looper, CharSequence text, int duration) {
        Toast result = new Toast(context, looper);
        View v = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate((int) R.layout.transient_notification, null);
        ((TextView) v.findViewById(16908299)).setText(ToastInjector.addAppName(context, text));
        result.mNextView = v;
        result.mDuration = duration;
        return result;
    }

    public static Toast makeText(Context context, int resId, int duration) throws NotFoundException {
        return makeText(context, context.getResources().getText(resId), duration);
    }

    public void setText(int resId) {
        setText(this.mContext.getText(resId));
    }

    public void setText(CharSequence s) {
        View view = this.mNextView;
        String str = "This Toast was not created with Toast.makeText()";
        if (view != null) {
            TextView tv = (TextView) view.findViewById(16908299);
            if (tv != null) {
                tv.setText(s);
                return;
            }
            throw new RuntimeException(str);
        }
        throw new RuntimeException(str);
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    private static INotificationManager getService() {
        INotificationManager iNotificationManager = sService;
        if (iNotificationManager != null) {
            return iNotificationManager;
        }
        sService = INotificationManager.Stub.asInterface(ServiceManager.getService("notification"));
        return sService;
    }

    public void setType(int type) {
        this.mTN.mParams.type = type;
    }
}
