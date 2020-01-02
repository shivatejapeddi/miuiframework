package miui.process;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class PreloadProcessData implements Parcelable {
    public static final Creator<PreloadProcessData> CREATOR = new Creator<PreloadProcessData>() {
        public PreloadProcessData createFromParcel(Parcel in) {
            return new PreloadProcessData(in);
        }

        public PreloadProcessData[] newArray(int size) {
            return new PreloadProcessData[size];
        }
    };
    private Intent intent;
    private String packageName;
    private boolean startActivity;

    public PreloadProcessData(String packageName, boolean startActivity, Intent intent) {
        this.packageName = packageName;
        this.startActivity = startActivity;
        this.intent = intent;
    }

    protected PreloadProcessData(Parcel in) {
        this.packageName = in.readString();
        this.startActivity = in.readByte() != (byte) 0;
        this.intent = (Intent) in.readParcelable(Intent.class.getClassLoader());
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.packageName);
        dest.writeByte((byte) this.startActivity);
        dest.writeParcelable(this.intent, flags);
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean startActivity() {
        return this.startActivity;
    }

    public void startActivity(boolean startActivity) {
        this.startActivity = startActivity;
    }

    public Intent getIntent() {
        return this.intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }
}
