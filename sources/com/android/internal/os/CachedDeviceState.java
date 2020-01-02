package com.android.internal.os;

import android.os.SystemClock;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;

public class CachedDeviceState {
    private volatile boolean mCharging;
    @GuardedBy({"mStopwatchLock"})
    private final ArrayList<TimeInStateStopwatch> mOnBatteryStopwatches;
    private volatile boolean mScreenInteractive;
    private final Object mStopwatchesLock;

    public class Readonly {
        public boolean isCharging() {
            return CachedDeviceState.this.mCharging;
        }

        public boolean isScreenInteractive() {
            return CachedDeviceState.this.mScreenInteractive;
        }

        public TimeInStateStopwatch createTimeOnBatteryStopwatch() {
            TimeInStateStopwatch stopwatch;
            synchronized (CachedDeviceState.this.mStopwatchesLock) {
                stopwatch = new TimeInStateStopwatch();
                CachedDeviceState.this.mOnBatteryStopwatches.add(stopwatch);
                if (!CachedDeviceState.this.mCharging) {
                    stopwatch.start();
                }
            }
            return stopwatch;
        }
    }

    public class TimeInStateStopwatch implements AutoCloseable {
        private final Object mLock = new Object();
        @GuardedBy({"mLock"})
        private long mStartTimeMillis;
        @GuardedBy({"mLock"})
        private long mTotalTimeMillis;

        public long getMillis() {
            long elapsedTime;
            synchronized (this.mLock) {
                elapsedTime = this.mTotalTimeMillis + elapsedTime();
            }
            return elapsedTime;
        }

        public void reset() {
            synchronized (this.mLock) {
                long j = 0;
                this.mTotalTimeMillis = 0;
                if (isRunning()) {
                    j = SystemClock.elapsedRealtime();
                }
                this.mStartTimeMillis = j;
            }
        }

        private void start() {
            synchronized (this.mLock) {
                if (!isRunning()) {
                    this.mStartTimeMillis = SystemClock.elapsedRealtime();
                }
            }
        }

        private void stop() {
            synchronized (this.mLock) {
                if (isRunning()) {
                    this.mTotalTimeMillis += elapsedTime();
                    this.mStartTimeMillis = 0;
                }
            }
        }

        private long elapsedTime() {
            return isRunning() ? SystemClock.elapsedRealtime() - this.mStartTimeMillis : 0;
        }

        @VisibleForTesting
        public boolean isRunning() {
            return this.mStartTimeMillis > 0;
        }

        public void close() {
            synchronized (CachedDeviceState.this.mStopwatchesLock) {
                CachedDeviceState.this.mOnBatteryStopwatches.remove(this);
            }
        }
    }

    public CachedDeviceState() {
        this.mStopwatchesLock = new Object();
        this.mOnBatteryStopwatches = new ArrayList();
        this.mCharging = true;
        this.mScreenInteractive = false;
    }

    @VisibleForTesting
    public CachedDeviceState(boolean isCharging, boolean isScreenInteractive) {
        this.mStopwatchesLock = new Object();
        this.mOnBatteryStopwatches = new ArrayList();
        this.mCharging = isCharging;
        this.mScreenInteractive = isScreenInteractive;
    }

    public void setScreenInteractive(boolean screenInteractive) {
        this.mScreenInteractive = screenInteractive;
    }

    public void setCharging(boolean charging) {
        if (this.mCharging != charging) {
            this.mCharging = charging;
            updateStopwatches(charging ^ 1);
        }
    }

    private void updateStopwatches(boolean shouldStart) {
        synchronized (this.mStopwatchesLock) {
            int size = this.mOnBatteryStopwatches.size();
            for (int i = 0; i < size; i++) {
                if (shouldStart) {
                    ((TimeInStateStopwatch) this.mOnBatteryStopwatches.get(i)).start();
                } else {
                    ((TimeInStateStopwatch) this.mOnBatteryStopwatches.get(i)).stop();
                }
            }
        }
    }

    public Readonly getReadonlyClient() {
        return new Readonly();
    }
}
