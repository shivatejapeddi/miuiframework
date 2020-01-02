package android.telecom;

import android.annotation.UnsupportedAppUsage;
import android.content.ComponentName;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Process;
import android.os.UserHandle;
import android.util.Log;
import java.util.Objects;

public final class PhoneAccountHandle implements Parcelable {
    public static final Creator<PhoneAccountHandle> CREATOR = new Creator<PhoneAccountHandle>() {
        public PhoneAccountHandle createFromParcel(Parcel in) {
            return new PhoneAccountHandle(in, null);
        }

        public PhoneAccountHandle[] newArray(int size) {
            return new PhoneAccountHandle[size];
        }
    };
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 127403196)
    private final ComponentName mComponentName;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private final String mId;
    private final UserHandle mUserHandle;

    public PhoneAccountHandle(ComponentName componentName, String id) {
        this(componentName, id, Process.myUserHandle());
    }

    public PhoneAccountHandle(ComponentName componentName, String id, UserHandle userHandle) {
        checkParameters(componentName, userHandle);
        this.mComponentName = componentName;
        this.mId = id;
        this.mUserHandle = userHandle;
    }

    public ComponentName getComponentName() {
        return this.mComponentName;
    }

    public String getId() {
        return this.mId;
    }

    public UserHandle getUserHandle() {
        return this.mUserHandle;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.mComponentName, this.mId, this.mUserHandle});
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.mComponentName);
        String str = ", ";
        stringBuilder.append(str);
        stringBuilder.append(Log.pii(this.mId));
        stringBuilder.append(str);
        stringBuilder.append(this.mUserHandle);
        return stringBuilder.toString();
    }

    public boolean equals(Object other) {
        return other != null && (other instanceof PhoneAccountHandle) && Objects.equals(((PhoneAccountHandle) other).getComponentName(), getComponentName()) && Objects.equals(((PhoneAccountHandle) other).getId(), getId()) && Objects.equals(((PhoneAccountHandle) other).getUserHandle(), getUserHandle());
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        this.mComponentName.writeToParcel(out, flags);
        out.writeString(this.mId);
        this.mUserHandle.writeToParcel(out, flags);
    }

    private void checkParameters(ComponentName componentName, UserHandle userHandle) {
        String str = "PhoneAccountHandle";
        if (componentName == null) {
            Log.w(str, new Exception("PhoneAccountHandle has been created with null ComponentName!"));
        }
        if (userHandle == null) {
            Log.w(str, new Exception("PhoneAccountHandle has been created with null UserHandle!"));
        }
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private PhoneAccountHandle(Parcel in) {
        this((ComponentName) ComponentName.CREATOR.createFromParcel(in), in.readString(), (UserHandle) UserHandle.CREATOR.createFromParcel(in));
    }

    public static boolean areFromSamePackage(PhoneAccountHandle a, PhoneAccountHandle b) {
        String bPackageName = null;
        String aPackageName = a != null ? a.getComponentName().getPackageName() : null;
        if (b != null) {
            bPackageName = b.getComponentName().getPackageName();
        }
        return Objects.equals(aPackageName, bPackageName);
    }
}
