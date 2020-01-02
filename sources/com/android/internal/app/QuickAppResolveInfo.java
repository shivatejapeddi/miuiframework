package com.android.internal.app;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

public class QuickAppResolveInfo implements Parcelable {
    public static final Creator<QuickAppResolveInfo> CREATOR = new Creator<QuickAppResolveInfo>() {
        public QuickAppResolveInfo createFromParcel(Parcel parcel) {
            return new QuickAppResolveInfo(parcel, null);
        }

        public QuickAppResolveInfo[] newArray(int size) {
            return new QuickAppResolveInfo[size];
        }
    };
    public Uri iconUri;
    public String label;
    public String packageName;
    public String pagePath;

    /* synthetic */ QuickAppResolveInfo(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public QuickAppResolveInfo(String packageName, String pagePath, String label, Uri iconUri) {
        this.packageName = packageName;
        this.pagePath = pagePath;
        this.label = label;
        this.iconUri = iconUri;
    }

    private QuickAppResolveInfo(Parcel source) {
        readFromParcel(source);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeString(this.packageName);
        parcel.writeString(this.pagePath);
        parcel.writeString(this.label);
        parcel.writeParcelable(this.iconUri, flag);
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof QuickAppResolveInfo)) {
            return false;
        }
        QuickAppResolveInfo that = (QuickAppResolveInfo) o;
        if (!(Objects.equals(this.packageName, that.packageName) && Objects.equals(this.pagePath, that.pagePath) && Objects.equals(this.label, that.label))) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.packageName, this.pagePath, this.label});
    }

    private void readFromParcel(Parcel source) {
        this.packageName = source.readString();
        this.pagePath = source.readString();
        this.label = source.readString();
        this.iconUri = (Uri) source.readParcelable(Uri.class.getClassLoader());
    }
}
