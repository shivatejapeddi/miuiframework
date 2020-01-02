package com.miui.mishare;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IMiShareListener extends IInterface {

    public static class Default implements IMiShareListener {
        public void onTaskReceived(MiShareTask task) throws RemoteException {
        }

        public void onTaskStart(MiShareTask task) throws RemoteException {
        }

        public void onTaskProgress(MiShareTask task, long progress, long total) throws RemoteException {
        }

        public void onTaskFinish(MiShareTask task, int error) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMiShareListener {
        private static final String DESCRIPTOR = "com.miui.mishare.IMiShareListener";
        static final int TRANSACTION_onTaskFinish = 4;
        static final int TRANSACTION_onTaskProgress = 3;
        static final int TRANSACTION_onTaskReceived = 1;
        static final int TRANSACTION_onTaskStart = 2;

        private static class Proxy implements IMiShareListener {
            public static IMiShareListener sDefaultImpl;
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

            public void onTaskReceived(MiShareTask task) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (task != null) {
                        _data.writeInt(1);
                        task.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onTaskReceived(task);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onTaskStart(MiShareTask task) throws RemoteException {
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
                        Stub.getDefaultImpl().onTaskStart(task);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onTaskProgress(MiShareTask task, long progress, long total) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (task != null) {
                        _data.writeInt(1);
                        task.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeLong(progress);
                    _data.writeLong(total);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onTaskProgress(task, progress, total);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onTaskFinish(MiShareTask task, int error) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (task != null) {
                        _data.writeInt(1);
                        task.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(error);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onTaskFinish(task, error);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMiShareListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IMiShareListener)) {
                return new Proxy(obj);
            }
            return (IMiShareListener) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onTaskReceived";
            }
            if (transactionCode == 2) {
                return "onTaskStart";
            }
            if (transactionCode == 3) {
                return "onTaskProgress";
            }
            if (transactionCode != 4) {
                return null;
            }
            return "onTaskFinish";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            String descriptor = DESCRIPTOR;
            Parcel parcel2;
            MiShareTask _arg0;
            if (i == 1) {
                parcel2 = reply;
                parcel.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (MiShareTask) MiShareTask.CREATOR.createFromParcel(parcel);
                } else {
                    _arg0 = null;
                }
                onTaskReceived(_arg0);
                return true;
            } else if (i == 2) {
                parcel2 = reply;
                parcel.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (MiShareTask) MiShareTask.CREATOR.createFromParcel(parcel);
                } else {
                    _arg0 = null;
                }
                onTaskStart(_arg0);
                return true;
            } else if (i == 3) {
                MiShareTask _arg02;
                parcel2 = reply;
                parcel.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg02 = (MiShareTask) MiShareTask.CREATOR.createFromParcel(parcel);
                } else {
                    _arg02 = null;
                }
                onTaskProgress(_arg02, data.readLong(), data.readLong());
                return true;
            } else if (i == 4) {
                parcel2 = reply;
                parcel.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (MiShareTask) MiShareTask.CREATOR.createFromParcel(parcel);
                } else {
                    _arg0 = null;
                }
                onTaskFinish(_arg0, data.readInt());
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IMiShareListener impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IMiShareListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onTaskFinish(MiShareTask miShareTask, int i) throws RemoteException;

    void onTaskProgress(MiShareTask miShareTask, long j, long j2) throws RemoteException;

    void onTaskReceived(MiShareTask miShareTask) throws RemoteException;

    void onTaskStart(MiShareTask miShareTask) throws RemoteException;
}
