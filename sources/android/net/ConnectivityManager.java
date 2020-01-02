package android.net;

import android.Manifest.permission;
import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.app.PendingIntent;
import android.content.Context;
import android.net.ISocketKeepaliveCallback.Stub;
import android.net.IpSecManager.UdpEncapsulationSocket;
import android.net.NetworkRequest.Builder;
import android.net.SocketKeepalive.Callback;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.INetworkActivityListener;
import android.os.INetworkManagementService;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ServiceManager;
import android.os.ServiceSpecificException;
import android.provider.Settings;
import android.security.keystore.KeyProperties;
import android.telephony.SubscriptionManager;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseIntArray;
import com.android.internal.R;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.telephony.ITelephony;
import com.android.internal.util.Preconditions;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import libcore.net.event.NetworkEventDispatcher;

public class ConnectivityManager {
    @Deprecated
    public static final String ACTION_BACKGROUND_DATA_SETTING_CHANGED = "android.net.conn.BACKGROUND_DATA_SETTING_CHANGED";
    public static final String ACTION_CAPTIVE_PORTAL_SIGN_IN = "android.net.conn.CAPTIVE_PORTAL";
    public static final String ACTION_CAPTIVE_PORTAL_TEST_COMPLETED = "android.net.conn.CAPTIVE_PORTAL_TEST_COMPLETED";
    public static final String ACTION_DATA_ACTIVITY_CHANGE = "android.net.conn.DATA_ACTIVITY_CHANGE";
    public static final String ACTION_PROMPT_LOST_VALIDATION = "android.net.conn.PROMPT_LOST_VALIDATION";
    public static final String ACTION_PROMPT_PARTIAL_CONNECTIVITY = "android.net.conn.PROMPT_PARTIAL_CONNECTIVITY";
    public static final String ACTION_PROMPT_UNVALIDATED = "android.net.conn.PROMPT_UNVALIDATED";
    public static final String ACTION_RESTRICT_BACKGROUND_CHANGED = "android.net.conn.RESTRICT_BACKGROUND_CHANGED";
    @UnsupportedAppUsage
    public static final String ACTION_TETHER_STATE_CHANGED = "android.net.conn.TETHER_STATE_CHANGED";
    private static final NetworkRequest ALREADY_UNREGISTERED = new Builder().clearCapabilities().build();
    private static final int BASE = 524288;
    public static final int CALLBACK_AVAILABLE = 524290;
    public static final int CALLBACK_BLK_CHANGED = 524299;
    public static final int CALLBACK_CAP_CHANGED = 524294;
    public static final int CALLBACK_IP_CHANGED = 524295;
    public static final int CALLBACK_LOSING = 524291;
    public static final int CALLBACK_LOST = 524292;
    public static final int CALLBACK_PRECHECK = 524289;
    public static final int CALLBACK_RESUMED = 524298;
    public static final int CALLBACK_SUSPENDED = 524297;
    public static final int CALLBACK_UNAVAIL = 524293;
    @Deprecated
    public static final String CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    public static final String CONNECTIVITY_ACTION_SUPL = "android.net.conn.CONNECTIVITY_CHANGE_SUPL";
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    @Deprecated
    public static final int DEFAULT_NETWORK_PREFERENCE = 1;
    private static final int EXPIRE_LEGACY_REQUEST = 524296;
    public static final String EXTRA_ACTIVE_LOCAL_ONLY = "localOnlyArray";
    @UnsupportedAppUsage
    public static final String EXTRA_ACTIVE_TETHER = "tetherArray";
    public static final String EXTRA_ADD_TETHER_TYPE = "extraAddTetherType";
    @UnsupportedAppUsage
    public static final String EXTRA_AVAILABLE_TETHER = "availableArray";
    public static final String EXTRA_CAPTIVE_PORTAL = "android.net.extra.CAPTIVE_PORTAL";
    @SystemApi
    public static final String EXTRA_CAPTIVE_PORTAL_PROBE_SPEC = "android.net.extra.CAPTIVE_PORTAL_PROBE_SPEC";
    public static final String EXTRA_CAPTIVE_PORTAL_URL = "android.net.extra.CAPTIVE_PORTAL_URL";
    @SystemApi
    public static final String EXTRA_CAPTIVE_PORTAL_USER_AGENT = "android.net.extra.CAPTIVE_PORTAL_USER_AGENT";
    public static final String EXTRA_DEVICE_TYPE = "deviceType";
    @UnsupportedAppUsage
    public static final String EXTRA_ERRORED_TETHER = "erroredArray";
    @Deprecated
    public static final String EXTRA_EXTRA_INFO = "extraInfo";
    public static final String EXTRA_INET_CONDITION = "inetCondition";
    public static final String EXTRA_IS_ACTIVE = "isActive";
    public static final String EXTRA_IS_CAPTIVE_PORTAL = "captivePortal";
    @Deprecated
    public static final String EXTRA_IS_FAILOVER = "isFailover";
    public static final String EXTRA_NETWORK = "android.net.extra.NETWORK";
    @Deprecated
    public static final String EXTRA_NETWORK_INFO = "networkInfo";
    public static final String EXTRA_NETWORK_REQUEST = "android.net.extra.NETWORK_REQUEST";
    @Deprecated
    public static final String EXTRA_NETWORK_TYPE = "networkType";
    public static final String EXTRA_NO_CONNECTIVITY = "noConnectivity";
    @Deprecated
    public static final String EXTRA_OTHER_NETWORK_INFO = "otherNetwork";
    public static final String EXTRA_PROVISION_CALLBACK = "extraProvisionCallback";
    public static final String EXTRA_REALTIME_NS = "tsNanos";
    public static final String EXTRA_REASON = "reason";
    public static final String EXTRA_REM_TETHER_TYPE = "extraRemTetherType";
    public static final String EXTRA_RUN_PROVISION = "extraRunProvision";
    public static final String EXTRA_SET_ALARM = "extraSetAlarm";
    @UnsupportedAppUsage
    public static final String INET_CONDITION_ACTION = "android.net.conn.INET_CONDITION_ACTION";
    private static final int LISTEN = 1;
    public static final int MAX_NETWORK_TYPE = 18;
    public static final int MAX_RADIO_TYPE = 18;
    private static final int MIN_NETWORK_TYPE = 0;
    public static final int MULTIPATH_PREFERENCE_HANDOVER = 1;
    public static final int MULTIPATH_PREFERENCE_PERFORMANCE = 4;
    public static final int MULTIPATH_PREFERENCE_RELIABILITY = 2;
    public static final int MULTIPATH_PREFERENCE_UNMETERED = 7;
    public static final int NETID_UNSET = 0;
    public static final String PRIVATE_DNS_DEFAULT_MODE_FALLBACK = "off";
    public static final String PRIVATE_DNS_MODE_OFF = "off";
    public static final String PRIVATE_DNS_MODE_OPPORTUNISTIC = "opportunistic";
    public static final String PRIVATE_DNS_MODE_PROVIDER_HOSTNAME = "hostname";
    private static final int REQUEST = 2;
    public static final int REQUEST_ID_UNSET = 0;
    public static final int RESTRICT_BACKGROUND_STATUS_DISABLED = 1;
    public static final int RESTRICT_BACKGROUND_STATUS_ENABLED = 3;
    public static final int RESTRICT_BACKGROUND_STATUS_WHITELISTED = 2;
    private static final String TAG = "ConnectivityManager";
    @SystemApi
    public static final int TETHERING_BLUETOOTH = 2;
    public static final int TETHERING_INVALID = -1;
    @SystemApi
    public static final int TETHERING_USB = 1;
    @SystemApi
    public static final int TETHERING_WIFI = 0;
    public static final int TETHERING_WIGIG = 3;
    public static final int TETHER_ERROR_DHCPSERVER_ERROR = 12;
    public static final int TETHER_ERROR_DISABLE_NAT_ERROR = 9;
    public static final int TETHER_ERROR_ENABLE_NAT_ERROR = 8;
    @SystemApi
    public static final int TETHER_ERROR_ENTITLEMENT_UNKONWN = 13;
    public static final int TETHER_ERROR_IFACE_CFG_ERROR = 10;
    public static final int TETHER_ERROR_MASTER_ERROR = 5;
    @SystemApi
    public static final int TETHER_ERROR_NO_ERROR = 0;
    @SystemApi
    public static final int TETHER_ERROR_PROVISION_FAILED = 11;
    public static final int TETHER_ERROR_SERVICE_UNAVAIL = 2;
    public static final int TETHER_ERROR_TETHER_IFACE_ERROR = 6;
    public static final int TETHER_ERROR_UNAVAIL_IFACE = 4;
    public static final int TETHER_ERROR_UNKNOWN_IFACE = 1;
    public static final int TETHER_ERROR_UNSUPPORTED = 3;
    public static final int TETHER_ERROR_UNTETHER_IFACE_ERROR = 7;
    @Deprecated
    public static final int TYPE_BLUETOOTH = 7;
    @Deprecated
    public static final int TYPE_DUMMY = 8;
    @Deprecated
    public static final int TYPE_ETHERNET = 9;
    @Deprecated
    public static final int TYPE_MOBILE = 0;
    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 130143562)
    public static final int TYPE_MOBILE_CBS = 12;
    @Deprecated
    public static final int TYPE_MOBILE_DUN = 4;
    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 130143562)
    public static final int TYPE_MOBILE_EMERGENCY = 15;
    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 130143562)
    public static final int TYPE_MOBILE_FOTA = 10;
    @Deprecated
    public static final int TYPE_MOBILE_HIPRI = 5;
    @Deprecated
    @UnsupportedAppUsage
    public static final int TYPE_MOBILE_IA = 14;
    @Deprecated
    @UnsupportedAppUsage
    public static final int TYPE_MOBILE_IMS = 11;
    @Deprecated
    public static final int TYPE_MOBILE_MMS = 2;
    @Deprecated
    public static final int TYPE_MOBILE_SUPL = 3;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 130143562)
    public static final int TYPE_NONE = -1;
    @Deprecated
    @UnsupportedAppUsage
    public static final int TYPE_PROXY = 16;
    @Deprecated
    public static final int TYPE_TEST = 18;
    @Deprecated
    public static final int TYPE_VPN = 17;
    @Deprecated
    public static final int TYPE_WIFI = 1;
    @Deprecated
    @UnsupportedAppUsage
    public static final int TYPE_WIFI_P2P = 13;
    @Deprecated
    public static final int TYPE_WIMAX = 6;
    private static CallbackHandler sCallbackHandler;
    private static final HashMap<NetworkRequest, NetworkCallback> sCallbacks = new HashMap();
    private static ConnectivityManager sInstance;
    @UnsupportedAppUsage
    private static final HashMap<NetworkCapabilities, LegacyRequest> sLegacyRequests = new HashMap();
    private static final SparseIntArray sLegacyTypeToCapability = new SparseIntArray();
    private static final SparseIntArray sLegacyTypeToTransport = new SparseIntArray();
    private final Context mContext;
    private INetworkManagementService mNMService;
    private INetworkPolicyManager mNPManager;
    private final ArrayMap<OnNetworkActiveListener, INetworkActivityListener> mNetworkActivityListeners = new ArrayMap();
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 130143562)
    private final IConnectivityManager mService;
    @GuardedBy({"mTetheringEventCallbacks"})
    private final ArrayMap<OnTetheringEventCallback, ITetheringEventCallback> mTetheringEventCallbacks = new ArrayMap();

    private class CallbackHandler extends Handler {
        private static final boolean DBG = false;
        private static final String TAG = "ConnectivityManager.CallbackHandler";

        CallbackHandler(Looper looper) {
            super(looper);
        }

        CallbackHandler(ConnectivityManager connectivityManager, Handler handler) {
            this(((Handler) Preconditions.checkNotNull(handler, "Handler cannot be null.")).getLooper());
        }

        /* JADX WARNING: Missing block: B:16:0x006d, code skipped:
            r4 = true;
     */
        /* JADX WARNING: Missing block: B:17:0x0071, code skipped:
            switch(r9.what) {
                case android.net.ConnectivityManager.CALLBACK_PRECHECK :int: goto L_0x00c8;
                case 524290: goto L_0x00ae;
                case 524291: goto L_0x00a8;
                case 524292: goto L_0x00a4;
                case android.net.ConnectivityManager.CALLBACK_UNAVAIL :int: goto L_0x00a0;
                case android.net.ConnectivityManager.CALLBACK_CAP_CHANGED :int: goto L_0x0094;
                case android.net.ConnectivityManager.CALLBACK_IP_CHANGED :int: goto L_0x0088;
                case android.net.ConnectivityManager.EXPIRE_LEGACY_REQUEST :int: goto L_0x0074;
                case android.net.ConnectivityManager.CALLBACK_SUSPENDED :int: goto L_0x0084;
                case android.net.ConnectivityManager.CALLBACK_RESUMED :int: goto L_0x0080;
                case android.net.ConnectivityManager.CALLBACK_BLK_CHANGED :int: goto L_0x0075;
                default: goto L_0x0074;
            };
     */
        /* JADX WARNING: Missing block: B:19:0x0077, code skipped:
            if (r9.arg1 == 0) goto L_0x007a;
     */
        /* JADX WARNING: Missing block: B:20:0x007a, code skipped:
            r4 = false;
     */
        /* JADX WARNING: Missing block: B:21:0x007b, code skipped:
            r3.onBlockedStatusChanged(r1, r4);
     */
        /* JADX WARNING: Missing block: B:22:0x0080, code skipped:
            r3.onNetworkResumed(r1);
     */
        /* JADX WARNING: Missing block: B:23:0x0084, code skipped:
            r3.onNetworkSuspended(r1);
     */
        /* JADX WARNING: Missing block: B:24:0x0088, code skipped:
            r3.onLinkPropertiesChanged(r1, (android.net.LinkProperties) getObject(r9, android.net.LinkProperties.class));
     */
        /* JADX WARNING: Missing block: B:25:0x0094, code skipped:
            r3.onCapabilitiesChanged(r1, (android.net.NetworkCapabilities) getObject(r9, android.net.NetworkCapabilities.class));
     */
        /* JADX WARNING: Missing block: B:26:0x00a0, code skipped:
            r3.onUnavailable();
     */
        /* JADX WARNING: Missing block: B:27:0x00a4, code skipped:
            r3.onLost(r1);
     */
        /* JADX WARNING: Missing block: B:28:0x00a8, code skipped:
            r3.onLosing(r1, r9.arg1);
     */
        /* JADX WARNING: Missing block: B:29:0x00ae, code skipped:
            r2 = (android.net.NetworkCapabilities) getObject(r9, android.net.NetworkCapabilities.class);
            r6 = (android.net.LinkProperties) getObject(r9, android.net.LinkProperties.class);
     */
        /* JADX WARNING: Missing block: B:30:0x00c0, code skipped:
            if (r9.arg1 == 0) goto L_0x00c3;
     */
        /* JADX WARNING: Missing block: B:31:0x00c3, code skipped:
            r4 = false;
     */
        /* JADX WARNING: Missing block: B:32:0x00c4, code skipped:
            r3.onAvailable(r1, r2, r6, r4);
     */
        /* JADX WARNING: Missing block: B:33:0x00c8, code skipped:
            r3.onPreCheck(r1);
     */
        /* JADX WARNING: Missing block: B:34:0x00cc, code skipped:
            return;
     */
        public void handleMessage(android.os.Message r9) {
            /*
            r8 = this;
            r0 = r9.what;
            r1 = 524296; // 0x80008 float:7.34695E-40 double:2.590366E-318;
            if (r0 != r1) goto L_0x0013;
        L_0x0007:
            r0 = android.net.ConnectivityManager.this;
            r1 = r9.obj;
            r1 = (android.net.NetworkCapabilities) r1;
            r2 = r9.arg1;
            r0.expireRequest(r1, r2);
            return;
        L_0x0013:
            r0 = android.net.NetworkRequest.class;
            r0 = r8.getObject(r9, r0);
            r0 = (android.net.NetworkRequest) r0;
            r1 = android.net.Network.class;
            r1 = r8.getObject(r9, r1);
            r1 = (android.net.Network) r1;
            r2 = android.net.ConnectivityManager.sCallbacks;
            monitor-enter(r2);
            r3 = android.net.ConnectivityManager.sCallbacks;	 Catch:{ all -> 0x00cd }
            r3 = r3.get(r0);	 Catch:{ all -> 0x00cd }
            r3 = (android.net.ConnectivityManager.NetworkCallback) r3;	 Catch:{ all -> 0x00cd }
            if (r3 != 0) goto L_0x0057;
        L_0x0034:
            r4 = "ConnectivityManager.CallbackHandler";
            r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00cd }
            r5.<init>();	 Catch:{ all -> 0x00cd }
            r6 = "callback not found for ";
            r5.append(r6);	 Catch:{ all -> 0x00cd }
            r6 = r9.what;	 Catch:{ all -> 0x00cd }
            r6 = android.net.ConnectivityManager.getCallbackName(r6);	 Catch:{ all -> 0x00cd }
            r5.append(r6);	 Catch:{ all -> 0x00cd }
            r6 = " message";
            r5.append(r6);	 Catch:{ all -> 0x00cd }
            r5 = r5.toString();	 Catch:{ all -> 0x00cd }
            android.util.Log.w(r4, r5);	 Catch:{ all -> 0x00cd }
            monitor-exit(r2);	 Catch:{ all -> 0x00cd }
            return;
        L_0x0057:
            r4 = r9.what;	 Catch:{ all -> 0x00cd }
            r5 = 524293; // 0x80005 float:7.34691E-40 double:2.59035E-318;
            if (r4 != r5) goto L_0x006c;
        L_0x005e:
            r4 = android.net.ConnectivityManager.sCallbacks;	 Catch:{ all -> 0x00cd }
            r4.remove(r0);	 Catch:{ all -> 0x00cd }
            r4 = android.net.ConnectivityManager.ALREADY_UNREGISTERED;	 Catch:{ all -> 0x00cd }
            r3.networkRequest = r4;	 Catch:{ all -> 0x00cd }
        L_0x006c:
            monitor-exit(r2);	 Catch:{ all -> 0x00cd }
            r2 = r9.what;
            r4 = 1;
            r5 = 0;
            switch(r2) {
                case 524289: goto L_0x00c8;
                case 524290: goto L_0x00ae;
                case 524291: goto L_0x00a8;
                case 524292: goto L_0x00a4;
                case 524293: goto L_0x00a0;
                case 524294: goto L_0x0094;
                case 524295: goto L_0x0088;
                case 524296: goto L_0x0074;
                case 524297: goto L_0x0084;
                case 524298: goto L_0x0080;
                case 524299: goto L_0x0075;
                default: goto L_0x0074;
            };
        L_0x0074:
            goto L_0x00cc;
        L_0x0075:
            r2 = r9.arg1;
            if (r2 == 0) goto L_0x007a;
        L_0x0079:
            goto L_0x007b;
        L_0x007a:
            r4 = r5;
        L_0x007b:
            r2 = r4;
            r3.onBlockedStatusChanged(r1, r2);
            goto L_0x00cc;
        L_0x0080:
            r3.onNetworkResumed(r1);
            goto L_0x00cc;
        L_0x0084:
            r3.onNetworkSuspended(r1);
            goto L_0x00cc;
        L_0x0088:
            r2 = android.net.LinkProperties.class;
            r2 = r8.getObject(r9, r2);
            r2 = (android.net.LinkProperties) r2;
            r3.onLinkPropertiesChanged(r1, r2);
            goto L_0x00cc;
        L_0x0094:
            r2 = android.net.NetworkCapabilities.class;
            r2 = r8.getObject(r9, r2);
            r2 = (android.net.NetworkCapabilities) r2;
            r3.onCapabilitiesChanged(r1, r2);
            goto L_0x00cc;
        L_0x00a0:
            r3.onUnavailable();
            goto L_0x00cc;
        L_0x00a4:
            r3.onLost(r1);
            goto L_0x00cc;
        L_0x00a8:
            r2 = r9.arg1;
            r3.onLosing(r1, r2);
            goto L_0x00cc;
        L_0x00ae:
            r2 = android.net.NetworkCapabilities.class;
            r2 = r8.getObject(r9, r2);
            r2 = (android.net.NetworkCapabilities) r2;
            r6 = android.net.LinkProperties.class;
            r6 = r8.getObject(r9, r6);
            r6 = (android.net.LinkProperties) r6;
            r7 = r9.arg1;
            if (r7 == 0) goto L_0x00c3;
        L_0x00c2:
            goto L_0x00c4;
        L_0x00c3:
            r4 = r5;
        L_0x00c4:
            r3.onAvailable(r1, r2, r6, r4);
            goto L_0x00cc;
        L_0x00c8:
            r3.onPreCheck(r1);
        L_0x00cc:
            return;
        L_0x00cd:
            r3 = move-exception;
            monitor-exit(r2);	 Catch:{ all -> 0x00cd }
            throw r3;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.net.ConnectivityManager$CallbackHandler.handleMessage(android.os.Message):void");
        }

        private <T> T getObject(Message msg, Class<T> c) {
            return msg.getData().getParcelable(c.getSimpleName());
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface EntitlementResultCode {
    }

    public interface Errors {
        public static final int TOO_MANY_REQUESTS = 1;
    }

    public static class NetworkCallback {
        private NetworkRequest networkRequest;

        public void onPreCheck(Network network) {
        }

        public void onAvailable(Network network, NetworkCapabilities networkCapabilities, LinkProperties linkProperties, boolean blocked) {
            onAvailable(network);
            if (!networkCapabilities.hasCapability(21)) {
                onNetworkSuspended(network);
            }
            onCapabilitiesChanged(network, networkCapabilities);
            onLinkPropertiesChanged(network, linkProperties);
            onBlockedStatusChanged(network, blocked);
        }

        public void onAvailable(Network network) {
        }

        public void onLosing(Network network, int maxMsToLive) {
        }

        public void onLost(Network network) {
        }

        public void onUnavailable() {
        }

        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        }

        public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
        }

        public void onNetworkSuspended(Network network) {
        }

        public void onNetworkResumed(Network network) {
        }

        public void onBlockedStatusChanged(Network network, boolean blocked) {
        }
    }

    private static class LegacyRequest {
        Network currentNetwork;
        int delay;
        int expireSequenceNumber;
        NetworkCallback networkCallback;
        NetworkCapabilities networkCapabilities;
        NetworkRequest networkRequest;

        private LegacyRequest() {
            this.delay = -1;
            this.networkCallback = new NetworkCallback() {
                public void onAvailable(Network network) {
                    LegacyRequest.this.currentNetwork = network;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("startUsingNetworkFeature got Network:");
                    stringBuilder.append(network);
                    Log.d(ConnectivityManager.TAG, stringBuilder.toString());
                    ConnectivityManager.setProcessDefaultNetworkForHostResolution(network);
                }

                public void onLost(Network network) {
                    if (network.equals(LegacyRequest.this.currentNetwork)) {
                        LegacyRequest.this.clearDnsBinding();
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("startUsingNetworkFeature lost Network:");
                    stringBuilder.append(network);
                    Log.d(ConnectivityManager.TAG, stringBuilder.toString());
                }
            };
        }

        /* synthetic */ LegacyRequest(AnonymousClass1 x0) {
            this();
        }

        private void clearDnsBinding() {
            if (this.currentNetwork != null) {
                this.currentNetwork = null;
                ConnectivityManager.setProcessDefaultNetworkForHostResolution(null);
            }
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface MultipathPreference {
    }

    public interface OnNetworkActiveListener {
        void onNetworkActive();
    }

    @SystemApi
    public static abstract class OnStartTetheringCallback {
        public void onTetheringStarted() {
        }

        public void onTetheringFailed() {
        }
    }

    @SystemApi
    public interface OnTetheringEntitlementResultListener {
        void onTetheringEntitlementResult(int i);
    }

    @SystemApi
    public static abstract class OnTetheringEventCallback {
        public void onUpstreamChanged(Network network) {
        }
    }

    public class PacketKeepalive {
        public static final int BINDER_DIED = -10;
        public static final int ERROR_HARDWARE_ERROR = -31;
        public static final int ERROR_HARDWARE_UNSUPPORTED = -30;
        public static final int ERROR_INVALID_INTERVAL = -24;
        public static final int ERROR_INVALID_IP_ADDRESS = -21;
        public static final int ERROR_INVALID_LENGTH = -23;
        public static final int ERROR_INVALID_NETWORK = -20;
        public static final int ERROR_INVALID_PORT = -22;
        public static final int MIN_INTERVAL = 10;
        public static final int NATT_PORT = 4500;
        public static final int NO_KEEPALIVE = -1;
        public static final int SUCCESS = 0;
        private static final String TAG = "PacketKeepalive";
        private final ISocketKeepaliveCallback mCallback;
        private final ExecutorService mExecutor;
        private final Network mNetwork;
        private volatile Integer mSlot;

        /* synthetic */ PacketKeepalive(ConnectivityManager x0, Network x1, PacketKeepaliveCallback x2, AnonymousClass1 x3) {
            this(x1, x2);
        }

        @UnsupportedAppUsage
        public void stop() {
            try {
                this.mExecutor.execute(new -$$Lambda$ConnectivityManager$PacketKeepalive$--8nwufwzyblnuYRFEYIKx7L4Vg(this));
            } catch (RejectedExecutionException e) {
            }
        }

        public /* synthetic */ void lambda$stop$0$ConnectivityManager$PacketKeepalive() {
            try {
                if (this.mSlot != null) {
                    ConnectivityManager.this.mService.stopKeepalive(this.mNetwork, this.mSlot.intValue());
                }
            } catch (RemoteException e) {
                Log.e(TAG, "Error stopping packet keepalive: ", e);
                throw e.rethrowFromSystemServer();
            }
        }

        private PacketKeepalive(Network network, final PacketKeepaliveCallback callback) {
            Preconditions.checkNotNull(network, "network cannot be null");
            Preconditions.checkNotNull(callback, "callback cannot be null");
            this.mNetwork = network;
            this.mExecutor = Executors.newSingleThreadExecutor();
            this.mCallback = new Stub(ConnectivityManager.this) {
                public void onStarted(int slot) {
                    Binder.withCleanCallingIdentity(new -$$Lambda$ConnectivityManager$PacketKeepalive$1$iOtsqOYp69ztB6u3PYNu-iI_PGo(this, slot, callback));
                }

                public /* synthetic */ void lambda$onStarted$1$ConnectivityManager$PacketKeepalive$1(int slot, PacketKeepaliveCallback callback) throws Exception {
                    PacketKeepalive.this.mExecutor.execute(new -$$Lambda$ConnectivityManager$PacketKeepalive$1$NfMgP6Nh6Ep6LcaiJ10o_zBccII(this, slot, callback));
                }

                public /* synthetic */ void lambda$onStarted$0$ConnectivityManager$PacketKeepalive$1(int slot, PacketKeepaliveCallback callback) {
                    PacketKeepalive.this.mSlot = Integer.valueOf(slot);
                    callback.onStarted();
                }

                public void onStopped() {
                    Binder.withCleanCallingIdentity(new -$$Lambda$ConnectivityManager$PacketKeepalive$1$-H5tzn67t3ydWL8tXpl9UyOmDcc(this, callback));
                    PacketKeepalive.this.mExecutor.shutdown();
                }

                public /* synthetic */ void lambda$onStopped$3$ConnectivityManager$PacketKeepalive$1(PacketKeepaliveCallback callback) throws Exception {
                    PacketKeepalive.this.mExecutor.execute(new -$$Lambda$ConnectivityManager$PacketKeepalive$1$WmmtbYWlzqL-V8wWUDKe3CWjvy0(this, callback));
                }

                public /* synthetic */ void lambda$onStopped$2$ConnectivityManager$PacketKeepalive$1(PacketKeepaliveCallback callback) {
                    PacketKeepalive.this.mSlot = null;
                    callback.onStopped();
                }

                public void onError(int error) {
                    Binder.withCleanCallingIdentity(new -$$Lambda$ConnectivityManager$PacketKeepalive$1$nt5Pgsn85fhX6h9EJ0eAK_PXAjU(this, callback, error));
                    PacketKeepalive.this.mExecutor.shutdown();
                }

                public /* synthetic */ void lambda$onError$5$ConnectivityManager$PacketKeepalive$1(PacketKeepaliveCallback callback, int error) throws Exception {
                    PacketKeepalive.this.mExecutor.execute(new -$$Lambda$ConnectivityManager$PacketKeepalive$1$JWcQQZv8Qrs81cZ-BMAOZZ8MUeU(this, callback, error));
                }

                public /* synthetic */ void lambda$onError$4$ConnectivityManager$PacketKeepalive$1(PacketKeepaliveCallback callback, int error) {
                    PacketKeepalive.this.mSlot = null;
                    callback.onError(error);
                }

                public void onDataReceived() {
                }
            };
        }
    }

    public static class PacketKeepaliveCallback {
        @UnsupportedAppUsage
        public void onStarted() {
        }

        @UnsupportedAppUsage
        public void onStopped() {
        }

        @UnsupportedAppUsage
        public void onError(int error) {
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface RestrictBackgroundStatus {
    }

    public static class TooManyRequestsException extends RuntimeException {
    }

    static {
        sLegacyTypeToTransport.put(0, 0);
        sLegacyTypeToTransport.put(12, 0);
        sLegacyTypeToTransport.put(4, 0);
        sLegacyTypeToTransport.put(10, 0);
        sLegacyTypeToTransport.put(5, 0);
        sLegacyTypeToTransport.put(11, 0);
        sLegacyTypeToTransport.put(2, 0);
        sLegacyTypeToTransport.put(3, 0);
        sLegacyTypeToTransport.put(1, 1);
        sLegacyTypeToTransport.put(13, 1);
        sLegacyTypeToTransport.put(7, 2);
        sLegacyTypeToTransport.put(9, 3);
        sLegacyTypeToCapability.put(12, 5);
        sLegacyTypeToCapability.put(4, 2);
        sLegacyTypeToCapability.put(10, 3);
        sLegacyTypeToCapability.put(11, 4);
        sLegacyTypeToCapability.put(2, 0);
        sLegacyTypeToCapability.put(3, 1);
        sLegacyTypeToCapability.put(13, 6);
    }

    @Deprecated
    public static boolean isNetworkTypeValid(int networkType) {
        return networkType >= 0 && networkType <= 18;
    }

    @Deprecated
    @UnsupportedAppUsage
    public static String getNetworkTypeName(int type) {
        switch (type) {
            case -1:
                return KeyProperties.DIGEST_NONE;
            case 0:
                return "MOBILE";
            case 1:
                return "WIFI";
            case 2:
                return "MOBILE_MMS";
            case 3:
                return "MOBILE_SUPL";
            case 4:
                return "MOBILE_DUN";
            case 5:
                return "MOBILE_HIPRI";
            case 6:
                return "WIMAX";
            case 7:
                return "BLUETOOTH";
            case 8:
                return "DUMMY";
            case 9:
                return "ETHERNET";
            case 10:
                return "MOBILE_FOTA";
            case 11:
                return "MOBILE_IMS";
            case 12:
                return "MOBILE_CBS";
            case 13:
                return "WIFI_P2P";
            case 14:
                return "MOBILE_IA";
            case 15:
                return "MOBILE_EMERGENCY";
            case 16:
                return "PROXY";
            case 17:
                return "VPN";
            default:
                return Integer.toString(type);
        }
    }

    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 130143562)
    public static boolean isNetworkTypeMobile(int networkType) {
        if (!(networkType == 0 || networkType == 2 || networkType == 3 || networkType == 4 || networkType == 5 || networkType == 14 || networkType == 15)) {
            switch (networkType) {
                case 10:
                case 11:
                case 12:
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    @Deprecated
    public static boolean isNetworkTypeWifi(int networkType) {
        if (networkType == 1 || networkType == 13) {
            return true;
        }
        return false;
    }

    @Deprecated
    public void setNetworkPreference(int preference) {
    }

    @Deprecated
    public int getNetworkPreference() {
        return -1;
    }

    @Deprecated
    public NetworkInfo getActiveNetworkInfo() {
        try {
            return this.mService.getActiveNetworkInfo();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public Network getActiveNetwork() {
        try {
            return this.mService.getActiveNetwork();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public Network getActiveNetworkForUid(int uid) {
        return getActiveNetworkForUid(uid, false);
    }

    public Network getActiveNetworkForUid(int uid, boolean ignoreBlocked) {
        try {
            return this.mService.getActiveNetworkForUid(uid, ignoreBlocked);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isAlwaysOnVpnPackageSupportedForUser(int userId, String vpnPackage) {
        try {
            return this.mService.isAlwaysOnVpnPackageSupported(userId, vpnPackage);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean setAlwaysOnVpnPackageForUser(int userId, String vpnPackage, boolean lockdownEnabled, List<String> lockdownWhitelist) {
        try {
            return this.mService.setAlwaysOnVpnPackage(userId, vpnPackage, lockdownEnabled, lockdownWhitelist);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String getAlwaysOnVpnPackageForUser(int userId) {
        try {
            return this.mService.getAlwaysOnVpnPackage(userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isVpnLockdownEnabled(int userId) {
        try {
            return this.mService.isVpnLockdownEnabled(userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<String> getVpnLockdownWhitelist(int userId) {
        try {
            return this.mService.getVpnLockdownWhitelist(userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public NetworkInfo getActiveNetworkInfoForUid(int uid) {
        return getActiveNetworkInfoForUid(uid, false);
    }

    public NetworkInfo getActiveNetworkInfoForUid(int uid, boolean ignoreBlocked) {
        try {
            return this.mService.getActiveNetworkInfoForUid(uid, ignoreBlocked);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public NetworkInfo getNetworkInfo(int networkType) {
        try {
            return this.mService.getNetworkInfo(networkType);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public NetworkInfo getNetworkInfo(Network network) {
        return getNetworkInfoForUid(network, Process.myUid(), false);
    }

    public NetworkInfo getNetworkInfoForUid(Network network, int uid, boolean ignoreBlocked) {
        try {
            return this.mService.getNetworkInfoForUid(network, uid, ignoreBlocked);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public NetworkInfo[] getAllNetworkInfo() {
        try {
            return this.mService.getAllNetworkInfo();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    @UnsupportedAppUsage
    public Network getNetworkForType(int networkType) {
        try {
            return this.mService.getNetworkForType(networkType);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public Network[] getAllNetworks() {
        try {
            return this.mService.getAllNetworks();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public NetworkCapabilities[] getDefaultNetworkCapabilitiesForUser(int userId) {
        try {
            return this.mService.getDefaultNetworkCapabilitiesForUser(userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 109783091)
    public LinkProperties getActiveLinkProperties() {
        try {
            return this.mService.getActiveLinkProperties();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 130143562)
    public LinkProperties getLinkProperties(int networkType) {
        try {
            return this.mService.getLinkPropertiesForType(networkType);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public LinkProperties getLinkProperties(Network network) {
        try {
            return this.mService.getLinkProperties(network);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public NetworkCapabilities getNetworkCapabilities(Network network) {
        try {
            return this.mService.getNetworkCapabilities(network);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public String getCaptivePortalServerUrl() {
        try {
            return this.mService.getCaptivePortalServerUrl();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* JADX WARNING: Missing block: B:18:0x0063, code skipped:
            if (r2 == null) goto L_0x007d;
     */
    /* JADX WARNING: Missing block: B:19:0x0065, code skipped:
            r1 = new java.lang.StringBuilder();
            r1.append("starting startUsingNetworkFeature for request ");
            r1.append(r2);
            android.util.Log.d(TAG, r1.toString());
     */
    /* JADX WARNING: Missing block: B:20:0x007c, code skipped:
            return 1;
     */
    /* JADX WARNING: Missing block: B:21:0x007d, code skipped:
            android.util.Log.d(TAG, " request Failed");
     */
    /* JADX WARNING: Missing block: B:22:0x0084, code skipped:
            return 3;
     */
    @java.lang.Deprecated
    public int startUsingNetworkFeature(int r9, java.lang.String r10) {
        /*
        r8 = this;
        r8.checkLegacyRoutingApiAccess();
        r0 = r8.networkCapabilitiesForFeature(r9, r10);
        r1 = 3;
        if (r0 != 0) goto L_0x0029;
    L_0x000a:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Can't satisfy startUsingNetworkFeature for ";
        r2.append(r3);
        r2.append(r9);
        r3 = ", ";
        r2.append(r3);
        r2.append(r10);
        r2 = r2.toString();
        r3 = "ConnectivityManager";
        android.util.Log.d(r3, r2);
        return r1;
    L_0x0029:
        r2 = 0;
        r3 = sLegacyRequests;
        monitor-enter(r3);
        r4 = sLegacyRequests;	 Catch:{ all -> 0x0085 }
        r4 = r4.get(r0);	 Catch:{ all -> 0x0085 }
        r4 = (android.net.ConnectivityManager.LegacyRequest) r4;	 Catch:{ all -> 0x0085 }
        r5 = 1;
        if (r4 == 0) goto L_0x005d;
    L_0x0038:
        r1 = "ConnectivityManager";
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0085 }
        r6.<init>();	 Catch:{ all -> 0x0085 }
        r7 = "renewing startUsingNetworkFeature request ";
        r6.append(r7);	 Catch:{ all -> 0x0085 }
        r7 = r4.networkRequest;	 Catch:{ all -> 0x0085 }
        r6.append(r7);	 Catch:{ all -> 0x0085 }
        r6 = r6.toString();	 Catch:{ all -> 0x0085 }
        android.util.Log.d(r1, r6);	 Catch:{ all -> 0x0085 }
        r8.renewRequestLocked(r4);	 Catch:{ all -> 0x0085 }
        r1 = r4.currentNetwork;	 Catch:{ all -> 0x0085 }
        if (r1 == 0) goto L_0x005b;
    L_0x0058:
        r1 = 0;
        monitor-exit(r3);	 Catch:{ all -> 0x0085 }
        return r1;
    L_0x005b:
        monitor-exit(r3);	 Catch:{ all -> 0x0085 }
        return r5;
    L_0x005d:
        r6 = r8.requestNetworkForFeatureLocked(r0);	 Catch:{ all -> 0x0085 }
        r2 = r6;
        monitor-exit(r3);	 Catch:{ all -> 0x0085 }
        if (r2 == 0) goto L_0x007d;
    L_0x0065:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r3 = "starting startUsingNetworkFeature for request ";
        r1.append(r3);
        r1.append(r2);
        r1 = r1.toString();
        r3 = "ConnectivityManager";
        android.util.Log.d(r3, r1);
        return r5;
    L_0x007d:
        r3 = "ConnectivityManager";
        r4 = " request Failed";
        android.util.Log.d(r3, r4);
        return r1;
    L_0x0085:
        r1 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0085 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.ConnectivityManager.startUsingNetworkFeature(int, java.lang.String):int");
    }

    @Deprecated
    public int stopUsingNetworkFeature(int networkType, String feature) {
        checkLegacyRoutingApiAccess();
        NetworkCapabilities netCap = networkCapabilitiesForFeature(networkType, feature);
        String str = ", ";
        String str2 = TAG;
        StringBuilder stringBuilder;
        if (netCap == null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Can't satisfy stopUsingNetworkFeature for ");
            stringBuilder.append(networkType);
            stringBuilder.append(str);
            stringBuilder.append(feature);
            Log.d(str2, stringBuilder.toString());
            return -1;
        }
        if (removeRequestForFeature(netCap)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("stopUsingNetworkFeature for ");
            stringBuilder.append(networkType);
            stringBuilder.append(str);
            stringBuilder.append(feature);
            Log.d(str2, stringBuilder.toString());
        }
        return 1;
    }

    /* JADX WARNING: Missing block: B:20:0x0049, code skipped:
            if (r10.equals("enableDUN") != false) goto L_0x0061;
     */
    @android.annotation.UnsupportedAppUsage
    private android.net.NetworkCapabilities networkCapabilitiesForFeature(int r9, java.lang.String r10) {
        /*
        r8 = this;
        r0 = 0;
        r1 = 1;
        if (r9 != 0) goto L_0x008e;
    L_0x0004:
        r2 = -1;
        r3 = r10.hashCode();
        r4 = 3;
        r5 = 2;
        r6 = 5;
        r7 = 4;
        switch(r3) {
            case -1451370941: goto L_0x0056;
            case -631682191: goto L_0x004c;
            case -631680646: goto L_0x0043;
            case -631676084: goto L_0x0039;
            case -631672240: goto L_0x002f;
            case 1892790521: goto L_0x0025;
            case 1893183457: goto L_0x001b;
            case 1998933033: goto L_0x0011;
            default: goto L_0x0010;
        };
    L_0x0010:
        goto L_0x0060;
    L_0x0011:
        r1 = "enableDUNAlways";
        r1 = r10.equals(r1);
        if (r1 == 0) goto L_0x0010;
    L_0x0019:
        r1 = r5;
        goto L_0x0061;
    L_0x001b:
        r1 = "enableSUPL";
        r1 = r10.equals(r1);
        if (r1 == 0) goto L_0x0010;
    L_0x0023:
        r1 = 7;
        goto L_0x0061;
    L_0x0025:
        r1 = "enableFOTA";
        r1 = r10.equals(r1);
        if (r1 == 0) goto L_0x0010;
    L_0x002d:
        r1 = r4;
        goto L_0x0061;
    L_0x002f:
        r1 = "enableMMS";
        r1 = r10.equals(r1);
        if (r1 == 0) goto L_0x0010;
    L_0x0037:
        r1 = 6;
        goto L_0x0061;
    L_0x0039:
        r1 = "enableIMS";
        r1 = r10.equals(r1);
        if (r1 == 0) goto L_0x0010;
    L_0x0041:
        r1 = r6;
        goto L_0x0061;
    L_0x0043:
        r3 = "enableDUN";
        r3 = r10.equals(r3);
        if (r3 == 0) goto L_0x0010;
    L_0x004b:
        goto L_0x0061;
    L_0x004c:
        r1 = "enableCBS";
        r1 = r10.equals(r1);
        if (r1 == 0) goto L_0x0010;
    L_0x0054:
        r1 = 0;
        goto L_0x0061;
    L_0x0056:
        r1 = "enableHIPRI";
        r1 = r10.equals(r1);
        if (r1 == 0) goto L_0x0010;
    L_0x005e:
        r1 = r7;
        goto L_0x0061;
    L_0x0060:
        r1 = r2;
    L_0x0061:
        switch(r1) {
            case 0: goto L_0x0087;
            case 1: goto L_0x0082;
            case 2: goto L_0x0082;
            case 3: goto L_0x007b;
            case 4: goto L_0x0076;
            case 5: goto L_0x006f;
            case 6: goto L_0x006a;
            case 7: goto L_0x0065;
            default: goto L_0x0064;
        };
    L_0x0064:
        return r0;
    L_0x0065:
        r0 = networkCapabilitiesForType(r4);
        return r0;
    L_0x006a:
        r0 = networkCapabilitiesForType(r5);
        return r0;
    L_0x006f:
        r0 = 11;
        r0 = networkCapabilitiesForType(r0);
        return r0;
    L_0x0076:
        r0 = networkCapabilitiesForType(r6);
        return r0;
    L_0x007b:
        r0 = 10;
        r0 = networkCapabilitiesForType(r0);
        return r0;
    L_0x0082:
        r0 = networkCapabilitiesForType(r7);
        return r0;
    L_0x0087:
        r0 = 12;
        r0 = networkCapabilitiesForType(r0);
        return r0;
    L_0x008e:
        if (r9 != r1) goto L_0x00a0;
    L_0x0090:
        r1 = "p2p";
        r1 = r1.equals(r10);
        if (r1 == 0) goto L_0x00a0;
    L_0x0099:
        r0 = 13;
        r0 = networkCapabilitiesForType(r0);
        return r0;
    L_0x00a0:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.ConnectivityManager.networkCapabilitiesForFeature(int, java.lang.String):android.net.NetworkCapabilities");
    }

    private int inferLegacyTypeForNetworkCapabilities(NetworkCapabilities netCap) {
        if (netCap == null || !netCap.hasTransport(0) || !netCap.hasCapability(1)) {
            return -1;
        }
        String type = null;
        int result = -1;
        if (netCap.hasCapability(5)) {
            type = "enableCBS";
            result = 12;
        } else if (netCap.hasCapability(4)) {
            type = "enableIMS";
            result = 11;
        } else if (netCap.hasCapability(3)) {
            type = "enableFOTA";
            result = 10;
        } else if (netCap.hasCapability(2)) {
            type = "enableDUN";
            result = 4;
        } else if (netCap.hasCapability(1)) {
            type = "enableSUPL";
            result = 3;
        } else if (netCap.hasCapability(12)) {
            type = "enableHIPRI";
            result = 5;
        }
        if (type != null) {
            NetworkCapabilities testCap = networkCapabilitiesForFeature(0, type);
            if (testCap.equalsNetCapabilities(netCap) && testCap.equalsTransportTypes(netCap)) {
                return result;
            }
            return -1;
        }
        return -1;
    }

    private int legacyTypeForNetworkCapabilities(NetworkCapabilities netCap) {
        if (netCap == null) {
            return -1;
        }
        if (netCap.hasCapability(5)) {
            return 12;
        }
        if (netCap.hasCapability(4)) {
            return 11;
        }
        if (netCap.hasCapability(3)) {
            return 10;
        }
        if (netCap.hasCapability(2)) {
            return 4;
        }
        if (netCap.hasCapability(1)) {
            return 3;
        }
        if (netCap.hasCapability(0)) {
            return 2;
        }
        if (netCap.hasCapability(12)) {
            return 5;
        }
        if (netCap.hasCapability(6)) {
            return 13;
        }
        return -1;
    }

    private NetworkRequest findRequestForFeature(NetworkCapabilities netCap) {
        synchronized (sLegacyRequests) {
            LegacyRequest l = (LegacyRequest) sLegacyRequests.get(netCap);
            if (l != null) {
                NetworkRequest networkRequest = l.networkRequest;
                return networkRequest;
            }
            return null;
        }
    }

    private void renewRequestLocked(LegacyRequest l) {
        l.expireSequenceNumber++;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("renewing request to seqNum ");
        stringBuilder.append(l.expireSequenceNumber);
        Log.d(TAG, stringBuilder.toString());
        sendExpireMsgForFeature(l.networkCapabilities, l.expireSequenceNumber, l.delay);
    }

    /* JADX WARNING: Missing block: B:11:0x001b, code skipped:
            r1 = new java.lang.StringBuilder();
            r1.append("expireRequest with ");
            r1.append(r0);
            r1.append(", ");
            r1.append(r6);
            android.util.Log.d(TAG, r1.toString());
     */
    /* JADX WARNING: Missing block: B:12:0x0039, code skipped:
            return;
     */
    private void expireRequest(android.net.NetworkCapabilities r5, int r6) {
        /*
        r4 = this;
        r0 = -1;
        r1 = sLegacyRequests;
        monitor-enter(r1);
        r2 = sLegacyRequests;	 Catch:{ all -> 0x003a }
        r2 = r2.get(r5);	 Catch:{ all -> 0x003a }
        r2 = (android.net.ConnectivityManager.LegacyRequest) r2;	 Catch:{ all -> 0x003a }
        if (r2 != 0) goto L_0x0010;
    L_0x000e:
        monitor-exit(r1);	 Catch:{ all -> 0x003a }
        return;
    L_0x0010:
        r3 = r2.expireSequenceNumber;	 Catch:{ all -> 0x003a }
        r0 = r3;
        r3 = r2.expireSequenceNumber;	 Catch:{ all -> 0x003a }
        if (r3 != r6) goto L_0x001a;
    L_0x0017:
        r4.removeRequestForFeature(r5);	 Catch:{ all -> 0x003a }
    L_0x001a:
        monitor-exit(r1);	 Catch:{ all -> 0x003a }
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "expireRequest with ";
        r1.append(r2);
        r1.append(r0);
        r2 = ", ";
        r1.append(r2);
        r1.append(r6);
        r1 = r1.toString();
        r2 = "ConnectivityManager";
        android.util.Log.d(r2, r1);
        return;
    L_0x003a:
        r2 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x003a }
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.ConnectivityManager.expireRequest(android.net.NetworkCapabilities, int):void");
    }

    @UnsupportedAppUsage
    private NetworkRequest requestNetworkForFeatureLocked(NetworkCapabilities netCap) {
        int type = legacyTypeForNetworkCapabilities(netCap);
        try {
            int delay = this.mService.getRestoreDefaultNetworkDelay(type);
            LegacyRequest l = new LegacyRequest();
            l.networkCapabilities = netCap;
            l.delay = delay;
            l.expireSequenceNumber = 0;
            l.networkRequest = sendRequestForNetwork(netCap, l.networkCallback, 0, 2, type, getDefaultHandler());
            if (l.networkRequest == null) {
                return null;
            }
            sLegacyRequests.put(netCap, l);
            sendExpireMsgForFeature(netCap, l.expireSequenceNumber, delay);
            return l.networkRequest;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private void sendExpireMsgForFeature(NetworkCapabilities netCap, int seqNum, int delay) {
        if (delay >= 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("sending expire msg with seqNum ");
            stringBuilder.append(seqNum);
            stringBuilder.append(" and delay ");
            stringBuilder.append(delay);
            Log.d(TAG, stringBuilder.toString());
            CallbackHandler handler = getDefaultHandler();
            handler.sendMessageDelayed(handler.obtainMessage(EXPIRE_LEGACY_REQUEST, seqNum, 0, netCap), (long) delay);
        }
    }

    @UnsupportedAppUsage
    private boolean removeRequestForFeature(NetworkCapabilities netCap) {
        LegacyRequest l;
        synchronized (sLegacyRequests) {
            l = (LegacyRequest) sLegacyRequests.remove(netCap);
        }
        if (l == null) {
            return false;
        }
        unregisterNetworkCallback(l.networkCallback);
        l.clearDnsBinding();
        return true;
    }

    public static NetworkCapabilities networkCapabilitiesForType(int type) {
        NetworkCapabilities nc = new NetworkCapabilities();
        int transport = sLegacyTypeToTransport.get(type, -1);
        boolean z = transport != -1;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("unknown legacy type: ");
        stringBuilder.append(type);
        Preconditions.checkArgument(z, stringBuilder.toString());
        nc.addTransportType(transport);
        nc.addCapability(sLegacyTypeToCapability.get(type, 12));
        nc.maybeMarkCapabilitiesRestricted();
        return nc;
    }

    @UnsupportedAppUsage
    public PacketKeepalive startNattKeepalive(Network network, int intervalSeconds, PacketKeepaliveCallback callback, InetAddress srcAddr, int srcPort, InetAddress dstAddr) {
        PacketKeepalive k = new PacketKeepalive(this, network, callback, null);
        try {
            this.mService.startNattKeepalive(network, intervalSeconds, k.mCallback, srcAddr.getHostAddress(), srcPort, dstAddr.getHostAddress());
            return k;
        } catch (RemoteException e) {
            Log.e(TAG, "Error starting packet keepalive: ", e);
            throw e.rethrowFromSystemServer();
        }
    }

    public SocketKeepalive createSocketKeepalive(Network network, UdpEncapsulationSocket socket, InetAddress source, InetAddress destination, Executor executor, Callback callback) {
        ParcelFileDescriptor dup;
        try {
            dup = ParcelFileDescriptor.dup(socket.getFileDescriptor());
        } catch (IOException e) {
            dup = new ParcelFileDescriptor(new FileDescriptor());
        }
        return new NattSocketKeepalive(this.mService, network, dup, socket.getResourceId(), source, destination, executor, callback);
    }

    @SystemApi
    public SocketKeepalive createNattKeepalive(Network network, ParcelFileDescriptor pfd, InetAddress source, InetAddress destination, Executor executor, Callback callback) {
        ParcelFileDescriptor dup;
        try {
            dup = pfd.dup();
        } catch (IOException ignored) {
            IOException ignored2 = ignored2;
            dup = new ParcelFileDescriptor(new FileDescriptor());
        }
        return new NattSocketKeepalive(this.mService, network, dup, -1, source, destination, executor, callback);
    }

    @SystemApi
    public SocketKeepalive createSocketKeepalive(Network network, Socket socket, Executor executor, Callback callback) {
        ParcelFileDescriptor dup;
        try {
            dup = ParcelFileDescriptor.fromSocket(socket);
        } catch (UncheckedIOException e) {
            dup = new ParcelFileDescriptor(new FileDescriptor());
        }
        return new TcpSocketKeepalive(this.mService, network, dup, executor, callback);
    }

    @Deprecated
    public boolean requestRouteToHost(int networkType, int hostAddress) {
        return requestRouteToHostAddress(networkType, NetworkUtils.intToInetAddress(hostAddress));
    }

    @Deprecated
    @UnsupportedAppUsage
    public boolean requestRouteToHostAddress(int networkType, InetAddress hostAddress) {
        checkLegacyRoutingApiAccess();
        try {
            return this.mService.requestRouteToHostAddress(networkType, hostAddress.getAddress());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public boolean getBackgroundDataSetting() {
        return true;
    }

    @Deprecated
    @UnsupportedAppUsage
    public void setBackgroundDataSetting(boolean allowBackgroundData) {
    }

    @Deprecated
    @UnsupportedAppUsage
    public NetworkQuotaInfo getActiveNetworkQuotaInfo() {
        try {
            return this.mService.getActiveNetworkQuotaInfo();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    @UnsupportedAppUsage
    public boolean getMobileDataEnabled() {
        IBinder b = ServiceManager.getService("phone");
        String str = TAG;
        if (b != null) {
            try {
                ITelephony it = ITelephony.Stub.asInterface(b);
                int subId = SubscriptionManager.getDefaultDataSubscriptionId();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("getMobileDataEnabled()+ subId=");
                stringBuilder.append(subId);
                Log.d(str, stringBuilder.toString());
                boolean retVal = it.isUserDataEnabled(subId);
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("getMobileDataEnabled()- subId=");
                stringBuilder2.append(subId);
                stringBuilder2.append(" retVal=");
                stringBuilder2.append(retVal);
                Log.d(str, stringBuilder2.toString());
                return retVal;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        Log.d(str, "getMobileDataEnabled()- remote exception retVal=false");
        return false;
    }

    private INetworkManagementService getNetworkManagementService() {
        synchronized (this) {
            if (this.mNMService != null) {
                INetworkManagementService iNetworkManagementService = this.mNMService;
                return iNetworkManagementService;
            }
            this.mNMService = INetworkManagementService.Stub.asInterface(ServiceManager.getService(Context.NETWORKMANAGEMENT_SERVICE));
            INetworkManagementService iNetworkManagementService2 = this.mNMService;
            return iNetworkManagementService2;
        }
    }

    public void addDefaultNetworkActiveListener(final OnNetworkActiveListener l) {
        INetworkActivityListener rl = new INetworkActivityListener.Stub() {
            public void onNetworkActive() throws RemoteException {
                l.onNetworkActive();
            }
        };
        try {
            getNetworkManagementService().registerNetworkActivityListener(rl);
            this.mNetworkActivityListeners.put(l, rl);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void removeDefaultNetworkActiveListener(OnNetworkActiveListener l) {
        INetworkActivityListener rl = (INetworkActivityListener) this.mNetworkActivityListeners.get(l);
        Preconditions.checkArgument(rl != null, "Listener was not registered.");
        try {
            getNetworkManagementService().unregisterNetworkActivityListener(rl);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isDefaultNetworkActive() {
        try {
            return getNetworkManagementService().isNetworkActive();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public ConnectivityManager(Context context, IConnectivityManager service) {
        this.mContext = (Context) Preconditions.checkNotNull(context, "missing context");
        this.mService = (IConnectivityManager) Preconditions.checkNotNull(service, "missing IConnectivityManager");
        sInstance = this;
    }

    @UnsupportedAppUsage
    public static ConnectivityManager from(Context context) {
        return (ConnectivityManager) context.getSystemService("connectivity");
    }

    public NetworkRequest getDefaultRequest() {
        try {
            return this.mService.getDefaultRequest();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static final void enforceChangePermission(Context context) {
        int uid = Binder.getCallingUid();
        Settings.checkAndNoteChangeNetworkStateOperation(context, uid, Settings.getPackageNameForUid(context, uid), true);
    }

    public static final void enforceTetherChangePermission(Context context, String callingPkg) {
        Preconditions.checkNotNull(context, "Context cannot be null");
        Preconditions.checkNotNull(callingPkg, "callingPkg cannot be null");
        if (context.getResources().getStringArray(R.array.config_mobile_hotspot_provision_app).length == 2) {
            context.enforceCallingOrSelfPermission(permission.TETHER_PRIVILEGED, "ConnectivityService");
        } else {
            Settings.checkAndNoteWriteSettingsOperation(context, Binder.getCallingUid(), callingPkg, true);
        }
    }

    @Deprecated
    static ConnectivityManager getInstanceOrNull() {
        return sInstance;
    }

    @Deprecated
    @UnsupportedAppUsage
    private static ConnectivityManager getInstance() {
        if (getInstanceOrNull() != null) {
            return getInstanceOrNull();
        }
        throw new IllegalStateException("No ConnectivityManager yet constructed");
    }

    @UnsupportedAppUsage
    public String[] getTetherableIfaces() {
        try {
            return this.mService.getTetherableIfaces();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public String[] getTetheredIfaces() {
        try {
            return this.mService.getTetheredIfaces();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public String[] getTetheringErroredIfaces() {
        try {
            return this.mService.getTetheringErroredIfaces();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String[] getTetheredDhcpRanges() {
        try {
            return this.mService.getTetheredDhcpRanges();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public int tether(String iface) {
        try {
            String pkgName = this.mContext.getOpPackageName();
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("tether caller:");
            stringBuilder.append(pkgName);
            Log.i(str, stringBuilder.toString());
            return this.mService.tether(iface, pkgName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public int untether(String iface) {
        try {
            String pkgName = this.mContext.getOpPackageName();
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("untether caller:");
            stringBuilder.append(pkgName);
            Log.i(str, stringBuilder.toString());
            return this.mService.untether(iface, pkgName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public boolean isTetheringSupported() {
        try {
            return this.mService.isTetheringSupported(this.mContext.getOpPackageName());
        } catch (SecurityException e) {
            return false;
        } catch (RemoteException e2) {
            throw e2.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void startTethering(int type, boolean showProvisioningUi, OnStartTetheringCallback callback) {
        startTethering(type, showProvisioningUi, callback, null);
    }

    @SystemApi
    public void startTethering(int type, boolean showProvisioningUi, final OnStartTetheringCallback callback, Handler handler) {
        String str = TAG;
        Preconditions.checkNotNull(callback, "OnStartTetheringCallback cannot be null.");
        ResultReceiver wrappedCallback = new ResultReceiver(handler) {
            /* Access modifiers changed, original: protected */
            public void onReceiveResult(int resultCode, Bundle resultData) {
                if (resultCode == 0) {
                    callback.onTetheringStarted();
                } else {
                    callback.onTetheringFailed();
                }
            }
        };
        try {
            String pkgName = this.mContext.getOpPackageName();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("startTethering caller:");
            stringBuilder.append(pkgName);
            Log.i(str, stringBuilder.toString());
            this.mService.startTethering(type, wrappedCallback, showProvisioningUi, pkgName);
        } catch (RemoteException e) {
            Log.e(str, "Exception trying to start tethering.", e);
            wrappedCallback.send(2, null);
        }
    }

    @SystemApi
    public void stopTethering(int type) {
        try {
            String pkgName = this.mContext.getOpPackageName();
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("stopTethering caller:");
            stringBuilder.append(pkgName);
            Log.i(str, stringBuilder.toString());
            this.mService.stopTethering(type, pkgName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void registerTetheringEventCallback(final Executor executor, final OnTetheringEventCallback callback) {
        Preconditions.checkNotNull(callback, "OnTetheringEventCallback cannot be null.");
        synchronized (this.mTetheringEventCallbacks) {
            Preconditions.checkArgument(!this.mTetheringEventCallbacks.containsKey(callback), "callback was already registered.");
            ITetheringEventCallback remoteCallback = new ITetheringEventCallback.Stub() {
                public void onUpstreamChanged(Network network) throws RemoteException {
                    Binder.withCleanCallingIdentity(new -$$Lambda$ConnectivityManager$3$BfAvTRJTF0an2PdeqkENEBULYBU(executor, callback, network));
                }
            };
            try {
                String pkgName = this.mContext.getOpPackageName();
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("registerTetheringUpstreamCallback:");
                stringBuilder.append(pkgName);
                Log.i(str, stringBuilder.toString());
                this.mService.registerTetheringEventCallback(remoteCallback, pkgName);
                this.mTetheringEventCallbacks.put(callback, remoteCallback);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    @SystemApi
    public void unregisterTetheringEventCallback(OnTetheringEventCallback callback) {
        synchronized (this.mTetheringEventCallbacks) {
            ITetheringEventCallback remoteCallback = (ITetheringEventCallback) this.mTetheringEventCallbacks.remove(callback);
            Preconditions.checkNotNull(remoteCallback, "callback was not registered.");
            try {
                String pkgName = this.mContext.getOpPackageName();
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("unregisterTetheringEventCallback:");
                stringBuilder.append(pkgName);
                Log.i(str, stringBuilder.toString());
                this.mService.unregisterTetheringEventCallback(remoteCallback, pkgName);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    @UnsupportedAppUsage
    public String[] getTetherableUsbRegexs() {
        try {
            return this.mService.getTetherableUsbRegexs();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public String[] getTetherableWifiRegexs() {
        try {
            return this.mService.getTetherableWifiRegexs();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public String[] getTetherableBluetoothRegexs() {
        try {
            return this.mService.getTetherableBluetoothRegexs();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public int setUsbTethering(boolean enable) {
        try {
            String pkgName = this.mContext.getOpPackageName();
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("setUsbTethering caller:");
            stringBuilder.append(pkgName);
            Log.i(str, stringBuilder.toString());
            return this.mService.setUsbTethering(enable, pkgName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public int getLastTetherError(String iface) {
        try {
            return this.mService.getLastTetherError(iface);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void getLatestTetheringEntitlementResult(int type, boolean showEntitlementUi, final Executor executor, final OnTetheringEntitlementResultListener listener) {
        Preconditions.checkNotNull(listener, "TetheringEntitlementResultListener cannot be null.");
        ResultReceiver wrappedListener = new ResultReceiver(null) {
            /* Access modifiers changed, original: protected */
            public void onReceiveResult(int resultCode, Bundle resultData) {
                Binder.withCleanCallingIdentity(new -$$Lambda$ConnectivityManager$4$Jk-u9vR1DwqMOUorHyaTIOdhOAs(executor, listener, resultCode));
            }
        };
        try {
            String pkgName = this.mContext.getOpPackageName();
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getLatestTetheringEntitlementResult:");
            stringBuilder.append(pkgName);
            Log.i(str, stringBuilder.toString());
            this.mService.getLatestTetheringEntitlementResult(type, wrappedListener, showEntitlementUi, pkgName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void reportInetCondition(int networkType, int percentage) {
        printStackTrace();
        try {
            this.mService.reportInetCondition(networkType, percentage);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public void reportBadNetwork(Network network) {
        printStackTrace();
        try {
            this.mService.reportNetworkConnectivity(network, true);
            this.mService.reportNetworkConnectivity(network, false);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void reportNetworkConnectivity(Network network, boolean hasConnectivity) {
        printStackTrace();
        try {
            this.mService.reportNetworkConnectivity(network, hasConnectivity);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setGlobalProxy(ProxyInfo p) {
        try {
            this.mService.setGlobalProxy(p);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public ProxyInfo getGlobalProxy() {
        try {
            return this.mService.getGlobalProxy();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public ProxyInfo getProxyForNetwork(Network network) {
        try {
            return this.mService.getProxyForNetwork(network);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public ProxyInfo getDefaultProxy() {
        return getProxyForNetwork(getBoundNetworkForProcess());
    }

    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 130143562)
    public boolean isNetworkSupported(int networkType) {
        try {
            return this.mService.isNetworkSupported(networkType);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isActiveNetworkMetered() {
        try {
            return this.mService.isActiveNetworkMetered();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean updateLockdownVpn() {
        try {
            return this.mService.updateLockdownVpn();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int checkMobileProvisioning(int suggestedTimeOutMs) {
        try {
            return this.mService.checkMobileProvisioning(suggestedTimeOutMs);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String getMobileProvisioningUrl() {
        try {
            return this.mService.getMobileProvisioningUrl();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public void setProvisioningNotificationVisible(boolean visible, int networkType, String action) {
        try {
            this.mService.setProvisioningNotificationVisible(visible, networkType, action);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void setAirplaneMode(boolean enable) {
        try {
            this.mService.setAirplaneMode(enable);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public int registerNetworkFactory(Messenger messenger, String name) {
        try {
            return this.mService.registerNetworkFactory(messenger, name);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public void unregisterNetworkFactory(Messenger messenger) {
        try {
            this.mService.unregisterNetworkFactory(messenger);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int registerNetworkAgent(Messenger messenger, NetworkInfo ni, LinkProperties lp, NetworkCapabilities nc, int score, NetworkMisc misc) {
        return registerNetworkAgent(messenger, ni, lp, nc, score, misc, -1);
    }

    public int registerNetworkAgent(Messenger messenger, NetworkInfo ni, LinkProperties lp, NetworkCapabilities nc, int score, NetworkMisc misc, int factorySerialNumber) {
        try {
            return this.mService.registerNetworkAgent(messenger, ni, lp, nc, score, misc, factorySerialNumber);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private static RuntimeException convertServiceException(ServiceSpecificException e) {
        if (e.errorCode == 1) {
            return new TooManyRequestsException();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unknown service error code ");
        stringBuilder.append(e.errorCode);
        Log.w(TAG, stringBuilder.toString());
        return new RuntimeException(e);
    }

    public static String getCallbackName(int whichCallback) {
        switch (whichCallback) {
            case CALLBACK_PRECHECK /*524289*/:
                return "CALLBACK_PRECHECK";
            case 524290:
                return "CALLBACK_AVAILABLE";
            case 524291:
                return "CALLBACK_LOSING";
            case 524292:
                return "CALLBACK_LOST";
            case CALLBACK_UNAVAIL /*524293*/:
                return "CALLBACK_UNAVAIL";
            case CALLBACK_CAP_CHANGED /*524294*/:
                return "CALLBACK_CAP_CHANGED";
            case CALLBACK_IP_CHANGED /*524295*/:
                return "CALLBACK_IP_CHANGED";
            case EXPIRE_LEGACY_REQUEST /*524296*/:
                return "EXPIRE_LEGACY_REQUEST";
            case CALLBACK_SUSPENDED /*524297*/:
                return "CALLBACK_SUSPENDED";
            case CALLBACK_RESUMED /*524298*/:
                return "CALLBACK_RESUMED";
            case CALLBACK_BLK_CHANGED /*524299*/:
                return "CALLBACK_BLK_CHANGED";
            default:
                return Integer.toString(whichCallback);
        }
    }

    private CallbackHandler getDefaultHandler() {
        CallbackHandler callbackHandler;
        synchronized (sCallbacks) {
            if (sCallbackHandler == null) {
                sCallbackHandler = new CallbackHandler(ConnectivityThread.getInstanceLooper());
            }
            callbackHandler = sCallbackHandler;
        }
        return callbackHandler;
    }

    private NetworkRequest sendRequestForNetwork(NetworkCapabilities need, NetworkCallback callback, int timeoutMs, int action, int legacyType, CallbackHandler handler) {
        Throwable th;
        CallbackHandler callbackHandler;
        RemoteException e;
        ServiceSpecificException e2;
        NetworkCapabilities networkCapabilities = need;
        NetworkCallback networkCallback = callback;
        int i = action;
        printStackTrace();
        checkCallbackNotNull(callback);
        boolean z = i == 2 || networkCapabilities != null;
        Preconditions.checkArgument(z, "null NetworkCapabilities");
        try {
            synchronized (sCallbacks) {
                try {
                    NetworkRequest request;
                    if (!(callback.networkRequest == null || callback.networkRequest == ALREADY_UNREGISTERED)) {
                        Log.e(TAG, "NetworkCallback was already registered");
                    }
                    Messenger messenger = new Messenger((Handler) handler);
                    Binder binder = new Binder();
                    if (i == 1) {
                        request = this.mService.listenForNetwork(networkCapabilities, messenger, binder);
                    } else {
                        request = this.mService.requestNetwork(need, messenger, timeoutMs, binder, legacyType);
                    }
                    if (request != null) {
                        sCallbacks.put(request, networkCallback);
                    }
                    networkCallback.networkRequest = request;
                    return request;
                } catch (Throwable th2) {
                    th = th2;
                    throw th;
                }
            }
        } catch (RemoteException e3) {
            e = e3;
            callbackHandler = handler;
            throw e.rethrowFromSystemServer();
        } catch (ServiceSpecificException e4) {
            e2 = e4;
            callbackHandler = handler;
            throw convertServiceException(e2);
        }
    }

    public void requestNetwork(NetworkRequest request, NetworkCallback networkCallback, int timeoutMs, int legacyType, Handler handler) {
        sendRequestForNetwork(request.networkCapabilities, networkCallback, timeoutMs, 2, legacyType, new CallbackHandler(this, handler));
    }

    public void requestNetwork(NetworkRequest request, NetworkCallback networkCallback) {
        requestNetwork(request, networkCallback, getDefaultHandler());
    }

    public void requestNetwork(NetworkRequest request, NetworkCallback networkCallback, Handler handler) {
        requestNetwork(request, networkCallback, 0, inferLegacyTypeForNetworkCapabilities(request.networkCapabilities), new CallbackHandler(this, handler));
    }

    public void requestNetwork(NetworkRequest request, NetworkCallback networkCallback, int timeoutMs) {
        checkTimeout(timeoutMs);
        requestNetwork(request, networkCallback, timeoutMs, inferLegacyTypeForNetworkCapabilities(request.networkCapabilities), getDefaultHandler());
    }

    public void requestNetwork(NetworkRequest request, NetworkCallback networkCallback, Handler handler, int timeoutMs) {
        checkTimeout(timeoutMs);
        requestNetwork(request, networkCallback, timeoutMs, inferLegacyTypeForNetworkCapabilities(request.networkCapabilities), new CallbackHandler(this, handler));
    }

    public void requestNetwork(NetworkRequest request, PendingIntent operation) {
        printStackTrace();
        checkPendingIntentNotNull(operation);
        try {
            this.mService.pendingRequestForNetwork(request.networkCapabilities, operation);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        } catch (ServiceSpecificException e2) {
            throw convertServiceException(e2);
        }
    }

    public void releaseNetworkRequest(PendingIntent operation) {
        printStackTrace();
        checkPendingIntentNotNull(operation);
        try {
            this.mService.releasePendingNetworkRequest(operation);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private static void checkPendingIntentNotNull(PendingIntent intent) {
        Preconditions.checkNotNull(intent, "PendingIntent cannot be null.");
    }

    private static void checkCallbackNotNull(NetworkCallback callback) {
        Preconditions.checkNotNull(callback, "null NetworkCallback");
    }

    private static void checkTimeout(int timeoutMs) {
        Preconditions.checkArgumentPositive(timeoutMs, "timeoutMs must be strictly positive.");
    }

    public void registerNetworkCallback(NetworkRequest request, NetworkCallback networkCallback) {
        registerNetworkCallback(request, networkCallback, getDefaultHandler());
    }

    public void registerNetworkCallback(NetworkRequest request, NetworkCallback networkCallback, Handler handler) {
        sendRequestForNetwork(request.networkCapabilities, networkCallback, 0, 1, -1, new CallbackHandler(this, handler));
    }

    public void registerNetworkCallback(NetworkRequest request, PendingIntent operation) {
        printStackTrace();
        checkPendingIntentNotNull(operation);
        try {
            this.mService.pendingListenForNetwork(request.networkCapabilities, operation);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        } catch (ServiceSpecificException e2) {
            throw convertServiceException(e2);
        }
    }

    public void registerDefaultNetworkCallback(NetworkCallback networkCallback) {
        registerDefaultNetworkCallback(networkCallback, getDefaultHandler());
    }

    public void registerDefaultNetworkCallback(NetworkCallback networkCallback, Handler handler) {
        sendRequestForNetwork(null, networkCallback, 0, 2, -1, new CallbackHandler(this, handler));
    }

    public boolean requestBandwidthUpdate(Network network) {
        try {
            return this.mService.requestBandwidthUpdate(network);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void unregisterNetworkCallback(NetworkCallback networkCallback) {
        printStackTrace();
        checkCallbackNotNull(networkCallback);
        List<NetworkRequest> reqs = new ArrayList();
        synchronized (sCallbacks) {
            Preconditions.checkArgument(networkCallback.networkRequest != null, "NetworkCallback was not registered");
            if (networkCallback.networkRequest == ALREADY_UNREGISTERED) {
                Log.d(TAG, "NetworkCallback was already unregistered");
                return;
            }
            for (Entry<NetworkRequest, NetworkCallback> e : sCallbacks.entrySet()) {
                if (e.getValue() == networkCallback) {
                    reqs.add((NetworkRequest) e.getKey());
                }
            }
            for (NetworkRequest r : reqs) {
                try {
                    this.mService.releaseNetworkRequest(r);
                    sCallbacks.remove(r);
                } catch (RemoteException e2) {
                    throw e2.rethrowFromSystemServer();
                }
            }
            networkCallback.networkRequest = ALREADY_UNREGISTERED;
        }
    }

    public void unregisterNetworkCallback(PendingIntent operation) {
        checkPendingIntentNotNull(operation);
        releaseNetworkRequest(operation);
    }

    public void setAcceptUnvalidated(Network network, boolean accept, boolean always) {
        try {
            this.mService.setAcceptUnvalidated(network, accept, always);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setAcceptPartialConnectivity(Network network, boolean accept, boolean always) {
        try {
            this.mService.setAcceptPartialConnectivity(network, accept, always);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setAvoidUnvalidated(Network network) {
        try {
            this.mService.setAvoidUnvalidated(network);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void startCaptivePortalApp(Network network) {
        try {
            this.mService.startCaptivePortalApp(network);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void startCaptivePortalApp(Network network, Bundle appExtras) {
        try {
            this.mService.startCaptivePortalAppInternal(network, appExtras);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public boolean shouldAvoidBadWifi() {
        try {
            return this.mService.shouldAvoidBadWifi();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getMultipathPreference(Network network) {
        try {
            return this.mService.getMultipathPreference(network);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void factoryReset() {
        try {
            this.mService.factoryReset();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean bindProcessToNetwork(Network network) {
        return setProcessDefaultNetwork(network);
    }

    @Deprecated
    public static boolean setProcessDefaultNetwork(Network network) {
        int netId = network == null ? 0 : network.netId;
        boolean isSameNetId = netId == NetworkUtils.getBoundNetworkForProcess();
        if (netId != 0) {
            netId = network.getNetIdForResolv();
        }
        if (!NetworkUtils.bindProcessToNetwork(netId)) {
            return false;
        }
        if (!isSameNetId) {
            try {
                Proxy.setHttpProxySystemProperty(getInstance().getDefaultProxy());
            } catch (SecurityException e) {
                Log.e(TAG, "Can't set proxy properties", e);
            }
            InetAddress.clearDnsCache();
            NetworkEventDispatcher.getInstance().onNetworkConfigurationChanged();
        }
        return true;
    }

    public Network getBoundNetworkForProcess() {
        return getProcessDefaultNetwork();
    }

    @Deprecated
    public static Network getProcessDefaultNetwork() {
        int netId = NetworkUtils.getBoundNetworkForProcess();
        if (netId == 0) {
            return null;
        }
        return new Network(netId);
    }

    private void unsupportedStartingFrom(int version) {
        if (Process.myUid() != 1000 && this.mContext.getApplicationInfo().targetSdkVersion >= version) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("This method is not supported in target SDK version ");
            stringBuilder.append(version);
            stringBuilder.append(" and above");
            throw new UnsupportedOperationException(stringBuilder.toString());
        }
    }

    private void checkLegacyRoutingApiAccess() {
        unsupportedStartingFrom(23);
    }

    @Deprecated
    @UnsupportedAppUsage
    public static boolean setProcessDefaultNetworkForHostResolution(Network network) {
        return NetworkUtils.bindProcessToNetworkForHostResolution(network == null ? 0 : network.getNetIdForResolv());
    }

    private INetworkPolicyManager getNetworkPolicyManager() {
        synchronized (this) {
            INetworkPolicyManager iNetworkPolicyManager;
            if (this.mNPManager != null) {
                iNetworkPolicyManager = this.mNPManager;
                return iNetworkPolicyManager;
            }
            this.mNPManager = INetworkPolicyManager.Stub.asInterface(ServiceManager.getService(Context.NETWORK_POLICY_SERVICE));
            iNetworkPolicyManager = this.mNPManager;
            return iNetworkPolicyManager;
        }
    }

    public int getRestrictBackgroundStatus() {
        try {
            return getNetworkPolicyManager().getRestrictBackgroundByCaller();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public byte[] getNetworkWatchlistConfigHash() {
        try {
            return this.mService.getNetworkWatchlistConfigHash();
        } catch (RemoteException e) {
            Log.e(TAG, "Unable to get watchlist config hash");
            throw e.rethrowFromSystemServer();
        }
    }

    public int getConnectionOwnerUid(int protocol, InetSocketAddress local, InetSocketAddress remote) {
        try {
            return this.mService.getConnectionOwnerUid(new ConnectionInfo(protocol, local, remote));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private void printStackTrace() {
        if (DEBUG) {
            StackTraceElement[] callStack = Thread.currentThread().getStackTrace();
            StringBuffer sb = new StringBuffer();
            for (int i = 3; i < callStack.length; i++) {
                String stackTrace = callStack[i].toString();
                if (stackTrace == null || stackTrace.contains("android.os")) {
                    break;
                }
                sb.append(" [");
                sb.append(stackTrace);
                sb.append("]");
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("StackLog:");
            stringBuilder.append(sb.toString());
            Log.d(TAG, stringBuilder.toString());
        }
    }
}
