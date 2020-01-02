package android.service.carrier;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ICarrierMessagingCallback extends IInterface {

    public static class Default implements ICarrierMessagingCallback {
        public void onFilterComplete(int result) throws RemoteException {
        }

        public void onSendSmsComplete(int result, int messageRef) throws RemoteException {
        }

        public void onSendMultipartSmsComplete(int result, int[] messageRefs) throws RemoteException {
        }

        public void onSendMmsComplete(int result, byte[] sendConfPdu) throws RemoteException {
        }

        public void onDownloadMmsComplete(int result) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ICarrierMessagingCallback {
        private static final String DESCRIPTOR = "android.service.carrier.ICarrierMessagingCallback";
        static final int TRANSACTION_onDownloadMmsComplete = 5;
        static final int TRANSACTION_onFilterComplete = 1;
        static final int TRANSACTION_onSendMmsComplete = 4;
        static final int TRANSACTION_onSendMultipartSmsComplete = 3;
        static final int TRANSACTION_onSendSmsComplete = 2;

        private static class Proxy implements ICarrierMessagingCallback {
            public static ICarrierMessagingCallback sDefaultImpl;
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

            public void onFilterComplete(int result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(result);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onFilterComplete(result);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onSendSmsComplete(int result, int messageRef) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(result);
                    _data.writeInt(messageRef);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onSendSmsComplete(result, messageRef);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onSendMultipartSmsComplete(int result, int[] messageRefs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(result);
                    _data.writeIntArray(messageRefs);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onSendMultipartSmsComplete(result, messageRefs);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onSendMmsComplete(int result, byte[] sendConfPdu) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(result);
                    _data.writeByteArray(sendConfPdu);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onSendMmsComplete(result, sendConfPdu);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onDownloadMmsComplete(int result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(result);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onDownloadMmsComplete(result);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ICarrierMessagingCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ICarrierMessagingCallback)) {
                return new Proxy(obj);
            }
            return (ICarrierMessagingCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onFilterComplete";
            }
            if (transactionCode == 2) {
                return "onSendSmsComplete";
            }
            if (transactionCode == 3) {
                return "onSendMultipartSmsComplete";
            }
            if (transactionCode == 4) {
                return "onSendMmsComplete";
            }
            if (transactionCode != 5) {
                return null;
            }
            return "onDownloadMmsComplete";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                onFilterComplete(data.readInt());
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                onSendSmsComplete(data.readInt(), data.readInt());
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                onSendMultipartSmsComplete(data.readInt(), data.createIntArray());
                return true;
            } else if (code == 4) {
                data.enforceInterface(descriptor);
                onSendMmsComplete(data.readInt(), data.createByteArray());
                return true;
            } else if (code == 5) {
                data.enforceInterface(descriptor);
                onDownloadMmsComplete(data.readInt());
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(ICarrierMessagingCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ICarrierMessagingCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onDownloadMmsComplete(int i) throws RemoteException;

    void onFilterComplete(int i) throws RemoteException;

    void onSendMmsComplete(int i, byte[] bArr) throws RemoteException;

    void onSendMultipartSmsComplete(int i, int[] iArr) throws RemoteException;

    void onSendSmsComplete(int i, int i2) throws RemoteException;
}
