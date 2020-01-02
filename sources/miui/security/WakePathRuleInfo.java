package miui.security;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Slog;

public class WakePathRuleInfo implements Parcelable {
    public static final Creator<WakePathRuleInfo> CREATOR = new Creator<WakePathRuleInfo>() {
        public WakePathRuleInfo createFromParcel(Parcel in) {
            return new WakePathRuleInfo(in, null);
        }

        public WakePathRuleInfo[] newArray(int size) {
            return new WakePathRuleInfo[size];
        }
    };
    private static final int EXPRESS_TYPE_WILDCARD_ALL = 2;
    private static final int EXPRESS_TYPE_WILDCARD_END = 1;
    private static final int EXPRESS_TYPE_WILDCARD_NONE = 0;
    private static final int EXPRESS_TYPE_WILDCARD_OTHER = 3;
    private static final String EXPRESS_WILDCARD = "*";
    private static final String TAG = WakePathRuleInfo.class.getName();
    public static final int WAKE_TYPE_ALLOW_START_ACTIVITY = 17;
    public static final int WAKE_TYPE_CALL_LIST = 16;
    public static final int WAKE_TYPE_GET_CONTENT_PROVIDER = 4;
    public static final int WAKE_TYPE_SEND_BROADCAST = 2;
    public static final int WAKE_TYPE_START_ACTIVITY = 1;
    public static final int WAKE_TYPE_START_SERVICE = 8;
    public String mActionExpress;
    private int mActionExpressType;
    public String mCalleeExpress;
    private int mCalleeExpressType;
    public String mCallerExpress;
    private int mCallerExpressType;
    public String mClassNameExpress;
    private int mClassNameExpressType;
    public int mHashCode;
    public int mUserSettings;
    public int mWakeType;

    public static class UserSettings {
        public static final int ACCEPT = 1;
        public static final int REJECT = 2;
        public static final int UNDEF = 0;
    }

    /* synthetic */ WakePathRuleInfo(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public WakePathRuleInfo(String actionExpress, String classNameExpress, String callerExpress, String calleeExpress, int wakeType, int userSettings) throws Exception {
        this.mActionExpress = actionExpress;
        this.mActionExpressType = getExpressType(this.mActionExpress);
        this.mClassNameExpress = classNameExpress;
        this.mClassNameExpressType = getExpressType(this.mClassNameExpress);
        this.mCallerExpress = callerExpress;
        this.mCallerExpressType = getExpressType(this.mCallerExpress);
        this.mCalleeExpress = calleeExpress;
        this.mCalleeExpressType = getExpressType(this.mCalleeExpress);
        this.mWakeType = wakeType;
        this.mUserSettings = userSettings;
        if (this.mWakeType == 16) {
            this.mHashCode = getHashCode(actionExpress, classNameExpress, callerExpress, calleeExpress);
        } else {
            this.mHashCode = 0;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mActionExpress);
        dest.writeInt(this.mActionExpressType);
        dest.writeString(this.mClassNameExpress);
        dest.writeInt(this.mClassNameExpressType);
        dest.writeString(this.mCallerExpress);
        dest.writeInt(this.mCallerExpressType);
        dest.writeString(this.mCalleeExpress);
        dest.writeInt(this.mCalleeExpressType);
        dest.writeInt(this.mWakeType);
        dest.writeInt(this.mUserSettings);
        dest.writeInt(this.mHashCode);
    }

    private WakePathRuleInfo(Parcel in) {
        this.mActionExpress = in.readString();
        this.mActionExpressType = in.readInt();
        this.mClassNameExpress = in.readString();
        this.mClassNameExpressType = in.readInt();
        this.mCallerExpress = in.readString();
        this.mCallerExpressType = in.readInt();
        this.mCalleeExpress = in.readString();
        this.mCalleeExpressType = in.readInt();
        this.mWakeType = in.readInt();
        this.mUserSettings = in.readInt();
        this.mHashCode = in.readInt();
    }

    public boolean equals(Object o) {
        boolean z = false;
        if (o == null) {
            return false;
        }
        try {
            WakePathRuleInfo info = (WakePathRuleInfo) o;
            if (TextUtils.equals(this.mActionExpress, info.mActionExpress) && TextUtils.equals(this.mClassNameExpress, info.mClassNameExpress) && TextUtils.equals(this.mCallerExpress, info.mCallerExpress) && TextUtils.equals(this.mCalleeExpress, info.mCalleeExpress) && this.mWakeType == info.mWakeType) {
                z = true;
            }
            return z;
        } catch (ClassCastException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean equals(String actionExpress, String classNameExpress, String callerExpress, String calleeExpress, int wakeType) {
        if ((this.mWakeType & wakeType) != 0 && expressCompare(this.mActionExpress, this.mActionExpressType, actionExpress) && expressCompare(this.mClassNameExpress, this.mClassNameExpressType, classNameExpress) && expressCompare(this.mCallerExpress, this.mCallerExpressType, callerExpress) && expressCompare(this.mCalleeExpress, this.mCalleeExpressType, calleeExpress)) {
            return true;
        }
        return false;
    }

    public boolean equals(int hashCode) {
        boolean z = false;
        if (this.mWakeType != 16 || hashCode == 0) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("MIUILOG-WAKEPATH equals: Invalid parameter!! mWakeType=");
            stringBuilder.append(this.mWakeType);
            stringBuilder.append(" hashCode=");
            stringBuilder.append(hashCode);
            Slog.w(str, stringBuilder.toString());
            return false;
        }
        if (this.mHashCode == hashCode) {
            z = true;
        }
        return z;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("WakePathRuleInfo: mActionExpress=");
        stringBuilder.append(this.mActionExpress);
        stringBuilder.append(" mClassNameExpress=");
        stringBuilder.append(this.mClassNameExpress);
        stringBuilder.append(" mCallerExpress=");
        stringBuilder.append(this.mCallerExpress);
        stringBuilder.append(" mCalleeExpress= ");
        stringBuilder.append(this.mCalleeExpress);
        stringBuilder.append(" mWakeType=");
        stringBuilder.append(this.mWakeType);
        stringBuilder.append(" userSettings=");
        stringBuilder.append(this.mUserSettings);
        return stringBuilder.toString();
    }

    public static int getHashCode(String actionExpress, String classNameExpress, String callerExpress, String calleeExpress) {
        StringBuffer strBuffer = new StringBuffer();
        strBuffer.append(actionExpress);
        strBuffer.append(classNameExpress);
        strBuffer.append(callerExpress);
        strBuffer.append(calleeExpress);
        return strBuffer.toString().hashCode();
    }

    private static int getExpressType(String express) {
        if (TextUtils.isEmpty(express)) {
            return 0;
        }
        String str = "*";
        if (express.equals(str)) {
            return 2;
        }
        if (express.endsWith(str)) {
            return 1;
        }
        if (express.contains(str)) {
            return 3;
        }
        return 0;
    }

    /* JADX WARNING: Missing block: B:22:0x0046, code skipped:
            return false;
     */
    private static boolean expressCompare(java.lang.String r6, int r7, java.lang.String r8) {
        /*
        r0 = 1;
        r1 = 0;
        if (r7 == 0) goto L_0x0065;
    L_0x0004:
        if (r7 == r0) goto L_0x0048;
    L_0x0006:
        r2 = 3;
        if (r7 == r2) goto L_0x000a;
    L_0x0009:
        goto L_0x006c;
    L_0x000a:
        r2 = android.text.TextUtils.isEmpty(r8);
        if (r2 != 0) goto L_0x0047;
    L_0x0010:
        r2 = android.text.TextUtils.isEmpty(r6);
        if (r2 == 0) goto L_0x0017;
    L_0x0016:
        goto L_0x0047;
    L_0x0017:
        r2 = "*";
        r2 = r6.indexOf(r2);
        r3 = -1;
        if (r2 != r3) goto L_0x0021;
    L_0x0020:
        return r1;
    L_0x0021:
        r3 = r6.substring(r1, r2);
        r4 = r2 + 1;
        r4 = r6.substring(r4);
        r5 = android.text.TextUtils.isEmpty(r3);
        if (r5 != 0) goto L_0x0046;
    L_0x0031:
        r5 = android.text.TextUtils.isEmpty(r4);
        if (r5 == 0) goto L_0x0038;
    L_0x0037:
        goto L_0x0046;
    L_0x0038:
        r5 = r8.startsWith(r3);
        if (r5 != 0) goto L_0x003f;
    L_0x003e:
        return r1;
    L_0x003f:
        r5 = r8.endsWith(r4);
        if (r5 != 0) goto L_0x006c;
    L_0x0045:
        return r1;
    L_0x0046:
        return r1;
    L_0x0047:
        return r1;
    L_0x0048:
        r2 = r6.length();
        r3 = 2;
        if (r2 < r3) goto L_0x0058;
    L_0x004f:
        r2 = r6.length();
        r2 = r2 - r3;
        r6 = r6.substring(r1, r2);
    L_0x0058:
        r2 = android.text.TextUtils.isEmpty(r8);
        if (r2 != 0) goto L_0x0064;
    L_0x005e:
        r2 = r8.startsWith(r6);
        if (r2 != 0) goto L_0x006c;
    L_0x0064:
        return r1;
    L_0x0065:
        r2 = android.text.TextUtils.equals(r6, r8);
        if (r2 != 0) goto L_0x006c;
    L_0x006b:
        return r1;
    L_0x006c:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.security.WakePathRuleInfo.expressCompare(java.lang.String, int, java.lang.String):boolean");
    }

    public String getCalleeExpress() {
        return this.mCalleeExpress;
    }

    public String getCallerExpress() {
        return this.mCallerExpress;
    }

    public int getUserSettings() {
        return this.mUserSettings;
    }

    public static boolean checkCompatibility(String actionExpress, String classNameExpress, String callerExpress, String calleeExpress, int wakeType) {
        if (wakeType == 17) {
            return true;
        }
        String str = "*";
        if ((TextUtils.equals(actionExpress, str) && TextUtils.equals(classNameExpress, str) && TextUtils.equals(callerExpress, str) && TextUtils.equals(calleeExpress, str)) || ((actionExpress.length() + classNameExpress.length()) + callerExpress.length()) + calleeExpress.length() < 10) {
            return false;
        }
        String str2 = "com.miui.home";
        if (TextUtils.equals(callerExpress, str2) || TextUtils.equals(calleeExpress, str2)) {
            return false;
        }
        str2 = "android";
        if (TextUtils.equals(callerExpress, str2) || TextUtils.equals(calleeExpress, str2)) {
            if (TextUtils.equals(calleeExpress, str2)) {
                return false;
            }
            if (TextUtils.equals(actionExpress, str) && TextUtils.equals(classNameExpress, str)) {
                return false;
            }
            return true;
        }
        return true;
    }
}
