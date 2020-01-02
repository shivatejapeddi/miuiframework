package android.content.pm;

import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

public final class ModuleInfo implements Parcelable {
    public static final Creator<ModuleInfo> CREATOR = new Creator<ModuleInfo>() {
        public ModuleInfo createFromParcel(Parcel source) {
            return new ModuleInfo(source, null);
        }

        public ModuleInfo[] newArray(int size) {
            return new ModuleInfo[size];
        }
    };
    private boolean mHidden;
    private CharSequence mName;
    private String mPackageName;

    /* synthetic */ ModuleInfo(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public ModuleInfo(ModuleInfo orig) {
        this.mName = orig.mName;
        this.mPackageName = orig.mPackageName;
        this.mHidden = orig.mHidden;
    }

    public ModuleInfo setName(CharSequence name) {
        this.mName = name;
        return this;
    }

    public CharSequence getName() {
        return this.mName;
    }

    public ModuleInfo setPackageName(String packageName) {
        this.mPackageName = packageName;
        return this;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public ModuleInfo setHidden(boolean hidden) {
        this.mHidden = hidden;
        return this;
    }

    public boolean isHidden() {
        return this.mHidden;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ModuleInfo{");
        stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
        stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        stringBuilder.append(this.mName);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public int describeContents() {
        return 0;
    }

    public int hashCode() {
        return (((((0 * 31) + Objects.hashCode(this.mName)) * 31) + Objects.hashCode(this.mPackageName)) * 31) + Boolean.hashCode(this.mHidden);
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (!(obj instanceof ModuleInfo)) {
            return false;
        }
        ModuleInfo other = (ModuleInfo) obj;
        if (Objects.equals(this.mName, other.mName) && Objects.equals(this.mPackageName, other.mPackageName) && this.mHidden == other.mHidden) {
            z = true;
        }
        return z;
    }

    public void writeToParcel(Parcel dest, int parcelableFlags) {
        dest.writeCharSequence(this.mName);
        dest.writeString(this.mPackageName);
        dest.writeBoolean(this.mHidden);
    }

    private ModuleInfo(Parcel source) {
        this.mName = source.readCharSequence();
        this.mPackageName = source.readString();
        this.mHidden = source.readBoolean();
    }
}
