package android.service.notification;

import android.service.notification.ZenModeConfig.ScheduleInfo;
import android.util.ArraySet;
import android.util.Log;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

public class ScheduleCalendar {
    public static final boolean DEBUG = Log.isLoggable("ConditionProviders", 3);
    public static final String TAG = "ScheduleCalendar";
    private final Calendar mCalendar = Calendar.getInstance();
    private final ArraySet<Integer> mDays = new ArraySet();
    private ScheduleInfo mSchedule;

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ScheduleCalendar[mDays=");
        stringBuilder.append(this.mDays);
        stringBuilder.append(", mSchedule=");
        stringBuilder.append(this.mSchedule);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public boolean exitAtAlarm() {
        return this.mSchedule.exitAtAlarm;
    }

    public void setSchedule(ScheduleInfo schedule) {
        if (!Objects.equals(this.mSchedule, schedule)) {
            this.mSchedule = schedule;
            updateDays();
        }
    }

    public void maybeSetNextAlarm(long now, long nextAlarm) {
        ScheduleInfo scheduleInfo = this.mSchedule;
        if (scheduleInfo != null && scheduleInfo.exitAtAlarm) {
            if (nextAlarm == 0) {
                this.mSchedule.nextAlarm = 0;
            }
            if (nextAlarm > now) {
                if (this.mSchedule.nextAlarm == 0 || this.mSchedule.nextAlarm < now) {
                    this.mSchedule.nextAlarm = nextAlarm;
                    return;
                }
                scheduleInfo = this.mSchedule;
                scheduleInfo.nextAlarm = Math.min(scheduleInfo.nextAlarm, nextAlarm);
            } else if (this.mSchedule.nextAlarm < now) {
                if (DEBUG) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("All alarms are in the past ");
                    stringBuilder.append(this.mSchedule.nextAlarm);
                    Log.d(TAG, stringBuilder.toString());
                }
                this.mSchedule.nextAlarm = 0;
            }
        }
    }

    public void setTimeZone(TimeZone tz) {
        this.mCalendar.setTimeZone(tz);
    }

    public long getNextChangeTime(long now) {
        ScheduleInfo scheduleInfo = this.mSchedule;
        if (scheduleInfo == null) {
            return 0;
        }
        return Math.min(getNextTime(now, scheduleInfo.startHour, this.mSchedule.startMinute), getNextTime(now, this.mSchedule.endHour, this.mSchedule.endMinute));
    }

    private long getNextTime(long now, int hr, int min) {
        long time = getTime(now, hr, min);
        return time <= now ? addDays(time, 1) : time;
    }

    private long getTime(long millis, int hour, int min) {
        this.mCalendar.setTimeInMillis(millis);
        this.mCalendar.set(11, hour);
        this.mCalendar.set(12, min);
        this.mCalendar.set(13, 0);
        this.mCalendar.set(14, 0);
        return this.mCalendar.getTimeInMillis();
    }

    public boolean isInSchedule(long time) {
        long j = time;
        if (this.mSchedule == null || this.mDays.size() == 0) {
            return false;
        }
        long end;
        long start = getTime(j, this.mSchedule.startHour, this.mSchedule.startMinute);
        long end2 = getTime(j, this.mSchedule.endHour, this.mSchedule.endMinute);
        boolean z = true;
        if (end2 <= start) {
            end = addDays(end2, 1);
        } else {
            end = end2;
        }
        if (!(isInSchedule(-1, time, start, end) || isInSchedule(0, time, start, end))) {
            z = false;
        }
        return z;
    }

    public boolean isAlarmInSchedule(long alarm, long now) {
        long j = alarm;
        boolean z = false;
        if (this.mSchedule == null || this.mDays.size() == 0) {
            return false;
        }
        long end;
        long start = getTime(j, this.mSchedule.startHour, this.mSchedule.startMinute);
        long end2 = getTime(j, this.mSchedule.endHour, this.mSchedule.endMinute);
        if (end2 <= start) {
            end = addDays(end2, 1);
        } else {
            end = end2;
        }
        if ((isInSchedule(-1, alarm, start, end) && isInSchedule(-1, now, start, end)) || (isInSchedule(0, alarm, start, end) && isInSchedule(0, now, start, end))) {
            z = true;
        }
        return z;
    }

    public boolean shouldExitForAlarm(long time) {
        ScheduleInfo scheduleInfo = this.mSchedule;
        boolean z = false;
        if (scheduleInfo == null) {
            return false;
        }
        if (scheduleInfo.exitAtAlarm && this.mSchedule.nextAlarm != 0 && time >= this.mSchedule.nextAlarm && isAlarmInSchedule(this.mSchedule.nextAlarm, time)) {
            z = true;
        }
        return z;
    }

    private boolean isInSchedule(int daysOffset, long time, long start, long end) {
        int day = ((((getDayOfWeek(time) - 1) + (daysOffset % 7)) + 7) % 7) + 1;
        start = addDays(start, daysOffset);
        end = addDays(end, daysOffset);
        if (!this.mDays.contains(Integer.valueOf(day)) || time < start || time >= end) {
            return false;
        }
        return true;
    }

    private int getDayOfWeek(long time) {
        this.mCalendar.setTimeInMillis(time);
        return this.mCalendar.get(7);
    }

    private void updateDays() {
        this.mDays.clear();
        ScheduleInfo scheduleInfo = this.mSchedule;
        if (scheduleInfo != null && scheduleInfo.days != null) {
            for (int valueOf : this.mSchedule.days) {
                this.mDays.add(Integer.valueOf(valueOf));
            }
        }
    }

    private long addDays(long time, int days) {
        this.mCalendar.setTimeInMillis(time);
        this.mCalendar.add(5, days);
        return this.mCalendar.getTimeInMillis();
    }
}
