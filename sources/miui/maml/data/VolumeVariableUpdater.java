package miui.maml.data;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;

public class VolumeVariableUpdater extends NotifierVariableUpdater {
    private static final int SHOW_DELAY_TIME = 1000;
    public static final String VAR_VOLUME_LEVEL = "volume_level";
    public static final String VAR_VOLUME_LEVEL_OLD = "volume_level_old";
    public static final String VAR_VOLUME_TYPE = "volume_type";
    private Handler mHandler = new Handler();
    private final Runnable mResetType = new Runnable() {
        public void run() {
            VolumeVariableUpdater.this.mVolumeType.set(-1.0d);
        }
    };
    private IndexedVariable mVolumeLevel = new IndexedVariable(VAR_VOLUME_LEVEL, getContext().mVariables, true);
    private IndexedVariable mVolumeLevelOld = new IndexedVariable(VAR_VOLUME_LEVEL_OLD, getContext().mVariables, true);
    private IndexedVariable mVolumeType = new IndexedVariable(VAR_VOLUME_TYPE, getContext().mVariables, true);

    public VolumeVariableUpdater(VariableUpdaterManager m) {
        super(m, AudioManager.VOLUME_CHANGED_ACTION);
        this.mVolumeType.set(-1.0d);
    }

    public void onNotify(Context context, Intent intent, Object o) {
        if (intent.getAction().equals(AudioManager.VOLUME_CHANGED_ACTION)) {
            this.mVolumeType.set((double) intent.getIntExtra(AudioManager.EXTRA_VOLUME_STREAM_TYPE, -1));
            int newVolLevel = intent.getIntExtra(AudioManager.EXTRA_VOLUME_STREAM_VALUE, 0);
            this.mVolumeLevel.set((double) newVolLevel);
            int oldVolLevel = intent.getIntExtra(AudioManager.EXTRA_PREV_VOLUME_STREAM_VALUE, 0);
            if (oldVolLevel != newVolLevel) {
                this.mVolumeLevelOld.set((double) oldVolLevel);
            }
            getRoot().requestUpdate();
            this.mHandler.removeCallbacks(this.mResetType);
            this.mHandler.postDelayed(this.mResetType, 1000);
        }
    }
}
