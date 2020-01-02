package android.net;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.MathUtils;
import android.util.proto.ProtoOutputStream;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.IndentingPrintWriter;
import java.io.CharArrayWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ProtocolException;
import java.util.Arrays;
import java.util.Random;
import libcore.util.EmptyArray;

public class NetworkStatsHistory implements Parcelable {
    @UnsupportedAppUsage
    public static final Creator<NetworkStatsHistory> CREATOR = new Creator<NetworkStatsHistory>() {
        public NetworkStatsHistory createFromParcel(Parcel in) {
            return new NetworkStatsHistory(in);
        }

        public NetworkStatsHistory[] newArray(int size) {
            return new NetworkStatsHistory[size];
        }
    };
    public static final int FIELD_ACTIVE_TIME = 1;
    public static final int FIELD_ALL = -1;
    public static final int FIELD_OPERATIONS = 32;
    public static final int FIELD_RX_BYTES = 2;
    public static final int FIELD_RX_PACKETS = 4;
    public static final int FIELD_TX_BYTES = 8;
    public static final int FIELD_TX_PACKETS = 16;
    private static final int VERSION_ADD_ACTIVE = 3;
    private static final int VERSION_ADD_PACKETS = 2;
    private static final int VERSION_INIT = 1;
    private long[] activeTime;
    private int bucketCount;
    private long bucketDuration;
    private long[] bucketStart;
    private long[] operations;
    private long[] rxBytes;
    private long[] rxPackets;
    private long totalBytes;
    private long[] txBytes;
    private long[] txPackets;

    public static class DataStreamUtils {
        @Deprecated
        public static long[] readFullLongArray(DataInputStream in) throws IOException {
            int size = in.readInt();
            if (size >= 0) {
                long[] values = new long[size];
                for (int i = 0; i < values.length; i++) {
                    values[i] = in.readLong();
                }
                return values;
            }
            throw new ProtocolException("negative array size");
        }

        public static long readVarLong(DataInputStream in) throws IOException {
            long result = 0;
            for (int shift = 0; shift < 64; shift += 7) {
                byte b = in.readByte();
                result |= ((long) (b & 127)) << shift;
                if ((b & 128) == 0) {
                    return result;
                }
            }
            throw new ProtocolException("malformed long");
        }

        public static void writeVarLong(DataOutputStream out, long value) throws IOException {
            while ((-128 & value) != 0) {
                out.writeByte((((int) value) & 127) | 128);
                value >>>= 7;
            }
            out.writeByte((int) value);
        }

        public static long[] readVarLongArray(DataInputStream in) throws IOException {
            int size = in.readInt();
            if (size == -1) {
                return null;
            }
            if (size >= 0) {
                long[] values = new long[size];
                for (int i = 0; i < values.length; i++) {
                    values[i] = readVarLong(in);
                }
                return values;
            }
            throw new ProtocolException("negative array size");
        }

        public static void writeVarLongArray(DataOutputStream out, long[] values, int size) throws IOException {
            if (values == null) {
                out.writeInt(-1);
            } else if (size <= values.length) {
                out.writeInt(size);
                for (int i = 0; i < size; i++) {
                    writeVarLong(out, values[i]);
                }
            } else {
                throw new IllegalArgumentException("size larger than length");
            }
        }
    }

    public static class Entry {
        public static final long UNKNOWN = -1;
        public long activeTime;
        @UnsupportedAppUsage
        public long bucketDuration;
        @UnsupportedAppUsage
        public long bucketStart;
        public long operations;
        @UnsupportedAppUsage
        public long rxBytes;
        @UnsupportedAppUsage
        public long rxPackets;
        @UnsupportedAppUsage
        public long txBytes;
        @UnsupportedAppUsage
        public long txPackets;
    }

    public static class ParcelUtils {
        public static long[] readLongArray(Parcel in) {
            int size = in.readInt();
            if (size == -1) {
                return null;
            }
            long[] values = new long[size];
            for (int i = 0; i < values.length; i++) {
                values[i] = in.readLong();
            }
            return values;
        }

        public static void writeLongArray(Parcel out, long[] values, int size) {
            if (values == null) {
                out.writeInt(-1);
            } else if (size <= values.length) {
                out.writeInt(size);
                for (int i = 0; i < size; i++) {
                    out.writeLong(values[i]);
                }
            } else {
                throw new IllegalArgumentException("size larger than length");
            }
        }
    }

    @UnsupportedAppUsage
    public NetworkStatsHistory(long bucketDuration) {
        this(bucketDuration, 10, -1);
    }

    public NetworkStatsHistory(long bucketDuration, int initialSize) {
        this(bucketDuration, initialSize, -1);
    }

    public NetworkStatsHistory(long bucketDuration, int initialSize, int fields) {
        this.bucketDuration = bucketDuration;
        this.bucketStart = new long[initialSize];
        if ((fields & 1) != 0) {
            this.activeTime = new long[initialSize];
        }
        if ((fields & 2) != 0) {
            this.rxBytes = new long[initialSize];
        }
        if ((fields & 4) != 0) {
            this.rxPackets = new long[initialSize];
        }
        if ((fields & 8) != 0) {
            this.txBytes = new long[initialSize];
        }
        if ((fields & 16) != 0) {
            this.txPackets = new long[initialSize];
        }
        if ((fields & 32) != 0) {
            this.operations = new long[initialSize];
        }
        this.bucketCount = 0;
        this.totalBytes = 0;
    }

    public NetworkStatsHistory(NetworkStatsHistory existing, long bucketDuration) {
        this(bucketDuration, existing.estimateResizeBuckets(bucketDuration));
        recordEntireHistory(existing);
    }

    @UnsupportedAppUsage
    public NetworkStatsHistory(Parcel in) {
        this.bucketDuration = in.readLong();
        this.bucketStart = ParcelUtils.readLongArray(in);
        this.activeTime = ParcelUtils.readLongArray(in);
        this.rxBytes = ParcelUtils.readLongArray(in);
        this.rxPackets = ParcelUtils.readLongArray(in);
        this.txBytes = ParcelUtils.readLongArray(in);
        this.txPackets = ParcelUtils.readLongArray(in);
        this.operations = ParcelUtils.readLongArray(in);
        this.bucketCount = this.bucketStart.length;
        this.totalBytes = in.readLong();
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.bucketDuration);
        ParcelUtils.writeLongArray(out, this.bucketStart, this.bucketCount);
        ParcelUtils.writeLongArray(out, this.activeTime, this.bucketCount);
        ParcelUtils.writeLongArray(out, this.rxBytes, this.bucketCount);
        ParcelUtils.writeLongArray(out, this.rxPackets, this.bucketCount);
        ParcelUtils.writeLongArray(out, this.txBytes, this.bucketCount);
        ParcelUtils.writeLongArray(out, this.txPackets, this.bucketCount);
        ParcelUtils.writeLongArray(out, this.operations, this.bucketCount);
        out.writeLong(this.totalBytes);
    }

    public NetworkStatsHistory(DataInputStream in) throws IOException {
        int version = in.readInt();
        long[] jArr;
        if (version == 1) {
            this.bucketDuration = in.readLong();
            this.bucketStart = DataStreamUtils.readFullLongArray(in);
            this.rxBytes = DataStreamUtils.readFullLongArray(in);
            this.rxPackets = new long[this.bucketStart.length];
            this.txBytes = DataStreamUtils.readFullLongArray(in);
            jArr = this.bucketStart;
            this.txPackets = new long[jArr.length];
            this.operations = new long[jArr.length];
            this.bucketCount = jArr.length;
            this.totalBytes = ArrayUtils.total(this.rxBytes) + ArrayUtils.total(this.txBytes);
        } else if (version == 2 || version == 3) {
            this.bucketDuration = in.readLong();
            this.bucketStart = DataStreamUtils.readVarLongArray(in);
            if (version >= 3) {
                jArr = DataStreamUtils.readVarLongArray(in);
            } else {
                jArr = new long[this.bucketStart.length];
            }
            this.activeTime = jArr;
            this.rxBytes = DataStreamUtils.readVarLongArray(in);
            this.rxPackets = DataStreamUtils.readVarLongArray(in);
            this.txBytes = DataStreamUtils.readVarLongArray(in);
            this.txPackets = DataStreamUtils.readVarLongArray(in);
            this.operations = DataStreamUtils.readVarLongArray(in);
            this.bucketCount = this.bucketStart.length;
            this.totalBytes = ArrayUtils.total(this.rxBytes) + ArrayUtils.total(this.txBytes);
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("unexpected version: ");
            stringBuilder.append(version);
            throw new ProtocolException(stringBuilder.toString());
        }
        int length = this.bucketStart.length;
        int i = this.bucketCount;
        if (length != i || this.rxBytes.length != i || this.rxPackets.length != i || this.txBytes.length != i || this.txPackets.length != i || this.operations.length != i) {
            throw new ProtocolException("Mismatched history lengths");
        }
    }

    public void writeToStream(DataOutputStream out) throws IOException {
        out.writeInt(3);
        out.writeLong(this.bucketDuration);
        DataStreamUtils.writeVarLongArray(out, this.bucketStart, this.bucketCount);
        DataStreamUtils.writeVarLongArray(out, this.activeTime, this.bucketCount);
        DataStreamUtils.writeVarLongArray(out, this.rxBytes, this.bucketCount);
        DataStreamUtils.writeVarLongArray(out, this.rxPackets, this.bucketCount);
        DataStreamUtils.writeVarLongArray(out, this.txBytes, this.bucketCount);
        DataStreamUtils.writeVarLongArray(out, this.txPackets, this.bucketCount);
        DataStreamUtils.writeVarLongArray(out, this.operations, this.bucketCount);
    }

    public int describeContents() {
        return 0;
    }

    @UnsupportedAppUsage
    public int size() {
        return this.bucketCount;
    }

    public long getBucketDuration() {
        return this.bucketDuration;
    }

    @UnsupportedAppUsage
    public long getStart() {
        if (this.bucketCount > 0) {
            return this.bucketStart[0];
        }
        return Long.MAX_VALUE;
    }

    @UnsupportedAppUsage
    public long getEnd() {
        int i = this.bucketCount;
        if (i > 0) {
            return this.bucketStart[i - 1] + this.bucketDuration;
        }
        return Long.MIN_VALUE;
    }

    public long getTotalBytes() {
        return this.totalBytes;
    }

    @UnsupportedAppUsage
    public int getIndexBefore(long time) {
        int index;
        int index2 = Arrays.binarySearch(this.bucketStart, 0, this.bucketCount, time);
        if (index2 < 0) {
            index = (~index2) - 1;
        } else {
            index = index2 - 1;
        }
        return MathUtils.constrain(index, 0, this.bucketCount - 1);
    }

    public int getIndexAfter(long time) {
        int index = Arrays.binarySearch(this.bucketStart, 0, this.bucketCount, time);
        if (index < 0) {
            index = ~index;
        } else {
            index++;
        }
        return MathUtils.constrain(index, 0, this.bucketCount - 1);
    }

    @UnsupportedAppUsage
    public Entry getValues(int i, Entry recycle) {
        Entry entry = recycle != null ? recycle : new Entry();
        entry.bucketStart = this.bucketStart[i];
        entry.bucketDuration = this.bucketDuration;
        entry.activeTime = getLong(this.activeTime, i, -1);
        entry.rxBytes = getLong(this.rxBytes, i, -1);
        entry.rxPackets = getLong(this.rxPackets, i, -1);
        entry.txBytes = getLong(this.txBytes, i, -1);
        entry.txPackets = getLong(this.txPackets, i, -1);
        entry.operations = getLong(this.operations, i, -1);
        return entry;
    }

    public void setValues(int i, Entry entry) {
        long[] jArr = this.rxBytes;
        if (jArr != null) {
            this.totalBytes -= jArr[i];
        }
        jArr = this.txBytes;
        if (jArr != null) {
            this.totalBytes -= jArr[i];
        }
        this.bucketStart[i] = entry.bucketStart;
        setLong(this.activeTime, i, entry.activeTime);
        setLong(this.rxBytes, i, entry.rxBytes);
        setLong(this.rxPackets, i, entry.rxPackets);
        setLong(this.txBytes, i, entry.txBytes);
        setLong(this.txPackets, i, entry.txPackets);
        setLong(this.operations, i, entry.operations);
        jArr = this.rxBytes;
        if (jArr != null) {
            this.totalBytes += jArr[i];
        }
        jArr = this.txBytes;
        if (jArr != null) {
            this.totalBytes += jArr[i];
        }
    }

    @Deprecated
    public void recordData(long start, long end, long rxBytes, long txBytes) {
        recordData(start, end, new android.net.NetworkStats.Entry(NetworkStats.IFACE_ALL, -1, 0, 0, rxBytes, 0, txBytes, 0, 0));
    }

    public void recordData(long start, long end, android.net.NetworkStats.Entry entry) {
        long j = start;
        long j2 = end;
        android.net.NetworkStats.Entry entry2 = entry;
        long rxBytes = entry2.rxBytes;
        long rxPackets = entry2.rxPackets;
        long txBytes = entry2.txBytes;
        long txPackets = entry2.txPackets;
        long operations = entry2.operations;
        if (entry.isNegative()) {
            throw new IllegalArgumentException("tried recording negative data");
        } else if (!entry.isEmpty()) {
            ensureBuckets(start, end);
            long rxBytes2 = rxBytes;
            long operations2 = operations;
            long duration = j2 - j;
            int i = getIndexAfter(j2);
            long txBytes2 = txBytes;
            long txPackets2 = txPackets;
            long rxPackets2 = rxPackets;
            while (i >= 0) {
                long curStart = this.bucketStart[i];
                long curEnd = curStart + this.bucketDuration;
                if (curEnd < j) {
                    break;
                }
                if (curStart <= j2) {
                    long overlap = Math.min(curEnd, j2) - Math.max(curStart, j);
                    if (overlap > 0) {
                        long overlap2 = overlap;
                        curEnd = overlap2;
                        j = NetworkStats.multiplySafe(rxBytes2, curEnd, duration);
                        j2 = NetworkStats.multiplySafe(rxPackets2, curEnd, duration);
                        long fracTxBytes = NetworkStats.multiplySafe(txBytes2, curEnd, duration);
                        long fracTxPackets = NetworkStats.multiplySafe(txPackets2, curEnd, duration);
                        overlap = NetworkStats.multiplySafe(operations2, curEnd, duration);
                        txBytes = overlap2;
                        addLong(this.activeTime, i, txBytes);
                        addLong(this.rxBytes, i, j);
                        rxBytes2 -= j;
                        addLong(this.rxPackets, i, j2);
                        rxPackets2 -= j2;
                        j = fracTxBytes;
                        addLong(this.txBytes, i, j);
                        txBytes2 -= j;
                        j = fracTxPackets;
                        addLong(this.txPackets, i, j);
                        txPackets2 -= j;
                        addLong(this.operations, i, overlap);
                        operations2 -= overlap;
                        duration -= txBytes;
                    }
                }
                i--;
                j = start;
                j2 = end;
            }
            this.totalBytes += entry2.rxBytes + entry2.txBytes;
        }
    }

    @UnsupportedAppUsage
    public void recordEntireHistory(NetworkStatsHistory input) {
        recordHistory(input, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    public void recordHistory(NetworkStatsHistory input, long start, long end) {
        NetworkStatsHistory networkStatsHistory = input;
        android.net.NetworkStats.Entry entry = new android.net.NetworkStats.Entry(NetworkStats.IFACE_ALL, -1, 0, 0, 0, 0, 0, 0, 0);
        for (int i = 0; i < networkStatsHistory.bucketCount; i++) {
            long bucketStart = networkStatsHistory.bucketStart[i];
            long bucketEnd = networkStatsHistory.bucketDuration + bucketStart;
            if (bucketStart >= start && bucketEnd <= end) {
                entry.rxBytes = getLong(networkStatsHistory.rxBytes, i, 0);
                entry.rxPackets = getLong(networkStatsHistory.rxPackets, i, 0);
                entry.txBytes = getLong(networkStatsHistory.txBytes, i, 0);
                entry.txPackets = getLong(networkStatsHistory.txPackets, i, 0);
                entry.operations = getLong(networkStatsHistory.operations, i, 0);
                recordData(bucketStart, bucketEnd, entry);
            }
        }
    }

    private void ensureBuckets(long start, long end) {
        long j = this.bucketDuration;
        end += (j - (end % j)) % j;
        j = start - (start % j);
        while (j < end) {
            int index = Arrays.binarySearch(this.bucketStart, 0, this.bucketCount, j);
            if (index < 0) {
                insertBucket(~index, j);
            }
            j += this.bucketDuration;
        }
    }

    private void insertBucket(int index, long start) {
        int i = this.bucketCount;
        long[] jArr = this.bucketStart;
        if (i >= jArr.length) {
            i = (Math.max(jArr.length, 10) * 3) / 2;
            this.bucketStart = Arrays.copyOf(this.bucketStart, i);
            jArr = this.activeTime;
            if (jArr != null) {
                this.activeTime = Arrays.copyOf(jArr, i);
            }
            jArr = this.rxBytes;
            if (jArr != null) {
                this.rxBytes = Arrays.copyOf(jArr, i);
            }
            jArr = this.rxPackets;
            if (jArr != null) {
                this.rxPackets = Arrays.copyOf(jArr, i);
            }
            jArr = this.txBytes;
            if (jArr != null) {
                this.txBytes = Arrays.copyOf(jArr, i);
            }
            jArr = this.txPackets;
            if (jArr != null) {
                this.txPackets = Arrays.copyOf(jArr, i);
            }
            jArr = this.operations;
            if (jArr != null) {
                this.operations = Arrays.copyOf(jArr, i);
            }
        }
        i = this.bucketCount;
        if (index < i) {
            int dstPos = index + 1;
            i -= index;
            long[] jArr2 = this.bucketStart;
            System.arraycopy(jArr2, index, jArr2, dstPos, i);
            jArr2 = this.activeTime;
            if (jArr2 != null) {
                System.arraycopy(jArr2, index, jArr2, dstPos, i);
            }
            jArr2 = this.rxBytes;
            if (jArr2 != null) {
                System.arraycopy(jArr2, index, jArr2, dstPos, i);
            }
            jArr2 = this.rxPackets;
            if (jArr2 != null) {
                System.arraycopy(jArr2, index, jArr2, dstPos, i);
            }
            jArr2 = this.txBytes;
            if (jArr2 != null) {
                System.arraycopy(jArr2, index, jArr2, dstPos, i);
            }
            jArr2 = this.txPackets;
            if (jArr2 != null) {
                System.arraycopy(jArr2, index, jArr2, dstPos, i);
            }
            jArr2 = this.operations;
            if (jArr2 != null) {
                System.arraycopy(jArr2, index, jArr2, dstPos, i);
            }
        }
        this.bucketStart[index] = start;
        setLong(this.activeTime, index, 0);
        setLong(this.rxBytes, index, 0);
        setLong(this.rxPackets, index, 0);
        setLong(this.txBytes, index, 0);
        setLong(this.txPackets, index, 0);
        setLong(this.operations, index, 0);
        this.bucketCount++;
    }

    public void clear() {
        this.bucketStart = EmptyArray.LONG;
        if (this.activeTime != null) {
            this.activeTime = EmptyArray.LONG;
        }
        if (this.rxBytes != null) {
            this.rxBytes = EmptyArray.LONG;
        }
        if (this.rxPackets != null) {
            this.rxPackets = EmptyArray.LONG;
        }
        if (this.txBytes != null) {
            this.txBytes = EmptyArray.LONG;
        }
        if (this.txPackets != null) {
            this.txPackets = EmptyArray.LONG;
        }
        if (this.operations != null) {
            this.operations = EmptyArray.LONG;
        }
        this.bucketCount = 0;
        this.totalBytes = 0;
    }

    @Deprecated
    public void removeBucketsBefore(long cutoff) {
        int i = 0;
        while (i < this.bucketCount) {
            if (this.bucketDuration + this.bucketStart[i] > cutoff) {
                break;
            }
            i++;
        }
        if (i > 0) {
            long[] jArr = this.bucketStart;
            int length = jArr.length;
            this.bucketStart = Arrays.copyOfRange(jArr, i, length);
            jArr = this.activeTime;
            if (jArr != null) {
                this.activeTime = Arrays.copyOfRange(jArr, i, length);
            }
            jArr = this.rxBytes;
            if (jArr != null) {
                this.rxBytes = Arrays.copyOfRange(jArr, i, length);
            }
            jArr = this.rxPackets;
            if (jArr != null) {
                this.rxPackets = Arrays.copyOfRange(jArr, i, length);
            }
            jArr = this.txBytes;
            if (jArr != null) {
                this.txBytes = Arrays.copyOfRange(jArr, i, length);
            }
            jArr = this.txPackets;
            if (jArr != null) {
                this.txPackets = Arrays.copyOfRange(jArr, i, length);
            }
            jArr = this.operations;
            if (jArr != null) {
                this.operations = Arrays.copyOfRange(jArr, i, length);
            }
            this.bucketCount -= i;
        }
    }

    @UnsupportedAppUsage
    public Entry getValues(long start, long end, Entry recycle) {
        return getValues(start, end, Long.MAX_VALUE, recycle);
    }

    @UnsupportedAppUsage
    public Entry getValues(long start, long end, long now, Entry recycle) {
        long j = start;
        long j2 = end;
        Entry entry = recycle != null ? recycle : new Entry();
        entry.bucketDuration = j2 - j;
        entry.bucketStart = j;
        long j3 = -1;
        entry.activeTime = this.activeTime != null ? 0 : -1;
        entry.rxBytes = this.rxBytes != null ? 0 : -1;
        entry.rxPackets = this.rxPackets != null ? 0 : -1;
        entry.txBytes = this.txBytes != null ? 0 : -1;
        entry.txPackets = this.txPackets != null ? 0 : -1;
        if (this.operations != null) {
            j3 = 0;
        }
        entry.operations = j3;
        int i = getIndexAfter(j2);
        while (i >= 0) {
            long curStart = this.bucketStart[i];
            long curEnd = this.bucketDuration + curStart;
            if (curEnd <= j) {
                break;
            }
            if (curStart < j2) {
                long overlap;
                boolean activeBucket = curStart < now && curEnd > now;
                if (activeBucket) {
                    overlap = this.bucketDuration;
                } else {
                    overlap = (curEnd < j2 ? curEnd : j2) - (curStart > j ? curStart : j);
                }
                if (overlap > 0) {
                    if (this.activeTime != null) {
                        entry.activeTime += (this.activeTime[i] * overlap) / this.bucketDuration;
                    }
                    if (this.rxBytes != null) {
                        entry.rxBytes += (this.rxBytes[i] * overlap) / this.bucketDuration;
                    }
                    if (this.rxPackets != null) {
                        entry.rxPackets += (this.rxPackets[i] * overlap) / this.bucketDuration;
                    }
                    if (this.txBytes != null) {
                        entry.txBytes += (this.txBytes[i] * overlap) / this.bucketDuration;
                    }
                    if (this.txPackets != null) {
                        entry.txPackets += (this.txPackets[i] * overlap) / this.bucketDuration;
                    }
                    if (this.operations != null) {
                        entry.operations += (this.operations[i] * overlap) / this.bucketDuration;
                    }
                }
            }
            i--;
            j = start;
            j2 = end;
        }
        return entry;
    }

    @Deprecated
    public void generateRandom(long start, long end, long bytes) {
        long j = bytes;
        Random r = new Random();
        float fractionRx = r.nextFloat();
        long rxBytes = (long) (((float) j) * fractionRx);
        long j2 = (long) (((float) j) * (1.0f - fractionRx));
        long txBytes = j2;
        generateRandom(start, end, rxBytes, rxBytes / 1024, j2, j2 / 1024, rxBytes / 2048, r);
    }

    @Deprecated
    public void generateRandom(long start, long end, long rxBytes, long rxPackets, long txBytes, long txPackets, long operations, Random r) {
        long j = end;
        Random random = r;
        ensureBuckets(start, end);
        android.net.NetworkStats.Entry entry = new android.net.NetworkStats.Entry(NetworkStats.IFACE_ALL, -1, 0, 0, 0, 0, 0, 0, 0);
        long rxBytes2 = rxBytes;
        long rxPackets2 = rxPackets;
        long txBytes2 = txBytes;
        long txPackets2 = txPackets;
        long operations2 = operations;
        while (true) {
            if (rxBytes2 > 1024 || rxPackets2 > 128 || txBytes2 > 1024 || txPackets2 > 128 || operations2 > 32) {
                long curStart = randomLong(random, start, j);
                txPackets = curStart + randomLong(random, 0, (j - curStart) / 2);
                entry.rxBytes = randomLong(random, 0, rxBytes2);
                entry.rxPackets = randomLong(random, 0, rxPackets2);
                entry.txBytes = randomLong(random, 0, txBytes2);
                entry.txPackets = randomLong(random, 0, txPackets2);
                entry.operations = randomLong(random, 0, operations2);
                rxBytes2 -= entry.rxBytes;
                rxPackets2 -= entry.rxPackets;
                txBytes2 -= entry.txBytes;
                txPackets2 -= entry.txPackets;
                operations2 -= entry.operations;
                recordData(curStart, txPackets, entry);
                j = end;
            } else {
                return;
            }
        }
    }

    public static long randomLong(Random r, long start, long end) {
        return (long) (((float) start) + (r.nextFloat() * ((float) (end - start))));
    }

    public boolean intersects(long start, long end) {
        long dataStart = getStart();
        long dataEnd = getEnd();
        if (start >= dataStart && start <= dataEnd) {
            return true;
        }
        if (end >= dataStart && end <= dataEnd) {
            return true;
        }
        if (dataStart >= start && dataStart <= end) {
            return true;
        }
        if (dataEnd < start || dataEnd > end) {
            return false;
        }
        return true;
    }

    public void dump(IndentingPrintWriter pw, boolean fullHistory) {
        pw.print("NetworkStatsHistory: bucketDuration=");
        pw.println(this.bucketDuration / 1000);
        pw.increaseIndent();
        int start = 0;
        if (!fullHistory) {
            start = Math.max(0, this.bucketCount - 32);
        }
        if (start > 0) {
            pw.print("(omitting ");
            pw.print(start);
            pw.println(" buckets)");
        }
        for (int i = start; i < this.bucketCount; i++) {
            pw.print("st=");
            pw.print(this.bucketStart[i] / 1000);
            if (this.rxBytes != null) {
                pw.print(" rb=");
                pw.print(this.rxBytes[i]);
            }
            if (this.rxPackets != null) {
                pw.print(" rp=");
                pw.print(this.rxPackets[i]);
            }
            if (this.txBytes != null) {
                pw.print(" tb=");
                pw.print(this.txBytes[i]);
            }
            if (this.txPackets != null) {
                pw.print(" tp=");
                pw.print(this.txPackets[i]);
            }
            if (this.operations != null) {
                pw.print(" op=");
                pw.print(this.operations[i]);
            }
            pw.println();
        }
        pw.decreaseIndent();
    }

    public void dumpCheckin(PrintWriter pw) {
        pw.print("d,");
        pw.print(this.bucketDuration / 1000);
        pw.println();
        for (int i = 0; i < this.bucketCount; i++) {
            pw.print("b,");
            pw.print(this.bucketStart[i] / 1000);
            pw.print(',');
            long[] jArr = this.rxBytes;
            String str = "*";
            if (jArr != null) {
                pw.print(jArr[i]);
            } else {
                pw.print(str);
            }
            pw.print(',');
            jArr = this.rxPackets;
            if (jArr != null) {
                pw.print(jArr[i]);
            } else {
                pw.print(str);
            }
            pw.print(',');
            jArr = this.txBytes;
            if (jArr != null) {
                pw.print(jArr[i]);
            } else {
                pw.print(str);
            }
            pw.print(',');
            jArr = this.txPackets;
            if (jArr != null) {
                pw.print(jArr[i]);
            } else {
                pw.print(str);
            }
            pw.print(',');
            long[] jArr2 = this.operations;
            if (jArr2 != null) {
                pw.print(jArr2[i]);
            } else {
                pw.print(str);
            }
            pw.println();
        }
    }

    public void writeToProto(ProtoOutputStream proto, long tag) {
        long start = proto.start(tag);
        proto.write(1112396529665L, this.bucketDuration);
        for (int i = 0; i < this.bucketCount; i++) {
            long startBucket = proto.start(2246267895810L);
            proto.write(1112396529665L, this.bucketStart[i]);
            writeToProto(proto, 1112396529666L, this.rxBytes, i);
            writeToProto(proto, 1112396529667L, this.rxPackets, i);
            writeToProto(proto, 1112396529668L, this.txBytes, i);
            writeToProto(proto, 1112396529669L, this.txPackets, i);
            writeToProto(proto, 1112396529670L, this.operations, i);
            proto.end(startBucket);
        }
        proto.end(start);
    }

    private static void writeToProto(ProtoOutputStream proto, long tag, long[] array, int index) {
        if (array != null) {
            proto.write(tag, array[index]);
        }
    }

    public String toString() {
        CharArrayWriter writer = new CharArrayWriter();
        dump(new IndentingPrintWriter(writer, "  "), false);
        return writer.toString();
    }

    private static long getLong(long[] array, int i, long value) {
        return array != null ? array[i] : value;
    }

    private static void setLong(long[] array, int i, long value) {
        if (array != null) {
            array[i] = value;
        }
    }

    private static void addLong(long[] array, int i, long value) {
        if (array != null) {
            array[i] = array[i] + value;
        }
    }

    public int estimateResizeBuckets(long newBucketDuration) {
        return (int) ((((long) size()) * getBucketDuration()) / newBucketDuration);
    }
}
