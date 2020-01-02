package com.android.ims.internal.uce;

import com.android.ims.internal.uce.common.UceLong;
import com.android.ims.internal.uce.options.IOptionsListener;
import com.android.ims.internal.uce.options.IOptionsService;
import com.android.ims.internal.uce.presence.IPresenceListener;
import com.android.ims.internal.uce.presence.IPresenceService;
import com.android.ims.internal.uce.uceservice.IUceListener;
import com.android.ims.internal.uce.uceservice.IUceService.Stub;

public abstract class UceServiceBase {
    private UceServiceBinder mBinder;

    private final class UceServiceBinder extends Stub {
        private UceServiceBinder() {
        }

        public boolean startService(IUceListener uceListener) {
            return UceServiceBase.this.onServiceStart(uceListener);
        }

        public boolean stopService() {
            return UceServiceBase.this.onStopService();
        }

        public boolean isServiceStarted() {
            return UceServiceBase.this.onIsServiceStarted();
        }

        public int createOptionsService(IOptionsListener optionsListener, UceLong optionsServiceListenerHdl) {
            return UceServiceBase.this.onCreateOptionsService(optionsListener, optionsServiceListenerHdl);
        }

        public int createOptionsServiceForSubscription(IOptionsListener optionsListener, UceLong optionsServiceListenerHdl, String iccId) {
            return UceServiceBase.this.onCreateOptionsService(optionsListener, optionsServiceListenerHdl, iccId);
        }

        public void destroyOptionsService(int optionsServiceHandle) {
            UceServiceBase.this.onDestroyOptionsService(optionsServiceHandle);
        }

        public int createPresenceService(IPresenceListener presServiceListener, UceLong presServiceListenerHdl) {
            return UceServiceBase.this.onCreatePresService(presServiceListener, presServiceListenerHdl);
        }

        public int createPresenceServiceForSubscription(IPresenceListener presServiceListener, UceLong presServiceListenerHdl, String iccId) {
            return UceServiceBase.this.onCreatePresService(presServiceListener, presServiceListenerHdl, iccId);
        }

        public void destroyPresenceService(int presServiceHdl) {
            UceServiceBase.this.onDestroyPresService(presServiceHdl);
        }

        public boolean getServiceStatus() {
            return UceServiceBase.this.onGetServiceStatus();
        }

        public IPresenceService getPresenceService() {
            return UceServiceBase.this.onGetPresenceService();
        }

        public IPresenceService getPresenceServiceForSubscription(String iccId) {
            return UceServiceBase.this.onGetPresenceService(iccId);
        }

        public IOptionsService getOptionsService() {
            return UceServiceBase.this.onGetOptionsService();
        }

        public IOptionsService getOptionsServiceForSubscription(String iccId) {
            return UceServiceBase.this.onGetOptionsService(iccId);
        }
    }

    public UceServiceBinder getBinder() {
        if (this.mBinder == null) {
            this.mBinder = new UceServiceBinder();
        }
        return this.mBinder;
    }

    /* Access modifiers changed, original: protected */
    public boolean onServiceStart(IUceListener uceListener) {
        return false;
    }

    /* Access modifiers changed, original: protected */
    public boolean onStopService() {
        return false;
    }

    /* Access modifiers changed, original: protected */
    public boolean onIsServiceStarted() {
        return false;
    }

    /* Access modifiers changed, original: protected */
    public int onCreateOptionsService(IOptionsListener optionsListener, UceLong optionsServiceListenerHdl) {
        return 0;
    }

    /* Access modifiers changed, original: protected */
    public int onCreateOptionsService(IOptionsListener optionsListener, UceLong optionsServiceListenerHdl, String iccId) {
        return 0;
    }

    /* Access modifiers changed, original: protected */
    public void onDestroyOptionsService(int cdServiceHandle) {
    }

    /* Access modifiers changed, original: protected */
    public int onCreatePresService(IPresenceListener presServiceListener, UceLong presServiceListenerHdl) {
        return 0;
    }

    /* Access modifiers changed, original: protected */
    public int onCreatePresService(IPresenceListener presServiceListener, UceLong presServiceListenerHdl, String iccId) {
        return 0;
    }

    /* Access modifiers changed, original: protected */
    public void onDestroyPresService(int presServiceHdl) {
    }

    /* Access modifiers changed, original: protected */
    public boolean onGetServiceStatus() {
        return false;
    }

    /* Access modifiers changed, original: protected */
    public IPresenceService onGetPresenceService() {
        return null;
    }

    /* Access modifiers changed, original: protected */
    public IPresenceService onGetPresenceService(String iccId) {
        return null;
    }

    /* Access modifiers changed, original: protected */
    public IOptionsService onGetOptionsService() {
        return null;
    }

    /* Access modifiers changed, original: protected */
    public IOptionsService onGetOptionsService(String iccId) {
        return null;
    }
}
