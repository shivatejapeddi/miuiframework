package miui.miprint.sdk;

import android.net.Uri;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.print.PrinterId;

public final class P2pPrinterInfo implements Parcelable {
    public static final Creator<P2pPrinterInfo> CREATOR = new Creator<P2pPrinterInfo>() {
        public P2pPrinterInfo createFromParcel(Parcel source) {
            return new P2pPrinterInfo(source);
        }

        public P2pPrinterInfo[] newArray(int size) {
            return new P2pPrinterInfo[size];
        }
    };
    private String mLocation;
    private WifiP2pDevice mPeer;
    private PrinterId mPrinterId;
    private String mSummary;
    private String mTitle;
    private Uri mUuid;

    public P2pPrinterInfo(PrinterId printerId, String title, String summary, String location, Uri uuid, WifiP2pDevice peer) {
        this.mPrinterId = printerId;
        this.mTitle = title;
        this.mSummary = summary;
        this.mLocation = location;
        this.mUuid = uuid;
        this.mPeer = peer;
    }

    public P2pPrinterInfo(Parcel parcel) {
        readFromParcel(parcel);
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(this.mPrinterId, flags);
        parcel.writeString(this.mTitle);
        parcel.writeString(this.mSummary);
        parcel.writeString(this.mLocation);
        parcel.writeParcelable(this.mUuid, flags);
        parcel.writeParcelable(this.mPeer, flags);
    }

    public void readFromParcel(Parcel parcel) {
        this.mPrinterId = (PrinterId) parcel.readParcelable(null);
        this.mTitle = parcel.readString();
        this.mSummary = parcel.readString();
        this.mLocation = parcel.readString();
        this.mUuid = (Uri) parcel.readParcelable(null);
        this.mPeer = (WifiP2pDevice) parcel.readParcelable(null);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DiscoveredPrinterInfo{");
        if (this.mPrinterId != null) {
            builder.append("mPrinterId=");
            builder.append(this.mPrinterId.toString());
        }
        builder.append(", mTitle=");
        builder.append(this.mTitle);
        builder.append(", mSummary=");
        builder.append(this.mSummary);
        builder.append(", mLocation=");
        builder.append(this.mLocation);
        if (this.mUuid != null) {
            builder.append(", mUuid=");
            builder.append(this.mUuid.toString());
        }
        if (this.mPeer != null) {
            builder.append(", mPeer=");
            builder.append(this.mPeer.toString());
        }
        builder.append('}');
        return builder.toString();
    }

    public PrinterId getPrinterId() {
        return this.mPrinterId;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public String getSummary() {
        return this.mSummary;
    }

    public String getLocation() {
        return this.mLocation;
    }

    public Uri getUuid() {
        return this.mUuid;
    }

    public WifiP2pDevice getPeer() {
        return this.mPeer;
    }

    public int describeContents() {
        return 0;
    }
}
