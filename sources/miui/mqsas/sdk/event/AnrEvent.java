package miui.mqsas.sdk.event;

import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnrEvent extends ExceptionEvent implements Parcelable {
    public static final Creator<AnrEvent> CREATOR = new Creator<AnrEvent>() {
        public AnrEvent createFromParcel(Parcel in) {
            return new AnrEvent(in, null);
        }

        public AnrEvent[] newArray(int size) {
            return new AnrEvent[size];
        }
    };
    private static final Pattern PATTERN_ACTION = Pattern.compile("act=[^\\(\\)\\{\\}\\[\\]\\s]+");
    private static final Pattern PATTERN_COMPONENT = Pattern.compile("cmp=[^\\(\\)\\{\\}\\[\\]\\s]+");
    private static final String REGEX_DECIMAL = "\\d{1,}\\.\\d{1,}";
    private static final String REGEX_EXTRAS = "(?<=timed out \\().*?(?=Waiting)";
    private static final String REGEX_INT = "\\d{1,}";
    private static final String REGEX_STATUS = "NORMAL|BROKEN|ZOMBIE|UNKNOWN";
    private static final String REGEX_TARGET_TYPE = "touched|focused";
    private static final String REPLACEMENT = "XX";
    private static final String SPECIAL_CHARS = "\\(\\)\\{\\}\\[\\]\\s";
    private String mCpuInfo;
    private boolean mIsBgAnr;
    private String mParent;
    private String mReason;
    private String mTargetActivity;

    /* synthetic */ AnrEvent(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public AnrEvent() {
        this.mIsBgAnr = false;
        String str = "";
        this.mReason = str;
        this.mTargetActivity = str;
        this.mParent = str;
        this.mCpuInfo = str;
    }

    public void initType() {
        this.mType = 8;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mType);
        dest.writeInt(this.mPid);
        dest.writeString(this.mProcessName);
        dest.writeString(this.mPackageName);
        dest.writeInt(this.mIsSystem);
        dest.writeLong(this.mTimeStamp);
        dest.writeString(this.mSummary);
        dest.writeString(this.mDetails);
        dest.writeString(this.mDigest);
        dest.writeString(this.mLogName);
        dest.writeString(this.mKeyWord);
        dest.writeInt(this.mIsBgAnr);
        dest.writeString(this.mReason);
        dest.writeString(this.mTargetActivity);
        dest.writeString(this.mParent);
        dest.writeString(this.mCpuInfo);
        dest.writeInt(this.mIsUpload);
    }

    private AnrEvent(Parcel source) {
        this.mType = source.readInt();
        this.mPid = source.readInt();
        this.mProcessName = source.readString();
        this.mPackageName = source.readString();
        boolean z = false;
        this.mIsSystem = source.readInt() == 1;
        this.mTimeStamp = source.readLong();
        this.mSummary = source.readString();
        this.mDetails = source.readString();
        this.mDigest = source.readString();
        this.mLogName = source.readString();
        this.mKeyWord = source.readString();
        this.mIsBgAnr = source.readInt() == 1;
        this.mReason = source.readString();
        this.mTargetActivity = source.readString();
        this.mParent = source.readString();
        this.mCpuInfo = source.readString();
        if (source.readInt() == 1) {
            z = true;
        }
        this.mIsUpload = z;
    }

    public boolean getBgAnr() {
        return this.mIsBgAnr;
    }

    public void setBgAnr(boolean mIsBgAnr) {
        this.mIsBgAnr = mIsBgAnr;
    }

    public String getReason() {
        return this.mReason;
    }

    public void setReason(String mReason) {
        this.mReason = getFixedReason(mReason);
    }

    public String getTargetActivity() {
        return this.mTargetActivity;
    }

    public void setTargetActivity(String mTargetActivity) {
        this.mTargetActivity = mTargetActivity;
    }

    public String getParent() {
        return this.mParent;
    }

    public void setParent(String mParent) {
        this.mParent = mParent;
    }

    public String getCpuInfo() {
        return this.mCpuInfo;
    }

    public void setCpuInfo(String cpuInfo) {
        this.mCpuInfo = cpuInfo;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("AnrEvent { mType=");
        stringBuilder.append(this.mType);
        stringBuilder.append(" Pid=");
        stringBuilder.append(this.mPid);
        stringBuilder.append(" processName=");
        stringBuilder.append(this.mProcessName);
        stringBuilder.append(" mTimeStamp=");
        stringBuilder.append(this.mTimeStamp);
        String str = " mSummary=";
        stringBuilder.append(str);
        stringBuilder.append(this.mSummary);
        String str2 = " mDetails=";
        stringBuilder.append(str2);
        stringBuilder.append(this.mDetails);
        stringBuilder.append(" mDigest=");
        stringBuilder.append(this.mDigest);
        stringBuilder.append(str2);
        stringBuilder.append(this.mDetails);
        stringBuilder.append(str);
        stringBuilder.append(this.mSummary);
        stringBuilder.append(" mLogName=");
        stringBuilder.append(this.mLogName);
        stringBuilder.append(" mKeyWord=");
        stringBuilder.append(this.mKeyWord);
        stringBuilder.append(" mIsBgAnr=");
        stringBuilder.append(this.mIsBgAnr);
        stringBuilder.append(" mReason=");
        stringBuilder.append(this.mReason);
        stringBuilder.append(" mTargetActivity=");
        stringBuilder.append(this.mTargetActivity);
        stringBuilder.append(" mParent=");
        stringBuilder.append(this.mParent);
        stringBuilder.append(" mCpuinfo=");
        stringBuilder.append(this.mCpuInfo);
        sb.append(stringBuilder.toString());
        return sb.toString();
    }

    public String toShortString() {
        StringBuilder sb = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("AnrEvent { mType=");
        stringBuilder.append(this.mType);
        stringBuilder.append(" Pid=");
        stringBuilder.append(this.mPid);
        stringBuilder.append(" processName=");
        stringBuilder.append(this.mProcessName);
        stringBuilder.append(" mTimeStamp=");
        stringBuilder.append(this.mTimeStamp);
        stringBuilder.append(" mDigest=");
        stringBuilder.append(this.mDigest);
        stringBuilder.append(" mSummary=");
        stringBuilder.append(this.mSummary);
        stringBuilder.append(" mLogName=");
        stringBuilder.append(this.mLogName);
        stringBuilder.append(" mKeyWord=");
        stringBuilder.append(this.mKeyWord);
        stringBuilder.append(" mIsBgAnr=");
        stringBuilder.append(this.mIsBgAnr);
        stringBuilder.append(" mReason=");
        stringBuilder.append(this.mReason);
        stringBuilder.append(" mTargetActivity=");
        stringBuilder.append(this.mTargetActivity);
        stringBuilder.append(" mParent=");
        stringBuilder.append(this.mParent);
        stringBuilder.append(" mCpuinfo=");
        stringBuilder.append(this.mCpuInfo);
        sb.append(stringBuilder.toString());
        return sb.toString();
    }

    private String getBroadcastDetails() {
        StringBuilder stringBuilder;
        String stringBuilder2;
        StringBuilder details = new StringBuilder();
        Matcher act = PATTERN_ACTION.matcher(getReason());
        Matcher cmp = PATTERN_COMPONENT.matcher(getReason());
        details.append("Broadcast of Intent { ");
        boolean find = act.find();
        String str = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        String str2 = "";
        if (find) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(act.group());
            stringBuilder.append(str);
            stringBuilder2 = stringBuilder.toString();
        } else {
            stringBuilder2 = str2;
        }
        details.append(stringBuilder2);
        if (cmp.find()) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(cmp.group());
            stringBuilder.append(str);
            str2 = stringBuilder.toString();
        }
        details.append(str2);
        details.append("}");
        return details.toString();
    }

    private String getInformation() {
        String information = getReason();
        if (isBroadcastAnr()) {
            information = getBroadcastDetails();
        } else if (isInputAnr()) {
            if (!"system".equals(getProcessName())) {
                return information.replaceAll(REGEX_EXTRAS, "");
            }
        }
        return information;
    }

    public String createDefaultSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("ANR in ");
        summary.append(getProcessName());
        String str = "--";
        summary.append(str);
        if (!TextUtils.isEmpty(this.mTargetActivity)) {
            summary.append(this.mTargetActivity);
            summary.append(str);
        }
        summary.append(getInformation());
        return summary.toString();
    }

    public String createDefaultDigest() {
        StringBuilder digest = new StringBuilder();
        digest.append(getProcessName());
        String str = "--";
        digest.append(str);
        if (!TextUtils.isEmpty(this.mTargetActivity)) {
            digest.append(this.mTargetActivity);
            digest.append(str);
        }
        digest.append(getInformation());
        return digest.toString();
    }

    public String createDefaultDetails() {
        StringBuilder detailBuilder = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("bgAnr=");
        stringBuilder.append(getBgAnr());
        stringBuilder.append("@@@");
        detailBuilder.append(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("type=");
        stringBuilder.append(getAnrType());
        detailBuilder.append(stringBuilder.toString());
        return detailBuilder.toString();
    }

    private String getAnrType() {
        if (isInputAnr()) {
            return "input";
        }
        if (isServiceAnr()) {
            return "service";
        }
        if (isBroadcastAnr()) {
            return "broadcast";
        }
        if (isProviderAnr()) {
            return "provider";
        }
        return "unknown";
    }

    public boolean isInputAnr() {
        if (TextUtils.isEmpty(this.mReason) || !this.mReason.contains("Input dispatching timed out")) {
            return false;
        }
        return true;
    }

    public boolean isServiceAnr() {
        if (TextUtils.isEmpty(this.mReason) || !this.mReason.contains("executing service")) {
            return false;
        }
        return true;
    }

    public boolean isBroadcastAnr() {
        if (TextUtils.isEmpty(this.mReason) || !this.mReason.contains("Broadcast of")) {
            return false;
        }
        return true;
    }

    public boolean isProviderAnr() {
        if (TextUtils.isEmpty(this.mReason) || !this.mReason.contains("ContentProvider not responding")) {
            return false;
        }
        return true;
    }

    private String getFixedReason(String reason) {
        String newStr = REPLACEMENT;
        if (!TextUtils.isEmpty(reason) && reason.startsWith("Input dispatching timed out")) {
            try {
                return reason.replaceAll(REGEX_DECIMAL, newStr).replaceAll(REGEX_INT, newStr).replaceAll(REGEX_TARGET_TYPE, newStr).replaceAll(REGEX_STATUS, newStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return reason;
    }
}
