package miui.contentcatcher.sdk.injector;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class PageInjectorInfo implements Parcelable {
    public static final Creator<PageInjectorInfo> CREATOR = new Creator<PageInjectorInfo>() {
        public PageInjectorInfo createFromParcel(Parcel in) {
            return new PageInjectorInfo(in, null);
        }

        public PageInjectorInfo[] newArray(int size) {
            return new PageInjectorInfo[size];
        }
    };

    /* synthetic */ PageInjectorInfo(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
    }

    private PageInjectorInfo(Parcel source) {
    }
}
