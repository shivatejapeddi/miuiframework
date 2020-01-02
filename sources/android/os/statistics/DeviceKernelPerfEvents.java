package android.os.statistics;

public class DeviceKernelPerfEvents {
    public static native int fetchDeviceKernelPerfEvents(FilteringPerfEvent[] filteringPerfEventArr);

    public static native boolean initDeviceKernelPerfEvents();

    public static native boolean waitForDeviceKernelPerfEventArrived(int i);
}
