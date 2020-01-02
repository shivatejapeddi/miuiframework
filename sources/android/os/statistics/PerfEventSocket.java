package android.os.statistics;

import android.os.Parcel;

public class PerfEventSocket {
    public static native int receivePerfEvents(int i, Parcel parcel, long j, int i2, FilteringPerfEvent[] filteringPerfEventArr);

    public static native int sendPerfEvent(int i, Parcel parcel, long j, int i2);

    public static native int waitForPerfEventArrived(int i);
}
