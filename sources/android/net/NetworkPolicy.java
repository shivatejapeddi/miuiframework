package android.net;

import android.annotation.UnsupportedAppUsage;
import android.os.AnrMonitor;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.BackupUtils;
import android.util.BackupUtils.BadVersionException;
import android.util.Range;
import android.util.RecurrenceRule;
import com.android.internal.util.Preconditions;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.Objects;

public class NetworkPolicy implements Parcelable, Comparable<NetworkPolicy> {
    @UnsupportedAppUsage
    public static final Creator<NetworkPolicy> CREATOR = new Creator<NetworkPolicy>() {
        public NetworkPolicy createFromParcel(Parcel in) {
            return new NetworkPolicy(in, null);
        }

        public NetworkPolicy[] newArray(int size) {
            return new NetworkPolicy[size];
        }
    };
    public static final int CYCLE_NONE = -1;
    private static final long DEFAULT_MTU = 1500;
    public static final long LIMIT_DISABLED = -1;
    public static final long SNOOZE_NEVER = -1;
    private static final int VERSION_INIT = 1;
    private static final int VERSION_RAPID = 3;
    private static final int VERSION_RULE = 2;
    public static final long WARNING_DISABLED = -1;
    public RecurrenceRule cycleRule;
    @UnsupportedAppUsage
    public boolean inferred;
    public long lastLimitSnooze;
    public long lastRapidSnooze;
    public long lastWarningSnooze;
    @UnsupportedAppUsage
    public long limitBytes;
    @Deprecated
    @UnsupportedAppUsage
    public boolean metered;
    @UnsupportedAppUsage
    public NetworkTemplate template;
    @UnsupportedAppUsage
    public long warningBytes;

    /* synthetic */ NetworkPolicy(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public static RecurrenceRule buildRule(int cycleDay, ZoneId cycleTimezone) {
        if (cycleDay != -1) {
            return RecurrenceRule.buildRecurringMonthly(cycleDay, cycleTimezone);
        }
        return RecurrenceRule.buildNever();
    }

    @Deprecated
    public NetworkPolicy(NetworkTemplate template, int cycleDay, String cycleTimezone, long warningBytes, long limitBytes, boolean metered) {
        this(template, cycleDay, cycleTimezone, warningBytes, limitBytes, -1, -1, metered, false);
    }

    @Deprecated
    @UnsupportedAppUsage
    public NetworkPolicy(NetworkTemplate template, int cycleDay, String cycleTimezone, long warningBytes, long limitBytes, long lastWarningSnooze, long lastLimitSnooze, boolean metered, boolean inferred) {
        NetworkTemplate networkTemplate = template;
        this(networkTemplate, buildRule(cycleDay, ZoneId.of(cycleTimezone)), warningBytes, limitBytes, lastWarningSnooze, lastLimitSnooze, metered, inferred);
    }

    @Deprecated
    public NetworkPolicy(NetworkTemplate template, RecurrenceRule cycleRule, long warningBytes, long limitBytes, long lastWarningSnooze, long lastLimitSnooze, boolean metered, boolean inferred) {
        this(template, cycleRule, warningBytes, limitBytes, lastWarningSnooze, lastLimitSnooze, -1, metered, inferred);
    }

    public NetworkPolicy(NetworkTemplate template, RecurrenceRule cycleRule, long warningBytes, long limitBytes, long lastWarningSnooze, long lastLimitSnooze, long lastRapidSnooze, boolean metered, boolean inferred) {
        this.warningBytes = -1;
        this.limitBytes = -1;
        this.lastWarningSnooze = -1;
        this.lastLimitSnooze = -1;
        this.lastRapidSnooze = -1;
        this.metered = true;
        this.inferred = false;
        this.template = (NetworkTemplate) Preconditions.checkNotNull(template, "missing NetworkTemplate");
        this.cycleRule = (RecurrenceRule) Preconditions.checkNotNull(cycleRule, "missing RecurrenceRule");
        this.warningBytes = warningBytes;
        this.limitBytes = limitBytes;
        this.lastWarningSnooze = lastWarningSnooze;
        this.lastLimitSnooze = lastLimitSnooze;
        this.lastRapidSnooze = lastRapidSnooze;
        this.metered = metered;
        this.inferred = inferred;
    }

    private NetworkPolicy(Parcel source) {
        this.warningBytes = -1;
        this.limitBytes = -1;
        this.lastWarningSnooze = -1;
        this.lastLimitSnooze = -1;
        this.lastRapidSnooze = -1;
        boolean z = true;
        this.metered = true;
        this.inferred = false;
        this.template = (NetworkTemplate) source.readParcelable(null);
        this.cycleRule = (RecurrenceRule) source.readParcelable(null);
        this.warningBytes = source.readLong();
        this.limitBytes = source.readLong();
        this.lastWarningSnooze = source.readLong();
        this.lastLimitSnooze = source.readLong();
        this.lastRapidSnooze = source.readLong();
        this.metered = source.readInt() != 0;
        if (source.readInt() == 0) {
            z = false;
        }
        this.inferred = z;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.template, flags);
        dest.writeParcelable(this.cycleRule, flags);
        dest.writeLong(this.warningBytes);
        dest.writeLong(this.limitBytes);
        dest.writeLong(this.lastWarningSnooze);
        dest.writeLong(this.lastLimitSnooze);
        dest.writeLong(this.lastRapidSnooze);
        dest.writeInt(this.metered);
        dest.writeInt(this.inferred);
    }

    public int describeContents() {
        return 0;
    }

    public Iterator<Range<ZonedDateTime>> cycleIterator() {
        return this.cycleRule.cycleIterator();
    }

    @UnsupportedAppUsage
    public boolean isOverWarning(long totalBytes) {
        long j = this.warningBytes;
        return j != -1 && totalBytes >= j;
    }

    @UnsupportedAppUsage
    public boolean isOverLimit(long totalBytes) {
        totalBytes += AnrMonitor.PERF_EVENT_LOGGING_TIMEOUT;
        long j = this.limitBytes;
        return j != -1 && totalBytes >= j;
    }

    @UnsupportedAppUsage
    public void clearSnooze() {
        this.lastWarningSnooze = -1;
        this.lastLimitSnooze = -1;
        this.lastRapidSnooze = -1;
    }

    public boolean hasCycle() {
        return this.cycleRule.cycleIterator().hasNext();
    }

    @UnsupportedAppUsage
    public int compareTo(NetworkPolicy another) {
        if (another != null) {
            long j = another.limitBytes;
            if (j != -1) {
                long j2 = this.limitBytes;
                if (j2 == -1 || j < j2) {
                    return 1;
                }
                return 0;
            }
        }
        return -1;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.template, this.cycleRule, Long.valueOf(this.warningBytes), Long.valueOf(this.limitBytes), Long.valueOf(this.lastWarningSnooze), Long.valueOf(this.lastLimitSnooze), Long.valueOf(this.lastRapidSnooze), Boolean.valueOf(this.metered), Boolean.valueOf(this.inferred)});
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (!(obj instanceof NetworkPolicy)) {
            return false;
        }
        NetworkPolicy other = (NetworkPolicy) obj;
        if (this.warningBytes == other.warningBytes && this.limitBytes == other.limitBytes && this.lastWarningSnooze == other.lastWarningSnooze && this.lastLimitSnooze == other.lastLimitSnooze && this.lastRapidSnooze == other.lastRapidSnooze && this.metered == other.metered && this.inferred == other.inferred && Objects.equals(this.template, other.template) && Objects.equals(this.cycleRule, other.cycleRule)) {
            z = true;
        }
        return z;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("NetworkPolicy{");
        stringBuilder.append("template=");
        stringBuilder.append(this.template);
        stringBuilder.append(" cycleRule=");
        stringBuilder.append(this.cycleRule);
        stringBuilder.append(" warningBytes=");
        stringBuilder.append(this.warningBytes);
        stringBuilder.append(" limitBytes=");
        stringBuilder.append(this.limitBytes);
        stringBuilder.append(" lastWarningSnooze=");
        stringBuilder.append(this.lastWarningSnooze);
        stringBuilder.append(" lastLimitSnooze=");
        stringBuilder.append(this.lastLimitSnooze);
        stringBuilder.append(" lastRapidSnooze=");
        stringBuilder.append(this.lastRapidSnooze);
        stringBuilder.append(" metered=");
        stringBuilder.append(this.metered);
        stringBuilder.append(" inferred=");
        stringBuilder.append(this.inferred);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public byte[] getBytesForBackup() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        out.writeInt(3);
        out.write(this.template.getBytesForBackup());
        this.cycleRule.writeToStream(out);
        out.writeLong(this.warningBytes);
        out.writeLong(this.limitBytes);
        out.writeLong(this.lastWarningSnooze);
        out.writeLong(this.lastLimitSnooze);
        out.writeLong(this.lastRapidSnooze);
        out.writeInt(this.metered);
        out.writeInt(this.inferred);
        return baos.toByteArray();
    }

    public static NetworkPolicy getNetworkPolicyFromBackup(DataInputStream in) throws IOException, BadVersionException {
        int version = in.readInt();
        if (version < 1 || version > 3) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unknown backup version: ");
            stringBuilder.append(version);
            throw new BadVersionException(stringBuilder.toString());
        }
        RecurrenceRule cycleRule;
        long lastRapidSnooze;
        NetworkTemplate template = NetworkTemplate.getNetworkTemplateFromBackup(in);
        if (version >= 2) {
            cycleRule = new RecurrenceRule(in);
        } else {
            DataInputStream dataInputStream = in;
            cycleRule = buildRule(in.readInt(), ZoneId.of(BackupUtils.readString(in)));
        }
        long warningBytes = in.readLong();
        long limitBytes = in.readLong();
        long lastWarningSnooze = in.readLong();
        long lastLimitSnooze = in.readLong();
        if (version >= 3) {
            lastRapidSnooze = in.readLong();
        } else {
            lastRapidSnooze = -1;
        }
        return new NetworkPolicy(template, cycleRule, warningBytes, limitBytes, lastWarningSnooze, lastLimitSnooze, lastRapidSnooze, in.readInt() == 1, in.readInt() == 1);
    }
}
