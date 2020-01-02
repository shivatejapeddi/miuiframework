package android.hardware.location;

import android.annotation.SystemApi;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.telecom.Logging.Session;
import java.util.Arrays;

@SystemApi
public final class NanoAppMessage implements Parcelable {
    public static final Creator<NanoAppMessage> CREATOR = new Creator<NanoAppMessage>() {
        public NanoAppMessage createFromParcel(Parcel in) {
            return new NanoAppMessage(in, null);
        }

        public NanoAppMessage[] newArray(int size) {
            return new NanoAppMessage[size];
        }
    };
    private static final int DEBUG_LOG_NUM_BYTES = 16;
    private boolean mIsBroadcasted;
    private byte[] mMessageBody;
    private int mMessageType;
    private long mNanoAppId;

    /* synthetic */ NanoAppMessage(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    private NanoAppMessage(long nanoAppId, int messageType, byte[] messageBody, boolean broadcasted) {
        this.mNanoAppId = nanoAppId;
        this.mMessageType = messageType;
        this.mMessageBody = messageBody;
        this.mIsBroadcasted = broadcasted;
    }

    public static NanoAppMessage createMessageToNanoApp(long targetNanoAppId, int messageType, byte[] messageBody) {
        return new NanoAppMessage(targetNanoAppId, messageType, messageBody, false);
    }

    public static NanoAppMessage createMessageFromNanoApp(long sourceNanoAppId, int messageType, byte[] messageBody, boolean broadcasted) {
        return new NanoAppMessage(sourceNanoAppId, messageType, messageBody, broadcasted);
    }

    public long getNanoAppId() {
        return this.mNanoAppId;
    }

    public int getMessageType() {
        return this.mMessageType;
    }

    public byte[] getMessageBody() {
        return this.mMessageBody;
    }

    public boolean isBroadcastMessage() {
        return this.mIsBroadcasted;
    }

    private NanoAppMessage(Parcel in) {
        this.mNanoAppId = in.readLong();
        boolean z = true;
        if (in.readInt() != 1) {
            z = false;
        }
        this.mIsBroadcasted = z;
        this.mMessageType = in.readInt();
        this.mMessageBody = new byte[in.readInt()];
        in.readByteArray(this.mMessageBody);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.mNanoAppId);
        out.writeInt(this.mIsBroadcasted);
        out.writeInt(this.mMessageType);
        out.writeInt(this.mMessageBody.length);
        out.writeByteArray(this.mMessageBody);
    }

    public String toString() {
        StringBuilder stringBuilder;
        int length = this.mMessageBody.length;
        String ret = new StringBuilder();
        ret.append("NanoAppMessage[type = ");
        ret.append(this.mMessageType);
        ret.append(", length = ");
        ret.append(this.mMessageBody.length);
        ret.append(" bytes, ");
        ret.append(this.mIsBroadcasted ? "broadcast" : "unicast");
        ret.append(", nanoapp = 0x");
        ret.append(Long.toHexString(this.mNanoAppId));
        ret.append("](");
        ret = ret.toString();
        if (length > 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(ret);
            stringBuilder.append("data = 0x");
            ret = stringBuilder.toString();
        }
        for (int i = 0; i < Math.min(length, 16); i++) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(ret);
            stringBuilder2.append(Byte.toHexString(this.mMessageBody[i], true));
            ret = stringBuilder2.toString();
            if ((i + 1) % 4 == 0) {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(ret);
                stringBuilder2.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                ret = stringBuilder2.toString();
            }
        }
        if (length > 16) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(ret);
            stringBuilder.append(Session.TRUNCATE_STRING);
            ret = stringBuilder.toString();
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(ret);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public boolean equals(Object object) {
        boolean z = true;
        if (object == this) {
            return true;
        }
        boolean isEqual = false;
        if (object instanceof NanoAppMessage) {
            NanoAppMessage other = (NanoAppMessage) object;
            if (!(other.getNanoAppId() == this.mNanoAppId && other.getMessageType() == this.mMessageType && other.isBroadcastMessage() == this.mIsBroadcasted && Arrays.equals(other.getMessageBody(), this.mMessageBody))) {
                z = false;
            }
            isEqual = z;
        }
        return isEqual;
    }
}
