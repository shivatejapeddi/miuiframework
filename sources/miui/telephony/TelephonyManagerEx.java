package miui.telephony;

import android.content.ContentResolver;
import android.content.Context;
import android.location.Country;
import android.location.CountryDetector;
import android.miui.R;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ServiceManager;
import android.provider.Settings.Global;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.Rlog;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import com.android.internal.telecom.ITelecomService;
import com.android.internal.telephony.ITelephony;
import com.android.internal.telephony.ITelephony.Stub;
import com.android.internal.telephony.ITelephonyRegistry;
import java.util.ArrayList;
import java.util.List;
import miui.util.AppConstants;

public class TelephonyManagerEx extends TelephonyManager {
    public static final int NETWORK_CLASS_2_G = 1;
    public static final int NETWORK_CLASS_3_G = 2;
    public static final int NETWORK_CLASS_4_G = 3;
    public static final int NETWORK_CLASS_UNKNOWN = 0;
    public static final int NETWORK_TYPE_GSM = 16;
    public static final int NETWORK_TYPE_TD_SCDMA = 17;
    public static final String PROPERTY_DBG_VOLTE_AVAIL_OVERRIDE = "persist.dbg.volte_avail_ovr";
    public static final String PROPERTY_DBG_VT_AVAIL_OVERRIDE = "persist.dbg.vt_avail_ovr";
    private static final String TAG = "TelephonyManager";
    private static ITelephonyRegistry sRegistry;

    static class Holder {
        static final Context CONTEXT = AppConstants.getCurrentApplication();
        static final int CT_VOLTE_SUPPORTED_MODE = CONTEXT.getResources().getInteger(R.integer.config_ct_volte_supported_mode);
        static final TelephonyManagerEx INSTANCE = new TelephonyManagerEx();
        static final boolean IS_CMCC_COOPERATION_DEVICE = CONTEXT.getResources().getBoolean(R.bool.config_is_cmcc_cooperation_device);
        static final boolean IS_DUAL_VOLTE_SUPPORTED = CONTEXT.getResources().getBoolean(R.bool.config_dual_volte_supported);
        static final int PHONE_COUNT = TELEPHONY_MANAGER.getPhoneCount();
        static final SubscriptionManager SUBSCRIPTION_MANAGER = SubscriptionManager.getDefault();
        static final TelephonyManager TELEPHONY_MANAGER = ((TelephonyManager) CONTEXT.getSystemService("phone"));

        private Holder() {
        }
    }

    private TelephonyManagerEx() {
    }

    private ITelephony getITelephony() {
        return Stub.asInterface(ServiceManager.getService("phone"));
    }

    private ITelecomService getTelecomService() {
        return ITelecomService.Stub.asInterface(ServiceManager.getService(Context.TELECOM_SERVICE));
    }

    public static TelephonyManagerEx getDefault() {
        return Holder.INSTANCE;
    }

    private int normalizeSlotId(int slotId) {
        if (slotId == SubscriptionManager.DEFAULT_SLOT_ID) {
            return Holder.SUBSCRIPTION_MANAGER.getDefaultSlotId();
        }
        return slotId;
    }

    private int normalizeSubscriptionId(int subId) {
        if (subId == SubscriptionManager.DEFAULT_SUBSCRIPTION_ID) {
            return Holder.SUBSCRIPTION_MANAGER.getDefaultSubscriptionId();
        }
        return subId;
    }

    public boolean isMultiSimEnabled() {
        return getPhoneCount() > 1;
    }

    public int getPhoneCount() {
        return Holder.PHONE_COUNT;
    }

    public String getDeviceSoftwareVersion() {
        return Holder.TELEPHONY_MANAGER.getDeviceSoftwareVersion();
    }

    public String getDeviceSoftwareVersionForSlot(int slotId) {
        return Holder.TELEPHONY_MANAGER.getDeviceSoftwareVersion();
    }

    public String getDeviceSoftwareVersionForSubscription(int subId) {
        return Holder.TELEPHONY_MANAGER.getDeviceSoftwareVersion();
    }

    public String getMiuiDeviceId() {
        String str = null;
        try {
            IMiuiTelephony telephony = getMiuiTelephony();
            if (telephony != null) {
                str = telephony.getDeviceId(Holder.CONTEXT.getOpPackageName());
            }
            return str;
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getDeviceId");
            stringBuilder.append(e);
            Rlog.e(TAG, stringBuilder.toString());
            return null;
        }
    }

    public String getSmallDeviceId() {
        String str = null;
        try {
            IMiuiTelephony telephony = getMiuiTelephony();
            if (telephony != null) {
                str = telephony.getSmallDeviceId(Holder.CONTEXT.getOpPackageName());
            }
            return str;
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getSmallDeviceId");
            stringBuilder.append(e);
            Rlog.e(TAG, stringBuilder.toString());
            return null;
        }
    }

    public List<String> getDeviceIdList() {
        try {
            IMiuiTelephony telephony = getMiuiTelephony();
            return telephony == null ? null : telephony.getDeviceIdList(Holder.CONTEXT.getOpPackageName());
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getDeviceIdList");
            stringBuilder.append(e);
            Rlog.e(TAG, stringBuilder.toString());
            return new ArrayList(0);
        }
    }

    public List<String> getImeiList() {
        try {
            IMiuiTelephony telephony = getMiuiTelephony();
            return telephony == null ? null : telephony.getImeiList(Holder.CONTEXT.getOpPackageName());
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getImeiList");
            stringBuilder.append(e);
            Rlog.e(TAG, stringBuilder.toString());
            return new ArrayList(0);
        }
    }

    public List<String> getMeidList() {
        try {
            IMiuiTelephony telephony = getMiuiTelephony();
            return telephony == null ? null : telephony.getMeidList(Holder.CONTEXT.getOpPackageName());
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getMeidList");
            stringBuilder.append(e);
            Rlog.e(TAG, stringBuilder.toString());
            return new ArrayList(0);
        }
    }

    public String getDeviceId() {
        return getDeviceIdForSlot(Holder.SUBSCRIPTION_MANAGER.getDefaultSlotId());
    }

    public String getDeviceIdForSlot(int slotId) {
        return Holder.TELEPHONY_MANAGER.getDeviceId(normalizeSlotId(slotId));
    }

    public String getDeviceIdForSubscription(int subId) {
        return getDeviceIdForSlot(Holder.SUBSCRIPTION_MANAGER.getSlotIdForSubscription(subId));
    }

    public String getImei() {
        return getImeiForSlot(Holder.SUBSCRIPTION_MANAGER.getDefaultSlotId());
    }

    public String getImeiForSlot(int slotId) {
        String str = null;
        try {
            IMiuiTelephony telephony = getMiuiTelephony();
            if (telephony != null) {
                str = telephony.getImei(normalizeSlotId(slotId), Holder.CONTEXT.getOpPackageName());
            }
            return str;
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getImeiForSlot ");
            stringBuilder.append(e);
            Rlog.e(TAG, stringBuilder.toString());
            return null;
        }
    }

    public String getImeiForSubscription(int subId) {
        return getImeiForSlot(Holder.SUBSCRIPTION_MANAGER.getSlotIdForSubscription(subId));
    }

    public String getMeid() {
        return getMeidForSlot(Holder.SUBSCRIPTION_MANAGER.getDefaultSlotId());
    }

    public String getMeidForSlot(int slotId) {
        String str = null;
        try {
            IMiuiTelephony telephony = getMiuiTelephony();
            if (telephony != null) {
                str = telephony.getMeid(normalizeSlotId(slotId), Holder.CONTEXT.getOpPackageName());
            }
            return str;
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getMeidForSlot ");
            stringBuilder.append(e);
            Rlog.e(TAG, stringBuilder.toString());
            return null;
        }
    }

    public String getMeidForSubscription(int subId) {
        return getMeidForSlot(Holder.SUBSCRIPTION_MANAGER.getSlotIdForSubscription(subId));
    }

    public CellLocation getCellLocation() {
        return Holder.TELEPHONY_MANAGER.getCellLocation();
    }

    public CellLocation getCellLocationForSlot(int slotId) {
        String str = TAG;
        try {
            IMiuiTelephony telephony = getMiuiTelephony();
            if (telephony == null) {
                return null;
            }
            Bundle bundle = telephony.getCellLocationForSlot(slotId, Holder.CONTEXT.getOpPackageName());
            if (bundle.isEmpty()) {
                Rlog.d(str, "getCellLocationForSlot returning null because bundle is empty");
                return null;
            }
            CellLocation cl = CellLocation.newFromBundle(bundle);
            if (!cl.isEmpty()) {
                return cl;
            }
            Rlog.d(str, "getCellLocationForSlot returning null because CellLocation is empty");
            return null;
        } catch (Exception ex) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getCellLocationForSlot returning null due to Exception ");
            stringBuilder.append(ex);
            Rlog.d(str, stringBuilder.toString());
            return null;
        }
    }

    public CellLocation getCellLocationForSubscription(int subId) {
        return Holder.TELEPHONY_MANAGER.getCellLocation();
    }

    public List<CellInfo> getAllCellInfo() {
        return getAllCellInfoForSubscription(Holder.SUBSCRIPTION_MANAGER.getDefaultSubscriptionId());
    }

    public List<CellInfo> getAllCellInfoForSlot(int slotId) {
        return getAllCellInfoForSubscription(Holder.SUBSCRIPTION_MANAGER.getSubscriptionIdForSlot(slotId));
    }

    public List<CellInfo> getAllCellInfoForSubscription(int subId) {
        return Holder.TELEPHONY_MANAGER.getAllCellInfo();
    }

    public List<NeighboringCellInfo> getNeighboringCellInfo() {
        return Holder.TELEPHONY_MANAGER.getNeighboringCellInfo();
    }

    public List<NeighboringCellInfo> getNeighboringCellInfoForSlot(int slotId) {
        return Holder.TELEPHONY_MANAGER.getNeighboringCellInfo();
    }

    public List<NeighboringCellInfo> getNeighboringCellInfoForSubscription(int subId) {
        return Holder.TELEPHONY_MANAGER.getNeighboringCellInfo();
    }

    public int getPhoneType() {
        return getPhoneTypeForSubscription(Holder.SUBSCRIPTION_MANAGER.getDefaultSubscriptionId());
    }

    public int getPhoneTypeForSlot(int slotId) {
        return getPhoneTypeForSubscription(Holder.SUBSCRIPTION_MANAGER.getSubscriptionIdForSlot(slotId));
    }

    public int getPhoneTypeForSubscription(int subId) {
        if (isVoiceCapable()) {
            return Holder.TELEPHONY_MANAGER.getCurrentPhoneType(normalizeSubscriptionId(subId));
        }
        return 0;
    }

    public String getNetworkOperator() {
        return getNetworkOperatorForSubscription(Holder.SUBSCRIPTION_MANAGER.getDefaultSubscriptionId());
    }

    public String getNetworkOperatorForSlot(int slotId) {
        return Holder.TELEPHONY_MANAGER.getNetworkOperatorForPhone(normalizeSlotId(slotId));
    }

    public String getNetworkOperatorForSubscription(int subId) {
        return Holder.TELEPHONY_MANAGER.getNetworkOperator(normalizeSubscriptionId(subId));
    }

    public String getNetworkOperatorName() {
        return getNetworkOperatorNameForSubscription(Holder.SUBSCRIPTION_MANAGER.getDefaultSubscriptionId());
    }

    public String getNetworkOperatorNameForSlot(int slotId) {
        return getNetworkOperatorNameForSubscription(Holder.SUBSCRIPTION_MANAGER.getSubscriptionIdForSlot(slotId));
    }

    public String getNetworkOperatorNameForSubscription(int subId) {
        return Holder.TELEPHONY_MANAGER.getNetworkOperatorName(normalizeSubscriptionId(subId));
    }

    public String getNetworkCountryIso() {
        return getNetworkCountryIsoForSubscription(Holder.SUBSCRIPTION_MANAGER.getDefaultSubscriptionId());
    }

    public String getNetworkCountryIsoForSlot(int slotId) {
        return Holder.TELEPHONY_MANAGER.getNetworkCountryIsoForPhone(normalizeSlotId(slotId));
    }

    public String getNetworkCountryIsoForSubscription(int subId) {
        return Holder.TELEPHONY_MANAGER.getNetworkCountryIso(normalizeSubscriptionId(subId));
    }

    public int getNetworkType() {
        return getNetworkTypeForSubscription(Holder.SUBSCRIPTION_MANAGER.getDefaultSubscriptionId());
    }

    public int getNetworkTypeForSlot(int slotId) {
        return getNetworkTypeForSubscription(Holder.SUBSCRIPTION_MANAGER.getSubscriptionIdForSlot(slotId));
    }

    public int getNetworkTypeForSubscription(int subId) {
        return Holder.TELEPHONY_MANAGER.getNetworkType(normalizeSubscriptionId(subId));
    }

    public boolean isNetworkRoaming() {
        return isNetworkRoamingForSubscription(Holder.SUBSCRIPTION_MANAGER.getDefaultSubscriptionId());
    }

    public boolean isNetworkRoamingForSlot(int slotId) {
        return isNetworkRoamingForSubscription(Holder.SUBSCRIPTION_MANAGER.getSubscriptionIdForSlot(slotId));
    }

    public boolean isNetworkRoamingForSubscription(int subId) {
        return Holder.TELEPHONY_MANAGER.isNetworkRoaming(normalizeSubscriptionId(subId));
    }

    public int getVoiceNetworkType() {
        return getVoiceNetworkTypeForSubscription(Holder.SUBSCRIPTION_MANAGER.getDefaultSubscriptionId());
    }

    public int getVoiceNetworkTypeForSlot(int slotId) {
        return getVoiceNetworkTypeForSubscription(Holder.SUBSCRIPTION_MANAGER.getSubscriptionIdForSlot(slotId));
    }

    public int getVoiceNetworkTypeForSubscription(int subId) {
        return Holder.TELEPHONY_MANAGER.getVoiceNetworkType(normalizeSubscriptionId(subId));
    }

    public int getNetworkClass(int networkType) {
        return TelephonyManager.getNetworkClass(networkType);
    }

    public String getNetworkTypeName(int networkType) {
        return TelephonyManager.getNetworkTypeName(networkType);
    }

    public boolean hasIccCard() {
        int max = getPhoneCount();
        for (int i = 0; i < max; i++) {
            if (hasIccCard(i)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasIccCard(int slotId) {
        return Holder.TELEPHONY_MANAGER.hasIccCard(normalizeSlotId(slotId));
    }

    public int getSimState() {
        return getSimStateForSlot(Holder.SUBSCRIPTION_MANAGER.getDefaultSlotId());
    }

    public int getSimStateForSlot(int slotId) {
        return Holder.TELEPHONY_MANAGER.getSimState(normalizeSlotId(slotId));
    }

    public int getSimStateForSubscription(int subId) {
        return getSimStateForSlot(Holder.SUBSCRIPTION_MANAGER.getSlotIdForSubscription(subId));
    }

    public String getSimOperator() {
        return getSimOperatorForSubscription(Holder.SUBSCRIPTION_MANAGER.getDefaultSubscriptionId());
    }

    public String getSimOperatorForSlot(int slotId) {
        return getSimOperatorForSubscription(Holder.SUBSCRIPTION_MANAGER.getSubscriptionIdForSlot(slotId));
    }

    public String getSimOperatorForSubscription(int subId) {
        return Holder.TELEPHONY_MANAGER.getSimOperator(normalizeSubscriptionId(subId));
    }

    public String getSimOperatorName() {
        return getSimOperatorNameForSubscription(Holder.SUBSCRIPTION_MANAGER.getDefaultSubscriptionId());
    }

    public String getSimOperatorNameForSlot(int slotId) {
        return Holder.TELEPHONY_MANAGER.getSimOperatorNameForPhone(normalizeSlotId(slotId));
    }

    public String getSimOperatorNameForSubscription(int subId) {
        return Holder.TELEPHONY_MANAGER.getSimOperatorName(normalizeSubscriptionId(subId));
    }

    public String getSimCountryIso() {
        return getSimCountryIsoForSubscription(Holder.SUBSCRIPTION_MANAGER.getDefaultSubscriptionId());
    }

    public String getSimCountryIsoForSlot(int slotId) {
        return getSimCountryIsoForSubscription(Holder.SUBSCRIPTION_MANAGER.getSubscriptionIdForSlot(slotId));
    }

    public String getSimCountryIsoForSubscription(int subId) {
        return Holder.TELEPHONY_MANAGER.getSimCountryIso(normalizeSubscriptionId(subId));
    }

    public String getSimSerialNumber() {
        return getSimSerialNumberForSubscription(Holder.SUBSCRIPTION_MANAGER.getDefaultSubscriptionId());
    }

    public String getSimSerialNumberForSlot(int slotId) {
        return getSimSerialNumberForSubscription(Holder.SUBSCRIPTION_MANAGER.getSubscriptionIdForSlot(slotId));
    }

    public String getSimSerialNumberForSubscription(int subId) {
        return Holder.TELEPHONY_MANAGER.getSimSerialNumber(normalizeSubscriptionId(subId));
    }

    public String getSubscriberId() {
        return getSubscriberIdForSubscription(Holder.SUBSCRIPTION_MANAGER.getDefaultSubscriptionId());
    }

    public String getSubscriberIdForSlot(int slotId) {
        return getSubscriberIdForSubscription(Holder.SUBSCRIPTION_MANAGER.getSubscriptionIdForSlot(slotId));
    }

    public String getSubscriberIdForSubscription(int subId) {
        return Holder.TELEPHONY_MANAGER.getSubscriberId(normalizeSubscriptionId(subId));
    }

    public String getLine1Number() {
        return getLine1NumberForSubscription(Holder.SUBSCRIPTION_MANAGER.getDefaultSubscriptionId());
    }

    public String getLine1NumberForSlot(int slotId) {
        return getLine1NumberForSubscription(Holder.SUBSCRIPTION_MANAGER.getSubscriptionIdForSlot(slotId));
    }

    public String getLine1NumberForSubscription(int subId) {
        return Holder.TELEPHONY_MANAGER.getLine1Number(normalizeSubscriptionId(subId));
    }

    public String getVoiceMailNumber() {
        return getVoiceMailNumberForSubscription(Holder.SUBSCRIPTION_MANAGER.getDefaultSubscriptionId());
    }

    public String getVoiceMailNumberForSlot(int slotId) {
        return getVoiceMailNumberForSubscription(Holder.SUBSCRIPTION_MANAGER.getSubscriptionIdForSlot(slotId));
    }

    public String getVoiceMailNumberForSubscription(int subId) {
        return Holder.TELEPHONY_MANAGER.getVoiceMailNumber(normalizeSubscriptionId(subId));
    }

    public String getVoiceMailAlphaTag() {
        return getVoiceMailAlphaTagForSubscription(Holder.SUBSCRIPTION_MANAGER.getDefaultSubscriptionId());
    }

    public String getVoiceMailAlphaTagForSlot(int slotId) {
        return getVoiceMailAlphaTagForSubscription(Holder.SUBSCRIPTION_MANAGER.getSubscriptionIdForSlot(slotId));
    }

    public String getVoiceMailAlphaTagForSubscription(int subId) {
        return Holder.TELEPHONY_MANAGER.getVoiceMailAlphaTag(normalizeSubscriptionId(subId));
    }

    public int getCallState() {
        return Holder.TELEPHONY_MANAGER.getCallState();
    }

    public int getCallStateForSlot(int slotId) {
        return getCallStateForSubscription(Holder.SUBSCRIPTION_MANAGER.getSubscriptionIdForSlot(slotId));
    }

    public int getCallStateForSubscription(int subId) {
        return Holder.TELEPHONY_MANAGER.getCallState(normalizeSubscriptionId(subId));
    }

    public int getDataActivity() {
        return Holder.TELEPHONY_MANAGER.getDataActivity();
    }

    public int getDataActivityForSlot(int slotId) {
        if (slotId == SubscriptionManager.DEFAULT_SLOT_ID || slotId == Holder.SUBSCRIPTION_MANAGER.getDefaultDataSlotId()) {
            return Holder.TELEPHONY_MANAGER.getDataActivity();
        }
        return 0;
    }

    public int getDataActivityForSubscription(int subId) {
        if (subId == SubscriptionManager.DEFAULT_SUBSCRIPTION_ID || subId == Holder.SUBSCRIPTION_MANAGER.getDefaultDataSubscriptionId()) {
            return Holder.TELEPHONY_MANAGER.getDataActivity();
        }
        return 0;
    }

    public int getDataState() {
        return Holder.TELEPHONY_MANAGER.getDataState();
    }

    public int getDataStateForSlot(int slotId) {
        if (slotId == SubscriptionManager.DEFAULT_SLOT_ID || slotId == Holder.SUBSCRIPTION_MANAGER.getDefaultDataSlotId()) {
            return Holder.TELEPHONY_MANAGER.getDataState();
        }
        return 0;
    }

    public int getDataStateForSubscription(int subId) {
        if (subId == SubscriptionManager.DEFAULT_SUBSCRIPTION_ID || subId == Holder.SUBSCRIPTION_MANAGER.getDefaultDataSubscriptionId()) {
            return Holder.TELEPHONY_MANAGER.getDataState();
        }
        return 0;
    }

    public String getMsisdn() {
        return getMsisdnForSubscription(Holder.SUBSCRIPTION_MANAGER.getDefaultSubscriptionId());
    }

    public String getMsisdnForSlot(int slotId) {
        return getMsisdnForSubscription(Holder.SUBSCRIPTION_MANAGER.getSubscriptionIdForSlot(slotId));
    }

    public String getMsisdnForSubscription(int subId) {
        return Holder.TELEPHONY_MANAGER.getMsisdn(normalizeSubscriptionId(subId));
    }

    public void listen(PhoneStateListener listener, int events) {
        Holder.TELEPHONY_MANAGER.listen(listener, events);
    }

    public void listenForSlot(int slotId, PhoneStateListener listener, int events) {
        listenForSubscription(Holder.SUBSCRIPTION_MANAGER.getSubscriptionIdForSlot(slotId), listener, events);
    }

    public void listenForSubscription(int subId, PhoneStateListener listener, int events) {
        Integer old = listener.updateSubscription(Integer.valueOf(subId));
        Holder.TELEPHONY_MANAGER.listen(listener, events);
        listener.updateSubscription(old);
    }

    public boolean isVoiceCapable() {
        return Holder.TELEPHONY_MANAGER.isVoiceCapable();
    }

    public boolean isSmsCapable() {
        return Holder.TELEPHONY_MANAGER.isSmsCapable();
    }

    public String getTelephonySetting(int slotId, ContentResolver cr, String name) {
        int slot = normalizeSlotId(slotId);
        String v = Global.getString(cr, name);
        if (v != null) {
            String[] valArray = v.split(",");
            if (slot >= 0 && slot < valArray.length && valArray[slot] != null) {
                return valArray[slot];
            }
        }
        return "";
    }

    public boolean putTelephonySetting(int slotId, ContentResolver cr, String name, String value) {
        int slot = normalizeSlotId(slotId);
        StringBuilder data = new StringBuilder(128);
        String[] valArray = null;
        String v = Global.getString(cr, name);
        if (v != null) {
            valArray = v.split(",");
        }
        int i = 0;
        while (i < slot) {
            String str = "";
            if (valArray != null && i < valArray.length) {
                str = valArray[i];
            }
            data.append(str);
            data.append(',');
            i++;
        }
        data.append(value == null ? "" : value);
        if (valArray != null) {
            for (i = slot + 1; i < valArray.length; i++) {
                data.append(',');
                data.append(valArray[i]);
            }
        }
        return Global.putString(cr, name, data.toString());
    }

    public void setTelephonyProperty(int slotId, String property, String value) {
        TelephonyManager.setTelephonyProperty(normalizeSlotId(slotId), property, value);
    }

    public String getTelephonyProperty(int slotId, String property, String defaultVal) {
        return TelephonyManager.getTelephonyProperty(normalizeSlotId(slotId), property, defaultVal);
    }

    public int[] supplyPinReportResult(String pin) {
        return supplyPinReportResultForSubscription(Holder.SUBSCRIPTION_MANAGER.getDefaultSubscriptionId(), pin);
    }

    public int[] supplyPinReportResultForSlot(int slotId, String pin) {
        return supplyPinReportResultForSubscription(Holder.SUBSCRIPTION_MANAGER.getSubscriptionIdForSlot(slotId), pin);
    }

    public int[] supplyPinReportResultForSubscription(int subId, String pin) {
        try {
            return getITelephony().supplyPinReportResultForSubscriber(normalizeSubscriptionId(subId), pin);
        } catch (RemoteException e) {
            Rlog.e(TAG, "Error calling ITelephony#supplyPinReportResultForSubscriber", e);
            return new int[0];
        }
    }

    public int[] supplyPukReportResult(String puk, String pin) {
        return supplyPukReportResultForSubscription(Holder.SUBSCRIPTION_MANAGER.getDefaultSubscriptionId(), puk, pin);
    }

    public int[] supplyPukReportResultForSlot(int slotId, String puk, String pin) {
        return supplyPukReportResultForSubscription(Holder.SUBSCRIPTION_MANAGER.getSubscriptionIdForSlot(slotId), puk, pin);
    }

    public int[] supplyPukReportResultForSubscription(int subId, String puk, String pin) {
        try {
            return getITelephony().supplyPukReportResultForSubscriber(normalizeSubscriptionId(subId), puk, pin);
        } catch (RemoteException e) {
            Rlog.e(TAG, "Error calling ITelephony#supplyPukReportResultForSubscriber", e);
            return new int[0];
        }
    }

    public void cancelMissedCallsNotification() {
        try {
            getTelecomService().cancelMissedCallsNotification(Holder.CONTEXT.getOpPackageName());
        } catch (RemoteException e) {
            Rlog.e(TAG, "Error call ITelecomService#cancelMissedCallsNotification", e);
        }
    }

    public void silenceRinger() {
        try {
            getTelecomService().silenceRinger(Holder.CONTEXT.getOpPackageName());
        } catch (RemoteException e) {
            Rlog.e(TAG, "Error call ITelecomService#silenceRinger", e);
        }
    }

    public boolean endCall() {
        try {
            return getTelecomService().endCall(Holder.CONTEXT.getOpPackageName());
        } catch (RemoteException e) {
            Rlog.e(TAG, "Error calling ITelecomService#endCall", e);
            return false;
        }
    }

    public void answerRingingCall() {
        try {
            getTelecomService().acceptRingingCall(Holder.CONTEXT.getOpPackageName());
        } catch (RemoteException e) {
            Rlog.e(TAG, "Error calling ITelecomService#acceptRingingCall", e);
        }
    }

    public boolean isRadioOn() {
        int n = getPhoneCount();
        for (int i = 0; i < n; i++) {
            if (isRadioOnForSlot(i)) {
                return true;
            }
        }
        return false;
    }

    public boolean isRadioOnForSlot(int slotId) {
        return isRadioOnForSubscription(Holder.SUBSCRIPTION_MANAGER.getSubscriptionIdForSlot(slotId));
    }

    public boolean isRadioOnForSubscription(int subId) {
        try {
            return getITelephony().isRadioOnForSubscriber(normalizeSubscriptionId(subId), Holder.CONTEXT.getOpPackageName());
        } catch (RemoteException e) {
            Rlog.e(TAG, "Error calling ITelephony#supplyPukReportResultForSubscriber", e);
            return false;
        }
    }

    public boolean showCallScreen() {
        return showCallScreenWithDialpad(false);
    }

    public boolean showCallScreenWithDialpad(boolean showDialpad) {
        try {
            getTelecomService().showInCallScreen(showDialpad, Holder.CONTEXT.getOpPackageName());
            return true;
        } catch (RemoteException e) {
            Rlog.e(TAG, "Error calling ITelecomService#showInCallScreen", e);
            return false;
        }
    }

    public boolean enableDataConnectivity() {
        return Holder.TELEPHONY_MANAGER.enableDataConnectivity();
    }

    public boolean enableDataConnectivityForSlot(int slotId) {
        if (slotId == SubscriptionManager.DEFAULT_SLOT_ID || slotId == Holder.SUBSCRIPTION_MANAGER.getDefaultDataSlotId()) {
            return Holder.TELEPHONY_MANAGER.enableDataConnectivity();
        }
        return false;
    }

    public boolean enableDataConnectivityForSubscription(int subId) {
        if (subId == SubscriptionManager.DEFAULT_SUBSCRIPTION_ID || subId == Holder.SUBSCRIPTION_MANAGER.getDefaultDataSubscriptionId()) {
            return Holder.TELEPHONY_MANAGER.enableDataConnectivity();
        }
        return false;
    }

    public boolean isSameOperator(String numeric, String anotherNumeric) {
        boolean z = false;
        try {
            IMiuiTelephony telephony = getMiuiTelephony();
            if (telephony != null) {
                z = telephony.isSameOperator(numeric, anotherNumeric);
            }
            return z;
        } catch (RemoteException e) {
            Rlog.e(TAG, "isSameOperator error", e);
            return false;
        }
    }

    public String getSpn(String numeric, int slotId, String spn, boolean longName) {
        String result = null;
        try {
            IMiuiTelephony telephony = getMiuiTelephony();
            result = telephony == null ? null : telephony.getSpn(numeric, slotId, spn, longName);
        } catch (RemoteException e) {
            Rlog.e(TAG, "getSpn error", e);
        }
        if (result != null) {
            return result;
        }
        String simOperatorNameForSlot = ((spn == null || spn.length() == 0) && SubscriptionManager.isValidSlotId(slotId)) ? getSimOperatorNameForSlot(slotId) : spn;
        return simOperatorNameForSlot;
    }

    public String onOperatorNumericOrNameSet(int slotId, String property, String value) {
        try {
            IMiuiTelephony telephony = getMiuiTelephony();
            return telephony == null ? value : telephony.onOperatorNumericOrNameSet(slotId, property, value);
        } catch (RemoteException e) {
            Rlog.e(TAG, "onOperatorNumericOrNameSet error", e);
            return value;
        }
    }

    public IMiuiTelephony getMiuiTelephony() {
        try {
            if (sRegistry == null) {
                synchronized (TelephonyManagerEx.class) {
                    if (sRegistry == null) {
                        sRegistry = ITelephonyRegistry.Stub.asInterface(ServiceManager.getService("telephony.registry"));
                    }
                }
            }
            if (sRegistry != null) {
                return sRegistry.getMiuiTelephony();
            }
        } catch (RemoteException e) {
            Rlog.e(TAG, "getMiuiTelephony error", e);
        }
        return null;
    }

    public boolean isVolteEnabledByUser() {
        boolean z = false;
        try {
            IMiuiTelephony telephony = getMiuiTelephony();
            if (telephony != null) {
                z = telephony.isVolteEnabledByUser();
            }
            return z;
        } catch (Exception e) {
            Rlog.e(TAG, "isVolteEnabledByUser exception", e);
            return false;
        }
    }

    public boolean isVolteEnabledByUser(int slotId) {
        boolean z = false;
        try {
            IMiuiTelephony telephony = getMiuiTelephony();
            if (telephony != null) {
                z = telephony.isVolteEnabledByUserForSlot(slotId);
            }
            return z;
        } catch (Exception e) {
            Rlog.e(TAG, "isVolteEnabledByUser exception", e);
            return false;
        }
    }

    public boolean isVtEnabledByPlatform() {
        boolean z = false;
        try {
            IMiuiTelephony telephony = getMiuiTelephony();
            if (telephony != null) {
                z = telephony.isVtEnabledByPlatform();
            }
            return z;
        } catch (Exception e) {
            Rlog.e(TAG, "isVtEnabledByPlatform exception", e);
            return false;
        }
    }

    public boolean isVtEnabledByPlatform(int slotId) {
        boolean z = false;
        try {
            IMiuiTelephony telephony = getMiuiTelephony();
            if (telephony != null) {
                z = telephony.isVtEnabledByPlatformForSlot(slotId);
            }
            return z;
        } catch (Exception e) {
            Rlog.e(TAG, "isVtEnabledByPlatform exception", e);
            return false;
        }
    }

    public boolean isVolteEnabledByPlatform() {
        boolean z = false;
        try {
            IMiuiTelephony telephony = getMiuiTelephony();
            if (telephony != null) {
                z = telephony.isVolteEnabledByPlatform();
            }
            return z;
        } catch (Exception e) {
            Rlog.e(TAG, "isVolteEnabledByPlatform exception", e);
            return false;
        }
    }

    public boolean isVolteEnabledByPlatform(int slotId) {
        boolean z = false;
        try {
            IMiuiTelephony telephony = getMiuiTelephony();
            if (telephony != null) {
                z = telephony.isVolteEnabledByPlatformForSlot(slotId);
            }
            return z;
        } catch (Exception e) {
            Rlog.e(TAG, "isVolteEnabledByPlatform exception", e);
            return false;
        }
    }

    public boolean isImsRegistered(int phoneId) {
        boolean z = false;
        try {
            IMiuiTelephony telephony = getMiuiTelephony();
            if (telephony != null) {
                z = telephony.isImsRegistered(phoneId);
            }
            return z;
        } catch (Exception e) {
            Rlog.e(TAG, "isImsRegistered exception", e);
            return false;
        }
    }

    public boolean isVideoTelephonyAvailable(int phoneId) {
        boolean z = false;
        try {
            IMiuiTelephony telephony = getMiuiTelephony();
            if (telephony != null) {
                z = telephony.isVideoTelephonyAvailable(phoneId);
            }
            return z;
        } catch (Exception e) {
            Rlog.e(TAG, "isVideoTelephonyAvailable exception", e);
            return false;
        }
    }

    public boolean isWifiCallingAvailable(int phoneId) {
        boolean z = false;
        try {
            IMiuiTelephony telephony = getMiuiTelephony();
            if (telephony != null) {
                z = telephony.isWifiCallingAvailable(phoneId);
            }
            return z;
        } catch (Exception e) {
            Rlog.e(TAG, "isWifiCallingAvailable exception", e);
            return false;
        }
    }

    public boolean isDualVolteSupported() {
        return Holder.IS_DUAL_VOLTE_SUPPORTED && !isCustSingleSimDevice();
    }

    public boolean isCmccCooperationDevice() {
        return Holder.IS_CMCC_COOPERATION_DEVICE;
    }

    public static boolean isLocalEmergencyNumber(Context context, String number) {
        return isLocalEmergencyNumberInternal(context, number, true);
    }

    public static boolean isPotentialLocalEmergencyNumber(Context context, String number) {
        return isLocalEmergencyNumberInternal(context, number, false);
    }

    private static String getCountryIso(Context context) {
        String countryIso = null;
        CountryDetector detector = (CountryDetector) context.getSystemService(Context.COUNTRY_DETECTOR);
        if (detector != null) {
            Country country = detector.detectCountry();
            if (country != null) {
                countryIso = country.getCountryIso();
            }
        }
        if (countryIso != null) {
            return countryIso;
        }
        countryIso = context.getResources().getConfiguration().locale.getCountry();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("No CountryDetector; falling back to countryIso based on locale: ");
        stringBuilder.append(countryIso);
        Rlog.w(TAG, stringBuilder.toString());
        return countryIso;
    }

    private static boolean isLocalEmergencyNumberInternal(Context context, String number, boolean useExactMatch) {
        int i;
        String defaultCountryIso = getCountryIso(context);
        boolean equalsIgnoreCase = "IN".equalsIgnoreCase(defaultCountryIso);
        String str = TAG;
        if (equalsIgnoreCase) {
            for (String INemergencyNum : new String[]{"100", "101", "102", "108"}) {
                if (INemergencyNum.equals(number)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("isLocalEmergencyNumberInternal :number:");
                    stringBuilder.append(number);
                    stringBuilder.append(" is not a real IN emergency number,return false");
                    Rlog.d(str, stringBuilder.toString());
                    return false;
                }
            }
        }
        int phoneCount = Holder.INSTANCE.getPhoneCount();
        if (phoneCount < 2) {
            boolean isLocalEmergencyNumber;
            if (useExactMatch) {
                isLocalEmergencyNumber = PhoneNumberUtils.isLocalEmergencyNumber(context, number);
            } else {
                isLocalEmergencyNumber = PhoneNumberUtils.isPotentialLocalEmergencyNumber(context, number);
            }
            return isLocalEmergencyNumber;
        }
        int subId;
        if ("IT".equalsIgnoreCase(defaultCountryIso) || "MM".equalsIgnoreCase(defaultCountryIso)) {
            i = 0;
            int validSlot = -1;
            for (int i2 = 0; i2 < phoneCount; i2++) {
                if (Holder.TELEPHONY_MANAGER.getSimState(i2) == 5) {
                    i++;
                    validSlot = i2;
                }
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("isLocalEmergencyNumberInternal : in Italy or Myanmar,insert ");
            stringBuilder2.append(i);
            stringBuilder2.append(" sim card, validSlot is");
            stringBuilder2.append(validSlot);
            Rlog.d(str, stringBuilder2.toString());
            if (i == 1) {
                subId = Holder.SUBSCRIPTION_MANAGER.getSubscriptionIdForSlot(validSlot);
                if ((!useExactMatch || !PhoneNumberUtils.isLocalEmergencyNumber(context, subId, number)) && (useExactMatch || !PhoneNumberUtils.isPotentialLocalEmergencyNumber(context, subId, number))) {
                    return false;
                }
                return true;
            }
        }
        for (subId = 0; subId < phoneCount; subId++) {
            i = Holder.SUBSCRIPTION_MANAGER.getSubscriptionIdForSlot(subId);
            if ((useExactMatch && PhoneNumberUtils.isLocalEmergencyNumber(context, i, number)) || (!useExactMatch && PhoneNumberUtils.isPotentialLocalEmergencyNumber(context, i, number))) {
                return true;
            }
        }
        return false;
    }

    public void setCallForwardingOption(int phoneId, int action, int reason, String number, ResultReceiver callback) {
        try {
            IMiuiTelephony telephony = getMiuiTelephony();
            if (telephony != null) {
                telephony.setCallForwardingOption(phoneId, action, reason, number, callback);
            }
        } catch (Exception e) {
            Rlog.e(TAG, "setCallForwardingOption exception", e);
            if (callback != null) {
                callback.send(-1, null);
            }
        }
    }

    public void setIccCardActivate(int slotId, boolean isActivate) {
        try {
            IMiuiTelephony telephony = getMiuiTelephony();
            if (telephony != null) {
                telephony.setIccCardActivate(slotId, isActivate);
            }
        } catch (RemoteException e) {
            Rlog.e(TAG, "setIccCardActivate error", e);
        }
    }

    public boolean isGwsdSupport() {
        boolean z = false;
        try {
            IMiuiTelephony telephony = getMiuiTelephony();
            if (telephony != null) {
                z = telephony.isGwsdSupport();
            }
            return z;
        } catch (Exception e) {
            Rlog.e(TAG, "isGwsdSupport exception", e);
            return false;
        }
    }

    public int getCtVolteSupportedMode() {
        return Holder.CT_VOLTE_SUPPORTED_MODE;
    }

    public int getMiuiLevel(SignalStrength ss) {
        return ss.getMiuiLevel();
    }
}
