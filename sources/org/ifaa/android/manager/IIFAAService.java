package org.ifaa.android.manager;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IIFAAService extends IInterface {

    public static class Default implements IIFAAService {
        public byte[] processCmd_v2(byte[] param) throws RemoteException {
            return null;
        }

        public int[] getIDList(int bioType) throws RemoteException {
            return null;
        }

        public int faceEnroll(String sessionId, int flags) throws RemoteException {
            return 0;
        }

        public int faceUpgrade(int action, String path, int offset, byte[] data, int data_len) throws RemoteException {
            return 0;
        }

        public int faceAuthenticate_v2(String sessionId, int flags) throws RemoteException {
            return 0;
        }

        public int faceCancel_v2(String sessionId) throws RemoteException {
            return 0;
        }

        public byte[] faceInvokeCommand(byte[] param) throws RemoteException {
            return null;
        }

        public int faceGetCellinfo() throws RemoteException {
            return 0;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IIFAAService {
        private static final String DESCRIPTOR = "org.ifaa.android.manager.IIFAAService";
        static final int TRANSACTION_faceAuthenticate_v2 = 5;
        static final int TRANSACTION_faceCancel_v2 = 6;
        static final int TRANSACTION_faceEnroll = 3;
        static final int TRANSACTION_faceGetCellinfo = 8;
        static final int TRANSACTION_faceInvokeCommand = 7;
        static final int TRANSACTION_faceUpgrade = 4;
        static final int TRANSACTION_getIDList = 2;
        static final int TRANSACTION_processCmd_v2 = 1;

        private static class Proxy implements IIFAAService {
            public static IIFAAService sDefaultImpl;
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

            public byte[] processCmd_v2(byte[] param) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(param);
                    byte[] bArr = 1;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        bArr = Stub.getDefaultImpl();
                        if (bArr != 0) {
                            bArr = Stub.getDefaultImpl().processCmd_v2(param);
                            return bArr;
                        }
                    }
                    _reply.readException();
                    bArr = _reply.createByteArray();
                    byte[] _result = bArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] getIDList(int bioType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(bioType);
                    int[] iArr = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        iArr = Stub.getDefaultImpl();
                        if (iArr != 0) {
                            iArr = Stub.getDefaultImpl().getIDList(bioType);
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

            public int faceEnroll(String sessionId, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(sessionId);
                    _data.writeInt(flags);
                    int i = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().faceEnroll(sessionId, flags);
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

            public int faceUpgrade(int action, String path, int offset, byte[] data, int data_len) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(action);
                    _data.writeString(path);
                    _data.writeInt(offset);
                    _data.writeByteArray(data);
                    _data.writeInt(data_len);
                    int i = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().faceUpgrade(action, path, offset, data, data_len);
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

            public int faceAuthenticate_v2(String sessionId, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(sessionId);
                    _data.writeInt(flags);
                    int i = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().faceAuthenticate_v2(sessionId, flags);
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

            public int faceCancel_v2(String sessionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(sessionId);
                    int i = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().faceCancel_v2(sessionId);
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

            public byte[] faceInvokeCommand(byte[] param) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(param);
                    byte[] bArr = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        bArr = Stub.getDefaultImpl();
                        if (bArr != 0) {
                            bArr = Stub.getDefaultImpl().faceInvokeCommand(param);
                            return bArr;
                        }
                    }
                    _reply.readException();
                    bArr = _reply.createByteArray();
                    byte[] _result = bArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int faceGetCellinfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 8;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().faceGetCellinfo();
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IIFAAService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IIFAAService)) {
                return new Proxy(obj);
            }
            return (IIFAAService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "processCmd_v2";
                case 2:
                    return "getIDList";
                case 3:
                    return "faceEnroll";
                case 4:
                    return "faceUpgrade";
                case 5:
                    return "faceAuthenticate_v2";
                case 6:
                    return "faceCancel_v2";
                case 7:
                    return "faceInvokeCommand";
                case 8:
                    return "faceGetCellinfo";
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
                byte[] _result;
                int _result2;
                int _result3;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        _result = processCmd_v2(data.createByteArray());
                        reply.writeNoException();
                        parcel2.writeByteArray(_result);
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        int[] _result4 = getIDList(data.readInt());
                        reply.writeNoException();
                        parcel2.writeIntArray(_result4);
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        _result2 = faceEnroll(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        _result3 = faceUpgrade(data.readInt(), data.readString(), data.readInt(), data.createByteArray(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        _result2 = faceAuthenticate_v2(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        int _result5 = faceCancel_v2(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        _result = faceInvokeCommand(data.createByteArray());
                        reply.writeNoException();
                        parcel2.writeByteArray(_result);
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        _result3 = faceGetCellinfo();
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IIFAAService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IIFAAService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    int faceAuthenticate_v2(String str, int i) throws RemoteException;

    int faceCancel_v2(String str) throws RemoteException;

    int faceEnroll(String str, int i) throws RemoteException;

    int faceGetCellinfo() throws RemoteException;

    byte[] faceInvokeCommand(byte[] bArr) throws RemoteException;

    int faceUpgrade(int i, String str, int i2, byte[] bArr, int i3) throws RemoteException;

    int[] getIDList(int i) throws RemoteException;

    byte[] processCmd_v2(byte[] bArr) throws RemoteException;
}
