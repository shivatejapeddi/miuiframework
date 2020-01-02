package android.os.statistics;

import android.os.AnrMonitor;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.statistics.MicroscopicEvent.MicroEventFields;
import android.util.TimedRemoteCaller;
import java.util.ArrayList;

public abstract class LooperCheckPointDetector {
    private static final int FIRST_TIME_DETECT_DELAY_TIME_MS = 5000;
    private static final int LATER_DETECT_DELAY_TIME_MS = 2000;
    private static final int MESSAGE_DETECTING_CHECKPOINT = 0;
    private static DetectingHandler mDetectingHandler;
    private static HandlerThread mDetectingThread;

    private static class DetectingHandler extends Handler {
        private final ArrayList<Thread> detectingThreads = new ArrayList();
        private final Object lockObject = new Object();

        public DetectingHandler(Looper looper) {
            super(looper);
        }

        public void beginLoopOnce(int threadId, long beginUptimeMs) {
            Thread currentThread = Thread.currentThread();
            synchronized (this.lockObject) {
                this.detectingThreads.add(currentThread);
            }
            LooperCheckPointDetector.mDetectingHandler.sendMessageDelayed(obtainDetectMessage(currentThread, threadId, beginUptimeMs), TimedRemoteCaller.DEFAULT_CALL_TIMEOUT_MILLIS);
        }

        public void endLooperOnce() {
            Thread currentThread = Thread.currentThread();
            LooperCheckPointDetector.mDetectingHandler.removeMessages(0, currentThread);
            synchronized (this.lockObject) {
                this.detectingThreads.remove(currentThread);
            }
        }

        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                boolean isDetecting;
                OsUtils.setThreadPriorityUnconditonally(Process.myTid(), -10);
                Thread targetThread = decodeDetectingThread(msg);
                LooperCheckPointDetector.mDetectingHandler.removeMessages(0, targetThread);
                synchronized (this.lockObject) {
                    isDetecting = this.detectingThreads.contains(targetThread);
                }
                if (isDetecting && targetThread.isAlive()) {
                    int tid = decodeDetectingTid(msg);
                    long beginUptimeMs = decodeDetectingBeginUptimeMs(msg);
                    long curUptimeMillis = OsUtils.getCoarseUptimeMillisFast();
                    LooperCheckPoint checkpoint = new LooperCheckPoint();
                    checkpoint.eventFlags = 5;
                    checkpoint.beginUptimeMillis = beginUptimeMs;
                    checkpoint.endUptimeMillis = curUptimeMillis;
                    checkpoint.inclusionId = PerfEvent.generateCoordinationId(PerfSuperviser.MY_PID, tid);
                    MicroEventFields details = (MicroEventFields) checkpoint.getDetailsFields();
                    details.pid = PerfSuperviser.MY_PID;
                    details.threadId = tid;
                    PerfEventReporter.report(checkpoint);
                    LooperCheckPointDetector.mDetectingHandler.sendMessageDelayed(obtainDetectMessage(targetThread, tid, curUptimeMillis), AnrMonitor.MESSAGE_EXECUTION_TIMEOUT);
                }
            }
        }

        private Message obtainDetectMessage(Thread targetThread, int tid, long beginUptimeMs) {
            Message message = Message.obtain();
            message.what = 0;
            message.obj = targetThread;
            message.arg1 = (int) (4294967295L & beginUptimeMs);
            message.arg2 = (int) (beginUptimeMs >> 32);
            message.arg2 |= tid << 8;
            return message;
        }

        private Thread decodeDetectingThread(Message msg) {
            return (Thread) msg.obj;
        }

        private int decodeDetectingTid(Message msg) {
            return msg.arg2 >> 8;
        }

        private long decodeDetectingBeginUptimeMs(Message msg) {
            return (((long) (msg.arg2 & 255)) << 32) + ((long) msg.arg1);
        }
    }

    private LooperCheckPointDetector() {
    }

    static void start() {
        mDetectingThread = new HandlerThread("Binder:looper-check-point-detect", -10);
        mDetectingThread.start();
        mDetectingHandler = new DetectingHandler(mDetectingThread.getLooper());
    }

    public static void beginLoopOnce(int threadId, long beginUptimeMs) {
        DetectingHandler detectingHandler = mDetectingHandler;
        if (detectingHandler != null) {
            detectingHandler.beginLoopOnce(threadId, beginUptimeMs);
        }
    }

    public static void endLooperOnce() {
        DetectingHandler detectingHandler = mDetectingHandler;
        if (detectingHandler != null) {
            detectingHandler.endLooperOnce();
        }
    }
}
