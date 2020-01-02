package android.media.tv;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ITvInputManagerCallback extends IInterface {

    public static class Default implements ITvInputManagerCallback {
        public void onInputAdded(String inputId) throws RemoteException {
        }

        public void onInputRemoved(String inputId) throws RemoteException {
        }

        public void onInputUpdated(String inputId) throws RemoteException {
        }

        public void onInputStateChanged(String inputId, int state) throws RemoteException {
        }

        public void onTvInputInfoUpdated(TvInputInfo TvInputInfo) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ITvInputManagerCallback {
        private static final String DESCRIPTOR = "android.media.tv.ITvInputManagerCallback";
        static final int TRANSACTION_onInputAdded = 1;
        static final int TRANSACTION_onInputRemoved = 2;
        static final int TRANSACTION_onInputStateChanged = 4;
        static final int TRANSACTION_onInputUpdated = 3;
        static final int TRANSACTION_onTvInputInfoUpdated = 5;

        private static class Proxy implements ITvInputManagerCallback {
            public static ITvInputManagerCallback sDefaultImpl;
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

            public void onInputAdded(String inputId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(inputId);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onInputAdded(inputId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onInputRemoved(String inputId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(inputId);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onInputRemoved(inputId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onInputUpdated(String inputId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(inputId);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onInputUpdated(inputId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onInputStateChanged(String inputId, int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(inputId);
                    _data.writeInt(state);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onInputStateChanged(inputId, state);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onTvInputInfoUpdated(TvInputInfo TvInputInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (TvInputInfo != null) {
                        _data.writeInt(1);
                        TvInputInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onTvInputInfoUpdated(TvInputInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ITvInputManagerCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ITvInputManagerCallback)) {
                return new Proxy(obj);
            }
            return (ITvInputManagerCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onInputAdded";
            }
            if (transactionCode == 2) {
                return "onInputRemoved";
            }
            if (transactionCode == 3) {
                return "onInputUpdated";
            }
            if (transactionCode == 4) {
                return "onInputStateChanged";
            }
            if (transactionCode != 5) {
                return null;
            }
            return "onTvInputInfoUpdated";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                onInputAdded(data.readString());
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                onInputRemoved(data.readString());
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                onInputUpdated(data.readString());
                return true;
            } else if (code == 4) {
                data.enforceInterface(descriptor);
                onInputStateChanged(data.readString(), data.readInt());
                return true;
            } else if (code == 5) {
                TvInputInfo _arg0;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (TvInputInfo) TvInputInfo.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                onTvInputInfoUpdated(_arg0);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(ITvInputManagerCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ITvInputManagerCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onInputAdded(String str) throws RemoteException;

    void onInputRemoved(String str) throws RemoteException;

    void onInputStateChanged(String str, int i) throws RemoteException;

    void onInputUpdated(String str) throws RemoteException;

    void onTvInputInfoUpdated(TvInputInfo tvInputInfo) throws RemoteException;
}
