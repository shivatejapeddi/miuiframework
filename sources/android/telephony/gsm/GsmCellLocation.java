package android.telephony.gsm;

import android.annotation.UnsupportedAppUsage;
import android.os.Bundle;
import android.provider.Telephony.CellBroadcasts;
import android.telephony.CellLocation;
import miui.telephony.PhoneDebug;

public class GsmCellLocation extends CellLocation {
    private int mCid;
    private int mLac;
    private int mPsc;

    public GsmCellLocation() {
        this.mLac = -1;
        this.mCid = -1;
        this.mPsc = -1;
    }

    public GsmCellLocation(Bundle bundle) {
        this.mLac = bundle.getInt(CellBroadcasts.LAC, -1);
        this.mCid = bundle.getInt("cid", -1);
        this.mPsc = bundle.getInt("psc", -1);
    }

    public int getLac() {
        return this.mLac;
    }

    public int getCid() {
        return this.mCid;
    }

    public int getPsc() {
        return this.mPsc;
    }

    public void setStateInvalid() {
        this.mLac = -1;
        this.mCid = -1;
        this.mPsc = -1;
    }

    public void setLacAndCid(int lac, int cid) {
        this.mLac = lac;
        this.mCid = cid;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public void setPsc(int psc) {
        this.mPsc = psc;
    }

    public int hashCode() {
        return this.mLac ^ this.mCid;
    }

    public boolean equals(Object o) {
        boolean z = false;
        try {
            GsmCellLocation s = (GsmCellLocation) o;
            if (o == null) {
                return false;
            }
            if (equalsHandlesNulls(Integer.valueOf(this.mLac), Integer.valueOf(s.mLac)) && equalsHandlesNulls(Integer.valueOf(this.mCid), Integer.valueOf(s.mCid)) && equalsHandlesNulls(Integer.valueOf(this.mPsc), Integer.valueOf(s.mPsc))) {
                z = true;
            }
            return z;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public String toString() {
        if (!PhoneDebug.VDBG) {
            return "GsmCellLocation:[...]";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        stringBuilder.append(this.mLac);
        String str = ",";
        stringBuilder.append(str);
        stringBuilder.append(this.mCid);
        stringBuilder.append(str);
        stringBuilder.append(this.mPsc);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    private static boolean equalsHandlesNulls(Object a, Object b) {
        if (a == null) {
            return b == null;
        } else {
            return a.equals(b);
        }
    }

    public void fillInNotifierBundle(Bundle m) {
        m.putInt(CellBroadcasts.LAC, this.mLac);
        m.putInt("cid", this.mCid);
        m.putInt("psc", this.mPsc);
    }

    public boolean isEmpty() {
        return this.mLac == -1 && this.mCid == -1 && this.mPsc == -1;
    }
}
