package android.net.wifi.rtt;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.WorkSource;

public interface IWifiRttManager extends IInterface {

    public static class Default implements IWifiRttManager {
        public boolean isAvailable() throws RemoteException {
            return false;
        }

        public void startRanging(IBinder binder, String callingPackage, WorkSource workSource, RangingRequest request, IRttCallback callback) throws RemoteException {
        }

        public void cancelRanging(WorkSource workSource) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IWifiRttManager {
        private static final String DESCRIPTOR = "android.net.wifi.rtt.IWifiRttManager";
        static final int TRANSACTION_cancelRanging = 3;
        static final int TRANSACTION_isAvailable = 1;
        static final int TRANSACTION_startRanging = 2;

        private static class Proxy implements IWifiRttManager {
            public static IWifiRttManager sDefaultImpl;
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

            public boolean isAvailable() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    if (this.mRemote.transact(1, _data, _reply, z) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() != 0) {
                            z = true;
                        }
                        boolean _result = z;
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    z = Stub.getDefaultImpl().isAvailable();
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startRanging(IBinder binder, String callingPackage, WorkSource workSource, RangingRequest request, IRttCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    _data.writeString(callingPackage);
                    if (workSource != null) {
                        _data.writeInt(1);
                        workSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startRanging(binder, callingPackage, workSource, request, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cancelRanging(WorkSource workSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (workSource != null) {
                        _data.writeInt(1);
                        workSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().cancelRanging(workSource);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IWifiRttManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IWifiRttManager)) {
                return new Proxy(obj);
            }
            return (IWifiRttManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "isAvailable";
            }
            if (transactionCode == 2) {
                return "startRanging";
            }
            if (transactionCode != 3) {
                return null;
            }
            return "cancelRanging";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            Parcel parcel2 = reply;
            String descriptor = DESCRIPTOR;
            if (i == 1) {
                parcel.enforceInterface(descriptor);
                boolean _result = isAvailable();
                reply.writeNoException();
                parcel2.writeInt(_result);
                return true;
            } else if (i == 2) {
                WorkSource _arg2;
                RangingRequest _arg3;
                parcel.enforceInterface(descriptor);
                IBinder _arg0 = data.readStrongBinder();
                String _arg1 = data.readString();
                if (data.readInt() != 0) {
                    _arg2 = (WorkSource) WorkSource.CREATOR.createFromParcel(parcel);
                } else {
                    _arg2 = null;
                }
                if (data.readInt() != 0) {
                    _arg3 = (RangingRequest) RangingRequest.CREATOR.createFromParcel(parcel);
                } else {
                    _arg3 = null;
                }
                startRanging(_arg0, _arg1, _arg2, _arg3, android.net.wifi.rtt.IRttCallback.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                return true;
            } else if (i == 3) {
                WorkSource _arg02;
                parcel.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg02 = (WorkSource) WorkSource.CREATOR.createFromParcel(parcel);
                } else {
                    _arg02 = null;
                }
                cancelRanging(_arg02);
                reply.writeNoException();
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(code, data, reply, flags);
            } else {
                parcel2.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IWifiRttManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IWifiRttManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void cancelRanging(WorkSource workSource) throws RemoteException;

    boolean isAvailable() throws RemoteException;

    void startRanging(IBinder iBinder, String str, WorkSource workSource, RangingRequest rangingRequest, IRttCallback iRttCallback) throws RemoteException;
}
