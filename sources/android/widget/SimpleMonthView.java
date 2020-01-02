package android.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.icu.text.DisplayContext;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.IntArray;
import android.util.MathUtils;
import android.util.StateSet;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import com.android.internal.R;
import com.android.internal.widget.ExploreByTouchHelper;
import java.text.NumberFormat;
import java.util.Locale;
import libcore.icu.LocaleData;

class SimpleMonthView extends View {
    private static final int DAYS_IN_WEEK = 7;
    private static final int DEFAULT_SELECTED_DAY = -1;
    private static final int DEFAULT_WEEK_START = 1;
    private static final int MAX_WEEKS_IN_MONTH = 6;
    private static final String MONTH_YEAR_FORMAT = "MMMMy";
    private static final int SELECTED_HIGHLIGHT_ALPHA = 176;
    private int mActivatedDay;
    private final Calendar mCalendar;
    private int mCellWidth;
    private final NumberFormat mDayFormatter;
    private int mDayHeight;
    private final Paint mDayHighlightPaint;
    private final Paint mDayHighlightSelectorPaint;
    private int mDayOfWeekHeight;
    private final String[] mDayOfWeekLabels;
    private final TextPaint mDayOfWeekPaint;
    private int mDayOfWeekStart;
    private final TextPaint mDayPaint;
    private final Paint mDaySelectorPaint;
    private int mDaySelectorRadius;
    private ColorStateList mDayTextColor;
    private int mDaysInMonth;
    private final int mDesiredCellWidth;
    private final int mDesiredDayHeight;
    private final int mDesiredDayOfWeekHeight;
    private final int mDesiredDaySelectorRadius;
    private final int mDesiredMonthHeight;
    private int mEnabledDayEnd;
    private int mEnabledDayStart;
    private int mHighlightedDay;
    private boolean mIsTouchHighlighted;
    private final Locale mLocale;
    private int mMonth;
    private int mMonthHeight;
    private final TextPaint mMonthPaint;
    private String mMonthYearLabel;
    private OnDayClickListener mOnDayClickListener;
    private int mPaddedHeight;
    private int mPaddedWidth;
    private int mPreviouslyHighlightedDay;
    private int mToday;
    private final MonthViewTouchHelper mTouchHelper;
    private int mWeekStart;
    private int mYear;

    public interface OnDayClickListener {
        void onDayClick(SimpleMonthView simpleMonthView, Calendar calendar);
    }

    private class MonthViewTouchHelper extends ExploreByTouchHelper {
        private static final String DATE_FORMAT = "dd MMMM yyyy";
        private final Calendar mTempCalendar = Calendar.getInstance();
        private final Rect mTempRect = new Rect();

        public MonthViewTouchHelper(View host) {
            super(host);
        }

        /* Access modifiers changed, original: protected */
        public int getVirtualViewAt(float x, float y) {
            int day = SimpleMonthView.this.getDayAtLocation((int) (x + 0.5f), (int) (0.5f + y));
            if (day != -1) {
                return day;
            }
            return Integer.MIN_VALUE;
        }

        /* Access modifiers changed, original: protected */
        public void getVisibleVirtualViews(IntArray virtualViewIds) {
            for (int day = 1; day <= SimpleMonthView.this.mDaysInMonth; day++) {
                virtualViewIds.add(day);
            }
        }

        /* Access modifiers changed, original: protected */
        public void onPopulateEventForVirtualView(int virtualViewId, AccessibilityEvent event) {
            event.setContentDescription(getDayDescription(virtualViewId));
        }

        /* Access modifiers changed, original: protected */
        public void onPopulateNodeForVirtualView(int virtualViewId, AccessibilityNodeInfo node) {
            if (SimpleMonthView.this.getBoundsForDay(virtualViewId, this.mTempRect)) {
                node.setText(getDayText(virtualViewId));
                node.setContentDescription(getDayDescription(virtualViewId));
                node.setBoundsInParent(this.mTempRect);
                boolean isDayEnabled = SimpleMonthView.this.isDayEnabled(virtualViewId);
                if (isDayEnabled) {
                    node.addAction(AccessibilityAction.ACTION_CLICK);
                }
                node.setEnabled(isDayEnabled);
                if (virtualViewId == SimpleMonthView.this.mActivatedDay) {
                    node.setChecked(true);
                }
                return;
            }
            this.mTempRect.setEmpty();
            node.setContentDescription("");
            node.setBoundsInParent(this.mTempRect);
            node.setVisibleToUser(false);
        }

        /* Access modifiers changed, original: protected */
        public boolean onPerformActionForVirtualView(int virtualViewId, int action, Bundle arguments) {
            if (action != 16) {
                return false;
            }
            return SimpleMonthView.this.onDayClicked(virtualViewId);
        }

        private CharSequence getDayDescription(int id) {
            if (!SimpleMonthView.this.isValidDayOfMonth(id)) {
                return "";
            }
            this.mTempCalendar.set(SimpleMonthView.this.mYear, SimpleMonthView.this.mMonth, id);
            return DateFormat.format(DATE_FORMAT, this.mTempCalendar.getTimeInMillis());
        }

        private CharSequence getDayText(int id) {
            if (SimpleMonthView.this.isValidDayOfMonth(id)) {
                return SimpleMonthView.this.mDayFormatter.format((long) id);
            }
            return null;
        }
    }

    public SimpleMonthView(Context context) {
        this(context, null);
    }

    public SimpleMonthView(Context context, AttributeSet attrs) {
        this(context, attrs, 16843612);
    }

    public SimpleMonthView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SimpleMonthView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mMonthPaint = new TextPaint();
        this.mDayOfWeekPaint = new TextPaint();
        this.mDayPaint = new TextPaint();
        this.mDaySelectorPaint = new Paint();
        this.mDayHighlightPaint = new Paint();
        this.mDayHighlightSelectorPaint = new Paint();
        this.mDayOfWeekLabels = new String[7];
        this.mActivatedDay = -1;
        this.mToday = -1;
        this.mWeekStart = 1;
        this.mEnabledDayStart = 1;
        this.mEnabledDayEnd = 31;
        this.mHighlightedDay = -1;
        this.mPreviouslyHighlightedDay = -1;
        this.mIsTouchHighlighted = false;
        Resources res = context.getResources();
        this.mDesiredMonthHeight = res.getDimensionPixelSize(R.dimen.date_picker_month_height);
        this.mDesiredDayOfWeekHeight = res.getDimensionPixelSize(R.dimen.date_picker_day_of_week_height);
        this.mDesiredDayHeight = res.getDimensionPixelSize(R.dimen.date_picker_day_height);
        this.mDesiredCellWidth = res.getDimensionPixelSize(R.dimen.date_picker_day_width);
        this.mDesiredDaySelectorRadius = res.getDimensionPixelSize(R.dimen.date_picker_day_selector_radius);
        this.mTouchHelper = new MonthViewTouchHelper(this);
        setAccessibilityDelegate(this.mTouchHelper);
        setImportantForAccessibility(1);
        this.mLocale = res.getConfiguration().locale;
        this.mCalendar = Calendar.getInstance(this.mLocale);
        this.mDayFormatter = NumberFormat.getIntegerInstance(this.mLocale);
        updateMonthYearLabel();
        updateDayOfWeekLabels();
        initPaints(res);
    }

    private void updateMonthYearLabel() {
        SimpleDateFormat formatter = new SimpleDateFormat(DateFormat.getBestDateTimePattern(this.mLocale, MONTH_YEAR_FORMAT), this.mLocale);
        formatter.setContext(DisplayContext.CAPITALIZATION_FOR_STANDALONE);
        this.mMonthYearLabel = formatter.format(this.mCalendar.getTime());
    }

    private void updateDayOfWeekLabels() {
        String[] tinyWeekdayNames = LocaleData.get(this.mLocale).tinyWeekdayNames;
        for (int i = 0; i < 7; i++) {
            this.mDayOfWeekLabels[i] = tinyWeekdayNames[(((this.mWeekStart + i) - 1) % 7) + 1];
        }
    }

    private ColorStateList applyTextAppearance(Paint p, int resId) {
        TypedArray ta = this.mContext.obtainStyledAttributes(null, R.styleable.TextAppearance, 0, resId);
        String fontFamily = ta.getString(12);
        if (fontFamily != null) {
            p.setTypeface(Typeface.create(fontFamily, 0));
        }
        p.setTextSize((float) ta.getDimensionPixelSize(0, (int) p.getTextSize()));
        ColorStateList textColor = ta.getColorStateList(3);
        if (textColor != null) {
            p.setColor(textColor.getColorForState(ENABLED_STATE_SET, 0));
        }
        ta.recycle();
        return textColor;
    }

    public int getMonthHeight() {
        return this.mMonthHeight;
    }

    public int getCellWidth() {
        return this.mCellWidth;
    }

    public void setMonthTextAppearance(int resId) {
        applyTextAppearance(this.mMonthPaint, resId);
        invalidate();
    }

    public void setDayOfWeekTextAppearance(int resId) {
        applyTextAppearance(this.mDayOfWeekPaint, resId);
        invalidate();
    }

    public void setDayTextAppearance(int resId) {
        ColorStateList textColor = applyTextAppearance(this.mDayPaint, resId);
        if (textColor != null) {
            this.mDayTextColor = textColor;
        }
        invalidate();
    }

    private void initPaints(Resources res) {
        String monthTypeface = res.getString(R.string.date_picker_month_typeface);
        String dayOfWeekTypeface = res.getString(R.string.date_picker_day_of_week_typeface);
        String dayTypeface = res.getString(R.string.date_picker_day_typeface);
        int monthTextSize = res.getDimensionPixelSize(R.dimen.date_picker_month_text_size);
        int dayOfWeekTextSize = res.getDimensionPixelSize(R.dimen.date_picker_day_of_week_text_size);
        int dayTextSize = res.getDimensionPixelSize(R.dimen.date_picker_day_text_size);
        this.mMonthPaint.setAntiAlias(true);
        this.mMonthPaint.setTextSize((float) monthTextSize);
        this.mMonthPaint.setTypeface(Typeface.create(monthTypeface, 0));
        this.mMonthPaint.setTextAlign(Align.CENTER);
        this.mMonthPaint.setStyle(Style.FILL);
        this.mDayOfWeekPaint.setAntiAlias(true);
        this.mDayOfWeekPaint.setTextSize((float) dayOfWeekTextSize);
        this.mDayOfWeekPaint.setTypeface(Typeface.create(dayOfWeekTypeface, 0));
        this.mDayOfWeekPaint.setTextAlign(Align.CENTER);
        this.mDayOfWeekPaint.setStyle(Style.FILL);
        this.mDaySelectorPaint.setAntiAlias(true);
        this.mDaySelectorPaint.setStyle(Style.FILL);
        this.mDayHighlightPaint.setAntiAlias(true);
        this.mDayHighlightPaint.setStyle(Style.FILL);
        this.mDayHighlightSelectorPaint.setAntiAlias(true);
        this.mDayHighlightSelectorPaint.setStyle(Style.FILL);
        this.mDayPaint.setAntiAlias(true);
        this.mDayPaint.setTextSize((float) dayTextSize);
        this.mDayPaint.setTypeface(Typeface.create(dayTypeface, 0));
        this.mDayPaint.setTextAlign(Align.CENTER);
        this.mDayPaint.setStyle(Style.FILL);
    }

    /* Access modifiers changed, original: 0000 */
    public void setMonthTextColor(ColorStateList monthTextColor) {
        this.mMonthPaint.setColor(monthTextColor.getColorForState(ENABLED_STATE_SET, 0));
        invalidate();
    }

    /* Access modifiers changed, original: 0000 */
    public void setDayOfWeekTextColor(ColorStateList dayOfWeekTextColor) {
        this.mDayOfWeekPaint.setColor(dayOfWeekTextColor.getColorForState(ENABLED_STATE_SET, 0));
        invalidate();
    }

    /* Access modifiers changed, original: 0000 */
    public void setDayTextColor(ColorStateList dayTextColor) {
        this.mDayTextColor = dayTextColor;
        invalidate();
    }

    /* Access modifiers changed, original: 0000 */
    public void setDaySelectorColor(ColorStateList dayBackgroundColor) {
        int activatedColor = dayBackgroundColor.getColorForState(StateSet.get(40), 0);
        this.mDaySelectorPaint.setColor(activatedColor);
        this.mDayHighlightSelectorPaint.setColor(activatedColor);
        this.mDayHighlightSelectorPaint.setAlpha(176);
        invalidate();
    }

    /* Access modifiers changed, original: 0000 */
    public void setDayHighlightColor(ColorStateList dayHighlightColor) {
        this.mDayHighlightPaint.setColor(dayHighlightColor.getColorForState(StateSet.get(24), 0));
        invalidate();
    }

    public void setOnDayClickListener(OnDayClickListener listener) {
        this.mOnDayClickListener = listener;
    }

    public boolean dispatchHoverEvent(MotionEvent event) {
        return this.mTouchHelper.dispatchHoverEvent(event) || super.dispatchHoverEvent(event);
    }

    /* JADX WARNING: Missing block: B:6:0x001c, code skipped:
            if (r2 != 3) goto L_0x0045;
     */
    public boolean onTouchEvent(android.view.MotionEvent r8) {
        /*
        r7 = this;
        r0 = r8.getX();
        r1 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r0 = r0 + r1;
        r0 = (int) r0;
        r2 = r8.getY();
        r2 = r2 + r1;
        r1 = (int) r2;
        r2 = r8.getAction();
        r3 = 0;
        r4 = 1;
        if (r2 == 0) goto L_0x002f;
    L_0x0016:
        if (r2 == r4) goto L_0x001f;
    L_0x0018:
        r5 = 2;
        if (r2 == r5) goto L_0x002f;
    L_0x001b:
        r5 = 3;
        if (r2 == r5) goto L_0x0026;
    L_0x001e:
        goto L_0x0045;
    L_0x001f:
        r5 = r7.getDayAtLocation(r0, r1);
        r7.onDayClicked(r5);
    L_0x0026:
        r5 = -1;
        r7.mHighlightedDay = r5;
        r7.mIsTouchHighlighted = r3;
        r7.invalidate();
        goto L_0x0045;
    L_0x002f:
        r5 = r7.getDayAtLocation(r0, r1);
        r7.mIsTouchHighlighted = r4;
        r6 = r7.mHighlightedDay;
        if (r6 == r5) goto L_0x0040;
    L_0x0039:
        r7.mHighlightedDay = r5;
        r7.mPreviouslyHighlightedDay = r5;
        r7.invalidate();
    L_0x0040:
        if (r2 != 0) goto L_0x0045;
    L_0x0042:
        if (r5 >= 0) goto L_0x0045;
    L_0x0044:
        return r3;
    L_0x0045:
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.SimpleMonthView.onTouchEvent(android.view.MotionEvent):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:40:0x008b  */
    public boolean onKeyDown(int r7, android.view.KeyEvent r8) {
        /*
        r6 = this;
        r0 = 0;
        r1 = r8.getKeyCode();
        r2 = 61;
        r3 = 1;
        if (r1 == r2) goto L_0x0064;
    L_0x000a:
        r2 = 66;
        if (r1 == r2) goto L_0x005b;
    L_0x000e:
        r2 = 7;
        switch(r1) {
            case 19: goto L_0x0049;
            case 20: goto L_0x0034;
            case 21: goto L_0x0025;
            case 22: goto L_0x0014;
            case 23: goto L_0x005b;
            default: goto L_0x0012;
        };
    L_0x0012:
        goto L_0x008f;
    L_0x0014:
        r1 = r8.hasNoModifiers();
        if (r1 == 0) goto L_0x008f;
    L_0x001a:
        r1 = r6.isLayoutRtl();
        r1 = r1 ^ r3;
        r0 = r6.moveOneDay(r1);
        goto L_0x008f;
    L_0x0025:
        r1 = r8.hasNoModifiers();
        if (r1 == 0) goto L_0x008f;
    L_0x002b:
        r1 = r6.isLayoutRtl();
        r0 = r6.moveOneDay(r1);
        goto L_0x008f;
    L_0x0034:
        r1 = r8.hasNoModifiers();
        if (r1 == 0) goto L_0x008f;
    L_0x003a:
        r6.ensureFocusedDay();
        r1 = r6.mHighlightedDay;
        r4 = r6.mDaysInMonth;
        r4 = r4 - r2;
        if (r1 > r4) goto L_0x008f;
    L_0x0044:
        r1 = r1 + r2;
        r6.mHighlightedDay = r1;
        r0 = 1;
        goto L_0x008f;
    L_0x0049:
        r1 = r8.hasNoModifiers();
        if (r1 == 0) goto L_0x008f;
    L_0x004f:
        r6.ensureFocusedDay();
        r1 = r6.mHighlightedDay;
        if (r1 <= r2) goto L_0x008f;
    L_0x0056:
        r1 = r1 - r2;
        r6.mHighlightedDay = r1;
        r0 = 1;
        goto L_0x008f;
    L_0x005b:
        r1 = r6.mHighlightedDay;
        r2 = -1;
        if (r1 == r2) goto L_0x008f;
    L_0x0060:
        r6.onDayClicked(r1);
        return r3;
    L_0x0064:
        r1 = 0;
        r2 = r8.hasNoModifiers();
        if (r2 == 0) goto L_0x006d;
    L_0x006b:
        r1 = 2;
        goto L_0x0074;
    L_0x006d:
        r2 = r8.hasModifiers(r3);
        if (r2 == 0) goto L_0x0074;
    L_0x0073:
        r1 = 1;
    L_0x0074:
        if (r1 == 0) goto L_0x008f;
    L_0x0076:
        r2 = r6.getParent();
        r4 = r6;
    L_0x007b:
        r4 = r4.focusSearch(r1);
        if (r4 == 0) goto L_0x0089;
    L_0x0081:
        if (r4 == r6) goto L_0x0089;
    L_0x0083:
        r5 = r4.getParent();
        if (r5 == r2) goto L_0x007b;
    L_0x0089:
        if (r4 == 0) goto L_0x008f;
    L_0x008b:
        r4.requestFocus();
        return r3;
    L_0x008f:
        if (r0 == 0) goto L_0x0095;
    L_0x0091:
        r6.invalidate();
        return r3;
    L_0x0095:
        r1 = super.onKeyDown(r7, r8);
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.SimpleMonthView.onKeyDown(int, android.view.KeyEvent):boolean");
    }

    private boolean moveOneDay(boolean positive) {
        ensureFocusedDay();
        int i;
        if (positive) {
            if (isLastDayOfWeek(this.mHighlightedDay)) {
                return false;
            }
            i = this.mHighlightedDay;
            if (i >= this.mDaysInMonth) {
                return false;
            }
            this.mHighlightedDay = i + 1;
            return true;
        } else if (isFirstDayOfWeek(this.mHighlightedDay)) {
            return false;
        } else {
            i = this.mHighlightedDay;
            if (i <= 1) {
                return false;
            }
            this.mHighlightedDay = i - 1;
            return true;
        }
    }

    /* Access modifiers changed, original: protected */
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        if (gainFocus) {
            int offset = findDayOffset();
            int i = 1;
            int col;
            int i2;
            if (direction == 17) {
                this.mHighlightedDay = Math.min(this.mDaysInMonth, ((findClosestRow(previouslyFocusedRect) + 1) * 7) - offset);
            } else if (direction == 33) {
                col = findClosestColumn(previouslyFocusedRect);
                i2 = this.mDaysInMonth;
                int day = ((col - offset) + (((offset + i2) / 7) * 7)) + 1;
                this.mHighlightedDay = day > i2 ? day - 7 : day;
            } else if (direction == 66) {
                col = findClosestRow(previouslyFocusedRect);
                if (col != 0) {
                    i = 1 + ((col * 7) - offset);
                }
                this.mHighlightedDay = i;
            } else if (direction == 130) {
                i2 = (findClosestColumn(previouslyFocusedRect) - offset) + 1;
                this.mHighlightedDay = i2 < 1 ? i2 + 7 : i2;
            }
            ensureFocusedDay();
            invalidate();
        }
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    private int findClosestRow(Rect previouslyFocusedRect) {
        if (previouslyFocusedRect == null) {
            return 3;
        }
        if (this.mDayHeight == 0) {
            return 0;
        }
        int centerY = previouslyFocusedRect.centerY();
        TextPaint p = this.mDayPaint;
        int headerHeight = this.mMonthHeight + this.mDayOfWeekHeight;
        int rowHeight = this.mDayHeight;
        int maxDay = findDayOffset() + this.mDaysInMonth;
        return MathUtils.constrain(Math.round(((float) ((int) (((float) centerY) - (((float) ((rowHeight / 2) + headerHeight)) - ((p.ascent() + p.descent()) / 2.0f))))) / ((float) rowHeight)), 0, (maxDay / 7) - (maxDay % 7 == 0 ? 1 : 0));
    }

    private int findClosestColumn(Rect previouslyFocusedRect) {
        if (previouslyFocusedRect == null) {
            return 3;
        }
        if (this.mCellWidth == 0) {
            return 0;
        }
        int columnFromLeft = MathUtils.constrain((previouslyFocusedRect.centerX() - this.mPaddingLeft) / this.mCellWidth, 0, 6);
        return isLayoutRtl() ? (7 - columnFromLeft) - 1 : columnFromLeft;
    }

    public void getFocusedRect(Rect r) {
        int i = this.mHighlightedDay;
        if (i > 0) {
            getBoundsForDay(i, r);
        } else {
            super.getFocusedRect(r);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onFocusLost() {
        if (!this.mIsTouchHighlighted) {
            this.mPreviouslyHighlightedDay = this.mHighlightedDay;
            this.mHighlightedDay = -1;
            invalidate();
        }
        super.onFocusLost();
    }

    private void ensureFocusedDay() {
        if (this.mHighlightedDay == -1) {
            int i = this.mPreviouslyHighlightedDay;
            if (i != -1) {
                this.mHighlightedDay = i;
                return;
            }
            i = this.mActivatedDay;
            if (i != -1) {
                this.mHighlightedDay = i;
            } else {
                this.mHighlightedDay = 1;
            }
        }
    }

    private boolean isFirstDayOfWeek(int day) {
        return ((findDayOffset() + day) - 1) % 7 == 0;
    }

    private boolean isLastDayOfWeek(int day) {
        return (findDayOffset() + day) % 7 == 0;
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        canvas.translate((float) paddingLeft, (float) paddingTop);
        drawMonth(canvas);
        drawDaysOfWeek(canvas);
        drawDays(canvas);
        canvas.translate((float) (-paddingLeft), (float) (-paddingTop));
    }

    private void drawMonth(Canvas canvas) {
        canvas.drawText(this.mMonthYearLabel, ((float) this.mPaddedWidth) / 2.0f, (((float) this.mMonthHeight) - (this.mMonthPaint.ascent() + this.mMonthPaint.descent())) / 2.0f, this.mMonthPaint);
    }

    public String getMonthYearLabel() {
        return this.mMonthYearLabel;
    }

    private void drawDaysOfWeek(Canvas canvas) {
        TextPaint p = this.mDayOfWeekPaint;
        int headerHeight = this.mMonthHeight;
        int rowHeight = this.mDayOfWeekHeight;
        int colWidth = this.mCellWidth;
        float halfLineHeight = (p.ascent() + p.descent()) / 2.0f;
        int rowCenter = (rowHeight / 2) + headerHeight;
        for (int col = 0; col < 7; col++) {
            int colCenterRtl;
            int colCenter = (colWidth * col) + (colWidth / 2);
            if (isLayoutRtl()) {
                colCenterRtl = this.mPaddedWidth - colCenter;
            } else {
                colCenterRtl = colCenter;
            }
            canvas.drawText(this.mDayOfWeekLabels[col], (float) colCenterRtl, ((float) rowCenter) - halfLineHeight, p);
        }
    }

    private void drawDays(Canvas canvas) {
        Canvas canvas2 = canvas;
        TextPaint p = this.mDayPaint;
        int headerHeight = this.mMonthHeight + this.mDayOfWeekHeight;
        int rowHeight = this.mDayHeight;
        int colWidth = this.mCellWidth;
        float halfLineHeight = (p.ascent() + p.descent()) / 2.0f;
        int rowCenter = (rowHeight / 2) + headerHeight;
        int day = 1;
        int col = findDayOffset();
        while (day <= this.mDaysInMonth) {
            int colCenterRtl;
            int headerHeight2;
            int colWidth2;
            int stateMask;
            int colCenter = (colWidth * col) + (colWidth / 2);
            if (isLayoutRtl()) {
                colCenterRtl = this.mPaddedWidth - colCenter;
            } else {
                colCenterRtl = colCenter;
            }
            int stateMask2 = 0;
            boolean isDayEnabled = isDayEnabled(day);
            if (isDayEnabled) {
                stateMask2 = 0 | 8;
            }
            boolean z = true;
            boolean isDayActivated = this.mActivatedDay == day;
            boolean isDayHighlighted = this.mHighlightedDay == day;
            if (isDayActivated) {
                stateMask2 |= 32;
                if (isDayHighlighted) {
                    headerHeight2 = headerHeight;
                    headerHeight = this.mDayHighlightSelectorPaint;
                } else {
                    headerHeight2 = headerHeight;
                    headerHeight = this.mDaySelectorPaint;
                }
                colWidth2 = colWidth;
                stateMask = stateMask2;
                canvas2.drawCircle((float) colCenterRtl, (float) rowCenter, (float) this.mDaySelectorRadius, headerHeight);
            } else {
                headerHeight2 = headerHeight;
                colWidth2 = colWidth;
                int i = colCenter;
                if (isDayHighlighted) {
                    stateMask2 |= 16;
                    if (isDayEnabled) {
                        stateMask = stateMask2;
                        canvas2.drawCircle((float) colCenterRtl, (float) rowCenter, (float) this.mDaySelectorRadius, this.mDayHighlightPaint);
                    } else {
                        stateMask = stateMask2;
                    }
                } else {
                    stateMask = stateMask2;
                }
            }
            if (this.mToday != day) {
                z = false;
            }
            if (!z || isDayActivated) {
                colWidth = this.mDayTextColor.getColorForState(StateSet.get(stateMask), 0);
            } else {
                colWidth = this.mDaySelectorPaint.getColor();
            }
            p.setColor(colWidth);
            canvas2.drawText(this.mDayFormatter.format((long) day), (float) colCenterRtl, ((float) rowCenter) - halfLineHeight, p);
            col++;
            if (col == 7) {
                col = 0;
                rowCenter += rowHeight;
            }
            day++;
            headerHeight = headerHeight2;
            colWidth = colWidth2;
        }
    }

    private boolean isDayEnabled(int day) {
        return day >= this.mEnabledDayStart && day <= this.mEnabledDayEnd;
    }

    private boolean isValidDayOfMonth(int day) {
        return day >= 1 && day <= this.mDaysInMonth;
    }

    private static boolean isValidDayOfWeek(int day) {
        return day >= 1 && day <= 7;
    }

    private static boolean isValidMonth(int month) {
        return month >= 0 && month <= 11;
    }

    public void setSelectedDay(int dayOfMonth) {
        this.mActivatedDay = dayOfMonth;
        this.mTouchHelper.invalidateRoot();
        invalidate();
    }

    public void setFirstDayOfWeek(int weekStart) {
        if (isValidDayOfWeek(weekStart)) {
            this.mWeekStart = weekStart;
        } else {
            this.mWeekStart = this.mCalendar.getFirstDayOfWeek();
        }
        updateDayOfWeekLabels();
        this.mTouchHelper.invalidateRoot();
        invalidate();
    }

    /* Access modifiers changed, original: 0000 */
    public void setMonthParams(int selectedDay, int month, int year, int weekStart, int enabledDayStart, int enabledDayEnd) {
        this.mActivatedDay = selectedDay;
        if (isValidMonth(month)) {
            this.mMonth = month;
        }
        this.mYear = year;
        this.mCalendar.set(2, this.mMonth);
        this.mCalendar.set(1, this.mYear);
        this.mCalendar.set(5, 1);
        this.mDayOfWeekStart = this.mCalendar.get(7);
        if (isValidDayOfWeek(weekStart)) {
            this.mWeekStart = weekStart;
        } else {
            this.mWeekStart = this.mCalendar.getFirstDayOfWeek();
        }
        Calendar today = Calendar.getInstance();
        this.mToday = -1;
        this.mDaysInMonth = getDaysInMonth(this.mMonth, this.mYear);
        int i = 0;
        while (true) {
            int i2 = this.mDaysInMonth;
            if (i < i2) {
                i2 = i + 1;
                if (sameDay(i2, today)) {
                    this.mToday = i2;
                }
                i++;
            } else {
                this.mEnabledDayStart = MathUtils.constrain(enabledDayStart, 1, i2);
                this.mEnabledDayEnd = MathUtils.constrain(enabledDayEnd, this.mEnabledDayStart, this.mDaysInMonth);
                updateMonthYearLabel();
                updateDayOfWeekLabels();
                this.mTouchHelper.invalidateRoot();
                invalidate();
                return;
            }
        }
    }

    private static int getDaysInMonth(int month, int year) {
        switch (month) {
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                return 31;
            case 1:
                return year % 4 == 0 ? 29 : 28;
            case 3:
            case 5:
            case 8:
            case 10:
                return 30;
            default:
                throw new IllegalArgumentException("Invalid Month");
        }
    }

    private boolean sameDay(int day, Calendar today) {
        if (this.mYear == today.get(1) && this.mMonth == today.get(2) && day == today.get(5)) {
            return true;
        }
        return false;
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(View.resolveSize(((this.mDesiredCellWidth * 7) + getPaddingStart()) + getPaddingEnd(), widthMeasureSpec), View.resolveSize(((((this.mDesiredDayHeight * 6) + this.mDesiredDayOfWeekHeight) + this.mDesiredMonthHeight) + getPaddingTop()) + getPaddingBottom(), heightMeasureSpec));
    }

    public void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
        requestLayout();
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            int w = right - left;
            int h = bottom - top;
            int paddingLeft = getPaddingLeft();
            int paddingTop = getPaddingTop();
            int paddingRight = getPaddingRight();
            int paddingBottom = getPaddingBottom();
            int paddedWidth = (w - paddingRight) - paddingLeft;
            int paddedHeight = (h - paddingBottom) - paddingTop;
            int i;
            int i2;
            if (paddedWidth == this.mPaddedWidth) {
                i = h;
                i2 = paddingLeft;
            } else if (paddedHeight == this.mPaddedHeight) {
                int i3 = w;
                i = h;
                i2 = paddingLeft;
            } else {
                this.mPaddedWidth = paddedWidth;
                this.mPaddedHeight = paddedHeight;
                float scaleH = ((float) paddedHeight) / ((float) ((getMeasuredHeight() - paddingTop) - paddingBottom));
                int cellWidth = this.mPaddedWidth / 7;
                this.mMonthHeight = (int) (((float) this.mDesiredMonthHeight) * scaleH);
                this.mDayOfWeekHeight = (int) (((float) this.mDesiredDayOfWeekHeight) * scaleH);
                this.mDayHeight = (int) (((float) this.mDesiredDayHeight) * scaleH);
                this.mCellWidth = cellWidth;
                this.mDaySelectorRadius = Math.min(this.mDesiredDaySelectorRadius, Math.min((cellWidth / 2) + Math.min(paddingLeft, paddingRight), (this.mDayHeight / 2) + paddingBottom));
                this.mTouchHelper.invalidateRoot();
            }
        }
    }

    private int findDayOffset() {
        int i = this.mDayOfWeekStart;
        int i2 = this.mWeekStart;
        int offset = i - i2;
        if (i < i2) {
            return offset + 7;
        }
        return offset;
    }

    private int getDayAtLocation(int x, int y) {
        int paddedX = x - getPaddingLeft();
        if (paddedX < 0 || paddedX >= this.mPaddedWidth) {
            return -1;
        }
        int headerHeight = this.mMonthHeight + this.mDayOfWeekHeight;
        int paddedY = y - getPaddingTop();
        if (paddedY < headerHeight || paddedY >= this.mPaddedHeight) {
            return -1;
        }
        int paddedXRtl;
        if (isLayoutRtl()) {
            paddedXRtl = this.mPaddedWidth - paddedX;
        } else {
            paddedXRtl = paddedX;
        }
        int col = (paddedXRtl * 7) / this.mPaddedWidth;
        int day = (((((paddedY - headerHeight) / this.mDayHeight) * 7) + col) + 1) - findDayOffset();
        if (isValidDayOfMonth(day)) {
            return day;
        }
        return -1;
    }

    public boolean getBoundsForDay(int id, Rect outBounds) {
        if (!isValidDayOfMonth(id)) {
            return false;
        }
        int left;
        int index = (id - 1) + findDayOffset();
        int col = index % 7;
        int colWidth = this.mCellWidth;
        if (isLayoutRtl()) {
            left = (getWidth() - getPaddingRight()) - ((col + 1) * colWidth);
        } else {
            left = getPaddingLeft() + (col * colWidth);
        }
        int row = index / 7;
        int rowHeight = this.mDayHeight;
        int top = (getPaddingTop() + (this.mMonthHeight + this.mDayOfWeekHeight)) + (row * rowHeight);
        outBounds.set(left, top, left + colWidth, top + rowHeight);
        return true;
    }

    private boolean onDayClicked(int day) {
        if (!isValidDayOfMonth(day) || !isDayEnabled(day)) {
            return false;
        }
        if (this.mOnDayClickListener != null) {
            Calendar date = Calendar.getInstance();
            date.set(this.mYear, this.mMonth, day);
            this.mOnDayClickListener.onDayClick(this, date);
        }
        this.mTouchHelper.sendEventForVirtualView(day, 1);
        return true;
    }

    public PointerIcon onResolvePointerIcon(MotionEvent event, int pointerIndex) {
        if (!isEnabled()) {
            return null;
        }
        if (getDayAtLocation((int) (event.getX() + 1056964608), (int) (event.getY() + 0.5f)) >= 0) {
            return PointerIcon.getSystemIcon(getContext(), 1002);
        }
        return super.onResolvePointerIcon(event, pointerIndex);
    }
}
