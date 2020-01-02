package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IIpConnectivityMetrics extends IInterface {

    public static class Default implements IIpConnectivityMetrics {
        public int logEvent(ConnectivityMetricsEvent event) throws RemoteException {
            return 0;
        }

        public boolean addNetdEventCallback(int callerType, INetdEventCallback callback) throws RemoteException {
            return false;
        }

        public boolean removeNetdEventCallback(int callerType) throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IIpConnectivityMetrics {
        private static final String DESCRIPTOR = "android.net.IIpConnectivityMetrics";
        static final int TRANSACTION_addNetdEventCallback = 2;
        static final int TRANSACTION_logEvent = 1;
        static final int TRANSACTION_removeNetdEventCallback = 3;

        private static class Proxy implements IIpConnectivityMetrics {
            public static IIpConnectivityMetrics sDefaultImpl;
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

            public int logEvent(ConnectivityMetricsEvent event) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    if (event != null) {
                        _data.writeInt(1);
                        event.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().logEvent(event);
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

            public boolean addNetdEventCallback(int callerType, INetdEventCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(callerType);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().addNetdEventCallback(callerType, callback);
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

            public boolean removeNetdEventCallback(int callerType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(callerType);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().removeNetdEventCallback(callerType);
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IIpConnectivityMetrics asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IIpConnectivityMetrics)) {
                return new Proxy(obj);
            }
            return (IIpConnectivityMetrics) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "logEvent";
            }
            if (transactionCode == 2) {
                return "addNetdEventCallback";
            }
            if (transactionCode != 3) {
                return null;
            }
            return "removeNetdEventCallback";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                ConnectivityMetricsEvent _arg0;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (ConnectivityMetricsEvent) ConnectivityMetricsEvent.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                int _result = logEvent(_arg0);
                reply.writeNoException();
                reply.writeInt(_result);
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                boolean _result2 = addNetdEventCallback(data.readInt(), android.net.INetdEventCallback.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                reply.writeInt(_result2);
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                boolean _result3 = removeNetdEventCallback(data.readInt());
                reply.writeNoException();
                reply.writeInt(_result3);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IIpConnectivityMetrics impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IIpConnectivityMetrics getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    boolean addNetdEventCallback(int i, INetdEventCallback iNetdEventCallback) throws RemoteException;

    int logEvent(ConnectivityMetricsEvent connectivityMetricsEvent) throws RemoteException;

    boolean removeNetdEventCallback(int i) throws RemoteException;
}
