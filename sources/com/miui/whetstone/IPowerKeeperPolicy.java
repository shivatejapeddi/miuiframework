package com.miui.whetstone;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IPowerKeeperPolicy extends IInterface {

    public static class Default implements IPowerKeeperPolicy {
        public void setBroadcastPolicy(BroadcastPolicy[] policies, boolean clear) throws RemoteException {
        }

        public BroadcastPolicy[] getBroadcastPolicies() throws RemoteException {
            return null;
        }

        public void setAlarmPolicy(AlarmPolicy[] policies, boolean clear) throws RemoteException {
        }

        public AlarmPolicy[] getAlarmPolicies() throws RemoteException {
            return null;
        }

        public void updateWakelockBlockedUid(int uid, String tag, boolean isBlocked) throws RemoteException {
        }

        public void offerPowerKeeperIBinder(IBinder client) throws RemoteException {
        }

        public void setAppPushAlarmProperty(int uid, Intent intent, boolean isEnable) throws RemoteException {
        }

        public void setAppPushAlarmLeader(int uid, Intent intent) throws RemoteException {
        }

        public void setLeScanFeature(boolean enable) throws RemoteException {
        }

        public boolean isLeScanAllowed(int uid) throws RemoteException {
            return false;
        }

        public void startLeScan(Bundle bundle) throws RemoteException {
        }

        public void stopLeScan(Bundle bundle) throws RemoteException {
        }

        public void setAppBroadcastControlStat(int uid, boolean isBlocked) throws RemoteException {
        }

        public boolean getAppBroadcastControlStat(int uid) throws RemoteException {
            return false;
        }

        public void setLeScanParam(Bundle bundle) throws RemoteException {
        }

        public void setAppBGIdleFeatureEnable(boolean enable) throws RemoteException {
        }

        public void setAppBGIdleLevel(int uid, int level) throws RemoteException {
        }

        public void enableATrace(boolean enable, String processName) throws RemoteException {
        }

        public void checkNetworkState(String url, int netId, INetStateCallback callback) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IPowerKeeperPolicy {
        private static final String DESCRIPTOR = "com.miui.whetstone.IPowerKeeperPolicy";
        static final int TRANSACTION_checkNetworkState = 19;
        static final int TRANSACTION_enableATrace = 18;
        static final int TRANSACTION_getAlarmPolicies = 4;
        static final int TRANSACTION_getAppBroadcastControlStat = 14;
        static final int TRANSACTION_getBroadcastPolicies = 2;
        static final int TRANSACTION_isLeScanAllowed = 10;
        static final int TRANSACTION_offerPowerKeeperIBinder = 6;
        static final int TRANSACTION_setAlarmPolicy = 3;
        static final int TRANSACTION_setAppBGIdleFeatureEnable = 16;
        static final int TRANSACTION_setAppBGIdleLevel = 17;
        static final int TRANSACTION_setAppBroadcastControlStat = 13;
        static final int TRANSACTION_setAppPushAlarmLeader = 8;
        static final int TRANSACTION_setAppPushAlarmProperty = 7;
        static final int TRANSACTION_setBroadcastPolicy = 1;
        static final int TRANSACTION_setLeScanFeature = 9;
        static final int TRANSACTION_setLeScanParam = 15;
        static final int TRANSACTION_startLeScan = 11;
        static final int TRANSACTION_stopLeScan = 12;
        static final int TRANSACTION_updateWakelockBlockedUid = 5;

        private static class Proxy implements IPowerKeeperPolicy {
            public static IPowerKeeperPolicy sDefaultImpl;
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

            public void setBroadcastPolicy(BroadcastPolicy[] policies, boolean clear) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedArray(policies, 0);
                    _data.writeInt(clear ? 1 : 0);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setBroadcastPolicy(policies, clear);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public BroadcastPolicy[] getBroadcastPolicies() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    BroadcastPolicy[] broadcastPolicyArr = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        broadcastPolicyArr = Stub.getDefaultImpl();
                        if (broadcastPolicyArr != 0) {
                            broadcastPolicyArr = Stub.getDefaultImpl().getBroadcastPolicies();
                            return broadcastPolicyArr;
                        }
                    }
                    _reply.readException();
                    BroadcastPolicy[] _result = (BroadcastPolicy[]) _reply.createTypedArray(BroadcastPolicy.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAlarmPolicy(AlarmPolicy[] policies, boolean clear) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedArray(policies, 0);
                    _data.writeInt(clear ? 1 : 0);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAlarmPolicy(policies, clear);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public AlarmPolicy[] getAlarmPolicies() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    AlarmPolicy[] alarmPolicyArr = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        alarmPolicyArr = Stub.getDefaultImpl();
                        if (alarmPolicyArr != 0) {
                            alarmPolicyArr = Stub.getDefaultImpl().getAlarmPolicies();
                            return alarmPolicyArr;
                        }
                    }
                    _reply.readException();
                    AlarmPolicy[] _result = (AlarmPolicy[]) _reply.createTypedArray(AlarmPolicy.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateWakelockBlockedUid(int uid, String tag, boolean isBlocked) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(tag);
                    _data.writeInt(isBlocked ? 1 : 0);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateWakelockBlockedUid(uid, tag, isBlocked);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void offerPowerKeeperIBinder(IBinder client) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().offerPowerKeeperIBinder(client);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAppPushAlarmProperty(int uid, Intent intent, boolean isEnable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    int i = 1;
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!isEnable) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAppPushAlarmProperty(uid, intent, isEnable);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAppPushAlarmLeader(int uid, Intent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAppPushAlarmLeader(uid, intent);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setLeScanFeature(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setLeScanFeature(enable);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isLeScanAllowed(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isLeScanAllowed(uid);
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

            public void startLeScan(Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        _data.writeInt(1);
                        bundle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(11, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().startLeScan(bundle);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void stopLeScan(Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        _data.writeInt(1);
                        bundle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(12, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().stopLeScan(bundle);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setAppBroadcastControlStat(int uid, boolean isBlocked) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(isBlocked ? 1 : 0);
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAppBroadcastControlStat(uid, isBlocked);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getAppBroadcastControlStat(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(14, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().getAppBroadcastControlStat(uid);
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

            public void setLeScanParam(Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        _data.writeInt(1);
                        bundle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(15, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setLeScanParam(bundle);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAppBGIdleFeatureEnable(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    if (this.mRemote.transact(16, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAppBGIdleFeatureEnable(enable);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAppBGIdleLevel(int uid, int level) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(level);
                    if (this.mRemote.transact(17, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAppBGIdleLevel(uid, level);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableATrace(boolean enable, String processName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    _data.writeString(processName);
                    if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().enableATrace(enable, processName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void checkNetworkState(String url, int netId, INetStateCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(url);
                    _data.writeInt(netId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(19, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().checkNetworkState(url, netId, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPowerKeeperPolicy asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IPowerKeeperPolicy)) {
                return new Proxy(obj);
            }
            return (IPowerKeeperPolicy) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "setBroadcastPolicy";
                case 2:
                    return "getBroadcastPolicies";
                case 3:
                    return "setAlarmPolicy";
                case 4:
                    return "getAlarmPolicies";
                case 5:
                    return "updateWakelockBlockedUid";
                case 6:
                    return "offerPowerKeeperIBinder";
                case 7:
                    return "setAppPushAlarmProperty";
                case 8:
                    return "setAppPushAlarmLeader";
                case 9:
                    return "setLeScanFeature";
                case 10:
                    return "isLeScanAllowed";
                case 11:
                    return "startLeScan";
                case 12:
                    return "stopLeScan";
                case 13:
                    return "setAppBroadcastControlStat";
                case 14:
                    return "getAppBroadcastControlStat";
                case 15:
                    return "setLeScanParam";
                case 16:
                    return "setAppBGIdleFeatureEnable";
                case 17:
                    return "setAppBGIdleLevel";
                case 18:
                    return "enableATrace";
                case 19:
                    return "checkNetworkState";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code != IBinder.INTERFACE_TRANSACTION) {
                boolean _arg1 = false;
                int _arg0;
                boolean _result;
                Bundle _arg02;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        BroadcastPolicy[] _arg03 = (BroadcastPolicy[]) data.createTypedArray(BroadcastPolicy.CREATOR);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setBroadcastPolicy(_arg03, _arg1);
                        reply.writeNoException();
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        BroadcastPolicy[] _result2 = getBroadcastPolicies();
                        reply.writeNoException();
                        reply.writeTypedArray(_result2, 1);
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        AlarmPolicy[] _arg04 = (AlarmPolicy[]) data.createTypedArray(AlarmPolicy.CREATOR);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setAlarmPolicy(_arg04, _arg1);
                        reply.writeNoException();
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        AlarmPolicy[] _result3 = getAlarmPolicies();
                        reply.writeNoException();
                        reply.writeTypedArray(_result3, 1);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        String _arg12 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        updateWakelockBlockedUid(_arg0, _arg12, _arg1);
                        reply.writeNoException();
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        offerPowerKeeperIBinder(data.readStrongBinder());
                        reply.writeNoException();
                        return true;
                    case 7:
                        Intent _arg13;
                        data.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg13 = (Intent) Intent.CREATOR.createFromParcel(data);
                        } else {
                            _arg13 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setAppPushAlarmProperty(_arg0, _arg13, _arg1);
                        reply.writeNoException();
                        return true;
                    case 8:
                        Intent _arg14;
                        data.enforceInterface(descriptor);
                        int _arg05 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg14 = (Intent) Intent.CREATOR.createFromParcel(data);
                        } else {
                            _arg14 = null;
                        }
                        setAppPushAlarmLeader(_arg05, _arg14);
                        reply.writeNoException();
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setLeScanFeature(_arg1);
                        reply.writeNoException();
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        _result = isLeScanAllowed(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        startLeScan(_arg02);
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        stopLeScan(_arg02);
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setAppBroadcastControlStat(_arg0, _arg1);
                        reply.writeNoException();
                        return true;
                    case 14:
                        data.enforceInterface(descriptor);
                        _result = getAppBroadcastControlStat(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 15:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        setLeScanParam(_arg02);
                        reply.writeNoException();
                        return true;
                    case 16:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setAppBGIdleFeatureEnable(_arg1);
                        reply.writeNoException();
                        return true;
                    case 17:
                        data.enforceInterface(descriptor);
                        setAppBGIdleLevel(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 18:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        enableATrace(_arg1, data.readString());
                        reply.writeNoException();
                        return true;
                    case 19:
                        data.enforceInterface(descriptor);
                        checkNetworkState(data.readString(), data.readInt(), com.miui.whetstone.INetStateCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IPowerKeeperPolicy impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IPowerKeeperPolicy getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void checkNetworkState(String str, int i, INetStateCallback iNetStateCallback) throws RemoteException;

    void enableATrace(boolean z, String str) throws RemoteException;

    AlarmPolicy[] getAlarmPolicies() throws RemoteException;

    boolean getAppBroadcastControlStat(int i) throws RemoteException;

    BroadcastPolicy[] getBroadcastPolicies() throws RemoteException;

    boolean isLeScanAllowed(int i) throws RemoteException;

    void offerPowerKeeperIBinder(IBinder iBinder) throws RemoteException;

    void setAlarmPolicy(AlarmPolicy[] alarmPolicyArr, boolean z) throws RemoteException;

    void setAppBGIdleFeatureEnable(boolean z) throws RemoteException;

    void setAppBGIdleLevel(int i, int i2) throws RemoteException;

    void setAppBroadcastControlStat(int i, boolean z) throws RemoteException;

    void setAppPushAlarmLeader(int i, Intent intent) throws RemoteException;

    void setAppPushAlarmProperty(int i, Intent intent, boolean z) throws RemoteException;

    void setBroadcastPolicy(BroadcastPolicy[] broadcastPolicyArr, boolean z) throws RemoteException;

    void setLeScanFeature(boolean z) throws RemoteException;

    void setLeScanParam(Bundle bundle) throws RemoteException;

    void startLeScan(Bundle bundle) throws RemoteException;

    void stopLeScan(Bundle bundle) throws RemoteException;

    void updateWakelockBlockedUid(int i, String str, boolean z) throws RemoteException;
}
