package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface INetworkManagementEventObserver extends IInterface {

    public static class Default implements INetworkManagementEventObserver {
        public void interfaceStatusChanged(String iface, boolean up) throws RemoteException {
        }

        public void interfaceLinkStateChanged(String iface, boolean up) throws RemoteException {
        }

        public void interfaceAdded(String iface) throws RemoteException {
        }

        public void interfaceRemoved(String iface) throws RemoteException {
        }

        public void addressUpdated(String iface, LinkAddress address) throws RemoteException {
        }

        public void addressRemoved(String iface, LinkAddress address) throws RemoteException {
        }

        public void limitReached(String limitName, String iface) throws RemoteException {
        }

        public void interfaceClassDataActivityChanged(String label, boolean active, long tsNanos) throws RemoteException {
        }

        public void interfaceDnsServerInfo(String iface, long lifetime, String[] servers) throws RemoteException {
        }

        public void routeUpdated(RouteInfo route) throws RemoteException {
        }

        public void routeRemoved(RouteInfo route) throws RemoteException {
        }

        public void interfaceConfigurationLost() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements INetworkManagementEventObserver {
        private static final String DESCRIPTOR = "android.net.INetworkManagementEventObserver";
        static final int TRANSACTION_addressRemoved = 6;
        static final int TRANSACTION_addressUpdated = 5;
        static final int TRANSACTION_interfaceAdded = 3;
        static final int TRANSACTION_interfaceClassDataActivityChanged = 8;
        static final int TRANSACTION_interfaceConfigurationLost = 12;
        static final int TRANSACTION_interfaceDnsServerInfo = 9;
        static final int TRANSACTION_interfaceLinkStateChanged = 2;
        static final int TRANSACTION_interfaceRemoved = 4;
        static final int TRANSACTION_interfaceStatusChanged = 1;
        static final int TRANSACTION_limitReached = 7;
        static final int TRANSACTION_routeRemoved = 11;
        static final int TRANSACTION_routeUpdated = 10;

        private static class Proxy implements INetworkManagementEventObserver {
            public static INetworkManagementEventObserver sDefaultImpl;
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

            public void interfaceStatusChanged(String iface, boolean up) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeInt(up ? 1 : 0);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().interfaceStatusChanged(iface, up);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void interfaceLinkStateChanged(String iface, boolean up) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeInt(up ? 1 : 0);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().interfaceLinkStateChanged(iface, up);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void interfaceAdded(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().interfaceAdded(iface);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void interfaceRemoved(String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().interfaceRemoved(iface);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void addressUpdated(String iface, LinkAddress address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    if (address != null) {
                        _data.writeInt(1);
                        address.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().addressUpdated(iface, address);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void addressRemoved(String iface, LinkAddress address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    if (address != null) {
                        _data.writeInt(1);
                        address.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().addressRemoved(iface, address);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void limitReached(String limitName, String iface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(limitName);
                    _data.writeString(iface);
                    if (this.mRemote.transact(7, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().limitReached(limitName, iface);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void interfaceClassDataActivityChanged(String label, boolean active, long tsNanos) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(label);
                    _data.writeInt(active ? 1 : 0);
                    _data.writeLong(tsNanos);
                    if (this.mRemote.transact(8, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().interfaceClassDataActivityChanged(label, active, tsNanos);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void interfaceDnsServerInfo(String iface, long lifetime, String[] servers) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeLong(lifetime);
                    _data.writeStringArray(servers);
                    if (this.mRemote.transact(9, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().interfaceDnsServerInfo(iface, lifetime, servers);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void routeUpdated(RouteInfo route) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (route != null) {
                        _data.writeInt(1);
                        route.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(10, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().routeUpdated(route);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void routeRemoved(RouteInfo route) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (route != null) {
                        _data.writeInt(1);
                        route.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(11, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().routeRemoved(route);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void interfaceConfigurationLost() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(12, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().interfaceConfigurationLost();
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static INetworkManagementEventObserver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof INetworkManagementEventObserver)) {
                return new Proxy(obj);
            }
            return (INetworkManagementEventObserver) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "interfaceStatusChanged";
                case 2:
                    return "interfaceLinkStateChanged";
                case 3:
                    return "interfaceAdded";
                case 4:
                    return "interfaceRemoved";
                case 5:
                    return "addressUpdated";
                case 6:
                    return "addressRemoved";
                case 7:
                    return "limitReached";
                case 8:
                    return "interfaceClassDataActivityChanged";
                case 9:
                    return "interfaceDnsServerInfo";
                case 10:
                    return "routeUpdated";
                case 11:
                    return "routeRemoved";
                case 12:
                    return "interfaceConfigurationLost";
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
                String _arg0;
                String _arg02;
                LinkAddress _arg12;
                RouteInfo _arg03;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        interfaceStatusChanged(_arg0, _arg1);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        interfaceLinkStateChanged(_arg0, _arg1);
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        interfaceAdded(data.readString());
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        interfaceRemoved(data.readString());
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg12 = (LinkAddress) LinkAddress.CREATOR.createFromParcel(data);
                        } else {
                            _arg12 = null;
                        }
                        addressUpdated(_arg02, _arg12);
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg12 = (LinkAddress) LinkAddress.CREATOR.createFromParcel(data);
                        } else {
                            _arg12 = null;
                        }
                        addressRemoved(_arg02, _arg12);
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        limitReached(data.readString(), data.readString());
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        interfaceClassDataActivityChanged(_arg0, _arg1, data.readLong());
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        interfaceDnsServerInfo(data.readString(), data.readLong(), data.createStringArray());
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (RouteInfo) RouteInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg03 = null;
                        }
                        routeUpdated(_arg03);
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (RouteInfo) RouteInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg03 = null;
                        }
                        routeRemoved(_arg03);
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        interfaceConfigurationLost();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(INetworkManagementEventObserver impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static INetworkManagementEventObserver getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void addressRemoved(String str, LinkAddress linkAddress) throws RemoteException;

    void addressUpdated(String str, LinkAddress linkAddress) throws RemoteException;

    void interfaceAdded(String str) throws RemoteException;

    void interfaceClassDataActivityChanged(String str, boolean z, long j) throws RemoteException;

    void interfaceConfigurationLost() throws RemoteException;

    void interfaceDnsServerInfo(String str, long j, String[] strArr) throws RemoteException;

    void interfaceLinkStateChanged(String str, boolean z) throws RemoteException;

    void interfaceRemoved(String str) throws RemoteException;

    void interfaceStatusChanged(String str, boolean z) throws RemoteException;

    void limitReached(String str, String str2) throws RemoteException;

    void routeRemoved(RouteInfo routeInfo) throws RemoteException;

    void routeUpdated(RouteInfo routeInfo) throws RemoteException;
}
