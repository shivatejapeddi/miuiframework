package android.print;

import android.os.Binder;
import android.os.IBinder;
import android.os.ICancellationSignal;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.text.TextUtils;

public interface ILayoutResultCallback extends IInterface {

    public static class Default implements ILayoutResultCallback {
        public void onLayoutStarted(ICancellationSignal cancellation, int sequence) throws RemoteException {
        }

        public void onLayoutFinished(PrintDocumentInfo info, boolean changed, int sequence) throws RemoteException {
        }

        public void onLayoutFailed(CharSequence error, int sequence) throws RemoteException {
        }

        public void onLayoutCanceled(int sequence) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ILayoutResultCallback {
        private static final String DESCRIPTOR = "android.print.ILayoutResultCallback";
        static final int TRANSACTION_onLayoutCanceled = 4;
        static final int TRANSACTION_onLayoutFailed = 3;
        static final int TRANSACTION_onLayoutFinished = 2;
        static final int TRANSACTION_onLayoutStarted = 1;

        private static class Proxy implements ILayoutResultCallback {
            public static ILayoutResultCallback sDefaultImpl;
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

            public void onLayoutStarted(ICancellationSignal cancellation, int sequence) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cancellation != null ? cancellation.asBinder() : null);
                    _data.writeInt(sequence);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onLayoutStarted(cancellation, sequence);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onLayoutFinished(PrintDocumentInfo info, boolean changed, int sequence) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (changed) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeInt(sequence);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onLayoutFinished(info, changed, sequence);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onLayoutFailed(CharSequence error, int sequence) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (error != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(error, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(sequence);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onLayoutFailed(error, sequence);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onLayoutCanceled(int sequence) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sequence);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onLayoutCanceled(sequence);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ILayoutResultCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ILayoutResultCallback)) {
                return new Proxy(obj);
            }
            return (ILayoutResultCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onLayoutStarted";
            }
            if (transactionCode == 2) {
                return "onLayoutFinished";
            }
            if (transactionCode == 3) {
                return "onLayoutFailed";
            }
            if (transactionCode != 4) {
                return null;
            }
            return "onLayoutCanceled";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                onLayoutStarted(android.os.ICancellationSignal.Stub.asInterface(data.readStrongBinder()), data.readInt());
                return true;
            } else if (code == 2) {
                PrintDocumentInfo _arg0;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (PrintDocumentInfo) PrintDocumentInfo.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                onLayoutFinished(_arg0, data.readInt() != 0, data.readInt());
                return true;
            } else if (code == 3) {
                CharSequence _arg02;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg02 = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data);
                } else {
                    _arg02 = null;
                }
                onLayoutFailed(_arg02, data.readInt());
                return true;
            } else if (code == 4) {
                data.enforceInterface(descriptor);
                onLayoutCanceled(data.readInt());
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(ILayoutResultCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ILayoutResultCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onLayoutCanceled(int i) throws RemoteException;

    void onLayoutFailed(CharSequence charSequence, int i) throws RemoteException;

    void onLayoutFinished(PrintDocumentInfo printDocumentInfo, boolean z, int i) throws RemoteException;

    void onLayoutStarted(ICancellationSignal iCancellationSignal, int i) throws RemoteException;
}
