package android.net.wifi.aware;

import android.content.Context;
import android.net.NetworkSpecifier;
import android.net.wifi.aware.IWifiAwareDiscoverySessionCallback.Stub;
import android.net.wifi.aware.TlvBufferUtils.TlvIterable;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.nio.BufferOverflowException;
import java.util.List;
import libcore.util.HexEncoding;

public class WifiAwareManager {
    public static final String ACTION_WIFI_AWARE_STATE_CHANGED = "android.net.wifi.aware.action.WIFI_AWARE_STATE_CHANGED";
    private static final boolean DBG = false;
    private static final String TAG = "WifiAwareManager";
    private static final boolean VDBG = false;
    public static final int WIFI_AWARE_DATA_PATH_ROLE_INITIATOR = 0;
    public static final int WIFI_AWARE_DATA_PATH_ROLE_RESPONDER = 1;
    private final Context mContext;
    private final Object mLock = new Object();
    private final IWifiAwareManager mService;

    @Retention(RetentionPolicy.SOURCE)
    public @interface DataPathRole {
    }

    private static class WifiAwareDiscoverySessionCallbackProxy extends Stub {
        private static final int CALLBACK_MATCH = 4;
        private static final int CALLBACK_MATCH_WITH_DISTANCE = 8;
        private static final int CALLBACK_MESSAGE_RECEIVED = 7;
        private static final int CALLBACK_MESSAGE_SEND_FAIL = 6;
        private static final int CALLBACK_MESSAGE_SEND_SUCCESS = 5;
        private static final int CALLBACK_SESSION_CONFIG_FAIL = 2;
        private static final int CALLBACK_SESSION_CONFIG_SUCCESS = 1;
        private static final int CALLBACK_SESSION_STARTED = 0;
        private static final int CALLBACK_SESSION_TERMINATED = 3;
        private static final String MESSAGE_BUNDLE_KEY_MESSAGE = "message";
        private static final String MESSAGE_BUNDLE_KEY_MESSAGE2 = "message2";
        private final WeakReference<WifiAwareManager> mAwareManager;
        private final int mClientId;
        private final Handler mHandler;
        private final boolean mIsPublish;
        private final DiscoverySessionCallback mOriginalCallback;
        private DiscoverySession mSession;

        WifiAwareDiscoverySessionCallbackProxy(WifiAwareManager mgr, Looper looper, boolean isPublish, DiscoverySessionCallback originalCallback, int clientId) {
            this.mAwareManager = new WeakReference(mgr);
            this.mIsPublish = isPublish;
            this.mOriginalCallback = originalCallback;
            this.mClientId = clientId;
            this.mHandler = new Handler(looper) {
                public void handleMessage(Message msg) {
                    Object obj = WifiAwareDiscoverySessionCallbackProxy.this.mAwareManager.get();
                    String str = WifiAwareManager.TAG;
                    if (obj == null) {
                        Log.w(str, "WifiAwareDiscoverySessionCallbackProxy: handleMessage post GC");
                        return;
                    }
                    switch (msg.what) {
                        case 0:
                            WifiAwareDiscoverySessionCallbackProxy.this.onProxySessionStarted(msg.arg1);
                            break;
                        case 1:
                            WifiAwareDiscoverySessionCallbackProxy.this.mOriginalCallback.onSessionConfigUpdated();
                            break;
                        case 2:
                            WifiAwareDiscoverySessionCallbackProxy.this.mOriginalCallback.onSessionConfigFailed();
                            if (WifiAwareDiscoverySessionCallbackProxy.this.mSession == null) {
                                WifiAwareDiscoverySessionCallbackProxy.this.mAwareManager.clear();
                                break;
                            }
                            break;
                        case 3:
                            WifiAwareDiscoverySessionCallbackProxy.this.onProxySessionTerminated(msg.arg1);
                            break;
                        case 4:
                        case 8:
                            List<byte[]> matchFilter;
                            byte[] arg = msg.getData().getByteArray(WifiAwareDiscoverySessionCallbackProxy.MESSAGE_BUNDLE_KEY_MESSAGE2);
                            try {
                                str = new TlvIterable(0, 1, arg).toList();
                                matchFilter = str;
                            } catch (BufferOverflowException e) {
                                matchFilter = null;
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("onServiceDiscovered: invalid match filter byte array '");
                                stringBuilder.append(new String(HexEncoding.encode(arg)));
                                stringBuilder.append("' - cannot be parsed: e=");
                                stringBuilder.append(e);
                                Log.e(str, stringBuilder.toString());
                            }
                            String str2 = "message";
                            if (msg.what != 4) {
                                WifiAwareDiscoverySessionCallbackProxy.this.mOriginalCallback.onServiceDiscoveredWithinRange(new PeerHandle(msg.arg1), msg.getData().getByteArray(str2), matchFilter, msg.arg2);
                                break;
                            } else {
                                WifiAwareDiscoverySessionCallbackProxy.this.mOriginalCallback.onServiceDiscovered(new PeerHandle(msg.arg1), msg.getData().getByteArray(str2), matchFilter);
                                break;
                            }
                        case 5:
                            WifiAwareDiscoverySessionCallbackProxy.this.mOriginalCallback.onMessageSendSucceeded(msg.arg1);
                            break;
                        case 6:
                            WifiAwareDiscoverySessionCallbackProxy.this.mOriginalCallback.onMessageSendFailed(msg.arg1);
                            break;
                        case 7:
                            WifiAwareDiscoverySessionCallbackProxy.this.mOriginalCallback.onMessageReceived(new PeerHandle(msg.arg1), (byte[]) msg.obj);
                            break;
                    }
                }
            };
        }

        public void onSessionStarted(int sessionId) {
            Message msg = this.mHandler.obtainMessage(0);
            msg.arg1 = sessionId;
            this.mHandler.sendMessage(msg);
        }

        public void onSessionConfigSuccess() {
            this.mHandler.sendMessage(this.mHandler.obtainMessage(1));
        }

        public void onSessionConfigFail(int reason) {
            Message msg = this.mHandler.obtainMessage(2);
            msg.arg1 = reason;
            this.mHandler.sendMessage(msg);
        }

        public void onSessionTerminated(int reason) {
            Message msg = this.mHandler.obtainMessage(3);
            msg.arg1 = reason;
            this.mHandler.sendMessage(msg);
        }

        private void onMatchCommon(int messageType, int peerId, byte[] serviceSpecificInfo, byte[] matchFilter, int distanceMm) {
            Bundle data = new Bundle();
            data.putByteArray("message", serviceSpecificInfo);
            data.putByteArray(MESSAGE_BUNDLE_KEY_MESSAGE2, matchFilter);
            Message msg = this.mHandler.obtainMessage(messageType);
            msg.arg1 = peerId;
            msg.arg2 = distanceMm;
            msg.setData(data);
            this.mHandler.sendMessage(msg);
        }

        public void onMatch(int peerId, byte[] serviceSpecificInfo, byte[] matchFilter) {
            onMatchCommon(4, peerId, serviceSpecificInfo, matchFilter, 0);
        }

        public void onMatchWithDistance(int peerId, byte[] serviceSpecificInfo, byte[] matchFilter, int distanceMm) {
            onMatchCommon(8, peerId, serviceSpecificInfo, matchFilter, distanceMm);
        }

        public void onMessageSendSuccess(int messageId) {
            Message msg = this.mHandler.obtainMessage(5);
            msg.arg1 = messageId;
            this.mHandler.sendMessage(msg);
        }

        public void onMessageSendFail(int messageId, int reason) {
            Message msg = this.mHandler.obtainMessage(6);
            msg.arg1 = messageId;
            msg.arg2 = reason;
            this.mHandler.sendMessage(msg);
        }

        public void onMessageReceived(int peerId, byte[] message) {
            Message msg = this.mHandler.obtainMessage(7);
            msg.arg1 = peerId;
            msg.obj = message;
            this.mHandler.sendMessage(msg);
        }

        public void onProxySessionStarted(int sessionId) {
            DiscoverySession discoverySession = this.mSession;
            String str = WifiAwareManager.TAG;
            if (discoverySession == null) {
                WifiAwareManager mgr = (WifiAwareManager) this.mAwareManager.get();
                if (mgr == null) {
                    Log.w(str, "onProxySessionStarted: mgr GC'd");
                    return;
                }
                if (this.mIsPublish) {
                    PublishDiscoverySession session = new PublishDiscoverySession(mgr, this.mClientId, sessionId);
                    this.mSession = session;
                    this.mOriginalCallback.onPublishStarted(session);
                } else {
                    SubscribeDiscoverySession session2 = new SubscribeDiscoverySession(mgr, this.mClientId, sessionId);
                    this.mSession = session2;
                    this.mOriginalCallback.onSubscribeStarted(session2);
                }
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            String str2 = "onSessionStarted: sessionId=";
            stringBuilder.append(str2);
            stringBuilder.append(sessionId);
            String str3 = ": session already created!?";
            stringBuilder.append(str3);
            Log.e(str, stringBuilder.toString());
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str2);
            stringBuilder2.append(sessionId);
            stringBuilder2.append(str3);
            throw new IllegalStateException(stringBuilder2.toString());
        }

        public void onProxySessionTerminated(int reason) {
            DiscoverySession discoverySession = this.mSession;
            if (discoverySession != null) {
                discoverySession.setTerminated();
                this.mSession = null;
            } else {
                Log.w(WifiAwareManager.TAG, "Proxy: onSessionTerminated called but mSession is null!?");
            }
            this.mAwareManager.clear();
            this.mOriginalCallback.onSessionTerminated();
        }
    }

    private static class WifiAwareEventCallbackProxy extends IWifiAwareEventCallback.Stub {
        private static final int CALLBACK_CONNECT_FAIL = 1;
        private static final int CALLBACK_CONNECT_SUCCESS = 0;
        private static final int CALLBACK_IDENTITY_CHANGED = 2;
        private final WeakReference<WifiAwareManager> mAwareManager;
        private final Binder mBinder;
        private final Handler mHandler;
        private final Looper mLooper;

        WifiAwareEventCallbackProxy(WifiAwareManager mgr, Looper looper, Binder binder, final AttachCallback attachCallback, final IdentityChangedListener identityChangedListener) {
            this.mAwareManager = new WeakReference(mgr);
            this.mLooper = looper;
            this.mBinder = binder;
            this.mHandler = new Handler(looper) {
                public void handleMessage(Message msg) {
                    WifiAwareManager mgr = (WifiAwareManager) WifiAwareEventCallbackProxy.this.mAwareManager.get();
                    String str = WifiAwareManager.TAG;
                    if (mgr == null) {
                        Log.w(str, "WifiAwareEventCallbackProxy: handleMessage post GC");
                        return;
                    }
                    int i = msg.what;
                    if (i == 0) {
                        attachCallback.onAttached(new WifiAwareSession(mgr, WifiAwareEventCallbackProxy.this.mBinder, msg.arg1));
                    } else if (i == 1) {
                        WifiAwareEventCallbackProxy.this.mAwareManager.clear();
                        attachCallback.onAttachFailed();
                    } else if (i == 2) {
                        IdentityChangedListener identityChangedListener = identityChangedListener;
                        if (identityChangedListener == null) {
                            Log.e(str, "CALLBACK_IDENTITY_CHANGED: null listener.");
                        } else {
                            identityChangedListener.onIdentityChanged((byte[]) msg.obj);
                        }
                    }
                }
            };
        }

        public void onConnectSuccess(int clientId) {
            Message msg = this.mHandler.obtainMessage(0);
            msg.arg1 = clientId;
            this.mHandler.sendMessage(msg);
        }

        public void onConnectFail(int reason) {
            Message msg = this.mHandler.obtainMessage(1);
            msg.arg1 = reason;
            this.mHandler.sendMessage(msg);
        }

        public void onIdentityChanged(byte[] mac) {
            Message msg = this.mHandler.obtainMessage(2);
            msg.obj = mac;
            this.mHandler.sendMessage(msg);
        }
    }

    public WifiAwareManager(Context context, IWifiAwareManager service) {
        this.mContext = context;
        this.mService = service;
    }

    public boolean isAvailable() {
        try {
            return this.mService.isUsageEnabled();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public Characteristics getCharacteristics() {
        try {
            return this.mService.getCharacteristics();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void attach(AttachCallback attachCallback, Handler handler) {
        attach(handler, null, attachCallback, null);
    }

    public void attach(AttachCallback attachCallback, IdentityChangedListener identityChangedListener, Handler handler) {
        attach(handler, null, attachCallback, identityChangedListener);
    }

    public void attach(Handler handler, ConfigRequest configRequest, AttachCallback attachCallback, IdentityChangedListener identityChangedListener) {
        if (attachCallback != null) {
            synchronized (this.mLock) {
                Looper looper = handler == null ? Looper.getMainLooper() : handler.getLooper();
                try {
                    Binder binder = new Binder();
                    this.mService.connect(binder, this.mContext.getOpPackageName(), new WifiAwareEventCallbackProxy(this, looper, binder, attachCallback, identityChangedListener), configRequest, identityChangedListener != null);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
            return;
        }
        throw new IllegalArgumentException("Null callback provided");
    }

    public void disconnect(int clientId, Binder binder) {
        try {
            this.mService.disconnect(clientId, binder);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void publish(int clientId, Looper looper, PublishConfig publishConfig, DiscoverySessionCallback callback) {
        if (callback != null) {
            try {
                this.mService.publish(this.mContext.getOpPackageName(), clientId, publishConfig, new WifiAwareDiscoverySessionCallbackProxy(this, looper, true, callback, clientId));
                return;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        throw new IllegalArgumentException("Null callback provided");
    }

    public void updatePublish(int clientId, int sessionId, PublishConfig publishConfig) {
        try {
            this.mService.updatePublish(clientId, sessionId, publishConfig);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void subscribe(int clientId, Looper looper, SubscribeConfig subscribeConfig, DiscoverySessionCallback callback) {
        if (callback != null) {
            try {
                this.mService.subscribe(this.mContext.getOpPackageName(), clientId, subscribeConfig, new WifiAwareDiscoverySessionCallbackProxy(this, looper, false, callback, clientId));
                return;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        throw new IllegalArgumentException("Null callback provided");
    }

    public void updateSubscribe(int clientId, int sessionId, SubscribeConfig subscribeConfig) {
        try {
            this.mService.updateSubscribe(clientId, sessionId, subscribeConfig);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void terminateSession(int clientId, int sessionId) {
        try {
            this.mService.terminateSession(clientId, sessionId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void sendMessage(int clientId, int sessionId, PeerHandle peerHandle, byte[] message, int messageId, int retryCount) {
        if (peerHandle != null) {
            try {
                this.mService.sendMessage(clientId, sessionId, peerHandle.peerId, message, messageId, retryCount);
                return;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        throw new IllegalArgumentException("sendMessage: invalid peerHandle - must be non-null");
    }

    public NetworkSpecifier createNetworkSpecifier(int clientId, int role, int sessionId, PeerHandle peerHandle, byte[] pmk, String passphrase) {
        int i = role;
        PeerHandle peerHandle2 = peerHandle;
        if (!WifiAwareUtils.isLegacyVersion(this.mContext, 29)) {
            throw new UnsupportedOperationException("API deprecated - use WifiAwareNetworkSpecifier.Builder");
        } else if (i != 0 && i != 1) {
            throw new IllegalArgumentException("createNetworkSpecifier: Invalid 'role' argument when creating a network specifier");
        } else if ((i == 0 || !WifiAwareUtils.isLegacyVersion(this.mContext, 28)) && peerHandle2 == null) {
            throw new IllegalArgumentException("createNetworkSpecifier: Invalid peer handle - cannot be null");
        } else {
            int i2;
            if (peerHandle2 == null) {
                i2 = 1;
            } else {
                i2 = 0;
            }
            return new WifiAwareNetworkSpecifier(i2, role, clientId, sessionId, peerHandle2 != null ? peerHandle2.peerId : 0, null, pmk, passphrase, 0, -1, Process.myUid());
        }
    }

    /* JADX WARNING: Missing block: B:8:0x001e, code skipped:
            if (android.net.wifi.aware.WifiAwareUtils.isLegacyVersion(r16.mContext, 28) == false) goto L_0x0023;
     */
    public android.net.NetworkSpecifier createNetworkSpecifier(int r17, int r18, byte[] r19, byte[] r20, java.lang.String r21) {
        /*
        r16 = this;
        r12 = r18;
        r13 = r19;
        if (r12 == 0) goto L_0x0012;
    L_0x0006:
        r0 = 1;
        if (r12 != r0) goto L_0x000a;
    L_0x0009:
        goto L_0x0012;
    L_0x000a:
        r0 = new java.lang.IllegalArgumentException;
        r1 = "createNetworkSpecifier: Invalid 'role' argument when creating a network specifier";
        r0.<init>(r1);
        throw r0;
    L_0x0012:
        if (r12 == 0) goto L_0x0021;
    L_0x0014:
        r14 = r16;
        r0 = r14.mContext;
        r1 = 28;
        r0 = android.net.wifi.aware.WifiAwareUtils.isLegacyVersion(r0, r1);
        if (r0 != 0) goto L_0x0025;
    L_0x0020:
        goto L_0x0023;
    L_0x0021:
        r14 = r16;
    L_0x0023:
        if (r13 == 0) goto L_0x0054;
    L_0x0025:
        if (r13 == 0) goto L_0x0034;
    L_0x0027:
        r0 = r13.length;
        r1 = 6;
        if (r0 != r1) goto L_0x002c;
    L_0x002b:
        goto L_0x0034;
    L_0x002c:
        r0 = new java.lang.IllegalArgumentException;
        r1 = "createNetworkSpecifier: Invalid peer MAC address";
        r0.<init>(r1);
        throw r0;
    L_0x0034:
        r15 = new android.net.wifi.aware.WifiAwareNetworkSpecifier;
        if (r13 != 0) goto L_0x003b;
    L_0x0038:
        r0 = 3;
        r1 = r0;
        goto L_0x003d;
    L_0x003b:
        r0 = 2;
        r1 = r0;
    L_0x003d:
        r4 = 0;
        r5 = 0;
        r9 = 0;
        r10 = -1;
        r11 = android.os.Process.myUid();
        r0 = r15;
        r2 = r18;
        r3 = r17;
        r6 = r19;
        r7 = r20;
        r8 = r21;
        r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11);
        return r15;
    L_0x0054:
        r0 = new java.lang.IllegalArgumentException;
        r1 = "createNetworkSpecifier: Invalid peer MAC - cannot be null";
        r0.<init>(r1);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.aware.WifiAwareManager.createNetworkSpecifier(int, int, byte[], byte[], java.lang.String):android.net.NetworkSpecifier");
    }
}
