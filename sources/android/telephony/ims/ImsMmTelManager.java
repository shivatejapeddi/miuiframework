package android.telephony.ims;

import android.annotation.SystemApi;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.telephony.SubscriptionManager;
import android.telephony.ims.aidl.IImsCapabilityCallback;
import android.telephony.ims.aidl.IImsCapabilityCallback.Stub;
import android.telephony.ims.aidl.IImsRegistrationCallback;
import android.telephony.ims.feature.MmTelFeature.MmTelCapabilities;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.telephony.ITelephony;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

@SystemApi
public class ImsMmTelManager {
    private static final String TAG = "ImsMmTelManager";
    public static final int WIFI_MODE_CELLULAR_PREFERRED = 1;
    public static final int WIFI_MODE_WIFI_ONLY = 0;
    public static final int WIFI_MODE_WIFI_PREFERRED = 2;
    private int mSubId;

    public static class CapabilityCallback {
        private final CapabilityBinder mBinder = new CapabilityBinder(this);

        private static class CapabilityBinder extends Stub {
            private Executor mExecutor;
            private final CapabilityCallback mLocalCallback;

            CapabilityBinder(CapabilityCallback c) {
                this.mLocalCallback = c;
            }

            public void onCapabilitiesStatusChanged(int config) {
                if (this.mLocalCallback != null) {
                    Binder.withCleanCallingIdentity(new -$$Lambda$ImsMmTelManager$CapabilityCallback$CapabilityBinder$gK2iK9ZQ3GDeuMTfhRd7yjiYlO8(this, config));
                }
            }

            public /* synthetic */ void lambda$onCapabilitiesStatusChanged$0$ImsMmTelManager$CapabilityCallback$CapabilityBinder(int config) {
                this.mLocalCallback.onCapabilitiesStatusChanged(new MmTelCapabilities(config));
            }

            public /* synthetic */ void lambda$onCapabilitiesStatusChanged$1$ImsMmTelManager$CapabilityCallback$CapabilityBinder(int config) throws Exception {
                this.mExecutor.execute(new -$$Lambda$ImsMmTelManager$CapabilityCallback$CapabilityBinder$4YNlUy9HsD02E7Sbv2VeVtbao08(this, config));
            }

            public void onQueryCapabilityConfiguration(int capability, int radioTech, boolean isEnabled) {
            }

            public void onChangeCapabilityConfigurationError(int capability, int radioTech, int reason) {
            }

            private void setExecutor(Executor executor) {
                this.mExecutor = executor;
            }
        }

        public void onCapabilitiesStatusChanged(MmTelCapabilities capabilities) {
        }

        public final IImsCapabilityCallback getBinder() {
            return this.mBinder;
        }

        public final void setExecutor(Executor executor) {
            this.mBinder.setExecutor(executor);
        }
    }

    public static class RegistrationCallback {
        private final RegistrationBinder mBinder = new RegistrationBinder(this);

        private static class RegistrationBinder extends IImsRegistrationCallback.Stub {
            private static final Map<Integer, Integer> IMS_REG_TO_ACCESS_TYPE_MAP = new HashMap<Integer, Integer>() {
                {
                    Integer valueOf = Integer.valueOf(-1);
                    put(valueOf, valueOf);
                    valueOf = Integer.valueOf(0);
                    Integer valueOf2 = Integer.valueOf(1);
                    put(valueOf, valueOf2);
                    put(valueOf2, Integer.valueOf(2));
                }
            };
            private Executor mExecutor;
            private final RegistrationCallback mLocalCallback;

            RegistrationBinder(RegistrationCallback localCallback) {
                this.mLocalCallback = localCallback;
            }

            public void onRegistered(int imsRadioTech) {
                if (this.mLocalCallback != null) {
                    Binder.withCleanCallingIdentity(new -$$Lambda$ImsMmTelManager$RegistrationCallback$RegistrationBinder$8xq93ap6i0L56Aegaj-ZEUt9ISc(this, imsRadioTech));
                }
            }

            public /* synthetic */ void lambda$onRegistered$1$ImsMmTelManager$RegistrationCallback$RegistrationBinder(int imsRadioTech) throws Exception {
                this.mExecutor.execute(new -$$Lambda$ImsMmTelManager$RegistrationCallback$RegistrationBinder$oDp7ilyKfflFThUCP4Du9EYoDoQ(this, imsRadioTech));
            }

            public /* synthetic */ void lambda$onRegistered$0$ImsMmTelManager$RegistrationCallback$RegistrationBinder(int imsRadioTech) {
                this.mLocalCallback.onRegistered(getAccessType(imsRadioTech));
            }

            public void onRegistering(int imsRadioTech) {
                if (this.mLocalCallback != null) {
                    Binder.withCleanCallingIdentity(new -$$Lambda$ImsMmTelManager$RegistrationCallback$RegistrationBinder$iuI3HyNU5eUABA_QRyzQ8Jw2-8g(this, imsRadioTech));
                }
            }

            public /* synthetic */ void lambda$onRegistering$3$ImsMmTelManager$RegistrationCallback$RegistrationBinder(int imsRadioTech) throws Exception {
                this.mExecutor.execute(new -$$Lambda$ImsMmTelManager$RegistrationCallback$RegistrationBinder$J4VhgcUtd6SivHcdkzpurbTuyLc(this, imsRadioTech));
            }

            public /* synthetic */ void lambda$onRegistering$2$ImsMmTelManager$RegistrationCallback$RegistrationBinder(int imsRadioTech) {
                this.mLocalCallback.onRegistering(getAccessType(imsRadioTech));
            }

            public void onDeregistered(ImsReasonInfo info) {
                if (this.mLocalCallback != null) {
                    Binder.withCleanCallingIdentity(new -$$Lambda$ImsMmTelManager$RegistrationCallback$RegistrationBinder$F58PRHsH__07pmyvC0NTRprfEPU(this, info));
                }
            }

            public /* synthetic */ void lambda$onDeregistered$4$ImsMmTelManager$RegistrationCallback$RegistrationBinder(ImsReasonInfo info) {
                this.mLocalCallback.onUnregistered(info);
            }

            public /* synthetic */ void lambda$onDeregistered$5$ImsMmTelManager$RegistrationCallback$RegistrationBinder(ImsReasonInfo info) throws Exception {
                this.mExecutor.execute(new -$$Lambda$ImsMmTelManager$RegistrationCallback$RegistrationBinder$q0Uz23ATIYan5EBJYUigIVvwE3g(this, info));
            }

            public void onTechnologyChangeFailed(int imsRadioTech, ImsReasonInfo info) {
                if (this.mLocalCallback != null) {
                    Binder.withCleanCallingIdentity(new -$$Lambda$ImsMmTelManager$RegistrationCallback$RegistrationBinder$Nztp9t3A8T2T3SbLvxLZoInLgWY(this, imsRadioTech, info));
                }
            }

            public /* synthetic */ void lambda$onTechnologyChangeFailed$6$ImsMmTelManager$RegistrationCallback$RegistrationBinder(int imsRadioTech, ImsReasonInfo info) {
                this.mLocalCallback.onTechnologyChangeFailed(getAccessType(imsRadioTech), info);
            }

            public /* synthetic */ void lambda$onTechnologyChangeFailed$7$ImsMmTelManager$RegistrationCallback$RegistrationBinder(int imsRadioTech, ImsReasonInfo info) throws Exception {
                this.mExecutor.execute(new -$$Lambda$ImsMmTelManager$RegistrationCallback$RegistrationBinder$IeNlpXTAPM2z2VxFA81E0v9udZw(this, imsRadioTech, info));
            }

            public void onSubscriberAssociatedUriChanged(Uri[] uris) {
                if (this.mLocalCallback != null) {
                    Binder.withCleanCallingIdentity(new -$$Lambda$ImsMmTelManager$RegistrationCallback$RegistrationBinder$AhnK6VJjwgpDNC1GXRrwfgtYvkM(this, uris));
                }
            }

            public /* synthetic */ void lambda$onSubscriberAssociatedUriChanged$9$ImsMmTelManager$RegistrationCallback$RegistrationBinder(Uri[] uris) throws Exception {
                this.mExecutor.execute(new -$$Lambda$ImsMmTelManager$RegistrationCallback$RegistrationBinder$jAP4lCkBQEdyrlgt5jaNPTlFXlY(this, uris));
            }

            public /* synthetic */ void lambda$onSubscriberAssociatedUriChanged$8$ImsMmTelManager$RegistrationCallback$RegistrationBinder(Uri[] uris) {
                this.mLocalCallback.onSubscriberAssociatedUriChanged(uris);
            }

            private void setExecutor(Executor executor) {
                this.mExecutor = executor;
            }

            private static int getAccessType(int regType) {
                if (IMS_REG_TO_ACCESS_TYPE_MAP.containsKey(Integer.valueOf(regType))) {
                    return ((Integer) IMS_REG_TO_ACCESS_TYPE_MAP.get(Integer.valueOf(regType))).intValue();
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("RegistrationBinder - invalid regType returned: ");
                stringBuilder.append(regType);
                Log.w(ImsMmTelManager.TAG, stringBuilder.toString());
                return -1;
            }
        }

        public void onRegistered(int imsTransportType) {
        }

        public void onRegistering(int imsTransportType) {
        }

        public void onUnregistered(ImsReasonInfo info) {
        }

        public void onTechnologyChangeFailed(int imsTransportType, ImsReasonInfo info) {
        }

        public void onSubscriberAssociatedUriChanged(Uri[] uris) {
        }

        public final IImsRegistrationCallback getBinder() {
            return this.mBinder;
        }

        public void setExecutor(Executor executor) {
            this.mBinder.setExecutor(executor);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface WiFiCallingMode {
    }

    public static ImsMmTelManager createForSubscriptionId(int subId) {
        if (SubscriptionManager.isValidSubscriptionId(subId)) {
            return new ImsMmTelManager(subId);
        }
        throw new IllegalArgumentException("Invalid subscription ID");
    }

    @VisibleForTesting
    public ImsMmTelManager(int subId) {
        this.mSubId = subId;
    }

    public void registerImsRegistrationCallback(Executor executor, RegistrationCallback c) throws ImsException {
        if (c == null) {
            throw new IllegalArgumentException("Must include a non-null RegistrationCallback.");
        } else if (executor == null) {
            throw new IllegalArgumentException("Must include a non-null Executor.");
        } else if (isImsAvailableOnDevice()) {
            c.setExecutor(executor);
            try {
                getITelephony().registerImsRegistrationCallback(this.mSubId, c.getBinder());
            } catch (RemoteException | IllegalStateException e) {
                throw new ImsException(e.getMessage(), 1);
            }
        } else {
            throw new ImsException("IMS not available on device.", 2);
        }
    }

    public void unregisterImsRegistrationCallback(RegistrationCallback c) {
        if (c != null) {
            try {
                getITelephony().unregisterImsRegistrationCallback(this.mSubId, c.getBinder());
                return;
            } catch (RemoteException e) {
                throw e.rethrowAsRuntimeException();
            }
        }
        throw new IllegalArgumentException("Must include a non-null RegistrationCallback.");
    }

    public void registerMmTelCapabilityCallback(Executor executor, CapabilityCallback c) throws ImsException {
        if (c == null) {
            throw new IllegalArgumentException("Must include a non-null RegistrationCallback.");
        } else if (executor == null) {
            throw new IllegalArgumentException("Must include a non-null Executor.");
        } else if (isImsAvailableOnDevice()) {
            c.setExecutor(executor);
            try {
                getITelephony().registerMmTelCapabilityCallback(this.mSubId, c.getBinder());
            } catch (RemoteException e) {
                throw e.rethrowAsRuntimeException();
            } catch (IllegalStateException e2) {
                throw new ImsException(e2.getMessage(), 1);
            }
        } else {
            throw new ImsException("IMS not available on device.", 2);
        }
    }

    public void unregisterMmTelCapabilityCallback(CapabilityCallback c) {
        if (c != null) {
            try {
                getITelephony().unregisterMmTelCapabilityCallback(this.mSubId, c.getBinder());
                return;
            } catch (RemoteException e) {
                throw e.rethrowAsRuntimeException();
            }
        }
        throw new IllegalArgumentException("Must include a non-null RegistrationCallback.");
    }

    public boolean isAdvancedCallingSettingEnabled() {
        try {
            return getITelephony().isAdvancedCallingSettingEnabled(this.mSubId);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public void setAdvancedCallingSettingEnabled(boolean isEnabled) {
        try {
            getITelephony().setAdvancedCallingSettingEnabled(this.mSubId, isEnabled);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public boolean isCapable(int capability, int imsRegTech) {
        try {
            return getITelephony().isCapable(this.mSubId, capability, imsRegTech);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public boolean isAvailable(int capability, int imsRegTech) {
        try {
            return getITelephony().isAvailable(this.mSubId, capability, imsRegTech);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public boolean isVtSettingEnabled() {
        try {
            return getITelephony().isVtSettingEnabled(this.mSubId);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public void setVtSettingEnabled(boolean isEnabled) {
        try {
            getITelephony().setVtSettingEnabled(this.mSubId, isEnabled);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public boolean isVoWiFiSettingEnabled() {
        try {
            return getITelephony().isVoWiFiSettingEnabled(this.mSubId);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public void setVoWiFiSettingEnabled(boolean isEnabled) {
        try {
            getITelephony().setVoWiFiSettingEnabled(this.mSubId, isEnabled);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public boolean isVoWiFiRoamingSettingEnabled() {
        try {
            return getITelephony().isVoWiFiRoamingSettingEnabled(this.mSubId);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public void setVoWiFiRoamingSettingEnabled(boolean isEnabled) {
        try {
            getITelephony().setVoWiFiRoamingSettingEnabled(this.mSubId, isEnabled);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public void setVoWiFiNonPersistent(boolean isCapable, int mode) {
        try {
            getITelephony().setVoWiFiNonPersistent(this.mSubId, isCapable, mode);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public int getVoWiFiModeSetting() {
        try {
            return getITelephony().getVoWiFiModeSetting(this.mSubId);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public void setVoWiFiModeSetting(int mode) {
        try {
            getITelephony().setVoWiFiModeSetting(this.mSubId, mode);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public int getVoWiFiRoamingModeSetting() {
        try {
            return getITelephony().getVoWiFiRoamingModeSetting(this.mSubId);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public void setVoWiFiRoamingModeSetting(int mode) {
        try {
            getITelephony().setVoWiFiRoamingModeSetting(this.mSubId, mode);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public void setRttCapabilitySetting(boolean isEnabled) {
        try {
            getITelephony().setRttCapabilitySetting(this.mSubId, isEnabled);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isTtyOverVolteEnabled() {
        try {
            return getITelephony().isTtyOverVolteEnabled(this.mSubId);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    private static boolean isImsAvailableOnDevice() {
        IPackageManager pm = IPackageManager.Stub.asInterface(ServiceManager.getService("package"));
        boolean z = true;
        if (pm == null) {
            return true;
        }
        try {
            z = pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY_IMS, 0);
            return z;
        } catch (RemoteException e) {
            return z;
        }
    }

    private static ITelephony getITelephony() {
        ITelephony binder = ITelephony.Stub.asInterface(ServiceManager.getService("phone"));
        if (binder != null) {
            return binder;
        }
        throw new RuntimeException("Could not find Telephony Service.");
    }
}
