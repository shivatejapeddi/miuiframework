package android.telephony.ims.compat.stub;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;
import com.android.ims.ImsConfig;
import com.android.ims.ImsConfigListener;
import com.android.ims.internal.IImsConfig;
import com.android.ims.internal.IImsConfig.Stub;
import com.android.internal.annotations.VisibleForTesting;
import java.lang.ref.WeakReference;
import java.util.HashMap;

public class ImsConfigImplBase {
    private static final String TAG = "ImsConfigImplBase";
    ImsConfigStub mImsConfigStub;

    @VisibleForTesting
    public static class ImsConfigStub extends Stub {
        Context mContext;
        WeakReference<ImsConfigImplBase> mImsConfigImplBaseWeakReference;
        private HashMap<Integer, Integer> mProvisionedIntValue = new HashMap();
        private HashMap<Integer, String> mProvisionedStringValue = new HashMap();

        @VisibleForTesting
        public ImsConfigStub(ImsConfigImplBase imsConfigImplBase, Context context) {
            this.mContext = context;
            this.mImsConfigImplBaseWeakReference = new WeakReference(imsConfigImplBase);
        }

        /* JADX WARNING: Missing block: B:12:0x002f, code skipped:
            return r0;
     */
        public synchronized int getProvisionedValue(int r3) throws android.os.RemoteException {
            /*
            r2 = this;
            monitor-enter(r2);
            r0 = r2.mProvisionedIntValue;	 Catch:{ all -> 0x0030 }
            r1 = java.lang.Integer.valueOf(r3);	 Catch:{ all -> 0x0030 }
            r0 = r0.containsKey(r1);	 Catch:{ all -> 0x0030 }
            if (r0 == 0) goto L_0x001f;
        L_0x000d:
            r0 = r2.mProvisionedIntValue;	 Catch:{ all -> 0x0030 }
            r1 = java.lang.Integer.valueOf(r3);	 Catch:{ all -> 0x0030 }
            r0 = r0.get(r1);	 Catch:{ all -> 0x0030 }
            r0 = (java.lang.Integer) r0;	 Catch:{ all -> 0x0030 }
            r0 = r0.intValue();	 Catch:{ all -> 0x0030 }
            monitor-exit(r2);
            return r0;
        L_0x001f:
            r0 = r2.getImsConfigImpl();	 Catch:{ all -> 0x0030 }
            r0 = r0.getProvisionedValue(r3);	 Catch:{ all -> 0x0030 }
            r1 = -1;
            if (r0 == r1) goto L_0x002e;
        L_0x002a:
            r1 = 0;
            r2.updateCachedValue(r3, r0, r1);	 Catch:{ all -> 0x0030 }
        L_0x002e:
            monitor-exit(r2);
            return r0;
        L_0x0030:
            r3 = move-exception;
            monitor-exit(r2);
            throw r3;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.telephony.ims.compat.stub.ImsConfigImplBase$ImsConfigStub.getProvisionedValue(int):int");
        }

        /* JADX WARNING: Missing block: B:12:0x002a, code skipped:
            return r0;
     */
        public synchronized java.lang.String getProvisionedStringValue(int r3) throws android.os.RemoteException {
            /*
            r2 = this;
            monitor-enter(r2);
            r0 = r2.mProvisionedIntValue;	 Catch:{ all -> 0x002b }
            r1 = java.lang.Integer.valueOf(r3);	 Catch:{ all -> 0x002b }
            r0 = r0.containsKey(r1);	 Catch:{ all -> 0x002b }
            if (r0 == 0) goto L_0x001b;
        L_0x000d:
            r0 = r2.mProvisionedStringValue;	 Catch:{ all -> 0x002b }
            r1 = java.lang.Integer.valueOf(r3);	 Catch:{ all -> 0x002b }
            r0 = r0.get(r1);	 Catch:{ all -> 0x002b }
            r0 = (java.lang.String) r0;	 Catch:{ all -> 0x002b }
            monitor-exit(r2);
            return r0;
        L_0x001b:
            r0 = r2.getImsConfigImpl();	 Catch:{ all -> 0x002b }
            r0 = r0.getProvisionedStringValue(r3);	 Catch:{ all -> 0x002b }
            if (r0 == 0) goto L_0x0029;
        L_0x0025:
            r1 = 0;
            r2.updateCachedValue(r3, r0, r1);	 Catch:{ all -> 0x002b }
        L_0x0029:
            monitor-exit(r2);
            return r0;
        L_0x002b:
            r3 = move-exception;
            monitor-exit(r2);
            throw r3;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.telephony.ims.compat.stub.ImsConfigImplBase$ImsConfigStub.getProvisionedStringValue(int):java.lang.String");
        }

        public synchronized int setProvisionedValue(int item, int value) throws RemoteException {
            int retVal;
            this.mProvisionedIntValue.remove(Integer.valueOf(item));
            retVal = getImsConfigImpl().setProvisionedValue(item, value);
            if (retVal == 0) {
                updateCachedValue(item, value, true);
            } else {
                String str = ImsConfigImplBase.TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Set provision value of ");
                stringBuilder.append(item);
                stringBuilder.append(" to ");
                stringBuilder.append(value);
                stringBuilder.append(" failed with error code ");
                stringBuilder.append(retVal);
                Log.d(str, stringBuilder.toString());
            }
            return retVal;
        }

        public synchronized int setProvisionedStringValue(int item, String value) throws RemoteException {
            int retVal;
            this.mProvisionedStringValue.remove(Integer.valueOf(item));
            retVal = getImsConfigImpl().setProvisionedStringValue(item, value);
            if (retVal == 0) {
                updateCachedValue(item, value, true);
            }
            return retVal;
        }

        public void getFeatureValue(int feature, int network, ImsConfigListener listener) throws RemoteException {
            getImsConfigImpl().getFeatureValue(feature, network, listener);
        }

        public void setFeatureValue(int feature, int network, int value, ImsConfigListener listener) throws RemoteException {
            getImsConfigImpl().setFeatureValue(feature, network, value, listener);
        }

        public boolean getVolteProvisioned() throws RemoteException {
            return getImsConfigImpl().getVolteProvisioned();
        }

        public void getVideoQuality(ImsConfigListener listener) throws RemoteException {
            getImsConfigImpl().getVideoQuality(listener);
        }

        public void setVideoQuality(int quality, ImsConfigListener listener) throws RemoteException {
            getImsConfigImpl().setVideoQuality(quality, listener);
        }

        private ImsConfigImplBase getImsConfigImpl() throws RemoteException {
            ImsConfigImplBase ref = (ImsConfigImplBase) this.mImsConfigImplBaseWeakReference.get();
            if (ref != null) {
                return ref;
            }
            throw new RemoteException("Fail to get ImsConfigImpl");
        }

        private void sendImsConfigChangedIntent(int item, int value) {
            sendImsConfigChangedIntent(item, Integer.toString(value));
        }

        private void sendImsConfigChangedIntent(int item, String value) {
            Intent configChangedIntent = new Intent(ImsConfig.ACTION_IMS_CONFIG_CHANGED);
            configChangedIntent.putExtra(ImsConfig.EXTRA_CHANGED_ITEM, item);
            configChangedIntent.putExtra("value", value);
            Context context = this.mContext;
            if (context != null) {
                context.sendBroadcast(configChangedIntent);
            }
        }

        /* Access modifiers changed, original: protected|declared_synchronized */
        public synchronized void updateCachedValue(int item, int value, boolean notifyChange) {
            this.mProvisionedIntValue.put(Integer.valueOf(item), Integer.valueOf(value));
            if (notifyChange) {
                sendImsConfigChangedIntent(item, value);
            }
        }

        /* Access modifiers changed, original: protected|declared_synchronized */
        public synchronized void updateCachedValue(int item, String value, boolean notifyChange) {
            this.mProvisionedStringValue.put(Integer.valueOf(item), value);
            if (notifyChange) {
                sendImsConfigChangedIntent(item, value);
            }
        }
    }

    @UnsupportedAppUsage
    public ImsConfigImplBase(Context context) {
        this.mImsConfigStub = new ImsConfigStub(this, context);
    }

    public int getProvisionedValue(int item) throws RemoteException {
        return -1;
    }

    public String getProvisionedStringValue(int item) throws RemoteException {
        return null;
    }

    public int setProvisionedValue(int item, int value) throws RemoteException {
        return 1;
    }

    public int setProvisionedStringValue(int item, String value) throws RemoteException {
        return 1;
    }

    public void getFeatureValue(int feature, int network, ImsConfigListener listener) throws RemoteException {
    }

    public void setFeatureValue(int feature, int network, int value, ImsConfigListener listener) throws RemoteException {
    }

    public boolean getVolteProvisioned() throws RemoteException {
        return false;
    }

    public void getVideoQuality(ImsConfigListener listener) throws RemoteException {
    }

    public void setVideoQuality(int quality, ImsConfigListener listener) throws RemoteException {
    }

    @UnsupportedAppUsage
    public IImsConfig getIImsConfig() {
        return this.mImsConfigStub;
    }

    public final void notifyProvisionedValueChanged(int item, int value) {
        this.mImsConfigStub.updateCachedValue(item, value, true);
    }

    public final void notifyProvisionedValueChanged(int item, String value) {
        this.mImsConfigStub.updateCachedValue(item, value, true);
    }
}
