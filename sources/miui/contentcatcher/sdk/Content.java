package miui.contentcatcher.sdk;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.service.quicksettings.TileService;
import android.text.TextUtils;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Content implements Parcelable {
    public static final Creator<Content> CREATOR = new Creator<Content>() {
        public Content createFromParcel(Parcel in) {
            return new Content(in, null);
        }

        public Content[] newArray(int size) {
            return new Content[size];
        }
    };
    public static String JOB = "job";
    public static String TARGET = "target";
    public static String TOKEN_KEY = TileService.EXTRA_TOKEN;
    public int mCatchType;
    public String mCatcherToken;
    public Intent mFavIntent;
    public Token mInjectorToken;
    public String mJob;
    public Map<String, Object> mResultMap;
    public String mTarget;

    public Content(int catchType, String catcherToken, Token injectorToken, Map<String, Object> resultMap) {
        this.mCatchType = 0;
        this.mCatcherToken = null;
        this.mInjectorToken = null;
        this.mResultMap = null;
        this.mFavIntent = null;
        String str = "";
        this.mTarget = str;
        this.mJob = str;
        this.mCatchType = catchType;
        this.mCatcherToken = catcherToken;
        this.mInjectorToken = injectorToken;
        this.mResultMap = resultMap;
    }

    public Content(Intent favIntent, Bundle bundle) {
        this.mCatchType = 0;
        this.mCatcherToken = null;
        this.mInjectorToken = null;
        this.mResultMap = null;
        this.mFavIntent = null;
        String str = "";
        this.mTarget = str;
        this.mJob = str;
        this.mFavIntent = favIntent;
        Parcelable token = bundle.getParcelable(TOKEN_KEY);
        if (token != null && (token instanceof Token)) {
            this.mInjectorToken = (Token) token;
        }
        this.mTarget = bundle.getString(TARGET);
        this.mJob = bundle.getString(JOB);
    }

    public String toString() {
        StringBuilder stringBuilder;
        StringBuffer buff = new StringBuffer();
        buff.append("Content{ ");
        String str = "; ";
        if (this.mFavIntent != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("mFavIntent=");
            stringBuilder.append(this.mFavIntent);
            stringBuilder.append(str);
            buff.append(stringBuilder.toString());
        }
        if (this.mCatchType != 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("mCatchType=");
            stringBuilder.append(this.mCatchType);
            stringBuilder.append(str);
            buff.append(stringBuilder.toString());
        }
        if (!TextUtils.isEmpty(this.mCatcherToken)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("mCatcherToken=");
            stringBuilder.append(this.mCatcherToken);
            stringBuilder.append(str);
            buff.append(stringBuilder.toString());
        }
        if (this.mInjectorToken != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("mInjectorToken=");
            stringBuilder.append(this.mInjectorToken.getPkgName());
            String str2 = ",";
            stringBuilder.append(str2);
            stringBuilder.append(this.mInjectorToken.getActivityName());
            stringBuilder.append(str2);
            stringBuilder.append(this.mInjectorToken.getVersionCode());
            buff.append(stringBuilder.toString());
        }
        if (this.mResultMap != null) {
            buff.append("mResultMap[ ");
            for (Entry<String, Object> entry : this.mResultMap.entrySet()) {
                String resultValue = "";
                Object resultObj = entry.getValue();
                try {
                    if (resultObj instanceof Bitmap) {
                        resultValue = "bitmap";
                    } else {
                        resultValue = resultObj.toString();
                    }
                } catch (Exception e) {
                }
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append((String) entry.getKey());
                stringBuilder2.append("=");
                stringBuilder2.append(resultValue);
                stringBuilder2.append(str);
                buff.append(stringBuilder2.toString());
            }
            buff.append(" ];  ");
        }
        if (!TextUtils.isEmpty(this.mTarget)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("mTarget=");
            stringBuilder.append(this.mTarget);
            stringBuilder.append(str);
            buff.append(stringBuilder.toString());
        }
        if (!TextUtils.isEmpty(this.mJob)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("mJob=");
            stringBuilder.append(this.mJob);
            stringBuilder.append(str);
            buff.append(stringBuilder.toString());
        }
        buff.append("}");
        return buff.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.mCatchType);
        parcel.writeString(this.mCatcherToken);
        parcel.writeParcelable(this.mInjectorToken, flags);
        parcel.writeMap(this.mResultMap);
        parcel.writeParcelable(this.mFavIntent, flags);
        parcel.writeString(this.mTarget);
        parcel.writeString(this.mJob);
    }

    private Content(Parcel parcel) {
        this.mCatchType = 0;
        this.mCatcherToken = null;
        this.mInjectorToken = null;
        this.mResultMap = null;
        this.mFavIntent = null;
        String str = "";
        this.mTarget = str;
        this.mJob = str;
        this.mCatchType = parcel.readInt();
        this.mCatcherToken = parcel.readString();
        this.mInjectorToken = (Token) parcel.readParcelable(null);
        this.mResultMap = parcel.readHashMap(ClassLoader.getSystemClassLoader());
        this.mFavIntent = (Intent) parcel.readParcelable(null);
        this.mTarget = parcel.readString();
        this.mJob = parcel.readString();
    }

    public List<Object> getTargetTokens() {
        return null;
    }

    public Token getInjectorToken() {
        return this.mInjectorToken;
    }
}
