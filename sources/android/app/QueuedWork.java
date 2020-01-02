package android.app;

import android.annotation.UnsupportedAppUsage;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.ExponentiallyBucketedHistogram;
import java.util.LinkedList;

public class QueuedWork {
    private static final boolean DEBUG = false;
    private static final long DELAY = 100;
    private static final String LOG_TAG = QueuedWork.class.getSimpleName();
    private static final long MAX_WAIT_TIME_MILLIS = 512;
    private static final int QUEUEDWORK_WAIT_TIMEOUT = 10000;
    private static final int SLOW_LOG_TIMEOUT = 100;
    private static int mNumWaits = 0;
    @GuardedBy({"sLock"})
    private static final ExponentiallyBucketedHistogram mWaitTimes = new ExponentiallyBucketedHistogram(16);
    @GuardedBy({"sLock"})
    private static boolean sCanDelay = true;
    @GuardedBy({"sLock"})
    @UnsupportedAppUsage
    private static final LinkedList<Runnable> sFinishers = new LinkedList();
    @GuardedBy({"sLock"})
    private static Handler sHandler = null;
    private static final Object sLock = new Object();
    private static Object sProcessingWork = new Object();
    @GuardedBy({"sLock"})
    private static final LinkedList<Runnable> sWork = new LinkedList();

    private static class QueuedWorkHandler extends Handler {
        static final int MSG_RUN = 1;

        QueuedWorkHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                QueuedWork.processPendingWork(true);
            }
        }
    }

    @UnsupportedAppUsage
    private static Handler getHandler() {
        Handler handler;
        synchronized (sLock) {
            if (sHandler == null) {
                HandlerThread handlerThread = new HandlerThread("queued-work-looper", -2);
                handlerThread.start();
                sHandler = new QueuedWorkHandler(handlerThread.getLooper());
            }
            handler = sHandler;
        }
        return handler;
    }

    @UnsupportedAppUsage
    public static void addFinisher(Runnable finisher) {
        synchronized (sLock) {
            sFinishers.add(finisher);
        }
    }

    @UnsupportedAppUsage
    public static void removeFinisher(Runnable finisher) {
        synchronized (sLock) {
            sFinishers.remove(finisher);
        }
    }

    public static void waitToFinish() {
        boolean z;
        boolean interrupt = false;
        long startTime = System.currentTimeMillis();
        Handler handler = getHandler();
        synchronized (sLock) {
            z = true;
            if (handler.hasMessages(1)) {
                handler.removeMessages(1);
            }
            sCanDelay = false;
        }
        ThreadPolicy oldPolicy = StrictMode.allowThreadDiskWrites();
        try {
            Runnable finisher;
            interrupt = processPendingWork(false);
            while (!interrupt) {
                synchronized (sLock) {
                    finisher = (Runnable) sFinishers.poll();
                }
                if (finisher == null) {
                    break;
                }
                finisher.run();
            }
            try {
                synchronized (sLock) {
                    sFinishers.clear();
                }
                sCanDelay = z;
                synchronized (sLock) {
                    finisher = System.currentTimeMillis() - startTime;
                    if (finisher > DELAY) {
                        String str = LOG_TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Slow Operation: QueueWork waitToFinish took ");
                        stringBuilder.append(finisher);
                        stringBuilder.append("ms");
                        Slog.w(str, stringBuilder.toString());
                    }
                    if (finisher > 0 || false) {
                        mWaitTimes.add(Long.valueOf(finisher).intValue());
                        mNumWaits += z;
                        if (mNumWaits % 1024 == 0 || finisher > 512) {
                            mWaitTimes.log(LOG_TAG, "waited: ");
                        }
                    }
                }
            } catch (Throwable th) {
                sCanDelay = z;
            }
        } finally {
            StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    @UnsupportedAppUsage
    public static void queue(Runnable work, boolean shouldDelay) {
        Handler handler = getHandler();
        synchronized (sLock) {
            sWork.add(work);
            if (shouldDelay && sCanDelay) {
                handler.sendEmptyMessageDelayed(1, DELAY);
            } else {
                handler.sendEmptyMessage(1);
            }
        }
    }

    public static boolean hasPendingWork() {
        boolean z;
        synchronized (sLock) {
            z = !sWork.isEmpty();
        }
        return z;
    }

    /* JADX WARNING: Missing block: B:24:0x0080, code skipped:
            return false;
     */
    private static boolean processPendingWork(boolean r12) {
        /*
        r0 = 0;
        r2 = 0;
        r0 = java.lang.System.currentTimeMillis();
        r3 = sProcessingWork;
        monitor-enter(r3);
        r4 = sLock;	 Catch:{ all -> 0x0084 }
        monitor-enter(r4);	 Catch:{ all -> 0x0084 }
        r5 = sWork;	 Catch:{ all -> 0x0081 }
        r5 = r5.clone();	 Catch:{ all -> 0x0081 }
        r5 = (java.util.LinkedList) r5;	 Catch:{ all -> 0x0081 }
        r6 = sWork;	 Catch:{ all -> 0x0081 }
        r6.clear();	 Catch:{ all -> 0x0081 }
        r6 = getHandler();	 Catch:{ all -> 0x0081 }
        r7 = 1;
        r6.removeMessages(r7);	 Catch:{ all -> 0x0081 }
        monitor-exit(r4);	 Catch:{ all -> 0x0081 }
        r4 = r5.size();	 Catch:{ all -> 0x0084 }
        if (r4 <= 0) goto L_0x007e;
    L_0x0029:
        r4 = r5.iterator();	 Catch:{ all -> 0x0084 }
    L_0x002d:
        r6 = r4.hasNext();	 Catch:{ all -> 0x0084 }
        if (r6 == 0) goto L_0x007e;
    L_0x0033:
        r6 = r4.next();	 Catch:{ all -> 0x0084 }
        r6 = (java.lang.Runnable) r6;	 Catch:{ all -> 0x0084 }
        r6.run();	 Catch:{ all -> 0x0084 }
        if (r12 != 0) goto L_0x007d;
    L_0x003e:
        r2 = r2 + 1;
        r8 = java.lang.System.currentTimeMillis();	 Catch:{ all -> 0x0084 }
        r8 = r8 - r0;
        r10 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
        r10 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));
        if (r10 <= 0) goto L_0x007d;
    L_0x004b:
        r4 = LOG_TAG;	 Catch:{ all -> 0x0084 }
        r10 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0084 }
        r10.<init>();	 Catch:{ all -> 0x0084 }
        r11 = "wait to finish, totalCount= ";
        r10.append(r11);	 Catch:{ all -> 0x0084 }
        r11 = r5.size();	 Catch:{ all -> 0x0084 }
        r10.append(r11);	 Catch:{ all -> 0x0084 }
        r11 = ", workNdx=";
        r10.append(r11);	 Catch:{ all -> 0x0084 }
        r10.append(r2);	 Catch:{ all -> 0x0084 }
        r11 = ", took too long ";
        r10.append(r11);	 Catch:{ all -> 0x0084 }
        r10.append(r8);	 Catch:{ all -> 0x0084 }
        r11 = " ms";
        r10.append(r11);	 Catch:{ all -> 0x0084 }
        r10 = r10.toString();	 Catch:{ all -> 0x0084 }
        android.util.Slog.i(r4, r10);	 Catch:{ all -> 0x0084 }
        monitor-exit(r3);	 Catch:{ all -> 0x0084 }
        return r7;
    L_0x007d:
        goto L_0x002d;
    L_0x007e:
        monitor-exit(r3);	 Catch:{ all -> 0x0084 }
        r3 = 0;
        return r3;
    L_0x0081:
        r5 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0081 }
        throw r5;	 Catch:{ all -> 0x0084 }
    L_0x0084:
        r4 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0084 }
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.QueuedWork.processPendingWork(boolean):boolean");
    }
}
