package android.net;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.util.Slog;
import android.util.SparseBooleanArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.util.ArrayUtils;
import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import libcore.util.EmptyArray;

public class NetworkStats implements Parcelable {
    private static final String CLATD_INTERFACE_PREFIX = "v4-";
    @UnsupportedAppUsage
    public static final Creator<NetworkStats> CREATOR = new Creator<NetworkStats>() {
        public NetworkStats createFromParcel(Parcel in) {
            return new NetworkStats(in);
        }

        public NetworkStats[] newArray(int size) {
            return new NetworkStats[size];
        }
    };
    public static final int DEFAULT_NETWORK_ALL = -1;
    public static final int DEFAULT_NETWORK_NO = 0;
    public static final int DEFAULT_NETWORK_YES = 1;
    public static final String IFACE_ALL = null;
    public static final String[] INTERFACES_ALL = null;
    private static final int IPV4V6_HEADER_DELTA = 20;
    public static final int METERED_ALL = -1;
    public static final int METERED_NO = 0;
    public static final int METERED_YES = 1;
    public static final int ROAMING_ALL = -1;
    public static final int ROAMING_NO = 0;
    public static final int ROAMING_YES = 1;
    public static final int SET_ALL = -1;
    public static final int SET_DBG_VPN_IN = 1001;
    public static final int SET_DBG_VPN_OUT = 1002;
    public static final int SET_DEBUG_START = 1000;
    public static final int SET_DEFAULT = 0;
    public static final int SET_FOREGROUND = 1;
    public static final int STATS_PER_IFACE = 0;
    public static final int STATS_PER_UID = 1;
    private static final String TAG = "NetworkStats";
    public static final int TAG_ALL = -1;
    public static final int TAG_NONE = 0;
    public static final int UID_ALL = -1;
    @UnsupportedAppUsage
    private int capacity;
    @UnsupportedAppUsage
    private int[] defaultNetwork;
    private long elapsedRealtime;
    @UnsupportedAppUsage
    private String[] iface;
    @UnsupportedAppUsage
    private int[] metered;
    @UnsupportedAppUsage
    private long[] operations;
    @UnsupportedAppUsage
    private int[] roaming;
    @UnsupportedAppUsage
    private long[] rxBytes;
    @UnsupportedAppUsage
    private long[] rxPackets;
    @UnsupportedAppUsage
    private int[] set;
    @UnsupportedAppUsage
    private int size;
    @UnsupportedAppUsage
    private int[] tag;
    @UnsupportedAppUsage
    private long[] txBytes;
    @UnsupportedAppUsage
    private long[] txPackets;
    @UnsupportedAppUsage
    private int[] uid;

    public static class Entry {
        public int defaultNetwork;
        @UnsupportedAppUsage
        public String iface;
        public int metered;
        public long operations;
        public int roaming;
        @UnsupportedAppUsage
        public long rxBytes;
        @UnsupportedAppUsage
        public long rxPackets;
        @UnsupportedAppUsage
        public int set;
        @UnsupportedAppUsage
        public int tag;
        @UnsupportedAppUsage
        public long txBytes;
        @UnsupportedAppUsage
        public long txPackets;
        @UnsupportedAppUsage
        public int uid;

        @UnsupportedAppUsage
        public Entry() {
            this(NetworkStats.IFACE_ALL, -1, 0, 0, 0, 0, 0, 0, 0);
        }

        public Entry(long rxBytes, long rxPackets, long txBytes, long txPackets, long operations) {
            this(NetworkStats.IFACE_ALL, -1, 0, 0, rxBytes, rxPackets, txBytes, txPackets, operations);
        }

        public Entry(String iface, int uid, int set, int tag, long rxBytes, long rxPackets, long txBytes, long txPackets, long operations) {
            this(iface, uid, set, tag, 0, 0, 0, rxBytes, rxPackets, txBytes, txPackets, operations);
        }

        public Entry(String iface, int uid, int set, int tag, int metered, int roaming, int defaultNetwork, long rxBytes, long rxPackets, long txBytes, long txPackets, long operations) {
            this.iface = iface;
            this.uid = uid;
            this.set = set;
            this.tag = tag;
            this.metered = metered;
            this.roaming = roaming;
            this.defaultNetwork = defaultNetwork;
            this.rxBytes = rxBytes;
            this.rxPackets = rxPackets;
            this.txBytes = txBytes;
            this.txPackets = txPackets;
            this.operations = operations;
        }

        public boolean isNegative() {
            return this.rxBytes < 0 || this.rxPackets < 0 || this.txBytes < 0 || this.txPackets < 0 || this.operations < 0;
        }

        public boolean isEmpty() {
            return this.rxBytes == 0 && this.rxPackets == 0 && this.txBytes == 0 && this.txPackets == 0 && this.operations == 0;
        }

        public void add(Entry another) {
            this.rxBytes += another.rxBytes;
            this.rxPackets += another.rxPackets;
            this.txBytes += another.txBytes;
            this.txPackets += another.txPackets;
            this.operations += another.operations;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("iface=");
            builder.append(this.iface);
            builder.append(" uid=");
            builder.append(this.uid);
            builder.append(" set=");
            builder.append(NetworkStats.setToString(this.set));
            builder.append(" tag=");
            builder.append(NetworkStats.tagToString(this.tag));
            builder.append(" metered=");
            builder.append(NetworkStats.meteredToString(this.metered));
            builder.append(" roaming=");
            builder.append(NetworkStats.roamingToString(this.roaming));
            builder.append(" defaultNetwork=");
            builder.append(NetworkStats.defaultNetworkToString(this.defaultNetwork));
            builder.append(" rxBytes=");
            builder.append(this.rxBytes);
            builder.append(" rxPackets=");
            builder.append(this.rxPackets);
            builder.append(" txBytes=");
            builder.append(this.txBytes);
            builder.append(" txPackets=");
            builder.append(this.txPackets);
            builder.append(" operations=");
            builder.append(this.operations);
            return builder.toString();
        }

        public boolean equals(Object o) {
            boolean z = false;
            if (!(o instanceof Entry)) {
                return false;
            }
            Entry e = (Entry) o;
            if (this.uid == e.uid && this.set == e.set && this.tag == e.tag && this.metered == e.metered && this.roaming == e.roaming && this.defaultNetwork == e.defaultNetwork && this.rxBytes == e.rxBytes && this.rxPackets == e.rxPackets && this.txBytes == e.txBytes && this.txPackets == e.txPackets && this.operations == e.operations && this.iface.equals(e.iface)) {
                z = true;
            }
            return z;
        }

        public int hashCode() {
            return Objects.hash(new Object[]{Integer.valueOf(this.uid), Integer.valueOf(this.set), Integer.valueOf(this.tag), Integer.valueOf(this.metered), Integer.valueOf(this.roaming), Integer.valueOf(this.defaultNetwork), this.iface});
        }
    }

    public interface NonMonotonicObserver<C> {
        void foundNonMonotonic(NetworkStats networkStats, int i, NetworkStats networkStats2, int i2, C c);

        void foundNonMonotonic(NetworkStats networkStats, int i, C c);
    }

    @UnsupportedAppUsage
    public NetworkStats(long elapsedRealtime, int initialSize) {
        this.elapsedRealtime = elapsedRealtime;
        this.size = 0;
        if (initialSize > 0) {
            this.capacity = initialSize;
            this.iface = new String[initialSize];
            this.uid = new int[initialSize];
            this.set = new int[initialSize];
            this.tag = new int[initialSize];
            this.metered = new int[initialSize];
            this.roaming = new int[initialSize];
            this.defaultNetwork = new int[initialSize];
            this.rxBytes = new long[initialSize];
            this.rxPackets = new long[initialSize];
            this.txBytes = new long[initialSize];
            this.txPackets = new long[initialSize];
            this.operations = new long[initialSize];
            return;
        }
        clear();
    }

    @UnsupportedAppUsage
    public NetworkStats(Parcel parcel) {
        this.elapsedRealtime = parcel.readLong();
        this.size = parcel.readInt();
        this.capacity = parcel.readInt();
        this.iface = parcel.createStringArray();
        this.uid = parcel.createIntArray();
        this.set = parcel.createIntArray();
        this.tag = parcel.createIntArray();
        this.metered = parcel.createIntArray();
        this.roaming = parcel.createIntArray();
        this.defaultNetwork = parcel.createIntArray();
        this.rxBytes = parcel.createLongArray();
        this.rxPackets = parcel.createLongArray();
        this.txBytes = parcel.createLongArray();
        this.txPackets = parcel.createLongArray();
        this.operations = parcel.createLongArray();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.elapsedRealtime);
        dest.writeInt(this.size);
        dest.writeInt(this.capacity);
        dest.writeStringArray(this.iface);
        dest.writeIntArray(this.uid);
        dest.writeIntArray(this.set);
        dest.writeIntArray(this.tag);
        dest.writeIntArray(this.metered);
        dest.writeIntArray(this.roaming);
        dest.writeIntArray(this.defaultNetwork);
        dest.writeLongArray(this.rxBytes);
        dest.writeLongArray(this.rxPackets);
        dest.writeLongArray(this.txBytes);
        dest.writeLongArray(this.txPackets);
        dest.writeLongArray(this.operations);
    }

    public NetworkStats clone() {
        NetworkStats clone = new NetworkStats(this.elapsedRealtime, this.size);
        Entry entry = null;
        for (int i = 0; i < this.size; i++) {
            entry = getValues(i, entry);
            clone.addValues(entry);
        }
        return clone;
    }

    public void clear() {
        this.capacity = 0;
        this.iface = EmptyArray.STRING;
        this.uid = EmptyArray.INT;
        this.set = EmptyArray.INT;
        this.tag = EmptyArray.INT;
        this.metered = EmptyArray.INT;
        this.roaming = EmptyArray.INT;
        this.defaultNetwork = EmptyArray.INT;
        this.rxBytes = EmptyArray.LONG;
        this.rxPackets = EmptyArray.LONG;
        this.txBytes = EmptyArray.LONG;
        this.txPackets = EmptyArray.LONG;
        this.operations = EmptyArray.LONG;
    }

    @VisibleForTesting
    public NetworkStats addIfaceValues(String iface, long rxBytes, long rxPackets, long txBytes, long txPackets) {
        return addValues(iface, -1, 0, 0, rxBytes, rxPackets, txBytes, txPackets, 0);
    }

    @VisibleForTesting
    public NetworkStats addValues(String iface, int uid, int set, int tag, long rxBytes, long rxPackets, long txBytes, long txPackets, long operations) {
        return addValues(new Entry(iface, uid, set, tag, rxBytes, rxPackets, txBytes, txPackets, operations));
    }

    @VisibleForTesting
    public NetworkStats addValues(String iface, int uid, int set, int tag, int metered, int roaming, int defaultNetwork, long rxBytes, long rxPackets, long txBytes, long txPackets, long operations) {
        Entry entry = r0;
        Entry entry2 = new Entry(iface, uid, set, tag, metered, roaming, defaultNetwork, rxBytes, rxPackets, txBytes, txPackets, operations);
        return addValues(entry);
    }

    public NetworkStats addValues(Entry entry) {
        int i = this.size;
        if (i >= this.capacity) {
            i = (Math.max(i, 10) * 3) / 2;
            this.iface = (String[]) Arrays.copyOf(this.iface, i);
            this.uid = Arrays.copyOf(this.uid, i);
            this.set = Arrays.copyOf(this.set, i);
            this.tag = Arrays.copyOf(this.tag, i);
            this.metered = Arrays.copyOf(this.metered, i);
            this.roaming = Arrays.copyOf(this.roaming, i);
            this.defaultNetwork = Arrays.copyOf(this.defaultNetwork, i);
            this.rxBytes = Arrays.copyOf(this.rxBytes, i);
            this.rxPackets = Arrays.copyOf(this.rxPackets, i);
            this.txBytes = Arrays.copyOf(this.txBytes, i);
            this.txPackets = Arrays.copyOf(this.txPackets, i);
            this.operations = Arrays.copyOf(this.operations, i);
            this.capacity = i;
        }
        setValues(this.size, entry);
        this.size++;
        return this;
    }

    private void setValues(int i, Entry entry) {
        this.iface[i] = entry.iface;
        this.uid[i] = entry.uid;
        this.set[i] = entry.set;
        this.tag[i] = entry.tag;
        this.metered[i] = entry.metered;
        this.roaming[i] = entry.roaming;
        this.defaultNetwork[i] = entry.defaultNetwork;
        this.rxBytes[i] = entry.rxBytes;
        this.rxPackets[i] = entry.rxPackets;
        this.txBytes[i] = entry.txBytes;
        this.txPackets[i] = entry.txPackets;
        this.operations[i] = entry.operations;
    }

    @UnsupportedAppUsage
    public Entry getValues(int i, Entry recycle) {
        Entry entry = recycle != null ? recycle : new Entry();
        entry.iface = this.iface[i];
        entry.uid = this.uid[i];
        entry.set = this.set[i];
        entry.tag = this.tag[i];
        entry.metered = this.metered[i];
        entry.roaming = this.roaming[i];
        entry.defaultNetwork = this.defaultNetwork[i];
        entry.rxBytes = this.rxBytes[i];
        entry.rxPackets = this.rxPackets[i];
        entry.txBytes = this.txBytes[i];
        entry.txPackets = this.txPackets[i];
        entry.operations = this.operations[i];
        return entry;
    }

    private void maybeCopyEntry(int dest, int src) {
        if (dest != src) {
            String[] strArr = this.iface;
            strArr[dest] = strArr[src];
            int[] iArr = this.uid;
            iArr[dest] = iArr[src];
            iArr = this.set;
            iArr[dest] = iArr[src];
            iArr = this.tag;
            iArr[dest] = iArr[src];
            iArr = this.metered;
            iArr[dest] = iArr[src];
            iArr = this.roaming;
            iArr[dest] = iArr[src];
            iArr = this.defaultNetwork;
            iArr[dest] = iArr[src];
            long[] jArr = this.rxBytes;
            jArr[dest] = jArr[src];
            jArr = this.rxPackets;
            jArr[dest] = jArr[src];
            jArr = this.txBytes;
            jArr[dest] = jArr[src];
            jArr = this.txPackets;
            jArr[dest] = jArr[src];
            jArr = this.operations;
            jArr[dest] = jArr[src];
        }
    }

    public long getElapsedRealtime() {
        return this.elapsedRealtime;
    }

    public void setElapsedRealtime(long time) {
        this.elapsedRealtime = time;
    }

    public long getElapsedRealtimeAge() {
        return SystemClock.elapsedRealtime() - this.elapsedRealtime;
    }

    @UnsupportedAppUsage
    public int size() {
        return this.size;
    }

    @VisibleForTesting
    public int internalSize() {
        return this.capacity;
    }

    @Deprecated
    public NetworkStats combineValues(String iface, int uid, int tag, long rxBytes, long rxPackets, long txBytes, long txPackets, long operations) {
        return combineValues(iface, uid, 0, tag, rxBytes, rxPackets, txBytes, txPackets, operations);
    }

    public NetworkStats combineValues(String iface, int uid, int set, int tag, long rxBytes, long rxPackets, long txBytes, long txPackets, long operations) {
        return combineValues(new Entry(iface, uid, set, tag, rxBytes, rxPackets, txBytes, txPackets, operations));
    }

    @UnsupportedAppUsage
    public NetworkStats combineValues(Entry entry) {
        int i = findIndex(entry.iface, entry.uid, entry.set, entry.tag, entry.metered, entry.roaming, entry.defaultNetwork);
        if (i == -1) {
            addValues(entry);
        } else {
            long[] jArr = this.rxBytes;
            jArr[i] = jArr[i] + entry.rxBytes;
            jArr = this.rxPackets;
            jArr[i] = jArr[i] + entry.rxPackets;
            jArr = this.txBytes;
            jArr[i] = jArr[i] + entry.txBytes;
            jArr = this.txPackets;
            jArr[i] = jArr[i] + entry.txPackets;
            jArr = this.operations;
            jArr[i] = jArr[i] + entry.operations;
        }
        return this;
    }

    @UnsupportedAppUsage
    public void combineAllValues(NetworkStats another) {
        Entry entry = null;
        for (int i = 0; i < another.size; i++) {
            entry = another.getValues(i, entry);
            combineValues(entry);
        }
    }

    public int findIndex(String iface, int uid, int set, int tag, int metered, int roaming, int defaultNetwork) {
        int i = 0;
        while (i < this.size) {
            if (uid == this.uid[i] && set == this.set[i] && tag == this.tag[i] && metered == this.metered[i] && roaming == this.roaming[i] && defaultNetwork == this.defaultNetwork[i] && Objects.equals(iface, this.iface[i])) {
                return i;
            }
            i++;
        }
        return -1;
    }

    @VisibleForTesting
    public int findIndexHinted(String iface, int uid, int set, int tag, int metered, int roaming, int defaultNetwork, int hintIndex) {
        int offset = 0;
        while (true) {
            int i = this.size;
            if (offset >= i) {
                return -1;
            }
            int i2;
            int halfOffset = offset / 2;
            if (offset % 2 == 0) {
                i2 = (hintIndex + halfOffset) % i;
            } else {
                i2 = (((i + hintIndex) - halfOffset) - 1) % i;
            }
            if (uid == this.uid[i2] && set == this.set[i2] && tag == this.tag[i2] && metered == this.metered[i2] && roaming == this.roaming[i2] && defaultNetwork == this.defaultNetwork[i2] && Objects.equals(iface, this.iface[i2])) {
                return i2;
            }
            offset++;
        }
    }

    public void spliceOperationsFrom(NetworkStats stats) {
        for (int i = 0; i < this.size; i++) {
            int j = stats.findIndex(this.iface[i], this.uid[i], this.set[i], this.tag[i], this.metered[i], this.roaming[i], this.defaultNetwork[i]);
            if (j == -1) {
                this.operations[i] = 0;
            } else {
                this.operations[i] = stats.operations[j];
            }
        }
    }

    public String[] getUniqueIfaces() {
        HashSet<String> ifaces = new HashSet();
        for (String iface : this.iface) {
            if (iface != IFACE_ALL) {
                ifaces.add(iface);
            }
        }
        return (String[]) ifaces.toArray(new String[ifaces.size()]);
    }

    @UnsupportedAppUsage
    public int[] getUniqueUids() {
        int i;
        SparseBooleanArray uids = new SparseBooleanArray();
        for (int uid : this.uid) {
            uids.put(uid, true);
        }
        int size = uids.size();
        int[] result = new int[size];
        for (i = 0; i < size; i++) {
            result[i] = uids.keyAt(i);
        }
        return result;
    }

    @UnsupportedAppUsage
    public long getTotalBytes() {
        Entry entry = getTotal(null);
        return entry.rxBytes + entry.txBytes;
    }

    @UnsupportedAppUsage
    public Entry getTotal(Entry recycle) {
        return getTotal(recycle, null, -1, false);
    }

    @UnsupportedAppUsage
    public Entry getTotal(Entry recycle, int limitUid) {
        return getTotal(recycle, null, limitUid, false);
    }

    public Entry getTotal(Entry recycle, HashSet<String> limitIface) {
        return getTotal(recycle, limitIface, -1, false);
    }

    @UnsupportedAppUsage
    public Entry getTotalIncludingTags(Entry recycle) {
        return getTotal(recycle, null, -1, true);
    }

    private Entry getTotal(Entry recycle, HashSet<String> limitIface, int limitUid, boolean includeTags) {
        Entry entry = recycle != null ? recycle : new Entry();
        entry.iface = IFACE_ALL;
        entry.uid = limitUid;
        entry.set = -1;
        entry.tag = 0;
        entry.metered = -1;
        entry.roaming = -1;
        entry.defaultNetwork = -1;
        entry.rxBytes = 0;
        entry.rxPackets = 0;
        entry.txBytes = 0;
        entry.txPackets = 0;
        entry.operations = 0;
        int i = 0;
        while (i < this.size) {
            boolean matchesIface = true;
            boolean matchesUid = limitUid == -1 || limitUid == this.uid[i];
            if (!(limitIface == null || limitIface.contains(this.iface[i]))) {
                matchesIface = false;
            }
            if (matchesUid && matchesIface && (this.tag[i] == 0 || includeTags)) {
                entry.rxBytes += this.rxBytes[i];
                entry.rxPackets += this.rxPackets[i];
                entry.txBytes += this.txBytes[i];
                entry.txPackets += this.txPackets[i];
                entry.operations += this.operations[i];
            }
            i++;
        }
        return entry;
    }

    public long getTotalPackets() {
        long total = 0;
        for (int i = this.size - 1; i >= 0; i--) {
            total += this.rxPackets[i] + this.txPackets[i];
        }
        return total;
    }

    public NetworkStats subtract(NetworkStats right) {
        return subtract(this, right, null, null);
    }

    public static <C> NetworkStats subtract(NetworkStats left, NetworkStats right, NonMonotonicObserver<C> observer, C cookie) {
        return subtract(left, right, observer, cookie, null);
    }

    public static <C> NetworkStats subtract(NetworkStats left, NetworkStats right, NonMonotonicObserver<C> observer, C cookie, NetworkStats recycle) {
        long deltaRealtime;
        NetworkStats result;
        long deltaRealtime2;
        NetworkStats networkStats;
        NetworkStats networkStats2 = left;
        NetworkStats networkStats3 = right;
        NetworkStats networkStats4 = recycle;
        long deltaRealtime3 = networkStats2.elapsedRealtime - networkStats3.elapsedRealtime;
        if (deltaRealtime3 < 0) {
            if (observer != null) {
                observer.foundNonMonotonic(left, -1, right, -1, cookie);
            }
            deltaRealtime = 0;
        } else {
            deltaRealtime = deltaRealtime3;
        }
        Entry entry = new Entry();
        if (networkStats4 == null || networkStats4.capacity < networkStats2.size) {
            result = new NetworkStats(deltaRealtime, networkStats2.size);
        } else {
            NetworkStats result2 = recycle;
            result2.size = 0;
            result2.elapsedRealtime = deltaRealtime;
            result = result2;
        }
        int i = 0;
        boolean needDump = false;
        while (i < networkStats2.size) {
            long j;
            entry.iface = networkStats2.iface[i];
            entry.uid = networkStats2.uid[i];
            entry.set = networkStats2.set[i];
            entry.tag = networkStats2.tag[i];
            entry.metered = networkStats2.metered[i];
            entry.roaming = networkStats2.roaming[i];
            entry.defaultNetwork = networkStats2.defaultNetwork[i];
            entry.rxBytes = networkStats2.rxBytes[i];
            entry.rxPackets = networkStats2.rxPackets[i];
            entry.txBytes = networkStats2.txBytes[i];
            entry.txPackets = networkStats2.txPackets[i];
            entry.operations = networkStats2.operations[i];
            deltaRealtime2 = deltaRealtime;
            networkStats = networkStats3;
            int j2 = right.findIndexHinted(entry.iface, entry.uid, entry.set, entry.tag, entry.metered, entry.roaming, entry.defaultNetwork, i);
            if (j2 != -1) {
                entry.rxBytes -= networkStats.rxBytes[j2];
                entry.rxPackets -= networkStats.rxPackets[j2];
                entry.txBytes -= networkStats.txBytes[j2];
                entry.txPackets -= networkStats.txPackets[j2];
                entry.operations -= networkStats.operations[j2];
            }
            if (entry.isNegative() != null) {
                j = 0;
                entry.rxBytes = Math.max(entry.rxBytes, 0);
                entry.rxPackets = Math.max(entry.rxPackets, 0);
                entry.txBytes = Math.max(entry.txBytes, 0);
                entry.txPackets = Math.max(entry.txPackets, 0);
                entry.operations = Math.max(entry.operations, 0);
                needDump = 1;
            } else {
                j = 0;
            }
            result.addValues(entry);
            i++;
            networkStats4 = recycle;
            networkStats3 = networkStats;
            long j3 = j;
            deltaRealtime = deltaRealtime2;
        }
        deltaRealtime2 = deltaRealtime;
        networkStats = networkStats3;
        if (!needDump || observer == null) {
            Entry entry2 = entry;
            long j4 = deltaRealtime2;
            return result;
        }
        NetworkStats result3 = result;
        observer.foundNonMonotonic(left, -1, right, -1, cookie);
        return result3;
    }

    public static void apply464xlatAdjustments(NetworkStats baseTraffic, NetworkStats stackedTraffic, Map<String, String> stackedIfaces, boolean useBpfStats) {
        Map<String, String> map;
        NetworkStats networkStats = baseTraffic;
        NetworkStats networkStats2 = stackedTraffic;
        NetworkStats adjustments = new NetworkStats(0, stackedIfaces.size());
        Entry entry = null;
        Entry adjust = new Entry(IFACE_ALL, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        for (int i = 0; i < networkStats2.size; i++) {
            entry = networkStats2.getValues(i, entry);
            if (entry.iface == null) {
                map = stackedIfaces;
            } else if (entry.iface.startsWith(CLATD_INTERFACE_PREFIX)) {
                String baseIface = (String) stackedIfaces.get(entry.iface);
                if (baseIface != null) {
                    adjust.iface = baseIface;
                    if (!useBpfStats) {
                        adjust.rxBytes = -(entry.rxBytes + (entry.rxPackets * 20));
                        adjust.rxPackets = -entry.rxPackets;
                    }
                    adjustments.combineValues(adjust);
                    entry.rxBytes += entry.rxPackets * 20;
                    entry.txBytes += entry.txPackets * 20;
                    networkStats2.setValues(i, entry);
                }
            } else {
                map = stackedIfaces;
            }
        }
        map = stackedIfaces;
        networkStats.removeUids(new int[]{1029});
        networkStats.combineAllValues(adjustments);
    }

    public void apply464xlatAdjustments(Map<String, String> stackedIfaces, boolean useBpfStats) {
        apply464xlatAdjustments(this, this, stackedIfaces, useBpfStats);
    }

    public NetworkStats groupedByIface() {
        NetworkStats stats = new NetworkStats(this.elapsedRealtime, 10);
        Entry entry = new Entry();
        entry.uid = -1;
        entry.set = -1;
        entry.tag = 0;
        entry.metered = -1;
        entry.roaming = -1;
        entry.defaultNetwork = -1;
        entry.operations = 0;
        for (int i = 0; i < this.size; i++) {
            if (this.tag[i] == 0) {
                entry.iface = this.iface[i];
                entry.rxBytes = this.rxBytes[i];
                entry.rxPackets = this.rxPackets[i];
                entry.txBytes = this.txBytes[i];
                entry.txPackets = this.txPackets[i];
                stats.combineValues(entry);
            }
        }
        return stats;
    }

    public NetworkStats groupedByUid() {
        NetworkStats stats = new NetworkStats(this.elapsedRealtime, 10);
        Entry entry = new Entry();
        entry.iface = IFACE_ALL;
        entry.set = -1;
        entry.tag = 0;
        entry.metered = -1;
        entry.roaming = -1;
        entry.defaultNetwork = -1;
        for (int i = 0; i < this.size; i++) {
            if (this.tag[i] == 0) {
                entry.uid = this.uid[i];
                entry.rxBytes = this.rxBytes[i];
                entry.rxPackets = this.rxPackets[i];
                entry.txBytes = this.txBytes[i];
                entry.txPackets = this.txPackets[i];
                entry.operations = this.operations[i];
                stats.combineValues(entry);
            }
        }
        return stats;
    }

    public void removeUids(int[] uids) {
        int nextOutputEntry = 0;
        for (int i = 0; i < this.size; i++) {
            if (!ArrayUtils.contains(uids, this.uid[i])) {
                maybeCopyEntry(nextOutputEntry, i);
                nextOutputEntry++;
            }
        }
        this.size = nextOutputEntry;
    }

    public void filter(int limitUid, String[] limitIfaces, int limitTag) {
        if (limitUid != -1 || limitTag != -1 || limitIfaces != INTERFACES_ALL) {
            Entry entry = new Entry();
            int nextOutputEntry = 0;
            for (int i = 0; i < this.size; i++) {
                entry = getValues(i, entry);
                boolean matches = (limitUid == -1 || limitUid == entry.uid) && ((limitTag == -1 || limitTag == entry.tag) && (limitIfaces == INTERFACES_ALL || ArrayUtils.contains((Object[]) limitIfaces, entry.iface)));
                if (matches) {
                    setValues(nextOutputEntry, entry);
                    nextOutputEntry++;
                }
            }
            this.size = nextOutputEntry;
        }
    }

    public void dump(String prefix, PrintWriter pw) {
        pw.print(prefix);
        pw.print("NetworkStats: elapsedRealtime=");
        pw.println(this.elapsedRealtime);
        for (int i = 0; i < this.size; i++) {
            pw.print(prefix);
            pw.print("  [");
            pw.print(i);
            pw.print("]");
            pw.print(" iface=");
            pw.print(this.iface[i]);
            pw.print(" uid=");
            pw.print(this.uid[i]);
            pw.print(" set=");
            pw.print(setToString(this.set[i]));
            pw.print(" tag=");
            pw.print(tagToString(this.tag[i]));
            pw.print(" metered=");
            pw.print(meteredToString(this.metered[i]));
            pw.print(" roaming=");
            pw.print(roamingToString(this.roaming[i]));
            pw.print(" defaultNetwork=");
            pw.print(defaultNetworkToString(this.defaultNetwork[i]));
            pw.print(" rxBytes=");
            pw.print(this.rxBytes[i]);
            pw.print(" rxPackets=");
            pw.print(this.rxPackets[i]);
            pw.print(" txBytes=");
            pw.print(this.txBytes[i]);
            pw.print(" txPackets=");
            pw.print(this.txPackets[i]);
            pw.print(" operations=");
            pw.println(this.operations[i]);
        }
    }

    public static String setToString(int set) {
        if (set == -1) {
            return "ALL";
        }
        if (set == 0) {
            return "DEFAULT";
        }
        if (set == 1) {
            return "FOREGROUND";
        }
        if (set == 1001) {
            return "DBG_VPN_IN";
        }
        if (set != 1002) {
            return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
        }
        return "DBG_VPN_OUT";
    }

    public static String setToCheckinString(int set) {
        if (set == -1) {
            return "all";
        }
        if (set == 0) {
            return "def";
        }
        if (set == 1) {
            return "fg";
        }
        if (set == 1001) {
            return "vpnin";
        }
        if (set != 1002) {
            return "unk";
        }
        return "vpnout";
    }

    public static boolean setMatches(int querySet, int dataSet) {
        boolean z = true;
        if (querySet == dataSet) {
            return true;
        }
        if (querySet != -1 || dataSet >= 1000) {
            z = false;
        }
        return z;
    }

    public static String tagToString(int tag) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("0x");
        stringBuilder.append(Integer.toHexString(tag));
        return stringBuilder.toString();
    }

    public static String meteredToString(int metered) {
        if (metered == -1) {
            return "ALL";
        }
        if (metered == 0) {
            return "NO";
        }
        if (metered != 1) {
            return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
        }
        return "YES";
    }

    public static String roamingToString(int roaming) {
        if (roaming == -1) {
            return "ALL";
        }
        if (roaming == 0) {
            return "NO";
        }
        if (roaming != 1) {
            return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
        }
        return "YES";
    }

    public static String defaultNetworkToString(int defaultNetwork) {
        if (defaultNetwork == -1) {
            return "ALL";
        }
        if (defaultNetwork == 0) {
            return "NO";
        }
        if (defaultNetwork != 1) {
            return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
        }
        return "YES";
    }

    public String toString() {
        CharArrayWriter writer = new CharArrayWriter();
        dump("", new PrintWriter(writer));
        return writer.toString();
    }

    public int describeContents() {
        return 0;
    }

    public boolean migrateTun(int tunUid, String tunIface, String underlyingIface) {
        Entry tunIfaceTotal = new Entry();
        Entry underlyingIfaceTotal = new Entry();
        tunAdjustmentInit(tunUid, tunIface, underlyingIface, tunIfaceTotal, underlyingIfaceTotal);
        Entry pool = tunGetPool(tunIfaceTotal, underlyingIfaceTotal);
        if (pool.isEmpty()) {
            return true;
        }
        Entry moved = addTrafficToApplications(tunUid, tunIface, underlyingIface, tunIfaceTotal, pool);
        deductTrafficFromVpnApp(tunUid, underlyingIface, moved);
        if (moved.isEmpty()) {
            return true;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Failed to deduct underlying network traffic from VPN package. Moved=");
        stringBuilder.append(moved);
        Slog.wtf(TAG, stringBuilder.toString());
        return false;
    }

    private void tunAdjustmentInit(int tunUid, String tunIface, String underlyingIface, Entry tunIfaceTotal, Entry underlyingIfaceTotal) {
        Entry recycle = new Entry();
        int i = 0;
        while (i < this.size) {
            getValues(i, recycle);
            if (recycle.uid == -1) {
                throw new IllegalStateException("Cannot adjust VPN accounting on an iface aggregated NetworkStats.");
            } else if (recycle.set == 1001 || recycle.set == 1002) {
                throw new IllegalStateException("Cannot adjust VPN accounting on a NetworkStats containing SET_DBG_VPN_*");
            } else {
                if (recycle.uid == tunUid && recycle.tag == 0 && Objects.equals(underlyingIface, recycle.iface)) {
                    underlyingIfaceTotal.add(recycle);
                }
                if (recycle.uid != tunUid && recycle.tag == 0 && Objects.equals(tunIface, recycle.iface)) {
                    tunIfaceTotal.add(recycle);
                }
                i++;
            }
        }
    }

    private static Entry tunGetPool(Entry tunIfaceTotal, Entry underlyingIfaceTotal) {
        Entry pool = new Entry();
        pool.rxBytes = Math.min(tunIfaceTotal.rxBytes, underlyingIfaceTotal.rxBytes);
        pool.rxPackets = Math.min(tunIfaceTotal.rxPackets, underlyingIfaceTotal.rxPackets);
        pool.txBytes = Math.min(tunIfaceTotal.txBytes, underlyingIfaceTotal.txBytes);
        pool.txPackets = Math.min(tunIfaceTotal.txPackets, underlyingIfaceTotal.txPackets);
        pool.operations = Math.min(tunIfaceTotal.operations, underlyingIfaceTotal.operations);
        return pool;
    }

    private Entry addTrafficToApplications(int tunUid, String tunIface, String underlyingIface, Entry tunIfaceTotal, Entry pool) {
        Entry moved = new Entry();
        Entry tmpEntry = new Entry();
        tmpEntry.iface = underlyingIface;
        int i = 0;
        while (i < this.size) {
            if (Objects.equals(this.iface[i], tunIface) && this.uid[i] != tunUid) {
                if (tunIfaceTotal.rxBytes > 0) {
                    tmpEntry.rxBytes = (pool.rxBytes * this.rxBytes[i]) / tunIfaceTotal.rxBytes;
                } else {
                    tmpEntry.rxBytes = 0;
                }
                if (tunIfaceTotal.rxPackets > 0) {
                    tmpEntry.rxPackets = (pool.rxPackets * this.rxPackets[i]) / tunIfaceTotal.rxPackets;
                } else {
                    tmpEntry.rxPackets = 0;
                }
                if (tunIfaceTotal.txBytes > 0) {
                    tmpEntry.txBytes = (pool.txBytes * this.txBytes[i]) / tunIfaceTotal.txBytes;
                } else {
                    tmpEntry.txBytes = 0;
                }
                if (tunIfaceTotal.txPackets > 0) {
                    tmpEntry.txPackets = (pool.txPackets * this.txPackets[i]) / tunIfaceTotal.txPackets;
                } else {
                    tmpEntry.txPackets = 0;
                }
                if (tunIfaceTotal.operations > 0) {
                    tmpEntry.operations = (pool.operations * this.operations[i]) / tunIfaceTotal.operations;
                } else {
                    tmpEntry.operations = 0;
                }
                tmpEntry.uid = this.uid[i];
                tmpEntry.tag = this.tag[i];
                tmpEntry.set = this.set[i];
                tmpEntry.metered = this.metered[i];
                tmpEntry.roaming = this.roaming[i];
                tmpEntry.defaultNetwork = this.defaultNetwork[i];
                combineValues(tmpEntry);
                if (this.tag[i] == 0) {
                    moved.add(tmpEntry);
                    tmpEntry.set = 1001;
                    combineValues(tmpEntry);
                }
            }
            i++;
        }
        return moved;
    }

    public static long multiplySafe(long value, long num, long den) {
        long den2 = den == 0 ? 1 : den;
        long x = value;
        long y = num;
        long r = x * y;
        long j;
        if (((Math.abs(x) | Math.abs(y)) >>> 31) == 0) {
            j = value;
            den = x;
        } else if ((y == 0 || r / y == x) && !(x == Long.MIN_VALUE && y == -1)) {
            j = value;
            den = x;
        } else {
            return (long) ((((double) num) / ((double) den2)) * ((double) value));
        }
        return r / den2;
    }

    private void deductTrafficFromVpnApp(int tunUid, String underlyingIface, Entry moved) {
        moved.uid = tunUid;
        moved.set = 1002;
        moved.tag = 0;
        moved.iface = underlyingIface;
        moved.metered = -1;
        moved.roaming = -1;
        moved.defaultNetwork = -1;
        combineValues(moved);
        int idxVpnBackground = findIndex(underlyingIface, tunUid, 0, 0, 0, 0, 0);
        if (idxVpnBackground != -1) {
            tunSubtract(idxVpnBackground, this, moved);
        }
        int idxVpnForeground = findIndex(underlyingIface, tunUid, 1, 0, 0, 0, 0);
        if (idxVpnForeground != -1) {
            tunSubtract(idxVpnForeground, this, moved);
        }
    }

    private static void tunSubtract(int i, NetworkStats left, Entry right) {
        long rxBytes = Math.min(left.rxBytes[i], right.rxBytes);
        long[] jArr = left.rxBytes;
        jArr[i] = jArr[i] - rxBytes;
        right.rxBytes -= rxBytes;
        long rxPackets = Math.min(left.rxPackets[i], right.rxPackets);
        long[] jArr2 = left.rxPackets;
        jArr2[i] = jArr2[i] - rxPackets;
        right.rxPackets -= rxPackets;
        long txBytes = Math.min(left.txBytes[i], right.txBytes);
        long[] jArr3 = left.txBytes;
        jArr3[i] = jArr3[i] - txBytes;
        right.txBytes -= txBytes;
        long txPackets = Math.min(left.txPackets[i], right.txPackets);
        long[] jArr4 = left.txPackets;
        jArr4[i] = jArr4[i] - txPackets;
        right.txPackets -= txPackets;
    }
}
