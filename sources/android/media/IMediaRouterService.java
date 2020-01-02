package android.media;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IMediaRouterService extends IInterface {

    public static class Default implements IMediaRouterService {
        public void registerClientAsUser(IMediaRouterClient client, String packageName, int userId) throws RemoteException {
        }

        public void unregisterClient(IMediaRouterClient client) throws RemoteException {
        }

        public void registerClientGroupId(IMediaRouterClient client, String groupId) throws RemoteException {
        }

        public MediaRouterClientState getState(IMediaRouterClient client) throws RemoteException {
            return null;
        }

        public boolean isPlaybackActive(IMediaRouterClient client) throws RemoteException {
            return false;
        }

        public void setDiscoveryRequest(IMediaRouterClient client, int routeTypes, boolean activeScan) throws RemoteException {
        }

        public void setSelectedRoute(IMediaRouterClient client, String routeId, boolean explicit) throws RemoteException {
        }

        public void requestSetVolume(IMediaRouterClient client, String routeId, int volume) throws RemoteException {
        }

        public void requestUpdateVolume(IMediaRouterClient client, String routeId, int direction) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMediaRouterService {
        private static final String DESCRIPTOR = "android.media.IMediaRouterService";
        static final int TRANSACTION_getState = 4;
        static final int TRANSACTION_isPlaybackActive = 5;
        static final int TRANSACTION_registerClientAsUser = 1;
        static final int TRANSACTION_registerClientGroupId = 3;
        static final int TRANSACTION_requestSetVolume = 8;
        static final int TRANSACTION_requestUpdateVolume = 9;
        static final int TRANSACTION_setDiscoveryRequest = 6;
        static final int TRANSACTION_setSelectedRoute = 7;
        static final int TRANSACTION_unregisterClient = 2;

        private static class Proxy implements IMediaRouterService {
            public static IMediaRouterService sDefaultImpl;
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

            public void registerClientAsUser(IMediaRouterClient client, String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerClientAsUser(client, packageName, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterClient(IMediaRouterClient client) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterClient(client);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerClientGroupId(IMediaRouterClient client, String groupId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    _data.writeString(groupId);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerClientGroupId(client, groupId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public MediaRouterClientState getState(IMediaRouterClient client) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    MediaRouterClientState mediaRouterClientState = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        mediaRouterClientState = Stub.getDefaultImpl();
                        if (mediaRouterClientState != 0) {
                            mediaRouterClientState = Stub.getDefaultImpl().getState(client);
                            return mediaRouterClientState;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        mediaRouterClientState = (MediaRouterClientState) MediaRouterClientState.CREATOR.createFromParcel(_reply);
                    } else {
                        mediaRouterClientState = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return mediaRouterClientState;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isPlaybackActive(IMediaRouterClient client) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isPlaybackActive(client);
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

            public void setDiscoveryRequest(IMediaRouterClient client, int routeTypes, boolean activeScan) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    _data.writeInt(routeTypes);
                    _data.writeInt(activeScan ? 1 : 0);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setDiscoveryRequest(client, routeTypes, activeScan);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setSelectedRoute(IMediaRouterClient client, String routeId, boolean explicit) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    _data.writeString(routeId);
                    _data.writeInt(explicit ? 1 : 0);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setSelectedRoute(client, routeId, explicit);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestSetVolume(IMediaRouterClient client, String routeId, int volume) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    _data.writeString(routeId);
                    _data.writeInt(volume);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().requestSetVolume(client, routeId, volume);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestUpdateVolume(IMediaRouterClient client, String routeId, int direction) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    _data.writeString(routeId);
                    _data.writeInt(direction);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().requestUpdateVolume(client, routeId, direction);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMediaRouterService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IMediaRouterService)) {
                return new Proxy(obj);
            }
            return (IMediaRouterService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "registerClientAsUser";
                case 2:
                    return "unregisterClient";
                case 3:
                    return "registerClientGroupId";
                case 4:
                    return "getState";
                case 5:
                    return "isPlaybackActive";
                case 6:
                    return "setDiscoveryRequest";
                case 7:
                    return "setSelectedRoute";
                case 8:
                    return "requestSetVolume";
                case 9:
                    return "requestUpdateVolume";
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
                IMediaRouterClient _arg0;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        registerClientAsUser(android.media.IMediaRouterClient.Stub.asInterface(data.readStrongBinder()), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        unregisterClient(android.media.IMediaRouterClient.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        registerClientGroupId(android.media.IMediaRouterClient.Stub.asInterface(data.readStrongBinder()), data.readString());
                        reply.writeNoException();
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        MediaRouterClientState _result = getState(android.media.IMediaRouterClient.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        if (_result != null) {
                            reply.writeInt(1);
                            _result.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        boolean _result2 = isPlaybackActive(android.media.IMediaRouterClient.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        _arg0 = android.media.IMediaRouterClient.Stub.asInterface(data.readStrongBinder());
                        int _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        setDiscoveryRequest(_arg0, _arg1, _arg2);
                        reply.writeNoException();
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        _arg0 = android.media.IMediaRouterClient.Stub.asInterface(data.readStrongBinder());
                        String _arg12 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        setSelectedRoute(_arg0, _arg12, _arg2);
                        reply.writeNoException();
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        requestSetVolume(android.media.IMediaRouterClient.Stub.asInterface(data.readStrongBinder()), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        requestUpdateVolume(android.media.IMediaRouterClient.Stub.asInterface(data.readStrongBinder()), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IMediaRouterService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IMediaRouterService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    MediaRouterClientState getState(IMediaRouterClient iMediaRouterClient) throws RemoteException;

    boolean isPlaybackActive(IMediaRouterClient iMediaRouterClient) throws RemoteException;

    void registerClientAsUser(IMediaRouterClient iMediaRouterClient, String str, int i) throws RemoteException;

    void registerClientGroupId(IMediaRouterClient iMediaRouterClient, String str) throws RemoteException;

    void requestSetVolume(IMediaRouterClient iMediaRouterClient, String str, int i) throws RemoteException;

    void requestUpdateVolume(IMediaRouterClient iMediaRouterClient, String str, int i) throws RemoteException;

    void setDiscoveryRequest(IMediaRouterClient iMediaRouterClient, int i, boolean z) throws RemoteException;

    void setSelectedRoute(IMediaRouterClient iMediaRouterClient, String str, boolean z) throws RemoteException;

    void unregisterClient(IMediaRouterClient iMediaRouterClient) throws RemoteException;
}
