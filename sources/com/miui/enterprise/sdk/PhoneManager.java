package com.miui.enterprise.sdk;

import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import com.miui.enterprise.EnterpriseManager;
import com.miui.enterprise.IPhoneManager;
import com.miui.enterprise.IPhoneManager.Stub;
import java.util.List;

public class PhoneManager {
    public static final int CLOSE = 3;
    public static final int DISABLE = 0;
    public static final int ENABLE = 1;
    public static final int FLAG_DEFAULT = 0;
    public static final int FLAG_DISALLOW_IN = 1;
    public static final int FLAG_DISALLOW_OUT = 2;
    public static final int FORCE_OPEN = 4;
    public static final int OPEN = 2;
    public static final int RESTRICTION_MODE_BLACK_LIST = 2;
    public static final int RESTRICTION_MODE_DEFAULT = 0;
    public static final int RESTRICTION_MODE_WHITE_LIST = 1;
    private static final String TAG = "PhoneManager";
    private static IPhoneManager mService;
    private static volatile PhoneManager sInstance;

    private PhoneManager() {
        mService = Stub.asInterface(EnterpriseManager.getEnterpriseService(EnterpriseManager.PHONE_MANAGER));
    }

    public static synchronized PhoneManager getInstance() {
        PhoneManager phoneManager;
        synchronized (PhoneManager.class) {
            if (sInstance == null) {
                sInstance = new PhoneManager();
            }
            phoneManager = sInstance;
        }
        return phoneManager;
    }

    public void controlSMS(int flags) {
        try {
            mService.controlSMS(flags, UserHandle.myUserId());
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
        }
    }

    public void controlPhoneCall(int flags) {
        try {
            mService.controlPhoneCall(flags, UserHandle.myUserId());
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
        }
    }

    public void controlCellular(int flags) {
        try {
            mService.controlCellular(flags, UserHandle.myUserId());
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
        }
    }

    public int getSMSStatus() {
        try {
            return mService.getSMSStatus(UserHandle.myUserId());
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
            return 0;
        }
    }

    public int getPhoneCallStatus() {
        try {
            return mService.getPhoneCallStatus(UserHandle.myUserId());
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
            return 0;
        }
    }

    public int getCellularStatus() {
        try {
            return mService.getCellularStatus(UserHandle.myUserId());
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
            return 0;
        }
    }

    public String getIMEI(int slotId) {
        try {
            return mService.getIMEI(slotId);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
            return "";
        }
    }

    public void setPhoneCallAutoRecord(boolean isAutoRecord) {
        try {
            mService.setPhoneCallAutoRecord(isAutoRecord, UserHandle.myUserId());
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
        }
    }

    public void setPhoneCallAutoRecordDir(String filePath) {
        try {
            mService.setPhoneCallAutoRecordDir(filePath);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
        }
    }

    public boolean isAutoRecordPhoneCall() {
        try {
            return mService.isAutoRecordPhoneCall(UserHandle.myUserId());
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
            return false;
        }
    }

    public void setSMSBlackList(List<String> list) {
        try {
            mService.setSMSBlackList(list, UserHandle.myUserId());
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
        }
    }

    public List<String> getSMSBlackList() {
        try {
            return mService.getSMSBlackList(UserHandle.myUserId());
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
            return null;
        }
    }

    public void setSMSWhiteList(List<String> list) {
        try {
            mService.setSMSWhiteList(list, UserHandle.myUserId());
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
        }
    }

    public List<String> getSMSWhiteList() {
        try {
            return mService.getSMSWhiteList(UserHandle.myUserId());
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
            return null;
        }
    }

    public void setSMSContactRestriction(int mode) {
        try {
            mService.setSMSContactRestriction(mode, UserHandle.myUserId());
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
        }
    }

    public int getSMSContactRestriction() {
        try {
            return mService.getSMSContactRestriction(UserHandle.myUserId());
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
            return 0;
        }
    }

    public void setCallBlackList(List<String> list) {
        try {
            mService.setCallBlackList(list, UserHandle.myUserId());
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
        }
    }

    public List<String> getCallBlackList() {
        try {
            return mService.getCallBlackList(UserHandle.myUserId());
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
            return null;
        }
    }

    public void setCallWhiteList(List<String> list) {
        try {
            mService.setCallWhiteList(list, UserHandle.myUserId());
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
        }
    }

    public List<String> getCallWhiteList() {
        try {
            return mService.getCallWhiteList(UserHandle.myUserId());
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
            return null;
        }
    }

    public void setCallContactRestriction(int mode) {
        try {
            mService.setCallContactRestriction(mode, UserHandle.myUserId());
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
        }
    }

    public int getCallContactRestriction() {
        try {
            return mService.getCallContactRestriction(UserHandle.myUserId());
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
            return 0;
        }
    }

    public void endCall() {
        try {
            mService.endCall();
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
        }
    }

    public void disableCallForward(boolean disable) {
        try {
            mService.disableCallForward(disable);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
        }
    }

    public void disableCallLog(boolean disable) {
        try {
            mService.disableCallLog(disable);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
        }
    }

    public String getAreaCode(String phoneNumber) {
        try {
            return mService.getAreaCode(phoneNumber);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
            return "";
        }
    }

    public String getMeid(int slotId) {
        try {
            return mService.getMeid(slotId);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
            return "";
        }
    }

    public void setIccCardActivate(int slotId, boolean isActive) {
        try {
            mService.setIccCardActivate(slotId, isActive);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
        }
    }
}
