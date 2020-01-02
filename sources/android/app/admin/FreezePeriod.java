package android.app.admin;

import android.app.admin.SystemUpdatePolicy.ValidationFailedException;
import android.util.Log;
import android.util.Pair;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FreezePeriod {
    static final int DAYS_IN_YEAR = 365;
    private static final int DUMMY_YEAR = 2001;
    private static final String TAG = "FreezePeriod";
    private final MonthDay mEnd;
    private final int mEndDay;
    private final MonthDay mStart;
    private final int mStartDay;

    public FreezePeriod(MonthDay start, MonthDay end) {
        this.mStart = start;
        this.mStartDay = this.mStart.atYear(2001).getDayOfYear();
        this.mEnd = end;
        this.mEndDay = this.mEnd.atYear(2001).getDayOfYear();
    }

    public MonthDay getStart() {
        return this.mStart;
    }

    public MonthDay getEnd() {
        return this.mEnd;
    }

    private FreezePeriod(int startDay, int endDay) {
        this.mStartDay = startDay;
        this.mStart = dayOfYearToMonthDay(startDay);
        this.mEndDay = endDay;
        this.mEnd = dayOfYearToMonthDay(endDay);
    }

    /* Access modifiers changed, original: 0000 */
    public int getLength() {
        return (getEffectiveEndDay() - this.mStartDay) + 1;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isWrapped() {
        return this.mEndDay < this.mStartDay;
    }

    /* Access modifiers changed, original: 0000 */
    public int getEffectiveEndDay() {
        if (isWrapped()) {
            return this.mEndDay + 365;
        }
        return this.mEndDay;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean contains(LocalDate localDate) {
        int daysOfYear = dayOfYearDisregardLeapYear(localDate);
        boolean z = false;
        if (isWrapped()) {
            if (this.mStartDay <= daysOfYear || daysOfYear <= this.mEndDay) {
                z = true;
            }
            return z;
        }
        if (this.mStartDay <= daysOfYear && daysOfYear <= this.mEndDay) {
            z = true;
        }
        return z;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean after(LocalDate localDate) {
        return this.mStartDay > dayOfYearDisregardLeapYear(localDate);
    }

    /* Access modifiers changed, original: 0000 */
    public Pair<LocalDate, LocalDate> toCurrentOrFutureRealDates(LocalDate now) {
        int startYearAdjustment;
        int endYearAdjustment;
        int nowDays = dayOfYearDisregardLeapYear(now);
        if (contains(now)) {
            if (this.mStartDay <= nowDays) {
                startYearAdjustment = 0;
                endYearAdjustment = isWrapped();
            } else {
                startYearAdjustment = -1;
                endYearAdjustment = 0;
            }
        } else if (this.mStartDay > nowDays) {
            startYearAdjustment = 0;
            endYearAdjustment = isWrapped();
        } else {
            startYearAdjustment = 1;
            endYearAdjustment = 1;
        }
        return new Pair(LocalDate.ofYearDay(2001, this.mStartDay).withYear(now.getYear() + startYearAdjustment), LocalDate.ofYearDay(2001, this.mEndDay).withYear(now.getYear() + endYearAdjustment));
    }

    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(LocalDate.ofYearDay(2001, this.mStartDay).format(formatter));
        stringBuilder.append(" - ");
        stringBuilder.append(LocalDate.ofYearDay(2001, this.mEndDay).format(formatter));
        return stringBuilder.toString();
    }

    private static MonthDay dayOfYearToMonthDay(int dayOfYear) {
        LocalDate date = LocalDate.ofYearDay(2001, dayOfYear);
        return MonthDay.of(date.getMonth(), date.getDayOfMonth());
    }

    private static int dayOfYearDisregardLeapYear(LocalDate date) {
        return date.withYear(2001).getDayOfYear();
    }

    public static int distanceWithoutLeapYear(LocalDate first, LocalDate second) {
        return (dayOfYearDisregardLeapYear(first) - dayOfYearDisregardLeapYear(second)) + ((first.getYear() - second.getYear()) * 365);
    }

    static List<FreezePeriod> canonicalizePeriods(List<FreezePeriod> intervals) {
        int i;
        boolean[] taken = new boolean[365];
        for (FreezePeriod interval : intervals) {
            for (i = interval.mStartDay; i <= interval.getEffectiveEndDay(); i++) {
                taken[(i - 1) % 365] = true;
            }
        }
        List<FreezePeriod> result = new ArrayList();
        int i2 = 0;
        while (i2 < 365) {
            if (taken[i2]) {
                i = i2 + 1;
                while (i2 < 365 && taken[i2]) {
                    i2++;
                }
                result.add(new FreezePeriod(i, i2));
            } else {
                i2++;
            }
        }
        i = result.size() - 1;
        if (i > 0 && ((FreezePeriod) result.get(i)).mEndDay == 365 && ((FreezePeriod) result.get(0)).mStartDay == 1) {
            result.set(i, new FreezePeriod(((FreezePeriod) result.get(i)).mStartDay, ((FreezePeriod) result.get(0)).mEndDay));
            result.remove(0);
        }
        return result;
    }

    static void validatePeriods(List<FreezePeriod> periods) {
        List<FreezePeriod> allPeriods = canonicalizePeriods(periods);
        if (allPeriods.size() == periods.size()) {
            int i = 0;
            while (i < allPeriods.size()) {
                FreezePeriod current = (FreezePeriod) allPeriods.get(i);
                if (current.getLength() <= 90) {
                    FreezePeriod previous;
                    if (i > 0) {
                        previous = (FreezePeriod) allPeriods.get(i - 1);
                    } else {
                        previous = (FreezePeriod) allPeriods.get(allPeriods.size() - 1);
                    }
                    if (previous != current) {
                        int separation;
                        if (i != 0 || previous.isWrapped()) {
                            separation = (current.mStartDay - previous.mEndDay) - 1;
                        } else {
                            separation = (current.mStartDay + (365 - previous.mEndDay)) - 1;
                        }
                        if (separation < 60) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Freeze periods ");
                            stringBuilder.append(previous);
                            stringBuilder.append(" and ");
                            stringBuilder.append(current);
                            stringBuilder.append(" are too close together: ");
                            stringBuilder.append(separation);
                            stringBuilder.append(" days apart");
                            throw ValidationFailedException.freezePeriodTooClose(stringBuilder.toString());
                        }
                    }
                    i++;
                } else {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Freeze period ");
                    stringBuilder2.append(current);
                    stringBuilder2.append(" is too long: ");
                    stringBuilder2.append(current.getLength());
                    stringBuilder2.append(" days");
                    throw ValidationFailedException.freezePeriodTooLong(stringBuilder2.toString());
                }
            }
            return;
        }
        throw ValidationFailedException.duplicateOrOverlapPeriods();
    }

    static void validateAgainstPreviousFreezePeriod(List<FreezePeriod> periods, LocalDate prevPeriodStart, LocalDate prevPeriodEnd, LocalDate now) {
        if (periods.size() != 0 && prevPeriodStart != null && prevPeriodEnd != null) {
            String str = ",";
            if (prevPeriodStart.isAfter(now) || prevPeriodEnd.isAfter(now)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Previous period (");
                stringBuilder.append(prevPeriodStart);
                stringBuilder.append(str);
                stringBuilder.append(prevPeriodEnd);
                stringBuilder.append(") is after current date ");
                stringBuilder.append(now);
                Log.w(TAG, stringBuilder.toString());
            }
            List<FreezePeriod> allPeriods = canonicalizePeriods(periods);
            FreezePeriod curOrNextFreezePeriod = (FreezePeriod) allPeriods.get(0);
            for (FreezePeriod interval : allPeriods) {
                if (interval.contains(now) || interval.mStartDay > dayOfYearDisregardLeapYear(now)) {
                    curOrNextFreezePeriod = interval;
                    break;
                }
            }
            Pair<LocalDate, LocalDate> curOrNextFreezeDates = curOrNextFreezePeriod.toCurrentOrFutureRealDates(now);
            if (now.isAfter((ChronoLocalDate) curOrNextFreezeDates.first)) {
                curOrNextFreezeDates = new Pair(now, (LocalDate) curOrNextFreezeDates.second);
            }
            StringBuilder stringBuilder2;
            if (((LocalDate) curOrNextFreezeDates.first).isAfter((ChronoLocalDate) curOrNextFreezeDates.second)) {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Current freeze dates inverted: ");
                stringBuilder2.append(curOrNextFreezeDates.first);
                stringBuilder2.append("-");
                stringBuilder2.append(curOrNextFreezeDates.second);
                throw new IllegalStateException(stringBuilder2.toString());
            }
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Prev: ");
            stringBuilder2.append(prevPeriodStart);
            stringBuilder2.append(str);
            stringBuilder2.append(prevPeriodEnd);
            stringBuilder2.append("; cur: ");
            stringBuilder2.append(curOrNextFreezeDates.first);
            stringBuilder2.append(str);
            stringBuilder2.append(curOrNextFreezeDates.second);
            str = stringBuilder2.toString();
            long separation = (long) (distanceWithoutLeapYear((LocalDate) curOrNextFreezeDates.first, prevPeriodEnd) - 1);
            String str2 = ", ";
            StringBuilder stringBuilder3;
            if (separation <= 0) {
                long length = (long) (distanceWithoutLeapYear((LocalDate) curOrNextFreezeDates.second, prevPeriodStart) + 1);
                if (length > 90) {
                    stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("Combined freeze period exceeds maximum days: ");
                    stringBuilder3.append(length);
                    stringBuilder3.append(str2);
                    stringBuilder3.append(str);
                    throw ValidationFailedException.combinedPeriodTooLong(stringBuilder3.toString());
                }
            } else if (separation < 60) {
                stringBuilder3 = new StringBuilder();
                stringBuilder3.append("Previous freeze period too close to new period: ");
                stringBuilder3.append(separation);
                stringBuilder3.append(str2);
                stringBuilder3.append(str);
                throw ValidationFailedException.combinedPeriodTooClose(stringBuilder3.toString());
            }
        }
    }
}
