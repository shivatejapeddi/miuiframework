package android.service.autofill;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IAutoFillService extends IInterface {

    public static abstract class Stub extends Binder implements IAutoFillService {
        private static final String DESCRIPTOR = "android.service.autofill.IAutoFillService";
        static final int TRANSACTION_onConnectedStateChanged = 1;
        static final int TRANSACTION_onFillRequest = 2;
        static final int TRANSACTION_onSaveRequest = 3;

        private static class Proxy implements IAutoFillService {
            public static IAutoFillService sDefaultImpl;
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

            public void onConnectedStateChanged(boolean connected) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(connected ? 1 : 0);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onConnectedStateChanged(connected);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onFillRequest(FillRequest request, IFillCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onFillRequest(request, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onSaveRequest(SaveRequest request, ISaveCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onSaveRequest(request, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAutoFillService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IAutoFillService)) {
                return new Proxy(obj);
            }
            return (IAutoFillService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onConnectedStateChanged";
            }
            if (transactionCode == 2) {
                return "onFillRequest";
            }
            if (transactionCode != 3) {
                return null;
            }
            return "onSaveRequest";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                onConnectedStateChanged(data.readInt() != 0);
                return true;
            } else if (code == 2) {
                FillRequest _arg0;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (FillRequest) FillRequest.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                onFillRequest(_arg0, android.service.autofill.IFillCallback.Stub.asInterface(data.readStrongBinder()));
                return true;
            } else if (code == 3) {
                SaveRequest _arg02;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg02 = (SaveRequest) SaveRequest.CREATOR.createFromParcel(data);
                } else {
                    _arg02 = null;
                }
                onSaveRequest(_arg02, android.service.autofill.ISaveCallback.Stub.asInterface(data.readStrongBinder()));
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IAutoFillService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IAutoFillService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    public static class Default implements IAutoFillService {
        public void onConnectedStateChanged(boolean connected) throws RemoteException {
        }

        public void onFillRequest(FillRequest request, IFillCallback callback) throws RemoteException {
        }

        public void onSaveRequest(SaveRequest request, ISaveCallback callback) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    void onConnectedStateChanged(boolean z) throws RemoteException;

    void onFillRequest(FillRequest fillRequest, IFillCallback iFillCallback) throws RemoteException;

    void onSaveRequest(SaveRequest saveRequest, ISaveCallback iSaveCallback) throws RemoteException;
}
