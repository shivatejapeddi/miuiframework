package android.se.omapi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISecureElementService extends IInterface {

    public static class Default implements ISecureElementService {
        public String[] getReaders() throws RemoteException {
            return null;
        }

        public String getSpiSignedPK() throws RemoteException {
            return null;
        }

        public ISecureElementReader getReader(String reader) throws RemoteException {
            return null;
        }

        public boolean[] isNFCEventAllowed(String reader, byte[] aid, String[] packageNames) throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ISecureElementService {
        private static final String DESCRIPTOR = "android.se.omapi.ISecureElementService";
        static final int TRANSACTION_getReader = 3;
        static final int TRANSACTION_getReaders = 1;
        static final int TRANSACTION_getSpiSignedPK = 2;
        static final int TRANSACTION_isNFCEventAllowed = 4;

        private static class Proxy implements ISecureElementService {
            public static ISecureElementService sDefaultImpl;
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

            public String[] getReaders() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String[] strArr = 1;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getReaders();
                            return strArr;
                        }
                    }
                    _reply.readException();
                    strArr = _reply.createStringArray();
                    String[] _result = strArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getSpiSignedPK() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getSpiSignedPK();
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

            public ISecureElementReader getReader(String reader) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(reader);
                    ISecureElementReader iSecureElementReader = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        iSecureElementReader = Stub.getDefaultImpl();
                        if (iSecureElementReader != 0) {
                            iSecureElementReader = Stub.getDefaultImpl().getReader(reader);
                            return iSecureElementReader;
                        }
                    }
                    _reply.readException();
                    iSecureElementReader = android.se.omapi.ISecureElementReader.Stub.asInterface(_reply.readStrongBinder());
                    ISecureElementReader _result = iSecureElementReader;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean[] isNFCEventAllowed(String reader, byte[] aid, String[] packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(reader);
                    _data.writeByteArray(aid);
                    _data.writeStringArray(packageNames);
                    boolean[] zArr = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        zArr = Stub.getDefaultImpl();
                        if (zArr != 0) {
                            zArr = Stub.getDefaultImpl().isNFCEventAllowed(reader, aid, packageNames);
                            return zArr;
                        }
                    }
                    _reply.readException();
                    zArr = _reply.createBooleanArray();
                    boolean[] _result = zArr;
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

        public static ISecureElementService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ISecureElementService)) {
                return new Proxy(obj);
            }
            return (ISecureElementService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "getReaders";
            }
            if (transactionCode == 2) {
                return "getSpiSignedPK";
            }
            if (transactionCode == 3) {
                return "getReader";
            }
            if (transactionCode != 4) {
                return null;
            }
            return "isNFCEventAllowed";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                String[] _result = getReaders();
                reply.writeNoException();
                reply.writeStringArray(_result);
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                String _result2 = getSpiSignedPK();
                reply.writeNoException();
                reply.writeString(_result2);
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                ISecureElementReader _result3 = getReader(data.readString());
                reply.writeNoException();
                reply.writeStrongBinder(_result3 != null ? _result3.asBinder() : null);
                return true;
            } else if (code == 4) {
                data.enforceInterface(descriptor);
                boolean[] _result4 = isNFCEventAllowed(data.readString(), data.createByteArray(), data.createStringArray());
                reply.writeNoException();
                reply.writeBooleanArray(_result4);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(ISecureElementService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ISecureElementService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    ISecureElementReader getReader(String str) throws RemoteException;

    String[] getReaders() throws RemoteException;

    String getSpiSignedPK() throws RemoteException;

    boolean[] isNFCEventAllowed(String str, byte[] bArr, String[] strArr) throws RemoteException;
}
