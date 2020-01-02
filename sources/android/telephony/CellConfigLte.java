package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

public class CellConfigLte implements Parcelable {
    public static final Creator<CellConfigLte> CREATOR = new Creator<CellConfigLte>() {
        public CellConfigLte createFromParcel(Parcel in) {
            return new CellConfigLte(in, null);
        }

        public CellConfigLte[] newArray(int size) {
            return new CellConfigLte[0];
        }
    };
    private final boolean mIsEndcAvailable;

    /* synthetic */ CellConfigLte(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public CellConfigLte() {
        this.mIsEndcAvailable = false;
    }

    public CellConfigLte(android.hardware.radio.V1_4.CellConfigLte cellConfig) {
        this.mIsEndcAvailable = cellConfig.isEndcAvailable;
    }

    public CellConfigLte(boolean isEndcAvailable) {
        this.mIsEndcAvailable = isEndcAvailable;
    }

    public CellConfigLte(CellConfigLte config) {
        this.mIsEndcAvailable = config.mIsEndcAvailable;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isEndcAvailable() {
        return this.mIsEndcAvailable;
    }

    public int describeContents() {
        return 0;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Boolean.valueOf(this.mIsEndcAvailable)});
    }

    public boolean equals(Object other) {
        boolean z = false;
        if (!(other instanceof CellConfigLte)) {
            return false;
        }
        if (this.mIsEndcAvailable == ((CellConfigLte) other).mIsEndcAvailable) {
            z = true;
        }
        return z;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(this.mIsEndcAvailable);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getClass().getName());
        stringBuilder.append(" :{");
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(" isEndcAvailable = ");
        stringBuilder2.append(this.mIsEndcAvailable);
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder.append(" }");
        return stringBuilder.toString();
    }

    private CellConfigLte(Parcel in) {
        this.mIsEndcAvailable = in.readBoolean();
    }
}
