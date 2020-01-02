package miui.contentcatcher.sdk.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;

public class PageConfig implements Parcelable {
    public static final Creator<PageConfig> CREATOR = new Creator<PageConfig>() {
        public PageConfig createFromParcel(Parcel in) {
            return new PageConfig(in);
        }

        public PageConfig[] newArray(int size) {
            return new PageConfig[size];
        }
    };
    private ArrayList<CatcherInfo> mCatchers = new ArrayList();
    private ArrayList<FeatureInfo> mFeatures = new ArrayList();

    public PageConfig(ArrayList<FeatureInfo> features, ArrayList<CatcherInfo> catchers) {
        this.mFeatures = features;
        this.mCatchers = catchers;
    }

    public ArrayList<FeatureInfo> getFeatures() {
        return this.mFeatures;
    }

    public void setFeatures(ArrayList<FeatureInfo> features) {
        this.mFeatures = features;
    }

    public ArrayList<CatcherInfo> getCatchers() {
        return this.mCatchers;
    }

    public void setCatchers(ArrayList<CatcherInfo> catchers) {
        this.mCatchers = catchers;
    }

    public PageConfig(Parcel in) {
        this.mFeatures = in.readArrayList(ClassLoader.getSystemClassLoader());
        this.mCatchers = in.readArrayList(ClassLoader.getSystemClassLoader());
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.mFeatures);
        dest.writeList(this.mCatchers);
    }
}
