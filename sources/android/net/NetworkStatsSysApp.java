package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import com.android.internal.annotations.VisibleForTesting;
import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Objects;
import libcore.util.EmptyArray;

public class NetworkStatsSysApp implements Parcelable {
    public static final Creator<NetworkStatsSysApp> CREATOR = new Creator<NetworkStatsSysApp>() {
        public NetworkStatsSysApp createFromParcel(Parcel in) {
            return new NetworkStatsSysApp(in);
        }

        public NetworkStatsSysApp[] newArray(int size) {
            return new NetworkStatsSysApp[size];
        }
    };
    public static final String IFACE_ALL = null;
    public static final String PACKAGE_DEFAULT = "";
    private int capacity;
    private long elapsedRealtime;
    private String[] iface;
    private String[] nameOrHashs;
    private long[] rxBytes;
    private long[] rxPackets;
    private int size;
    private long[] txBytes;
    private long[] txPackets;

    public static class Entry {
        public String iface;
        public String nameOrHash;
        public long rxBytes;
        public long rxPackets;
        public long txBytes;
        public long txPackets;

        public Entry() {
            this(NetworkStatsSysApp.IFACE_ALL, "", 0, 0, 0, 0);
        }

        public Entry(String iface, String nameOrHash, long rxBytes, long rxPackets, long txBytes, long txPackets) {
            this.iface = iface;
            this.nameOrHash = nameOrHash;
            this.rxPackets = rxPackets;
            this.txBytes = txBytes;
            this.rxBytes = rxBytes;
            this.txPackets = txPackets;
        }

        public boolean isNegative() {
            return this.rxBytes < 0 || this.rxPackets < 0 || this.txBytes < 0 || this.txPackets < 0;
        }

        public boolean isEmpty() {
            return this.rxBytes == 0 && this.rxPackets == 0 && this.txBytes == 0 && this.txPackets == 0;
        }

        public void add(Entry another) {
            this.rxBytes += another.rxBytes;
            this.rxPackets += another.rxPackets;
            this.txBytes += another.txBytes;
            this.txPackets += another.txPackets;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("iface=");
            builder.append(this.iface);
            builder.append("nameOrHash=");
            builder.append(this.nameOrHash);
            builder.append(" rxBytes=");
            builder.append(this.rxBytes);
            builder.append(" rxPackets=");
            builder.append(this.rxPackets);
            builder.append(" txBytes=");
            builder.append(this.txBytes);
            builder.append(" txPackets=");
            builder.append(this.txPackets);
            return builder.toString();
        }

        public boolean equals(Object o) {
            boolean z = false;
            if (!(o instanceof Entry)) {
                return false;
            }
            Entry e = (Entry) o;
            if (this.rxBytes == e.rxBytes && this.rxPackets == e.rxPackets && this.txBytes == e.txBytes && this.txPackets == e.txPackets && this.iface.equals(e.iface) && this.nameOrHash.equals(e.nameOrHash)) {
                z = true;
            }
            return z;
        }

        public int hashCode() {
            return Objects.hash(new Object[]{this.iface, this.nameOrHash});
        }
    }

    public NetworkStatsSysApp(long elapsedRealtime, int initialSize) {
        this.elapsedRealtime = elapsedRealtime;
        this.size = 0;
        if (initialSize >= 0) {
            this.capacity = initialSize;
            this.iface = new String[initialSize];
            this.nameOrHashs = new String[initialSize];
            this.rxBytes = new long[initialSize];
            this.rxPackets = new long[initialSize];
            this.txBytes = new long[initialSize];
            this.txPackets = new long[initialSize];
            return;
        }
        this.capacity = 0;
        this.iface = EmptyArray.STRING;
        this.nameOrHashs = EmptyArray.STRING;
        this.rxBytes = EmptyArray.LONG;
        this.rxPackets = EmptyArray.LONG;
        this.txBytes = EmptyArray.LONG;
        this.txPackets = EmptyArray.LONG;
    }

    public NetworkStatsSysApp(Parcel parcel) {
        this.elapsedRealtime = parcel.readLong();
        this.size = parcel.readInt();
        this.capacity = parcel.readInt();
        this.iface = parcel.createStringArray();
        this.nameOrHashs = parcel.createStringArray();
        this.rxBytes = parcel.createLongArray();
        this.rxPackets = parcel.createLongArray();
        this.txBytes = parcel.createLongArray();
        this.txPackets = parcel.createLongArray();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.elapsedRealtime);
        dest.writeInt(this.size);
        dest.writeInt(this.capacity);
        dest.writeStringArray(this.iface);
        dest.writeStringArray(this.nameOrHashs);
        dest.writeLongArray(this.rxBytes);
        dest.writeLongArray(this.rxPackets);
        dest.writeLongArray(this.txBytes);
        dest.writeLongArray(this.txPackets);
    }

    public NetworkStatsSysApp addValues(Entry entry) {
        int i = this.size;
        if (i >= this.capacity) {
            i = (Math.max(i, 10) * 3) / 2;
            this.iface = (String[]) Arrays.copyOf(this.iface, i);
            this.nameOrHashs = (String[]) Arrays.copyOf(this.nameOrHashs, i);
            this.rxBytes = Arrays.copyOf(this.rxBytes, i);
            this.rxPackets = Arrays.copyOf(this.rxPackets, i);
            this.txBytes = Arrays.copyOf(this.txBytes, i);
            this.txPackets = Arrays.copyOf(this.txPackets, i);
            this.capacity = i;
        }
        this.iface[this.size] = entry.iface;
        this.nameOrHashs[this.size] = entry.nameOrHash;
        this.rxBytes[this.size] = entry.rxBytes;
        this.rxPackets[this.size] = entry.rxPackets;
        this.txBytes[this.size] = entry.txBytes;
        this.txPackets[this.size] = entry.txPackets;
        this.size++;
        return this;
    }

    public Entry getValues(int i, Entry recycle) {
        Entry entry = recycle != null ? recycle : new Entry();
        entry.iface = this.iface[i];
        entry.nameOrHash = this.nameOrHashs[i];
        entry.rxBytes = this.rxBytes[i];
        entry.rxPackets = this.rxPackets[i];
        entry.txBytes = this.txBytes[i];
        entry.txPackets = this.txPackets[i];
        return entry;
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

    public int size() {
        return this.size;
    }

    @VisibleForTesting
    public int internalSize() {
        return this.capacity;
    }

    public NetworkStatsSysApp combineValues(Entry entry) {
        int i = findIndex(entry.iface, entry.nameOrHash);
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
        }
        return this;
    }

    public int findIndex(String iface, String pname) {
        int i = 0;
        while (i < this.size) {
            if (Objects.equals(iface, this.iface[i]) && Objects.equals(pname, this.nameOrHashs[i])) {
                return i;
            }
            i++;
        }
        return -1;
    }

    @VisibleForTesting
    public int findIndexHinted(String iface, String nameOrHash, int hintIndex) {
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
            if (Objects.equals(iface, this.iface[i2]) && Objects.equals(nameOrHash, this.nameOrHashs[i2])) {
                return i2;
            }
            offset++;
        }
    }

    public long getTotalPackets() {
        long total = 0;
        for (int i = this.size - 1; i >= 0; i--) {
            total += this.rxPackets[i] + this.txPackets[i];
        }
        return total;
    }

    public long getTotalRxBytes() {
        long total = 0;
        for (int i = this.size - 1; i >= 0; i--) {
            total += this.rxBytes[i];
        }
        return total;
    }

    public long getTotalTxBytes() {
        long total = 0;
        for (int i = this.size - 1; i >= 0; i--) {
            total += this.txBytes[i];
        }
        return total;
    }

    public static NetworkStatsSysApp subtract(NetworkStatsSysApp left, NetworkStatsSysApp right, NetworkStatsSysApp recycle) {
        NetworkStatsSysApp result;
        long deltaRealtime = left.elapsedRealtime - right.elapsedRealtime;
        if (deltaRealtime < 0) {
            deltaRealtime = 0;
        }
        Entry entry = new Entry();
        if (recycle == null || recycle.capacity < left.size) {
            result = new NetworkStatsSysApp(deltaRealtime, left.size);
        } else {
            result = recycle;
            result.size = 0;
            result.elapsedRealtime = deltaRealtime;
        }
        for (int i = 0; i < left.size; i++) {
            entry.iface = left.iface[i];
            entry.nameOrHash = left.nameOrHashs[i];
            int j = right.findIndexHinted(entry.iface, entry.nameOrHash, i);
            if (j == -1) {
                entry.rxBytes = left.rxBytes[i];
                entry.rxPackets = left.rxPackets[i];
                entry.txBytes = left.txBytes[i];
                entry.txPackets = left.txPackets[i];
            } else {
                entry.rxBytes = left.rxBytes[i] - right.rxBytes[j];
                entry.rxPackets = left.rxPackets[i] - right.rxPackets[j];
                entry.txBytes = left.txBytes[i] - right.txBytes[j];
                entry.txPackets = left.txPackets[i] - right.txPackets[j];
                if (entry.rxBytes < 0 || entry.rxPackets < 0 || entry.txBytes < 0 || entry.txPackets < 0) {
                    entry.rxBytes = Math.max(entry.rxBytes, 0);
                    entry.rxPackets = Math.max(entry.rxPackets, 0);
                    entry.txBytes = Math.max(entry.txBytes, 0);
                    entry.txPackets = Math.max(entry.txPackets, 0);
                }
            }
            result.addValues(entry);
        }
        return result;
    }

    public void dump(String prefix, PrintWriter pw) {
        pw.print(prefix);
        pw.print("NetworkStatsSysApp: elapsedRealtime=");
        pw.println(this.elapsedRealtime);
        for (int i = 0; i < this.size; i++) {
            pw.print(prefix);
            pw.print("  [");
            pw.print(i);
            pw.print("]");
            pw.print(" iface=");
            pw.print(this.iface[i]);
            pw.print(" nameOrHashs=");
            pw.print(this.nameOrHashs[i]);
            pw.print(" rxBytes=");
            pw.print(this.rxBytes[i]);
            pw.print(" rxPackets=");
            pw.print(this.rxPackets[i]);
            pw.print(" txBytes=");
            pw.print(this.txBytes[i]);
            pw.print(" txPackets=");
            pw.print(this.txPackets[i]);
        }
    }

    public String toString() {
        CharArrayWriter writer = new CharArrayWriter();
        dump("", new PrintWriter(writer));
        return writer.toString();
    }

    public int describeContents() {
        return 0;
    }
}
