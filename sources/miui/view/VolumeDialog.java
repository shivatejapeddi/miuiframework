package miui.view;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.AudioSystem;
import android.miui.R;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.UserHandle;
import android.os.Vibrator;
import android.provider.MiuiSettings.SilenceMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.miui.whetstone.Event.EventLogTags;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import miui.util.AudioManagerHelper;
import miui.util.CustomizeUtil;
import miui.widget.SeekBar;

public class VolumeDialog {
    private static final int LAYOUT_TRANSITION_ANIMATION_DURATION = 200;
    private static final String STREAM_DEVICES_CHANGED_ACTION = "android.media.STREAM_DEVICES_CHANGED_ACTION";
    private static final String STREAM_MUTE_CHANGED_ACTION = "android.media.STREAM_MUTE_CHANGED_ACTION";
    private static final String TAG = "VolumeDialog";
    private static final int TYPE_FM = 10;
    private static final int UPDATE_ANIMATION_DURATION = 80;
    private static final long USER_ATTEMPT_GRACE_PERIOD = 1000;
    private static final int VIBRATE_DELAY = 300;
    private static final int VOLUME_ICON_TYPE_ALARM = 0;
    private static final int VOLUME_ICON_TYPE_BLUETOOTH = 1;
    private static final int VOLUME_ICON_TYPE_FM = 8;
    private static final int VOLUME_ICON_TYPE_HEADSET = 2;
    private static final int VOLUME_ICON_TYPE_HIFI = 7;
    private static final int VOLUME_ICON_TYPE_MEDIA = 3;
    private static final int VOLUME_ICON_TYPE_PHONE = 4;
    private static final int VOLUME_ICON_TYPE_RING = 5;
    private static final int VOLUME_ICON_TYPE_SPEAKER = 6;
    private static final Map<Integer, VolumeIconRes> sVolumeIconTypeMap = new HashMap();
    static VolumeSeekbarProgress sVolumeSeekbarProgress = new VolumeSeekbarProgress(R.drawable.volume_seekbar_progress, R.drawable.volume_seekbar_progress_d, null);
    private final int[] STREAM_VOLUME_ALIAS_DEFAULT = new int[]{0, 2, 2, 3, 4, 2, 6, 2, 2, 3, 10};
    private int mActiveStream;
    private final AudioManager mAm;
    private BroadcastReceiver mBootBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (AudioManager.RINGER_MODE_CHANGED_ACTION.equals(intent.getAction())) {
                int ringerMode = intent.getIntExtra(AudioManager.EXTRA_RINGER_MODE, -1);
                if (VolumeDialog.this.mRingerMode != ringerMode) {
                    if (VolumeDialog.this.mRingerMode != -1 && ringerMode == 1) {
                        VolumeDialog.this.mHandler.sendMessageDelayed(VolumeDialog.this.mHandler.obtainMessage(10), 300);
                    }
                    VolumeDialog.this.mRingerMode = ringerMode;
                }
            }
        }
    };
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_SCREEN_OFF.equals(action) || Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)) {
                VolumeDialog.this.dismiss();
            } else if ("miui.intent.TAKE_SCREENSHOT".equals(action)) {
                if (intent.getBooleanExtra("IsFinished", true)) {
                    VolumeDialog.this.mHandler.removeMessages(11);
                    VolumeDialog.this.mInScreenshot = false;
                    return;
                }
                VolumeDialog.this.mInScreenshot = true;
                VolumeDialog.this.mHandler.sendEmptyMessageDelayed(11, 500);
                if (SystemClock.uptimeMillis() - VolumeDialog.this.mDialogShowTime < 500) {
                    VolumeDialog.this.dismiss();
                }
            } else if ("android.media.STREAM_DEVICES_CHANGED_ACTION".equals(action)) {
                VolumeDialog.this.streamDeviceChanged(intent.getIntExtra(AudioManager.EXTRA_VOLUME_STREAM_TYPE, -1));
            }
        }
    };
    private final OnClickListener mClickExpand = new OnClickListener() {
        public void onClick(View v) {
            if (!VolumeDialog.this.mExpandAnimating) {
                VolumeDialog volumeDialog = VolumeDialog.this;
                boolean z = true;
                volumeDialog.setExpandedH(volumeDialog.mExpanded ^ 1);
                if (VolumeDialog.this.mExpanded) {
                    volumeDialog = VolumeDialog.this;
                    volumeDialog.mLastStatus = volumeDialog.mRingerModeLayout.mSilenceModeExpanded;
                }
                RingerModeLayout access$3500 = VolumeDialog.this.mRingerModeLayout;
                if (VolumeDialog.this.mExpanded || !VolumeDialog.this.mLastStatus) {
                    z = false;
                }
                access$3500.expandSilenceModeContent(z);
            }
        }
    };
    private long mCollapseTime;
    private final Context mContext;
    private VolumePanelDelegate mDelegate;
    private CustomDialog mDialog;
    private ViewGroup mDialogContentView;
    private long mDialogShowTime = -1;
    private ViewGroup mDialogView;
    public boolean mExpandAnimating;
    private ValueAnimator mExpandAnimator;
    private ImageButton mExpandButton;
    public boolean mExpanded;
    private final H mHandler = new H();
    private boolean mInScreenshot = false;
    private boolean mLastStatus;
    private LayoutTransition mLayoutTransition;
    private int mRingerMode = -1;
    private RingerModeLayout mRingerModeLayout;
    private final List<VolumeRow> mRows = new ArrayList();
    private AlertDialog mSafetyWarning;
    private final Object mSafetyWarningLock = new Object();
    private boolean mShowing;
    private int mStatusBarHeight = -1;
    private final Vibrator mVibrator;
    private final List<View> mVolumeRowSpaces = new ArrayList();
    private final List<View> mVolumeRowViews = new ArrayList();

    private final class CustomDialog extends Dialog {
        private float mDownX;
        private float mDownY;

        public CustomDialog(Context context) {
            super(context);
        }

        public boolean dispatchTouchEvent(MotionEvent ev) {
            Rect r = new Rect();
            if (VolumeDialog.this.mDialogView != null) {
                VolumeDialog.this.mDialogView.getHitRect(r);
            }
            if (ev.getAction() != 0 || r.contains((int) ev.getX(), (int) ev.getY())) {
                VolumeDialog.this.rescheduleTimeoutH();
            } else {
                VolumeDialog.this.dismissH();
            }
            return super.dispatchTouchEvent(ev);
        }

        /* Access modifiers changed, original: protected */
        public void onStart() {
            super.onStart();
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction("android.media.STREAM_DEVICES_CHANGED_ACTION");
            filter.addAction("android.media.STREAM_MUTE_CHANGED_ACTION");
            filter.addAction("miui.intent.TAKE_SCREENSHOT");
            VolumeDialog.this.mContext.registerReceiverAsUser(VolumeDialog.this.mBroadcastReceiver, UserHandle.ALL, filter, null, null);
        }

        /* Access modifiers changed, original: protected */
        public void onStop() {
            super.onStop();
            VolumeDialog.this.mContext.unregisterReceiver(VolumeDialog.this.mBroadcastReceiver);
            VolumeDialog.this.mHandler.sendEmptyMessage(4);
        }

        public boolean onTouchEvent(MotionEvent event) {
            if (isShowing()) {
                if (event.getAction() == 4) {
                    VolumeDialog.this.dismissH();
                    return true;
                } else if (event.getActionMasked() == 0) {
                    this.mDownX = event.getX();
                    this.mDownY = event.getY();
                } else if (event.getActionMasked() == 2) {
                    float diffy = this.mDownY - event.getY();
                    if (Math.abs(this.mDownX - event.getX()) < diffy && diffy > ((float) ViewConfiguration.get(VolumeDialog.this.mContext).getScaledTouchSlop())) {
                        VolumeDialog.this.dismissH();
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private final class H extends Handler {
        private static final int DISMISS = 2;
        private static final int RECHECK = 3;
        private static final int RECHECK_ALL = 4;
        private static final int RESCHEDULE_TIMEOUT = 5;
        private static final int RESET_SCREENSHOT = 11;
        private static final int SHOW = 1;
        private static final int SHOW_SAFE_WARNING = 9;
        private static final int STATE_CHANGED = 6;
        private static final int UPDATE_BOTTOM_MARGIN = 7;
        private static final int UPDATE_LAYOUT_DIRECTION = 8;
        private static final int VIBRATE = 10;

        public H() {
            super(Looper.getMainLooper());
        }

        public void handleMessage(Message msg) {
            int i = msg.what;
            String str = VolumeDialog.TAG;
            switch (i) {
                case 1:
                    Log.d(str, "SHOW");
                    VolumeDialog.this.showH(msg.arg1);
                    return;
                case 2:
                    Log.d(str, "DISMISS");
                    VolumeDialog.this.dismissH();
                    return;
                case 3:
                    Log.d(str, "RECHECK");
                    VolumeDialog.this.recheckH((VolumeRow) msg.obj);
                    return;
                case 4:
                    Log.d(str, "RECHECK_ALL");
                    VolumeDialog.this.recheckH(null);
                    return;
                case 5:
                    Log.d(str, "RESCHEDULE_TIMEOUT");
                    VolumeDialog.this.rescheduleTimeoutH();
                    return;
                case 6:
                    Log.d(str, "STATE_CHANGED");
                    VolumeDialog.this.stateChangedH(msg.arg1, msg.arg2);
                    return;
                case 7:
                    Log.d(str, "UPDATE_BOTTOM_MARGIN");
                    VolumeDialog.this.updateDialogBottomMarginH();
                    return;
                case 8:
                    Log.d(str, "UPDATE_LAYOUT_DIRECTION");
                    VolumeDialog.this.updateLayoutDirectionH(msg.arg1);
                    return;
                case 9:
                    Log.d(str, "SHOW_SAFE_WARNING");
                    VolumeDialog.this.showSafetyWarningH(msg.arg1);
                    return;
                case 10:
                    Log.d(str, "VIBRATE");
                    VolumeDialog.this.vibrateH();
                    return;
                case 11:
                    Log.d(str, "RESET_SCREENSHOT");
                    VolumeDialog.this.mInScreenshot = false;
                    return;
                default:
                    return;
            }
        }
    }

    private final class SafetyWarningDialog extends AlertDialog {
        public SafetyWarningDialog(Context context) {
            super(context);
            getWindow().setType(2010);
            LayoutParams attributes = getWindow().getAttributes();
            attributes.privateFlags |= 16;
            setMessage(context.getString(R.string.android_safe_media_volume_warning));
            setButton(-1, (CharSequence) context.getString(17039379), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener(VolumeDialog.this) {
                public void onClick(DialogInterface dialog, int which) {
                    VolumeDialog.this.mDelegate.disableSafeMediaVolume();
                }
            });
            setButton(-2, (CharSequence) context.getString(17039369), (DialogInterface.OnClickListener) null);
            setIconAttribute(16843605);
            setOnDismissListener(new OnDismissListener(VolumeDialog.this) {
                public void onDismiss(DialogInterface dialog) {
                    synchronized (VolumeDialog.this.mSafetyWarningLock) {
                        VolumeDialog.this.mSafetyWarning = null;
                    }
                }
            });
        }

        public boolean onKeyDown(int keyCode, KeyEvent event) {
            if (24 == keyCode || 25 == keyCode) {
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }

        public boolean onKeyUp(int keyCode, KeyEvent event) {
            if (24 == keyCode || 25 == keyCode) {
                return true;
            }
            return super.onKeyUp(keyCode, event);
        }
    }

    private static final class StreamState {
        private int level;
        private int levelMax;
        private int levelMin;
        private boolean muteSupported;
        private boolean muted;

        private StreamState() {
        }

        public StreamState copy() {
            StreamState ret = new StreamState();
            ret.level = this.level;
            ret.levelMin = this.levelMin;
            ret.levelMax = this.levelMax;
            ret.muted = this.muted;
            ret.muteSupported = this.muteSupported;
            return ret;
        }

        public static StreamState getStreamStateByStreamType(Context context, int type, VolumePanelDelegate delegate) {
            StreamState ret = new StreamState();
            AudioManager am = (AudioManager) context.getSystemService("audio");
            ret.level = am.getLastAudibleStreamVolume(type);
            ret.levelMax = am.getStreamMaxVolume(type);
            ret.levelMin = delegate.getStreamMinVolume(type);
            ret.muted = am.isStreamMute(type);
            ret.muteSupported = delegate.isStreamAffectedByMute(type);
            if (VERSION.SDK_INT < 23 && (type == 6 || type == 0)) {
                ret.level++;
                ret.levelMax++;
            }
            return ret;
        }
    }

    private static class VolumeIconRes {
        int mutedIconRes;
        int normalIconRes;
        int selectedIconRes;

        /* synthetic */ VolumeIconRes(int x0, int x1, int x2, AnonymousClass1 x3) {
            this(x0, x1, x2);
        }

        private VolumeIconRes(int nIcon, int sIcon, int mIcon) {
            this.normalIconRes = nIcon;
            this.selectedIconRes = sIcon;
            this.mutedIconRes = mIcon;
        }
    }

    public interface VolumePanelDelegate {
        void disableSafeMediaVolume();

        int getMasterStreamType();

        int getRingerMode();

        int getStreamMinVolume(int i);

        boolean isStreamAffectedByMute(int i);

        void notifyVolumeControllerVisible(boolean z);

        void setRingerMode(int i);

        boolean showSafeVolumeDialogByFlags(int i);
    }

    private static class VolumeRow {
        private ObjectAnimator anim;
        private int animTargetProgress;
        private int cachedIconRes;
        private int cachedProgressRes;
        private ImageButton icon;
        private int iconsMapKey;
        private boolean important;
        private int initIconsMapKey;
        private int lastLevel;
        private SeekBar slider;
        private View space;
        private StreamState ss;
        private int stream;
        private boolean tracking;
        private long userAttempt;
        private View view;

        private VolumeRow() {
            this.lastLevel = 1;
        }

        /* synthetic */ VolumeRow(AnonymousClass1 x0) {
            this();
        }
    }

    private final class VolumeSeekBarChangeListener implements OnSeekBarChangeListener {
        private VolumeSeekBarChangeListener() {
        }

        /* synthetic */ VolumeSeekBarChangeListener(VolumeDialog x0, AnonymousClass1 x1) {
            this();
        }

        private VolumeRow getVolumeRow(android.widget.SeekBar seekBar) {
            if (seekBar.getTag() == null || !(seekBar.getTag() instanceof VolumeRow)) {
                return null;
            }
            return (VolumeRow) seekBar.getTag();
        }

        public void onProgressChanged(android.widget.SeekBar seekBar, int progress, boolean fromUser) {
            VolumeRow row = getVolumeRow(seekBar);
            if (row != null && fromUser) {
                int minProgress;
                if (row.ss.levelMin > 0) {
                    minProgress = row.ss.levelMin * 100;
                    if (progress < minProgress) {
                        seekBar.setProgress(minProgress);
                    }
                }
                minProgress = VolumeDialog.getImpliedLevel(seekBar, progress);
                row.userAttempt = SystemClock.uptimeMillis();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("VolumeBar:onProgressChanged ");
                stringBuilder.append(row.stream);
                stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                stringBuilder.append(minProgress);
                Log.d(VolumeDialog.TAG, stringBuilder.toString());
                VolumeDialog.this.mAm.setStreamVolume(row.stream, minProgress, 1048576);
            }
        }

        public void onStartTrackingTouch(android.widget.SeekBar seekBar) {
            VolumeRow row = getVolumeRow(seekBar);
            if (row != null) {
                row.cachedIconRes = ((VolumeIconRes) VolumeDialog.sVolumeIconTypeMap.get(Integer.valueOf(row.iconsMapKey))).selectedIconRes;
                row.icon.setImageResource(row.cachedIconRes);
                row.tracking = true;
            }
        }

        public void onStopTrackingTouch(android.widget.SeekBar seekBar) {
            VolumeRow row = getVolumeRow(seekBar);
            if (row != null) {
                row.tracking = false;
                row.userAttempt = SystemClock.uptimeMillis();
                int userLevel = VolumeDialog.getImpliedLevel(seekBar, seekBar.getProgress());
                VolumeDialog.this.mHandler.sendMessageDelayed(VolumeDialog.this.mHandler.obtainMessage(3, row), 1000);
            }
        }
    }

    private static class VolumeSeekbarProgress {
        int normalProgress;
        int silentProgress;

        /* synthetic */ VolumeSeekbarProgress(int x0, int x1, AnonymousClass1 x2) {
            this(x0, x1);
        }

        private VolumeSeekbarProgress(int normal, int silent) {
            this.normalProgress = normal;
            this.silentProgress = silent;
        }
    }

    static {
        Object volumeIconRes;
        sVolumeIconTypeMap.put(Integer.valueOf(0), new VolumeIconRes(R.drawable.ic_audio_alarm_n, R.drawable.ic_audio_alarm_p, R.drawable.ic_audio_alarm_mute, null));
        sVolumeIconTypeMap.put(Integer.valueOf(1), new VolumeIconRes(R.drawable.ic_audio_bt_n, R.drawable.ic_audio_bt_p, R.drawable.ic_audio_bt_mute, null));
        sVolumeIconTypeMap.put(Integer.valueOf(2), new VolumeIconRes(R.drawable.ic_audio_headset_n, R.drawable.ic_audio_headset_p, R.drawable.ic_audio_headset_mute, null));
        sVolumeIconTypeMap.put(Integer.valueOf(3), new VolumeIconRes(R.drawable.ic_audio_media_n, R.drawable.ic_audio_media_p, R.drawable.ic_audio_media_mute, null));
        sVolumeIconTypeMap.put(Integer.valueOf(4), new VolumeIconRes(R.drawable.ic_audio_phone_n, R.drawable.ic_audio_phone_p, R.drawable.ic_audio_phone_mute, null));
        sVolumeIconTypeMap.put(Integer.valueOf(5), new VolumeIconRes(R.drawable.ic_audio_ring_n, R.drawable.ic_audio_ring_p, R.drawable.ic_audio_ring_mute, null));
        sVolumeIconTypeMap.put(Integer.valueOf(6), new VolumeIconRes(R.drawable.ic_audio_speaker_n, R.drawable.ic_audio_speaker_p, R.drawable.ic_audio_speaker_mute, null));
        Map map = sVolumeIconTypeMap;
        Integer valueOf = Integer.valueOf(7);
        if (!"scorpio".equals(Build.DEVICE)) {
            if (!"lithium".equals(Build.DEVICE)) {
                volumeIconRes = new VolumeIconRes(R.drawable.ic_audio_hifi_n, R.drawable.ic_audio_hifi_p, R.drawable.ic_audio_hifi_mute, null);
                map.put(valueOf, volumeIconRes);
                sVolumeIconTypeMap.put(Integer.valueOf(8), new VolumeIconRes(R.drawable.ic_audio_media_n, R.drawable.ic_audio_media_p, R.drawable.ic_audio_media_mute, null));
            }
        }
        volumeIconRes = new VolumeIconRes(R.drawable.ic_audio_media_n, R.drawable.ic_audio_media_p, R.drawable.ic_audio_media_mute, null);
        map.put(valueOf, volumeIconRes);
        sVolumeIconTypeMap.put(Integer.valueOf(8), new VolumeIconRes(R.drawable.ic_audio_media_n, R.drawable.ic_audio_media_p, R.drawable.ic_audio_media_mute, null));
    }

    public VolumeDialog(Context context, VolumePanelDelegate delegate) {
        this.mContext = context;
        this.mDelegate = delegate;
        this.mVibrator = (Vibrator) this.mContext.getSystemService(Context.VIBRATOR_SERVICE);
        this.mAm = (AudioManager) this.mContext.getSystemService("audio");
        IntentFilter filter = new IntentFilter();
        filter.addAction(AudioManager.RINGER_MODE_CHANGED_ACTION);
        this.mContext.registerReceiverAsUser(this.mBootBroadcastReceiver, UserHandle.ALL, filter, null, null);
    }

    private void addRow(int stream, int iconMapKey, boolean important) {
        VolumeRow row = initRow(stream, iconMapKey, important);
        if (!this.mRows.isEmpty()) {
            View v = new View(this.mContext);
            v.setId(16908288);
            this.mDialogContentView.addView(v, new LinearLayout.LayoutParams(-1, this.mContext.getResources().getDimensionPixelSize(R.dimen.volume_slider_interspacing)));
            this.mVolumeRowSpaces.add(v);
            row.space = v;
        }
        this.mDialogContentView.addView(row.view);
        this.mVolumeRowViews.add(row.view);
        this.mRows.add(row);
    }

    private VolumeRow initRow(int stream, int iconMapKey, boolean important) {
        final VolumeRow row = new VolumeRow();
        row.stream = stream;
        row.iconsMapKey = iconMapKey;
        row.initIconsMapKey = iconMapKey;
        row.important = important;
        row.view = this.mDialog.getLayoutInflater().inflate((int) R.layout.volume_dialog_row, null);
        row.view.setTag(row);
        row.slider = (SeekBar) row.view.findViewById(R.id.volume_row_slider);
        row.slider.setTag(row);
        row.slider.setOnSeekBarChangeListener(new VolumeSeekBarChangeListener(this, null));
        row.view.setOnTouchListener(new OnTouchListener() {
            private boolean dragging;
            private final Rect sliderHitRect = new Rect();

            public boolean onTouch(View v, MotionEvent event) {
                row.slider.getHitRect(this.sliderHitRect);
                if (!this.dragging && event.getActionMasked() == 0 && event.getY() < ((float) this.sliderHitRect.top)) {
                    this.dragging = true;
                }
                if (!this.dragging) {
                    return false;
                }
                event.offsetLocation((float) (-this.sliderHitRect.left), (float) (-this.sliderHitRect.top));
                row.slider.dispatchTouchEvent(event);
                if (event.getActionMasked() == 1 || event.getActionMasked() == 3) {
                    this.dragging = false;
                }
                return true;
            }
        });
        row.icon = (ImageButton) row.view.findViewById(R.id.volume_row_icon);
        row.icon.setImageResource(((VolumeIconRes) sVolumeIconTypeMap.get(Integer.valueOf(iconMapKey))).normalIconRes);
        return row;
    }

    private boolean isAttached() {
        ViewGroup viewGroup = this.mDialogContentView;
        return viewGroup != null && viewGroup.isAttachedToWindow();
    }

    private VolumeRow findRow(int stream) {
        for (VolumeRow row : this.mRows) {
            if (row.stream == stream) {
                return row;
            }
        }
        return null;
    }

    private void orderVolumeRowsH() {
        int volumeRowIdx = 0;
        for (int i = 0; i < this.mVolumeRowViews.size(); i++) {
            int volumeRowIdx2;
            View v = (View) this.mVolumeRowViews.get(i);
            VolumeRow row = null;
            boolean viewShow = true;
            if (i == 0) {
                row = findRow(this.mActiveStream);
                if (row == null) {
                    Log.d(TAG, "terrible thing happens in orderVolumeRowsH");
                }
            }
            if (row == null) {
                int volumeRowIdx3 = volumeRowIdx + 1;
                VolumeRow row2 = (VolumeRow) this.mRows.get(volumeRowIdx);
                if (row2.stream == this.mActiveStream) {
                    volumeRowIdx2 = volumeRowIdx3 + 1;
                    row = (VolumeRow) this.mRows.get(volumeRowIdx3);
                } else {
                    row = row2;
                    volumeRowIdx2 = volumeRowIdx3;
                }
                if (row.important) {
                    volumeRowIdx = volumeRowIdx2;
                } else {
                    viewShow = false;
                    volumeRowIdx = volumeRowIdx2;
                }
            }
            volumeRowIdx2 = 8;
            if (viewShow) {
                v.setVisibility(0);
            } else {
                v.setVisibility(8);
            }
            row.view = v;
            row.cachedIconRes = 0;
            row.icon = (ImageButton) v.findViewById(R.id.volume_row_icon);
            row.icon.setImageResource(((VolumeIconRes) sVolumeIconTypeMap.get(Integer.valueOf(row.initIconsMapKey))).normalIconRes);
            row.slider = (SeekBar) v.findViewById(R.id.volume_row_slider);
            row.view.setTag(row);
            row.slider.setTag(row);
            if (i > 0) {
                row.space = (View) this.mVolumeRowSpaces.get(i - 1);
                View view = (View) this.mVolumeRowSpaces.get(i - 1);
                if (viewShow) {
                    volumeRowIdx2 = 0;
                }
                view.setVisibility(volumeRowIdx2);
            } else {
                row.space = null;
            }
        }
    }

    private int computeTimeoutH() {
        if (!(this.mExpanded || this.mExpandAnimating)) {
            RingerModeLayout ringerModeLayout = this.mRingerModeLayout;
            if (ringerModeLayout == null || !ringerModeLayout.mSilenceModeExpanded) {
                return EventLogTags.BOOT_PROGRESS_START;
            }
        }
        return 6000;
    }

    private void stateChangedH(int stream, int level) {
        VolumeRow row = findRow(getMappedStream(stream));
        if (row != null) {
            updateVolumeRowH(row);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("stateChangedH can not find volume row for stream ");
        stringBuilder.append(stream);
        Log.e(TAG, stringBuilder.toString());
    }

    private int getMappedStream(int stream) {
        int[] iArr = this.STREAM_VOLUME_ALIAS_DEFAULT;
        if (stream >= iArr.length) {
            return 3;
        }
        return iArr[stream];
    }

    private void showH(int stream) {
        if (this.mRows.size() == 0) {
            this.mDialog = new CustomDialog(this.mContext);
            Window window = this.mDialog.getWindow();
            window.requestFeature(1);
            window.setBackgroundDrawable(new ColorDrawable(0));
            window.clearFlags(2);
            window.addFlags(com.android.internal.R.string.capability_title_canRequestTouchExploration);
            this.mDialog.setCanceledOnTouchOutside(true);
            this.mDialog.setContentView((int) R.layout.volume_dialog);
            LayoutParams lp = window.getAttributes();
            lp.height = -2;
            lp.width = -1;
            lp.type = 2020;
            lp.format = -3;
            lp.setTitle("Volume Control");
            lp.gravity = 48;
            lp.windowAnimations = R.style.Animation_VolumePanel;
            window.setAttributes(lp);
            window.setSoftInputMode(48);
            this.mDialogView = (ViewGroup) this.mDialog.findViewById(R.id.volume_dialog);
            this.mDialogContentView = (ViewGroup) this.mDialog.findViewById(R.id.volume_dialog_content);
            this.mExpandButton = (ImageButton) this.mDialogView.findViewById(R.id.volume_expand_button);
            this.mExpandButton.setOnClickListener(this.mClickExpand);
            this.mRingerModeLayout = (RingerModeLayout) this.mDialog.findViewById(R.id.ringer_mode_layout);
            this.mExpandAnimator = ValueAnimator.ofInt(0, 0);
            this.mLayoutTransition = new LayoutTransition();
            this.mLayoutTransition.setDuration(200);
            addRow(2, 5, true);
            addRow(3, 3, true);
            addRow(4, 0, true);
            addRow(0, 4, false);
            addRow(6, 4, false);
            if (AudioSystem.getNumStreamTypes() > 10) {
                addRow(10, 8, false);
            }
        }
        this.mDialogView.setBackgroundResource(0);
        this.mDialogView.setBackgroundResource(R.drawable.volume_dialog_background);
        ViewGroup.LayoutParams lp2 = this.mDialogView.getLayoutParams();
        lp2.width = this.mContext.getResources().getDimensionPixelSize(R.dimen.volume_dialog_width);
        if (lp2.width == 0) {
            lp2.width = -1;
        }
        this.mDialogView.setLayoutParams(lp2);
        this.mLastStatus = false;
        if (!this.mShowing) {
            if (this.mAm.getMode() == 2 || !SilenceMode.isSupported) {
                this.mRingerModeLayout.setVisibility(8);
            }
            this.mRingerModeLayout.setVolumeDialog(this);
            this.mRingerModeLayout.setLooper(Looper.getMainLooper());
            this.mRingerModeLayout.setParentDialog(this.mDialogView);
            this.mRingerModeLayout.init();
        }
        adjustDialogPosition();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("showH ");
        stringBuilder.append(stream);
        String str = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        stringBuilder.append(str);
        stringBuilder.append(this.mActiveStream);
        stringBuilder.append(str);
        stringBuilder.append(this.mShowing);
        Log.d(TAG, stringBuilder.toString());
        stream = getMappedStream(stream);
        this.mHandler.removeMessages(1);
        this.mHandler.removeMessages(2);
        if (!(this.mShowing && this.mActiveStream == stream)) {
            this.mActiveStream = stream;
            orderVolumeRowsH();
        }
        rescheduleTimeoutH();
        updateVolumeRowsH();
        if (this.mInScreenshot || this.mShowing) {
            if (this.mShowing) {
                updateExpandButtonH();
                updateRowsVisibilityByExpandH();
            }
            return;
        }
        this.mExpanded = false;
        this.mExpandAnimating = false;
        updateExpandButtonH();
        updateRowsVisibilityByExpandH();
        this.mShowing = true;
        this.mDialogShowTime = SystemClock.uptimeMillis();
        this.mDialog.show();
        this.mDelegate.notifyVolumeControllerVisible(true);
    }

    private void vibrateH() {
        ((Vibrator) this.mContext.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(300);
    }

    private void dismissH() {
        this.mHandler.removeMessages(2);
        this.mHandler.removeMessages(1);
        if (this.mShowing) {
            this.mShowing = false;
            synchronized (this.mSafetyWarningLock) {
                if (this.mSafetyWarning != null) {
                    this.mSafetyWarning.dismiss();
                }
            }
            this.mDialog.dismiss();
            this.mExpandAnimator.cancel();
            this.mDelegate.notifyVolumeControllerVisible(false);
            this.mRingerModeLayout.cleanUp();
            this.mRingerModeLayout = null;
            this.mExpandAnimator = null;
            this.mDialog = null;
            this.mDialogContentView = null;
            this.mDialogView = null;
            this.mExpandButton = null;
            this.mRows.clear();
            this.mVolumeRowViews.clear();
            this.mVolumeRowSpaces.clear();
        }
    }

    private void adjustDialogPosition() {
        if (CustomizeUtil.HAS_NOTCH) {
            Window window = this.mDialog.getWindow();
            LayoutParams lp = window.getAttributes();
            lp.y = this.mContext.getResources().getConfiguration().orientation == 1 ? getStatusBarHeight() : 0;
            window.setAttributes(lp);
        }
    }

    private int getStatusBarHeight() {
        if (this.mStatusBarHeight < 0) {
            this.mStatusBarHeight = this.mContext.getResources().getDimensionPixelSize(com.android.internal.R.dimen.status_bar_height);
        }
        return this.mStatusBarHeight;
    }

    private void updateDialogBottomMarginH() {
        if (this.mDialogView != null) {
            long diff = System.currentTimeMillis();
            long j = this.mCollapseTime;
            boolean collapsing = j != 0 && diff - j < ((long) getConservativeCollapseDuration());
            ViewGroup dialogParentView = (ViewGroup) this.mDialogView.getParent();
            MarginLayoutParams mlp = (MarginLayoutParams) dialogParentView.getLayoutParams();
            mlp.height = collapsing ? dialogParentView.getHeight() : -2;
            dialogParentView.setLayoutParams(mlp);
        }
    }

    private int getConservativeCollapseDuration() {
        return 600;
    }

    private void updateExpandButtonH() {
        ImageButton imageButton = this.mExpandButton;
        if (imageButton != null) {
            imageButton.setVisibility(this.mActiveStream == 0 ? 4 : 0);
            this.mExpandButton.setImageResource(this.mExpanded ? R.drawable.volume_collapse_btn : R.drawable.volume_expand_btn);
            this.mExpandButton.setClickable(this.mExpandAnimating ^ 1);
        }
    }

    private void prepareForCollapse() {
        this.mHandler.removeMessages(7);
        this.mCollapseTime = System.currentTimeMillis();
        updateDialogBottomMarginH();
        this.mHandler.sendEmptyMessageDelayed(7, (long) getConservativeCollapseDuration());
    }

    public void setExpandedH(boolean expanded) {
        if (this.mExpanded != expanded) {
            this.mExpanded = expanded;
            this.mExpandAnimating = isAttached();
            if (this.mExpandAnimating) {
                updateExpandButtonH();
                expandVolumeBar(expanded);
            }
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    VolumeDialog volumeDialog = VolumeDialog.this;
                    volumeDialog.mExpandAnimating = false;
                    volumeDialog.updateExpandButtonH();
                }
            }, (long) getConservativeCollapseDuration());
            rescheduleTimeoutH();
        }
    }

    private void expandVolumeBar(final boolean isExpanded) {
        this.mExpandAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                VolumeDialog.this.mDialogContentView.getLayoutParams().height = ((Integer) animation.getAnimatedValue()).intValue();
                VolumeDialog.this.mDialogContentView.requestLayout();
            }
        });
        this.mExpandAnimator.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
                if (isExpanded) {
                    VolumeDialog.this.updateRowsVisibilityByExpandH();
                }
                ViewGroup dialogParentView = (ViewGroup) VolumeDialog.this.mDialogView.getParent();
                MarginLayoutParams mlp = (MarginLayoutParams) dialogParentView.getLayoutParams();
                mlp.height = 1000;
                dialogParentView.setLayoutParams(mlp);
                VolumeDialog.this.mDialogContentView.getLayoutParams().height = -2;
                VolumeDialog.this.mDialogContentView.measure(0, 0);
                ((VolumeRow) VolumeDialog.this.mRows.get(0)).view.measure(0, 0);
                int height = isExpanded ? VolumeDialog.this.mDialogContentView.getMeasuredHeight() : ((VolumeRow) VolumeDialog.this.mRows.get(0)).view.getMeasuredHeight();
                VolumeDialog.this.mExpandAnimator.setIntValues(VolumeDialog.this.mDialogContentView.getHeight(), height);
            }

            public void onAnimationEnd(Animator animation) {
                if (!isExpanded) {
                    VolumeDialog.this.updateRowsVisibilityByExpandH();
                }
                VolumeDialog.this.mDialogContentView.getLayoutParams().height = -2;
                VolumeDialog.this.mDialogContentView.requestLayout();
                ViewGroup dialogParentView = (ViewGroup) VolumeDialog.this.mDialogView.getParent();
                MarginLayoutParams mlp = (MarginLayoutParams) dialogParentView.getLayoutParams();
                mlp.height = -2;
                dialogParentView.setLayoutParams(mlp);
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
        this.mExpandAnimator.setInterpolator(new DecelerateInterpolator());
        this.mExpandAnimator.setDuration(300);
        this.mExpandAnimator.start();
    }

    private void updateRowsVisibilityByExpandH() {
        for (VolumeRow row : this.mRows) {
            int visibility = 8;
            if (row.stream == this.mActiveStream) {
                visibility = 0;
            } else if (row.important) {
                visibility = this.mExpanded ? 0 : 8;
            }
            row.view.setVisibility(visibility);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("updateRowsVisibilityByExpandH ");
            stringBuilder.append(row.stream);
            String str = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
            stringBuilder.append(str);
            stringBuilder.append(visibility);
            stringBuilder.append(str);
            stringBuilder.append(Integer.toHexString(System.identityHashCode(row.view)));
            Log.d(TAG, stringBuilder.toString());
            if (row.space != null) {
                row.space.setVisibility(visibility);
            }
        }
    }

    private void updateLayoutDirectionH(int layoutDirection) {
        ViewGroup viewGroup = this.mDialogView;
        if (viewGroup != null) {
            viewGroup.setLayoutDirection(layoutDirection);
        }
    }

    private void updateVolumeRowsH() {
        for (VolumeRow row : this.mRows) {
            updateVolumeRowH(row);
        }
    }

    private void rescheduleTimeoutH() {
        this.mHandler.removeMessages(2);
        int timeout = computeTimeoutH();
        H h = this.mHandler;
        h.sendMessageDelayed(h.obtainMessage(2), (long) timeout);
    }

    private void recheckH(VolumeRow row) {
        if (row == null) {
            for (VolumeRow r : this.mRows) {
                updateVolumeRowH(r);
            }
            return;
        }
        updateVolumeRowH(row);
    }

    private void updateVolumeRowH(VolumeRow row) {
        StreamState ss = StreamState.getStreamStateByStreamType(this.mContext, row.stream, this.mDelegate);
        if (ss != null) {
            row.ss = ss;
            if (ss.level > 0) {
                row.lastLevel = ss.level;
            }
            boolean z = false;
            boolean isRingStream = row.stream == 2;
            if (isRingStream && this.mDelegate.getRingerMode() == 1) {
                int i = 1;
            } else {
                boolean isRingVibrate = false;
            }
            if (isRingStream && this.mDelegate.getRingerMode() == 0) {
                int i2 = 1;
            } else {
                boolean isRingSilent = false;
            }
            int max = ss.levelMax * 100;
            if (max != row.slider.getMax()) {
                row.slider.setMax(max);
            }
            int iconsMapKey = row.initIconsMapKey;
            if (row.stream == this.mActiveStream) {
                int device = this.mAm.getDevicesForStream(row.stream);
                if (row.stream == 0 && this.mAm.isSpeakerphoneOn()) {
                    iconsMapKey = 6;
                }
                if (!((device & 4) == 0 && (device & 8) == 0)) {
                    iconsMapKey = 2;
                }
                if (row.stream == 3 && AudioManagerHelper.isHiFiMode(this.mContext)) {
                    iconsMapKey = 7;
                }
            }
            row.iconsMapKey = iconsMapKey;
            boolean streamMuted = ((AudioManager) this.mContext.getSystemService("audio")).getStreamVolume(row.stream) == 0;
            if (VERSION.SDK_INT < 23 && (row.stream == 6 || row.stream == 0)) {
                if (ss.muted && ss.muteSupported) {
                    z = true;
                }
                streamMuted = z;
            }
            VolumeIconRes iconsRes = (VolumeIconRes) sVolumeIconTypeMap.get(Integer.valueOf(iconsMapKey));
            int iconRes = streamMuted ? iconsRes.mutedIconRes : iconsRes.normalIconRes;
            if (iconRes != row.cachedIconRes) {
                row.cachedIconRes = iconRes;
                row.icon.setImageResource(iconRes);
            }
            int progressRes = sVolumeSeekbarProgress;
            progressRes = streamMuted ? progressRes.silentProgress : progressRes.normalProgress;
            if (progressRes != row.cachedProgressRes) {
                row.cachedProgressRes = progressRes;
                row.slider.setProgressDrawable(row.slider.getResources().getDrawable(progressRes));
            }
            updateVolumeRowSliderH(row, streamMuted ? this.mAm.getLastAudibleStreamVolume(row.stream) : row.ss.level);
        }
    }

    private void updateVolumeRowSliderH(VolumeRow row, int vlevel) {
        if (!row.tracking) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("updateVolumeRowSliderH start ");
            stringBuilder.append(row.stream);
            String str = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
            stringBuilder.append(str);
            stringBuilder.append(vlevel);
            String stringBuilder2 = stringBuilder.toString();
            String str2 = TAG;
            Log.d(str2, stringBuilder2);
            int progress = row.slider.getProgress();
            int level = getImpliedLevel(row.slider, progress);
            boolean rowVisible = row.view.getVisibility() == 0;
            boolean inGracePeriod = SystemClock.uptimeMillis() - row.userAttempt < 1000;
            this.mHandler.removeMessages(3, row);
            if (this.mShowing && rowVisible && inGracePeriod) {
                H h = this.mHandler;
                h.sendMessageAtTime(h.obtainMessage(3, row), row.userAttempt + 1000);
            } else if (vlevel != level || !this.mShowing || !rowVisible) {
                int newProgress = vlevel * 100;
                if (progress != newProgress) {
                    if (!this.mShowing || !rowVisible) {
                        if (row.anim != null) {
                            row.anim.cancel();
                        }
                        row.slider.setProgress(newProgress);
                    } else if (row.anim == null || !row.anim.isRunning() || row.animTargetProgress != newProgress) {
                        StringBuilder stringBuilder3;
                        int startProgress = progress;
                        if (row.anim != null && row.anim.isRunning()) {
                            stringBuilder3 = new StringBuilder();
                            stringBuilder3.append("updateVolumeRowSliderH cancel animation: ");
                            stringBuilder3.append(row.stream);
                            Log.d(str2, stringBuilder3.toString());
                            row.anim.cancel();
                            startProgress = row.animTargetProgress;
                        }
                        stringBuilder3 = new StringBuilder();
                        stringBuilder3.append("updateVolumeRowSliderH animation: ");
                        stringBuilder3.append(startProgress);
                        stringBuilder3.append(str);
                        stringBuilder3.append(newProgress);
                        Log.d(str2, stringBuilder3.toString());
                        row.anim = ObjectAnimator.ofInt(row.slider, "progress", startProgress, newProgress);
                        row.anim.setInterpolator(null);
                        row.animTargetProgress = newProgress;
                        row.anim.setDuration(80);
                        row.anim.start();
                    }
                }
            }
        }
    }

    private void showSafetyWarningH(int flags) {
        if (this.mDelegate.showSafeVolumeDialogByFlags(flags) || this.mShowing) {
            synchronized (this.mSafetyWarningLock) {
                if (this.mSafetyWarning != null) {
                    return;
                }
                this.mSafetyWarning = new SafetyWarningDialog(this.mContext);
                this.mSafetyWarning.show();
                this.mSafetyWarning.setOnDismissListener(new OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        synchronized (VolumeDialog.this.mSafetyWarningLock) {
                            VolumeDialog.this.mSafetyWarning = null;
                        }
                    }
                });
            }
        }
        rescheduleTimeoutH();
    }

    private static int getImpliedLevel(android.widget.SeekBar seekBar, int progress) {
        int m = seekBar.getMax();
        int n = (m / 100) - 1;
        if (progress == 0) {
            return 0;
        }
        return progress == m ? m / 100 : ((int) ((((float) progress) / ((float) m)) * ((float) n))) + 1;
    }

    private void streamDeviceChanged(int stream) {
        this.mHandler.obtainMessage(3, findRow(stream)).sendToTarget();
    }

    public void show(int stream, int flags) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("show ");
        stringBuilder.append(stream);
        stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        stringBuilder.append(flags);
        Log.d(TAG, stringBuilder.toString());
        this.mHandler.obtainMessage(1, stream, flags).sendToTarget();
    }

    public void recheckAll() {
        this.mHandler.sendEmptyMessage(4);
    }

    public void dismiss() {
        this.mHandler.obtainMessage(2).sendToTarget();
    }

    public void dismiss(long delayMillis) {
        this.mHandler.sendEmptyMessageDelayed(2, delayMillis);
    }

    public void stateChanged(int stream) {
        stateChanged(stream, this.mAm.getStreamVolume(stream));
    }

    public void stateChanged(int stream, int level) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("stateChanged ");
        stringBuilder.append(stream);
        stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        stringBuilder.append(level);
        Log.d(TAG, stringBuilder.toString());
        this.mHandler.obtainMessage(6, stream, level).sendToTarget();
    }

    public void showSafeWarningDialog(int flags) {
        this.mHandler.obtainMessage(9, flags, 0).sendToTarget();
    }

    public void updateLayoutDirection(int layoutDirection) {
        this.mHandler.obtainMessage(8, layoutDirection, 0).sendToTarget();
    }

    public void masterVolumeChanged(int flags) {
    }

    public void masterMuteChanged(int flags) {
    }

    public boolean isShowing() {
        return this.mShowing;
    }

    public void rescheduleTimeout(boolean needReschedule) {
        this.mHandler.removeMessages(2);
        this.mHandler.removeMessages(5);
        if (needReschedule) {
            this.mHandler.sendEmptyMessage(5);
        }
    }
}
