package android.net.wifi;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ParceledListSlice;
import android.net.DhcpInfo;
import android.net.Network;
import android.net.NetworkRequest.Builder;
import android.net.wifi.IDppCallback.Stub;
import android.net.wifi.hotspot2.IProvisioningCallback;
import android.net.wifi.hotspot2.OsuProvider;
import android.net.wifi.hotspot2.PasspointConfiguration;
import android.net.wifi.hotspot2.ProvisioningCallback;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.WorkSource;
import android.util.Log;
import android.util.Pair;
import android.util.SeempLog;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.AsyncChannel;
import com.android.server.net.NetworkPinner;
import dalvik.system.CloseGuard;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

public class WifiManager {
    public static final String ACTION_PASSPOINT_DEAUTH_IMMINENT = "android.net.wifi.action.PASSPOINT_DEAUTH_IMMINENT";
    public static final String ACTION_PASSPOINT_ICON = "android.net.wifi.action.PASSPOINT_ICON";
    public static final String ACTION_PASSPOINT_LAUNCH_OSU_VIEW = "android.net.wifi.action.PASSPOINT_LAUNCH_OSU_VIEW";
    public static final String ACTION_PASSPOINT_OSU_PROVIDERS_LIST = "android.net.wifi.action.PASSPOINT_OSU_PROVIDERS_LIST";
    public static final String ACTION_PASSPOINT_SUBSCRIPTION_REMEDIATION = "android.net.wifi.action.PASSPOINT_SUBSCRIPTION_REMEDIATION";
    public static final String ACTION_PICK_WIFI_NETWORK = "android.net.wifi.PICK_WIFI_NETWORK";
    public static final String ACTION_REQUEST_DISABLE = "android.net.wifi.action.REQUEST_DISABLE";
    public static final String ACTION_REQUEST_ENABLE = "android.net.wifi.action.REQUEST_ENABLE";
    public static final String ACTION_REQUEST_SCAN_ALWAYS_AVAILABLE = "android.net.wifi.action.REQUEST_SCAN_ALWAYS_AVAILABLE";
    public static final String ACTION_WIFI_DISCONNECT_IN_PROGRESS = "com.qualcomm.qti.net.wifi.WIFI_DISCONNECT_IN_PROGRESS";
    public static final String ACTION_WIFI_NETWORK_SUGGESTION_POST_CONNECTION = "android.net.wifi.action.WIFI_NETWORK_SUGGESTION_POST_CONNECTION";
    private static final int BASE = 151552;
    @Deprecated
    public static final String BATCHED_SCAN_RESULTS_AVAILABLE_ACTION = "android.net.wifi.BATCHED_RESULTS";
    public static final int BUSY = 2;
    public static final int CANCEL_WPS = 151566;
    public static final int CANCEL_WPS_FAILED = 151567;
    public static final int CANCEL_WPS_SUCCEDED = 151568;
    @SystemApi
    public static final int CHANGE_REASON_ADDED = 0;
    @SystemApi
    public static final int CHANGE_REASON_CONFIG_CHANGE = 2;
    @SystemApi
    public static final int CHANGE_REASON_REMOVED = 1;
    @SystemApi
    public static final String CONFIGURED_NETWORKS_CHANGED_ACTION = "android.net.wifi.CONFIGURED_NETWORKS_CHANGE";
    public static final int CONNECT_NETWORK = 151553;
    public static final int CONNECT_NETWORK_FAILED = 151554;
    public static final int CONNECT_NETWORK_SUCCEEDED = 151555;
    public static final boolean DEFAULT_POOR_NETWORK_AVOIDANCE_ENABLED = false;
    @SystemApi
    public static final int DEVICE_MOBILITY_STATE_HIGH_MVMT = 1;
    @SystemApi
    public static final int DEVICE_MOBILITY_STATE_LOW_MVMT = 2;
    @SystemApi
    public static final int DEVICE_MOBILITY_STATE_STATIONARY = 3;
    @SystemApi
    public static final int DEVICE_MOBILITY_STATE_UNKNOWN = 0;
    public static final int DISABLE_NETWORK = 151569;
    public static final int DISABLE_NETWORK_FAILED = 151570;
    public static final int DISABLE_NETWORK_SUCCEEDED = 151571;
    public static final String DPP_EVENT_ACTION = "com.qualcomm.qti.net.wifi.DPP_EVENT";
    @SystemApi
    public static final int EASY_CONNECT_NETWORK_ROLE_AP = 1;
    @SystemApi
    public static final int EASY_CONNECT_NETWORK_ROLE_STA = 0;
    public static final int ERROR = 0;
    @Deprecated
    public static final int ERROR_AUTHENTICATING = 1;
    @Deprecated
    public static final int ERROR_AUTH_FAILURE_EAP_FAILURE = 3;
    @Deprecated
    public static final int ERROR_AUTH_FAILURE_NONE = 0;
    @Deprecated
    public static final int ERROR_AUTH_FAILURE_TIMEOUT = 1;
    @Deprecated
    public static final int ERROR_AUTH_FAILURE_WRONG_PSWD = 2;
    public static final String EXTRA_ANQP_ELEMENT_DATA = "android.net.wifi.extra.ANQP_ELEMENT_DATA";
    @Deprecated
    public static final String EXTRA_BSSID = "bssid";
    public static final String EXTRA_BSSID_LONG = "android.net.wifi.extra.BSSID_LONG";
    @SystemApi
    public static final String EXTRA_CHANGE_REASON = "changeReason";
    public static final String EXTRA_COUNTRY_CODE = "country_code";
    public static final String EXTRA_DELAY = "android.net.wifi.extra.DELAY";
    public static final String EXTRA_DPP_EVENT_DATA = "dppEventData";
    public static final String EXTRA_DPP_EVENT_TYPE = "dppEventType";
    public static final String EXTRA_ESS = "android.net.wifi.extra.ESS";
    public static final String EXTRA_FILENAME = "android.net.wifi.extra.FILENAME";
    public static final String EXTRA_ICON = "android.net.wifi.extra.ICON";
    public static final String EXTRA_LINK_PROPERTIES = "linkProperties";
    @SystemApi
    public static final String EXTRA_MULTIPLE_NETWORKS_CHANGED = "multipleChanges";
    public static final String EXTRA_NETWORK_CAPABILITIES = "networkCapabilities";
    public static final String EXTRA_NETWORK_INFO = "networkInfo";
    public static final String EXTRA_NETWORK_SUGGESTION = "android.net.wifi.extra.NETWORK_SUGGESTION";
    public static final String EXTRA_NEW_RSSI = "newRssi";
    @Deprecated
    public static final String EXTRA_NEW_STATE = "newState";
    public static final String EXTRA_OSU_NETWORK = "android.net.wifi.extra.OSU_NETWORK";
    @SystemApi
    public static final String EXTRA_PREVIOUS_WIFI_AP_STATE = "previous_wifi_state";
    public static final String EXTRA_PREVIOUS_WIFI_STATE = "previous_wifi_state";
    public static final String EXTRA_RESULTS_UPDATED = "resultsUpdated";
    public static final String EXTRA_SCAN_AVAILABLE = "scan_enabled";
    public static final String EXTRA_SUBSCRIPTION_REMEDIATION_METHOD = "android.net.wifi.extra.SUBSCRIPTION_REMEDIATION_METHOD";
    @Deprecated
    public static final String EXTRA_SUPPLICANT_CONNECTED = "connected";
    @Deprecated
    public static final String EXTRA_SUPPLICANT_ERROR = "supplicantError";
    @Deprecated
    public static final String EXTRA_SUPPLICANT_ERROR_REASON = "supplicantErrorReason";
    public static final String EXTRA_URL = "android.net.wifi.extra.URL";
    public static final String EXTRA_WIFI_AP_FAILURE_REASON = "wifi_ap_error_code";
    public static final String EXTRA_WIFI_AP_INTERFACE_NAME = "wifi_ap_interface_name";
    public static final String EXTRA_WIFI_AP_MODE = "wifi_ap_mode";
    @SystemApi
    public static final String EXTRA_WIFI_AP_STATE = "wifi_state";
    @SystemApi
    public static final String EXTRA_WIFI_CONFIGURATION = "wifiConfiguration";
    @SystemApi
    public static final String EXTRA_WIFI_CREDENTIAL_EVENT_TYPE = "et";
    @SystemApi
    public static final String EXTRA_WIFI_CREDENTIAL_SSID = "ssid";
    public static final String EXTRA_WIFI_DATA_STALL_REASON = "data_stall_reasoncode";
    @Deprecated
    public static final String EXTRA_WIFI_INFO = "wifiInfo";
    public static final String EXTRA_WIFI_STATE = "wifi_state";
    public static final int FORGET_NETWORK = 151556;
    public static final int FORGET_NETWORK_FAILED = 151557;
    public static final int FORGET_NETWORK_SUCCEEDED = 151558;
    public static final int HOTSPOT_FAILED = 2;
    public static final int HOTSPOT_OBSERVER_REGISTERED = 3;
    public static final int HOTSPOT_STARTED = 0;
    public static final int HOTSPOT_STOPPED = 1;
    public static final int IFACE_IP_MODE_CONFIGURATION_ERROR = 0;
    public static final int IFACE_IP_MODE_LOCAL_ONLY = 2;
    public static final int IFACE_IP_MODE_TETHERED = 1;
    public static final int IFACE_IP_MODE_UNSPECIFIED = -1;
    public static final int INVALID_ARGS = 8;
    private static final int INVALID_KEY = 0;
    public static final int IN_PROGRESS = 1;
    @UnsupportedAppUsage
    public static final String LINK_CONFIGURATION_CHANGED_ACTION = "android.net.wifi.LINK_CONFIGURATION_CHANGED";
    private static final int MAX_ACTIVE_LOCKS = 50;
    @UnsupportedAppUsage
    private static final int MAX_RSSI = -55;
    @UnsupportedAppUsage
    private static final int MIN_RSSI = -100;
    public static final String NETWORK_IDS_CHANGED_ACTION = "android.net.wifi.NETWORK_IDS_CHANGED";
    public static final String NETWORK_STATE_CHANGED_ACTION = "android.net.wifi.STATE_CHANGE";
    public static final int NETWORK_SUGGESTIONS_MAX_PER_APP = (ActivityManager.isLowRamDeviceStatic() ? 256 : 1024);
    public static final int NOT_AUTHORIZED = 9;
    @SystemApi
    public static final int PASSPOINT_HOME_NETWORK = 0;
    @SystemApi
    public static final int PASSPOINT_ROAMING_NETWORK = 1;
    public static final String RSSI_CHANGED_ACTION = "android.net.wifi.RSSI_CHANGED";
    @UnsupportedAppUsage
    public static final int RSSI_LEVELS = 5;
    public static final int RSSI_PKTCNT_FETCH = 151572;
    public static final int RSSI_PKTCNT_FETCH_FAILED = 151574;
    public static final int RSSI_PKTCNT_FETCH_SUCCEEDED = 151573;
    public static final int SAP_START_FAILURE_GENERAL = 0;
    public static final int SAP_START_FAILURE_NO_CHANNEL = 1;
    public static final int SAVE_NETWORK = 151559;
    public static final int SAVE_NETWORK_FAILED = 151560;
    public static final int SAVE_NETWORK_SUCCEEDED = 151561;
    public static final String SCAN_RESULTS_AVAILABLE_ACTION = "android.net.wifi.SCAN_RESULTS";
    public static final int START_WPS = 151562;
    public static final int START_WPS_SUCCEEDED = 151563;
    public static final int STATUS_NETWORK_SUGGESTIONS_ERROR_ADD_DUPLICATE = 3;
    public static final int STATUS_NETWORK_SUGGESTIONS_ERROR_ADD_EXCEEDS_MAX_PER_APP = 4;
    public static final int STATUS_NETWORK_SUGGESTIONS_ERROR_APP_DISALLOWED = 2;
    public static final int STATUS_NETWORK_SUGGESTIONS_ERROR_INTERNAL = 1;
    public static final int STATUS_NETWORK_SUGGESTIONS_ERROR_REMOVE_INVALID = 5;
    public static final int STATUS_NETWORK_SUGGESTIONS_SUCCESS = 0;
    @Deprecated
    public static final String SUPPLICANT_CONNECTION_CHANGE_ACTION = "android.net.wifi.supplicant.CONNECTION_CHANGE";
    @Deprecated
    public static final String SUPPLICANT_STATE_CHANGED_ACTION = "android.net.wifi.supplicant.STATE_CHANGE";
    private static final String TAG = "WifiManager";
    @SystemApi
    public static final String WIFI_AP_STATE_CHANGED_ACTION = "android.net.wifi.WIFI_AP_STATE_CHANGED";
    @SystemApi
    public static final int WIFI_AP_STATE_DISABLED = 11;
    @SystemApi
    public static final int WIFI_AP_STATE_DISABLING = 10;
    @SystemApi
    public static final int WIFI_AP_STATE_ENABLED = 13;
    @SystemApi
    public static final int WIFI_AP_STATE_ENABLING = 12;
    @SystemApi
    public static final int WIFI_AP_STATE_FAILED = 14;
    public static final String WIFI_COUNTRY_CODE_CHANGED_ACTION = "android.net.wifi.COUNTRY_CODE_CHANGED";
    @SystemApi
    public static final String WIFI_CREDENTIAL_CHANGED_ACTION = "android.net.wifi.WIFI_CREDENTIAL_CHANGED";
    @SystemApi
    public static final int WIFI_CREDENTIAL_FORGOT = 1;
    @SystemApi
    public static final int WIFI_CREDENTIAL_SAVED = 0;
    public static final String WIFI_DATA_STALL = "com.qualcomm.qti.net.wifi.WIFI_DATA_STALL";
    public static final int WIFI_FEATURE_ADDITIONAL_STA = 2048;
    public static final int WIFI_FEATURE_AP_STA = 32768;
    public static final int WIFI_FEATURE_AWARE = 64;
    public static final int WIFI_FEATURE_BATCH_SCAN = 512;
    public static final int WIFI_FEATURE_CONFIG_NDO = 2097152;
    public static final int WIFI_FEATURE_CONTROL_ROAMING = 8388608;
    public static final int WIFI_FEATURE_D2AP_RTT = 256;
    public static final int WIFI_FEATURE_D2D_RTT = 128;
    public static final int WIFI_FEATURE_DPP = Integer.MIN_VALUE;
    public static final int WIFI_FEATURE_EPR = 16384;
    public static final int WIFI_FEATURE_HAL_EPNO = 262144;
    public static final int WIFI_FEATURE_IE_WHITELIST = 16777216;
    public static final int WIFI_FEATURE_INFRA = 1;
    public static final int WIFI_FEATURE_INFRA_5G = 2;
    public static final int WIFI_FEATURE_LINK_LAYER_STATS = 65536;
    public static final int WIFI_FEATURE_LOGGER = 131072;
    public static final int WIFI_FEATURE_LOW_LATENCY = 1073741824;
    public static final int WIFI_FEATURE_MKEEP_ALIVE = 1048576;
    public static final int WIFI_FEATURE_MOBILE_HOTSPOT = 16;
    public static final int WIFI_FEATURE_OWE = 536870912;
    public static final int WIFI_FEATURE_P2P = 8;
    public static final long WIFI_FEATURE_P2P_RAND_MAC = 4294967296L;
    public static final int WIFI_FEATURE_PASSPOINT = 4;
    public static final int WIFI_FEATURE_PNO = 1024;
    public static final int WIFI_FEATURE_RSSI_MONITOR = 524288;
    public static final int WIFI_FEATURE_SCANNER = 32;
    public static final int WIFI_FEATURE_SCAN_RAND = 33554432;
    public static final int WIFI_FEATURE_TDLS = 4096;
    public static final int WIFI_FEATURE_TDLS_OFFCHANNEL = 8192;
    public static final int WIFI_FEATURE_TRANSMIT_POWER = 4194304;
    public static final int WIFI_FEATURE_TX_POWER_LIMIT = 67108864;
    public static final long WIFI_FEATURE_WAPI_CERT = 17179869184L;
    public static final long WIFI_FEATURE_WAPI_PSK = 8589934592L;
    public static final int WIFI_FEATURE_WPA3_SAE = 134217728;
    public static final int WIFI_FEATURE_WPA3_SUITE_B = 268435456;
    @UnsupportedAppUsage
    public static final int WIFI_FREQUENCY_BAND_2GHZ = 2;
    @UnsupportedAppUsage
    public static final int WIFI_FREQUENCY_BAND_5GHZ = 1;
    @UnsupportedAppUsage
    public static final int WIFI_FREQUENCY_BAND_AUTO = 0;
    @Deprecated
    public static final int WIFI_MODE_FULL = 1;
    public static final int WIFI_MODE_FULL_HIGH_PERF = 3;
    public static final int WIFI_MODE_FULL_LOW_LATENCY = 4;
    public static final int WIFI_MODE_NO_LOCKS_HELD = 0;
    @Deprecated
    public static final int WIFI_MODE_SCAN_ONLY = 2;
    public static final String WIFI_SCAN_AVAILABLE = "wifi_scan_available";
    public static final String WIFI_STATE_CHANGED_ACTION = "android.net.wifi.WIFI_STATE_CHANGED";
    public static final int WIFI_STATE_DISABLED = 1;
    public static final int WIFI_STATE_DISABLING = 0;
    public static final int WIFI_STATE_ENABLED = 3;
    public static final int WIFI_STATE_ENABLING = 2;
    public static final int WIFI_STATE_UNKNOWN = 4;
    public static final int WPS_AUTH_FAILURE = 6;
    public static final int WPS_COMPLETED = 151565;
    public static final int WPS_FAILED = 151564;
    public static final int WPS_OVERLAP_ERROR = 3;
    public static final int WPS_TIMED_OUT = 7;
    public static final int WPS_TKIP_ONLY_PROHIBITED = 5;
    public static final int WPS_WEP_PROHIBITED = 4;
    private static final Object sServiceHandlerDispatchLock = new Object();
    @UnsupportedAppUsage
    private int mActiveLockCount;
    private AsyncChannel mAsyncChannel;
    private CountDownLatch mConnected;
    private Context mContext;
    @GuardedBy({"mLock"})
    private LocalOnlyHotspotCallbackProxy mLOHSCallbackProxy;
    @GuardedBy({"mLock"})
    private LocalOnlyHotspotObserverProxy mLOHSObserverProxy;
    private int mListenerKey = 1;
    private final SparseArray mListenerMap = new SparseArray();
    private final Object mListenerMapLock = new Object();
    private final Object mLock = new Object();
    private Looper mLooper;
    @UnsupportedAppUsage
    IWifiManager mService;
    private final int mTargetSdkVersion;
    private boolean mVerboseLoggingEnabled = false;

    @SystemApi
    public interface ActionListener {
        void onFailure(int i);

        void onSuccess();
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DeviceMobilityState {
    }

    private static class EasyConnectCallbackProxy extends Stub {
        private final EasyConnectStatusCallback mEasyConnectStatusCallback;
        private final Executor mExecutor;

        EasyConnectCallbackProxy(Executor executor, EasyConnectStatusCallback easyConnectStatusCallback) {
            this.mExecutor = executor;
            this.mEasyConnectStatusCallback = easyConnectStatusCallback;
        }

        public void onSuccessConfigReceived(int newNetworkId) {
            Log.d(WifiManager.TAG, "Easy Connect onSuccessConfigReceived callback");
            this.mExecutor.execute(new -$$Lambda$WifiManager$EasyConnectCallbackProxy$ObU39aoKguVIx_qQTyZyomhDAAg(this, newNetworkId));
        }

        public /* synthetic */ void lambda$onSuccessConfigReceived$0$WifiManager$EasyConnectCallbackProxy(int newNetworkId) {
            this.mEasyConnectStatusCallback.onEnrolleeSuccess(newNetworkId);
        }

        public void onSuccess(int status) {
            Log.d(WifiManager.TAG, "Easy Connect onSuccess callback");
            this.mExecutor.execute(new -$$Lambda$WifiManager$EasyConnectCallbackProxy$wTsmN4734yyutavZxcKa2TZ-4Cc(this, status));
        }

        public /* synthetic */ void lambda$onSuccess$1$WifiManager$EasyConnectCallbackProxy(int status) {
            this.mEasyConnectStatusCallback.onConfiguratorSuccess(status);
        }

        public void onFailure(int status) {
            Log.d(WifiManager.TAG, "Easy Connect onFailure callback");
            this.mExecutor.execute(new -$$Lambda$WifiManager$EasyConnectCallbackProxy$fmVMj2ImIgtBYa9roBT0GyOubTI(this, status));
        }

        public /* synthetic */ void lambda$onFailure$2$WifiManager$EasyConnectCallbackProxy(int status) {
            this.mEasyConnectStatusCallback.onFailure(status);
        }

        public void onProgress(int status) {
            Log.d(WifiManager.TAG, "Easy Connect onProgress callback");
            this.mExecutor.execute(new -$$Lambda$WifiManager$EasyConnectCallbackProxy$YV1XBtKl8L8u8zCEX4lzLkOT6LQ(this, status));
        }

        public /* synthetic */ void lambda$onProgress$3$WifiManager$EasyConnectCallbackProxy(int status) {
            this.mEasyConnectStatusCallback.onProgress(status);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface EasyConnectNetworkRole {
    }

    public static class LocalOnlyHotspotCallback {
        public static final int ERROR_GENERIC = 2;
        public static final int ERROR_INCOMPATIBLE_MODE = 3;
        public static final int ERROR_NO_CHANNEL = 1;
        public static final int ERROR_TETHERING_DISALLOWED = 4;
        public static final int REQUEST_REGISTERED = 0;

        public void onStarted(LocalOnlyHotspotReservation reservation) {
        }

        public void onStopped() {
        }

        public void onFailed(int reason) {
        }
    }

    private static class LocalOnlyHotspotCallbackProxy {
        private final Handler mHandler;
        private final Looper mLooper;
        private final Messenger mMessenger = new Messenger(this.mHandler);
        private final WeakReference<WifiManager> mWifiManager;

        LocalOnlyHotspotCallbackProxy(WifiManager manager, Looper looper, final LocalOnlyHotspotCallback callback) {
            this.mWifiManager = new WeakReference(manager);
            this.mLooper = looper;
            this.mHandler = new Handler(looper) {
                public void handleMessage(Message msg) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("LocalOnlyHotspotCallbackProxy: handle message what: ");
                    stringBuilder.append(msg.what);
                    stringBuilder.append(" msg: ");
                    stringBuilder.append(msg);
                    String stringBuilder2 = stringBuilder.toString();
                    String str = WifiManager.TAG;
                    Log.d(str, stringBuilder2);
                    WifiManager manager = (WifiManager) LocalOnlyHotspotCallbackProxy.this.mWifiManager.get();
                    if (manager == null) {
                        Log.w(str, "LocalOnlyHotspotCallbackProxy: handle message post GC");
                        return;
                    }
                    int i = msg.what;
                    if (i == 0) {
                        WifiConfiguration config = msg.obj;
                        if (config == null) {
                            Log.e(str, "LocalOnlyHotspotCallbackProxy: config cannot be null.");
                            callback.onFailed(2);
                            return;
                        }
                        LocalOnlyHotspotCallback localOnlyHotspotCallback = callback;
                        Objects.requireNonNull(manager);
                        localOnlyHotspotCallback.onStarted(new LocalOnlyHotspotReservation(config));
                    } else if (i == 1) {
                        Log.w(str, "LocalOnlyHotspotCallbackProxy: hotspot stopped");
                        callback.onStopped();
                    } else if (i != 2) {
                        StringBuilder stringBuilder3 = new StringBuilder();
                        stringBuilder3.append("LocalOnlyHotspotCallbackProxy unhandled message.  type: ");
                        stringBuilder3.append(msg.what);
                        Log.e(str, stringBuilder3.toString());
                    } else {
                        i = msg.arg1;
                        StringBuilder stringBuilder4 = new StringBuilder();
                        stringBuilder4.append("LocalOnlyHotspotCallbackProxy: failed to start.  reason: ");
                        stringBuilder4.append(i);
                        Log.w(str, stringBuilder4.toString());
                        callback.onFailed(i);
                        Log.w(str, "done with the callback...");
                    }
                }
            };
        }

        public Messenger getMessenger() {
            return this.mMessenger;
        }

        public void notifyFailed(int reason) throws RemoteException {
            Message msg = Message.obtain();
            msg.what = 2;
            msg.arg1 = reason;
            this.mMessenger.send(msg);
        }
    }

    public static class LocalOnlyHotspotObserver {
        public void onRegistered(LocalOnlyHotspotSubscription subscription) {
        }

        public void onStarted(WifiConfiguration config) {
        }

        public void onStopped() {
        }
    }

    private static class LocalOnlyHotspotObserverProxy {
        private final Handler mHandler;
        private final Looper mLooper;
        private final Messenger mMessenger = new Messenger(this.mHandler);
        private final WeakReference<WifiManager> mWifiManager;

        LocalOnlyHotspotObserverProxy(WifiManager manager, Looper looper, final LocalOnlyHotspotObserver observer) {
            this.mWifiManager = new WeakReference(manager);
            this.mLooper = looper;
            this.mHandler = new Handler(looper) {
                public void handleMessage(Message msg) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("LocalOnlyHotspotObserverProxy: handle message what: ");
                    stringBuilder.append(msg.what);
                    stringBuilder.append(" msg: ");
                    stringBuilder.append(msg);
                    String stringBuilder2 = stringBuilder.toString();
                    String str = WifiManager.TAG;
                    Log.d(str, stringBuilder2);
                    WifiManager manager = (WifiManager) LocalOnlyHotspotObserverProxy.this.mWifiManager.get();
                    if (manager == null) {
                        Log.w(str, "LocalOnlyHotspotObserverProxy: handle message post GC");
                        return;
                    }
                    int i = msg.what;
                    if (i == 0) {
                        WifiConfiguration config = msg.obj;
                        if (config == null) {
                            Log.e(str, "LocalOnlyHotspotObserverProxy: config cannot be null.");
                            return;
                        }
                        observer.onStarted(config);
                    } else if (i == 1) {
                        observer.onStopped();
                    } else if (i != 3) {
                        StringBuilder stringBuilder3 = new StringBuilder();
                        stringBuilder3.append("LocalOnlyHotspotObserverProxy unhandled message.  type: ");
                        stringBuilder3.append(msg.what);
                        Log.e(str, stringBuilder3.toString());
                    } else {
                        LocalOnlyHotspotObserver localOnlyHotspotObserver = observer;
                        Objects.requireNonNull(manager);
                        localOnlyHotspotObserver.onRegistered(new LocalOnlyHotspotSubscription());
                    }
                }
            };
        }

        public Messenger getMessenger() {
            return this.mMessenger;
        }

        public void registered() throws RemoteException {
            Message msg = Message.obtain();
            msg.what = 3;
            this.mMessenger.send(msg);
        }
    }

    public class LocalOnlyHotspotReservation implements AutoCloseable {
        private final CloseGuard mCloseGuard = CloseGuard.get();
        private final WifiConfiguration mConfig;

        @VisibleForTesting
        public LocalOnlyHotspotReservation(WifiConfiguration config) {
            this.mConfig = config;
            this.mCloseGuard.open("close");
        }

        public WifiConfiguration getWifiConfiguration() {
            return this.mConfig;
        }

        public void close() {
            try {
                WifiManager.this.stopLocalOnlyHotspot();
                this.mCloseGuard.close();
            } catch (Exception e) {
                Log.e(WifiManager.TAG, "Failed to stop Local Only Hotspot.");
            }
        }

        /* Access modifiers changed, original: protected */
        public void finalize() throws Throwable {
            try {
                if (this.mCloseGuard != null) {
                    this.mCloseGuard.warnIfOpen();
                }
                close();
            } finally {
                super.finalize();
            }
        }
    }

    public class LocalOnlyHotspotSubscription implements AutoCloseable {
        private final CloseGuard mCloseGuard = CloseGuard.get();

        @VisibleForTesting
        public LocalOnlyHotspotSubscription() {
            this.mCloseGuard.open("close");
        }

        public void close() {
            try {
                WifiManager.this.unregisterLocalOnlyHotspotObserver();
                this.mCloseGuard.close();
            } catch (Exception e) {
                Log.e(WifiManager.TAG, "Failed to unregister LocalOnlyHotspotObserver.");
            }
        }

        /* Access modifiers changed, original: protected */
        public void finalize() throws Throwable {
            try {
                if (this.mCloseGuard != null) {
                    this.mCloseGuard.warnIfOpen();
                }
                close();
            } finally {
                super.finalize();
            }
        }
    }

    public class MulticastLock {
        private final IBinder mBinder;
        private boolean mHeld;
        private int mRefCount;
        private boolean mRefCounted;
        private String mTag;

        /* synthetic */ MulticastLock(WifiManager x0, String x1, AnonymousClass1 x2) {
            this(x1);
        }

        private MulticastLock(String tag) {
            this.mTag = tag;
            this.mBinder = new Binder();
            this.mRefCount = 0;
            this.mRefCounted = true;
            this.mHeld = false;
        }

        /* JADX WARNING: Missing block: B:9:0x0012, code skipped:
            if (r5.mHeld == false) goto L_0x0014;
     */
        public void acquire() {
            /*
            r5 = this;
            r0 = r5.mBinder;
            monitor-enter(r0);
            r1 = r5.mRefCounted;	 Catch:{ all -> 0x0052 }
            r2 = 1;
            if (r1 == 0) goto L_0x0010;
        L_0x0008:
            r1 = r5.mRefCount;	 Catch:{ all -> 0x0052 }
            r1 = r1 + r2;
            r5.mRefCount = r1;	 Catch:{ all -> 0x0052 }
            if (r1 != r2) goto L_0x0050;
        L_0x000f:
            goto L_0x0014;
        L_0x0010:
            r1 = r5.mHeld;	 Catch:{ all -> 0x0052 }
            if (r1 != 0) goto L_0x0050;
        L_0x0014:
            r1 = android.net.wifi.WifiManager.this;	 Catch:{ RemoteException -> 0x004a }
            r1 = r1.mService;	 Catch:{ RemoteException -> 0x004a }
            r3 = r5.mBinder;	 Catch:{ RemoteException -> 0x004a }
            r4 = r5.mTag;	 Catch:{ RemoteException -> 0x004a }
            r1.acquireMulticastLock(r3, r4);	 Catch:{ RemoteException -> 0x004a }
            r1 = android.net.wifi.WifiManager.this;	 Catch:{ RemoteException -> 0x004a }
            monitor-enter(r1);	 Catch:{ RemoteException -> 0x004a }
            r3 = android.net.wifi.WifiManager.this;	 Catch:{ all -> 0x0047 }
            r3 = r3.mActiveLockCount;	 Catch:{ all -> 0x0047 }
            r4 = 50;
            if (r3 >= r4) goto L_0x0036;
        L_0x002c:
            r3 = android.net.wifi.WifiManager.this;	 Catch:{ all -> 0x0047 }
            r3.mActiveLockCount = r3.mActiveLockCount + 1;	 Catch:{ all -> 0x0047 }
            monitor-exit(r1);	 Catch:{ all -> 0x0047 }
            r5.mHeld = r2;	 Catch:{ all -> 0x0052 }
            goto L_0x0050;
        L_0x0036:
            r2 = android.net.wifi.WifiManager.this;	 Catch:{ all -> 0x0047 }
            r2 = r2.mService;	 Catch:{ all -> 0x0047 }
            r3 = r5.mTag;	 Catch:{ all -> 0x0047 }
            r2.releaseMulticastLock(r3);	 Catch:{ all -> 0x0047 }
            r2 = new java.lang.UnsupportedOperationException;	 Catch:{ all -> 0x0047 }
            r3 = "Exceeded maximum number of wifi locks";
            r2.<init>(r3);	 Catch:{ all -> 0x0047 }
            throw r2;	 Catch:{ all -> 0x0047 }
        L_0x0047:
            r2 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x0047 }
            throw r2;	 Catch:{ RemoteException -> 0x004a }
        L_0x004a:
            r1 = move-exception;
            r2 = r1.rethrowFromSystemServer();	 Catch:{ all -> 0x0052 }
            throw r2;	 Catch:{ all -> 0x0052 }
        L_0x0050:
            monitor-exit(r0);	 Catch:{ all -> 0x0052 }
            return;
        L_0x0052:
            r1 = move-exception;
            monitor-exit(r0);	 Catch:{ all -> 0x0052 }
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.WifiManager$MulticastLock.acquire():void");
        }

        /* JADX WARNING: Removed duplicated region for block: B:32:0x003a  */
        /* JADX WARNING: Removed duplicated region for block: B:30:0x0038 A:{SKIP} */
        /* JADX WARNING: Missing block: B:9:0x0012, code skipped:
            if (r4.mHeld != false) goto L_0x0014;
     */
        public void release() {
            /*
            r4 = this;
            r0 = r4.mBinder;
            monitor-enter(r0);
            r1 = r4.mRefCounted;	 Catch:{ all -> 0x0053 }
            if (r1 == 0) goto L_0x0010;
        L_0x0007:
            r1 = r4.mRefCount;	 Catch:{ all -> 0x0053 }
            r1 = r1 + -1;
            r4.mRefCount = r1;	 Catch:{ all -> 0x0053 }
            if (r1 != 0) goto L_0x0034;
        L_0x000f:
            goto L_0x0014;
        L_0x0010:
            r1 = r4.mHeld;	 Catch:{ all -> 0x0053 }
            if (r1 == 0) goto L_0x0034;
        L_0x0014:
            r1 = android.net.wifi.WifiManager.this;	 Catch:{ RemoteException -> 0x002e }
            r1 = r1.mService;	 Catch:{ RemoteException -> 0x002e }
            r2 = r4.mTag;	 Catch:{ RemoteException -> 0x002e }
            r1.releaseMulticastLock(r2);	 Catch:{ RemoteException -> 0x002e }
            r1 = android.net.wifi.WifiManager.this;	 Catch:{ RemoteException -> 0x002e }
            monitor-enter(r1);	 Catch:{ RemoteException -> 0x002e }
            r2 = android.net.wifi.WifiManager.this;	 Catch:{ all -> 0x002b }
            r2.mActiveLockCount = r2.mActiveLockCount - 1;	 Catch:{ all -> 0x002b }
            monitor-exit(r1);	 Catch:{ all -> 0x002b }
            r1 = 0;
            r4.mHeld = r1;	 Catch:{ all -> 0x0053 }
            goto L_0x0034;
        L_0x002b:
            r2 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x002b }
            throw r2;	 Catch:{ RemoteException -> 0x002e }
        L_0x002e:
            r1 = move-exception;
            r2 = r1.rethrowFromSystemServer();	 Catch:{ all -> 0x0053 }
            throw r2;	 Catch:{ all -> 0x0053 }
        L_0x0034:
            r1 = r4.mRefCount;	 Catch:{ all -> 0x0053 }
            if (r1 < 0) goto L_0x003a;
        L_0x0038:
            monitor-exit(r0);	 Catch:{ all -> 0x0053 }
            return;
        L_0x003a:
            r1 = new java.lang.RuntimeException;	 Catch:{ all -> 0x0053 }
            r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0053 }
            r2.<init>();	 Catch:{ all -> 0x0053 }
            r3 = "MulticastLock under-locked ";
            r2.append(r3);	 Catch:{ all -> 0x0053 }
            r3 = r4.mTag;	 Catch:{ all -> 0x0053 }
            r2.append(r3);	 Catch:{ all -> 0x0053 }
            r2 = r2.toString();	 Catch:{ all -> 0x0053 }
            r1.<init>(r2);	 Catch:{ all -> 0x0053 }
            throw r1;	 Catch:{ all -> 0x0053 }
        L_0x0053:
            r1 = move-exception;
            monitor-exit(r0);	 Catch:{ all -> 0x0053 }
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.WifiManager$MulticastLock.release():void");
        }

        public void setReferenceCounted(boolean refCounted) {
            this.mRefCounted = refCounted;
        }

        public boolean isHeld() {
            boolean z;
            synchronized (this.mBinder) {
                z = this.mHeld;
            }
            return z;
        }

        public String toString() {
            String stringBuilder;
            synchronized (this.mBinder) {
                String s3;
                String s1 = Integer.toHexString(System.identityHashCode(this));
                String s2 = this.mHeld ? "held; " : "";
                if (this.mRefCounted) {
                    s3 = new StringBuilder();
                    s3.append("refcounted: refcount = ");
                    s3.append(this.mRefCount);
                    s3 = s3.toString();
                } else {
                    s3 = "not refcounted";
                }
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("MulticastLock{ ");
                stringBuilder2.append(s1);
                stringBuilder2.append("; ");
                stringBuilder2.append(s2);
                stringBuilder2.append(s3);
                stringBuilder2.append(" }");
                stringBuilder = stringBuilder2.toString();
            }
            return stringBuilder;
        }

        /* Access modifiers changed, original: protected */
        public void finalize() throws Throwable {
            super.finalize();
            setReferenceCounted(false);
            release();
        }
    }

    public interface NetworkRequestMatchCallback {
        void onAbort();

        void onMatch(List<ScanResult> list);

        void onUserSelectionCallbackRegistration(NetworkRequestUserSelectionCallback networkRequestUserSelectionCallback);

        void onUserSelectionConnectFailure(WifiConfiguration wifiConfiguration);

        void onUserSelectionConnectSuccess(WifiConfiguration wifiConfiguration);
    }

    private class NetworkRequestMatchCallbackProxy extends INetworkRequestMatchCallback.Stub {
        private final NetworkRequestMatchCallback mCallback;
        private final Handler mHandler;

        NetworkRequestMatchCallbackProxy(Looper looper, NetworkRequestMatchCallback callback) {
            this.mHandler = new Handler(looper);
            this.mCallback = callback;
        }

        public void onUserSelectionCallbackRegistration(INetworkRequestUserSelectionCallback userSelectionCallback) {
            if (WifiManager.this.mVerboseLoggingEnabled) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("NetworkRequestMatchCallbackProxy: onUserSelectionCallbackRegistration callback: ");
                stringBuilder.append(userSelectionCallback);
                Log.v(WifiManager.TAG, stringBuilder.toString());
            }
            this.mHandler.post(new -$$Lambda$WifiManager$NetworkRequestMatchCallbackProxy$DYo-nMH0tB37PG_5OviApSTSGXg(this, userSelectionCallback));
        }

        public /* synthetic */ void lambda$onUserSelectionCallbackRegistration$0$WifiManager$NetworkRequestMatchCallbackProxy(INetworkRequestUserSelectionCallback userSelectionCallback) {
            this.mCallback.onUserSelectionCallbackRegistration(new NetworkRequestUserSelectionCallbackProxy(userSelectionCallback));
        }

        public void onAbort() {
            if (WifiManager.this.mVerboseLoggingEnabled) {
                Log.v(WifiManager.TAG, "NetworkRequestMatchCallbackProxy: onAbort");
            }
            this.mHandler.post(new -$$Lambda$WifiManager$NetworkRequestMatchCallbackProxy$sy4224jn5G2QTmFKYUY0fGWCJ5Q(this));
        }

        public /* synthetic */ void lambda$onAbort$1$WifiManager$NetworkRequestMatchCallbackProxy() {
            this.mCallback.onAbort();
        }

        public void onMatch(List<ScanResult> scanResults) {
            if (WifiManager.this.mVerboseLoggingEnabled) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("NetworkRequestMatchCallbackProxy: onMatch scanResults: ");
                stringBuilder.append(scanResults);
                Log.v(WifiManager.TAG, stringBuilder.toString());
            }
            this.mHandler.post(new -$$Lambda$WifiManager$NetworkRequestMatchCallbackProxy$8wy7AFc9OgD124mPKDe8H6vuPTQ(this, scanResults));
        }

        public /* synthetic */ void lambda$onMatch$2$WifiManager$NetworkRequestMatchCallbackProxy(List scanResults) {
            this.mCallback.onMatch(scanResults);
        }

        public void onUserSelectionConnectSuccess(WifiConfiguration wifiConfiguration) {
            if (WifiManager.this.mVerboseLoggingEnabled) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("NetworkRequestMatchCallbackProxy: onUserSelectionConnectSuccess  wificonfiguration: ");
                stringBuilder.append(wifiConfiguration);
                Log.v(WifiManager.TAG, stringBuilder.toString());
            }
            this.mHandler.post(new -$$Lambda$WifiManager$NetworkRequestMatchCallbackProxy$KPxBZNMm8VDinf6ZcYWL1RJk9Zc(this, wifiConfiguration));
        }

        public /* synthetic */ void lambda$onUserSelectionConnectSuccess$3$WifiManager$NetworkRequestMatchCallbackProxy(WifiConfiguration wifiConfiguration) {
            this.mCallback.onUserSelectionConnectSuccess(wifiConfiguration);
        }

        public void onUserSelectionConnectFailure(WifiConfiguration wifiConfiguration) {
            if (WifiManager.this.mVerboseLoggingEnabled) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("NetworkRequestMatchCallbackProxy: onUserSelectionConnectFailure wificonfiguration: ");
                stringBuilder.append(wifiConfiguration);
                Log.v(WifiManager.TAG, stringBuilder.toString());
            }
            this.mHandler.post(new -$$Lambda$WifiManager$NetworkRequestMatchCallbackProxy$MJqaBvGtvUfHUJtjhgTRIQ7GCr4(this, wifiConfiguration));
        }

        public /* synthetic */ void lambda$onUserSelectionConnectFailure$4$WifiManager$NetworkRequestMatchCallbackProxy(WifiConfiguration wifiConfiguration) {
            this.mCallback.onUserSelectionConnectFailure(wifiConfiguration);
        }
    }

    public interface NetworkRequestUserSelectionCallback {
        void reject();

        void select(WifiConfiguration wifiConfiguration);
    }

    private class NetworkRequestUserSelectionCallbackProxy implements NetworkRequestUserSelectionCallback {
        private final INetworkRequestUserSelectionCallback mCallback;

        NetworkRequestUserSelectionCallbackProxy(INetworkRequestUserSelectionCallback callback) {
            this.mCallback = callback;
        }

        public void select(WifiConfiguration wifiConfiguration) {
            boolean access$000 = WifiManager.this.mVerboseLoggingEnabled;
            String str = WifiManager.TAG;
            if (access$000) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("NetworkRequestUserSelectionCallbackProxy: select wificonfiguration: ");
                stringBuilder.append(wifiConfiguration);
                Log.v(str, stringBuilder.toString());
            }
            try {
                this.mCallback.select(wifiConfiguration);
            } catch (RemoteException e) {
                Log.e(str, "Failed to invoke onSelected", e);
                throw e.rethrowFromSystemServer();
            }
        }

        public void reject() {
            boolean access$000 = WifiManager.this.mVerboseLoggingEnabled;
            String str = WifiManager.TAG;
            if (access$000) {
                Log.v(str, "NetworkRequestUserSelectionCallbackProxy: reject");
            }
            try {
                this.mCallback.reject();
            } catch (RemoteException e) {
                Log.e(str, "Failed to invoke onRejected", e);
                throw e.rethrowFromSystemServer();
            }
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface NetworkSuggestionsStatusCode {
    }

    @SystemApi
    public interface OnWifiUsabilityStatsListener {
        void onWifiUsabilityStats(int i, boolean z, WifiUsabilityStatsEntry wifiUsabilityStatsEntry);
    }

    private static class ProvisioningCallbackProxy extends IProvisioningCallback.Stub {
        private final ProvisioningCallback mCallback;
        private final Executor mExecutor;

        ProvisioningCallbackProxy(Executor executor, ProvisioningCallback callback) {
            this.mExecutor = executor;
            this.mCallback = callback;
        }

        public /* synthetic */ void lambda$onProvisioningStatus$0$WifiManager$ProvisioningCallbackProxy(int status) {
            this.mCallback.onProvisioningStatus(status);
        }

        public void onProvisioningStatus(int status) {
            this.mExecutor.execute(new -$$Lambda$WifiManager$ProvisioningCallbackProxy$0_NXiwyrbrT_579x-6QMO0y3rzc(this, status));
        }

        public /* synthetic */ void lambda$onProvisioningFailure$1$WifiManager$ProvisioningCallbackProxy(int status) {
            this.mCallback.onProvisioningFailure(status);
        }

        public void onProvisioningFailure(int status) {
            this.mExecutor.execute(new -$$Lambda$WifiManager$ProvisioningCallbackProxy$rgPeSRj_1qriYZtaCu57EZHtc_Q(this, status));
        }

        public /* synthetic */ void lambda$onProvisioningComplete$2$WifiManager$ProvisioningCallbackProxy() {
            this.mCallback.onProvisioningComplete();
        }

        public void onProvisioningComplete() {
            this.mExecutor.execute(new -$$Lambda$WifiManager$ProvisioningCallbackProxy$ARmFIxMD9Os9eGpiffTyA3WhD0Q(this));
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SapStartFailure {
    }

    private class ServiceHandler extends Handler {
        ServiceHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            synchronized (WifiManager.sServiceHandlerDispatchLock) {
                dispatchMessageToListeners(message);
            }
        }

        private void dispatchMessageToListeners(Message message) {
            Object listener = WifiManager.this.removeListener(message.arg2);
            int i = message.what;
            String str = WifiManager.TAG;
            switch (i) {
                case 69632:
                    if (message.arg1 == 0) {
                        WifiManager.this.mAsyncChannel.sendMessage((int) AsyncChannel.CMD_CHANNEL_FULL_CONNECTION);
                    } else {
                        Log.e(str, "Failed to set up channel connection");
                        WifiManager.this.mAsyncChannel = null;
                    }
                    WifiManager.this.mConnected.countDown();
                    return;
                case AsyncChannel.CMD_CHANNEL_DISCONNECTED /*69636*/:
                    Log.e(str, "Channel connection lost");
                    WifiManager.this.mAsyncChannel = null;
                    getLooper().quit();
                    return;
                case WifiManager.CONNECT_NETWORK_FAILED /*151554*/:
                case WifiManager.FORGET_NETWORK_FAILED /*151557*/:
                case WifiManager.SAVE_NETWORK_FAILED /*151560*/:
                case WifiManager.DISABLE_NETWORK_FAILED /*151570*/:
                    if (listener != null) {
                        ((ActionListener) listener).onFailure(message.arg1);
                        return;
                    }
                    return;
                case WifiManager.CONNECT_NETWORK_SUCCEEDED /*151555*/:
                case WifiManager.FORGET_NETWORK_SUCCEEDED /*151558*/:
                case WifiManager.SAVE_NETWORK_SUCCEEDED /*151561*/:
                case WifiManager.DISABLE_NETWORK_SUCCEEDED /*151571*/:
                    if (listener != null) {
                        ((ActionListener) listener).onSuccess();
                        return;
                    }
                    return;
                case WifiManager.RSSI_PKTCNT_FETCH_SUCCEEDED /*151573*/:
                    if (listener != null) {
                        RssiPacketCountInfo info = message.obj;
                        if (info != null) {
                            ((TxPacketCountListener) listener).onSuccess(info.txgood + info.txbad);
                            return;
                        } else {
                            ((TxPacketCountListener) listener).onFailure(0);
                            return;
                        }
                    }
                    return;
                case WifiManager.RSSI_PKTCNT_FETCH_FAILED /*151574*/:
                    if (listener != null) {
                        ((TxPacketCountListener) listener).onFailure(message.arg1);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public interface SoftApCallback {
        void onNumClientsChanged(int i);

        void onStaConnected(String str, int i);

        void onStaDisconnected(String str, int i);

        void onStateChanged(int i, int i2);
    }

    private class SoftApCallbackProxy extends ISoftApCallback.Stub {
        private final SoftApCallback mCallback;
        private final Handler mHandler;

        SoftApCallbackProxy(Looper looper, SoftApCallback callback) {
            this.mHandler = new Handler(looper);
            this.mCallback = callback;
        }

        public void onStateChanged(int state, int failureReason) {
            if (WifiManager.this.mVerboseLoggingEnabled) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("SoftApCallbackProxy: onStateChanged: state=");
                stringBuilder.append(state);
                stringBuilder.append(", failureReason=");
                stringBuilder.append(failureReason);
                Log.v(WifiManager.TAG, stringBuilder.toString());
            }
            this.mHandler.post(new -$$Lambda$WifiManager$SoftApCallbackProxy$vmSW5veUpC52oRINBy419US5snk(this, state, failureReason));
        }

        public /* synthetic */ void lambda$onStateChanged$0$WifiManager$SoftApCallbackProxy(int state, int failureReason) {
            this.mCallback.onStateChanged(state, failureReason);
        }

        public void onNumClientsChanged(int numClients) {
            if (WifiManager.this.mVerboseLoggingEnabled) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("SoftApCallbackProxy: onNumClientsChanged: numClients=");
                stringBuilder.append(numClients);
                Log.v(WifiManager.TAG, stringBuilder.toString());
            }
            this.mHandler.post(new -$$Lambda$WifiManager$SoftApCallbackProxy$f44R8L0UcqgnIaD5lXMmeuRHCWI(this, numClients));
        }

        public /* synthetic */ void lambda$onNumClientsChanged$1$WifiManager$SoftApCallbackProxy(int numClients) {
            this.mCallback.onNumClientsChanged(numClients);
        }

        public void onStaConnected(String Macaddr, int numClients) throws RemoteException {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SoftApCallbackProxy: [");
            stringBuilder.append(numClients);
            stringBuilder.append("]onStaConnected Macaddr =");
            stringBuilder.append(Macaddr);
            Log.v(WifiManager.TAG, stringBuilder.toString());
            this.mHandler.post(new -$$Lambda$WifiManager$SoftApCallbackProxy$vo4E4HQhX8ezRZP1e1kxdx6MvpE(this, Macaddr, numClients));
        }

        public /* synthetic */ void lambda$onStaConnected$2$WifiManager$SoftApCallbackProxy(String Macaddr, int numClients) {
            this.mCallback.onStaConnected(Macaddr, numClients);
        }

        public void onStaDisconnected(String Macaddr, int numClients) throws RemoteException {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SoftApCallbackProxy: [");
            stringBuilder.append(numClients);
            stringBuilder.append("]onStaDisconnected Macaddr =");
            stringBuilder.append(Macaddr);
            Log.v(WifiManager.TAG, stringBuilder.toString());
            this.mHandler.post(new -$$Lambda$WifiManager$SoftApCallbackProxy$X5LJgdNUCXHctJ7m4-CGDjDEfkU(this, Macaddr, numClients));
        }

        public /* synthetic */ void lambda$onStaDisconnected$3$WifiManager$SoftApCallbackProxy(String Macaddr, int numClients) {
            this.mCallback.onStaDisconnected(Macaddr, numClients);
        }
    }

    public interface TrafficStateCallback {
        public static final int DATA_ACTIVITY_IN = 1;
        public static final int DATA_ACTIVITY_INOUT = 3;
        public static final int DATA_ACTIVITY_NONE = 0;
        public static final int DATA_ACTIVITY_OUT = 2;

        void onStateChanged(int i);
    }

    private class TrafficStateCallbackProxy extends ITrafficStateCallback.Stub {
        private final TrafficStateCallback mCallback;
        private final Handler mHandler;

        TrafficStateCallbackProxy(Looper looper, TrafficStateCallback callback) {
            this.mHandler = new Handler(looper);
            this.mCallback = callback;
        }

        public void onStateChanged(int state) {
            if (WifiManager.this.mVerboseLoggingEnabled) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("TrafficStateCallbackProxy: onStateChanged state=");
                stringBuilder.append(state);
                Log.v(WifiManager.TAG, stringBuilder.toString());
            }
            this.mHandler.post(new -$$Lambda$WifiManager$TrafficStateCallbackProxy$zQoZBZ4jRXbcyDZer28skV_T0jI(this, state));
        }

        public /* synthetic */ void lambda$onStateChanged$0$WifiManager$TrafficStateCallbackProxy(int state) {
            this.mCallback.onStateChanged(state);
        }
    }

    public interface TxPacketCountListener {
        void onFailure(int i);

        void onSuccess(int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface WifiApState {
    }

    public class WifiLock {
        private final IBinder mBinder;
        private boolean mHeld;
        int mLockType;
        private int mRefCount;
        private boolean mRefCounted;
        private String mTag;
        private WorkSource mWorkSource;

        /* synthetic */ WifiLock(WifiManager x0, int x1, String x2, AnonymousClass1 x3) {
            this(x1, x2);
        }

        private WifiLock(int lockType, String tag) {
            this.mTag = tag;
            this.mLockType = lockType;
            this.mBinder = new Binder();
            this.mRefCount = 0;
            this.mRefCounted = true;
            this.mHeld = false;
        }

        /* JADX WARNING: Missing block: B:9:0x0012, code skipped:
            if (r7.mHeld == false) goto L_0x0014;
     */
        public void acquire() {
            /*
            r7 = this;
            r0 = r7.mBinder;
            monitor-enter(r0);
            r1 = r7.mRefCounted;	 Catch:{ all -> 0x0056 }
            r2 = 1;
            if (r1 == 0) goto L_0x0010;
        L_0x0008:
            r1 = r7.mRefCount;	 Catch:{ all -> 0x0056 }
            r1 = r1 + r2;
            r7.mRefCount = r1;	 Catch:{ all -> 0x0056 }
            if (r1 != r2) goto L_0x0054;
        L_0x000f:
            goto L_0x0014;
        L_0x0010:
            r1 = r7.mHeld;	 Catch:{ all -> 0x0056 }
            if (r1 != 0) goto L_0x0054;
        L_0x0014:
            r1 = android.net.wifi.WifiManager.this;	 Catch:{ RemoteException -> 0x004e }
            r1 = r1.mService;	 Catch:{ RemoteException -> 0x004e }
            r3 = r7.mBinder;	 Catch:{ RemoteException -> 0x004e }
            r4 = r7.mLockType;	 Catch:{ RemoteException -> 0x004e }
            r5 = r7.mTag;	 Catch:{ RemoteException -> 0x004e }
            r6 = r7.mWorkSource;	 Catch:{ RemoteException -> 0x004e }
            r1.acquireWifiLock(r3, r4, r5, r6);	 Catch:{ RemoteException -> 0x004e }
            r1 = android.net.wifi.WifiManager.this;	 Catch:{ RemoteException -> 0x004e }
            monitor-enter(r1);	 Catch:{ RemoteException -> 0x004e }
            r3 = android.net.wifi.WifiManager.this;	 Catch:{ all -> 0x004b }
            r3 = r3.mActiveLockCount;	 Catch:{ all -> 0x004b }
            r4 = 50;
            if (r3 >= r4) goto L_0x003a;
        L_0x0030:
            r3 = android.net.wifi.WifiManager.this;	 Catch:{ all -> 0x004b }
            r3.mActiveLockCount = r3.mActiveLockCount + 1;	 Catch:{ all -> 0x004b }
            monitor-exit(r1);	 Catch:{ all -> 0x004b }
            r7.mHeld = r2;	 Catch:{ all -> 0x0056 }
            goto L_0x0054;
        L_0x003a:
            r2 = android.net.wifi.WifiManager.this;	 Catch:{ all -> 0x004b }
            r2 = r2.mService;	 Catch:{ all -> 0x004b }
            r3 = r7.mBinder;	 Catch:{ all -> 0x004b }
            r2.releaseWifiLock(r3);	 Catch:{ all -> 0x004b }
            r2 = new java.lang.UnsupportedOperationException;	 Catch:{ all -> 0x004b }
            r3 = "Exceeded maximum number of wifi locks";
            r2.<init>(r3);	 Catch:{ all -> 0x004b }
            throw r2;	 Catch:{ all -> 0x004b }
        L_0x004b:
            r2 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x004b }
            throw r2;	 Catch:{ RemoteException -> 0x004e }
        L_0x004e:
            r1 = move-exception;
            r2 = r1.rethrowFromSystemServer();	 Catch:{ all -> 0x0056 }
            throw r2;	 Catch:{ all -> 0x0056 }
        L_0x0054:
            monitor-exit(r0);	 Catch:{ all -> 0x0056 }
            return;
        L_0x0056:
            r1 = move-exception;
            monitor-exit(r0);	 Catch:{ all -> 0x0056 }
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.WifiManager$WifiLock.acquire():void");
        }

        /* JADX WARNING: Removed duplicated region for block: B:32:0x003a  */
        /* JADX WARNING: Removed duplicated region for block: B:30:0x0038 A:{SKIP} */
        /* JADX WARNING: Missing block: B:9:0x0012, code skipped:
            if (r4.mHeld != false) goto L_0x0014;
     */
        public void release() {
            /*
            r4 = this;
            r0 = r4.mBinder;
            monitor-enter(r0);
            r1 = r4.mRefCounted;	 Catch:{ all -> 0x0053 }
            if (r1 == 0) goto L_0x0010;
        L_0x0007:
            r1 = r4.mRefCount;	 Catch:{ all -> 0x0053 }
            r1 = r1 + -1;
            r4.mRefCount = r1;	 Catch:{ all -> 0x0053 }
            if (r1 != 0) goto L_0x0034;
        L_0x000f:
            goto L_0x0014;
        L_0x0010:
            r1 = r4.mHeld;	 Catch:{ all -> 0x0053 }
            if (r1 == 0) goto L_0x0034;
        L_0x0014:
            r1 = android.net.wifi.WifiManager.this;	 Catch:{ RemoteException -> 0x002e }
            r1 = r1.mService;	 Catch:{ RemoteException -> 0x002e }
            r2 = r4.mBinder;	 Catch:{ RemoteException -> 0x002e }
            r1.releaseWifiLock(r2);	 Catch:{ RemoteException -> 0x002e }
            r1 = android.net.wifi.WifiManager.this;	 Catch:{ RemoteException -> 0x002e }
            monitor-enter(r1);	 Catch:{ RemoteException -> 0x002e }
            r2 = android.net.wifi.WifiManager.this;	 Catch:{ all -> 0x002b }
            r2.mActiveLockCount = r2.mActiveLockCount - 1;	 Catch:{ all -> 0x002b }
            monitor-exit(r1);	 Catch:{ all -> 0x002b }
            r1 = 0;
            r4.mHeld = r1;	 Catch:{ all -> 0x0053 }
            goto L_0x0034;
        L_0x002b:
            r2 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x002b }
            throw r2;	 Catch:{ RemoteException -> 0x002e }
        L_0x002e:
            r1 = move-exception;
            r2 = r1.rethrowFromSystemServer();	 Catch:{ all -> 0x0053 }
            throw r2;	 Catch:{ all -> 0x0053 }
        L_0x0034:
            r1 = r4.mRefCount;	 Catch:{ all -> 0x0053 }
            if (r1 < 0) goto L_0x003a;
        L_0x0038:
            monitor-exit(r0);	 Catch:{ all -> 0x0053 }
            return;
        L_0x003a:
            r1 = new java.lang.RuntimeException;	 Catch:{ all -> 0x0053 }
            r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0053 }
            r2.<init>();	 Catch:{ all -> 0x0053 }
            r3 = "WifiLock under-locked ";
            r2.append(r3);	 Catch:{ all -> 0x0053 }
            r3 = r4.mTag;	 Catch:{ all -> 0x0053 }
            r2.append(r3);	 Catch:{ all -> 0x0053 }
            r2 = r2.toString();	 Catch:{ all -> 0x0053 }
            r1.<init>(r2);	 Catch:{ all -> 0x0053 }
            throw r1;	 Catch:{ all -> 0x0053 }
        L_0x0053:
            r1 = move-exception;
            monitor-exit(r0);	 Catch:{ all -> 0x0053 }
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.WifiManager$WifiLock.release():void");
        }

        public void setReferenceCounted(boolean refCounted) {
            this.mRefCounted = refCounted;
        }

        public boolean isHeld() {
            boolean z;
            synchronized (this.mBinder) {
                z = this.mHeld;
            }
            return z;
        }

        public void setWorkSource(WorkSource ws) {
            synchronized (this.mBinder) {
                if (ws != null) {
                    try {
                        if (ws.isEmpty()) {
                            ws = null;
                        }
                    } catch (RemoteException e) {
                        throw e.rethrowFromSystemServer();
                    } catch (Throwable th) {
                    }
                }
                boolean changed = true;
                if (ws == null) {
                    this.mWorkSource = null;
                } else {
                    ws.clearNames();
                    boolean z = true;
                    if (this.mWorkSource == null) {
                        if (this.mWorkSource == null) {
                            z = false;
                        }
                        changed = z;
                        this.mWorkSource = new WorkSource(ws);
                    } else {
                        changed = this.mWorkSource.equals(ws) ^ 1;
                        if (changed) {
                            this.mWorkSource.set(ws);
                        }
                    }
                }
                if (changed && this.mHeld) {
                    WifiManager.this.mService.updateWifiLockWorkSource(this.mBinder, this.mWorkSource);
                }
            }
        }

        public String toString() {
            String stringBuilder;
            synchronized (this.mBinder) {
                String s3;
                String s1 = Integer.toHexString(System.identityHashCode(this));
                String s2 = this.mHeld ? "held; " : "";
                if (this.mRefCounted) {
                    s3 = new StringBuilder();
                    s3.append("refcounted: refcount = ");
                    s3.append(this.mRefCount);
                    s3 = s3.toString();
                } else {
                    s3 = "not refcounted";
                }
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("WifiLock{ ");
                stringBuilder2.append(s1);
                stringBuilder2.append("; ");
                stringBuilder2.append(s2);
                stringBuilder2.append(s3);
                stringBuilder2.append(" }");
                stringBuilder = stringBuilder2.toString();
            }
            return stringBuilder;
        }

        /* Access modifiers changed, original: protected */
        public void finalize() throws Throwable {
            super.finalize();
            synchronized (this.mBinder) {
                if (this.mHeld) {
                    try {
                        WifiManager.this.mService.releaseWifiLock(this.mBinder);
                        synchronized (WifiManager.this) {
                            WifiManager.this.mActiveLockCount = WifiManager.this.mActiveLockCount - 1;
                        }
                    } catch (RemoteException e) {
                        throw e.rethrowFromSystemServer();
                    }
                }
            }
        }
    }

    public static abstract class WpsCallback {
        public abstract void onFailed(int i);

        public abstract void onStarted(String str);

        public abstract void onSucceeded();
    }

    public WifiManager(Context context, IWifiManager service, Looper looper) {
        Context appContext = context.getApplicationContext();
        this.mContext = appContext == null ? context : appContext;
        this.mService = service;
        this.mLooper = looper;
        this.mTargetSdkVersion = context.getApplicationInfo().targetSdkVersion;
        updateVerboseLoggingEnabledFromService();
    }

    @Deprecated
    public List<WifiConfiguration> getConfiguredNetworks() {
        try {
            ParceledListSlice<WifiConfiguration> parceledList = this.mService.getConfiguredNetworks(this.mContext.getOpPackageName());
            if (parceledList == null) {
                return Collections.emptyList();
            }
            return parceledList.getList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public List<WifiConfiguration> getPrivilegedConfiguredNetworks() {
        try {
            ParceledListSlice<WifiConfiguration> parceledList = this.mService.getPrivilegedConfiguredNetworks(this.mContext.getOpPackageName());
            if (parceledList == null) {
                return Collections.emptyList();
            }
            return parceledList.getList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public List<Pair<WifiConfiguration, Map<Integer, List<ScanResult>>>> getAllMatchingWifiConfigs(List<ScanResult> scanResults) {
        List<Pair<WifiConfiguration, Map<Integer, List<ScanResult>>>> configs = new ArrayList();
        try {
            Map<String, Map<Integer, List<ScanResult>>> results = this.mService.getAllMatchingFqdnsForScanResults(scanResults);
            if (results.isEmpty()) {
                return configs;
            }
            for (WifiConfiguration configuration : this.mService.getWifiConfigsForPasspointProfiles(new ArrayList(results.keySet()))) {
                Map<Integer, List<ScanResult>> scanResultsPerNetworkType = (Map) results.get(configuration.FQDN);
                if (scanResultsPerNetworkType != null) {
                    configs.add(Pair.create(configuration, scanResultsPerNetworkType));
                }
            }
            return configs;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public Map<OsuProvider, List<ScanResult>> getMatchingOsuProviders(List<ScanResult> scanResults) {
        if (scanResults == null) {
            return new HashMap();
        }
        try {
            return this.mService.getMatchingOsuProviders(scanResults);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public Map<OsuProvider, PasspointConfiguration> getMatchingPasspointConfigsForOsuProviders(Set<OsuProvider> osuProviders) {
        try {
            return this.mService.getMatchingPasspointConfigsForOsuProviders(new ArrayList(osuProviders));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public int addNetwork(WifiConfiguration config) {
        if (config == null) {
            return -1;
        }
        config.networkId = -1;
        return addOrUpdateNetwork(config);
    }

    @Deprecated
    public int updateNetwork(WifiConfiguration config) {
        if (config == null || config.networkId < 0) {
            return -1;
        }
        return addOrUpdateNetwork(config);
    }

    public boolean isExtendingWifi() {
        try {
            return this.mService.isExtendingWifi();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isWifiCoverageExtendFeatureEnabled() {
        try {
            return this.mService.isWifiCoverageExtendFeatureEnabled();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void enableWifiCoverageExtendFeature(boolean enable) {
        try {
            this.mService.enableWifiCoverageExtendFeature(enable);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private int addOrUpdateNetwork(WifiConfiguration config) {
        try {
            return this.mService.addOrUpdateNetwork(config, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void registerNetworkRequestMatchCallback(NetworkRequestMatchCallback callback, Handler handler) {
        if (callback != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("registerNetworkRequestMatchCallback: callback=");
            stringBuilder.append(callback);
            stringBuilder.append(", handler=");
            stringBuilder.append(handler);
            Log.v(TAG, stringBuilder.toString());
            try {
                this.mService.registerNetworkRequestMatchCallback(new Binder(), new NetworkRequestMatchCallbackProxy(handler == null ? this.mContext.getMainLooper() : handler.getLooper(), callback), callback.hashCode());
                return;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        throw new IllegalArgumentException("callback cannot be null");
    }

    public void unregisterNetworkRequestMatchCallback(NetworkRequestMatchCallback callback) {
        if (callback != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("unregisterNetworkRequestMatchCallback: callback=");
            stringBuilder.append(callback);
            Log.v(TAG, stringBuilder.toString());
            try {
                this.mService.unregisterNetworkRequestMatchCallback(callback.hashCode());
                return;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        throw new IllegalArgumentException("callback cannot be null");
    }

    public int addNetworkSuggestions(List<WifiNetworkSuggestion> networkSuggestions) {
        try {
            return this.mService.addNetworkSuggestions(networkSuggestions, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int removeNetworkSuggestions(List<WifiNetworkSuggestion> networkSuggestions) {
        try {
            return this.mService.removeNetworkSuggestions(networkSuggestions, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getMaxNumberOfNetworkSuggestionsPerApp() {
        return NETWORK_SUGGESTIONS_MAX_PER_APP;
    }

    public void addOrUpdatePasspointConfiguration(PasspointConfiguration config) {
        try {
            if (!this.mService.addOrUpdatePasspointConfiguration(config, this.mContext.getOpPackageName())) {
                throw new IllegalArgumentException();
            }
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public void removePasspointConfiguration(String fqdn) {
        try {
            if (!this.mService.removePasspointConfiguration(fqdn, this.mContext.getOpPackageName())) {
                throw new IllegalArgumentException();
            }
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public List<PasspointConfiguration> getPasspointConfigurations() {
        try {
            return this.mService.getPasspointConfigurations(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void queryPasspointIcon(long bssid, String fileName) {
        try {
            this.mService.queryPasspointIcon(bssid, fileName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int matchProviderWithCurrentNetwork(String fqdn) {
        try {
            return this.mService.matchProviderWithCurrentNetwork(fqdn);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void deauthenticateNetwork(long holdoff, boolean ess) {
        try {
            this.mService.deauthenticateNetwork(holdoff, ess);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public boolean removeNetwork(int netId) {
        try {
            return this.mService.removeNetwork(netId, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public boolean enableNetwork(int netId, boolean attemptConnect) {
        boolean pin = attemptConnect && this.mTargetSdkVersion < 21;
        if (pin) {
            NetworkPinner.pin(this.mContext, new Builder().clearCapabilities().addCapability(15).addTransportType(1).build());
        }
        try {
            boolean success = this.mService.enableNetwork(netId, attemptConnect, this.mContext.getOpPackageName());
            if (pin && !success) {
                NetworkPinner.unpin();
            }
            return success;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public boolean disableNetwork(int netId) {
        try {
            return this.mService.disableNetwork(netId, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public boolean disconnect() {
        try {
            return this.mService.disconnect(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public boolean reconnect() {
        try {
            return this.mService.reconnect(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public boolean reassociate() {
        try {
            return this.mService.reassociate(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public boolean pingSupplicant() {
        return isWifiEnabled();
    }

    private long getSupportedFeatures() {
        try {
            return this.mService.getSupportedFeatures();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private boolean isFeatureSupported(long feature) {
        return (getSupportedFeatures() & feature) == feature;
    }

    public boolean is5GHzBandSupported() {
        return isFeatureSupported(2);
    }

    public boolean isPasspointSupported() {
        return isFeatureSupported(4);
    }

    public boolean isP2pSupported() {
        return isFeatureSupported(8);
    }

    @SystemApi
    public boolean isPortableHotspotSupported() {
        return isFeatureSupported(16);
    }

    @SystemApi
    public boolean isWifiScannerSupported() {
        return isFeatureSupported(32);
    }

    public boolean isWifiAwareSupported() {
        return isFeatureSupported(64);
    }

    @SystemApi
    @Deprecated
    public boolean isDeviceToDeviceRttSupported() {
        return isFeatureSupported(128);
    }

    @Deprecated
    public boolean isDeviceToApRttSupported() {
        return isFeatureSupported(256);
    }

    public boolean isPreferredNetworkOffloadSupported() {
        return isFeatureSupported(1024);
    }

    public boolean isAdditionalStaSupported() {
        return isFeatureSupported(2048);
    }

    public boolean isTdlsSupported() {
        return isFeatureSupported(4096);
    }

    public boolean isOffChannelTdlsSupported() {
        return isFeatureSupported(8192);
    }

    public boolean isEnhancedPowerReportingSupported() {
        return isFeatureSupported(65536);
    }

    public WifiActivityEnergyInfo getControllerActivityEnergyInfo() {
        if (this.mService == null) {
            return null;
        }
        try {
            WifiActivityEnergyInfo reportActivityInfo;
            synchronized (this) {
                reportActivityInfo = this.mService.reportActivityInfo();
            }
            return reportActivityInfo;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public boolean startScan() {
        return startScan(null);
    }

    @SystemApi
    public boolean startScan(WorkSource workSource) {
        try {
            return this.mService.startScan(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String getCurrentNetworkWpsNfcConfigurationToken() {
        return null;
    }

    public WifiInfo getConnectionInfo() {
        try {
            return this.mService.getConnectionInfo(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<ScanResult> getScanResults() {
        SeempLog.record(55);
        try {
            return this.mService.getScanResults(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public boolean isScanAlwaysAvailable() {
        try {
            return this.mService.isScanAlwaysAvailable();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public boolean saveConfiguration() {
        return false;
    }

    public void setCountryCode(String country) {
        try {
            this.mService.setCountryCode(country);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public String getCountryCode() {
        try {
            return this.mService.getCountryCode();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public boolean isDualBandSupported() {
        try {
            return this.mService.isDualBandSupported();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isDualModeSupported() {
        try {
            return this.mService.needs5GHzToAnyApBandConversion();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public DhcpInfo getDhcpInfo() {
        try {
            return this.mService.getDhcpInfo();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public boolean setWifiEnabled(boolean enabled) {
        try {
            return this.mService.setWifiEnabled(this.mContext.getOpPackageName(), enabled);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getWifiState() {
        try {
            return this.mService.getWifiEnabledState();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isWifiEnabled() {
        return getWifiState() == 3;
    }

    public void getTxPacketCount(TxPacketCountListener listener) {
        getChannel().sendMessage(RSSI_PKTCNT_FETCH, 0, putListener(listener));
    }

    public static int calculateSignalLevel(int rssi, int numLevels) {
        if (rssi <= -100) {
            return 0;
        }
        if (rssi >= -55) {
            return numLevels - 1;
        }
        return (int) ((((float) (rssi + 100)) * ((float) (numLevels - 1))) / 45.0f);
    }

    public static int compareSignalLevel(int rssiA, int rssiB) {
        return rssiA - rssiB;
    }

    public void updateInterfaceIpState(String ifaceName, int mode) {
        try {
            this.mService.updateInterfaceIpState(ifaceName, mode);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean startSoftAp(WifiConfiguration wifiConfig) {
        try {
            return this.mService.startSoftAp(wifiConfig);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean stopSoftAp() {
        try {
            return this.mService.stopSoftAp();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void startLocalOnlyHotspot(LocalOnlyHotspotCallback callback, Handler handler) {
        synchronized (this.mLock) {
            LocalOnlyHotspotCallbackProxy proxy = new LocalOnlyHotspotCallbackProxy(this, handler == null ? this.mContext.getMainLooper() : handler.getLooper(), callback);
            try {
                int returnCode = this.mService.startLocalOnlyHotspot(proxy.getMessenger(), new Binder(), this.mContext.getOpPackageName());
                if (returnCode != 0) {
                    proxy.notifyFailed(returnCode);
                    return;
                }
                this.mLOHSCallbackProxy = proxy;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    @UnsupportedAppUsage
    public void cancelLocalOnlyHotspotRequest() {
        synchronized (this.mLock) {
            stopLocalOnlyHotspot();
        }
    }

    private void stopLocalOnlyHotspot() {
        synchronized (this.mLock) {
            if (this.mLOHSCallbackProxy == null) {
                return;
            }
            this.mLOHSCallbackProxy = null;
            try {
                this.mService.stopLocalOnlyHotspot();
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public void watchLocalOnlyHotspot(LocalOnlyHotspotObserver observer, Handler handler) {
        synchronized (this.mLock) {
            this.mLOHSObserverProxy = new LocalOnlyHotspotObserverProxy(this, handler == null ? this.mContext.getMainLooper() : handler.getLooper(), observer);
            try {
                this.mService.startWatchLocalOnlyHotspot(this.mLOHSObserverProxy.getMessenger(), new Binder());
                this.mLOHSObserverProxy.registered();
            } catch (RemoteException e) {
                this.mLOHSObserverProxy = null;
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public void unregisterLocalOnlyHotspotObserver() {
        synchronized (this.mLock) {
            if (this.mLOHSObserverProxy == null) {
                return;
            }
            this.mLOHSObserverProxy = null;
            try {
                this.mService.stopWatchLocalOnlyHotspot();
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    @SystemApi
    public int getWifiApState() {
        try {
            return this.mService.getWifiApEnabledState();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public boolean isWifiApEnabled() {
        return getWifiApState() == 13;
    }

    @SystemApi
    public WifiConfiguration getWifiApConfiguration() {
        try {
            return this.mService.getWifiApConfiguration();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public boolean setWifiApConfiguration(WifiConfiguration wifiConfig) {
        try {
            return this.mService.setWifiApConfiguration(wifiConfig, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void notifyUserOfApBandConversion() {
        Log.d(TAG, "apBand was converted, notify the user");
        try {
            this.mService.notifyUserOfApBandConversion(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setTdlsEnabled(InetAddress remoteIPAddress, boolean enable) {
        try {
            this.mService.enableTdls(remoteIPAddress.getHostAddress(), enable);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setTdlsEnabledWithMacAddress(String remoteMacAddress, boolean enable) {
        try {
            this.mService.enableTdlsWithMacAddress(remoteMacAddress, enable);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void registerSoftApCallback(SoftApCallback callback, Handler handler) {
        if (callback != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("registerSoftApCallback: callback=");
            stringBuilder.append(callback);
            stringBuilder.append(", handler=");
            stringBuilder.append(handler);
            Log.v(TAG, stringBuilder.toString());
            try {
                this.mService.registerSoftApCallback(new Binder(), new SoftApCallbackProxy(handler == null ? this.mContext.getMainLooper() : handler.getLooper(), callback), callback.hashCode());
                return;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        throw new IllegalArgumentException("callback cannot be null");
    }

    public void unregisterSoftApCallback(SoftApCallback callback) {
        if (callback != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("unregisterSoftApCallback: callback=");
            stringBuilder.append(callback);
            Log.v(TAG, stringBuilder.toString());
            try {
                this.mService.unregisterSoftApCallback(callback.hashCode());
                return;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        throw new IllegalArgumentException("callback cannot be null");
    }

    private int putListener(Object listener) {
        if (listener == null) {
            return 0;
        }
        int key;
        synchronized (this.mListenerMapLock) {
            do {
                key = this.mListenerKey;
                this.mListenerKey = key + 1;
            } while (key == 0);
            this.mListenerMap.put(key, listener);
        }
        return key;
    }

    private Object removeListener(int key) {
        if (key == 0) {
            return null;
        }
        Object listener;
        synchronized (this.mListenerMapLock) {
            listener = this.mListenerMap.get(key);
            this.mListenerMap.remove(key);
        }
        return listener;
    }

    private synchronized AsyncChannel getChannel() {
        if (this.mAsyncChannel == null) {
            Messenger messenger = getWifiServiceMessenger();
            if (messenger != null) {
                this.mAsyncChannel = new AsyncChannel();
                this.mConnected = new CountDownLatch(1);
                this.mAsyncChannel.connect(this.mContext, new ServiceHandler(this.mLooper), messenger);
                try {
                    this.mConnected.await();
                } catch (InterruptedException e) {
                    Log.e(TAG, "interrupted wait at init");
                }
            } else {
                throw new IllegalStateException("getWifiServiceMessenger() returned null!  This is invalid.");
            }
        }
        return this.mAsyncChannel;
    }

    @SystemApi
    public void connect(WifiConfiguration config, ActionListener listener) {
        if (config != null) {
            getChannel().sendMessage(CONNECT_NETWORK, -1, putListener(listener), config);
            return;
        }
        throw new IllegalArgumentException("config cannot be null");
    }

    @SystemApi
    public void connect(int networkId, ActionListener listener) {
        if (networkId >= 0) {
            getChannel().sendMessage(CONNECT_NETWORK, networkId, putListener(listener));
            return;
        }
        throw new IllegalArgumentException("Network id cannot be negative");
    }

    @SystemApi
    public void save(WifiConfiguration config, ActionListener listener) {
        if (config != null) {
            getChannel().sendMessage(SAVE_NETWORK, 0, putListener(listener), config);
            return;
        }
        throw new IllegalArgumentException("config cannot be null");
    }

    @SystemApi
    public void forget(int netId, ActionListener listener) {
        if (netId >= 0) {
            getChannel().sendMessage(FORGET_NETWORK, netId, putListener(listener));
            return;
        }
        throw new IllegalArgumentException("Network id cannot be negative");
    }

    @SystemApi
    public void disable(int netId, ActionListener listener) {
        if (netId >= 0) {
            getChannel().sendMessage(DISABLE_NETWORK, netId, putListener(listener));
            return;
        }
        throw new IllegalArgumentException("Network id cannot be negative");
    }

    public void disableEphemeralNetwork(String SSID) {
        if (SSID != null) {
            try {
                this.mService.disableEphemeralNetwork(SSID, this.mContext.getOpPackageName());
                return;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        throw new IllegalArgumentException("SSID cannot be null");
    }

    public void startWps(WpsInfo config, WpsCallback listener) {
        if (listener != null) {
            listener.onFailed(0);
        }
    }

    public void cancelWps(WpsCallback listener) {
        if (listener != null) {
            listener.onFailed(0);
        }
    }

    @UnsupportedAppUsage
    private Messenger getWifiServiceMessenger() {
        try {
            return this.mService.getWifiServiceMessenger(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public WifiLock createWifiLock(int lockType, String tag) {
        return new WifiLock(this, lockType, tag, null);
    }

    @Deprecated
    public WifiLock createWifiLock(String tag) {
        return new WifiLock(this, 1, tag, null);
    }

    public MulticastLock createMulticastLock(String tag) {
        return new MulticastLock(this, tag, null);
    }

    public boolean isMulticastEnabled() {
        try {
            return this.mService.isMulticastEnabled();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public boolean initializeMulticastFiltering() {
        try {
            this.mService.initializeMulticastFiltering();
            return true;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        try {
            if (this.mAsyncChannel != null) {
                this.mAsyncChannel.disconnect();
            }
            super.finalize();
        } catch (Throwable th) {
            super.finalize();
        }
    }

    @UnsupportedAppUsage
    public void enableVerboseLogging(int verbose) {
        try {
            this.mService.enableVerboseLogging(verbose);
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("enableVerboseLogging ");
            stringBuilder.append(e.toString());
            Log.e(TAG, stringBuilder.toString());
        }
    }

    @UnsupportedAppUsage
    public int getVerboseLoggingLevel() {
        try {
            return this.mService.getVerboseLoggingLevel();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void factoryReset() {
        try {
            this.mService.factoryReset(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public Network getCurrentNetwork() {
        try {
            return this.mService.getCurrentNetwork();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean setEnableAutoJoinWhenAssociated(boolean enabled) {
        return false;
    }

    public boolean getEnableAutoJoinWhenAssociated() {
        return false;
    }

    public void enableWifiConnectivityManager(boolean enabled) {
        try {
            this.mService.enableWifiConnectivityManager(enabled);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public byte[] retrieveBackupData() {
        try {
            return this.mService.retrieveBackupData();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void restoreBackupData(byte[] data) {
        try {
            this.mService.restoreBackupData(data);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public void restoreSupplicantBackupData(byte[] supplicantData, byte[] ipConfigData) {
        try {
            this.mService.restoreSupplicantBackupData(supplicantData, ipConfigData);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void startSubscriptionProvisioning(OsuProvider provider, Executor executor, ProvisioningCallback callback) {
        if (executor == null) {
            throw new IllegalArgumentException("executor must not be null");
        } else if (callback != null) {
            try {
                this.mService.startSubscriptionProvisioning(provider, new ProvisioningCallbackProxy(executor, callback));
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("callback must not be null");
        }
    }

    public void registerTrafficStateCallback(TrafficStateCallback callback, Handler handler) {
        if (callback != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("registerTrafficStateCallback: callback=");
            stringBuilder.append(callback);
            stringBuilder.append(", handler=");
            stringBuilder.append(handler);
            Log.v(TAG, stringBuilder.toString());
            try {
                this.mService.registerTrafficStateCallback(new Binder(), new TrafficStateCallbackProxy(handler == null ? this.mContext.getMainLooper() : handler.getLooper(), callback), callback.hashCode());
                return;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        throw new IllegalArgumentException("callback cannot be null");
    }

    public void unregisterTrafficStateCallback(TrafficStateCallback callback) {
        if (callback != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("unregisterTrafficStateCallback: callback=");
            stringBuilder.append(callback);
            Log.v(TAG, stringBuilder.toString());
            try {
                this.mService.unregisterTrafficStateCallback(callback.hashCode());
                return;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        throw new IllegalArgumentException("callback cannot be null");
    }

    private void updateVerboseLoggingEnabledFromService() {
        this.mVerboseLoggingEnabled = getVerboseLoggingLevel() > 0;
    }

    public String getCapabilities(String capaType) {
        try {
            return this.mService.getCapabilities(capaType);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int dppAddBootstrapQrCode(String uri) {
        try {
            return this.mService.dppAddBootstrapQrCode(uri);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int dppBootstrapGenerate(WifiDppConfig config) {
        try {
            return this.mService.dppBootstrapGenerate(config);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String dppGetUri(int bootstrap_id) {
        try {
            return this.mService.dppGetUri(bootstrap_id);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int dppBootstrapRemove(int bootstrap_id) {
        try {
            return this.mService.dppBootstrapRemove(bootstrap_id);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int dppListen(String frequency, int dpp_role, boolean qr_mutual, boolean netrole_ap) {
        try {
            return this.mService.dppListen(frequency, dpp_role, qr_mutual, netrole_ap);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void dppStopListen() {
        try {
            this.mService.dppStopListen();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int dppConfiguratorAdd(String curve, String key, int expiry) {
        try {
            return this.mService.dppConfiguratorAdd(curve, key, expiry);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int dppConfiguratorRemove(int config_id) {
        try {
            return this.mService.dppConfiguratorRemove(config_id);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int dppStartAuth(WifiDppConfig config) {
        try {
            return this.mService.dppStartAuth(config);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String dppConfiguratorGetKey(int id) {
        try {
            return this.mService.dppConfiguratorGetKey(id);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isWpa3SaeSupported() {
        return isFeatureSupported(134217728);
    }

    public boolean isWpa3SuiteBSupported() {
        return isFeatureSupported(268435456);
    }

    public boolean isEnhancedOpenSupported() {
        return isFeatureSupported(536870912);
    }

    public boolean isWapiPskSupported() {
        return isFeatureSupported(8589934592L);
    }

    public boolean isWapiCertSupported() {
        return isFeatureSupported(17179869184L);
    }

    public boolean isEasyConnectSupported() {
        return isFeatureSupported(-2147483648L);
    }

    public String[] getFactoryMacAddresses() {
        try {
            return this.mService.getFactoryMacAddresses();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void setDeviceMobilityState(int state) {
        try {
            this.mService.setDeviceMobilityState(state);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void startEasyConnectAsConfiguratorInitiator(String enrolleeUri, int selectedNetworkId, int enrolleeNetworkRole, Executor executor, EasyConnectStatusCallback callback) {
        try {
            this.mService.startDppAsConfiguratorInitiator(new Binder(), enrolleeUri, selectedNetworkId, enrolleeNetworkRole, new EasyConnectCallbackProxy(executor, callback));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void startEasyConnectAsEnrolleeInitiator(String configuratorUri, Executor executor, EasyConnectStatusCallback callback) {
        try {
            this.mService.startDppAsEnrolleeInitiator(new Binder(), configuratorUri, new EasyConnectCallbackProxy(executor, callback));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void stopEasyConnectSession() {
        try {
            this.mService.stopDppSession();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void addOnWifiUsabilityStatsListener(final Executor executor, final OnWifiUsabilityStatsListener listener) {
        if (executor == null) {
            throw new IllegalArgumentException("executor cannot be null");
        } else if (listener != null) {
            if (this.mVerboseLoggingEnabled) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("addOnWifiUsabilityStatsListener: listener=");
                stringBuilder.append(listener);
                Log.v(TAG, stringBuilder.toString());
            }
            try {
                this.mService.addOnWifiUsabilityStatsListener(new Binder(), new IOnWifiUsabilityStatsListener.Stub() {
                    public void onWifiUsabilityStats(int seqNum, boolean isSameBssidAndFreq, WifiUsabilityStatsEntry stats) {
                        if (WifiManager.this.mVerboseLoggingEnabled) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("OnWifiUsabilityStatsListener: onWifiUsabilityStats: seqNum=");
                            stringBuilder.append(seqNum);
                            Log.v(WifiManager.TAG, stringBuilder.toString());
                        }
                        Binder.withCleanCallingIdentity(new -$$Lambda$WifiManager$1$HHq94tH9ygKDknRiBOn9DYskiOc(executor, listener, seqNum, isSameBssidAndFreq, stats));
                    }
                }, listener.hashCode());
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("listener cannot be null");
        }
    }

    @SystemApi
    public void removeOnWifiUsabilityStatsListener(OnWifiUsabilityStatsListener listener) {
        if (listener != null) {
            if (this.mVerboseLoggingEnabled) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("removeOnWifiUsabilityStatsListener: listener=");
                stringBuilder.append(listener);
                Log.v(TAG, stringBuilder.toString());
            }
            try {
                this.mService.removeOnWifiUsabilityStatsListener(listener.hashCode());
                return;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        throw new IllegalArgumentException("listener cannot be null");
    }

    @SystemApi
    public void updateWifiUsabilityScore(int seqNum, int score, int predictionHorizonSec) {
        try {
            this.mService.updateWifiUsabilityScore(seqNum, score, predictionHorizonSec);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isMiWifi() {
        WifiInfo wifiInfo = getConnectionInfo();
        if (wifiInfo != null) {
            if ("XIAOMI_ROUTER".equalsIgnoreCase(wifiInfo.getVendorInfo())) {
                return true;
            }
        }
        return false;
    }
}
