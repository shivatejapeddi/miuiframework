package android.telephony.mbms.vendor;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.mbms.DownloadRequest;
import android.telephony.mbms.FileInfo;
import android.telephony.mbms.IDownloadProgressListener;
import android.telephony.mbms.IDownloadStatusListener;
import android.telephony.mbms.IMbmsDownloadSessionCallback;
import java.util.List;

public interface IMbmsDownloadService extends IInterface {

    public static class Default implements IMbmsDownloadService {
        public int initialize(int subId, IMbmsDownloadSessionCallback listener) throws RemoteException {
            return 0;
        }

        public int requestUpdateFileServices(int subId, List<String> list) throws RemoteException {
            return 0;
        }

        public int setTempFileRootDirectory(int subId, String rootDirectoryPath) throws RemoteException {
            return 0;
        }

        public int download(DownloadRequest downloadRequest) throws RemoteException {
            return 0;
        }

        public int addStatusListener(DownloadRequest downloadRequest, IDownloadStatusListener listener) throws RemoteException {
            return 0;
        }

        public int removeStatusListener(DownloadRequest downloadRequest, IDownloadStatusListener listener) throws RemoteException {
            return 0;
        }

        public int addProgressListener(DownloadRequest downloadRequest, IDownloadProgressListener listener) throws RemoteException {
            return 0;
        }

        public int removeProgressListener(DownloadRequest downloadRequest, IDownloadProgressListener listener) throws RemoteException {
            return 0;
        }

        public List<DownloadRequest> listPendingDownloads(int subscriptionId) throws RemoteException {
            return null;
        }

        public int cancelDownload(DownloadRequest downloadRequest) throws RemoteException {
            return 0;
        }

        public int requestDownloadState(DownloadRequest downloadRequest, FileInfo fileInfo) throws RemoteException {
            return 0;
        }

        public int resetDownloadKnowledge(DownloadRequest downloadRequest) throws RemoteException {
            return 0;
        }

        public void dispose(int subId) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMbmsDownloadService {
        private static final String DESCRIPTOR = "android.telephony.mbms.vendor.IMbmsDownloadService";
        static final int TRANSACTION_addProgressListener = 7;
        static final int TRANSACTION_addStatusListener = 5;
        static final int TRANSACTION_cancelDownload = 10;
        static final int TRANSACTION_dispose = 13;
        static final int TRANSACTION_download = 4;
        static final int TRANSACTION_initialize = 1;
        static final int TRANSACTION_listPendingDownloads = 9;
        static final int TRANSACTION_removeProgressListener = 8;
        static final int TRANSACTION_removeStatusListener = 6;
        static final int TRANSACTION_requestDownloadState = 11;
        static final int TRANSACTION_requestUpdateFileServices = 2;
        static final int TRANSACTION_resetDownloadKnowledge = 12;
        static final int TRANSACTION_setTempFileRootDirectory = 3;

        private static class Proxy implements IMbmsDownloadService {
            public static IMbmsDownloadService sDefaultImpl;
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

            public int initialize(int subId, IMbmsDownloadSessionCallback listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    int i = 1;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().initialize(subId, listener);
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

            public int requestUpdateFileServices(int subId, List<String> serviceClasses) throws RemoteException {
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
                            i = Stub.getDefaultImpl().requestUpdateFileServices(subId, serviceClasses);
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

            public int setTempFileRootDirectory(int subId, String rootDirectoryPath) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(rootDirectoryPath);
                    int i = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().setTempFileRootDirectory(subId, rootDirectoryPath);
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

            public int download(DownloadRequest downloadRequest) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (downloadRequest != null) {
                        _data.writeInt(1);
                        downloadRequest.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(4, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().download(downloadRequest);
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

            public int addStatusListener(DownloadRequest downloadRequest, IDownloadStatusListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (downloadRequest != null) {
                        _data.writeInt(1);
                        downloadRequest.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    int i = this.mRemote;
                    if (!i.transact(5, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().addStatusListener(downloadRequest, listener);
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

            public int removeStatusListener(DownloadRequest downloadRequest, IDownloadStatusListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (downloadRequest != null) {
                        _data.writeInt(1);
                        downloadRequest.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    int i = this.mRemote;
                    if (!i.transact(6, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().removeStatusListener(downloadRequest, listener);
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

            public int addProgressListener(DownloadRequest downloadRequest, IDownloadProgressListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (downloadRequest != null) {
                        _data.writeInt(1);
                        downloadRequest.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    int i = this.mRemote;
                    if (!i.transact(7, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().addProgressListener(downloadRequest, listener);
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

            public int removeProgressListener(DownloadRequest downloadRequest, IDownloadProgressListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (downloadRequest != null) {
                        _data.writeInt(1);
                        downloadRequest.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    int i = this.mRemote;
                    if (!i.transact(8, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().removeProgressListener(downloadRequest, listener);
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

            public List<DownloadRequest> listPendingDownloads(int subscriptionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subscriptionId);
                    List<DownloadRequest> list = 9;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().listPendingDownloads(subscriptionId);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(DownloadRequest.CREATOR);
                    List<DownloadRequest> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int cancelDownload(DownloadRequest downloadRequest) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (downloadRequest != null) {
                        _data.writeInt(1);
                        downloadRequest.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(10, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().cancelDownload(downloadRequest);
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

            public int requestDownloadState(DownloadRequest downloadRequest, FileInfo fileInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    if (downloadRequest != null) {
                        _data.writeInt(1);
                        downloadRequest.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (fileInfo != null) {
                        _data.writeInt(1);
                        fileInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().requestDownloadState(downloadRequest, fileInfo);
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

            public int resetDownloadKnowledge(DownloadRequest downloadRequest) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (downloadRequest != null) {
                        _data.writeInt(1);
                        downloadRequest.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(12, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().resetDownloadKnowledge(downloadRequest);
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

            public void dispose(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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

        public static IMbmsDownloadService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IMbmsDownloadService)) {
                return new Proxy(obj);
            }
            return (IMbmsDownloadService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return ContentResolver.SYNC_EXTRAS_INITIALIZE;
                case 2:
                    return "requestUpdateFileServices";
                case 3:
                    return "setTempFileRootDirectory";
                case 4:
                    return Context.DOWNLOAD_SERVICE;
                case 5:
                    return "addStatusListener";
                case 6:
                    return "removeStatusListener";
                case 7:
                    return "addProgressListener";
                case 8:
                    return "removeProgressListener";
                case 9:
                    return "listPendingDownloads";
                case 10:
                    return "cancelDownload";
                case 11:
                    return "requestDownloadState";
                case 12:
                    return "resetDownloadKnowledge";
                case 13:
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
                DownloadRequest _arg0;
                int _result2;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        _result = initialize(data.readInt(), android.telephony.mbms.IMbmsDownloadSessionCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        _result = requestUpdateFileServices(data.readInt(), data.createStringArrayList());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        _result = setTempFileRootDirectory(data.readInt(), data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (DownloadRequest) DownloadRequest.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        _result2 = download(_arg0);
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (DownloadRequest) DownloadRequest.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        _result = addStatusListener(_arg0, android.telephony.mbms.IDownloadStatusListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (DownloadRequest) DownloadRequest.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        _result = removeStatusListener(_arg0, android.telephony.mbms.IDownloadStatusListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (DownloadRequest) DownloadRequest.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        _result = addProgressListener(_arg0, android.telephony.mbms.IDownloadProgressListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (DownloadRequest) DownloadRequest.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        _result = removeProgressListener(_arg0, android.telephony.mbms.IDownloadProgressListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        List<DownloadRequest> _result3 = listPendingDownloads(data.readInt());
                        reply.writeNoException();
                        reply.writeTypedList(_result3);
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (DownloadRequest) DownloadRequest.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        _result2 = cancelDownload(_arg0);
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 11:
                        FileInfo _arg1;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (DownloadRequest) DownloadRequest.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg1 = (FileInfo) FileInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        _result = requestDownloadState(_arg0, _arg1);
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (DownloadRequest) DownloadRequest.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        _result2 = resetDownloadKnowledge(_arg0);
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 13:
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

        public static boolean setDefaultImpl(IMbmsDownloadService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IMbmsDownloadService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    int addProgressListener(DownloadRequest downloadRequest, IDownloadProgressListener iDownloadProgressListener) throws RemoteException;

    int addStatusListener(DownloadRequest downloadRequest, IDownloadStatusListener iDownloadStatusListener) throws RemoteException;

    int cancelDownload(DownloadRequest downloadRequest) throws RemoteException;

    void dispose(int i) throws RemoteException;

    int download(DownloadRequest downloadRequest) throws RemoteException;

    int initialize(int i, IMbmsDownloadSessionCallback iMbmsDownloadSessionCallback) throws RemoteException;

    List<DownloadRequest> listPendingDownloads(int i) throws RemoteException;

    int removeProgressListener(DownloadRequest downloadRequest, IDownloadProgressListener iDownloadProgressListener) throws RemoteException;

    int removeStatusListener(DownloadRequest downloadRequest, IDownloadStatusListener iDownloadStatusListener) throws RemoteException;

    int requestDownloadState(DownloadRequest downloadRequest, FileInfo fileInfo) throws RemoteException;

    int requestUpdateFileServices(int i, List<String> list) throws RemoteException;

    int resetDownloadKnowledge(DownloadRequest downloadRequest) throws RemoteException;

    int setTempFileRootDirectory(int i, String str) throws RemoteException;
}
