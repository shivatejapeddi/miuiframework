package miui.contentcatcher.sdk.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FeatureInfo implements Parcelable {
    public static final Creator<FeatureInfo> CREATOR = new Creator<FeatureInfo>() {
        public FeatureInfo createFromParcel(Parcel in) {
            return new FeatureInfo(in);
        }

        public FeatureInfo[] newArray(int size) {
            return new FeatureInfo[size];
        }
    };
    public static final String KEY_BLACKLIST = "blacklist";
    public static final String KEY_CATCHER_NAME = "catchers";
    public static final String KEY_WHITELIST = "whitelist";
    public static final String NAME_VALUE_FAV_MODE = "fav_mode";
    public static final String NAME_VALUE_PICK_MODE = "pick_mode";
    public boolean enable = true;
    public String jobTag;
    public HashMap<String, Object> mParams = new HashMap();
    public String name;
    public String target;

    public List<String> getCatcherNameList() {
        HashMap hashMap = this.mParams;
        String str = KEY_CATCHER_NAME;
        if (!hashMap.containsKey(str)) {
            return null;
        }
        String catcherNamesString = (String) this.mParams.get(str);
        if (TextUtils.isEmpty(catcherNamesString)) {
            return null;
        }
        return Arrays.asList(catcherNamesString.split(","));
    }

    public Set<String> getBlackList() {
        HashMap hashMap = this.mParams;
        String str = KEY_BLACKLIST;
        if (!hashMap.containsKey(str)) {
            return null;
        }
        String blackListString = (String) this.mParams.get(str);
        if (TextUtils.isEmpty(blackListString)) {
            return null;
        }
        return new HashSet(Arrays.asList(blackListString.split(",")));
    }

    public Set<String> getWhiteList() {
        HashMap hashMap = this.mParams;
        String str = KEY_WHITELIST;
        if (!hashMap.containsKey(str)) {
            return null;
        }
        String whiteListString = (String) this.mParams.get(str);
        if (TextUtils.isEmpty(whiteListString)) {
            return null;
        }
        return new HashSet(Arrays.asList(whiteListString.split(",")));
    }

    public boolean isMatched(String packageName, String activityName, int version) {
        Set<String> whiteList = getWhiteList();
        if (whiteList != null && !whiteList.isEmpty()) {
            return whiteList.contains(packageName);
        }
        Set<String> blackList = getBlackList();
        if (blackList == null || blackList.isEmpty()) {
            return true;
        }
        return 1 ^ blackList.contains(packageName);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{name:");
        stringBuilder.append(this.name);
        stringBuilder.append(", enable:");
        stringBuilder.append(this.enable);
        String str = ",";
        stringBuilder.append(str);
        buffer.append(stringBuilder.toString());
        for (String name : this.mParams.keySet()) {
            String valueObj = this.mParams.get(name);
            String value = "non-string";
            if (valueObj == null) {
                value = null;
            } else if (valueObj instanceof String) {
                value = valueObj;
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(name);
            stringBuilder2.append(":");
            stringBuilder2.append(value);
            stringBuilder2.append(str);
            buffer.append(stringBuilder2.toString());
        }
        if (buffer.charAt(buffer.length() - 1) == ',') {
            buffer.setLength(buffer.length() - 1);
        }
        buffer.append("}");
        return buffer.toString();
    }

    public FeatureInfo(Parcel parcel) {
        boolean z = true;
        this.name = parcel.readString();
        this.target = parcel.readString();
        this.jobTag = parcel.readString();
        if (parcel.readInt() != 1) {
            z = false;
        }
        this.enable = z;
        this.mParams = parcel.readHashMap(ClassLoader.getSystemClassLoader());
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.target);
        dest.writeString(this.jobTag);
        dest.writeInt(this.enable);
        dest.writeMap(this.mParams);
    }
}
