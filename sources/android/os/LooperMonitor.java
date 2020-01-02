package android.os;

import miui.util.ReflectionUtils;

public class LooperMonitor implements ILooperMonitorable {
    private boolean mEnableMonitor;

    public boolean isMonitorLooper() {
        return this.mEnableMonitor;
    }

    public void enableMonitor(boolean enable) {
        this.mEnableMonitor = enable;
        if (enable) {
            try {
                MessageQueue queue = (MessageQueue) ReflectionUtils.callMethod(this, "getQueue", MessageQueue.class, new Object[0]);
                if (queue != null) {
                    ReflectionUtils.callMethod(queue, "enableMonitor", Void.class, new Object[0]);
                }
            } catch (Exception e) {
            }
        }
    }
}
