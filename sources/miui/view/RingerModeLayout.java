package miui.view;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.AlarmManager;
import android.app.ExtraNotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.miui.R;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.UserHandle;
import android.provider.MiuiSettings.SilenceMode;
import android.provider.Settings.Global;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.DecelerateInterpolator;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import miui.widget.SlidingButton;

public class RingerModeLayout extends LinearLayout {
    private static final int ANIMATION_DURATION = 300;
    private static final String TAG = "RingerModeLayout";
    private int ContentHeight;
    private boolean mAnimating;
    private final Context mContext;
    private int mCurrentMode;
    private ViewGroup mDialogView;
    private H mHandler;
    private ImageView mHelpBtn;
    private OnClickListener mHelpButtonListener = new OnClickListener() {
        public void onClick(View view) {
            RingerModeLayout.this.mHelpBtn.setImageDrawable(RingerModeLayout.this.getResources().getDrawable(R.drawable.explain_s));
            ComponentName component = ComponentName.unflattenFromString("com.android.settings/com.android.settings.Settings$MiuiSilentModeAcivity");
            if (component != null) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setComponent(component);
                intent.setFlags(335544320);
                try {
                    RingerModeLayout.this.mContext.startActivityAsUser(intent, UserHandle.CURRENT);
                } catch (Exception e) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("start activity exception, component = ");
                    stringBuilder.append(component);
                    Log.e(RingerModeLayout.TAG, stringBuilder.toString());
                }
                RingerModeLayout.this.mVolumeDialog.dismiss();
            }
        }
    };
    private Looper mLooper;
    private int mOrignalMode;
    private long mOrignalRemain;
    private OnClickListener mRadioButtonListener = new OnClickListener() {
        public void onClick(View view) {
            Uri id = ExtraNotificationManager.getConditionId(RingerModeLayout.this.mContext);
            int mode = 0;
            int id2 = view.getId();
            String str = RingerModeLayout.TAG;
            if (id2 == R.id.radiobtn_total) {
                Log.d(str, "set total mode");
                mode = 1;
            } else if (view.getId() == R.id.radiobtn_standard) {
                Log.d(str, "set standard mode");
                mode = 4;
            }
            SilenceMode.setSilenceMode(RingerModeLayout.this.mContext, mode, id);
        }
    };
    private RadioGroup mRadioGroup;
    private boolean mRemainTextShown;
    private TextView mRemainTime_1;
    private TextView mRemainTime_2;
    private TextView mSelectedText;
    private boolean mShowing;
    private OnCheckedChangeListener mSilenceButtonChangedListener = new OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (RingerModeLayout.this.mShowing) {
                String str = RingerModeLayout.TAG;
                if (isChecked) {
                    Log.d(str, "sliding to true");
                    RingerModeLayout.this.mVolumeDialog.setExpandedH(false);
                    RingerModeLayout.this.expandSilenceModeContent(true);
                    if (RingerModeLayout.this.mCurrentMode == 0) {
                        SilenceMode.setSilenceMode(RingerModeLayout.this.mContext, SilenceMode.getLastestQuietMode(RingerModeLayout.this.mContext), null);
                    }
                    RingerModeLayout.this.updateRadioGroup();
                } else {
                    Log.d(str, "sliding to false");
                    RingerModeLayout.this.mRadioGroup.clearCheck();
                    RingerModeLayout.this.mCurrentMode = 0;
                    RingerModeLayout.this.expandSilenceModeContent(false);
                    SilenceMode.setSilenceMode(RingerModeLayout.this.mContext, 0, null);
                    RingerModeLayout.this.mVolumeDialog.recheckAll();
                    RingerModeLayout.this.mHandler.removeMessages(1);
                    RingerModeLayout.this.mHandler.sendEmptyMessageDelayed(1, 50);
                }
            }
        }
    };
    private RelativeLayout mSilenceModeContent;
    private LinearLayout mSilenceModeExpandContent;
    public boolean mSilenceModeExpanded;
    private final SilenceModeObserver mSilenceModeObserver = new SilenceModeObserver();
    private TextView mSilenceModeTitle;
    private SlidingButton mSlidingButton;
    private RadioButton mStandardBtn;
    private RelativeLayout mTimeLabel;
    private OnClickListener mTimeLabelListener = new OnClickListener() {
        public void onClick(View v) {
            int value = Integer.parseInt((String) v.getTag());
            RingerModeLayout.this.mTimeSeekbar.setProgress(value * 25);
            int time = RingerModeLayout.this.progressToMinute(value * 25);
            int mode = SilenceMode.getZenMode(RingerModeLayout.this.mContext);
            RingerModeLayout.this.mHandler.removeMessages(1);
            ExtraNotificationManager.startCountDownSilenceMode(RingerModeLayout.this.mContext, mode, time);
            if (value > 0) {
                RingerModeLayout.this.mHandler.sendEmptyMessageDelayed(1, 50);
            }
        }
    };
    private List<TextView> mTimeList;
    private OnSeekBarChangeListener mTimeSeekBarChangedListener = new OnSeekBarChangeListener() {
        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            if (RingerModeLayout.this.mTimeLabel.getVisibility() == 0) {
                int index = RingerModeLayout.this.getProgressLevel(progress);
                if (!((TextView) RingerModeLayout.this.mTimeList.get(index)).equals(RingerModeLayout.this.mSelectedText)) {
                    RingerModeLayout.this.mSelectedText.setTextSize(1, 10.0f);
                    RingerModeLayout.this.mSelectedText.setTextColor(RingerModeLayout.this.getResources().getColor(R.color.time_not_selected));
                    RingerModeLayout ringerModeLayout = RingerModeLayout.this;
                    ringerModeLayout.mSelectedText = (TextView) ringerModeLayout.mTimeList.get(index);
                    RingerModeLayout.this.mSelectedText.setTextSize(1, 12.0f);
                    RingerModeLayout.this.mSelectedText.setTextColor(RingerModeLayout.this.getResources().getColor(R.color.time_selected));
                }
            }
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            RingerModeLayout.this.mTimeLabel.setVisibility(0);
            RingerModeLayout.this.mRemainTime_2.setVisibility(8);
            RingerModeLayout.this.mHandler.removeMessages(1);
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            int level = RingerModeLayout.this.getProgressLevel(seekBar.getProgress());
            seekBar.setProgress(level * 25);
            int time = RingerModeLayout.this.progressToMinute(level * 25);
            int mode = SilenceMode.getZenMode(RingerModeLayout.this.mContext);
            RingerModeLayout.this.mHandler.removeMessages(1);
            ExtraNotificationManager.startCountDownSilenceMode(RingerModeLayout.this.mContext, mode, time);
            if (seekBar.getProgress() > 0) {
                RingerModeLayout.this.mHandler.sendEmptyMessageDelayed(1, 50);
            }
        }
    };
    private miui.widget.SeekBar mTimeSeekbar;
    private RadioButton mTotalBtn;
    private VolumeDialog mVolumeDialog;

    private final class H extends Handler {
        private static final int UPDATE_EXPAND_CONTENT = 2;
        private static final int UPDATE_TIME = 1;

        public H(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            if (RingerModeLayout.this.mShowing) {
                int i = msg.what;
                if (i == 1) {
                    RingerModeLayout.this.updateRemainTimeSeekbar();
                } else if (i == 2) {
                    RingerModeLayout.this.updateRadioGroup();
                    RingerModeLayout.this.mSlidingButton.setChecked(RingerModeLayout.this.isSilenceModeEnabled());
                    RingerModeLayout.this.mVolumeDialog.recheckAll();
                }
            }
        }
    }

    private final class SilenceModeObserver extends ContentObserver {
        private final Uri silence_mode = Global.getUriFor("zen_mode");

        public SilenceModeObserver() {
            super(RingerModeLayout.this.mHandler);
        }

        public void register() {
            RingerModeLayout.this.mContext.getContentResolver().registerContentObserver(this.silence_mode, false, this, -1);
        }

        public void unregister() {
            RingerModeLayout.this.mContext.getContentResolver().unregisterContentObserver(this);
        }

        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            int mode = SilenceMode.getZenMode(RingerModeLayout.this.mContext);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Zenmode changeded ");
            stringBuilder.append(RingerModeLayout.this.mCurrentMode);
            stringBuilder.append(" -> ");
            stringBuilder.append(mode);
            Log.i(RingerModeLayout.TAG, stringBuilder.toString());
            RingerModeLayout.this.mCurrentMode = mode;
            RingerModeLayout.this.mHandler.sendEmptyMessage(2);
        }
    }

    public RingerModeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.mOrignalRemain = ExtraNotificationManager.getRemainTime(this.mContext);
        this.mOrignalMode = ExtraNotificationManager.getZenMode(this.mContext);
    }

    public void cleanUp() {
        this.mHandler.removeMessages(1);
        if (this.mShowing) {
            long remain = ExtraNotificationManager.getRemainTime(this.mContext);
            int mode = ExtraNotificationManager.getZenMode(this.mContext);
            if (Math.abs(this.mOrignalRemain - remain) >= ((long) 30000) || this.mOrignalMode != mode) {
                String params = String.valueOf(timeToMinute(remain));
                SilenceMode.reportRingerModeInfo(SilenceMode.MISTAT_SILENCE_DND, SilenceMode.MISTAT_RINGERMODE_LIST[mode], params, System.currentTimeMillis());
            }
            this.mShowing = false;
            this.mSilenceModeObserver.unregister();
            this.mTimeSeekbar = null;
            this.mRemainTime_2 = null;
            this.mRadioGroup = null;
            this.mTimeList.clear();
        }
    }

    private int timeToMinute(long timeMs) {
        if (timeMs == 0) {
            return 0;
        }
        if (timeMs <= AlarmManager.INTERVAL_HALF_HOUR) {
            return 30;
        }
        if (timeMs <= 3600000) {
            return 60;
        }
        if (timeMs <= 7200000) {
            return 120;
        }
        return 480;
    }

    private String turnMillSecondsToHour(long ms) {
        StringBuilder stringBuilder = new StringBuilder();
        int hour = (int) (ms / 3600000);
        int lastms = (int) (ms % 3600000);
        int min = lastms / 60000;
        int seconds = (lastms % 60000) / 1000;
        String str = ":";
        String str2 = "0";
        if (hour > 0) {
            if (hour < 10) {
                stringBuilder.append(str2);
            }
            stringBuilder.append(hour);
            stringBuilder.append(str);
        }
        if (min < 10) {
            stringBuilder.append(str2);
        }
        stringBuilder.append(min);
        stringBuilder.append(str);
        if (seconds < 10) {
            stringBuilder.append(str2);
        }
        stringBuilder.append(seconds);
        return stringBuilder.toString();
    }

    public void setParentDialog(ViewGroup view) {
        this.mDialogView = view;
    }

    public void setVolumeDialog(VolumeDialog volumeDialog) {
        this.mVolumeDialog = volumeDialog;
    }

    private void updateRemainTimeSeekbar() {
        if (this.mShowing) {
            long remain = ExtraNotificationManager.getRemainTime(this.mContext);
            if (remain > 0) {
                this.mTimeLabel.setVisibility(8);
                this.mRemainTime_2.setVisibility(0);
                updateRemainText(this.mSilenceModeExpanded ^ 1);
                this.mTimeSeekbar.setProgress(timeToProgress(remain / 1000));
                this.mRemainTime_1.setText(this.mContext.getString(R.string.remain, turnMillSecondsToHour(remain)));
                this.mRemainTime_2.setText(turnMillSecondsToHour(remain));
                LayoutParams paramsStrength = (LayoutParams) this.mRemainTime_2.getLayoutParams();
                paramsStrength.setMarginStart(getXPosition(this.mTimeSeekbar));
                this.mRemainTime_2.setLayoutParams(paramsStrength);
                this.mHandler.removeMessages(1);
                H h = this.mHandler;
                h.sendMessageDelayed(h.obtainMessage(1), 1000);
            } else {
                this.mTimeLabel.setVisibility(0);
                this.mRemainTime_2.setVisibility(8);
                updateRemainText(false);
                this.mTimeSeekbar.setProgress(0);
            }
        }
    }

    private int timeToProgress(long time) {
        long progress = 0;
        if (time <= 3600) {
            progress = time / 72;
        } else if (time <= 7200) {
            progress = ((time - 3600) / 144) + 50;
        } else if (time <= 28800) {
            progress = ((time - 3600) / 864) + 75;
        }
        return (int) progress;
    }

    private int progressToMinute(int progress) {
        if (progress <= 50) {
            return (progress / 25) * 30;
        }
        if (progress <= 75) {
            return 120;
        }
        if (progress <= 100) {
            return 480;
        }
        return 0;
    }

    private int getProgressLevel(int progress) {
        int currentProgress = progress;
        if (currentProgress <= 12) {
            return 0;
        }
        if (Math.abs(currentProgress - 25) <= 12) {
            return 1;
        }
        if (Math.abs(currentProgress - 50) <= 12) {
            return 2;
        }
        if (Math.abs(currentProgress - 75) <= 12) {
            return 3;
        }
        return 4;
    }

    private void updateRadioGroup() {
        if (isSilenceModeEnabled() && this.mShowing) {
            this.mRadioGroup.check(this.mCurrentMode == 4 ? R.id.radiobtn_standard : R.id.radiobtn_total);
            changeSilenceModeTitle(true);
        }
    }

    private boolean isSilenceModeEnabled() {
        return this.mCurrentMode > 0;
    }

    public void setLooper(Looper looper) {
        this.mLooper = looper;
        this.mHandler = new H(this.mLooper);
    }

    public void init() {
        Log.d(TAG, "init...");
        this.mSilenceModeContent = (RelativeLayout) findViewById(R.id.silence_mode_content);
        this.mSilenceModeExpandContent = (LinearLayout) findViewById(R.id.silence_mode_expand_content);
        this.mSilenceModeTitle = (TextView) findViewById(R.id.silence_mode_title);
        this.mTimeSeekbar = (miui.widget.SeekBar) findViewById(R.id.timer_seekbar);
        this.mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        this.mStandardBtn = (RadioButton) findViewById(R.id.radiobtn_standard);
        this.mTotalBtn = (RadioButton) findViewById(R.id.radiobtn_total);
        this.mHelpBtn = (ImageView) findViewById(R.id.help_btn);
        this.mSlidingButton = (SlidingButton) findViewById(R.id.slidingBtn);
        this.mRemainTime_1 = (TextView) findViewById(R.id.remain_time_1);
        this.mRemainTime_2 = (TextView) findViewById(R.id.remain_time_2);
        this.mTimeLabel = (RelativeLayout) findViewById(R.id.time_label);
        this.mTimeList = new ArrayList();
        this.mTimeList.add((TextView) findViewById(R.id.always));
        this.mTimeList.add((TextView) findViewById(R.id.onehour));
        this.mTimeList.add((TextView) findViewById(R.id.twohours));
        this.mTimeList.add((TextView) findViewById(R.id.fourhours));
        this.mTimeList.add((TextView) findViewById(R.id.eighthours));
        for (TextView v : this.mTimeList) {
            v.setOnClickListener(this.mTimeLabelListener);
        }
        this.mSelectedText = (TextView) this.mTimeList.get(0);
        this.mSelectedText.setTextColor(getResources().getColor(R.color.time_selected));
        this.mSelectedText.setTextSize(1, 12.0f);
        this.mRadioGroup.measure(0, 0);
        this.mStandardBtn.getLayoutParams().height = this.mRadioGroup.getMeasuredHeight();
        this.mTotalBtn.getLayoutParams().height = this.mRadioGroup.getMeasuredHeight();
        findViewById(R.id.second_layer).getLayoutParams().height = this.mRadioGroup.getMeasuredHeight();
        this.mSilenceModeExpandContent.measure(0, 0);
        this.ContentHeight = this.mSilenceModeExpandContent.getMeasuredHeight();
        this.mHelpBtn.setOnClickListener(this.mHelpButtonListener);
        this.mShowing = true;
        this.mSilenceModeObserver.register();
        this.mSilenceModeContent.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (!RingerModeLayout.this.mAnimating && !RingerModeLayout.this.mVolumeDialog.mExpandAnimating) {
                    RingerModeLayout ringerModeLayout = RingerModeLayout.this;
                    ringerModeLayout.expandSilenceModeContent(ringerModeLayout.mSilenceModeExpanded ^ 1);
                    RingerModeLayout.this.mVolumeDialog.setExpandedH(false);
                }
            }
        });
        this.mCurrentMode = SilenceMode.getZenMode(this.mContext);
        this.mSlidingButton.setChecked(isSilenceModeEnabled());
        updateRadioGroup();
        this.mSlidingButton.setOnCheckedChangeListener(this.mSilenceButtonChangedListener);
        this.mTimeSeekbar.setOnSeekBarChangeListener(this.mTimeSeekBarChangedListener);
        this.mTotalBtn.setOnClickListener(this.mRadioButtonListener);
        this.mStandardBtn.setOnClickListener(this.mRadioButtonListener);
        this.mSilenceModeExpandContent.getLayoutParams().height = 0;
        this.mSilenceModeExpanded = false;
        this.mRemainTextShown = false;
        if (isSilenceModeEnabled()) {
            this.mSilenceModeTitle.setText(this.mCurrentMode == 4 ? R.string.standard_title : R.string.total_title);
        } else {
            this.mSilenceModeTitle.setText((int) R.string.silence_mode);
        }
        updateRemainTimeSeekbar();
    }

    private void changeSilenceModeTitle(boolean showTitle) {
        if (showTitle) {
            this.mSilenceModeTitle.setText(this.mCurrentMode == 4 ? R.string.standard_title : R.string.total_title);
        } else {
            this.mSilenceModeTitle.setText((int) R.string.silence_mode);
        }
    }

    private void updateRemainText(boolean show) {
        if (this.mRemainTextShown != show) {
            float from;
            float to;
            Log.d(TAG, "updateRemainText...");
            this.mRemainTextShown = show;
            if (show) {
                from = 0.0f;
                to = 1.0f;
            } else {
                from = 1.0f;
                to = 0.0f;
            }
            ValueAnimator animator = ValueAnimator.ofFloat(new float[]{from, to});
            animator.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    RingerModeLayout.this.mRemainTime_1.setAlpha(((Float) animation.getAnimatedValue()).floatValue());
                }
            });
            animator.setDuration(300);
            animator.start();
        }
    }

    private int getXPosition(miui.widget.SeekBar seekBar) {
        return (int) (((((((float) seekBar.getProgress()) * ((float) (seekBar.getMeasuredWidth() - seekBar.getThumb().getIntrinsicWidth()))) / ((float) seekBar.getMax())) + ((float) (seekBar.getThumb().getIntrinsicWidth() / 2))) - (this.mRemainTime_2.getPaint().measureText(this.mRemainTime_2.getText().toString()) / 2.0f)) + ((float) ((LayoutParams) seekBar.getLayoutParams()).getMarginStart()));
    }

    public void expandSilenceModeContent(boolean isExpanded) {
        LinearLayout linearLayout = this.mSilenceModeExpandContent;
        boolean z = linearLayout != null && linearLayout.isAttachedToWindow();
        this.mAnimating = z;
        changeSilenceModeTitle(isSilenceModeEnabled());
        if (this.mSilenceModeExpanded == isExpanded || ((isExpanded && !this.mSlidingButton.isChecked()) || !this.mAnimating)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Silence mode content is alread ");
            stringBuilder.append(isExpanded);
            Log.d(TAG, stringBuilder.toString());
            this.mAnimating = false;
            return;
        }
        int fromYres;
        int toYres;
        this.mSilenceModeExpanded = isExpanded;
        if (isExpanded) {
            fromYres = 0;
            toYres = this.ContentHeight;
        } else {
            fromYres = this.mSilenceModeExpandContent.getHeight();
            toYres = 0;
        }
        ValueAnimator animator = ValueAnimator.ofInt(fromYres, toYres);
        animator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                RingerModeLayout.this.mSilenceModeExpandContent.getLayoutParams().height = ((Integer) animation.getAnimatedValue()).intValue();
                RingerModeLayout.this.mSilenceModeExpandContent.requestLayout();
            }
        });
        animator.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
                ViewGroup dialogParentView = (ViewGroup) RingerModeLayout.this.mDialogView.getParent();
                MarginLayoutParams mlp = (MarginLayoutParams) dialogParentView.getLayoutParams();
                mlp.height = 1000;
                dialogParentView.setLayoutParams(mlp);
            }

            public void onAnimationEnd(Animator animation) {
                ViewGroup dialogParentView = (ViewGroup) RingerModeLayout.this.mDialogView.getParent();
                MarginLayoutParams mlp = (MarginLayoutParams) dialogParentView.getLayoutParams();
                mlp.height = -2;
                dialogParentView.setLayoutParams(mlp);
                RingerModeLayout.this.mVolumeDialog.rescheduleTimeout(true);
                RingerModeLayout.this.mAnimating = false;
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(300);
        animator.start();
        this.mHandler.removeMessages(1);
        this.mHandler.sendEmptyMessage(1);
    }
}
