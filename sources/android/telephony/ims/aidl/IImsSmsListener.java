package android.telephony.ims.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IImsSmsListener extends IInterface {

    public static class Default implements IImsSmsListener {
        public void onSendSmsResult(int token, int messageRef, int status, int reason) throws RemoteException {
        }

        public void onSmsStatusReportReceived(int token, int messageRef, String format, byte[] pdu) throws RemoteException {
        }

        public void onSmsReceived(int token, String format, byte[] pdu) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IImsSmsListener {
        private static final String DESCRIPTOR = "android.telephony.ims.aidl.IImsSmsListener";
        static final int TRANSACTION_onSendSmsResult = 1;
        static final int TRANSACTION_onSmsReceived = 3;
        static final int TRANSACTION_onSmsStatusReportReceived = 2;

        private static class Proxy implements IImsSmsListener {
            public static IImsSmsListener sDefaultImpl;
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

            public void onSendSmsResult(int token, int messageRef, int status, int reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(token);
                    _data.writeInt(messageRef);
                    _data.writeInt(status);
                    _data.writeInt(reason);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onSendSmsResult(token, messageRef, status, reason);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onSmsStatusReportReceived(int token, int messageRef, String format, byte[] pdu) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(token);
                    _data.writeInt(messageRef);
                    _data.writeString(format);
                    _data.writeByteArray(pdu);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onSmsStatusReportReceived(token, messageRef, format, pdu);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onSmsReceived(int token, String format, byte[] pdu) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(token);
                    _data.writeString(format);
                    _data.writeByteArray(pdu);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onSmsReceived(token, format, pdu);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IImsSmsListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IImsSmsListener)) {
                return new Proxy(obj);
            }
            return (IImsSmsListener) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onSendSmsResult";
            }
            if (transactionCode == 2) {
                return "onSmsStatusReportReceived";
            }
            if (transactionCode != 3) {
                return null;
            }
            return "onSmsReceived";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                onSendSmsResult(data.readInt(), data.readInt(), data.readInt(), data.readInt());
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                onSmsStatusReportReceived(data.readInt(), data.readInt(), data.readString(), data.createByteArray());
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                onSmsReceived(data.readInt(), data.readString(), data.createByteArray());
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IImsSmsListener impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IImsSmsListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onSendSmsResult(int i, int i2, int i3, int i4) throws RemoteException;

    void onSmsReceived(int i, String str, byte[] bArr) throws RemoteException;

    void onSmsStatusReportReceived(int i, int i2, String str, byte[] bArr) throws RemoteException;
}
