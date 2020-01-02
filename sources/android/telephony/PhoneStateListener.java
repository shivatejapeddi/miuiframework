package android.telephony;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Looper;
import android.telephony.emergency.EmergencyNumber;
import android.telephony.ims.ImsReasonInfo;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import com.android.internal.telephony.IPhoneStateListener;
import com.android.internal.telephony.IPhoneStateListener.Stub;
import dalvik.system.VMRuntime;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public class PhoneStateListener {
    private static final boolean DBG = false;
    public static final int LISTEN_ACTIVE_DATA_SUBSCRIPTION_ID_CHANGE = 4194304;
    @SystemApi
    public static final int LISTEN_CALL_ATTRIBUTES_CHANGED = 67108864;
    @SystemApi
    public static final int LISTEN_CALL_DISCONNECT_CAUSES = 33554432;
    public static final int LISTEN_CALL_FORWARDING_INDICATOR = 8;
    public static final int LISTEN_CALL_STATE = 32;
    public static final int LISTEN_CARRIER_NETWORK_CHANGE = 65536;
    public static final int LISTEN_CELL_INFO = 1024;
    public static final int LISTEN_CELL_LOCATION = 16;
    public static final int LISTEN_DATA_ACTIVATION_STATE = 262144;
    public static final int LISTEN_DATA_ACTIVITY = 128;
    @Deprecated
    public static final int LISTEN_DATA_CONNECTION_REAL_TIME_INFO = 8192;
    public static final int LISTEN_DATA_CONNECTION_STATE = 64;
    public static final int LISTEN_EMERGENCY_NUMBER_LIST = 16777216;
    @SystemApi
    public static final int LISTEN_IMS_CALL_DISCONNECT_CAUSES = 134217728;
    public static final int LISTEN_MESSAGE_WAITING_INDICATOR = 4;
    public static final int LISTEN_NONE = 0;
    @Deprecated
    public static final int LISTEN_OEM_HOOK_RAW_EVENT = 32768;
    public static final int LISTEN_OTASP_CHANGED = 512;
    public static final int LISTEN_PHONE_CAPABILITY_CHANGE = 2097152;
    public static final int LISTEN_PHYSICAL_CHANNEL_CONFIGURATION = 1048576;
    @SystemApi
    public static final int LISTEN_PRECISE_CALL_STATE = 2048;
    @SystemApi
    public static final int LISTEN_PRECISE_DATA_CONNECTION_STATE = 4096;
    @SystemApi
    public static final int LISTEN_RADIO_POWER_STATE_CHANGED = 8388608;
    public static final int LISTEN_SERVICE_STATE = 1;
    @Deprecated
    public static final int LISTEN_SIGNAL_STRENGTH = 2;
    public static final int LISTEN_SIGNAL_STRENGTHS = 256;
    @SystemApi
    public static final int LISTEN_SRVCC_STATE_CHANGED = 16384;
    public static final int LISTEN_USER_MOBILE_DATA_STATE = 524288;
    @SystemApi
    public static final int LISTEN_VOICE_ACTIVATION_STATE = 131072;
    private static final String LOG_TAG = "PhoneStateListener";
    @VisibleForTesting(visibility = Visibility.PACKAGE)
    @UnsupportedAppUsage
    public final IPhoneStateListener callback;
    @UnsupportedAppUsage
    protected Integer mSubId;

    private static class IPhoneStateListenerStub extends Stub {
        private Executor mExecutor;
        private WeakReference<PhoneStateListener> mPhoneStateListenerWeakRef;

        IPhoneStateListenerStub(PhoneStateListener phoneStateListener, Executor executor) {
            this.mPhoneStateListenerWeakRef = new WeakReference(phoneStateListener);
            this.mExecutor = executor;
        }

        public void onServiceStateChanged(ServiceState serviceState) {
            PhoneStateListener psl = (PhoneStateListener) this.mPhoneStateListenerWeakRef.get();
            if (psl != null) {
                Binder.withCleanCallingIdentity(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$uC5syhzl229gIpaK7Jfs__OCJxQ(this, psl, serviceState));
            }
        }

        public /* synthetic */ void lambda$onServiceStateChanged$1$PhoneStateListener$IPhoneStateListenerStub(PhoneStateListener psl, ServiceState serviceState) throws Exception {
            this.mExecutor.execute(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$nrGqSRBJrc3_EwotCDNwfKeizIo(psl, serviceState));
        }

        public void onSignalStrengthChanged(int asu) {
            PhoneStateListener psl = (PhoneStateListener) this.mPhoneStateListenerWeakRef.get();
            if (psl != null) {
                Binder.withCleanCallingIdentity(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$M39is_Zyt8D7Camw2NS4EGTDn-s(this, psl, asu));
            }
        }

        public /* synthetic */ void lambda$onSignalStrengthChanged$3$PhoneStateListener$IPhoneStateListenerStub(PhoneStateListener psl, int asu) throws Exception {
            this.mExecutor.execute(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$5J-sdvem6pUpdVwRdm8IbDhvuv8(psl, asu));
        }

        public void onMessageWaitingIndicatorChanged(boolean mwi) {
            PhoneStateListener psl = (PhoneStateListener) this.mPhoneStateListenerWeakRef.get();
            if (psl != null) {
                Binder.withCleanCallingIdentity(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$okPCYOx4UxYuvUHlM2iS425QGIg(this, psl, mwi));
            }
        }

        public /* synthetic */ void lambda$onMessageWaitingIndicatorChanged$5$PhoneStateListener$IPhoneStateListenerStub(PhoneStateListener psl, boolean mwi) throws Exception {
            this.mExecutor.execute(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$TqrkuLPlaG_ucU7VbLS4tnf8hG8(psl, mwi));
        }

        public void onCallForwardingIndicatorChanged(boolean cfi) {
            PhoneStateListener psl = (PhoneStateListener) this.mPhoneStateListenerWeakRef.get();
            if (psl != null) {
                Binder.withCleanCallingIdentity(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$1M3m0i6211i2YjWyTDT7l0bJm3I(this, psl, cfi));
            }
        }

        public /* synthetic */ void lambda$onCallForwardingIndicatorChanged$7$PhoneStateListener$IPhoneStateListenerStub(PhoneStateListener psl, boolean cfi) throws Exception {
            this.mExecutor.execute(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$WYWtWHdkZDxBd9anjoxyZozPWHc(psl, cfi));
        }

        public void onCellLocationChanged(Bundle bundle) {
            CellLocation location = CellLocation.newFromBundle(bundle);
            PhoneStateListener psl = (PhoneStateListener) this.mPhoneStateListenerWeakRef.get();
            if (psl != null) {
                Binder.withCleanCallingIdentity(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$Hbn6-eZxY2p3rjOfStodI04A8E8(this, psl, location));
            }
        }

        public /* synthetic */ void lambda$onCellLocationChanged$9$PhoneStateListener$IPhoneStateListenerStub(PhoneStateListener psl, CellLocation location) throws Exception {
            this.mExecutor.execute(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$2cMrwdqnKBpixpApeIX38rmRLak(psl, location));
        }

        public void onCallStateChanged(int state, String incomingNumber) {
            PhoneStateListener psl = (PhoneStateListener) this.mPhoneStateListenerWeakRef.get();
            if (psl != null) {
                Binder.withCleanCallingIdentity(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$oDAZqs8paeefe_3k_uRKV5plQW4(this, psl, state, incomingNumber));
            }
        }

        public /* synthetic */ void lambda$onCallStateChanged$11$PhoneStateListener$IPhoneStateListenerStub(PhoneStateListener psl, int state, String incomingNumber) throws Exception {
            this.mExecutor.execute(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$6czWSGzxct0CXPVO54T0aq05qls(psl, state, incomingNumber));
        }

        public void onDataConnectionStateChanged(int state, int networkType) {
            PhoneStateListener psl = (PhoneStateListener) this.mPhoneStateListenerWeakRef.get();
            if (psl != null) {
                Binder.withCleanCallingIdentity(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$2VMO21pFQN-JN3kpn6vQN1zPFEU(this, psl, state, networkType));
            }
        }

        public /* synthetic */ void lambda$onDataConnectionStateChanged$13$PhoneStateListener$IPhoneStateListenerStub(PhoneStateListener psl, int state, int networkType) throws Exception {
            this.mExecutor.execute(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$dUc3j82sK9P9Zpaq-91n9bk_Rpc(psl, state, networkType));
        }

        static /* synthetic */ void lambda$onDataConnectionStateChanged$12(PhoneStateListener psl, int state, int networkType) {
            psl.onDataConnectionStateChanged(state, networkType);
            psl.onDataConnectionStateChanged(state);
        }

        public void onDataActivity(int direction) {
            PhoneStateListener psl = (PhoneStateListener) this.mPhoneStateListenerWeakRef.get();
            if (psl != null) {
                Binder.withCleanCallingIdentity(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$XyayAGWQZC2dNjwr697SfSGBBOc(this, psl, direction));
            }
        }

        public /* synthetic */ void lambda$onDataActivity$15$PhoneStateListener$IPhoneStateListenerStub(PhoneStateListener psl, int direction) throws Exception {
            this.mExecutor.execute(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$JalixlMNdjktPsNntP_JT9pymhs(psl, direction));
        }

        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            PhoneStateListener psl = (PhoneStateListener) this.mPhoneStateListenerWeakRef.get();
            if (psl != null) {
                Binder.withCleanCallingIdentity(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$aysbwPqxcLV_5w6LP0TzZu2D-ew(this, psl, signalStrength));
            }
        }

        public /* synthetic */ void lambda$onSignalStrengthsChanged$17$PhoneStateListener$IPhoneStateListenerStub(PhoneStateListener psl, SignalStrength signalStrength) throws Exception {
            this.mExecutor.execute(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$0s34qsuHFsa43jUHrTkD62ni6Ds(psl, signalStrength));
        }

        public void onOtaspChanged(int otaspMode) {
            PhoneStateListener psl = (PhoneStateListener) this.mPhoneStateListenerWeakRef.get();
            if (psl != null) {
                Binder.withCleanCallingIdentity(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$i4r8mBqOfCy4bnbF_JG7ujDXEOQ(this, psl, otaspMode));
            }
        }

        public /* synthetic */ void lambda$onOtaspChanged$19$PhoneStateListener$IPhoneStateListenerStub(PhoneStateListener psl, int otaspMode) throws Exception {
            this.mExecutor.execute(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$H1CbxYUcdxs1WggP_RRULTY01K8(psl, otaspMode));
        }

        public void onCellInfoChanged(List<CellInfo> cellInfo) {
            PhoneStateListener psl = (PhoneStateListener) this.mPhoneStateListenerWeakRef.get();
            if (psl != null) {
                Binder.withCleanCallingIdentity(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$yvQnAlFGg5EWDG2vcA9X-4xnalA(this, psl, cellInfo));
            }
        }

        public /* synthetic */ void lambda$onCellInfoChanged$21$PhoneStateListener$IPhoneStateListenerStub(PhoneStateListener psl, List cellInfo) throws Exception {
            this.mExecutor.execute(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$Q2A8FgYlU8_D6PD78tThGut_rTc(psl, cellInfo));
        }

        public void onPreciseCallStateChanged(PreciseCallState callState) {
            PhoneStateListener psl = (PhoneStateListener) this.mPhoneStateListenerWeakRef.get();
            if (psl != null) {
                Binder.withCleanCallingIdentity(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$bELzxgwsPigyVKYkAXBO2BjcSm8(this, psl, callState));
            }
        }

        public /* synthetic */ void lambda$onPreciseCallStateChanged$23$PhoneStateListener$IPhoneStateListenerStub(PhoneStateListener psl, PreciseCallState callState) throws Exception {
            this.mExecutor.execute(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$4NHt5Shg_DHV-T1IxfcQLHP5-j0(psl, callState));
        }

        public void onCallDisconnectCauseChanged(int disconnectCause, int preciseDisconnectCause) {
            PhoneStateListener psl = (PhoneStateListener) this.mPhoneStateListenerWeakRef.get();
            if (psl != null) {
                Binder.withCleanCallingIdentity(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$icX71zgNszuMfnDaCmahcqWacFM(this, psl, disconnectCause, preciseDisconnectCause));
            }
        }

        public /* synthetic */ void lambda$onCallDisconnectCauseChanged$25$PhoneStateListener$IPhoneStateListenerStub(PhoneStateListener psl, int disconnectCause, int preciseDisconnectCause) throws Exception {
            this.mExecutor.execute(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$hxq77a5O_MUfoptHg15ipzFvMkI(psl, disconnectCause, preciseDisconnectCause));
        }

        public void onPreciseDataConnectionStateChanged(PreciseDataConnectionState dataConnectionState) {
            PhoneStateListener psl = (PhoneStateListener) this.mPhoneStateListenerWeakRef.get();
            if (psl != null) {
                Binder.withCleanCallingIdentity(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$RC2x2ijetA-pQrLa4QakzMBjh_k(this, psl, dataConnectionState));
            }
        }

        public /* synthetic */ void lambda$onPreciseDataConnectionStateChanged$27$PhoneStateListener$IPhoneStateListenerStub(PhoneStateListener psl, PreciseDataConnectionState dataConnectionState) throws Exception {
            this.mExecutor.execute(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$HEcWn-J1WRb0wLERu2qoMIZDfjY(psl, dataConnectionState));
        }

        public void onDataConnectionRealTimeInfoChanged(DataConnectionRealTimeInfo dcRtInfo) {
            PhoneStateListener psl = (PhoneStateListener) this.mPhoneStateListenerWeakRef.get();
            if (psl != null) {
                Binder.withCleanCallingIdentity(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$OfwFKKtcQHRmtv70FCopw6FDAAU(this, psl, dcRtInfo));
            }
        }

        public /* synthetic */ void lambda$onDataConnectionRealTimeInfoChanged$29$PhoneStateListener$IPhoneStateListenerStub(PhoneStateListener psl, DataConnectionRealTimeInfo dcRtInfo) throws Exception {
            this.mExecutor.execute(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$IU278K5QbmReF-mbpcNVAvVlhFI(psl, dcRtInfo));
        }

        public void onSrvccStateChanged(int state) {
            PhoneStateListener psl = (PhoneStateListener) this.mPhoneStateListenerWeakRef.get();
            if (psl != null) {
                Binder.withCleanCallingIdentity(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$nR7W5ox6SCgPxtH9IRcENwKeFI4(this, psl, state));
            }
        }

        public /* synthetic */ void lambda$onSrvccStateChanged$31$PhoneStateListener$IPhoneStateListenerStub(PhoneStateListener psl, int state) throws Exception {
            this.mExecutor.execute(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$ygzOWFRiY4sZQ4WYUPIefqgiGvM(psl, state));
        }

        public void onVoiceActivationStateChanged(int activationState) {
            PhoneStateListener psl = (PhoneStateListener) this.mPhoneStateListenerWeakRef.get();
            if (psl != null) {
                Binder.withCleanCallingIdentity(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$5rF2IFj8mrb7uZc0HMKiuCodUn0(this, psl, activationState));
            }
        }

        public /* synthetic */ void lambda$onVoiceActivationStateChanged$33$PhoneStateListener$IPhoneStateListenerStub(PhoneStateListener psl, int activationState) throws Exception {
            this.mExecutor.execute(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$y-tK7my_uXPo_oQ7AytfnekGEbU(psl, activationState));
        }

        public void onDataActivationStateChanged(int activationState) {
            PhoneStateListener psl = (PhoneStateListener) this.mPhoneStateListenerWeakRef.get();
            if (psl != null) {
                Binder.withCleanCallingIdentity(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$t2gWJ_jA36kAdNXSmlzw85aU-tM(this, psl, activationState));
            }
        }

        public /* synthetic */ void lambda$onDataActivationStateChanged$35$PhoneStateListener$IPhoneStateListenerStub(PhoneStateListener psl, int activationState) throws Exception {
            this.mExecutor.execute(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$W65ui1dCCc-JnQa7gon1I7Bz7Sk(psl, activationState));
        }

        public void onUserMobileDataStateChanged(boolean enabled) {
            PhoneStateListener psl = (PhoneStateListener) this.mPhoneStateListenerWeakRef.get();
            if (psl != null) {
                Binder.withCleanCallingIdentity(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$5uu-05j4ojTh9mEHkN-ynQqQRGM(this, psl, enabled));
            }
        }

        public /* synthetic */ void lambda$onUserMobileDataStateChanged$37$PhoneStateListener$IPhoneStateListenerStub(PhoneStateListener psl, boolean enabled) throws Exception {
            this.mExecutor.execute(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$5Uf5OZWCyPD0lZtySzbYw18FWhU(psl, enabled));
        }

        public void onOemHookRawEvent(byte[] rawData) {
            PhoneStateListener psl = (PhoneStateListener) this.mPhoneStateListenerWeakRef.get();
            if (psl != null) {
                Binder.withCleanCallingIdentity(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$jNtyZYh5ZAuvyDZA_6f30zhW_dI(this, psl, rawData));
            }
        }

        public /* synthetic */ void lambda$onOemHookRawEvent$39$PhoneStateListener$IPhoneStateListenerStub(PhoneStateListener psl, byte[] rawData) throws Exception {
            this.mExecutor.execute(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$jclAV5yU3RtV94suRvvhafvGuhw(psl, rawData));
        }

        public void onCarrierNetworkChange(boolean active) {
            PhoneStateListener psl = (PhoneStateListener) this.mPhoneStateListenerWeakRef.get();
            if (psl != null) {
                Binder.withCleanCallingIdentity(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$YY3srkIkMm8vTSFJZHoiKzUUrGs(this, psl, active));
            }
        }

        public /* synthetic */ void lambda$onCarrierNetworkChange$41$PhoneStateListener$IPhoneStateListenerStub(PhoneStateListener psl, boolean active) throws Exception {
            this.mExecutor.execute(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$jlNX9JiqGSNg9W49vDcKucKdeCI(psl, active));
        }

        public void onPhysicalChannelConfigurationChanged(List<PhysicalChannelConfig> configs) {
            PhoneStateListener psl = (PhoneStateListener) this.mPhoneStateListenerWeakRef.get();
            if (psl != null) {
                Binder.withCleanCallingIdentity(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$OIAjnTzp_YIf6Y7jPFABi9BXZvs(this, psl, configs));
            }
        }

        public /* synthetic */ void lambda$onPhysicalChannelConfigurationChanged$43$PhoneStateListener$IPhoneStateListenerStub(PhoneStateListener psl, List configs) throws Exception {
            this.mExecutor.execute(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$nMiL2eSbUjYsM-AZ8joz_n4dLz0(psl, configs));
        }

        public void onEmergencyNumberListChanged(Map emergencyNumberList) {
            PhoneStateListener psl = (PhoneStateListener) this.mPhoneStateListenerWeakRef.get();
            if (psl != null) {
                Binder.withCleanCallingIdentity(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$d9DVwzLraeX80tegF_wEzf_k2FI(this, psl, emergencyNumberList));
            }
        }

        public /* synthetic */ void lambda$onEmergencyNumberListChanged$45$PhoneStateListener$IPhoneStateListenerStub(PhoneStateListener psl, Map emergencyNumberList) throws Exception {
            this.mExecutor.execute(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$jGj-qFMdpjbsKaUErqJEeOALEGo(psl, emergencyNumberList));
        }

        public void onPhoneCapabilityChanged(PhoneCapability capability) {
            PhoneStateListener psl = (PhoneStateListener) this.mPhoneStateListenerWeakRef.get();
            if (psl != null) {
                Binder.withCleanCallingIdentity(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$-CiOzgf6ys4EwlCYOVUsuz9YQ5c(this, psl, capability));
            }
        }

        public /* synthetic */ void lambda$onPhoneCapabilityChanged$47$PhoneStateListener$IPhoneStateListenerStub(PhoneStateListener psl, PhoneCapability capability) throws Exception {
            this.mExecutor.execute(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$lHL69WZlO89JjNC1LLvFWp2OuKY(psl, capability));
        }

        public void onRadioPowerStateChanged(int state) {
            PhoneStateListener psl = (PhoneStateListener) this.mPhoneStateListenerWeakRef.get();
            if (psl != null) {
                Binder.withCleanCallingIdentity(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$TYOBpOfoS3xjFssrzOJyHTelndw(this, psl, state));
            }
        }

        public /* synthetic */ void lambda$onRadioPowerStateChanged$49$PhoneStateListener$IPhoneStateListenerStub(PhoneStateListener psl, int state) throws Exception {
            this.mExecutor.execute(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$bI97h5HT-IYvguXIcngwUrpGxrw(psl, state));
        }

        public void onCallAttributesChanged(CallAttributes callAttributes) {
            PhoneStateListener psl = (PhoneStateListener) this.mPhoneStateListenerWeakRef.get();
            if (psl != null) {
                Binder.withCleanCallingIdentity(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$Q_Cpm8aB8qYt8lGxD5PXek_-4bA(this, psl, callAttributes));
            }
        }

        public /* synthetic */ void lambda$onCallAttributesChanged$51$PhoneStateListener$IPhoneStateListenerStub(PhoneStateListener psl, CallAttributes callAttributes) throws Exception {
            this.mExecutor.execute(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$5t7yF_frkRH7MdItRlwmP00irsM(psl, callAttributes));
        }

        public void onActiveDataSubIdChanged(int subId) {
            PhoneStateListener psl = (PhoneStateListener) this.mPhoneStateListenerWeakRef.get();
            if (psl != null) {
                Binder.withCleanCallingIdentity(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$ipH9N0fJiGE9EBJHahQeXcCZXzo(this, psl, subId));
            }
        }

        public /* synthetic */ void lambda$onActiveDataSubIdChanged$53$PhoneStateListener$IPhoneStateListenerStub(PhoneStateListener psl, int subId) throws Exception {
            this.mExecutor.execute(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$nnG75RvQ1_1KZGJk1ySeCH1JJRg(psl, subId));
        }

        public void onImsCallDisconnectCauseChanged(ImsReasonInfo disconnectCause) {
            PhoneStateListener psl = (PhoneStateListener) this.mPhoneStateListenerWeakRef.get();
            if (psl != null) {
                Binder.withCleanCallingIdentity(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$Bzok3Q_pjLC0O4ulkDfbWru0v6w(this, psl, disconnectCause));
            }
        }

        public /* synthetic */ void lambda$onImsCallDisconnectCauseChanged$55$PhoneStateListener$IPhoneStateListenerStub(PhoneStateListener psl, ImsReasonInfo disconnectCause) throws Exception {
            this.mExecutor.execute(new -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$eYTgM6ABgThWqEatVha4ZuIpI0A(psl, disconnectCause));
        }
    }

    public Integer updateSubscription(Integer subId) {
        Integer old = this.mSubId;
        this.mSubId = subId;
        return old;
    }

    public PhoneStateListener() {
        this(null, Looper.myLooper());
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public PhoneStateListener(Looper looper) {
        this(null, looper);
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public PhoneStateListener(Integer subId) {
        this(subId, Looper.myLooper());
        if (subId != null && VMRuntime.getRuntime().getTargetSdkVersion() >= 29) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("PhoneStateListener with subId: ");
            stringBuilder.append(subId);
            stringBuilder.append(" is not supported, use default constructor");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public PhoneStateListener(Integer subId, Looper looper) {
        this(subId, new HandlerExecutor(new Handler(looper)));
        if (subId != null && VMRuntime.getRuntime().getTargetSdkVersion() >= 29) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("PhoneStateListener with subId: ");
            stringBuilder.append(subId);
            stringBuilder.append(" is not supported, use default constructor");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public PhoneStateListener(Executor executor) {
        this(null, executor);
    }

    private PhoneStateListener(Integer subId, Executor e) {
        if (e != null) {
            this.mSubId = subId;
            this.callback = new IPhoneStateListenerStub(this, e);
            return;
        }
        throw new IllegalArgumentException("PhoneStateListener Executor must be non-null");
    }

    public void onServiceStateChanged(ServiceState serviceState) {
    }

    @Deprecated
    public void onSignalStrengthChanged(int asu) {
    }

    public void onMessageWaitingIndicatorChanged(boolean mwi) {
    }

    public void onCallForwardingIndicatorChanged(boolean cfi) {
    }

    public void onCellLocationChanged(CellLocation location) {
    }

    public void onCallStateChanged(int state, String phoneNumber) {
    }

    public void onDataConnectionStateChanged(int state) {
    }

    public void onDataConnectionStateChanged(int state, int networkType) {
    }

    public void onDataActivity(int direction) {
    }

    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
    }

    @UnsupportedAppUsage
    public void onOtaspChanged(int otaspMode) {
    }

    public void onCellInfoChanged(List<CellInfo> list) {
    }

    @SystemApi
    public void onPreciseCallStateChanged(PreciseCallState callState) {
    }

    @SystemApi
    public void onCallDisconnectCauseChanged(int disconnectCause, int preciseDisconnectCause) {
    }

    @SystemApi
    public void onImsCallDisconnectCauseChanged(ImsReasonInfo imsReasonInfo) {
    }

    @SystemApi
    public void onPreciseDataConnectionStateChanged(PreciseDataConnectionState dataConnectionState) {
    }

    @UnsupportedAppUsage
    public void onDataConnectionRealTimeInfoChanged(DataConnectionRealTimeInfo dcRtInfo) {
    }

    @SystemApi
    public void onSrvccStateChanged(int srvccState) {
    }

    @SystemApi
    public void onVoiceActivationStateChanged(int state) {
    }

    public void onDataActivationStateChanged(int state) {
    }

    public void onUserMobileDataStateChanged(boolean enabled) {
    }

    public void onPhysicalChannelConfigurationChanged(List<PhysicalChannelConfig> list) {
    }

    public void onEmergencyNumberListChanged(Map<Integer, List<EmergencyNumber>> map) {
    }

    @UnsupportedAppUsage
    public void onOemHookRawEvent(byte[] rawData) {
    }

    public void onPhoneCapabilityChanged(PhoneCapability capability) {
    }

    public void onActiveDataSubscriptionIdChanged(int subId) {
    }

    @SystemApi
    public void onCallAttributesChanged(CallAttributes callAttributes) {
    }

    @SystemApi
    public void onRadioPowerStateChanged(int state) {
    }

    public void onCarrierNetworkChange(boolean active) {
    }

    private void log(String s) {
        Rlog.d(LOG_TAG, s);
    }

    public int getSubId() {
        return this.mSubId.intValue();
    }

    public void setSubId(int mSubId) {
        this.mSubId = Integer.valueOf(mSubId);
    }
}
