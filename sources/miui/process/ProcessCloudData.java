package miui.process;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ProcessCloudData implements Parcelable {
    public static final Creator<ProcessCloudData> CREATOR = new Creator<ProcessCloudData>() {
        public ProcessCloudData createFromParcel(Parcel in) {
            return new ProcessCloudData(in);
        }

        public ProcessCloudData[] newArray(int size) {
            return new ProcessCloudData[size];
        }
    };
    private Map<String, Integer> mAppProtectMap;
    private Map<String, Integer> mCameraMemThresholdMap;
    private List<String> mCloudWhiteList;
    private List<String> mFastBootList;
    private Map<String, Integer> mFgProtectMap;
    private List<String> mSecretlyProtectAppList;

    protected ProcessCloudData(Parcel in) {
        readFromParcel(in);
    }

    public List<String> getCloudWhiteList() {
        return this.mCloudWhiteList;
    }

    public void setCloudWhiteList(List<String> mCloudWhiteList) {
        this.mCloudWhiteList = mCloudWhiteList;
    }

    public Map<String, Integer> getAppProtectMap() {
        return this.mAppProtectMap;
    }

    public void setAppProtectMap(Map<String, Integer> mAppProtectMap) {
        this.mAppProtectMap = mAppProtectMap;
    }

    public Map<String, Integer> getFgProtectMap() {
        return this.mFgProtectMap;
    }

    public void setFgProtectMap(Map<String, Integer> mFgProtectMap) {
        this.mFgProtectMap = mFgProtectMap;
    }

    public List<String> getFastBootList() {
        return this.mFastBootList;
    }

    public void setFastBootList(List<String> mFastBootList) {
        this.mFastBootList = mFastBootList;
    }

    public Map<String, Integer> getCameraMemThresholdMap() {
        return this.mCameraMemThresholdMap;
    }

    public void setCameraMemThresholdMap(Map<String, Integer> cameraMemThresholdMap) {
        this.mCameraMemThresholdMap = cameraMemThresholdMap;
    }

    public List<String> getSecretlyProtectAppList() {
        return this.mSecretlyProtectAppList;
    }

    public void setSecretlyProtectAppList(List<String> secretlyProtectAppList) {
        this.mSecretlyProtectAppList = secretlyProtectAppList;
    }

    public void readFromParcel(Parcel source) {
        int i;
        String key;
        int value;
        if (source.readInt() != 0) {
            this.mCloudWhiteList = source.readArrayList(List.class.getClassLoader());
        }
        int size = source.readInt();
        for (i = 0; i < size; i++) {
            key = source.readString();
            value = source.readInt();
            if (this.mAppProtectMap == null) {
                this.mAppProtectMap = new HashMap();
            }
            this.mAppProtectMap.put(key, Integer.valueOf(value));
        }
        size = source.readInt();
        for (i = 0; i < size; i++) {
            key = source.readString();
            value = source.readInt();
            if (this.mFgProtectMap == null) {
                this.mFgProtectMap = new HashMap();
            }
            this.mFgProtectMap.put(key, Integer.valueOf(value));
        }
        if (source.readInt() != 0) {
            this.mFastBootList = source.readArrayList(List.class.getClassLoader());
        }
        size = source.readInt();
        for (i = 0; i < size; i++) {
            key = source.readString();
            value = source.readInt();
            if (this.mCameraMemThresholdMap == null) {
                this.mCameraMemThresholdMap = new HashMap();
            }
            this.mCameraMemThresholdMap.put(key, Integer.valueOf(value));
        }
        if (source.readInt() != 0) {
            this.mSecretlyProtectAppList = source.readArrayList(List.class.getClassLoader());
        }
    }

    public void writeToParcel(Parcel dest, int flags) {
        if (this.mCloudWhiteList != null) {
            dest.writeInt(1);
            dest.writeList(this.mCloudWhiteList);
        } else {
            dest.writeInt(0);
        }
        Map map = this.mAppProtectMap;
        if (map != null) {
            dest.writeInt(map.size());
            for (Entry<String, Integer> entry : this.mAppProtectMap.entrySet()) {
                dest.writeString((String) entry.getKey());
                dest.writeInt(((Integer) entry.getValue()).intValue());
            }
        } else {
            dest.writeInt(0);
        }
        map = this.mFgProtectMap;
        if (map != null) {
            dest.writeInt(map.size());
            for (Entry<String, Integer> entry2 : this.mFgProtectMap.entrySet()) {
                dest.writeString((String) entry2.getKey());
                dest.writeInt(((Integer) entry2.getValue()).intValue());
            }
        } else {
            dest.writeInt(0);
        }
        if (this.mFastBootList != null) {
            dest.writeInt(1);
            dest.writeList(this.mFastBootList);
        } else {
            dest.writeInt(0);
        }
        map = this.mCameraMemThresholdMap;
        if (map != null) {
            dest.writeInt(map.size());
            for (Entry<String, Integer> entry22 : this.mCameraMemThresholdMap.entrySet()) {
                dest.writeString((String) entry22.getKey());
                dest.writeInt(((Integer) entry22.getValue()).intValue());
            }
        } else {
            dest.writeInt(0);
        }
        if (this.mSecretlyProtectAppList != null) {
            dest.writeInt(1);
            dest.writeList(this.mSecretlyProtectAppList);
            return;
        }
        dest.writeInt(0);
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        String packageName;
        Integer priorityLevel;
        StringBuilder sb = new StringBuilder();
        sb.append("ProcessCloudData{");
        if (this.mCloudWhiteList != null) {
            sb.append("mCloudWhiteList=");
            sb.append(Arrays.toString(this.mCloudWhiteList.toArray()));
        }
        String str = ",priorityLevel=";
        String str2 = "} ";
        String str3 = "{packageName=";
        if (this.mAppProtectMap != null) {
            sb.append(" mAppProtectMap=");
            for (Entry<String, Integer> entry : this.mAppProtectMap.entrySet()) {
                packageName = (String) entry.getKey();
                priorityLevel = (Integer) entry.getValue();
                if (!(packageName == null || priorityLevel == null)) {
                    sb.append(str3);
                    sb.append(packageName);
                    sb.append(str);
                    sb.append(priorityLevel);
                    sb.append(str2);
                }
            }
        }
        if (this.mFgProtectMap != null) {
            sb.append(" mFgProtectMap=");
            for (Entry<String, Integer> entry2 : this.mFgProtectMap.entrySet()) {
                packageName = (String) entry2.getKey();
                priorityLevel = (Integer) entry2.getValue();
                if (!(packageName == null || priorityLevel == null)) {
                    sb.append(str3);
                    sb.append(packageName);
                    sb.append(str);
                    sb.append(priorityLevel);
                    sb.append(str2);
                }
            }
        }
        if (this.mFastBootList != null) {
            sb.append(" mFastBootList=");
            sb.append(Arrays.toString(this.mFastBootList.toArray()));
        }
        if (this.mCameraMemThresholdMap != null) {
            sb.append(" mCameraMemThresholdMap=");
            for (Entry<String, Integer> entry3 : this.mCameraMemThresholdMap.entrySet()) {
                String packageName2 = (String) entry3.getKey();
                Integer threshold = (Integer) entry3.getValue();
                if (!(packageName2 == null || threshold == null)) {
                    sb.append(str3);
                    sb.append(packageName2);
                    sb.append(",sizeLimit=");
                    sb.append(threshold.intValue() / 1024);
                    sb.append("MB");
                    sb.append(str2);
                }
            }
        }
        if (this.mSecretlyProtectAppList != null) {
            sb.append(" mSecretlyProtectAppList=");
            sb.append(Arrays.toString(this.mSecretlyProtectAppList.toArray()));
        }
        sb.append("}");
        return sb.toString();
    }
}
