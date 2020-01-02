package miui.slide;

import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.ContentObserver;
import android.miui.R;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.MediaStore;
import android.provider.MiuiSettings;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.telecom.Logging.Session;
import android.telecom.TelecomManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Slog;
import android.view.IWindowManager;
import android.view.WindowManagerGlobal;
import com.android.internal.os.SomeArgs;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import miui.os.MiuiBoosterClient;
import miui.process.IActivityChangeListener.Stub;
import miui.slide.SlideCameraMonitor.CameraOpenListener;

public class SlideCoverEventManager {
    private static final String FIRST_FRONT_CAMERA_OPEN = "first_front_camera_open";
    private static final String GAME_BOOST_SEGMENT_NAME = "gb_boosting";
    private static final int MSG_ACTIVITY_CHANGED = 101;
    private static final int MSG_FRONT_CAMERA_OPEN_STATUS = 102;
    private static final int MSG_INIT_OTHER_INFO = 100;
    private static final int SLIDER_FIRST_TIP_SHOW = 0;
    private static final int SLIDER_SECOND_TIP_SHOW = 2;
    private static final String SLIDE_COVER_EVENT_STATUS = "sc_event_status";
    public static final String TAG = "SlideCoverEventManager";
    private static final String USER_SETUP_COMPLETE = "user_setup_complete";
    private static final Object sCallBackLock = new Object();
    private static SlideCoverEventManager sInstance;
    private static ArrayList<String> sListenerWhiteList = new ArrayList();
    private static final Object sLock = new Object();
    private Stub mActivityListener = new Stub() {
        public void onActivityChanged(ComponentName preName, ComponentName curName) {
            if (preName != null && curName != null) {
                SomeArgs someArgs = SomeArgs.obtain();
                someArgs.arg1 = preName;
                someArgs.arg2 = curName;
                SlideCoverEventManager.this.mHandler.obtainMessage(101, someArgs).sendToTarget();
            }
        }
    };
    private SlideAnimationController mAnimationController;
    private int mAnswerCallCount;
    private SlideCallbacks mCallBacks;
    private CameraOpenListener mCameraOpenListener = new CameraOpenListener() {
        public void onCameraOpen(int cameraId) {
            if (SlideCameraMonitor.getInstance().getFrontCameraID().contains(Integer.valueOf(cameraId))) {
                SlideCoverEventManager.this.mFrontCameraOpening = true;
                if (SlideCoverEventManager.this.mSlideCoverStatus == 1) {
                    SlideCoverEventManager.this.bindSliderViewServiceDelay();
                } else {
                    SlideCoverEventManager.this.hideTipsView();
                }
            }
        }

        public void onCameraClose(int cameraId) {
            if (SlideCameraMonitor.getInstance().getFrontCameraID().contains(Integer.valueOf(cameraId))) {
                SlideCoverEventManager.this.mFrontCameraOpening = false;
                SlideCoverEventManager.this.hideTipsView();
            }
        }
    };
    private ServiceConnection mConn = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            Slog.i(SlideCoverEventManager.TAG, "onServiceConnected");
            SlideCoverEventManager.this.mSliderViewService = ISliderViewService.Stub.asInterface(service);
            SlideCoverEventManager.this.showTipsView();
        }

        public void onServiceDisconnected(ComponentName componentName) {
            Slog.d(SlideCoverEventManager.TAG, "onServiceDisconnected");
            SlideCoverEventManager.this.mSliderViewService = null;
            SlideCoverEventManager.this.bindSliderView();
        }
    };
    private Context mContext;
    private int mCurrentUserId = 0;
    private boolean mFirstEvent = true;
    private boolean mFirstFrontCameraOpen;
    private ArrayList<String> mForbiddenActivities;
    private ComponentName mForegroundComponent;
    private boolean mFrontCameraOpening;
    private boolean mGameBoostMode;
    private int mGameBoosterCount;
    private Handler mHandler;
    private boolean mInDriveMode;
    private boolean mInitMonitor;
    private int mLaunchAppCount;
    private int mLaunchCameraCount;
    private int mLaunchPanelCount;
    private String mLaunchPkg;
    private boolean mOnForbiddenActivity;
    private boolean mOnMiuiAdjustActivity;
    private boolean mOnTargetApps;
    private boolean mPhoneFloating;
    private boolean mPhoneForeground;
    private PhoneStateListener mPhoneListener;
    private int mPhoneState;
    private PowerManager mPowerManager;
    private ComponentName mPreComponent;
    private BroadcastReceiver mReceiver;
    private SettingsObserver mSettingsObserver;
    private boolean mSetupCompleted;
    private boolean mShowingTipsView;
    private int mSlideChoice;
    private int mSlideCoverStatus = -1;
    private SlideCoverListener mSliderListener;
    private ISliderViewService mSliderViewService;
    private boolean mSoundEnable;
    private TelecomManager mTelecomManager;
    private int mUseOnAdaptedAppCount;
    private int mWakeUpCount;
    private IWindowManager mWindowManager;

    private class H extends Handler {
        public H(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    SlideCoverEventManager.this.initOtherInfo();
                    return;
                case 101:
                    SlideCoverEventManager.this.onActivityChanged((SomeArgs) msg.obj);
                    return;
                case 102:
                    SlideCoverEventManager.this.handleBindSliderView();
                    return;
                default:
                    return;
            }
        }
    }

    private final class SettingsObserver extends ContentObserver {
        public SettingsObserver(Handler handler) {
            super(handler);
        }

        public void onChange(boolean r14, android.net.Uri r15) {
            /*
            r13 = this;
            r0 = r15.getLastPathSegment();
            r1 = miui.slide.SlideCoverEventManager.this;
            r1 = r1.mContext;
            r1 = r1.getContentResolver();
            r2 = r0.hashCode();
            r3 = "drive_mode_drive_mode";
            r4 = "gb_boosting";
            r5 = 5;
            r6 = 4;
            r7 = 3;
            r8 = 2;
            r9 = 0;
            r10 = 1;
            switch(r2) {
                case -2125952279: goto L_0x004f;
                case -881969351: goto L_0x0045;
                case -377350077: goto L_0x003d;
                case -10138609: goto L_0x0033;
                case 1184581850: goto L_0x0028;
                case 1497178783: goto L_0x0020;
                default: goto L_0x001f;
            };
        L_0x001f:
            goto L_0x0059;
        L_0x0020:
            r2 = r0.equals(r3);
            if (r2 == 0) goto L_0x001f;
        L_0x0026:
            r2 = r10;
            goto L_0x005a;
        L_0x0028:
            r2 = "status_bar_in_call_notification_floating";
            r2 = r0.equals(r2);
            if (r2 == 0) goto L_0x001f;
        L_0x0031:
            r2 = r9;
            goto L_0x005a;
        L_0x0033:
            r2 = "miui_slider_launch_pkg";
            r2 = r0.equals(r2);
            if (r2 == 0) goto L_0x001f;
        L_0x003b:
            r2 = r6;
            goto L_0x005a;
        L_0x003d:
            r2 = r0.equals(r4);
            if (r2 == 0) goto L_0x001f;
        L_0x0043:
            r2 = r8;
            goto L_0x005a;
        L_0x0045:
            r2 = "miui_slider_tool_choice";
            r2 = r0.equals(r2);
            if (r2 == 0) goto L_0x001f;
        L_0x004d:
            r2 = r7;
            goto L_0x005a;
        L_0x004f:
            r2 = "miui_slider_sound_check";
            r2 = r0.equals(r2);
            if (r2 == 0) goto L_0x001f;
        L_0x0057:
            r2 = r5;
            goto L_0x005a;
        L_0x0059:
            r2 = -1;
        L_0x005a:
            r11 = "SlideCoverEventManager";
            if (r2 == 0) goto L_0x00cd;
        L_0x005e:
            r12 = -2;
            if (r2 == r10) goto L_0x00a6;
        L_0x0061:
            if (r2 == r8) goto L_0x007f;
        L_0x0063:
            if (r2 == r7) goto L_0x0079;
        L_0x0065:
            if (r2 == r6) goto L_0x0072;
        L_0x0067:
            if (r2 == r5) goto L_0x006b;
        L_0x0069:
            goto L_0x00ed;
        L_0x006b:
            r2 = miui.slide.SlideCoverEventManager.this;
            r2.updateSoundCheck(r1);
            goto L_0x00ed;
        L_0x0072:
            r2 = miui.slide.SlideCoverEventManager.this;
            r2.updateLaunchApp(r1);
            goto L_0x00ed;
        L_0x0079:
            r2 = miui.slide.SlideCoverEventManager.this;
            r2.updateSlideChoice(r1);
            goto L_0x00ed;
        L_0x007f:
            r2 = miui.slide.SlideCoverEventManager.this;
            r3 = android.provider.Settings.Secure.getIntForUser(r1, r4, r9, r12);
            if (r3 != r10) goto L_0x0088;
        L_0x0087:
            r9 = r10;
        L_0x0088:
            r2.mGameBoostMode = r9;
            r2 = new java.lang.StringBuilder;
            r2.<init>();
            r3 = "mGameBoostMode=";
            r2.append(r3);
            r3 = miui.slide.SlideCoverEventManager.this;
            r3 = r3.mGameBoostMode;
            r2.append(r3);
            r2 = r2.toString();
            android.util.Slog.d(r11, r2);
            goto L_0x00ed;
        L_0x00a6:
            r2 = miui.slide.SlideCoverEventManager.this;
            r3 = android.provider.Settings.System.getIntForUser(r1, r3, r9, r12);
            if (r3 == 0) goto L_0x00af;
        L_0x00ae:
            r9 = r10;
        L_0x00af:
            r2.mInDriveMode = r9;
            r2 = new java.lang.StringBuilder;
            r2.<init>();
            r3 = "mInDriveMode=";
            r2.append(r3);
            r3 = miui.slide.SlideCoverEventManager.this;
            r3 = r3.mInDriveMode;
            r2.append(r3);
            r2 = r2.toString();
            android.util.Slog.d(r11, r2);
            goto L_0x00ed;
        L_0x00cd:
            r2 = miui.slide.SlideCoverEventManager.this;
            r2.updatePhoneFloating(r1);
            r2 = new java.lang.StringBuilder;
            r2.<init>();
            r3 = "mPhoneForegroundState=";
            r2.append(r3);
            r3 = miui.slide.SlideCoverEventManager.this;
            r3 = r3.mPhoneFloating;
            r2.append(r3);
            r2 = r2.toString();
            android.util.Slog.d(r11, r2);
        L_0x00ed:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: miui.slide.SlideCoverEventManager$SettingsObserver.onChange(boolean, android.net.Uri):void");
        }
    }

    private class SlideCallbacks extends Handler {
        private final ArrayMap<IBinder, Callback> mCallbackMap = new ArrayMap();

        private final class Callback implements DeathRecipient {
            final String mIdentity;
            final ISlideChangeListener mListener;
            final String mProcessName;
            final int mUserId;

            Callback(ISlideChangeListener listener, int userId, String processName, String identity) {
                this.mListener = listener;
                this.mUserId = userId;
                this.mProcessName = processName;
                this.mIdentity = identity;
            }

            public void binderDied() {
                synchronized (SlideCoverEventManager.sCallBackLock) {
                    SlideCallbacks.this.mCallbackMap.remove(this.mListener.asBinder());
                }
            }

            public String toString() {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.mProcessName);
                String str = Session.SESSION_SEPARATION_CHAR_CHILD;
                stringBuilder.append(str);
                stringBuilder.append(this.mIdentity);
                stringBuilder.append(str);
                stringBuilder.append(this.mUserId);
                return stringBuilder.toString();
            }
        }

        public SlideCallbacks(Looper looper) {
            super(looper);
        }

        public ArrayMap<IBinder, Callback> getCallbacks() {
            return this.mCallbackMap;
        }

        public void register(String identity, int userId, String processName, ISlideChangeListener callback) {
            synchronized (SlideCoverEventManager.sCallBackLock) {
                try {
                    IBinder binder = callback.asBinder();
                    Callback cb = new Callback(callback, userId, processName, identity);
                    binder.linkToDeath(cb, 0);
                    String str = SlideCoverEventManager.TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(processName);
                    stringBuilder.append(Session.SESSION_SEPARATION_CHAR_CHILD);
                    stringBuilder.append(identity);
                    stringBuilder.append(" registered");
                    Slog.d(str, stringBuilder.toString());
                    this.mCallbackMap.put(binder, cb);
                } catch (RemoteException e) {
                    String str2 = SlideCoverEventManager.TAG;
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("failed to register listener for ");
                    stringBuilder2.append(processName);
                    stringBuilder2.append(Session.SESSION_SEPARATION_CHAR_CHILD);
                    stringBuilder2.append(identity);
                    stringBuilder2.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                    stringBuilder2.append(e.toString());
                    Slog.d(str2, stringBuilder2.toString());
                }
            }
        }

        public void unregister(IBinder binder) {
            synchronized (SlideCoverEventManager.sCallBackLock) {
                Callback cb = (Callback) this.mCallbackMap.remove(binder);
                if (cb != null) {
                    cb.mListener.asBinder().unlinkToDeath(cb, 0);
                }
            }
        }

        private boolean notifyStatusChanged(int status) {
            synchronized (SlideCoverEventManager.sCallBackLock) {
                Callback cb = null;
                Iterator it = SlideCoverEventManager.sListenerWhiteList.iterator();
                while (it.hasNext()) {
                    String identity = (String) it.next();
                    if (!identity.contains("gamebooster") || SlideCoverEventManager.this.mGameBoostMode) {
                        if (identity.contains("sliderpanel")) {
                            if (SlideCoverEventManager.this.mSlideChoice != 2) {
                                continue;
                            } else if (SlideCoverEventManager.this.mContext.getDisplay().getRotation() != 0) {
                            }
                        }
                        boolean sent = false;
                        for (IBinder binder : this.mCallbackMap.keySet()) {
                            cb = (Callback) this.mCallbackMap.get(binder);
                            if (cb != null && cb.mIdentity.equals(identity)) {
                                SomeArgs someArgs = SomeArgs.obtain();
                                someArgs.arg1 = cb;
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append(cb.mProcessName);
                                stringBuilder.append(Session.SESSION_SEPARATION_CHAR_CHILD);
                                stringBuilder.append(cb.mIdentity);
                                someArgs.arg2 = stringBuilder.toString();
                                Message.obtain(this, status, someArgs).sendToTarget();
                                sent = true;
                            }
                        }
                        if (sent) {
                            if (cb != null && cb.mUserId == SlideCoverEventManager.this.mCurrentUserId && status == 0) {
                                if (identity.contains("gamebooster")) {
                                    SlideCoverEventManager.this.mGameBoosterCount = SlideCoverEventManager.this.mGameBoosterCount + 1;
                                }
                                if (identity.contains("sliderpanel")) {
                                    SlideCoverEventManager.this.mLaunchPanelCount = SlideCoverEventManager.this.mLaunchPanelCount + 1;
                                }
                            }
                            return true;
                        }
                    }
                }
                return false;
            }
        }

        public void handleMessage(Message msg) {
            String str = SlideCoverEventManager.TAG;
            SomeArgs someArgs = msg.obj;
            Callback cb = someArgs.arg1;
            String pkg = someArgs.arg2;
            try {
                cb.mListener.onSlideChanged(msg.what);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("notify event ");
                stringBuilder.append(msg.what);
                stringBuilder.append(" on user ");
                stringBuilder.append(cb.mUserId);
                stringBuilder.append(" for ");
                stringBuilder.append(pkg);
                Slog.d(str, stringBuilder.toString());
            } catch (RemoteException e) {
                Slog.e(str, e.toString());
            } catch (Throwable th) {
                someArgs.recycle();
            }
            someArgs.recycle();
        }
    }

    private final class SlideReceiver extends BroadcastReceiver {
        private SlideReceiver() {
        }

        /* synthetic */ SlideReceiver(SlideCoverEventManager x0, AnonymousClass1 x1) {
            this();
        }

        /* JADX WARNING: Removed duplicated region for block: B:14:0x0035  */
        /* JADX WARNING: Removed duplicated region for block: B:12:0x002c  */
        /* JADX WARNING: Removed duplicated region for block: B:12:0x002c  */
        /* JADX WARNING: Removed duplicated region for block: B:14:0x0035  */
        public void onReceive(android.content.Context r6, android.content.Intent r7) {
            /*
            r5 = this;
            r0 = r7.getAction();
            r1 = r0.hashCode();
            r2 = -1514214344; // 0xffffffffa5beec38 float:-3.3119814E-16 double:NaN;
            r3 = 0;
            r4 = 1;
            if (r1 == r2) goto L_0x001f;
        L_0x000f:
            r2 = 959232034; // 0x392cb822 float:1.6471793E-4 double:4.739235944E-315;
            if (r1 == r2) goto L_0x0015;
        L_0x0014:
            goto L_0x0029;
        L_0x0015:
            r1 = "android.intent.action.USER_SWITCHED";
            r0 = r0.equals(r1);
            if (r0 == 0) goto L_0x0014;
        L_0x001d:
            r0 = r3;
            goto L_0x002a;
        L_0x001f:
            r1 = "android.intent.action.MEDIA_MOUNTED";
            r0 = r0.equals(r1);
            if (r0 == 0) goto L_0x0014;
        L_0x0027:
            r0 = r4;
            goto L_0x002a;
        L_0x0029:
            r0 = -1;
        L_0x002a:
            if (r0 == 0) goto L_0x0035;
        L_0x002c:
            if (r0 == r4) goto L_0x002f;
        L_0x002e:
            goto L_0x0052;
        L_0x002f:
            r0 = miui.slide.SlideCoverEventManager.this;
            r0.bindSliderView();
            goto L_0x0052;
        L_0x0035:
            r0 = "android.intent.extra.user_handle";
            r0 = r7.getIntExtra(r0, r3);
            r1 = miui.slide.SlideCoverEventManager.this;
            r1 = r1.mCurrentUserId;
            if (r0 == r1) goto L_0x0052;
        L_0x0043:
            r1 = miui.slide.SlideCoverEventManager.this;
            r1.mCurrentUserId = r0;
            r1 = miui.slide.SlideCoverEventManager.this;
            r1.updateSettings();
            r1 = miui.slide.SlideCoverEventManager.this;
            r1.bindSliderView();
        L_0x0052:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: miui.slide.SlideCoverEventManager$SlideReceiver.onReceive(android.content.Context, android.content.Intent):void");
        }
    }

    static {
        sListenerWhiteList.add("gamebooster");
        sListenerWhiteList.add("sliderpanel");
    }

    public SlideCoverEventManager(SlideCoverListener sliderListener, Context context, Looper looper) {
        this.mSliderListener = sliderListener;
        this.mContext = context;
        this.mHandler = new H(looper);
        this.mCallBacks = new SlideCallbacks(this.mHandler.getLooper());
        sInstance = this;
    }

    public void systemReady() {
        this.mWindowManager = WindowManagerGlobal.getWindowManagerService();
        this.mPhoneListener = new PhoneStateListener() {
            public void onCallStateChanged(int state, String incomingNumber) {
                SlideCoverEventManager.this.mPhoneState = state;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("mPhoneState=");
                stringBuilder.append(SlideCoverEventManager.this.mPhoneState);
                Slog.d(SlideCoverEventManager.TAG, stringBuilder.toString());
            }
        };
        TelephonyManager.from(this.mContext).listen(this.mPhoneListener, 32);
        this.mTelecomManager = TelecomManager.from(this.mContext);
        this.mPowerManager = (PowerManager) this.mContext.getSystemService(Context.POWER_SERVICE);
        this.mAnimationController = new SlideAnimationController(this.mContext, this.mHandler.getLooper());
        this.mHandler.obtainMessage(100).sendToTarget();
    }

    public boolean handleSlideCoverEvent(int event) {
        if (event != 2) {
            if (this.mSlideCoverStatus == event) {
                return false;
            }
            this.mSlideCoverStatus = event;
        }
        if (this.mFirstEvent) {
            this.mFirstEvent = false;
            return false;
        }
        if (!this.mSetupCompleted) {
            updateSetupComplete(this.mContext.getContentResolver());
            if (!this.mSetupCompleted) {
                handleSlideCoverAnimation(event);
                return false;
            }
        }
        if (this.mContext.getDisplay().getRotation() == 0) {
            handleSystem(event);
            if (handlePhone(event)) {
                return false;
            }
            if (handleKeyGuard(event)) {
                return true;
            }
            if ((this.mGameBoostMode && this.mCallBacks.notifyStatusChanged(event)) || handleWechatHardCoder(event)) {
                return false;
            }
            if (this.mOnForbiddenActivity) {
                return true;
            }
            if (handleChoice(event)) {
                return false;
            }
            return handleCamera(event);
        } else if (handlePhone(event)) {
            return false;
        } else {
            handleSlideCoverSound(event);
            this.mCallBacks.notifyStatusChanged(event);
            return false;
        }
    }

    private void handleSystem(int event) {
        updateEventStatus(event);
        if (event == 0) {
            PowerManager powerManager = this.mPowerManager;
            if (powerManager != null) {
                if (!powerManager.isInteractive()) {
                    this.mWakeUpCount++;
                }
                this.mPowerManager.wakeUp(SystemClock.uptimeMillis(), "android.policy:SLIDE");
            }
            hideTipsView();
        }
        handleSlideCoverAnimation(event);
        if (event == 1 && this.mFrontCameraOpening) {
            bindSliderViewServiceDelay();
        }
    }

    private void handleSlideCoverAnimation(int event) {
        this.mAnimationController.showView(event);
        handleSlideCoverSound(event);
    }

    private void handleSlideCoverSound(int event) {
        if (this.mSoundEnable) {
            ISliderViewService iSliderViewService = this.mSliderViewService;
            if (iSliderViewService != null) {
                try {
                    iSliderViewService.playSound(event);
                } catch (RemoteException e) {
                    Slog.d(TAG, "error to play sound");
                }
            }
        }
    }

    private boolean handlePhone(int event) {
        String str = TAG;
        int i = this.mPhoneState;
        if (i == 2) {
            return true;
        }
        if (event == 0 && i == 1 && (this.mPhoneForeground || this.mPhoneFloating || this.mInDriveMode)) {
            try {
                Slog.d(str, "answer a call");
                this.mTelecomManager.acceptRingingCall();
                this.mAnswerCallCount++;
                return true;
            } catch (Exception e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("error to answer a call:");
                stringBuilder.append(e.toString());
                Slog.d(str, stringBuilder.toString());
            }
        }
        return false;
    }

    private boolean handleKeyGuard(int event) {
        if (event != 0 || !isKeyguardLocked()) {
            return false;
        }
        Slog.d(TAG, "event to be handled by keyguard");
        return true;
    }

    public boolean isKeyguardLocked() {
        try {
            return this.mWindowManager.isKeyguardLocked();
        } catch (RemoteException e) {
            Slog.d(TAG, "error to get keyguard status");
            return false;
        }
    }

    private boolean handleWechatHardCoder(int event) {
        ComponentName componentName = this.mForegroundComponent;
        if (componentName != null) {
            if ("com.tencent.mm".equals(componentName.getPackageName())) {
                if (!"com.tencent.mm.plugin.voip.ui.VideoActivity".equals(this.mForegroundComponent.getClassName()) || !SystemProperties.getBoolean("sys.hardcoder.registered", false)) {
                    return false;
                }
                if (event == 0) {
                    return MiuiBoosterClient.getInstance().writeEvent(1);
                }
                if (event == 1) {
                    return MiuiBoosterClient.getInstance().writeEvent(2);
                }
                return true;
            }
        }
        return false;
    }

    private boolean handleChoice(int event) {
        ComponentName componentName = this.mForegroundComponent;
        boolean z = false;
        if ((componentName != null && componentName.getPackageName().equals("com.android.camera")) || this.mOnMiuiAdjustActivity) {
            return false;
        }
        if (this.mFrontCameraOpening) {
            if (this.mSlideChoice != 1) {
                z = true;
            }
            return z;
        }
        int i = this.mSlideChoice;
        if (i == 0) {
            return true;
        }
        if (event == 0 && i == 3) {
            Intent intent = this.mLaunchPkg;
            if (intent != null) {
                intent = SlideUtils.getLaunchIntentForPackageAsUser(intent, this.mCurrentUserId);
                String str = TAG;
                StringBuilder stringBuilder;
                if (intent == null) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("error to launch ");
                    stringBuilder.append(this.mLaunchPkg);
                    Slog.d(str, stringBuilder.toString());
                    return true;
                }
                this.mContext.startActivityAsUser(intent, UserHandle.CURRENT);
                stringBuilder = new StringBuilder();
                stringBuilder.append("launch ");
                stringBuilder.append(this.mLaunchPkg);
                Slog.d(str, stringBuilder.toString());
                this.mLaunchAppCount++;
                return true;
            }
        }
        return this.mCallBacks.notifyStatusChanged(event);
    }

    private boolean handleMiuiAdjustApp(int event) {
        return this.mOnMiuiAdjustActivity;
    }

    /* JADX WARNING: Missing block: B:12:0x002b, code skipped:
            if (r0.getPackageName().equals("com.android.camera") != false) goto L_0x002d;
     */
    private boolean handleCamera(int r4) {
        /*
        r3 = this;
        r0 = r3.handleMiuiAdjustApp(r4);
        r1 = 1;
        if (r0 == 0) goto L_0x000d;
    L_0x0007:
        r0 = r3.mUseOnAdaptedAppCount;
        r0 = r0 + r1;
        r3.mUseOnAdaptedAppCount = r0;
        return r1;
    L_0x000d:
        if (r4 != 0) goto L_0x0037;
    L_0x000f:
        r0 = r3.mOnTargetApps;
        if (r0 != 0) goto L_0x0037;
    L_0x0013:
        r0 = miui.slide.SlideCameraMonitor.getInstance();
        r0 = r0.isCameraOpening();
        if (r0 == 0) goto L_0x002d;
    L_0x001d:
        r0 = r3.mPreComponent;
        if (r0 == 0) goto L_0x0037;
    L_0x0021:
        r0 = r0.getPackageName();
        r2 = "com.android.camera";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x0037;
    L_0x002d:
        r3.launchCamera();
        r0 = r3.mLaunchCameraCount;
        r0 = r0 + r1;
        r3.mLaunchCameraCount = r0;
        r0 = 0;
        return r0;
    L_0x0037:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.slide.SlideCoverEventManager.handleCamera(int):boolean");
    }

    private void launchCamera() {
        Intent intent = new Intent();
        intent.setFlags(268468224);
        intent.putExtra("ShowCameraWhenLocked", true);
        intent.putExtra("StartActivityWhenLocked", true);
        intent.setAction(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        intent.putExtra("autofocus", true);
        intent.putExtra("fullScreen", false);
        intent.putExtra("showActionIcons", false);
        intent.putExtra("android.intent.extras.SCREEN_SLIDE", true);
        intent.setComponent(new ComponentName("com.android.camera", "com.android.camera.Camera"));
        ActivityOptions op = ActivityOptions.makeCustomAnimation(this.mContext, R.anim.camera_activity_enter, R.anim.third_app_exit);
        Slog.d(TAG, "launchCamera");
        this.mContext.startActivityAsUser(intent, op.toBundle(), UserHandle.CURRENT);
    }

    private void onActivityChanged(SomeArgs someArgs) {
        ComponentName preName = someArgs.arg1;
        ComponentName curName = someArgs.arg2;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("notifyChange! preName = ");
        stringBuilder.append(preName.toString());
        stringBuilder.append("; curName = ");
        stringBuilder.append(curName.toString());
        Log.i(TAG, stringBuilder.toString());
        String className = curName.getClassName();
        this.mForegroundComponent = curName;
        this.mPreComponent = preName;
        this.mOnForbiddenActivity = this.mForbiddenActivities.contains(className);
        this.mOnMiuiAdjustActivity = SlideCloudConfigHelper.getInstance().is3rdAppProcessingActivity(curName.getPackageName(), className);
        this.mOnTargetApps = SlideCloudConfigHelper.getInstance().isMiuiAdapteringApp(curName.getPackageName());
        boolean z = true;
        if (curName.getPackageName().equals("com.miui.home") && !this.mInitMonitor) {
            SlideCameraMonitor.getInstance().init(this.mContext, this.mHandler.getLooper());
            SlideCameraMonitor.getInstance().setCameraOpenListener(this.mCameraOpenListener);
            this.mInitMonitor = true;
        }
        if (this.mFrontCameraOpening && !className.equals("com.android.systemui.recents.RecentsActivity")) {
            hideTipsView();
        }
        if (!(className.equals("com.android.incallui.InCallActivity") || className.equals("com.android.contacts.activities.PeopleActivity") || className.equals("com.android.phone.MiuiEmergencyDialer"))) {
            z = false;
        }
        this.mPhoneForeground = z;
        someArgs.recycle();
    }

    private void hideTipsView() {
        synchronized (sLock) {
            this.mHandler.removeMessages(102);
            try {
                if (this.mSliderViewService != null && this.mShowingTipsView) {
                    Slog.d(TAG, "removeSliderView");
                    this.mSliderViewService.removeSliderView(1);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            this.mShowingTipsView = false;
        }
    }

    /* JADX WARNING: Missing block: B:25:0x0053, code skipped:
            return;
     */
    private void showTipsView() {
        /*
        r6 = this;
        r0 = sLock;
        monitor-enter(r0);
        r1 = r6.mSliderViewService;	 Catch:{ all -> 0x005e }
        if (r1 == 0) goto L_0x0054;
    L_0x0007:
        r1 = r6.mFrontCameraOpening;	 Catch:{ all -> 0x005e }
        if (r1 == 0) goto L_0x0054;
    L_0x000b:
        r1 = r6.mSlideCoverStatus;	 Catch:{ all -> 0x005e }
        if (r1 == 0) goto L_0x0054;
    L_0x000f:
        r1 = r6.mShowingTipsView;	 Catch:{ all -> 0x005e }
        if (r1 == 0) goto L_0x0014;
    L_0x0013:
        goto L_0x0054;
    L_0x0014:
        r1 = r6.mFirstFrontCameraOpen;	 Catch:{ all -> 0x005e }
        r2 = 0;
        if (r1 == 0) goto L_0x0024;
    L_0x0019:
        r1 = "SlideCoverEventManager";
        r3 = "first open front camera";
        android.util.Slog.d(r1, r3);	 Catch:{ all -> 0x005e }
        r6.mFirstFrontCameraOpen = r2;	 Catch:{ all -> 0x005e }
        r1 = 0;
        goto L_0x0025;
    L_0x0024:
        r1 = 2;
    L_0x0025:
        r3 = "SlideCoverEventManager";
        r4 = "showSliderView";
        android.util.Slog.d(r3, r4);	 Catch:{ RemoteException -> 0x0036 }
        r3 = r6.mSliderViewService;	 Catch:{ RemoteException -> 0x0036 }
        r3.showSliderView(r1);	 Catch:{ RemoteException -> 0x0036 }
        r3 = 1;
        r6.mShowingTipsView = r3;	 Catch:{ RemoteException -> 0x0036 }
        goto L_0x0045;
    L_0x0036:
        r3 = move-exception;
        r4 = "SlideCoverEventManager";
        r5 = r3.toString();	 Catch:{ all -> 0x005e }
        android.util.Slog.d(r4, r5);	 Catch:{ all -> 0x005e }
        r6.mShowingTipsView = r2;	 Catch:{ all -> 0x005e }
        r4 = 0;
        r6.mSliderViewService = r4;	 Catch:{ all -> 0x005e }
    L_0x0045:
        if (r1 != 0) goto L_0x0052;
    L_0x0047:
        r3 = r6.mContext;	 Catch:{ all -> 0x005e }
        r3 = r3.getContentResolver();	 Catch:{ all -> 0x005e }
        r4 = "first_front_camera_open";
        android.provider.Settings.System.putIntForUser(r3, r4, r2, r2);	 Catch:{ all -> 0x005e }
    L_0x0052:
        monitor-exit(r0);	 Catch:{ all -> 0x005e }
        return;
    L_0x0054:
        r1 = "SlideCoverEventManager";
        r2 = "show tips conditions are not satisfied";
        android.util.Slog.d(r1, r2);	 Catch:{ all -> 0x005e }
        monitor-exit(r0);	 Catch:{ all -> 0x005e }
        return;
    L_0x005e:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x005e }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.slide.SlideCoverEventManager.showTipsView():void");
    }

    private void handleBindSliderView() {
        this.mHandler.removeMessages(102);
        if (!this.mShowingTipsView) {
            if (this.mSliderViewService != null) {
                showTipsView();
            } else {
                bindSliderView();
            }
        }
    }

    private void bindSliderView() {
        Slog.d(TAG, "bindSliderView");
        Intent intent = new Intent();
        intent.setAction("com.android.systemui.sliderview.SliderViewService");
        intent.setPackage("com.android.systemui");
        this.mContext.bindServiceAsUser(intent, this.mConn, 1, this.mHandler, UserHandle.CURRENT);
    }

    /* JADX WARNING: Missing block: B:3:0x000e, code skipped:
            if ("com.android.keyguard.settings.MiuiNormalCameraFaceInput".equals(r0.getClassName()) == false) goto L_0x0010;
     */
    private void bindSliderViewServiceDelay() {
        /*
        r4 = this;
        r0 = r4.mForegroundComponent;
        if (r0 == 0) goto L_0x0010;
    L_0x0004:
        r0 = r0.getClassName();
        r1 = "com.android.keyguard.settings.MiuiNormalCameraFaceInput";
        r0 = r1.equals(r0);
        if (r0 != 0) goto L_0x0014;
    L_0x0010:
        r0 = r4.mShowingTipsView;
        if (r0 == 0) goto L_0x0015;
    L_0x0014:
        return;
    L_0x0015:
        r0 = r4.mHandler;
        r1 = 102; // 0x66 float:1.43E-43 double:5.04E-322;
        r0.removeMessages(r1);
        r0 = r4.mHandler;
        r0 = r0.obtainMessage(r1);
        r1 = r4.mHandler;
        r2 = 2000; // 0x7d0 float:2.803E-42 double:9.88E-321;
        r1.sendMessageDelayed(r0, r2);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.slide.SlideCoverEventManager.bindSliderViewServiceDelay():void");
    }

    private void updateEventStatus(int status) {
        System.putIntForUser(this.mContext.getContentResolver(), SLIDE_COVER_EVENT_STATUS, status, 0);
    }

    private void initOtherInfo() {
        SlideCloudConfigHelper.getInstance().setActivityChangeListener(this.mActivityListener);
        this.mForbiddenActivities = new ArrayList();
        this.mForbiddenActivities.add("com.android.keyguard.settings.MiuiNormalCameraFaceInput");
        this.mForbiddenActivities.add("com.android.keyguard.settings.MiuiFaceDataIntroduction");
        ContentResolver resolver = this.mContext.getContentResolver();
        this.mSettingsObserver = new SettingsObserver(this.mHandler);
        resolver.registerContentObserver(System.getUriFor(MiuiSettings.System.STATUS_BAR_IN_CALL_NOTIFICATION_FLOATING), false, this.mSettingsObserver, -1);
        resolver.registerContentObserver(System.getUriFor(MiuiSettings.System.DRIVE_MODE_DRIVE_MODE), false, this.mSettingsObserver, -1);
        resolver.registerContentObserver(Secure.getUriFor(GAME_BOOST_SEGMENT_NAME), false, this.mSettingsObserver, -1);
        resolver.registerContentObserver(System.getUriFor(MiuiSettings.System.MIUI_SLIDER_TOOL_CHOICE), false, this.mSettingsObserver, -1);
        resolver.registerContentObserver(System.getUriFor(MiuiSettings.System.MIUI_SLIDER_LAUNCH_PKG), false, this.mSettingsObserver, -1);
        resolver.registerContentObserver(System.getUriFor(MiuiSettings.System.MIUI_SLIDER_SOUND_CHECK), false, this.mSettingsObserver, -1);
        boolean z = true;
        this.mFirstFrontCameraOpen = System.getIntForUser(resolver, FIRST_FRONT_CAMERA_OPEN, 1, 0) == 1;
        if (Secure.getIntForUser(this.mContext.getContentResolver(), "user_setup_complete", 0, -2) != 1) {
            z = false;
        }
        this.mSetupCompleted = z;
        this.mReceiver = new SlideReceiver(this, null);
        IntentFilter userFilter = new IntentFilter();
        userFilter.addAction(Intent.ACTION_USER_SWITCHED);
        this.mContext.registerReceiver(this.mReceiver, userFilter);
        IntentFilter mediaFilter = new IntentFilter();
        mediaFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        mediaFilter.addDataScheme(ContentResolver.SCHEME_FILE);
        this.mContext.registerReceiver(this.mReceiver, mediaFilter);
        updateSettings();
    }

    private void updateSettings() {
        ContentResolver resolver = this.mContext.getContentResolver();
        updateSetupComplete(resolver);
        updatePhoneFloating(resolver);
        updateSlideChoice(resolver);
        updateLaunchApp(resolver);
        updateSoundCheck(resolver);
    }

    private void updateSetupComplete(ContentResolver resolver) {
        boolean z = false;
        if (Secure.getIntForUser(resolver, "user_setup_complete", 0, -2) == 1) {
            z = true;
        }
        this.mSetupCompleted = z;
    }

    private void updatePhoneFloating(ContentResolver resolver) {
        boolean z = false;
        if (System.getIntForUser(resolver, MiuiSettings.System.STATUS_BAR_IN_CALL_NOTIFICATION_FLOATING, 0, -2) == 1) {
            z = true;
        }
        this.mPhoneFloating = z;
    }

    private void updateSlideChoice(ContentResolver resolver) {
        this.mSlideChoice = System.getIntForUser(resolver, MiuiSettings.System.MIUI_SLIDER_TOOL_CHOICE, 1, -2);
    }

    private void updateLaunchApp(ContentResolver resolver) {
        this.mLaunchPkg = System.getStringForUser(resolver, MiuiSettings.System.MIUI_SLIDER_LAUNCH_PKG, -2);
    }

    private void updateSoundCheck(ContentResolver resolver) {
        boolean z = true;
        if (System.getIntForUser(resolver, MiuiSettings.System.MIUI_SLIDER_SOUND_CHECK, 1, -2) != 1) {
            z = false;
        }
        this.mSoundEnable = z;
    }

    public static SlideCoverEventManager getInstance() {
        return sInstance;
    }

    public void registerSlideChangeListener(String identity, ISlideChangeListener listener) {
        String processName = SlideUtils.getProcessName(this.mContext, Binder.getCallingPid());
        int userId = UserHandle.getUserId(Binder.getCallingUid());
        String str = TAG;
        StringBuilder stringBuilder;
        if (processName == null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("can't find processName for pid ");
            stringBuilder.append(Binder.getCallingPid());
            stringBuilder.append(" when register");
            Slog.d(str, stringBuilder.toString());
        } else if (sListenerWhiteList.contains(identity)) {
            this.mCallBacks.register(identity, userId, processName, listener);
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append(identity);
            stringBuilder.append(" is not authenticated, ignore");
            Slog.d(str, stringBuilder.toString());
        }
    }

    public void unregisterSlideChangeListener(ISlideChangeListener callback) {
        this.mCallBacks.unregister(callback.asBinder());
    }

    public void dump(String prefix, PrintWriter pw, String[] args) {
        String str;
        this.mSliderListener.dump(prefix, pw, args);
        pw.println("SlideCoverEventManager:");
        pw.print(prefix);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("mWakeUpCount=");
        stringBuilder.append(this.mWakeUpCount);
        pw.println(stringBuilder.toString());
        pw.print(prefix);
        stringBuilder = new StringBuilder();
        stringBuilder.append("mLaunchCameraCount=");
        stringBuilder.append(this.mLaunchCameraCount);
        pw.println(stringBuilder.toString());
        pw.print(prefix);
        stringBuilder = new StringBuilder();
        stringBuilder.append("mAnswerCallCount=");
        stringBuilder.append(this.mAnswerCallCount);
        pw.println(stringBuilder.toString());
        pw.print(prefix);
        stringBuilder = new StringBuilder();
        stringBuilder.append("mUseOnAdaptedAppCount=");
        stringBuilder.append(this.mUseOnAdaptedAppCount);
        pw.println(stringBuilder.toString());
        pw.print(prefix);
        stringBuilder = new StringBuilder();
        stringBuilder.append("mLaunchAppCount=");
        stringBuilder.append(this.mLaunchAppCount);
        pw.println(stringBuilder.toString());
        pw.print(prefix);
        stringBuilder = new StringBuilder();
        stringBuilder.append("mLaunchPanelCount=");
        stringBuilder.append(this.mLaunchPanelCount);
        pw.println(stringBuilder.toString());
        pw.print(prefix);
        stringBuilder = new StringBuilder();
        stringBuilder.append("mGameBoosterCount=");
        stringBuilder.append(this.mGameBoosterCount);
        pw.println(stringBuilder.toString());
        pw.print(prefix);
        stringBuilder = new StringBuilder();
        stringBuilder.append("mSlideCoverStatus=");
        stringBuilder.append(this.mSlideCoverStatus);
        pw.println(stringBuilder.toString());
        pw.print(prefix);
        stringBuilder = new StringBuilder();
        stringBuilder.append("mSlideChoice=");
        stringBuilder.append(this.mSlideChoice);
        pw.println(stringBuilder.toString());
        pw.print(prefix);
        stringBuilder = new StringBuilder();
        stringBuilder.append("mFrontCameraOpening=");
        stringBuilder.append(this.mFrontCameraOpening);
        pw.println(stringBuilder.toString());
        pw.print(prefix);
        stringBuilder = new StringBuilder();
        stringBuilder.append("mInDriveMode=");
        stringBuilder.append(this.mInDriveMode);
        pw.println(stringBuilder.toString());
        pw.print(prefix);
        stringBuilder = new StringBuilder();
        stringBuilder.append("mLaunchPkg=");
        stringBuilder.append(this.mLaunchPkg);
        pw.println(stringBuilder.toString());
        pw.print(prefix);
        stringBuilder = new StringBuilder();
        stringBuilder.append("mSoundEnable=");
        stringBuilder.append(this.mSoundEnable);
        pw.println(stringBuilder.toString());
        pw.print(prefix);
        stringBuilder = new StringBuilder();
        stringBuilder.append("mPhoneState=");
        stringBuilder.append(this.mPhoneState);
        pw.println(stringBuilder.toString());
        pw.print(prefix);
        stringBuilder = new StringBuilder();
        stringBuilder.append("mGameBoostMode=");
        stringBuilder.append(this.mGameBoostMode);
        pw.println(stringBuilder.toString());
        pw.print(prefix);
        stringBuilder = new StringBuilder();
        stringBuilder.append("mCurrentUserId=");
        stringBuilder.append(this.mCurrentUserId);
        pw.println(stringBuilder.toString());
        pw.print(prefix);
        stringBuilder = new StringBuilder();
        stringBuilder.append("mForegroundComponent=");
        ComponentName componentName = this.mForegroundComponent;
        if (componentName == null) {
            str = "";
        } else {
            str = componentName.toString();
        }
        stringBuilder.append(str);
        pw.println(stringBuilder.toString());
        pw.print(prefix);
        stringBuilder = new StringBuilder();
        stringBuilder.append("mOnTargetApps=");
        stringBuilder.append(this.mOnTargetApps);
        pw.println(stringBuilder.toString());
        pw.print(prefix);
        pw.println("mForbiddenActivities:");
        Iterator it = this.mForbiddenActivities.iterator();
        while (it.hasNext()) {
            str = (String) it.next();
            pw.print(prefix);
            pw.print(prefix);
            pw.println(str);
        }
        pw.print(prefix);
        pw.println("sListenerWhiteList:");
        it = sListenerWhiteList.iterator();
        while (it.hasNext()) {
            str = (String) it.next();
            pw.print(prefix);
            pw.print(prefix);
            pw.println(str);
        }
        pw.print(prefix);
        pw.println("registered slide event listener:");
        synchronized (sCallBackLock) {
            ArrayMap<IBinder, Callback> callbacks = this.mCallBacks.getCallbacks();
            for (IBinder binder : callbacks.keySet()) {
                pw.print(prefix);
                pw.print(prefix);
                pw.println(callbacks.get(binder));
            }
        }
        ISliderViewService iSliderViewService = this.mSliderViewService;
        if (iSliderViewService != null) {
            try {
                pw.print(iSliderViewService.getDumpContent(prefix));
            } catch (RemoteException e) {
            }
        }
    }
}
