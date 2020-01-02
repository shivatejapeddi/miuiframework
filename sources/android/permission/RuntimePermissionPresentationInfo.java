package android.permission;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.Preconditions;

@SystemApi
public final class RuntimePermissionPresentationInfo implements Parcelable {
    public static final Creator<RuntimePermissionPresentationInfo> CREATOR = new Creator<RuntimePermissionPresentationInfo>() {
        public RuntimePermissionPresentationInfo createFromParcel(Parcel source) {
            CharSequence label = source.readCharSequence();
            int flags = source.readInt();
            boolean z = false;
            boolean z2 = (flags & 1) != 0;
            if ((flags & 2) != 0) {
                z = true;
            }
            return new RuntimePermissionPresentationInfo(label, z2, z);
        }

        public RuntimePermissionPresentationInfo[] newArray(int size) {
            return new RuntimePermissionPresentationInfo[size];
        }
    };
    private static final int FLAG_GRANTED = 1;
    private static final int FLAG_STANDARD = 2;
    private final int mFlags;
    private final CharSequence mLabel;

    public RuntimePermissionPresentationInfo(CharSequence label, boolean granted, boolean standard) {
        Preconditions.checkNotNull(label);
        this.mLabel = label;
        int flags = 0;
        if (granted) {
            flags = 0 | 1;
        }
        if (standard) {
            flags |= 2;
        }
        this.mFlags = flags;
    }

    public boolean isGranted() {
        return (this.mFlags & 1) != 0;
    }

    public boolean isStandard() {
        return (this.mFlags & 2) != 0;
    }

    public CharSequence getLabel() {
        return this.mLabel;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeCharSequence(this.mLabel);
        parcel.writeInt(this.mFlags);
    }
}
