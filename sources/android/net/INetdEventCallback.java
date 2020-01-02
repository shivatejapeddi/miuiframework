package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface INetdEventCallback extends IInterface {
    public static final int CALLBACK_CALLER_CONNECTIVITY_SERVICE = 0;
    public static final int CALLBACK_CALLER_DEVICE_POLICY = 1;
    public static final int CALLBACK_CALLER_NETWORK_WATCHLIST = 2;

    public static class Default implements INetdEventCallback {
        public void onDnsEvent(int netId, int eventType, int returnCode, String hostname, String[] ipAddresses, int ipAddressesCount, long timestamp, int uid) throws RemoteException {
        }

        public void onNat64PrefixEvent(int netId, boolean added, String prefixString, int prefixLength) throws RemoteException {
        }

        public void onPrivateDnsValidationEvent(int netId, String ipAddress, String hostname, boolean validated) throws RemoteException {
        }

        public void onConnectEvent(String ipAddr, int port, long timestamp, int uid) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements INetdEventCallback {
        private static final String DESCRIPTOR = "android.net.INetdEventCallback";
        static final int TRANSACTION_onConnectEvent = 4;
        static final int TRANSACTION_onDnsEvent = 1;
        static final int TRANSACTION_onNat64PrefixEvent = 2;
        static final int TRANSACTION_onPrivateDnsValidationEvent = 3;

        private static class Proxy implements INetdEventCallback {
            public static INetdEventCallback sDefaultImpl;
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

            public void onDnsEvent(int netId, int eventType, int returnCode, String hostname, String[] ipAddresses, int ipAddressesCount, long timestamp, int uid) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                String str;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(netId);
                    } catch (Throwable th2) {
                        th = th2;
                        i = eventType;
                        i2 = returnCode;
                        str = hostname;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(eventType);
                        try {
                            _data.writeInt(returnCode);
                        } catch (Throwable th3) {
                            th = th3;
                            str = hostname;
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeString(hostname);
                            _data.writeStringArray(ipAddresses);
                            _data.writeInt(ipAddressesCount);
                            _data.writeLong(timestamp);
                            _data.writeInt(uid);
                            if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().onDnsEvent(netId, eventType, returnCode, hostname, ipAddresses, ipAddressesCount, timestamp, uid);
                            _data.recycle();
                        } catch (Throwable th4) {
                            th = th4;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        i2 = returnCode;
                        str = hostname;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    int i3 = netId;
                    i = eventType;
                    i2 = returnCode;
                    str = hostname;
                    _data.recycle();
                    throw th;
                }
            }

            public void onNat64PrefixEvent(int netId, boolean added, String prefixString, int prefixLength) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(netId);
                    _data.writeInt(added ? 1 : 0);
                    _data.writeString(prefixString);
                    _data.writeInt(prefixLength);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onNat64PrefixEvent(netId, added, prefixString, prefixLength);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onPrivateDnsValidationEvent(int netId, String ipAddress, String hostname, boolean validated) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(netId);
                    _data.writeString(ipAddress);
                    _data.writeString(hostname);
                    _data.writeInt(validated ? 1 : 0);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onPrivateDnsValidationEvent(netId, ipAddress, hostname, validated);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onConnectEvent(String ipAddr, int port, long timestamp, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(ipAddr);
                    _data.writeInt(port);
                    _data.writeLong(timestamp);
                    _data.writeInt(uid);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onConnectEvent(ipAddr, port, timestamp, uid);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static INetdEventCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof INetdEventCallback)) {
                return new Proxy(obj);
            }
            return (INetdEventCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onDnsEvent";
            }
            if (transactionCode == 2) {
                return "onNat64PrefixEvent";
            }
            if (transactionCode == 3) {
                return "onPrivateDnsValidationEvent";
            }
            if (transactionCode != 4) {
                return null;
            }
            return "onConnectEvent";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            String descriptor = DESCRIPTOR;
            Parcel parcel2;
            if (i != 1) {
                boolean _arg3 = false;
                int _arg0;
                if (i == 2) {
                    parcel2 = reply;
                    parcel.enforceInterface(descriptor);
                    _arg0 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg3 = true;
                    }
                    onNat64PrefixEvent(_arg0, _arg3, data.readString(), data.readInt());
                    return true;
                } else if (i == 3) {
                    parcel2 = reply;
                    parcel.enforceInterface(descriptor);
                    _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    String _arg2 = data.readString();
                    if (data.readInt() != 0) {
                        _arg3 = true;
                    }
                    onPrivateDnsValidationEvent(_arg0, _arg1, _arg2, _arg3);
                    return true;
                } else if (i == 4) {
                    parcel2 = reply;
                    parcel.enforceInterface(descriptor);
                    onConnectEvent(data.readString(), data.readInt(), data.readLong(), data.readInt());
                    return true;
                } else if (i != 1598968902) {
                    return super.onTransact(code, data, reply, flags);
                } else {
                    reply.writeString(descriptor);
                    return true;
                }
            }
            parcel2 = reply;
            parcel.enforceInterface(descriptor);
            onDnsEvent(data.readInt(), data.readInt(), data.readInt(), data.readString(), data.createStringArray(), data.readInt(), data.readLong(), data.readInt());
            return true;
        }

        public static boolean setDefaultImpl(INetdEventCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static INetdEventCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onConnectEvent(String str, int i, long j, int i2) throws RemoteException;

    void onDnsEvent(int i, int i2, int i3, String str, String[] strArr, int i4, long j, int i5) throws RemoteException;

    void onNat64PrefixEvent(int i, boolean z, String str, int i2) throws RemoteException;

    void onPrivateDnsValidationEvent(int i, String str, String str2, boolean z) throws RemoteException;
}
