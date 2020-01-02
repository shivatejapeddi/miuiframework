package android.widget;

import android.app.backup.FullBackup;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcelable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.text.style.TtsSpan.VerbatimBuilder;
import android.util.AttributeSet;
import android.util.StateSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.AccessibilityDelegate;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout.LayoutParams;
import com.android.internal.R;
import com.android.internal.widget.NumericTextView;
import com.android.internal.widget.NumericTextView.OnValueChangedListener;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Calendar;
import java.util.List;

class TimePickerClockDelegate extends AbstractTimePickerDelegate {
    private static final int AM = 0;
    private static final int[] ATTRS_DISABLED_ALPHA = new int[]{16842803};
    private static final int[] ATTRS_TEXT_COLOR = new int[]{16842904};
    private static final long DELAY_COMMIT_MILLIS = 2000;
    private static final int FROM_EXTERNAL_API = 0;
    private static final int FROM_INPUT_PICKER = 2;
    private static final int FROM_RADIAL_PICKER = 1;
    private static final int HOURS_IN_HALF_DAY = 12;
    private static final int HOUR_INDEX = 0;
    private static final int MINUTE_INDEX = 1;
    private static final int PM = 1;
    private boolean mAllowAutoAdvance;
    private final RadioButton mAmLabel;
    private final View mAmPmLayout;
    private final OnClickListener mClickListener = new OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.am_label /*16908726*/:
                    TimePickerClockDelegate.this.setAmOrPm(0);
                    break;
                case R.id.hours /*16909002*/:
                    TimePickerClockDelegate.this.setCurrentItemShowing(0, true, true);
                    break;
                case R.id.minutes /*16909133*/:
                    TimePickerClockDelegate.this.setCurrentItemShowing(1, true, true);
                    break;
                case R.id.pm_label /*16909275*/:
                    TimePickerClockDelegate.this.setAmOrPm(1);
                    break;
                default:
                    return;
            }
            TimePickerClockDelegate.this.tryVibrate();
        }
    };
    private final Runnable mCommitHour = new Runnable() {
        public void run() {
            TimePickerClockDelegate timePickerClockDelegate = TimePickerClockDelegate.this;
            timePickerClockDelegate.setHour(timePickerClockDelegate.mHourView.getValue());
        }
    };
    private final Runnable mCommitMinute = new Runnable() {
        public void run() {
            TimePickerClockDelegate timePickerClockDelegate = TimePickerClockDelegate.this;
            timePickerClockDelegate.setMinute(timePickerClockDelegate.mMinuteView.getValue());
        }
    };
    private int mCurrentHour;
    private int mCurrentMinute;
    private final OnValueChangedListener mDigitEnteredListener = new OnValueChangedListener() {
        public void onValueChanged(NumericTextView view, int value, boolean isValid, boolean isFinished) {
            Runnable commitCallback;
            View nextFocusTarget;
            if (view == TimePickerClockDelegate.this.mHourView) {
                commitCallback = TimePickerClockDelegate.this.mCommitHour;
                nextFocusTarget = view.isFocused() ? TimePickerClockDelegate.this.mMinuteView : null;
            } else if (view == TimePickerClockDelegate.this.mMinuteView) {
                commitCallback = TimePickerClockDelegate.this.mCommitMinute;
                nextFocusTarget = null;
            } else {
                return;
            }
            view.removeCallbacks(commitCallback);
            if (isValid) {
                if (isFinished) {
                    commitCallback.run();
                    if (nextFocusTarget != null) {
                        nextFocusTarget.requestFocus();
                    }
                } else {
                    view.postDelayed(commitCallback, 2000);
                }
            }
        }
    };
    private final OnFocusChangeListener mFocusListener = new OnFocusChangeListener() {
        public void onFocusChange(View v, boolean focused) {
            if (focused) {
                switch (v.getId()) {
                    case R.id.am_label /*16908726*/:
                        TimePickerClockDelegate.this.setAmOrPm(0);
                        break;
                    case R.id.hours /*16909002*/:
                        TimePickerClockDelegate.this.setCurrentItemShowing(0, true, true);
                        break;
                    case R.id.minutes /*16909133*/:
                        TimePickerClockDelegate.this.setCurrentItemShowing(1, true, true);
                        break;
                    case R.id.pm_label /*16909275*/:
                        TimePickerClockDelegate.this.setAmOrPm(1);
                        break;
                    default:
                        return;
                }
                TimePickerClockDelegate.this.tryVibrate();
            }
        }
    };
    private boolean mHourFormatShowLeadingZero;
    private boolean mHourFormatStartsAtZero;
    private final NumericTextView mHourView;
    private boolean mIs24Hour;
    private boolean mIsAmPmAtLeft = false;
    private boolean mIsAmPmAtTop = false;
    private boolean mIsEnabled = true;
    private boolean mLastAnnouncedIsHour;
    private CharSequence mLastAnnouncedText;
    private final NumericTextView mMinuteView;
    private final OnValueSelectedListener mOnValueSelectedListener = new OnValueSelectedListener() {
        public void onValueSelected(int pickerType, int newValue, boolean autoAdvance) {
            boolean valueChanged = false;
            if (pickerType == 0) {
                if (TimePickerClockDelegate.this.getHour() != newValue) {
                    valueChanged = true;
                }
                boolean isTransition = TimePickerClockDelegate.this.mAllowAutoAdvance && autoAdvance;
                TimePickerClockDelegate.this.setHourInternal(newValue, 1, !isTransition, true);
                if (isTransition) {
                    TimePickerClockDelegate.this.setCurrentItemShowing(1, true, false);
                    int localizedHour = TimePickerClockDelegate.this.getLocalizedHour(newValue);
                    TimePicker timePicker = TimePickerClockDelegate.this.mDelegator;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(localizedHour);
                    stringBuilder.append(". ");
                    stringBuilder.append(TimePickerClockDelegate.this.mSelectMinutes);
                    timePicker.announceForAccessibility(stringBuilder.toString());
                }
            } else if (pickerType == 1) {
                if (TimePickerClockDelegate.this.getMinute() != newValue) {
                    valueChanged = true;
                }
                TimePickerClockDelegate.this.setMinuteInternal(newValue, 1, true);
            }
            if (TimePickerClockDelegate.this.mOnTimeChangedListener != null && valueChanged) {
                TimePickerClockDelegate.this.mOnTimeChangedListener.onTimeChanged(TimePickerClockDelegate.this.mDelegator, TimePickerClockDelegate.this.getHour(), TimePickerClockDelegate.this.getMinute());
            }
        }
    };
    private final OnValueTypedListener mOnValueTypedListener = new OnValueTypedListener() {
        public void onValueChanged(int pickerType, int newValue) {
            if (pickerType == 0) {
                TimePickerClockDelegate.this.setHourInternal(newValue, 2, false, true);
            } else if (pickerType == 1) {
                TimePickerClockDelegate.this.setMinuteInternal(newValue, 2, true);
            } else if (pickerType == 2) {
                TimePickerClockDelegate.this.setAmOrPm(newValue);
            }
        }
    };
    private final RadioButton mPmLabel;
    private boolean mRadialPickerModeEnabled = true;
    private final View mRadialTimePickerHeader;
    private final ImageButton mRadialTimePickerModeButton;
    private final String mRadialTimePickerModeEnabledDescription;
    private final RadialTimePickerView mRadialTimePickerView;
    private final String mSelectHours;
    private final String mSelectMinutes;
    private final TextView mSeparatorView;
    private final Calendar mTempCalendar;
    private final View mTextInputPickerHeader;
    private final String mTextInputPickerModeEnabledDescription;
    private final TextInputTimePickerView mTextInputPickerView;

    @Retention(RetentionPolicy.SOURCE)
    private @interface ChangeSource {
    }

    private static class ClickActionDelegate extends AccessibilityDelegate {
        private final AccessibilityAction mClickAction;

        public ClickActionDelegate(Context context, int resId) {
            this.mClickAction = new AccessibilityAction(16, context.getString(resId));
        }

        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfo info) {
            super.onInitializeAccessibilityNodeInfo(host, info);
            info.addAction(this.mClickAction);
        }
    }

    private static class NearestTouchDelegate implements OnTouchListener {
        private View mInitialTouchTarget;

        private NearestTouchDelegate() {
        }

        /* synthetic */ NearestTouchDelegate(AnonymousClass1 x0) {
            this();
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked == 0) {
                if (view instanceof ViewGroup) {
                    this.mInitialTouchTarget = findNearestChild((ViewGroup) view, (int) motionEvent.getX(), (int) motionEvent.getY());
                } else {
                    this.mInitialTouchTarget = null;
                }
            }
            View child = this.mInitialTouchTarget;
            if (child == null) {
                return false;
            }
            float offsetX = (float) (view.getScrollX() - child.getLeft());
            float offsetY = (float) (view.getScrollY() - child.getTop());
            motionEvent.offsetLocation(offsetX, offsetY);
            boolean handled = child.dispatchTouchEvent(motionEvent);
            motionEvent.offsetLocation(-offsetX, -offsetY);
            if (actionMasked == 1 || actionMasked == 3) {
                this.mInitialTouchTarget = null;
            }
            return handled;
        }

        private View findNearestChild(ViewGroup v, int x, int y) {
            View bestChild = null;
            int bestDist = Integer.MAX_VALUE;
            int count = v.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = v.getChildAt(i);
                int dX = x - (child.getLeft() + (child.getWidth() / 2));
                int dY = y - (child.getTop() + (child.getHeight() / 2));
                int dist = (dX * dX) + (dY * dY);
                if (bestDist > dist) {
                    bestChild = child;
                    bestDist = dist;
                }
            }
            return bestChild;
        }
    }

    public TimePickerClockDelegate(TimePicker delegator, Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        Context context2 = context;
        AttributeSet attributeSet = attrs;
        int i = defStyleAttr;
        int i2 = defStyleRes;
        super(delegator, context);
        TypedArray a = this.mContext.obtainStyledAttributes(attributeSet, R.styleable.TimePicker, i, i2);
        LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resources res = this.mContext.getResources();
        this.mSelectHours = res.getString(R.string.select_hours);
        this.mSelectMinutes = res.getString(R.string.select_minutes);
        View mainView = inflater.inflate(a.getResourceId(12, R.layout.time_picker_material), (ViewGroup) delegator);
        mainView.setSaveFromParentEnabled(false);
        this.mRadialTimePickerHeader = mainView.findViewById(R.id.time_header);
        this.mRadialTimePickerHeader.setOnTouchListener(new NearestTouchDelegate());
        this.mHourView = (NumericTextView) mainView.findViewById(R.id.hours);
        this.mHourView.setOnClickListener(this.mClickListener);
        this.mHourView.setOnFocusChangeListener(this.mFocusListener);
        this.mHourView.setOnDigitEnteredListener(this.mDigitEnteredListener);
        this.mHourView.setAccessibilityDelegate(new ClickActionDelegate(context2, R.string.select_hours));
        this.mSeparatorView = (TextView) mainView.findViewById(R.id.separator);
        this.mMinuteView = (NumericTextView) mainView.findViewById(R.id.minutes);
        this.mMinuteView.setOnClickListener(this.mClickListener);
        this.mMinuteView.setOnFocusChangeListener(this.mFocusListener);
        this.mMinuteView.setOnDigitEnteredListener(this.mDigitEnteredListener);
        this.mMinuteView.setAccessibilityDelegate(new ClickActionDelegate(context2, R.string.select_minutes));
        this.mMinuteView.setRange(0, 59);
        this.mAmPmLayout = mainView.findViewById(R.id.ampm_layout);
        this.mAmPmLayout.setOnTouchListener(new NearestTouchDelegate());
        String[] amPmStrings = TimePicker.getAmPmStrings(context);
        this.mAmLabel = (RadioButton) this.mAmPmLayout.findViewById(R.id.am_label);
        this.mAmLabel.setText(obtainVerbatim(amPmStrings[0]));
        this.mAmLabel.setOnClickListener(this.mClickListener);
        ensureMinimumTextWidth(this.mAmLabel);
        this.mPmLabel = (RadioButton) this.mAmPmLayout.findViewById(R.id.pm_label);
        this.mPmLabel.setText(obtainVerbatim(amPmStrings[1]));
        this.mPmLabel.setOnClickListener(this.mClickListener);
        ensureMinimumTextWidth(this.mPmLabel);
        ColorStateList headerTextColor = null;
        int timeHeaderTextAppearance = a.getResourceId(1, 0);
        if (timeHeaderTextAppearance != 0) {
            TypedArray textAppearance = this.mContext.obtainStyledAttributes(null, ATTRS_TEXT_COLOR, 0, timeHeaderTextAppearance);
            headerTextColor = applyLegacyColorFixes(textAppearance.getColorStateList(0));
            textAppearance.recycle();
        }
        if (headerTextColor == null) {
            headerTextColor = a.getColorStateList(11);
        }
        this.mTextInputPickerHeader = mainView.findViewById(R.id.input_header);
        if (headerTextColor != null) {
            this.mHourView.setTextColor(headerTextColor);
            this.mSeparatorView.setTextColor(headerTextColor);
            this.mMinuteView.setTextColor(headerTextColor);
            this.mAmLabel.setTextColor(headerTextColor);
            this.mPmLabel.setTextColor(headerTextColor);
        }
        if (a.hasValueOrEmpty(0)) {
            this.mRadialTimePickerHeader.setBackground(a.getDrawable(0));
            this.mTextInputPickerHeader.setBackground(a.getDrawable(0));
        }
        a.recycle();
        this.mRadialTimePickerView = (RadialTimePickerView) mainView.findViewById(R.id.radial_picker);
        this.mRadialTimePickerView.applyAttributes(attributeSet, i, i2);
        this.mRadialTimePickerView.setOnValueSelectedListener(this.mOnValueSelectedListener);
        this.mTextInputPickerView = (TextInputTimePickerView) mainView.findViewById(R.id.input_mode);
        this.mTextInputPickerView.setListener(this.mOnValueTypedListener);
        this.mRadialTimePickerModeButton = (ImageButton) mainView.findViewById(R.id.toggle_mode);
        this.mRadialTimePickerModeButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                TimePickerClockDelegate.this.toggleRadialPickerMode();
            }
        });
        this.mRadialTimePickerModeEnabledDescription = context.getResources().getString(R.string.time_picker_radial_mode_description);
        this.mTextInputPickerModeEnabledDescription = context.getResources().getString(R.string.time_picker_text_input_mode_description);
        this.mAllowAutoAdvance = true;
        updateHourFormat();
        this.mTempCalendar = Calendar.getInstance(this.mLocale);
        initialize(this.mTempCalendar.get(11), this.mTempCalendar.get(12), this.mIs24Hour, 0);
    }

    private void toggleRadialPickerMode() {
        if (this.mRadialPickerModeEnabled) {
            this.mRadialTimePickerView.setVisibility(8);
            this.mRadialTimePickerHeader.setVisibility(8);
            this.mTextInputPickerHeader.setVisibility(0);
            this.mTextInputPickerView.setVisibility(0);
            this.mRadialTimePickerModeButton.setImageResource(R.drawable.btn_clock_material);
            this.mRadialTimePickerModeButton.setContentDescription(this.mRadialTimePickerModeEnabledDescription);
            this.mRadialPickerModeEnabled = false;
            return;
        }
        this.mRadialTimePickerView.setVisibility(0);
        this.mRadialTimePickerHeader.setVisibility(0);
        this.mTextInputPickerHeader.setVisibility(8);
        this.mTextInputPickerView.setVisibility(8);
        this.mRadialTimePickerModeButton.setImageResource(R.drawable.btn_keyboard_key_material);
        this.mRadialTimePickerModeButton.setContentDescription(this.mTextInputPickerModeEnabledDescription);
        updateTextInputPicker();
        InputMethodManager imm = (InputMethodManager) this.mContext.getSystemService(InputMethodManager.class);
        if (imm != null) {
            imm.hideSoftInputFromWindow(this.mDelegator.getWindowToken(), 0);
        }
        this.mRadialPickerModeEnabled = true;
    }

    public boolean validateInput() {
        return this.mTextInputPickerView.validateInput();
    }

    private static void ensureMinimumTextWidth(TextView v) {
        v.measure(0, 0);
        int minWidth = v.getMeasuredWidth();
        v.setMinWidth(minWidth);
        v.setMinimumWidth(minWidth);
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x0055  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0052  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0074 A:{LOOP_END, LOOP:1: B:30:0x0070->B:32:0x0074} */
    private void updateHourFormat() {
        /*
        r10 = this;
        r0 = r10.mLocale;
        r1 = r10.mIs24Hour;
        if (r1 == 0) goto L_0x0009;
    L_0x0006:
        r1 = "Hm";
        goto L_0x000b;
    L_0x0009:
        r1 = "hm";
    L_0x000b:
        r0 = android.text.format.DateFormat.getBestDateTimePattern(r0, r1);
        r1 = r0.length();
        r2 = 0;
        r3 = 0;
        r4 = 0;
    L_0x0016:
        r5 = 72;
        r6 = 75;
        if (r4 >= r1) goto L_0x003e;
    L_0x001c:
        r7 = r0.charAt(r4);
        if (r7 == r5) goto L_0x0030;
    L_0x0022:
        r8 = 104; // 0x68 float:1.46E-43 double:5.14E-322;
        if (r7 == r8) goto L_0x0030;
    L_0x0026:
        if (r7 == r6) goto L_0x0030;
    L_0x0028:
        r8 = 107; // 0x6b float:1.5E-43 double:5.3E-322;
        if (r7 != r8) goto L_0x002d;
    L_0x002c:
        goto L_0x0030;
    L_0x002d:
        r4 = r4 + 1;
        goto L_0x0016;
    L_0x0030:
        r3 = r7;
        r8 = r4 + 1;
        if (r8 >= r1) goto L_0x003e;
    L_0x0035:
        r8 = r4 + 1;
        r8 = r0.charAt(r8);
        if (r7 != r8) goto L_0x003e;
    L_0x003d:
        r2 = 1;
    L_0x003e:
        r10.mHourFormatShowLeadingZero = r2;
        r4 = 1;
        if (r3 == r6) goto L_0x0048;
    L_0x0043:
        if (r3 != r5) goto L_0x0046;
    L_0x0045:
        goto L_0x0048;
    L_0x0046:
        r5 = 0;
        goto L_0x0049;
    L_0x0048:
        r5 = r4;
    L_0x0049:
        r10.mHourFormatStartsAtZero = r5;
        r5 = r10.mHourFormatStartsAtZero;
        r4 = r4 ^ r5;
        r5 = r10.mIs24Hour;
        if (r5 == 0) goto L_0x0055;
    L_0x0052:
        r5 = 23;
        goto L_0x0057;
    L_0x0055:
        r5 = 11;
    L_0x0057:
        r5 = r5 + r4;
        r6 = r10.mHourView;
        r6.setRange(r4, r5);
        r6 = r10.mHourView;
        r7 = r10.mHourFormatShowLeadingZero;
        r6.setShowLeadingZeroes(r7);
        r6 = r10.mLocale;
        r6 = android.icu.text.DecimalFormatSymbols.getInstance(r6);
        r6 = r6.getDigitStrings();
        r7 = 0;
        r8 = 0;
    L_0x0070:
        r9 = 10;
        if (r8 >= r9) goto L_0x0081;
    L_0x0074:
        r9 = r6[r8];
        r9 = r9.length();
        r7 = java.lang.Math.max(r7, r9);
        r8 = r8 + 1;
        goto L_0x0070;
    L_0x0081:
        r8 = r10.mTextInputPickerView;
        r9 = r7 * 2;
        r8.setHourFormat(r9);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.TimePickerClockDelegate.updateHourFormat():void");
    }

    static final CharSequence obtainVerbatim(String text) {
        return new SpannableStringBuilder().append((CharSequence) text, new VerbatimBuilder(text).build(), 0);
    }

    private ColorStateList applyLegacyColorFixes(ColorStateList color) {
        if (color == null || color.hasState(16843518)) {
            return color;
        }
        int activatedColor;
        int defaultColor;
        if (color.hasState(16842913)) {
            activatedColor = color.getColorForState(StateSet.get(10), 0);
            defaultColor = color.getColorForState(StateSet.get(8), 0);
        } else {
            activatedColor = color.getDefaultColor();
            defaultColor = multiplyAlphaComponent(activatedColor, this.mContext.obtainStyledAttributes(ATTRS_DISABLED_ALPHA).getFloat(0, 0.3f));
        }
        if (activatedColor == 0 || defaultColor == 0) {
            return null;
        }
        r5 = new int[2][];
        r5[0] = new int[]{16843518};
        r5[1] = new int[0];
        return new ColorStateList(r5, new int[]{activatedColor, defaultColor});
    }

    private int multiplyAlphaComponent(int color, float alphaMod) {
        return (((int) ((((float) ((color >> 24) & 255)) * alphaMod) + 1056964608)) << 24) | (16777215 & color);
    }

    private void initialize(int hourOfDay, int minute, boolean is24HourView, int index) {
        this.mCurrentHour = hourOfDay;
        this.mCurrentMinute = minute;
        this.mIs24Hour = is24HourView;
        updateUI(index);
    }

    private void updateUI(int index) {
        updateHeaderAmPm();
        updateHeaderHour(this.mCurrentHour, false);
        updateHeaderSeparator();
        updateHeaderMinute(this.mCurrentMinute, false);
        updateRadialPicker(index);
        updateTextInputPicker();
        this.mDelegator.invalidate();
    }

    private void updateTextInputPicker() {
        this.mTextInputPickerView.updateTextInputValues(getLocalizedHour(this.mCurrentHour), this.mCurrentMinute, this.mCurrentHour < 12 ? 0 : 1, this.mIs24Hour, this.mHourFormatStartsAtZero);
    }

    private void updateRadialPicker(int index) {
        this.mRadialTimePickerView.initialize(this.mCurrentHour, this.mCurrentMinute, this.mIs24Hour);
        setCurrentItemShowing(index, false, true);
    }

    private void updateHeaderAmPm() {
        if (this.mIs24Hour) {
            this.mAmPmLayout.setVisibility(8);
            return;
        }
        setAmPmStart(DateFormat.getBestDateTimePattern(this.mLocale, "hm").startsWith(FullBackup.APK_TREE_TOKEN));
        updateAmPmLabelStates(this.mCurrentHour < 12 ? 0 : 1);
    }

    private void setAmPmStart(boolean isAmPmAtStart) {
        LayoutParams params = (LayoutParams) this.mAmPmLayout.getLayoutParams();
        int margin;
        if (params.getRule(1) != 0 || params.getRule(0) != 0) {
            boolean isAmPmAtLeft;
            margin = (int) (this.mContext.getResources().getDisplayMetrics().density * 1090519040);
            if (TextUtils.getLayoutDirectionFromLocale(this.mLocale) == 0) {
                isAmPmAtLeft = isAmPmAtStart;
            } else {
                isAmPmAtLeft = isAmPmAtStart ^ 1;
            }
            if (isAmPmAtLeft) {
                params.removeRule(1);
                params.addRule(0, this.mHourView.getId());
            } else {
                params.removeRule(0);
                params.addRule(1, this.mMinuteView.getId());
            }
            if (isAmPmAtStart) {
                params.setMarginStart(0);
                params.setMarginEnd(margin);
            } else {
                params.setMarginStart(margin);
                params.setMarginEnd(0);
            }
            this.mIsAmPmAtLeft = isAmPmAtLeft;
        } else if (!(params.getRule(3) == 0 && params.getRule(2) == 0)) {
            if (this.mIsAmPmAtTop != isAmPmAtStart) {
                if (isAmPmAtStart) {
                    margin = params.getRule(3);
                    params.removeRule(3);
                    params.addRule(2, margin);
                } else {
                    margin = params.getRule(2);
                    params.removeRule(2);
                    params.addRule(3, margin);
                }
                View otherView = this.mRadialTimePickerHeader.findViewById(margin);
                int top = otherView.getPaddingTop();
                otherView.setPadding(otherView.getPaddingLeft(), otherView.getPaddingBottom(), otherView.getPaddingRight(), top);
                this.mIsAmPmAtTop = isAmPmAtStart;
            } else {
                return;
            }
        }
        this.mAmPmLayout.setLayoutParams(params);
    }

    public void setDate(int hour, int minute) {
        setHourInternal(hour, 0, true, false);
        setMinuteInternal(minute, 0, false);
        onTimeChanged();
    }

    public void setHour(int hour) {
        setHourInternal(hour, 0, true, true);
    }

    private void setHourInternal(int hour, int source, boolean announce, boolean notify) {
        if (this.mCurrentHour != hour) {
            resetAutofilledValue();
            this.mCurrentHour = hour;
            updateHeaderHour(hour, announce);
            updateHeaderAmPm();
            int i = 1;
            if (source != 1) {
                this.mRadialTimePickerView.setCurrentHour(hour);
                RadialTimePickerView radialTimePickerView = this.mRadialTimePickerView;
                if (hour < 12) {
                    i = 0;
                }
                radialTimePickerView.setAmOrPm(i);
            }
            if (source != 2) {
                updateTextInputPicker();
            }
            this.mDelegator.invalidate();
            if (notify) {
                onTimeChanged();
            }
        }
    }

    public int getHour() {
        int currentHour = this.mRadialTimePickerView.getCurrentHour();
        if (this.mIs24Hour) {
            return currentHour;
        }
        if (this.mRadialTimePickerView.getAmOrPm() == 1) {
            return (currentHour % 12) + 12;
        }
        return currentHour % 12;
    }

    public void setMinute(int minute) {
        setMinuteInternal(minute, 0, true);
    }

    private void setMinuteInternal(int minute, int source, boolean notify) {
        if (this.mCurrentMinute != minute) {
            resetAutofilledValue();
            this.mCurrentMinute = minute;
            updateHeaderMinute(minute, true);
            if (source != 1) {
                this.mRadialTimePickerView.setCurrentMinute(minute);
            }
            if (source != 2) {
                updateTextInputPicker();
            }
            this.mDelegator.invalidate();
            if (notify) {
                onTimeChanged();
            }
        }
    }

    public int getMinute() {
        return this.mRadialTimePickerView.getCurrentMinute();
    }

    public void setIs24Hour(boolean is24Hour) {
        if (this.mIs24Hour != is24Hour) {
            this.mIs24Hour = is24Hour;
            this.mCurrentHour = getHour();
            updateHourFormat();
            updateUI(this.mRadialTimePickerView.getCurrentItemShowing());
        }
    }

    public boolean is24Hour() {
        return this.mIs24Hour;
    }

    public void setEnabled(boolean enabled) {
        this.mHourView.setEnabled(enabled);
        this.mMinuteView.setEnabled(enabled);
        this.mAmLabel.setEnabled(enabled);
        this.mPmLabel.setEnabled(enabled);
        this.mRadialTimePickerView.setEnabled(enabled);
        this.mIsEnabled = enabled;
    }

    public boolean isEnabled() {
        return this.mIsEnabled;
    }

    public int getBaseline() {
        return -1;
    }

    public Parcelable onSaveInstanceState(Parcelable superState) {
        return new SavedState(superState, getHour(), getMinute(), is24Hour(), getCurrentItemShowing());
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState ss = (SavedState) state;
            initialize(ss.getHour(), ss.getMinute(), ss.is24HourMode(), ss.getCurrentItemShowing());
            this.mRadialTimePickerView.invalidate();
        }
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        onPopulateAccessibilityEvent(event);
        return true;
    }

    public void onPopulateAccessibilityEvent(AccessibilityEvent event) {
        int flags;
        if (this.mIs24Hour) {
            flags = 1 | 128;
        } else {
            flags = 1 | 64;
        }
        this.mTempCalendar.set(11, getHour());
        this.mTempCalendar.set(12, getMinute());
        String selectedTime = DateUtils.formatDateTime(this.mContext, this.mTempCalendar.getTimeInMillis(), flags);
        String selectionMode = this.mRadialTimePickerView.getCurrentItemShowing() == 0 ? this.mSelectHours : this.mSelectMinutes;
        List text = event.getText();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(selectedTime);
        stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        stringBuilder.append(selectionMode);
        text.add(stringBuilder.toString());
    }

    public View getHourView() {
        return this.mHourView;
    }

    public View getMinuteView() {
        return this.mMinuteView;
    }

    public View getAmView() {
        return this.mAmLabel;
    }

    public View getPmView() {
        return this.mPmLabel;
    }

    private int getCurrentItemShowing() {
        return this.mRadialTimePickerView.getCurrentItemShowing();
    }

    private void onTimeChanged() {
        this.mDelegator.sendAccessibilityEvent(4);
        if (this.mOnTimeChangedListener != null) {
            this.mOnTimeChangedListener.onTimeChanged(this.mDelegator, getHour(), getMinute());
        }
        if (this.mAutoFillChangeListener != null) {
            this.mAutoFillChangeListener.onTimeChanged(this.mDelegator, getHour(), getMinute());
        }
    }

    private void tryVibrate() {
        this.mDelegator.performHapticFeedback(4);
    }

    private void updateAmPmLabelStates(int amOrPm) {
        boolean isPm = false;
        boolean isAm = amOrPm == 0;
        this.mAmLabel.setActivated(isAm);
        this.mAmLabel.setChecked(isAm);
        if (amOrPm == 1) {
            isPm = true;
        }
        this.mPmLabel.setActivated(isPm);
        this.mPmLabel.setChecked(isPm);
    }

    private int getLocalizedHour(int hourOfDay) {
        if (!this.mIs24Hour) {
            hourOfDay %= 12;
        }
        if (this.mHourFormatStartsAtZero || hourOfDay != 0) {
            return hourOfDay;
        }
        return this.mIs24Hour ? 24 : 12;
    }

    private void updateHeaderHour(int hourOfDay, boolean announce) {
        this.mHourView.setValue(getLocalizedHour(hourOfDay));
        if (announce) {
            tryAnnounceForAccessibility(this.mHourView.getText(), true);
        }
    }

    private void updateHeaderMinute(int minuteOfHour, boolean announce) {
        this.mMinuteView.setValue(minuteOfHour);
        if (announce) {
            tryAnnounceForAccessibility(this.mMinuteView.getText(), false);
        }
    }

    private void updateHeaderSeparator() {
        CharSequence separatorText = getHourMinSeparatorFromPattern(DateFormat.getBestDateTimePattern(this.mLocale, this.mIs24Hour ? "Hm" : "hm"));
        this.mSeparatorView.setText(separatorText);
        this.mTextInputPickerView.updateSeparator(separatorText);
    }

    private static String getHourMinSeparatorFromPattern(String dateTimePattern) {
        String defaultSeparator = ":";
        boolean foundHourPattern = false;
        for (int i = 0; i < dateTimePattern.length(); i++) {
            char charAt = dateTimePattern.charAt(i);
            if (charAt != ' ') {
                if (charAt != DateFormat.QUOTE) {
                    if (charAt == 'H' || charAt == 'K' || charAt == DateFormat.HOUR || charAt == DateFormat.HOUR_OF_DAY) {
                        foundHourPattern = true;
                    } else if (foundHourPattern) {
                        return Character.toString(dateTimePattern.charAt(i));
                    }
                } else if (foundHourPattern) {
                    SpannableStringBuilder quotedSubstring = new SpannableStringBuilder(dateTimePattern.substring(i));
                    return quotedSubstring.subSequence(0, DateFormat.appendQuotedText(quotedSubstring, 0)).toString();
                }
            }
        }
        return ":";
    }

    private static int lastIndexOfAny(String str, char[] any) {
        if (lengthAny > 0) {
            for (int i = str.length() - 1; i >= 0; i--) {
                char c = str.charAt(i);
                for (char c2 : any) {
                    if (c == c2) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    private void tryAnnounceForAccessibility(CharSequence text, boolean isHour) {
        if (this.mLastAnnouncedIsHour != isHour || !text.equals(this.mLastAnnouncedText)) {
            this.mDelegator.announceForAccessibility(text);
            this.mLastAnnouncedText = text;
            this.mLastAnnouncedIsHour = isHour;
        }
    }

    private void setCurrentItemShowing(int index, boolean animateCircle, boolean announce) {
        this.mRadialTimePickerView.setCurrentItemShowing(index, animateCircle);
        if (index == 0) {
            if (announce) {
                this.mDelegator.announceForAccessibility(this.mSelectHours);
            }
        } else if (announce) {
            this.mDelegator.announceForAccessibility(this.mSelectMinutes);
        }
        boolean z = false;
        this.mHourView.setActivated(index == 0);
        NumericTextView numericTextView = this.mMinuteView;
        if (index == 1) {
            z = true;
        }
        numericTextView.setActivated(z);
    }

    private void setAmOrPm(int amOrPm) {
        updateAmPmLabelStates(amOrPm);
        if (this.mRadialTimePickerView.setAmOrPm(amOrPm)) {
            this.mCurrentHour = getHour();
            updateTextInputPicker();
            if (this.mOnTimeChangedListener != null) {
                this.mOnTimeChangedListener.onTimeChanged(this.mDelegator, getHour(), getMinute());
            }
        }
    }
}
