package android.content.om;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

@SystemApi
public final class OverlayInfo implements Parcelable {
    public static final String CATEGORY_THEME = "android.theme";
    public static final Creator<OverlayInfo> CREATOR = new Creator<OverlayInfo>() {
        public OverlayInfo createFromParcel(Parcel source) {
            return new OverlayInfo(source);
        }

        public OverlayInfo[] newArray(int size) {
            return new OverlayInfo[size];
        }
    };
    public static final int STATE_DISABLED = 2;
    public static final int STATE_ENABLED = 3;
    public static final int STATE_ENABLED_STATIC = 6;
    public static final int STATE_MISSING_TARGET = 0;
    public static final int STATE_NO_IDMAP = 1;
    public static final int STATE_OVERLAY_IS_BEING_REPLACED = 5;
    @Deprecated
    public static final int STATE_TARGET_IS_BEING_REPLACED = 4;
    public static final int STATE_UNKNOWN = -1;
    public final String baseCodePath;
    public final String category;
    public final boolean isStatic;
    public final String packageName;
    public final int priority;
    @UnsupportedAppUsage
    public final int state;
    public final String targetOverlayableName;
    public final String targetPackageName;
    public final int userId;

    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }

    public OverlayInfo(OverlayInfo source, int state) {
        this(source.packageName, source.targetPackageName, source.targetOverlayableName, source.category, source.baseCodePath, state, source.userId, source.priority, source.isStatic);
    }

    public OverlayInfo(String packageName, String targetPackageName, String targetOverlayableName, String category, String baseCodePath, int state, int userId, int priority, boolean isStatic) {
        this.packageName = packageName;
        this.targetPackageName = targetPackageName;
        this.targetOverlayableName = targetOverlayableName;
        this.category = category;
        this.baseCodePath = baseCodePath;
        this.state = state;
        this.userId = userId;
        this.priority = priority;
        this.isStatic = isStatic;
        ensureValidState();
    }

    public OverlayInfo(Parcel source) {
        this.packageName = source.readString();
        this.targetPackageName = source.readString();
        this.targetOverlayableName = source.readString();
        this.category = source.readString();
        this.baseCodePath = source.readString();
        this.state = source.readInt();
        this.userId = source.readInt();
        this.priority = source.readInt();
        this.isStatic = source.readBoolean();
        ensureValidState();
    }

    @SystemApi
    public String getPackageName() {
        return this.packageName;
    }

    @SystemApi
    public String getTargetPackageName() {
        return this.targetPackageName;
    }

    @SystemApi
    public String getCategory() {
        return this.category;
    }

    @SystemApi
    public int getUserId() {
        return this.userId;
    }

    @SystemApi
    public String getTargetOverlayableName() {
        return this.targetOverlayableName;
    }

    private void ensureValidState() {
        if (this.packageName == null) {
            throw new IllegalArgumentException("packageName must not be null");
        } else if (this.targetPackageName == null) {
            throw new IllegalArgumentException("targetPackageName must not be null");
        } else if (this.baseCodePath != null) {
            switch (this.state) {
                case -1:
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                    return;
                default:
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("State ");
                    stringBuilder.append(this.state);
                    stringBuilder.append(" is not a valid state");
                    throw new IllegalArgumentException(stringBuilder.toString());
            }
        } else {
            throw new IllegalArgumentException("baseCodePath must not be null");
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.packageName);
        dest.writeString(this.targetPackageName);
        dest.writeString(this.targetOverlayableName);
        dest.writeString(this.category);
        dest.writeString(this.baseCodePath);
        dest.writeInt(this.state);
        dest.writeInt(this.userId);
        dest.writeInt(this.priority);
        dest.writeBoolean(this.isStatic);
    }

    @SystemApi
    public boolean isEnabled() {
        int i = this.state;
        if (i == 3 || i == 6) {
            return true;
        }
        return false;
    }

    public static String stateToString(int state) {
        switch (state) {
            case -1:
                return "STATE_UNKNOWN";
            case 0:
                return "STATE_MISSING_TARGET";
            case 1:
                return "STATE_NO_IDMAP";
            case 2:
                return "STATE_DISABLED";
            case 3:
                return "STATE_ENABLED";
            case 4:
                return "STATE_TARGET_IS_BEING_REPLACED";
            case 5:
                return "STATE_OVERLAY_IS_BEING_REPLACED";
            case 6:
                return "STATE_ENABLED_STATIC";
            default:
                return "<unknown state>";
        }
    }

    public int hashCode() {
        int i;
        int i2 = ((((1 * 31) + this.userId) * 31) + this.state) * 31;
        String str = this.packageName;
        int i3 = 0;
        int hashCode = (i2 + (str == null ? 0 : str.hashCode())) * 31;
        str = this.targetPackageName;
        i2 = (hashCode + (str == null ? 0 : str.hashCode())) * 31;
        str = this.targetOverlayableName;
        if (str == null) {
            i = 0;
        } else {
            i = str.hashCode();
        }
        hashCode = (i2 + i) * 31;
        str = this.category;
        i2 = (hashCode + (str == null ? 0 : str.hashCode())) * 31;
        str = this.baseCodePath;
        if (str != null) {
            i3 = str.hashCode();
        }
        return i2 + i3;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        OverlayInfo other = (OverlayInfo) obj;
        if (this.userId == other.userId && this.state == other.state && this.packageName.equals(other.packageName) && this.targetPackageName.equals(other.targetPackageName) && Objects.equals(this.targetOverlayableName, other.targetOverlayableName) && Objects.equals(this.category, other.category) && this.baseCodePath.equals(other.baseCodePath)) {
            return true;
        }
        return false;
    }

    public String toString() {
        String str;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("OverlayInfo { overlay=");
        stringBuilder.append(this.packageName);
        stringBuilder.append(", targetPackage=");
        stringBuilder.append(this.targetPackageName);
        if (this.targetOverlayableName == null) {
            str = "";
        } else {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(", targetOverlayable=");
            stringBuilder2.append(this.targetOverlayableName);
            str = stringBuilder2.toString();
        }
        stringBuilder.append(str);
        stringBuilder.append(", state=");
        stringBuilder.append(this.state);
        stringBuilder.append(" (");
        stringBuilder.append(stateToString(this.state));
        stringBuilder.append("), userId=");
        stringBuilder.append(this.userId);
        stringBuilder.append(" }");
        return stringBuilder.toString();
    }
}
