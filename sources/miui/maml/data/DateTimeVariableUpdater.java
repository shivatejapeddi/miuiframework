package miui.maml.data;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings.System;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import miui.date.Calendar;
import miui.maml.NotifierManager;

public class DateTimeVariableUpdater extends NotifierVariableUpdater {
    private static final String LOG_TAG = "DateTimeVariableUpdater";
    private static final int TIME_DAY = 86400000;
    private static final int TIME_HOUR = 3600000;
    private static final int TIME_MINUTE = 60000;
    private static final int TIME_SECOND = 1000;
    public static final String USE_TAG = "DateTime";
    private static final int[] fields = new int[]{22, 21, 20, 18, 9};
    private static Calendar sCalendar;
    private IndexedVariable mAmPm;
    protected Calendar mCalendar;
    private long mCurrentTime;
    private IndexedVariable mDate;
    private IndexedVariable mDateLunar;
    private IndexedVariable mDayOfWeek;
    private IndexedVariable mHour12;
    private IndexedVariable mHour24;
    private long mLastUpdatedTime;
    private IndexedVariable mMinute;
    private IndexedVariable mMonth;
    private IndexedVariable mMonth1;
    private IndexedVariable mMonthLunar;
    private IndexedVariable mMonthLunarLeap;
    private IndexedVariable mNextAlarm;
    private long mNextUpdateTime;
    private IndexedVariable mSecond;
    private IndexedVariable mTime;
    private long mTimeAccuracy;
    private int mTimeAccuracyField;
    private int mTimeFormat;
    private IndexedVariable mTimeFormatVar;
    private IndexedVariable mTimeSys;
    private final Runnable mTimeUpdater;
    private IndexedVariable mYear;
    private IndexedVariable mYearLunar;
    private IndexedVariable mYearLunar1864;

    /* renamed from: miui.maml.data.DateTimeVariableUpdater$2 */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$miui$maml$data$DateTimeVariableUpdater$Accuracy = new int[Accuracy.values().length];

        static {
            try {
                $SwitchMap$miui$maml$data$DateTimeVariableUpdater$Accuracy[Accuracy.Day.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$miui$maml$data$DateTimeVariableUpdater$Accuracy[Accuracy.Hour.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$miui$maml$data$DateTimeVariableUpdater$Accuracy[Accuracy.Minute.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$miui$maml$data$DateTimeVariableUpdater$Accuracy[Accuracy.Second.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    public enum Accuracy {
        Day,
        Hour,
        Minute,
        Second
    }

    public DateTimeVariableUpdater(VariableUpdaterManager m) {
        this(m, Accuracy.Minute);
    }

    public DateTimeVariableUpdater(VariableUpdaterManager m, String accuracy) {
        super(m, NotifierManager.TYPE_TIME_CHANGED);
        this.mCalendar = new Calendar();
        this.mTimeFormat = -1;
        this.mTimeUpdater = new Runnable() {
            public void run() {
                DateTimeVariableUpdater.this.checkUpdateTime();
            }
        };
        Accuracy acc = null;
        if (!TextUtils.isEmpty(accuracy)) {
            for (Accuracy a : Accuracy.values()) {
                if (a.name().equals(accuracy)) {
                    acc = a;
                }
            }
        }
        if (acc == null) {
            acc = Accuracy.Minute;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("invalid accuracy tag:");
            stringBuilder.append(accuracy);
            Log.w(LOG_TAG, stringBuilder.toString());
        }
        initInner(acc);
    }

    public DateTimeVariableUpdater(VariableUpdaterManager m, Accuracy accuracy) {
        super(m, NotifierManager.TYPE_TIME_CHANGED);
        this.mCalendar = new Calendar();
        this.mTimeFormat = -1;
        this.mTimeUpdater = /* anonymous class already generated */;
        initInner(accuracy);
    }

    private void initInner(Accuracy accuracy) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("init with accuracy:");
        stringBuilder.append(accuracy.name());
        Log.i(LOG_TAG, stringBuilder.toString());
        int i = AnonymousClass2.$SwitchMap$miui$maml$data$DateTimeVariableUpdater$Accuracy[accuracy.ordinal()];
        if (i == 1) {
            this.mTimeAccuracy = 86400000;
            this.mTimeAccuracyField = 9;
        } else if (i == 2) {
            this.mTimeAccuracy = 3600000;
            this.mTimeAccuracyField = 18;
        } else if (i == 3) {
            this.mTimeAccuracy = 60000;
            this.mTimeAccuracyField = 20;
        } else if (i != 4) {
            this.mTimeAccuracy = 60000;
            this.mTimeAccuracyField = 20;
        } else {
            this.mTimeAccuracy = 1000;
            this.mTimeAccuracyField = 21;
        }
        Variables vars = getContext().mVariables;
        this.mYear = new IndexedVariable("year", vars, true);
        this.mMonth = new IndexedVariable(VariableNames.VAR_MONTH, vars, true);
        this.mMonth1 = new IndexedVariable("month1", vars, true);
        this.mDate = new IndexedVariable("date", vars, true);
        this.mYearLunar = new IndexedVariable(VariableNames.VAR_YEAR_LUNAR, vars, true);
        this.mYearLunar1864 = new IndexedVariable(VariableNames.VAR_YEAR_LUNAR1864, vars, true);
        this.mMonthLunar = new IndexedVariable(VariableNames.VAR_MONTH_LUNAR, vars, true);
        this.mMonthLunarLeap = new IndexedVariable(VariableNames.VAR_MONTH_LUNAR_LEAP, vars, true);
        this.mDateLunar = new IndexedVariable(VariableNames.VAR_DATE_LUNAR, vars, true);
        this.mDayOfWeek = new IndexedVariable(VariableNames.VAR_DAY_OF_WEEK, vars, true);
        this.mAmPm = new IndexedVariable(VariableNames.VAR_AMPM, vars, true);
        this.mHour12 = new IndexedVariable(VariableNames.VAR_HOUR12, vars, true);
        this.mHour24 = new IndexedVariable(VariableNames.VAR_HOUR24, vars, true);
        this.mMinute = new IndexedVariable(VariableNames.VAR_MINUTE, vars, true);
        this.mSecond = new IndexedVariable(VariableNames.VAR_SECOND, vars, true);
        this.mTime = new IndexedVariable("time", vars, true);
        this.mTimeSys = new IndexedVariable(VariableNames.VAR_TIME_SYS, vars, true);
        this.mTimeSys.set((double) System.currentTimeMillis());
        this.mNextAlarm = new IndexedVariable(VariableNames.VAR_NEXT_ALARM_TIME, vars, false);
        this.mTimeFormatVar = new IndexedVariable(VariableNames.VAR_TIME_FORMAT, vars, true);
    }

    public void init() {
        super.init();
        refreshAlarm();
        updateTime();
        checkUpdateTime();
    }

    public void tick(long currentTime) {
        super.tick(currentTime);
        this.mTime.set((double) currentTime);
        updateTime();
    }

    public void resume() {
        super.resume();
        refreshAlarm();
        resetCalendar();
        checkUpdateTime();
    }

    /* Access modifiers changed, original: protected */
    public void resetCalendar() {
        this.mCalendar = new Calendar();
        if (sCalendar != null) {
            sCalendar = new Calendar();
        }
    }

    public void pause() {
        super.pause();
        getContext().getHandler().removeCallbacks(this.mTimeUpdater);
    }

    public void finish() {
        super.finish();
        this.mLastUpdatedTime = 0;
        sCalendar = null;
        getContext().getHandler().removeCallbacks(this.mTimeUpdater);
    }

    public static String formatDate(CharSequence inFormat, long inTimeInMillis) {
        if (sCalendar == null) {
            sCalendar = new Calendar();
        }
        sCalendar.setTimeInMillis(inTimeInMillis);
        return sCalendar.format(inFormat);
    }

    private void refreshAlarm() {
        this.mNextAlarm.set(System.getString(getContext().mContext.getContentResolver(), System.NEXT_ALARM_FORMATTED));
    }

    private void checkUpdateTime() {
        getContext().getHandler().removeCallbacks(this.mTimeUpdater);
        long currentTimeMillis = System.currentTimeMillis();
        this.mCalendar.setTimeInMillis(currentTimeMillis);
        int i = 0;
        for (int f : fields) {
            if (f == this.mTimeAccuracyField) {
                break;
            }
            this.mCalendar.set(f, 0);
        }
        if (DateFormat.is24HourFormat(getContext().mContext)) {
            i = 1;
        }
        int timeFormat = i;
        long currentTime = this.mCalendar.getTimeInMillis();
        if (!(this.mCurrentTime == currentTime && this.mTimeFormat == timeFormat)) {
            this.mCurrentTime = currentTime;
            this.mNextUpdateTime = this.mCurrentTime + this.mTimeAccuracy;
            this.mTimeFormat = timeFormat;
            this.mTimeFormatVar.set((double) this.mTimeFormat);
            getRoot().requestUpdate();
        }
        getContext().getHandler().postDelayed(this.mTimeUpdater, this.mNextUpdateTime - currentTimeMillis);
    }

    private void updateTime() {
        long currentTimeMillis = System.currentTimeMillis();
        this.mTimeSys.set((double) currentTimeMillis);
        long currentTime = currentTimeMillis / 1000;
        if (currentTime != this.mLastUpdatedTime) {
            this.mCalendar.setTimeInMillis(currentTimeMillis);
            int year = this.mCalendar.get(1);
            int month = this.mCalendar.get(5);
            int date = this.mCalendar.get(9);
            this.mAmPm.set((double) this.mCalendar.get(17));
            this.mHour24.set((double) this.mCalendar.get(18));
            int hour12 = this.mCalendar.get(18) % 12;
            this.mHour12.set(hour12 == 0 ? 12.0d : (double) hour12);
            this.mMinute.set((double) this.mCalendar.get(20));
            this.mYear.set((double) year);
            this.mMonth.set((double) month);
            this.mMonth1.set((double) (month + 1));
            this.mDate.set((double) date);
            this.mDayOfWeek.set((double) this.mCalendar.get(14));
            this.mSecond.set((double) this.mCalendar.get(21));
            this.mYearLunar.set((double) this.mCalendar.get(2));
            this.mMonthLunar.set((double) this.mCalendar.get(6));
            this.mDateLunar.set((double) this.mCalendar.get(10));
            this.mYearLunar1864.set((double) this.mCalendar.get(4));
            this.mMonthLunarLeap.set((double) this.mCalendar.get(8));
            this.mLastUpdatedTime = currentTime;
        }
    }

    public void onNotify(Context context, Intent intent, Object o) {
        resetCalendar();
        checkUpdateTime();
    }
}
