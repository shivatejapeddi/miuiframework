package android.os;

public abstract class CountDownTimer {
    private static final int MSG = 1;
    private boolean mCancelled = false;
    private final long mCountdownInterval;
    private Handler mHandler = new Handler() {
        /* JADX WARNING: Missing block: B:21:0x0061, code skipped:
            return;
     */
        public void handleMessage(android.os.Message r14) {
            /*
            r13 = this;
            r0 = android.os.CountDownTimer.this;
            monitor-enter(r0);
            r1 = android.os.CountDownTimer.this;	 Catch:{ all -> 0x0062 }
            r1 = r1.mCancelled;	 Catch:{ all -> 0x0062 }
            if (r1 == 0) goto L_0x000d;
        L_0x000b:
            monitor-exit(r0);	 Catch:{ all -> 0x0062 }
            return;
        L_0x000d:
            r1 = android.os.CountDownTimer.this;	 Catch:{ all -> 0x0062 }
            r1 = r1.mStopTimeInFuture;	 Catch:{ all -> 0x0062 }
            r3 = android.os.SystemClock.elapsedRealtime();	 Catch:{ all -> 0x0062 }
            r1 = r1 - r3;
            r3 = 0;
            r5 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1));
            if (r5 > 0) goto L_0x0024;
        L_0x001e:
            r3 = android.os.CountDownTimer.this;	 Catch:{ all -> 0x0062 }
            r3.onFinish();	 Catch:{ all -> 0x0062 }
            goto L_0x0060;
        L_0x0024:
            r5 = android.os.SystemClock.elapsedRealtime();	 Catch:{ all -> 0x0062 }
            r7 = android.os.CountDownTimer.this;	 Catch:{ all -> 0x0062 }
            r7.onTick(r1);	 Catch:{ all -> 0x0062 }
            r7 = android.os.SystemClock.elapsedRealtime();	 Catch:{ all -> 0x0062 }
            r7 = r7 - r5;
            r9 = android.os.CountDownTimer.this;	 Catch:{ all -> 0x0062 }
            r9 = r9.mCountdownInterval;	 Catch:{ all -> 0x0062 }
            r9 = (r1 > r9 ? 1 : (r1 == r9 ? 0 : -1));
            if (r9 >= 0) goto L_0x0045;
        L_0x003c:
            r9 = r1 - r7;
            r3 = (r9 > r3 ? 1 : (r9 == r3 ? 0 : -1));
            if (r3 >= 0) goto L_0x0058;
        L_0x0042:
            r9 = 0;
            goto L_0x0058;
        L_0x0045:
            r9 = android.os.CountDownTimer.this;	 Catch:{ all -> 0x0062 }
            r9 = r9.mCountdownInterval;	 Catch:{ all -> 0x0062 }
            r9 = r9 - r7;
        L_0x004c:
            r11 = (r9 > r3 ? 1 : (r9 == r3 ? 0 : -1));
            if (r11 >= 0) goto L_0x0058;
        L_0x0050:
            r11 = android.os.CountDownTimer.this;	 Catch:{ all -> 0x0062 }
            r11 = r11.mCountdownInterval;	 Catch:{ all -> 0x0062 }
            r9 = r9 + r11;
            goto L_0x004c;
        L_0x0058:
            r3 = 1;
            r3 = r13.obtainMessage(r3);	 Catch:{ all -> 0x0062 }
            r13.sendMessageDelayed(r3, r9);	 Catch:{ all -> 0x0062 }
        L_0x0060:
            monitor-exit(r0);	 Catch:{ all -> 0x0062 }
            return;
        L_0x0062:
            r1 = move-exception;
            monitor-exit(r0);	 Catch:{ all -> 0x0062 }
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.os.CountDownTimer$AnonymousClass1.handleMessage(android.os.Message):void");
        }
    };
    private final long mMillisInFuture;
    private long mStopTimeInFuture;

    public abstract void onFinish();

    public abstract void onTick(long j);

    public CountDownTimer(long millisInFuture, long countDownInterval) {
        this.mMillisInFuture = millisInFuture;
        this.mCountdownInterval = countDownInterval;
    }

    public final synchronized void cancel() {
        this.mCancelled = true;
        this.mHandler.removeMessages(1);
    }

    public final synchronized CountDownTimer start() {
        this.mCancelled = false;
        if (this.mMillisInFuture <= 0) {
            onFinish();
            return this;
        }
        this.mStopTimeInFuture = SystemClock.elapsedRealtime() + this.mMillisInFuture;
        this.mHandler.sendMessage(this.mHandler.obtainMessage(1));
        return this;
    }
}
