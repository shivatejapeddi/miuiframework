package com.android.internal.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface INetworkWatchlistManager extends IInterface {

    public static class Default implements INetworkWatchlistManager {
        public boolean startWatchlistLogging() throws RemoteException {
            return false;
        }

        public boolean stopWatchlistLogging() throws RemoteException {
            return false;
        }

        public void reloadWatchlist() throws RemoteException {
        }

        public void reportWatchlistIfNecessary() throws RemoteException {
        }

        public byte[] getWatchlistConfigHash() throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements INetworkWatchlistManager {
        private static final String DESCRIPTOR = "com.android.internal.net.INetworkWatchlistManager";
        static final int TRANSACTION_getWatchlistConfigHash = 5;
        static final int TRANSACTION_reloadWatchlist = 3;
        static final int TRANSACTION_reportWatchlistIfNecessary = 4;
        static final int TRANSACTION_startWatchlistLogging = 1;
        static final int TRANSACTION_stopWatchlistLogging = 2;

        private static class Proxy implements INetworkWatchlistManager {
            public static INetworkWatchlistManager sDefaultImpl;
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

            public boolean startWatchlistLogging() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    if (this.mRemote.transact(1, _data, _reply, z) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() != 0) {
                            z = true;
                        }
                        boolean _result = z;
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    z = Stub.getDefaultImpl().startWatchlistLogging();
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean stopWatchlistLogging() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().stopWatchlistLogging();
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

            public void reloadWatchlist() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reloadWatchlist();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportWatchlistIfNecessary() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reportWatchlistIfNecessary();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] getWatchlistConfigHash() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    byte[] bArr = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        bArr = Stub.getDefaultImpl();
                        if (bArr != 0) {
                            bArr = Stub.getDefaultImpl().getWatchlistConfigHash();
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static INetworkWatchlistManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof INetworkWatchlistManager)) {
                return new Proxy(obj);
            }
            return (INetworkWatchlistManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "startWatchlistLogging";
            }
            if (transactionCode == 2) {
                return "stopWatchlistLogging";
            }
            if (transactionCode == 3) {
                return "reloadWatchlist";
            }
            if (transactionCode == 4) {
                return "reportWatchlistIfNecessary";
            }
            if (transactionCode != 5) {
                return null;
            }
            return "getWatchlistConfigHash";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            boolean _result;
            if (code == 1) {
                data.enforceInterface(descriptor);
                _result = startWatchlistLogging();
                reply.writeNoException();
                reply.writeInt(_result);
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                _result = stopWatchlistLogging();
                reply.writeNoException();
                reply.writeInt(_result);
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                reloadWatchlist();
                reply.writeNoException();
                return true;
            } else if (code == 4) {
                data.enforceInterface(descriptor);
                reportWatchlistIfNecessary();
                reply.writeNoException();
                return true;
            } else if (code == 5) {
                data.enforceInterface(descriptor);
                byte[] _result2 = getWatchlistConfigHash();
                reply.writeNoException();
                reply.writeByteArray(_result2);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(INetworkWatchlistManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static INetworkWatchlistManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    byte[] getWatchlistConfigHash() throws RemoteException;

    void reloadWatchlist() throws RemoteException;

    void reportWatchlistIfNecessary() throws RemoteException;

    boolean startWatchlistLogging() throws RemoteException;

    boolean stopWatchlistLogging() throws RemoteException;
}
