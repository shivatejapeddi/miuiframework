package android.location;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ILocationPolicyManager extends IInterface {

    public static class Default implements ILocationPolicyManager {
        public void setUidPolicy(int uid, int policy) throws RemoteException {
        }

        public int getUidPolicy(int uid) throws RemoteException {
            return 0;
        }

        public int[] getUidsWithPolicy(int policy) throws RemoteException {
            return null;
        }

        public void setUidNavigationStart(int uid) throws RemoteException {
        }

        public void setUidNavigationStop(int uid) throws RemoteException {
        }

        public boolean checkUidNavigationScreenLock(int uid) throws RemoteException {
            return false;
        }

        public boolean isUidForeground(int uid) throws RemoteException {
            return false;
        }

        public boolean checkUidLocationOp(int uid, int op) throws RemoteException {
            return false;
        }

        public void registerListener(ILocationPolicyListener listener) throws RemoteException {
        }

        public void unregisterListener(ILocationPolicyListener listener) throws RemoteException {
        }

        public void setLocationPolicies(LocationPolicy[] policies) throws RemoteException {
        }

        public LocationPolicy[] getLocationPolicies() throws RemoteException {
            return null;
        }

        public void setRestrictBackground(boolean restrictBackground) throws RemoteException {
        }

        public boolean getRestrictBackground() throws RemoteException {
            return false;
        }

        public void setFakeGpsFeatureOnState(boolean on) throws RemoteException {
        }

        public void setPhoneStationary(boolean stationary, Location location) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ILocationPolicyManager {
        private static final String DESCRIPTOR = "android.location.ILocationPolicyManager";
        static final int TRANSACTION_checkUidLocationOp = 8;
        static final int TRANSACTION_checkUidNavigationScreenLock = 6;
        static final int TRANSACTION_getLocationPolicies = 12;
        static final int TRANSACTION_getRestrictBackground = 14;
        static final int TRANSACTION_getUidPolicy = 2;
        static final int TRANSACTION_getUidsWithPolicy = 3;
        static final int TRANSACTION_isUidForeground = 7;
        static final int TRANSACTION_registerListener = 9;
        static final int TRANSACTION_setFakeGpsFeatureOnState = 15;
        static final int TRANSACTION_setLocationPolicies = 11;
        static final int TRANSACTION_setPhoneStationary = 16;
        static final int TRANSACTION_setRestrictBackground = 13;
        static final int TRANSACTION_setUidNavigationStart = 4;
        static final int TRANSACTION_setUidNavigationStop = 5;
        static final int TRANSACTION_setUidPolicy = 1;
        static final int TRANSACTION_unregisterListener = 10;

        private static class Proxy implements ILocationPolicyManager {
            public static ILocationPolicyManager sDefaultImpl;
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

            public int getUidPolicy(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    int i = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
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
                    int[] iArr = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
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

            public void setUidNavigationStart(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setUidNavigationStart(uid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setUidNavigationStop(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setUidNavigationStop(uid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean checkUidNavigationScreenLock(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().checkUidNavigationScreenLock(uid);
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

            public boolean isUidForeground(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isUidForeground(uid);
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

            public boolean checkUidLocationOp(int uid, int op) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(op);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().checkUidLocationOp(uid, op);
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

            public void registerListener(ILocationPolicyListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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

            public void unregisterListener(ILocationPolicyListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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

            public void setLocationPolicies(LocationPolicy[] policies) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedArray(policies, 0);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setLocationPolicies(policies);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public LocationPolicy[] getLocationPolicies() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    LocationPolicy[] locationPolicyArr = 12;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        locationPolicyArr = Stub.getDefaultImpl();
                        if (locationPolicyArr != 0) {
                            locationPolicyArr = Stub.getDefaultImpl().getLocationPolicies();
                            return locationPolicyArr;
                        }
                    }
                    _reply.readException();
                    LocationPolicy[] _result = (LocationPolicy[]) _reply.createTypedArray(LocationPolicy.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
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
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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
                    if (!this.mRemote.transact(14, _data, _reply, 0)) {
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

            public void setFakeGpsFeatureOnState(boolean on) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(on ? 1 : 0);
                    if (this.mRemote.transact(15, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setFakeGpsFeatureOnState(on);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setPhoneStationary(boolean stationary, Location location) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(stationary ? 1 : 0);
                    if (location != null) {
                        _data.writeInt(1);
                        location.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(16, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setPhoneStationary(stationary, location);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ILocationPolicyManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ILocationPolicyManager)) {
                return new Proxy(obj);
            }
            return (ILocationPolicyManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "setUidPolicy";
                case 2:
                    return "getUidPolicy";
                case 3:
                    return "getUidsWithPolicy";
                case 4:
                    return "setUidNavigationStart";
                case 5:
                    return "setUidNavigationStop";
                case 6:
                    return "checkUidNavigationScreenLock";
                case 7:
                    return "isUidForeground";
                case 8:
                    return "checkUidLocationOp";
                case 9:
                    return "registerListener";
                case 10:
                    return "unregisterListener";
                case 11:
                    return "setLocationPolicies";
                case 12:
                    return "getLocationPolicies";
                case 13:
                    return "setRestrictBackground";
                case 14:
                    return "getRestrictBackground";
                case 15:
                    return "setFakeGpsFeatureOnState";
                case 16:
                    return "setPhoneStationary";
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
                boolean _arg0 = false;
                boolean _result;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        setUidPolicy(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        int _result2 = getUidPolicy(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        int[] _result3 = getUidsWithPolicy(data.readInt());
                        reply.writeNoException();
                        reply.writeIntArray(_result3);
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        setUidNavigationStart(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        setUidNavigationStop(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        _result = checkUidNavigationScreenLock(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        _result = isUidForeground(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        boolean _result4 = checkUidLocationOp(data.readInt(), data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        registerListener(android.location.ILocationPolicyListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        unregisterListener(android.location.ILocationPolicyListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        setLocationPolicies((LocationPolicy[]) data.createTypedArray(LocationPolicy.CREATOR));
                        reply.writeNoException();
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        LocationPolicy[] _result5 = getLocationPolicies();
                        reply.writeNoException();
                        reply.writeTypedArray(_result5, 1);
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        setRestrictBackground(_arg0);
                        reply.writeNoException();
                        return true;
                    case 14:
                        data.enforceInterface(descriptor);
                        _arg0 = getRestrictBackground();
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    case 15:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        setFakeGpsFeatureOnState(_arg0);
                        return true;
                    case 16:
                        Location _arg1;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        if (data.readInt() != 0) {
                            _arg1 = (Location) Location.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        setPhoneStationary(_arg0, _arg1);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(ILocationPolicyManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ILocationPolicyManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    boolean checkUidLocationOp(int i, int i2) throws RemoteException;

    boolean checkUidNavigationScreenLock(int i) throws RemoteException;

    LocationPolicy[] getLocationPolicies() throws RemoteException;

    boolean getRestrictBackground() throws RemoteException;

    int getUidPolicy(int i) throws RemoteException;

    int[] getUidsWithPolicy(int i) throws RemoteException;

    boolean isUidForeground(int i) throws RemoteException;

    void registerListener(ILocationPolicyListener iLocationPolicyListener) throws RemoteException;

    void setFakeGpsFeatureOnState(boolean z) throws RemoteException;

    void setLocationPolicies(LocationPolicy[] locationPolicyArr) throws RemoteException;

    void setPhoneStationary(boolean z, Location location) throws RemoteException;

    void setRestrictBackground(boolean z) throws RemoteException;

    void setUidNavigationStart(int i) throws RemoteException;

    void setUidNavigationStop(int i) throws RemoteException;

    void setUidPolicy(int i, int i2) throws RemoteException;

    void unregisterListener(ILocationPolicyListener iLocationPolicyListener) throws RemoteException;
}
