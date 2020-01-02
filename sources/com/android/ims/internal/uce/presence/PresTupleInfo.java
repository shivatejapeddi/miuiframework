package com.android.ims.internal.uce.presence;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class PresTupleInfo implements Parcelable {
    public static final Creator<PresTupleInfo> CREATOR = new Creator<PresTupleInfo>() {
        public PresTupleInfo createFromParcel(Parcel source) {
            return new PresTupleInfo(source, null);
        }

        public PresTupleInfo[] newArray(int size) {
            return new PresTupleInfo[size];
        }
    };
    private String mContactUri;
    private String mFeatureTag;
    private String mTimestamp;

    /* synthetic */ PresTupleInfo(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public String getFeatureTag() {
        return this.mFeatureTag;
    }

    @UnsupportedAppUsage
    public void setFeatureTag(String featureTag) {
        this.mFeatureTag = featureTag;
    }

    public String getContactUri() {
        return this.mContactUri;
    }

    @UnsupportedAppUsage
    public void setContactUri(String contactUri) {
        this.mContactUri = contactUri;
    }

    public String getTimestamp() {
        return this.mTimestamp;
    }

    @UnsupportedAppUsage
    public void setTimestamp(String timestamp) {
        this.mTimestamp = timestamp;
    }

    @UnsupportedAppUsage
    public PresTupleInfo() {
        String str = "";
        this.mFeatureTag = str;
        this.mContactUri = str;
        this.mTimestamp = str;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mFeatureTag);
        dest.writeString(this.mContactUri);
        dest.writeString(this.mTimestamp);
    }

    private PresTupleInfo(Parcel source) {
        String str = "";
        this.mFeatureTag = str;
        this.mContactUri = str;
        this.mTimestamp = str;
        readFromParcel(source);
    }

    public void readFromParcel(Parcel source) {
        this.mFeatureTag = source.readString();
        this.mContactUri = source.readString();
        this.mTimestamp = source.readString();
    }
}
