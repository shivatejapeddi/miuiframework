package com.miui.enterprise.sdk;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.text.format.DateFormat;

public class APNConfig implements Parcelable {
    public static final Creator<APNConfig> CREATOR = new Creator<APNConfig>() {
        public APNConfig createFromParcel(Parcel source) {
            return new APNConfig(source);
        }

        public APNConfig[] newArray(int size) {
            return new APNConfig[size];
        }
    };
    public String mApn;
    public int mAuthType = 1;
    public int mBearer = 0;
    public int mCarrier_enabled = -1;
    public int mCurrent = -1;
    public String mDialNumber;
    public String mMcc = null;
    public String mMmsc = null;
    public String mMmsport = null;
    public String mMmsproxy = null;
    public String mMnc = null;
    public String mMvno_match_data = null;
    public String mMvno_type = null;
    public String mName;
    public String mNumeric = null;
    public String mPassword;
    public String mPort = null;
    public String mProtocol = null;
    public String mProxy = null;
    public String mRoaming_protocol = null;
    public String mServer = null;
    public String mSub_id = null;
    public String mType = null;
    public String mUser;

    public APNConfig(Parcel in) {
        readFromParcel(in);
    }

    public APNConfig(String name, String apn, String user, String password, int authType, String dialNumber, int bearer) {
        this.mName = name;
        this.mApn = apn;
        this.mUser = user;
        this.mPassword = password;
        this.mAuthType = authType;
        this.mDialNumber = dialNumber;
        this.mBearer = bearer;
    }

    public boolean isValidate() {
        return (TextUtils.isEmpty(this.mName) || TextUtils.isEmpty(this.mApn) || TextUtils.isEmpty(this.mUser) || TextUtils.isEmpty(this.mPassword)) ? false : true;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeString(this.mApn);
        dest.writeString(this.mUser);
        dest.writeString(this.mPassword);
        dest.writeInt(this.mAuthType);
        dest.writeString(this.mDialNumber);
        dest.writeInt(this.mBearer);
        dest.writeInt(this.mCarrier_enabled);
        dest.writeInt(this.mCurrent);
        dest.writeString(this.mMcc);
        dest.writeString(this.mMmsc);
        dest.writeString(this.mMmsport);
        dest.writeString(this.mMmsproxy);
        dest.writeString(this.mMnc);
        dest.writeString(this.mMvno_match_data);
        dest.writeString(this.mMvno_type);
        dest.writeString(this.mNumeric);
        dest.writeString(this.mPort);
        dest.writeString(this.mProtocol);
        dest.writeString(this.mProxy);
        dest.writeString(this.mRoaming_protocol);
        dest.writeString(this.mServer);
        dest.writeString(this.mSub_id);
        dest.writeString(this.mType);
    }

    public void readFromParcel(Parcel in) {
        this.mName = in.readString();
        this.mApn = in.readString();
        this.mUser = in.readString();
        this.mPassword = in.readString();
        this.mAuthType = in.readInt();
        this.mDialNumber = in.readString();
        this.mBearer = in.readInt();
        this.mCarrier_enabled = in.readInt();
        this.mCurrent = in.readInt();
        this.mMcc = in.readString();
        this.mMmsc = in.readString();
        this.mMmsport = in.readString();
        this.mMmsproxy = in.readString();
        this.mMnc = in.readString();
        this.mMvno_match_data = in.readString();
        this.mMvno_type = in.readString();
        this.mNumeric = in.readString();
        this.mPort = in.readString();
        this.mProtocol = in.readString();
        this.mProxy = in.readString();
        this.mRoaming_protocol = in.readString();
        this.mServer = in.readString();
        this.mSub_id = in.readString();
        this.mType = in.readString();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("APNConfig{mName='");
        stringBuilder.append(this.mName);
        stringBuilder.append(DateFormat.QUOTE);
        stringBuilder.append(", mApn='");
        stringBuilder.append(this.mApn);
        stringBuilder.append(DateFormat.QUOTE);
        stringBuilder.append(", mUser='");
        stringBuilder.append(this.mUser);
        stringBuilder.append(DateFormat.QUOTE);
        stringBuilder.append(", mPassword='");
        stringBuilder.append(this.mPassword);
        stringBuilder.append(DateFormat.QUOTE);
        stringBuilder.append(", mAuthType=");
        stringBuilder.append(this.mAuthType);
        stringBuilder.append(", mDialNumber='");
        stringBuilder.append(this.mDialNumber);
        stringBuilder.append(DateFormat.QUOTE);
        stringBuilder.append(", mBearer=");
        stringBuilder.append(this.mBearer);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
