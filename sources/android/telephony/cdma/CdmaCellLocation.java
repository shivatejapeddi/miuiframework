package android.telephony.cdma;

import android.annotation.UnsupportedAppUsage;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.CellLocation;
import miui.telephony.PhoneDebug;

public class CdmaCellLocation extends CellLocation {
    public static final int INVALID_LAT_LONG = Integer.MAX_VALUE;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private int mBaseStationId;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private int mBaseStationLatitude;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private int mBaseStationLongitude;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private int mNetworkId;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private int mSystemId;

    public CdmaCellLocation() {
        this.mBaseStationId = -1;
        this.mBaseStationLatitude = Integer.MAX_VALUE;
        this.mBaseStationLongitude = Integer.MAX_VALUE;
        this.mSystemId = -1;
        this.mNetworkId = -1;
        this.mBaseStationId = -1;
        this.mBaseStationLatitude = Integer.MAX_VALUE;
        this.mBaseStationLongitude = Integer.MAX_VALUE;
        this.mSystemId = -1;
        this.mNetworkId = -1;
    }

    public CdmaCellLocation(Bundle bundle) {
        this.mBaseStationId = -1;
        this.mBaseStationLatitude = Integer.MAX_VALUE;
        this.mBaseStationLongitude = Integer.MAX_VALUE;
        this.mSystemId = -1;
        this.mNetworkId = -1;
        this.mBaseStationId = bundle.getInt("baseStationId", this.mBaseStationId);
        this.mBaseStationLatitude = bundle.getInt("baseStationLatitude", this.mBaseStationLatitude);
        this.mBaseStationLongitude = bundle.getInt("baseStationLongitude", this.mBaseStationLongitude);
        this.mSystemId = bundle.getInt(Intent.EXTRA_SYSTEM_ID, this.mSystemId);
        this.mNetworkId = bundle.getInt(Intent.EXTRA_NETWORK_ID, this.mNetworkId);
    }

    public int getBaseStationId() {
        return this.mBaseStationId;
    }

    public int getBaseStationLatitude() {
        return this.mBaseStationLatitude;
    }

    public int getBaseStationLongitude() {
        return this.mBaseStationLongitude;
    }

    public int getSystemId() {
        return this.mSystemId;
    }

    public int getNetworkId() {
        return this.mNetworkId;
    }

    public void setStateInvalid() {
        this.mBaseStationId = -1;
        this.mBaseStationLatitude = Integer.MAX_VALUE;
        this.mBaseStationLongitude = Integer.MAX_VALUE;
        this.mSystemId = -1;
        this.mNetworkId = -1;
    }

    public void setCellLocationData(int baseStationId, int baseStationLatitude, int baseStationLongitude) {
        this.mBaseStationId = baseStationId;
        this.mBaseStationLatitude = baseStationLatitude;
        this.mBaseStationLongitude = baseStationLongitude;
    }

    public void setCellLocationData(int baseStationId, int baseStationLatitude, int baseStationLongitude, int systemId, int networkId) {
        this.mBaseStationId = baseStationId;
        this.mBaseStationLatitude = baseStationLatitude;
        this.mBaseStationLongitude = baseStationLongitude;
        this.mSystemId = systemId;
        this.mNetworkId = networkId;
    }

    public int hashCode() {
        return (((this.mBaseStationId ^ this.mBaseStationLatitude) ^ this.mBaseStationLongitude) ^ this.mSystemId) ^ this.mNetworkId;
    }

    public boolean equals(Object o) {
        boolean z = false;
        try {
            CdmaCellLocation s = (CdmaCellLocation) o;
            if (o == null) {
                return false;
            }
            if (equalsHandlesNulls(Integer.valueOf(this.mBaseStationId), Integer.valueOf(s.mBaseStationId)) && equalsHandlesNulls(Integer.valueOf(this.mBaseStationLatitude), Integer.valueOf(s.mBaseStationLatitude)) && equalsHandlesNulls(Integer.valueOf(this.mBaseStationLongitude), Integer.valueOf(s.mBaseStationLongitude)) && equalsHandlesNulls(Integer.valueOf(this.mSystemId), Integer.valueOf(s.mSystemId)) && equalsHandlesNulls(Integer.valueOf(this.mNetworkId), Integer.valueOf(s.mNetworkId))) {
                z = true;
            }
            return z;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public String toString() {
        if (!PhoneDebug.VDBG) {
            return "CdmaCellLocation:[...]";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        stringBuilder.append(this.mBaseStationId);
        String str = ",";
        stringBuilder.append(str);
        stringBuilder.append(this.mBaseStationLatitude);
        stringBuilder.append(str);
        stringBuilder.append(this.mBaseStationLongitude);
        stringBuilder.append(str);
        stringBuilder.append(this.mSystemId);
        stringBuilder.append(str);
        stringBuilder.append(this.mNetworkId);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private static boolean equalsHandlesNulls(Object a, Object b) {
        if (a == null) {
            return b == null;
        } else {
            return a.equals(b);
        }
    }

    public void fillInNotifierBundle(Bundle bundleToFill) {
        bundleToFill.putInt("baseStationId", this.mBaseStationId);
        bundleToFill.putInt("baseStationLatitude", this.mBaseStationLatitude);
        bundleToFill.putInt("baseStationLongitude", this.mBaseStationLongitude);
        bundleToFill.putInt(Intent.EXTRA_SYSTEM_ID, this.mSystemId);
        bundleToFill.putInt(Intent.EXTRA_NETWORK_ID, this.mNetworkId);
    }

    public boolean isEmpty() {
        return this.mBaseStationId == -1 && this.mBaseStationLatitude == Integer.MAX_VALUE && this.mBaseStationLongitude == Integer.MAX_VALUE && this.mSystemId == -1 && this.mNetworkId == -1;
    }

    public static double convertQuartSecToDecDegrees(int quartSec) {
        if (!Double.isNaN((double) quartSec) && quartSec >= -2592000 && quartSec <= 2592000) {
            return ((double) quartSec) / 14400.0d;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid coordiante value:");
        stringBuilder.append(quartSec);
        throw new IllegalArgumentException(stringBuilder.toString());
    }
}
