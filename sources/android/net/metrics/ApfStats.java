package android.net.metrics;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.net.metrics.IpConnectivityLog.Event;
import android.os.Parcel;
import android.os.Parcelable.Creator;

@SystemApi
public final class ApfStats implements Event {
    public static final Creator<ApfStats> CREATOR = new Creator<ApfStats>() {
        public ApfStats createFromParcel(Parcel in) {
            return new ApfStats(in, null);
        }

        public ApfStats[] newArray(int size) {
            return new ApfStats[size];
        }
    };
    @UnsupportedAppUsage
    public final int droppedRas;
    @UnsupportedAppUsage
    public final long durationMs;
    @UnsupportedAppUsage
    public final int matchingRas;
    @UnsupportedAppUsage
    public final int maxProgramSize;
    @UnsupportedAppUsage
    public final int parseErrors;
    @UnsupportedAppUsage
    public final int programUpdates;
    @UnsupportedAppUsage
    public final int programUpdatesAll;
    @UnsupportedAppUsage
    public final int programUpdatesAllowingMulticast;
    @UnsupportedAppUsage
    public final int receivedRas;
    @UnsupportedAppUsage
    public final int zeroLifetimeRas;

    @SystemApi
    public static final class Builder {
        private int mDroppedRas;
        private long mDurationMs;
        private int mMatchingRas;
        private int mMaxProgramSize;
        private int mParseErrors;
        private int mProgramUpdates;
        private int mProgramUpdatesAll;
        private int mProgramUpdatesAllowingMulticast;
        private int mReceivedRas;
        private int mZeroLifetimeRas;

        public Builder setDurationMs(long durationMs) {
            this.mDurationMs = durationMs;
            return this;
        }

        public Builder setReceivedRas(int receivedRas) {
            this.mReceivedRas = receivedRas;
            return this;
        }

        public Builder setMatchingRas(int matchingRas) {
            this.mMatchingRas = matchingRas;
            return this;
        }

        public Builder setDroppedRas(int droppedRas) {
            this.mDroppedRas = droppedRas;
            return this;
        }

        public Builder setZeroLifetimeRas(int zeroLifetimeRas) {
            this.mZeroLifetimeRas = zeroLifetimeRas;
            return this;
        }

        public Builder setParseErrors(int parseErrors) {
            this.mParseErrors = parseErrors;
            return this;
        }

        public Builder setProgramUpdates(int programUpdates) {
            this.mProgramUpdates = programUpdates;
            return this;
        }

        public Builder setProgramUpdatesAll(int programUpdatesAll) {
            this.mProgramUpdatesAll = programUpdatesAll;
            return this;
        }

        public Builder setProgramUpdatesAllowingMulticast(int programUpdatesAllowingMulticast) {
            this.mProgramUpdatesAllowingMulticast = programUpdatesAllowingMulticast;
            return this;
        }

        public Builder setMaxProgramSize(int maxProgramSize) {
            this.mMaxProgramSize = maxProgramSize;
            return this;
        }

        public ApfStats build() {
            return new ApfStats(this.mDurationMs, this.mReceivedRas, this.mMatchingRas, this.mDroppedRas, this.mZeroLifetimeRas, this.mParseErrors, this.mProgramUpdates, this.mProgramUpdatesAll, this.mProgramUpdatesAllowingMulticast, this.mMaxProgramSize, null);
        }
    }

    /* synthetic */ ApfStats(long x0, int x1, int x2, int x3, int x4, int x5, int x6, int x7, int x8, int x9, AnonymousClass1 x10) {
        this(x0, x1, x2, x3, x4, x5, x6, x7, x8, x9);
    }

    /* synthetic */ ApfStats(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    private ApfStats(Parcel in) {
        this.durationMs = in.readLong();
        this.receivedRas = in.readInt();
        this.matchingRas = in.readInt();
        this.droppedRas = in.readInt();
        this.zeroLifetimeRas = in.readInt();
        this.parseErrors = in.readInt();
        this.programUpdates = in.readInt();
        this.programUpdatesAll = in.readInt();
        this.programUpdatesAllowingMulticast = in.readInt();
        this.maxProgramSize = in.readInt();
    }

    private ApfStats(long durationMs, int receivedRas, int matchingRas, int droppedRas, int zeroLifetimeRas, int parseErrors, int programUpdates, int programUpdatesAll, int programUpdatesAllowingMulticast, int maxProgramSize) {
        this.durationMs = durationMs;
        this.receivedRas = receivedRas;
        this.matchingRas = matchingRas;
        this.droppedRas = droppedRas;
        this.zeroLifetimeRas = zeroLifetimeRas;
        this.parseErrors = parseErrors;
        this.programUpdates = programUpdates;
        this.programUpdatesAll = programUpdatesAll;
        this.programUpdatesAllowingMulticast = programUpdatesAllowingMulticast;
        this.maxProgramSize = maxProgramSize;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.durationMs);
        out.writeInt(this.receivedRas);
        out.writeInt(this.matchingRas);
        out.writeInt(this.droppedRas);
        out.writeInt(this.zeroLifetimeRas);
        out.writeInt(this.parseErrors);
        out.writeInt(this.programUpdates);
        out.writeInt(this.programUpdatesAll);
        out.writeInt(this.programUpdatesAllowingMulticast);
        out.writeInt(this.maxProgramSize);
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("ApfStats(");
        stringBuilder.append(String.format("%dms ", new Object[]{Long.valueOf(this.durationMs)}));
        stringBuilder.append(String.format("%dB RA: {", new Object[]{Integer.valueOf(this.maxProgramSize)}));
        stringBuilder.append(String.format("%d received, ", new Object[]{Integer.valueOf(this.receivedRas)}));
        stringBuilder.append(String.format("%d matching, ", new Object[]{Integer.valueOf(this.matchingRas)}));
        stringBuilder.append(String.format("%d dropped, ", new Object[]{Integer.valueOf(this.droppedRas)}));
        stringBuilder.append(String.format("%d zero lifetime, ", new Object[]{Integer.valueOf(this.zeroLifetimeRas)}));
        stringBuilder.append(String.format("%d parse errors}, ", new Object[]{Integer.valueOf(this.parseErrors)}));
        stringBuilder.append(String.format("updates: {all: %d, RAs: %d, allow multicast: %d})", new Object[]{Integer.valueOf(this.programUpdatesAll), Integer.valueOf(this.programUpdates), Integer.valueOf(this.programUpdatesAllowingMulticast)}));
        return stringBuilder.toString();
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (obj == null || !obj.getClass().equals(ApfStats.class)) {
            return false;
        }
        ApfStats other = (ApfStats) obj;
        if (this.durationMs == other.durationMs && this.receivedRas == other.receivedRas && this.matchingRas == other.matchingRas && this.droppedRas == other.droppedRas && this.zeroLifetimeRas == other.zeroLifetimeRas && this.parseErrors == other.parseErrors && this.programUpdates == other.programUpdates && this.programUpdatesAll == other.programUpdatesAll && this.programUpdatesAllowingMulticast == other.programUpdatesAllowingMulticast && this.maxProgramSize == other.maxProgramSize) {
            z = true;
        }
        return z;
    }
}
