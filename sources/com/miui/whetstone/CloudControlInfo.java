package com.miui.whetstone;

import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;

public class CloudControlInfo implements Parcelable {
    public static final int CONFIG_MASK_APPEND_TOKEN_TO_URL = 1;
    public static final Creator<CloudControlInfo> CREATOR = new Creator() {
        public CloudControlInfo createFromParcel(Parcel in) {
            return new CloudControlInfo(in);
        }

        public CloudControlInfo[] newArray(int size) {
            return new CloudControlInfo[size];
        }
    };
    private static final boolean DBG = true;
    private static final int HTTP_MASK_CLASS = 2;
    private static final int HTTP_MASK_CONFIG = 512;
    private static final int HTTP_MASK_HTTP_METHOD = 16;
    private static final int HTTP_MASK_INTENT_WITH_RESPONSE = 256;
    private static final int HTTP_MASK_INTERVAL_AT_MIN = 64;
    private static final int HTTP_MASK_PACKAGE = 1;
    private static final int HTTP_MASK_PARAMS = 8;
    private static final int HTTP_MASK_TIMEOUT = 128;
    private static final int HTTP_MASK_TRIGGER_DELAY_AT_MIN = 32;
    private static final int HTTP_MASK_URL = 4;
    private static final String LOG_TAG = "CloudControlInfo";
    public String mClassName;
    public String mConfig;
    public String mHttpMethod;
    public int mIntervalAtMin;
    public boolean mIsIntentWithResponse;
    public String mParams;
    public String mPkgName;
    public int mTimeout;
    public int mTriggerDelayAtMin;
    public String mUrl;

    public CloudControlInfo(String pkgName, String className, String url, String params, String httpMethod) {
        initialize(pkgName, className, url, params, httpMethod, 0, 0, 0, false, null);
    }

    public CloudControlInfo(String pkgName, String className, String url, String params, String httpMethod, int triggerDelayAtMin, int intervalAtMin, int timeout, boolean isIntentWithResponse, String config) {
        initialize(pkgName, className, url, params, httpMethod, triggerDelayAtMin, intervalAtMin, timeout, isIntentWithResponse, config);
    }

    public CloudControlInfo(CloudControlInfo c) {
        copyFrom(c);
    }

    public void initialize(String pkgName, String className, String url, String params, String httpMethod, int triggerDelayAtMin, int intervalAtMin, int timeout, boolean isIntentWithResponse, String config) {
        this.mPkgName = pkgName;
        this.mClassName = className;
        this.mUrl = url;
        this.mParams = params;
        this.mHttpMethod = httpMethod;
        this.mTriggerDelayAtMin = triggerDelayAtMin;
        this.mIntervalAtMin = intervalAtMin;
        this.mTimeout = timeout;
        this.mIsIntentWithResponse = isIntentWithResponse;
        this.mConfig = config;
    }

    /* Access modifiers changed, original: protected */
    public void copyFrom(CloudControlInfo c) {
        this.mPkgName = c.mPkgName;
        this.mClassName = c.mClassName;
        this.mUrl = c.mUrl;
        this.mParams = c.mParams;
        this.mHttpMethod = c.mHttpMethod;
        this.mTriggerDelayAtMin = c.mTriggerDelayAtMin;
        this.mIntervalAtMin = c.mIntervalAtMin;
        this.mTimeout = c.mTimeout;
        this.mIsIntentWithResponse = c.mIsIntentWithResponse;
        this.mConfig = c.mConfig;
    }

    public CloudControlInfo(Parcel in) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Size of HttpRequest parcel:");
        stringBuilder.append(in.dataSize());
        log(stringBuilder.toString());
        int flag = in.readInt();
        if ((flag & 1) != 0) {
            this.mPkgName = in.readString();
        }
        if ((flag & 2) != 0) {
            this.mClassName = in.readString();
        }
        if ((flag & 4) != 0) {
            this.mUrl = in.readString();
        }
        if ((flag & 8) != 0) {
            this.mParams = in.readString();
        }
        if ((flag & 16) != 0) {
            this.mHttpMethod = in.readString();
        }
        if ((flag & 32) != 0) {
            this.mTriggerDelayAtMin = in.readInt();
        }
        if ((flag & 64) != 0) {
            this.mIntervalAtMin = in.readInt();
        }
        if ((flag & 128) != 0) {
            this.mTimeout = in.readInt();
        }
        if ((flag & 256) != 0) {
            this.mIsIntentWithResponse = in.readByte() != (byte) 0;
        }
        if ((flag & 512) != 0) {
            this.mConfig = in.readString();
        }
    }

    private int buildFlagValue() {
        int flag = 0;
        if (this.mPkgName != null) {
            flag = 0 | 1;
        }
        if (this.mClassName != null) {
            flag |= 2;
        }
        if (this.mUrl != null) {
            flag |= 4;
        }
        if (this.mParams != null) {
            flag |= 8;
        }
        if (this.mHttpMethod != null) {
            flag |= 16;
        }
        flag = (((flag | 32) | 64) | 128) | 256;
        if (this.mConfig != null) {
            return flag | 512;
        }
        return flag;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(buildFlagValue());
        String str = this.mPkgName;
        if (str != null) {
            out.writeString(str);
        }
        str = this.mClassName;
        if (str != null) {
            out.writeString(str);
        }
        str = this.mUrl;
        if (str != null) {
            out.writeString(str);
        }
        str = this.mParams;
        if (str != null) {
            out.writeString(str);
        }
        str = this.mHttpMethod;
        if (str != null) {
            out.writeString(str);
        }
        out.writeInt(this.mTriggerDelayAtMin);
        out.writeInt(this.mIntervalAtMin);
        out.writeInt(this.mTimeout);
        out.writeByte((byte) this.mIsIntentWithResponse);
        str = this.mConfig;
        if (str != null) {
            out.writeString(str);
        }
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object o) {
        boolean z = false;
        try {
            CloudControlInfo p = (CloudControlInfo) o;
            if (o == null) {
                return false;
            }
            if (this.mPkgName.equals(p.mPkgName) && this.mClassName.equals(p.mClassName) && this.mUrl.equals(p.mUrl) && this.mParams.equals(p.mParams) && this.mHttpMethod.equals(p.mHttpMethod) && this.mTriggerDelayAtMin == p.mTriggerDelayAtMin && this.mIntervalAtMin == p.mIntervalAtMin && this.mTimeout == p.mTimeout && this.mIsIntentWithResponse == p.mIsIntentWithResponse && this.mConfig.equals(p.mConfig)) {
                z = true;
            }
            return z;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CloudControlInfo: ");
        stringBuilder.append(this.mPkgName);
        String str = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        stringBuilder.append(str);
        stringBuilder.append(this.mClassName);
        stringBuilder.append(str);
        stringBuilder.append(this.mUrl);
        stringBuilder.append(str);
        stringBuilder.append(this.mParams);
        stringBuilder.append(str);
        stringBuilder.append(this.mHttpMethod);
        stringBuilder.append(str);
        stringBuilder.append(this.mTriggerDelayAtMin);
        stringBuilder.append(str);
        stringBuilder.append(this.mIntervalAtMin);
        stringBuilder.append(str);
        stringBuilder.append(this.mTimeout);
        stringBuilder.append(str);
        stringBuilder.append(this.mIsIntentWithResponse);
        stringBuilder.append(str);
        stringBuilder.append(this.mConfig);
        return stringBuilder.toString();
    }

    public String getComponentToShortString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append(this.mPkgName);
        stringBuilder.append("/");
        stringBuilder.append(this.mClassName);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public boolean isValid() {
        return (TextUtils.isEmpty(this.mPkgName) || TextUtils.isEmpty(this.mClassName) || TextUtils.isEmpty(this.mUrl) || this.mParams == null || TextUtils.isEmpty(this.mHttpMethod)) ? false : true;
    }

    private static void log(String s) {
        Log.w(LOG_TAG, s);
    }
}
