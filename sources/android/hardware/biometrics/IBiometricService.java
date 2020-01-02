package android.hardware.biometrics;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IBiometricService extends IInterface {

    public static class Default implements IBiometricService {
        public void authenticate(IBinder token, long sessionId, int userId, IBiometricServiceReceiver receiver, String opPackageName, Bundle bundle, IBiometricConfirmDeviceCredentialCallback callback) throws RemoteException {
        }

        public void cancelAuthentication(IBinder token, String opPackageName) throws RemoteException {
        }

        public int canAuthenticate(String opPackageName) throws RemoteException {
            return 0;
        }

        public void registerEnabledOnKeyguardCallback(IBiometricEnabledOnKeyguardCallback callback) throws RemoteException {
        }

        public void setActiveUser(int userId) throws RemoteException {
        }

        public void onReadyForAuthentication(int cookie, boolean requireConfirmation, int userId) throws RemoteException {
        }

        public void resetLockout(byte[] token) throws RemoteException {
        }

        public void onConfirmDeviceCredentialSuccess() throws RemoteException {
        }

        public void onConfirmDeviceCredentialError(int error, String message) throws RemoteException {
        }

        public void registerCancellationCallback(IBiometricConfirmDeviceCredentialCallback callback) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IBiometricService {
        private static final String DESCRIPTOR = "android.hardware.biometrics.IBiometricService";
        static final int TRANSACTION_authenticate = 1;
        static final int TRANSACTION_canAuthenticate = 3;
        static final int TRANSACTION_cancelAuthentication = 2;
        static final int TRANSACTION_onConfirmDeviceCredentialError = 9;
        static final int TRANSACTION_onConfirmDeviceCredentialSuccess = 8;
        static final int TRANSACTION_onReadyForAuthentication = 6;
        static final int TRANSACTION_registerCancellationCallback = 10;
        static final int TRANSACTION_registerEnabledOnKeyguardCallback = 4;
        static final int TRANSACTION_resetLockout = 7;
        static final int TRANSACTION_setActiveUser = 5;

        private static class Proxy implements IBiometricService {
            public static IBiometricService sDefaultImpl;
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

            public void authenticate(IBinder token, long sessionId, int userId, IBiometricServiceReceiver receiver, String opPackageName, Bundle bundle, IBiometricConfirmDeviceCredentialCallback callback) throws RemoteException {
                Throwable th;
                long j;
                Bundle bundle2 = bundle;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeStrongBinder(token);
                        try {
                            _data.writeLong(sessionId);
                            _data.writeInt(userId);
                            IBinder iBinder = null;
                            _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                            _data.writeString(opPackageName);
                            if (bundle2 != null) {
                                _data.writeInt(1);
                                bundle2.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            if (callback != null) {
                                iBinder = callback.asBinder();
                            }
                            _data.writeStrongBinder(iBinder);
                            if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().authenticate(token, sessionId, userId, receiver, opPackageName, bundle, callback);
                            _reply.recycle();
                            _data.recycle();
                        } catch (Throwable th2) {
                            th = th2;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        j = sessionId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    IBinder iBinder2 = token;
                    j = sessionId;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void cancelAuthentication(IBinder token, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeString(opPackageName);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().cancelAuthentication(token, opPackageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int canAuthenticate(String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(opPackageName);
                    int i = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().canAuthenticate(opPackageName);
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

            public void registerEnabledOnKeyguardCallback(IBiometricEnabledOnKeyguardCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerEnabledOnKeyguardCallback(callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setActiveUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setActiveUser(userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onReadyForAuthentication(int cookie, boolean requireConfirmation, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cookie);
                    _data.writeInt(requireConfirmation ? 1 : 0);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onReadyForAuthentication(cookie, requireConfirmation, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void resetLockout(byte[] token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(token);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().resetLockout(token);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onConfirmDeviceCredentialSuccess() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onConfirmDeviceCredentialSuccess();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onConfirmDeviceCredentialError(int error, String message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(error);
                    _data.writeString(message);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onConfirmDeviceCredentialError(error, message);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerCancellationCallback(IBiometricConfirmDeviceCredentialCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerCancellationCallback(callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IBiometricService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IBiometricService)) {
                return new Proxy(obj);
            }
            return (IBiometricService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "authenticate";
                case 2:
                    return "cancelAuthentication";
                case 3:
                    return "canAuthenticate";
                case 4:
                    return "registerEnabledOnKeyguardCallback";
                case 5:
                    return "setActiveUser";
                case 6:
                    return "onReadyForAuthentication";
                case 7:
                    return "resetLockout";
                case 8:
                    return "onConfirmDeviceCredentialSuccess";
                case 9:
                    return "onConfirmDeviceCredentialError";
                case 10:
                    return "registerCancellationCallback";
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
                switch (i) {
                    case 1:
                        Bundle _arg5;
                        parcel.enforceInterface(descriptor);
                        IBinder _arg0 = data.readStrongBinder();
                        long _arg1 = data.readLong();
                        int _arg2 = data.readInt();
                        IBiometricServiceReceiver _arg3 = android.hardware.biometrics.IBiometricServiceReceiver.Stub.asInterface(data.readStrongBinder());
                        String _arg4 = data.readString();
                        if (data.readInt() != 0) {
                            _arg5 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg5 = null;
                        }
                        authenticate(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, android.hardware.biometrics.IBiometricConfirmDeviceCredentialCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        cancelAuthentication(data.readStrongBinder(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        int _result = canAuthenticate(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        registerEnabledOnKeyguardCallback(android.hardware.biometrics.IBiometricEnabledOnKeyguardCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        setActiveUser(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        onReadyForAuthentication(data.readInt(), data.readInt() != 0, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        resetLockout(data.createByteArray());
                        reply.writeNoException();
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        onConfirmDeviceCredentialSuccess();
                        reply.writeNoException();
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        onConfirmDeviceCredentialError(data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        registerCancellationCallback(android.hardware.biometrics.IBiometricConfirmDeviceCredentialCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IBiometricService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IBiometricService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void authenticate(IBinder iBinder, long j, int i, IBiometricServiceReceiver iBiometricServiceReceiver, String str, Bundle bundle, IBiometricConfirmDeviceCredentialCallback iBiometricConfirmDeviceCredentialCallback) throws RemoteException;

    int canAuthenticate(String str) throws RemoteException;

    void cancelAuthentication(IBinder iBinder, String str) throws RemoteException;

    void onConfirmDeviceCredentialError(int i, String str) throws RemoteException;

    void onConfirmDeviceCredentialSuccess() throws RemoteException;

    void onReadyForAuthentication(int i, boolean z, int i2) throws RemoteException;

    void registerCancellationCallback(IBiometricConfirmDeviceCredentialCallback iBiometricConfirmDeviceCredentialCallback) throws RemoteException;

    void registerEnabledOnKeyguardCallback(IBiometricEnabledOnKeyguardCallback iBiometricEnabledOnKeyguardCallback) throws RemoteException;

    void resetLockout(byte[] bArr) throws RemoteException;

    void setActiveUser(int i) throws RemoteException;
}
