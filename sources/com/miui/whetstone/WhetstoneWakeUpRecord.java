package com.miui.whetstone;

import android.app.PendingIntent;
import android.util.TimedRemoteCaller;

public class WhetstoneWakeUpRecord {
    private static final int DELAY_STOP_RTC_APP_TIME = 5000;
    public PendingIntent intent;
    public String packageName;
    public boolean status = true;
    public int uid;
    public long updateTime;
    public int wakeup_count = 0;

    public WhetstoneWakeUpRecord(int uid, PendingIntent pendingIntent, String name) {
        this.uid = uid;
        this.intent = pendingIntent;
        this.packageName = name;
    }

    public void clearWakeupCount() {
        this.wakeup_count = 0;
    }

    public void disableWakeUpStatus() {
        this.status = false;
    }

    public void increaseWakeUpCount() {
        if (System.currentTimeMillis() - this.updateTime > TimedRemoteCaller.DEFAULT_CALL_TIMEOUT_MILLIS) {
            this.wakeup_count++;
            this.updateTime = System.currentTimeMillis();
        }
    }

    public int getWakeUpCount() {
        return this.wakeup_count;
    }

    public long getLastWakeUpTime() {
        return this.updateTime;
    }

    public boolean getRecordStatus() {
        return this.status;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("uid = ");
        stringBuilder.append(this.uid);
        stringBuilder.append("; wakeup_count = ");
        stringBuilder.append(this.wakeup_count);
        stringBuilder.append("; intent = ");
        stringBuilder.append(this.intent);
        stringBuilder.append("; status = ");
        stringBuilder.append(this.status);
        stringBuilder.append("; updateTime = ");
        stringBuilder.append(this.updateTime);
        return stringBuilder.toString();
    }

    public PendingIntent getRecordPendingIntent() {
        return this.intent;
    }
}
