package android.net;

import android.annotation.UnsupportedAppUsage;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.SubscriptionPlan;

public interface INetworkPolicyManager extends IInterface {

    public static class Default implements INetworkPolicyManager {
        public void setUidPolicy(int uid, int policy) throws RemoteException {
        }

        public void addUidPolicy(int uid, int policy) throws RemoteException {
        }

        public void removeUidPolicy(int uid, int policy) throws RemoteException {
        }

        public int getUidPolicy(int uid) throws RemoteException {
            return 0;
        }

        public int[] getUidsWithPolicy(int policy) throws RemoteException {
            return null;
        }

        public void registerListener(INetworkPolicyListener listener) throws RemoteException {
        }

        public void unregisterListener(INetworkPolicyListener listener) throws RemoteException {
        }

        public void setNetworkPolicies(NetworkPolicy[] policies) throws RemoteException {
        }

        public NetworkPolicy[] getNetworkPolicies(String callingPackage) throws RemoteException {
            return null;
        }

        public void snoozeLimit(NetworkTemplate template) throws RemoteException {
        }

        public void setRestrictBackground(boolean restrictBackground) throws RemoteException {
        }

        public boolean getRestrictBackground() throws RemoteException {
            return false;
        }

        public void onTetheringChanged(String iface, boolean tethering) throws RemoteException {
        }

        public int getRestrictBackgroundByCaller() throws RemoteException {
            return 0;
        }

        public void setDeviceIdleMode(boolean enabled) throws RemoteException {
        }

        public void setWifiMeteredOverride(String networkId, int meteredOverride) throws RemoteException {
        }

        public NetworkQuotaInfo getNetworkQuotaInfo(NetworkState state) throws RemoteException {
            return null;
        }

        public SubscriptionPlan[] getSubscriptionPlans(int subId, String callingPackage) throws RemoteException {
            return null;
        }

        public void setSubscriptionPlans(int subId, SubscriptionPlan[] plans, String callingPackage) throws RemoteException {
        }

        public String getSubscriptionPlansOwner(int subId) throws RemoteException {
            return null;
        }

        public void setSubscriptionOverride(int subId, int overrideMask, int overrideValue, long timeoutMillis, String callingPackage) throws RemoteException {
        }

        public void factoryReset(String subscriber) throws RemoteException {
        }

        public boolean isUidNetworkingBlocked(int uid, boolean meteredNetwork) throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements INetworkPolicyManager {
        private static final String DESCRIPTOR = "android.net.INetworkPolicyManager";
        static final int TRANSACTION_addUidPolicy = 2;
        static final int TRANSACTION_factoryReset = 22;
        static final int TRANSACTION_getNetworkPolicies = 9;
        static final int TRANSACTION_getNetworkQuotaInfo = 17;
        static final int TRANSACTION_getRestrictBackground = 12;
        static final int TRANSACTION_getRestrictBackgroundByCaller = 14;
        static final int TRANSACTION_getSubscriptionPlans = 18;
        static final int TRANSACTION_getSubscriptionPlansOwner = 20;
        static final int TRANSACTION_getUidPolicy = 4;
        static final int TRANSACTION_getUidsWithPolicy = 5;
        static final int TRANSACTION_isUidNetworkingBlocked = 23;
        static final int TRANSACTION_onTetheringChanged = 13;
        static final int TRANSACTION_registerListener = 6;
        static final int TRANSACTION_removeUidPolicy = 3;
        static final int TRANSACTION_setDeviceIdleMode = 15;
        static final int TRANSACTION_setNetworkPolicies = 8;
        static final int TRANSACTION_setRestrictBackground = 11;
        static final int TRANSACTION_setSubscriptionOverride = 21;
        static final int TRANSACTION_setSubscriptionPlans = 19;
        static final int TRANSACTION_setUidPolicy = 1;
        static final int TRANSACTION_setWifiMeteredOverride = 16;
        static final int TRANSACTION_snoozeLimit = 10;
        static final int TRANSACTION_unregisterListener = 7;

        private static class Proxy implements INetworkPolicyManager {
            public static INetworkPolicyManager sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public void setUidPolicy(int uid, int policy) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(policy);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setUidPolicy(uid, policy);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addUidPolicy(int uid, int policy) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(policy);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addUidPolicy(uid, policy);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeUidPolicy(int uid, int policy) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(policy);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeUidPolicy(uid, policy);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getUidPolicy(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    int i = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getUidPolicy(uid);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] getUidsWithPolicy(int policy) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(policy);
                    int[] iArr = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        iArr = Stub.getDefaultImpl();
                        if (iArr != 0) {
                            iArr = Stub.getDefaultImpl().getUidsWithPolicy(policy);
                            return iArr;
                        }
                    }
                    _reply.readException();
                    iArr = _reply.createIntArray();
                    int[] _result = iArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerListener(INetworkPolicyListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterListener(INetworkPolicyListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setNetworkPolicies(NetworkPolicy[] policies) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedArray(policies, 0);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setNetworkPolicies(policies);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public NetworkPolicy[] getNetworkPolicies(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    NetworkPolicy[] networkPolicyArr = 9;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        networkPolicyArr = Stub.getDefaultImpl();
                        if (networkPolicyArr != 0) {
                            networkPolicyArr = Stub.getDefaultImpl().getNetworkPolicies(callingPackage);
                            return networkPolicyArr;
                        }
                    }
                    _reply.readException();
                    NetworkPolicy[] _result = (NetworkPolicy[]) _reply.createTypedArray(NetworkPolicy.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void snoozeLimit(NetworkTemplate template) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (template != null) {
                        _data.writeInt(1);
                        template.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().snoozeLimit(template);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setRestrictBackground(boolean restrictBackground) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(restrictBackground ? 1 : 0);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setRestrictBackground(restrictBackground);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getRestrictBackground() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().getRestrictBackground();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onTetheringChanged(String iface, boolean tethering) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeInt(tethering ? 1 : 0);
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onTetheringChanged(iface, tethering);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getRestrictBackgroundByCaller() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 14;
                    if (!this.mRemote.transact(14, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getRestrictBackgroundByCaller();
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setDeviceIdleMode(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    if (this.mRemote.transact(15, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setDeviceIdleMode(enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setWifiMeteredOverride(String networkId, int meteredOverride) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(networkId);
                    _data.writeInt(meteredOverride);
                    if (this.mRemote.transact(16, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setWifiMeteredOverride(networkId, meteredOverride);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public NetworkQuotaInfo getNetworkQuotaInfo(NetworkState state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (state != null) {
                        _data.writeInt(1);
                        state.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    NetworkQuotaInfo networkQuotaInfo = this.mRemote;
                    if (!networkQuotaInfo.transact(17, _data, _reply, 0)) {
                        networkQuotaInfo = Stub.getDefaultImpl();
                        if (networkQuotaInfo != null) {
                            networkQuotaInfo = Stub.getDefaultImpl().getNetworkQuotaInfo(state);
                            return networkQuotaInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        networkQuotaInfo = (NetworkQuotaInfo) NetworkQuotaInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        networkQuotaInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return networkQuotaInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public SubscriptionPlan[] getSubscriptionPlans(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    SubscriptionPlan[] subscriptionPlanArr = 18;
                    if (!this.mRemote.transact(18, _data, _reply, 0)) {
                        subscriptionPlanArr = Stub.getDefaultImpl();
                        if (subscriptionPlanArr != 0) {
                            subscriptionPlanArr = Stub.getDefaultImpl().getSubscriptionPlans(subId, callingPackage);
                            return subscriptionPlanArr;
                        }
                    }
                    _reply.readException();
                    SubscriptionPlan[] _result = (SubscriptionPlan[]) _reply.createTypedArray(SubscriptionPlan.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setSubscriptionPlans(int subId, SubscriptionPlan[] plans, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeTypedArray(plans, 0);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setSubscriptionPlans(subId, plans, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getSubscriptionPlansOwner(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    String str = 20;
                    if (!this.mRemote.transact(20, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getSubscriptionPlansOwner(subId);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setSubscriptionOverride(int subId, int overrideMask, int overrideValue, long timeoutMillis, String callingPackage) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                long j;
                String str;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(subId);
                    } catch (Throwable th2) {
                        th = th2;
                        i = overrideMask;
                        i2 = overrideValue;
                        j = timeoutMillis;
                        str = callingPackage;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(overrideMask);
                    } catch (Throwable th3) {
                        th = th3;
                        i2 = overrideValue;
                        j = timeoutMillis;
                        str = callingPackage;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(overrideValue);
                        try {
                            _data.writeLong(timeoutMillis);
                        } catch (Throwable th4) {
                            th = th4;
                            str = callingPackage;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeString(callingPackage);
                            if (this.mRemote.transact(21, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().setSubscriptionOverride(subId, overrideMask, overrideValue, timeoutMillis, callingPackage);
                            _reply.recycle();
                            _data.recycle();
                        } catch (Throwable th5) {
                            th = th5;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        j = timeoutMillis;
                        str = callingPackage;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    int i3 = subId;
                    i = overrideMask;
                    i2 = overrideValue;
                    j = timeoutMillis;
                    str = callingPackage;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void factoryReset(String subscriber) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(subscriber);
                    if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().factoryReset(subscriber);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isUidNetworkingBlocked(int uid, boolean meteredNetwork) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    boolean _result = true;
                    _data.writeInt(meteredNetwork ? 1 : 0);
                    if (this.mRemote.transact(23, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isUidNetworkingBlocked(uid, meteredNetwork);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static INetworkPolicyManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof INetworkPolicyManager)) {
                return new Proxy(obj);
            }
            return (INetworkPolicyManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "setUidPolicy";
                case 2:
                    return "addUidPolicy";
                case 3:
                    return "removeUidPolicy";
                case 4:
                    return "getUidPolicy";
                case 5:
                    return "getUidsWithPolicy";
                case 6:
                    return "registerListener";
                case 7:
                    return "unregisterListener";
                case 8:
                    return "setNetworkPolicies";
                case 9:
                    return "getNetworkPolicies";
                case 10:
                    return "snoozeLimit";
                case 11:
                    return "setRestrictBackground";
                case 12:
                    return "getRestrictBackground";
                case 13:
                    return "onTetheringChanged";
                case 14:
                    return "getRestrictBackgroundByCaller";
                case 15:
                    return "setDeviceIdleMode";
                case 16:
                    return "setWifiMeteredOverride";
                case 17:
                    return "getNetworkQuotaInfo";
                case 18:
                    return "getSubscriptionPlans";
                case 19:
                    return "setSubscriptionPlans";
                case 20:
                    return "getSubscriptionPlansOwner";
                case 21:
                    return "setSubscriptionOverride";
                case 22:
                    return "factoryReset";
                case 23:
                    return "isUidNetworkingBlocked";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            Parcel parcel2 = reply;
            String descriptor = DESCRIPTOR;
            if (i != 1598968902) {
                boolean _arg1 = false;
                int _result;
                String _arg0;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        setUidPolicy(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        addUidPolicy(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        removeUidPolicy(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        _result = getUidPolicy(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        int[] _result2 = getUidsWithPolicy(data.readInt());
                        reply.writeNoException();
                        parcel2.writeIntArray(_result2);
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        registerListener(android.net.INetworkPolicyListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        unregisterListener(android.net.INetworkPolicyListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        setNetworkPolicies((NetworkPolicy[]) parcel.createTypedArray(NetworkPolicy.CREATOR));
                        reply.writeNoException();
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        NetworkPolicy[] _result3 = getNetworkPolicies(data.readString());
                        reply.writeNoException();
                        parcel2.writeTypedArray(_result3, 1);
                        return true;
                    case 10:
                        NetworkTemplate _arg02;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (NetworkTemplate) NetworkTemplate.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        snoozeLimit(_arg02);
                        reply.writeNoException();
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setRestrictBackground(_arg1);
                        reply.writeNoException();
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        _arg1 = getRestrictBackground();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        onTetheringChanged(_arg0, _arg1);
                        reply.writeNoException();
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        int _result4 = getRestrictBackgroundByCaller();
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setDeviceIdleMode(_arg1);
                        reply.writeNoException();
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        setWifiMeteredOverride(data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 17:
                        NetworkState _arg03;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (NetworkState) NetworkState.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        NetworkQuotaInfo _result5 = getNetworkQuotaInfo(_arg03);
                        reply.writeNoException();
                        if (_result5 != null) {
                            parcel2.writeInt(1);
                            _result5.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        SubscriptionPlan[] _result6 = getSubscriptionPlans(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeTypedArray(_result6, 1);
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        setSubscriptionPlans(data.readInt(), (SubscriptionPlan[]) parcel.createTypedArray(SubscriptionPlan.CREATOR), data.readString());
                        reply.writeNoException();
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        _arg0 = getSubscriptionPlansOwner(data.readInt());
                        reply.writeNoException();
                        parcel2.writeString(_arg0);
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        setSubscriptionOverride(data.readInt(), data.readInt(), data.readInt(), data.readLong(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        factoryReset(data.readString());
                        reply.writeNoException();
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        _result = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        boolean _result7 = isUidNetworkingBlocked(_result, _arg1);
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(INetworkPolicyManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static INetworkPolicyManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void addUidPolicy(int i, int i2) throws RemoteException;

    void factoryReset(String str) throws RemoteException;

    NetworkPolicy[] getNetworkPolicies(String str) throws RemoteException;

    @UnsupportedAppUsage
    NetworkQuotaInfo getNetworkQuotaInfo(NetworkState networkState) throws RemoteException;

    @UnsupportedAppUsage
    boolean getRestrictBackground() throws RemoteException;

    int getRestrictBackgroundByCaller() throws RemoteException;

    SubscriptionPlan[] getSubscriptionPlans(int i, String str) throws RemoteException;

    String getSubscriptionPlansOwner(int i) throws RemoteException;

    @UnsupportedAppUsage
    int getUidPolicy(int i) throws RemoteException;

    int[] getUidsWithPolicy(int i) throws RemoteException;

    boolean isUidNetworkingBlocked(int i, boolean z) throws RemoteException;

    void onTetheringChanged(String str, boolean z) throws RemoteException;

    void registerListener(INetworkPolicyListener iNetworkPolicyListener) throws RemoteException;

    void removeUidPolicy(int i, int i2) throws RemoteException;

    void setDeviceIdleMode(boolean z) throws RemoteException;

    @UnsupportedAppUsage
    void setNetworkPolicies(NetworkPolicy[] networkPolicyArr) throws RemoteException;

    @UnsupportedAppUsage
    void setRestrictBackground(boolean z) throws RemoteException;

    void setSubscriptionOverride(int i, int i2, int i3, long j, String str) throws RemoteException;

    void setSubscriptionPlans(int i, SubscriptionPlan[] subscriptionPlanArr, String str) throws RemoteException;

    @UnsupportedAppUsage
    void setUidPolicy(int i, int i2) throws RemoteException;

    void setWifiMeteredOverride(String str, int i) throws RemoteException;

    @UnsupportedAppUsage
    void snoozeLimit(NetworkTemplate networkTemplate) throws RemoteException;

    void unregisterListener(INetworkPolicyListener iNetworkPolicyListener) throws RemoteException;
}
