package android.preference;

import android.annotation.UnsupportedAppUsage;
import android.app.NotificationManager;
import android.app.NotificationManager.Policy;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.media.AudioAttributes;
import android.media.AudioAttributes.Builder;
import android.media.AudioManager;
import android.media.AudioManager.VolumeGroupCallback;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.audiopolicy.AudioProductStrategy;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.preference.VolumePreference.VolumeStore;
import android.provider.Settings.System;
import android.service.notification.ZenModeConfig;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.os.SomeArgs;

@Deprecated
public class SeekBarVolumizer implements OnSeekBarChangeListener, android.os.Handler.Callback {
    private static final int CHECK_RINGTONE_PLAYBACK_DELAY_MS = 1000;
    private static final int MSG_GROUP_VOLUME_CHANGED = 1;
    private static final int MSG_INIT_SAMPLE = 3;
    private static final int MSG_SET_STREAM_VOLUME = 0;
    private static final int MSG_START_SAMPLE = 1;
    private static final int MSG_STOP_SAMPLE = 2;
    private static final String TAG = "SeekBarVolumizer";
    private boolean mAffectedByRingerMode;
    private boolean mAllowAlarms;
    private boolean mAllowMedia;
    private boolean mAllowRinger;
    private AudioAttributes mAttributes;
    @UnsupportedAppUsage
    private final AudioManager mAudioManager;
    private final Callback mCallback;
    @UnsupportedAppUsage
    private final Context mContext;
    private final Uri mDefaultUri;
    private Handler mHandler;
    private int mLastAudibleStreamVolume;
    @UnsupportedAppUsage
    private int mLastProgress;
    private final int mMaxStreamVolume;
    private boolean mMuted;
    private final NotificationManager mNotificationManager;
    private boolean mNotificationOrRing;
    private Policy mNotificationPolicy;
    @UnsupportedAppUsage
    private int mOriginalStreamVolume;
    private boolean mPlaySample;
    private final Receiver mReceiver;
    private int mRingerMode;
    @GuardedBy({"this"})
    @UnsupportedAppUsage
    private Ringtone mRingtone;
    @UnsupportedAppUsage
    private SeekBar mSeekBar;
    @UnsupportedAppUsage
    private final int mStreamType;
    private final H mUiHandler;
    private int mVolumeBeforeMute;
    private final VolumeGroupCallback mVolumeGroupCallback;
    private int mVolumeGroupId;
    private final Handler mVolumeHandler;
    private Observer mVolumeObserver;
    private int mZenMode;

    public interface Callback {
        void onMuted(boolean z, boolean z2);

        void onProgressChanged(SeekBar seekBar, int i, boolean z);

        void onSampleStarting(SeekBarVolumizer seekBarVolumizer);
    }

    private final class H extends Handler {
        private static final int UPDATE_SLIDER = 1;

        private H() {
        }

        /* synthetic */ H(SeekBarVolumizer x0, AnonymousClass1 x1) {
            this();
        }

        public void handleMessage(Message msg) {
            if (msg.what == 1 && SeekBarVolumizer.this.mSeekBar != null) {
                SeekBarVolumizer.this.mLastProgress = msg.arg1;
                SeekBarVolumizer.this.mLastAudibleStreamVolume = msg.arg2;
                boolean muted = ((Boolean) msg.obj).booleanValue();
                if (muted != SeekBarVolumizer.this.mMuted) {
                    SeekBarVolumizer.this.mMuted = muted;
                    if (SeekBarVolumizer.this.mCallback != null) {
                        SeekBarVolumizer.this.mCallback.onMuted(SeekBarVolumizer.this.mMuted, SeekBarVolumizer.this.isZenMuted());
                    }
                }
                SeekBarVolumizer.this.updateSeekBar();
            }
        }

        public void postUpdateSlider(int volume, int lastAudibleVolume, boolean mute) {
            obtainMessage(1, volume, lastAudibleVolume, new Boolean(mute)).sendToTarget();
        }
    }

    private final class Observer extends ContentObserver {
        public Observer(Handler handler) {
            super(handler);
        }

        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            SeekBarVolumizer.this.updateSlider();
        }
    }

    private final class Receiver extends BroadcastReceiver {
        private boolean mListening;

        private Receiver() {
        }

        /* synthetic */ Receiver(SeekBarVolumizer x0, AnonymousClass1 x1) {
            this();
        }

        public void setListening(boolean listening) {
            if (this.mListening != listening) {
                this.mListening = listening;
                if (listening) {
                    IntentFilter filter = new IntentFilter(AudioManager.VOLUME_CHANGED_ACTION);
                    filter.addAction(AudioManager.INTERNAL_RINGER_MODE_CHANGED_ACTION);
                    filter.addAction(NotificationManager.ACTION_INTERRUPTION_FILTER_CHANGED);
                    filter.addAction(NotificationManager.ACTION_NOTIFICATION_POLICY_CHANGED);
                    filter.addAction(AudioManager.STREAM_DEVICES_CHANGED_ACTION);
                    SeekBarVolumizer.this.mContext.registerReceiver(this, filter);
                } else {
                    SeekBarVolumizer.this.mContext.unregisterReceiver(this);
                }
            }
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            boolean equals = AudioManager.VOLUME_CHANGED_ACTION.equals(action);
            String str = AudioManager.EXTRA_VOLUME_STREAM_TYPE;
            int streamType;
            int streamValue;
            SeekBarVolumizer seekBarVolumizer;
            if (equals) {
                streamType = intent.getIntExtra(str, -1);
                streamValue = intent.getIntExtra(AudioManager.EXTRA_VOLUME_STREAM_VALUE, -1);
                if (SeekBarVolumizer.this.hasAudioProductStrategies()) {
                    updateVolumeSlider(streamType, streamValue);
                }
            } else if (AudioManager.INTERNAL_RINGER_MODE_CHANGED_ACTION.equals(action)) {
                if (SeekBarVolumizer.this.mNotificationOrRing) {
                    seekBarVolumizer = SeekBarVolumizer.this;
                    seekBarVolumizer.mRingerMode = seekBarVolumizer.mAudioManager.getRingerModeInternal();
                }
                if (SeekBarVolumizer.this.mAffectedByRingerMode) {
                    SeekBarVolumizer.this.updateSlider();
                }
            } else if (AudioManager.STREAM_DEVICES_CHANGED_ACTION.equals(action)) {
                streamType = intent.getIntExtra(str, -1);
                if (SeekBarVolumizer.this.hasAudioProductStrategies()) {
                    updateVolumeSlider(streamType, SeekBarVolumizer.this.mAudioManager.getStreamVolume(streamType));
                    return;
                }
                streamValue = SeekBarVolumizer.this.getVolumeGroupIdForLegacyStreamType(streamType);
                if (streamValue != -1 && streamValue == SeekBarVolumizer.this.mVolumeGroupId) {
                    updateVolumeSlider(streamType, SeekBarVolumizer.this.mAudioManager.getStreamVolume(streamType));
                }
            } else if (NotificationManager.ACTION_INTERRUPTION_FILTER_CHANGED.equals(action)) {
                seekBarVolumizer = SeekBarVolumizer.this;
                seekBarVolumizer.mZenMode = seekBarVolumizer.mNotificationManager.getZenMode();
                SeekBarVolumizer.this.updateSlider();
            } else if (NotificationManager.ACTION_NOTIFICATION_POLICY_CHANGED.equals(action)) {
                seekBarVolumizer = SeekBarVolumizer.this;
                seekBarVolumizer.mNotificationPolicy = seekBarVolumizer.mNotificationManager.getConsolidatedNotificationPolicy();
                seekBarVolumizer = SeekBarVolumizer.this;
                boolean z = false;
                seekBarVolumizer.mAllowAlarms = (seekBarVolumizer.mNotificationPolicy.priorityCategories & 32) != 0;
                seekBarVolumizer = SeekBarVolumizer.this;
                if ((seekBarVolumizer.mNotificationPolicy.priorityCategories & 64) != 0) {
                    z = true;
                }
                seekBarVolumizer.mAllowMedia = z;
                seekBarVolumizer = SeekBarVolumizer.this;
                seekBarVolumizer.mAllowRinger = ZenModeConfig.areAllPriorityOnlyNotificationZenSoundsMuted(seekBarVolumizer.mNotificationPolicy) ^ 1;
                SeekBarVolumizer.this.updateSlider();
            }
        }

        private void updateVolumeSlider(int streamType, int streamValue) {
            boolean muted = true;
            boolean streamMatch = SeekBarVolumizer.this.mNotificationOrRing ? SeekBarVolumizer.isNotificationOrRing(streamType) : streamType == SeekBarVolumizer.this.mStreamType;
            if (SeekBarVolumizer.this.mSeekBar != null && streamMatch && streamValue != -1) {
                if (!(SeekBarVolumizer.this.mAudioManager.isStreamMute(SeekBarVolumizer.this.mStreamType) || streamValue == 0)) {
                    muted = false;
                }
                SeekBarVolumizer.this.mUiHandler.postUpdateSlider(streamValue, SeekBarVolumizer.this.mLastAudibleStreamVolume, muted);
            }
        }
    }

    private class VolumeHandler extends Handler {
        private VolumeHandler() {
        }

        /* synthetic */ VolumeHandler(SeekBarVolumizer x0, AnonymousClass1 x1) {
            this();
        }

        public void handleMessage(Message msg) {
            SomeArgs args = msg.obj;
            if (msg.what == 1 && SeekBarVolumizer.this.mVolumeGroupId == ((Integer) args.arg1).intValue() && SeekBarVolumizer.this.mVolumeGroupId != -1) {
                SeekBarVolumizer.this.updateSlider();
            }
        }
    }

    @UnsupportedAppUsage
    public SeekBarVolumizer(Context context, int streamType, Uri defaultUri, Callback callback) {
        this(context, streamType, defaultUri, callback, true);
    }

    public SeekBarVolumizer(Context context, int streamType, Uri defaultUri, Callback callback, boolean playSample) {
        this.mVolumeHandler = new VolumeHandler(this, null);
        this.mVolumeGroupCallback = new VolumeGroupCallback() {
            public void onAudioVolumeGroupChanged(int group, int flags) {
                if (SeekBarVolumizer.this.mHandler != null) {
                    SomeArgs args = SomeArgs.obtain();
                    args.arg1 = Integer.valueOf(group);
                    args.arg2 = Integer.valueOf(flags);
                    SeekBarVolumizer.this.mVolumeHandler.sendMessage(SeekBarVolumizer.this.mHandler.obtainMessage(1, args));
                }
            }
        };
        this.mUiHandler = new H(this, null);
        this.mReceiver = new Receiver(this, null);
        this.mLastProgress = -1;
        this.mVolumeBeforeMute = -1;
        this.mContext = context;
        this.mAudioManager = (AudioManager) context.getSystemService(AudioManager.class);
        this.mNotificationManager = (NotificationManager) context.getSystemService(NotificationManager.class);
        this.mNotificationPolicy = this.mNotificationManager.getConsolidatedNotificationPolicy();
        boolean z = false;
        this.mAllowAlarms = (this.mNotificationPolicy.priorityCategories & 32) != 0;
        if ((this.mNotificationPolicy.priorityCategories & 64) != 0) {
            z = true;
        }
        this.mAllowMedia = z;
        this.mAllowRinger = ZenModeConfig.areAllPriorityOnlyNotificationZenSoundsMuted(this.mNotificationPolicy) ^ 1;
        this.mStreamType = streamType;
        this.mAffectedByRingerMode = this.mAudioManager.isStreamAffectedByRingerMode(this.mStreamType);
        this.mNotificationOrRing = isNotificationOrRing(this.mStreamType);
        if (this.mNotificationOrRing) {
            this.mRingerMode = this.mAudioManager.getRingerModeInternal();
        }
        this.mZenMode = this.mNotificationManager.getZenMode();
        if (hasAudioProductStrategies()) {
            this.mVolumeGroupId = getVolumeGroupIdForLegacyStreamType(this.mStreamType);
            this.mAttributes = getAudioAttributesForLegacyStreamType(this.mStreamType);
        }
        this.mMaxStreamVolume = this.mAudioManager.getStreamMaxVolume(this.mStreamType);
        this.mCallback = callback;
        this.mOriginalStreamVolume = this.mAudioManager.getStreamVolume(this.mStreamType);
        this.mLastAudibleStreamVolume = this.mAudioManager.getLastAudibleStreamVolume(this.mStreamType);
        this.mMuted = this.mAudioManager.isStreamMute(this.mStreamType);
        this.mPlaySample = playSample;
        Callback callback2 = this.mCallback;
        if (callback2 != null) {
            callback2.onMuted(this.mMuted, isZenMuted());
        }
        if (defaultUri == null) {
            int i = this.mStreamType;
            if (i == 2) {
                defaultUri = System.DEFAULT_RINGTONE_URI;
            } else if (i == 5) {
                defaultUri = System.DEFAULT_NOTIFICATION_URI;
            } else {
                defaultUri = System.DEFAULT_ALARM_ALERT_URI;
            }
        }
        this.mDefaultUri = defaultUri;
    }

    private boolean hasAudioProductStrategies() {
        return AudioManager.getAudioProductStrategies().size() > 0;
    }

    private int getVolumeGroupIdForLegacyStreamType(int streamType) {
        for (AudioProductStrategy productStrategy : AudioManager.getAudioProductStrategies()) {
            int volumeGroupId = productStrategy.getVolumeGroupIdForLegacyStreamType(streamType);
            if (volumeGroupId != -1) {
                return volumeGroupId;
            }
        }
        return ((Integer) AudioManager.getAudioProductStrategies().stream().map(-$$Lambda$SeekBarVolumizer$ezNr2aLs33rVlzIsAVW8OXqqpIs.INSTANCE).filter(-$$Lambda$SeekBarVolumizer$pv2-5S-FjgAtIix6Vp68yZJoqvQ.INSTANCE).findFirst().orElse(Integer.valueOf(-1))).intValue();
    }

    static /* synthetic */ boolean lambda$getVolumeGroupIdForLegacyStreamType$1(Integer volumeGroupId) {
        return volumeGroupId.intValue() != -1;
    }

    private AudioAttributes getAudioAttributesForLegacyStreamType(int streamType) {
        for (AudioProductStrategy productStrategy : AudioManager.getAudioProductStrategies()) {
            AudioAttributes aa = productStrategy.getAudioAttributesForLegacyStreamType(streamType);
            if (aa != null) {
                return aa;
            }
        }
        return new Builder().setContentType(0).setUsage(0).build();
    }

    private static boolean isNotificationOrRing(int stream) {
        return stream == 2 || stream == 5;
    }

    private static boolean isAlarmsStream(int stream) {
        return stream == 4;
    }

    private static boolean isMediaStream(int stream) {
        return stream == 3;
    }

    public void setSeekBar(SeekBar seekBar) {
        SeekBar seekBar2 = this.mSeekBar;
        if (seekBar2 != null) {
            seekBar2.setOnSeekBarChangeListener(null);
        }
        this.mSeekBar = seekBar;
        this.mSeekBar.setOnSeekBarChangeListener(null);
        this.mSeekBar.setMax(this.mMaxStreamVolume);
        updateSeekBar();
        this.mSeekBar.setOnSeekBarChangeListener(this);
    }

    private boolean isZenMuted() {
        if (this.mNotificationOrRing && this.mZenMode == 3) {
            return true;
        }
        int i = this.mZenMode;
        if (i == 2) {
            return true;
        }
        if (i == 1) {
            if (!this.mAllowAlarms && isAlarmsStream(this.mStreamType)) {
                return true;
            }
            if (!this.mAllowMedia && isMediaStream(this.mStreamType)) {
                return true;
            }
            if (!this.mAllowRinger && isNotificationOrRing(this.mStreamType)) {
                return true;
            }
        }
        return false;
    }

    /* Access modifiers changed, original: protected */
    public void updateSeekBar() {
        boolean zenMuted = isZenMuted();
        this.mSeekBar.setEnabled(zenMuted ^ 1);
        if (zenMuted) {
            this.mSeekBar.setProgress(this.mLastAudibleStreamVolume, true);
        } else if (this.mNotificationOrRing && this.mRingerMode == 1) {
            this.mSeekBar.setProgress(0, true);
        } else if (this.mMuted) {
            this.mSeekBar.setProgress(0, true);
        } else {
            SeekBar seekBar = this.mSeekBar;
            int i = this.mLastProgress;
            if (i <= -1) {
                i = this.mOriginalStreamVolume;
            }
            seekBar.setProgress(i, true);
        }
    }

    public boolean handleMessage(Message msg) {
        int i = msg.what;
        if (i == 0) {
            if (this.mMuted && this.mLastProgress > 0) {
                this.mAudioManager.adjustStreamVolume(this.mStreamType, 100, 0);
            } else if (!this.mMuted && this.mLastProgress == 0) {
                this.mAudioManager.adjustStreamVolume(this.mStreamType, -100, 0);
            }
            this.mAudioManager.setStreamVolume(this.mStreamType, this.mLastProgress, 1024);
        } else if (i != 1) {
            if (i != 2) {
                if (i != 3) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("invalid SeekBarVolumizer message: ");
                    stringBuilder.append(msg.what);
                    Log.e(TAG, stringBuilder.toString());
                } else if (this.mPlaySample) {
                    onInitSample();
                }
            } else if (this.mPlaySample) {
                onStopSample();
            }
        } else if (this.mPlaySample) {
            onStartSample();
        }
        return true;
    }

    private void onInitSample() {
        synchronized (this) {
            this.mRingtone = RingtoneManager.getRingtone(this.mContext, this.mDefaultUri);
            if (this.mRingtone != null) {
                this.mRingtone.setStreamType(this.mStreamType);
            }
        }
    }

    private void postStartSample() {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeMessages(1);
            handler = this.mHandler;
            handler.sendMessageDelayed(handler.obtainMessage(1), isSamplePlaying() ? 1000 : 0);
        }
    }

    private void onStartSample() {
        if (!isSamplePlaying()) {
            Callback callback = this.mCallback;
            if (callback != null) {
                callback.onSampleStarting(this);
            }
            synchronized (this) {
                if (this.mRingtone != null) {
                    try {
                        this.mRingtone.setAudioAttributes(new Builder(this.mRingtone.getAudioAttributes()).setFlags(128).build());
                        this.mRingtone.play();
                    } catch (Throwable e) {
                        String str = TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Error playing ringtone, stream ");
                        stringBuilder.append(this.mStreamType);
                        Log.w(str, stringBuilder.toString(), e);
                    }
                }
            }
        }
    }

    private void postStopSample() {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeMessages(1);
            this.mHandler.removeMessages(2);
            handler = this.mHandler;
            handler.sendMessage(handler.obtainMessage(2));
        }
    }

    private void onStopSample() {
        synchronized (this) {
            if (this.mRingtone != null) {
                this.mRingtone.stop();
            }
        }
    }

    @UnsupportedAppUsage
    public void stop() {
        if (this.mHandler != null) {
            postStopSample();
            this.mContext.getContentResolver().unregisterContentObserver(this.mVolumeObserver);
            this.mReceiver.setListening(false);
            if (hasAudioProductStrategies()) {
                unregisterVolumeGroupCb();
            }
            this.mSeekBar.setOnSeekBarChangeListener(null);
            this.mHandler.getLooper().quitSafely();
            this.mHandler = null;
            this.mVolumeObserver = null;
        }
    }

    public void start() {
        if (this.mHandler == null) {
            HandlerThread thread = new HandlerThread("SeekBarVolumizer.CallbackHandler");
            thread.start();
            this.mHandler = new Handler(thread.getLooper(), (android.os.Handler.Callback) this);
            this.mHandler.sendEmptyMessage(3);
            this.mVolumeObserver = new Observer(this.mHandler);
            this.mContext.getContentResolver().registerContentObserver(System.getUriFor(System.VOLUME_SETTINGS_INT[this.mStreamType]), false, this.mVolumeObserver);
            this.mReceiver.setListening(true);
            if (hasAudioProductStrategies()) {
                registerVolumeGroupCb();
            }
        }
    }

    public void revertVolume() {
        this.mAudioManager.setStreamVolume(this.mStreamType, this.mOriginalStreamVolume, 0);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
        if (fromTouch) {
            postSetVolume(progress);
        }
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onProgressChanged(seekBar, progress, fromTouch);
        }
    }

    private void postSetVolume(int progress) {
        Handler handler = this.mHandler;
        if (handler != null) {
            this.mLastProgress = progress;
            handler.removeMessages(0);
            handler = this.mHandler;
            handler.sendMessage(handler.obtainMessage(0));
        }
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        postStartSample();
    }

    public boolean isSamplePlaying() {
        boolean z;
        synchronized (this) {
            z = this.mRingtone != null && this.mRingtone.isPlaying();
        }
        return z;
    }

    public void startSample() {
        postStartSample();
    }

    public void stopSample() {
        postStopSample();
    }

    public SeekBar getSeekBar() {
        return this.mSeekBar;
    }

    public void changeVolumeBy(int amount) {
        this.mSeekBar.incrementProgressBy(amount);
        postSetVolume(this.mSeekBar.getProgress());
        postStartSample();
        this.mVolumeBeforeMute = -1;
    }

    public void muteVolume() {
        int i = this.mVolumeBeforeMute;
        if (i != -1) {
            this.mSeekBar.setProgress(i, true);
            postSetVolume(this.mVolumeBeforeMute);
            postStartSample();
            this.mVolumeBeforeMute = -1;
            return;
        }
        this.mVolumeBeforeMute = this.mSeekBar.getProgress();
        this.mSeekBar.setProgress(0, true);
        postStopSample();
        postSetVolume(0);
    }

    public void onSaveInstanceState(VolumeStore volumeStore) {
        int i = this.mLastProgress;
        if (i >= 0) {
            volumeStore.volume = i;
            volumeStore.originalVolume = this.mOriginalStreamVolume;
        }
    }

    public void onRestoreInstanceState(VolumeStore volumeStore) {
        if (volumeStore.volume != -1) {
            this.mOriginalStreamVolume = volumeStore.originalVolume;
            this.mLastProgress = volumeStore.volume;
            postSetVolume(this.mLastProgress);
        }
    }

    private void updateSlider() {
        if (this.mSeekBar != null) {
            int volume = this.mAudioManager;
            if (volume != 0) {
                this.mUiHandler.postUpdateSlider(volume.getStreamVolume(this.mStreamType), this.mAudioManager.getLastAudibleStreamVolume(this.mStreamType), this.mAudioManager.isStreamMute(this.mStreamType));
            }
        }
    }

    private void registerVolumeGroupCb() {
        if (this.mVolumeGroupId != -1) {
            this.mAudioManager.registerVolumeGroupCallback(-$$Lambda$_14QHG018Z6p13d3hzJuGTWnNeo.INSTANCE, this.mVolumeGroupCallback);
            updateSlider();
        }
    }

    private void unregisterVolumeGroupCb() {
        if (this.mVolumeGroupId != -1) {
            this.mAudioManager.unregisterVolumeGroupCallback(this.mVolumeGroupCallback);
        }
    }
}
