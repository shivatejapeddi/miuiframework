package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

public final class UiccCardInfo implements Parcelable {
    public static final Creator<UiccCardInfo> CREATOR = new Creator<UiccCardInfo>() {
        public UiccCardInfo createFromParcel(Parcel in) {
            return new UiccCardInfo(in, null);
        }

        public UiccCardInfo[] newArray(int size) {
            return new UiccCardInfo[size];
        }
    };
    private final int mCardId;
    private final String mEid;
    private final String mIccId;
    private final boolean mIsEuicc;
    private final boolean mIsRemovable;
    private final int mSlotIndex;

    /* synthetic */ UiccCardInfo(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    private UiccCardInfo(Parcel in) {
        boolean z = true;
        this.mIsEuicc = in.readByte() != (byte) 0;
        this.mCardId = in.readInt();
        this.mEid = in.readString();
        this.mIccId = in.readString();
        this.mSlotIndex = in.readInt();
        if (in.readByte() == (byte) 0) {
            z = false;
        }
        this.mIsRemovable = z;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) this.mIsEuicc);
        dest.writeInt(this.mCardId);
        dest.writeString(this.mEid);
        dest.writeString(this.mIccId);
        dest.writeInt(this.mSlotIndex);
        dest.writeByte((byte) this.mIsRemovable);
    }

    public int describeContents() {
        return 0;
    }

    public UiccCardInfo(boolean isEuicc, int cardId, String eid, String iccId, int slotIndex, boolean isRemovable) {
        this.mIsEuicc = isEuicc;
        this.mCardId = cardId;
        this.mEid = eid;
        this.mIccId = iccId;
        this.mSlotIndex = slotIndex;
        this.mIsRemovable = isRemovable;
    }

    public boolean isEuicc() {
        return this.mIsEuicc;
    }

    public int getCardId() {
        return this.mCardId;
    }

    public String getEid() {
        if (this.mIsEuicc) {
            return this.mEid;
        }
        return null;
    }

    public String getIccId() {
        return this.mIccId;
    }

    public int getSlotIndex() {
        return this.mSlotIndex;
    }

    public UiccCardInfo getUnprivileged() {
        return new UiccCardInfo(this.mIsEuicc, this.mCardId, null, null, this.mSlotIndex, this.mIsRemovable);
    }

    public boolean isRemovable() {
        return this.mIsRemovable;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        UiccCardInfo that = (UiccCardInfo) obj;
        if (!(this.mIsEuicc == that.mIsEuicc && this.mCardId == that.mCardId && Objects.equals(this.mEid, that.mEid) && Objects.equals(this.mIccId, that.mIccId) && this.mSlotIndex == that.mSlotIndex && this.mIsRemovable == that.mIsRemovable)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Boolean.valueOf(this.mIsEuicc), Integer.valueOf(this.mCardId), this.mEid, this.mIccId, Integer.valueOf(this.mSlotIndex), Boolean.valueOf(this.mIsRemovable)});
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UiccCardInfo (mIsEuicc=");
        stringBuilder.append(this.mIsEuicc);
        stringBuilder.append(", mCardId=");
        stringBuilder.append(this.mCardId);
        stringBuilder.append(", mEid=");
        stringBuilder.append(this.mEid);
        stringBuilder.append(", mIccId=");
        stringBuilder.append(this.mIccId);
        stringBuilder.append(", mSlotIndex=");
        stringBuilder.append(this.mSlotIndex);
        stringBuilder.append(", mIsRemovable=");
        stringBuilder.append(this.mIsRemovable);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
