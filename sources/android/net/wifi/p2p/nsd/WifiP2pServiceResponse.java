package android.net.wifi.p2p.nsd;

import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.telephony.IccCardConstants;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WifiP2pServiceResponse implements Parcelable {
    public static final Creator<WifiP2pServiceResponse> CREATOR = new Creator<WifiP2pServiceResponse>() {
        public WifiP2pServiceResponse createFromParcel(Parcel in) {
            byte[] data;
            int type = in.readInt();
            int status = in.readInt();
            int transId = in.readInt();
            WifiP2pDevice dev = (WifiP2pDevice) in.readParcelable(null);
            int len = in.readInt();
            if (len > 0) {
                byte[] data2 = new byte[len];
                in.readByteArray(data2);
                data = data2;
            } else {
                data = null;
            }
            if (type == 1) {
                return WifiP2pDnsSdServiceResponse.newInstance(status, transId, dev, data);
            }
            if (type == 2) {
                return WifiP2pUpnpServiceResponse.newInstance(status, transId, dev, data);
            }
            return new WifiP2pServiceResponse(type, status, transId, dev, data);
        }

        public WifiP2pServiceResponse[] newArray(int size) {
            return new WifiP2pServiceResponse[size];
        }
    };
    private static int MAX_BUF_SIZE = 1024;
    protected byte[] mData;
    protected WifiP2pDevice mDevice;
    protected int mServiceType;
    protected int mStatus;
    protected int mTransId;

    public static class Status {
        public static final int BAD_REQUEST = 3;
        public static final int REQUESTED_INFORMATION_NOT_AVAILABLE = 2;
        public static final int SERVICE_PROTOCOL_NOT_AVAILABLE = 1;
        public static final int SUCCESS = 0;

        public static String toString(int status) {
            if (status == 0) {
                return "SUCCESS";
            }
            if (status == 1) {
                return "SERVICE_PROTOCOL_NOT_AVAILABLE";
            }
            if (status == 2) {
                return "REQUESTED_INFORMATION_NOT_AVAILABLE";
            }
            if (status != 3) {
                return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
            }
            return "BAD_REQUEST";
        }

        private Status() {
        }
    }

    protected WifiP2pServiceResponse(int serviceType, int status, int transId, WifiP2pDevice device, byte[] data) {
        this.mServiceType = serviceType;
        this.mStatus = status;
        this.mTransId = transId;
        this.mDevice = device;
        this.mData = data;
    }

    public int getServiceType() {
        return this.mServiceType;
    }

    public int getStatus() {
        return this.mStatus;
    }

    public int getTransactionId() {
        return this.mTransId;
    }

    public byte[] getRawData() {
        return this.mData;
    }

    public WifiP2pDevice getSrcDevice() {
        return this.mDevice;
    }

    public void setSrcDevice(WifiP2pDevice dev) {
        if (dev != null) {
            this.mDevice = dev;
        }
    }

    public static List<WifiP2pServiceResponse> newInstance(String srcAddr, byte[] tlvsBin) {
        byte[] bArr = tlvsBin;
        ArrayList respList = new ArrayList();
        WifiP2pDevice dev = new WifiP2pDevice();
        dev.deviceAddress = srcAddr;
        List<WifiP2pServiceResponse> data = null;
        if (bArr == null) {
            return null;
        }
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bArr));
        while (dis.available() > 0) {
            try {
                int length = (dis.readUnsignedByte() + (dis.readUnsignedByte() << 8)) - 3;
                int type = dis.readUnsignedByte();
                int transId = dis.readUnsignedByte();
                int status = dis.readUnsignedByte();
                if (length < 0) {
                    return data;
                }
                WifiP2pServiceResponse wifiP2pServiceResponse;
                if (length == 0) {
                    if (status == 0) {
                        WifiP2pServiceResponse wifiP2pServiceResponse2 = wifiP2pServiceResponse;
                        wifiP2pServiceResponse = new WifiP2pServiceResponse(type, status, transId, dev, null);
                        respList.add(wifiP2pServiceResponse2);
                        data = null;
                    } else {
                        data = null;
                    }
                } else if (length > MAX_BUF_SIZE) {
                    dis.skip((long) length);
                    data = null;
                } else {
                    byte[] data2 = new byte[length];
                    dis.readFully(data2);
                    if (type == 1) {
                        wifiP2pServiceResponse = WifiP2pDnsSdServiceResponse.newInstance(status, transId, dev, data2);
                    } else if (type == 2) {
                        wifiP2pServiceResponse = WifiP2pUpnpServiceResponse.newInstance(status, transId, dev, data2);
                    } else {
                        wifiP2pServiceResponse = new WifiP2pServiceResponse(type, status, transId, dev, data2);
                    }
                    if (wifiP2pServiceResponse != null && wifiP2pServiceResponse.getStatus() == 0) {
                        respList.add(wifiP2pServiceResponse);
                    }
                    data = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (respList.size() > 0) {
                    return respList;
                }
                return null;
            }
        }
        return respList;
    }

    private static byte[] hexStr2Bin(String hex) {
        int sz = hex.length() / 2;
        byte[] b = new byte[(hex.length() / 2)];
        int i = 0;
        while (i < sz) {
            try {
                b[i] = (byte) Integer.parseInt(hex.substring(i * 2, (i * 2) + 2), 16);
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return b;
    }

    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("serviceType:");
        sbuf.append(this.mServiceType);
        sbuf.append(" status:");
        sbuf.append(Status.toString(this.mStatus));
        sbuf.append(" srcAddr:");
        sbuf.append(this.mDevice.deviceAddress);
        sbuf.append(" data:");
        sbuf.append(Arrays.toString(this.mData));
        return sbuf.toString();
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (o == this) {
            return true;
        }
        if (!(o instanceof WifiP2pServiceResponse)) {
            return false;
        }
        WifiP2pServiceResponse req = (WifiP2pServiceResponse) o;
        if (!(req.mServiceType == this.mServiceType && req.mStatus == this.mStatus && equals(req.mDevice.deviceAddress, this.mDevice.deviceAddress) && Arrays.equals(req.mData, this.mData))) {
            z = false;
        }
        return z;
    }

    private boolean equals(Object a, Object b) {
        if (a == null && b == null) {
            return true;
        }
        if (a != null) {
            return a.equals(b);
        }
        return false;
    }

    public int hashCode() {
        int i = 0;
        int hashCode = ((((((((17 * 31) + this.mServiceType) * 31) + this.mStatus) * 31) + this.mTransId) * 31) + (this.mDevice.deviceAddress == null ? 0 : this.mDevice.deviceAddress.hashCode())) * 31;
        byte[] bArr = this.mData;
        if (bArr != null) {
            i = Arrays.hashCode(bArr);
        }
        return hashCode + i;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mServiceType);
        dest.writeInt(this.mStatus);
        dest.writeInt(this.mTransId);
        dest.writeParcelable(this.mDevice, flags);
        byte[] bArr = this.mData;
        if (bArr == null || bArr.length == 0) {
            dest.writeInt(0);
            return;
        }
        dest.writeInt(bArr.length);
        dest.writeByteArray(this.mData);
    }
}
