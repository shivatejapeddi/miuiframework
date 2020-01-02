package android.permission;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.Preconditions;

@SystemApi
public final class RuntimePermissionUsageInfo implements Parcelable {
    public static final Creator<RuntimePermissionUsageInfo> CREATOR = new Creator<RuntimePermissionUsageInfo>() {
        public RuntimePermissionUsageInfo createFromParcel(Parcel source) {
            return new RuntimePermissionUsageInfo(source, null);
        }

        public RuntimePermissionUsageInfo[] newArray(int size) {
            return new RuntimePermissionUsageInfo[size];
        }
    };
    private final String mName;
    private final int mNumUsers;

    public RuntimePermissionUsageInfo(String name, int numUsers) {
        Preconditions.checkNotNull(name);
        Preconditions.checkArgumentNonnegative(numUsers);
        this.mName = name;
        this.mNumUsers = numUsers;
    }

    private RuntimePermissionUsageInfo(Parcel parcel) {
        this(parcel.readString(), parcel.readInt());
    }

    public int getAppAccessCount() {
        return this.mNumUsers;
    }

    public String getName() {
        return this.mName;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.mName);
        parcel.writeInt(this.mNumUsers);
    }
}
