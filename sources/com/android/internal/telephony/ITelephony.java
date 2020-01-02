package com.android.internal.telephony;

import android.annotation.UnsupportedAppUsage;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.NetworkStats;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Messenger;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.WorkSource;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telephony.CarrierRestrictionRules;
import android.telephony.CellInfo;
import android.telephony.ClientRequestStats;
import android.telephony.ICellInfoCallback;
import android.telephony.IccOpenLogicalChannelResponse;
import android.telephony.NeighboringCellInfo;
import android.telephony.NetworkScanRequest;
import android.telephony.PhoneNumberRange;
import android.telephony.RadioAccessFamily;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyHistogram;
import android.telephony.UiccCardInfo;
import android.telephony.UiccSlotInfo;
import android.telephony.VisualVoicemailSmsFilterSettings;
import android.telephony.emergency.EmergencyNumber;
import android.telephony.ims.aidl.IImsCapabilityCallback;
import android.telephony.ims.aidl.IImsConfig;
import android.telephony.ims.aidl.IImsConfigCallback;
import android.telephony.ims.aidl.IImsMmTelFeature;
import android.telephony.ims.aidl.IImsRcsFeature;
import android.telephony.ims.aidl.IImsRegistration;
import android.telephony.ims.aidl.IImsRegistrationCallback;
import com.android.ims.internal.IImsServiceFeatureCallback;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ITelephony extends IInterface {

    public static class Default implements ITelephony {
        public void dial(String number) throws RemoteException {
        }

        public void call(String callingPackage, String number) throws RemoteException {
        }

        public boolean isRadioOn(String callingPackage) throws RemoteException {
            return false;
        }

        public boolean isRadioOnForSubscriber(int subId, String callingPackage) throws RemoteException {
            return false;
        }

        public boolean supplyPin(String pin) throws RemoteException {
            return false;
        }

        public boolean supplyPinForSubscriber(int subId, String pin) throws RemoteException {
            return false;
        }

        public boolean supplyPuk(String puk, String pin) throws RemoteException {
            return false;
        }

        public boolean supplyPukForSubscriber(int subId, String puk, String pin) throws RemoteException {
            return false;
        }

        public int[] supplyPinReportResult(String pin) throws RemoteException {
            return null;
        }

        public int[] supplyPinReportResultForSubscriber(int subId, String pin) throws RemoteException {
            return null;
        }

        public int[] supplyPukReportResult(String puk, String pin) throws RemoteException {
            return null;
        }

        public int[] supplyPukReportResultForSubscriber(int subId, String puk, String pin) throws RemoteException {
            return null;
        }

        public boolean handlePinMmi(String dialString) throws RemoteException {
            return false;
        }

        public void handleUssdRequest(int subId, String ussdRequest, ResultReceiver wrappedCallback) throws RemoteException {
        }

        public boolean handlePinMmiForSubscriber(int subId, String dialString) throws RemoteException {
            return false;
        }

        public void toggleRadioOnOff() throws RemoteException {
        }

        public void toggleRadioOnOffForSubscriber(int subId) throws RemoteException {
        }

        public boolean setRadio(boolean turnOn) throws RemoteException {
            return false;
        }

        public boolean setRadioForSubscriber(int subId, boolean turnOn) throws RemoteException {
            return false;
        }

        public boolean setRadioPower(boolean turnOn) throws RemoteException {
            return false;
        }

        public void updateServiceLocation() throws RemoteException {
        }

        public void updateServiceLocationForSubscriber(int subId) throws RemoteException {
        }

        public void enableLocationUpdates() throws RemoteException {
        }

        public void enableLocationUpdatesForSubscriber(int subId) throws RemoteException {
        }

        public void disableLocationUpdates() throws RemoteException {
        }

        public void disableLocationUpdatesForSubscriber(int subId) throws RemoteException {
        }

        public boolean enableDataConnectivity() throws RemoteException {
            return false;
        }

        public boolean disableDataConnectivity() throws RemoteException {
            return false;
        }

        public boolean isDataConnectivityPossible(int subId) throws RemoteException {
            return false;
        }

        public Bundle getCellLocation(String callingPkg) throws RemoteException {
            return null;
        }

        public String getNetworkCountryIsoForPhone(int phoneId) throws RemoteException {
            return null;
        }

        public List<NeighboringCellInfo> getNeighboringCellInfo(String callingPkg) throws RemoteException {
            return null;
        }

        public int getCallState() throws RemoteException {
            return 0;
        }

        public int getCallStateForSlot(int slotIndex) throws RemoteException {
            return 0;
        }

        public int getDataActivity() throws RemoteException {
            return 0;
        }

        public int getDataState() throws RemoteException {
            return 0;
        }

        public int getActivePhoneType() throws RemoteException {
            return 0;
        }

        public int getActivePhoneTypeForSlot(int slotIndex) throws RemoteException {
            return 0;
        }

        public int getCdmaEriIconIndex(String callingPackage) throws RemoteException {
            return 0;
        }

        public int getCdmaEriIconIndexForSubscriber(int subId, String callingPackage) throws RemoteException {
            return 0;
        }

        public int getCdmaEriIconMode(String callingPackage) throws RemoteException {
            return 0;
        }

        public int getCdmaEriIconModeForSubscriber(int subId, String callingPackage) throws RemoteException {
            return 0;
        }

        public String getCdmaEriText(String callingPackage) throws RemoteException {
            return null;
        }

        public String getCdmaEriTextForSubscriber(int subId, String callingPackage) throws RemoteException {
            return null;
        }

        public boolean needsOtaServiceProvisioning() throws RemoteException {
            return false;
        }

        public boolean setVoiceMailNumber(int subId, String alphaTag, String number) throws RemoteException {
            return false;
        }

        public void setVoiceActivationState(int subId, int activationState) throws RemoteException {
        }

        public void setDataActivationState(int subId, int activationState) throws RemoteException {
        }

        public int getVoiceActivationState(int subId, String callingPackage) throws RemoteException {
            return 0;
        }

        public int getDataActivationState(int subId, String callingPackage) throws RemoteException {
            return 0;
        }

        public int getVoiceMessageCountForSubscriber(int subId, String callingPackage) throws RemoteException {
            return 0;
        }

        public boolean isConcurrentVoiceAndDataAllowed(int subId) throws RemoteException {
            return false;
        }

        public Bundle getVisualVoicemailSettings(String callingPackage, int subId) throws RemoteException {
            return null;
        }

        public String getVisualVoicemailPackageName(String callingPackage, int subId) throws RemoteException {
            return null;
        }

        public void enableVisualVoicemailSmsFilter(String callingPackage, int subId, VisualVoicemailSmsFilterSettings settings) throws RemoteException {
        }

        public void disableVisualVoicemailSmsFilter(String callingPackage, int subId) throws RemoteException {
        }

        public VisualVoicemailSmsFilterSettings getVisualVoicemailSmsFilterSettings(String callingPackage, int subId) throws RemoteException {
            return null;
        }

        public VisualVoicemailSmsFilterSettings getActiveVisualVoicemailSmsFilterSettings(int subId) throws RemoteException {
            return null;
        }

        public void sendVisualVoicemailSmsForSubscriber(String callingPackage, int subId, String number, int port, String text, PendingIntent sentIntent) throws RemoteException {
        }

        public void sendDialerSpecialCode(String callingPackageName, String inputCode) throws RemoteException {
        }

        public int getNetworkTypeForSubscriber(int subId, String callingPackage) throws RemoteException {
            return 0;
        }

        public int getDataNetworkType(String callingPackage) throws RemoteException {
            return 0;
        }

        public int getDataNetworkTypeForSubscriber(int subId, String callingPackage) throws RemoteException {
            return 0;
        }

        public int getVoiceNetworkTypeForSubscriber(int subId, String callingPackage) throws RemoteException {
            return 0;
        }

        public boolean hasIccCard() throws RemoteException {
            return false;
        }

        public boolean hasIccCardUsingSlotIndex(int slotIndex) throws RemoteException {
            return false;
        }

        public int getLteOnCdmaMode(String callingPackage) throws RemoteException {
            return 0;
        }

        public int getLteOnCdmaModeForSubscriber(int subId, String callingPackage) throws RemoteException {
            return 0;
        }

        public List<CellInfo> getAllCellInfo(String callingPkg) throws RemoteException {
            return null;
        }

        public void requestCellInfoUpdate(int subId, ICellInfoCallback cb, String callingPkg) throws RemoteException {
        }

        public void requestCellInfoUpdateWithWorkSource(int subId, ICellInfoCallback cb, String callingPkg, WorkSource ws) throws RemoteException {
        }

        public void setCellInfoListRate(int rateInMillis) throws RemoteException {
        }

        public IccOpenLogicalChannelResponse iccOpenLogicalChannelBySlot(int slotIndex, String callingPackage, String AID, int p2) throws RemoteException {
            return null;
        }

        public IccOpenLogicalChannelResponse iccOpenLogicalChannel(int subId, String callingPackage, String AID, int p2) throws RemoteException {
            return null;
        }

        public boolean iccCloseLogicalChannelBySlot(int slotIndex, int channel) throws RemoteException {
            return false;
        }

        public boolean iccCloseLogicalChannel(int subId, int channel) throws RemoteException {
            return false;
        }

        public String iccTransmitApduLogicalChannelBySlot(int slotIndex, int channel, int cla, int instruction, int p1, int p2, int p3, String data) throws RemoteException {
            return null;
        }

        public String iccTransmitApduLogicalChannel(int subId, int channel, int cla, int instruction, int p1, int p2, int p3, String data) throws RemoteException {
            return null;
        }

        public String iccTransmitApduBasicChannelBySlot(int slotIndex, String callingPackage, int cla, int instruction, int p1, int p2, int p3, String data) throws RemoteException {
            return null;
        }

        public String iccTransmitApduBasicChannel(int subId, String callingPackage, int cla, int instruction, int p1, int p2, int p3, String data) throws RemoteException {
            return null;
        }

        public byte[] iccExchangeSimIO(int subId, int fileID, int command, int p1, int p2, int p3, String filePath) throws RemoteException {
            return null;
        }

        public String sendEnvelopeWithStatus(int subId, String content) throws RemoteException {
            return null;
        }

        public String nvReadItem(int itemID) throws RemoteException {
            return null;
        }

        public boolean nvWriteItem(int itemID, String itemValue) throws RemoteException {
            return false;
        }

        public boolean nvWriteCdmaPrl(byte[] preferredRoamingList) throws RemoteException {
            return false;
        }

        public boolean resetModemConfig(int slotIndex) throws RemoteException {
            return false;
        }

        public boolean rebootModem(int slotIndex) throws RemoteException {
            return false;
        }

        public int getCalculatedPreferredNetworkType(String callingPackage) throws RemoteException {
            return 0;
        }

        public int getPreferredNetworkType(int subId) throws RemoteException {
            return 0;
        }

        public boolean getTetherApnRequiredForSubscriber(int subId) throws RemoteException {
            return false;
        }

        public void enableIms(int slotId) throws RemoteException {
        }

        public void disableIms(int slotId) throws RemoteException {
        }

        public IImsMmTelFeature getMmTelFeatureAndListen(int slotId, IImsServiceFeatureCallback callback) throws RemoteException {
            return null;
        }

        public IImsRcsFeature getRcsFeatureAndListen(int slotId, IImsServiceFeatureCallback callback) throws RemoteException {
            return null;
        }

        public IImsRegistration getImsRegistration(int slotId, int feature) throws RemoteException {
            return null;
        }

        public IImsConfig getImsConfig(int slotId, int feature) throws RemoteException {
            return null;
        }

        public boolean setImsService(int slotId, boolean isCarrierImsService, String packageName) throws RemoteException {
            return false;
        }

        public String getImsService(int slotId, boolean isCarrierImsService) throws RemoteException {
            return null;
        }

        public void setNetworkSelectionModeAutomatic(int subId) throws RemoteException {
        }

        public CellNetworkScanResult getCellNetworkScanResults(int subId, String callingPackage) throws RemoteException {
            return null;
        }

        public int requestNetworkScan(int subId, NetworkScanRequest request, Messenger messenger, IBinder binder, String callingPackage) throws RemoteException {
            return 0;
        }

        public void stopNetworkScan(int subId, int scanId) throws RemoteException {
        }

        public boolean setNetworkSelectionModeManual(int subId, OperatorInfo operatorInfo, boolean persisSelection) throws RemoteException {
            return false;
        }

        public boolean setPreferredNetworkType(int subId, int networkType) throws RemoteException {
            return false;
        }

        public void setUserDataEnabled(int subId, boolean enable) throws RemoteException {
        }

        public boolean getDataEnabled(int subId) throws RemoteException {
            return false;
        }

        public boolean isUserDataEnabled(int subId) throws RemoteException {
            return false;
        }

        public boolean isDataEnabled(int subId) throws RemoteException {
            return false;
        }

        public boolean isManualNetworkSelectionAllowed(int subId) throws RemoteException {
            return false;
        }

        public String[] getPcscfAddress(String apnType, String callingPackage) throws RemoteException {
            return null;
        }

        public void setImsRegistrationState(boolean registered) throws RemoteException {
        }

        public String getCdmaMdn(int subId) throws RemoteException {
            return null;
        }

        public String getCdmaMin(int subId) throws RemoteException {
            return null;
        }

        public void requestNumberVerification(PhoneNumberRange range, long timeoutMillis, INumberVerificationCallback callback, String callingPackage) throws RemoteException {
        }

        public int getCarrierPrivilegeStatus(int subId) throws RemoteException {
            return 0;
        }

        public int getCarrierPrivilegeStatusForUid(int subId, int uid) throws RemoteException {
            return 0;
        }

        public int checkCarrierPrivilegesForPackage(int subId, String pkgName) throws RemoteException {
            return 0;
        }

        public int checkCarrierPrivilegesForPackageAnyPhone(String pkgName) throws RemoteException {
            return 0;
        }

        public List<String> getCarrierPackageNamesForIntentAndPhone(Intent intent, int phoneId) throws RemoteException {
            return null;
        }

        public boolean setLine1NumberForDisplayForSubscriber(int subId, String alphaTag, String number) throws RemoteException {
            return false;
        }

        public String getLine1NumberForDisplay(int subId, String callingPackage) throws RemoteException {
            return null;
        }

        public String getLine1AlphaTagForDisplay(int subId, String callingPackage) throws RemoteException {
            return null;
        }

        public String[] getMergedSubscriberIds(int subId, String callingPackage) throws RemoteException {
            return null;
        }

        public boolean setOperatorBrandOverride(int subId, String brand) throws RemoteException {
            return false;
        }

        public boolean setRoamingOverride(int subId, List<String> list, List<String> list2, List<String> list3, List<String> list4) throws RemoteException {
            return false;
        }

        public int invokeOemRilRequestRaw(byte[] oemReq, byte[] oemResp) throws RemoteException {
            return 0;
        }

        public boolean needMobileRadioShutdown() throws RemoteException {
            return false;
        }

        public void shutdownMobileRadios() throws RemoteException {
        }

        public void setRadioCapability(RadioAccessFamily[] rafs) throws RemoteException {
        }

        public int getRadioAccessFamily(int phoneId, String callingPackage) throws RemoteException {
            return 0;
        }

        public void enableVideoCalling(boolean enable) throws RemoteException {
        }

        public boolean isVideoCallingEnabled(String callingPackage) throws RemoteException {
            return false;
        }

        public boolean canChangeDtmfToneLength(int subId, String callingPackage) throws RemoteException {
            return false;
        }

        public boolean isWorldPhone(int subId, String callingPackage) throws RemoteException {
            return false;
        }

        public boolean isTtyModeSupported() throws RemoteException {
            return false;
        }

        public boolean isRttSupported(int subscriptionId) throws RemoteException {
            return false;
        }

        public boolean isHearingAidCompatibilitySupported() throws RemoteException {
            return false;
        }

        public boolean isImsRegistered(int subId) throws RemoteException {
            return false;
        }

        public boolean isWifiCallingAvailable(int subId) throws RemoteException {
            return false;
        }

        public boolean isVideoTelephonyAvailable(int subId) throws RemoteException {
            return false;
        }

        public int getImsRegTechnologyForMmTel(int subId) throws RemoteException {
            return 0;
        }

        public String getDeviceId(String callingPackage) throws RemoteException {
            return null;
        }

        public String getImeiForSlot(int slotIndex, String callingPackage) throws RemoteException {
            return null;
        }

        public String getTypeAllocationCodeForSlot(int slotIndex) throws RemoteException {
            return null;
        }

        public String getMeidForSlot(int slotIndex, String callingPackage) throws RemoteException {
            return null;
        }

        public String getManufacturerCodeForSlot(int slotIndex) throws RemoteException {
            return null;
        }

        public String getDeviceSoftwareVersionForSlot(int slotIndex, String callingPackage) throws RemoteException {
            return null;
        }

        public int getSubIdForPhoneAccount(PhoneAccount phoneAccount) throws RemoteException {
            return 0;
        }

        public PhoneAccountHandle getPhoneAccountHandleForSubscriptionId(int subscriptionId) throws RemoteException {
            return null;
        }

        public void factoryReset(int subId) throws RemoteException {
        }

        public String getSimLocaleForSubscriber(int subId) throws RemoteException {
            return null;
        }

        public void requestModemActivityInfo(ResultReceiver result) throws RemoteException {
        }

        public ServiceState getServiceStateForSubscriber(int subId, String callingPackage) throws RemoteException {
            return null;
        }

        public Uri getVoicemailRingtoneUri(PhoneAccountHandle accountHandle) throws RemoteException {
            return null;
        }

        public void setVoicemailRingtoneUri(String callingPackage, PhoneAccountHandle phoneAccountHandle, Uri uri) throws RemoteException {
        }

        public boolean isVoicemailVibrationEnabled(PhoneAccountHandle accountHandle) throws RemoteException {
            return false;
        }

        public void setVoicemailVibrationEnabled(String callingPackage, PhoneAccountHandle phoneAccountHandle, boolean enabled) throws RemoteException {
        }

        public List<String> getPackagesWithCarrierPrivileges(int phoneId) throws RemoteException {
            return null;
        }

        public List<String> getPackagesWithCarrierPrivilegesForAllPhones() throws RemoteException {
            return null;
        }

        public String getAidForAppType(int subId, int appType) throws RemoteException {
            return null;
        }

        public String getEsn(int subId) throws RemoteException {
            return null;
        }

        public String getCdmaPrlVersion(int subId) throws RemoteException {
            return null;
        }

        public List<TelephonyHistogram> getTelephonyHistograms() throws RemoteException {
            return null;
        }

        public int setAllowedCarriers(CarrierRestrictionRules carrierRestrictionRules) throws RemoteException {
            return 0;
        }

        public CarrierRestrictionRules getAllowedCarriers() throws RemoteException {
            return null;
        }

        public int getSubscriptionCarrierId(int subId) throws RemoteException {
            return 0;
        }

        public String getSubscriptionCarrierName(int subId) throws RemoteException {
            return null;
        }

        public int getSubscriptionSpecificCarrierId(int subId) throws RemoteException {
            return 0;
        }

        public String getSubscriptionSpecificCarrierName(int subId) throws RemoteException {
            return null;
        }

        public int getCarrierIdFromMccMnc(int slotIndex, String mccmnc, boolean isSubscriptionMccMnc) throws RemoteException {
            return 0;
        }

        public void carrierActionSetMeteredApnsEnabled(int subId, boolean visible) throws RemoteException {
        }

        public void carrierActionSetRadioEnabled(int subId, boolean enabled) throws RemoteException {
        }

        public void carrierActionReportDefaultNetworkStatus(int subId, boolean report) throws RemoteException {
        }

        public void carrierActionResetAll(int subId) throws RemoteException {
        }

        public NetworkStats getVtDataUsage(int subId, boolean perUidStats) throws RemoteException {
            return null;
        }

        public void setPolicyDataEnabled(boolean enabled, int subId) throws RemoteException {
        }

        public List<ClientRequestStats> getClientRequestStats(String callingPackage, int subid) throws RemoteException {
            return null;
        }

        public void setSimPowerStateForSlot(int slotIndex, int state) throws RemoteException {
        }

        public String[] getForbiddenPlmns(int subId, int appType, String callingPackage) throws RemoteException {
            return null;
        }

        public boolean getEmergencyCallbackMode(int subId) throws RemoteException {
            return false;
        }

        public SignalStrength getSignalStrength(int subId) throws RemoteException {
            return null;
        }

        public int getCardIdForDefaultEuicc(int subId, String callingPackage) throws RemoteException {
            return 0;
        }

        public List<UiccCardInfo> getUiccCardsInfo(String callingPackage) throws RemoteException {
            return null;
        }

        public UiccSlotInfo[] getUiccSlotsInfo() throws RemoteException {
            return null;
        }

        public boolean switchSlots(int[] physicalSlots) throws RemoteException {
            return false;
        }

        public void setRadioIndicationUpdateMode(int subId, int filters, int mode) throws RemoteException {
        }

        public boolean isDataRoamingEnabled(int subId) throws RemoteException {
            return false;
        }

        public void setDataRoamingEnabled(int subId, boolean isEnabled) throws RemoteException {
        }

        public int getCdmaRoamingMode(int subId) throws RemoteException {
            return 0;
        }

        public boolean setCdmaRoamingMode(int subId, int mode) throws RemoteException {
            return false;
        }

        public boolean setCdmaSubscriptionMode(int subId, int mode) throws RemoteException {
            return false;
        }

        public void setCarrierTestOverride(int subId, String mccmnc, String imsi, String iccid, String gid1, String gid2, String plmn, String spn, String carrierPrivilegeRules, String apn) throws RemoteException {
        }

        public int getCarrierIdListVersion(int subId) throws RemoteException {
            return 0;
        }

        public void refreshUiccProfile(int subId) throws RemoteException {
        }

        public int getNumberOfModemsWithSimultaneousDataConnections(int subId, String callingPackage) throws RemoteException {
            return 0;
        }

        public int getNetworkSelectionMode(int subId) throws RemoteException {
            return 0;
        }

        public boolean isInEmergencySmsMode() throws RemoteException {
            return false;
        }

        public String[] getSmsApps(int userId) throws RemoteException {
            return null;
        }

        public String getDefaultSmsApp(int userId) throws RemoteException {
            return null;
        }

        public void setDefaultSmsApp(int userId, String packageName) throws RemoteException {
        }

        public int getRadioPowerState(int slotIndex, String callingPackage) throws RemoteException {
            return 0;
        }

        public void registerImsRegistrationCallback(int subId, IImsRegistrationCallback c) throws RemoteException {
        }

        public void unregisterImsRegistrationCallback(int subId, IImsRegistrationCallback c) throws RemoteException {
        }

        public void registerMmTelCapabilityCallback(int subId, IImsCapabilityCallback c) throws RemoteException {
        }

        public void unregisterMmTelCapabilityCallback(int subId, IImsCapabilityCallback c) throws RemoteException {
        }

        public boolean isCapable(int subId, int capability, int regTech) throws RemoteException {
            return false;
        }

        public boolean isAvailable(int subId, int capability, int regTech) throws RemoteException {
            return false;
        }

        public boolean isAdvancedCallingSettingEnabled(int subId) throws RemoteException {
            return false;
        }

        public void setAdvancedCallingSettingEnabled(int subId, boolean isEnabled) throws RemoteException {
        }

        public boolean isVtSettingEnabled(int subId) throws RemoteException {
            return false;
        }

        public void setVtSettingEnabled(int subId, boolean isEnabled) throws RemoteException {
        }

        public boolean isVoWiFiSettingEnabled(int subId) throws RemoteException {
            return false;
        }

        public void setVoWiFiSettingEnabled(int subId, boolean isEnabled) throws RemoteException {
        }

        public boolean isVoWiFiRoamingSettingEnabled(int subId) throws RemoteException {
            return false;
        }

        public void setVoWiFiRoamingSettingEnabled(int subId, boolean isEnabled) throws RemoteException {
        }

        public void setVoWiFiNonPersistent(int subId, boolean isCapable, int mode) throws RemoteException {
        }

        public int getVoWiFiModeSetting(int subId) throws RemoteException {
            return 0;
        }

        public void setVoWiFiModeSetting(int subId, int mode) throws RemoteException {
        }

        public int getVoWiFiRoamingModeSetting(int subId) throws RemoteException {
            return 0;
        }

        public void setVoWiFiRoamingModeSetting(int subId, int mode) throws RemoteException {
        }

        public void setRttCapabilitySetting(int subId, boolean isEnabled) throws RemoteException {
        }

        public boolean isTtyOverVolteEnabled(int subId) throws RemoteException {
            return false;
        }

        public Map getEmergencyNumberList(String callingPackage) throws RemoteException {
            return null;
        }

        public boolean isEmergencyNumber(String number, boolean exactMatch) throws RemoteException {
            return false;
        }

        public List<String> getCertsFromCarrierPrivilegeAccessRules(int subId) throws RemoteException {
            return null;
        }

        public void registerImsProvisioningChangedCallback(int subId, IImsConfigCallback callback) throws RemoteException {
        }

        public void unregisterImsProvisioningChangedCallback(int subId, IImsConfigCallback callback) throws RemoteException {
        }

        public void setImsProvisioningStatusForCapability(int subId, int capability, int tech, boolean isProvisioned) throws RemoteException {
        }

        public boolean getImsProvisioningStatusForCapability(int subId, int capability, int tech) throws RemoteException {
            return false;
        }

        public boolean isMmTelCapabilityProvisionedInCache(int subId, int capability, int tech) throws RemoteException {
            return false;
        }

        public void cacheMmTelCapabilityProvisioning(int subId, int capability, int tech, boolean isProvisioned) throws RemoteException {
        }

        public int getImsProvisioningInt(int subId, int key) throws RemoteException {
            return 0;
        }

        public String getImsProvisioningString(int subId, int key) throws RemoteException {
            return null;
        }

        public int setImsProvisioningInt(int subId, int key, int value) throws RemoteException {
            return 0;
        }

        public int setImsProvisioningString(int subId, int key, String value) throws RemoteException {
            return 0;
        }

        public void updateEmergencyNumberListTestMode(int action, EmergencyNumber num) throws RemoteException {
        }

        public List<String> getEmergencyNumberListTestMode() throws RemoteException {
            return null;
        }

        public boolean enableModemForSlot(int slotIndex, boolean enable) throws RemoteException {
            return false;
        }

        public void setMultiSimCarrierRestriction(boolean isMultiSimCarrierRestricted) throws RemoteException {
        }

        public int isMultiSimSupported(String callingPackage) throws RemoteException {
            return 0;
        }

        public void switchMultiSimConfig(int numOfSims) throws RemoteException {
        }

        public boolean doesSwitchMultiSimConfigTriggerReboot(int subId, String callingPackage) throws RemoteException {
            return false;
        }

        public int[] getSlotsMapping() throws RemoteException {
            return null;
        }

        public int getRadioHalVersion() throws RemoteException {
            return 0;
        }

        public boolean isModemEnabledForSlot(int slotIndex, String callingPackage) throws RemoteException {
            return false;
        }

        public boolean isDataEnabledForApn(int apnType, int subId, String callingPackage) throws RemoteException {
            return false;
        }

        public boolean isApnMetered(int apnType, int subId) throws RemoteException {
            return false;
        }

        public void enqueueSmsPickResult(String callingPackage, IIntegerConsumer subIdResult) throws RemoteException {
        }

        public String getMmsUserAgent(int subId) throws RemoteException {
            return null;
        }

        public String getMmsUAProfUrl(int subId) throws RemoteException {
            return null;
        }

        public boolean setDataAllowedDuringVoiceCall(int subId, boolean allow) throws RemoteException {
            return false;
        }

        public boolean isDataAllowedInVoiceCall(int subId) throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ITelephony {
        private static final String DESCRIPTOR = "com.android.internal.telephony.ITelephony";
        static final int TRANSACTION_cacheMmTelCapabilityProvisioning = 231;
        static final int TRANSACTION_call = 2;
        static final int TRANSACTION_canChangeDtmfToneLength = 133;
        static final int TRANSACTION_carrierActionReportDefaultNetworkStatus = 173;
        static final int TRANSACTION_carrierActionResetAll = 174;
        static final int TRANSACTION_carrierActionSetMeteredApnsEnabled = 171;
        static final int TRANSACTION_carrierActionSetRadioEnabled = 172;
        static final int TRANSACTION_checkCarrierPrivilegesForPackage = 117;
        static final int TRANSACTION_checkCarrierPrivilegesForPackageAnyPhone = 118;
        static final int TRANSACTION_dial = 1;
        static final int TRANSACTION_disableDataConnectivity = 28;
        static final int TRANSACTION_disableIms = 92;
        static final int TRANSACTION_disableLocationUpdates = 25;
        static final int TRANSACTION_disableLocationUpdatesForSubscriber = 26;
        static final int TRANSACTION_disableVisualVoicemailSmsFilter = 56;
        static final int TRANSACTION_doesSwitchMultiSimConfigTriggerReboot = 242;
        static final int TRANSACTION_enableDataConnectivity = 27;
        static final int TRANSACTION_enableIms = 91;
        static final int TRANSACTION_enableLocationUpdates = 23;
        static final int TRANSACTION_enableLocationUpdatesForSubscriber = 24;
        static final int TRANSACTION_enableModemForSlot = 238;
        static final int TRANSACTION_enableVideoCalling = 131;
        static final int TRANSACTION_enableVisualVoicemailSmsFilter = 55;
        static final int TRANSACTION_enqueueSmsPickResult = 248;
        static final int TRANSACTION_factoryReset = 150;
        static final int TRANSACTION_getActivePhoneType = 37;
        static final int TRANSACTION_getActivePhoneTypeForSlot = 38;
        static final int TRANSACTION_getActiveVisualVoicemailSmsFilterSettings = 58;
        static final int TRANSACTION_getAidForAppType = 160;
        static final int TRANSACTION_getAllCellInfo = 69;
        static final int TRANSACTION_getAllowedCarriers = 165;
        static final int TRANSACTION_getCalculatedPreferredNetworkType = 88;
        static final int TRANSACTION_getCallState = 33;
        static final int TRANSACTION_getCallStateForSlot = 34;
        static final int TRANSACTION_getCardIdForDefaultEuicc = 182;
        static final int TRANSACTION_getCarrierIdFromMccMnc = 170;
        static final int TRANSACTION_getCarrierIdListVersion = 193;
        static final int TRANSACTION_getCarrierPackageNamesForIntentAndPhone = 119;
        static final int TRANSACTION_getCarrierPrivilegeStatus = 115;
        static final int TRANSACTION_getCarrierPrivilegeStatusForUid = 116;
        static final int TRANSACTION_getCdmaEriIconIndex = 39;
        static final int TRANSACTION_getCdmaEriIconIndexForSubscriber = 40;
        static final int TRANSACTION_getCdmaEriIconMode = 41;
        static final int TRANSACTION_getCdmaEriIconModeForSubscriber = 42;
        static final int TRANSACTION_getCdmaEriText = 43;
        static final int TRANSACTION_getCdmaEriTextForSubscriber = 44;
        static final int TRANSACTION_getCdmaMdn = 112;
        static final int TRANSACTION_getCdmaMin = 113;
        static final int TRANSACTION_getCdmaPrlVersion = 162;
        static final int TRANSACTION_getCdmaRoamingMode = 189;
        static final int TRANSACTION_getCellLocation = 30;
        static final int TRANSACTION_getCellNetworkScanResults = 100;
        static final int TRANSACTION_getCertsFromCarrierPrivilegeAccessRules = 225;
        static final int TRANSACTION_getClientRequestStats = 177;
        static final int TRANSACTION_getDataActivationState = 50;
        static final int TRANSACTION_getDataActivity = 35;
        static final int TRANSACTION_getDataEnabled = 106;
        static final int TRANSACTION_getDataNetworkType = 62;
        static final int TRANSACTION_getDataNetworkTypeForSubscriber = 63;
        static final int TRANSACTION_getDataState = 36;
        static final int TRANSACTION_getDefaultSmsApp = 199;
        static final int TRANSACTION_getDeviceId = 142;
        static final int TRANSACTION_getDeviceSoftwareVersionForSlot = 147;
        static final int TRANSACTION_getEmergencyCallbackMode = 180;
        static final int TRANSACTION_getEmergencyNumberList = 223;
        static final int TRANSACTION_getEmergencyNumberListTestMode = 237;
        static final int TRANSACTION_getEsn = 161;
        static final int TRANSACTION_getForbiddenPlmns = 179;
        static final int TRANSACTION_getImeiForSlot = 143;
        static final int TRANSACTION_getImsConfig = 96;
        static final int TRANSACTION_getImsProvisioningInt = 232;
        static final int TRANSACTION_getImsProvisioningStatusForCapability = 229;
        static final int TRANSACTION_getImsProvisioningString = 233;
        static final int TRANSACTION_getImsRegTechnologyForMmTel = 141;
        static final int TRANSACTION_getImsRegistration = 95;
        static final int TRANSACTION_getImsService = 98;
        static final int TRANSACTION_getLine1AlphaTagForDisplay = 122;
        static final int TRANSACTION_getLine1NumberForDisplay = 121;
        static final int TRANSACTION_getLteOnCdmaMode = 67;
        static final int TRANSACTION_getLteOnCdmaModeForSubscriber = 68;
        static final int TRANSACTION_getManufacturerCodeForSlot = 146;
        static final int TRANSACTION_getMeidForSlot = 145;
        static final int TRANSACTION_getMergedSubscriberIds = 123;
        static final int TRANSACTION_getMmTelFeatureAndListen = 93;
        static final int TRANSACTION_getMmsUAProfUrl = 250;
        static final int TRANSACTION_getMmsUserAgent = 249;
        static final int TRANSACTION_getNeighboringCellInfo = 32;
        static final int TRANSACTION_getNetworkCountryIsoForPhone = 31;
        static final int TRANSACTION_getNetworkSelectionMode = 196;
        static final int TRANSACTION_getNetworkTypeForSubscriber = 61;
        static final int TRANSACTION_getNumberOfModemsWithSimultaneousDataConnections = 195;
        static final int TRANSACTION_getPackagesWithCarrierPrivileges = 158;
        static final int TRANSACTION_getPackagesWithCarrierPrivilegesForAllPhones = 159;
        static final int TRANSACTION_getPcscfAddress = 110;
        static final int TRANSACTION_getPhoneAccountHandleForSubscriptionId = 149;
        static final int TRANSACTION_getPreferredNetworkType = 89;
        static final int TRANSACTION_getRadioAccessFamily = 130;
        static final int TRANSACTION_getRadioHalVersion = 244;
        static final int TRANSACTION_getRadioPowerState = 201;
        static final int TRANSACTION_getRcsFeatureAndListen = 94;
        static final int TRANSACTION_getServiceStateForSubscriber = 153;
        static final int TRANSACTION_getSignalStrength = 181;
        static final int TRANSACTION_getSimLocaleForSubscriber = 151;
        static final int TRANSACTION_getSlotsMapping = 243;
        static final int TRANSACTION_getSmsApps = 198;
        static final int TRANSACTION_getSubIdForPhoneAccount = 148;
        static final int TRANSACTION_getSubscriptionCarrierId = 166;
        static final int TRANSACTION_getSubscriptionCarrierName = 167;
        static final int TRANSACTION_getSubscriptionSpecificCarrierId = 168;
        static final int TRANSACTION_getSubscriptionSpecificCarrierName = 169;
        static final int TRANSACTION_getTelephonyHistograms = 163;
        static final int TRANSACTION_getTetherApnRequiredForSubscriber = 90;
        static final int TRANSACTION_getTypeAllocationCodeForSlot = 144;
        static final int TRANSACTION_getUiccCardsInfo = 183;
        static final int TRANSACTION_getUiccSlotsInfo = 184;
        static final int TRANSACTION_getVisualVoicemailPackageName = 54;
        static final int TRANSACTION_getVisualVoicemailSettings = 53;
        static final int TRANSACTION_getVisualVoicemailSmsFilterSettings = 57;
        static final int TRANSACTION_getVoWiFiModeSetting = 217;
        static final int TRANSACTION_getVoWiFiRoamingModeSetting = 219;
        static final int TRANSACTION_getVoiceActivationState = 49;
        static final int TRANSACTION_getVoiceMessageCountForSubscriber = 51;
        static final int TRANSACTION_getVoiceNetworkTypeForSubscriber = 64;
        static final int TRANSACTION_getVoicemailRingtoneUri = 154;
        static final int TRANSACTION_getVtDataUsage = 175;
        static final int TRANSACTION_handlePinMmi = 13;
        static final int TRANSACTION_handlePinMmiForSubscriber = 15;
        static final int TRANSACTION_handleUssdRequest = 14;
        static final int TRANSACTION_hasIccCard = 65;
        static final int TRANSACTION_hasIccCardUsingSlotIndex = 66;
        static final int TRANSACTION_iccCloseLogicalChannel = 76;
        static final int TRANSACTION_iccCloseLogicalChannelBySlot = 75;
        static final int TRANSACTION_iccExchangeSimIO = 81;
        static final int TRANSACTION_iccOpenLogicalChannel = 74;
        static final int TRANSACTION_iccOpenLogicalChannelBySlot = 73;
        static final int TRANSACTION_iccTransmitApduBasicChannel = 80;
        static final int TRANSACTION_iccTransmitApduBasicChannelBySlot = 79;
        static final int TRANSACTION_iccTransmitApduLogicalChannel = 78;
        static final int TRANSACTION_iccTransmitApduLogicalChannelBySlot = 77;
        static final int TRANSACTION_invokeOemRilRequestRaw = 126;
        static final int TRANSACTION_isAdvancedCallingSettingEnabled = 208;
        static final int TRANSACTION_isApnMetered = 247;
        static final int TRANSACTION_isAvailable = 207;
        static final int TRANSACTION_isCapable = 206;
        static final int TRANSACTION_isConcurrentVoiceAndDataAllowed = 52;
        static final int TRANSACTION_isDataAllowedInVoiceCall = 252;
        static final int TRANSACTION_isDataConnectivityPossible = 29;
        static final int TRANSACTION_isDataEnabled = 108;
        static final int TRANSACTION_isDataEnabledForApn = 246;
        static final int TRANSACTION_isDataRoamingEnabled = 187;
        static final int TRANSACTION_isEmergencyNumber = 224;
        static final int TRANSACTION_isHearingAidCompatibilitySupported = 137;
        static final int TRANSACTION_isImsRegistered = 138;
        static final int TRANSACTION_isInEmergencySmsMode = 197;
        static final int TRANSACTION_isManualNetworkSelectionAllowed = 109;
        static final int TRANSACTION_isMmTelCapabilityProvisionedInCache = 230;
        static final int TRANSACTION_isModemEnabledForSlot = 245;
        static final int TRANSACTION_isMultiSimSupported = 240;
        static final int TRANSACTION_isRadioOn = 3;
        static final int TRANSACTION_isRadioOnForSubscriber = 4;
        static final int TRANSACTION_isRttSupported = 136;
        static final int TRANSACTION_isTtyModeSupported = 135;
        static final int TRANSACTION_isTtyOverVolteEnabled = 222;
        static final int TRANSACTION_isUserDataEnabled = 107;
        static final int TRANSACTION_isVideoCallingEnabled = 132;
        static final int TRANSACTION_isVideoTelephonyAvailable = 140;
        static final int TRANSACTION_isVoWiFiRoamingSettingEnabled = 214;
        static final int TRANSACTION_isVoWiFiSettingEnabled = 212;
        static final int TRANSACTION_isVoicemailVibrationEnabled = 156;
        static final int TRANSACTION_isVtSettingEnabled = 210;
        static final int TRANSACTION_isWifiCallingAvailable = 139;
        static final int TRANSACTION_isWorldPhone = 134;
        static final int TRANSACTION_needMobileRadioShutdown = 127;
        static final int TRANSACTION_needsOtaServiceProvisioning = 45;
        static final int TRANSACTION_nvReadItem = 83;
        static final int TRANSACTION_nvWriteCdmaPrl = 85;
        static final int TRANSACTION_nvWriteItem = 84;
        static final int TRANSACTION_rebootModem = 87;
        static final int TRANSACTION_refreshUiccProfile = 194;
        static final int TRANSACTION_registerImsProvisioningChangedCallback = 226;
        static final int TRANSACTION_registerImsRegistrationCallback = 202;
        static final int TRANSACTION_registerMmTelCapabilityCallback = 204;
        static final int TRANSACTION_requestCellInfoUpdate = 70;
        static final int TRANSACTION_requestCellInfoUpdateWithWorkSource = 71;
        static final int TRANSACTION_requestModemActivityInfo = 152;
        static final int TRANSACTION_requestNetworkScan = 101;
        static final int TRANSACTION_requestNumberVerification = 114;
        static final int TRANSACTION_resetModemConfig = 86;
        static final int TRANSACTION_sendDialerSpecialCode = 60;
        static final int TRANSACTION_sendEnvelopeWithStatus = 82;
        static final int TRANSACTION_sendVisualVoicemailSmsForSubscriber = 59;
        static final int TRANSACTION_setAdvancedCallingSettingEnabled = 209;
        static final int TRANSACTION_setAllowedCarriers = 164;
        static final int TRANSACTION_setCarrierTestOverride = 192;
        static final int TRANSACTION_setCdmaRoamingMode = 190;
        static final int TRANSACTION_setCdmaSubscriptionMode = 191;
        static final int TRANSACTION_setCellInfoListRate = 72;
        static final int TRANSACTION_setDataActivationState = 48;
        static final int TRANSACTION_setDataAllowedDuringVoiceCall = 251;
        static final int TRANSACTION_setDataRoamingEnabled = 188;
        static final int TRANSACTION_setDefaultSmsApp = 200;
        static final int TRANSACTION_setImsProvisioningInt = 234;
        static final int TRANSACTION_setImsProvisioningStatusForCapability = 228;
        static final int TRANSACTION_setImsProvisioningString = 235;
        static final int TRANSACTION_setImsRegistrationState = 111;
        static final int TRANSACTION_setImsService = 97;
        static final int TRANSACTION_setLine1NumberForDisplayForSubscriber = 120;
        static final int TRANSACTION_setMultiSimCarrierRestriction = 239;
        static final int TRANSACTION_setNetworkSelectionModeAutomatic = 99;
        static final int TRANSACTION_setNetworkSelectionModeManual = 103;
        static final int TRANSACTION_setOperatorBrandOverride = 124;
        static final int TRANSACTION_setPolicyDataEnabled = 176;
        static final int TRANSACTION_setPreferredNetworkType = 104;
        static final int TRANSACTION_setRadio = 18;
        static final int TRANSACTION_setRadioCapability = 129;
        static final int TRANSACTION_setRadioForSubscriber = 19;
        static final int TRANSACTION_setRadioIndicationUpdateMode = 186;
        static final int TRANSACTION_setRadioPower = 20;
        static final int TRANSACTION_setRoamingOverride = 125;
        static final int TRANSACTION_setRttCapabilitySetting = 221;
        static final int TRANSACTION_setSimPowerStateForSlot = 178;
        static final int TRANSACTION_setUserDataEnabled = 105;
        static final int TRANSACTION_setVoWiFiModeSetting = 218;
        static final int TRANSACTION_setVoWiFiNonPersistent = 216;
        static final int TRANSACTION_setVoWiFiRoamingModeSetting = 220;
        static final int TRANSACTION_setVoWiFiRoamingSettingEnabled = 215;
        static final int TRANSACTION_setVoWiFiSettingEnabled = 213;
        static final int TRANSACTION_setVoiceActivationState = 47;
        static final int TRANSACTION_setVoiceMailNumber = 46;
        static final int TRANSACTION_setVoicemailRingtoneUri = 155;
        static final int TRANSACTION_setVoicemailVibrationEnabled = 157;
        static final int TRANSACTION_setVtSettingEnabled = 211;
        static final int TRANSACTION_shutdownMobileRadios = 128;
        static final int TRANSACTION_stopNetworkScan = 102;
        static final int TRANSACTION_supplyPin = 5;
        static final int TRANSACTION_supplyPinForSubscriber = 6;
        static final int TRANSACTION_supplyPinReportResult = 9;
        static final int TRANSACTION_supplyPinReportResultForSubscriber = 10;
        static final int TRANSACTION_supplyPuk = 7;
        static final int TRANSACTION_supplyPukForSubscriber = 8;
        static final int TRANSACTION_supplyPukReportResult = 11;
        static final int TRANSACTION_supplyPukReportResultForSubscriber = 12;
        static final int TRANSACTION_switchMultiSimConfig = 241;
        static final int TRANSACTION_switchSlots = 185;
        static final int TRANSACTION_toggleRadioOnOff = 16;
        static final int TRANSACTION_toggleRadioOnOffForSubscriber = 17;
        static final int TRANSACTION_unregisterImsProvisioningChangedCallback = 227;
        static final int TRANSACTION_unregisterImsRegistrationCallback = 203;
        static final int TRANSACTION_unregisterMmTelCapabilityCallback = 205;
        static final int TRANSACTION_updateEmergencyNumberListTestMode = 236;
        static final int TRANSACTION_updateServiceLocation = 21;
        static final int TRANSACTION_updateServiceLocationForSubscriber = 22;

        private static class Proxy implements ITelephony {
            public static ITelephony sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public void dial(String number) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(number);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().dial(number);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void call(String callingPackage, String number) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(number);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().call(callingPackage, number);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isRadioOn(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isRadioOn(callingPackage);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isRadioOnForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isRadioOnForSubscriber(subId, callingPackage);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean supplyPin(String pin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pin);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().supplyPin(pin);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean supplyPinForSubscriber(int subId, String pin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(pin);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().supplyPinForSubscriber(subId, pin);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean supplyPuk(String puk, String pin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(puk);
                    _data.writeString(pin);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().supplyPuk(puk, pin);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean supplyPukForSubscriber(int subId, String puk, String pin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(puk);
                    _data.writeString(pin);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().supplyPukForSubscriber(subId, puk, pin);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] supplyPinReportResult(String pin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pin);
                    int[] iArr = 9;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        iArr = Stub.getDefaultImpl();
                        if (iArr != 0) {
                            iArr = Stub.getDefaultImpl().supplyPinReportResult(pin);
                            return iArr;
                        }
                    }
                    _reply.readException();
                    iArr = _reply.createIntArray();
                    int[] _result = iArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] supplyPinReportResultForSubscriber(int subId, String pin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(pin);
                    int[] iArr = 10;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        iArr = Stub.getDefaultImpl();
                        if (iArr != 0) {
                            iArr = Stub.getDefaultImpl().supplyPinReportResultForSubscriber(subId, pin);
                            return iArr;
                        }
                    }
                    _reply.readException();
                    iArr = _reply.createIntArray();
                    int[] _result = iArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] supplyPukReportResult(String puk, String pin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(puk);
                    _data.writeString(pin);
                    int[] iArr = 11;
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        iArr = Stub.getDefaultImpl();
                        if (iArr != 0) {
                            iArr = Stub.getDefaultImpl().supplyPukReportResult(puk, pin);
                            return iArr;
                        }
                    }
                    _reply.readException();
                    iArr = _reply.createIntArray();
                    int[] _result = iArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] supplyPukReportResultForSubscriber(int subId, String puk, String pin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(puk);
                    _data.writeString(pin);
                    int[] iArr = 12;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        iArr = Stub.getDefaultImpl();
                        if (iArr != 0) {
                            iArr = Stub.getDefaultImpl().supplyPukReportResultForSubscriber(subId, puk, pin);
                            return iArr;
                        }
                    }
                    _reply.readException();
                    iArr = _reply.createIntArray();
                    int[] _result = iArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean handlePinMmi(String dialString) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(dialString);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().handlePinMmi(dialString);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void handleUssdRequest(int subId, String ussdRequest, ResultReceiver wrappedCallback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(ussdRequest);
                    if (wrappedCallback != null) {
                        _data.writeInt(1);
                        wrappedCallback.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().handleUssdRequest(subId, ussdRequest, wrappedCallback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean handlePinMmiForSubscriber(int subId, String dialString) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(dialString);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().handlePinMmiForSubscriber(subId, dialString);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void toggleRadioOnOff() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(16, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().toggleRadioOnOff();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void toggleRadioOnOffForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    if (this.mRemote.transact(17, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().toggleRadioOnOffForSubscriber(subId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setRadio(boolean turnOn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    _data.writeInt(turnOn ? 1 : 0);
                    if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setRadio(turnOn);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setRadioForSubscriber(int subId, boolean turnOn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _result = true;
                    _data.writeInt(turnOn ? 1 : 0);
                    if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setRadioForSubscriber(subId, turnOn);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setRadioPower(boolean turnOn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    _data.writeInt(turnOn ? 1 : 0);
                    if (this.mRemote.transact(20, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setRadioPower(turnOn);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateServiceLocation() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(21, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateServiceLocation();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateServiceLocationForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateServiceLocationForSubscriber(subId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableLocationUpdates() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(23, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().enableLocationUpdates();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableLocationUpdatesForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    if (this.mRemote.transact(24, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().enableLocationUpdatesForSubscriber(subId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disableLocationUpdates() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(25, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().disableLocationUpdates();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disableLocationUpdatesForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    if (this.mRemote.transact(26, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().disableLocationUpdatesForSubscriber(subId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean enableDataConnectivity() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(27, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().enableDataConnectivity();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean disableDataConnectivity() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(28, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().disableDataConnectivity();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isDataConnectivityPossible(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(29, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isDataConnectivityPossible(subId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bundle getCellLocation(String callingPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    Bundle bundle = 30;
                    if (!this.mRemote.transact(30, _data, _reply, 0)) {
                        bundle = Stub.getDefaultImpl();
                        if (bundle != 0) {
                            bundle = Stub.getDefaultImpl().getCellLocation(callingPkg);
                            return bundle;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        bundle = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return bundle;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getNetworkCountryIsoForPhone(int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    String str = 31;
                    if (!this.mRemote.transact(31, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getNetworkCountryIsoForPhone(phoneId);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<NeighboringCellInfo> getNeighboringCellInfo(String callingPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    List<NeighboringCellInfo> list = 32;
                    if (!this.mRemote.transact(32, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getNeighboringCellInfo(callingPkg);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(NeighboringCellInfo.CREATOR);
                    List<NeighboringCellInfo> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCallState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 33;
                    if (!this.mRemote.transact(33, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getCallState();
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCallStateForSlot(int slotIndex) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    int i = 34;
                    if (!this.mRemote.transact(34, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getCallStateForSlot(slotIndex);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getDataActivity() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 35;
                    if (!this.mRemote.transact(35, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getDataActivity();
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getDataState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 36;
                    if (!this.mRemote.transact(36, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getDataState();
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getActivePhoneType() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 37;
                    if (!this.mRemote.transact(37, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getActivePhoneType();
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getActivePhoneTypeForSlot(int slotIndex) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    int i = 38;
                    if (!this.mRemote.transact(38, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getActivePhoneTypeForSlot(slotIndex);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCdmaEriIconIndex(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    int i = 39;
                    if (!this.mRemote.transact(39, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getCdmaEriIconIndex(callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCdmaEriIconIndexForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    int i = 40;
                    if (!this.mRemote.transact(40, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getCdmaEriIconIndexForSubscriber(subId, callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCdmaEriIconMode(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    int i = 41;
                    if (!this.mRemote.transact(41, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getCdmaEriIconMode(callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCdmaEriIconModeForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    int i = 42;
                    if (!this.mRemote.transact(42, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getCdmaEriIconModeForSubscriber(subId, callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getCdmaEriText(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    String str = 43;
                    if (!this.mRemote.transact(43, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getCdmaEriText(callingPackage);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getCdmaEriTextForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    String str = 44;
                    if (!this.mRemote.transact(44, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getCdmaEriTextForSubscriber(subId, callingPackage);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean needsOtaServiceProvisioning() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(45, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().needsOtaServiceProvisioning();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setVoiceMailNumber(int subId, String alphaTag, String number) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(alphaTag);
                    _data.writeString(number);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(46, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().setVoiceMailNumber(subId, alphaTag, number);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setVoiceActivationState(int subId, int activationState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(activationState);
                    if (this.mRemote.transact(47, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setVoiceActivationState(subId, activationState);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setDataActivationState(int subId, int activationState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(activationState);
                    if (this.mRemote.transact(48, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setDataActivationState(subId, activationState);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getVoiceActivationState(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    int i = 49;
                    if (!this.mRemote.transact(49, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getVoiceActivationState(subId, callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getDataActivationState(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    int i = 50;
                    if (!this.mRemote.transact(50, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getDataActivationState(subId, callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getVoiceMessageCountForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    int i = 51;
                    if (!this.mRemote.transact(51, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getVoiceMessageCountForSubscriber(subId, callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isConcurrentVoiceAndDataAllowed(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(52, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isConcurrentVoiceAndDataAllowed(subId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bundle getVisualVoicemailSettings(String callingPackage, int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(subId);
                    Bundle bundle = 53;
                    if (!this.mRemote.transact(53, _data, _reply, 0)) {
                        bundle = Stub.getDefaultImpl();
                        if (bundle != 0) {
                            bundle = Stub.getDefaultImpl().getVisualVoicemailSettings(callingPackage, subId);
                            return bundle;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        bundle = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return bundle;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getVisualVoicemailPackageName(String callingPackage, int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(subId);
                    String str = 54;
                    if (!this.mRemote.transact(54, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getVisualVoicemailPackageName(callingPackage, subId);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableVisualVoicemailSmsFilter(String callingPackage, int subId, VisualVoicemailSmsFilterSettings settings) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(subId);
                    if (settings != null) {
                        _data.writeInt(1);
                        settings.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(55, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().enableVisualVoicemailSmsFilter(callingPackage, subId, settings);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disableVisualVoicemailSmsFilter(String callingPackage, int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(subId);
                    if (this.mRemote.transact(56, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().disableVisualVoicemailSmsFilter(callingPackage, subId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public VisualVoicemailSmsFilterSettings getVisualVoicemailSmsFilterSettings(String callingPackage, int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(subId);
                    VisualVoicemailSmsFilterSettings visualVoicemailSmsFilterSettings = 57;
                    if (!this.mRemote.transact(57, _data, _reply, 0)) {
                        visualVoicemailSmsFilterSettings = Stub.getDefaultImpl();
                        if (visualVoicemailSmsFilterSettings != 0) {
                            visualVoicemailSmsFilterSettings = Stub.getDefaultImpl().getVisualVoicemailSmsFilterSettings(callingPackage, subId);
                            return visualVoicemailSmsFilterSettings;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        visualVoicemailSmsFilterSettings = (VisualVoicemailSmsFilterSettings) VisualVoicemailSmsFilterSettings.CREATOR.createFromParcel(_reply);
                    } else {
                        visualVoicemailSmsFilterSettings = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return visualVoicemailSmsFilterSettings;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public VisualVoicemailSmsFilterSettings getActiveVisualVoicemailSmsFilterSettings(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    VisualVoicemailSmsFilterSettings visualVoicemailSmsFilterSettings = 58;
                    if (!this.mRemote.transact(58, _data, _reply, 0)) {
                        visualVoicemailSmsFilterSettings = Stub.getDefaultImpl();
                        if (visualVoicemailSmsFilterSettings != 0) {
                            visualVoicemailSmsFilterSettings = Stub.getDefaultImpl().getActiveVisualVoicemailSmsFilterSettings(subId);
                            return visualVoicemailSmsFilterSettings;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        visualVoicemailSmsFilterSettings = (VisualVoicemailSmsFilterSettings) VisualVoicemailSmsFilterSettings.CREATOR.createFromParcel(_reply);
                    } else {
                        visualVoicemailSmsFilterSettings = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return visualVoicemailSmsFilterSettings;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendVisualVoicemailSmsForSubscriber(String callingPackage, int subId, String number, int port, String text, PendingIntent sentIntent) throws RemoteException {
                Throwable th;
                String str;
                int i;
                String str2;
                int i2;
                PendingIntent pendingIntent = sentIntent;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(callingPackage);
                        try {
                            _data.writeInt(subId);
                        } catch (Throwable th2) {
                            th = th2;
                            str = number;
                            i = port;
                            str2 = text;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeString(number);
                        } catch (Throwable th3) {
                            th = th3;
                            i = port;
                            str2 = text;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        i2 = subId;
                        str = number;
                        i = port;
                        str2 = text;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(port);
                        try {
                            _data.writeString(text);
                            if (pendingIntent != null) {
                                _data.writeInt(1);
                                pendingIntent.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            if (this.mRemote.transact(59, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().sendVisualVoicemailSmsForSubscriber(callingPackage, subId, number, port, text, sentIntent);
                            _reply.recycle();
                            _data.recycle();
                        } catch (Throwable th5) {
                            th = th5;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        str2 = text;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    String str3 = callingPackage;
                    i2 = subId;
                    str = number;
                    i = port;
                    str2 = text;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void sendDialerSpecialCode(String callingPackageName, String inputCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackageName);
                    _data.writeString(inputCode);
                    if (this.mRemote.transact(60, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().sendDialerSpecialCode(callingPackageName, inputCode);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getNetworkTypeForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    int i = 61;
                    if (!this.mRemote.transact(61, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getNetworkTypeForSubscriber(subId, callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getDataNetworkType(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    int i = 62;
                    if (!this.mRemote.transact(62, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getDataNetworkType(callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getDataNetworkTypeForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    int i = 63;
                    if (!this.mRemote.transact(63, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getDataNetworkTypeForSubscriber(subId, callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getVoiceNetworkTypeForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    int i = 64;
                    if (!this.mRemote.transact(64, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getVoiceNetworkTypeForSubscriber(subId, callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean hasIccCard() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(65, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().hasIccCard();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean hasIccCardUsingSlotIndex(int slotIndex) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(66, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().hasIccCardUsingSlotIndex(slotIndex);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getLteOnCdmaMode(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    int i = 67;
                    if (!this.mRemote.transact(67, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getLteOnCdmaMode(callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getLteOnCdmaModeForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    int i = 68;
                    if (!this.mRemote.transact(68, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getLteOnCdmaModeForSubscriber(subId, callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<CellInfo> getAllCellInfo(String callingPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    List<CellInfo> list = 69;
                    if (!this.mRemote.transact(69, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getAllCellInfo(callingPkg);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(CellInfo.CREATOR);
                    List<CellInfo> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestCellInfoUpdate(int subId, ICellInfoCallback cb, String callingPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    _data.writeString(callingPkg);
                    if (this.mRemote.transact(70, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().requestCellInfoUpdate(subId, cb, callingPkg);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestCellInfoUpdateWithWorkSource(int subId, ICellInfoCallback cb, String callingPkg, WorkSource ws) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    _data.writeString(callingPkg);
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(71, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().requestCellInfoUpdateWithWorkSource(subId, cb, callingPkg, ws);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setCellInfoListRate(int rateInMillis) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(rateInMillis);
                    if (this.mRemote.transact(72, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setCellInfoListRate(rateInMillis);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IccOpenLogicalChannelResponse iccOpenLogicalChannelBySlot(int slotIndex, String callingPackage, String AID, int p2) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    _data.writeString(callingPackage);
                    _data.writeString(AID);
                    _data.writeInt(p2);
                    IccOpenLogicalChannelResponse iccOpenLogicalChannelResponse = 73;
                    if (!this.mRemote.transact(73, _data, _reply, 0)) {
                        iccOpenLogicalChannelResponse = Stub.getDefaultImpl();
                        if (iccOpenLogicalChannelResponse != 0) {
                            iccOpenLogicalChannelResponse = Stub.getDefaultImpl().iccOpenLogicalChannelBySlot(slotIndex, callingPackage, AID, p2);
                            return iccOpenLogicalChannelResponse;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        iccOpenLogicalChannelResponse = (IccOpenLogicalChannelResponse) IccOpenLogicalChannelResponse.CREATOR.createFromParcel(_reply);
                    } else {
                        iccOpenLogicalChannelResponse = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return iccOpenLogicalChannelResponse;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IccOpenLogicalChannelResponse iccOpenLogicalChannel(int subId, String callingPackage, String AID, int p2) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    _data.writeString(AID);
                    _data.writeInt(p2);
                    IccOpenLogicalChannelResponse iccOpenLogicalChannelResponse = 74;
                    if (!this.mRemote.transact(74, _data, _reply, 0)) {
                        iccOpenLogicalChannelResponse = Stub.getDefaultImpl();
                        if (iccOpenLogicalChannelResponse != 0) {
                            iccOpenLogicalChannelResponse = Stub.getDefaultImpl().iccOpenLogicalChannel(subId, callingPackage, AID, p2);
                            return iccOpenLogicalChannelResponse;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        iccOpenLogicalChannelResponse = (IccOpenLogicalChannelResponse) IccOpenLogicalChannelResponse.CREATOR.createFromParcel(_reply);
                    } else {
                        iccOpenLogicalChannelResponse = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return iccOpenLogicalChannelResponse;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean iccCloseLogicalChannelBySlot(int slotIndex, int channel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    _data.writeInt(channel);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(75, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().iccCloseLogicalChannelBySlot(slotIndex, channel);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean iccCloseLogicalChannel(int subId, int channel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(channel);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(76, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().iccCloseLogicalChannel(subId, channel);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String iccTransmitApduLogicalChannelBySlot(int slotIndex, int channel, int cla, int instruction, int p1, int p2, int p3, String data) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                int i3;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(slotIndex);
                    } catch (Throwable th2) {
                        th = th2;
                        i = channel;
                        i2 = cla;
                        i3 = instruction;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(channel);
                    } catch (Throwable th3) {
                        th = th3;
                        i2 = cla;
                        i3 = instruction;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(cla);
                        try {
                            _data.writeInt(instruction);
                            _data.writeInt(p1);
                            _data.writeInt(p2);
                            _data.writeInt(p3);
                            _data.writeString(data);
                            if (this.mRemote.transact(77, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                String _result = _reply.readString();
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            String iccTransmitApduLogicalChannelBySlot = Stub.getDefaultImpl().iccTransmitApduLogicalChannelBySlot(slotIndex, channel, cla, instruction, p1, p2, p3, data);
                            _reply.recycle();
                            _data.recycle();
                            return iccTransmitApduLogicalChannelBySlot;
                        } catch (Throwable th4) {
                            th = th4;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        i3 = instruction;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    int i4 = slotIndex;
                    i = channel;
                    i2 = cla;
                    i3 = instruction;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public String iccTransmitApduLogicalChannel(int subId, int channel, int cla, int instruction, int p1, int p2, int p3, String data) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                int i3;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(subId);
                    } catch (Throwable th2) {
                        th = th2;
                        i = channel;
                        i2 = cla;
                        i3 = instruction;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(channel);
                    } catch (Throwable th3) {
                        th = th3;
                        i2 = cla;
                        i3 = instruction;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(cla);
                        try {
                            _data.writeInt(instruction);
                            _data.writeInt(p1);
                            _data.writeInt(p2);
                            _data.writeInt(p3);
                            _data.writeString(data);
                            if (this.mRemote.transact(78, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                String _result = _reply.readString();
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            String iccTransmitApduLogicalChannel = Stub.getDefaultImpl().iccTransmitApduLogicalChannel(subId, channel, cla, instruction, p1, p2, p3, data);
                            _reply.recycle();
                            _data.recycle();
                            return iccTransmitApduLogicalChannel;
                        } catch (Throwable th4) {
                            th = th4;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        i3 = instruction;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    int i4 = subId;
                    i = channel;
                    i2 = cla;
                    i3 = instruction;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public String iccTransmitApduBasicChannelBySlot(int slotIndex, String callingPackage, int cla, int instruction, int p1, int p2, int p3, String data) throws RemoteException {
                Throwable th;
                String str;
                int i;
                int i2;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(slotIndex);
                    } catch (Throwable th2) {
                        th = th2;
                        str = callingPackage;
                        i = cla;
                        i2 = instruction;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(callingPackage);
                    } catch (Throwable th3) {
                        th = th3;
                        i = cla;
                        i2 = instruction;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(cla);
                        try {
                            _data.writeInt(instruction);
                            _data.writeInt(p1);
                            _data.writeInt(p2);
                            _data.writeInt(p3);
                            _data.writeString(data);
                            if (this.mRemote.transact(79, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                String _result = _reply.readString();
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            String iccTransmitApduBasicChannelBySlot = Stub.getDefaultImpl().iccTransmitApduBasicChannelBySlot(slotIndex, callingPackage, cla, instruction, p1, p2, p3, data);
                            _reply.recycle();
                            _data.recycle();
                            return iccTransmitApduBasicChannelBySlot;
                        } catch (Throwable th4) {
                            th = th4;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        i2 = instruction;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    int i3 = slotIndex;
                    str = callingPackage;
                    i = cla;
                    i2 = instruction;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public String iccTransmitApduBasicChannel(int subId, String callingPackage, int cla, int instruction, int p1, int p2, int p3, String data) throws RemoteException {
                Throwable th;
                String str;
                int i;
                int i2;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(subId);
                    } catch (Throwable th2) {
                        th = th2;
                        str = callingPackage;
                        i = cla;
                        i2 = instruction;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(callingPackage);
                    } catch (Throwable th3) {
                        th = th3;
                        i = cla;
                        i2 = instruction;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(cla);
                        try {
                            _data.writeInt(instruction);
                            _data.writeInt(p1);
                            _data.writeInt(p2);
                            _data.writeInt(p3);
                            _data.writeString(data);
                            if (this.mRemote.transact(80, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                String _result = _reply.readString();
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            String iccTransmitApduBasicChannel = Stub.getDefaultImpl().iccTransmitApduBasicChannel(subId, callingPackage, cla, instruction, p1, p2, p3, data);
                            _reply.recycle();
                            _data.recycle();
                            return iccTransmitApduBasicChannel;
                        } catch (Throwable th4) {
                            th = th4;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        i2 = instruction;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    int i3 = subId;
                    str = callingPackage;
                    i = cla;
                    i2 = instruction;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public byte[] iccExchangeSimIO(int subId, int fileID, int command, int p1, int p2, int p3, String filePath) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                int i3;
                int i4;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(subId);
                    } catch (Throwable th2) {
                        th = th2;
                        i = fileID;
                        i2 = command;
                        i3 = p1;
                        i4 = p2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(fileID);
                    } catch (Throwable th3) {
                        th = th3;
                        i2 = command;
                        i3 = p1;
                        i4 = p2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(command);
                        try {
                            _data.writeInt(p1);
                        } catch (Throwable th4) {
                            th = th4;
                            i4 = p2;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeInt(p2);
                            _data.writeInt(p3);
                            _data.writeString(filePath);
                            if (this.mRemote.transact(81, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                byte[] _result = _reply.createByteArray();
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            byte[] iccExchangeSimIO = Stub.getDefaultImpl().iccExchangeSimIO(subId, fileID, command, p1, p2, p3, filePath);
                            _reply.recycle();
                            _data.recycle();
                            return iccExchangeSimIO;
                        } catch (Throwable th5) {
                            th = th5;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        i3 = p1;
                        i4 = p2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    int i5 = subId;
                    i = fileID;
                    i2 = command;
                    i3 = p1;
                    i4 = p2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public String sendEnvelopeWithStatus(int subId, String content) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(content);
                    String str = 82;
                    if (!this.mRemote.transact(82, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().sendEnvelopeWithStatus(subId, content);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String nvReadItem(int itemID) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(itemID);
                    String str = 83;
                    if (!this.mRemote.transact(83, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().nvReadItem(itemID);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean nvWriteItem(int itemID, String itemValue) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(itemID);
                    _data.writeString(itemValue);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(84, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().nvWriteItem(itemID, itemValue);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean nvWriteCdmaPrl(byte[] preferredRoamingList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(preferredRoamingList);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(85, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().nvWriteCdmaPrl(preferredRoamingList);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean resetModemConfig(int slotIndex) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(86, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().resetModemConfig(slotIndex);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean rebootModem(int slotIndex) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(87, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().rebootModem(slotIndex);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCalculatedPreferredNetworkType(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    int i = 88;
                    if (!this.mRemote.transact(88, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getCalculatedPreferredNetworkType(callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPreferredNetworkType(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    int i = 89;
                    if (!this.mRemote.transact(89, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getPreferredNetworkType(subId);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getTetherApnRequiredForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(90, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().getTetherApnRequiredForSubscriber(subId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableIms(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    if (this.mRemote.transact(91, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().enableIms(slotId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disableIms(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    if (this.mRemote.transact(92, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().disableIms(slotId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IImsMmTelFeature getMmTelFeatureAndListen(int slotId, IImsServiceFeatureCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    IImsMmTelFeature iImsMmTelFeature = 93;
                    if (!this.mRemote.transact(93, _data, _reply, 0)) {
                        iImsMmTelFeature = Stub.getDefaultImpl();
                        if (iImsMmTelFeature != 0) {
                            iImsMmTelFeature = Stub.getDefaultImpl().getMmTelFeatureAndListen(slotId, callback);
                            return iImsMmTelFeature;
                        }
                    }
                    _reply.readException();
                    iImsMmTelFeature = android.telephony.ims.aidl.IImsMmTelFeature.Stub.asInterface(_reply.readStrongBinder());
                    IImsMmTelFeature _result = iImsMmTelFeature;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IImsRcsFeature getRcsFeatureAndListen(int slotId, IImsServiceFeatureCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    IImsRcsFeature iImsRcsFeature = 94;
                    if (!this.mRemote.transact(94, _data, _reply, 0)) {
                        iImsRcsFeature = Stub.getDefaultImpl();
                        if (iImsRcsFeature != 0) {
                            iImsRcsFeature = Stub.getDefaultImpl().getRcsFeatureAndListen(slotId, callback);
                            return iImsRcsFeature;
                        }
                    }
                    _reply.readException();
                    iImsRcsFeature = android.telephony.ims.aidl.IImsRcsFeature.Stub.asInterface(_reply.readStrongBinder());
                    IImsRcsFeature _result = iImsRcsFeature;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IImsRegistration getImsRegistration(int slotId, int feature) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(feature);
                    IImsRegistration iImsRegistration = 95;
                    if (!this.mRemote.transact(95, _data, _reply, 0)) {
                        iImsRegistration = Stub.getDefaultImpl();
                        if (iImsRegistration != 0) {
                            iImsRegistration = Stub.getDefaultImpl().getImsRegistration(slotId, feature);
                            return iImsRegistration;
                        }
                    }
                    _reply.readException();
                    iImsRegistration = android.telephony.ims.aidl.IImsRegistration.Stub.asInterface(_reply.readStrongBinder());
                    IImsRegistration _result = iImsRegistration;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IImsConfig getImsConfig(int slotId, int feature) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(feature);
                    IImsConfig iImsConfig = 96;
                    if (!this.mRemote.transact(96, _data, _reply, 0)) {
                        iImsConfig = Stub.getDefaultImpl();
                        if (iImsConfig != 0) {
                            iImsConfig = Stub.getDefaultImpl().getImsConfig(slotId, feature);
                            return iImsConfig;
                        }
                    }
                    _reply.readException();
                    iImsConfig = android.telephony.ims.aidl.IImsConfig.Stub.asInterface(_reply.readStrongBinder());
                    IImsConfig _result = iImsConfig;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setImsService(int slotId, boolean isCarrierImsService, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    boolean _result = true;
                    _data.writeInt(isCarrierImsService ? 1 : 0);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(97, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setImsService(slotId, isCarrierImsService, packageName);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getImsService(int slotId, boolean isCarrierImsService) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(isCarrierImsService ? 1 : 0);
                    String str = this.mRemote;
                    if (!str.transact(98, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != null) {
                            str = Stub.getDefaultImpl().getImsService(slotId, isCarrierImsService);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setNetworkSelectionModeAutomatic(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    if (this.mRemote.transact(99, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setNetworkSelectionModeAutomatic(subId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public CellNetworkScanResult getCellNetworkScanResults(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    CellNetworkScanResult cellNetworkScanResult = 100;
                    if (!this.mRemote.transact(100, _data, _reply, 0)) {
                        cellNetworkScanResult = Stub.getDefaultImpl();
                        if (cellNetworkScanResult != 0) {
                            cellNetworkScanResult = Stub.getDefaultImpl().getCellNetworkScanResults(subId, callingPackage);
                            return cellNetworkScanResult;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        cellNetworkScanResult = (CellNetworkScanResult) CellNetworkScanResult.CREATOR.createFromParcel(_reply);
                    } else {
                        cellNetworkScanResult = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return cellNetworkScanResult;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int requestNetworkScan(int subId, NetworkScanRequest request, Messenger messenger, IBinder binder, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    int i = 0;
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (messenger != null) {
                        _data.writeInt(1);
                        messenger.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(binder);
                    _data.writeString(callingPackage);
                    if (!this.mRemote.transact(101, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().requestNetworkScan(subId, request, messenger, binder, callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopNetworkScan(int subId, int scanId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(scanId);
                    if (this.mRemote.transact(102, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().stopNetworkScan(subId, scanId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setNetworkSelectionModeManual(int subId, OperatorInfo operatorInfo, boolean persisSelection) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _result = true;
                    if (operatorInfo != null) {
                        _data.writeInt(1);
                        operatorInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(persisSelection ? 1 : 0);
                    if (this.mRemote.transact(103, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setNetworkSelectionModeManual(subId, operatorInfo, persisSelection);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setPreferredNetworkType(int subId, int networkType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(networkType);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(104, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().setPreferredNetworkType(subId, networkType);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setUserDataEnabled(int subId, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(enable ? 1 : 0);
                    if (this.mRemote.transact(105, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setUserDataEnabled(subId, enable);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getDataEnabled(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(106, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().getDataEnabled(subId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isUserDataEnabled(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(107, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isUserDataEnabled(subId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isDataEnabled(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(108, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isDataEnabled(subId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isManualNetworkSelectionAllowed(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(109, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isManualNetworkSelectionAllowed(subId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getPcscfAddress(String apnType, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(apnType);
                    _data.writeString(callingPackage);
                    String[] strArr = 110;
                    if (!this.mRemote.transact(110, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getPcscfAddress(apnType, callingPackage);
                            return strArr;
                        }
                    }
                    _reply.readException();
                    strArr = _reply.createStringArray();
                    String[] _result = strArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setImsRegistrationState(boolean registered) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(registered ? 1 : 0);
                    if (this.mRemote.transact(111, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setImsRegistrationState(registered);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getCdmaMdn(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    String str = 112;
                    if (!this.mRemote.transact(112, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getCdmaMdn(subId);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getCdmaMin(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    String str = 113;
                    if (!this.mRemote.transact(113, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getCdmaMin(subId);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestNumberVerification(PhoneNumberRange range, long timeoutMillis, INumberVerificationCallback callback, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (range != null) {
                        _data.writeInt(1);
                        range.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeLong(timeoutMillis);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(114, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().requestNumberVerification(range, timeoutMillis, callback, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCarrierPrivilegeStatus(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    int i = 115;
                    if (!this.mRemote.transact(115, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getCarrierPrivilegeStatus(subId);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCarrierPrivilegeStatusForUid(int subId, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(uid);
                    int i = 116;
                    if (!this.mRemote.transact(116, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getCarrierPrivilegeStatusForUid(subId, uid);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int checkCarrierPrivilegesForPackage(int subId, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(pkgName);
                    int i = 117;
                    if (!this.mRemote.transact(117, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().checkCarrierPrivilegesForPackage(subId, pkgName);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int checkCarrierPrivilegesForPackageAnyPhone(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    int i = 118;
                    if (!this.mRemote.transact(118, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().checkCarrierPrivilegesForPackageAnyPhone(pkgName);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getCarrierPackageNamesForIntentAndPhone(Intent intent, int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(phoneId);
                    List<String> list = this.mRemote;
                    if (!list.transact(119, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != null) {
                            list = Stub.getDefaultImpl().getCarrierPackageNamesForIntentAndPhone(intent, phoneId);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createStringArrayList();
                    List<String> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setLine1NumberForDisplayForSubscriber(int subId, String alphaTag, String number) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(alphaTag);
                    _data.writeString(number);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(120, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().setLine1NumberForDisplayForSubscriber(subId, alphaTag, number);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getLine1NumberForDisplay(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    String str = 121;
                    if (!this.mRemote.transact(121, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getLine1NumberForDisplay(subId, callingPackage);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getLine1AlphaTagForDisplay(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    String str = 122;
                    if (!this.mRemote.transact(122, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getLine1AlphaTagForDisplay(subId, callingPackage);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getMergedSubscriberIds(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    String[] strArr = 123;
                    if (!this.mRemote.transact(123, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getMergedSubscriberIds(subId, callingPackage);
                            return strArr;
                        }
                    }
                    _reply.readException();
                    strArr = _reply.createStringArray();
                    String[] _result = strArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setOperatorBrandOverride(int subId, String brand) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(brand);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(124, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().setOperatorBrandOverride(subId, brand);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setRoamingOverride(int subId, List<String> gsmRoamingList, List<String> gsmNonRoamingList, List<String> cdmaRoamingList, List<String> cdmaNonRoamingList) throws RemoteException {
                Throwable th;
                List<String> list;
                List<String> list2;
                List<String> list3;
                List<String> list4;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(subId);
                    } catch (Throwable th2) {
                        th = th2;
                        list = gsmRoamingList;
                        list2 = gsmNonRoamingList;
                        list3 = cdmaRoamingList;
                        list4 = cdmaNonRoamingList;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeStringList(gsmRoamingList);
                        try {
                            _data.writeStringList(gsmNonRoamingList);
                            try {
                                _data.writeStringList(cdmaRoamingList);
                            } catch (Throwable th3) {
                                th = th3;
                                list4 = cdmaNonRoamingList;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            list3 = cdmaRoamingList;
                            list4 = cdmaNonRoamingList;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        list2 = gsmNonRoamingList;
                        list3 = cdmaRoamingList;
                        list4 = cdmaNonRoamingList;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeStringList(cdmaNonRoamingList);
                        try {
                            boolean z = false;
                            if (this.mRemote.transact(125, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                if (_reply.readInt() != 0) {
                                    z = true;
                                }
                                boolean _result = z;
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            boolean roamingOverride = Stub.getDefaultImpl().setRoamingOverride(subId, gsmRoamingList, gsmNonRoamingList, cdmaRoamingList, cdmaNonRoamingList);
                            _reply.recycle();
                            _data.recycle();
                            return roamingOverride;
                        } catch (Throwable th6) {
                            th = th6;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th7) {
                        th = th7;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th8) {
                    th = th8;
                    int i = subId;
                    list = gsmRoamingList;
                    list2 = gsmNonRoamingList;
                    list3 = cdmaRoamingList;
                    list4 = cdmaNonRoamingList;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public int invokeOemRilRequestRaw(byte[] oemReq, byte[] oemResp) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(oemReq);
                    if (oemResp == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(oemResp.length);
                    }
                    int _result = 126;
                    if (!this.mRemote.transact(126, _data, _reply, 0)) {
                        _result = Stub.getDefaultImpl();
                        if (_result != 0) {
                            _result = Stub.getDefaultImpl().invokeOemRilRequestRaw(oemReq, oemResp);
                            return _result;
                        }
                    }
                    _reply.readException();
                    _result = _reply.readInt();
                    _reply.readByteArray(oemResp);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean needMobileRadioShutdown() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(127, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().needMobileRadioShutdown();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void shutdownMobileRadios() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(128, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().shutdownMobileRadios();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setRadioCapability(RadioAccessFamily[] rafs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedArray(rafs, 0);
                    if (this.mRemote.transact(129, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setRadioCapability(rafs);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getRadioAccessFamily(int phoneId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeString(callingPackage);
                    int i = 130;
                    if (!this.mRemote.transact(130, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getRadioAccessFamily(phoneId, callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableVideoCalling(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    if (this.mRemote.transact(131, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().enableVideoCalling(enable);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isVideoCallingEnabled(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(132, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isVideoCallingEnabled(callingPackage);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean canChangeDtmfToneLength(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(133, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().canChangeDtmfToneLength(subId, callingPackage);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isWorldPhone(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(134, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isWorldPhone(subId, callingPackage);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isTtyModeSupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(135, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isTtyModeSupported();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isRttSupported(int subscriptionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subscriptionId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(136, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isRttSupported(subscriptionId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isHearingAidCompatibilitySupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(137, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isHearingAidCompatibilitySupported();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isImsRegistered(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(138, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isImsRegistered(subId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isWifiCallingAvailable(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(139, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isWifiCallingAvailable(subId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isVideoTelephonyAvailable(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(140, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isVideoTelephonyAvailable(subId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getImsRegTechnologyForMmTel(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    int i = 141;
                    if (!this.mRemote.transact(141, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getImsRegTechnologyForMmTel(subId);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getDeviceId(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    String str = 142;
                    if (!this.mRemote.transact(142, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getDeviceId(callingPackage);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getImeiForSlot(int slotIndex, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    _data.writeString(callingPackage);
                    String str = 143;
                    if (!this.mRemote.transact(143, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getImeiForSlot(slotIndex, callingPackage);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getTypeAllocationCodeForSlot(int slotIndex) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    String str = 144;
                    if (!this.mRemote.transact(144, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getTypeAllocationCodeForSlot(slotIndex);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getMeidForSlot(int slotIndex, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    _data.writeString(callingPackage);
                    String str = 145;
                    if (!this.mRemote.transact(145, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getMeidForSlot(slotIndex, callingPackage);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getManufacturerCodeForSlot(int slotIndex) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    String str = 146;
                    if (!this.mRemote.transact(146, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getManufacturerCodeForSlot(slotIndex);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getDeviceSoftwareVersionForSlot(int slotIndex, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    _data.writeString(callingPackage);
                    String str = 147;
                    if (!this.mRemote.transact(147, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getDeviceSoftwareVersionForSlot(slotIndex, callingPackage);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getSubIdForPhoneAccount(PhoneAccount phoneAccount) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (phoneAccount != null) {
                        _data.writeInt(1);
                        phoneAccount.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(148, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getSubIdForPhoneAccount(phoneAccount);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public PhoneAccountHandle getPhoneAccountHandleForSubscriptionId(int subscriptionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subscriptionId);
                    PhoneAccountHandle phoneAccountHandle = 149;
                    if (!this.mRemote.transact(149, _data, _reply, 0)) {
                        phoneAccountHandle = Stub.getDefaultImpl();
                        if (phoneAccountHandle != 0) {
                            phoneAccountHandle = Stub.getDefaultImpl().getPhoneAccountHandleForSubscriptionId(subscriptionId);
                            return phoneAccountHandle;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        phoneAccountHandle = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(_reply);
                    } else {
                        phoneAccountHandle = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return phoneAccountHandle;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void factoryReset(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    if (this.mRemote.transact(150, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().factoryReset(subId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getSimLocaleForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    String str = 151;
                    if (!this.mRemote.transact(151, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getSimLocaleForSubscriber(subId);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestModemActivityInfo(ResultReceiver result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (result != null) {
                        _data.writeInt(1);
                        result.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(152, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().requestModemActivityInfo(result);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public ServiceState getServiceStateForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    ServiceState serviceState = 153;
                    if (!this.mRemote.transact(153, _data, _reply, 0)) {
                        serviceState = Stub.getDefaultImpl();
                        if (serviceState != 0) {
                            serviceState = Stub.getDefaultImpl().getServiceStateForSubscriber(subId, callingPackage);
                            return serviceState;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        serviceState = (ServiceState) ServiceState.CREATOR.createFromParcel(_reply);
                    } else {
                        serviceState = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return serviceState;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Uri getVoicemailRingtoneUri(PhoneAccountHandle accountHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (accountHandle != null) {
                        _data.writeInt(1);
                        accountHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    Uri uri = this.mRemote;
                    if (!uri.transact(154, _data, _reply, 0)) {
                        uri = Stub.getDefaultImpl();
                        if (uri != null) {
                            uri = Stub.getDefaultImpl().getVoicemailRingtoneUri(accountHandle);
                            return uri;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        uri = (Uri) Uri.CREATOR.createFromParcel(_reply);
                    } else {
                        uri = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return uri;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setVoicemailRingtoneUri(String callingPackage, PhoneAccountHandle phoneAccountHandle, Uri uri) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    if (phoneAccountHandle != null) {
                        _data.writeInt(1);
                        phoneAccountHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(155, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setVoicemailRingtoneUri(callingPackage, phoneAccountHandle, uri);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isVoicemailVibrationEnabled(PhoneAccountHandle accountHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (accountHandle != null) {
                        _data.writeInt(1);
                        accountHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(156, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isVoicemailVibrationEnabled(accountHandle);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setVoicemailVibrationEnabled(String callingPackage, PhoneAccountHandle phoneAccountHandle, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    int i = 1;
                    if (phoneAccountHandle != null) {
                        _data.writeInt(1);
                        phoneAccountHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!enabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(157, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setVoicemailVibrationEnabled(callingPackage, phoneAccountHandle, enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getPackagesWithCarrierPrivileges(int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    List<String> list = 158;
                    if (!this.mRemote.transact(158, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getPackagesWithCarrierPrivileges(phoneId);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createStringArrayList();
                    List<String> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getPackagesWithCarrierPrivilegesForAllPhones() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<String> list = 159;
                    if (!this.mRemote.transact(159, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getPackagesWithCarrierPrivilegesForAllPhones();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createStringArrayList();
                    List<String> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getAidForAppType(int subId, int appType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(appType);
                    String str = 160;
                    if (!this.mRemote.transact(160, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getAidForAppType(subId, appType);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getEsn(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    String str = 161;
                    if (!this.mRemote.transact(161, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getEsn(subId);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getCdmaPrlVersion(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    String str = 162;
                    if (!this.mRemote.transact(162, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getCdmaPrlVersion(subId);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<TelephonyHistogram> getTelephonyHistograms() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<TelephonyHistogram> list = 163;
                    if (!this.mRemote.transact(163, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getTelephonyHistograms();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(TelephonyHistogram.CREATOR);
                    List<TelephonyHistogram> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setAllowedCarriers(CarrierRestrictionRules carrierRestrictionRules) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (carrierRestrictionRules != null) {
                        _data.writeInt(1);
                        carrierRestrictionRules.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(164, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().setAllowedCarriers(carrierRestrictionRules);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public CarrierRestrictionRules getAllowedCarriers() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    CarrierRestrictionRules carrierRestrictionRules = 165;
                    if (!this.mRemote.transact(165, _data, _reply, 0)) {
                        carrierRestrictionRules = Stub.getDefaultImpl();
                        if (carrierRestrictionRules != 0) {
                            carrierRestrictionRules = Stub.getDefaultImpl().getAllowedCarriers();
                            return carrierRestrictionRules;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        carrierRestrictionRules = (CarrierRestrictionRules) CarrierRestrictionRules.CREATOR.createFromParcel(_reply);
                    } else {
                        carrierRestrictionRules = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return carrierRestrictionRules;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getSubscriptionCarrierId(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    int i = 166;
                    if (!this.mRemote.transact(166, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getSubscriptionCarrierId(subId);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getSubscriptionCarrierName(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    String str = 167;
                    if (!this.mRemote.transact(167, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getSubscriptionCarrierName(subId);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getSubscriptionSpecificCarrierId(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    int i = 168;
                    if (!this.mRemote.transact(168, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getSubscriptionSpecificCarrierId(subId);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getSubscriptionSpecificCarrierName(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    String str = 169;
                    if (!this.mRemote.transact(169, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getSubscriptionSpecificCarrierName(subId);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCarrierIdFromMccMnc(int slotIndex, String mccmnc, boolean isSubscriptionMccMnc) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    _data.writeString(mccmnc);
                    _data.writeInt(isSubscriptionMccMnc ? 1 : 0);
                    int i = this.mRemote;
                    if (!i.transact(170, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getCarrierIdFromMccMnc(slotIndex, mccmnc, isSubscriptionMccMnc);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void carrierActionSetMeteredApnsEnabled(int subId, boolean visible) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(visible ? 1 : 0);
                    if (this.mRemote.transact(171, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().carrierActionSetMeteredApnsEnabled(subId, visible);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void carrierActionSetRadioEnabled(int subId, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(enabled ? 1 : 0);
                    if (this.mRemote.transact(172, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().carrierActionSetRadioEnabled(subId, enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void carrierActionReportDefaultNetworkStatus(int subId, boolean report) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(report ? 1 : 0);
                    if (this.mRemote.transact(173, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().carrierActionReportDefaultNetworkStatus(subId, report);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void carrierActionResetAll(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    if (this.mRemote.transact(174, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().carrierActionResetAll(subId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public NetworkStats getVtDataUsage(int subId, boolean perUidStats) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(perUidStats ? 1 : 0);
                    NetworkStats networkStats = this.mRemote;
                    if (!networkStats.transact(175, _data, _reply, 0)) {
                        networkStats = Stub.getDefaultImpl();
                        if (networkStats != null) {
                            networkStats = Stub.getDefaultImpl().getVtDataUsage(subId, perUidStats);
                            return networkStats;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        networkStats = (NetworkStats) NetworkStats.CREATOR.createFromParcel(_reply);
                    } else {
                        networkStats = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return networkStats;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPolicyDataEnabled(boolean enabled, int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    _data.writeInt(subId);
                    if (this.mRemote.transact(176, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPolicyDataEnabled(enabled, subId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<ClientRequestStats> getClientRequestStats(String callingPackage, int subid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(subid);
                    List<ClientRequestStats> list = 177;
                    if (!this.mRemote.transact(177, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getClientRequestStats(callingPackage, subid);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(ClientRequestStats.CREATOR);
                    List<ClientRequestStats> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setSimPowerStateForSlot(int slotIndex, int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    _data.writeInt(state);
                    if (this.mRemote.transact(178, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setSimPowerStateForSlot(slotIndex, state);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getForbiddenPlmns(int subId, int appType, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(appType);
                    _data.writeString(callingPackage);
                    String[] strArr = 179;
                    if (!this.mRemote.transact(179, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getForbiddenPlmns(subId, appType, callingPackage);
                            return strArr;
                        }
                    }
                    _reply.readException();
                    strArr = _reply.createStringArray();
                    String[] _result = strArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getEmergencyCallbackMode(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(180, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().getEmergencyCallbackMode(subId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public SignalStrength getSignalStrength(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    SignalStrength signalStrength = 181;
                    if (!this.mRemote.transact(181, _data, _reply, 0)) {
                        signalStrength = Stub.getDefaultImpl();
                        if (signalStrength != 0) {
                            signalStrength = Stub.getDefaultImpl().getSignalStrength(subId);
                            return signalStrength;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        signalStrength = (SignalStrength) SignalStrength.CREATOR.createFromParcel(_reply);
                    } else {
                        signalStrength = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return signalStrength;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCardIdForDefaultEuicc(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    int i = 182;
                    if (!this.mRemote.transact(182, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getCardIdForDefaultEuicc(subId, callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<UiccCardInfo> getUiccCardsInfo(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    List<UiccCardInfo> list = 183;
                    if (!this.mRemote.transact(183, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getUiccCardsInfo(callingPackage);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(UiccCardInfo.CREATOR);
                    List<UiccCardInfo> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public UiccSlotInfo[] getUiccSlotsInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    UiccSlotInfo[] uiccSlotInfoArr = 184;
                    if (!this.mRemote.transact(184, _data, _reply, 0)) {
                        uiccSlotInfoArr = Stub.getDefaultImpl();
                        if (uiccSlotInfoArr != 0) {
                            uiccSlotInfoArr = Stub.getDefaultImpl().getUiccSlotsInfo();
                            return uiccSlotInfoArr;
                        }
                    }
                    _reply.readException();
                    UiccSlotInfo[] _result = (UiccSlotInfo[]) _reply.createTypedArray(UiccSlotInfo.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean switchSlots(int[] physicalSlots) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(physicalSlots);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(185, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().switchSlots(physicalSlots);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setRadioIndicationUpdateMode(int subId, int filters, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(filters);
                    _data.writeInt(mode);
                    if (this.mRemote.transact(186, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setRadioIndicationUpdateMode(subId, filters, mode);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isDataRoamingEnabled(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(187, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isDataRoamingEnabled(subId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setDataRoamingEnabled(int subId, boolean isEnabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(isEnabled ? 1 : 0);
                    if (this.mRemote.transact(188, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setDataRoamingEnabled(subId, isEnabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCdmaRoamingMode(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    int i = 189;
                    if (!this.mRemote.transact(189, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getCdmaRoamingMode(subId);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setCdmaRoamingMode(int subId, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(mode);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(190, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().setCdmaRoamingMode(subId, mode);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setCdmaSubscriptionMode(int subId, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(mode);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(191, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().setCdmaSubscriptionMode(subId, mode);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setCarrierTestOverride(int subId, String mccmnc, String imsi, String iccid, String gid1, String gid2, String plmn, String spn, String carrierPrivilegeRules, String apn) throws RemoteException {
                Throwable th;
                String str;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(subId);
                    } catch (Throwable th2) {
                        th = th2;
                        str = mccmnc;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(mccmnc);
                        _data.writeString(imsi);
                        _data.writeString(iccid);
                        _data.writeString(gid1);
                        _data.writeString(gid2);
                        _data.writeString(plmn);
                        _data.writeString(spn);
                        _data.writeString(carrierPrivilegeRules);
                        _data.writeString(apn);
                        if (this.mRemote.transact(192, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            _reply.recycle();
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().setCarrierTestOverride(subId, mccmnc, imsi, iccid, gid1, gid2, plmn, spn, carrierPrivilegeRules, apn);
                        _reply.recycle();
                        _data.recycle();
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    int i = subId;
                    str = mccmnc;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public int getCarrierIdListVersion(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    int i = 193;
                    if (!this.mRemote.transact(193, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getCarrierIdListVersion(subId);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void refreshUiccProfile(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    if (this.mRemote.transact(194, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().refreshUiccProfile(subId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getNumberOfModemsWithSimultaneousDataConnections(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    int i = 195;
                    if (!this.mRemote.transact(195, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getNumberOfModemsWithSimultaneousDataConnections(subId, callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getNetworkSelectionMode(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    int i = 196;
                    if (!this.mRemote.transact(196, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getNetworkSelectionMode(subId);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isInEmergencySmsMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(197, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isInEmergencySmsMode();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getSmsApps(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    String[] strArr = 198;
                    if (!this.mRemote.transact(198, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getSmsApps(userId);
                            return strArr;
                        }
                    }
                    _reply.readException();
                    strArr = _reply.createStringArray();
                    String[] _result = strArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getDefaultSmsApp(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    String str = 199;
                    if (!this.mRemote.transact(199, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getDefaultSmsApp(userId);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setDefaultSmsApp(int userId, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(200, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setDefaultSmsApp(userId, packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getRadioPowerState(int slotIndex, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    _data.writeString(callingPackage);
                    int i = 201;
                    if (!this.mRemote.transact(201, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getRadioPowerState(slotIndex, callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerImsRegistrationCallback(int subId, IImsRegistrationCallback c) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeStrongBinder(c != null ? c.asBinder() : null);
                    if (this.mRemote.transact(202, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerImsRegistrationCallback(subId, c);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterImsRegistrationCallback(int subId, IImsRegistrationCallback c) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeStrongBinder(c != null ? c.asBinder() : null);
                    if (this.mRemote.transact(203, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterImsRegistrationCallback(subId, c);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerMmTelCapabilityCallback(int subId, IImsCapabilityCallback c) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeStrongBinder(c != null ? c.asBinder() : null);
                    if (this.mRemote.transact(204, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerMmTelCapabilityCallback(subId, c);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterMmTelCapabilityCallback(int subId, IImsCapabilityCallback c) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeStrongBinder(c != null ? c.asBinder() : null);
                    if (this.mRemote.transact(205, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterMmTelCapabilityCallback(subId, c);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isCapable(int subId, int capability, int regTech) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(capability);
                    _data.writeInt(regTech);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(206, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isCapable(subId, capability, regTech);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isAvailable(int subId, int capability, int regTech) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(capability);
                    _data.writeInt(regTech);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(207, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isAvailable(subId, capability, regTech);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isAdvancedCallingSettingEnabled(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(208, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isAdvancedCallingSettingEnabled(subId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAdvancedCallingSettingEnabled(int subId, boolean isEnabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(isEnabled ? 1 : 0);
                    if (this.mRemote.transact(209, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAdvancedCallingSettingEnabled(subId, isEnabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isVtSettingEnabled(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(210, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isVtSettingEnabled(subId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setVtSettingEnabled(int subId, boolean isEnabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(isEnabled ? 1 : 0);
                    if (this.mRemote.transact(211, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setVtSettingEnabled(subId, isEnabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isVoWiFiSettingEnabled(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(212, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isVoWiFiSettingEnabled(subId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setVoWiFiSettingEnabled(int subId, boolean isEnabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(isEnabled ? 1 : 0);
                    if (this.mRemote.transact(213, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setVoWiFiSettingEnabled(subId, isEnabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isVoWiFiRoamingSettingEnabled(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(214, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isVoWiFiRoamingSettingEnabled(subId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setVoWiFiRoamingSettingEnabled(int subId, boolean isEnabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(isEnabled ? 1 : 0);
                    if (this.mRemote.transact(215, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setVoWiFiRoamingSettingEnabled(subId, isEnabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setVoWiFiNonPersistent(int subId, boolean isCapable, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(isCapable ? 1 : 0);
                    _data.writeInt(mode);
                    if (this.mRemote.transact(216, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setVoWiFiNonPersistent(subId, isCapable, mode);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getVoWiFiModeSetting(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    int i = 217;
                    if (!this.mRemote.transact(217, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getVoWiFiModeSetting(subId);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setVoWiFiModeSetting(int subId, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(mode);
                    if (this.mRemote.transact(218, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setVoWiFiModeSetting(subId, mode);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getVoWiFiRoamingModeSetting(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    int i = 219;
                    if (!this.mRemote.transact(219, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getVoWiFiRoamingModeSetting(subId);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setVoWiFiRoamingModeSetting(int subId, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(mode);
                    if (this.mRemote.transact(220, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setVoWiFiRoamingModeSetting(subId, mode);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setRttCapabilitySetting(int subId, boolean isEnabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(isEnabled ? 1 : 0);
                    if (this.mRemote.transact(221, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setRttCapabilitySetting(subId, isEnabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isTtyOverVolteEnabled(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(222, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isTtyOverVolteEnabled(subId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Map getEmergencyNumberList(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    Map map = 223;
                    if (!this.mRemote.transact(223, _data, _reply, 0)) {
                        map = Stub.getDefaultImpl();
                        if (map != 0) {
                            map = Stub.getDefaultImpl().getEmergencyNumberList(callingPackage);
                            return map;
                        }
                    }
                    _reply.readException();
                    map = getClass().getClassLoader();
                    HashMap _result = _reply.readHashMap(map);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isEmergencyNumber(String number, boolean exactMatch) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(number);
                    boolean _result = true;
                    _data.writeInt(exactMatch ? 1 : 0);
                    if (this.mRemote.transact(224, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isEmergencyNumber(number, exactMatch);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getCertsFromCarrierPrivilegeAccessRules(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    List<String> list = 225;
                    if (!this.mRemote.transact(225, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getCertsFromCarrierPrivilegeAccessRules(subId);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createStringArrayList();
                    List<String> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerImsProvisioningChangedCallback(int subId, IImsConfigCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(226, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerImsProvisioningChangedCallback(subId, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterImsProvisioningChangedCallback(int subId, IImsConfigCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(227, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterImsProvisioningChangedCallback(subId, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setImsProvisioningStatusForCapability(int subId, int capability, int tech, boolean isProvisioned) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(capability);
                    _data.writeInt(tech);
                    _data.writeInt(isProvisioned ? 1 : 0);
                    if (this.mRemote.transact(228, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setImsProvisioningStatusForCapability(subId, capability, tech, isProvisioned);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getImsProvisioningStatusForCapability(int subId, int capability, int tech) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(capability);
                    _data.writeInt(tech);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(229, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().getImsProvisioningStatusForCapability(subId, capability, tech);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isMmTelCapabilityProvisionedInCache(int subId, int capability, int tech) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(capability);
                    _data.writeInt(tech);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(230, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isMmTelCapabilityProvisionedInCache(subId, capability, tech);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cacheMmTelCapabilityProvisioning(int subId, int capability, int tech, boolean isProvisioned) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(capability);
                    _data.writeInt(tech);
                    _data.writeInt(isProvisioned ? 1 : 0);
                    if (this.mRemote.transact(231, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().cacheMmTelCapabilityProvisioning(subId, capability, tech, isProvisioned);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getImsProvisioningInt(int subId, int key) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(key);
                    int i = 232;
                    if (!this.mRemote.transact(232, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getImsProvisioningInt(subId, key);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getImsProvisioningString(int subId, int key) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(key);
                    String str = 233;
                    if (!this.mRemote.transact(233, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getImsProvisioningString(subId, key);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setImsProvisioningInt(int subId, int key, int value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(key);
                    _data.writeInt(value);
                    int i = 234;
                    if (!this.mRemote.transact(234, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().setImsProvisioningInt(subId, key, value);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setImsProvisioningString(int subId, int key, String value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(key);
                    _data.writeString(value);
                    int i = 235;
                    if (!this.mRemote.transact(235, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().setImsProvisioningString(subId, key, value);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateEmergencyNumberListTestMode(int action, EmergencyNumber num) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(action);
                    if (num != null) {
                        _data.writeInt(1);
                        num.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(236, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateEmergencyNumberListTestMode(action, num);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getEmergencyNumberListTestMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<String> list = 237;
                    if (!this.mRemote.transact(237, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getEmergencyNumberListTestMode();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createStringArrayList();
                    List<String> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean enableModemForSlot(int slotIndex, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    boolean _result = true;
                    _data.writeInt(enable ? 1 : 0);
                    if (this.mRemote.transact(238, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().enableModemForSlot(slotIndex, enable);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setMultiSimCarrierRestriction(boolean isMultiSimCarrierRestricted) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isMultiSimCarrierRestricted ? 1 : 0);
                    if (this.mRemote.transact(239, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setMultiSimCarrierRestriction(isMultiSimCarrierRestricted);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int isMultiSimSupported(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    int i = 240;
                    if (!this.mRemote.transact(240, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().isMultiSimSupported(callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void switchMultiSimConfig(int numOfSims) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(numOfSims);
                    if (this.mRemote.transact(241, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().switchMultiSimConfig(numOfSims);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean doesSwitchMultiSimConfigTriggerReboot(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(242, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().doesSwitchMultiSimConfigTriggerReboot(subId, callingPackage);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] getSlotsMapping() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int[] iArr = 243;
                    if (!this.mRemote.transact(243, _data, _reply, 0)) {
                        iArr = Stub.getDefaultImpl();
                        if (iArr != 0) {
                            iArr = Stub.getDefaultImpl().getSlotsMapping();
                            return iArr;
                        }
                    }
                    _reply.readException();
                    iArr = _reply.createIntArray();
                    int[] _result = iArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getRadioHalVersion() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 244;
                    if (!this.mRemote.transact(244, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getRadioHalVersion();
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isModemEnabledForSlot(int slotIndex, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    _data.writeString(callingPackage);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(245, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isModemEnabledForSlot(slotIndex, callingPackage);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isDataEnabledForApn(int apnType, int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(apnType);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(246, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isDataEnabledForApn(apnType, subId, callingPackage);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isApnMetered(int apnType, int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(apnType);
                    _data.writeInt(subId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(247, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isApnMetered(apnType, subId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enqueueSmsPickResult(String callingPackage, IIntegerConsumer subIdResult) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeStrongBinder(subIdResult != null ? subIdResult.asBinder() : null);
                    if (this.mRemote.transact(248, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().enqueueSmsPickResult(callingPackage, subIdResult);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public String getMmsUserAgent(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    String str = 249;
                    if (!this.mRemote.transact(249, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getMmsUserAgent(subId);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getMmsUAProfUrl(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    String str = 250;
                    if (!this.mRemote.transact(250, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getMmsUAProfUrl(subId);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setDataAllowedDuringVoiceCall(int subId, boolean allow) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean _result = true;
                    _data.writeInt(allow ? 1 : 0);
                    if (this.mRemote.transact(251, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setDataAllowedDuringVoiceCall(subId, allow);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isDataAllowedInVoiceCall(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(252, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isDataAllowedInVoiceCall(subId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ITelephony asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ITelephony)) {
                return new Proxy(obj);
            }
            return (ITelephony) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "dial";
                case 2:
                    return "call";
                case 3:
                    return "isRadioOn";
                case 4:
                    return "isRadioOnForSubscriber";
                case 5:
                    return "supplyPin";
                case 6:
                    return "supplyPinForSubscriber";
                case 7:
                    return "supplyPuk";
                case 8:
                    return "supplyPukForSubscriber";
                case 9:
                    return "supplyPinReportResult";
                case 10:
                    return "supplyPinReportResultForSubscriber";
                case 11:
                    return "supplyPukReportResult";
                case 12:
                    return "supplyPukReportResultForSubscriber";
                case 13:
                    return "handlePinMmi";
                case 14:
                    return "handleUssdRequest";
                case 15:
                    return "handlePinMmiForSubscriber";
                case 16:
                    return "toggleRadioOnOff";
                case 17:
                    return "toggleRadioOnOffForSubscriber";
                case 18:
                    return "setRadio";
                case 19:
                    return "setRadioForSubscriber";
                case 20:
                    return "setRadioPower";
                case 21:
                    return "updateServiceLocation";
                case 22:
                    return "updateServiceLocationForSubscriber";
                case 23:
                    return "enableLocationUpdates";
                case 24:
                    return "enableLocationUpdatesForSubscriber";
                case 25:
                    return "disableLocationUpdates";
                case 26:
                    return "disableLocationUpdatesForSubscriber";
                case 27:
                    return "enableDataConnectivity";
                case 28:
                    return "disableDataConnectivity";
                case 29:
                    return "isDataConnectivityPossible";
                case 30:
                    return "getCellLocation";
                case 31:
                    return "getNetworkCountryIsoForPhone";
                case 32:
                    return "getNeighboringCellInfo";
                case 33:
                    return "getCallState";
                case 34:
                    return "getCallStateForSlot";
                case 35:
                    return "getDataActivity";
                case 36:
                    return "getDataState";
                case 37:
                    return "getActivePhoneType";
                case 38:
                    return "getActivePhoneTypeForSlot";
                case 39:
                    return "getCdmaEriIconIndex";
                case 40:
                    return "getCdmaEriIconIndexForSubscriber";
                case 41:
                    return "getCdmaEriIconMode";
                case 42:
                    return "getCdmaEriIconModeForSubscriber";
                case 43:
                    return "getCdmaEriText";
                case 44:
                    return "getCdmaEriTextForSubscriber";
                case 45:
                    return "needsOtaServiceProvisioning";
                case 46:
                    return "setVoiceMailNumber";
                case 47:
                    return "setVoiceActivationState";
                case 48:
                    return "setDataActivationState";
                case 49:
                    return "getVoiceActivationState";
                case 50:
                    return "getDataActivationState";
                case 51:
                    return "getVoiceMessageCountForSubscriber";
                case 52:
                    return "isConcurrentVoiceAndDataAllowed";
                case 53:
                    return "getVisualVoicemailSettings";
                case 54:
                    return "getVisualVoicemailPackageName";
                case 55:
                    return "enableVisualVoicemailSmsFilter";
                case 56:
                    return "disableVisualVoicemailSmsFilter";
                case 57:
                    return "getVisualVoicemailSmsFilterSettings";
                case 58:
                    return "getActiveVisualVoicemailSmsFilterSettings";
                case 59:
                    return "sendVisualVoicemailSmsForSubscriber";
                case 60:
                    return "sendDialerSpecialCode";
                case 61:
                    return "getNetworkTypeForSubscriber";
                case 62:
                    return "getDataNetworkType";
                case 63:
                    return "getDataNetworkTypeForSubscriber";
                case 64:
                    return "getVoiceNetworkTypeForSubscriber";
                case 65:
                    return "hasIccCard";
                case 66:
                    return "hasIccCardUsingSlotIndex";
                case 67:
                    return "getLteOnCdmaMode";
                case 68:
                    return "getLteOnCdmaModeForSubscriber";
                case 69:
                    return "getAllCellInfo";
                case 70:
                    return "requestCellInfoUpdate";
                case 71:
                    return "requestCellInfoUpdateWithWorkSource";
                case 72:
                    return "setCellInfoListRate";
                case 73:
                    return "iccOpenLogicalChannelBySlot";
                case 74:
                    return "iccOpenLogicalChannel";
                case 75:
                    return "iccCloseLogicalChannelBySlot";
                case 76:
                    return "iccCloseLogicalChannel";
                case 77:
                    return "iccTransmitApduLogicalChannelBySlot";
                case 78:
                    return "iccTransmitApduLogicalChannel";
                case 79:
                    return "iccTransmitApduBasicChannelBySlot";
                case 80:
                    return "iccTransmitApduBasicChannel";
                case 81:
                    return "iccExchangeSimIO";
                case 82:
                    return "sendEnvelopeWithStatus";
                case 83:
                    return "nvReadItem";
                case 84:
                    return "nvWriteItem";
                case 85:
                    return "nvWriteCdmaPrl";
                case 86:
                    return "resetModemConfig";
                case 87:
                    return "rebootModem";
                case 88:
                    return "getCalculatedPreferredNetworkType";
                case 89:
                    return "getPreferredNetworkType";
                case 90:
                    return "getTetherApnRequiredForSubscriber";
                case 91:
                    return "enableIms";
                case 92:
                    return "disableIms";
                case 93:
                    return "getMmTelFeatureAndListen";
                case 94:
                    return "getRcsFeatureAndListen";
                case 95:
                    return "getImsRegistration";
                case 96:
                    return "getImsConfig";
                case 97:
                    return "setImsService";
                case 98:
                    return "getImsService";
                case 99:
                    return "setNetworkSelectionModeAutomatic";
                case 100:
                    return "getCellNetworkScanResults";
                case 101:
                    return "requestNetworkScan";
                case 102:
                    return "stopNetworkScan";
                case 103:
                    return "setNetworkSelectionModeManual";
                case 104:
                    return "setPreferredNetworkType";
                case 105:
                    return "setUserDataEnabled";
                case 106:
                    return "getDataEnabled";
                case 107:
                    return "isUserDataEnabled";
                case 108:
                    return "isDataEnabled";
                case 109:
                    return "isManualNetworkSelectionAllowed";
                case 110:
                    return "getPcscfAddress";
                case 111:
                    return "setImsRegistrationState";
                case 112:
                    return "getCdmaMdn";
                case 113:
                    return "getCdmaMin";
                case 114:
                    return "requestNumberVerification";
                case 115:
                    return "getCarrierPrivilegeStatus";
                case 116:
                    return "getCarrierPrivilegeStatusForUid";
                case 117:
                    return "checkCarrierPrivilegesForPackage";
                case 118:
                    return "checkCarrierPrivilegesForPackageAnyPhone";
                case 119:
                    return "getCarrierPackageNamesForIntentAndPhone";
                case 120:
                    return "setLine1NumberForDisplayForSubscriber";
                case 121:
                    return "getLine1NumberForDisplay";
                case 122:
                    return "getLine1AlphaTagForDisplay";
                case 123:
                    return "getMergedSubscriberIds";
                case 124:
                    return "setOperatorBrandOverride";
                case 125:
                    return "setRoamingOverride";
                case 126:
                    return "invokeOemRilRequestRaw";
                case 127:
                    return "needMobileRadioShutdown";
                case 128:
                    return "shutdownMobileRadios";
                case 129:
                    return "setRadioCapability";
                case 130:
                    return "getRadioAccessFamily";
                case 131:
                    return "enableVideoCalling";
                case 132:
                    return "isVideoCallingEnabled";
                case 133:
                    return "canChangeDtmfToneLength";
                case 134:
                    return "isWorldPhone";
                case 135:
                    return "isTtyModeSupported";
                case 136:
                    return "isRttSupported";
                case 137:
                    return "isHearingAidCompatibilitySupported";
                case 138:
                    return "isImsRegistered";
                case 139:
                    return "isWifiCallingAvailable";
                case 140:
                    return "isVideoTelephonyAvailable";
                case 141:
                    return "getImsRegTechnologyForMmTel";
                case 142:
                    return "getDeviceId";
                case 143:
                    return "getImeiForSlot";
                case 144:
                    return "getTypeAllocationCodeForSlot";
                case 145:
                    return "getMeidForSlot";
                case 146:
                    return "getManufacturerCodeForSlot";
                case 147:
                    return "getDeviceSoftwareVersionForSlot";
                case 148:
                    return "getSubIdForPhoneAccount";
                case 149:
                    return "getPhoneAccountHandleForSubscriptionId";
                case 150:
                    return "factoryReset";
                case 151:
                    return "getSimLocaleForSubscriber";
                case 152:
                    return "requestModemActivityInfo";
                case 153:
                    return "getServiceStateForSubscriber";
                case 154:
                    return "getVoicemailRingtoneUri";
                case 155:
                    return "setVoicemailRingtoneUri";
                case 156:
                    return "isVoicemailVibrationEnabled";
                case 157:
                    return "setVoicemailVibrationEnabled";
                case 158:
                    return "getPackagesWithCarrierPrivileges";
                case 159:
                    return "getPackagesWithCarrierPrivilegesForAllPhones";
                case 160:
                    return "getAidForAppType";
                case 161:
                    return "getEsn";
                case 162:
                    return "getCdmaPrlVersion";
                case 163:
                    return "getTelephonyHistograms";
                case 164:
                    return "setAllowedCarriers";
                case 165:
                    return "getAllowedCarriers";
                case 166:
                    return "getSubscriptionCarrierId";
                case 167:
                    return "getSubscriptionCarrierName";
                case 168:
                    return "getSubscriptionSpecificCarrierId";
                case 169:
                    return "getSubscriptionSpecificCarrierName";
                case 170:
                    return "getCarrierIdFromMccMnc";
                case 171:
                    return "carrierActionSetMeteredApnsEnabled";
                case 172:
                    return "carrierActionSetRadioEnabled";
                case 173:
                    return "carrierActionReportDefaultNetworkStatus";
                case 174:
                    return "carrierActionResetAll";
                case 175:
                    return "getVtDataUsage";
                case 176:
                    return "setPolicyDataEnabled";
                case 177:
                    return "getClientRequestStats";
                case 178:
                    return "setSimPowerStateForSlot";
                case 179:
                    return "getForbiddenPlmns";
                case 180:
                    return "getEmergencyCallbackMode";
                case 181:
                    return "getSignalStrength";
                case 182:
                    return "getCardIdForDefaultEuicc";
                case 183:
                    return "getUiccCardsInfo";
                case 184:
                    return "getUiccSlotsInfo";
                case 185:
                    return "switchSlots";
                case 186:
                    return "setRadioIndicationUpdateMode";
                case 187:
                    return "isDataRoamingEnabled";
                case 188:
                    return "setDataRoamingEnabled";
                case 189:
                    return "getCdmaRoamingMode";
                case 190:
                    return "setCdmaRoamingMode";
                case 191:
                    return "setCdmaSubscriptionMode";
                case 192:
                    return "setCarrierTestOverride";
                case 193:
                    return "getCarrierIdListVersion";
                case 194:
                    return "refreshUiccProfile";
                case 195:
                    return "getNumberOfModemsWithSimultaneousDataConnections";
                case 196:
                    return "getNetworkSelectionMode";
                case 197:
                    return "isInEmergencySmsMode";
                case 198:
                    return "getSmsApps";
                case 199:
                    return "getDefaultSmsApp";
                case 200:
                    return "setDefaultSmsApp";
                case 201:
                    return "getRadioPowerState";
                case 202:
                    return "registerImsRegistrationCallback";
                case 203:
                    return "unregisterImsRegistrationCallback";
                case 204:
                    return "registerMmTelCapabilityCallback";
                case 205:
                    return "unregisterMmTelCapabilityCallback";
                case 206:
                    return "isCapable";
                case 207:
                    return "isAvailable";
                case 208:
                    return "isAdvancedCallingSettingEnabled";
                case 209:
                    return "setAdvancedCallingSettingEnabled";
                case 210:
                    return "isVtSettingEnabled";
                case 211:
                    return "setVtSettingEnabled";
                case 212:
                    return "isVoWiFiSettingEnabled";
                case 213:
                    return "setVoWiFiSettingEnabled";
                case 214:
                    return "isVoWiFiRoamingSettingEnabled";
                case 215:
                    return "setVoWiFiRoamingSettingEnabled";
                case 216:
                    return "setVoWiFiNonPersistent";
                case 217:
                    return "getVoWiFiModeSetting";
                case 218:
                    return "setVoWiFiModeSetting";
                case 219:
                    return "getVoWiFiRoamingModeSetting";
                case 220:
                    return "setVoWiFiRoamingModeSetting";
                case 221:
                    return "setRttCapabilitySetting";
                case 222:
                    return "isTtyOverVolteEnabled";
                case 223:
                    return "getEmergencyNumberList";
                case 224:
                    return "isEmergencyNumber";
                case 225:
                    return "getCertsFromCarrierPrivilegeAccessRules";
                case 226:
                    return "registerImsProvisioningChangedCallback";
                case 227:
                    return "unregisterImsProvisioningChangedCallback";
                case 228:
                    return "setImsProvisioningStatusForCapability";
                case 229:
                    return "getImsProvisioningStatusForCapability";
                case 230:
                    return "isMmTelCapabilityProvisionedInCache";
                case 231:
                    return "cacheMmTelCapabilityProvisioning";
                case 232:
                    return "getImsProvisioningInt";
                case 233:
                    return "getImsProvisioningString";
                case 234:
                    return "setImsProvisioningInt";
                case 235:
                    return "setImsProvisioningString";
                case 236:
                    return "updateEmergencyNumberListTestMode";
                case 237:
                    return "getEmergencyNumberListTestMode";
                case 238:
                    return "enableModemForSlot";
                case 239:
                    return "setMultiSimCarrierRestriction";
                case 240:
                    return "isMultiSimSupported";
                case 241:
                    return "switchMultiSimConfig";
                case 242:
                    return "doesSwitchMultiSimConfigTriggerReboot";
                case 243:
                    return "getSlotsMapping";
                case 244:
                    return "getRadioHalVersion";
                case 245:
                    return "isModemEnabledForSlot";
                case 246:
                    return "isDataEnabledForApn";
                case 247:
                    return "isApnMetered";
                case 248:
                    return "enqueueSmsPickResult";
                case 249:
                    return "getMmsUserAgent";
                case 250:
                    return "getMmsUAProfUrl";
                case 251:
                    return "setDataAllowedDuringVoiceCall";
                case 252:
                    return "isDataAllowedInVoiceCall";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            Parcel parcel2 = reply;
            String descriptor = DESCRIPTOR;
            boolean z;
            if (i != 1598968902) {
                IBinder iBinder = null;
                boolean _arg1 = false;
                boolean _result;
                boolean _result2;
                int[] _result3;
                int _arg0;
                String _arg12;
                boolean _result4;
                int _result5;
                int _result6;
                String _result7;
                String _arg02;
                VisualVoicemailSmsFilterSettings _arg2;
                IccOpenLogicalChannelResponse _result8;
                byte[] _result9;
                String[] _result10;
                int _result11;
                PhoneAccountHandle _result12;
                PhoneAccountHandle _arg03;
                Uri _result13;
                List<String> _result14;
                List<String> _result15;
                CarrierRestrictionRules _arg04;
                switch (i) {
                    case 1:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        dial(data.readString());
                        reply.writeNoException();
                        return z;
                    case 2:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        call(data.readString(), data.readString());
                        reply.writeNoException();
                        return z;
                    case 3:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg1 = isRadioOn(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return z;
                    case 4:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result = isRadioOnForSubscriber(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return z;
                    case 5:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg1 = supplyPin(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return z;
                    case 6:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result = supplyPinForSubscriber(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return z;
                    case 7:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result = supplyPuk(data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return z;
                    case 8:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result2 = supplyPukForSubscriber(data.readInt(), data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return z;
                    case 9:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        int[] _result16 = supplyPinReportResult(data.readString());
                        reply.writeNoException();
                        parcel2.writeIntArray(_result16);
                        return z;
                    case 10:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result3 = supplyPinReportResultForSubscriber(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeIntArray(_result3);
                        return z;
                    case 11:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result3 = supplyPukReportResult(data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeIntArray(_result3);
                        return z;
                    case 12:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        int[] _result17 = supplyPukReportResultForSubscriber(data.readInt(), data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeIntArray(_result17);
                        return z;
                    case 13:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg1 = handlePinMmi(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return z;
                    case 14:
                        ResultReceiver _arg22;
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        _arg12 = data.readString();
                        if (data.readInt() != 0) {
                            _arg22 = (ResultReceiver) ResultReceiver.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg22 = null;
                        }
                        handleUssdRequest(_arg0, _arg12, _arg22);
                        reply.writeNoException();
                        return z;
                    case 15:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result = handlePinMmiForSubscriber(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return z;
                    case 16:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        toggleRadioOnOff();
                        reply.writeNoException();
                        return z;
                    case 17:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        toggleRadioOnOffForSubscriber(data.readInt());
                        reply.writeNoException();
                        return z;
                    case 18:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = z;
                        }
                        _arg1 = setRadio(_arg1);
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return z;
                    case 19:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = z;
                        }
                        _result = setRadioForSubscriber(_arg0, _arg1);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return z;
                    case 20:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = z;
                        }
                        _arg1 = setRadioPower(_arg1);
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return z;
                    case 21:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        updateServiceLocation();
                        reply.writeNoException();
                        return z;
                    case 22:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        updateServiceLocationForSubscriber(data.readInt());
                        reply.writeNoException();
                        return z;
                    case 23:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        enableLocationUpdates();
                        reply.writeNoException();
                        return z;
                    case 24:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        enableLocationUpdatesForSubscriber(data.readInt());
                        reply.writeNoException();
                        return z;
                    case 25:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        disableLocationUpdates();
                        reply.writeNoException();
                        return z;
                    case 26:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        disableLocationUpdatesForSubscriber(data.readInt());
                        reply.writeNoException();
                        return z;
                    case 27:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result4 = enableDataConnectivity();
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return z;
                    case 28:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result4 = disableDataConnectivity();
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return z;
                    case 29:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg1 = isDataConnectivityPossible(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return z;
                    case 30:
                        i = 1;
                        parcel.enforceInterface(descriptor);
                        Bundle _result18 = getCellLocation(data.readString());
                        reply.writeNoException();
                        if (_result18 != null) {
                            parcel2.writeInt(i);
                            _result18.writeToParcel(parcel2, i);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return i;
                    case 31:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg12 = getNetworkCountryIsoForPhone(data.readInt());
                        reply.writeNoException();
                        parcel2.writeString(_arg12);
                        return z;
                    case 32:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        List<NeighboringCellInfo> _result19 = getNeighboringCellInfo(data.readString());
                        reply.writeNoException();
                        parcel2.writeTypedList(_result19);
                        return z;
                    case 33:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg0 = getCallState();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return z;
                    case 34:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result5 = getCallStateForSlot(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return z;
                    case 35:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg0 = getDataActivity();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return z;
                    case 36:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg0 = getDataState();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return z;
                    case 37:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg0 = getActivePhoneType();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return z;
                    case 38:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result5 = getActivePhoneTypeForSlot(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return z;
                    case 39:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result5 = getCdmaEriIconIndex(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return z;
                    case 40:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result6 = getCdmaEriIconIndexForSubscriber(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return z;
                    case 41:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result5 = getCdmaEriIconMode(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return z;
                    case 42:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result6 = getCdmaEriIconModeForSubscriber(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return z;
                    case 43:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg12 = getCdmaEriText(data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_arg12);
                        return z;
                    case 44:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result7 = getCdmaEriTextForSubscriber(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_result7);
                        return z;
                    case 45:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result4 = needsOtaServiceProvisioning();
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return z;
                    case 46:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result2 = setVoiceMailNumber(data.readInt(), data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return z;
                    case 47:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        setVoiceActivationState(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return z;
                    case 48:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        setDataActivationState(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return z;
                    case 49:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result6 = getVoiceActivationState(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return z;
                    case 50:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result6 = getDataActivationState(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return z;
                    case 51:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result6 = getVoiceMessageCountForSubscriber(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return z;
                    case 52:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg1 = isConcurrentVoiceAndDataAllowed(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return z;
                    case 53:
                        i = 1;
                        parcel.enforceInterface(descriptor);
                        Bundle _result20 = getVisualVoicemailSettings(data.readString(), data.readInt());
                        reply.writeNoException();
                        if (_result20 != null) {
                            parcel2.writeInt(i);
                            _result20.writeToParcel(parcel2, i);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return i;
                    case 54:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result7 = getVisualVoicemailPackageName(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeString(_result7);
                        return z;
                    case 55:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        _result5 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = (VisualVoicemailSmsFilterSettings) VisualVoicemailSmsFilterSettings.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        enableVisualVoicemailSmsFilter(_arg02, _result5, _arg2);
                        reply.writeNoException();
                        return z;
                    case 56:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        disableVisualVoicemailSmsFilter(data.readString(), data.readInt());
                        return z;
                    case 57:
                        i = 1;
                        parcel.enforceInterface(descriptor);
                        VisualVoicemailSmsFilterSettings _result21 = getVisualVoicemailSmsFilterSettings(data.readString(), data.readInt());
                        reply.writeNoException();
                        if (_result21 != null) {
                            parcel2.writeInt(i);
                            _result21.writeToParcel(parcel2, i);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return i;
                    case 58:
                        i = 1;
                        parcel.enforceInterface(descriptor);
                        _arg2 = getActiveVisualVoicemailSmsFilterSettings(data.readInt());
                        reply.writeNoException();
                        if (_arg2 != null) {
                            parcel2.writeInt(i);
                            _arg2.writeToParcel(parcel2, i);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return i;
                    case 59:
                        PendingIntent _arg5;
                        z = true;
                        parcel.enforceInterface(descriptor);
                        String _arg05 = data.readString();
                        int _arg13 = data.readInt();
                        String _arg23 = data.readString();
                        int _arg3 = data.readInt();
                        String _arg4 = data.readString();
                        if (data.readInt() != 0) {
                            _arg5 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg5 = null;
                        }
                        sendVisualVoicemailSmsForSubscriber(_arg05, _arg13, _arg23, _arg3, _arg4, _arg5);
                        reply.writeNoException();
                        return z;
                    case 60:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        sendDialerSpecialCode(data.readString(), data.readString());
                        reply.writeNoException();
                        return z;
                    case 61:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result6 = getNetworkTypeForSubscriber(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return z;
                    case 62:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result5 = getDataNetworkType(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return z;
                    case 63:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result6 = getDataNetworkTypeForSubscriber(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return z;
                    case 64:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result6 = getVoiceNetworkTypeForSubscriber(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return z;
                    case 65:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result4 = hasIccCard();
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return z;
                    case 66:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg1 = hasIccCardUsingSlotIndex(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return z;
                    case 67:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result5 = getLteOnCdmaMode(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return z;
                    case 68:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result6 = getLteOnCdmaModeForSubscriber(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return z;
                    case 69:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        List<CellInfo> _result22 = getAllCellInfo(data.readString());
                        reply.writeNoException();
                        parcel2.writeTypedList(_result22);
                        return z;
                    case 70:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        requestCellInfoUpdate(data.readInt(), android.telephony.ICellInfoCallback.Stub.asInterface(data.readStrongBinder()), data.readString());
                        reply.writeNoException();
                        return z;
                    case 71:
                        WorkSource _arg32;
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        ICellInfoCallback _arg14 = android.telephony.ICellInfoCallback.Stub.asInterface(data.readStrongBinder());
                        _result7 = data.readString();
                        if (data.readInt() != 0) {
                            _arg32 = (WorkSource) WorkSource.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg32 = null;
                        }
                        requestCellInfoUpdateWithWorkSource(_arg0, _arg14, _result7, _arg32);
                        reply.writeNoException();
                        return z;
                    case 72:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        setCellInfoListRate(data.readInt());
                        reply.writeNoException();
                        return z;
                    case 73:
                        i = 1;
                        parcel.enforceInterface(descriptor);
                        _result8 = iccOpenLogicalChannelBySlot(data.readInt(), data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        if (_result8 != null) {
                            parcel2.writeInt(i);
                            _result8.writeToParcel(parcel2, i);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return i;
                    case 74:
                        i = 1;
                        parcel.enforceInterface(descriptor);
                        _result8 = iccOpenLogicalChannel(data.readInt(), data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        if (_result8 != null) {
                            parcel2.writeInt(i);
                            _result8.writeToParcel(parcel2, i);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return i;
                    case 75:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result = iccCloseLogicalChannelBySlot(data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return z;
                    case 76:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result = iccCloseLogicalChannel(data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return z;
                    case 77:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg02 = iccTransmitApduLogicalChannelBySlot(data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_arg02);
                        return z;
                    case 78:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg02 = iccTransmitApduLogicalChannel(data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_arg02);
                        return z;
                    case 79:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg02 = iccTransmitApduBasicChannelBySlot(data.readInt(), data.readString(), data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_arg02);
                        return z;
                    case 80:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg02 = iccTransmitApduBasicChannel(data.readInt(), data.readString(), data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_arg02);
                        return z;
                    case 81:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result9 = iccExchangeSimIO(data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeByteArray(_result9);
                        return z;
                    case 82:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result7 = sendEnvelopeWithStatus(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_result7);
                        return z;
                    case 83:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg12 = nvReadItem(data.readInt());
                        reply.writeNoException();
                        parcel2.writeString(_arg12);
                        return z;
                    case 84:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result = nvWriteItem(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return z;
                    case 85:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg1 = nvWriteCdmaPrl(data.createByteArray());
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return z;
                    case 86:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg1 = resetModemConfig(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return z;
                    case 87:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg1 = rebootModem(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return z;
                    case 88:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result5 = getCalculatedPreferredNetworkType(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return z;
                    case 89:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result5 = getPreferredNetworkType(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return z;
                    case 90:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg1 = getTetherApnRequiredForSubscriber(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return z;
                    case 91:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        enableIms(data.readInt());
                        reply.writeNoException();
                        return z;
                    case 92:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        disableIms(data.readInt());
                        reply.writeNoException();
                        return z;
                    case 93:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        IImsMmTelFeature _result23 = getMmTelFeatureAndListen(data.readInt(), com.android.ims.internal.IImsServiceFeatureCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        if (_result23 != null) {
                            iBinder = _result23.asBinder();
                        }
                        parcel2.writeStrongBinder(iBinder);
                        return z;
                    case 94:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        IImsRcsFeature _result24 = getRcsFeatureAndListen(data.readInt(), com.android.ims.internal.IImsServiceFeatureCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        if (_result24 != null) {
                            iBinder = _result24.asBinder();
                        }
                        parcel2.writeStrongBinder(iBinder);
                        return z;
                    case 95:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        IImsRegistration _result25 = getImsRegistration(data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result25 != null) {
                            iBinder = _result25.asBinder();
                        }
                        parcel2.writeStrongBinder(iBinder);
                        return z;
                    case 96:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        IImsConfig _result26 = getImsConfig(data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result26 != null) {
                            iBinder = _result26.asBinder();
                        }
                        parcel2.writeStrongBinder(iBinder);
                        return z;
                    case 97:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = z;
                        }
                        _result2 = setImsService(_arg0, _arg1, data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return z;
                    case 98:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = z;
                        }
                        _result7 = getImsService(_arg0, _arg1);
                        reply.writeNoException();
                        parcel2.writeString(_result7);
                        return z;
                    case 99:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        setNetworkSelectionModeAutomatic(data.readInt());
                        reply.writeNoException();
                        return z;
                    case 100:
                        i = 1;
                        parcel.enforceInterface(descriptor);
                        CellNetworkScanResult _result27 = getCellNetworkScanResults(data.readInt(), data.readString());
                        reply.writeNoException();
                        if (_result27 != null) {
                            parcel2.writeInt(i);
                            _result27.writeToParcel(parcel2, i);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return i;
                    case 101:
                        NetworkScanRequest _arg15;
                        Messenger _arg24;
                        z = true;
                        parcel.enforceInterface(descriptor);
                        int _arg06 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg15 = (NetworkScanRequest) NetworkScanRequest.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg15 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg24 = (Messenger) Messenger.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg24 = null;
                        }
                        _arg0 = requestNetworkScan(_arg06, _arg15, _arg24, data.readStrongBinder(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return z;
                    case 102:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        stopNetworkScan(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return z;
                    case 103:
                        OperatorInfo _arg16;
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg16 = (OperatorInfo) OperatorInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg16 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg1 = z;
                        }
                        _result2 = setNetworkSelectionModeManual(_arg0, _arg16, _arg1);
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return z;
                    case 104:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result = setPreferredNetworkType(data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return z;
                    case 105:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = z;
                        }
                        setUserDataEnabled(_arg0, _arg1);
                        reply.writeNoException();
                        return z;
                    case 106:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg1 = getDataEnabled(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return z;
                    case 107:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg1 = isUserDataEnabled(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return z;
                    case 108:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg1 = isDataEnabled(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return z;
                    case 109:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg1 = isManualNetworkSelectionAllowed(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return z;
                    case 110:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result10 = getPcscfAddress(data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeStringArray(_result10);
                        return z;
                    case 111:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = z;
                        }
                        setImsRegistrationState(_arg1);
                        reply.writeNoException();
                        return z;
                    case 112:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg12 = getCdmaMdn(data.readInt());
                        reply.writeNoException();
                        parcel2.writeString(_arg12);
                        return z;
                    case 113:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg12 = getCdmaMin(data.readInt());
                        reply.writeNoException();
                        parcel2.writeString(_arg12);
                        return z;
                    case 114:
                        PhoneNumberRange _arg07;
                        z = true;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg07 = (PhoneNumberRange) PhoneNumberRange.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg07 = null;
                        }
                        requestNumberVerification(_arg07, data.readLong(), com.android.internal.telephony.INumberVerificationCallback.Stub.asInterface(data.readStrongBinder()), data.readString());
                        reply.writeNoException();
                        return z;
                    case 115:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result5 = getCarrierPrivilegeStatus(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return z;
                    case 116:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result6 = getCarrierPrivilegeStatusForUid(data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return z;
                    case 117:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result6 = checkCarrierPrivilegesForPackage(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return z;
                    case 118:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result5 = checkCarrierPrivilegesForPackageAnyPhone(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return z;
                    case 119:
                        Intent _arg08;
                        z = true;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg08 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg08 = null;
                        }
                        List<String> _result28 = getCarrierPackageNamesForIntentAndPhone(_arg08, data.readInt());
                        reply.writeNoException();
                        parcel2.writeStringList(_result28);
                        return z;
                    case 120:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result2 = setLine1NumberForDisplayForSubscriber(data.readInt(), data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return z;
                    case 121:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result7 = getLine1NumberForDisplay(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_result7);
                        return z;
                    case 122:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result7 = getLine1AlphaTagForDisplay(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_result7);
                        return z;
                    case 123:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result10 = getMergedSubscriberIds(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeStringArray(_result10);
                        return z;
                    case 124:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result = setOperatorBrandOverride(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return z;
                    case 125:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result4 = setRoamingOverride(data.readInt(), data.createStringArrayList(), data.createStringArrayList(), data.createStringArrayList(), data.createStringArrayList());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return z;
                    case 126:
                        byte[] _arg17;
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result9 = data.createByteArray();
                        _result5 = data.readInt();
                        if (_result5 < 0) {
                            _arg17 = null;
                        } else {
                            _arg17 = new byte[_result5];
                        }
                        _result11 = invokeOemRilRequestRaw(_result9, _arg17);
                        reply.writeNoException();
                        parcel2.writeInt(_result11);
                        parcel2.writeByteArray(_arg17);
                        return z;
                    case 127:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result4 = needMobileRadioShutdown();
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return z;
                    case 128:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        shutdownMobileRadios();
                        reply.writeNoException();
                        return z;
                    case 129:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        setRadioCapability((RadioAccessFamily[]) parcel.createTypedArray(RadioAccessFamily.CREATOR));
                        reply.writeNoException();
                        return z;
                    case 130:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result6 = getRadioAccessFamily(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return z;
                    case 131:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = z;
                        }
                        enableVideoCalling(_arg1);
                        reply.writeNoException();
                        return z;
                    case 132:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg1 = isVideoCallingEnabled(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return z;
                    case 133:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result = canChangeDtmfToneLength(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return z;
                    case 134:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result = isWorldPhone(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return z;
                    case 135:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result4 = isTtyModeSupported();
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return z;
                    case 136:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg1 = isRttSupported(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return z;
                    case 137:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result4 = isHearingAidCompatibilitySupported();
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return z;
                    case 138:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg1 = isImsRegistered(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return z;
                    case 139:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg1 = isWifiCallingAvailable(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return z;
                    case 140:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg1 = isVideoTelephonyAvailable(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return z;
                    case 141:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result5 = getImsRegTechnologyForMmTel(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return z;
                    case 142:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg12 = getDeviceId(data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_arg12);
                        return z;
                    case 143:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result7 = getImeiForSlot(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_result7);
                        return z;
                    case 144:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg12 = getTypeAllocationCodeForSlot(data.readInt());
                        reply.writeNoException();
                        parcel2.writeString(_arg12);
                        return z;
                    case 145:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result7 = getMeidForSlot(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_result7);
                        return z;
                    case 146:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg12 = getManufacturerCodeForSlot(data.readInt());
                        reply.writeNoException();
                        parcel2.writeString(_arg12);
                        return z;
                    case 147:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result7 = getDeviceSoftwareVersionForSlot(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_result7);
                        return z;
                    case 148:
                        PhoneAccount _arg09;
                        z = true;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg09 = (PhoneAccount) PhoneAccount.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg09 = null;
                        }
                        _result5 = getSubIdForPhoneAccount(_arg09);
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return z;
                    case 149:
                        i = 1;
                        parcel.enforceInterface(descriptor);
                        _result12 = getPhoneAccountHandleForSubscriptionId(data.readInt());
                        reply.writeNoException();
                        if (_result12 != null) {
                            parcel2.writeInt(i);
                            _result12.writeToParcel(parcel2, i);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return i;
                    case 150:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        factoryReset(data.readInt());
                        reply.writeNoException();
                        return z;
                    case 151:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg12 = getSimLocaleForSubscriber(data.readInt());
                        reply.writeNoException();
                        parcel2.writeString(_arg12);
                        return z;
                    case 152:
                        ResultReceiver _arg010;
                        z = true;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg010 = (ResultReceiver) ResultReceiver.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg010 = null;
                        }
                        requestModemActivityInfo(_arg010);
                        return z;
                    case 153:
                        i = 1;
                        parcel.enforceInterface(descriptor);
                        ServiceState _result29 = getServiceStateForSubscriber(data.readInt(), data.readString());
                        reply.writeNoException();
                        if (_result29 != null) {
                            parcel2.writeInt(i);
                            _result29.writeToParcel(parcel2, i);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return i;
                    case 154:
                        i = 1;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        _result13 = getVoicemailRingtoneUri(_arg03);
                        reply.writeNoException();
                        if (_result13 != null) {
                            parcel2.writeInt(i);
                            _result13.writeToParcel(parcel2, i);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return i;
                    case 155:
                        PhoneAccountHandle _arg18;
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg18 = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg18 = null;
                        }
                        if (data.readInt() != 0) {
                            _result13 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _result13 = null;
                        }
                        setVoicemailRingtoneUri(_arg02, _arg18, _result13);
                        reply.writeNoException();
                        return z;
                    case 156:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        _arg1 = isVoicemailVibrationEnabled(_arg03);
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return z;
                    case 157:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _result12 = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(parcel);
                        } else {
                            _result12 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg1 = z;
                        }
                        setVoicemailVibrationEnabled(_arg02, _result12, _arg1);
                        reply.writeNoException();
                        return z;
                    case 158:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result14 = getPackagesWithCarrierPrivileges(data.readInt());
                        reply.writeNoException();
                        parcel2.writeStringList(_result14);
                        return z;
                    case 159:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result15 = getPackagesWithCarrierPrivilegesForAllPhones();
                        reply.writeNoException();
                        parcel2.writeStringList(_result15);
                        return z;
                    case 160:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result7 = getAidForAppType(data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeString(_result7);
                        return z;
                    case 161:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg12 = getEsn(data.readInt());
                        reply.writeNoException();
                        parcel2.writeString(_arg12);
                        return z;
                    case 162:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg12 = getCdmaPrlVersion(data.readInt());
                        reply.writeNoException();
                        parcel2.writeString(_arg12);
                        return z;
                    case 163:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        List<TelephonyHistogram> _result30 = getTelephonyHistograms();
                        reply.writeNoException();
                        parcel2.writeTypedList(_result30);
                        return z;
                    case 164:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (CarrierRestrictionRules) CarrierRestrictionRules.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        _result5 = setAllowedCarriers(_arg04);
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return z;
                    case 165:
                        i = 1;
                        parcel.enforceInterface(descriptor);
                        _arg04 = getAllowedCarriers();
                        reply.writeNoException();
                        if (_arg04 != null) {
                            parcel2.writeInt(i);
                            _arg04.writeToParcel(parcel2, i);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return i;
                    case 166:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result5 = getSubscriptionCarrierId(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return z;
                    case 167:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg12 = getSubscriptionCarrierName(data.readInt());
                        reply.writeNoException();
                        parcel2.writeString(_arg12);
                        return z;
                    case 168:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result5 = getSubscriptionSpecificCarrierId(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return z;
                    case 169:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg12 = getSubscriptionSpecificCarrierName(data.readInt());
                        reply.writeNoException();
                        parcel2.writeString(_arg12);
                        return z;
                    case 170:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        _result7 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = z;
                        }
                        _result11 = getCarrierIdFromMccMnc(_arg0, _result7, _arg1);
                        reply.writeNoException();
                        parcel2.writeInt(_result11);
                        return z;
                    case 171:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = z;
                        }
                        carrierActionSetMeteredApnsEnabled(_arg0, _arg1);
                        reply.writeNoException();
                        return z;
                    case 172:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = z;
                        }
                        carrierActionSetRadioEnabled(_arg0, _arg1);
                        reply.writeNoException();
                        return z;
                    case 173:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = z;
                        }
                        carrierActionReportDefaultNetworkStatus(_arg0, _arg1);
                        reply.writeNoException();
                        return z;
                    case 174:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        carrierActionResetAll(data.readInt());
                        reply.writeNoException();
                        return z;
                    case 175:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        NetworkStats _result31 = getVtDataUsage(data.readInt(), data.readInt() != 0 ? z : false);
                        reply.writeNoException();
                        if (_result31 != null) {
                            parcel2.writeInt(z);
                            _result31.writeToParcel(parcel2, z);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return z;
                    case 176:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = z;
                        }
                        setPolicyDataEnabled(_arg1, data.readInt());
                        reply.writeNoException();
                        return z;
                    case 177:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        List<ClientRequestStats> _result32 = getClientRequestStats(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeTypedList(_result32);
                        return z;
                    case 178:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        setSimPowerStateForSlot(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return z;
                    case 179:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        String[] _result33 = getForbiddenPlmns(data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeStringArray(_result33);
                        return z;
                    case 180:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg1 = getEmergencyCallbackMode(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return z;
                    case 181:
                        i = 1;
                        parcel.enforceInterface(descriptor);
                        SignalStrength _result34 = getSignalStrength(data.readInt());
                        reply.writeNoException();
                        if (_result34 != null) {
                            parcel2.writeInt(i);
                            _result34.writeToParcel(parcel2, i);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return i;
                    case 182:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result6 = getCardIdForDefaultEuicc(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return z;
                    case 183:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        List<UiccCardInfo> _result35 = getUiccCardsInfo(data.readString());
                        reply.writeNoException();
                        parcel2.writeTypedList(_result35);
                        return z;
                    case 184:
                        i = 1;
                        parcel.enforceInterface(descriptor);
                        UiccSlotInfo[] _result36 = getUiccSlotsInfo();
                        reply.writeNoException();
                        parcel2.writeTypedArray(_result36, i);
                        return i;
                    case 185:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg1 = switchSlots(data.createIntArray());
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return z;
                    case 186:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        setRadioIndicationUpdateMode(data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return z;
                    case 187:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg1 = isDataRoamingEnabled(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return z;
                    case 188:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = z;
                        }
                        setDataRoamingEnabled(_arg0, _arg1);
                        reply.writeNoException();
                        return z;
                    case 189:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result5 = getCdmaRoamingMode(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return z;
                    case 190:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result = setCdmaRoamingMode(data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return z;
                    case 191:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result = setCdmaSubscriptionMode(data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return z;
                    case 192:
                        parcel.enforceInterface(descriptor);
                        z = true;
                        setCarrierTestOverride(data.readInt(), data.readString(), data.readString(), data.readString(), data.readString(), data.readString(), data.readString(), data.readString(), data.readString(), data.readString());
                        reply.writeNoException();
                        return z;
                    case 193:
                        parcel.enforceInterface(descriptor);
                        _result5 = getCarrierIdListVersion(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 194:
                        parcel.enforceInterface(descriptor);
                        refreshUiccProfile(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 195:
                        parcel.enforceInterface(descriptor);
                        _result6 = getNumberOfModemsWithSimultaneousDataConnections(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return true;
                    case 196:
                        parcel.enforceInterface(descriptor);
                        _result5 = getNetworkSelectionMode(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 197:
                        parcel.enforceInterface(descriptor);
                        _result4 = isInEmergencySmsMode();
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 198:
                        parcel.enforceInterface(descriptor);
                        String[] _result37 = getSmsApps(data.readInt());
                        reply.writeNoException();
                        parcel2.writeStringArray(_result37);
                        return true;
                    case 199:
                        parcel.enforceInterface(descriptor);
                        _arg12 = getDefaultSmsApp(data.readInt());
                        reply.writeNoException();
                        parcel2.writeString(_arg12);
                        return true;
                    case 200:
                        parcel.enforceInterface(descriptor);
                        setDefaultSmsApp(data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 201:
                        parcel.enforceInterface(descriptor);
                        _result6 = getRadioPowerState(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return true;
                    case 202:
                        parcel.enforceInterface(descriptor);
                        registerImsRegistrationCallback(data.readInt(), android.telephony.ims.aidl.IImsRegistrationCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 203:
                        parcel.enforceInterface(descriptor);
                        unregisterImsRegistrationCallback(data.readInt(), android.telephony.ims.aidl.IImsRegistrationCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 204:
                        parcel.enforceInterface(descriptor);
                        registerMmTelCapabilityCallback(data.readInt(), android.telephony.ims.aidl.IImsCapabilityCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 205:
                        parcel.enforceInterface(descriptor);
                        unregisterMmTelCapabilityCallback(data.readInt(), android.telephony.ims.aidl.IImsCapabilityCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 206:
                        parcel.enforceInterface(descriptor);
                        _result2 = isCapable(data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 207:
                        parcel.enforceInterface(descriptor);
                        _result2 = isAvailable(data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 208:
                        parcel.enforceInterface(descriptor);
                        _arg1 = isAdvancedCallingSettingEnabled(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 209:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setAdvancedCallingSettingEnabled(_arg0, _arg1);
                        reply.writeNoException();
                        return true;
                    case 210:
                        parcel.enforceInterface(descriptor);
                        _arg1 = isVtSettingEnabled(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 211:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setVtSettingEnabled(_arg0, _arg1);
                        reply.writeNoException();
                        return true;
                    case 212:
                        parcel.enforceInterface(descriptor);
                        _arg1 = isVoWiFiSettingEnabled(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 213:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setVoWiFiSettingEnabled(_arg0, _arg1);
                        reply.writeNoException();
                        return true;
                    case 214:
                        parcel.enforceInterface(descriptor);
                        _arg1 = isVoWiFiRoamingSettingEnabled(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 215:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setVoWiFiRoamingSettingEnabled(_arg0, _arg1);
                        reply.writeNoException();
                        return true;
                    case 216:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setVoWiFiNonPersistent(_arg0, _arg1, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 217:
                        parcel.enforceInterface(descriptor);
                        _result5 = getVoWiFiModeSetting(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 218:
                        parcel.enforceInterface(descriptor);
                        setVoWiFiModeSetting(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 219:
                        parcel.enforceInterface(descriptor);
                        _result5 = getVoWiFiRoamingModeSetting(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 220:
                        parcel.enforceInterface(descriptor);
                        setVoWiFiRoamingModeSetting(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 221:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setRttCapabilitySetting(_arg0, _arg1);
                        reply.writeNoException();
                        return true;
                    case 222:
                        parcel.enforceInterface(descriptor);
                        _arg1 = isTtyOverVolteEnabled(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 223:
                        parcel.enforceInterface(descriptor);
                        Map _result38 = getEmergencyNumberList(data.readString());
                        reply.writeNoException();
                        parcel2.writeMap(_result38);
                        return true;
                    case 224:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        _result = isEmergencyNumber(_arg02, _arg1);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 225:
                        parcel.enforceInterface(descriptor);
                        _result14 = getCertsFromCarrierPrivilegeAccessRules(data.readInt());
                        reply.writeNoException();
                        parcel2.writeStringList(_result14);
                        return true;
                    case 226:
                        parcel.enforceInterface(descriptor);
                        registerImsProvisioningChangedCallback(data.readInt(), android.telephony.ims.aidl.IImsConfigCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 227:
                        parcel.enforceInterface(descriptor);
                        unregisterImsProvisioningChangedCallback(data.readInt(), android.telephony.ims.aidl.IImsConfigCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 228:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        _result6 = data.readInt();
                        _result11 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setImsProvisioningStatusForCapability(_arg0, _result6, _result11, _arg1);
                        reply.writeNoException();
                        return true;
                    case 229:
                        parcel.enforceInterface(descriptor);
                        _result2 = getImsProvisioningStatusForCapability(data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 230:
                        parcel.enforceInterface(descriptor);
                        _result2 = isMmTelCapabilityProvisionedInCache(data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 231:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        _result6 = data.readInt();
                        _result11 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        cacheMmTelCapabilityProvisioning(_arg0, _result6, _result11, _arg1);
                        reply.writeNoException();
                        return true;
                    case 232:
                        parcel.enforceInterface(descriptor);
                        _result6 = getImsProvisioningInt(data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return true;
                    case 233:
                        parcel.enforceInterface(descriptor);
                        _result7 = getImsProvisioningString(data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeString(_result7);
                        return true;
                    case 234:
                        parcel.enforceInterface(descriptor);
                        _result11 = setImsProvisioningInt(data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result11);
                        return true;
                    case 235:
                        parcel.enforceInterface(descriptor);
                        _result11 = setImsProvisioningString(data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result11);
                        return true;
                    case 236:
                        EmergencyNumber _arg19;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg19 = (EmergencyNumber) EmergencyNumber.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg19 = null;
                        }
                        updateEmergencyNumberListTestMode(_arg0, _arg19);
                        reply.writeNoException();
                        return true;
                    case 237:
                        parcel.enforceInterface(descriptor);
                        _result15 = getEmergencyNumberListTestMode();
                        reply.writeNoException();
                        parcel2.writeStringList(_result15);
                        return true;
                    case 238:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        _result = enableModemForSlot(_arg0, _arg1);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 239:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setMultiSimCarrierRestriction(_arg1);
                        reply.writeNoException();
                        return true;
                    case 240:
                        parcel.enforceInterface(descriptor);
                        _result5 = isMultiSimSupported(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 241:
                        parcel.enforceInterface(descriptor);
                        switchMultiSimConfig(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 242:
                        parcel.enforceInterface(descriptor);
                        _result = doesSwitchMultiSimConfigTriggerReboot(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 243:
                        parcel.enforceInterface(descriptor);
                        int[] _result39 = getSlotsMapping();
                        reply.writeNoException();
                        parcel2.writeIntArray(_result39);
                        return true;
                    case 244:
                        parcel.enforceInterface(descriptor);
                        _arg0 = getRadioHalVersion();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return true;
                    case 245:
                        parcel.enforceInterface(descriptor);
                        _result = isModemEnabledForSlot(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 246:
                        parcel.enforceInterface(descriptor);
                        _result2 = isDataEnabledForApn(data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 247:
                        parcel.enforceInterface(descriptor);
                        _result = isApnMetered(data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 248:
                        parcel.enforceInterface(descriptor);
                        enqueueSmsPickResult(data.readString(), com.android.internal.telephony.IIntegerConsumer.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 249:
                        parcel.enforceInterface(descriptor);
                        _arg12 = getMmsUserAgent(data.readInt());
                        reply.writeNoException();
                        parcel2.writeString(_arg12);
                        return true;
                    case 250:
                        parcel.enforceInterface(descriptor);
                        _arg12 = getMmsUAProfUrl(data.readInt());
                        reply.writeNoException();
                        parcel2.writeString(_arg12);
                        return true;
                    case 251:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        _result = setDataAllowedDuringVoiceCall(_arg0, _arg1);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 252:
                        parcel.enforceInterface(descriptor);
                        _arg1 = isDataAllowedInVoiceCall(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            z = true;
            parcel2.writeString(descriptor);
            return z;
        }

        public static boolean setDefaultImpl(ITelephony impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ITelephony getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void cacheMmTelCapabilityProvisioning(int i, int i2, int i3, boolean z) throws RemoteException;

    @UnsupportedAppUsage
    void call(String str, String str2) throws RemoteException;

    boolean canChangeDtmfToneLength(int i, String str) throws RemoteException;

    void carrierActionReportDefaultNetworkStatus(int i, boolean z) throws RemoteException;

    void carrierActionResetAll(int i) throws RemoteException;

    void carrierActionSetMeteredApnsEnabled(int i, boolean z) throws RemoteException;

    void carrierActionSetRadioEnabled(int i, boolean z) throws RemoteException;

    int checkCarrierPrivilegesForPackage(int i, String str) throws RemoteException;

    int checkCarrierPrivilegesForPackageAnyPhone(String str) throws RemoteException;

    @UnsupportedAppUsage
    void dial(String str) throws RemoteException;

    @UnsupportedAppUsage
    boolean disableDataConnectivity() throws RemoteException;

    void disableIms(int i) throws RemoteException;

    @UnsupportedAppUsage
    void disableLocationUpdates() throws RemoteException;

    void disableLocationUpdatesForSubscriber(int i) throws RemoteException;

    void disableVisualVoicemailSmsFilter(String str, int i) throws RemoteException;

    boolean doesSwitchMultiSimConfigTriggerReboot(int i, String str) throws RemoteException;

    @UnsupportedAppUsage
    boolean enableDataConnectivity() throws RemoteException;

    void enableIms(int i) throws RemoteException;

    @UnsupportedAppUsage
    void enableLocationUpdates() throws RemoteException;

    void enableLocationUpdatesForSubscriber(int i) throws RemoteException;

    boolean enableModemForSlot(int i, boolean z) throws RemoteException;

    void enableVideoCalling(boolean z) throws RemoteException;

    void enableVisualVoicemailSmsFilter(String str, int i, VisualVoicemailSmsFilterSettings visualVoicemailSmsFilterSettings) throws RemoteException;

    void enqueueSmsPickResult(String str, IIntegerConsumer iIntegerConsumer) throws RemoteException;

    void factoryReset(int i) throws RemoteException;

    @UnsupportedAppUsage
    int getActivePhoneType() throws RemoteException;

    int getActivePhoneTypeForSlot(int i) throws RemoteException;

    VisualVoicemailSmsFilterSettings getActiveVisualVoicemailSmsFilterSettings(int i) throws RemoteException;

    String getAidForAppType(int i, int i2) throws RemoteException;

    List<CellInfo> getAllCellInfo(String str) throws RemoteException;

    CarrierRestrictionRules getAllowedCarriers() throws RemoteException;

    int getCalculatedPreferredNetworkType(String str) throws RemoteException;

    @UnsupportedAppUsage
    int getCallState() throws RemoteException;

    int getCallStateForSlot(int i) throws RemoteException;

    int getCardIdForDefaultEuicc(int i, String str) throws RemoteException;

    int getCarrierIdFromMccMnc(int i, String str, boolean z) throws RemoteException;

    int getCarrierIdListVersion(int i) throws RemoteException;

    List<String> getCarrierPackageNamesForIntentAndPhone(Intent intent, int i) throws RemoteException;

    int getCarrierPrivilegeStatus(int i) throws RemoteException;

    int getCarrierPrivilegeStatusForUid(int i, int i2) throws RemoteException;

    int getCdmaEriIconIndex(String str) throws RemoteException;

    int getCdmaEriIconIndexForSubscriber(int i, String str) throws RemoteException;

    int getCdmaEriIconMode(String str) throws RemoteException;

    int getCdmaEriIconModeForSubscriber(int i, String str) throws RemoteException;

    String getCdmaEriText(String str) throws RemoteException;

    String getCdmaEriTextForSubscriber(int i, String str) throws RemoteException;

    String getCdmaMdn(int i) throws RemoteException;

    String getCdmaMin(int i) throws RemoteException;

    String getCdmaPrlVersion(int i) throws RemoteException;

    int getCdmaRoamingMode(int i) throws RemoteException;

    Bundle getCellLocation(String str) throws RemoteException;

    CellNetworkScanResult getCellNetworkScanResults(int i, String str) throws RemoteException;

    List<String> getCertsFromCarrierPrivilegeAccessRules(int i) throws RemoteException;

    List<ClientRequestStats> getClientRequestStats(String str, int i) throws RemoteException;

    int getDataActivationState(int i, String str) throws RemoteException;

    @UnsupportedAppUsage
    int getDataActivity() throws RemoteException;

    @UnsupportedAppUsage
    boolean getDataEnabled(int i) throws RemoteException;

    int getDataNetworkType(String str) throws RemoteException;

    int getDataNetworkTypeForSubscriber(int i, String str) throws RemoteException;

    @UnsupportedAppUsage
    int getDataState() throws RemoteException;

    String getDefaultSmsApp(int i) throws RemoteException;

    String getDeviceId(String str) throws RemoteException;

    String getDeviceSoftwareVersionForSlot(int i, String str) throws RemoteException;

    boolean getEmergencyCallbackMode(int i) throws RemoteException;

    Map getEmergencyNumberList(String str) throws RemoteException;

    List<String> getEmergencyNumberListTestMode() throws RemoteException;

    String getEsn(int i) throws RemoteException;

    String[] getForbiddenPlmns(int i, int i2, String str) throws RemoteException;

    String getImeiForSlot(int i, String str) throws RemoteException;

    IImsConfig getImsConfig(int i, int i2) throws RemoteException;

    int getImsProvisioningInt(int i, int i2) throws RemoteException;

    boolean getImsProvisioningStatusForCapability(int i, int i2, int i3) throws RemoteException;

    String getImsProvisioningString(int i, int i2) throws RemoteException;

    int getImsRegTechnologyForMmTel(int i) throws RemoteException;

    IImsRegistration getImsRegistration(int i, int i2) throws RemoteException;

    String getImsService(int i, boolean z) throws RemoteException;

    String getLine1AlphaTagForDisplay(int i, String str) throws RemoteException;

    String getLine1NumberForDisplay(int i, String str) throws RemoteException;

    int getLteOnCdmaMode(String str) throws RemoteException;

    int getLteOnCdmaModeForSubscriber(int i, String str) throws RemoteException;

    String getManufacturerCodeForSlot(int i) throws RemoteException;

    String getMeidForSlot(int i, String str) throws RemoteException;

    String[] getMergedSubscriberIds(int i, String str) throws RemoteException;

    IImsMmTelFeature getMmTelFeatureAndListen(int i, IImsServiceFeatureCallback iImsServiceFeatureCallback) throws RemoteException;

    String getMmsUAProfUrl(int i) throws RemoteException;

    String getMmsUserAgent(int i) throws RemoteException;

    List<NeighboringCellInfo> getNeighboringCellInfo(String str) throws RemoteException;

    String getNetworkCountryIsoForPhone(int i) throws RemoteException;

    int getNetworkSelectionMode(int i) throws RemoteException;

    int getNetworkTypeForSubscriber(int i, String str) throws RemoteException;

    int getNumberOfModemsWithSimultaneousDataConnections(int i, String str) throws RemoteException;

    List<String> getPackagesWithCarrierPrivileges(int i) throws RemoteException;

    List<String> getPackagesWithCarrierPrivilegesForAllPhones() throws RemoteException;

    String[] getPcscfAddress(String str, String str2) throws RemoteException;

    PhoneAccountHandle getPhoneAccountHandleForSubscriptionId(int i) throws RemoteException;

    int getPreferredNetworkType(int i) throws RemoteException;

    int getRadioAccessFamily(int i, String str) throws RemoteException;

    int getRadioHalVersion() throws RemoteException;

    int getRadioPowerState(int i, String str) throws RemoteException;

    IImsRcsFeature getRcsFeatureAndListen(int i, IImsServiceFeatureCallback iImsServiceFeatureCallback) throws RemoteException;

    ServiceState getServiceStateForSubscriber(int i, String str) throws RemoteException;

    SignalStrength getSignalStrength(int i) throws RemoteException;

    String getSimLocaleForSubscriber(int i) throws RemoteException;

    int[] getSlotsMapping() throws RemoteException;

    String[] getSmsApps(int i) throws RemoteException;

    int getSubIdForPhoneAccount(PhoneAccount phoneAccount) throws RemoteException;

    int getSubscriptionCarrierId(int i) throws RemoteException;

    String getSubscriptionCarrierName(int i) throws RemoteException;

    int getSubscriptionSpecificCarrierId(int i) throws RemoteException;

    String getSubscriptionSpecificCarrierName(int i) throws RemoteException;

    List<TelephonyHistogram> getTelephonyHistograms() throws RemoteException;

    boolean getTetherApnRequiredForSubscriber(int i) throws RemoteException;

    String getTypeAllocationCodeForSlot(int i) throws RemoteException;

    List<UiccCardInfo> getUiccCardsInfo(String str) throws RemoteException;

    UiccSlotInfo[] getUiccSlotsInfo() throws RemoteException;

    String getVisualVoicemailPackageName(String str, int i) throws RemoteException;

    Bundle getVisualVoicemailSettings(String str, int i) throws RemoteException;

    VisualVoicemailSmsFilterSettings getVisualVoicemailSmsFilterSettings(String str, int i) throws RemoteException;

    int getVoWiFiModeSetting(int i) throws RemoteException;

    int getVoWiFiRoamingModeSetting(int i) throws RemoteException;

    int getVoiceActivationState(int i, String str) throws RemoteException;

    int getVoiceMessageCountForSubscriber(int i, String str) throws RemoteException;

    int getVoiceNetworkTypeForSubscriber(int i, String str) throws RemoteException;

    Uri getVoicemailRingtoneUri(PhoneAccountHandle phoneAccountHandle) throws RemoteException;

    NetworkStats getVtDataUsage(int i, boolean z) throws RemoteException;

    @UnsupportedAppUsage
    boolean handlePinMmi(String str) throws RemoteException;

    @UnsupportedAppUsage
    boolean handlePinMmiForSubscriber(int i, String str) throws RemoteException;

    void handleUssdRequest(int i, String str, ResultReceiver resultReceiver) throws RemoteException;

    @UnsupportedAppUsage
    boolean hasIccCard() throws RemoteException;

    boolean hasIccCardUsingSlotIndex(int i) throws RemoteException;

    @UnsupportedAppUsage
    boolean iccCloseLogicalChannel(int i, int i2) throws RemoteException;

    boolean iccCloseLogicalChannelBySlot(int i, int i2) throws RemoteException;

    byte[] iccExchangeSimIO(int i, int i2, int i3, int i4, int i5, int i6, String str) throws RemoteException;

    IccOpenLogicalChannelResponse iccOpenLogicalChannel(int i, String str, String str2, int i2) throws RemoteException;

    IccOpenLogicalChannelResponse iccOpenLogicalChannelBySlot(int i, String str, String str2, int i2) throws RemoteException;

    String iccTransmitApduBasicChannel(int i, String str, int i2, int i3, int i4, int i5, int i6, String str2) throws RemoteException;

    String iccTransmitApduBasicChannelBySlot(int i, String str, int i2, int i3, int i4, int i5, int i6, String str2) throws RemoteException;

    @UnsupportedAppUsage
    String iccTransmitApduLogicalChannel(int i, int i2, int i3, int i4, int i5, int i6, int i7, String str) throws RemoteException;

    String iccTransmitApduLogicalChannelBySlot(int i, int i2, int i3, int i4, int i5, int i6, int i7, String str) throws RemoteException;

    int invokeOemRilRequestRaw(byte[] bArr, byte[] bArr2) throws RemoteException;

    boolean isAdvancedCallingSettingEnabled(int i) throws RemoteException;

    boolean isApnMetered(int i, int i2) throws RemoteException;

    boolean isAvailable(int i, int i2, int i3) throws RemoteException;

    boolean isCapable(int i, int i2, int i3) throws RemoteException;

    boolean isConcurrentVoiceAndDataAllowed(int i) throws RemoteException;

    boolean isDataAllowedInVoiceCall(int i) throws RemoteException;

    boolean isDataConnectivityPossible(int i) throws RemoteException;

    boolean isDataEnabled(int i) throws RemoteException;

    boolean isDataEnabledForApn(int i, int i2, String str) throws RemoteException;

    boolean isDataRoamingEnabled(int i) throws RemoteException;

    boolean isEmergencyNumber(String str, boolean z) throws RemoteException;

    boolean isHearingAidCompatibilitySupported() throws RemoteException;

    boolean isImsRegistered(int i) throws RemoteException;

    boolean isInEmergencySmsMode() throws RemoteException;

    boolean isManualNetworkSelectionAllowed(int i) throws RemoteException;

    boolean isMmTelCapabilityProvisionedInCache(int i, int i2, int i3) throws RemoteException;

    boolean isModemEnabledForSlot(int i, String str) throws RemoteException;

    int isMultiSimSupported(String str) throws RemoteException;

    boolean isRadioOn(String str) throws RemoteException;

    @UnsupportedAppUsage
    boolean isRadioOnForSubscriber(int i, String str) throws RemoteException;

    boolean isRttSupported(int i) throws RemoteException;

    boolean isTtyModeSupported() throws RemoteException;

    boolean isTtyOverVolteEnabled(int i) throws RemoteException;

    boolean isUserDataEnabled(int i) throws RemoteException;

    boolean isVideoCallingEnabled(String str) throws RemoteException;

    boolean isVideoTelephonyAvailable(int i) throws RemoteException;

    boolean isVoWiFiRoamingSettingEnabled(int i) throws RemoteException;

    boolean isVoWiFiSettingEnabled(int i) throws RemoteException;

    boolean isVoicemailVibrationEnabled(PhoneAccountHandle phoneAccountHandle) throws RemoteException;

    boolean isVtSettingEnabled(int i) throws RemoteException;

    boolean isWifiCallingAvailable(int i) throws RemoteException;

    boolean isWorldPhone(int i, String str) throws RemoteException;

    boolean needMobileRadioShutdown() throws RemoteException;

    boolean needsOtaServiceProvisioning() throws RemoteException;

    String nvReadItem(int i) throws RemoteException;

    boolean nvWriteCdmaPrl(byte[] bArr) throws RemoteException;

    boolean nvWriteItem(int i, String str) throws RemoteException;

    boolean rebootModem(int i) throws RemoteException;

    void refreshUiccProfile(int i) throws RemoteException;

    void registerImsProvisioningChangedCallback(int i, IImsConfigCallback iImsConfigCallback) throws RemoteException;

    void registerImsRegistrationCallback(int i, IImsRegistrationCallback iImsRegistrationCallback) throws RemoteException;

    void registerMmTelCapabilityCallback(int i, IImsCapabilityCallback iImsCapabilityCallback) throws RemoteException;

    void requestCellInfoUpdate(int i, ICellInfoCallback iCellInfoCallback, String str) throws RemoteException;

    void requestCellInfoUpdateWithWorkSource(int i, ICellInfoCallback iCellInfoCallback, String str, WorkSource workSource) throws RemoteException;

    void requestModemActivityInfo(ResultReceiver resultReceiver) throws RemoteException;

    int requestNetworkScan(int i, NetworkScanRequest networkScanRequest, Messenger messenger, IBinder iBinder, String str) throws RemoteException;

    void requestNumberVerification(PhoneNumberRange phoneNumberRange, long j, INumberVerificationCallback iNumberVerificationCallback, String str) throws RemoteException;

    boolean resetModemConfig(int i) throws RemoteException;

    void sendDialerSpecialCode(String str, String str2) throws RemoteException;

    String sendEnvelopeWithStatus(int i, String str) throws RemoteException;

    void sendVisualVoicemailSmsForSubscriber(String str, int i, String str2, int i2, String str3, PendingIntent pendingIntent) throws RemoteException;

    void setAdvancedCallingSettingEnabled(int i, boolean z) throws RemoteException;

    int setAllowedCarriers(CarrierRestrictionRules carrierRestrictionRules) throws RemoteException;

    void setCarrierTestOverride(int i, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9) throws RemoteException;

    boolean setCdmaRoamingMode(int i, int i2) throws RemoteException;

    boolean setCdmaSubscriptionMode(int i, int i2) throws RemoteException;

    void setCellInfoListRate(int i) throws RemoteException;

    void setDataActivationState(int i, int i2) throws RemoteException;

    boolean setDataAllowedDuringVoiceCall(int i, boolean z) throws RemoteException;

    void setDataRoamingEnabled(int i, boolean z) throws RemoteException;

    void setDefaultSmsApp(int i, String str) throws RemoteException;

    int setImsProvisioningInt(int i, int i2, int i3) throws RemoteException;

    void setImsProvisioningStatusForCapability(int i, int i2, int i3, boolean z) throws RemoteException;

    int setImsProvisioningString(int i, int i2, String str) throws RemoteException;

    void setImsRegistrationState(boolean z) throws RemoteException;

    boolean setImsService(int i, boolean z, String str) throws RemoteException;

    boolean setLine1NumberForDisplayForSubscriber(int i, String str, String str2) throws RemoteException;

    void setMultiSimCarrierRestriction(boolean z) throws RemoteException;

    void setNetworkSelectionModeAutomatic(int i) throws RemoteException;

    boolean setNetworkSelectionModeManual(int i, OperatorInfo operatorInfo, boolean z) throws RemoteException;

    boolean setOperatorBrandOverride(int i, String str) throws RemoteException;

    void setPolicyDataEnabled(boolean z, int i) throws RemoteException;

    boolean setPreferredNetworkType(int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    boolean setRadio(boolean z) throws RemoteException;

    void setRadioCapability(RadioAccessFamily[] radioAccessFamilyArr) throws RemoteException;

    boolean setRadioForSubscriber(int i, boolean z) throws RemoteException;

    void setRadioIndicationUpdateMode(int i, int i2, int i3) throws RemoteException;

    boolean setRadioPower(boolean z) throws RemoteException;

    boolean setRoamingOverride(int i, List<String> list, List<String> list2, List<String> list3, List<String> list4) throws RemoteException;

    void setRttCapabilitySetting(int i, boolean z) throws RemoteException;

    void setSimPowerStateForSlot(int i, int i2) throws RemoteException;

    void setUserDataEnabled(int i, boolean z) throws RemoteException;

    void setVoWiFiModeSetting(int i, int i2) throws RemoteException;

    void setVoWiFiNonPersistent(int i, boolean z, int i2) throws RemoteException;

    void setVoWiFiRoamingModeSetting(int i, int i2) throws RemoteException;

    void setVoWiFiRoamingSettingEnabled(int i, boolean z) throws RemoteException;

    void setVoWiFiSettingEnabled(int i, boolean z) throws RemoteException;

    void setVoiceActivationState(int i, int i2) throws RemoteException;

    boolean setVoiceMailNumber(int i, String str, String str2) throws RemoteException;

    void setVoicemailRingtoneUri(String str, PhoneAccountHandle phoneAccountHandle, Uri uri) throws RemoteException;

    void setVoicemailVibrationEnabled(String str, PhoneAccountHandle phoneAccountHandle, boolean z) throws RemoteException;

    void setVtSettingEnabled(int i, boolean z) throws RemoteException;

    void shutdownMobileRadios() throws RemoteException;

    void stopNetworkScan(int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    boolean supplyPin(String str) throws RemoteException;

    boolean supplyPinForSubscriber(int i, String str) throws RemoteException;

    int[] supplyPinReportResult(String str) throws RemoteException;

    int[] supplyPinReportResultForSubscriber(int i, String str) throws RemoteException;

    boolean supplyPuk(String str, String str2) throws RemoteException;

    boolean supplyPukForSubscriber(int i, String str, String str2) throws RemoteException;

    int[] supplyPukReportResult(String str, String str2) throws RemoteException;

    int[] supplyPukReportResultForSubscriber(int i, String str, String str2) throws RemoteException;

    void switchMultiSimConfig(int i) throws RemoteException;

    boolean switchSlots(int[] iArr) throws RemoteException;

    @UnsupportedAppUsage
    void toggleRadioOnOff() throws RemoteException;

    void toggleRadioOnOffForSubscriber(int i) throws RemoteException;

    void unregisterImsProvisioningChangedCallback(int i, IImsConfigCallback iImsConfigCallback) throws RemoteException;

    void unregisterImsRegistrationCallback(int i, IImsRegistrationCallback iImsRegistrationCallback) throws RemoteException;

    void unregisterMmTelCapabilityCallback(int i, IImsCapabilityCallback iImsCapabilityCallback) throws RemoteException;

    void updateEmergencyNumberListTestMode(int i, EmergencyNumber emergencyNumber) throws RemoteException;

    @UnsupportedAppUsage
    void updateServiceLocation() throws RemoteException;

    void updateServiceLocationForSubscriber(int i) throws RemoteException;
}
