package miui.slide;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.input.InputManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.os.VibrationEffect;
import android.os.VibrationEffect.OneShot;
import android.os.Vibrator;
import android.provider.MiuiSettings;
import android.provider.Settings.System;
import android.util.Slog;
import android.view.KeyEvent;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;

public class SlideCoverListener {
    private static final int DELAY_TIME_MS = 300;
    private static final int MSG_DARK_SCREEN_EVENT = 100;
    private static final int MSG_ON_SCREEN_EVENT = 101;
    private static final int MSG_UPDATE_STATUS = 102;
    private static final boolean SEND_BROADCAST = false;
    public static final int SLIDE_COVER_SENSOR_TYPE = 33171002;
    public static final int SLIDE_EVENT_CLOSE = 1;
    public static final int SLIDE_EVENT_OPEN = 0;
    public static final int SLIDE_EVENT_SLIDING = 2;
    public static final String TAG = "SlideCoverListener";
    private static final int TIME_COST_MOST_EXPECTED = 50;
    private static final long VIBRATION_TIME_THRESHOLD = 8000;
    private final Context mContext;
    private final Handler mHandler;
    private int mLastEvent = -1;
    private long mLastEventTime = -1;
    private long mLastVibrateTime;
    private final SensorEventListener mListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent event) {
            int ev = SlideCoverListener.this.getFrameworkSlideEvent((int) event.values[0]);
            if (SlideCoverListener.this.mLastEvent != ev) {
                if (!SlideCoverListener.this.mWakeLock.isHeld()) {
                    SlideCoverListener.this.mWakeLock.acquire();
                }
                SlideCoverListener.this.mLastEvent = ev;
                if (ev != 2) {
                    SlideCoverListener.this.mSlideEventCount = SlideCoverListener.this.mSlideEventCount + 1;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("event values = ");
                    stringBuilder.append(ev);
                    Slog.d(SlideCoverListener.TAG, stringBuilder.toString());
                    Message.obtain(SlideCoverListener.this.mHandler, 102, ev, 0).sendToTarget();
                    long now = SystemClock.uptimeMillis();
                    if (now - SlideCoverListener.this.mLastEventTime < 300) {
                        SlideCoverListener.this.mQuickSlideEventCount = SlideCoverListener.this.mQuickSlideEventCount + 1;
                        SlideCoverListener.this.mLastEventTime = now;
                        if (SlideCoverListener.this.mVibrator != null && SlideCoverListener.this.mVibrator.hasVibrator() && now - SlideCoverListener.this.mLastVibrateTime > SlideCoverListener.VIBRATION_TIME_THRESHOLD) {
                            SlideCoverListener.this.mVibrator.vibrate(SlideCoverListener.this.mVibrationEffect);
                            SlideCoverListener.this.mLastVibrateTime = now;
                        }
                    }
                    SlideCoverListener.this.mLastEventTime = now;
                }
                Message msg = SlideCoverListener.this.mHandler.obtainMessage();
                msg.arg1 = ev;
                msg.obj = Long.valueOf(System.currentTimeMillis());
                msg.what = 101;
                SlideCoverListener.this.mHandler.sendMessage(msg);
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    private final PowerManager mPowerManager;
    private int mQuickSlideEventCount;
    private Sensor mSensor;
    private final SensorManager mSensorManager;
    private final SlideCoverEventManager mSlideCoverEventManager;
    private int mSlideEventCount;
    private int mTotalHistoryCount;
    private VibrationEffect mVibrationEffect;
    private final Vibrator mVibrator;
    private WakeLock mWakeLock;

    private class H extends Handler {
        public H(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                case 101:
                    SlideCoverListener.this.handleDispatchMessage(msg);
                    return;
                case 102:
                    SlideCoverListener.this.updateSystemStatus(msg.arg1);
                    return;
                default:
                    return;
            }
        }
    }

    public SlideCoverListener(Context context) {
        this.mContext = context;
        this.mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.mPowerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        this.mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        this.mVibrationEffect = new OneShot(50, 1);
        HandlerThread t = new HandlerThread("slide_cover", -2);
        t.start();
        this.mHandler = new H(t.getLooper());
        this.mSlideCoverEventManager = new SlideCoverEventManager(this, context, this.mHandler.getLooper());
        this.mSensor = this.mSensorManager.getDefaultSensor(SLIDE_COVER_SENSOR_TYPE, true);
    }

    public void systemReady() {
        this.mTotalHistoryCount = System.getInt(this.mContext.getContentResolver(), MiuiSettings.System.MIUI_SLIDER_HISTORY_COUNT, 0);
        this.mSlideCoverEventManager.systemReady();
        this.mWakeLock = this.mPowerManager.newWakeLock(1, "SlideCover.mWakelock");
        this.mSensorManager.registerListener(this.mListener, this.mSensor, 3, this.mHandler);
    }

    public void setSlideCoverState(int stateEvent) {
        SensorEvent event = createSlideSensorEvent((float) stateEvent);
        if (event != null) {
            this.mListener.onSensorChanged(event);
        }
    }

    private SensorEvent createSlideSensorEvent(float eventCode) {
        SensorEvent event;
        try {
            Constructor<SensorEvent> constr = SensorEvent.class.getDeclaredConstructor(new Class[]{Integer.TYPE});
            constr.setAccessible(true);
            event = (SensorEvent) constr.newInstance(new Object[]{Integer.valueOf(16)});
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("createSlideSensorEvent: ");
            stringBuilder.append(e.toString());
            Slog.e(TAG, stringBuilder.toString());
            event = null;
        }
        if (event != null) {
            event.values[0] = eventCode;
        }
        return event;
    }

    private void updateSystemStatus(int status) {
        System.putIntForUser(this.mContext.getContentResolver(), MiuiSettings.System.MIUI_SLIDER_COVER_STATUS, status, 0);
        this.mTotalHistoryCount++;
        System.putIntForUser(this.mContext.getContentResolver(), MiuiSettings.System.MIUI_SLIDER_HISTORY_COUNT, this.mTotalHistoryCount, 0);
    }

    private int getFrameworkSlideEvent(int nativeEvent) {
        if (nativeEvent == 1) {
            return 0;
        }
        if (nativeEvent == 0) {
            return 1;
        }
        if (nativeEvent == 2) {
            return 2;
        }
        return -1;
    }

    public void dump(String prefix, PrintWriter pw, String[] args) {
        pw.println("SlideCoverListener:");
        pw.print(prefix);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("mSlideEventCount=");
        stringBuilder.append(this.mSlideEventCount);
        pw.println(stringBuilder.toString());
        pw.print(prefix);
        stringBuilder = new StringBuilder();
        stringBuilder.append("mQuickSlideEventCount=");
        stringBuilder.append(this.mQuickSlideEventCount);
        pw.println(stringBuilder.toString());
        pw.print(prefix);
        stringBuilder = new StringBuilder();
        stringBuilder.append("mTotalHistoryCount=");
        stringBuilder.append(this.mTotalHistoryCount);
        pw.println(stringBuilder.toString());
    }

    private void handleDispatchMessage(Message msg) {
        Message message = msg;
        int event = message.arg1;
        long before = System.currentTimeMillis();
        long msgDispatchTime = ((Long) message.obj).longValue();
        long dispatchTimeCost = before - msgDispatchTime;
        int i = (dispatchTimeCost > 50 ? 1 : (dispatchTimeCost == 50 ? 0 : -1));
        String str = " took ";
        String str2 = "event ";
        String str3 = TAG;
        if (i > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str2);
            stringBuilder.append(event);
            stringBuilder.append(str);
            stringBuilder.append(dispatchTimeCost);
            stringBuilder.append(" ms before handle");
            Slog.e(str3, stringBuilder.toString());
        }
        boolean result = false;
        int keyCode = -1;
        if (event == 0) {
            result = this.mSlideCoverEventManager.handleSlideCoverEvent(0);
            keyCode = 700;
        } else if (event == 1) {
            result = this.mSlideCoverEventManager.handleSlideCoverEvent(1);
            keyCode = 701;
        } else if (event == 2) {
            result = this.mSlideCoverEventManager.handleSlideCoverEvent(2);
            keyCode = 702;
        }
        if (result) {
            long now = SystemClock.uptimeMillis();
            long j = now;
            long j2 = now;
            int i2 = keyCode;
            KeyEvent eventDown = new KeyEvent(j, j2, 0, i2, 0, 0, -1, 0);
            InputManager.getInstance().injectInputEvent(eventDown, 0);
            KeyEvent keyEvent = new KeyEvent(j, j2, 1, i2, 0, 0, -1, 0);
            InputManager.getInstance().injectInputEvent(keyEvent, null);
            if (System.currentTimeMillis() - before > 50) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(str2);
                stringBuilder2.append(event);
                stringBuilder2.append(str);
                stringBuilder2.append(System.currentTimeMillis() - before);
                stringBuilder2.append(" ms during handle");
                Slog.e(str3, stringBuilder2.toString());
            }
            if (this.mWakeLock.isHeld()) {
                this.mWakeLock.release();
            }
            return;
        }
        if (this.mWakeLock.isHeld()) {
            this.mWakeLock.release();
        }
    }
}
