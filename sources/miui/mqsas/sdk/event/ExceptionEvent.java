package miui.mqsas.sdk.event;

import android.telephony.TelephonyManager;
import com.android.internal.os.Zygote;

public class ExceptionEvent {
    public static final int IMPORTANT_PROCESS = 1;
    public static final int NORMAL_PROCESS = 2;
    public static final int SEVERITY_FATAL = 0;
    public static final int SEVERITY_MAJOR = 1;
    public static final int SEVERITY_MINOR = 3;
    public static final int SEVERITY_NORMAL = 2;
    public static final int UNKWOWN_PROCESS = -1;
    public static final int VERY_IMPORTANT_PROCESS = 0;
    protected String mDetails;
    protected String mDigest;
    public final String[] mImportantProcesses = new String[]{"com.android.systemui", "com.miui.home", TelephonyManager.PHONE_PROCESS_NAME, "mediaserver"};
    protected boolean mIsSystem;
    protected boolean mIsUpload;
    protected String mKeyWord;
    protected String mLogName;
    protected String mPackageName;
    protected int mPid;
    protected String mProcessName;
    protected String mSummary;
    protected long mTimeStamp;
    protected int mType;
    public final String[] mVerytImportantProcesses = new String[]{"system_server", Zygote.PRIMARY_SOCKET_NAME, "zygote64", "surfaceflinger"};

    public int judgeProcessLevel() {
        if (this.mProcessName == null || this.mPackageName == null) {
            return -1;
        }
        int i = 0;
        for (String process : this.mVerytImportantProcesses) {
            if (process.equals(this.mProcessName) || process.equals(this.mPackageName)) {
                return 0;
            }
        }
        String[] strArr = this.mImportantProcesses;
        int length = strArr.length;
        while (i < length) {
            String process2 = strArr[i];
            if (process2.equals(this.mProcessName) || process2.equals(this.mPackageName)) {
                return 1;
            }
            i++;
        }
        return 2;
    }

    public ExceptionEvent() {
        initType();
        this.mPid = -1;
        String str = "";
        this.mProcessName = str;
        this.mPackageName = str;
        this.mIsSystem = false;
        this.mTimeStamp = -1;
        this.mSummary = str;
        this.mDetails = str;
        this.mDigest = str;
        this.mLogName = str;
        this.mKeyWord = str;
        this.mIsUpload = false;
    }

    public int getType() {
        return this.mType;
    }

    public void setType(int mType) {
        this.mType = mType;
    }

    public void initType() {
        this.mType = -1;
    }

    public int getPid() {
        return this.mPid;
    }

    public void setPid(int mPid) {
        this.mPid = mPid;
    }

    public String getProcessName() {
        return this.mProcessName;
    }

    public void setProcessName(String mProcessName) {
        this.mProcessName = mProcessName;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public void setPackageName(String mPackageName) {
        this.mPackageName = mPackageName;
    }

    public boolean isSystem() {
        return this.mIsSystem;
    }

    public void setSystem(boolean system) {
        this.mIsSystem = system;
    }

    public long getTimeStamp() {
        return this.mTimeStamp;
    }

    public void setTimeStamp(long mTimeStamp) {
        this.mTimeStamp = mTimeStamp;
    }

    public String getSummary() {
        return this.mSummary;
    }

    public void setSummary(String mSummary) {
        this.mSummary = mSummary;
    }

    public String getDetails() {
        return this.mDetails;
    }

    public void setDetails(String mDetails) {
        this.mDetails = mDetails;
    }

    public String getDigest() {
        return this.mDigest;
    }

    public void setDigest(String mDigest) {
        this.mDigest = mDigest;
    }

    public String getLogName() {
        return this.mLogName;
    }

    public void setLogName(String mLogName) {
        this.mLogName = mLogName;
    }

    public String getKeyWord() {
        return this.mKeyWord;
    }

    public void setKeyWord(String mKeyWord) {
        this.mKeyWord = mKeyWord;
    }

    public boolean isUpload() {
        return this.mIsUpload;
    }

    public void setUpload(boolean upload) {
        this.mIsUpload = upload;
    }

    public int getEventSeverity() {
        return 2;
    }
}
