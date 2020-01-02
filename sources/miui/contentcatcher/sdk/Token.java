package miui.contentcatcher.sdk;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Token implements Parcelable {
    public static final Creator<Token> CREATOR = new Creator<Token>() {
        public Token createFromParcel(Parcel in) {
            return new Token(in);
        }

        public Token[] newArray(int size) {
            return new Token[size];
        }
    };
    private String mActivityHashCode;
    private String mActivityName;
    private String mPkgName;
    private int mVersionCode = -1;

    public Token() {
        String str = "";
        this.mPkgName = str;
        this.mActivityName = str;
        this.mActivityHashCode = str;
    }

    public Token(String packageName, String activityName, int versionCode) {
        String str = "";
        this.mPkgName = str;
        this.mActivityName = str;
        this.mActivityHashCode = str;
        this.mActivityName = activityName;
        this.mPkgName = packageName;
        this.mVersionCode = versionCode;
    }

    public Token(String packageName, String activityName, String activityHashCode, int versionCode) {
        String str = "";
        this.mPkgName = str;
        this.mActivityName = str;
        this.mActivityHashCode = str;
        this.mActivityName = activityName;
        this.mPkgName = packageName;
        this.mVersionCode = versionCode;
        this.mActivityHashCode = activityHashCode;
    }

    public Token(Parcel source) {
        String str = "";
        this.mPkgName = str;
        this.mActivityName = str;
        this.mActivityHashCode = str;
        this.mVersionCode = source.readInt();
        this.mPkgName = source.readString();
        this.mActivityName = source.readString();
        this.mActivityHashCode = source.readString();
    }

    public String getPkgName() {
        return this.mPkgName;
    }

    public String getActivityName() {
        return this.mActivityName;
    }

    public String getActivityHashCode() {
        return this.mActivityHashCode;
    }

    public int getVersionCode() {
        return this.mVersionCode;
    }

    public boolean isMatch(Token token) {
        return token.getPkgName().equals(this.mPkgName) && token.getActivityName().equals(this.mActivityName) && token.getVersionCode() == this.mVersionCode && token.getActivityHashCode() == this.mActivityHashCode;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mVersionCode);
        dest.writeString(this.mPkgName);
        dest.writeString(this.mActivityName);
        dest.writeString(this.mActivityHashCode);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Token {pkg name:");
        stringBuilder.append(this.mPkgName);
        stringBuilder.append(", activity name:");
        stringBuilder.append(this.mActivityName);
        stringBuilder.append(", activityhashcode:");
        stringBuilder.append(this.mActivityHashCode);
        stringBuilder.append(", version:");
        stringBuilder.append(this.mVersionCode);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
