package android.telephony;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.app.ActivityThread;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkStats;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelableException;
import android.os.PersistableBundle;
import android.os.Process;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.WorkSource;
import android.provider.MediaStore;
import android.provider.Settings.Global;
import android.provider.Settings.SettingNotFoundException;
import android.service.carrier.CarrierIdentifier;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.TelephonyScanManager.NetworkScanCallback;
import android.telephony.emergency.EmergencyNumber;
import android.telephony.ims.aidl.IImsConfig;
import android.telephony.ims.aidl.IImsMmTelFeature;
import android.telephony.ims.aidl.IImsRcsFeature;
import android.telephony.ims.aidl.IImsRegistration;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.util.SeempLog;
import com.android.ims.internal.IImsServiceFeatureCallback;
import com.android.internal.R;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.internal.telecom.ITelecomService;
import com.android.internal.telephony.CellNetworkScanResult;
import com.android.internal.telephony.INumberVerificationCallback;
import com.android.internal.telephony.IOns;
import com.android.internal.telephony.IPhoneSubInfo;
import com.android.internal.telephony.IPhoneSubInfo.Stub;
import com.android.internal.telephony.ISetOpportunisticDataCallback;
import com.android.internal.telephony.ITelephony;
import com.android.internal.telephony.ITelephonyRegistry;
import com.android.internal.telephony.IUpdateAvailableNetworksCallback;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.OperatorInfo;
import com.android.internal.telephony.PhoneConstants.State;
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.util.Preconditions;
import dalvik.system.VMRuntime;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import miui.maml.util.net.SimpleRequest;
import miui.telephony.TelephonyManagerEx;

public class TelephonyManager {
    @SystemApi
    public static final String ACTION_ANOMALY_REPORTED = "android.telephony.action.ANOMALY_REPORTED";
    public static final String ACTION_CALL_DISCONNECT_CAUSE_CHANGED = "android.intent.action.CALL_DISCONNECT_CAUSE";
    public static final String ACTION_CARRIER_MESSAGING_CLIENT_SERVICE = "android.telephony.action.CARRIER_MESSAGING_CLIENT_SERVICE";
    public static final String ACTION_CONFIGURE_VOICEMAIL = "android.telephony.action.CONFIGURE_VOICEMAIL";
    public static final String ACTION_DATA_STALL_DETECTED = "android.intent.action.DATA_STALL_DETECTED";
    public static final String ACTION_EMERGENCY_ASSISTANCE = "android.telephony.action.EMERGENCY_ASSISTANCE";
    public static final String ACTION_NETWORK_COUNTRY_CHANGED = "android.telephony.action.NETWORK_COUNTRY_CHANGED";
    public static final String ACTION_PHONE_STATE_CHANGED = "android.intent.action.PHONE_STATE";
    public static final String ACTION_PRECISE_CALL_STATE_CHANGED = "android.intent.action.PRECISE_CALL_STATE";
    @Deprecated
    @UnsupportedAppUsage
    public static final String ACTION_PRECISE_DATA_CONNECTION_STATE_CHANGED = "android.intent.action.PRECISE_DATA_CONNECTION_STATE_CHANGED";
    public static final String ACTION_PRIMARY_SUBSCRIPTION_LIST_CHANGED = "android.telephony.action.PRIMARY_SUBSCRIPTION_LIST_CHANGED";
    public static final String ACTION_RESPOND_VIA_MESSAGE = "android.intent.action.RESPOND_VIA_MESSAGE";
    public static final String ACTION_SECRET_CODE = "android.telephony.action.SECRET_CODE";
    public static final String ACTION_SHOW_VOICEMAIL_NOTIFICATION = "android.telephony.action.SHOW_VOICEMAIL_NOTIFICATION";
    @SystemApi
    public static final String ACTION_SIM_APPLICATION_STATE_CHANGED = "android.telephony.action.SIM_APPLICATION_STATE_CHANGED";
    @SystemApi
    public static final String ACTION_SIM_CARD_STATE_CHANGED = "android.telephony.action.SIM_CARD_STATE_CHANGED";
    @SystemApi
    public static final String ACTION_SIM_SLOT_STATUS_CHANGED = "android.telephony.action.SIM_SLOT_STATUS_CHANGED";
    public static final String ACTION_SUBSCRIPTION_CARRIER_IDENTITY_CHANGED = "android.telephony.action.SUBSCRIPTION_CARRIER_IDENTITY_CHANGED";
    public static final String ACTION_SUBSCRIPTION_SPECIFIC_CARRIER_IDENTITY_CHANGED = "android.telephony.action.SUBSCRIPTION_SPECIFIC_CARRIER_IDENTITY_CHANGED";
    public static final int APPTYPE_CSIM = 4;
    public static final int APPTYPE_ISIM = 5;
    public static final int APPTYPE_RUIM = 3;
    public static final int APPTYPE_SIM = 1;
    public static final int APPTYPE_USIM = 2;
    public static final int AUTHTYPE_EAP_AKA = 129;
    public static final int AUTHTYPE_EAP_SIM = 128;
    public static final int CALL_STATE_IDLE = 0;
    public static final int CALL_STATE_OFFHOOK = 2;
    public static final int CALL_STATE_RINGING = 1;
    public static final int CARD_POWER_DOWN = 0;
    public static final int CARD_POWER_UP = 1;
    public static final int CARD_POWER_UP_PASS_THROUGH = 2;
    @SystemApi
    public static final int CARRIER_PRIVILEGE_STATUS_ERROR_LOADING_RULES = -2;
    @SystemApi
    public static final int CARRIER_PRIVILEGE_STATUS_HAS_ACCESS = 1;
    @SystemApi
    public static final int CARRIER_PRIVILEGE_STATUS_NO_ACCESS = 0;
    @SystemApi
    public static final int CARRIER_PRIVILEGE_STATUS_RULES_NOT_LOADED = -1;
    public static final int CDMA_ROAMING_MODE_AFFILIATED = 1;
    public static final int CDMA_ROAMING_MODE_ANY = 2;
    public static final int CDMA_ROAMING_MODE_HOME = 0;
    public static final int CDMA_ROAMING_MODE_RADIO_DEFAULT = -1;
    public static final int DATA_ACTIVITY_DORMANT = 4;
    public static final int DATA_ACTIVITY_IN = 1;
    public static final int DATA_ACTIVITY_INOUT = 3;
    public static final int DATA_ACTIVITY_NONE = 0;
    public static final int DATA_ACTIVITY_OUT = 2;
    public static final int DATA_CONNECTED = 2;
    public static final int DATA_CONNECTING = 1;
    public static final int DATA_DISCONNECTED = 0;
    public static final int DATA_SUSPENDED = 3;
    public static final int DATA_UNKNOWN = -1;
    public static final boolean EMERGENCY_ASSISTANCE_ENABLED = true;
    public static final String EVENT_CALL_FORWARDED = "android.telephony.event.EVENT_CALL_FORWARDED";
    public static final String EVENT_DOWNGRADE_DATA_DISABLED = "android.telephony.event.EVENT_DOWNGRADE_DATA_DISABLED";
    public static final String EVENT_DOWNGRADE_DATA_LIMIT_REACHED = "android.telephony.event.EVENT_DOWNGRADE_DATA_LIMIT_REACHED";
    public static final String EVENT_HANDOVER_TO_WIFI_FAILED = "android.telephony.event.EVENT_HANDOVER_TO_WIFI_FAILED";
    public static final String EVENT_HANDOVER_VIDEO_FROM_LTE_TO_WIFI = "android.telephony.event.EVENT_HANDOVER_VIDEO_FROM_LTE_TO_WIFI";
    public static final String EVENT_HANDOVER_VIDEO_FROM_WIFI_TO_LTE = "android.telephony.event.EVENT_HANDOVER_VIDEO_FROM_WIFI_TO_LTE";
    public static final String EVENT_NOTIFY_INTERNATIONAL_CALL_ON_WFC = "android.telephony.event.EVENT_NOTIFY_INTERNATIONAL_CALL_ON_WFC";
    public static final String EVENT_SUPPLEMENTARY_SERVICE_NOTIFICATION = "android.telephony.event.EVENT_SUPPLEMENTARY_SERVICE_NOTIFICATION";
    @SystemApi
    public static final String EXTRA_ANOMALY_DESCRIPTION = "android.telephony.extra.ANOMALY_DESCRIPTION";
    @SystemApi
    public static final String EXTRA_ANOMALY_ID = "android.telephony.extra.ANOMALY_ID";
    public static final String EXTRA_BACKGROUND_CALL_STATE = "background_state";
    public static final String EXTRA_CALL_VOICEMAIL_INTENT = "android.telephony.extra.CALL_VOICEMAIL_INTENT";
    public static final String EXTRA_CARRIER_ID = "android.telephony.extra.CARRIER_ID";
    public static final String EXTRA_CARRIER_NAME = "android.telephony.extra.CARRIER_NAME";
    public static final String EXTRA_DATA_APN = "apn";
    public static final String EXTRA_DATA_APN_TYPE = "apnType";
    public static final String EXTRA_DATA_FAILURE_CAUSE = "failCause";
    public static final String EXTRA_DATA_LINK_PROPERTIES_KEY = "linkProperties";
    public static final String EXTRA_DATA_NETWORK_TYPE = "networkType";
    public static final String EXTRA_DATA_STATE = "state";
    public static final String EXTRA_DEFAULT_SUBSCRIPTION_SELECT_TYPE = "android.telephony.extra.DEFAULT_SUBSCRIPTION_SELECT_TYPE";
    public static final int EXTRA_DEFAULT_SUBSCRIPTION_SELECT_TYPE_ALL = 4;
    public static final int EXTRA_DEFAULT_SUBSCRIPTION_SELECT_TYPE_DATA = 1;
    public static final int EXTRA_DEFAULT_SUBSCRIPTION_SELECT_TYPE_NONE = 0;
    public static final int EXTRA_DEFAULT_SUBSCRIPTION_SELECT_TYPE_SMS = 3;
    public static final int EXTRA_DEFAULT_SUBSCRIPTION_SELECT_TYPE_VOICE = 2;
    public static final String EXTRA_DISCONNECT_CAUSE = "disconnect_cause";
    public static final String EXTRA_FOREGROUND_CALL_STATE = "foreground_state";
    public static final String EXTRA_HIDE_PUBLIC_SETTINGS = "android.telephony.extra.HIDE_PUBLIC_SETTINGS";
    @Deprecated
    public static final String EXTRA_INCOMING_NUMBER = "incoming_number";
    public static final String EXTRA_IS_REFRESH = "android.telephony.extra.IS_REFRESH";
    public static final String EXTRA_LAUNCH_VOICEMAIL_SETTINGS_INTENT = "android.telephony.extra.LAUNCH_VOICEMAIL_SETTINGS_INTENT";
    public static final String EXTRA_NETWORK_COUNTRY = "android.telephony.extra.NETWORK_COUNTRY";
    public static final String EXTRA_NOTIFICATION_CODE = "android.telephony.extra.NOTIFICATION_CODE";
    public static final String EXTRA_NOTIFICATION_COUNT = "android.telephony.extra.NOTIFICATION_COUNT";
    public static final String EXTRA_NOTIFICATION_MESSAGE = "android.telephony.extra.NOTIFICATION_MESSAGE";
    public static final String EXTRA_NOTIFICATION_TYPE = "android.telephony.extra.NOTIFICATION_TYPE";
    public static final String EXTRA_PHONE_ACCOUNT_HANDLE = "android.telephony.extra.PHONE_ACCOUNT_HANDLE";
    public static final String EXTRA_PRECISE_DISCONNECT_CAUSE = "precise_disconnect_cause";
    public static final String EXTRA_RECOVERY_ACTION = "recoveryAction";
    public static final String EXTRA_RINGING_CALL_STATE = "ringing_state";
    public static final String EXTRA_SIM_COMBINATION_NAMES = "android.telephony.extra.SIM_COMBINATION_NAMES";
    public static final String EXTRA_SIM_COMBINATION_WARNING_TYPE = "android.telephony.extra.SIM_COMBINATION_WARNING_TYPE";
    public static final int EXTRA_SIM_COMBINATION_WARNING_TYPE_DUAL_CDMA = 1;
    public static final int EXTRA_SIM_COMBINATION_WARNING_TYPE_NONE = 0;
    @SystemApi
    public static final String EXTRA_SIM_STATE = "android.telephony.extra.SIM_STATE";
    public static final String EXTRA_SPECIFIC_CARRIER_ID = "android.telephony.extra.SPECIFIC_CARRIER_ID";
    public static final String EXTRA_SPECIFIC_CARRIER_NAME = "android.telephony.extra.SPECIFIC_CARRIER_NAME";
    public static final String EXTRA_STATE = "state";
    public static final String EXTRA_STATE_IDLE = State.IDLE.toString();
    public static final String EXTRA_STATE_OFFHOOK = State.OFFHOOK.toString();
    public static final String EXTRA_STATE_RINGING = State.RINGING.toString();
    public static final String EXTRA_SUBSCRIPTION_ID = "android.telephony.extra.SUBSCRIPTION_ID";
    @SystemApi
    public static final String EXTRA_VISUAL_VOICEMAIL_ENABLED_BY_USER_BOOL = "android.telephony.extra.VISUAL_VOICEMAIL_ENABLED_BY_USER_BOOL";
    public static final String EXTRA_VOICEMAIL_NUMBER = "android.telephony.extra.VOICEMAIL_NUMBER";
    @SystemApi
    public static final String EXTRA_VOICEMAIL_SCRAMBLED_PIN_STRING = "android.telephony.extra.VOICEMAIL_SCRAMBLED_PIN_STRING";
    public static final int INDICATION_FILTER_DATA_CALL_DORMANCY_CHANGED = 4;
    public static final int INDICATION_FILTER_FULL_NETWORK_STATE = 2;
    public static final int INDICATION_FILTER_LINK_CAPACITY_ESTIMATE = 8;
    public static final int INDICATION_FILTER_PHYSICAL_CHANNEL_CONFIG = 16;
    public static final int INDICATION_FILTER_SIGNAL_STRENGTH = 1;
    public static final int INDICATION_UPDATE_MODE_IGNORE_SCREEN_OFF = 2;
    public static final int INDICATION_UPDATE_MODE_NORMAL = 1;
    public static final int KEY_TYPE_EPDG = 1;
    public static final int KEY_TYPE_WLAN = 2;
    public static final int MAX_NETWORK_TYPE = 20;
    private static final long MAX_NUMBER_VERIFICATION_TIMEOUT_MILLIS = 60000;
    public static final String METADATA_HIDE_VOICEMAIL_SETTINGS_MENU = "android.telephony.HIDE_VOICEMAIL_SETTINGS_MENU";
    public static final String MODEM_ACTIVITY_RESULT_KEY = "controller_activity";
    public static final int MULTISIM_ALLOWED = 0;
    public static final int MULTISIM_NOT_SUPPORTED_BY_CARRIER = 2;
    public static final int MULTISIM_NOT_SUPPORTED_BY_HARDWARE = 1;
    @UnsupportedAppUsage
    public static final int NETWORK_CLASS_2_G = 1;
    @UnsupportedAppUsage
    public static final int NETWORK_CLASS_3_G = 2;
    @UnsupportedAppUsage
    public static final int NETWORK_CLASS_4_G = 3;
    public static final int NETWORK_CLASS_UNKNOWN = 0;
    public static final int NETWORK_MODE_CDMA_EVDO = 4;
    public static final int NETWORK_MODE_CDMA_NO_EVDO = 5;
    public static final int NETWORK_MODE_EVDO_NO_CDMA = 6;
    public static final int NETWORK_MODE_GLOBAL = 7;
    public static final int NETWORK_MODE_GSM_ONLY = 1;
    public static final int NETWORK_MODE_GSM_UMTS = 3;
    public static final int NETWORK_MODE_LTE_CDMA_EVDO = 8;
    public static final int NETWORK_MODE_LTE_CDMA_EVDO_GSM_WCDMA = 10;
    public static final int NETWORK_MODE_LTE_GSM_WCDMA = 9;
    public static final int NETWORK_MODE_LTE_ONLY = 11;
    public static final int NETWORK_MODE_LTE_TDSCDMA = 15;
    public static final int NETWORK_MODE_LTE_TDSCDMA_CDMA_EVDO_GSM_WCDMA = 22;
    public static final int NETWORK_MODE_LTE_TDSCDMA_GSM = 17;
    public static final int NETWORK_MODE_LTE_TDSCDMA_GSM_WCDMA = 20;
    public static final int NETWORK_MODE_LTE_TDSCDMA_WCDMA = 19;
    public static final int NETWORK_MODE_LTE_WCDMA = 12;
    public static final int NETWORK_MODE_NR_LTE = 24;
    public static final int NETWORK_MODE_NR_LTE_CDMA_EVDO = 25;
    public static final int NETWORK_MODE_NR_LTE_CDMA_EVDO_GSM_WCDMA = 27;
    public static final int NETWORK_MODE_NR_LTE_GSM_WCDMA = 26;
    public static final int NETWORK_MODE_NR_LTE_TDSCDMA = 29;
    public static final int NETWORK_MODE_NR_LTE_TDSCDMA_CDMA_EVDO_GSM_WCDMA = 33;
    public static final int NETWORK_MODE_NR_LTE_TDSCDMA_GSM = 30;
    public static final int NETWORK_MODE_NR_LTE_TDSCDMA_GSM_WCDMA = 32;
    public static final int NETWORK_MODE_NR_LTE_TDSCDMA_WCDMA = 31;
    public static final int NETWORK_MODE_NR_LTE_WCDMA = 28;
    public static final int NETWORK_MODE_NR_ONLY = 23;
    public static final int NETWORK_MODE_TDSCDMA_CDMA_EVDO_GSM_WCDMA = 21;
    public static final int NETWORK_MODE_TDSCDMA_GSM = 16;
    public static final int NETWORK_MODE_TDSCDMA_GSM_WCDMA = 18;
    public static final int NETWORK_MODE_TDSCDMA_ONLY = 13;
    public static final int NETWORK_MODE_TDSCDMA_WCDMA = 14;
    public static final int NETWORK_MODE_WCDMA_ONLY = 2;
    public static final int NETWORK_MODE_WCDMA_PREF = 0;
    public static final int NETWORK_SELECTION_MODE_AUTO = 1;
    public static final int NETWORK_SELECTION_MODE_MANUAL = 2;
    public static final int NETWORK_SELECTION_MODE_UNKNOWN = 0;
    public static final int NETWORK_TYPE_1xRTT = 7;
    @SystemApi
    public static final long NETWORK_TYPE_BITMASK_1xRTT = 64;
    @SystemApi
    public static final long NETWORK_TYPE_BITMASK_CDMA = 8;
    @SystemApi
    public static final long NETWORK_TYPE_BITMASK_EDGE = 2;
    @SystemApi
    public static final long NETWORK_TYPE_BITMASK_EHRPD = 8192;
    @SystemApi
    public static final long NETWORK_TYPE_BITMASK_EVDO_0 = 16;
    @SystemApi
    public static final long NETWORK_TYPE_BITMASK_EVDO_A = 32;
    @SystemApi
    public static final long NETWORK_TYPE_BITMASK_EVDO_B = 2048;
    @SystemApi
    public static final long NETWORK_TYPE_BITMASK_GPRS = 1;
    @SystemApi
    public static final long NETWORK_TYPE_BITMASK_GSM = 32768;
    @SystemApi
    public static final long NETWORK_TYPE_BITMASK_HSDPA = 128;
    @SystemApi
    public static final long NETWORK_TYPE_BITMASK_HSPA = 512;
    @SystemApi
    public static final long NETWORK_TYPE_BITMASK_HSPAP = 16384;
    @SystemApi
    public static final long NETWORK_TYPE_BITMASK_HSUPA = 256;
    @SystemApi
    public static final long NETWORK_TYPE_BITMASK_IWLAN = 131072;
    @SystemApi
    public static final long NETWORK_TYPE_BITMASK_LTE = 4096;
    @SystemApi
    public static final long NETWORK_TYPE_BITMASK_LTE_CA = 262144;
    @SystemApi
    public static final long NETWORK_TYPE_BITMASK_NR = 524288;
    @SystemApi
    public static final long NETWORK_TYPE_BITMASK_TD_SCDMA = 65536;
    @SystemApi
    public static final long NETWORK_TYPE_BITMASK_UMTS = 4;
    @SystemApi
    public static final long NETWORK_TYPE_BITMASK_UNKNOWN = 0;
    public static final int NETWORK_TYPE_CDMA = 4;
    public static final int NETWORK_TYPE_EDGE = 2;
    public static final int NETWORK_TYPE_EHRPD = 14;
    public static final int NETWORK_TYPE_EVDO_0 = 5;
    public static final int NETWORK_TYPE_EVDO_A = 6;
    public static final int NETWORK_TYPE_EVDO_B = 12;
    public static final int NETWORK_TYPE_GPRS = 1;
    public static final int NETWORK_TYPE_GSM = 16;
    public static final int NETWORK_TYPE_HSDPA = 8;
    public static final int NETWORK_TYPE_HSPA = 10;
    public static final int NETWORK_TYPE_HSPAP = 15;
    public static final int NETWORK_TYPE_HSUPA = 9;
    public static final int NETWORK_TYPE_IDEN = 11;
    public static final int NETWORK_TYPE_IWLAN = 18;
    public static final int NETWORK_TYPE_LTE = 13;
    @UnsupportedAppUsage
    public static final int NETWORK_TYPE_LTE_CA = 19;
    public static final int NETWORK_TYPE_NR = 20;
    public static final int NETWORK_TYPE_TD_SCDMA = 17;
    public static final int NETWORK_TYPE_UMTS = 3;
    public static final int NETWORK_TYPE_UNKNOWN = 0;
    public static final int OTASP_NEEDED = 2;
    public static final int OTASP_NOT_NEEDED = 3;
    public static final int OTASP_SIM_UNPROVISIONED = 5;
    public static final int OTASP_UNINITIALIZED = 0;
    public static final int OTASP_UNKNOWN = 1;
    public static final String PHONE_PROCESS_NAME = "com.android.phone";
    public static final int PHONE_TYPE_CDMA = 2;
    public static final int PHONE_TYPE_GSM = 1;
    public static final int PHONE_TYPE_NONE = 0;
    public static final int PHONE_TYPE_SIP = 3;
    @SystemApi
    public static final int RADIO_POWER_OFF = 0;
    @SystemApi
    public static final int RADIO_POWER_ON = 1;
    @SystemApi
    public static final int RADIO_POWER_UNAVAILABLE = 2;
    @SystemApi
    public static final int SET_CARRIER_RESTRICTION_ERROR = 2;
    @SystemApi
    public static final int SET_CARRIER_RESTRICTION_NOT_SUPPORTED = 1;
    @SystemApi
    public static final int SET_CARRIER_RESTRICTION_SUCCESS = 0;
    public static final int SET_OPPORTUNISTIC_SUB_INACTIVE_SUBSCRIPTION = 2;
    public static final int SET_OPPORTUNISTIC_SUB_SUCCESS = 0;
    public static final int SET_OPPORTUNISTIC_SUB_VALIDATION_FAILED = 1;
    @SystemApi
    public static final int SIM_ACTIVATION_STATE_ACTIVATED = 2;
    @SystemApi
    public static final int SIM_ACTIVATION_STATE_ACTIVATING = 1;
    @SystemApi
    public static final int SIM_ACTIVATION_STATE_DEACTIVATED = 3;
    @SystemApi
    public static final int SIM_ACTIVATION_STATE_RESTRICTED = 4;
    @SystemApi
    public static final int SIM_ACTIVATION_STATE_UNKNOWN = 0;
    public static final int SIM_STATE_ABSENT = 1;
    public static final int SIM_STATE_CARD_IO_ERROR = 8;
    public static final int SIM_STATE_CARD_RESTRICTED = 9;
    @SystemApi
    public static final int SIM_STATE_LOADED = 10;
    public static final int SIM_STATE_NETWORK_LOCKED = 4;
    public static final int SIM_STATE_NOT_READY = 6;
    public static final int SIM_STATE_PERM_DISABLED = 7;
    public static final int SIM_STATE_PIN_REQUIRED = 2;
    @SystemApi
    public static final int SIM_STATE_PRESENT = 11;
    public static final int SIM_STATE_PUK_REQUIRED = 3;
    public static final int SIM_STATE_READY = 5;
    public static final int SIM_STATE_UNKNOWN = 0;
    @SystemApi
    public static final int SRVCC_STATE_HANDOVER_CANCELED = 3;
    @SystemApi
    public static final int SRVCC_STATE_HANDOVER_COMPLETED = 1;
    @SystemApi
    public static final int SRVCC_STATE_HANDOVER_FAILED = 2;
    @SystemApi
    public static final int SRVCC_STATE_HANDOVER_NONE = -1;
    @SystemApi
    public static final int SRVCC_STATE_HANDOVER_STARTED = 0;
    private static final String TAG = "TelephonyManager";
    public static final int UNINITIALIZED_CARD_ID = -2;
    public static final int UNKNOWN_CARRIER_ID = -1;
    public static final int UNKNOWN_CARRIER_ID_LIST_VERSION = -1;
    public static final int UNSUPPORTED_CARD_ID = -1;
    public static final int UPDATE_AVAILABLE_NETWORKS_ABORTED = 2;
    public static final int UPDATE_AVAILABLE_NETWORKS_INVALID_ARGUMENTS = 3;
    public static final int UPDATE_AVAILABLE_NETWORKS_NO_CARRIER_PRIVILEGE = 4;
    public static final int UPDATE_AVAILABLE_NETWORKS_SUCCESS = 0;
    public static final int UPDATE_AVAILABLE_NETWORKS_UNKNOWN_FAILURE = 1;
    public static final int USSD_ERROR_SERVICE_UNAVAIL = -2;
    public static final String USSD_RESPONSE = "USSD_RESPONSE";
    public static final int USSD_RETURN_FAILURE = -1;
    public static final int USSD_RETURN_SUCCESS = 100;
    public static final String VVM_TYPE_CVVM = "vvm_type_cvvm";
    public static final String VVM_TYPE_OMTP = "vvm_type_omtp";
    private static String multiSimConfig = SystemProperties.get(TelephonyProperties.PROPERTY_MULTI_SIM_CONFIG);
    private static TelephonyManager sInstance = new TelephonyManager();
    private static final String sKernelCmdLine = getProcCmdLine();
    private static final String sLteOnCdmaProductType = SystemProperties.get(TelephonyProperties.PROPERTY_LTE_ON_CDMA_PRODUCT_TYPE, "");
    private static final Pattern sProductTypePattern = Pattern.compile("\\sproduct_type\\s*=\\s*(\\w+)");
    private final Context mContext;
    private final int mSubId;
    @UnsupportedAppUsage
    private SubscriptionManager mSubscriptionManager;
    private TelephonyScanManager mTelephonyScanManager;

    /* renamed from: android.telephony.TelephonyManager$7 */
    static /* synthetic */ class AnonymousClass7 {
        static final /* synthetic */ int[] $SwitchMap$android$telephony$TelephonyManager$MultiSimVariants = new int[MultiSimVariants.values().length];

        static {
            try {
                $SwitchMap$android$telephony$TelephonyManager$MultiSimVariants[MultiSimVariants.UNKNOWN.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$telephony$TelephonyManager$MultiSimVariants[MultiSimVariants.DSDS.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$telephony$TelephonyManager$MultiSimVariants[MultiSimVariants.DSDA.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$telephony$TelephonyManager$MultiSimVariants[MultiSimVariants.TSTS.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface CallState {
    }

    public static abstract class CellInfoCallback {
        public static final int ERROR_MODEM_ERROR = 2;
        public static final int ERROR_TIMEOUT = 1;

        @Retention(RetentionPolicy.SOURCE)
        public @interface CellInfoCallbackError {
        }

        public abstract void onCellInfo(List<CellInfo> list);

        public void onError(int errorCode, Throwable detail) {
            onCellInfo(new ArrayList());
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DataState {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DefaultSubscriptionSelectType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface IndicationFilters {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface IndicationUpdateMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface IsMultiSimSupportedResult {
    }

    public enum MultiSimVariants {
        DSDS,
        DSDA,
        TSTS,
        UNKNOWN
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface NetworkSelectionMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface NetworkType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface NetworkTypeBitMask {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface PrefNetworkMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface RadioPowerState {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SetCarrierRestrictionResult {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SetOpportunisticSubscriptionResult {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SimActivationState {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SimCombinationWarningType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SrvccState {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface UiccAppType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface UpdateAvailableNetworksResult {
    }

    public static abstract class UssdResponseCallback {
        public void onReceiveUssdResponse(TelephonyManager telephonyManager, String request, CharSequence response) {
        }

        public void onReceiveUssdResponseFailed(TelephonyManager telephonyManager, String request, int failureCode) {
        }
    }

    public interface WifiCallingChoices {
        public static final int ALWAYS_USE = 0;
        public static final int ASK_EVERY_TIME = 1;
        public static final int NEVER_USE = 2;
    }

    @UnsupportedAppUsage
    public TelephonyManager(Context context) {
        this(context, Integer.MAX_VALUE);
    }

    @UnsupportedAppUsage
    public TelephonyManager(Context context, int subId) {
        this.mSubId = subId;
        Context appContext = context.getApplicationContext();
        if (appContext != null) {
            this.mContext = appContext;
        } else {
            this.mContext = context;
        }
        this.mSubscriptionManager = SubscriptionManager.from(this.mContext);
    }

    @UnsupportedAppUsage
    private TelephonyManager() {
        this.mContext = null;
        this.mSubId = -1;
    }

    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public static TelephonyManager getDefault() {
        return sInstance;
    }

    private String getOpPackageName() {
        Context context = this.mContext;
        if (context != null) {
            return context.getOpPackageName();
        }
        return ActivityThread.currentOpPackageName();
    }

    private boolean isSystemProcess() {
        return Process.myUid() == 1000;
    }

    @UnsupportedAppUsage
    public MultiSimVariants getMultiSimConfiguration() {
        String mSimConfig = SystemProperties.get(TelephonyProperties.PROPERTY_MULTI_SIM_CONFIG);
        if (mSimConfig.equals("dsds")) {
            return MultiSimVariants.DSDS;
        }
        if (mSimConfig.equals("dsda")) {
            return MultiSimVariants.DSDA;
        }
        if (mSimConfig.equals("tsts")) {
            return MultiSimVariants.TSTS;
        }
        return MultiSimVariants.UNKNOWN;
    }

    public int getPhoneCount() {
        int i = AnonymousClass7.$SwitchMap$android$telephony$TelephonyManager$MultiSimVariants[getMultiSimConfiguration().ordinal()];
        if (i == 1) {
            Context context = this.mContext;
            ConnectivityManager cm;
            if (context == null) {
                cm = null;
            } else {
                cm = (ConnectivityManager) context.getSystemService("connectivity");
            }
            if (isVoiceCapable() || isSmsCapable() || cm == null || cm.isNetworkSupported(0)) {
                return 1;
            }
            return 0;
        } else if (i == 2 || i == 3) {
            return 2;
        } else {
            if (i != 4) {
                return 1;
            }
            return 3;
        }
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public static TelephonyManager from(Context context) {
        return (TelephonyManager) context.getSystemService("phone");
    }

    public TelephonyManager createForSubscriptionId(int subId) {
        return new TelephonyManager(this.mContext, subId);
    }

    public TelephonyManager createForPhoneAccountHandle(PhoneAccountHandle phoneAccountHandle) {
        int subId = getSubIdForPhoneAccountHandle(phoneAccountHandle);
        if (SubscriptionManager.isValidSubscriptionId(subId)) {
            return new TelephonyManager(this.mContext, subId);
        }
        return null;
    }

    @UnsupportedAppUsage
    public boolean isMultiSimEnabled() {
        return multiSimConfig.equals("dsds") || multiSimConfig.equals("dsda") || multiSimConfig.equals("tsts");
    }

    public String getDeviceSoftwareVersion() {
        return getDeviceSoftwareVersion(getSlotIndex());
    }

    @UnsupportedAppUsage
    public String getDeviceSoftwareVersion(int slotIndex) {
        ITelephony telephony = getITelephony();
        String str = null;
        if (telephony == null) {
            return null;
        }
        try {
            str = telephony.getDeviceSoftwareVersionForSlot(slotIndex, getOpPackageName());
            return str;
        } catch (RemoteException e) {
            return str;
        } catch (NullPointerException e2) {
            return str;
        }
    }

    @Deprecated
    public String getDeviceId() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony == null) {
                return null;
            }
            return telephony.getDeviceId(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            return null;
        } catch (NullPointerException e2) {
            return null;
        }
    }

    @Deprecated
    public String getDeviceId(int slotIndex) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("");
        stringBuilder.append(slotIndex);
        SeempLog.record_str(8, stringBuilder.toString());
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info == null) {
                return null;
            }
            return info.getDeviceIdForPhone(slotIndex, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            return null;
        } catch (NullPointerException e2) {
            return null;
        }
    }

    public String getImei() {
        return getImei(getSlotIndex());
    }

    public String getImei(int slotIndex) {
        ITelephony telephony = getITelephony();
        String str = null;
        if (telephony == null) {
            return null;
        }
        try {
            str = telephony.getImeiForSlot(slotIndex, getOpPackageName());
            return str;
        } catch (RemoteException e) {
            return str;
        } catch (NullPointerException e2) {
            return str;
        }
    }

    public String getTypeAllocationCode() {
        return getTypeAllocationCode(getSlotIndex());
    }

    public String getTypeAllocationCode(int slotIndex) {
        ITelephony telephony = getITelephony();
        String str = null;
        if (telephony == null) {
            return null;
        }
        try {
            str = telephony.getTypeAllocationCodeForSlot(slotIndex);
            return str;
        } catch (RemoteException e) {
            return str;
        } catch (NullPointerException e2) {
            return str;
        }
    }

    public String getMeid() {
        return getMeid(getSlotIndex());
    }

    public String getMeid(int slotIndex) {
        ITelephony telephony = getITelephony();
        String str = null;
        if (telephony == null) {
            return null;
        }
        try {
            str = telephony.getMeidForSlot(slotIndex, getOpPackageName());
            return str;
        } catch (RemoteException e) {
            return str;
        } catch (NullPointerException e2) {
            return str;
        }
    }

    public String getManufacturerCode() {
        return getManufacturerCode(getSlotIndex());
    }

    public String getManufacturerCode(int slotIndex) {
        ITelephony telephony = getITelephony();
        String str = null;
        if (telephony == null) {
            return null;
        }
        try {
            str = telephony.getManufacturerCodeForSlot(slotIndex);
            return str;
        } catch (RemoteException e) {
            return str;
        } catch (NullPointerException e2) {
            return str;
        }
    }

    public String getNai() {
        return getNaiBySubscriberId(getSubId());
    }

    @UnsupportedAppUsage
    public String getNai(int slotIndex) {
        int[] subId = SubscriptionManager.getSubId(slotIndex);
        if (subId == null) {
            return null;
        }
        return getNaiBySubscriberId(subId[0]);
    }

    private String getNaiBySubscriberId(int subId) {
        String str = TAG;
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info == null) {
                return null;
            }
            String nai = info.getNaiForSubscriber(subId, this.mContext.getOpPackageName());
            if (Log.isLoggable(str, 2)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Nai = ");
                stringBuilder.append(nai);
                Rlog.v(str, stringBuilder.toString());
            }
            return nai;
        } catch (RemoteException e) {
            return null;
        } catch (NullPointerException e2) {
            return null;
        }
    }

    @Deprecated
    public CellLocation getCellLocation() {
        String str = TAG;
        SeempLog.record(49);
        try {
            ITelephony telephony = getITelephony();
            if (telephony == null) {
                Rlog.d(str, "getCellLocation returning null because telephony is null");
                return null;
            }
            Bundle bundle = telephony.getCellLocation(this.mContext.getOpPackageName());
            if (bundle != null) {
                if (!bundle.isEmpty()) {
                    CellLocation cl = CellLocation.newFromBundle(bundle);
                    if (cl != null) {
                        if (!cl.isEmpty()) {
                            return cl;
                        }
                    }
                    Rlog.d(str, "getCellLocation returning null because CellLocation is empty or phone type doesn't match CellLocation type");
                    return null;
                }
            }
            Rlog.d(str, "getCellLocation returning null because CellLocation is unavailable");
            return null;
        } catch (RemoteException ex) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getCellLocation returning null due to RemoteException ");
            stringBuilder.append(ex);
            Rlog.d(str, stringBuilder.toString());
            return null;
        }
    }

    public void enableLocationUpdates() {
        enableLocationUpdates(getSubId());
    }

    public void enableLocationUpdates(int subId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.enableLocationUpdatesForSubscriber(subId);
            }
        } catch (RemoteException | NullPointerException e) {
        }
    }

    public void disableLocationUpdates() {
        disableLocationUpdates(getSubId());
    }

    public void disableLocationUpdates(int subId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.disableLocationUpdatesForSubscriber(subId);
            }
        } catch (RemoteException | NullPointerException e) {
        }
    }

    @Deprecated
    public List<NeighboringCellInfo> getNeighboringCellInfo() {
        SeempLog.record(50);
        try {
            ITelephony telephony = getITelephony();
            if (telephony == null) {
                return null;
            }
            return telephony.getNeighboringCellInfo(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            return null;
        } catch (NullPointerException e2) {
            return null;
        }
    }

    @SystemApi
    public int getCurrentPhoneType() {
        return getCurrentPhoneType(getSubId());
    }

    @SystemApi
    public int getCurrentPhoneType(int subId) {
        int phoneId;
        if (subId == -1) {
            phoneId = 0;
        } else {
            phoneId = SubscriptionManager.getPhoneId(subId);
        }
        return getCurrentPhoneTypeForSlot(phoneId);
    }

    public int getCurrentPhoneTypeForSlot(int slotIndex) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getActivePhoneTypeForSlot(slotIndex);
            }
            return getPhoneTypeFromProperty(slotIndex);
        } catch (RemoteException e) {
            return getPhoneTypeFromProperty(slotIndex);
        } catch (NullPointerException e2) {
            return getPhoneTypeFromProperty(slotIndex);
        }
    }

    public int getPhoneType() {
        if (isVoiceCapable()) {
            return getCurrentPhoneType();
        }
        return 0;
    }

    private int getPhoneTypeFromProperty() {
        return getPhoneTypeFromProperty(getPhoneId());
    }

    @UnsupportedAppUsage
    private int getPhoneTypeFromProperty(int phoneId) {
        String type = getTelephonyProperty(phoneId, TelephonyProperties.CURRENT_ACTIVE_PHONE, null);
        if (type == null || type.isEmpty()) {
            return getPhoneTypeFromNetworkType(phoneId);
        }
        return Integer.parseInt(type);
    }

    private int getPhoneTypeFromNetworkType() {
        return getPhoneTypeFromNetworkType(getPhoneId());
    }

    private int getPhoneTypeFromNetworkType(int phoneId) {
        String mode = getTelephonyProperty(phoneId, "ro.telephony.default_network", null);
        if (mode == null || mode.isEmpty()) {
            return 0;
        }
        return getPhoneType(Integer.parseInt(mode));
    }

    @UnsupportedAppUsage
    public static int getPhoneType(int networkMode) {
        switch (networkMode) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 9:
            case 10:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 22:
                return 1;
            case 4:
            case 5:
            case 6:
                return 2;
            case 7:
            case 8:
            case 21:
                return 2;
            case 11:
                if (getLteOnCdmaModeStatic() == 1) {
                    return 2;
                }
                return 1;
            default:
                return 1;
        }
    }

    @UnsupportedAppUsage
    private static String getProcCmdLine() {
        String str = TAG;
        String cmdline = "";
        FileInputStream is = null;
        try {
            is = new FileInputStream("/proc/cmdline");
            byte[] buffer = new byte[2048];
            int count = is.read(buffer);
            if (count > 0) {
                cmdline = new String(buffer, 0, count);
            }
            try {
                is.close();
            } catch (IOException e) {
            }
        } catch (IOException e2) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("No /proc/cmdline exception=");
            stringBuilder.append(e2);
            Rlog.d(str, stringBuilder.toString());
            if (is != null) {
                is.close();
            }
        } catch (Throwable th) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e3) {
                }
            }
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("/proc/cmdline=");
        stringBuilder2.append(cmdline);
        Rlog.d(str, stringBuilder2.toString());
        return cmdline;
    }

    @SystemApi
    public static long getMaxNumberVerificationTimeoutMillis() {
        return 60000;
    }

    @UnsupportedAppUsage
    public static int getLteOnCdmaModeStatic() {
        String productType = "";
        int curVal = SystemProperties.getInt(TelephonyProperties.PROPERTY_LTE_ON_CDMA_DEVICE, -1);
        int retVal = curVal;
        if (retVal == -1) {
            Matcher matcher = sProductTypePattern.matcher(sKernelCmdLine);
            if (matcher.find()) {
                productType = matcher.group(1);
                if (sLteOnCdmaProductType.equals(productType)) {
                    retVal = 1;
                } else {
                    retVal = 0;
                }
            } else {
                retVal = 0;
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("getLteOnCdmaMode=");
        stringBuilder.append(retVal);
        stringBuilder.append(" curVal=");
        stringBuilder.append(curVal);
        stringBuilder.append(" product_type='");
        stringBuilder.append(productType);
        stringBuilder.append("' lteOnCdmaProductType='");
        stringBuilder.append(sLteOnCdmaProductType);
        stringBuilder.append("'");
        Rlog.d(TAG, stringBuilder.toString());
        return retVal;
    }

    public String getNetworkOperatorName() {
        return getNetworkOperatorName(getSubId());
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public String getNetworkOperatorName(int subId) {
        return getTelephonyProperty(SubscriptionManager.getPhoneId(subId), TelephonyProperties.PROPERTY_OPERATOR_ALPHA, "");
    }

    public String getNetworkOperator() {
        return getNetworkOperatorForPhone(getPhoneId());
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public String getNetworkOperator(int subId) {
        return getNetworkOperatorForPhone(SubscriptionManager.getPhoneId(subId));
    }

    @UnsupportedAppUsage
    public String getNetworkOperatorForPhone(int phoneId) {
        return getTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_OPERATOR_NUMERIC, "");
    }

    public String getNetworkSpecifier() {
        return String.valueOf(getSubId());
    }

    public PersistableBundle getCarrierConfig() {
        return ((CarrierConfigManager) this.mContext.getSystemService(CarrierConfigManager.class)).getConfigForSubId(getSubId());
    }

    public boolean isNetworkRoaming() {
        return isNetworkRoaming(getSubId());
    }

    @UnsupportedAppUsage
    public boolean isNetworkRoaming(int subId) {
        return Boolean.parseBoolean(getTelephonyProperty(SubscriptionManager.getPhoneId(subId), TelephonyProperties.PROPERTY_OPERATOR_ISROAMING, null));
    }

    public String getNetworkCountryIso() {
        return getNetworkCountryIsoForPhone(getPhoneId());
    }

    @UnsupportedAppUsage
    public String getNetworkCountryIso(int subId) {
        return getNetworkCountryIsoForPhone(getPhoneId(subId));
    }

    @UnsupportedAppUsage
    public String getNetworkCountryIsoForPhone(int phoneId) {
        String str = "";
        try {
            ITelephony telephony = getITelephony();
            if (telephony == null) {
                return str;
            }
            return telephony.getNetworkCountryIsoForPhone(phoneId);
        } catch (RemoteException e) {
            return str;
        }
    }

    public int getNetworkType() {
        return getNetworkType(getSubId(SubscriptionManager.getActiveDataSubscriptionId()));
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public int getNetworkType(int subId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getNetworkTypeForSubscriber(subId, getOpPackageName());
            }
            return 0;
        } catch (RemoteException e) {
            return 0;
        } catch (NullPointerException e2) {
            return 0;
        }
    }

    public int getDataNetworkType() {
        return getDataNetworkType(getSubId(SubscriptionManager.getActiveDataSubscriptionId()));
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public int getDataNetworkType(int subId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getDataNetworkTypeForSubscriber(subId, getOpPackageName());
            }
            return 0;
        } catch (RemoteException e) {
            return 0;
        } catch (NullPointerException e2) {
            return 0;
        }
    }

    public int getVoiceNetworkType() {
        return getVoiceNetworkType(getSubId());
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public int getVoiceNetworkType(int subId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getVoiceNetworkTypeForSubscriber(subId, getOpPackageName());
            }
            return 0;
        } catch (RemoteException e) {
            return 0;
        } catch (NullPointerException e2) {
            return 0;
        }
    }

    @UnsupportedAppUsage
    public static int getNetworkClass(int networkType) {
        switch (networkType) {
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
            case 16:
                return 1;
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 12:
            case 14:
            case 15:
            case 17:
                return 2;
            case 13:
            case 18:
            case 19:
                return 3;
            default:
                return 0;
        }
    }

    @UnsupportedAppUsage
    public String getNetworkTypeName() {
        return getNetworkTypeName(getNetworkType());
    }

    @UnsupportedAppUsage
    public static String getNetworkTypeName(int type) {
        switch (type) {
            case 1:
                return "GPRS";
            case 2:
                return "EDGE";
            case 3:
                return "UMTS";
            case 4:
                return "CDMA";
            case 5:
                return "CDMA - EvDo rev. 0";
            case 6:
                return "CDMA - EvDo rev. A";
            case 7:
                return "CDMA - 1xRTT";
            case 8:
                return "HSDPA";
            case 9:
                return "HSUPA";
            case 10:
                return "HSPA";
            case 11:
                return "iDEN";
            case 12:
                return "CDMA - EvDo rev. B";
            case 13:
                return "LTE";
            case 14:
                return "CDMA - eHRPD";
            case 15:
                return "HSPA+";
            case 16:
                return "GSM";
            case 17:
                return "TD_SCDMA";
            case 18:
                return "IWLAN";
            case 19:
                return "LTE_CA";
            case 20:
                return "NR";
            default:
                return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
        }
    }

    public boolean hasIccCard() {
        return hasIccCard(getSlotIndex());
    }

    @UnsupportedAppUsage
    public boolean hasIccCard(int slotIndex) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony == null) {
                return false;
            }
            return telephony.hasIccCardUsingSlotIndex(slotIndex);
        } catch (RemoteException e) {
            return false;
        } catch (NullPointerException e2) {
            return false;
        }
    }

    public int getSimState() {
        int simState = getSimStateIncludingLoaded();
        if (simState == 10) {
            return 5;
        }
        return simState;
    }

    private int getSimStateIncludingLoaded() {
        int slotIndex = getSlotIndex();
        if (slotIndex >= 0) {
            return SubscriptionManager.getSimStateForSlotIndex(slotIndex);
        }
        int i = 0;
        while (true) {
            int phoneCount = getPhoneCount();
            String str = "getSimState: default sim:";
            String str2 = TAG;
            if (i < phoneCount) {
                phoneCount = getSimState(i);
                if (phoneCount != 1) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(str);
                    stringBuilder.append(slotIndex);
                    stringBuilder.append(", sim state for slotIndex=");
                    stringBuilder.append(i);
                    stringBuilder.append(" is ");
                    stringBuilder.append(phoneCount);
                    stringBuilder.append(", return state as unknown");
                    Rlog.d(str2, stringBuilder.toString());
                    return 0;
                }
                i++;
            } else {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(str);
                stringBuilder2.append(slotIndex);
                stringBuilder2.append(", all SIMs absent, return state as absent");
                Rlog.d(str2, stringBuilder2.toString());
                return 1;
            }
        }
    }

    @SystemApi
    public int getSimCardState() {
        int simCardState = getSimState();
        if (simCardState == 0 || simCardState == 1 || simCardState == 8 || simCardState == 9) {
            return simCardState;
        }
        return 11;
    }

    @SystemApi
    public int getSimApplicationState() {
        int simApplicationState = getSimStateIncludingLoaded();
        if (!(simApplicationState == 0 || simApplicationState == 1)) {
            if (simApplicationState == 5) {
                return 6;
            }
            if (!(simApplicationState == 8 || simApplicationState == 9)) {
                return simApplicationState;
            }
        }
        return 0;
    }

    public int getSimState(int slotIndex) {
        int simState = SubscriptionManager.getSimStateForSlotIndex(slotIndex);
        if (simState == 10) {
            return 5;
        }
        return simState;
    }

    public String getSimOperator() {
        return getSimOperatorNumeric();
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public String getSimOperator(int subId) {
        return getSimOperatorNumeric(subId);
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public String getSimOperatorNumeric() {
        int subId = this.mSubId;
        if (!SubscriptionManager.isUsableSubIdValue(subId)) {
            subId = SubscriptionManager.getDefaultDataSubscriptionId();
            if (!SubscriptionManager.isUsableSubIdValue(subId)) {
                subId = SubscriptionManager.getDefaultSmsSubscriptionId();
                if (!SubscriptionManager.isUsableSubIdValue(subId)) {
                    subId = SubscriptionManager.getDefaultVoiceSubscriptionId();
                    if (!SubscriptionManager.isUsableSubIdValue(subId)) {
                        subId = SubscriptionManager.getDefaultSubscriptionId();
                    }
                }
            }
        }
        return getSimOperatorNumeric(subId);
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public String getSimOperatorNumeric(int subId) {
        return getSimOperatorNumericForPhone(SubscriptionManager.getPhoneId(subId));
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public String getSimOperatorNumericForPhone(int phoneId) {
        return getTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_ICC_OPERATOR_NUMERIC, "");
    }

    public String getSimOperatorName() {
        return getSimOperatorNameForPhone(getPhoneId());
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public String getSimOperatorName(int subId) {
        return getSimOperatorNameForPhone(SubscriptionManager.getPhoneId(subId));
    }

    @UnsupportedAppUsage
    public String getSimOperatorNameForPhone(int phoneId) {
        return getTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_ICC_OPERATOR_ALPHA, "");
    }

    public String getSimCountryIso() {
        return getSimCountryIsoForPhone(getPhoneId());
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public String getSimCountryIso(int subId) {
        return getSimCountryIsoForPhone(SubscriptionManager.getPhoneId(subId));
    }

    @UnsupportedAppUsage
    public String getSimCountryIsoForPhone(int phoneId) {
        return getTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_ICC_OPERATOR_ISO_COUNTRY, "");
    }

    public String getSimSerialNumber() {
        return getSimSerialNumber(getSubId());
    }

    @UnsupportedAppUsage
    public String getSimSerialNumber(int subId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("");
        stringBuilder.append(subId);
        SeempLog.record_str(388, stringBuilder.toString());
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info == null) {
                return null;
            }
            return info.getIccSerialNumberForSubscriber(subId, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            return null;
        } catch (NullPointerException e2) {
            return null;
        }
    }

    @UnsupportedAppUsage
    public int getLteOnCdmaMode() {
        return getLteOnCdmaMode(getSubId());
    }

    @UnsupportedAppUsage
    public int getLteOnCdmaMode(int subId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony == null) {
                return -1;
            }
            return telephony.getLteOnCdmaModeForSubscriber(subId, getOpPackageName());
        } catch (RemoteException e) {
            return -1;
        } catch (NullPointerException e2) {
            return -1;
        }
    }

    public int getCardIdForDefaultEuicc() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony == null) {
                return -2;
            }
            return telephony.getCardIdForDefaultEuicc(this.mSubId, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            return -2;
        }
    }

    public List<UiccCardInfo> getUiccCardsInfo() {
        String str = TAG;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getUiccCardsInfo(this.mContext.getOpPackageName());
            }
            Log.e(str, "Error in getUiccCardsInfo: unable to connect to Telephony service.");
            return new ArrayList();
        } catch (RemoteException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Error in getUiccCardsInfo: ");
            stringBuilder.append(e);
            Log.e(str, stringBuilder.toString());
            return new ArrayList();
        }
    }

    @SystemApi
    public UiccSlotInfo[] getUiccSlotsInfo() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony == null) {
                return null;
            }
            return telephony.getUiccSlotsInfo();
        } catch (RemoteException e) {
            return null;
        }
    }

    public void refreshUiccProfile() {
        try {
            getITelephony().refreshUiccProfile(this.mSubId);
        } catch (RemoteException ex) {
            Rlog.w(TAG, "RemoteException", ex);
        }
    }

    @SystemApi
    public boolean switchSlots(int[] physicalSlots) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony == null) {
                return false;
            }
            return telephony.switchSlots(physicalSlots);
        } catch (RemoteException e) {
            return false;
        }
    }

    @SystemApi
    public Map<Integer, Integer> getLogicalToPhysicalSlotMapping() {
        Map<Integer, Integer> slotMapping = new HashMap();
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                int[] slotMappingArray = telephony.getSlotsMapping();
                for (int i = 0; i < slotMappingArray.length; i++) {
                    slotMapping.put(Integer.valueOf(i), Integer.valueOf(slotMappingArray[i]));
                }
            }
        } catch (RemoteException e) {
            Log.e(TAG, "getSlotsMapping RemoteException", e);
        }
        return slotMapping;
    }

    public String getSubscriberId() {
        return getSubscriberId(getSubId());
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public String getSubscriberId(int subId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("");
        stringBuilder.append(subId);
        SeempLog.record_str(MetricsEvent.ACTION_WINDOW_DOCK_RESIZE, stringBuilder.toString());
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info == null) {
                return null;
            }
            return info.getSubscriberIdForSubscriber(subId, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            return null;
        } catch (NullPointerException e2) {
            return null;
        }
    }

    public ImsiEncryptionInfo getCarrierInfoForImsiEncryption(int keyType) {
        StringBuilder stringBuilder;
        String str = "IMSI error: key is required but not found";
        String str2 = TAG;
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info == null) {
                Rlog.e(str2, "IMSI error: Subscriber Info is null");
                return null;
            }
            int subId = getSubId(SubscriptionManager.getDefaultDataSubscriptionId());
            if (keyType != 1) {
                if (keyType != 2) {
                    throw new IllegalArgumentException("IMSI error: Invalid key type");
                }
            }
            ImsiEncryptionInfo imsiEncryptionInfo = info.getCarrierInfoForImsiEncryption(subId, keyType, this.mContext.getOpPackageName());
            if (imsiEncryptionInfo == null) {
                if (isImsiEncryptionRequired(subId, keyType)) {
                    Rlog.e(str2, str);
                    throw new IllegalArgumentException(str);
                }
            }
            return imsiEncryptionInfo;
        } catch (RemoteException ex) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("getCarrierInfoForImsiEncryption RemoteException");
            stringBuilder.append(ex);
            Rlog.e(str2, stringBuilder.toString());
            return null;
        } catch (NullPointerException ex2) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("getCarrierInfoForImsiEncryption NullPointerException");
            stringBuilder.append(ex2);
            Rlog.e(str2, stringBuilder.toString());
            return null;
        }
    }

    public void resetCarrierKeysForImsiEncryption() {
        String str = "IMSI error: Subscriber Info is null";
        String str2 = TAG;
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info == null) {
                Rlog.e(str2, str);
                if (!isSystemProcess()) {
                    throw new RuntimeException(str);
                }
                return;
            }
            info.resetCarrierKeysForImsiEncryption(getSubId(SubscriptionManager.getDefaultDataSubscriptionId()), this.mContext.getOpPackageName());
        } catch (RemoteException ex) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getCarrierInfoForImsiEncryption RemoteException");
            stringBuilder.append(ex);
            Rlog.e(str2, stringBuilder.toString());
            if (!isSystemProcess()) {
                ex.rethrowAsRuntimeException();
            }
        }
    }

    private static boolean isKeyEnabled(int keyAvailability, int keyType) {
        if (((keyAvailability >> (keyType - 1)) & 1) == 1) {
            return true;
        }
        return false;
    }

    private boolean isImsiEncryptionRequired(int subId, int keyType) {
        CarrierConfigManager configManager = (CarrierConfigManager) this.mContext.getSystemService(Context.CARRIER_CONFIG_SERVICE);
        if (configManager == null) {
            return false;
        }
        PersistableBundle pb = configManager.getConfigForSubId(subId);
        if (pb == null) {
            return false;
        }
        return isKeyEnabled(pb.getInt(CarrierConfigManager.IMSI_KEY_AVAILABILITY_INT), keyType);
    }

    public void setCarrierInfoForImsiEncryption(ImsiEncryptionInfo imsiEncryptionInfo) {
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info != null) {
                info.setCarrierInfoForImsiEncryption(this.mSubId, this.mContext.getOpPackageName(), imsiEncryptionInfo);
            }
        } catch (NullPointerException e) {
        } catch (RemoteException ex) {
            Rlog.e(TAG, "setCarrierInfoForImsiEncryption RemoteException", ex);
        }
    }

    public String getGroupIdLevel1() {
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info == null) {
                return null;
            }
            return info.getGroupIdLevel1ForSubscriber(getSubId(), this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            return null;
        } catch (NullPointerException e2) {
            return null;
        }
    }

    @UnsupportedAppUsage
    public String getGroupIdLevel1(int subId) {
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info == null) {
                return null;
            }
            return info.getGroupIdLevel1ForSubscriber(subId, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            return null;
        } catch (NullPointerException e2) {
            return null;
        }
    }

    public String getLine1Number() {
        return getLine1Number(getSubId());
    }

    @UnsupportedAppUsage
    public String getLine1Number(int subId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("");
        stringBuilder.append(subId);
        SeempLog.record_str(9, stringBuilder.toString());
        String number = null;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                number = telephony.getLine1NumberForDisplay(subId, this.mContext.getOpPackageName());
            }
        } catch (RemoteException | NullPointerException e) {
        }
        if (number != null) {
            return number;
        }
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info == null) {
                return null;
            }
            return info.getLine1NumberForSubscriber(subId, this.mContext.getOpPackageName());
        } catch (RemoteException e2) {
            return null;
        } catch (NullPointerException e3) {
            return null;
        }
    }

    public boolean setLine1NumberForDisplay(String alphaTag, String number) {
        return setLine1NumberForDisplay(getSubId(), alphaTag, number);
    }

    public boolean setLine1NumberForDisplay(int subId, String alphaTag, String number) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.setLine1NumberForDisplayForSubscriber(subId, alphaTag, number);
            }
        } catch (RemoteException | NullPointerException e) {
        }
        return false;
    }

    public String getLine1AlphaTag() {
        return getLine1AlphaTag(getSubId());
    }

    @UnsupportedAppUsage
    public String getLine1AlphaTag(int subId) {
        String alphaTag = null;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                alphaTag = telephony.getLine1AlphaTagForDisplay(subId, getOpPackageName());
            }
        } catch (RemoteException | NullPointerException e) {
        }
        if (alphaTag != null) {
            return alphaTag;
        }
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info == null) {
                return null;
            }
            return info.getLine1AlphaTagForSubscriber(subId, getOpPackageName());
        } catch (RemoteException e2) {
            return null;
        } catch (NullPointerException e3) {
            return null;
        }
    }

    @UnsupportedAppUsage
    public String[] getMergedSubscriberIds() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getMergedSubscriberIds(getSubId(), getOpPackageName());
            }
        } catch (RemoteException | NullPointerException e) {
        }
        return null;
    }

    @UnsupportedAppUsage
    public String getMsisdn() {
        return getMsisdn(getSubId());
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public String getMsisdn(int subId) {
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info == null) {
                return null;
            }
            return info.getMsisdnForSubscriber(subId, getOpPackageName());
        } catch (RemoteException e) {
            return null;
        } catch (NullPointerException e2) {
            return null;
        }
    }

    public String getVoiceMailNumber() {
        return getVoiceMailNumber(getSubId());
    }

    @UnsupportedAppUsage
    public String getVoiceMailNumber(int subId) {
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info == null) {
                return null;
            }
            return info.getVoiceMailNumberForSubscriber(subId, getOpPackageName());
        } catch (RemoteException e) {
            return null;
        } catch (NullPointerException e2) {
            return null;
        }
    }

    public boolean setVoiceMailNumber(String alphaTag, String number) {
        return setVoiceMailNumber(getSubId(), alphaTag, number);
    }

    public boolean setVoiceMailNumber(int subId, String alphaTag, String number) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.setVoiceMailNumber(subId, alphaTag, number);
            }
        } catch (RemoteException | NullPointerException e) {
        }
        return false;
    }

    @SuppressLint({"Doclava125"})
    @SystemApi
    public void setVisualVoicemailEnabled(PhoneAccountHandle phoneAccountHandle, boolean enabled) {
    }

    @SuppressLint({"Doclava125"})
    @SystemApi
    public boolean isVisualVoicemailEnabled(PhoneAccountHandle phoneAccountHandle) {
        return false;
    }

    @SuppressLint({"Doclava125"})
    @SystemApi
    public Bundle getVisualVoicemailSettings() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getVisualVoicemailSettings(this.mContext.getOpPackageName(), this.mSubId);
            }
        } catch (RemoteException | NullPointerException e) {
        }
        return null;
    }

    public String getVisualVoicemailPackageName() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getVisualVoicemailPackageName(this.mContext.getOpPackageName(), getSubId());
            }
        } catch (RemoteException | NullPointerException e) {
        }
        return null;
    }

    public void setVisualVoicemailSmsFilterSettings(VisualVoicemailSmsFilterSettings settings) {
        if (settings == null) {
            disableVisualVoicemailSmsFilter(this.mSubId);
        } else {
            enableVisualVoicemailSmsFilter(this.mSubId, settings);
        }
    }

    public void sendVisualVoicemailSms(String number, int port, String text, PendingIntent sentIntent) {
        sendVisualVoicemailSmsForSubscriber(this.mSubId, number, port, text, sentIntent);
    }

    public void enableVisualVoicemailSmsFilter(int subId, VisualVoicemailSmsFilterSettings settings) {
        if (settings != null) {
            try {
                ITelephony telephony = getITelephony();
                if (telephony != null) {
                    telephony.enableVisualVoicemailSmsFilter(this.mContext.getOpPackageName(), subId, settings);
                    return;
                }
                return;
            } catch (RemoteException | NullPointerException e) {
                return;
            }
        }
        throw new IllegalArgumentException("Settings cannot be null");
    }

    public void disableVisualVoicemailSmsFilter(int subId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.disableVisualVoicemailSmsFilter(this.mContext.getOpPackageName(), subId);
            }
        } catch (RemoteException | NullPointerException e) {
        }
    }

    public VisualVoicemailSmsFilterSettings getVisualVoicemailSmsFilterSettings(int subId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getVisualVoicemailSmsFilterSettings(this.mContext.getOpPackageName(), subId);
            }
        } catch (RemoteException | NullPointerException e) {
        }
        return null;
    }

    public VisualVoicemailSmsFilterSettings getActiveVisualVoicemailSmsFilterSettings(int subId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getActiveVisualVoicemailSmsFilterSettings(subId);
            }
        } catch (RemoteException | NullPointerException e) {
        }
        return null;
    }

    public void sendVisualVoicemailSmsForSubscriber(int subId, String number, int port, String text, PendingIntent sentIntent) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.sendVisualVoicemailSmsForSubscriber(this.mContext.getOpPackageName(), subId, number, port, text, sentIntent);
            }
        } catch (RemoteException e) {
        }
    }

    @SystemApi
    public void setVoiceActivationState(int activationState) {
        setVoiceActivationState(getSubId(), activationState);
    }

    public void setVoiceActivationState(int subId, int activationState) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.setVoiceActivationState(subId, activationState);
            }
        } catch (RemoteException | NullPointerException e) {
        }
    }

    @SystemApi
    public void setDataActivationState(int activationState) {
        setDataActivationState(getSubId(), activationState);
    }

    public void setDataActivationState(int subId, int activationState) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.setDataActivationState(subId, activationState);
            }
        } catch (RemoteException | NullPointerException e) {
        }
    }

    @SystemApi
    public int getVoiceActivationState() {
        return getVoiceActivationState(getSubId());
    }

    public int getVoiceActivationState(int subId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getVoiceActivationState(subId, getOpPackageName());
            }
        } catch (RemoteException | NullPointerException e) {
        }
        return 0;
    }

    @SystemApi
    public int getDataActivationState() {
        return getDataActivationState(getSubId());
    }

    public int getDataActivationState(int subId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getDataActivationState(subId, getOpPackageName());
            }
        } catch (RemoteException | NullPointerException e) {
        }
        return 0;
    }

    @UnsupportedAppUsage
    public int getVoiceMessageCount() {
        return getVoiceMessageCount(getSubId());
    }

    @UnsupportedAppUsage
    public int getVoiceMessageCount(int subId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony == null) {
                return 0;
            }
            return telephony.getVoiceMessageCountForSubscriber(subId, getOpPackageName());
        } catch (RemoteException e) {
            return 0;
        } catch (NullPointerException e2) {
            return 0;
        }
    }

    public String getVoiceMailAlphaTag() {
        return getVoiceMailAlphaTag(getSubId());
    }

    @UnsupportedAppUsage
    public String getVoiceMailAlphaTag(int subId) {
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info == null) {
                return null;
            }
            return info.getVoiceMailAlphaTagForSubscriber(subId, getOpPackageName());
        } catch (RemoteException e) {
            return null;
        } catch (NullPointerException e2) {
            return null;
        }
    }

    public void sendDialerSpecialCode(String inputCode) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.sendDialerSpecialCode(this.mContext.getOpPackageName(), inputCode);
            } else if (!isSystemProcess()) {
                throw new RuntimeException("Telephony service unavailable");
            }
        } catch (RemoteException ex) {
            if (!isSystemProcess()) {
                ex.rethrowAsRuntimeException();
            }
        }
    }

    @UnsupportedAppUsage
    public String getIsimImpi() {
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info == null) {
                return null;
            }
            return info.getIsimImpi(getSubId());
        } catch (RemoteException e) {
            return null;
        } catch (NullPointerException e2) {
            return null;
        }
    }

    @SystemApi
    public String getIsimDomain() {
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info == null) {
                return null;
            }
            return info.getIsimDomain(getSubId());
        } catch (RemoteException e) {
            return null;
        } catch (NullPointerException e2) {
            return null;
        }
    }

    @UnsupportedAppUsage
    public String[] getIsimImpu() {
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info == null) {
                return null;
            }
            return info.getIsimImpu(getSubId());
        } catch (RemoteException e) {
            return null;
        } catch (NullPointerException e2) {
            return null;
        }
    }

    @UnsupportedAppUsage
    private IPhoneSubInfo getSubscriberInfo() {
        return Stub.asInterface(ServiceManager.getService("iphonesubinfo"));
    }

    public int getCallState() {
        try {
            ITelecomService telecom = getTelecomService();
            if (telecom != null) {
                return telecom.getCallState();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelecomService#getCallState", e);
        }
        return 0;
    }

    @UnsupportedAppUsage
    public int getCallState(int subId) {
        return getCallStateForSlot(SubscriptionManager.getPhoneId(subId));
    }

    public int getCallStateForSlot(int slotIndex) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony == null) {
                return 0;
            }
            return telephony.getCallStateForSlot(slotIndex);
        } catch (RemoteException e) {
            return 0;
        } catch (NullPointerException e2) {
            return 0;
        }
    }

    public int getDataActivity() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony == null) {
                return 0;
            }
            return telephony.getDataActivity();
        } catch (RemoteException e) {
            return 0;
        } catch (NullPointerException e2) {
            return 0;
        }
    }

    public int getDataState() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony == null) {
                return 0;
            }
            return telephony.getDataState();
        } catch (RemoteException e) {
            return 0;
        } catch (NullPointerException e2) {
            return 0;
        }
    }

    public static String dataStateToString(int state) {
        if (state == 0) {
            return "DISCONNECTED";
        }
        if (state == 1) {
            return "CONNECTING";
        }
        if (state == 2) {
            return "CONNECTED";
        }
        if (state == 3) {
            return "SUSPENDED";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UNKNOWN(");
        stringBuilder.append(state);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    private ITelephony getITelephony() {
        return ITelephony.Stub.asInterface(ServiceManager.getService("phone"));
    }

    private ITelecomService getTelecomService() {
        return ITelecomService.Stub.asInterface(ServiceManager.getService(Context.TELECOM_SERVICE));
    }

    private ITelephonyRegistry getTelephonyRegistry() {
        return ITelephonyRegistry.Stub.asInterface(ServiceManager.getService("telephony.registry"));
    }

    private IOns getIOns() {
        return IOns.Stub.asInterface(ServiceManager.getService("ions"));
    }

    public void listen(PhoneStateListener listener, int events) {
        if (this.mContext != null) {
            try {
                boolean notifyNow = getITelephony() != null;
                ITelephonyRegistry registry = getTelephonyRegistry();
                if (registry != null) {
                    int subId;
                    int subId2 = this.mSubId;
                    if (VMRuntime.getRuntime().getTargetSdkVersion() >= 29) {
                        listener.mSubId = Integer.valueOf(events == 0 ? -1 : subId2);
                    } else if (listener.mSubId != null) {
                        subId = listener.mSubId.intValue();
                        registry.listenForSubscriber(subId, getOpPackageName(), listener.callback, events, notifyNow);
                    }
                    subId = subId2;
                    registry.listenForSubscriber(subId, getOpPackageName(), listener.callback, events, notifyNow);
                } else {
                    Rlog.w(TAG, "telephony registry not ready.");
                }
            } catch (RemoteException e) {
            }
        }
    }

    public int getCdmaEriIconIndex() {
        return getCdmaEriIconIndex(getSubId());
    }

    @UnsupportedAppUsage
    public int getCdmaEriIconIndex(int subId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony == null) {
                return -1;
            }
            return telephony.getCdmaEriIconIndexForSubscriber(subId, getOpPackageName());
        } catch (RemoteException e) {
            return -1;
        } catch (NullPointerException e2) {
            return -1;
        }
    }

    public int getCdmaEriIconMode() {
        return getCdmaEriIconMode(getSubId());
    }

    @UnsupportedAppUsage
    public int getCdmaEriIconMode(int subId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony == null) {
                return -1;
            }
            return telephony.getCdmaEriIconModeForSubscriber(subId, getOpPackageName());
        } catch (RemoteException e) {
            return -1;
        } catch (NullPointerException e2) {
            return -1;
        }
    }

    public String getCdmaEriText() {
        return getCdmaEriText(getSubId());
    }

    @UnsupportedAppUsage
    public String getCdmaEriText(int subId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony == null) {
                return null;
            }
            return telephony.getCdmaEriTextForSubscriber(subId, getOpPackageName());
        } catch (RemoteException e) {
            return null;
        } catch (NullPointerException e2) {
            return null;
        }
    }

    public boolean isVoiceCapable() {
        Context context = this.mContext;
        if (context == null) {
            return true;
        }
        return context.getResources().getBoolean(R.bool.config_voice_capable);
    }

    public boolean isSmsCapable() {
        Context context = this.mContext;
        if (context == null) {
            return true;
        }
        return context.getResources().getBoolean(R.bool.config_sms_capable);
    }

    public List<CellInfo> getAllCellInfo() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony == null) {
                return null;
            }
            return telephony.getAllCellInfo(getOpPackageName());
        } catch (RemoteException | NullPointerException e) {
            return null;
        }
    }

    public void requestCellInfoUpdate(final Executor executor, final CellInfoCallback callback) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.requestCellInfoUpdate(getSubId(), new ICellInfoCallback.Stub() {
                    public void onCellInfo(List<CellInfo> cellInfo) {
                        Binder.withCleanCallingIdentity(new -$$Lambda$TelephonyManager$1$888GQVMXufCYSJI5ivTjjUxEprI(executor, callback, cellInfo));
                    }

                    public void onError(int errorCode, ParcelableException detail) {
                        Binder.withCleanCallingIdentity(new -$$Lambda$TelephonyManager$1$5jj__2hbfx_RMVO7qjBdMYFfP1s(executor, callback, errorCode, detail));
                    }
                }, getOpPackageName());
            }
        } catch (RemoteException e) {
        }
    }

    @SystemApi
    public void requestCellInfoUpdate(WorkSource workSource, final Executor executor, final CellInfoCallback callback) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.requestCellInfoUpdateWithWorkSource(getSubId(), new ICellInfoCallback.Stub() {
                    public void onCellInfo(List<CellInfo> cellInfo) {
                        Binder.withCleanCallingIdentity(new -$$Lambda$TelephonyManager$2$hWPf2raNadUBIhTQLEUpRhHWKoI(executor, callback, cellInfo));
                    }

                    public void onError(int errorCode, ParcelableException detail) {
                        Binder.withCleanCallingIdentity(new -$$Lambda$TelephonyManager$2$6owqHJtmTOa9dDQAz_9oKh9XFVk(executor, callback, errorCode, detail));
                    }
                }, getOpPackageName(), workSource);
            }
        } catch (RemoteException e) {
        }
    }

    public void setCellInfoListRate(int rateInMillis) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.setCellInfoListRate(rateInMillis);
            }
        } catch (RemoteException | NullPointerException e) {
        }
    }

    public String getMmsUserAgent() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getMmsUserAgent(getSubId());
            }
        } catch (RemoteException | NullPointerException e) {
        }
        return null;
    }

    public String getMmsUAProfUrl() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getMmsUAProfUrl(getSubId());
            }
        } catch (RemoteException | NullPointerException e) {
        }
        return null;
    }

    @Deprecated
    public IccOpenLogicalChannelResponse iccOpenLogicalChannel(String AID) {
        return iccOpenLogicalChannel(getSubId(), AID, -1);
    }

    @SystemApi
    public IccOpenLogicalChannelResponse iccOpenLogicalChannelBySlot(int slotIndex, String aid, int p2) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.iccOpenLogicalChannelBySlot(slotIndex, getOpPackageName(), aid, p2);
            }
        } catch (RemoteException | NullPointerException e) {
        }
        return null;
    }

    public IccOpenLogicalChannelResponse iccOpenLogicalChannel(String AID, int p2) {
        return iccOpenLogicalChannel(getSubId(), AID, p2);
    }

    public IccOpenLogicalChannelResponse iccOpenLogicalChannel(int subId, String AID, int p2) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.iccOpenLogicalChannel(subId, getOpPackageName(), AID, p2);
            }
        } catch (RemoteException | NullPointerException e) {
        }
        return null;
    }

    @SystemApi
    public boolean iccCloseLogicalChannelBySlot(int slotIndex, int channel) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.iccCloseLogicalChannelBySlot(slotIndex, channel);
            }
        } catch (RemoteException | NullPointerException e) {
        }
        return false;
    }

    public boolean iccCloseLogicalChannel(int channel) {
        return iccCloseLogicalChannel(getSubId(), channel);
    }

    public boolean iccCloseLogicalChannel(int subId, int channel) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.iccCloseLogicalChannel(subId, channel);
            }
        } catch (RemoteException | NullPointerException e) {
        }
        return false;
    }

    @SystemApi
    public String iccTransmitApduLogicalChannelBySlot(int slotIndex, int channel, int cla, int instruction, int p1, int p2, int p3, String data) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.iccTransmitApduLogicalChannelBySlot(slotIndex, channel, cla, instruction, p1, p2, p3, data);
            }
        } catch (RemoteException | NullPointerException e) {
        }
        return null;
    }

    public String iccTransmitApduLogicalChannel(int channel, int cla, int instruction, int p1, int p2, int p3, String data) {
        return iccTransmitApduLogicalChannel(getSubId(), channel, cla, instruction, p1, p2, p3, data);
    }

    public String iccTransmitApduLogicalChannel(int subId, int channel, int cla, int instruction, int p1, int p2, int p3, String data) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.iccTransmitApduLogicalChannel(subId, channel, cla, instruction, p1, p2, p3, data);
            }
        } catch (RemoteException | NullPointerException e) {
        }
        return "";
    }

    @SystemApi
    public String iccTransmitApduBasicChannelBySlot(int slotIndex, int cla, int instruction, int p1, int p2, int p3, String data) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.iccTransmitApduBasicChannelBySlot(slotIndex, getOpPackageName(), cla, instruction, p1, p2, p3, data);
            }
        } catch (RemoteException | NullPointerException e) {
        }
        return null;
    }

    public String iccTransmitApduBasicChannel(int cla, int instruction, int p1, int p2, int p3, String data) {
        return iccTransmitApduBasicChannel(getSubId(), cla, instruction, p1, p2, p3, data);
    }

    public String iccTransmitApduBasicChannel(int subId, int cla, int instruction, int p1, int p2, int p3, String data) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.iccTransmitApduBasicChannel(subId, getOpPackageName(), cla, instruction, p1, p2, p3, data);
            }
        } catch (RemoteException | NullPointerException e) {
        }
        return "";
    }

    public byte[] iccExchangeSimIO(int fileID, int command, int p1, int p2, int p3, String filePath) {
        return iccExchangeSimIO(getSubId(), fileID, command, p1, p2, p3, filePath);
    }

    public byte[] iccExchangeSimIO(int subId, int fileID, int command, int p1, int p2, int p3, String filePath) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.iccExchangeSimIO(subId, fileID, command, p1, p2, p3, filePath);
            }
        } catch (RemoteException | NullPointerException e) {
        }
        return null;
    }

    public String sendEnvelopeWithStatus(String content) {
        return sendEnvelopeWithStatus(getSubId(), content);
    }

    public String sendEnvelopeWithStatus(int subId, String content) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.sendEnvelopeWithStatus(subId, content);
            }
        } catch (RemoteException | NullPointerException e) {
        }
        return "";
    }

    @UnsupportedAppUsage
    public String nvReadItem(int itemID) {
        String str = TAG;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.nvReadItem(itemID);
            }
        } catch (RemoteException ex) {
            Rlog.e(str, "nvReadItem RemoteException", ex);
        } catch (NullPointerException ex2) {
            Rlog.e(str, "nvReadItem NPE", ex2);
        }
        return "";
    }

    public boolean nvWriteItem(int itemID, String itemValue) {
        String str = TAG;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.nvWriteItem(itemID, itemValue);
            }
        } catch (RemoteException ex) {
            Rlog.e(str, "nvWriteItem RemoteException", ex);
        } catch (NullPointerException ex2) {
            Rlog.e(str, "nvWriteItem NPE", ex2);
        }
        return false;
    }

    public boolean nvWriteCdmaPrl(byte[] preferredRoamingList) {
        String str = TAG;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.nvWriteCdmaPrl(preferredRoamingList);
            }
        } catch (RemoteException ex) {
            Rlog.e(str, "nvWriteCdmaPrl RemoteException", ex);
        } catch (NullPointerException ex2) {
            Rlog.e(str, "nvWriteCdmaPrl NPE", ex2);
        }
        return false;
    }

    @UnsupportedAppUsage
    public boolean nvResetConfig(int resetType) {
        String str = TAG;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                if (resetType == 1) {
                    return telephony.rebootModem(getSlotIndex());
                }
                if (resetType == 3) {
                    return telephony.resetModemConfig(getSlotIndex());
                }
                Rlog.e(str, "nvResetConfig unsupported reset type");
            }
        } catch (RemoteException ex) {
            Rlog.e(str, "nvResetConfig RemoteException", ex);
        } catch (NullPointerException ex2) {
            Rlog.e(str, "nvResetConfig NPE", ex2);
        }
        return false;
    }

    @SystemApi
    public boolean resetRadioConfig() {
        String str = TAG;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.resetModemConfig(getSlotIndex());
            }
        } catch (RemoteException ex) {
            Rlog.e(str, "resetRadioConfig RemoteException", ex);
        } catch (NullPointerException ex2) {
            Rlog.e(str, "resetRadioConfig NPE", ex2);
        }
        return false;
    }

    @SystemApi
    public boolean rebootRadio() {
        String str = TAG;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.rebootModem(getSlotIndex());
            }
        } catch (RemoteException ex) {
            Rlog.e(str, "rebootRadio RemoteException", ex);
        } catch (NullPointerException ex2) {
            Rlog.e(str, "rebootRadio NPE", ex2);
        }
        return false;
    }

    private int getSubId() {
        if (SubscriptionManager.isUsableSubIdValue(this.mSubId)) {
            return this.mSubId;
        }
        return SubscriptionManager.getDefaultSubscriptionId();
    }

    @UnsupportedAppUsage
    private int getSubId(int preferredSubId) {
        if (SubscriptionManager.isUsableSubIdValue(this.mSubId)) {
            return this.mSubId;
        }
        return preferredSubId;
    }

    private int getPhoneId() {
        return SubscriptionManager.getPhoneId(getSubId());
    }

    @UnsupportedAppUsage
    private int getPhoneId(int preferredSubId) {
        return SubscriptionManager.getPhoneId(getSubId(preferredSubId));
    }

    @VisibleForTesting
    @UnsupportedAppUsage
    public int getSlotIndex() {
        int slotIndex = SubscriptionManager.getSlotIndex(getSubId());
        if (slotIndex == -1) {
            return Integer.MAX_VALUE;
        }
        return slotIndex;
    }

    @SystemApi
    public void requestNumberVerification(PhoneNumberRange range, long timeoutMillis, final Executor executor, final NumberVerificationCallback callback) {
        if (executor == null) {
            throw new NullPointerException("Executor must be non-null");
        } else if (callback != null) {
            INumberVerificationCallback internalCallback = new INumberVerificationCallback.Stub() {
                public void onCallReceived(String phoneNumber) {
                    Binder.withCleanCallingIdentity(new -$$Lambda$TelephonyManager$3$ue1tJSNmFJObWAJcaHRYIrfBRNg(executor, callback, phoneNumber));
                }

                public void onVerificationFailed(int reason) {
                    Binder.withCleanCallingIdentity(new -$$Lambda$TelephonyManager$3$TrNEDm6VsUgT1BQFiXGiPDtbxuA(executor, callback, reason));
                }
            };
            try {
                ITelephony telephony = getITelephony();
                if (telephony != null) {
                    telephony.requestNumberVerification(range, timeoutMillis, internalCallback, getOpPackageName());
                }
            } catch (RemoteException ex) {
                Rlog.e(TAG, "requestNumberVerification RemoteException", ex);
                executor.execute(new -$$Lambda$TelephonyManager$4i1RRVjnCzfQvX2hIGG9K8g4DaY(callback));
            }
        } else {
            throw new NullPointerException("Callback must be non-null");
        }
    }

    @UnsupportedAppUsage
    public static void setTelephonyProperty(int phoneId, String property, String value) {
        if (TelephonyProperties.PROPERTY_ICC_OPERATOR_ALPHA.equals(property) || TelephonyProperties.PROPERTY_OPERATOR_ALPHA.equals(property) || TelephonyProperties.PROPERTY_ICC_OPERATOR_NUMERIC.equals(property) || TelephonyProperties.PROPERTY_OPERATOR_NUMERIC.equals(property)) {
            value = TelephonyManagerEx.getDefault().onOperatorNumericOrNameSet(phoneId, property, value);
        }
        String propVal = "";
        String[] p = null;
        String prop = SystemProperties.get(property);
        if (value == null) {
            value = "";
        }
        value.replace(',', ' ');
        String str = ",";
        if (prop != null) {
            p = prop.split(str);
        }
        boolean isValidPhoneId = SubscriptionManager.isValidPhoneId(phoneId);
        String str2 = " value: ";
        String str3 = " property=";
        String str4 = TAG;
        if (isValidPhoneId) {
            int i = 0;
            while (i < phoneId) {
                String str5 = "";
                if (p != null && i < p.length) {
                    str5 = p[i];
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(propVal);
                stringBuilder.append(str5);
                stringBuilder.append(str);
                propVal = stringBuilder.toString();
                i++;
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(propVal);
            stringBuilder2.append(value);
            propVal = stringBuilder2.toString();
            if (p != null) {
                for (i = phoneId + 1; i < p.length; i++) {
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append(propVal);
                    stringBuilder3.append(str);
                    stringBuilder3.append(p[i]);
                    propVal = stringBuilder3.toString();
                }
            }
            int propValLen = propVal.length();
            try {
                propValLen = propVal.getBytes(SimpleRequest.UTF8).length;
            } catch (UnsupportedEncodingException e) {
                Rlog.d(str4, "setTelephonyProperty: utf-8 not supported");
            }
            if (propValLen > 91) {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("setTelephonyProperty: property too long phoneId=");
                stringBuilder2.append(phoneId);
                stringBuilder2.append(str3);
                stringBuilder2.append(property);
                stringBuilder2.append(str2);
                stringBuilder2.append(value);
                stringBuilder2.append(" propVal=");
                stringBuilder2.append(propVal);
                Rlog.d(str4, stringBuilder2.toString());
                return;
            }
            SystemProperties.set(property, propVal);
            return;
        }
        StringBuilder stringBuilder4 = new StringBuilder();
        stringBuilder4.append("setTelephonyProperty: invalid phoneId=");
        stringBuilder4.append(phoneId);
        stringBuilder4.append(str3);
        stringBuilder4.append(property);
        stringBuilder4.append(str2);
        stringBuilder4.append(value);
        stringBuilder4.append(" prop=");
        stringBuilder4.append(prop);
        Rlog.d(str4, stringBuilder4.toString());
    }

    public static void setTelephonyProperty(String property, String value) {
        if (value == null) {
            value = "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("setTelephonyProperty: success property=");
        stringBuilder.append(property);
        stringBuilder.append(" value: ");
        stringBuilder.append(value);
        Rlog.d(TAG, stringBuilder.toString());
        SystemProperties.set(property, value);
    }

    @UnsupportedAppUsage
    public static int getIntAtIndex(ContentResolver cr, String name, int index) throws SettingNotFoundException {
        String v = Global.getString(cr, name);
        if (v != null) {
            String[] valArray = v.split(",");
            if (index >= 0 && index < valArray.length && valArray[index] != null) {
                try {
                    return Integer.parseInt(valArray[index]);
                } catch (NumberFormatException e) {
                }
            }
        }
        throw new SettingNotFoundException(name);
    }

    @UnsupportedAppUsage
    public static boolean putIntAtIndex(ContentResolver cr, String name, int index, int value) {
        String data = "";
        String[] valArray = null;
        String v = Global.getString(cr, name);
        StringBuilder stringBuilder;
        if (index == Integer.MAX_VALUE) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("putIntAtIndex index == MAX_VALUE index=");
            stringBuilder.append(index);
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (index >= 0) {
            String str = ",";
            if (v != null) {
                valArray = v.split(str);
            }
            int i = 0;
            while (i < index) {
                String str2 = "";
                if (valArray != null && i < valArray.length) {
                    str2 = valArray[i];
                }
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(data);
                stringBuilder2.append(str2);
                stringBuilder2.append(str);
                data = stringBuilder2.toString();
                i++;
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append(data);
            stringBuilder.append(value);
            data = stringBuilder.toString();
            if (valArray != null) {
                for (i = index + 1; i < valArray.length; i++) {
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append(data);
                    stringBuilder3.append(str);
                    stringBuilder3.append(valArray[i]);
                    data = stringBuilder3.toString();
                }
            }
            return Global.putString(cr, name, data);
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("putIntAtIndex index < 0 index=");
            stringBuilder.append(index);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    @UnsupportedAppUsage
    public static String getTelephonyProperty(int phoneId, String property, String defaultVal) {
        String propVal = null;
        String prop = SystemProperties.get(property);
        if (prop != null && prop.length() > 0) {
            String[] values = prop.split(",");
            if (phoneId >= 0 && phoneId < values.length && values[phoneId] != null) {
                propVal = values[phoneId];
            }
        }
        return propVal == null ? defaultVal : propVal;
    }

    @UnsupportedAppUsage
    public static String getTelephonyProperty(String property, String defaultVal) {
        String propVal = SystemProperties.get(property);
        return TextUtils.isEmpty(propVal) ? defaultVal : propVal;
    }

    @UnsupportedAppUsage
    public int getSimCount() {
        if (isMultiSimEnabled()) {
            return getPhoneCount();
        }
        return 1;
    }

    @SystemApi
    public String getIsimIst() {
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info == null) {
                return null;
            }
            return info.getIsimIst(getSubId());
        } catch (RemoteException e) {
            return null;
        } catch (NullPointerException e2) {
            return null;
        }
    }

    @UnsupportedAppUsage
    public String[] getIsimPcscf() {
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info == null) {
                return null;
            }
            return info.getIsimPcscf(getSubId());
        } catch (RemoteException e) {
            return null;
        } catch (NullPointerException e2) {
            return null;
        }
    }

    public String getIccAuthentication(int appType, int authType, String data) {
        return getIccAuthentication(getSubId(), appType, authType, data);
    }

    @UnsupportedAppUsage
    public String getIccAuthentication(int subId, int appType, int authType, String data) {
        try {
            IPhoneSubInfo info = getSubscriberInfo();
            if (info == null) {
                return null;
            }
            return info.getIccSimChallengeResponse(subId, appType, authType, data);
        } catch (RemoteException e) {
            return null;
        } catch (NullPointerException e2) {
            return null;
        }
    }

    public String[] getForbiddenPlmns() {
        return getForbiddenPlmns(getSubId(), 2);
    }

    public String[] getForbiddenPlmns(int subId, int appType) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony == null) {
                return null;
            }
            return telephony.getForbiddenPlmns(subId, appType, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            return null;
        } catch (NullPointerException e2) {
            return null;
        }
    }

    public String[] getPcscfAddress(String apnType) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony == null) {
                return new String[0];
            }
            return telephony.getPcscfAddress(apnType, getOpPackageName());
        } catch (RemoteException e) {
            return new String[0];
        }
    }

    public void enableIms(int slotId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.enableIms(slotId);
            }
        } catch (RemoteException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("enableIms, RemoteException: ");
            stringBuilder.append(e.getMessage());
            Rlog.e(TAG, stringBuilder.toString());
        }
    }

    public void disableIms(int slotId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.disableIms(slotId);
            }
        } catch (RemoteException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("disableIms, RemoteException: ");
            stringBuilder.append(e.getMessage());
            Rlog.e(TAG, stringBuilder.toString());
        }
    }

    public IImsMmTelFeature getImsMmTelFeatureAndListen(int slotIndex, IImsServiceFeatureCallback callback) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getMmTelFeatureAndListen(slotIndex, callback);
            }
        } catch (RemoteException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getImsMmTelFeatureAndListen, RemoteException: ");
            stringBuilder.append(e.getMessage());
            Rlog.e(TAG, stringBuilder.toString());
        }
        return null;
    }

    public IImsRcsFeature getImsRcsFeatureAndListen(int slotIndex, IImsServiceFeatureCallback callback) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getRcsFeatureAndListen(slotIndex, callback);
            }
        } catch (RemoteException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getImsRcsFeatureAndListen, RemoteException: ");
            stringBuilder.append(e.getMessage());
            Rlog.e(TAG, stringBuilder.toString());
        }
        return null;
    }

    @UnsupportedAppUsage
    public IImsRegistration getImsRegistration(int slotIndex, int feature) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getImsRegistration(slotIndex, feature);
            }
        } catch (RemoteException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getImsRegistration, RemoteException: ");
            stringBuilder.append(e.getMessage());
            Rlog.e(TAG, stringBuilder.toString());
        }
        return null;
    }

    @UnsupportedAppUsage
    public IImsConfig getImsConfig(int slotIndex, int feature) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getImsConfig(slotIndex, feature);
            }
        } catch (RemoteException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getImsRegistration, RemoteException: ");
            stringBuilder.append(e.getMessage());
            Rlog.e(TAG, stringBuilder.toString());
        }
        return null;
    }

    @UnsupportedAppUsage
    public void setImsRegistrationState(boolean registered) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.setImsRegistrationState(registered);
            }
        } catch (RemoteException e) {
        }
    }

    @UnsupportedAppUsage
    public int getPreferredNetworkType(int subId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getPreferredNetworkType(subId);
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "getPreferredNetworkType RemoteException", ex);
        }
        return -1;
    }

    @SystemApi
    public long getPreferredNetworkTypeBitmask() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return (long) RadioAccessFamily.getRafFromNetworkType(telephony.getPreferredNetworkType(getSubId()));
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "getPreferredNetworkTypeBitmask RemoteException", ex);
        }
        return 0;
    }

    public void setNetworkSelectionModeAutomatic() {
        String str = TAG;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.setNetworkSelectionModeAutomatic(getSubId());
            }
        } catch (RemoteException ex) {
            Rlog.e(str, "setNetworkSelectionModeAutomatic RemoteException", ex);
        } catch (NullPointerException ex2) {
            Rlog.e(str, "setNetworkSelectionModeAutomatic NPE", ex2);
        }
    }

    public CellNetworkScanResult getAvailableNetworks() {
        String str = TAG;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getCellNetworkScanResults(getSubId(), getOpPackageName());
            }
        } catch (RemoteException ex) {
            Rlog.e(str, "getAvailableNetworks RemoteException", ex);
        } catch (NullPointerException ex2) {
            Rlog.e(str, "getAvailableNetworks NPE", ex2);
        }
        return new CellNetworkScanResult(4, null);
    }

    public NetworkScan requestNetworkScan(NetworkScanRequest request, Executor executor, NetworkScanCallback callback) {
        synchronized (this) {
            if (this.mTelephonyScanManager == null) {
                this.mTelephonyScanManager = new TelephonyScanManager();
            }
        }
        return this.mTelephonyScanManager.requestNetworkScan(getSubId(), request, executor, callback, getOpPackageName());
    }

    @Deprecated
    public NetworkScan requestNetworkScan(NetworkScanRequest request, NetworkScanCallback callback) {
        return requestNetworkScan(request, AsyncTask.SERIAL_EXECUTOR, callback);
    }

    public boolean setNetworkSelectionModeManual(String operatorNumeric, boolean persistSelection) {
        String str = "";
        return setNetworkSelectionModeManual(new OperatorInfo(str, str, operatorNumeric), persistSelection);
    }

    public boolean setNetworkSelectionModeManual(OperatorInfo operatorInfo, boolean persistSelection) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.setNetworkSelectionModeManual(getSubId(), operatorInfo, persistSelection);
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "setNetworkSelectionModeManual RemoteException", ex);
        }
        return false;
    }

    public int getNetworkSelectionMode() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getNetworkSelectionMode(getSubId());
            }
            return 0;
        } catch (RemoteException ex) {
            Rlog.e(TAG, "getNetworkSelectionMode RemoteException", ex);
            return 0;
        }
    }

    public boolean isInEmergencySmsMode() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.isInEmergencySmsMode();
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "getNetworkSelectionMode RemoteException", ex);
        }
        return false;
    }

    @UnsupportedAppUsage
    public boolean setPreferredNetworkType(int subId, int networkType) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.setPreferredNetworkType(subId, networkType);
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "setPreferredNetworkType RemoteException", ex);
        }
        return false;
    }

    @SystemApi
    public boolean setPreferredNetworkTypeBitmask(long networkTypeBitmask) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.setPreferredNetworkType(getSubId(), RadioAccessFamily.getNetworkTypeFromRaf((int) networkTypeBitmask));
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "setPreferredNetworkTypeBitmask RemoteException", ex);
        }
        return false;
    }

    public boolean setPreferredNetworkTypeToGlobal() {
        return setPreferredNetworkTypeToGlobal(getSubId());
    }

    public boolean setPreferredNetworkTypeToGlobal(int subId) {
        return setPreferredNetworkType(subId, 10);
    }

    public boolean getTetherApnRequired() {
        return getTetherApnRequired(getSubId(SubscriptionManager.getActiveDataSubscriptionId()));
    }

    public boolean getTetherApnRequired(int subId) {
        String str = TAG;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getTetherApnRequiredForSubscriber(subId);
            }
        } catch (RemoteException ex) {
            Rlog.e(str, "hasMatchedTetherApnSetting RemoteException", ex);
        } catch (NullPointerException ex2) {
            Rlog.e(str, "hasMatchedTetherApnSetting NPE", ex2);
        }
        return false;
    }

    public boolean hasCarrierPrivileges() {
        return hasCarrierPrivileges(getSubId());
    }

    public boolean hasCarrierPrivileges(int subId) {
        String str = TAG;
        boolean z = false;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                if (telephony.getCarrierPrivilegeStatus(subId) == 1) {
                    z = true;
                }
                return z;
            }
        } catch (RemoteException ex) {
            Rlog.e(str, "hasCarrierPrivileges RemoteException", ex);
        } catch (NullPointerException ex2) {
            Rlog.e(str, "hasCarrierPrivileges NPE", ex2);
        }
        return false;
    }

    public boolean setOperatorBrandOverride(String brand) {
        return setOperatorBrandOverride(getSubId(), brand);
    }

    public boolean setOperatorBrandOverride(int subId, String brand) {
        String str = TAG;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.setOperatorBrandOverride(subId, brand);
            }
        } catch (RemoteException ex) {
            Rlog.e(str, "setOperatorBrandOverride RemoteException", ex);
        } catch (NullPointerException ex2) {
            Rlog.e(str, "setOperatorBrandOverride NPE", ex2);
        }
        return false;
    }

    @UnsupportedAppUsage
    public boolean setRoamingOverride(List<String> gsmRoamingList, List<String> gsmNonRoamingList, List<String> cdmaRoamingList, List<String> cdmaNonRoamingList) {
        return setRoamingOverride(getSubId(), gsmRoamingList, gsmNonRoamingList, cdmaRoamingList, cdmaNonRoamingList);
    }

    public boolean setRoamingOverride(int subId, List<String> gsmRoamingList, List<String> gsmNonRoamingList, List<String> cdmaRoamingList, List<String> cdmaNonRoamingList) {
        String str = TAG;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.setRoamingOverride(subId, gsmRoamingList, gsmNonRoamingList, cdmaRoamingList, cdmaNonRoamingList);
            }
        } catch (RemoteException ex) {
            Rlog.e(str, "setRoamingOverride RemoteException", ex);
        } catch (NullPointerException ex2) {
            Rlog.e(str, "setRoamingOverride NPE", ex2);
        }
        return false;
    }

    @SystemApi
    public String getCdmaMdn() {
        return getCdmaMdn(getSubId());
    }

    @SystemApi
    public String getCdmaMdn(int subId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony == null) {
                return null;
            }
            return telephony.getCdmaMdn(subId);
        } catch (RemoteException e) {
            return null;
        } catch (NullPointerException e2) {
            return null;
        }
    }

    @SystemApi
    public String getCdmaMin() {
        return getCdmaMin(getSubId());
    }

    @SystemApi
    public String getCdmaMin(int subId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony == null) {
                return null;
            }
            return telephony.getCdmaMin(subId);
        } catch (RemoteException e) {
            return null;
        } catch (NullPointerException e2) {
            return null;
        }
    }

    @SuppressLint({"Doclava125"})
    @SystemApi
    public int checkCarrierPrivilegesForPackage(String pkgName) {
        String str = TAG;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.checkCarrierPrivilegesForPackage(getSubId(), pkgName);
            }
        } catch (RemoteException ex) {
            Rlog.e(str, "checkCarrierPrivilegesForPackage RemoteException", ex);
        } catch (NullPointerException ex2) {
            Rlog.e(str, "checkCarrierPrivilegesForPackage NPE", ex2);
        }
        return 0;
    }

    @SuppressLint({"Doclava125"})
    @SystemApi
    public int checkCarrierPrivilegesForPackageAnyPhone(String pkgName) {
        String str = TAG;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.checkCarrierPrivilegesForPackageAnyPhone(pkgName);
            }
        } catch (RemoteException ex) {
            Rlog.e(str, "checkCarrierPrivilegesForPackageAnyPhone RemoteException", ex);
        } catch (NullPointerException ex2) {
            Rlog.e(str, "checkCarrierPrivilegesForPackageAnyPhone NPE", ex2);
        }
        return 0;
    }

    @SystemApi
    public List<String> getCarrierPackageNamesForIntent(Intent intent) {
        return getCarrierPackageNamesForIntentAndPhone(intent, getPhoneId());
    }

    @SystemApi
    public List<String> getCarrierPackageNamesForIntentAndPhone(Intent intent, int phoneId) {
        String str = TAG;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getCarrierPackageNamesForIntentAndPhone(intent, phoneId);
            }
        } catch (RemoteException ex) {
            Rlog.e(str, "getCarrierPackageNamesForIntentAndPhone RemoteException", ex);
        } catch (NullPointerException ex2) {
            Rlog.e(str, "getCarrierPackageNamesForIntentAndPhone NPE", ex2);
        }
        return null;
    }

    public List<String> getPackagesWithCarrierPrivileges() {
        String str = TAG;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getPackagesWithCarrierPrivileges(getPhoneId());
            }
        } catch (RemoteException ex) {
            Rlog.e(str, "getPackagesWithCarrierPrivileges RemoteException", ex);
        } catch (NullPointerException ex2) {
            Rlog.e(str, "getPackagesWithCarrierPrivileges NPE", ex2);
        }
        return Collections.EMPTY_LIST;
    }

    public List<String> getPackagesWithCarrierPrivilegesForAllPhones() {
        String str = TAG;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getPackagesWithCarrierPrivilegesForAllPhones();
            }
        } catch (RemoteException ex) {
            Rlog.e(str, "getPackagesWithCarrierPrivilegesForAllPhones RemoteException", ex);
        } catch (NullPointerException ex2) {
            Rlog.e(str, "getPackagesWithCarrierPrivilegesForAllPhones NPE", ex2);
        }
        return Collections.EMPTY_LIST;
    }

    @SuppressLint({"Doclava125"})
    @SystemApi
    public void dial(String number) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.dial(number);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#dial", e);
        }
    }

    @SystemApi
    @Deprecated
    public void call(String callingPackage, String number) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.call(callingPackage, number);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#call", e);
        }
    }

    @SystemApi
    @Deprecated
    public boolean endCall() {
        return false;
    }

    @SystemApi
    @Deprecated
    public void answerRingingCall() {
    }

    @SuppressLint({"Doclava125"})
    @SystemApi
    @Deprecated
    public void silenceRinger() {
    }

    @SystemApi
    @Deprecated
    public boolean isOffhook() {
        return ((TelecomManager) this.mContext.getSystemService(Context.TELECOM_SERVICE)).isInCall();
    }

    @SystemApi
    @Deprecated
    public boolean isRinging() {
        return ((TelecomManager) this.mContext.getSystemService(Context.TELECOM_SERVICE)).isRinging();
    }

    @SystemApi
    @Deprecated
    public boolean isIdle() {
        return ((TelecomManager) this.mContext.getSystemService(Context.TELECOM_SERVICE)).isInCall() ^ 1;
    }

    @SystemApi
    @Deprecated
    public boolean isRadioOn() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.isRadioOn(getOpPackageName());
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#isRadioOn", e);
        }
        return false;
    }

    @SystemApi
    public boolean supplyPin(String pin) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.supplyPin(pin);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#supplyPin", e);
        }
        return false;
    }

    @SystemApi
    public boolean supplyPuk(String puk, String pin) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.supplyPuk(puk, pin);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#supplyPuk", e);
        }
        return false;
    }

    @SystemApi
    public int[] supplyPinReportResult(String pin) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.supplyPinReportResult(pin);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#supplyPinReportResult", e);
        }
        return new int[0];
    }

    @SystemApi
    public int[] supplyPukReportResult(String puk, String pin) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.supplyPukReportResult(puk, pin);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#]", e);
        }
        return new int[0];
    }

    public void sendUssdRequest(String ussdRequest, final UssdResponseCallback callback, Handler handler) {
        Preconditions.checkNotNull(callback, "UssdResponseCallback cannot be null.");
        ResultReceiver wrappedCallback = new ResultReceiver(handler) {
            /* Access modifiers changed, original: protected */
            public void onReceiveResult(int resultCode, Bundle ussdResponse) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("USSD:");
                stringBuilder.append(resultCode);
                Rlog.d(TelephonyManager.TAG, stringBuilder.toString());
                Preconditions.checkNotNull(ussdResponse, "ussdResponse cannot be null.");
                UssdResponse response = (UssdResponse) ussdResponse.getParcelable(TelephonyManager.USSD_RESPONSE);
                if (resultCode == 100) {
                    callback.onReceiveUssdResponse(this, response.getUssdRequest(), response.getReturnMessage());
                } else {
                    callback.onReceiveUssdResponseFailed(this, response.getUssdRequest(), resultCode);
                }
            }
        };
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.handleUssdRequest(getSubId(), ussdRequest, wrappedCallback);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#sendUSSDCode", e);
            UssdResponse response = new UssdResponse(ussdRequest, "");
            Bundle returnData = new Bundle();
            returnData.putParcelable(USSD_RESPONSE, response);
            wrappedCallback.send(-2, returnData);
        }
    }

    public boolean isConcurrentVoiceAndDataSupported() {
        boolean z = false;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                z = telephony.isConcurrentVoiceAndDataAllowed(getSubId());
            }
            return z;
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#isConcurrentVoiceAndDataAllowed", e);
            return false;
        }
    }

    @SystemApi
    public boolean handlePinMmi(String dialString) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.handlePinMmi(dialString);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#handlePinMmi", e);
        }
        return false;
    }

    @SystemApi
    public boolean handlePinMmiForSubscriber(int subId, String dialString) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.handlePinMmiForSubscriber(subId, dialString);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#handlePinMmi", e);
        }
        return false;
    }

    @SystemApi
    public void toggleRadioOnOff() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.toggleRadioOnOff();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#toggleRadioOnOff", e);
        }
    }

    @SystemApi
    public boolean setRadio(boolean turnOn) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.setRadio(turnOn);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#setRadio", e);
        }
        return false;
    }

    @SystemApi
    public boolean setRadioPower(boolean turnOn) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.setRadioPower(turnOn);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#setRadioPower", e);
        }
        return false;
    }

    @SystemApi
    public int getRadioPowerState() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getRadioPowerState(getSlotIndex(), this.mContext.getOpPackageName());
            }
        } catch (RemoteException e) {
        }
        return 2;
    }

    @SuppressLint({"Doclava125"})
    @SystemApi
    public void updateServiceLocation() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.updateServiceLocation();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#updateServiceLocation", e);
        }
    }

    @SystemApi
    public boolean enableDataConnectivity() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.enableDataConnectivity();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#enableDataConnectivity", e);
        }
        return false;
    }

    @SystemApi
    public boolean disableDataConnectivity() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.disableDataConnectivity();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#disableDataConnectivity", e);
        }
        return false;
    }

    @SystemApi
    public boolean isDataConnectivityPossible() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.isDataConnectivityPossible(getSubId(SubscriptionManager.getActiveDataSubscriptionId()));
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#isDataAllowed", e);
        }
        return false;
    }

    @SystemApi
    public boolean needsOtaServiceProvisioning() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.needsOtaServiceProvisioning();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#needsOtaServiceProvisioning", e);
        }
        return false;
    }

    public void setDataEnabled(boolean enable) {
        setDataEnabled(getSubId(SubscriptionManager.getDefaultDataSubscriptionId()), enable);
    }

    @SystemApi
    @Deprecated
    public void setDataEnabled(int subId, boolean enable) {
        String str = TAG;
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("setDataEnabled: enabled=");
            stringBuilder.append(enable);
            Log.d(str, stringBuilder.toString());
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.setUserDataEnabled(subId, enable);
            }
        } catch (RemoteException e) {
            Log.e(str, "Error calling ITelephony#setUserDataEnabled", e);
        }
    }

    @SystemApi
    @Deprecated
    public boolean getDataEnabled() {
        return isDataEnabled();
    }

    public boolean isDataEnabled() {
        return getDataEnabled(getSubId(SubscriptionManager.getDefaultDataSubscriptionId()));
    }

    public boolean isDataRoamingEnabled() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.isDataRoamingEnabled(getSubId(SubscriptionManager.getDefaultDataSubscriptionId()));
            }
            return false;
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#isDataRoamingEnabled", e);
            return false;
        }
    }

    public int getCdmaRoamingMode() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getCdmaRoamingMode(getSubId());
            }
            return -1;
        } catch (RemoteException ex) {
            Log.e(TAG, "Error calling ITelephony#getCdmaRoamingMode", ex);
            return -1;
        }
    }

    public boolean setCdmaRoamingMode(int mode) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.setCdmaRoamingMode(getSubId(), mode);
            }
        } catch (RemoteException ex) {
            Log.e(TAG, "Error calling ITelephony#setCdmaRoamingMode", ex);
        }
        return false;
    }

    public boolean setCdmaSubscriptionMode(int mode) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.setCdmaSubscriptionMode(getSubId(), mode);
            }
        } catch (RemoteException ex) {
            Log.e(TAG, "Error calling ITelephony#setCdmaSubscriptionMode", ex);
        }
        return false;
    }

    @SystemApi
    public void setDataRoamingEnabled(boolean isEnabled) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.setDataRoamingEnabled(getSubId(SubscriptionManager.getDefaultDataSubscriptionId()), isEnabled);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#setDataRoamingEnabled", e);
        }
    }

    @SystemApi
    @Deprecated
    public boolean getDataEnabled(int subId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.isUserDataEnabled(subId);
            }
            return false;
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#isUserDataEnabled", e);
            return false;
        } catch (NullPointerException e2) {
            return false;
        }
    }

    @Deprecated
    public int invokeOemRilRequestRaw(byte[] oemReq, byte[] oemResp) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.invokeOemRilRequestRaw(oemReq, oemResp);
            }
        } catch (RemoteException | NullPointerException e) {
        }
        return -1;
    }

    @SystemApi
    public void enableVideoCalling(boolean enable) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.enableVideoCalling(enable);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#enableVideoCalling", e);
        }
    }

    @SystemApi
    public boolean isVideoCallingEnabled() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.isVideoCallingEnabled(getOpPackageName());
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#isVideoCallingEnabled", e);
        }
        return false;
    }

    public boolean canChangeDtmfToneLength() {
        String str = TAG;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.canChangeDtmfToneLength(this.mSubId, getOpPackageName());
            }
        } catch (RemoteException e) {
            Log.e(str, "Error calling ITelephony#canChangeDtmfToneLength", e);
        } catch (SecurityException e2) {
            Log.e(str, "Permission error calling ITelephony#canChangeDtmfToneLength", e2);
        }
        return false;
    }

    public boolean isWorldPhone() {
        String str = TAG;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.isWorldPhone(this.mSubId, getOpPackageName());
            }
        } catch (RemoteException e) {
            Log.e(str, "Error calling ITelephony#isWorldPhone", e);
        } catch (SecurityException e2) {
            Log.e(str, "Permission error calling ITelephony#isWorldPhone", e2);
        }
        return false;
    }

    @Deprecated
    public boolean isTtyModeSupported() {
        try {
            TelecomManager telecomManager = TelecomManager.from(this.mContext);
            if (telecomManager != null) {
                return telecomManager.isTtySupported();
            }
        } catch (SecurityException e) {
            Log.e(TAG, "Permission error calling TelecomManager#isTtySupported", e);
        }
        return false;
    }

    public boolean isRttSupported() {
        String str = TAG;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.isRttSupported(this.mSubId);
            }
        } catch (RemoteException e) {
            Log.e(str, "Error calling ITelephony#isRttSupported", e);
        } catch (SecurityException e2) {
            Log.e(str, "Permission error calling ITelephony#isWorldPhone", e2);
        }
        return false;
    }

    public boolean isHearingAidCompatibilitySupported() {
        String str = TAG;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.isHearingAidCompatibilitySupported();
            }
        } catch (RemoteException e) {
            Log.e(str, "Error calling ITelephony#isHearingAidCompatibilitySupported", e);
        } catch (SecurityException e2) {
            Log.e(str, "Permission error calling ITelephony#isHearingAidCompatibilitySupported", e2);
        }
        return false;
    }

    public boolean isImsRegisteredForSubscriber(int subId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony == null) {
                return false;
            }
            return telephony.isImsRegistered(subId);
        } catch (RemoteException e) {
            return false;
        } catch (NullPointerException e2) {
            return false;
        }
    }

    public boolean isImsRegistered(int subId) {
        try {
            return getITelephony().isImsRegistered(subId);
        } catch (RemoteException | NullPointerException e) {
            return false;
        }
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public boolean isImsRegistered() {
        try {
            return getITelephony().isImsRegistered(getSubId());
        } catch (RemoteException | NullPointerException e) {
            return false;
        }
    }

    @UnsupportedAppUsage
    public boolean isVolteAvailable() {
        boolean z = false;
        try {
            z = getITelephony().isAvailable(getSubId(), 1, 0);
            return z;
        } catch (RemoteException | NullPointerException e) {
            return z;
        }
    }

    @UnsupportedAppUsage
    public boolean isVideoTelephonyAvailable() {
        try {
            return getITelephony().isVideoTelephonyAvailable(getSubId());
        } catch (RemoteException | NullPointerException e) {
            return false;
        }
    }

    @UnsupportedAppUsage
    public boolean isWifiCallingAvailable() {
        try {
            return getITelephony().isWifiCallingAvailable(getSubId());
        } catch (RemoteException | NullPointerException e) {
            return false;
        }
    }

    public int getImsRegTechnologyForMmTel() {
        try {
            return getITelephony().getImsRegTechnologyForMmTel(getSubId());
        } catch (RemoteException e) {
            return -1;
        }
    }

    public void setSimOperatorNumeric(String numeric) {
        setSimOperatorNumericForPhone(getPhoneId(), numeric);
    }

    @UnsupportedAppUsage
    public void setSimOperatorNumericForPhone(int phoneId, String numeric) {
        setTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_ICC_OPERATOR_NUMERIC, numeric);
    }

    public void setSimOperatorName(String name) {
        setSimOperatorNameForPhone(getPhoneId(), name);
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public void setSimOperatorNameForPhone(int phoneId, String name) {
        setTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_ICC_OPERATOR_ALPHA, name);
    }

    public void setSimCountryIso(String iso) {
        setSimCountryIsoForPhone(getPhoneId(), iso);
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public void setSimCountryIsoForPhone(int phoneId, String iso) {
        setTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_ICC_OPERATOR_ISO_COUNTRY, iso);
    }

    public void setSimState(String state) {
        setSimStateForPhone(getPhoneId(), state);
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public void setSimStateForPhone(int phoneId, String state) {
        setTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_SIM_STATE, state);
    }

    @SystemApi
    public void setSimPowerState(int state) {
        setSimPowerStateForSlot(getSlotIndex(), state);
    }

    @SystemApi
    public void setSimPowerStateForSlot(int slotIndex, int state) {
        String str = TAG;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.setSimPowerStateForSlot(slotIndex, state);
            }
        } catch (RemoteException e) {
            Log.e(str, "Error calling ITelephony#setSimPowerStateForSlot", e);
        } catch (SecurityException e2) {
            Log.e(str, "Permission error calling ITelephony#setSimPowerStateForSlot", e2);
        }
    }

    public void setBasebandVersion(String version) {
        setBasebandVersionForPhone(getPhoneId(), version);
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public void setBasebandVersionForPhone(int phoneId, String version) {
        setTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_BASEBAND_VERSION, version);
    }

    public String getBasebandVersion() {
        return getBasebandVersionForPhone(getPhoneId());
    }

    private String getBasebandVersionLegacy(int phoneId) {
        if (!SubscriptionManager.isValidPhoneId(phoneId)) {
            return null;
        }
        String prop = new StringBuilder();
        prop.append(TelephonyProperties.PROPERTY_BASEBAND_VERSION);
        prop.append(phoneId == 0 ? "" : Integer.toString(phoneId));
        return SystemProperties.get(prop.toString());
    }

    public String getBasebandVersionForPhone(int phoneId) {
        String version = getBasebandVersionLegacy(phoneId);
        if (!(version == null || version.isEmpty())) {
            setBasebandVersionForPhone(phoneId, version);
        }
        return getTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_BASEBAND_VERSION, "");
    }

    public void setPhoneType(int type) {
        setPhoneType(getPhoneId(), type);
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public void setPhoneType(int phoneId, int type) {
        if (SubscriptionManager.isValidPhoneId(phoneId)) {
            setTelephonyProperty(phoneId, TelephonyProperties.CURRENT_ACTIVE_PHONE, String.valueOf(type));
        }
    }

    public String getOtaSpNumberSchema(String defaultValue) {
        return getOtaSpNumberSchemaForPhone(getPhoneId(), defaultValue);
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public String getOtaSpNumberSchemaForPhone(int phoneId, String defaultValue) {
        if (SubscriptionManager.isValidPhoneId(phoneId)) {
            return getTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_OTASP_NUM_SCHEMA, defaultValue);
        }
        return defaultValue;
    }

    public boolean getSmsReceiveCapable(boolean defaultValue) {
        return getSmsReceiveCapableForPhone(getPhoneId(), defaultValue);
    }

    public boolean getSmsReceiveCapableForPhone(int phoneId, boolean defaultValue) {
        if (!SubscriptionManager.isValidPhoneId(phoneId)) {
            return defaultValue;
        }
        return Boolean.parseBoolean(getTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_SMS_RECEIVE, String.valueOf(defaultValue)));
    }

    public boolean getSmsSendCapable(boolean defaultValue) {
        return getSmsSendCapableForPhone(getPhoneId(), defaultValue);
    }

    public boolean getSmsSendCapableForPhone(int phoneId, boolean defaultValue) {
        if (!SubscriptionManager.isValidPhoneId(phoneId)) {
            return defaultValue;
        }
        return Boolean.parseBoolean(getTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_SMS_SEND, String.valueOf(defaultValue)));
    }

    public void setNetworkOperatorName(String name) {
        setNetworkOperatorNameForPhone(getPhoneId(), name);
    }

    @UnsupportedAppUsage
    public void setNetworkOperatorNameForPhone(int phoneId, String name) {
        if (SubscriptionManager.isValidPhoneId(phoneId)) {
            setTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_OPERATOR_ALPHA, name);
        }
    }

    public void setNetworkOperatorNumeric(String numeric) {
        setNetworkOperatorNumericForPhone(getPhoneId(), numeric);
    }

    @UnsupportedAppUsage
    public void setNetworkOperatorNumericForPhone(int phoneId, String numeric) {
        setTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_OPERATOR_NUMERIC, numeric);
    }

    public void setNetworkRoaming(boolean isRoaming) {
        setNetworkRoamingForPhone(getPhoneId(), isRoaming);
    }

    @UnsupportedAppUsage
    public void setNetworkRoamingForPhone(int phoneId, boolean isRoaming) {
        if (SubscriptionManager.isValidPhoneId(phoneId)) {
            setTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_OPERATOR_ISROAMING, isRoaming ? "true" : "false");
        }
    }

    public void setDataNetworkType(int type) {
        setDataNetworkTypeForPhone(getPhoneId(SubscriptionManager.getDefaultDataSubscriptionId()), type);
    }

    @UnsupportedAppUsage
    public void setDataNetworkTypeForPhone(int phoneId, int type) {
        if (SubscriptionManager.isValidPhoneId(phoneId)) {
            setTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_DATA_NETWORK_TYPE, ServiceState.rilRadioTechnologyToString(type));
        }
    }

    @UnsupportedAppUsage
    public int getSubIdForPhoneAccount(PhoneAccount phoneAccount) {
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                return service.getSubIdForPhoneAccount(phoneAccount);
            }
            return -1;
        } catch (RemoteException e) {
            return -1;
        }
    }

    public PhoneAccountHandle getPhoneAccountHandleForSubscriptionId(int subscriptionId) {
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                return service.getPhoneAccountHandleForSubscriptionId(subscriptionId);
            }
            return null;
        } catch (RemoteException e) {
            return null;
        }
    }

    private int getSubIdForPhoneAccountHandle(PhoneAccountHandle phoneAccountHandle) {
        try {
            ITelecomService service = getTelecomService();
            if (service != null) {
                return getSubIdForPhoneAccount(service.getPhoneAccount(phoneAccountHandle));
            }
            return -1;
        } catch (RemoteException e) {
            return -1;
        }
    }

    public void factoryReset(int subId) {
        try {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("factoryReset: subId=");
            stringBuilder.append(subId);
            Log.d(str, stringBuilder.toString());
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.factoryReset(subId);
            }
        } catch (RemoteException e) {
        }
    }

    @SystemApi
    public Locale getSimLocale() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                String languageTag = telephony.getSimLocaleForSubscriber(getSubId());
                if (!TextUtils.isEmpty(languageTag)) {
                    return Locale.forLanguageTag(languageTag);
                }
            }
        } catch (RemoteException e) {
        }
        return null;
    }

    public String getLocaleFromDefaultSim() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getSimLocaleForSubscriber(getSubId());
            }
        } catch (RemoteException e) {
        }
        return null;
    }

    public void requestModemActivityInfo(ResultReceiver result) {
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                service.requestModemActivityInfo(result);
                return;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#getModemActivityInfo", e);
        }
        result.send(0, null);
    }

    public ServiceState getServiceState() {
        return getServiceStateForSubscriber(getSubId());
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public ServiceState getServiceStateForSubscriber(int subId) {
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                return service.getServiceStateForSubscriber(subId, getOpPackageName());
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#getServiceStateForSubscriber", e);
        } catch (NullPointerException e2) {
            UUID fromString = UUID.fromString("a3ab0b9d-f2aa-4baf-911d-7096c0d4645a");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getServiceStateForSubscriber ");
            stringBuilder.append(subId);
            stringBuilder.append(" NPE");
            AnomalyReporter.reportAnomaly(fromString, stringBuilder.toString());
        }
        return null;
    }

    public Uri getVoicemailRingtoneUri(PhoneAccountHandle accountHandle) {
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                return service.getVoicemailRingtoneUri(accountHandle);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#getVoicemailRingtoneUri", e);
        }
        return null;
    }

    public void setVoicemailRingtoneUri(PhoneAccountHandle phoneAccountHandle, Uri uri) {
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                service.setVoicemailRingtoneUri(getOpPackageName(), phoneAccountHandle, uri);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#setVoicemailRingtoneUri", e);
        }
    }

    public boolean isVoicemailVibrationEnabled(PhoneAccountHandle accountHandle) {
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                return service.isVoicemailVibrationEnabled(accountHandle);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#isVoicemailVibrationEnabled", e);
        }
        return false;
    }

    public void setVoicemailVibrationEnabled(PhoneAccountHandle phoneAccountHandle, boolean enabled) {
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                service.setVoicemailVibrationEnabled(getOpPackageName(), phoneAccountHandle, enabled);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#isVoicemailVibrationEnabled", e);
        }
    }

    public int getSimCarrierId() {
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                return service.getSubscriptionCarrierId(getSubId());
            }
        } catch (RemoteException e) {
        }
        return -1;
    }

    public CharSequence getSimCarrierIdName() {
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                return service.getSubscriptionCarrierName(getSubId());
            }
        } catch (RemoteException e) {
        }
        return null;
    }

    public int getSimSpecificCarrierId() {
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                return service.getSubscriptionSpecificCarrierId(getSubId());
            }
        } catch (RemoteException e) {
        }
        return -1;
    }

    public CharSequence getSimSpecificCarrierIdName() {
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                return service.getSubscriptionSpecificCarrierName(getSubId());
            }
        } catch (RemoteException e) {
        }
        return null;
    }

    public int getCarrierIdFromSimMccMnc() {
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                return service.getCarrierIdFromMccMnc(getSlotIndex(), getSimOperator(), true);
            }
        } catch (RemoteException e) {
        }
        return -1;
    }

    public int getCarrierIdFromMccMnc(String mccmnc) {
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                return service.getCarrierIdFromMccMnc(getSlotIndex(), mccmnc, false);
            }
        } catch (RemoteException e) {
        }
        return -1;
    }

    public List<String> getCertsFromCarrierPrivilegeAccessRules() {
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                return service.getCertsFromCarrierPrivilegeAccessRules(getSubId());
            }
        } catch (RemoteException e) {
        }
        return null;
    }

    @SystemApi
    public String getAidForAppType(int appType) {
        return getAidForAppType(getSubId(), appType);
    }

    public String getAidForAppType(int subId, int appType) {
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                return service.getAidForAppType(subId, appType);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#getAidForAppType", e);
        }
        return null;
    }

    public String getEsn() {
        return getEsn(getSubId());
    }

    public String getEsn(int subId) {
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                return service.getEsn(subId);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#getEsn", e);
        }
        return null;
    }

    @SystemApi
    public String getCdmaPrlVersion() {
        return getCdmaPrlVersion(getSubId());
    }

    public String getCdmaPrlVersion(int subId) {
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                return service.getCdmaPrlVersion(subId);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#getCdmaPrlVersion", e);
        }
        return null;
    }

    @SystemApi
    public List<TelephonyHistogram> getTelephonyHistograms() {
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                return service.getTelephonyHistograms();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#getTelephonyHistograms", e);
        }
        return null;
    }

    /* JADX WARNING: Missing block: B:9:0x0027, code skipped:
            return -1;
     */
    @android.annotation.SystemApi
    public int setAllowedCarriers(int r4, java.util.List<android.service.carrier.CarrierIdentifier> r5) {
        /*
        r3 = this;
        r0 = -1;
        if (r5 == 0) goto L_0x0027;
    L_0x0003:
        r1 = android.telephony.SubscriptionManager.isValidPhoneId(r4);
        if (r1 != 0) goto L_0x000a;
    L_0x0009:
        goto L_0x0027;
    L_0x000a:
        r1 = android.telephony.CarrierRestrictionRules.newBuilder();
        r1 = r1.setAllowedCarriers(r5);
        r2 = 0;
        r1 = r1.setDefaultCarrierRestriction(r2);
        r1 = r1.build();
        r2 = r3.setCarrierRestrictionRules(r1);
        if (r2 != 0) goto L_0x0026;
    L_0x0021:
        r0 = r5.size();
        return r0;
    L_0x0026:
        return r0;
    L_0x0027:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.telephony.TelephonyManager.setAllowedCarriers(int, java.util.List):int");
    }

    @SystemApi
    public int setCarrierRestrictionRules(CarrierRestrictionRules rules) {
        String str = "Error calling ITelephony#setAllowedCarriers";
        String str2 = TAG;
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                return service.setAllowedCarriers(rules);
            }
        } catch (RemoteException e) {
            Log.e(str2, str, e);
        } catch (NullPointerException e2) {
            Log.e(str2, str, e2);
        }
        return 2;
    }

    @SystemApi
    @Deprecated
    public List<CarrierIdentifier> getAllowedCarriers(int slotIndex) {
        if (SubscriptionManager.isValidPhoneId(slotIndex)) {
            CarrierRestrictionRules carrierRestrictionRule = getCarrierRestrictionRules();
            if (carrierRestrictionRule != null) {
                return carrierRestrictionRule.getAllowedCarriers();
            }
        }
        return new ArrayList(0);
    }

    @SystemApi
    public CarrierRestrictionRules getCarrierRestrictionRules() {
        String str = "Error calling ITelephony#getAllowedCarriers";
        String str2 = TAG;
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                return service.getAllowedCarriers();
            }
        } catch (RemoteException e) {
            Log.e(str2, str, e);
        } catch (NullPointerException e2) {
            Log.e(str2, str, e2);
        }
        return null;
    }

    @SystemApi
    public void setCarrierDataEnabled(boolean enabled) {
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                service.carrierActionSetMeteredApnsEnabled(getSubId(SubscriptionManager.getDefaultDataSubscriptionId()), enabled);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#setCarrierDataEnabled", e);
        }
    }

    public void carrierActionSetRadioEnabled(int subId, boolean enabled) {
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                service.carrierActionSetRadioEnabled(subId, enabled);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#carrierActionSetRadioEnabled", e);
        }
    }

    public void carrierActionReportDefaultNetworkStatus(int subId, boolean report) {
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                service.carrierActionReportDefaultNetworkStatus(subId, report);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#carrierActionReportDefaultNetworkStatus", e);
        }
    }

    public void carrierActionResetAll(int subId) {
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                service.carrierActionResetAll(subId);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#carrierActionResetAll", e);
        }
    }

    public NetworkStats getVtDataUsage(int how) {
        boolean perUidStats = true;
        if (how != 1) {
            perUidStats = false;
        }
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                return service.getVtDataUsage(getSubId(), perUidStats);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#getVtDataUsage", e);
        }
        return null;
    }

    public void setPolicyDataEnabled(boolean enabled, int subId) {
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                service.setPolicyDataEnabled(enabled, subId);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#setPolicyDataEnabled", e);
        }
    }

    public List<ClientRequestStats> getClientRequestStats(int subId) {
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                return service.getClientRequestStats(getOpPackageName(), subId);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#getClientRequestStats", e);
        }
        return null;
    }

    @SystemApi
    public boolean getEmergencyCallbackMode() {
        return getEmergencyCallbackMode(getSubId());
    }

    public boolean getEmergencyCallbackMode(int subId) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony == null) {
                return false;
            }
            return telephony.getEmergencyCallbackMode(subId);
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#getEmergencyCallbackMode", e);
            return false;
        }
    }

    public boolean isManualNetworkSelectionAllowed() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.isManualNetworkSelectionAllowed(getSubId());
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#isManualNetworkSelectionAllowed", e);
        }
        return true;
    }

    public SignalStrength getSignalStrength() {
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                return service.getSignalStrength(getSubId());
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#getSignalStrength", e);
        }
        return null;
    }

    public boolean isDataCapable() {
        try {
            int subId = getSubId(SubscriptionManager.getDefaultDataSubscriptionId());
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.isDataEnabled(subId);
            }
            return false;
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelephony#isDataEnabled", e);
            return false;
        } catch (NullPointerException e2) {
            return false;
        }
    }

    public void setRadioIndicationUpdateMode(int filters, int updateMode) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.setRadioIndicationUpdateMode(getSubId(), filters, updateMode);
            }
        } catch (RemoteException ex) {
            if (!isSystemProcess()) {
                ex.rethrowAsRuntimeException();
            }
        }
    }

    @Deprecated
    public void setCarrierTestOverride(String mccmnc, String imsi, String iccid, String gid1, String gid2, String plmn, String spn) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.setCarrierTestOverride(getSubId(), mccmnc, imsi, iccid, gid1, gid2, plmn, spn, null, null);
            }
        } catch (RemoteException e) {
        }
    }

    public void setCarrierTestOverride(String mccmnc, String imsi, String iccid, String gid1, String gid2, String plmn, String spn, String carrierPriviledgeRules, String apn) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.setCarrierTestOverride(getSubId(), mccmnc, imsi, iccid, gid1, gid2, plmn, spn, carrierPriviledgeRules, apn);
            }
        } catch (RemoteException e) {
        }
    }

    public int getCarrierIdListVersion() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getCarrierIdListVersion(getSubId());
            }
        } catch (RemoteException e) {
        }
        return -1;
    }

    public int getNumberOfModemsWithSimultaneousDataConnections() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getNumberOfModemsWithSimultaneousDataConnections(getSubId(), getOpPackageName());
            }
        } catch (RemoteException e) {
        }
        return 0;
    }

    public boolean setOpportunisticNetworkState(boolean enable) {
        Context context = this.mContext;
        String pkgForDebug = context != null ? context.getOpPackageName() : MediaStore.UNKNOWN_STRING;
        try {
            IOns iOpportunisticNetworkService = getIOns();
            if (iOpportunisticNetworkService != null) {
                return iOpportunisticNetworkService.setEnable(enable, pkgForDebug);
            }
            return false;
        } catch (RemoteException ex) {
            Rlog.e(TAG, "enableOpportunisticNetwork RemoteException", ex);
            return false;
        }
    }

    public boolean isOpportunisticNetworkEnabled() {
        Context context = this.mContext;
        String pkgForDebug = context != null ? context.getOpPackageName() : MediaStore.UNKNOWN_STRING;
        try {
            IOns iOpportunisticNetworkService = getIOns();
            if (iOpportunisticNetworkService != null) {
                return iOpportunisticNetworkService.isEnabled(pkgForDebug);
            }
            return false;
        } catch (RemoteException ex) {
            Rlog.e(TAG, "enableOpportunisticNetwork RemoteException", ex);
            return false;
        }
    }

    @SystemApi
    public long getSupportedRadioAccessFamily() {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return (long) telephony.getRadioAccessFamily(getSlotIndex(), getOpPackageName());
            }
            return 0;
        } catch (RemoteException e) {
            return 0;
        } catch (NullPointerException e2) {
            return 0;
        }
    }

    @SystemApi
    public boolean isEmergencyAssistanceEnabled() {
        this.mContext.enforceCallingOrSelfPermission(permission.READ_PRIVILEGED_PHONE_STATE, "isEmergencyAssistanceEnabled");
        return true;
    }

    public Map<Integer, List<EmergencyNumber>> getEmergencyNumberList() {
        Map<Integer, List<EmergencyNumber>> emergencyNumberList = new HashMap();
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.getEmergencyNumberList(this.mContext.getOpPackageName());
            }
            throw new IllegalStateException("telephony service is null.");
        } catch (RemoteException ex) {
            Log.e(TAG, "getEmergencyNumberList RemoteException", ex);
            ex.rethrowAsRuntimeException();
            return emergencyNumberList;
        }
    }

    public Map<Integer, List<EmergencyNumber>> getEmergencyNumberList(int categories) {
        Map<Integer, List<EmergencyNumber>> emergencyNumberList = new HashMap();
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                emergencyNumberList = telephony.getEmergencyNumberList(this.mContext.getOpPackageName());
                if (emergencyNumberList != null) {
                    for (Integer subscriptionId : emergencyNumberList.keySet()) {
                        List<EmergencyNumber> numberList = (List) emergencyNumberList.get(subscriptionId);
                        for (EmergencyNumber number : numberList) {
                            if (!number.isInEmergencyServiceCategories(categories)) {
                                numberList.remove(number);
                            }
                        }
                    }
                }
                return emergencyNumberList;
            }
            throw new IllegalStateException("telephony service is null.");
        } catch (RemoteException ex) {
            Log.e(TAG, "getEmergencyNumberList with Categories RemoteException", ex);
            ex.rethrowAsRuntimeException();
            return emergencyNumberList;
        }
    }

    public boolean isEmergencyNumber(String number) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.isEmergencyNumber(number, true);
            }
            throw new IllegalStateException("telephony service is null.");
        } catch (RemoteException ex) {
            Log.e(TAG, "isEmergencyNumber RemoteException", ex);
            ex.rethrowAsRuntimeException();
            return false;
        }
    }

    @SystemApi
    public boolean isPotentialEmergencyNumber(String number) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.isEmergencyNumber(number, false);
            }
            throw new IllegalStateException("telephony service is null.");
        } catch (RemoteException ex) {
            Log.e(TAG, "isEmergencyNumber RemoteException", ex);
            ex.rethrowAsRuntimeException();
            return false;
        }
    }

    public void setPreferredOpportunisticDataSubscription(int subId, boolean needValidation, final Executor executor, final Consumer<Integer> callback) {
        Context context = this.mContext;
        String pkgForDebug = context != null ? context.getOpPackageName() : MediaStore.UNKNOWN_STRING;
        try {
            IOns iOpportunisticNetworkService = getIOns();
            if (iOpportunisticNetworkService != null) {
                iOpportunisticNetworkService.setPreferredDataSubscriptionId(subId, needValidation, new ISetOpportunisticDataCallback.Stub() {
                    public void onComplete(int result) {
                        Executor executor = executor;
                        if (executor != null) {
                            Consumer consumer = callback;
                            if (consumer != null) {
                                Binder.withCleanCallingIdentity(new -$$Lambda$TelephonyManager$5$RFt1EExZlmUUXRBea_EWHl9kTkc(executor, consumer, result));
                            }
                        }
                    }
                }, pkgForDebug);
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "setPreferredDataSubscriptionId RemoteException", ex);
        }
    }

    public int getPreferredOpportunisticDataSubscription() {
        Context context = this.mContext;
        String pkgForDebug = context != null ? context.getOpPackageName() : MediaStore.UNKNOWN_STRING;
        try {
            IOns iOpportunisticNetworkService = getIOns();
            if (iOpportunisticNetworkService != null) {
                return iOpportunisticNetworkService.getPreferredDataSubscriptionId(pkgForDebug);
            }
            return -1;
        } catch (RemoteException ex) {
            Rlog.e(TAG, "getPreferredDataSubscriptionId RemoteException", ex);
            return -1;
        }
    }

    public void updateAvailableNetworks(List<AvailableNetworkInfo> availableNetworks, final Executor executor, final Consumer<Integer> callback) {
        Context context = this.mContext;
        String pkgForDebug = context != null ? context.getOpPackageName() : MediaStore.UNKNOWN_STRING;
        try {
            IOns iOpportunisticNetworkService = getIOns();
            if (iOpportunisticNetworkService != null) {
                if (availableNetworks != null) {
                    iOpportunisticNetworkService.updateAvailableNetworks(availableNetworks, new IUpdateAvailableNetworksCallback.Stub() {
                        public void onComplete(int result) {
                            Executor executor = executor;
                            if (executor != null) {
                                Consumer consumer = callback;
                                if (consumer != null) {
                                    Binder.withCleanCallingIdentity(new -$$Lambda$TelephonyManager$6$1S5Pi2oZUOPIU8alAP53FlL2sjk(executor, consumer, result));
                                }
                            }
                        }
                    }, pkgForDebug);
                    return;
                }
            }
            Binder.withCleanCallingIdentity(new -$$Lambda$TelephonyManager$eMNW6lCcxHLvIrcBQvhUXUKuLFU(executor, callback));
        } catch (RemoteException ex) {
            Rlog.e(TAG, "updateAvailableNetworks RemoteException", ex);
        }
    }

    @SystemApi
    public boolean enableModemForSlot(int slotIndex, boolean enable) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.enableModemForSlot(slotIndex, enable);
            }
            return false;
        } catch (RemoteException ex) {
            Log.e(TAG, "enableModem RemoteException", ex);
            return false;
        }
    }

    public boolean isModemEnabledForSlot(int slotIndex) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                return telephony.isModemEnabledForSlot(slotIndex, this.mContext.getOpPackageName());
            }
        } catch (RemoteException ex) {
            Log.e(TAG, "enableModem RemoteException", ex);
        }
        return false;
    }

    @SystemApi
    public void setMultiSimCarrierRestriction(boolean isMultiSimCarrierRestricted) {
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                service.setMultiSimCarrierRestriction(isMultiSimCarrierRestricted);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "setMultiSimCarrierRestriction RemoteException", e);
        }
    }

    public int isMultiSimSupported() {
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                return service.isMultiSimSupported(getOpPackageName());
            }
        } catch (RemoteException e) {
            Log.e(TAG, "isMultiSimSupported RemoteException", e);
        }
        return 1;
    }

    public void switchMultiSimConfig(int numOfSims) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                telephony.switchMultiSimConfig(numOfSims);
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "switchMultiSimConfig RemoteException", ex);
        }
    }

    public boolean doesSwitchMultiSimConfigTriggerReboot() {
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                return service.doesSwitchMultiSimConfigTriggerReboot(getSubId(), getOpPackageName());
            }
        } catch (RemoteException e) {
            Log.e(TAG, "doesSwitchMultiSimConfigTriggerReboot RemoteException", e);
        }
        return false;
    }

    public Pair<Integer, Integer> getRadioHalVersion() {
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                int version = service.getRadioHalVersion();
                if (version == -1) {
                    return new Pair(Integer.valueOf(-1), Integer.valueOf(-1));
                }
                return new Pair(Integer.valueOf(version / 100), Integer.valueOf(version % 100));
            }
        } catch (RemoteException e) {
            Log.e(TAG, "getRadioHalVersion() RemoteException", e);
        }
        return new Pair(Integer.valueOf(-1), Integer.valueOf(-1));
    }

    public boolean isDataEnabledForApn(int apnType) {
        Context context = this.mContext;
        String pkgForDebug = context != null ? context.getOpPackageName() : MediaStore.UNKNOWN_STRING;
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                return service.isDataEnabledForApn(apnType, getSubId(), pkgForDebug);
            }
        } catch (RemoteException ex) {
            if (!isSystemProcess()) {
                ex.rethrowAsRuntimeException();
            }
        }
        return false;
    }

    public boolean isApnMetered(int apnType) {
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                return service.isApnMetered(apnType, getSubId());
            }
        } catch (RemoteException ex) {
            if (!isSystemProcess()) {
                ex.rethrowAsRuntimeException();
            }
        }
        return true;
    }

    public boolean setDataAllowedDuringVoiceCall(boolean allow) {
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                return service.setDataAllowedDuringVoiceCall(getSubId(), allow);
            }
        } catch (RemoteException ex) {
            if (!isSystemProcess()) {
                ex.rethrowAsRuntimeException();
            }
        }
        return false;
    }

    public boolean isDataAllowedInVoiceCall() {
        try {
            ITelephony service = getITelephony();
            if (service != null) {
                return service.isDataAllowedInVoiceCall(getSubId());
            }
        } catch (RemoteException ex) {
            if (!isSystemProcess()) {
                ex.rethrowAsRuntimeException();
            }
        }
        return false;
    }
}
