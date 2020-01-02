package miui.contentcatcher.sdk;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class DecorateContentParam implements Parcelable {
    public static final Creator<DecorateContentParam> CREATOR = new Creator<DecorateContentParam>() {
        public DecorateContentParam createFromParcel(Parcel in) {
            return new DecorateContentParam(in, null);
        }

        public DecorateContentParam[] newArray(int size) {
            return new DecorateContentParam[size];
        }
    };

    /* synthetic */ DecorateContentParam(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
    }

    private DecorateContentParam(Parcel source) {
    }
}
