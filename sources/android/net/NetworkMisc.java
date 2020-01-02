package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class NetworkMisc implements Parcelable {
    public static final Creator<NetworkMisc> CREATOR = new Creator<NetworkMisc>() {
        public NetworkMisc createFromParcel(Parcel in) {
            NetworkMisc networkMisc = new NetworkMisc();
            boolean z = true;
            networkMisc.allowBypass = in.readInt() != 0;
            networkMisc.explicitlySelected = in.readInt() != 0;
            networkMisc.acceptUnvalidated = in.readInt() != 0;
            networkMisc.keepScore = in.readInt() != 0;
            networkMisc.subscriberId = in.readString();
            networkMisc.provisioningNotificationDisabled = in.readInt() != 0;
            if (in.readInt() == 0) {
                z = false;
            }
            networkMisc.skip464xlat = z;
            return networkMisc;
        }

        public NetworkMisc[] newArray(int size) {
            return new NetworkMisc[size];
        }
    };
    public boolean acceptPartialConnectivity;
    public boolean acceptUnvalidated;
    public boolean allowBypass;
    public boolean explicitlySelected;
    public boolean keepScore;
    public boolean provisioningNotificationDisabled;
    public boolean skip464xlat;
    public String subscriberId;

    public NetworkMisc(NetworkMisc nm) {
        if (nm != null) {
            this.allowBypass = nm.allowBypass;
            this.explicitlySelected = nm.explicitlySelected;
            this.acceptUnvalidated = nm.acceptUnvalidated;
            this.subscriberId = nm.subscriberId;
            this.provisioningNotificationDisabled = nm.provisioningNotificationDisabled;
            this.skip464xlat = nm.skip464xlat;
            this.keepScore = nm.keepScore;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.allowBypass);
        out.writeInt(this.explicitlySelected);
        out.writeInt(this.acceptUnvalidated);
        out.writeInt(this.keepScore);
        out.writeString(this.subscriberId);
        out.writeInt(this.provisioningNotificationDisabled);
        out.writeInt(this.skip464xlat);
    }
}
