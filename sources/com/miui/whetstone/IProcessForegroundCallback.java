package com.miui.whetstone;

import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IProcessForegroundCallback extends IInterface {

    public static class Default implements IProcessForegroundCallback {
        public void onTargetProcessForeground(ComponentName preForegroundComponentName, ComponentName curForegroundComponentName) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IProcessForegroundCallback {
        private static final String DESCRIPTOR = "com.miui.whetstone.IProcessForegroundCallback";
        static final int TRANSACTION_onTargetProcessForeground = 1;

        private static class Proxy implements IProcessForegroundCallback {
            public static IProcessForegroundCallback sDefaultImpl;
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

            public void onTargetProcessForeground(ComponentName preForegroundComponentName, ComponentName curForegroundComponentName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (preForegroundComponentName != null) {
                        _data.writeInt(1);
                        preForegroundComponentName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (curForegroundComponentName != null) {
                        _data.writeInt(1);
                        curForegroundComponentName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onTargetProcessForeground(preForegroundComponentName, curForegroundComponentName);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IProcessForegroundCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IProcessForegroundCallback)) {
                return new Proxy(obj);
            }
            return (IProcessForegroundCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode != 1) {
                return null;
            }
            return "onTargetProcessForeground";
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
                onTargetProcessForeground(_arg0, _arg1);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IProcessForegroundCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IProcessForegroundCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onTargetProcessForeground(ComponentName componentName, ComponentName componentName2) throws RemoteException;
}
