package miui.contentcatcher.sdk;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.HashMap;

public class ClientToken implements Parcelable {
    public static final Creator<ClientToken> CREATOR = new Creator<ClientToken>() {
        public ClientToken createFromParcel(Parcel in) {
            return new ClientToken(in);
        }

        public ClientToken[] newArray(int size) {
            return new ClientToken[size];
        }
    };
    private String mJobTag;
    private HashMap<String, String> mParams = new HashMap();
    private String mPkgName;

    public ClientToken(String packageName) {
        this.mPkgName = packageName;
    }

    public ClientToken(Parcel source) {
        this.mPkgName = source.readString();
        this.mJobTag = source.readString();
        this.mParams = source.readHashMap(ClassLoader.getSystemClassLoader());
    }

    public void setJobTag(String jobTag) {
        this.mJobTag = jobTag;
    }

    public void setParams(HashMap<String, String> params) {
        this.mParams = params;
    }

    public String getPkgName() {
        return this.mPkgName;
    }

    public String getJobTag() {
        return this.mJobTag;
    }

    public HashMap<String, String> getParams() {
        return this.mParams;
    }

    public boolean isMatch(ClientToken token) {
        return this == token || (token != null && token.getPkgName().equals(this.mPkgName) && token.getJobTag().equals(this.mJobTag));
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mPkgName);
        dest.writeString(this.mJobTag);
        dest.writeMap(this.mParams);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Token {pkg name:");
        stringBuilder.append(this.mPkgName);
        stringBuilder.append(", mJobTag:");
        stringBuilder.append(this.mJobTag);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public int hashCode() {
        String str = this.mPkgName;
        int i = 0;
        int hashCode = str == null ? 0 : str.hashCode() * 47;
        String str2 = this.mJobTag;
        if (str2 != null) {
            i = str2.hashCode() * 67;
        }
        return hashCode + i;
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (!(obj instanceof ClientToken)) {
            return false;
        }
        if (hashCode() == obj.hashCode()) {
            z = true;
        }
        return z;
    }
}
