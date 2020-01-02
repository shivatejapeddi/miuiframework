package android.telephony.ims;

import android.annotation.SystemApi;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.telecom.Log;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

@SystemApi
public final class ImsConferenceState implements Parcelable {
    public static final Creator<ImsConferenceState> CREATOR = new Creator<ImsConferenceState>() {
        public ImsConferenceState createFromParcel(Parcel in) {
            return new ImsConferenceState(in, null);
        }

        public ImsConferenceState[] newArray(int size) {
            return new ImsConferenceState[size];
        }
    };
    public static final String DISPLAY_TEXT = "display-text";
    public static final String ENDPOINT = "endpoint";
    public static final String SIP_STATUS_CODE = "sipstatuscode";
    public static final String STATUS = "status";
    public static final String STATUS_ALERTING = "alerting";
    public static final String STATUS_CONNECTED = "connected";
    public static final String STATUS_CONNECT_FAIL = "connect-fail";
    public static final String STATUS_DIALING_IN = "dialing-in";
    public static final String STATUS_DIALING_OUT = "dialing-out";
    public static final String STATUS_DISCONNECTED = "disconnected";
    public static final String STATUS_DISCONNECTING = "disconnecting";
    public static final String STATUS_MUTED_VIA_FOCUS = "muted-via-focus";
    public static final String STATUS_ON_HOLD = "on-hold";
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_SEND_ONLY = "sendonly";
    public static final String STATUS_SEND_RECV = "sendrecv";
    public static final String USER = "user";
    public final HashMap<String, Bundle> mParticipants;

    /* synthetic */ ImsConferenceState(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public ImsConferenceState() {
        this.mParticipants = new LinkedHashMap();
    }

    private ImsConferenceState(Parcel in) {
        this.mParticipants = new LinkedHashMap();
        readFromParcel(in);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mParticipants.size());
        if (this.mParticipants.size() > 0) {
            Set<Entry<String, Bundle>> entries = this.mParticipants.entrySet();
            if (entries != null) {
                for (Entry<String, Bundle> entry : entries) {
                    out.writeString((String) entry.getKey());
                    out.writeParcelable((Parcelable) entry.getValue(), 0);
                }
            }
        }
    }

    private void readFromParcel(Parcel in) {
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            this.mParticipants.put(in.readString(), (Bundle) in.readParcelable(null));
        }
    }

    /* JADX WARNING: Missing block: B:29:0x0068, code skipped:
            return 4;
     */
    public static int getConnectionStateForStatus(java.lang.String r2) {
        /*
        r0 = "pending";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x000b;
    L_0x0009:
        r0 = 0;
        return r0;
    L_0x000b:
        r0 = "dialing-in";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x0015;
    L_0x0013:
        r0 = 2;
        return r0;
    L_0x0015:
        r0 = "alerting";
        r0 = r2.equals(r0);
        if (r0 != 0) goto L_0x006b;
    L_0x001d:
        r0 = "dialing-out";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x0026;
    L_0x0025:
        goto L_0x006b;
    L_0x0026:
        r0 = "on-hold";
        r0 = r2.equals(r0);
        if (r0 != 0) goto L_0x0069;
    L_0x002f:
        r0 = "sendonly";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x0039;
    L_0x0038:
        goto L_0x0069;
    L_0x0039:
        r0 = "connected";
        r0 = r2.equals(r0);
        r1 = 4;
        if (r0 != 0) goto L_0x0068;
    L_0x0042:
        r0 = "muted-via-focus";
        r0 = r2.equals(r0);
        if (r0 != 0) goto L_0x0068;
    L_0x004b:
        r0 = "disconnecting";
        r0 = r2.equals(r0);
        if (r0 != 0) goto L_0x0068;
    L_0x0053:
        r0 = "sendrecv";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x005d;
    L_0x005c:
        goto L_0x0068;
    L_0x005d:
        r0 = "disconnected";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x0067;
    L_0x0065:
        r0 = 6;
        return r0;
    L_0x0067:
        return r1;
    L_0x0068:
        return r1;
    L_0x0069:
        r0 = 5;
        return r0;
    L_0x006b:
        r0 = 3;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.telephony.ims.ImsConferenceState.getConnectionStateForStatus(java.lang.String):int");
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(ImsConferenceState.class.getSimpleName());
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        if (this.mParticipants.size() > 0) {
            Set<Entry<String, Bundle>> entries = this.mParticipants.entrySet();
            if (entries != null) {
                sb.append("<");
                for (Entry<String, Bundle> entry : entries) {
                    sb.append(Log.pii(entry.getKey()));
                    sb.append(": ");
                    Bundle participantData = (Bundle) entry.getValue();
                    for (String key : participantData.keySet()) {
                        sb.append(key);
                        sb.append("=");
                        if (ENDPOINT.equals(key) || "user".equals(key)) {
                            sb.append(Log.pii(participantData.get(key)));
                        } else {
                            sb.append(participantData.get(key));
                        }
                        sb.append(", ");
                    }
                }
                sb.append(">");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
