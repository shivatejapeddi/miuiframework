package android.media;

import android.app.NotificationManager;
import android.content.Context;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings.System;
import android.util.ArrayMap;
import android.util.Log;
import java.util.Map;

public class AudioStatusHandler {
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final long DELAY = 120000;
    public static final String KEY_PID = "pid";
    public static final String KEY_STATE = "state";
    public static final String KEY_USAGE = "usage";
    public static final String MODE_MASK_FOR_NOTIFY = "mode_mask_for_notify";
    public static final String MODE_MASK_FOR_NOTIFY_DEFAULT = "3";
    private static final int MSG_CANCEL_MODE_NOTIFICATION = 10002;
    private static final int MSG_SEND_MODE_NOTIFICATION = 10001;
    private static final String TAG = AudioStatusHandler.class.getSimpleName();
    private static AudioStatusHandler sAudioStatusHandler;
    private Map<Integer, AudioState> mAudioState = new ArrayMap();
    private Context mContext;
    private Map<Integer, DeviceState> mDeviceState = new ArrayMap();
    private Handler mHandle = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            synchronized (AudioStatusHandler.this.mStateLock) {
                for (AudioState audioState : AudioStatusHandler.this.mAudioState.values()) {
                    if (audioState.isActive()) {
                        audioState.onMessageReceived(msg);
                    }
                }
            }
        }
    };
    private final Object mStateLock = new Object();

    abstract class AudioState {
        final int DEFAULT_STATE;
        DeviceState mDeviceState;
        int mPid;
        long mStartTime;
        int mState;
        int mUsage;

        public abstract boolean onConfigChanaged();

        public abstract boolean onMessageReceived(Message message);

        public abstract void onStateChanged();

        public AudioState(AudioStatusHandler this$0, int usage) {
            this(usage, 0);
        }

        public AudioState(int usage, int defaultState) {
            this.DEFAULT_STATE = defaultState;
            this.mUsage = usage;
            this.mPid = 0;
            this.mStartTime = 0;
            this.mState = this.DEFAULT_STATE;
        }

        public boolean isActive() {
            return false;
        }

        public boolean updateDeviceState(DeviceState deviceState) {
            this.mDeviceState = deviceState;
            return true;
        }

        /* JADX WARNING: Missing block: B:20:0x004e, code skipped:
            return false;
     */
        public boolean update(android.os.Bundle r8) {
            /*
            r7 = this;
            r0 = 0;
            if (r8 == 0) goto L_0x004e;
        L_0x0003:
            r1 = r8.isEmpty();
            if (r1 == 0) goto L_0x000a;
        L_0x0009:
            goto L_0x004e;
        L_0x000a:
            r1 = r7.mUsage;
            r2 = "usage";
            r2 = r8.getInt(r2);
            if (r1 == r2) goto L_0x0016;
        L_0x0015:
            return r0;
        L_0x0016:
            r1 = "pid";
            r1 = r8.getInt(r1, r0);
            r2 = r7.DEFAULT_STATE;
            r3 = "state";
            r2 = r8.getInt(r3, r2);
            r3 = r7.mPid;
            if (r1 != r3) goto L_0x002f;
        L_0x002a:
            r3 = r7.mState;
            if (r2 != r3) goto L_0x002f;
        L_0x002e:
            return r0;
        L_0x002f:
            r7.mPid = r1;
            r7.mState = r2;
            r3 = r7.mStartTime;
            r5 = 0;
            r0 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
            if (r0 != 0) goto L_0x0041;
        L_0x003b:
            r3 = android.os.SystemClock.elapsedRealtime();
            r7.mStartTime = r3;
        L_0x0041:
            r0 = r7.mState;
            r3 = r7.DEFAULT_STATE;
            if (r0 != r3) goto L_0x0049;
        L_0x0047:
            r7.mStartTime = r5;
        L_0x0049:
            r7.onStateChanged();
            r0 = 1;
            return r0;
        L_0x004e:
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.media.AudioStatusHandler$AudioState.update(android.os.Bundle):boolean");
        }
    }

    class DeviceState {
        int mConfig = 0;
        int mPid = 0;
        String mReason = "";
        int mUid = 0;
        int mUsage;

        public DeviceState(int usage) {
            this.mUsage = usage;
        }

        public boolean update(int usage, int config, String eventSource) {
            if (usage != this.mUsage || config == this.mConfig) {
                return false;
            }
            this.mConfig = config;
            if (eventSource == null) {
                this.mReason = "";
                Log.w(AudioStatusHandler.TAG, "DeviceState update eventSource is null, but normal");
                return true;
            }
            String[] upInfo = eventSource.substring(eventSource.indexOf(58) + 1).split("/");
            try {
                this.mUid = Integer.valueOf(upInfo[0]).intValue();
                this.mPid = Integer.valueOf(upInfo[1]).intValue();
            } catch (Exception e) {
                this.mUid = 0;
                this.mPid = 0;
                Log.w(AudioStatusHandler.TAG, "DeviceState update failed to format uid & pid");
            }
            this.mReason = eventSource;
            if (AudioStatusHandler.DEBUG) {
                String access$200 = AudioStatusHandler.TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("DeviceState update ");
                stringBuilder.append(this.mUsage);
                String str = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
                stringBuilder.append(str);
                stringBuilder.append(this.mConfig);
                stringBuilder.append(str);
                stringBuilder.append(this.mUid);
                stringBuilder.append(str);
                stringBuilder.append(this.mPid);
                stringBuilder.append(str);
                stringBuilder.append(this.mReason);
                Log.e(access$200, stringBuilder.toString());
            }
            return true;
        }
    }

    class ModeState extends AudioState {
        private String mModeMask;

        public ModeState() {
            super(0, 0);
            this.mModeMask = System.getStringForUser(AudioStatusHandler.this.mContext.getContentResolver(), AudioStatusHandler.MODE_MASK_FOR_NOTIFY, -3);
            if (this.mModeMask == null) {
                this.mModeMask = "3";
            }
        }

        public boolean isActive() {
            return true;
        }

        private boolean showNotifyForMode() {
            if (AudioStatusHandler.DEBUG) {
                String access$200 = AudioStatusHandler.TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("showNotifyForMode mode ");
                stringBuilder.append(this.mState);
                stringBuilder.append(" modeString ");
                stringBuilder.append(this.mModeMask);
                Log.d(access$200, stringBuilder.toString());
            }
            if (this.mModeMask.contains(String.valueOf(this.mState))) {
                return true;
            }
            return false;
        }

        public boolean onMessageReceived(Message msg) {
            int i = msg.what;
            if (i == 10001) {
                sendModeNotification();
            } else if (i == 10002) {
                cancelModeNotification();
            }
            return false;
        }

        public void onStateChanged() {
            sendOrUpdateModeMsg();
        }

        public boolean onConfigChanaged() {
            sendOrUpdateModeMsg();
            return true;
        }

        private void sendOrUpdateModeMsg() {
            if (showNotifyForMode()) {
                long currentTime = SystemClock.elapsedRealtime();
                if (this.mStartTime == 0) {
                    this.mStartTime = currentTime;
                }
                long delay = AudioStatusHandler.DELAY - (currentTime - this.mStartTime);
                if (delay < 0) {
                    delay = 0;
                }
                AudioStatusHandler.this.sendMessage(10001, delay);
                return;
            }
            AudioStatusHandler.this.cancelMessage(10001);
            AudioStatusHandler.this.sendMessage(10002, 0);
        }

        private boolean isSpeakerOn() {
            return this.mDeviceState.mConfig == 1;
        }

        private void sendModeNotification() {
            String access$200;
            StringBuilder stringBuilder;
            if (showNotifyForMode()) {
                if (AudioStatusHandler.DEBUG) {
                    access$200 = AudioStatusHandler.TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("sendModeStatusNotification type ");
                    stringBuilder.append(this.mUsage);
                    stringBuilder.append(" pid ");
                    stringBuilder.append(this.mPid);
                    stringBuilder.append(" mode ");
                    stringBuilder.append(this.mState);
                    stringBuilder.append(" speakerOn ");
                    stringBuilder.append(isSpeakerOn());
                    Log.d(access$200, stringBuilder.toString());
                }
                AudioStatusNotify.sendAudioStatusNotification(AudioStatusHandler.this.mContext, this.mPid, isSpeakerOn());
                return;
            }
            access$200 = AudioStatusHandler.TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("sendModeStatusNotification not communication mode ");
            stringBuilder.append(this.mState);
            Log.w(access$200, stringBuilder.toString());
        }

        private void cancelModeNotification() {
            ((NotificationManager) AudioStatusHandler.this.mContext.getSystemService("notification")).cancel(10001);
            if (AudioStatusHandler.DEBUG) {
                Log.d(AudioStatusHandler.TAG, "cancelAudioStatusNotification id 10001");
            }
        }
    }

    private AudioStatusHandler(Context context) {
        this.mContext = context;
    }

    public static AudioStatusHandler getInstance(Context context) {
        if (sAudioStatusHandler == null) {
            synchronized (AudioStatusHandler.class) {
                if (sAudioStatusHandler == null) {
                    sAudioStatusHandler = new AudioStatusHandler(context);
                }
            }
        }
        return sAudioStatusHandler;
    }

    public void handleAudioStatusChanged(Bundle bundle) {
        if (bundle != null && !bundle.isEmpty()) {
            int usage = bundle.getInt(KEY_USAGE);
            AudioState audioState = addAudioState(usage);
            String str;
            StringBuilder stringBuilder;
            if (audioState == null || !audioState.update(bundle)) {
                if (DEBUG) {
                    str = TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("handleAudioStatusChanged no update for ");
                    stringBuilder.append(usage);
                    Log.w(str, stringBuilder.toString());
                }
                return;
            }
            if (DEBUG) {
                str = TAG;
                stringBuilder = new StringBuilder();
                stringBuilder.append("handleAudioStatusChanged type ");
                stringBuilder.append(audioState.mUsage);
                stringBuilder.append(" pid ");
                stringBuilder.append(audioState.mPid);
                stringBuilder.append(" state ");
                stringBuilder.append(audioState.mState);
                Log.d(str, stringBuilder.toString());
            }
        }
    }

    public void handleSetForceUse(int usage, int config, String eventSource) {
        DeviceState deviceState = addDeviceState(usage);
        String str;
        if (deviceState == null || !deviceState.update(usage, config, eventSource)) {
            if (DEBUG) {
                str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("handleSetForceUse no update for ");
                stringBuilder.append(usage);
                Log.w(str, stringBuilder.toString());
            }
            return;
        }
        if (DEBUG) {
            str = new StringBuilder("handleSetForceUse(");
            str.append(usage);
            str.append(", ");
            str.append(config);
            str.append(") due to ");
            str.append(eventSource);
            str = str.toString();
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("handleSetForceUse type ");
            stringBuilder2.append(str);
            Log.d(str2, stringBuilder2.toString());
        }
        AudioState audioState = (AudioState) this.mAudioState.get(Integer.valueOf(usage));
        if (audioState != null) {
            audioState.updateDeviceState(deviceState);
            audioState.onConfigChanaged();
        }
    }

    private AudioState addAudioState(int usage) {
        AudioState audioState = (AudioState) this.mAudioState.get(Integer.valueOf(usage));
        if (audioState != null) {
            return audioState;
        }
        if (usage == 0) {
            audioState = new ModeState();
        }
        if (audioState == null) {
            return null;
        }
        DeviceState deviceState = (DeviceState) this.mDeviceState.get(Integer.valueOf(usage));
        if (deviceState == null) {
            deviceState = addDeviceState(usage);
        }
        audioState.updateDeviceState(deviceState);
        synchronized (this.mStateLock) {
            this.mAudioState.put(Integer.valueOf(usage), audioState);
            this.mDeviceState.put(Integer.valueOf(usage), deviceState);
        }
        return audioState;
    }

    private DeviceState addDeviceState(int usage) {
        DeviceState deviceState = (DeviceState) this.mDeviceState.get(Integer.valueOf(usage));
        if (deviceState != null) {
            return deviceState;
        }
        deviceState = new DeviceState(usage);
        synchronized (this.mStateLock) {
            this.mDeviceState.put(Integer.valueOf(usage), deviceState);
        }
        return deviceState;
    }

    private void cancelMessage(int msg) {
        if (this.mHandle.hasMessages(msg)) {
            this.mHandle.removeMessages(msg);
        }
    }

    private void sendMessage(int msg, long delay) {
        cancelMessage(msg);
        Handler handler = this.mHandle;
        handler.sendMessageDelayed(handler.obtainMessage(msg), delay);
    }
}
