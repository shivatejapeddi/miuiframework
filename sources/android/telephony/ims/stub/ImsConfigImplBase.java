package android.telephony.ims.stub;

import android.annotation.SystemApi;
import android.content.Context;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.telephony.ims.aidl.IImsConfig;
import android.telephony.ims.aidl.IImsConfig.Stub;
import android.telephony.ims.aidl.IImsConfigCallback;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.HashMap;

@SystemApi
public class ImsConfigImplBase {
    public static final int CONFIG_RESULT_FAILED = 1;
    public static final int CONFIG_RESULT_SUCCESS = 0;
    public static final int CONFIG_RESULT_UNKNOWN = -1;
    private static final String TAG = "ImsConfigImplBase";
    private final RemoteCallbackList<IImsConfigCallback> mCallbacks = new RemoteCallbackList();
    ImsConfigStub mImsConfigStub = new ImsConfigStub(this);

    @VisibleForTesting
    public static class ImsConfigStub extends Stub {
        WeakReference<ImsConfigImplBase> mImsConfigImplBaseWeakReference;
        private HashMap<Integer, Integer> mProvisionedIntValue = new HashMap();
        private HashMap<Integer, String> mProvisionedStringValue = new HashMap();

        @VisibleForTesting
        public ImsConfigStub(ImsConfigImplBase imsConfigImplBase) {
            this.mImsConfigImplBaseWeakReference = new WeakReference(imsConfigImplBase);
        }

        public void addImsConfigCallback(IImsConfigCallback c) throws RemoteException {
            getImsConfigImpl().addImsConfigCallback(c);
        }

        public void removeImsConfigCallback(IImsConfigCallback c) throws RemoteException {
            getImsConfigImpl().removeImsConfigCallback(c);
        }

        /* JADX WARNING: Missing block: B:12:0x002f, code skipped:
            return r0;
     */
        public synchronized int getConfigInt(int r3) throws android.os.RemoteException {
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
            r0 = r0.getConfigInt(r3);	 Catch:{ all -> 0x0030 }
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
            throw new UnsupportedOperationException("Method not decompiled: android.telephony.ims.stub.ImsConfigImplBase$ImsConfigStub.getConfigInt(int):int");
        }

        /* JADX WARNING: Missing block: B:12:0x002a, code skipped:
            return r0;
     */
        public synchronized java.lang.String getConfigString(int r3) throws android.os.RemoteException {
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
            r0 = r0.getConfigString(r3);	 Catch:{ all -> 0x002b }
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
            throw new UnsupportedOperationException("Method not decompiled: android.telephony.ims.stub.ImsConfigImplBase$ImsConfigStub.getConfigString(int):java.lang.String");
        }

        public synchronized int setConfigInt(int item, int value) throws RemoteException {
            int retVal;
            this.mProvisionedIntValue.remove(Integer.valueOf(item));
            retVal = getImsConfigImpl().setConfig(item, value);
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

        public synchronized int setConfigString(int item, String value) throws RemoteException {
            int retVal;
            this.mProvisionedStringValue.remove(Integer.valueOf(item));
            retVal = getImsConfigImpl().setConfig(item, value);
            if (retVal == 0) {
                updateCachedValue(item, value, true);
            }
            return retVal;
        }

        private ImsConfigImplBase getImsConfigImpl() throws RemoteException {
            ImsConfigImplBase ref = (ImsConfigImplBase) this.mImsConfigImplBaseWeakReference.get();
            if (ref != null) {
                return ref;
            }
            throw new RemoteException("Fail to get ImsConfigImpl");
        }

        private void notifyImsConfigChanged(int item, int value) throws RemoteException {
            getImsConfigImpl().notifyConfigChanged(item, value);
        }

        private void notifyImsConfigChanged(int item, String value) throws RemoteException {
            getImsConfigImpl().notifyConfigChanged(item, value);
        }

        /* Access modifiers changed, original: protected|declared_synchronized */
        public synchronized void updateCachedValue(int item, int value, boolean notifyChange) throws RemoteException {
            this.mProvisionedIntValue.put(Integer.valueOf(item), Integer.valueOf(value));
            if (notifyChange) {
                notifyImsConfigChanged(item, value);
            }
        }

        /* Access modifiers changed, original: protected|declared_synchronized */
        public synchronized void updateCachedValue(int item, String value, boolean notifyChange) throws RemoteException {
            this.mProvisionedStringValue.put(Integer.valueOf(item), value);
            if (notifyChange) {
                notifyImsConfigChanged(item, value);
            }
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SetConfigResult {
    }

    public ImsConfigImplBase(Context context) {
    }

    private void addImsConfigCallback(IImsConfigCallback c) {
        this.mCallbacks.register(c);
    }

    private void removeImsConfigCallback(IImsConfigCallback c) {
        this.mCallbacks.unregister(c);
    }

    private final void notifyConfigChanged(int item, int value) {
        RemoteCallbackList remoteCallbackList = this.mCallbacks;
        if (remoteCallbackList != null) {
            remoteCallbackList.broadcast(new -$$Lambda$ImsConfigImplBase$yL4863k-FoQyqg_FX2mWsLMqbyA(item, value));
        }
    }

    static /* synthetic */ void lambda$notifyConfigChanged$0(int item, int value, IImsConfigCallback c) {
        try {
            c.onIntConfigChanged(item, value);
        } catch (RemoteException e) {
            Log.w(TAG, "notifyConfigChanged(int): dead binder in notify, skipping.");
        }
    }

    private void notifyConfigChanged(int item, String value) {
        RemoteCallbackList remoteCallbackList = this.mCallbacks;
        if (remoteCallbackList != null) {
            remoteCallbackList.broadcast(new -$$Lambda$ImsConfigImplBase$GAuYvQ8qBc7KgCJhNp4Pt4j5t-0(item, value));
        }
    }

    static /* synthetic */ void lambda$notifyConfigChanged$1(int item, String value, IImsConfigCallback c) {
        try {
            c.onStringConfigChanged(item, value);
        } catch (RemoteException e) {
            Log.w(TAG, "notifyConfigChanged(string): dead binder in notify, skipping.");
        }
    }

    public IImsConfig getIImsConfig() {
        return this.mImsConfigStub;
    }

    public final void notifyProvisionedValueChanged(int item, int value) {
        try {
            this.mImsConfigStub.updateCachedValue(item, value, true);
        } catch (RemoteException e) {
            Log.w(TAG, "notifyProvisionedValueChanged(int): Framework connection is dead.");
        }
    }

    public final void notifyProvisionedValueChanged(int item, String value) {
        try {
            this.mImsConfigStub.updateCachedValue(item, value, true);
        } catch (RemoteException e) {
            Log.w(TAG, "notifyProvisionedValueChanged(string): Framework connection is dead.");
        }
    }

    public int setConfig(int item, int value) {
        return 1;
    }

    public int setConfig(int item, String value) {
        return 1;
    }

    public int getConfigInt(int item) {
        return -1;
    }

    public String getConfigString(int item) {
        return null;
    }
}
