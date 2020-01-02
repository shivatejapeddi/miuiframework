package miui.slide;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISliderViewService extends IInterface {

    public static class Default implements ISliderViewService {
        public void showSliderView(int state) throws RemoteException {
        }

        public void removeSliderView(int state) throws RemoteException {
        }

        public void playSound(int state) throws RemoteException {
        }

        public String getDumpContent(String prefix) throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ISliderViewService {
        private static final String DESCRIPTOR = "miui.slide.ISliderViewService";
        static final int TRANSACTION_getDumpContent = 4;
        static final int TRANSACTION_playSound = 3;
        static final int TRANSACTION_removeSliderView = 2;
        static final int TRANSACTION_showSliderView = 1;

        private static class Proxy implements ISliderViewService {
            public static ISliderViewService sDefaultImpl;
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

            public void showSliderView(int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().showSliderView(state);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void removeSliderView(int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().removeSliderView(state);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void playSound(int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().playSound(state);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public String getDumpContent(String prefix) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(prefix);
                    String str = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getDumpContent(prefix);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISliderViewService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ISliderViewService)) {
                return new Proxy(obj);
            }
            return (ISliderViewService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "showSliderView";
            }
            if (transactionCode == 2) {
                return "removeSliderView";
            }
            if (transactionCode == 3) {
                return "playSound";
            }
            if (transactionCode != 4) {
                return null;
            }
            return "getDumpContent";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                showSliderView(data.readInt());
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                removeSliderView(data.readInt());
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                playSound(data.readInt());
                return true;
            } else if (code == 4) {
                data.enforceInterface(descriptor);
                String _result = getDumpContent(data.readString());
                reply.writeNoException();
                reply.writeString(_result);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(ISliderViewService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ISliderViewService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    String getDumpContent(String str) throws RemoteException;

    void playSound(int i) throws RemoteException;

    void removeSliderView(int i) throws RemoteException;

    void showSliderView(int i) throws RemoteException;
}
