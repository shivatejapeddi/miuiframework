package android.app.usage;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.ArrayMap;
import android.util.SparseIntArray;

public final class UsageStats implements Parcelable {
    public static final Creator<UsageStats> CREATOR = new Creator<UsageStats>() {
        public UsageStats createFromParcel(Parcel in) {
            UsageStats stats = new UsageStats();
            stats.mPackageName = in.readString();
            stats.mBeginTimeStamp = in.readLong();
            stats.mEndTimeStamp = in.readLong();
            stats.mLastTimeUsed = in.readLong();
            stats.mLastTimeVisible = in.readLong();
            stats.mLastTimeForegroundServiceUsed = in.readLong();
            stats.mTotalTimeInForeground = in.readLong();
            stats.mTotalTimeVisible = in.readLong();
            stats.mTotalTimeForegroundServiceUsed = in.readLong();
            stats.mLaunchCount = in.readInt();
            stats.mAppLaunchCount = in.readInt();
            stats.mLastEvent = in.readInt();
            Bundle allCounts = in.readBundle();
            if (allCounts != null) {
                stats.mChooserCounts = new ArrayMap();
                for (String action : allCounts.keySet()) {
                    if (!stats.mChooserCounts.containsKey(action)) {
                        stats.mChooserCounts.put(action, new ArrayMap());
                    }
                    Bundle currentCounts = allCounts.getBundle(action);
                    if (currentCounts != null) {
                        for (String key : currentCounts.keySet()) {
                            int value = currentCounts.getInt(key);
                            if (value > 0) {
                                ((ArrayMap) stats.mChooserCounts.get(action)).put(key, Integer.valueOf(value));
                            }
                        }
                    }
                }
            }
            readSparseIntArray(in, stats.mActivities);
            readBundleToEventMap(in.readBundle(), stats.mForegroundServices);
            return stats;
        }

        private void readSparseIntArray(Parcel in, SparseIntArray arr) {
            int size = in.readInt();
            for (int i = 0; i < size; i++) {
                arr.put(in.readInt(), in.readInt());
            }
        }

        private void readBundleToEventMap(Bundle bundle, ArrayMap<String, Integer> eventMap) {
            if (bundle != null) {
                for (String className : bundle.keySet()) {
                    eventMap.put(className, Integer.valueOf(bundle.getInt(className)));
                }
            }
        }

        public UsageStats[] newArray(int size) {
            return new UsageStats[size];
        }
    };
    public SparseIntArray mActivities = new SparseIntArray();
    public int mAppLaunchCount;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public long mBeginTimeStamp;
    public ArrayMap<String, ArrayMap<String, Integer>> mChooserCounts = new ArrayMap();
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public long mEndTimeStamp;
    public ArrayMap<String, Integer> mForegroundServices = new ArrayMap();
    @Deprecated
    @UnsupportedAppUsage
    public int mLastEvent;
    public long mLastTimeForegroundServiceUsed;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public long mLastTimeUsed;
    public long mLastTimeVisible;
    @UnsupportedAppUsage
    public int mLaunchCount;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public String mPackageName;
    public long mTotalTimeForegroundServiceUsed;
    @UnsupportedAppUsage
    public long mTotalTimeInForeground;
    public long mTotalTimeVisible;

    public UsageStats(UsageStats stats) {
        this.mPackageName = stats.mPackageName;
        this.mBeginTimeStamp = stats.mBeginTimeStamp;
        this.mEndTimeStamp = stats.mEndTimeStamp;
        this.mLastTimeUsed = stats.mLastTimeUsed;
        this.mLastTimeVisible = stats.mLastTimeVisible;
        this.mLastTimeForegroundServiceUsed = stats.mLastTimeForegroundServiceUsed;
        this.mTotalTimeInForeground = stats.mTotalTimeInForeground;
        this.mTotalTimeVisible = stats.mTotalTimeVisible;
        this.mTotalTimeForegroundServiceUsed = stats.mTotalTimeForegroundServiceUsed;
        this.mLaunchCount = stats.mLaunchCount;
        this.mAppLaunchCount = stats.mAppLaunchCount;
        this.mLastEvent = stats.mLastEvent;
        this.mActivities = stats.mActivities;
        this.mForegroundServices = stats.mForegroundServices;
        this.mChooserCounts = stats.mChooserCounts;
    }

    public UsageStats getObfuscatedForInstantApp() {
        UsageStats ret = new UsageStats(this);
        ret.mPackageName = UsageEvents.INSTANT_APP_PACKAGE_NAME;
        return ret;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public long getFirstTimeStamp() {
        return this.mBeginTimeStamp;
    }

    public long getLastTimeStamp() {
        return this.mEndTimeStamp;
    }

    public long getLastTimeUsed() {
        return this.mLastTimeUsed;
    }

    public long getLastTimeVisible() {
        return this.mLastTimeVisible;
    }

    public long getTotalTimeInForeground() {
        return this.mTotalTimeInForeground;
    }

    public long getTotalTimeVisible() {
        return this.mTotalTimeVisible;
    }

    public long getLastTimeForegroundServiceUsed() {
        return this.mLastTimeForegroundServiceUsed;
    }

    public long getTotalTimeForegroundServiceUsed() {
        return this.mTotalTimeForegroundServiceUsed;
    }

    @SystemApi
    public int getAppLaunchCount() {
        return this.mAppLaunchCount;
    }

    private void mergeEventMap(SparseIntArray left, SparseIntArray right) {
        int size = right.size();
        for (int i = 0; i < size; i++) {
            int instanceId = right.keyAt(i);
            int event = right.valueAt(i);
            int index = left.indexOfKey(instanceId);
            if (index >= 0) {
                left.put(instanceId, Math.max(left.valueAt(index), event));
            } else {
                left.put(instanceId, event);
            }
        }
    }

    private void mergeEventMap(ArrayMap<String, Integer> left, ArrayMap<String, Integer> right) {
        int size = right.size();
        for (int i = 0; i < size; i++) {
            String className = (String) right.keyAt(i);
            Integer event = (Integer) right.valueAt(i);
            if (left.containsKey(className)) {
                left.put(className, Integer.valueOf(Math.max(((Integer) left.get(className)).intValue(), event.intValue())));
            } else {
                left.put(className, event);
            }
        }
    }

    public void add(UsageStats right) {
        if (this.mPackageName.equals(right.mPackageName)) {
            if (right.mBeginTimeStamp > this.mBeginTimeStamp) {
                mergeEventMap(this.mActivities, right.mActivities);
                mergeEventMap(this.mForegroundServices, right.mForegroundServices);
                this.mLastTimeUsed = Math.max(this.mLastTimeUsed, right.mLastTimeUsed);
                this.mLastTimeVisible = Math.max(this.mLastTimeVisible, right.mLastTimeVisible);
                this.mLastTimeForegroundServiceUsed = Math.max(this.mLastTimeForegroundServiceUsed, right.mLastTimeForegroundServiceUsed);
            }
            this.mBeginTimeStamp = Math.min(this.mBeginTimeStamp, right.mBeginTimeStamp);
            this.mEndTimeStamp = Math.max(this.mEndTimeStamp, right.mEndTimeStamp);
            this.mTotalTimeInForeground += right.mTotalTimeInForeground;
            this.mTotalTimeVisible += right.mTotalTimeVisible;
            this.mTotalTimeForegroundServiceUsed += right.mTotalTimeForegroundServiceUsed;
            this.mLaunchCount += right.mLaunchCount;
            this.mAppLaunchCount += right.mAppLaunchCount;
            if (this.mChooserCounts == null) {
                this.mChooserCounts = right.mChooserCounts;
                return;
            }
            int chooserCountsSize = right.mChooserCounts;
            if (chooserCountsSize != 0) {
                chooserCountsSize = chooserCountsSize.size();
                for (int i = 0; i < chooserCountsSize; i++) {
                    String action = (String) right.mChooserCounts.keyAt(i);
                    ArrayMap<String, Integer> counts = (ArrayMap) right.mChooserCounts.valueAt(i);
                    if (!this.mChooserCounts.containsKey(action) || this.mChooserCounts.get(action) == null) {
                        this.mChooserCounts.put(action, counts);
                    } else {
                        int annotationSize = counts.size();
                        for (int j = 0; j < annotationSize; j++) {
                            String key = (String) counts.keyAt(j);
                            int rightValue = ((Integer) counts.valueAt(j)).intValue();
                            ((ArrayMap) this.mChooserCounts.get(action)).put(key, Integer.valueOf(((Integer) ((ArrayMap) this.mChooserCounts.get(action)).getOrDefault(key, Integer.valueOf(0))).intValue() + rightValue));
                        }
                    }
                }
                return;
            }
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Can't merge UsageStats for package '");
        stringBuilder.append(this.mPackageName);
        stringBuilder.append("' with UsageStats for package '");
        stringBuilder.append(right.mPackageName);
        stringBuilder.append("'.");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    private boolean hasForegroundActivity() {
        int size = this.mActivities.size();
        for (int i = 0; i < size; i++) {
            if (this.mActivities.valueAt(i) == 1) {
                return true;
            }
        }
        return false;
    }

    private boolean hasVisibleActivity() {
        int size = this.mActivities.size();
        for (int i = 0; i < size; i++) {
            int type = this.mActivities.valueAt(i);
            if (type == 1 || type == 2) {
                return true;
            }
        }
        return false;
    }

    private boolean anyForegroundServiceStarted() {
        return this.mForegroundServices.isEmpty() ^ 1;
    }

    private void incrementTimeUsed(long timeStamp) {
        long j = this.mLastTimeUsed;
        if (timeStamp > j) {
            this.mTotalTimeInForeground += timeStamp - j;
            this.mLastTimeUsed = timeStamp;
        }
    }

    private void incrementTimeVisible(long timeStamp) {
        long j = this.mLastTimeVisible;
        if (timeStamp > j) {
            this.mTotalTimeVisible += timeStamp - j;
            this.mLastTimeVisible = timeStamp;
        }
    }

    private void incrementServiceTimeUsed(long timeStamp) {
        long j = this.mLastTimeForegroundServiceUsed;
        if (timeStamp > j) {
            this.mTotalTimeForegroundServiceUsed += timeStamp - j;
            this.mLastTimeForegroundServiceUsed = timeStamp;
        }
    }

    private void updateActivity(String className, long timeStamp, int eventType, int instanceId) {
        if (eventType == 1 || eventType == 2 || eventType == 23 || eventType == 24) {
            int index = this.mActivities.indexOfKey(instanceId);
            if (index >= 0) {
                int lastEvent = this.mActivities.valueAt(index);
                if (lastEvent == 1) {
                    incrementTimeUsed(timeStamp);
                    incrementTimeVisible(timeStamp);
                } else if (lastEvent == 2) {
                    incrementTimeVisible(timeStamp);
                }
            }
            if (eventType == 1) {
                if (!hasVisibleActivity()) {
                    this.mLastTimeUsed = timeStamp;
                    this.mLastTimeVisible = timeStamp;
                } else if (!hasForegroundActivity()) {
                    this.mLastTimeUsed = timeStamp;
                }
                this.mActivities.put(instanceId, eventType);
            } else if (eventType == 2) {
                if (!hasVisibleActivity()) {
                    this.mLastTimeVisible = timeStamp;
                }
                this.mActivities.put(instanceId, eventType);
            } else if (eventType == 23 || eventType == 24) {
                this.mActivities.delete(instanceId);
            }
        }
    }

    private void updateForegroundService(String className, long timeStamp, int eventType) {
        if (eventType == 20 || eventType == 19) {
            Integer lastEvent = (Integer) this.mForegroundServices.get(className);
            if (lastEvent != null) {
                int intValue = lastEvent.intValue();
                if (intValue == 19 || intValue == 21) {
                    incrementServiceTimeUsed(timeStamp);
                }
            }
            if (eventType == 19) {
                if (!anyForegroundServiceStarted()) {
                    this.mLastTimeForegroundServiceUsed = timeStamp;
                }
                this.mForegroundServices.put(className, Integer.valueOf(eventType));
            } else if (eventType == 20) {
                this.mForegroundServices.remove(className);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x005e  */
    public void update(java.lang.String r4, long r5, int r7, int r8) {
        /*
        r3 = this;
        r0 = 1;
        if (r7 == r0) goto L_0x0056;
    L_0x0003:
        r1 = 2;
        if (r7 == r1) goto L_0x0056;
    L_0x0006:
        r1 = 3;
        if (r7 == r1) goto L_0x0043;
    L_0x0009:
        switch(r7) {
            case 19: goto L_0x003f;
            case 20: goto L_0x003f;
            case 21: goto L_0x0033;
            case 22: goto L_0x0029;
            case 23: goto L_0x0056;
            case 24: goto L_0x0056;
            case 25: goto L_0x000d;
            case 26: goto L_0x000d;
            default: goto L_0x000c;
        };
    L_0x000c:
        goto L_0x005a;
    L_0x000d:
        r1 = r3.hasForegroundActivity();
        if (r1 == 0) goto L_0x0016;
    L_0x0013:
        r3.incrementTimeUsed(r5);
    L_0x0016:
        r1 = r3.hasVisibleActivity();
        if (r1 == 0) goto L_0x001f;
    L_0x001c:
        r3.incrementTimeVisible(r5);
    L_0x001f:
        r1 = r3.anyForegroundServiceStarted();
        if (r1 == 0) goto L_0x005a;
    L_0x0025:
        r3.incrementServiceTimeUsed(r5);
        goto L_0x005a;
    L_0x0029:
        r1 = r3.anyForegroundServiceStarted();
        if (r1 == 0) goto L_0x005a;
    L_0x002f:
        r3.incrementServiceTimeUsed(r5);
        goto L_0x005a;
    L_0x0033:
        r3.mLastTimeForegroundServiceUsed = r5;
        r1 = r3.mForegroundServices;
        r2 = java.lang.Integer.valueOf(r7);
        r1.put(r4, r2);
        goto L_0x005a;
    L_0x003f:
        r3.updateForegroundService(r4, r5, r7);
        goto L_0x005a;
    L_0x0043:
        r1 = r3.hasForegroundActivity();
        if (r1 == 0) goto L_0x004c;
    L_0x0049:
        r3.incrementTimeUsed(r5);
    L_0x004c:
        r1 = r3.hasVisibleActivity();
        if (r1 == 0) goto L_0x005a;
    L_0x0052:
        r3.incrementTimeVisible(r5);
        goto L_0x005a;
    L_0x0056:
        r3.updateActivity(r4, r5, r7, r8);
    L_0x005a:
        r3.mEndTimeStamp = r5;
        if (r7 != r0) goto L_0x0063;
    L_0x005e:
        r1 = r3.mLaunchCount;
        r1 = r1 + r0;
        r3.mLaunchCount = r1;
    L_0x0063:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.usage.UsageStats.update(java.lang.String, long, int, int):void");
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mPackageName);
        dest.writeLong(this.mBeginTimeStamp);
        dest.writeLong(this.mEndTimeStamp);
        dest.writeLong(this.mLastTimeUsed);
        dest.writeLong(this.mLastTimeVisible);
        dest.writeLong(this.mLastTimeForegroundServiceUsed);
        dest.writeLong(this.mTotalTimeInForeground);
        dest.writeLong(this.mTotalTimeVisible);
        dest.writeLong(this.mTotalTimeForegroundServiceUsed);
        dest.writeInt(this.mLaunchCount);
        dest.writeInt(this.mAppLaunchCount);
        dest.writeInt(this.mLastEvent);
        Bundle allCounts = new Bundle();
        int chooserCountSize = this.mChooserCounts;
        if (chooserCountSize != 0) {
            chooserCountSize = chooserCountSize.size();
            for (int i = 0; i < chooserCountSize; i++) {
                String action = (String) this.mChooserCounts.keyAt(i);
                ArrayMap<String, Integer> counts = (ArrayMap) this.mChooserCounts.valueAt(i);
                Bundle currentCounts = new Bundle();
                int annotationSize = counts.size();
                for (int j = 0; j < annotationSize; j++) {
                    currentCounts.putInt((String) counts.keyAt(j), ((Integer) counts.valueAt(j)).intValue());
                }
                allCounts.putBundle(action, currentCounts);
            }
        }
        dest.writeBundle(allCounts);
        writeSparseIntArray(dest, this.mActivities);
        dest.writeBundle(eventMapToBundle(this.mForegroundServices));
    }

    private void writeSparseIntArray(Parcel dest, SparseIntArray arr) {
        int size = arr.size();
        dest.writeInt(size);
        for (int i = 0; i < size; i++) {
            dest.writeInt(arr.keyAt(i));
            dest.writeInt(arr.valueAt(i));
        }
    }

    private Bundle eventMapToBundle(ArrayMap<String, Integer> eventMap) {
        Bundle bundle = new Bundle();
        int size = eventMap.size();
        for (int i = 0; i < size; i++) {
            bundle.putInt((String) eventMap.keyAt(i), ((Integer) eventMap.valueAt(i)).intValue());
        }
        return bundle;
    }
}
