package android.service.contentcapture;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.contentcapture.ContentCaptureContext;
import android.view.contentcapture.DataRemovalRequest;
import com.android.internal.os.IResultReceiver;

public interface IContentCaptureService extends IInterface {

    public static abstract class Stub extends Binder implements IContentCaptureService {
        private static final String DESCRIPTOR = "android.service.contentcapture.IContentCaptureService";
        static final int TRANSACTION_onActivityEvent = 7;
        static final int TRANSACTION_onActivitySnapshot = 5;
        static final int TRANSACTION_onConnected = 1;
        static final int TRANSACTION_onDataRemovalRequest = 6;
        static final int TRANSACTION_onDisconnected = 2;
        static final int TRANSACTION_onSessionFinished = 4;
        static final int TRANSACTION_onSessionStarted = 3;

        private static class Proxy implements IContentCaptureService {
            public static IContentCaptureService sDefaultImpl;
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

            public void onConnected(IBinder callback, boolean verbose, boolean debug) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback);
                    int i = 0;
                    _data.writeInt(verbose ? 1 : 0);
                    if (debug) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onConnected(callback, verbose, debug);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onDisconnected() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onDisconnected();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onSessionStarted(ContentCaptureContext context, int sessionId, int uid, IResultReceiver clientReceiver, int initialState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (context != null) {
                        _data.writeInt(1);
                        context.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(sessionId);
                    _data.writeInt(uid);
                    _data.writeStrongBinder(clientReceiver != null ? clientReceiver.asBinder() : null);
                    _data.writeInt(initialState);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onSessionStarted(context, sessionId, uid, clientReceiver, initialState);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onSessionFinished(int sessionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onSessionFinished(sessionId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onActivitySnapshot(int sessionId, SnapshotData snapshotData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    if (snapshotData != null) {
                        _data.writeInt(1);
                        snapshotData.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onActivitySnapshot(sessionId, snapshotData);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onDataRemovalRequest(DataRemovalRequest request) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onDataRemovalRequest(request);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onActivityEvent(ActivityEvent event) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (event != null) {
                        _data.writeInt(1);
                        event.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(7, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onActivityEvent(event);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IContentCaptureService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IContentCaptureService)) {
                return new Proxy(obj);
            }
            return (IContentCaptureService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "onConnected";
                case 2:
                    return "onDisconnected";
                case 3:
                    return "onSessionStarted";
                case 4:
                    return "onSessionFinished";
                case 5:
                    return "onActivitySnapshot";
                case 6:
                    return "onDataRemovalRequest";
                case 7:
                    return "onActivityEvent";
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
            String descriptor = DESCRIPTOR;
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        IBinder _arg0 = data.readStrongBinder();
                        boolean _arg2 = false;
                        boolean _arg1 = data.readInt() != 0;
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        onConnected(_arg0, _arg1, _arg2);
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        onDisconnected();
                        return true;
                    case 3:
                        ContentCaptureContext _arg02;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (ContentCaptureContext) ContentCaptureContext.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        onSessionStarted(_arg02, data.readInt(), data.readInt(), com.android.internal.os.IResultReceiver.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        onSessionFinished(data.readInt());
                        return true;
                    case 5:
                        SnapshotData _arg12;
                        parcel.enforceInterface(descriptor);
                        int _arg03 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg12 = (SnapshotData) SnapshotData.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        onActivitySnapshot(_arg03, _arg12);
                        return true;
                    case 6:
                        DataRemovalRequest _arg04;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (DataRemovalRequest) DataRemovalRequest.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        onDataRemovalRequest(_arg04);
                        return true;
                    case 7:
                        ActivityEvent _arg05;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg05 = (ActivityEvent) ActivityEvent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg05 = null;
                        }
                        onActivityEvent(_arg05);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IContentCaptureService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IContentCaptureService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    public static class Default implements IContentCaptureService {
        public void onConnected(IBinder callback, boolean verbose, boolean debug) throws RemoteException {
        }

        public void onDisconnected() throws RemoteException {
        }

        public void onSessionStarted(ContentCaptureContext context, int sessionId, int uid, IResultReceiver clientReceiver, int initialState) throws RemoteException {
        }

        public void onSessionFinished(int sessionId) throws RemoteException {
        }

        public void onActivitySnapshot(int sessionId, SnapshotData snapshotData) throws RemoteException {
        }

        public void onDataRemovalRequest(DataRemovalRequest request) throws RemoteException {
        }

        public void onActivityEvent(ActivityEvent event) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    void onActivityEvent(ActivityEvent activityEvent) throws RemoteException;

    void onActivitySnapshot(int i, SnapshotData snapshotData) throws RemoteException;

    void onConnected(IBinder iBinder, boolean z, boolean z2) throws RemoteException;

    void onDataRemovalRequest(DataRemovalRequest dataRemovalRequest) throws RemoteException;

    void onDisconnected() throws RemoteException;

    void onSessionFinished(int i) throws RemoteException;

    void onSessionStarted(ContentCaptureContext contentCaptureContext, int i, int i2, IResultReceiver iResultReceiver, int i3) throws RemoteException;
}
