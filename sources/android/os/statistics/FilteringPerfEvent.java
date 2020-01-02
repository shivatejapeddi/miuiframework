package android.os.statistics;

import android.os.Parcel;

public final class FilteringPerfEvent extends FilteringPerfEventListNode {
    public long beginUptimeMillis;
    public long detailsPtr;
    public int durationMillis;
    public long endUptimeMillis;
    int enoughPeerWaitDuration;
    public PerfEvent event;
    public int eventFlags;
    public long eventSeq;
    public int eventType;
    public long inclusionId;
    volatile int matchedPeerWaitDuration;
    long sortingUptimeMillis;
    public long synchronizationId;

    private static native void nativeDipose(long j, int i);

    private static native void nativeResolve(PerfEvent perfEvent, long j, int i, Parcel parcel, long j2);

    private static native void nativeWriteToParcel(Parcel parcel, long j, int i, int i2, long j2, long j3, long j4, long j5, long j6, long j7);

    public FilteringPerfEvent() {
        this.value = this;
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        dispose();
        super.finalize();
    }

    public void set(PerfEvent _event) {
        this.eventType = _event.eventType;
        this.eventFlags = _event.eventFlags;
        this.beginUptimeMillis = _event.beginUptimeMillis;
        this.endUptimeMillis = _event.endUptimeMillis;
        this.inclusionId = _event.inclusionId;
        this.synchronizationId = _event.synchronizationId;
        this.eventSeq = _event.eventSeq;
        this.detailsPtr = 0;
        this.event = _event;
        this.durationMillis = (int) (this.endUptimeMillis - this.beginUptimeMillis);
    }

    public void resolve(Parcel tempParcel, long tempParcelNativePtr) {
        if (this.detailsPtr != 0) {
            PerfEvent temp = PerfEventFactory.createPerfEvent(this.eventType);
            int i = this.eventFlags;
            temp.eventFlags = i;
            temp.beginUptimeMillis = this.beginUptimeMillis;
            temp.endUptimeMillis = this.endUptimeMillis;
            temp.inclusionId = this.inclusionId;
            temp.synchronizationId = this.synchronizationId;
            temp.eventSeq = this.eventSeq;
            nativeResolve(temp, this.detailsPtr, i, tempParcel, tempParcelNativePtr);
            nativeDipose(this.detailsPtr, this.eventFlags);
            this.detailsPtr = 0;
            this.event = temp;
        }
    }

    public void resolveTo(PerfEvent targetEvent, Parcel tempParcel, long tempParcelNativePtr) {
        long j = this.detailsPtr;
        if (j != 0) {
            int i = this.eventFlags;
            targetEvent.eventFlags = i;
            targetEvent.beginUptimeMillis = this.beginUptimeMillis;
            targetEvent.endUptimeMillis = this.endUptimeMillis;
            targetEvent.inclusionId = this.inclusionId;
            targetEvent.synchronizationId = this.synchronizationId;
            targetEvent.eventSeq = this.eventSeq;
            nativeResolve(targetEvent, j, i, tempParcel, tempParcelNativePtr);
            nativeDipose(this.detailsPtr, this.eventFlags);
            this.detailsPtr = 0;
        }
    }

    public void dispose() {
        this.event = null;
        long j = this.detailsPtr;
        if (j != 0) {
            nativeDipose(j, this.eventFlags);
            this.detailsPtr = 0;
        }
    }

    public void writeToParcel(Parcel dest, long destNativePtr) {
        Parcel parcel = dest;
        long j = destNativePtr;
        Parcel parcel2 = parcel;
        long j2 = j;
        nativeWriteToParcel(parcel2, j2, this.eventType, this.eventFlags, this.beginUptimeMillis, this.endUptimeMillis, this.inclusionId, this.synchronizationId, this.eventSeq, this.detailsPtr);
    }
}
