package android.telephony.mbms.vendor;

import android.annotation.UnsupportedAppUsage;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.mbms.IMbmsStreamingSessionCallback;
import android.telephony.mbms.IStreamingServiceCallback;
import java.util.List;

public interface IMbmsStreamingService extends IInterface {

    public static class Default implements IMbmsStreamingService {
        public int initialize(IMbmsStreamingSessionCallback callback, int subId) throws RemoteException {
            return 0;
        }

        public int requestUpdateStreamingServices(int subId, List<String> list) throws RemoteException {
            return 0;
        }

        public int startStreaming(int subId, String serviceId, IStreamingServiceCallback callback) throws RemoteException {
            return 0;
        }

        public Uri getPlaybackUri(int subId, String serviceId) throws RemoteException {
            return null;
        }

        public void stopStreaming(int subId, String serviceId) throws RemoteException {
        }

        public void dispose(int subId) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMbmsStreamingService {
        private static final String DESCRIPTOR = "android.telephony.mbms.vendor.IMbmsStreamingService";
        static final int TRANSACTION_dispose = 6;
        static final int TRANSACTION_getPlaybackUri = 4;
        static final int TRANSACTION_initialize = 1;
        static final int TRANSACTION_requestUpdateStreamingServices = 2;
        static final int TRANSACTION_startStreaming = 3;
        static final int TRANSACTION_stopStreaming = 5;

        private static class Proxy implements IMbmsStreamingService {
            public static IMbmsStreamingService sDefaultImpl;
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

            public int initialize(IMbmsStreamingSessionCallback callback, int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeInt(subId);
                    int i = 1;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().initialize(callback, subId);
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

            public int requestUpdateStreamingServices(int subId, List<String> serviceClasses) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeStringList(serviceClasses);
                    int i = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().requestUpdateStreamingServices(subId, serviceClasses);
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

            public int startStreaming(int subId, String serviceId, IStreamingServiceCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(serviceId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    int i = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().startStreaming(subId, serviceId, callback);
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

            public Uri getPlaybackUri(int subId, String serviceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(serviceId);
                    Uri uri = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        uri = Stub.getDefaultImpl();
                        if (uri != 0) {
                            uri = Stub.getDefaultImpl().getPlaybackUri(subId, serviceId);
                            return uri;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        uri = (Uri) Uri.CREATOR.createFromParcel(_reply);
                    } else {
                        uri = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return uri;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopStreaming(int subId, String serviceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(serviceId);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().stopStreaming(subId, serviceId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void dispose(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().dispose(subId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMbmsStreamingService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IMbmsStreamingService)) {
                return new Proxy(obj);
            }
            return (IMbmsStreamingService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return ContentResolver.SYNC_EXTRAS_INITIALIZE;
                case 2:
                    return "requestUpdateStreamingServices";
                case 3:
                    return "startStreaming";
                case 4:
                    return "getPlaybackUri";
                case 5:
                    return "stopStreaming";
                case 6:
                    return "dispose";
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
                int _result;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        _result = initialize(android.telephony.mbms.IMbmsStreamingSessionCallback.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        _result = requestUpdateStreamingServices(data.readInt(), data.createStringArrayList());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        int _result2 = startStreaming(data.readInt(), data.readString(), android.telephony.mbms.IStreamingServiceCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        Uri _result3 = getPlaybackUri(data.readInt(), data.readString());
                        reply.writeNoException();
                        if (_result3 != null) {
                            reply.writeInt(1);
                            _result3.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        stopStreaming(data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        dispose(data.readInt());
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IMbmsStreamingService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IMbmsStreamingService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void dispose(int i) throws RemoteException;

    @UnsupportedAppUsage
    Uri getPlaybackUri(int i, String str) throws RemoteException;

    @UnsupportedAppUsage
    int initialize(IMbmsStreamingSessionCallback iMbmsStreamingSessionCallback, int i) throws RemoteException;

    @UnsupportedAppUsage
    int requestUpdateStreamingServices(int i, List<String> list) throws RemoteException;

    @UnsupportedAppUsage
    int startStreaming(int i, String str, IStreamingServiceCallback iStreamingServiceCallback) throws RemoteException;

    void stopStreaming(int i, String str) throws RemoteException;
}
