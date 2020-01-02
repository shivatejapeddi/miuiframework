package android.app.trust;

import android.hardware.biometrics.BiometricSourceType;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ITrustManager extends IInterface {

    public static class Default implements ITrustManager {
        public void reportUnlockAttempt(boolean successful, int userId) throws RemoteException {
        }

        public void reportUnlockLockout(int timeoutMs, int userId) throws RemoteException {
        }

        public void reportEnabledTrustAgentsChanged(int userId) throws RemoteException {
        }

        public void registerTrustListener(ITrustListener trustListener) throws RemoteException {
        }

        public void unregisterTrustListener(ITrustListener trustListener) throws RemoteException {
        }

        public void reportKeyguardShowingChanged() throws RemoteException {
        }

        public void setDeviceLockedForUser(int userId, boolean locked) throws RemoteException {
        }

        public boolean isDeviceLocked(int userId) throws RemoteException {
            return false;
        }

        public boolean isDeviceSecure(int userId) throws RemoteException {
            return false;
        }

        public boolean isTrustUsuallyManaged(int userId) throws RemoteException {
            return false;
        }

        public void unlockedByBiometricForUser(int userId, BiometricSourceType source) throws RemoteException {
        }

        public void clearAllBiometricRecognized(BiometricSourceType target) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ITrustManager {
        private static final String DESCRIPTOR = "android.app.trust.ITrustManager";
        static final int TRANSACTION_clearAllBiometricRecognized = 12;
        static final int TRANSACTION_isDeviceLocked = 8;
        static final int TRANSACTION_isDeviceSecure = 9;
        static final int TRANSACTION_isTrustUsuallyManaged = 10;
        static final int TRANSACTION_registerTrustListener = 4;
        static final int TRANSACTION_reportEnabledTrustAgentsChanged = 3;
        static final int TRANSACTION_reportKeyguardShowingChanged = 6;
        static final int TRANSACTION_reportUnlockAttempt = 1;
        static final int TRANSACTION_reportUnlockLockout = 2;
        static final int TRANSACTION_setDeviceLockedForUser = 7;
        static final int TRANSACTION_unlockedByBiometricForUser = 11;
        static final int TRANSACTION_unregisterTrustListener = 5;

        private static class Proxy implements ITrustManager {
            public static ITrustManager sDefaultImpl;
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

            public void reportUnlockAttempt(boolean successful, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(successful ? 1 : 0);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reportUnlockAttempt(successful, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportUnlockLockout(int timeoutMs, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(timeoutMs);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reportUnlockLockout(timeoutMs, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportEnabledTrustAgentsChanged(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reportEnabledTrustAgentsChanged(userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerTrustListener(ITrustListener trustListener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(trustListener != null ? trustListener.asBinder() : null);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerTrustListener(trustListener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterTrustListener(ITrustListener trustListener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(trustListener != null ? trustListener.asBinder() : null);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterTrustListener(trustListener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportKeyguardShowingChanged() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reportKeyguardShowingChanged();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setDeviceLockedForUser(int userId, boolean locked) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(locked ? 1 : 0);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setDeviceLockedForUser(userId, locked);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isDeviceLocked(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isDeviceLocked(userId);
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

            public boolean isDeviceSecure(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isDeviceSecure(userId);
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

            public boolean isTrustUsuallyManaged(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isTrustUsuallyManaged(userId);
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

            public void unlockedByBiometricForUser(int userId, BiometricSourceType source) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    if (source != null) {
                        _data.writeInt(1);
                        source.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unlockedByBiometricForUser(userId, source);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearAllBiometricRecognized(BiometricSourceType target) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (target != null) {
                        _data.writeInt(1);
                        target.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearAllBiometricRecognized(target);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ITrustManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ITrustManager)) {
                return new Proxy(obj);
            }
            return (ITrustManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "reportUnlockAttempt";
                case 2:
                    return "reportUnlockLockout";
                case 3:
                    return "reportEnabledTrustAgentsChanged";
                case 4:
                    return "registerTrustListener";
                case 5:
                    return "unregisterTrustListener";
                case 6:
                    return "reportKeyguardShowingChanged";
                case 7:
                    return "setDeviceLockedForUser";
                case 8:
                    return "isDeviceLocked";
                case 9:
                    return "isDeviceSecure";
                case 10:
                    return "isTrustUsuallyManaged";
                case 11:
                    return "unlockedByBiometricForUser";
                case 12:
                    return "clearAllBiometricRecognized";
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
                boolean _result;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        reportUnlockAttempt(_arg1, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        reportUnlockLockout(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        reportEnabledTrustAgentsChanged(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        registerTrustListener(android.app.trust.ITrustListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        unregisterTrustListener(android.app.trust.ITrustListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        reportKeyguardShowingChanged();
                        reply.writeNoException();
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        int _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setDeviceLockedForUser(_arg0, _arg1);
                        reply.writeNoException();
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        _result = isDeviceLocked(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        _result = isDeviceSecure(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        _result = isTrustUsuallyManaged(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 11:
                        BiometricSourceType _arg12;
                        data.enforceInterface(descriptor);
                        int _arg02 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg12 = (BiometricSourceType) BiometricSourceType.CREATOR.createFromParcel(data);
                        } else {
                            _arg12 = null;
                        }
                        unlockedByBiometricForUser(_arg02, _arg12);
                        reply.writeNoException();
                        return true;
                    case 12:
                        BiometricSourceType _arg03;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (BiometricSourceType) BiometricSourceType.CREATOR.createFromParcel(data);
                        } else {
                            _arg03 = null;
                        }
                        clearAllBiometricRecognized(_arg03);
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(ITrustManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ITrustManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void clearAllBiometricRecognized(BiometricSourceType biometricSourceType) throws RemoteException;

    boolean isDeviceLocked(int i) throws RemoteException;

    boolean isDeviceSecure(int i) throws RemoteException;

    boolean isTrustUsuallyManaged(int i) throws RemoteException;

    void registerTrustListener(ITrustListener iTrustListener) throws RemoteException;

    void reportEnabledTrustAgentsChanged(int i) throws RemoteException;

    void reportKeyguardShowingChanged() throws RemoteException;

    void reportUnlockAttempt(boolean z, int i) throws RemoteException;

    void reportUnlockLockout(int i, int i2) throws RemoteException;

    void setDeviceLockedForUser(int i, boolean z) throws RemoteException;

    void unlockedByBiometricForUser(int i, BiometricSourceType biometricSourceType) throws RemoteException;

    void unregisterTrustListener(ITrustListener iTrustListener) throws RemoteException;
}
