package android.telephony.data;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IDataServiceCallback extends IInterface {

    public static class Default implements IDataServiceCallback {
        public void onSetupDataCallComplete(int result, DataCallResponse dataCallResponse) throws RemoteException {
        }

        public void onDeactivateDataCallComplete(int result) throws RemoteException {
        }

        public void onSetInitialAttachApnComplete(int result) throws RemoteException {
        }

        public void onSetDataProfileComplete(int result) throws RemoteException {
        }

        public void onRequestDataCallListComplete(int result, List<DataCallResponse> list) throws RemoteException {
        }

        public void onDataCallListChanged(List<DataCallResponse> list) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IDataServiceCallback {
        private static final String DESCRIPTOR = "android.telephony.data.IDataServiceCallback";
        static final int TRANSACTION_onDataCallListChanged = 6;
        static final int TRANSACTION_onDeactivateDataCallComplete = 2;
        static final int TRANSACTION_onRequestDataCallListComplete = 5;
        static final int TRANSACTION_onSetDataProfileComplete = 4;
        static final int TRANSACTION_onSetInitialAttachApnComplete = 3;
        static final int TRANSACTION_onSetupDataCallComplete = 1;

        private static class Proxy implements IDataServiceCallback {
            public static IDataServiceCallback sDefaultImpl;
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

            public void onSetupDataCallComplete(int result, DataCallResponse dataCallResponse) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(result);
                    if (dataCallResponse != null) {
                        _data.writeInt(1);
                        dataCallResponse.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onSetupDataCallComplete(result, dataCallResponse);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onDeactivateDataCallComplete(int result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(result);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onDeactivateDataCallComplete(result);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onSetInitialAttachApnComplete(int result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(result);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onSetInitialAttachApnComplete(result);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onSetDataProfileComplete(int result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(result);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onSetDataProfileComplete(result);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onRequestDataCallListComplete(int result, List<DataCallResponse> dataCallList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(result);
                    _data.writeTypedList(dataCallList);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onRequestDataCallListComplete(result, dataCallList);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onDataCallListChanged(List<DataCallResponse> dataCallList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(dataCallList);
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onDataCallListChanged(dataCallList);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IDataServiceCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IDataServiceCallback)) {
                return new Proxy(obj);
            }
            return (IDataServiceCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "onSetupDataCallComplete";
                case 2:
                    return "onDeactivateDataCallComplete";
                case 3:
                    return "onSetInitialAttachApnComplete";
                case 4:
                    return "onSetDataProfileComplete";
                case 5:
                    return "onRequestDataCallListComplete";
                case 6:
                    return "onDataCallListChanged";
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
                switch (code) {
                    case 1:
                        DataCallResponse _arg1;
                        data.enforceInterface(descriptor);
                        int _arg0 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = (DataCallResponse) DataCallResponse.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        onSetupDataCallComplete(_arg0, _arg1);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        onDeactivateDataCallComplete(data.readInt());
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        onSetInitialAttachApnComplete(data.readInt());
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        onSetDataProfileComplete(data.readInt());
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        onRequestDataCallListComplete(data.readInt(), data.createTypedArrayList(DataCallResponse.CREATOR));
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        onDataCallListChanged(data.createTypedArrayList(DataCallResponse.CREATOR));
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IDataServiceCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IDataServiceCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onDataCallListChanged(List<DataCallResponse> list) throws RemoteException;

    void onDeactivateDataCallComplete(int i) throws RemoteException;

    void onRequestDataCallListComplete(int i, List<DataCallResponse> list) throws RemoteException;

    void onSetDataProfileComplete(int i) throws RemoteException;

    void onSetInitialAttachApnComplete(int i) throws RemoteException;

    void onSetupDataCallComplete(int i, DataCallResponse dataCallResponse) throws RemoteException;
}
