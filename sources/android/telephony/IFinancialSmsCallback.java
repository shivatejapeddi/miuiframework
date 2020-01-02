package android.telephony;

import android.database.CursorWindow;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IFinancialSmsCallback extends IInterface {

    public static class Default implements IFinancialSmsCallback {
        public void onGetSmsMessagesForFinancialApp(CursorWindow messages) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IFinancialSmsCallback {
        private static final String DESCRIPTOR = "android.telephony.IFinancialSmsCallback";
        static final int TRANSACTION_onGetSmsMessagesForFinancialApp = 1;

        private static class Proxy implements IFinancialSmsCallback {
            public static IFinancialSmsCallback sDefaultImpl;
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

            public void onGetSmsMessagesForFinancialApp(CursorWindow messages) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (messages != null) {
                        _data.writeInt(1);
                        messages.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onGetSmsMessagesForFinancialApp(messages);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IFinancialSmsCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IFinancialSmsCallback)) {
                return new Proxy(obj);
            }
            return (IFinancialSmsCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode != 1) {
                return null;
            }
            return "onGetSmsMessagesForFinancialApp";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                CursorWindow _arg0;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (CursorWindow) CursorWindow.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                onGetSmsMessagesForFinancialApp(_arg0);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IFinancialSmsCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IFinancialSmsCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onGetSmsMessagesForFinancialApp(CursorWindow cursorWindow) throws RemoteException;
}
