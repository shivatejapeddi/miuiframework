package android.util;

import android.net.TrafficStats;

public enum DataUnit {
    KILOBYTES {
        public long toBytes(long v) {
            return 1000 * v;
        }
    },
    MEGABYTES {
        public long toBytes(long v) {
            return TimeUtils.NANOS_PER_MS * v;
        }
    },
    GIGABYTES {
        public long toBytes(long v) {
            return 1000000000 * v;
        }
    },
    KIBIBYTES {
        public long toBytes(long v) {
            return 1024 * v;
        }
    },
    MEBIBYTES {
        public long toBytes(long v) {
            return 1048576 * v;
        }
    },
    GIBIBYTES {
        public long toBytes(long v) {
            return TrafficStats.GB_IN_BYTES * v;
        }
    };

    public long toBytes(long v) {
        throw new AbstractMethodError();
    }
}
