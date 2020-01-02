package android.os;

import android.os.Parcelable.Creator;
import android.os.WorkSource.WorkChain;
import android.util.Slog;
import java.util.ArrayList;
import java.util.List;

public final class StatsLogEventWrapper implements Parcelable {
    public static final Creator<StatsLogEventWrapper> CREATOR = new Creator<StatsLogEventWrapper>() {
        public StatsLogEventWrapper createFromParcel(Parcel in) {
            return new StatsLogEventWrapper(in, null);
        }

        public StatsLogEventWrapper[] newArray(int size) {
            return new StatsLogEventWrapper[size];
        }
    };
    static final boolean DEBUG = false;
    private static final int EVENT_TYPE_DOUBLE = 4;
    private static final int EVENT_TYPE_FLOAT = 3;
    private static final int EVENT_TYPE_INT = 1;
    private static final int EVENT_TYPE_LONG = 2;
    private static final int EVENT_TYPE_STORAGE = 6;
    private static final int EVENT_TYPE_STRING = 5;
    private static final int EVENT_TYPE_UNKNOWN = 0;
    static final String TAG = "StatsLogEventWrapper";
    long mElapsedTimeNs;
    int mTag;
    List<Integer> mTypes;
    List<Object> mValues;
    long mWallClockTimeNs;
    WorkSource mWorkSource;

    /* synthetic */ StatsLogEventWrapper(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public StatsLogEventWrapper(int tag, long elapsedTimeNs, long wallClockTimeNs) {
        this.mTypes = new ArrayList();
        this.mValues = new ArrayList();
        this.mWorkSource = null;
        this.mTag = tag;
        this.mElapsedTimeNs = elapsedTimeNs;
        this.mWallClockTimeNs = wallClockTimeNs;
    }

    private StatsLogEventWrapper(Parcel in) {
        this.mTypes = new ArrayList();
        this.mValues = new ArrayList();
        this.mWorkSource = null;
        readFromParcel(in);
    }

    public void setWorkSource(WorkSource ws) {
        if (ws.getWorkChains() == null || ws.getWorkChains().size() == 0) {
            Slog.w(TAG, "Empty worksource!");
        } else {
            this.mWorkSource = ws;
        }
    }

    public void writeInt(int val) {
        this.mTypes.add(Integer.valueOf(1));
        this.mValues.add(Integer.valueOf(val));
    }

    public void writeLong(long val) {
        this.mTypes.add(Integer.valueOf(2));
        this.mValues.add(Long.valueOf(val));
    }

    public void writeString(String val) {
        this.mTypes.add(Integer.valueOf(5));
        this.mValues.add(val == null ? "" : val);
    }

    public void writeFloat(float val) {
        this.mTypes.add(Integer.valueOf(3));
        this.mValues.add(Float.valueOf(val));
    }

    public void writeStorage(byte[] val) {
        this.mTypes.add(Integer.valueOf(6));
        this.mValues.add(val);
    }

    public void writeBoolean(boolean val) {
        this.mTypes.add(Integer.valueOf(1));
        this.mValues.add(Integer.valueOf(val));
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mTag);
        out.writeLong(this.mElapsedTimeNs);
        out.writeLong(this.mWallClockTimeNs);
        ArrayList<WorkChain> workChains = this.mWorkSource;
        if (workChains != null) {
            workChains = workChains.getWorkChains();
            out.writeInt(workChains.size());
            for (int i = 0; i < workChains.size(); i++) {
                WorkChain wc = (WorkChain) workChains.get(i);
                int size = wc.getSize();
                String str = TAG;
                if (size == 0) {
                    Slog.w(str, "Empty work chain.");
                    out.writeInt(0);
                } else if (wc.getUids().length == wc.getTags().length && wc.getUids().length == wc.getSize()) {
                    out.writeInt(wc.getSize());
                    size = 0;
                    while (size < wc.getSize()) {
                        out.writeInt(wc.getUids()[size]);
                        out.writeString(wc.getTags()[size] == null ? "" : wc.getTags()[size]);
                        size++;
                    }
                } else {
                    Slog.w(str, "Malformated work chain.");
                    out.writeInt(0);
                }
            }
        } else {
            out.writeInt(0);
        }
        out.writeInt(this.mTypes.size());
        for (int i2 = 0; i2 < this.mTypes.size(); i2++) {
            out.writeInt(((Integer) this.mTypes.get(i2)).intValue());
            switch (((Integer) this.mTypes.get(i2)).intValue()) {
                case 1:
                    out.writeInt(((Integer) this.mValues.get(i2)).intValue());
                    break;
                case 2:
                    out.writeLong(((Long) this.mValues.get(i2)).longValue());
                    break;
                case 3:
                    out.writeFloat(((Float) this.mValues.get(i2)).floatValue());
                    break;
                case 4:
                    out.writeDouble(((Double) this.mValues.get(i2)).doubleValue());
                    break;
                case 5:
                    out.writeString((String) this.mValues.get(i2));
                    break;
                case 6:
                    out.writeByteArray((byte[]) this.mValues.get(i2));
                    break;
                default:
                    break;
            }
        }
    }

    public void readFromParcel(Parcel in) {
        int i;
        int workChainSize;
        this.mTypes = new ArrayList();
        this.mValues = new ArrayList();
        this.mWorkSource = null;
        this.mTag = in.readInt();
        this.mElapsedTimeNs = in.readLong();
        this.mWallClockTimeNs = in.readLong();
        int numWorkChains = in.readInt();
        if (numWorkChains > 0) {
            this.mWorkSource = new WorkSource();
            for (i = 0; i < numWorkChains; i++) {
                WorkChain workChain = this.mWorkSource.createWorkChain();
                workChainSize = in.readInt();
                for (int j = 0; j < workChainSize; j++) {
                    workChain.addNode(in.readInt(), in.readString());
                }
            }
        }
        i = in.readInt();
        for (int i2 = 0; i2 < i; i2++) {
            workChainSize = in.readInt();
            this.mTypes.add(Integer.valueOf(workChainSize));
            switch (workChainSize) {
                case 1:
                    this.mValues.add(Integer.valueOf(in.readInt()));
                    break;
                case 2:
                    this.mValues.add(Long.valueOf(in.readLong()));
                    break;
                case 3:
                    this.mValues.add(Float.valueOf(in.readFloat()));
                    break;
                case 4:
                    this.mValues.add(Double.valueOf(in.readDouble()));
                    break;
                case 5:
                    this.mValues.add(in.readString());
                    break;
                case 6:
                    this.mValues.add(in.createByteArray());
                    break;
                default:
                    break;
            }
        }
    }

    public int describeContents() {
        return 0;
    }
}
