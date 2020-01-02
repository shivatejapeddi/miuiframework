package android.hardware.face;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IFaceServiceReceiver extends IInterface {

    public static abstract class Stub extends Binder implements IFaceServiceReceiver {
        private static final String DESCRIPTOR = "android.hardware.face.IFaceServiceReceiver";
        static final int TRANSACTION_onAcquired = 2;
        static final int TRANSACTION_onAuthenticationFailed = 4;
        static final int TRANSACTION_onAuthenticationSucceeded = 3;
        static final int TRANSACTION_onEnrollResult = 1;
        static final int TRANSACTION_onEnumerated = 7;
        static final int TRANSACTION_onError = 5;
        static final int TRANSACTION_onFeatureGet = 9;
        static final int TRANSACTION_onFeatureSet = 8;
        static final int TRANSACTION_onRemoved = 6;

        private static class Proxy implements IFaceServiceReceiver {
            public static IFaceServiceReceiver sDefaultImpl;
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

            public void onEnrollResult(long deviceId, int faceId, int remaining) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(deviceId);
                    _data.writeInt(faceId);
                    _data.writeInt(remaining);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onEnrollResult(deviceId, faceId, remaining);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onAcquired(long deviceId, int acquiredInfo, int vendorCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(deviceId);
                    _data.writeInt(acquiredInfo);
                    _data.writeInt(vendorCode);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onAcquired(deviceId, acquiredInfo, vendorCode);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onAuthenticationSucceeded(long deviceId, Face face, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(deviceId);
                    if (face != null) {
                        _data.writeInt(1);
                        face.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onAuthenticationSucceeded(deviceId, face, userId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onAuthenticationFailed(long deviceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(deviceId);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onAuthenticationFailed(deviceId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onError(long deviceId, int error, int vendorCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(deviceId);
                    _data.writeInt(error);
                    _data.writeInt(vendorCode);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onError(deviceId, error, vendorCode);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onRemoved(long deviceId, int faceId, int remaining) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(deviceId);
                    _data.writeInt(faceId);
                    _data.writeInt(remaining);
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onRemoved(deviceId, faceId, remaining);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onEnumerated(long deviceId, int faceId, int remaining) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(deviceId);
                    _data.writeInt(faceId);
                    _data.writeInt(remaining);
                    if (this.mRemote.transact(7, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onEnumerated(deviceId, faceId, remaining);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onFeatureSet(boolean success, int feature) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(success ? 1 : 0);
                    _data.writeInt(feature);
                    if (this.mRemote.transact(8, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onFeatureSet(success, feature);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onFeatureGet(boolean success, int feature, boolean value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    _data.writeInt(success ? 1 : 0);
                    _data.writeInt(feature);
                    if (value) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(9, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onFeatureGet(success, feature, value);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IFaceServiceReceiver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IFaceServiceReceiver)) {
                return new Proxy(obj);
            }
            return (IFaceServiceReceiver) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "onEnrollResult";
                case 2:
                    return "onAcquired";
                case 3:
                    return "onAuthenticationSucceeded";
                case 4:
                    return "onAuthenticationFailed";
                case 5:
                    return "onError";
                case 6:
                    return "onRemoved";
                case 7:
                    return "onEnumerated";
                case 8:
                    return "onFeatureSet";
                case 9:
                    return "onFeatureGet";
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
                boolean _arg2 = false;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        onEnrollResult(data.readLong(), data.readInt(), data.readInt());
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        onAcquired(data.readLong(), data.readInt(), data.readInt());
                        return true;
                    case 3:
                        Face _arg1;
                        data.enforceInterface(descriptor);
                        long _arg0 = data.readLong();
                        if (data.readInt() != 0) {
                            _arg1 = (Face) Face.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        onAuthenticationSucceeded(_arg0, _arg1, data.readInt());
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        onAuthenticationFailed(data.readLong());
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        onError(data.readLong(), data.readInt(), data.readInt());
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        onRemoved(data.readLong(), data.readInt(), data.readInt());
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        onEnumerated(data.readLong(), data.readInt(), data.readInt());
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        onFeatureSet(_arg2, data.readInt());
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        boolean _arg02 = data.readInt() != 0;
                        int _arg12 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        onFeatureGet(_arg02, _arg12, _arg2);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IFaceServiceReceiver impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IFaceServiceReceiver getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    public static class Default implements IFaceServiceReceiver {
        public void onEnrollResult(long deviceId, int faceId, int remaining) throws RemoteException {
        }

        public void onAcquired(long deviceId, int acquiredInfo, int vendorCode) throws RemoteException {
        }

        public void onAuthenticationSucceeded(long deviceId, Face face, int userId) throws RemoteException {
        }

        public void onAuthenticationFailed(long deviceId) throws RemoteException {
        }

        public void onError(long deviceId, int error, int vendorCode) throws RemoteException {
        }

        public void onRemoved(long deviceId, int faceId, int remaining) throws RemoteException {
        }

        public void onEnumerated(long deviceId, int faceId, int remaining) throws RemoteException {
        }

        public void onFeatureSet(boolean success, int feature) throws RemoteException {
        }

        public void onFeatureGet(boolean success, int feature, boolean value) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    void onAcquired(long j, int i, int i2) throws RemoteException;

    void onAuthenticationFailed(long j) throws RemoteException;

    void onAuthenticationSucceeded(long j, Face face, int i) throws RemoteException;

    void onEnrollResult(long j, int i, int i2) throws RemoteException;

    void onEnumerated(long j, int i, int i2) throws RemoteException;

    void onError(long j, int i, int i2) throws RemoteException;

    void onFeatureGet(boolean z, int i, boolean z2) throws RemoteException;

    void onFeatureSet(boolean z, int i) throws RemoteException;

    void onRemoved(long j, int i, int i2) throws RemoteException;
}
