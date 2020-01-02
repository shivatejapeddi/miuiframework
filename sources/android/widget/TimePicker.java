package android.widget;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.res.TypedArray;
import android.icu.util.Calendar;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.Log;
import android.util.MathUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.ViewStructure;
import android.view.accessibility.AccessibilityEvent;
import android.view.autofill.AutofillManager;
import android.view.autofill.AutofillValue;
import android.view.inspector.InspectionCompanion.UninitializedPropertyMapException;
import android.view.inspector.PropertyMapper;
import android.view.inspector.PropertyReader;
import com.android.internal.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;
import java.util.Objects;
import libcore.icu.LocaleData;
import miui.maml.data.VariableNames;

public class TimePicker extends FrameLayout {
    private static final String LOG_TAG = TimePicker.class.getSimpleName();
    public static final int MODE_CLOCK = 2;
    public static final int MODE_SPINNER = 1;
    @UnsupportedAppUsage
    private final TimePickerDelegate mDelegate;
    private final int mMode;

    public interface OnTimeChangedListener {
        void onTimeChanged(TimePicker timePicker, int i, int i2);
    }

    interface TimePickerDelegate {
        void autofill(AutofillValue autofillValue);

        boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent);

        View getAmView();

        AutofillValue getAutofillValue();

        int getBaseline();

        int getHour();

        View getHourView();

        int getMinute();

        View getMinuteView();

        View getPmView();

        boolean is24Hour();

        boolean isEnabled();

        void onPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent);

        void onRestoreInstanceState(Parcelable parcelable);

        Parcelable onSaveInstanceState(Parcelable parcelable);

        void setAutoFillChangeListener(OnTimeChangedListener onTimeChangedListener);

        void setDate(int i, int i2);

        void setEnabled(boolean z);

        void setHour(int i);

        void setIs24Hour(boolean z);

        void setMinute(int i);

        void setOnTimeChangedListener(OnTimeChangedListener onTimeChangedListener);

        boolean validateInput();
    }

    static abstract class AbstractTimePickerDelegate implements TimePickerDelegate {
        protected OnTimeChangedListener mAutoFillChangeListener;
        private long mAutofilledValue;
        protected final Context mContext;
        protected final TimePicker mDelegator;
        protected final Locale mLocale;
        protected OnTimeChangedListener mOnTimeChangedListener;

        protected static class SavedState extends BaseSavedState {
            public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
                public SavedState createFromParcel(Parcel in) {
                    return new SavedState(in);
                }

                public SavedState[] newArray(int size) {
                    return new SavedState[size];
                }
            };
            private final int mCurrentItemShowing;
            private final int mHour;
            private final boolean mIs24HourMode;
            private final int mMinute;

            public SavedState(Parcelable superState, int hour, int minute, boolean is24HourMode) {
                this(superState, hour, minute, is24HourMode, 0);
            }

            public SavedState(Parcelable superState, int hour, int minute, boolean is24HourMode, int currentItemShowing) {
                super(superState);
                this.mHour = hour;
                this.mMinute = minute;
                this.mIs24HourMode = is24HourMode;
                this.mCurrentItemShowing = currentItemShowing;
            }

            private SavedState(Parcel in) {
                super(in);
                this.mHour = in.readInt();
                this.mMinute = in.readInt();
                boolean z = true;
                if (in.readInt() != 1) {
                    z = false;
                }
                this.mIs24HourMode = z;
                this.mCurrentItemShowing = in.readInt();
            }

            public int getHour() {
                return this.mHour;
            }

            public int getMinute() {
                return this.mMinute;
            }

            public boolean is24HourMode() {
                return this.mIs24HourMode;
            }

            public int getCurrentItemShowing() {
                return this.mCurrentItemShowing;
            }

            public void writeToParcel(Parcel dest, int flags) {
                super.writeToParcel(dest, flags);
                dest.writeInt(this.mHour);
                dest.writeInt(this.mMinute);
                dest.writeInt(this.mIs24HourMode);
                dest.writeInt(this.mCurrentItemShowing);
            }
        }

        public AbstractTimePickerDelegate(TimePicker delegator, Context context) {
            this.mDelegator = delegator;
            this.mContext = context;
            this.mLocale = context.getResources().getConfiguration().locale;
        }

        public void setOnTimeChangedListener(OnTimeChangedListener callback) {
            this.mOnTimeChangedListener = callback;
        }

        public void setAutoFillChangeListener(OnTimeChangedListener callback) {
            this.mAutoFillChangeListener = callback;
        }

        public final void autofill(AutofillValue value) {
            if (value == null || !value.isDate()) {
                String access$000 = TimePicker.LOG_TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(value);
                stringBuilder.append(" could not be autofilled into ");
                stringBuilder.append(this);
                Log.w(access$000, stringBuilder.toString());
                return;
            }
            long time = value.getDateValue();
            Calendar cal = Calendar.getInstance(this.mLocale);
            cal.setTimeInMillis(time);
            setDate(cal.get(11), cal.get(12));
            this.mAutofilledValue = time;
        }

        public final AutofillValue getAutofillValue() {
            long j = this.mAutofilledValue;
            if (j != 0) {
                return AutofillValue.forDate(j);
            }
            Calendar cal = Calendar.getInstance(this.mLocale);
            cal.set(11, getHour());
            cal.set(12, getMinute());
            return AutofillValue.forDate(cal.getTimeInMillis());
        }

        /* Access modifiers changed, original: protected */
        public void resetAutofilledValue() {
            this.mAutofilledValue = 0;
        }
    }

    public final class InspectionCompanion implements android.view.inspector.InspectionCompanion<TimePicker> {
        private int m24HourId;
        private int mHourId;
        private int mMinuteId;
        private boolean mPropertiesMapped = false;
        private int mTimePickerModeId;

        public void mapProperties(PropertyMapper propertyMapper) {
            this.m24HourId = propertyMapper.mapBoolean("24Hour", 0);
            this.mHourId = propertyMapper.mapInt("hour", 0);
            this.mMinuteId = propertyMapper.mapInt(VariableNames.VAR_MINUTE, 0);
            SparseArray<String> timePickerModeEnumMapping = new SparseArray();
            timePickerModeEnumMapping.put(1, "spinner");
            timePickerModeEnumMapping.put(2, "clock");
            Objects.requireNonNull(timePickerModeEnumMapping);
            this.mTimePickerModeId = propertyMapper.mapIntEnum("timePickerMode", 16843956, new -$$Lambda$QY3N4tzLteuFdjRnyJFCbR1ajSI(timePickerModeEnumMapping));
            this.mPropertiesMapped = true;
        }

        public void readProperties(TimePicker node, PropertyReader propertyReader) {
            if (this.mPropertiesMapped) {
                propertyReader.readBoolean(this.m24HourId, node.is24HourView());
                propertyReader.readInt(this.mHourId, node.getHour());
                propertyReader.readInt(this.mMinuteId, node.getMinute());
                propertyReader.readIntEnum(this.mTimePickerModeId, node.getMode());
                return;
            }
            throw new UninitializedPropertyMapException();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TimePickerMode {
    }

    public TimePicker(Context context) {
        this(context, null);
    }

    public TimePicker(Context context, AttributeSet attrs) {
        this(context, attrs, 16843933);
    }

    public TimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TimePicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        if (getImportantForAutofill() == 0) {
            setImportantForAutofill(1);
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TimePicker, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.TimePicker, attrs, a, defStyleAttr, defStyleRes);
        boolean isDialogMode = a.getBoolean(true, false);
        int requestedMode = a.getInt(8, 1);
        a.recycle();
        if (requestedMode == 2 && isDialogMode) {
            this.mMode = context.getResources().getInteger(R.integer.time_picker_mode);
        } else {
            this.mMode = requestedMode;
        }
        if (this.mMode != 2) {
            this.mDelegate = new TimePickerSpinnerDelegate(this, context, attrs, defStyleAttr, defStyleRes);
        } else {
            this.mDelegate = new TimePickerClockDelegate(this, context, attrs, defStyleAttr, defStyleRes);
        }
        this.mDelegate.setAutoFillChangeListener(new -$$Lambda$TimePicker$2FhAB9WgnLgn4zn4f9rRT7DNfjw(this, context));
    }

    public /* synthetic */ void lambda$new$0$TimePicker(Context context, TimePicker v, int h, int m) {
        AutofillManager afm = (AutofillManager) context.getSystemService(AutofillManager.class);
        if (afm != null) {
            afm.notifyValueChanged(this);
        }
    }

    public int getMode() {
        return this.mMode;
    }

    public void setHour(int hour) {
        this.mDelegate.setHour(MathUtils.constrain(hour, 0, 23));
    }

    public int getHour() {
        return this.mDelegate.getHour();
    }

    public void setMinute(int minute) {
        this.mDelegate.setMinute(MathUtils.constrain(minute, 0, 59));
    }

    public int getMinute() {
        return this.mDelegate.getMinute();
    }

    @Deprecated
    public void setCurrentHour(Integer currentHour) {
        setHour(currentHour.intValue());
    }

    @Deprecated
    public Integer getCurrentHour() {
        return Integer.valueOf(getHour());
    }

    @Deprecated
    public void setCurrentMinute(Integer currentMinute) {
        setMinute(currentMinute.intValue());
    }

    @Deprecated
    public Integer getCurrentMinute() {
        return Integer.valueOf(getMinute());
    }

    public void setIs24HourView(Boolean is24HourView) {
        if (is24HourView != null) {
            this.mDelegate.setIs24Hour(is24HourView.booleanValue());
        }
    }

    public boolean is24HourView() {
        return this.mDelegate.is24Hour();
    }

    public void setOnTimeChangedListener(OnTimeChangedListener onTimeChangedListener) {
        this.mDelegate.setOnTimeChangedListener(onTimeChangedListener);
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.mDelegate.setEnabled(enabled);
    }

    public boolean isEnabled() {
        return this.mDelegate.isEnabled();
    }

    public int getBaseline() {
        return this.mDelegate.getBaseline();
    }

    public boolean validateInput() {
        return this.mDelegate.validateInput();
    }

    /* Access modifiers changed, original: protected */
    public Parcelable onSaveInstanceState() {
        return this.mDelegate.onSaveInstanceState(super.onSaveInstanceState());
    }

    /* Access modifiers changed, original: protected */
    public void onRestoreInstanceState(Parcelable state) {
        BaseSavedState ss = (BaseSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        this.mDelegate.onRestoreInstanceState(ss);
    }

    public CharSequence getAccessibilityClassName() {
        return TimePicker.class.getName();
    }

    public boolean dispatchPopulateAccessibilityEventInternal(AccessibilityEvent event) {
        return this.mDelegate.dispatchPopulateAccessibilityEvent(event);
    }

    public View getHourView() {
        return this.mDelegate.getHourView();
    }

    public View getMinuteView() {
        return this.mDelegate.getMinuteView();
    }

    public View getAmView() {
        return this.mDelegate.getAmView();
    }

    public View getPmView() {
        return this.mDelegate.getPmView();
    }

    static String[] getAmPmStrings(Context context) {
        LocaleData d = LocaleData.get(context.getResources().getConfiguration().locale);
        String[] result = new String[2];
        result[0] = d.amPm[0].length() > 4 ? d.narrowAm : d.amPm[0];
        result[1] = d.amPm[1].length() > 4 ? d.narrowPm : d.amPm[1];
        return result;
    }

    public void dispatchProvideAutofillStructure(ViewStructure structure, int flags) {
        structure.setAutofillId(getAutofillId());
        onProvideAutofillStructure(structure, flags);
    }

    public void autofill(AutofillValue value) {
        if (isEnabled()) {
            this.mDelegate.autofill(value);
        }
    }

    public int getAutofillType() {
        return isEnabled() ? 4 : 0;
    }

    public AutofillValue getAutofillValue() {
        return isEnabled() ? this.mDelegate.getAutofillValue() : null;
    }
}
