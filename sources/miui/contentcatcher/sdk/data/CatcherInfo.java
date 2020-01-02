package miui.contentcatcher.sdk.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.HashMap;
import java.util.Map;

public class CatcherInfo implements Parcelable {
    public static final Creator<CatcherInfo> CREATOR = new Creator<CatcherInfo>() {
        public CatcherInfo createFromParcel(Parcel parcel) {
            return new CatcherInfo(parcel);
        }

        public CatcherInfo[] newArray(int size) {
            return new CatcherInfo[size];
        }
    };
    public Map<String, Object> mParams = new HashMap();
    public String name;

    public CatcherInfo(Parcel parcel) {
        this.name = parcel.readString();
        this.mParams = parcel.readHashMap(ClassLoader.getSystemClassLoader());
    }

    public void merge(CatcherInfo old) {
        if (old.name.equals(this.name)) {
            this.mParams.putAll(old.mParams);
        }
    }

    public CatcherInfo(String name, Map<String, Object> mParams) {
        this.name = name;
        this.mParams = mParams;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{name:");
        stringBuilder.append(this.name);
        String str = ",";
        stringBuilder.append(str);
        buffer.append(stringBuilder.toString());
        for (String name : this.mParams.keySet()) {
            String value = (String) this.mParams.get(name);
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

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeMap(this.mParams);
    }
}
