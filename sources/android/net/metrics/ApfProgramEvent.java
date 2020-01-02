package android.net.metrics;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.net.metrics.IpConnectivityLog.Event;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.SparseArray;
import com.android.internal.util.MessageUtils;
import com.miui.mishare.RemoteDevice;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.BitSet;

@SystemApi
public final class ApfProgramEvent implements Event {
    public static final Creator<ApfProgramEvent> CREATOR = new Creator<ApfProgramEvent>() {
        public ApfProgramEvent createFromParcel(Parcel in) {
            return new ApfProgramEvent(in, null);
        }

        public ApfProgramEvent[] newArray(int size) {
            return new ApfProgramEvent[size];
        }
    };
    public static final int FLAG_HAS_IPV4_ADDRESS = 1;
    public static final int FLAG_MULTICAST_FILTER_ON = 0;
    @UnsupportedAppUsage
    public final long actualLifetime;
    @UnsupportedAppUsage
    public final int currentRas;
    @UnsupportedAppUsage
    public final int filteredRas;
    @UnsupportedAppUsage
    public final int flags;
    @UnsupportedAppUsage
    public final long lifetime;
    @UnsupportedAppUsage
    public final int programLength;

    public static final class Builder {
        private long mActualLifetime;
        private int mCurrentRas;
        private int mFilteredRas;
        private int mFlags;
        private long mLifetime;
        private int mProgramLength;

        public Builder setLifetime(long lifetime) {
            this.mLifetime = lifetime;
            return this;
        }

        public Builder setActualLifetime(long lifetime) {
            this.mActualLifetime = lifetime;
            return this;
        }

        public Builder setFilteredRas(int filteredRas) {
            this.mFilteredRas = filteredRas;
            return this;
        }

        public Builder setCurrentRas(int currentRas) {
            this.mCurrentRas = currentRas;
            return this;
        }

        public Builder setProgramLength(int programLength) {
            this.mProgramLength = programLength;
            return this;
        }

        public Builder setFlags(boolean hasIPv4, boolean multicastFilterOn) {
            this.mFlags = ApfProgramEvent.flagsFor(hasIPv4, multicastFilterOn);
            return this;
        }

        public ApfProgramEvent build() {
            return new ApfProgramEvent(this.mLifetime, this.mActualLifetime, this.mFilteredRas, this.mCurrentRas, this.mProgramLength, this.mFlags, null);
        }
    }

    static final class Decoder {
        static final SparseArray<String> constants = MessageUtils.findMessageNames(new Class[]{ApfProgramEvent.class}, new String[]{"FLAG_"});

        Decoder() {
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Flags {
    }

    /* synthetic */ ApfProgramEvent(long x0, long x1, int x2, int x3, int x4, int x5, AnonymousClass1 x6) {
        this(x0, x1, x2, x3, x4, x5);
    }

    /* synthetic */ ApfProgramEvent(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    private ApfProgramEvent(long lifetime, long actualLifetime, int filteredRas, int currentRas, int programLength, int flags) {
        this.lifetime = lifetime;
        this.actualLifetime = actualLifetime;
        this.filteredRas = filteredRas;
        this.currentRas = currentRas;
        this.programLength = programLength;
        this.flags = flags;
    }

    private ApfProgramEvent(Parcel in) {
        this.lifetime = in.readLong();
        this.actualLifetime = in.readLong();
        this.filteredRas = in.readInt();
        this.currentRas = in.readInt();
        this.programLength = in.readInt();
        this.flags = in.readInt();
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.lifetime);
        out.writeLong(this.actualLifetime);
        out.writeInt(this.filteredRas);
        out.writeInt(this.currentRas);
        out.writeInt(this.programLength);
        out.writeInt(this.flags);
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        String lifetimeString;
        if (this.lifetime < Long.MAX_VALUE) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.lifetime);
            stringBuilder.append(RemoteDevice.KEY_STATUS);
            lifetimeString = stringBuilder.toString();
        } else {
            lifetimeString = "forever";
        }
        return String.format("ApfProgramEvent(%d/%d RAs %dB %ds/%s %s)", new Object[]{Integer.valueOf(this.filteredRas), Integer.valueOf(this.currentRas), Integer.valueOf(this.programLength), Long.valueOf(this.actualLifetime), lifetimeString, namesOf(this.flags)});
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (obj == null || !obj.getClass().equals(ApfProgramEvent.class)) {
            return false;
        }
        ApfProgramEvent other = (ApfProgramEvent) obj;
        if (this.lifetime == other.lifetime && this.actualLifetime == other.actualLifetime && this.filteredRas == other.filteredRas && this.currentRas == other.currentRas && this.programLength == other.programLength && this.flags == other.flags) {
            z = true;
        }
        return z;
    }

    @UnsupportedAppUsage
    public static int flagsFor(boolean hasIPv4, boolean multicastFilterOn) {
        int bitfield = 0;
        if (hasIPv4) {
            bitfield = 0 | 2;
        }
        if (multicastFilterOn) {
            return bitfield | 1;
        }
        return bitfield;
    }

    private static String namesOf(int bitfield) {
        Iterable names = new ArrayList(Integer.bitCount(bitfield));
        BitSet set = BitSet.valueOf(new long[]{(long) (Integer.MAX_VALUE & bitfield)});
        for (int bit = set.nextSetBit(0); bit >= 0; bit = set.nextSetBit(bit + 1)) {
            names.add((String) Decoder.constants.get(bit));
        }
        return TextUtils.join((CharSequence) "|", names);
    }
}
