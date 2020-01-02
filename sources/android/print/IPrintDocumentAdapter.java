package android.print;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.provider.Telephony.BaseMmsColumns;

public interface IPrintDocumentAdapter extends IInterface {

    public static class Default implements IPrintDocumentAdapter {
        public void setObserver(IPrintDocumentAdapterObserver observer) throws RemoteException {
        }

        public void start() throws RemoteException {
        }

        public void layout(PrintAttributes oldAttributes, PrintAttributes newAttributes, ILayoutResultCallback callback, Bundle metadata, int sequence) throws RemoteException {
        }

        public void write(PageRange[] pages, ParcelFileDescriptor fd, IWriteResultCallback callback, int sequence) throws RemoteException {
        }

        public void finish() throws RemoteException {
        }

        public void kill(String reason) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IPrintDocumentAdapter {
        private static final String DESCRIPTOR = "android.print.IPrintDocumentAdapter";
        static final int TRANSACTION_finish = 5;
        static final int TRANSACTION_kill = 6;
        static final int TRANSACTION_layout = 3;
        static final int TRANSACTION_setObserver = 1;
        static final int TRANSACTION_start = 2;
        static final int TRANSACTION_write = 4;

        private static class Proxy implements IPrintDocumentAdapter {
            public static IPrintDocumentAdapter sDefaultImpl;
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

            public void setObserver(IPrintDocumentAdapterObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setObserver(observer);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void start() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().start();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void layout(PrintAttributes oldAttributes, PrintAttributes newAttributes, ILayoutResultCallback callback, Bundle metadata, int sequence) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (oldAttributes != null) {
                        _data.writeInt(1);
                        oldAttributes.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (newAttributes != null) {
                        _data.writeInt(1);
                        newAttributes.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (metadata != null) {
                        _data.writeInt(1);
                        metadata.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(sequence);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().layout(oldAttributes, newAttributes, callback, metadata, sequence);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void write(PageRange[] pages, ParcelFileDescriptor fd, IWriteResultCallback callback, int sequence) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedArray(pages, 0);
                    if (fd != null) {
                        _data.writeInt(1);
                        fd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeInt(sequence);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().write(pages, fd, callback, sequence);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void finish() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().finish();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void kill(String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(reason);
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().kill(reason);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPrintDocumentAdapter asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IPrintDocumentAdapter)) {
                return new Proxy(obj);
            }
            return (IPrintDocumentAdapter) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "setObserver";
                case 2:
                    return BaseMmsColumns.START;
                case 3:
                    return TtmlUtils.TAG_LAYOUT;
                case 4:
                    return "write";
                case 5:
                    return "finish";
                case 6:
                    return "kill";
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
                        setObserver(android.print.IPrintDocumentAdapterObserver.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        start();
                        return true;
                    case 3:
                        PrintAttributes _arg0;
                        PrintAttributes _arg1;
                        Bundle _arg3;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (PrintAttributes) PrintAttributes.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg1 = (PrintAttributes) PrintAttributes.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg1 = null;
                        }
                        ILayoutResultCallback _arg2 = android.print.ILayoutResultCallback.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg3 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        layout(_arg0, _arg1, _arg2, _arg3, data.readInt());
                        return true;
                    case 4:
                        ParcelFileDescriptor _arg12;
                        parcel.enforceInterface(descriptor);
                        PageRange[] _arg02 = (PageRange[]) parcel.createTypedArray(PageRange.CREATOR);
                        if (data.readInt() != 0) {
                            _arg12 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        write(_arg02, _arg12, android.print.IWriteResultCallback.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        finish();
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        kill(data.readString());
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IPrintDocumentAdapter impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IPrintDocumentAdapter getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void finish() throws RemoteException;

    void kill(String str) throws RemoteException;

    void layout(PrintAttributes printAttributes, PrintAttributes printAttributes2, ILayoutResultCallback iLayoutResultCallback, Bundle bundle, int i) throws RemoteException;

    void setObserver(IPrintDocumentAdapterObserver iPrintDocumentAdapterObserver) throws RemoteException;

    void start() throws RemoteException;

    void write(PageRange[] pageRangeArr, ParcelFileDescriptor parcelFileDescriptor, IWriteResultCallback iWriteResultCallback, int i) throws RemoteException;
}
