package android.view.autofill;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class AutofillId implements Parcelable {
    public static final Creator<AutofillId> CREATOR = new Creator<AutofillId>() {
        public AutofillId createFromParcel(Parcel source) {
            int viewId = source.readInt();
            int flags = source.readInt();
            int sessionId = (flags & 4) != 0 ? source.readInt() : 0;
            if ((flags & 1) != 0) {
                return new AutofillId(flags, viewId, (long) source.readInt(), sessionId, null);
            } else if ((flags & 2) == 0) {
                return new AutofillId(flags, viewId, -1, sessionId, null);
            } else {
                return new AutofillId(flags, viewId, source.readLong(), sessionId, null);
            }
        }

        public AutofillId[] newArray(int size) {
            return new AutofillId[size];
        }
    };
    private static final int FLAG_HAS_SESSION = 4;
    private static final int FLAG_IS_VIRTUAL_INT = 1;
    private static final int FLAG_IS_VIRTUAL_LONG = 2;
    public static final int NO_SESSION = 0;
    private int mFlags;
    private int mSessionId;
    private final int mViewId;
    private final int mVirtualIntId;
    private final long mVirtualLongId;

    /* synthetic */ AutofillId(int x0, int x1, long x2, int x3, AnonymousClass1 x4) {
        this(x0, x1, x2, x3);
    }

    public AutofillId(int id) {
        this(0, id, -1, 0);
    }

    public AutofillId(AutofillId hostId, int virtualChildId) {
        this(1, hostId.mViewId, (long) virtualChildId, 0);
    }

    public AutofillId(int hostId, int virtualChildId) {
        this(1, hostId, (long) virtualChildId, 0);
    }

    public AutofillId(AutofillId hostId, long virtualChildId, int sessionId) {
        this(6, hostId.mViewId, virtualChildId, sessionId);
    }

    private AutofillId(int flags, int parentId, long virtualChildId, int sessionId) {
        this.mFlags = flags;
        this.mViewId = parentId;
        this.mVirtualIntId = (flags & 1) != 0 ? (int) virtualChildId : -1;
        this.mVirtualLongId = (flags & 2) != 0 ? virtualChildId : -1;
        this.mSessionId = sessionId;
    }

    public static AutofillId withoutSession(AutofillId id) {
        return new AutofillId(id.mFlags & -5, id.mViewId, id.mVirtualLongId, 0);
    }

    public int getViewId() {
        return this.mViewId;
    }

    public int getVirtualChildIntId() {
        return this.mVirtualIntId;
    }

    public long getVirtualChildLongId() {
        return this.mVirtualLongId;
    }

    public boolean isVirtualInt() {
        return (this.mFlags & 1) != 0;
    }

    public boolean isVirtualLong() {
        return (this.mFlags & 2) != 0;
    }

    public boolean isNonVirtual() {
        return (isVirtualInt() || isVirtualLong()) ? false : true;
    }

    public boolean hasSession() {
        return (this.mFlags & 4) != 0;
    }

    public int getSessionId() {
        return this.mSessionId;
    }

    public void setSessionId(int sessionId) {
        this.mFlags |= 4;
        this.mSessionId = sessionId;
    }

    public void resetSessionId() {
        this.mFlags &= -5;
        this.mSessionId = 0;
    }

    public int hashCode() {
        int i = ((((1 * 31) + this.mViewId) * 31) + this.mVirtualIntId) * 31;
        long j = this.mVirtualLongId;
        return ((i + ((int) (j ^ (j >>> 32)))) * 31) + this.mSessionId;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AutofillId other = (AutofillId) obj;
        if (this.mViewId == other.mViewId && this.mVirtualIntId == other.mVirtualIntId && this.mVirtualLongId == other.mVirtualLongId && this.mSessionId == other.mSessionId) {
            return true;
        }
        return false;
    }

    public boolean equalsIgnoreSession(AutofillId other) {
        if (this == other) {
            return true;
        }
        if (other != null && this.mViewId == other.mViewId && this.mVirtualIntId == other.mVirtualIntId && this.mVirtualLongId == other.mVirtualLongId) {
            return true;
        }
        return false;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder().append(this.mViewId);
        if (isVirtualInt()) {
            builder.append(':');
            builder.append(this.mVirtualIntId);
        } else if (isVirtualLong()) {
            builder.append(':');
            builder.append(this.mVirtualLongId);
        }
        if (hasSession()) {
            builder.append('@');
            builder.append(this.mSessionId);
        }
        return builder.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.mViewId);
        parcel.writeInt(this.mFlags);
        if (hasSession()) {
            parcel.writeInt(this.mSessionId);
        }
        if (isVirtualInt()) {
            parcel.writeInt(this.mVirtualIntId);
        } else if (isVirtualLong()) {
            parcel.writeLong(this.mVirtualLongId);
        }
    }
}
