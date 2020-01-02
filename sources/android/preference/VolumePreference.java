package android.preference;

import android.annotation.UnsupportedAppUsage;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.preference.Preference.BaseSavedState;
import android.preference.PreferenceManager.OnActivityStopListener;
import android.preference.SeekBarVolumizer.Callback;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.SeekBar;
import com.android.internal.R;

@Deprecated
public class VolumePreference extends SeekBarDialogPreference implements OnActivityStopListener, OnKeyListener, Callback {
    private SeekBarVolumizer mSeekBarVolumizer;
    @UnsupportedAppUsage
    private int mStreamType;

    private static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        VolumeStore mVolumeStore = new VolumeStore();

        public SavedState(Parcel source) {
            super(source);
            this.mVolumeStore.volume = source.readInt();
            this.mVolumeStore.originalVolume = source.readInt();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.mVolumeStore.volume);
            dest.writeInt(this.mVolumeStore.originalVolume);
        }

        /* Access modifiers changed, original: 0000 */
        public VolumeStore getVolumeStore() {
            return this.mVolumeStore;
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }
    }

    public static class VolumeStore {
        @UnsupportedAppUsage
        public int originalVolume = -1;
        @UnsupportedAppUsage
        public int volume = -1;
    }

    public VolumePreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VolumePreference, defStyleAttr, defStyleRes);
        this.mStreamType = a.getInt(0, 0);
        a.recycle();
    }

    public VolumePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @UnsupportedAppUsage
    public VolumePreference(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.seekBarDialogPreferenceStyle);
    }

    public VolumePreference(Context context) {
        this(context, null);
    }

    public void setStreamType(int streamType) {
        this.mStreamType = streamType;
    }

    /* Access modifiers changed, original: protected */
    public void onBindDialogView(View view) {
        super.onBindDialogView(view);
        SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekbar);
        this.mSeekBarVolumizer = new SeekBarVolumizer(getContext(), this.mStreamType, null, this);
        this.mSeekBarVolumizer.start();
        this.mSeekBarVolumizer.setSeekBar(seekBar);
        getPreferenceManager().registerOnActivityStopListener(this);
        view.setOnKeyListener(this);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (this.mSeekBarVolumizer == null) {
            return true;
        }
        boolean isdown = event.getAction() == 0;
        if (keyCode == 24) {
            if (isdown) {
                this.mSeekBarVolumizer.changeVolumeBy(1);
            }
            return true;
        } else if (keyCode == 25) {
            if (isdown) {
                this.mSeekBarVolumizer.changeVolumeBy(-1);
            }
            return true;
        } else if (keyCode != 164) {
            return false;
        } else {
            if (isdown) {
                this.mSeekBarVolumizer.muteVolume();
            }
            return true;
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (!positiveResult) {
            SeekBarVolumizer seekBarVolumizer = this.mSeekBarVolumizer;
            if (seekBarVolumizer != null) {
                seekBarVolumizer.revertVolume();
            }
        }
        cleanup();
    }

    public void onActivityStop() {
        SeekBarVolumizer seekBarVolumizer = this.mSeekBarVolumizer;
        if (seekBarVolumizer != null) {
            seekBarVolumizer.stopSample();
        }
    }

    private void cleanup() {
        getPreferenceManager().unregisterOnActivityStopListener(this);
        if (this.mSeekBarVolumizer != null) {
            Dialog dialog = getDialog();
            if (dialog != null && dialog.isShowing()) {
                View view = dialog.getWindow().getDecorView().findViewById(R.id.seekbar);
                if (view != null) {
                    view.setOnKeyListener(null);
                }
                this.mSeekBarVolumizer.revertVolume();
            }
            this.mSeekBarVolumizer.stop();
            this.mSeekBarVolumizer = null;
        }
    }

    public void onSampleStarting(SeekBarVolumizer volumizer) {
        SeekBarVolumizer seekBarVolumizer = this.mSeekBarVolumizer;
        if (seekBarVolumizer != null && volumizer != seekBarVolumizer) {
            seekBarVolumizer.stopSample();
        }
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
    }

    public void onMuted(boolean muted, boolean zenMuted) {
    }

    /* Access modifiers changed, original: protected */
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            return superState;
        }
        SavedState myState = new SavedState(superState);
        SeekBarVolumizer seekBarVolumizer = this.mSeekBarVolumizer;
        if (seekBarVolumizer != null) {
            seekBarVolumizer.onSaveInstanceState(myState.getVolumeStore());
        }
        return myState;
    }

    /* Access modifiers changed, original: protected */
    public void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        SeekBarVolumizer seekBarVolumizer = this.mSeekBarVolumizer;
        if (seekBarVolumizer != null) {
            seekBarVolumizer.onRestoreInstanceState(myState.getVolumeStore());
        }
    }
}
