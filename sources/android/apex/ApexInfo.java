package android.apex;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ApexInfo implements Parcelable {
    public static final Creator<ApexInfo> CREATOR = new Creator<ApexInfo>() {
        public ApexInfo createFromParcel(Parcel _aidl_source) {
            ApexInfo _aidl_out = new ApexInfo();
            _aidl_out.readFromParcel(_aidl_source);
            return _aidl_out;
        }

        public ApexInfo[] newArray(int _aidl_size) {
            return new ApexInfo[_aidl_size];
        }
    };
    public boolean isActive;
    public boolean isFactory;
    public String packageName;
    public String packagePath;
    public long versionCode;
    public String versionName;

    public final void writeToParcel(Parcel _aidl_parcel, int _aidl_flag) {
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.writeInt(0);
        _aidl_parcel.writeString(this.packageName);
        _aidl_parcel.writeString(this.packagePath);
        _aidl_parcel.writeLong(this.versionCode);
        _aidl_parcel.writeString(this.versionName);
        _aidl_parcel.writeInt(this.isFactory);
        _aidl_parcel.writeInt(this.isActive);
        int _aidl_end_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.setDataPosition(_aidl_start_pos);
        _aidl_parcel.writeInt(_aidl_end_pos - _aidl_start_pos);
        _aidl_parcel.setDataPosition(_aidl_end_pos);
    }

    public final void readFromParcel(Parcel _aidl_parcel) {
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        int _aidl_parcelable_size = _aidl_parcel.readInt();
        if (_aidl_parcelable_size >= 0) {
            try {
                this.packageName = _aidl_parcel.readString();
                if (_aidl_parcel.dataPosition() - _aidl_start_pos < _aidl_parcelable_size) {
                    this.packagePath = _aidl_parcel.readString();
                    if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                        _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                        return;
                    }
                    this.versionCode = _aidl_parcel.readLong();
                    if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                        _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                        return;
                    }
                    this.versionName = _aidl_parcel.readString();
                    if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                        _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                        return;
                    }
                    boolean z = true;
                    this.isFactory = _aidl_parcel.readInt() != 0;
                    if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                        _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                        return;
                    }
                    if (_aidl_parcel.readInt() == 0) {
                        z = false;
                    }
                    this.isActive = z;
                    if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                        _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                    } else {
                        _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                    }
                }
            } finally {
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
            }
        }
    }

    public int describeContents() {
        return 0;
    }
}
