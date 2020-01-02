package android.os;

import android.app.Activity;
import android.app.ActivityThread;
import android.app.AlarmManager;
import android.app.servertransaction.ClientTransaction;
import android.content.ComponentName;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import android.util.Slog;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class MessageMonitor {
    private static final boolean DEBUG = false;
    private static final int DEFAULT_MESSAGE_HISTORY_DUMP_DURATION = 1800000;
    private static final int DELAY_BLKIO_INDEX = 0;
    private static final int DELAY_FREEPAGES_INDEX = 2;
    private static final int DELAY_NUMS = 5;
    private static final int DELAY_RUNNABLE_TIME_INDEX = 4;
    private static final int DELAY_RUNNING_TIME_INDEX = 3;
    private static final int DELAY_SWAPIN_INDEX = 1;
    private static final int MSG_QUEUE_SIZE = 512;
    private static final int NORMAL_LOG_CHAR_LENGTH = 256;
    private static final int SMALL_LOG_CHAR_LENGTH = 128;
    private static final String TAG = "MessageMonitor";
    private static final String TAG_LOOPER = "Looper";
    private static final int THREASHOLD_LATE_ACTIVITY_MS = 300;
    private static final int THREASHOLD_LATE_FRAME_MS = 300;
    private static final int THREASHOLD_MSG_PROCESS_MS = 50;
    private static final int THRESHOLD_SLOW_INPUT_MS = 300;
    private static final ArrayList<WeakReference<MessageMonitor>> sCallbacks = new ArrayList();
    private static final Object sCallbacksLock = new Object();
    private boolean mEnableMonitorMessage;
    private MessageMonitorInfo[] mLongMsgHistoryQueue = new MessageMonitorInfo[512];
    private int mLongMsgIndexNext = 0;
    private MessageMonitorInfo[] mMsgHistoryQueue = new MessageMonitorInfo[512];
    private int mMsgIndexNext = 0;
    private final Object mMsgLock = new Object();
    private long mNextSeq = 1;
    private Message mRunningMessage;
    private MessageMonitorInfo mRunningMessageInfo;
    private String mThreadName;
    private int mTid;

    static class MessageMonitorInfo implements Parcelable, Cloneable {
        public static final Creator<MessageMonitorInfo> CREATOR = new Creator<MessageMonitorInfo>() {
            public MessageMonitorInfo createFromParcel(Parcel source) {
                return new MessageMonitorInfo(source);
            }

            public MessageMonitorInfo[] newArray(int size) {
                return new MessageMonitorInfo[size];
            }
        };
        String callbackName;
        long dispatchBlkioDelay;
        long dispatchCurrentTime;
        long dispatchReclaimDelay;
        long dispatchRunnableTime;
        long dispatchRunningTime;
        long dispatchSwapinDelay;
        long dispatchTime;
        long finishBlkioDelay;
        long finishReclaimDelay;
        long finishRunnableTime;
        long finishRunningTime;
        long finishSwapinDelay;
        long finishTime;
        protected long mSeq;
        int msgWhat;
        long planCurrentTime;
        long planTime;
        String targetName;

        MessageMonitorInfo() {
        }

        MessageMonitorInfo(Parcel source) {
            this.planTime = source.readLong();
            this.planCurrentTime = source.readLong();
            this.dispatchTime = source.readLong();
            this.finishTime = source.readLong();
        }

        /* Access modifiers changed, original: 0000 */
        public void init(Message msg, long when) {
            if (when == 0) {
                this.planTime = SystemClock.uptimeMillis();
                this.planCurrentTime = System.currentTimeMillis();
                return;
            }
            this.planTime = when;
            this.planCurrentTime = System.currentTimeMillis() + (when - SystemClock.uptimeMillis());
        }

        /* Access modifiers changed, original: 0000 */
        public void reset() {
            this.planTime = 0;
            this.planCurrentTime = 0;
            this.dispatchTime = 0;
            this.finishTime = 0;
            this.targetName = null;
            this.callbackName = null;
            this.msgWhat = 0;
        }

        /* Access modifiers changed, original: 0000 */
        public void markDispatch(Message msg) {
            String str = "";
            this.targetName = msg.target == null ? str : msg.target.getClass().getName();
            if (msg.callback != null) {
                str = msg.callback.getClass().getName();
            }
            this.callbackName = str;
            this.msgWhat = msg.what;
            this.dispatchTime = SystemClock.uptimeMillis();
            this.dispatchCurrentTime = System.currentTimeMillis();
            long[] delays = new long[5];
            MessageMonitor.nGetThreadDelay(delays);
            this.dispatchBlkioDelay = delays[0];
            this.dispatchSwapinDelay = delays[1];
            this.dispatchReclaimDelay = delays[2];
            this.dispatchRunningTime = delays[3];
            this.dispatchRunnableTime = delays[4];
        }

        /* Access modifiers changed, original: 0000 */
        public void markFinish(Message msg) {
            this.finishTime = SystemClock.uptimeMillis();
            long[] delays = new long[5];
            MessageMonitor.nGetThreadDelay(delays);
            this.finishBlkioDelay = delays[0];
            this.finishSwapinDelay = delays[1];
            this.finishReclaimDelay = delays[2];
            this.finishRunningTime = delays[3];
            this.finishRunnableTime = delays[4];
        }

        /* Access modifiers changed, original: 0000 */
        public boolean isFinished() {
            return this.finishTime != 0;
        }

        /* Access modifiers changed, original: 0000 */
        public long getLatencyMillis() {
            return this.dispatchTime - this.planTime;
        }

        /* Access modifiers changed, original: 0000 */
        public long getWallMillis() {
            if (isFinished()) {
                return this.finishTime - this.dispatchTime;
            }
            return 0;
        }

        /* Access modifiers changed, original: 0000 */
        public long getTotalMillis() {
            if (isFinished()) {
                return this.finishTime - this.planTime;
            }
            return 0;
        }

        /* Access modifiers changed, original: 0000 */
        public long getBlkioDelay() {
            return this.finishBlkioDelay - this.dispatchBlkioDelay;
        }

        /* Access modifiers changed, original: 0000 */
        public long getSwapinDelay() {
            return this.finishSwapinDelay - this.dispatchSwapinDelay;
        }

        /* Access modifiers changed, original: 0000 */
        public long getReclaimDelay() {
            return this.finishReclaimDelay - this.dispatchReclaimDelay;
        }

        /* Access modifiers changed, original: 0000 */
        public long getRunningTime() {
            return this.finishRunningTime - this.dispatchRunningTime;
        }

        /* Access modifiers changed, original: 0000 */
        public long getRunnableTime() {
            return this.finishRunnableTime - this.dispatchRunnableTime;
        }

        public String toString() {
            String str = "ms";
            String result = "";
            StringBuilder sb;
            try {
                sb = new StringBuilder(128);
                if (!TextUtils.isEmpty(this.targetName)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(ActivityThread.PROC_START_SEQ_IDENT);
                    stringBuilder.append(this.mSeq);
                    sb.append(stringBuilder.toString());
                    if (this.planCurrentTime != 0) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(" plan=");
                        stringBuilder.append(MessageMonitor.formatCurrentTime(this.planCurrentTime));
                        sb.append(stringBuilder.toString());
                    }
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(" late=");
                    stringBuilder.append(getLatencyMillis());
                    stringBuilder.append(str);
                    sb.append(stringBuilder.toString());
                    if (isFinished()) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(" wall=");
                        stringBuilder.append(getWallMillis());
                        stringBuilder.append(str);
                        sb.append(stringBuilder.toString());
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(" running=");
                        stringBuilder.append(getRunningTime());
                        stringBuilder.append(str);
                        sb.append(stringBuilder.toString());
                        if (getRunnableTime() > 0) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append(" runnable=");
                            stringBuilder.append(getRunnableTime());
                            stringBuilder.append(str);
                            sb.append(stringBuilder.toString());
                        }
                        if (getBlkioDelay() > 0) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append(" io=");
                            stringBuilder.append(getBlkioDelay());
                            stringBuilder.append(str);
                            sb.append(stringBuilder.toString());
                        }
                        if (getSwapinDelay() > 0) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append(" swapin=");
                            stringBuilder.append(getSwapinDelay());
                            stringBuilder.append(str);
                            sb.append(stringBuilder.toString());
                        }
                        if (getReclaimDelay() > 0) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append(" reclaim=");
                            stringBuilder.append(getReclaimDelay());
                            stringBuilder.append(str);
                            sb.append(stringBuilder.toString());
                        }
                    }
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(" h=");
                    stringBuilder2.append(this.targetName);
                    sb.append(stringBuilder2.toString());
                    if (TextUtils.isEmpty(this.callbackName)) {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(" w=");
                        stringBuilder2.append(this.msgWhat);
                        sb.append(stringBuilder2.toString());
                    } else {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(" c=");
                        stringBuilder2.append(this.callbackName);
                        sb.append(stringBuilder2.toString());
                    }
                }
                return sb.toString();
            } catch (Exception e) {
                sb = new StringBuilder();
                sb.append("getMessageString failed ! ");
                sb.append(e.getMessage());
                Log.e(MessageMonitor.TAG, sb.toString());
                return result;
            }
        }

        /* Access modifiers changed, original: 0000 */
        public String getShortTime() {
            long sleepTime = (getWallMillis() - getRunningTime()) - getRunnableTime();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getRunningTime());
            String str = "|";
            stringBuilder.append(str);
            stringBuilder.append(getRunnableTime());
            stringBuilder.append(str);
            long j = 0;
            if (sleepTime > 0) {
                j = sleepTime;
            }
            stringBuilder.append(j);
            return stringBuilder.toString();
        }

        /* Access modifiers changed, original: 0000 */
        public String toShortString() {
            StringBuilder sb = new StringBuilder(128);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(ActivityThread.PROC_START_SEQ_IDENT);
            stringBuilder.append(this.mSeq);
            stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            sb.append(stringBuilder.toString());
            String str = "ms ";
            if (getRunningTime() > 0) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("running=");
                stringBuilder.append(getRunningTime());
                stringBuilder.append(str);
                sb.append(stringBuilder.toString());
            }
            if (getRunnableTime() > 0) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("runnable=");
                stringBuilder.append(getRunnableTime());
                stringBuilder.append(str);
                sb.append(stringBuilder.toString());
            }
            if (getBlkioDelay() > 0) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("io=");
                stringBuilder.append(getBlkioDelay());
                stringBuilder.append(str);
                sb.append(stringBuilder.toString());
            }
            if (getSwapinDelay() > 0) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("swapin=");
                stringBuilder.append(getSwapinDelay());
                stringBuilder.append(str);
                sb.append(stringBuilder.toString());
            }
            if (getReclaimDelay() > 0) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("reclaim=");
                stringBuilder.append(getReclaimDelay());
                stringBuilder.append(str);
                sb.append(stringBuilder.toString());
            }
            if (getLatencyMillis() > 0) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("late=");
                stringBuilder.append(getLatencyMillis());
                stringBuilder.append(str);
                sb.append(stringBuilder.toString());
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("h=");
            stringBuilder.append(this.targetName);
            sb.append(stringBuilder.toString());
            if (TextUtils.isEmpty(this.callbackName)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(" w=");
                stringBuilder.append(this.msgWhat);
                sb.append(stringBuilder.toString());
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append(" c=");
                stringBuilder.append(this.callbackName);
                sb.append(stringBuilder.toString());
            }
            return sb.toString();
        }

        /* Access modifiers changed, original: 0000 */
        public long getTookTime() {
            return 0;
        }

        /* Access modifiers changed, original: 0000 */
        public long getTookTimeAfterDispatch() {
            return 0;
        }

        /* Access modifiers changed, original: 0000 */
        public String getMessageSummary(Message msg) {
            return "";
        }

        /* Access modifiers changed, original: 0000 */
        public String createMonitorDigest() {
            return "";
        }

        /* Access modifiers changed, original: 0000 */
        public String getMonitorMessage() {
            return "";
        }

        /* Access modifiers changed, original: 0000 */
        public String getMonitorDigest() {
            return "";
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.planCurrentTime);
            dest.writeLong(this.planTime);
            dest.writeLong(this.dispatchTime);
            dest.writeLong(this.finishTime);
        }
    }

    public static native long nGetThreadCpuTime();

    public static native void nGetThreadDelay(long[] jArr);

    public void setThreadNameAndTid(String threadName, int tid) {
        this.mThreadName = threadName;
        this.mTid = tid;
    }

    private String getThreadInfo() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Looper (");
        stringBuilder.append(this.mThreadName);
        stringBuilder.append(", tid=");
        stringBuilder.append(this.mTid);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public void enableMonitorMessage(boolean enable) {
        this.mEnableMonitorMessage = enable;
        updateCallbackIfNeed(enable);
    }

    private void updateCallbackIfNeed(boolean enableMonitor) {
        synchronized (sCallbacksLock) {
            int findIndex = -1;
            for (int i = 0; i < sCallbacks.size(); i++) {
                if (((WeakReference) sCallbacks.get(i)).get() == this) {
                    findIndex = i;
                    break;
                }
            }
            if (enableMonitor && findIndex == -1) {
                sCallbacks.add(new WeakReference(this));
            } else if (!(enableMonitor || findIndex == -1)) {
                sCallbacks.remove(findIndex);
            }
        }
    }

    public void markDispatch(Message msg, MessageMonitorInfo info) {
        if (this.mEnableMonitorMessage) {
            this.mRunningMessage = msg;
            this.mRunningMessageInfo = info;
            long j = this.mNextSeq;
            this.mNextSeq = 1 + j;
            info.mSeq = j;
            info.markDispatch(msg);
        }
    }

    public void markFinish(Message msg, MessageMonitorInfo info) {
        if (this.mEnableMonitorMessage) {
            this.mRunningMessage = null;
            this.mRunningMessageInfo = null;
            info.markFinish(msg);
            checkMsg(msg, info);
            addMessageToHistoryIfNeed(info);
        }
    }

    private void checkMsg(Message msg, MessageMonitorInfo info) {
        if (info.getWallMillis() > 1000) {
            String logMsg = new StringBuilder();
            logMsg.append("Slow Looper ");
            logMsg.append(Thread.currentThread().getName());
            logMsg.append(": Long Msg: ");
            logMsg.append(info.toString());
            Slog.w(TAG_LOOPER, logMsg.toString());
        }
        checkEssentialMsg(msg, info);
    }

    private void checkEssentialMsg(Message msg, MessageMonitorInfo info) {
        if (isActivityTrancaction(msg)) {
            checkActivityLifecycle(msg, info);
        } else if (isDoFrame(msg)) {
            checkDoFrame(msg, info);
        }
    }

    private static boolean isDoFrame(Message msg) {
        boolean z = false;
        if (msg == null || msg.target == null || !msg.target.getClass().getName().equals("android.view.Choreographer$FrameHandler")) {
            return false;
        }
        if (msg.what == 0) {
            return true;
        }
        if (msg.callback != null && msg.callback.getClass().getName().equals("android.view.Choreographer$FrameDisplayEventReceiver")) {
            z = true;
        }
        return z;
    }

    private void checkDoFrame(Message msg, MessageMonitorInfo info) {
        if (info.getLatencyMillis() > 300) {
            StringBuilder sb = new StringBuilder(256);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Slow Looper ");
            stringBuilder.append(Thread.currentThread().getName());
            stringBuilder.append(": doFrame");
            sb.append(stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append(" is ");
            stringBuilder.append(info.getLatencyMillis());
            stringBuilder.append("ms late");
            sb.append(stringBuilder.toString());
            List<MessageMonitorInfo> infos = getHistoryMsgInfos(info.planTime);
            if (infos.size() > 0) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(" because of ");
                stringBuilder2.append(infos.size());
                stringBuilder2.append(" msg,");
                sb.append(stringBuilder2.toString());
                for (int i = 0; i < infos.size(); i++) {
                    MessageMonitorInfo thisInfo = (MessageMonitorInfo) infos.get(i);
                    if (thisInfo.getWallMillis() > 50) {
                        StringBuilder stringBuilder3 = new StringBuilder();
                        stringBuilder3.append(" msg ");
                        stringBuilder3.append(i + 1);
                        stringBuilder3.append(" took ");
                        stringBuilder3.append(thisInfo.getWallMillis());
                        stringBuilder3.append("ms (");
                        sb.append(stringBuilder3.toString());
                        stringBuilder3 = new StringBuilder();
                        stringBuilder3.append(thisInfo.toShortString());
                        stringBuilder3.append("),");
                        sb.append(stringBuilder3.toString());
                    }
                }
                sb.deleteCharAt(sb.length() - 1);
            }
            Slog.w(TAG_LOOPER, sb.toString());
        }
    }

    private static boolean isActivityTrancaction(Message msg) {
        boolean z = false;
        if (msg == null || msg.target == null || !msg.target.getClass().getName().equals("android.app.ActivityThread$H")) {
            return false;
        }
        if (msg.what == 159) {
            z = true;
        }
        return z;
    }

    private void checkActivityLifecycle(Message msg, MessageMonitorInfo info) {
        if (info.getLatencyMillis() > 300) {
            ClientTransaction transaction = msg.obj;
            IBinder token = transaction.getActivityToken();
            ActivityThread thread = ActivityThread.currentActivityThread();
            if (thread.getActivityClient(token) != null) {
                Activity activity = thread.getActivity(token);
                if (activity != null) {
                    ComponentName componentName = activity.getComponentName();
                    StringBuilder sb = new StringBuilder(256);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Slow Looper ");
                    stringBuilder.append(Thread.currentThread().getName());
                    stringBuilder.append(": Activity ");
                    stringBuilder.append(componentName.getPackageName());
                    stringBuilder.append("/");
                    stringBuilder.append(componentName.getShortClassName());
                    stringBuilder.append(" is ");
                    stringBuilder.append(info.getLatencyMillis());
                    stringBuilder.append("ms late (wall=");
                    stringBuilder.append(info.getWallMillis());
                    String str = "ms";
                    stringBuilder.append(str);
                    sb.append(stringBuilder.toString());
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(" running=");
                    stringBuilder.append(info.getRunningTime());
                    stringBuilder.append(str);
                    sb.append(stringBuilder.toString());
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                    stringBuilder.append(transaction.toShortString());
                    stringBuilder.append(")");
                    sb.append(stringBuilder.toString());
                    List<MessageMonitorInfo> infos = getHistoryMsgInfos(info.planTime);
                    if (infos.size() > 0) {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(" because of ");
                        stringBuilder2.append(infos.size());
                        stringBuilder2.append(" msg,");
                        sb.append(stringBuilder2.toString());
                        for (int i = 0; i < infos.size(); i++) {
                            MessageMonitorInfo thisInfo = (MessageMonitorInfo) infos.get(i);
                            if (thisInfo.getWallMillis() > 50) {
                                StringBuilder stringBuilder3 = new StringBuilder();
                                stringBuilder3.append(" msg ");
                                stringBuilder3.append(i + 1);
                                stringBuilder3.append(" took ");
                                stringBuilder3.append(thisInfo.getWallMillis());
                                stringBuilder3.append("ms (");
                                sb.append(stringBuilder3.toString());
                                stringBuilder3 = new StringBuilder();
                                stringBuilder3.append(thisInfo.toShortString());
                                stringBuilder3.append("),");
                                sb.append(stringBuilder3.toString());
                            }
                        }
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    Slog.w(TAG_LOOPER, sb.toString());
                }
            }
        }
    }

    public void checkInputEvent(InputEvent inputEvent) {
        long latency = SystemClock.uptimeMillis() - inputEvent.getEventTime();
        if (latency >= 300) {
            StringBuilder sb = new StringBuilder(256);
            String str = ")";
            String str2 = ", action=";
            String str3 = "ms late (event_seq=";
            String str4 = "Slow Looper ";
            StringBuilder stringBuilder;
            if (inputEvent instanceof KeyEvent) {
                KeyEvent keyEvent = (KeyEvent) inputEvent;
                if (keyEvent.getAction() == 0) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str4);
                    stringBuilder.append(Thread.currentThread().getName());
                    stringBuilder.append(": KeyEvent is ");
                    stringBuilder.append(latency);
                    stringBuilder.append(str3);
                    stringBuilder.append(inputEvent.getSequenceNumber());
                    stringBuilder.append(", code=");
                    stringBuilder.append(KeyEvent.keyCodeToString(keyEvent.getKeyCode()));
                    stringBuilder.append(str2);
                    stringBuilder.append(KeyEvent.actionToString(keyEvent.getAction()));
                    stringBuilder.append(str);
                    sb.append(stringBuilder.toString());
                } else {
                    return;
                }
            } else if (inputEvent instanceof MotionEvent) {
                MotionEvent motionEvent = (MotionEvent) inputEvent;
                if (motionEvent.getAction() == 0) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str4);
                    stringBuilder.append(Thread.currentThread().getName());
                    stringBuilder.append(": MotionEvent is ");
                    stringBuilder.append(latency);
                    stringBuilder.append(str3);
                    stringBuilder.append(inputEvent.getSequenceNumber());
                    stringBuilder.append(str2);
                    stringBuilder.append(MotionEvent.actionToString(motionEvent.getAction()));
                    stringBuilder.append(str);
                    sb.append(stringBuilder.toString());
                } else {
                    return;
                }
            } else {
                return;
            }
            List<MessageMonitorInfo> infos = getHistoryMsgInfos(inputEvent.getEventTime());
            if (infos.size() > 0) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(" because of ");
                stringBuilder2.append(infos.size());
                stringBuilder2.append(" msg,");
                sb.append(stringBuilder2.toString());
                for (int i = 0; i < infos.size(); i++) {
                    MessageMonitorInfo thisInfo = (MessageMonitorInfo) infos.get(i);
                    if (thisInfo.getWallMillis() > 50) {
                        StringBuilder stringBuilder3 = new StringBuilder();
                        stringBuilder3.append(" msg ");
                        stringBuilder3.append(i + 1);
                        stringBuilder3.append(" took ");
                        stringBuilder3.append(thisInfo.getWallMillis());
                        stringBuilder3.append("ms (");
                        sb.append(stringBuilder3.toString());
                        stringBuilder3 = new StringBuilder();
                        stringBuilder3.append(thisInfo.toShortString());
                        stringBuilder3.append("),");
                        sb.append(stringBuilder3.toString());
                    }
                }
                sb.deleteCharAt(sb.length() - 1);
            }
            Slog.w(TAG_LOOPER, sb.toString());
        }
    }

    private void addMessageToHistoryIfNeed(MessageMonitorInfo monitorInfo) {
        synchronized (this.mMsgLock) {
            this.mMsgHistoryQueue[this.mMsgIndexNext] = monitorInfo;
            this.mMsgIndexNext = ringAdvance(this.mMsgIndexNext, 1, 512);
            if (monitorInfo.getWallMillis() > 100) {
                this.mLongMsgHistoryQueue[this.mLongMsgIndexNext] = monitorInfo;
                this.mLongMsgIndexNext = ringAdvance(this.mLongMsgIndexNext, 1, 512);
            }
        }
    }

    private static int ringAdvance(int origin, int increment, int size) {
        int index = (origin + increment) % size;
        return index < 0 ? index + size : index;
    }

    private List<MessageMonitorInfo> getHistoryMsgInfos(long sinceUptime) {
        List<MessageMonitorInfo> ret = new ArrayList();
        int index = ringAdvance(this.mMsgIndexNext, -1, 512);
        for (int i = 0; i < 512; i++) {
            MessageMonitorInfo[] messageMonitorInfoArr = this.mMsgHistoryQueue;
            if (messageMonitorInfoArr[index] == null || messageMonitorInfoArr[index].finishTime < sinceUptime) {
                break;
            }
            ret.add(0, this.mMsgHistoryQueue[index]);
            index = ringAdvance(index, -1, 512);
        }
        return ret;
    }

    public static String formatCurrentTime(long curTimeMs) {
        return new SimpleDateFormat("HH:mm:ss.SSS ").format(new Date(curTimeMs));
    }

    /* JADX WARNING: Missing block: B:14:0x006c, code skipped:
            return;
     */
    private void dumpLtMessageHistory(java.io.PrintWriter r10, java.lang.String r11, long r12) {
        /*
        r9 = this;
        r0 = r9.mMsgLock;
        monitor-enter(r0);
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x006d }
        r1.<init>();	 Catch:{ all -> 0x006d }
        r1.append(r11);	 Catch:{ all -> 0x006d }
        r2 = "History of long time messages on ";
        r1.append(r2);	 Catch:{ all -> 0x006d }
        r2 = r9.getThreadInfo();	 Catch:{ all -> 0x006d }
        r1.append(r2);	 Catch:{ all -> 0x006d }
        r2 = ":";
        r1.append(r2);	 Catch:{ all -> 0x006d }
        r1 = r1.toString();	 Catch:{ all -> 0x006d }
        r10.println(r1);	 Catch:{ all -> 0x006d }
        r1 = r9.mLongMsgIndexNext;	 Catch:{ all -> 0x006d }
        r2 = r1;
        r3 = android.os.SystemClock.uptimeMillis();	 Catch:{ all -> 0x006d }
        r5 = 0;
    L_0x002b:
        r6 = -1;
        r7 = 512; // 0x200 float:7.175E-43 double:2.53E-321;
        r6 = ringAdvance(r2, r6, r7);	 Catch:{ all -> 0x006d }
        r2 = r6;
        r6 = r9.mLongMsgHistoryQueue;	 Catch:{ all -> 0x006d }
        r6 = r6[r2];	 Catch:{ all -> 0x006d }
        if (r6 == 0) goto L_0x006b;
    L_0x0039:
        r7 = r6.finishTime;	 Catch:{ all -> 0x006d }
        r7 = r3 - r7;
        r7 = (r7 > r12 ? 1 : (r7 == r12 ? 0 : -1));
        if (r7 <= 0) goto L_0x0042;
    L_0x0041:
        goto L_0x006b;
    L_0x0042:
        r7 = new java.lang.StringBuilder;	 Catch:{ all -> 0x006d }
        r7.<init>();	 Catch:{ all -> 0x006d }
        r7.append(r11);	 Catch:{ all -> 0x006d }
        r8 = "  Msg #";
        r7.append(r8);	 Catch:{ all -> 0x006d }
        r5 = r5 + 1;
        r7.append(r5);	 Catch:{ all -> 0x006d }
        r8 = ": ";
        r7.append(r8);	 Catch:{ all -> 0x006d }
        r8 = r6.toString();	 Catch:{ all -> 0x006d }
        r7.append(r8);	 Catch:{ all -> 0x006d }
        r7 = r7.toString();	 Catch:{ all -> 0x006d }
        r10.println(r7);	 Catch:{ all -> 0x006d }
        if (r2 != r1) goto L_0x002b;
    L_0x0069:
        monitor-exit(r0);	 Catch:{ all -> 0x006d }
        return;
    L_0x006b:
        monitor-exit(r0);	 Catch:{ all -> 0x006d }
        return;
    L_0x006d:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x006d }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.MessageMonitor.dumpLtMessageHistory(java.io.PrintWriter, java.lang.String, long):void");
    }

    private void dumpRunningMessage(PrintWriter pw, String prefix) {
        if (this.mRunningMessageInfo != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append("Current running message:");
            pw.println(stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append("  ");
            stringBuilder.append(this.mRunningMessageInfo.toString());
            pw.println(stringBuilder.toString());
        }
    }

    private void dumpInternal(PrintWriter writer, String prefix, long duration) {
        if (this.mEnableMonitorMessage) {
            dumpRunningMessage(writer, prefix);
            dumpLtMessageHistory(writer, prefix, duration);
        }
    }

    public void dumpAllLoopers(PrintWriter pw, String prefix) {
        if (this.mEnableMonitorMessage) {
            synchronized (sCallbacksLock) {
                Iterator it = sCallbacks.iterator();
                while (it.hasNext()) {
                    MessageMonitor monitor = (MessageMonitor) ((WeakReference) it.next()).get();
                    if (monitor != null) {
                        monitor.dumpLtMessageHistory(pw, prefix, AlarmManager.INTERVAL_HALF_HOUR);
                    }
                }
            }
        }
    }

    public String dumpAll(String prefix) {
        return dumpAll(prefix, AlarmManager.INTERVAL_HALF_HOUR);
    }

    public String dumpAll(String prefix, long duration) {
        long start = SystemClock.uptimeMillis();
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(prefix);
        stringBuilder.append("Dump time: ");
        stringBuilder.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z").format(new Date()));
        writer.println(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(prefix);
        stringBuilder.append("Package: ");
        stringBuilder.append(AnrMonitor.currentPackageName());
        writer.println(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(prefix);
        stringBuilder.append("Current looper: ");
        stringBuilder.append(getThreadInfo());
        writer.println(stringBuilder.toString());
        dumpInternal(writer, prefix, duration);
        writer.flush();
        stringBuilder = new StringBuilder();
        stringBuilder.append("Dump Looper message took ");
        stringBuilder.append(SystemClock.uptimeMillis() - start);
        stringBuilder.append("ms");
        Log.d(TAG, stringBuilder.toString());
        return stringWriter.toString();
    }

    public void logLateFrameIfNeed(long delayNanos, long frameTimeNanos) {
    }
}
