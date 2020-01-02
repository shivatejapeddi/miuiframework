package com.miui.whetstone;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IWhetstoneClient extends IInterface {

    public static abstract class Stub extends Binder implements IWhetstoneClient {
        private static final String DESCRIPTOR = "com.miui.whetstone.IWhetstoneClient";
        static final int TRANSACTION_setForegroundProcess = 1;

        private static class Proxy implements IWhetstoneClient {
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

            public void setForegroundProcess(int pid, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(pid);
                    _data.writeString(packageName);
                    this.mRemote.transact(1, _data, _reply, 1);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IWhetstoneClient asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IWhetstoneClient)) {
                return new Proxy(obj);
            }
            return (IWhetstoneClient) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String str = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(str);
                setForegroundProcess(data.readInt(), data.readString());
                reply.writeNoException();
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(str);
                return true;
            }
        }
    }

    void setForegroundProcess(int i, String str) throws RemoteException;
}
