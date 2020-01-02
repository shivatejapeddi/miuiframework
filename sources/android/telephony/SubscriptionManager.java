package android.telephony;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.app.BroadcastOptions;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.INetworkPolicyManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelUuid;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.MediaStore;
import android.telephony.euicc.EuiccManager;
import android.util.DisplayMetrics;
import android.util.Log;
import com.android.internal.telephony.IOnSubscriptionsChangedListener;
import com.android.internal.telephony.IOnSubscriptionsChangedListener.Stub;
import com.android.internal.telephony.ISetOpportunisticDataCallback;
import com.android.internal.telephony.ISub;
import com.android.internal.telephony.ITelephonyRegistry;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SubscriptionManager {
    public static final String ACCESS_RULES = "access_rules";
    public static final String ACTION_DEFAULT_SMS_SUBSCRIPTION_CHANGED = "android.telephony.action.DEFAULT_SMS_SUBSCRIPTION_CHANGED";
    public static final String ACTION_DEFAULT_SUBSCRIPTION_CHANGED = "android.telephony.action.DEFAULT_SUBSCRIPTION_CHANGED";
    public static final String ACTION_MANAGE_SUBSCRIPTION_PLANS = "android.telephony.action.MANAGE_SUBSCRIPTION_PLANS";
    public static final String ACTION_REFRESH_SUBSCRIPTION_PLANS = "android.telephony.action.REFRESH_SUBSCRIPTION_PLANS";
    public static final String ACTION_SUBSCRIPTION_PLANS_CHANGED = "android.telephony.action.SUBSCRIPTION_PLANS_CHANGED";
    @SystemApi
    public static final Uri ADVANCED_CALLING_ENABLED_CONTENT_URI = Uri.withAppendedPath(CONTENT_URI, "advanced_calling");
    public static final String CARD_ID = "card_id";
    public static final String CARRIER_ID = "carrier_id";
    public static final String CARRIER_NAME = "carrier_name";
    public static final String CB_ALERT_REMINDER_INTERVAL = "alert_reminder_interval";
    public static final String CB_ALERT_SOUND_DURATION = "alert_sound_duration";
    public static final String CB_ALERT_SPEECH = "enable_alert_speech";
    public static final String CB_ALERT_VIBRATE = "enable_alert_vibrate";
    public static final String CB_AMBER_ALERT = "enable_cmas_amber_alerts";
    public static final String CB_CHANNEL_50_ALERT = "enable_channel_50_alerts";
    public static final String CB_CMAS_TEST_ALERT = "enable_cmas_test_alerts";
    public static final String CB_EMERGENCY_ALERT = "enable_emergency_alerts";
    public static final String CB_ETWS_TEST_ALERT = "enable_etws_test_alerts";
    public static final String CB_EXTREME_THREAT_ALERT = "enable_cmas_extreme_threat_alerts";
    public static final String CB_OPT_OUT_DIALOG = "show_cmas_opt_out_dialog";
    public static final String CB_SEVERE_THREAT_ALERT = "enable_cmas_severe_threat_alerts";
    public static final String COLOR = "color";
    public static final int COLOR_1 = 0;
    public static final int COLOR_2 = 1;
    public static final int COLOR_3 = 2;
    public static final int COLOR_4 = 3;
    public static final int COLOR_DEFAULT = 0;
    @UnsupportedAppUsage
    public static final Uri CONTENT_URI = Uri.parse("content://telephony/siminfo");
    public static final String DATA_ENABLED_OVERRIDE_RULES = "data_enabled_override_rules";
    public static final String DATA_ROAMING = "data_roaming";
    public static final int DATA_ROAMING_DEFAULT = 0;
    public static final int DATA_ROAMING_DISABLE = 0;
    public static final int DATA_ROAMING_ENABLE = 1;
    private static final boolean DBG = false;
    public static final int DEFAULT_NAME_RES = 17039374;
    public static final int DEFAULT_PHONE_INDEX = Integer.MAX_VALUE;
    public static final int DEFAULT_SIM_SLOT_INDEX = Integer.MAX_VALUE;
    public static final int DEFAULT_SUBSCRIPTION_ID = Integer.MAX_VALUE;
    public static final String DISPLAY_NAME = "display_name";
    public static final int DISPLAY_NUMBER_DEFAULT = 1;
    public static final int DISPLAY_NUMBER_FIRST = 1;
    public static final String DISPLAY_NUMBER_FORMAT = "display_number_format";
    public static final int DISPLAY_NUMBER_LAST = 2;
    public static final int DISPLAY_NUMBER_NONE = 0;
    public static final int DUMMY_SUBSCRIPTION_ID_BASE = -2;
    public static final String EHPLMNS = "ehplmns";
    public static final String ENHANCED_4G_MODE_ENABLED = "volte_vt_enabled";
    public static final String EXTRA_SUBSCRIPTION_INDEX = "android.telephony.extra.SUBSCRIPTION_INDEX";
    public static final String GROUP_OWNER = "group_owner";
    public static final String GROUP_UUID = "group_uuid";
    public static final String HPLMNS = "hplmns";
    public static final String ICC_ID = "icc_id";
    public static final String IMSI = "imsi";
    public static final int INVALID_PHONE_INDEX = -1;
    public static final int INVALID_SIM_SLOT_INDEX = -1;
    public static final int INVALID_SUBSCRIPTION_ID = -1;
    public static final String ISO_COUNTRY_CODE = "iso_country_code";
    public static final String IS_EMBEDDED = "is_embedded";
    public static final String IS_METERED = "is_metered";
    public static final String IS_OPPORTUNISTIC = "is_opportunistic";
    public static final String IS_REMOVABLE = "is_removable";
    private static final String LOG_TAG = "SubscriptionManager";
    public static final int MAX_SUBSCRIPTION_ID_VALUE = 2147483646;
    public static final String MCC = "mcc";
    public static final String MCC_STRING = "mcc_string";
    public static final int MIN_SUBSCRIPTION_ID_VALUE = 0;
    public static final String MNC = "mnc";
    public static final String MNC_STRING = "mnc_string";
    public static final String NAME_SOURCE = "name_source";
    public static final int NAME_SOURCE_CARRIER = 3;
    public static final int NAME_SOURCE_DEFAULT_SOURCE = 0;
    public static final int NAME_SOURCE_SIM_SOURCE = 1;
    public static final int NAME_SOURCE_UNDEFINDED = -1;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public static final int NAME_SOURCE_USER_INPUT = 2;
    public static final String NUMBER = "number";
    public static final String PROFILE_CLASS = "profile_class";
    @SystemApi
    public static final int PROFILE_CLASS_DEFAULT = -1;
    @SystemApi
    public static final int PROFILE_CLASS_OPERATIONAL = 2;
    @SystemApi
    public static final int PROFILE_CLASS_PROVISIONING = 1;
    @SystemApi
    public static final int PROFILE_CLASS_TESTING = 0;
    @SystemApi
    public static final int PROFILE_CLASS_UNSET = -1;
    public static final int SIM_NOT_INSERTED = -1;
    public static final int SIM_PROVISIONED = 0;
    public static final String SIM_PROVISIONING_STATUS = "sim_provisioning_status";
    public static final String SIM_SLOT_INDEX = "sim_id";
    public static final int SLOT_INDEX_FOR_REMOTE_SIM_SUB = -1;
    public static final String SUBSCRIPTION_TYPE = "subscription_type";
    public static final int SUBSCRIPTION_TYPE_LOCAL_SIM = 0;
    public static final int SUBSCRIPTION_TYPE_REMOTE_SIM = 1;
    public static final String SUB_DEFAULT_CHANGED_ACTION = "android.intent.action.SUB_DEFAULT_CHANGED";
    public static final String UNIQUE_KEY_SUBSCRIPTION_ID = "_id";
    private static final boolean VDBG = false;
    @SystemApi
    public static final Uri VT_ENABLED_CONTENT_URI = Uri.withAppendedPath(CONTENT_URI, "vt_enabled");
    public static final String VT_IMS_ENABLED = "vt_ims_enabled";
    @SystemApi
    public static final Uri WFC_ENABLED_CONTENT_URI = Uri.withAppendedPath(CONTENT_URI, "wfc");
    public static final String WFC_IMS_ENABLED = "wfc_ims_enabled";
    public static final String WFC_IMS_MODE = "wfc_ims_mode";
    public static final String WFC_IMS_ROAMING_ENABLED = "wfc_ims_roaming_enabled";
    public static final String WFC_IMS_ROAMING_MODE = "wfc_ims_roaming_mode";
    @SystemApi
    public static final Uri WFC_MODE_CONTENT_URI = Uri.withAppendedPath(CONTENT_URI, "wfc_mode");
    @SystemApi
    public static final Uri WFC_ROAMING_ENABLED_CONTENT_URI = Uri.withAppendedPath(CONTENT_URI, "wfc_roaming_enabled");
    @SystemApi
    public static final Uri WFC_ROAMING_MODE_CONTENT_URI = Uri.withAppendedPath(CONTENT_URI, "wfc_roaming_mode");
    @Deprecated
    public static final String WHITE_LISTED_APN_DATA = "white_listed_apn_data";
    private final Context mContext;
    private volatile INetworkPolicyManager mNetworkPolicy;

    private interface CallISubMethodHelper {
        int callMethod(ISub iSub) throws RemoteException;
    }

    public static class OnOpportunisticSubscriptionsChangedListener {
        IOnSubscriptionsChangedListener callback = new Stub() {
            public void onSubscriptionsChanged() {
                long identity = Binder.clearCallingIdentity();
                try {
                    OnOpportunisticSubscriptionsChangedListener.this.mExecutor.execute(new -$$Lambda$SubscriptionManager$OnOpportunisticSubscriptionsChangedListener$1$3LINuEtkXs3dEn49nQkzD0NIY3E(this));
                } finally {
                    Binder.restoreCallingIdentity(identity);
                }
            }

            public /* synthetic */ void lambda$onSubscriptionsChanged$0$SubscriptionManager$OnOpportunisticSubscriptionsChangedListener$1() {
                OnOpportunisticSubscriptionsChangedListener.this.onOpportunisticSubscriptionsChanged();
            }
        };
        private Executor mExecutor;

        public void onOpportunisticSubscriptionsChanged() {
        }

        private void setExecutor(Executor executor) {
            this.mExecutor = executor;
        }

        private void log(String s) {
            Rlog.d(SubscriptionManager.LOG_TAG, s);
        }
    }

    public static class OnSubscriptionsChangedListener {
        IOnSubscriptionsChangedListener callback;
        private final Handler mHandler;

        private class OnSubscriptionsChangedListenerHandler extends Handler {
            OnSubscriptionsChangedListenerHandler() {
            }

            OnSubscriptionsChangedListenerHandler(Looper looper) {
                super(looper);
            }

            public void handleMessage(Message msg) {
                OnSubscriptionsChangedListener.this.onSubscriptionsChanged();
            }
        }

        public OnSubscriptionsChangedListener() {
            this.callback = new Stub() {
                public void onSubscriptionsChanged() {
                    OnSubscriptionsChangedListener.this.mHandler.sendEmptyMessage(0);
                }
            };
            this.mHandler = new OnSubscriptionsChangedListenerHandler();
        }

        public OnSubscriptionsChangedListener(Looper looper) {
            this.callback = /* anonymous class already generated */;
            this.mHandler = new OnSubscriptionsChangedListenerHandler(looper);
        }

        public void onSubscriptionsChanged() {
        }

        private void log(String s) {
            Rlog.d(SubscriptionManager.LOG_TAG, s);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ProfileClass {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SubscriptionType {
    }

    public static Uri getUriForSubscriptionId(int subscriptionId) {
        return Uri.withAppendedPath(CONTENT_URI, String.valueOf(subscriptionId));
    }

    @UnsupportedAppUsage
    public SubscriptionManager(Context context) {
        this.mContext = context;
    }

    @Deprecated
    public static SubscriptionManager from(Context context) {
        return (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
    }

    private final INetworkPolicyManager getNetworkPolicy() {
        if (this.mNetworkPolicy == null) {
            this.mNetworkPolicy = INetworkPolicyManager.Stub.asInterface(ServiceManager.getService(Context.NETWORK_POLICY_SERVICE));
        }
        return this.mNetworkPolicy;
    }

    public void addOnSubscriptionsChangedListener(OnSubscriptionsChangedListener listener) {
        Context context = this.mContext;
        String pkgName = context != null ? context.getOpPackageName() : MediaStore.UNKNOWN_STRING;
        try {
            ITelephonyRegistry tr = ITelephonyRegistry.Stub.asInterface(ServiceManager.getService("telephony.registry"));
            if (tr != null) {
                tr.addOnSubscriptionsChangedListener(pkgName, listener.callback);
            }
        } catch (RemoteException ex) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Remote exception ITelephonyRegistry ");
            stringBuilder.append(ex);
            Log.e(LOG_TAG, stringBuilder.toString());
        }
    }

    public void removeOnSubscriptionsChangedListener(OnSubscriptionsChangedListener listener) {
        Context context = this.mContext;
        String pkgForDebug = context != null ? context.getOpPackageName() : MediaStore.UNKNOWN_STRING;
        try {
            ITelephonyRegistry tr = ITelephonyRegistry.Stub.asInterface(ServiceManager.getService("telephony.registry"));
            if (tr != null) {
                tr.removeOnSubscriptionsChangedListener(pkgForDebug, listener.callback);
            }
        } catch (RemoteException ex) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Remote exception ITelephonyRegistry ");
            stringBuilder.append(ex);
            Log.e(LOG_TAG, stringBuilder.toString());
        }
    }

    public void addOnOpportunisticSubscriptionsChangedListener(Executor executor, OnOpportunisticSubscriptionsChangedListener listener) {
        if (executor != null && listener != null) {
            Context context = this.mContext;
            String pkgName = context != null ? context.getOpPackageName() : MediaStore.UNKNOWN_STRING;
            listener.setExecutor(executor);
            try {
                ITelephonyRegistry tr = ITelephonyRegistry.Stub.asInterface(ServiceManager.getService("telephony.registry"));
                if (tr != null) {
                    tr.addOnOpportunisticSubscriptionsChangedListener(pkgName, listener.callback);
                }
            } catch (RemoteException ex) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Remote exception ITelephonyRegistry ");
                stringBuilder.append(ex);
                Log.e(LOG_TAG, stringBuilder.toString());
            }
        }
    }

    public void removeOnOpportunisticSubscriptionsChangedListener(OnOpportunisticSubscriptionsChangedListener listener) {
        Preconditions.checkNotNull(listener, "listener cannot be null");
        Context context = this.mContext;
        String pkgForDebug = context != null ? context.getOpPackageName() : MediaStore.UNKNOWN_STRING;
        try {
            ITelephonyRegistry tr = ITelephonyRegistry.Stub.asInterface(ServiceManager.getService("telephony.registry"));
            if (tr != null) {
                tr.removeOnSubscriptionsChangedListener(pkgForDebug, listener.callback);
            }
        } catch (RemoteException ex) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Remote exception ITelephonyRegistry ");
            stringBuilder.append(ex);
            Log.e(LOG_TAG, stringBuilder.toString());
        }
    }

    public SubscriptionInfo getActiveSubscriptionInfo(int subId) {
        if (!isValidSubscriptionId(subId)) {
            return null;
        }
        SubscriptionInfo subInfo = null;
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                subInfo = iSub.getActiveSubscriptionInfo(subId, this.mContext.getOpPackageName());
            }
        } catch (RemoteException e) {
        }
        return subInfo;
    }

    public SubscriptionInfo getActiveSubscriptionInfoForIccIndex(String iccId) {
        if (iccId == null) {
            logd("[getActiveSubscriptionInfoForIccIndex]- null iccid");
            return null;
        }
        SubscriptionInfo result = null;
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                result = iSub.getActiveSubscriptionInfoForIccId(iccId, this.mContext.getOpPackageName());
            }
        } catch (RemoteException e) {
        }
        return result;
    }

    public SubscriptionInfo getActiveSubscriptionInfoForSimSlotIndex(int slotIndex) {
        if (isValidSlotIndex(slotIndex)) {
            SubscriptionInfo result = null;
            try {
                ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
                if (iSub != null) {
                    result = iSub.getActiveSubscriptionInfoForSimSlotIndex(slotIndex, this.mContext.getOpPackageName());
                }
            } catch (RemoteException e) {
            }
            return result;
        }
        logd("[getActiveSubscriptionInfoForSimSlotIndex]- invalid slotIndex");
        return null;
    }

    @UnsupportedAppUsage
    public List<SubscriptionInfo> getAllSubscriptionInfoList() {
        List<SubscriptionInfo> result = null;
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                result = iSub.getAllSubInfoList(this.mContext.getOpPackageName());
            }
        } catch (RemoteException e) {
        }
        if (result == null) {
            return new ArrayList();
        }
        return result;
    }

    public List<SubscriptionInfo> getActiveSubscriptionInfoList() {
        return getActiveSubscriptionInfoList(true);
    }

    public List<SubscriptionInfo> getActiveSubscriptionInfoList(boolean userVisibleOnly) {
        List<SubscriptionInfo> activeList = null;
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                activeList = iSub.getActiveSubscriptionInfoList(this.mContext.getOpPackageName());
            }
        } catch (RemoteException e) {
        }
        if (!userVisibleOnly || activeList == null) {
            return activeList;
        }
        return (List) activeList.stream().filter(new -$$Lambda$SubscriptionManager$R_uORt9bKcmEo6JnjiGP2KgjIOQ(this)).collect(Collectors.toList());
    }

    public /* synthetic */ boolean lambda$getActiveSubscriptionInfoList$0$SubscriptionManager(SubscriptionInfo subInfo) {
        return isSubscriptionVisible(subInfo);
    }

    @SystemApi
    public List<SubscriptionInfo> getAvailableSubscriptionInfoList() {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                return iSub.getAvailableSubscriptionInfoList(this.mContext.getOpPackageName());
            }
            return null;
        } catch (RemoteException e) {
            return null;
        }
    }

    public List<SubscriptionInfo> getAccessibleSubscriptionInfoList() {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                return iSub.getAccessibleSubscriptionInfoList(this.mContext.getOpPackageName());
            }
            return null;
        } catch (RemoteException e) {
            return null;
        }
    }

    @SystemApi
    public void requestEmbeddedSubscriptionInfoListRefresh() {
        int cardId = TelephonyManager.from(this.mContext).getCardIdForDefaultEuicc();
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                iSub.requestEmbeddedSubscriptionInfoListRefresh(cardId);
            }
        } catch (RemoteException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("requestEmbeddedSubscriptionInfoListFresh for card = ");
            stringBuilder.append(cardId);
            stringBuilder.append(" failed.");
            logd(stringBuilder.toString());
        }
    }

    @SystemApi
    public void requestEmbeddedSubscriptionInfoListRefresh(int cardId) {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                iSub.requestEmbeddedSubscriptionInfoListRefresh(cardId);
            }
        } catch (RemoteException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("requestEmbeddedSubscriptionInfoListFresh for card = ");
            stringBuilder.append(cardId);
            stringBuilder.append(" failed.");
            logd(stringBuilder.toString());
        }
    }

    @UnsupportedAppUsage
    public int getAllSubscriptionInfoCount() {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                return iSub.getAllSubInfoCount(this.mContext.getOpPackageName());
            }
            return 0;
        } catch (RemoteException e) {
            return 0;
        }
    }

    public int getActiveSubscriptionInfoCount() {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                return iSub.getActiveSubInfoCount(this.mContext.getOpPackageName());
            }
            return 0;
        } catch (RemoteException e) {
            return 0;
        }
    }

    public int getActiveSubscriptionInfoCountMax() {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                return iSub.getActiveSubInfoCountMax();
            }
            return 0;
        } catch (RemoteException e) {
            return 0;
        }
    }

    public Uri addSubscriptionInfoRecord(String iccId, int slotIndex) {
        if (iccId == null) {
            logd("[addSubscriptionInfoRecord]- null iccId");
        }
        if (!isValidSlotIndex(slotIndex)) {
            logd("[addSubscriptionInfoRecord]- invalid slotIndex");
        }
        addSubscriptionInfoRecord(iccId, null, slotIndex, 0);
        return null;
    }

    public void addSubscriptionInfoRecord(String uniqueId, String displayName, int slotIndex, int subscriptionType) {
        String str = LOG_TAG;
        if (uniqueId == null) {
            Log.e(str, "[addSubscriptionInfoRecord]- uniqueId is null");
            return;
        }
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub == null) {
                Log.e(str, "[addSubscriptionInfoRecord]- ISub service is null");
                return;
            }
            int result = iSub.addSubInfo(uniqueId, displayName, slotIndex, subscriptionType);
            if (result < 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Adding of subscription didn't succeed: error = ");
                stringBuilder.append(result);
                Log.e(str, stringBuilder.toString());
            } else {
                logd("successfully added new subscription");
            }
        } catch (RemoteException e) {
        }
    }

    public void removeSubscriptionInfoRecord(String uniqueId, int subscriptionType) {
        String str = LOG_TAG;
        if (uniqueId == null) {
            Log.e(str, "[addSubscriptionInfoRecord]- uniqueId is null");
            return;
        }
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub == null) {
                Log.e(str, "[removeSubscriptionInfoRecord]- ISub service is null");
                return;
            }
            int result = iSub.removeSubInfo(uniqueId, subscriptionType);
            if (result < 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Removal of subscription didn't succeed: error = ");
                stringBuilder.append(result);
                Log.e(str, stringBuilder.toString());
            } else {
                logd("successfully removed subscription");
            }
        } catch (RemoteException e) {
        }
    }

    @UnsupportedAppUsage
    public int setIconTint(int tint, int subId) {
        return setSubscriptionPropertyHelper(subId, "setIconTint", new -$$Lambda$SubscriptionManager$RmtPOPFQV3mOx5HejDzImseJ0Qg(tint, subId));
    }

    @UnsupportedAppUsage
    public int setDisplayName(String displayName, int subId, int nameSource) {
        return setSubscriptionPropertyHelper(subId, "setDisplayName", new -$$Lambda$SubscriptionManager$OS3WICha4HbZhTnWrKCxeu6dr6g(displayName, subId, nameSource));
    }

    @UnsupportedAppUsage
    public int setDisplayNumber(String number, int subId) {
        if (number == null) {
            logd("[setDisplayNumber]- fail");
            return -1;
        }
        return setSubscriptionPropertyHelper(subId, "setDisplayNumber", new -$$Lambda$SubscriptionManager$3ws2BzXOcyDc-7TPZx2HIBCIjbs(number, subId));
    }

    @UnsupportedAppUsage
    public int setDataRoaming(int roaming, int subId) {
        return setSubscriptionPropertyHelper(subId, "setDataRoaming", new -$$Lambda$SubscriptionManager$xw48SQFFAHLgpsIZZWeq63fMykw(roaming, subId));
    }

    public static int getSlotIndex(int subscriptionId) {
        isValidSubscriptionId(subscriptionId);
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                return iSub.getSlotIndex(subscriptionId);
            }
            return -1;
        } catch (RemoteException e) {
            return -1;
        }
    }

    public int[] getSubscriptionIds(int slotIndex) {
        return getSubId(slotIndex);
    }

    @UnsupportedAppUsage
    public static int[] getSubId(int slotIndex) {
        if (isValidSlotIndex(slotIndex)) {
            int[] subId = null;
            try {
                ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
                if (iSub != null) {
                    subId = iSub.getSubId(slotIndex);
                }
            } catch (RemoteException e) {
            }
            return subId;
        }
        logd("[getSubId]- fail");
        return null;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public static int getPhoneId(int subId) {
        if (!isValidSubscriptionId(subId)) {
            return -1;
        }
        int result = -1;
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                result = iSub.getPhoneId(subId);
            }
        } catch (RemoteException e) {
        }
        return result;
    }

    private static void logd(String msg) {
        Rlog.d(LOG_TAG, msg);
    }

    private static void loge(String msg) {
        Rlog.e(LOG_TAG, msg);
    }

    public static int getDefaultSubscriptionId() {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                return iSub.getDefaultSubId();
            }
            return -1;
        } catch (RemoteException e) {
            return -1;
        }
    }

    public static int getDefaultVoiceSubscriptionId() {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                return iSub.getDefaultVoiceSubId();
            }
            return -1;
        } catch (RemoteException e) {
            return -1;
        }
    }

    public void setDefaultVoiceSubId(int subId) {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                iSub.setDefaultVoiceSubId(subId);
            }
        } catch (RemoteException e) {
        }
    }

    @UnsupportedAppUsage
    public SubscriptionInfo getDefaultVoiceSubscriptionInfo() {
        return getActiveSubscriptionInfo(getDefaultVoiceSubscriptionId());
    }

    @UnsupportedAppUsage
    public static int getDefaultVoicePhoneId() {
        return getPhoneId(getDefaultVoiceSubscriptionId());
    }

    public static int getDefaultSmsSubscriptionId() {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                return iSub.getDefaultSmsSubId();
            }
            return -1;
        } catch (RemoteException e) {
            return -1;
        }
    }

    @SystemApi
    public void setDefaultSmsSubId(int subscriptionId) {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                iSub.setDefaultSmsSubId(subscriptionId);
            }
        } catch (RemoteException ex) {
            ex.rethrowFromSystemServer();
        }
    }

    public SubscriptionInfo getDefaultSmsSubscriptionInfo() {
        return getActiveSubscriptionInfo(getDefaultSmsSubscriptionId());
    }

    @UnsupportedAppUsage
    public int getDefaultSmsPhoneId() {
        return getPhoneId(getDefaultSmsSubscriptionId());
    }

    public static int getDefaultDataSubscriptionId() {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                return iSub.getDefaultDataSubId();
            }
            return -1;
        } catch (RemoteException e) {
            return -1;
        }
    }

    @SystemApi
    public void setDefaultDataSubId(int subscriptionId) {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                iSub.setDefaultDataSubId(subscriptionId);
            }
        } catch (RemoteException e) {
        }
    }

    @UnsupportedAppUsage
    public SubscriptionInfo getDefaultDataSubscriptionInfo() {
        return getActiveSubscriptionInfo(getDefaultDataSubscriptionId());
    }

    @UnsupportedAppUsage
    public int getDefaultDataPhoneId() {
        return getPhoneId(getDefaultDataSubscriptionId());
    }

    public void clearSubscriptionInfo() {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                iSub.clearSubInfo();
            }
        } catch (RemoteException e) {
        }
    }

    public boolean allDefaultsSelected() {
        if (isValidSubscriptionId(getDefaultDataSubscriptionId()) && isValidSubscriptionId(getDefaultSmsSubscriptionId()) && isValidSubscriptionId(getDefaultVoiceSubscriptionId())) {
            return true;
        }
        return false;
    }

    public static boolean isValidSubscriptionId(int subscriptionId) {
        return subscriptionId > -1;
    }

    public static boolean isUsableSubscriptionId(int subscriptionId) {
        return isUsableSubIdValue(subscriptionId);
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public static boolean isUsableSubIdValue(int subId) {
        return subId >= 0 && subId <= 2147483646;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public static boolean isValidSlotIndex(int slotIndex) {
        return slotIndex >= 0 && slotIndex < TelephonyManager.getDefault().getSimCount();
    }

    @UnsupportedAppUsage
    public static boolean isValidPhoneId(int phoneId) {
        return phoneId >= 0 && phoneId < TelephonyManager.getDefault().getPhoneCount();
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public static void putPhoneIdAndSubIdExtra(Intent intent, int phoneId) {
        int[] subIds = getSubId(phoneId);
        if (subIds == null || subIds.length <= 0) {
            logd("putPhoneIdAndSubIdExtra: no valid subs");
            intent.putExtra("phone", phoneId);
            return;
        }
        putPhoneIdAndSubIdExtra(intent, phoneId, subIds[0]);
    }

    @UnsupportedAppUsage
    public static void putPhoneIdAndSubIdExtra(Intent intent, int phoneId, int subId) {
        intent.putExtra(PhoneConstants.SUBSCRIPTION_KEY, subId);
        intent.putExtra("android.telephony.extra.SUBSCRIPTION_INDEX", subId);
        intent.putExtra("phone", phoneId);
        miui.telephony.SubscriptionManager.putSlotIdPhoneIdAndSubIdExtra(intent, phoneId, phoneId, subId);
    }

    @UnsupportedAppUsage
    public int[] getActiveSubscriptionIdList() {
        int[] subId = null;
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                subId = iSub.getActiveSubIdList(true);
            }
        } catch (RemoteException e) {
        }
        if (subId == null) {
            return new int[0];
        }
        return subId;
    }

    public boolean isNetworkRoaming(int subId) {
        if (getPhoneId(subId) < 0) {
            return false;
        }
        return TelephonyManager.getDefault().isNetworkRoaming(subId);
    }

    public static int getSimStateForSlotIndex(int slotIndex) {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                return iSub.getSimStateForSlotIndex(slotIndex);
            }
            return 0;
        } catch (RemoteException e) {
            return 0;
        }
    }

    public static void setSubscriptionProperty(int subId, String propKey, String propValue) {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                iSub.setSubscriptionProperty(subId, propKey, propValue);
            }
        } catch (RemoteException e) {
        }
    }

    private static String getSubscriptionProperty(int subId, String propKey, Context context) {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                return iSub.getSubscriptionProperty(subId, propKey, context.getOpPackageName());
            }
            return null;
        } catch (RemoteException e) {
            return null;
        }
    }

    public static boolean getBooleanSubscriptionProperty(int subId, String propKey, boolean defValue, Context context) {
        String result = getSubscriptionProperty(subId, propKey, context);
        if (result != null) {
            try {
                boolean z = true;
                if (Integer.parseInt(result) != 1) {
                    z = false;
                }
                return z;
            } catch (NumberFormatException e) {
                logd("getBooleanSubscriptionProperty NumberFormat exception");
            }
        }
        return defValue;
    }

    public static int getIntegerSubscriptionProperty(int subId, String propKey, int defValue, Context context) {
        String result = getSubscriptionProperty(subId, propKey, context);
        if (result != null) {
            try {
                return Integer.parseInt(result);
            } catch (NumberFormatException e) {
                logd("getBooleanSubscriptionProperty NumberFormat exception");
            }
        }
        return defValue;
    }

    @UnsupportedAppUsage
    public static Resources getResourcesForSubId(Context context, int subId) {
        return getResourcesForSubId(context, subId, false);
    }

    public static Resources getResourcesForSubId(Context context, int subId, boolean useRootLocale) {
        SubscriptionInfo subInfo = from(context).getActiveSubscriptionInfo(subId);
        Configuration config = context.getResources().getConfiguration();
        Configuration newConfig = new Configuration();
        newConfig.setTo(config);
        if (subInfo != null) {
            newConfig.mcc = subInfo.getMcc();
            newConfig.mnc = subInfo.getMnc();
            if (newConfig.mnc == 0) {
                newConfig.mnc = 65535;
            }
        }
        if (useRootLocale) {
            newConfig.setLocale(Locale.ROOT);
        }
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        DisplayMetrics newMetrics = new DisplayMetrics();
        newMetrics.setTo(metrics);
        return new Resources(context.getResources().getAssets(), newMetrics, newConfig);
    }

    public boolean isActiveSubscriptionId(int subscriptionId) {
        return isActiveSubId(subscriptionId);
    }

    @UnsupportedAppUsage
    public boolean isActiveSubId(int subId) {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                return iSub.isActiveSubId(subId, this.mContext.getOpPackageName());
            }
        } catch (RemoteException e) {
        }
        return false;
    }

    public List<SubscriptionPlan> getSubscriptionPlans(int subId) {
        try {
            SubscriptionPlan[] subscriptionPlans = getNetworkPolicy().getSubscriptionPlans(subId, this.mContext.getOpPackageName());
            return subscriptionPlans == null ? Collections.emptyList() : Arrays.asList(subscriptionPlans);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setSubscriptionPlans(int subId, List<SubscriptionPlan> plans) {
        try {
            getNetworkPolicy().setSubscriptionPlans(subId, (SubscriptionPlan[]) plans.toArray(new SubscriptionPlan[plans.size()]), this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private String getSubscriptionPlansOwner(int subId) {
        try {
            return getNetworkPolicy().getSubscriptionPlansOwner(subId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setSubscriptionOverrideUnmetered(int subId, boolean overrideUnmetered, long timeoutMillis) {
        try {
            getNetworkPolicy().setSubscriptionOverride(subId, 1, overrideUnmetered ? 1 : 0, timeoutMillis, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setSubscriptionOverrideCongested(int subId, boolean overrideCongested, long timeoutMillis) {
        try {
            getNetworkPolicy().setSubscriptionOverride(subId, 2, overrideCongested ? 2 : 0, timeoutMillis, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public Intent createManageSubscriptionIntent(int subId) {
        String owner = getSubscriptionPlansOwner(subId);
        if (owner == null || getSubscriptionPlans(subId).isEmpty()) {
            return null;
        }
        Intent intent = new Intent(ACTION_MANAGE_SUBSCRIPTION_PLANS);
        intent.setPackage(owner);
        intent.putExtra("android.telephony.extra.SUBSCRIPTION_INDEX", subId);
        if (this.mContext.getPackageManager().queryIntentActivities(intent, 65536).isEmpty()) {
            return null;
        }
        return intent;
    }

    private Intent createRefreshSubscriptionIntent(int subId) {
        String owner = getSubscriptionPlansOwner(subId);
        if (owner == null || getSubscriptionPlans(subId).isEmpty()) {
            return null;
        }
        Intent intent = new Intent(ACTION_REFRESH_SUBSCRIPTION_PLANS);
        intent.addFlags(268435456);
        intent.setPackage(owner);
        intent.putExtra("android.telephony.extra.SUBSCRIPTION_INDEX", subId);
        if (this.mContext.getPackageManager().queryBroadcastReceivers(intent, 0).isEmpty()) {
            return null;
        }
        return intent;
    }

    public boolean isSubscriptionPlansRefreshSupported(int subId) {
        return createRefreshSubscriptionIntent(subId) != null;
    }

    public void requestSubscriptionPlansRefresh(int subId) {
        Intent intent = createRefreshSubscriptionIntent(subId);
        BroadcastOptions options = BroadcastOptions.makeBasic();
        options.setTemporaryAppWhitelistDuration(TimeUnit.MINUTES.toMillis(1));
        this.mContext.sendBroadcast(intent, null, options.toBundle());
    }

    public boolean canManageSubscription(SubscriptionInfo info) {
        return canManageSubscription(info, this.mContext.getPackageName());
    }

    public boolean canManageSubscription(SubscriptionInfo info, String packageName) {
        if (!info.isEmbedded()) {
            throw new IllegalArgumentException("Not an embedded subscription");
        } else if (info.getAccessRules() == null) {
            return false;
        } else {
            try {
                PackageInfo packageInfo = this.mContext.getPackageManager().getPackageInfo(packageName, 64);
                for (UiccAccessRule rule : info.getAccessRules()) {
                    if (rule.getCarrierPrivilegeStatus(packageInfo) == 1) {
                        return true;
                    }
                }
                return false;
            } catch (NameNotFoundException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown package: ");
                stringBuilder.append(packageName);
                throw new IllegalArgumentException(stringBuilder.toString(), e);
            }
        }
    }

    @SystemApi
    public void setPreferredDataSubscriptionId(int subId, boolean needValidation, final Executor executor, final Consumer<Integer> callback) {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                iSub.setPreferredDataSubscriptionId(subId, needValidation, new ISetOpportunisticDataCallback.Stub() {
                    public void onComplete(int result) {
                        Executor executor = executor;
                        if (executor != null) {
                            Consumer consumer = callback;
                            if (consumer != null) {
                                Binder.withCleanCallingIdentity(new -$$Lambda$SubscriptionManager$1$qFZ-q9KyfPAkHTrQPCRyO6OQ_pc(executor, consumer, result));
                            }
                        }
                    }
                });
            }
        } catch (RemoteException e) {
        }
    }

    public int getPreferredDataSubscriptionId() {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                return iSub.getPreferredDataSubscriptionId();
            }
            return Integer.MAX_VALUE;
        } catch (RemoteException e) {
            return Integer.MAX_VALUE;
        }
    }

    public List<SubscriptionInfo> getOpportunisticSubscriptions() {
        Context context = this.mContext;
        String pkgForDebug = context != null ? context.getOpPackageName() : MediaStore.UNKNOWN_STRING;
        List<SubscriptionInfo> subInfoList = null;
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                subInfoList = iSub.getOpportunisticSubscriptions(pkgForDebug);
            }
        } catch (RemoteException e) {
        }
        if (subInfoList == null) {
            return new ArrayList();
        }
        return subInfoList;
    }

    public void switchToSubscription(int subId, PendingIntent callbackIntent) {
        Preconditions.checkNotNull(callbackIntent, "callbackIntent cannot be null");
        new EuiccManager(this.mContext).switchToSubscription(subId, callbackIntent);
    }

    public boolean setOpportunistic(boolean opportunistic, int subId) {
        return setSubscriptionPropertyHelper(subId, "setOpportunistic", new -$$Lambda$SubscriptionManager$NazcIP1h3U0cfnY--L174e3u4tk(this, opportunistic, subId)) == 1;
    }

    public /* synthetic */ int lambda$setOpportunistic$5$SubscriptionManager(boolean opportunistic, int subId, ISub iSub) throws RemoteException {
        return iSub.setOpportunistic(opportunistic, subId, this.mContext.getOpPackageName());
    }

    public ParcelUuid createSubscriptionGroup(List<Integer> subIdList) {
        Preconditions.checkNotNull(subIdList, "can't create group for null subId list");
        Context context = this.mContext;
        String pkgForDebug = context != null ? context.getOpPackageName() : MediaStore.UNKNOWN_STRING;
        ParcelUuid groupUuid = null;
        int[] subIdArray = subIdList.stream().mapToInt(-$$Lambda$SubscriptionManager$W41XrJh1c8ZX_i9kWtj1rBU9l8o.INSTANCE).toArray();
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                groupUuid = iSub.createSubscriptionGroup(subIdArray, pkgForDebug);
            } else if (!isSystemProcess()) {
                throw new IllegalStateException("telephony service is null.");
            }
        } catch (RemoteException ex) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("createSubscriptionGroup RemoteException ");
            stringBuilder.append(ex);
            loge(stringBuilder.toString());
            if (!isSystemProcess()) {
                ex.rethrowAsRuntimeException();
            }
        }
        return groupUuid;
    }

    public void addSubscriptionsIntoGroup(List<Integer> subIdList, ParcelUuid groupUuid) {
        Preconditions.checkNotNull(subIdList, "subIdList can't be null.");
        Preconditions.checkNotNull(groupUuid, "groupUuid can't be null.");
        Context context = this.mContext;
        String pkgForDebug = context != null ? context.getOpPackageName() : MediaStore.UNKNOWN_STRING;
        int[] subIdArray = subIdList.stream().mapToInt(-$$Lambda$SubscriptionManager$D5_PmvQ13e0qLtSnBvNd4R7l2qA.INSTANCE).toArray();
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                iSub.addSubscriptionsIntoGroup(subIdArray, groupUuid, pkgForDebug);
            } else if (!isSystemProcess()) {
                throw new IllegalStateException("telephony service is null.");
            }
        } catch (RemoteException ex) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("addSubscriptionsIntoGroup RemoteException ");
            stringBuilder.append(ex);
            loge(stringBuilder.toString());
            if (!isSystemProcess()) {
                ex.rethrowAsRuntimeException();
            }
        }
    }

    private boolean isSystemProcess() {
        return Process.myUid() == 1000;
    }

    public void removeSubscriptionsFromGroup(List<Integer> subIdList, ParcelUuid groupUuid) {
        Preconditions.checkNotNull(subIdList, "subIdList can't be null.");
        Preconditions.checkNotNull(groupUuid, "groupUuid can't be null.");
        Context context = this.mContext;
        String pkgForDebug = context != null ? context.getOpPackageName() : MediaStore.UNKNOWN_STRING;
        int[] subIdArray = subIdList.stream().mapToInt(-$$Lambda$SubscriptionManager$EEe2NsDpuDogw8-UijVBhj7Vuhk.INSTANCE).toArray();
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                iSub.removeSubscriptionsFromGroup(subIdArray, groupUuid, pkgForDebug);
            } else if (!isSystemProcess()) {
                throw new IllegalStateException("telephony service is null.");
            }
        } catch (RemoteException ex) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("removeSubscriptionsFromGroup RemoteException ");
            stringBuilder.append(ex);
            loge(stringBuilder.toString());
            if (!isSystemProcess()) {
                ex.rethrowAsRuntimeException();
            }
        }
    }

    public List<SubscriptionInfo> getSubscriptionsInGroup(ParcelUuid groupUuid) {
        Preconditions.checkNotNull(groupUuid, "groupUuid can't be null");
        Context context = this.mContext;
        String pkgForDebug = context != null ? context.getOpPackageName() : MediaStore.UNKNOWN_STRING;
        List<SubscriptionInfo> result = null;
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                result = iSub.getSubscriptionsInGroup(groupUuid, pkgForDebug);
            } else if (!isSystemProcess()) {
                throw new IllegalStateException("telephony service is null.");
            }
        } catch (RemoteException ex) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("removeSubscriptionsFromGroup RemoteException ");
            stringBuilder.append(ex);
            loge(stringBuilder.toString());
            if (!isSystemProcess()) {
                ex.rethrowAsRuntimeException();
            }
        }
        return result;
    }

    private boolean isSubscriptionVisible(SubscriptionInfo info) {
        boolean hasCarrierPrivilegePermission = false;
        if (info == null) {
            return false;
        }
        if (info.getGroupUuid() == null || !info.isOpportunistic()) {
            return true;
        }
        if (TelephonyManager.from(this.mContext).hasCarrierPrivileges(info.getSubscriptionId()) || (info.isEmbedded() && canManageSubscription(info))) {
            hasCarrierPrivilegePermission = true;
        }
        return hasCarrierPrivilegePermission;
    }

    public List<SubscriptionInfo> getSelectableSubscriptionInfoList() {
        List<SubscriptionInfo> availableList = getAvailableSubscriptionInfoList();
        if (availableList == null) {
            return null;
        }
        List<SubscriptionInfo> selectableList = new ArrayList();
        Map<ParcelUuid, SubscriptionInfo> groupMap = new HashMap();
        for (SubscriptionInfo info : availableList) {
            if (isSubscriptionVisible(info)) {
                ParcelUuid groupUuid = info.getGroupUuid();
                if (groupUuid == null) {
                    selectableList.add(info);
                } else if (!groupMap.containsKey(groupUuid) || (((SubscriptionInfo) groupMap.get(groupUuid)).getSimSlotIndex() == -1 && info.getSimSlotIndex() != -1)) {
                    selectableList.remove(groupMap.get(groupUuid));
                    selectableList.add(info);
                    groupMap.put(groupUuid, info);
                }
            }
        }
        return selectableList;
    }

    @SystemApi
    public boolean setSubscriptionEnabled(int subscriptionId, boolean enable) {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                return iSub.setSubscriptionEnabled(enable, subscriptionId);
            }
        } catch (RemoteException e) {
        }
        return false;
    }

    @SystemApi
    public boolean isSubscriptionEnabled(int subscriptionId) {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                return iSub.isSubscriptionEnabled(subscriptionId);
            }
        } catch (RemoteException e) {
        }
        return false;
    }

    @SystemApi
    public int getEnabledSubscriptionId(int slotIndex) {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                return iSub.getEnabledSubscriptionId(slotIndex);
            }
            return -1;
        } catch (RemoteException e) {
            return -1;
        }
    }

    public boolean setAlwaysAllowMmsData(int subId, boolean alwaysAllow) {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                return iSub.setAlwaysAllowMmsData(subId, alwaysAllow);
            }
        } catch (RemoteException ex) {
            if (!isSystemProcess()) {
                ex.rethrowAsRuntimeException();
            }
        }
        return false;
    }

    private int setSubscriptionPropertyHelper(int subId, String methodName, CallISubMethodHelper helper) {
        if (isValidSubscriptionId(subId)) {
            int result = 0;
            try {
                ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
                if (iSub != null) {
                    result = helper.callMethod(iSub);
                }
            } catch (RemoteException e) {
            }
            return result;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        stringBuilder.append(methodName);
        stringBuilder.append("]- fail");
        logd(stringBuilder.toString());
        return -1;
    }

    public static int getActiveDataSubscriptionId() {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                return iSub.getActiveDataSubscriptionId();
            }
        } catch (RemoteException e) {
        }
        return -1;
    }
}
