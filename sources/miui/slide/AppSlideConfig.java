package miui.slide;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class AppSlideConfig implements Parcelable {
    public static final Creator<AppSlideConfig> CREATOR = new Creator<AppSlideConfig>() {
        public AppSlideConfig createFromParcel(Parcel in) {
            return new AppSlideConfig(in, null);
        }

        public AppSlideConfig[] newArray(int size) {
            return new AppSlideConfig[size];
        }
    };
    public static final String TAG = "AppSlideConfig";
    public String mPackageName;
    public View mRecentClickView;
    private final List<SlideConfig> mSlideConfigArray;

    /* synthetic */ AppSlideConfig(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public AppSlideConfig() {
        this.mSlideConfigArray = new ArrayList();
    }

    public AppSlideConfig(String packageName) {
        this.mSlideConfigArray = new ArrayList();
        this.mPackageName = packageName;
    }

    private AppSlideConfig(Parcel in) {
        this.mSlideConfigArray = new ArrayList();
        this.mPackageName = in.readString();
        in.readTypedList(this.mSlideConfigArray, SlideConfig.CREATOR);
    }

    public void addSlideConfig(SlideConfig config) {
        this.mSlideConfigArray.add(config);
    }

    public boolean matchStartingActivity(String activityClassName) {
        for (SlideConfig config : this.mSlideConfigArray) {
            if (config.mStartingActivity.equals(activityClassName)) {
                return true;
            }
        }
        return false;
    }

    public void matchVersionSlideConfig(int versionCode) {
        List<SlideConfig> slideOpenConfigArray = new ArrayList();
        List<SlideConfig> slideCloseConfigArray = new ArrayList();
        for (SlideConfig slideConfig : this.mSlideConfigArray) {
            if (slideConfig.mKeyCode == 700) {
                slideOpenConfigArray.add(slideConfig);
            } else if (slideConfig.mKeyCode == 701) {
                slideCloseConfigArray.add(slideConfig);
            }
        }
        List<SlideConfig> matchSlideOpenConfigArray = new ArrayList();
        int mMatchedOpenVersionCode = 0;
        for (SlideConfig slideConfig2 : slideOpenConfigArray) {
            if (versionCode >= slideConfig2.mVersionCode) {
                if (mMatchedOpenVersionCode < slideConfig2.mVersionCode) {
                    matchSlideOpenConfigArray.clear();
                    matchSlideOpenConfigArray.add(slideConfig2);
                    mMatchedOpenVersionCode = slideConfig2.mVersionCode;
                } else if (mMatchedOpenVersionCode == slideConfig2.mVersionCode) {
                    matchSlideOpenConfigArray.add(slideConfig2);
                }
            }
        }
        List<SlideConfig> matchSlideCloseConfigArray = new ArrayList();
        int mMatchedCloseVersionCode = 0;
        for (SlideConfig slideConfig3 : slideCloseConfigArray) {
            if (versionCode >= slideConfig3.mVersionCode) {
                if (mMatchedCloseVersionCode < slideConfig3.mVersionCode) {
                    matchSlideCloseConfigArray.clear();
                    matchSlideCloseConfigArray.add(slideConfig3);
                    mMatchedCloseVersionCode = slideConfig3.mVersionCode;
                } else if (mMatchedCloseVersionCode == slideConfig3.mVersionCode) {
                    matchSlideCloseConfigArray.add(slideConfig3);
                }
            }
        }
        this.mSlideConfigArray.clear();
        this.mSlideConfigArray.addAll(matchSlideOpenConfigArray);
        this.mSlideConfigArray.addAll(matchSlideCloseConfigArray);
    }

    public int tryGotoTarget(int keyCode, Activity activity, View decorView) {
        int result = 0;
        for (SlideConfig config : this.mSlideConfigArray) {
            if (config.mKeyCode == keyCode && config.mStartingActivity.equals(activity.getComponentName().getClassName())) {
                result = config.tryGotoTarget(activity, decorView);
                if (result > 0) {
                    break;
                }
            }
        }
        return result;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mPackageName);
        dest.writeTypedList(this.mSlideConfigArray);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("AppSlideConfig{mPackageName='");
        stringBuilder.append(this.mPackageName);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
