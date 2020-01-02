package miui.security;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.List;

public class WakePathComponent implements Parcelable {
    public static final Creator<WakePathComponent> CREATOR = new Creator<WakePathComponent>() {
        public WakePathComponent createFromParcel(Parcel in) {
            return new WakePathComponent(in, null);
        }

        public WakePathComponent[] newArray(int size) {
            return new WakePathComponent[size];
        }
    };
    public static final int WAKE_PATH_COMPONENT_ACTIVITY = 3;
    public static final int WAKE_PATH_COMPONENT_PROVIDER = 4;
    public static final int WAKE_PATH_COMPONENT_RECEIVER = 1;
    public static final int WAKE_PATH_COMPONENT_SERVICE = 2;
    private String mClassname;
    private List<String> mIntentActions;
    private int mType;

    /* synthetic */ WakePathComponent(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public WakePathComponent(int type, String classname, List<String> intentActions) throws Exception {
        this.mIntentActions = new ArrayList();
        this.mType = type;
        this.mClassname = classname;
        this.mIntentActions.addAll(intentActions);
    }

    public WakePathComponent() {
        this.mIntentActions = new ArrayList();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mType);
        dest.writeString(this.mClassname);
        dest.writeStringList(this.mIntentActions);
    }

    private WakePathComponent(Parcel in) {
        this.mIntentActions = new ArrayList();
        this.mType = in.readInt();
        this.mClassname = in.readString();
        in.readStringList(this.mIntentActions);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("WakePathComponent: mType=");
        stringBuilder.append(this.mType);
        stringBuilder.append(" mClassname=");
        stringBuilder.append(this.mClassname);
        stringBuilder.append(" mIntentActions=");
        stringBuilder.append(this.mIntentActions.toString());
        return stringBuilder.toString();
    }

    public void setType(int type) {
        this.mType = type;
    }

    public int getType() {
        return this.mType;
    }

    public void setClassname(String classname) {
        this.mClassname = classname;
    }

    public String getClassname() {
        return this.mClassname;
    }

    public void addIntentAction(String action) {
        this.mIntentActions.add(action);
    }

    public List<String> getIntentActions() {
        return this.mIntentActions;
    }
}
