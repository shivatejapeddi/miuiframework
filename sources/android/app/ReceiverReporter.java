package android.app;

import android.content.Intent;
import android.content.pm.ParceledListSlice;
import android.os.AnrMonitor;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import miui.mqsas.sdk.MQSEventManagerDelegate;
import miui.mqsas.sdk.event.BroadcastEvent;
import miui.os.Build;

public class ReceiverReporter {
    private static final boolean DEBUG = false;
    private static final boolean IS_STABLE_VERSION = Build.IS_STABLE_VERSION;
    private static final int MAX_QUANTITY = 30;
    private static ArrayList<BroadcastEvent> RE_LIST = new ArrayList();
    private static final String TAG = "MIUI-ReceiverReporter";
    public static long mDispatchThreshold = SystemProperties.getLong("persist.receiver.dispatch", AnrMonitor.PERF_EVENT_LOGGING_TIMEOUT);
    private static long mHandleThreshold = SystemProperties.getLong("persist.receiver.handle", AnrMonitor.MESSAGE_EXECUTION_TIMEOUT);
    private static int mIndex = 0;
    private static final Object mObject = new Object();
    private static volatile ReceiverHandler mReHandler = null;
    private static HandlerThread mReThread = null;
    private static ArrayList<ReceiverObj> mReceiverMap = new ArrayList();
    private static String sPackageName = null;
    private static String sProcessName = null;
    private static boolean sSystemBootCompleted;

    private static final class ReceiverHandler extends Handler {
        static final int RECEIVER_RECORDS = 0;

        public ReceiverHandler(Looper looper) {
            super(looper, null);
        }

        public void handleMessage(Message msg) {
            int i = msg.what;
            String str = ReceiverReporter.TAG;
            if (i != 0) {
                Log.w(str, "wrong message received of BRReportHandler");
                return;
            }
            try {
                ParceledListSlice<BroadcastEvent> reportEvents = msg.obj;
                if (reportEvents != null && ReceiverReporter.isSystemBootCompleted()) {
                    MQSEventManagerDelegate.getInstance().reportBroadcastEvent(reportEvents);
                }
            } catch (Exception e) {
                Log.e(str, "report message record error.", e);
            }
        }
    }

    private static class ReceiverObj {
        private String action;
        private String packageName;
        private String receiver;

        public ReceiverObj(String action, String packageName, String receiver) {
            this.action = action;
            this.packageName = packageName;
            this.receiver = receiver;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof ReceiverObj)) {
                return super.equals(obj);
            }
            ReceiverObj receiverObj = (ReceiverObj) obj;
            boolean z = this.action.equals(receiverObj.action) && this.packageName.equals(receiverObj.packageName) && this.receiver.equals(receiverObj.receiver);
            return z;
        }

        public String toString() {
            String result = new StringBuilder();
            result.append("action: ");
            result.append(this.action);
            result.append(", packageName: ");
            result.append(this.packageName);
            result.append(", receiver: ");
            result.append(this.receiver);
            return result.toString();
        }
    }

    private static String currentProcessName() {
        if (TextUtils.isEmpty(sProcessName)) {
            String processName = ActivityThread.currentProcessName();
            if (TextUtils.isEmpty(processName)) {
                sProcessName = "system_server";
            } else {
                sProcessName = processName;
            }
        }
        return sProcessName;
    }

    private static String currentPackageName() {
        if (TextUtils.isEmpty(sPackageName)) {
            String packageName = ActivityThread.currentPackageName();
            if (TextUtils.isEmpty(packageName)) {
                sPackageName = "android";
            } else {
                sPackageName = packageName;
            }
        }
        return sPackageName;
    }

    static Handler getReceiverHandler() {
        if (mReHandler == null) {
            synchronized (mObject) {
                if (mReHandler == null) {
                    mReThread = new HandlerThread("receiver-thread");
                    mReThread.start();
                    mReHandler = new ReceiverHandler(mReThread.getLooper());
                }
            }
        }
        return mReHandler;
    }

    private static boolean isSystemBootCompleted() {
        if (!sSystemBootCompleted) {
            sSystemBootCompleted = "1".equals(SystemProperties.get("sys.boot_completed"));
        }
        return sSystemBootCompleted;
    }

    static void onReceiverFinished(Intent intent, long enTime, long disTime, long finTime, String broadcastReceiver, boolean isQuWorked) {
        long j = enTime;
        long j2 = disTime;
        long j3 = finTime;
        String str = broadcastReceiver;
        if (!IS_STABLE_VERSION) {
            String action = intent.getAction();
            if (action == null) {
                action = "null";
            }
            String mPackageName = currentPackageName();
            if (j2 - j >= mDispatchThreshold || j3 - j2 >= mHandleThreshold) {
                BroadcastEvent receiver = new BroadcastEvent();
                receiver.setType(65);
                receiver.setAction(action);
                receiver.setEnTime(j);
                receiver.setDisTime(j2);
                receiver.setFinTime(j3);
                receiver.setBrReceiver(str);
                receiver.setQuWorked(isQuWorked);
                receiver.setPid(Process.myPid());
                receiver.setProcessName(currentProcessName());
                receiver.setPackageName(mPackageName);
                receiver.setTimeStamp(System.currentTimeMillis());
                receiver.setSystem("android".equals(mPackageName));
                int i = mIndex;
                if (i < 0 || i > 30) {
                    i = 0;
                }
                mIndex = i;
                ReceiverObj receiverObj = new ReceiverObj(action, mPackageName, str);
                int i2 = mIndex;
                if (i2 == 0 || i2 > 30 || !mReceiverMap.contains(receiverObj)) {
                    if (mIndex >= 30) {
                        Message message = Message.obtain();
                        ReceiverHandler receiverHandler = mReHandler;
                        message.what = 0;
                        message.obj = new ParceledListSlice((ArrayList) RE_LIST.clone());
                        getReceiverHandler().sendMessage(message);
                        RE_LIST.clear();
                        mReceiverMap.clear();
                        mIndex = 0;
                    }
                    mIndex++;
                    mReceiverMap.add(receiverObj);
                    RE_LIST.add(receiver);
                } else {
                    RE_LIST.set(mReceiverMap.indexOf(receiverObj), receiver);
                }
            } else {
                boolean z = isQuWorked;
            }
        }
    }
}
