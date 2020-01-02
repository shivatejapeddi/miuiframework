package miui.telephony;

import android.os.Bundle;
import android.os.ResultReceiver;
import android.telephony.Rlog;
import java.util.ArrayList;
import java.util.List;
import miui.telephony.IMiuiTelephony.Stub;

public abstract class MiuiTelephonyBase extends Stub {
    private static String LOG_TAG = "MiuiTelephonyBase";

    public boolean isImsRegistered(int phoneId) {
        return false;
    }

    public boolean isVideoTelephonyAvailable(int phoneId) {
        return false;
    }

    public boolean isWifiCallingAvailable(int phoneId) {
        return false;
    }

    public boolean isVolteEnabledByUser() {
        return false;
    }

    public boolean isVolteEnabledByUserForSlot(int slotId) {
        return false;
    }

    public boolean isVtEnabledByPlatform() {
        return false;
    }

    public boolean isVtEnabledByPlatformForSlot(int slotId) {
        return false;
    }

    public boolean isVolteEnabledByPlatform() {
        return false;
    }

    public boolean isVolteEnabledByPlatformForSlot(int slotId) {
        return false;
    }

    public List<String> getDeviceIdList(String callingPackage) {
        Rlog.d(LOG_TAG, "unexpected getDeviceIdList method call");
        return new ArrayList(0);
    }

    public List<String> getImeiList(String callingPackage) {
        return new ArrayList(0);
    }

    public List<String> getMeidList(String callingPackage) {
        return new ArrayList(0);
    }

    public String getDeviceId(String callingPackage) {
        return null;
    }

    public String getImei(int slotId, String callingPackage) {
        return null;
    }

    public String getMeid(int slotId, String callingPackage) {
        return null;
    }

    public Bundle getCellLocationForSlot(int slotId, String callingPkg) {
        Rlog.d(LOG_TAG, "unexpected getCellLocation method call");
        return null;
    }

    public String getSmallDeviceId(String callingPackage) {
        return null;
    }

    public void setCallForwardingOption(int phoneId, int action, int reason, String number, ResultReceiver callback) {
        Rlog.d(LOG_TAG, "unexpected setCallForwardingOption method call");
    }

    public void setIccCardActivate(int slotId, boolean isActivate) {
    }

    public boolean isGwsdSupport() {
        return false;
    }
}
