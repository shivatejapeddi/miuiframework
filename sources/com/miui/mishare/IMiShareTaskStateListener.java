package com.miui.mishare;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IMiShareTaskStateListener extends IInterface {

    public static class Default implements IMiShareTaskStateListener {
        public void onTaskStateChanged(String taskId, int state) throws RemoteException {
        }

        public void onTaskIdChanged(MiShareTask task) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMiShareTaskStateListener {
        private static final String DESCRIPTOR = "com.miui.mishare.IMiShareTaskStateListener";
        static final int TRANSACTION_onTaskIdChanged = 2;
        static final int TRANSACTION_onTaskStateChanged = 1;

        private static class Proxy implements IMiShareTaskStateListener {
            public static IMiShareTaskStateListener sDefaultImpl;
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

            public void onTaskStateChanged(String taskId, int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(taskId);
                    _data.writeInt(state);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onTaskStateChanged(taskId, state);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onTaskIdChanged(MiShareTask task) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (task != null) {
                        _data.writeInt(1);
                        task.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onTaskIdChanged(task);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMiShareTaskStateListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IMiShareTaskStateListener)) {
                return new Proxy(obj);
            }
            return (IMiShareTaskStateListener) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onTaskStateChanged";
            }
            if (transactionCode != 2) {
                return null;
            }
            return "onTaskIdChanged";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                onTaskStateChanged(data.readString(), data.readInt());
                return true;
            } else if (code == 2) {
                MiShareTask _arg0;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (MiShareTask) MiShareTask.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                onTaskIdChanged(_arg0);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IMiShareTaskStateListener impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IMiShareTaskStateListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onTaskIdChanged(MiShareTask miShareTask) throws RemoteException;

    void onTaskStateChanged(String str, int i) throws RemoteException;
}
