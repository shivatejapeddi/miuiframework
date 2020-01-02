package com.miui.whetstone;

import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IPkgStatusCallback extends IInterface {

    public static class Default implements IPkgStatusCallback {
        public void notifyChange(ComponentName preForegroundName, ComponentName curForegroundName) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IPkgStatusCallback {
        private static final String DESCRIPTOR = "com.miui.whetstone.IPkgStatusCallback";
        static final int TRANSACTION_notifyChange = 1;

        private static class Proxy implements IPkgStatusCallback {
            public static IPkgStatusCallback sDefaultImpl;
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

            public void notifyChange(ComponentName preForegroundName, ComponentName curForegroundName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (preForegroundName != null) {
                        _data.writeInt(1);
                        preForegroundName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (curForegroundName != null) {
                        _data.writeInt(1);
                        curForegroundName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().notifyChange(preForegroundName, curForegroundName);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPkgStatusCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IPkgStatusCallback)) {
                return new Proxy(obj);
            }
            return (IPkgStatusCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode != 1) {
                return null;
            }
            return "notifyChange";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                ComponentName _arg0;
                ComponentName _arg1;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                if (data.readInt() != 0) {
                    _arg1 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                } else {
                    _arg1 = null;
                }
                notifyChange(_arg0, _arg1);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IPkgStatusCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IPkgStatusCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void notifyChange(ComponentName componentName, ComponentName componentName2) throws RemoteException;
}
