package android.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class BluetoothQualityReport implements Parcelable {
    public static final Creator<BluetoothQualityReport> CREATOR = new Creator<BluetoothQualityReport>() {
        public BluetoothQualityReport createFromParcel(Parcel in) {
            return new BluetoothQualityReport(in, null);
        }

        public BluetoothQualityReport[] newArray(int size) {
            return new BluetoothQualityReport[size];
        }
    };
    public static final int QUALITY_REPORT_ID_A2DP_CHOPPY = 3;
    public static final int QUALITY_REPORT_ID_APPROACH_LSTO = 2;
    public static final int QUALITY_REPORT_ID_CONN_FAIL = 32;
    public static final int QUALITY_REPORT_ID_MONITOR = 1;
    public static final int QUALITY_REPORT_ID_SCO_CHOPPY = 4;
    private static final String TAG = "BluetoothQualityReport";
    private String mAddr;
    private int mBluetoothClass;
    private BqrCommon mBqrCommon;
    private BqrVsA2dpChoppy mBqrVsA2dpChoppy;
    private BqrVsCommon mBqrVsCommon;
    private BqrVsConnectFail mBqrVsConnectFail;
    private BqrVsLsto mBqrVsLsto;
    private BqrVsScoChoppy mBqrVsScoChoppy;
    private int mLmpSubVer;
    private int mLmpVer;
    private int mManufacturerId;
    private String mName;

    enum AirMode {
        uLaw,
        aLaw,
        CVSD,
        transparent_msbc,
        INVALID;
        
        private static AirMode[] sAllValues;

        static {
            sAllValues = values();
        }

        static AirMode fromOrdinal(int n) {
            AirMode[] airModeArr = sAllValues;
            if (n < airModeArr.length - 1) {
                return airModeArr[n];
            }
            return INVALID;
        }
    }

    public class BqrCommon implements Parcelable {
        static final int BQR_COMMON_LEN = 48;
        private static final String TAG = "BluetoothQualityReport.BqrCommon";
        private int mAfhSelectUnidealChannelCount;
        private int mConnectionHandle;
        private int mConnectionRole;
        private long mFlowOffCount;
        private long mLastFlowOnTimestamp;
        private long mLastTxAckTimestamp;
        private int mLsto;
        private long mNakCount;
        private long mNoRxCount;
        private long mOverflowCount;
        private int mPacketType;
        private long mPiconetClock;
        private int mQualityReportId;
        private long mRetransmissionCount;
        private int mRssi;
        private int mSnr;
        private int mTxPowerLevel;
        private long mUnderflowCount;
        private int mUnusedAfhChannelCount;

        /* synthetic */ BqrCommon(BluetoothQualityReport x0, byte[] x1, int x2, AnonymousClass1 x3) {
            this(x1, x2);
        }

        private BqrCommon(byte[] rawData, int offset) {
            if (rawData == null || rawData.length < offset + 48) {
                throw new IllegalArgumentException("BluetoothQualityReport.BqrCommon: BQR raw data length is abnormal.");
            }
            ByteBuffer bqrBuf = ByteBuffer.wrap(rawData, offset, rawData.length - offset).asReadOnlyBuffer();
            bqrBuf.order(ByteOrder.LITTLE_ENDIAN);
            this.mQualityReportId = bqrBuf.get() & 255;
            this.mPacketType = bqrBuf.get() & 255;
            this.mConnectionHandle = bqrBuf.getShort() & 65535;
            this.mConnectionRole = bqrBuf.get() & 255;
            this.mTxPowerLevel = bqrBuf.get() & 255;
            this.mRssi = bqrBuf.get();
            this.mSnr = bqrBuf.get();
            this.mUnusedAfhChannelCount = bqrBuf.get() & 255;
            this.mAfhSelectUnidealChannelCount = bqrBuf.get() & 255;
            this.mLsto = bqrBuf.getShort() & 65535;
            this.mPiconetClock = ((long) bqrBuf.getInt()) & 4294967295L;
            this.mRetransmissionCount = ((long) bqrBuf.getInt()) & 4294967295L;
            this.mNoRxCount = ((long) bqrBuf.getInt()) & 4294967295L;
            this.mNakCount = ((long) bqrBuf.getInt()) & 4294967295L;
            this.mLastTxAckTimestamp = ((long) bqrBuf.getInt()) & 4294967295L;
            this.mFlowOffCount = ((long) bqrBuf.getInt()) & 4294967295L;
            this.mLastFlowOnTimestamp = ((long) bqrBuf.getInt()) & 4294967295L;
            this.mOverflowCount = ((long) bqrBuf.getInt()) & 4294967295L;
            this.mUnderflowCount = ((long) bqrBuf.getInt()) & 4294967295L;
        }

        private BqrCommon(Parcel in) {
            this.mQualityReportId = in.readInt();
            this.mPacketType = in.readInt();
            this.mConnectionHandle = in.readInt();
            this.mConnectionRole = in.readInt();
            this.mTxPowerLevel = in.readInt();
            this.mRssi = in.readInt();
            this.mSnr = in.readInt();
            this.mUnusedAfhChannelCount = in.readInt();
            this.mAfhSelectUnidealChannelCount = in.readInt();
            this.mLsto = in.readInt();
            this.mPiconetClock = in.readLong();
            this.mRetransmissionCount = in.readLong();
            this.mNoRxCount = in.readLong();
            this.mNakCount = in.readLong();
            this.mLastTxAckTimestamp = in.readLong();
            this.mFlowOffCount = in.readLong();
            this.mLastFlowOnTimestamp = in.readLong();
            this.mOverflowCount = in.readLong();
            this.mUnderflowCount = in.readLong();
        }

        /* Access modifiers changed, original: 0000 */
        public int getQualityReportId() {
            return this.mQualityReportId;
        }

        public int getPacketType() {
            return this.mPacketType;
        }

        public String getPacketTypeStr() {
            return PacketType.fromOrdinal(this.mPacketType).toString();
        }

        public int getConnectionHandle() {
            return this.mConnectionHandle;
        }

        public String getConnectionRole() {
            int i = this.mConnectionRole;
            if (i == 0) {
                return "Master";
            }
            if (i == 1) {
                return "Slave";
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("INVALID:");
            stringBuilder.append(this.mConnectionRole);
            return stringBuilder.toString();
        }

        public int getTxPowerLevel() {
            return this.mTxPowerLevel;
        }

        public int getRssi() {
            return this.mRssi;
        }

        public int getSnr() {
            return this.mSnr;
        }

        public int getUnusedAfhChannelCount() {
            return this.mUnusedAfhChannelCount;
        }

        public int getAfhSelectUnidealChannelCount() {
            return this.mAfhSelectUnidealChannelCount;
        }

        public int getLsto() {
            return this.mLsto;
        }

        public long getPiconetClock() {
            return this.mPiconetClock;
        }

        public long getRetransmissionCount() {
            return this.mRetransmissionCount;
        }

        public long getNoRxCount() {
            return this.mNoRxCount;
        }

        public long getNakCount() {
            return this.mNakCount;
        }

        public long getLastTxAckTimestamp() {
            return this.mLastTxAckTimestamp;
        }

        public long getFlowOffCount() {
            return this.mFlowOffCount;
        }

        public long getLastFlowOnTimestamp() {
            return this.mLastFlowOnTimestamp;
        }

        public long getOverflowCount() {
            return this.mOverflowCount;
        }

        public long getUnderflowCount() {
            return this.mUnderflowCount;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.mQualityReportId);
            dest.writeInt(this.mPacketType);
            dest.writeInt(this.mConnectionHandle);
            dest.writeInt(this.mConnectionRole);
            dest.writeInt(this.mTxPowerLevel);
            dest.writeInt(this.mRssi);
            dest.writeInt(this.mSnr);
            dest.writeInt(this.mUnusedAfhChannelCount);
            dest.writeInt(this.mAfhSelectUnidealChannelCount);
            dest.writeInt(this.mLsto);
            dest.writeLong(this.mPiconetClock);
            dest.writeLong(this.mRetransmissionCount);
            dest.writeLong(this.mNoRxCount);
            dest.writeLong(this.mNakCount);
            dest.writeLong(this.mLastTxAckTimestamp);
            dest.writeLong(this.mFlowOffCount);
            dest.writeLong(this.mLastFlowOnTimestamp);
            dest.writeLong(this.mOverflowCount);
            dest.writeLong(this.mUnderflowCount);
        }

        public String toString() {
            String str = new StringBuilder();
            str.append("  BqrCommon: {\n    mQualityReportId: ");
            str.append(BluetoothQualityReport.this.getQualityReportIdStr());
            String str2 = "(";
            str.append(str2);
            String str3 = "0x%02X";
            str.append(String.format(str3, new Object[]{Integer.valueOf(this.mQualityReportId)}));
            str.append("), mPacketType: ");
            str.append(getPacketTypeStr());
            str.append(str2);
            str.append(String.format(str3, new Object[]{Integer.valueOf(this.mPacketType)}));
            str.append("), mConnectionHandle: ");
            str.append(String.format("0x%04X", new Object[]{Integer.valueOf(this.mConnectionHandle)}));
            str.append(", mConnectionRole: ");
            str.append(getConnectionRole());
            str.append(str2);
            str.append(this.mConnectionRole);
            str.append("), mTxPowerLevel: ");
            str.append(this.mTxPowerLevel);
            str.append(", mRssi: ");
            str.append(this.mRssi);
            str.append(", mSnr: ");
            str.append(this.mSnr);
            str.append(", mUnusedAfhChannelCount: ");
            str.append(this.mUnusedAfhChannelCount);
            str.append(",\n    mAfhSelectUnidealChannelCount: ");
            str.append(this.mAfhSelectUnidealChannelCount);
            str.append(", mLsto: ");
            str.append(this.mLsto);
            str.append(", mPiconetClock: ");
            String str4 = "0x%08X";
            str.append(String.format(str4, new Object[]{Long.valueOf(this.mPiconetClock)}));
            str.append(", mRetransmissionCount: ");
            str.append(this.mRetransmissionCount);
            str.append(", mNoRxCount: ");
            str.append(this.mNoRxCount);
            str.append(", mNakCount: ");
            str.append(this.mNakCount);
            str.append(", mLastTxAckTimestamp: ");
            str.append(String.format(str4, new Object[]{Long.valueOf(this.mLastTxAckTimestamp)}));
            str.append(", mFlowOffCount: ");
            str.append(this.mFlowOffCount);
            str.append(",\n    mLastFlowOnTimestamp: ");
            str.append(String.format(str4, new Object[]{Long.valueOf(this.mLastFlowOnTimestamp)}));
            str.append(", mOverflowCount: ");
            str.append(this.mOverflowCount);
            str.append(", mUnderflowCount: ");
            str.append(this.mUnderflowCount);
            str.append("\n  }");
            return str.toString();
        }
    }

    public class BqrVsA2dpChoppy implements Parcelable {
        private static final String TAG = "BluetoothQualityReport.BqrVsA2dpChoppy";
        private int mAclTxQueueLength;
        private long mArrivalTime;
        private int mGlitchCount;
        private int mLinkQuality;
        private int mRxCxmDenials;
        private long mScheduleTime;
        private int mTxCxmDenials;

        /* synthetic */ BqrVsA2dpChoppy(BluetoothQualityReport x0, byte[] x1, int x2, AnonymousClass1 x3) {
            this(x1, x2);
        }

        private BqrVsA2dpChoppy(byte[] rawData, int offset) {
            if (rawData == null || rawData.length <= offset) {
                throw new IllegalArgumentException("BluetoothQualityReport.BqrVsA2dpChoppy: BQR raw data length is abnormal.");
            }
            ByteBuffer bqrBuf = ByteBuffer.wrap(rawData, offset, rawData.length - offset).asReadOnlyBuffer();
            bqrBuf.order(ByteOrder.LITTLE_ENDIAN);
            this.mArrivalTime = ((long) bqrBuf.getInt()) & 4294967295L;
            this.mScheduleTime = ((long) bqrBuf.getInt()) & 4294967295L;
            this.mGlitchCount = bqrBuf.getShort() & 65535;
            this.mTxCxmDenials = bqrBuf.getShort() & 65535;
            this.mRxCxmDenials = bqrBuf.getShort() & 65535;
            this.mAclTxQueueLength = bqrBuf.get() & 255;
            this.mLinkQuality = bqrBuf.get() & 255;
        }

        private BqrVsA2dpChoppy(Parcel in) {
            this.mArrivalTime = in.readLong();
            this.mScheduleTime = in.readLong();
            this.mGlitchCount = in.readInt();
            this.mTxCxmDenials = in.readInt();
            this.mRxCxmDenials = in.readInt();
            this.mAclTxQueueLength = in.readInt();
            this.mLinkQuality = in.readInt();
        }

        public long getArrivalTime() {
            return this.mArrivalTime;
        }

        public long getScheduleTime() {
            return this.mScheduleTime;
        }

        public int getGlitchCount() {
            return this.mGlitchCount;
        }

        public int getTxCxmDenials() {
            return this.mTxCxmDenials;
        }

        public int getRxCxmDenials() {
            return this.mRxCxmDenials;
        }

        public int getAclTxQueueLength() {
            return this.mAclTxQueueLength;
        }

        public int getLinkQuality() {
            return this.mLinkQuality;
        }

        public String getLinkQualityStr() {
            return LinkQuality.fromOrdinal(this.mLinkQuality).toString();
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.mArrivalTime);
            dest.writeLong(this.mScheduleTime);
            dest.writeInt(this.mGlitchCount);
            dest.writeInt(this.mTxCxmDenials);
            dest.writeInt(this.mRxCxmDenials);
            dest.writeInt(this.mAclTxQueueLength);
            dest.writeInt(this.mLinkQuality);
        }

        public String toString() {
            String str = new StringBuilder();
            str.append("  BqrVsA2dpChoppy: {\n    mArrivalTime: ");
            String str2 = "0x%08X";
            str.append(String.format(str2, new Object[]{Long.valueOf(this.mArrivalTime)}));
            str.append(", mScheduleTime: ");
            str.append(String.format(str2, new Object[]{Long.valueOf(this.mScheduleTime)}));
            str.append(", mGlitchCount: ");
            str.append(this.mGlitchCount);
            str.append(", mTxCxmDenials: ");
            str.append(this.mTxCxmDenials);
            str.append(", mRxCxmDenials: ");
            str.append(this.mRxCxmDenials);
            str.append(", mAclTxQueueLength: ");
            str.append(this.mAclTxQueueLength);
            str.append(", mLinkQuality: ");
            str.append(getLinkQualityStr());
            str.append("(");
            str.append(String.format("0x%02X", new Object[]{Integer.valueOf(this.mLinkQuality)}));
            str.append(")\n  }");
            return str.toString();
        }
    }

    public class BqrVsCommon implements Parcelable {
        private static final int BQR_VS_COMMON_LEN = 7;
        private static final String TAG = "BluetoothQualityReport.BqrVsCommon";
        private String mAddr;
        private int mCalFailedItemCount;

        /* synthetic */ BqrVsCommon(BluetoothQualityReport x0, byte[] x1, int x2, AnonymousClass1 x3) {
            this(x1, x2);
        }

        private BqrVsCommon(byte[] rawData, int offset) {
            if (rawData == null || rawData.length < offset + 7) {
                throw new IllegalArgumentException("BluetoothQualityReport.BqrVsCommon: BQR raw data length is abnormal.");
            }
            ByteBuffer bqrBuf = ByteBuffer.wrap(rawData, offset, rawData.length - offset).asReadOnlyBuffer();
            bqrBuf.order(ByteOrder.LITTLE_ENDIAN);
            this.mAddr = String.format("%02X:%02X:%02X:%02X:%02X:%02X", new Object[]{Byte.valueOf(bqrBuf.get(offset + 5)), Byte.valueOf(bqrBuf.get(offset + 4)), Byte.valueOf(bqrBuf.get(offset + 3)), Byte.valueOf(bqrBuf.get(offset + 2)), Byte.valueOf(bqrBuf.get(offset + 1)), Byte.valueOf(bqrBuf.get(offset + 0))});
            bqrBuf.position(offset + 6);
            this.mCalFailedItemCount = bqrBuf.get() & 255;
        }

        private BqrVsCommon(Parcel in) {
            this.mAddr = in.readString();
            this.mCalFailedItemCount = in.readInt();
        }

        public int getCalFailedItemCount() {
            return this.mCalFailedItemCount;
        }

        /* Access modifiers changed, original: 0000 */
        public int getLength() {
            return 7;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.mAddr);
            dest.writeInt(this.mCalFailedItemCount);
        }

        public String toString() {
            String str = new StringBuilder();
            str.append("  BqrVsCommon: {\n    mAddr: ");
            str.append(this.mAddr);
            str.append(", mCalFailedItemCount: ");
            str.append(this.mCalFailedItemCount);
            str.append("\n  }");
            return str.toString();
        }
    }

    public class BqrVsConnectFail implements Parcelable {
        private static final String TAG = "BluetoothQualityReport.BqrVsConnectFail";
        private int mFailReason;

        /* synthetic */ BqrVsConnectFail(BluetoothQualityReport x0, byte[] x1, int x2, AnonymousClass1 x3) {
            this(x1, x2);
        }

        private BqrVsConnectFail(byte[] rawData, int offset) {
            if (rawData == null || rawData.length <= offset) {
                throw new IllegalArgumentException("BluetoothQualityReport.BqrVsConnectFail: BQR raw data length is abnormal.");
            }
            ByteBuffer bqrBuf = ByteBuffer.wrap(rawData, offset, rawData.length - offset).asReadOnlyBuffer();
            bqrBuf.order(ByteOrder.LITTLE_ENDIAN);
            this.mFailReason = bqrBuf.get() & 255;
        }

        private BqrVsConnectFail(Parcel in) {
            this.mFailReason = in.readInt();
        }

        public int getFailReason() {
            return this.mFailReason;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.mFailReason);
        }

        public String toString() {
            String str = new StringBuilder();
            str.append("  BqrVsConnectFail: {\n    mFailReason: ");
            str.append(String.format("0x%02X", new Object[]{Integer.valueOf(this.mFailReason)}));
            str.append("\n  }");
            return str.toString();
        }
    }

    public class BqrVsLsto implements Parcelable {
        private static final String TAG = "BluetoothQualityReport.BqrVsLsto";
        private long mBasebandStats;
        private int mConnState;
        private int mCxmDenials;
        private long mLastTxAckTimestamp;
        private long mNativeClock;
        private int mRfLoss;
        private long mSlotsUsed;
        private int mTxSkipped;

        /* synthetic */ BqrVsLsto(BluetoothQualityReport x0, byte[] x1, int x2, AnonymousClass1 x3) {
            this(x1, x2);
        }

        private BqrVsLsto(byte[] rawData, int offset) {
            if (rawData == null || rawData.length <= offset) {
                throw new IllegalArgumentException("BluetoothQualityReport.BqrVsLsto: BQR raw data length is abnormal.");
            }
            ByteBuffer bqrBuf = ByteBuffer.wrap(rawData, offset, rawData.length - offset).asReadOnlyBuffer();
            bqrBuf.order(ByteOrder.LITTLE_ENDIAN);
            this.mConnState = bqrBuf.get() & 255;
            this.mBasebandStats = ((long) bqrBuf.getInt()) & 4294967295L;
            this.mSlotsUsed = ((long) bqrBuf.getInt()) & 4294967295L;
            this.mCxmDenials = bqrBuf.getShort() & 65535;
            this.mTxSkipped = bqrBuf.getShort() & 65535;
            this.mRfLoss = bqrBuf.getShort() & 65535;
            this.mNativeClock = ((long) bqrBuf.getInt()) & 4294967295L;
            this.mLastTxAckTimestamp = ((long) bqrBuf.getInt()) & 4294967295L;
        }

        private BqrVsLsto(Parcel in) {
            this.mConnState = in.readInt();
            this.mBasebandStats = in.readLong();
            this.mSlotsUsed = in.readLong();
            this.mCxmDenials = in.readInt();
            this.mTxSkipped = in.readInt();
            this.mRfLoss = in.readInt();
            this.mNativeClock = in.readLong();
            this.mLastTxAckTimestamp = in.readLong();
        }

        public int getConnState() {
            return this.mConnState;
        }

        public String getConnStateStr() {
            return ConnState.getName(this.mConnState);
        }

        public long getBasebandStats() {
            return this.mBasebandStats;
        }

        public long getSlotsUsed() {
            return this.mSlotsUsed;
        }

        public int getCxmDenials() {
            return this.mCxmDenials;
        }

        public int getTxSkipped() {
            return this.mTxSkipped;
        }

        public int getRfLoss() {
            return this.mRfLoss;
        }

        public long getNativeClock() {
            return this.mNativeClock;
        }

        public long getLastTxAckTimestamp() {
            return this.mLastTxAckTimestamp;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.mConnState);
            dest.writeLong(this.mBasebandStats);
            dest.writeLong(this.mSlotsUsed);
            dest.writeInt(this.mCxmDenials);
            dest.writeInt(this.mTxSkipped);
            dest.writeInt(this.mRfLoss);
            dest.writeLong(this.mNativeClock);
            dest.writeLong(this.mLastTxAckTimestamp);
        }

        public String toString() {
            String str = new StringBuilder();
            str.append("  BqrVsLsto: {\n    mConnState: ");
            str.append(getConnStateStr());
            str.append("(");
            str.append(String.format("0x%02X", new Object[]{Integer.valueOf(this.mConnState)}));
            str.append("), mBasebandStats: ");
            String str2 = "0x%08X";
            str.append(String.format(str2, new Object[]{Long.valueOf(this.mBasebandStats)}));
            str.append(", mSlotsUsed: ");
            str.append(this.mSlotsUsed);
            str.append(", mCxmDenials: ");
            str.append(this.mCxmDenials);
            str.append(", mTxSkipped: ");
            str.append(this.mTxSkipped);
            str.append(", mRfLoss: ");
            str.append(this.mRfLoss);
            str.append(", mNativeClock: ");
            str.append(String.format(str2, new Object[]{Long.valueOf(this.mNativeClock)}));
            str.append(", mLastTxAckTimestamp: ");
            str.append(String.format(str2, new Object[]{Long.valueOf(this.mLastTxAckTimestamp)}));
            str.append("\n  }");
            return str.toString();
        }
    }

    public class BqrVsScoChoppy implements Parcelable {
        private static final String TAG = "BluetoothQualityReport.BqrVsScoChoppy";
        private int mAirFormat;
        private int mGlitchCount;
        private int mInstanceCount;
        private int mIntervalEsco;
        private int mLateDispatch;
        private int mLpaIntrMiss;
        private int mMicIntrMiss;
        private int mPlcDiscardCount;
        private int mPlcFillCount;
        private int mRxCxmDenials;
        private int mSprIntrMiss;
        private int mTxAbortCount;
        private int mTxCxmDenials;
        private int mWindowEsco;

        /* synthetic */ BqrVsScoChoppy(BluetoothQualityReport x0, byte[] x1, int x2, AnonymousClass1 x3) {
            this(x1, x2);
        }

        private BqrVsScoChoppy(byte[] rawData, int offset) {
            if (rawData == null || rawData.length <= offset) {
                throw new IllegalArgumentException("BluetoothQualityReport.BqrVsScoChoppy: BQR raw data length is abnormal.");
            }
            ByteBuffer bqrBuf = ByteBuffer.wrap(rawData, offset, rawData.length - offset).asReadOnlyBuffer();
            bqrBuf.order(ByteOrder.LITTLE_ENDIAN);
            this.mGlitchCount = bqrBuf.getShort() & 65535;
            this.mIntervalEsco = bqrBuf.get() & 255;
            this.mWindowEsco = bqrBuf.get() & 255;
            this.mAirFormat = bqrBuf.get() & 255;
            this.mInstanceCount = bqrBuf.getShort() & 65535;
            this.mTxCxmDenials = bqrBuf.getShort() & 65535;
            this.mRxCxmDenials = bqrBuf.getShort() & 65535;
            this.mTxAbortCount = bqrBuf.getShort() & 65535;
            this.mLateDispatch = bqrBuf.getShort() & 65535;
            this.mMicIntrMiss = bqrBuf.getShort() & 65535;
            this.mLpaIntrMiss = bqrBuf.getShort() & 65535;
            this.mSprIntrMiss = bqrBuf.getShort() & 65535;
            this.mPlcFillCount = bqrBuf.getShort() & 65535;
            this.mPlcDiscardCount = bqrBuf.getShort() & 65535;
        }

        private BqrVsScoChoppy(Parcel in) {
            this.mGlitchCount = in.readInt();
            this.mIntervalEsco = in.readInt();
            this.mWindowEsco = in.readInt();
            this.mAirFormat = in.readInt();
            this.mInstanceCount = in.readInt();
            this.mTxCxmDenials = in.readInt();
            this.mRxCxmDenials = in.readInt();
            this.mTxAbortCount = in.readInt();
            this.mLateDispatch = in.readInt();
            this.mMicIntrMiss = in.readInt();
            this.mLpaIntrMiss = in.readInt();
            this.mSprIntrMiss = in.readInt();
            this.mPlcFillCount = in.readInt();
            this.mPlcDiscardCount = in.readInt();
        }

        public int getGlitchCount() {
            return this.mGlitchCount;
        }

        public int getIntervalEsco() {
            return this.mIntervalEsco;
        }

        public int getWindowEsco() {
            return this.mWindowEsco;
        }

        public int getAirFormat() {
            return this.mAirFormat;
        }

        public String getAirFormatStr() {
            return AirMode.fromOrdinal(this.mAirFormat).toString();
        }

        public int getInstanceCount() {
            return this.mInstanceCount;
        }

        public int getTxCxmDenials() {
            return this.mTxCxmDenials;
        }

        public int getRxCxmDenials() {
            return this.mRxCxmDenials;
        }

        public int getTxAbortCount() {
            return this.mTxAbortCount;
        }

        public int getLateDispatch() {
            return this.mLateDispatch;
        }

        public int getMicIntrMiss() {
            return this.mMicIntrMiss;
        }

        public int getLpaIntrMiss() {
            return this.mLpaIntrMiss;
        }

        public int getSprIntrMiss() {
            return this.mSprIntrMiss;
        }

        public int getPlcFillCount() {
            return this.mPlcFillCount;
        }

        public int getPlcDiscardCount() {
            return this.mPlcDiscardCount;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.mGlitchCount);
            dest.writeInt(this.mIntervalEsco);
            dest.writeInt(this.mWindowEsco);
            dest.writeInt(this.mAirFormat);
            dest.writeInt(this.mInstanceCount);
            dest.writeInt(this.mTxCxmDenials);
            dest.writeInt(this.mRxCxmDenials);
            dest.writeInt(this.mTxAbortCount);
            dest.writeInt(this.mLateDispatch);
            dest.writeInt(this.mMicIntrMiss);
            dest.writeInt(this.mLpaIntrMiss);
            dest.writeInt(this.mSprIntrMiss);
            dest.writeInt(this.mPlcFillCount);
            dest.writeInt(this.mPlcDiscardCount);
        }

        public String toString() {
            String str = new StringBuilder();
            str.append("  BqrVsScoChoppy: {\n    mGlitchCount: ");
            str.append(this.mGlitchCount);
            str.append(", mIntervalEsco: ");
            str.append(this.mIntervalEsco);
            str.append(", mWindowEsco: ");
            str.append(this.mWindowEsco);
            str.append(", mAirFormat: ");
            str.append(getAirFormatStr());
            str.append("(");
            str.append(String.format("0x%02X", new Object[]{Integer.valueOf(this.mAirFormat)}));
            str.append("), mInstanceCount: ");
            str.append(this.mInstanceCount);
            str.append(", mTxCxmDenials: ");
            str.append(this.mTxCxmDenials);
            str.append(", mRxCxmDenials: ");
            str.append(this.mRxCxmDenials);
            str.append(", mTxAbortCount: ");
            str.append(this.mTxAbortCount);
            str.append(",\n    mLateDispatch: ");
            str.append(this.mLateDispatch);
            str.append(", mMicIntrMiss: ");
            str.append(this.mMicIntrMiss);
            str.append(", mLpaIntrMiss: ");
            str.append(this.mLpaIntrMiss);
            str.append(", mSprIntrMiss: ");
            str.append(this.mSprIntrMiss);
            str.append(", mPlcFillCount: ");
            str.append(this.mPlcFillCount);
            str.append(", mPlcDiscardCount: ");
            str.append(this.mPlcDiscardCount);
            str.append("\n  }");
            return str.toString();
        }
    }

    enum ConnState {
        CONN_IDLE(0),
        CONN_ACTIVE(129),
        CONN_HOLD(2),
        CONN_SNIFF_IDLE(3),
        CONN_SNIFF_ACTIVE(132),
        CONN_SNIFF_MASTER_TRANSITION(133),
        CONN_PARK(6),
        CONN_PARK_PEND(71),
        CONN_UNPARK_PEND(8),
        CONN_UNPARK_ACTIVE(137),
        CONN_DISCONNECT_PENDING(74),
        CONN_PAGING(11),
        CONN_PAGE_SCAN(12),
        CONN_LOCAL_LOOPBACK(13),
        CONN_LE_ACTIVE(14),
        CONN_ANT_ACTIVE(15),
        CONN_TRIGGER_SCAN(16),
        CONN_RECONNECTING(17),
        CONN_SEMI_CONN(18);
        
        private static ConnState[] sAllStates;
        private int mValue;

        static {
            sAllStates = values();
        }

        private ConnState(int val) {
            this.mValue = val;
        }

        public static String getName(int val) {
            for (ConnState state : sAllStates) {
                if (state.mValue == val) {
                    return state.toString();
                }
            }
            return "INVALID";
        }
    }

    enum LinkQuality {
        ULTRA_HIGH,
        HIGH,
        STANDARD,
        MEDIUM,
        LOW,
        INVALID;
        
        private static LinkQuality[] sAllValues;

        static {
            sAllValues = values();
        }

        static LinkQuality fromOrdinal(int n) {
            LinkQuality[] linkQualityArr = sAllValues;
            if (n < linkQualityArr.length - 1) {
                return linkQualityArr[n];
            }
            return INVALID;
        }
    }

    enum PacketType {
        INVALID,
        TYPE_ID,
        TYPE_NULL,
        TYPE_POLL,
        TYPE_FHS,
        TYPE_HV1,
        TYPE_HV2,
        TYPE_HV3,
        TYPE_DV,
        TYPE_EV3,
        TYPE_EV4,
        TYPE_EV5,
        TYPE_2EV3,
        TYPE_2EV5,
        TYPE_3EV3,
        TYPE_3EV5,
        TYPE_DM1,
        TYPE_DH1,
        TYPE_DM3,
        TYPE_DH3,
        TYPE_DM5,
        TYPE_DH5,
        TYPE_AUX1,
        TYPE_2DH1,
        TYPE_2DH3,
        TYPE_2DH5,
        TYPE_3DH1,
        TYPE_3DH3,
        TYPE_3DH5;
        
        private static PacketType[] sAllValues;

        static {
            sAllValues = values();
        }

        static PacketType fromOrdinal(int n) {
            PacketType[] packetTypeArr = sAllValues;
            if (n < packetTypeArr.length) {
                return packetTypeArr[n];
            }
            return INVALID;
        }
    }

    /* synthetic */ BluetoothQualityReport(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public BluetoothQualityReport(String remoteAddr, int lmpVer, int lmpSubVer, int manufacturerId, String remoteName, int remoteCoD, byte[] rawData) {
        boolean checkBluetoothAddress = BluetoothAdapter.checkBluetoothAddress(remoteAddr);
        String str = TAG;
        if (checkBluetoothAddress) {
            this.mAddr = remoteAddr;
        } else {
            Log.d(str, "remote addr is invalid");
            this.mAddr = "00:00:00:00:00:00";
        }
        this.mLmpVer = lmpVer;
        this.mLmpSubVer = lmpSubVer;
        this.mManufacturerId = manufacturerId;
        if (remoteName == null) {
            Log.d(str, "remote name is null");
            this.mName = "";
        } else {
            this.mName = remoteName;
        }
        this.mBluetoothClass = remoteCoD;
        this.mBqrCommon = new BqrCommon(this, rawData, 0, null);
        this.mBqrVsCommon = new BqrVsCommon(this, rawData, 48, null);
        int id = this.mBqrCommon.getQualityReportId();
        if (id != 1) {
            int vsPartOffset = this.mBqrVsCommon.getLength() + 48;
            if (id == 2) {
                this.mBqrVsLsto = new BqrVsLsto(this, rawData, vsPartOffset, null);
            } else if (id == 3) {
                this.mBqrVsA2dpChoppy = new BqrVsA2dpChoppy(this, rawData, vsPartOffset, null);
            } else if (id == 4) {
                this.mBqrVsScoChoppy = new BqrVsScoChoppy(this, rawData, vsPartOffset, null);
            } else if (id == 32) {
                this.mBqrVsConnectFail = new BqrVsConnectFail(this, rawData, vsPartOffset, null);
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("BluetoothQualityReport: unkown quality report id:");
                stringBuilder.append(id);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        }
    }

    private BluetoothQualityReport(Parcel in) {
        this.mBqrCommon = new BqrCommon(this, in, null);
        this.mAddr = in.readString();
        this.mLmpVer = in.readInt();
        this.mLmpSubVer = in.readInt();
        this.mManufacturerId = in.readInt();
        this.mName = in.readString();
        this.mBluetoothClass = in.readInt();
        this.mBqrVsCommon = new BqrVsCommon(this, in, null);
        int id = this.mBqrCommon.getQualityReportId();
        if (id == 2) {
            this.mBqrVsLsto = new BqrVsLsto(this, in, null);
        } else if (id == 3) {
            this.mBqrVsA2dpChoppy = new BqrVsA2dpChoppy(this, in, null);
        } else if (id == 4) {
            this.mBqrVsScoChoppy = new BqrVsScoChoppy(this, in, null);
        } else if (id == 32) {
            this.mBqrVsConnectFail = new BqrVsConnectFail(this, in, null);
        }
    }

    public int getQualityReportId() {
        return this.mBqrCommon.getQualityReportId();
    }

    public String getQualityReportIdStr() {
        int id = this.mBqrCommon.getQualityReportId();
        if (id == 1) {
            return "Quality monitor";
        }
        if (id == 2) {
            return "Approaching LSTO";
        }
        if (id == 3) {
            return "A2DP choppy";
        }
        if (id == 4) {
            return "SCO choppy";
        }
        if (id != 32) {
            return "INVALID";
        }
        return "Connect fail";
    }

    public String getAddress() {
        return this.mAddr;
    }

    public int getLmpVersion() {
        return this.mLmpVer;
    }

    public int getLmpSubVersion() {
        return this.mLmpSubVer;
    }

    public int getManufacturerId() {
        return this.mManufacturerId;
    }

    public String getName() {
        return this.mName;
    }

    public int getBluetoothClass() {
        return this.mBluetoothClass;
    }

    public BqrCommon getBqrCommon() {
        return this.mBqrCommon;
    }

    public BqrVsCommon getBqrVsCommon() {
        return this.mBqrVsCommon;
    }

    public BqrVsLsto getBqrVsLsto() {
        return this.mBqrVsLsto;
    }

    public BqrVsA2dpChoppy getBqrVsA2dpChoppy() {
        return this.mBqrVsA2dpChoppy;
    }

    public BqrVsScoChoppy getBqrVsScoChoppy() {
        return this.mBqrVsScoChoppy;
    }

    public BqrVsConnectFail getBqrVsConnectFail() {
        return this.mBqrVsConnectFail;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        this.mBqrCommon.writeToParcel(out, flags);
        out.writeString(this.mAddr);
        out.writeInt(this.mLmpVer);
        out.writeInt(this.mLmpSubVer);
        out.writeInt(this.mManufacturerId);
        out.writeString(this.mName);
        out.writeInt(this.mBluetoothClass);
        this.mBqrVsCommon.writeToParcel(out, flags);
        int id = this.mBqrCommon.getQualityReportId();
        if (id == 2) {
            this.mBqrVsLsto.writeToParcel(out, flags);
        } else if (id == 3) {
            this.mBqrVsA2dpChoppy.writeToParcel(out, flags);
        } else if (id == 4) {
            this.mBqrVsScoChoppy.writeToParcel(out, flags);
        } else if (id == 32) {
            this.mBqrVsConnectFail.writeToParcel(out, flags);
        }
    }

    public String toString() {
        String str = new StringBuilder();
        str.append("BQR: {\n  mAddr: ");
        str.append(this.mAddr);
        str.append(", mLmpVer: ");
        str.append(String.format("0x%02X", new Object[]{Integer.valueOf(this.mLmpVer)}));
        str.append(", mLmpSubVer: ");
        String str2 = "0x%04X";
        str.append(String.format(str2, new Object[]{Integer.valueOf(this.mLmpSubVer)}));
        str.append(", mManufacturerId: ");
        str.append(String.format(str2, new Object[]{Integer.valueOf(this.mManufacturerId)}));
        str.append(", mName: ");
        str.append(this.mName);
        str.append(", mBluetoothClass: ");
        str.append(String.format("0x%X", new Object[]{Integer.valueOf(this.mBluetoothClass)}));
        str.append(",\n");
        str.append(this.mBqrCommon);
        String str3 = "\n";
        str.append(str3);
        str.append(this.mBqrVsCommon);
        str.append(str3);
        str = str.toString();
        int id = this.mBqrCommon.getQualityReportId();
        str2 = "\n}";
        StringBuilder stringBuilder;
        if (id == 2) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(this.mBqrVsLsto);
            stringBuilder.append(str2);
            return stringBuilder.toString();
        } else if (id == 3) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(this.mBqrVsA2dpChoppy);
            stringBuilder.append(str2);
            return stringBuilder.toString();
        } else if (id == 4) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(this.mBqrVsScoChoppy);
            stringBuilder.append(str2);
            return stringBuilder.toString();
        } else if (id == 32) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(this.mBqrVsConnectFail);
            stringBuilder.append(str2);
            return stringBuilder.toString();
        } else if (id != 1) {
            return str;
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }
}
