package android.media.tv;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ITvInputServiceCallback extends IInterface {

    public static class Default implements ITvInputServiceCallback {
        public void addHardwareInput(int deviceId, TvInputInfo inputInfo) throws RemoteException {
        }

        public void addHdmiInput(int id, TvInputInfo inputInfo) throws RemoteException {
        }

        public void removeHardwareInput(String inputId) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ITvInputServiceCallback {
        private static final String DESCRIPTOR = "android.media.tv.ITvInputServiceCallback";
        static final int TRANSACTION_addHardwareInput = 1;
        static final int TRANSACTION_addHdmiInput = 2;
        static final int TRANSACTION_removeHardwareInput = 3;

        private static class Proxy implements ITvInputServiceCallback {
            public static ITvInputServiceCallback sDefaultImpl;
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

            public void addHardwareInput(int deviceId, TvInputInfo inputInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(deviceId);
                    if (inputInfo != null) {
                        _data.writeInt(1);
                        inputInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().addHardwareInput(deviceId, inputInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void addHdmiInput(int id, TvInputInfo inputInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(id);
                    if (inputInfo != null) {
                        _data.writeInt(1);
                        inputInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().addHdmiInput(id, inputInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void removeHardwareInput(String inputId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(inputId);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().removeHardwareInput(inputId);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ITvInputServiceCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ITvInputServiceCallback)) {
                return new Proxy(obj);
            }
            return (ITvInputServiceCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "addHardwareInput";
            }
            if (transactionCode == 2) {
                return "addHdmiInput";
            }
            if (transactionCode != 3) {
                return null;
            }
            return "removeHardwareInput";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            int _arg0;
            TvInputInfo _arg1;
            if (code == 1) {
                data.enforceInterface(descriptor);
                _arg0 = data.readInt();
                if (data.readInt() != 0) {
                    _arg1 = (TvInputInfo) TvInputInfo.CREATOR.createFromParcel(data);
                } else {
                    _arg1 = null;
                }
                addHardwareInput(_arg0, _arg1);
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                _arg0 = data.readInt();
                if (data.readInt() != 0) {
                    _arg1 = (TvInputInfo) TvInputInfo.CREATOR.createFromParcel(data);
                } else {
                    _arg1 = null;
                }
                addHdmiInput(_arg0, _arg1);
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                removeHardwareInput(data.readString());
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(ITvInputServiceCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ITvInputServiceCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void addHardwareInput(int i, TvInputInfo tvInputInfo) throws RemoteException;

    void addHdmiInput(int i, TvInputInfo tvInputInfo) throws RemoteException;

    void removeHardwareInput(String str) throws RemoteException;
}
