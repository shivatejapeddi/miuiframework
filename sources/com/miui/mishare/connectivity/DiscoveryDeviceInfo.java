package com.miui.mishare.connectivity;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.format.DateFormat;

public class DiscoveryDeviceInfo implements Parcelable {
    public static final Creator<DiscoveryDeviceInfo> CREATOR = new Creator<DiscoveryDeviceInfo>() {
        public DiscoveryDeviceInfo createFromParcel(Parcel in) {
            return new DiscoveryDeviceInfo(in);
        }

        public DiscoveryDeviceInfo[] newArray(int size) {
            return new DiscoveryDeviceInfo[size];
        }
    };
    private final long mAccount;
    private final int mDeviceCode;
    private String mDeviceId;
    private int mDiscoverType;
    private final String mMacAddress;
    private final byte mManufactureCode;
    private final String mNickName;
    private final boolean mNickNameHasMore;
    private int mRssi;
    private boolean mSupport5gBand;
    private int mSupportWifiProtocol;

    public static final class Builder {
        private long mAccount;
        private int mDeviceCode;
        private String mDeviceId;
        private int mDiscoverType;
        private String mMacAddress;
        private byte mManufactureCode;
        private String mNickName;
        private boolean mNickNameHasMore;
        private int mRssi;
        private boolean mSupport5gBand;
        private int mSupportWifiProtocol;

        public Builder setManufactureCode(byte manufactureCode) {
            this.mManufactureCode = manufactureCode;
            return this;
        }

        public Builder setDeviceCode(int deviceCode) {
            this.mDeviceCode = deviceCode;
            return this;
        }

        public Builder setAccount(long account) {
            this.mAccount = account;
            return this;
        }

        public Builder setNickName(String nickName) {
            this.mNickName = nickName;
            return this;
        }

        public Builder setNickNameHasMore(boolean nickNameHasMore) {
            this.mNickNameHasMore = nickNameHasMore;
            return this;
        }

        public Builder setMacAddress(String macAddress) {
            this.mMacAddress = macAddress;
            return this;
        }

        public Builder setDeviceId(String deviceId) {
            this.mDeviceId = deviceId;
            return this;
        }

        public Builder setSupport5gBand(boolean support5gBand) {
            this.mSupport5gBand = support5gBand;
            return this;
        }

        public Builder setSupportWifiProtocol(int supportWifiProtocol) {
            this.mSupportWifiProtocol = supportWifiProtocol;
            return this;
        }

        public Builder setDiscoverType(int discoverType) {
            this.mDiscoverType = discoverType;
            return this;
        }

        public Builder setRssi(int rssi) {
            this.mRssi = rssi;
            return this;
        }

        public DiscoveryDeviceInfo build() {
            return new DiscoveryDeviceInfo(this.mManufactureCode, this.mDeviceCode, this.mAccount, this.mNickName, this.mNickNameHasMore, this.mMacAddress, this.mDeviceId, this.mSupport5gBand, this.mSupportWifiProtocol, this.mDiscoverType, this.mRssi);
        }
    }

    DiscoveryDeviceInfo(byte manufactureCode, int deviceCode, long account, String nickName, boolean nickNameHasMore, String macAddress, String deviceId, boolean support5gBand, int supportWifiProtocol, int discoverType, int rssi) {
        this.mManufactureCode = manufactureCode;
        this.mDeviceCode = deviceCode;
        this.mAccount = account;
        this.mNickName = nickName;
        this.mNickNameHasMore = nickNameHasMore;
        this.mMacAddress = macAddress;
        this.mDeviceId = deviceId;
        this.mSupport5gBand = support5gBand;
        this.mSupportWifiProtocol = supportWifiProtocol;
        this.mDiscoverType = discoverType;
        this.mRssi = rssi;
    }

    protected DiscoveryDeviceInfo(Parcel in) {
        this.mManufactureCode = in.readByte();
        this.mDeviceCode = in.readInt();
        this.mAccount = in.readLong();
        this.mNickName = in.readString();
        boolean z = true;
        this.mNickNameHasMore = in.readByte() != (byte) 0;
        this.mMacAddress = in.readString();
        this.mDeviceId = in.readString();
        if (in.readByte() == (byte) 0) {
            z = false;
        }
        this.mSupport5gBand = z;
        this.mSupportWifiProtocol = in.readInt();
        this.mDiscoverType = in.readInt();
        this.mRssi = in.readInt();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.mManufactureCode);
        dest.writeInt(this.mDeviceCode);
        dest.writeLong(this.mAccount);
        dest.writeString(this.mNickName);
        dest.writeByte((byte) this.mNickNameHasMore);
        dest.writeString(this.mMacAddress);
        dest.writeString(this.mDeviceId);
        dest.writeByte((byte) this.mSupport5gBand);
        dest.writeInt(this.mSupportWifiProtocol);
        dest.writeInt(this.mDiscoverType);
        dest.writeInt(this.mRssi);
    }

    public int describeContents() {
        return 0;
    }

    public byte getManufactureCode() {
        return this.mManufactureCode;
    }

    public int getDeviceCode() {
        return this.mDeviceCode;
    }

    public long getAccount() {
        return this.mAccount;
    }

    public String getNickName() {
        return this.mNickName;
    }

    public boolean getNickNameHasMore() {
        return this.mNickNameHasMore;
    }

    public String getMacAddress() {
        return this.mMacAddress;
    }

    public String getDeviceId() {
        return this.mDeviceId;
    }

    public void setDeviceId(String deviceId) {
        this.mDeviceId = deviceId;
    }

    public int getDiscoverType() {
        return this.mDiscoverType;
    }

    public boolean isSupport5gBand() {
        return this.mSupport5gBand;
    }

    public int getRssi() {
        return this.mRssi;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DiscoveryDeviceInfo{manufacture=");
        stringBuilder.append(this.mManufactureCode);
        stringBuilder.append(", deviceType=");
        stringBuilder.append(this.mDeviceCode);
        stringBuilder.append(", account=");
        stringBuilder.append(this.mAccount);
        stringBuilder.append(", nickName='");
        stringBuilder.append(this.mNickName);
        stringBuilder.append(DateFormat.QUOTE);
        stringBuilder.append(", nickNameHasMore='");
        stringBuilder.append(this.mNickNameHasMore);
        stringBuilder.append(DateFormat.QUOTE);
        stringBuilder.append(", macAddress='");
        stringBuilder.append(this.mMacAddress);
        stringBuilder.append(DateFormat.QUOTE);
        stringBuilder.append(", deviceId='");
        stringBuilder.append(this.mDeviceId);
        stringBuilder.append(DateFormat.QUOTE);
        stringBuilder.append(", support5GBand=");
        stringBuilder.append(this.mSupport5gBand);
        stringBuilder.append(", supportWifiProtocol=");
        stringBuilder.append(this.mSupportWifiProtocol);
        stringBuilder.append(", discoverType=");
        stringBuilder.append(this.mDiscoverType);
        stringBuilder.append(", mRssi=");
        stringBuilder.append(this.mRssi);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
