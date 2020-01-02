package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class AvailableNetworkInfo implements Parcelable {
    public static final Creator<AvailableNetworkInfo> CREATOR = new Creator<AvailableNetworkInfo>() {
        public AvailableNetworkInfo createFromParcel(Parcel in) {
            return new AvailableNetworkInfo(in, null);
        }

        public AvailableNetworkInfo[] newArray(int size) {
            return new AvailableNetworkInfo[size];
        }
    };
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_LOW = 3;
    public static final int PRIORITY_MED = 2;
    private ArrayList<Integer> mBands;
    private ArrayList<String> mMccMncs;
    private int mPriority;
    private int mSubId;

    /* synthetic */ AvailableNetworkInfo(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public int getSubId() {
        return this.mSubId;
    }

    public int getPriority() {
        return this.mPriority;
    }

    public List<String> getMccMncs() {
        return (List) this.mMccMncs.clone();
    }

    public List<Integer> getBands() {
        return (List) this.mBands.clone();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mSubId);
        dest.writeInt(this.mPriority);
        dest.writeStringList(this.mMccMncs);
        dest.writeList(this.mBands);
    }

    private AvailableNetworkInfo(Parcel in) {
        this.mSubId = in.readInt();
        this.mPriority = in.readInt();
        this.mMccMncs = new ArrayList();
        in.readStringList(this.mMccMncs);
        this.mBands = new ArrayList();
        in.readList(this.mBands, Integer.class.getClassLoader());
    }

    public AvailableNetworkInfo(int subId, int priority, List<String> mccMncs, List<Integer> bands) {
        this.mSubId = subId;
        this.mPriority = priority;
        this.mMccMncs = new ArrayList(mccMncs);
        this.mBands = new ArrayList(bands);
    }

    public boolean equals(Object o) {
        boolean z = false;
        try {
            AvailableNetworkInfo ani = (AvailableNetworkInfo) o;
            if (o == null) {
                return false;
            }
            if (this.mSubId == ani.mSubId && this.mPriority == ani.mPriority) {
                ArrayList arrayList = this.mMccMncs;
                if (arrayList != null && arrayList.equals(ani.mMccMncs) && this.mBands.equals(ani.mBands)) {
                    z = true;
                }
            }
            return z;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(this.mSubId), Integer.valueOf(this.mPriority), this.mMccMncs, this.mBands});
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("AvailableNetworkInfo: mSubId: ");
        stringBuilder.append(this.mSubId);
        stringBuilder.append(" mPriority: ");
        stringBuilder.append(this.mPriority);
        stringBuilder.append(" mMccMncs: ");
        stringBuilder.append(Arrays.toString(this.mMccMncs.toArray()));
        stringBuilder.append(" mBands: ");
        stringBuilder.append(Arrays.toString(this.mBands.toArray()));
        return stringBuilder.toString();
    }
}
