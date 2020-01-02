package android.net.wifi.aware;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IWifiAwareManager extends IInterface {

    public static class Default implements IWifiAwareManager {
        public boolean isUsageEnabled() throws RemoteException {
            return false;
        }

        public Characteristics getCharacteristics() throws RemoteException {
            return null;
        }

        public void connect(IBinder binder, String callingPackage, IWifiAwareEventCallback callback, ConfigRequest configRequest, boolean notifyOnIdentityChanged) throws RemoteException {
        }

        public void disconnect(int clientId, IBinder binder) throws RemoteException {
        }

        public void publish(String callingPackage, int clientId, PublishConfig publishConfig, IWifiAwareDiscoverySessionCallback callback) throws RemoteException {
        }

        public void subscribe(String callingPackage, int clientId, SubscribeConfig subscribeConfig, IWifiAwareDiscoverySessionCallback callback) throws RemoteException {
        }

        public void updatePublish(int clientId, int discoverySessionId, PublishConfig publishConfig) throws RemoteException {
        }

        public void updateSubscribe(int clientId, int discoverySessionId, SubscribeConfig subscribeConfig) throws RemoteException {
        }

        public void sendMessage(int clientId, int discoverySessionId, int peerId, byte[] message, int messageId, int retryCount) throws RemoteException {
        }

        public void terminateSession(int clientId, int discoverySessionId) throws RemoteException {
        }

        public void requestMacAddresses(int uid, List peerIds, IWifiAwareMacAddressProvider callback) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IWifiAwareManager {
        private static final String DESCRIPTOR = "android.net.wifi.aware.IWifiAwareManager";
        static final int TRANSACTION_connect = 3;
        static final int TRANSACTION_disconnect = 4;
        static final int TRANSACTION_getCharacteristics = 2;
        static final int TRANSACTION_isUsageEnabled = 1;
        static final int TRANSACTION_publish = 5;
        static final int TRANSACTION_requestMacAddresses = 11;
        static final int TRANSACTION_sendMessage = 9;
        static final int TRANSACTION_subscribe = 6;
        static final int TRANSACTION_terminateSession = 10;
        static final int TRANSACTION_updatePublish = 7;
        static final int TRANSACTION_updateSubscribe = 8;

        private static class Proxy implements IWifiAwareManager {
            public static IWifiAwareManager sDefaultImpl;
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

            public boolean isUsageEnabled() throws RemoteException {
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
                    z = Stub.getDefaultImpl().isUsageEnabled();
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Characteristics getCharacteristics() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    Characteristics characteristics = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        characteristics = Stub.getDefaultImpl();
                        if (characteristics != 0) {
                            characteristics = Stub.getDefaultImpl().getCharacteristics();
                            return characteristics;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        characteristics = (Characteristics) Characteristics.CREATOR.createFromParcel(_reply);
                    } else {
                        characteristics = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return characteristics;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void connect(IBinder binder, String callingPackage, IWifiAwareEventCallback callback, ConfigRequest configRequest, boolean notifyOnIdentityChanged) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    _data.writeString(callingPackage);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    int i = 1;
                    if (configRequest != null) {
                        _data.writeInt(1);
                        configRequest.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!notifyOnIdentityChanged) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().connect(binder, callingPackage, callback, configRequest, notifyOnIdentityChanged);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disconnect(int clientId, IBinder binder) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientId);
                    _data.writeStrongBinder(binder);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().disconnect(clientId, binder);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void publish(String callingPackage, int clientId, PublishConfig publishConfig, IWifiAwareDiscoverySessionCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(clientId);
                    if (publishConfig != null) {
                        _data.writeInt(1);
                        publishConfig.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().publish(callingPackage, clientId, publishConfig, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void subscribe(String callingPackage, int clientId, SubscribeConfig subscribeConfig, IWifiAwareDiscoverySessionCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(clientId);
                    if (subscribeConfig != null) {
                        _data.writeInt(1);
                        subscribeConfig.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().subscribe(callingPackage, clientId, subscribeConfig, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updatePublish(int clientId, int discoverySessionId, PublishConfig publishConfig) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientId);
                    _data.writeInt(discoverySessionId);
                    if (publishConfig != null) {
                        _data.writeInt(1);
                        publishConfig.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updatePublish(clientId, discoverySessionId, publishConfig);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateSubscribe(int clientId, int discoverySessionId, SubscribeConfig subscribeConfig) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientId);
                    _data.writeInt(discoverySessionId);
                    if (subscribeConfig != null) {
                        _data.writeInt(1);
                        subscribeConfig.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateSubscribe(clientId, discoverySessionId, subscribeConfig);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendMessage(int clientId, int discoverySessionId, int peerId, byte[] message, int messageId, int retryCount) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                byte[] bArr;
                int i3;
                int i4;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(clientId);
                    } catch (Throwable th2) {
                        th = th2;
                        i = discoverySessionId;
                        i2 = peerId;
                        bArr = message;
                        i3 = messageId;
                        i4 = retryCount;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(discoverySessionId);
                        try {
                            _data.writeInt(peerId);
                            try {
                                _data.writeByteArray(message);
                            } catch (Throwable th3) {
                                th = th3;
                                i3 = messageId;
                                i4 = retryCount;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            bArr = message;
                            i3 = messageId;
                            i4 = retryCount;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        i2 = peerId;
                        bArr = message;
                        i3 = messageId;
                        i4 = retryCount;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(messageId);
                        try {
                            _data.writeInt(retryCount);
                            if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().sendMessage(clientId, discoverySessionId, peerId, message, messageId, retryCount);
                            _reply.recycle();
                            _data.recycle();
                        } catch (Throwable th6) {
                            th = th6;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th7) {
                        th = th7;
                        i4 = retryCount;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th8) {
                    th = th8;
                    int i5 = clientId;
                    i = discoverySessionId;
                    i2 = peerId;
                    bArr = message;
                    i3 = messageId;
                    i4 = retryCount;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void terminateSession(int clientId, int discoverySessionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientId);
                    _data.writeInt(discoverySessionId);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().terminateSession(clientId, discoverySessionId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestMacAddresses(int uid, List peerIds, IWifiAwareMacAddressProvider callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeList(peerIds);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().requestMacAddresses(uid, peerIds, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IWifiAwareManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IWifiAwareManager)) {
                return new Proxy(obj);
            }
            return (IWifiAwareManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "isUsageEnabled";
                case 2:
                    return "getCharacteristics";
                case 3:
                    return "connect";
                case 4:
                    return "disconnect";
                case 5:
                    return "publish";
                case 6:
                    return "subscribe";
                case 7:
                    return "updatePublish";
                case 8:
                    return "updateSubscribe";
                case 9:
                    return "sendMessage";
                case 10:
                    return "terminateSession";
                case 11:
                    return "requestMacAddresses";
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
                String _arg0;
                int _arg1;
                PublishConfig _arg2;
                SubscribeConfig _arg22;
                int _arg02;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        boolean _result = isUsageEnabled();
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        Characteristics _result2 = getCharacteristics();
                        reply.writeNoException();
                        if (_result2 != null) {
                            parcel2.writeInt(1);
                            _result2.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 3:
                        ConfigRequest _arg3;
                        parcel.enforceInterface(descriptor);
                        IBinder _arg03 = data.readStrongBinder();
                        String _arg12 = data.readString();
                        IWifiAwareEventCallback _arg23 = android.net.wifi.aware.IWifiAwareEventCallback.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg3 = (ConfigRequest) ConfigRequest.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        connect(_arg03, _arg12, _arg23, _arg3, data.readInt() != 0);
                        reply.writeNoException();
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        disconnect(data.readInt(), data.readStrongBinder());
                        reply.writeNoException();
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = (PublishConfig) PublishConfig.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        publish(_arg0, _arg1, _arg2, android.net.wifi.aware.IWifiAwareDiscoverySessionCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg22 = (SubscribeConfig) SubscribeConfig.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg22 = null;
                        }
                        subscribe(_arg0, _arg1, _arg22, android.net.wifi.aware.IWifiAwareDiscoverySessionCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readInt();
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = (PublishConfig) PublishConfig.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        updatePublish(_arg02, _arg1, _arg2);
                        reply.writeNoException();
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readInt();
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg22 = (SubscribeConfig) SubscribeConfig.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg22 = null;
                        }
                        updateSubscribe(_arg02, _arg1, _arg22);
                        reply.writeNoException();
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        sendMessage(data.readInt(), data.readInt(), data.readInt(), data.createByteArray(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        terminateSession(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        requestMacAddresses(data.readInt(), parcel.readArrayList(getClass().getClassLoader()), android.net.wifi.aware.IWifiAwareMacAddressProvider.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IWifiAwareManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IWifiAwareManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void connect(IBinder iBinder, String str, IWifiAwareEventCallback iWifiAwareEventCallback, ConfigRequest configRequest, boolean z) throws RemoteException;

    void disconnect(int i, IBinder iBinder) throws RemoteException;

    Characteristics getCharacteristics() throws RemoteException;

    boolean isUsageEnabled() throws RemoteException;

    void publish(String str, int i, PublishConfig publishConfig, IWifiAwareDiscoverySessionCallback iWifiAwareDiscoverySessionCallback) throws RemoteException;

    void requestMacAddresses(int i, List list, IWifiAwareMacAddressProvider iWifiAwareMacAddressProvider) throws RemoteException;

    void sendMessage(int i, int i2, int i3, byte[] bArr, int i4, int i5) throws RemoteException;

    void subscribe(String str, int i, SubscribeConfig subscribeConfig, IWifiAwareDiscoverySessionCallback iWifiAwareDiscoverySessionCallback) throws RemoteException;

    void terminateSession(int i, int i2) throws RemoteException;

    void updatePublish(int i, int i2, PublishConfig publishConfig) throws RemoteException;

    void updateSubscribe(int i, int i2, SubscribeConfig subscribeConfig) throws RemoteException;
}
