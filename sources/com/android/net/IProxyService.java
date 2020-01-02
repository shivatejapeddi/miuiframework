package com.android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IProxyService extends IInterface {

    public static class Default implements IProxyService {
        public String resolvePacFile(String host, String url) throws RemoteException {
            return null;
        }

        public void setPacFile(String scriptContents) throws RemoteException {
        }

        public void startPacSystem() throws RemoteException {
        }

        public void stopPacSystem() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IProxyService {
        private static final String DESCRIPTOR = "com.android.net.IProxyService";
        static final int TRANSACTION_resolvePacFile = 1;
        static final int TRANSACTION_setPacFile = 2;
        static final int TRANSACTION_startPacSystem = 3;
        static final int TRANSACTION_stopPacSystem = 4;

        private static class Proxy implements IProxyService {
            public static IProxyService sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.android.net.IProxyService";
            }

            public String resolvePacFile(String host, String url) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.android.net.IProxyService");
                    _data.writeString(host);
                    _data.writeString(url);
                    String str = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().resolvePacFile(host, url);
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

            public void setPacFile(String scriptContents) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.android.net.IProxyService");
                    _data.writeString(scriptContents);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setPacFile(scriptContents);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void startPacSystem() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.android.net.IProxyService");
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().startPacSystem();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void stopPacSystem() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.android.net.IProxyService");
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().stopPacSystem();
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, "com.android.net.IProxyService");
        }

        public static IProxyService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface("com.android.net.IProxyService");
            if (iin == null || !(iin instanceof IProxyService)) {
                return new Proxy(obj);
            }
            return (IProxyService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "resolvePacFile";
            }
            if (transactionCode == 2) {
                return "setPacFile";
            }
            if (transactionCode == 3) {
                return "startPacSystem";
            }
            if (transactionCode != 4) {
                return null;
            }
            return "stopPacSystem";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "com.android.net.IProxyService";
            if (code == 1) {
                data.enforceInterface(descriptor);
                String _result = resolvePacFile(data.readString(), data.readString());
                reply.writeNoException();
                reply.writeString(_result);
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                setPacFile(data.readString());
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                startPacSystem();
                return true;
            } else if (code == 4) {
                data.enforceInterface(descriptor);
                stopPacSystem();
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IProxyService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IProxyService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    String resolvePacFile(String str, String str2) throws RemoteException;

    void setPacFile(String str) throws RemoteException;

    void startPacSystem() throws RemoteException;

    void stopPacSystem() throws RemoteException;
}
