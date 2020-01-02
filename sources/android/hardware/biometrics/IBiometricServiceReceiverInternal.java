package android.hardware.biometrics;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IBiometricServiceReceiverInternal extends IInterface {

    public static class Default implements IBiometricServiceReceiverInternal {
        public void onAuthenticationSucceeded(boolean requireConfirmation, byte[] token) throws RemoteException {
        }

        public void onAuthenticationFailed(int cookie, boolean requireConfirmation) throws RemoteException {
        }

        public void onError(int cookie, int error, String message) throws RemoteException {
        }

        public void onAcquired(int acquiredInfo, String message) throws RemoteException {
        }

        public void onDialogDismissed(int reason) throws RemoteException {
        }

        public void onTryAgainPressed() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IBiometricServiceReceiverInternal {
        private static final String DESCRIPTOR = "android.hardware.biometrics.IBiometricServiceReceiverInternal";
        static final int TRANSACTION_onAcquired = 4;
        static final int TRANSACTION_onAuthenticationFailed = 2;
        static final int TRANSACTION_onAuthenticationSucceeded = 1;
        static final int TRANSACTION_onDialogDismissed = 5;
        static final int TRANSACTION_onError = 3;
        static final int TRANSACTION_onTryAgainPressed = 6;

        private static class Proxy implements IBiometricServiceReceiverInternal {
            public static IBiometricServiceReceiverInternal sDefaultImpl;
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

            public void onAuthenticationSucceeded(boolean requireConfirmation, byte[] token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(requireConfirmation ? 1 : 0);
                    _data.writeByteArray(token);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onAuthenticationSucceeded(requireConfirmation, token);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onAuthenticationFailed(int cookie, boolean requireConfirmation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cookie);
                    _data.writeInt(requireConfirmation ? 1 : 0);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onAuthenticationFailed(cookie, requireConfirmation);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onError(int cookie, int error, String message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cookie);
                    _data.writeInt(error);
                    _data.writeString(message);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onError(cookie, error, message);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onAcquired(int acquiredInfo, String message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(acquiredInfo);
                    _data.writeString(message);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onAcquired(acquiredInfo, message);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onDialogDismissed(int reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(reason);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onDialogDismissed(reason);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onTryAgainPressed() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onTryAgainPressed();
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IBiometricServiceReceiverInternal asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IBiometricServiceReceiverInternal)) {
                return new Proxy(obj);
            }
            return (IBiometricServiceReceiverInternal) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "onAuthenticationSucceeded";
                case 2:
                    return "onAuthenticationFailed";
                case 3:
                    return "onError";
                case 4:
                    return "onAcquired";
                case 5:
                    return "onDialogDismissed";
                case 6:
                    return "onTryAgainPressed";
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
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        onAuthenticationSucceeded(_arg1, data.createByteArray());
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        int _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        onAuthenticationFailed(_arg0, _arg1);
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        onError(data.readInt(), data.readInt(), data.readString());
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        onAcquired(data.readInt(), data.readString());
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        onDialogDismissed(data.readInt());
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        onTryAgainPressed();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IBiometricServiceReceiverInternal impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IBiometricServiceReceiverInternal getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onAcquired(int i, String str) throws RemoteException;

    void onAuthenticationFailed(int i, boolean z) throws RemoteException;

    void onAuthenticationSucceeded(boolean z, byte[] bArr) throws RemoteException;

    void onDialogDismissed(int i) throws RemoteException;

    void onError(int i, int i2, String str) throws RemoteException;

    void onTryAgainPressed() throws RemoteException;
}
