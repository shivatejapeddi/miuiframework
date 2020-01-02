package android.service.gatekeeper;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IGateKeeperService extends IInterface {

    public static class Default implements IGateKeeperService {
        public GateKeeperResponse enroll(int uid, byte[] currentPasswordHandle, byte[] currentPassword, byte[] desiredPassword) throws RemoteException {
            return null;
        }

        public GateKeeperResponse verify(int uid, byte[] enrolledPasswordHandle, byte[] providedPassword) throws RemoteException {
            return null;
        }

        public GateKeeperResponse verifyChallenge(int uid, long challenge, byte[] enrolledPasswordHandle, byte[] providedPassword) throws RemoteException {
            return null;
        }

        public long getSecureUserId(int uid) throws RemoteException {
            return 0;
        }

        public void clearSecureUserId(int uid) throws RemoteException {
        }

        public void reportDeviceSetupComplete() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IGateKeeperService {
        private static final String DESCRIPTOR = "android.service.gatekeeper.IGateKeeperService";
        static final int TRANSACTION_clearSecureUserId = 5;
        static final int TRANSACTION_enroll = 1;
        static final int TRANSACTION_getSecureUserId = 4;
        static final int TRANSACTION_reportDeviceSetupComplete = 6;
        static final int TRANSACTION_verify = 2;
        static final int TRANSACTION_verifyChallenge = 3;

        private static class Proxy implements IGateKeeperService {
            public static IGateKeeperService sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "android.service.gatekeeper.IGateKeeperService";
            }

            public GateKeeperResponse enroll(int uid, byte[] currentPasswordHandle, byte[] currentPassword, byte[] desiredPassword) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.service.gatekeeper.IGateKeeperService");
                    _data.writeInt(uid);
                    _data.writeByteArray(currentPasswordHandle);
                    _data.writeByteArray(currentPassword);
                    _data.writeByteArray(desiredPassword);
                    GateKeeperResponse gateKeeperResponse = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        gateKeeperResponse = Stub.getDefaultImpl();
                        if (gateKeeperResponse != 0) {
                            gateKeeperResponse = Stub.getDefaultImpl().enroll(uid, currentPasswordHandle, currentPassword, desiredPassword);
                            return gateKeeperResponse;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        gateKeeperResponse = (GateKeeperResponse) GateKeeperResponse.CREATOR.createFromParcel(_reply);
                    } else {
                        gateKeeperResponse = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return gateKeeperResponse;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public GateKeeperResponse verify(int uid, byte[] enrolledPasswordHandle, byte[] providedPassword) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.service.gatekeeper.IGateKeeperService");
                    _data.writeInt(uid);
                    _data.writeByteArray(enrolledPasswordHandle);
                    _data.writeByteArray(providedPassword);
                    GateKeeperResponse gateKeeperResponse = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        gateKeeperResponse = Stub.getDefaultImpl();
                        if (gateKeeperResponse != 0) {
                            gateKeeperResponse = Stub.getDefaultImpl().verify(uid, enrolledPasswordHandle, providedPassword);
                            return gateKeeperResponse;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        gateKeeperResponse = (GateKeeperResponse) GateKeeperResponse.CREATOR.createFromParcel(_reply);
                    } else {
                        gateKeeperResponse = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return gateKeeperResponse;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public GateKeeperResponse verifyChallenge(int uid, long challenge, byte[] enrolledPasswordHandle, byte[] providedPassword) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.service.gatekeeper.IGateKeeperService");
                    _data.writeInt(uid);
                    _data.writeLong(challenge);
                    _data.writeByteArray(enrolledPasswordHandle);
                    _data.writeByteArray(providedPassword);
                    GateKeeperResponse gateKeeperResponse = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        gateKeeperResponse = Stub.getDefaultImpl();
                        if (gateKeeperResponse != 0) {
                            gateKeeperResponse = Stub.getDefaultImpl().verifyChallenge(uid, challenge, enrolledPasswordHandle, providedPassword);
                            return gateKeeperResponse;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        gateKeeperResponse = (GateKeeperResponse) GateKeeperResponse.CREATOR.createFromParcel(_reply);
                    } else {
                        gateKeeperResponse = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return gateKeeperResponse;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getSecureUserId(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.service.gatekeeper.IGateKeeperService");
                    _data.writeInt(uid);
                    long j = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getSecureUserId(uid);
                            return j;
                        }
                    }
                    _reply.readException();
                    j = _reply.readLong();
                    long _result = j;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearSecureUserId(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.service.gatekeeper.IGateKeeperService");
                    _data.writeInt(uid);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearSecureUserId(uid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportDeviceSetupComplete() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.service.gatekeeper.IGateKeeperService");
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reportDeviceSetupComplete();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, "android.service.gatekeeper.IGateKeeperService");
        }

        public static IGateKeeperService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface("android.service.gatekeeper.IGateKeeperService");
            if (iin == null || !(iin instanceof IGateKeeperService)) {
                return new Proxy(obj);
            }
            return (IGateKeeperService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "enroll";
                case 2:
                    return "verify";
                case 3:
                    return "verifyChallenge";
                case 4:
                    return "getSecureUserId";
                case 5:
                    return "clearSecureUserId";
                case 6:
                    return "reportDeviceSetupComplete";
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
            String descriptor = "android.service.gatekeeper.IGateKeeperService";
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        GateKeeperResponse _result = enroll(data.readInt(), data.createByteArray(), data.createByteArray(), data.createByteArray());
                        reply.writeNoException();
                        if (_result != null) {
                            parcel2.writeInt(1);
                            _result.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        GateKeeperResponse _result2 = verify(data.readInt(), data.createByteArray(), data.createByteArray());
                        reply.writeNoException();
                        if (_result2 != null) {
                            parcel2.writeInt(1);
                            _result2.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        GateKeeperResponse _result3 = verifyChallenge(data.readInt(), data.readLong(), data.createByteArray(), data.createByteArray());
                        reply.writeNoException();
                        if (_result3 != null) {
                            parcel2.writeInt(1);
                            _result3.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        long _result4 = getSecureUserId(data.readInt());
                        reply.writeNoException();
                        parcel2.writeLong(_result4);
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        clearSecureUserId(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        reportDeviceSetupComplete();
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IGateKeeperService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IGateKeeperService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void clearSecureUserId(int i) throws RemoteException;

    GateKeeperResponse enroll(int i, byte[] bArr, byte[] bArr2, byte[] bArr3) throws RemoteException;

    long getSecureUserId(int i) throws RemoteException;

    void reportDeviceSetupComplete() throws RemoteException;

    GateKeeperResponse verify(int i, byte[] bArr, byte[] bArr2) throws RemoteException;

    GateKeeperResponse verifyChallenge(int i, long j, byte[] bArr, byte[] bArr2) throws RemoteException;
}
