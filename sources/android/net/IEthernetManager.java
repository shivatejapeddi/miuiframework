package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IEthernetManager extends IInterface {

    public static class Default implements IEthernetManager {
        public String[] getAvailableInterfaces() throws RemoteException {
            return null;
        }

        public IpConfiguration getConfiguration(String iface) throws RemoteException {
            return null;
        }

        public void setConfiguration(String iface, IpConfiguration config) throws RemoteException {
        }

        public boolean isAvailable(String iface) throws RemoteException {
            return false;
        }

        public void addListener(IEthernetServiceListener listener) throws RemoteException {
        }

        public void removeListener(IEthernetServiceListener listener) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IEthernetManager {
        private static final String DESCRIPTOR = "android.net.IEthernetManager";
        static final int TRANSACTION_addListener = 5;
        static final int TRANSACTION_getAvailableInterfaces = 1;
        static final int TRANSACTION_getConfiguration = 2;
        static final int TRANSACTION_isAvailable = 4;
        static final int TRANSACTION_removeListener = 6;
        static final int TRANSACTION_setConfiguration = 3;

        private static class Proxy implements IEthernetManager {
            public static IEthernetManager sDefaultImpl;
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

            public String[] getAvailableInterfaces() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String[] strArr = 1;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getAvailableInterfaces();
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

            public IpConfiguration getConfiguration(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    IpConfiguration ipConfiguration = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        ipConfiguration = Stub.getDefaultImpl();
                        if (ipConfiguration != 0) {
                            ipConfiguration = Stub.getDefaultImpl().getConfiguration(iface);
                            return ipConfiguration;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        ipConfiguration = (IpConfiguration) IpConfiguration.CREATOR.createFromParcel(_reply);
                    } else {
                        ipConfiguration = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return ipConfiguration;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setConfiguration(String iface, IpConfiguration config) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    if (config != null) {
                        _data.writeInt(1);
                        config.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setConfiguration(iface, config);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isAvailable(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isAvailable(iface);
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

            public void addListener(IEthernetServiceListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeListener(IEthernetServiceListener listener) throws RemoteException {
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
                    Stub.getDefaultImpl().removeListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IEthernetManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IEthernetManager)) {
                return new Proxy(obj);
            }
            return (IEthernetManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getAvailableInterfaces";
                case 2:
                    return "getConfiguration";
                case 3:
                    return "setConfiguration";
                case 4:
                    return "isAvailable";
                case 5:
                    return "addListener";
                case 6:
                    return "removeListener";
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
                IpConfiguration _result;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        String[] _result2 = getAvailableInterfaces();
                        reply.writeNoException();
                        reply.writeStringArray(_result2);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        _result = getConfiguration(data.readString());
                        reply.writeNoException();
                        if (_result != null) {
                            reply.writeInt(1);
                            _result.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        String _arg0 = data.readString();
                        if (data.readInt() != 0) {
                            _result = (IpConfiguration) IpConfiguration.CREATOR.createFromParcel(data);
                        } else {
                            _result = null;
                        }
                        setConfiguration(_arg0, _result);
                        reply.writeNoException();
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        boolean _result3 = isAvailable(data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result3);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        addListener(android.net.IEthernetServiceListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        removeListener(android.net.IEthernetServiceListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IEthernetManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IEthernetManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void addListener(IEthernetServiceListener iEthernetServiceListener) throws RemoteException;

    String[] getAvailableInterfaces() throws RemoteException;

    IpConfiguration getConfiguration(String str) throws RemoteException;

    boolean isAvailable(String str) throws RemoteException;

    void removeListener(IEthernetServiceListener iEthernetServiceListener) throws RemoteException;

    void setConfiguration(String str, IpConfiguration ipConfiguration) throws RemoteException;
}
