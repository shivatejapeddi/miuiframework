package android.gamepad;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.Map;

public interface IBsGamePadService extends IInterface {

    public static class Default implements IBsGamePadService {
        public void setAppSwitch(boolean enable) throws RemoteException {
        }

        public void loadKeyMap(Map mapper, boolean isChooseMove, int rotation) throws RemoteException {
        }

        public void unloadKeyMap() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IBsGamePadService {
        private static final String DESCRIPTOR = "android.gamepad.IBsGamePadService";
        static final int TRANSACTION_loadKeyMap = 2;
        static final int TRANSACTION_setAppSwitch = 1;
        static final int TRANSACTION_unloadKeyMap = 3;

        private static class Proxy implements IBsGamePadService {
            public static IBsGamePadService sDefaultImpl;
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

            public void setAppSwitch(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAppSwitch(enable);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void loadKeyMap(Map mapper, boolean isChooseMove, int rotation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeMap(mapper);
                    _data.writeInt(isChooseMove ? 1 : 0);
                    _data.writeInt(rotation);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().loadKeyMap(mapper, isChooseMove, rotation);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unloadKeyMap() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unloadKeyMap();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IBsGamePadService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IBsGamePadService)) {
                return new Proxy(obj);
            }
            return (IBsGamePadService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "setAppSwitch";
            }
            if (transactionCode == 2) {
                return "loadKeyMap";
            }
            if (transactionCode != 3) {
                return null;
            }
            return "unloadKeyMap";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            boolean _arg1 = false;
            if (code == 1) {
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg1 = true;
                }
                setAppSwitch(_arg1);
                reply.writeNoException();
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                Map _arg0 = data.readHashMap(getClass().getClassLoader());
                if (data.readInt() != 0) {
                    _arg1 = true;
                }
                loadKeyMap(_arg0, _arg1, data.readInt());
                reply.writeNoException();
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                unloadKeyMap();
                reply.writeNoException();
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IBsGamePadService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IBsGamePadService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void loadKeyMap(Map map, boolean z, int i) throws RemoteException;

    void setAppSwitch(boolean z) throws RemoteException;

    void unloadKeyMap() throws RemoteException;
}
