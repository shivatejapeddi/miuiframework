package com.android.server;

import android.annotation.UnsupportedAppUsage;
import android.os.ConditionVariable;
import android.os.SystemClock;

abstract class ResettableTimeout {
    @UnsupportedAppUsage
    private ConditionVariable mLock = new ConditionVariable();
    @UnsupportedAppUsage
    private volatile long mOffAt;
    private volatile boolean mOffCalled;
    private Thread mThread;

    private class T extends Thread {
        private T() {
        }

        /* JADX WARNING: Missing block: B:10:?, code skipped:
            sleep(r0);
     */
        public void run() {
            /*
            r4 = this;
            r0 = com.android.server.ResettableTimeout.this;
            r0 = r0.mLock;
            r0.open();
        L_0x0009:
            monitor-enter(r4);
            r0 = com.android.server.ResettableTimeout.this;	 Catch:{ all -> 0x0035 }
            r0 = r0.mOffAt;	 Catch:{ all -> 0x0035 }
            r2 = android.os.SystemClock.uptimeMillis();	 Catch:{ all -> 0x0035 }
            r0 = r0 - r2;
            r2 = 0;
            r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
            if (r2 > 0) goto L_0x002e;
        L_0x001b:
            r2 = com.android.server.ResettableTimeout.this;	 Catch:{ all -> 0x0035 }
            r3 = 1;
            r2.mOffCalled = r3;	 Catch:{ all -> 0x0035 }
            r2 = com.android.server.ResettableTimeout.this;	 Catch:{ all -> 0x0035 }
            r2.off();	 Catch:{ all -> 0x0035 }
            r2 = com.android.server.ResettableTimeout.this;	 Catch:{ all -> 0x0035 }
            r3 = 0;
            r2.mThread = r3;	 Catch:{ all -> 0x0035 }
            monitor-exit(r4);	 Catch:{ all -> 0x0035 }
            return;
        L_0x002e:
            monitor-exit(r4);	 Catch:{ all -> 0x0035 }
            sleep(r0);	 Catch:{ InterruptedException -> 0x0033 }
            goto L_0x0034;
        L_0x0033:
            r2 = move-exception;
        L_0x0034:
            goto L_0x0009;
        L_0x0035:
            r0 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x0035 }
            throw r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.ResettableTimeout$T.run():void");
        }
    }

    public abstract void off();

    public abstract void on(boolean z);

    ResettableTimeout() {
    }

    public void go(long milliseconds) {
        synchronized (this) {
            boolean alreadyOn;
            this.mOffAt = SystemClock.uptimeMillis() + milliseconds;
            if (this.mThread == null) {
                alreadyOn = false;
                this.mLock.close();
                this.mThread = new T();
                this.mThread.start();
                this.mLock.block();
                this.mOffCalled = false;
            } else {
                alreadyOn = true;
                this.mThread.interrupt();
            }
            on(alreadyOn);
        }
    }

    public void cancel() {
        synchronized (this) {
            this.mOffAt = 0;
            if (this.mThread != null) {
                this.mThread.interrupt();
                this.mThread = null;
            }
            if (!this.mOffCalled) {
                this.mOffCalled = true;
                off();
            }
        }
    }
}
