package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.annotations.VisibleForTesting;

public final class IpSecConfig implements Parcelable {
    public static final Creator<IpSecConfig> CREATOR = new Creator<IpSecConfig>() {
        public IpSecConfig createFromParcel(Parcel in) {
            return new IpSecConfig(in, null);
        }

        public IpSecConfig[] newArray(int size) {
            return new IpSecConfig[size];
        }
    };
    private static final String TAG = "IpSecConfig";
    private IpSecAlgorithm mAuthenticatedEncryption;
    private IpSecAlgorithm mAuthentication;
    private String mDestinationAddress;
    private int mEncapRemotePort;
    private int mEncapSocketResourceId;
    private int mEncapType;
    private IpSecAlgorithm mEncryption;
    private int mMarkMask;
    private int mMarkValue;
    private int mMode;
    private int mNattKeepaliveInterval;
    private Network mNetwork;
    private String mSourceAddress;
    private int mSpiResourceId;
    private int mXfrmInterfaceId;

    /* synthetic */ IpSecConfig(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public void setMode(int mode) {
        this.mMode = mode;
    }

    public void setSourceAddress(String sourceAddress) {
        this.mSourceAddress = sourceAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.mDestinationAddress = destinationAddress;
    }

    public void setSpiResourceId(int resourceId) {
        this.mSpiResourceId = resourceId;
    }

    public void setEncryption(IpSecAlgorithm encryption) {
        this.mEncryption = encryption;
    }

    public void setAuthentication(IpSecAlgorithm authentication) {
        this.mAuthentication = authentication;
    }

    public void setAuthenticatedEncryption(IpSecAlgorithm authenticatedEncryption) {
        this.mAuthenticatedEncryption = authenticatedEncryption;
    }

    public void setNetwork(Network network) {
        this.mNetwork = network;
    }

    public void setEncapType(int encapType) {
        this.mEncapType = encapType;
    }

    public void setEncapSocketResourceId(int resourceId) {
        this.mEncapSocketResourceId = resourceId;
    }

    public void setEncapRemotePort(int port) {
        this.mEncapRemotePort = port;
    }

    public void setNattKeepaliveInterval(int interval) {
        this.mNattKeepaliveInterval = interval;
    }

    public void setMarkValue(int mark) {
        this.mMarkValue = mark;
    }

    public void setMarkMask(int mask) {
        this.mMarkMask = mask;
    }

    public void setXfrmInterfaceId(int xfrmInterfaceId) {
        this.mXfrmInterfaceId = xfrmInterfaceId;
    }

    public int getMode() {
        return this.mMode;
    }

    public String getSourceAddress() {
        return this.mSourceAddress;
    }

    public int getSpiResourceId() {
        return this.mSpiResourceId;
    }

    public String getDestinationAddress() {
        return this.mDestinationAddress;
    }

    public IpSecAlgorithm getEncryption() {
        return this.mEncryption;
    }

    public IpSecAlgorithm getAuthentication() {
        return this.mAuthentication;
    }

    public IpSecAlgorithm getAuthenticatedEncryption() {
        return this.mAuthenticatedEncryption;
    }

    public Network getNetwork() {
        return this.mNetwork;
    }

    public int getEncapType() {
        return this.mEncapType;
    }

    public int getEncapSocketResourceId() {
        return this.mEncapSocketResourceId;
    }

    public int getEncapRemotePort() {
        return this.mEncapRemotePort;
    }

    public int getNattKeepaliveInterval() {
        return this.mNattKeepaliveInterval;
    }

    public int getMarkValue() {
        return this.mMarkValue;
    }

    public int getMarkMask() {
        return this.mMarkMask;
    }

    public int getXfrmInterfaceId() {
        return this.mXfrmInterfaceId;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mMode);
        out.writeString(this.mSourceAddress);
        out.writeString(this.mDestinationAddress);
        out.writeParcelable(this.mNetwork, flags);
        out.writeInt(this.mSpiResourceId);
        out.writeParcelable(this.mEncryption, flags);
        out.writeParcelable(this.mAuthentication, flags);
        out.writeParcelable(this.mAuthenticatedEncryption, flags);
        out.writeInt(this.mEncapType);
        out.writeInt(this.mEncapSocketResourceId);
        out.writeInt(this.mEncapRemotePort);
        out.writeInt(this.mNattKeepaliveInterval);
        out.writeInt(this.mMarkValue);
        out.writeInt(this.mMarkMask);
        out.writeInt(this.mXfrmInterfaceId);
    }

    @VisibleForTesting
    public IpSecConfig() {
        this.mMode = 0;
        String str = "";
        this.mSourceAddress = str;
        this.mDestinationAddress = str;
        this.mSpiResourceId = -1;
        this.mEncapType = 0;
        this.mEncapSocketResourceId = -1;
    }

    @VisibleForTesting
    public IpSecConfig(IpSecConfig c) {
        this.mMode = 0;
        String str = "";
        this.mSourceAddress = str;
        this.mDestinationAddress = str;
        this.mSpiResourceId = -1;
        this.mEncapType = 0;
        this.mEncapSocketResourceId = -1;
        this.mMode = c.mMode;
        this.mSourceAddress = c.mSourceAddress;
        this.mDestinationAddress = c.mDestinationAddress;
        this.mNetwork = c.mNetwork;
        this.mSpiResourceId = c.mSpiResourceId;
        this.mEncryption = c.mEncryption;
        this.mAuthentication = c.mAuthentication;
        this.mAuthenticatedEncryption = c.mAuthenticatedEncryption;
        this.mEncapType = c.mEncapType;
        this.mEncapSocketResourceId = c.mEncapSocketResourceId;
        this.mEncapRemotePort = c.mEncapRemotePort;
        this.mNattKeepaliveInterval = c.mNattKeepaliveInterval;
        this.mMarkValue = c.mMarkValue;
        this.mMarkMask = c.mMarkMask;
        this.mXfrmInterfaceId = c.mXfrmInterfaceId;
    }

    private IpSecConfig(Parcel in) {
        this.mMode = 0;
        String str = "";
        this.mSourceAddress = str;
        this.mDestinationAddress = str;
        this.mSpiResourceId = -1;
        this.mEncapType = 0;
        this.mEncapSocketResourceId = -1;
        this.mMode = in.readInt();
        this.mSourceAddress = in.readString();
        this.mDestinationAddress = in.readString();
        this.mNetwork = (Network) in.readParcelable(Network.class.getClassLoader());
        this.mSpiResourceId = in.readInt();
        this.mEncryption = (IpSecAlgorithm) in.readParcelable(IpSecAlgorithm.class.getClassLoader());
        this.mAuthentication = (IpSecAlgorithm) in.readParcelable(IpSecAlgorithm.class.getClassLoader());
        this.mAuthenticatedEncryption = (IpSecAlgorithm) in.readParcelable(IpSecAlgorithm.class.getClassLoader());
        this.mEncapType = in.readInt();
        this.mEncapSocketResourceId = in.readInt();
        this.mEncapRemotePort = in.readInt();
        this.mNattKeepaliveInterval = in.readInt();
        this.mMarkValue = in.readInt();
        this.mMarkMask = in.readInt();
        this.mXfrmInterfaceId = in.readInt();
    }

    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("{mMode=");
        strBuilder.append(this.mMode == 1 ? "TUNNEL" : "TRANSPORT");
        strBuilder.append(", mSourceAddress=");
        strBuilder.append(this.mSourceAddress);
        strBuilder.append(", mDestinationAddress=");
        strBuilder.append(this.mDestinationAddress);
        strBuilder.append(", mNetwork=");
        strBuilder.append(this.mNetwork);
        strBuilder.append(", mEncapType=");
        strBuilder.append(this.mEncapType);
        strBuilder.append(", mEncapSocketResourceId=");
        strBuilder.append(this.mEncapSocketResourceId);
        strBuilder.append(", mEncapRemotePort=");
        strBuilder.append(this.mEncapRemotePort);
        strBuilder.append(", mNattKeepaliveInterval=");
        strBuilder.append(this.mNattKeepaliveInterval);
        strBuilder.append("{mSpiResourceId=");
        strBuilder.append(this.mSpiResourceId);
        strBuilder.append(", mEncryption=");
        strBuilder.append(this.mEncryption);
        strBuilder.append(", mAuthentication=");
        strBuilder.append(this.mAuthentication);
        strBuilder.append(", mAuthenticatedEncryption=");
        strBuilder.append(this.mAuthenticatedEncryption);
        strBuilder.append(", mMarkValue=");
        strBuilder.append(this.mMarkValue);
        strBuilder.append(", mMarkMask=");
        strBuilder.append(this.mMarkMask);
        strBuilder.append(", mXfrmInterfaceId=");
        strBuilder.append(this.mXfrmInterfaceId);
        strBuilder.append("}");
        return strBuilder.toString();
    }

    /* JADX WARNING: Missing block: B:12:0x002c, code skipped:
            if (r2.equals(r5.mNetwork) != false) goto L_0x0034;
     */
    /* JADX WARNING: Missing block: B:14:0x0032, code skipped:
            if (r4.mNetwork == r5.mNetwork) goto L_0x0034;
     */
    /* JADX WARNING: Missing block: B:16:0x0038, code skipped:
            if (r4.mEncapType != r5.mEncapType) goto L_0x0083;
     */
    /* JADX WARNING: Missing block: B:18:0x003e, code skipped:
            if (r4.mEncapSocketResourceId != r5.mEncapSocketResourceId) goto L_0x0083;
     */
    /* JADX WARNING: Missing block: B:20:0x0044, code skipped:
            if (r4.mEncapRemotePort != r5.mEncapRemotePort) goto L_0x0083;
     */
    /* JADX WARNING: Missing block: B:22:0x004a, code skipped:
            if (r4.mNattKeepaliveInterval != r5.mNattKeepaliveInterval) goto L_0x0083;
     */
    /* JADX WARNING: Missing block: B:24:0x0050, code skipped:
            if (r4.mSpiResourceId != r5.mSpiResourceId) goto L_0x0083;
     */
    /* JADX WARNING: Missing block: B:26:0x005a, code skipped:
            if (android.net.IpSecAlgorithm.equals(r4.mEncryption, r5.mEncryption) == false) goto L_0x0083;
     */
    /* JADX WARNING: Missing block: B:28:0x0064, code skipped:
            if (android.net.IpSecAlgorithm.equals(r4.mAuthenticatedEncryption, r5.mAuthenticatedEncryption) == false) goto L_0x0083;
     */
    /* JADX WARNING: Missing block: B:30:0x006e, code skipped:
            if (android.net.IpSecAlgorithm.equals(r4.mAuthentication, r5.mAuthentication) == false) goto L_0x0083;
     */
    /* JADX WARNING: Missing block: B:32:0x0074, code skipped:
            if (r4.mMarkValue != r5.mMarkValue) goto L_0x0083;
     */
    /* JADX WARNING: Missing block: B:34:0x007a, code skipped:
            if (r4.mMarkMask != r5.mMarkMask) goto L_0x0083;
     */
    /* JADX WARNING: Missing block: B:36:0x0080, code skipped:
            if (r4.mXfrmInterfaceId != r5.mXfrmInterfaceId) goto L_0x0083;
     */
    @com.android.internal.annotations.VisibleForTesting
    public static boolean equals(android.net.IpSecConfig r4, android.net.IpSecConfig r5) {
        /*
        r0 = 1;
        r1 = 0;
        if (r4 == 0) goto L_0x0085;
    L_0x0004:
        if (r5 != 0) goto L_0x0008;
    L_0x0006:
        goto L_0x0085;
    L_0x0008:
        r2 = r4.mMode;
        r3 = r5.mMode;
        if (r2 != r3) goto L_0x0083;
    L_0x000e:
        r2 = r4.mSourceAddress;
        r3 = r5.mSourceAddress;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0083;
    L_0x0018:
        r2 = r4.mDestinationAddress;
        r3 = r5.mDestinationAddress;
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x0083;
    L_0x0022:
        r2 = r4.mNetwork;
        if (r2 == 0) goto L_0x002e;
    L_0x0026:
        r3 = r5.mNetwork;
        r2 = r2.equals(r3);
        if (r2 != 0) goto L_0x0034;
    L_0x002e:
        r2 = r4.mNetwork;
        r3 = r5.mNetwork;
        if (r2 != r3) goto L_0x0083;
    L_0x0034:
        r2 = r4.mEncapType;
        r3 = r5.mEncapType;
        if (r2 != r3) goto L_0x0083;
    L_0x003a:
        r2 = r4.mEncapSocketResourceId;
        r3 = r5.mEncapSocketResourceId;
        if (r2 != r3) goto L_0x0083;
    L_0x0040:
        r2 = r4.mEncapRemotePort;
        r3 = r5.mEncapRemotePort;
        if (r2 != r3) goto L_0x0083;
    L_0x0046:
        r2 = r4.mNattKeepaliveInterval;
        r3 = r5.mNattKeepaliveInterval;
        if (r2 != r3) goto L_0x0083;
    L_0x004c:
        r2 = r4.mSpiResourceId;
        r3 = r5.mSpiResourceId;
        if (r2 != r3) goto L_0x0083;
    L_0x0052:
        r2 = r4.mEncryption;
        r3 = r5.mEncryption;
        r2 = android.net.IpSecAlgorithm.equals(r2, r3);
        if (r2 == 0) goto L_0x0083;
    L_0x005c:
        r2 = r4.mAuthenticatedEncryption;
        r3 = r5.mAuthenticatedEncryption;
        r2 = android.net.IpSecAlgorithm.equals(r2, r3);
        if (r2 == 0) goto L_0x0083;
    L_0x0066:
        r2 = r4.mAuthentication;
        r3 = r5.mAuthentication;
        r2 = android.net.IpSecAlgorithm.equals(r2, r3);
        if (r2 == 0) goto L_0x0083;
    L_0x0070:
        r2 = r4.mMarkValue;
        r3 = r5.mMarkValue;
        if (r2 != r3) goto L_0x0083;
    L_0x0076:
        r2 = r4.mMarkMask;
        r3 = r5.mMarkMask;
        if (r2 != r3) goto L_0x0083;
    L_0x007c:
        r2 = r4.mXfrmInterfaceId;
        r3 = r5.mXfrmInterfaceId;
        if (r2 != r3) goto L_0x0083;
    L_0x0082:
        goto L_0x0084;
    L_0x0083:
        r0 = r1;
    L_0x0084:
        return r0;
    L_0x0085:
        if (r4 != r5) goto L_0x0088;
    L_0x0087:
        goto L_0x0089;
    L_0x0088:
        r0 = r1;
    L_0x0089:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.IpSecConfig.equals(android.net.IpSecConfig, android.net.IpSecConfig):boolean");
    }
}
