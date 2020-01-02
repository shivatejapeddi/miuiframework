package android.bluetooth.le;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class ScanSettings implements Parcelable {
    public static final int CALLBACK_TYPE_ALL_MATCHES = 1;
    public static final int CALLBACK_TYPE_FIRST_MATCH = 2;
    public static final int CALLBACK_TYPE_MATCH_LOST = 4;
    public static final int CALLBACK_TYPE_SENSOR_ROUTING = 8;
    public static final Creator<ScanSettings> CREATOR = new Creator<ScanSettings>() {
        public ScanSettings[] newArray(int size) {
            return new ScanSettings[size];
        }

        public ScanSettings createFromParcel(Parcel in) {
            return new ScanSettings(in, null);
        }
    };
    public static final int MATCH_MODE_AGGRESSIVE = 1;
    public static final int MATCH_MODE_STICKY = 2;
    public static final int MATCH_NUM_FEW_ADVERTISEMENT = 2;
    public static final int MATCH_NUM_MAX_ADVERTISEMENT = 3;
    public static final int MATCH_NUM_ONE_ADVERTISEMENT = 1;
    public static final int PHY_LE_ALL_SUPPORTED = 255;
    public static final int SCAN_MODE_BALANCED = 1;
    public static final int SCAN_MODE_LOW_LATENCY = 2;
    public static final int SCAN_MODE_LOW_POWER = 0;
    public static final int SCAN_MODE_OPPORTUNISTIC = -1;
    @SystemApi
    public static final int SCAN_RESULT_TYPE_ABBREVIATED = 1;
    @SystemApi
    public static final int SCAN_RESULT_TYPE_FULL = 0;
    private int mCallbackType;
    private boolean mLegacy;
    private int mMatchMode;
    private int mNumOfMatchesPerFilter;
    private int mPhy;
    private long mReportDelayMillis;
    private int mScanMode;
    private int mScanResultType;

    public static final class Builder {
        private int mCallbackType = 1;
        private boolean mLegacy = true;
        private int mMatchMode = 1;
        private int mNumOfMatchesPerFilter = 3;
        private int mPhy = 255;
        private long mReportDelayMillis = 0;
        private int mScanMode = 0;
        private int mScanResultType = 0;

        public Builder setScanMode(int scanMode) {
            if (scanMode < -1 || scanMode > 2) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("invalid scan mode ");
                stringBuilder.append(scanMode);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            this.mScanMode = scanMode;
            return this;
        }

        public Builder setCallbackType(int callbackType) {
            if (isValidCallbackType(callbackType)) {
                this.mCallbackType = callbackType;
                return this;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("invalid callback type - ");
            stringBuilder.append(callbackType);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        private boolean isValidCallbackType(int callbackType) {
            boolean z = true;
            if (callbackType == 1 || callbackType == 2 || callbackType == 4 || callbackType == 8) {
                return true;
            }
            if (callbackType != 6) {
                z = false;
            }
            return z;
        }

        @SystemApi
        public Builder setScanResultType(int scanResultType) {
            if (scanResultType < 0 || scanResultType > 1) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("invalid scanResultType - ");
                stringBuilder.append(scanResultType);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            this.mScanResultType = scanResultType;
            return this;
        }

        public Builder setReportDelay(long reportDelayMillis) {
            if (reportDelayMillis >= 0) {
                this.mReportDelayMillis = reportDelayMillis;
                return this;
            }
            throw new IllegalArgumentException("reportDelay must be > 0");
        }

        public Builder setNumOfMatches(int numOfMatches) {
            if (numOfMatches < 1 || numOfMatches > 3) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("invalid numOfMatches ");
                stringBuilder.append(numOfMatches);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            this.mNumOfMatchesPerFilter = numOfMatches;
            return this;
        }

        public Builder setMatchMode(int matchMode) {
            if (matchMode < 1 || matchMode > 2) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("invalid matchMode ");
                stringBuilder.append(matchMode);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            this.mMatchMode = matchMode;
            return this;
        }

        public Builder setLegacy(boolean legacy) {
            this.mLegacy = legacy;
            return this;
        }

        public Builder setPhy(int phy) {
            this.mPhy = phy;
            return this;
        }

        public ScanSettings build() {
            return new ScanSettings(this.mScanMode, this.mCallbackType, this.mScanResultType, this.mReportDelayMillis, this.mMatchMode, this.mNumOfMatchesPerFilter, this.mLegacy, this.mPhy, null);
        }
    }

    /* synthetic */ ScanSettings(int x0, int x1, int x2, long x3, int x4, int x5, boolean x6, int x7, AnonymousClass1 x8) {
        this(x0, x1, x2, x3, x4, x5, x6, x7);
    }

    /* synthetic */ ScanSettings(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public int getScanMode() {
        return this.mScanMode;
    }

    public int getCallbackType() {
        return this.mCallbackType;
    }

    public int getScanResultType() {
        return this.mScanResultType;
    }

    public int getMatchMode() {
        return this.mMatchMode;
    }

    public int getNumOfMatches() {
        return this.mNumOfMatchesPerFilter;
    }

    public boolean getLegacy() {
        return this.mLegacy;
    }

    public int getPhy() {
        return this.mPhy;
    }

    public long getReportDelayMillis() {
        return this.mReportDelayMillis;
    }

    private ScanSettings(int scanMode, int callbackType, int scanResultType, long reportDelayMillis, int matchMode, int numOfMatchesPerFilter, boolean legacy, int phy) {
        this.mScanMode = scanMode;
        this.mCallbackType = callbackType;
        this.mScanResultType = scanResultType;
        this.mReportDelayMillis = reportDelayMillis;
        this.mNumOfMatchesPerFilter = numOfMatchesPerFilter;
        this.mMatchMode = matchMode;
        this.mLegacy = legacy;
        this.mPhy = phy;
    }

    private ScanSettings(Parcel in) {
        this.mScanMode = in.readInt();
        this.mCallbackType = in.readInt();
        this.mScanResultType = in.readInt();
        this.mReportDelayMillis = in.readLong();
        this.mMatchMode = in.readInt();
        this.mNumOfMatchesPerFilter = in.readInt();
        this.mLegacy = in.readInt() != 0;
        this.mPhy = in.readInt();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mScanMode);
        dest.writeInt(this.mCallbackType);
        dest.writeInt(this.mScanResultType);
        dest.writeLong(this.mReportDelayMillis);
        dest.writeInt(this.mMatchMode);
        dest.writeInt(this.mNumOfMatchesPerFilter);
        dest.writeInt(this.mLegacy);
        dest.writeInt(this.mPhy);
    }

    public int describeContents() {
        return 0;
    }
}
