package com.miui.internal.transition;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IMiuiGestureControlHelper extends IInterface {

    public static class Default implements IMiuiGestureControlHelper {
        public MiuiAppTransitionAnimationSpec getSpec(String componentName, int userId) throws RemoteException {
            return null;
        }

        public void notifyGestureStartRecents() throws RemoteException {
        }

        public void notifyGestureAnimationStart() throws RemoteException {
        }

        public void notifyGestureAnimationCancel() throws RemoteException {
        }

        public void notifyGestureAnimationEnd() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMiuiGestureControlHelper {
        private static final String DESCRIPTOR = "com.miui.internal.transition.IMiuiGestureControlHelper";
        static final int TRANSACTION_getSpec = 1;
        static final int TRANSACTION_notifyGestureAnimationCancel = 4;
        static final int TRANSACTION_notifyGestureAnimationEnd = 5;
        static final int TRANSACTION_notifyGestureAnimationStart = 3;
        static final int TRANSACTION_notifyGestureStartRecents = 2;

        private static class Proxy implements IMiuiGestureControlHelper {
            public static IMiuiGestureControlHelper sDefaultImpl;
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

            public MiuiAppTransitionAnimationSpec getSpec(String componentName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(componentName);
                    _data.writeInt(userId);
                    MiuiAppTransitionAnimationSpec miuiAppTransitionAnimationSpec = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        miuiAppTransitionAnimationSpec = Stub.getDefaultImpl();
                        if (miuiAppTransitionAnimationSpec != 0) {
                            miuiAppTransitionAnimationSpec = Stub.getDefaultImpl().getSpec(componentName, userId);
                            return miuiAppTransitionAnimationSpec;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        miuiAppTransitionAnimationSpec = (MiuiAppTransitionAnimationSpec) MiuiAppTransitionAnimationSpec.CREATOR.createFromParcel(_reply);
                    } else {
                        miuiAppTransitionAnimationSpec = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return miuiAppTransitionAnimationSpec;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyGestureStartRecents() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().notifyGestureStartRecents();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void notifyGestureAnimationStart() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().notifyGestureAnimationStart();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void notifyGestureAnimationCancel() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().notifyGestureAnimationCancel();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void notifyGestureAnimationEnd() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().notifyGestureAnimationEnd();
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMiuiGestureControlHelper asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IMiuiGestureControlHelper)) {
                return new Proxy(obj);
            }
            return (IMiuiGestureControlHelper) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "getSpec";
            }
            if (transactionCode == 2) {
                return "notifyGestureStartRecents";
            }
            if (transactionCode == 3) {
                return "notifyGestureAnimationStart";
            }
            if (transactionCode == 4) {
                return "notifyGestureAnimationCancel";
            }
            if (transactionCode != 5) {
                return null;
            }
            return "notifyGestureAnimationEnd";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                MiuiAppTransitionAnimationSpec _result = getSpec(data.readString(), data.readInt());
                reply.writeNoException();
                if (_result != null) {
                    reply.writeInt(1);
                    _result.writeToParcel(reply, 1);
                } else {
                    reply.writeInt(0);
                }
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                notifyGestureStartRecents();
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                notifyGestureAnimationStart();
                return true;
            } else if (code == 4) {
                data.enforceInterface(descriptor);
                notifyGestureAnimationCancel();
                return true;
            } else if (code == 5) {
                data.enforceInterface(descriptor);
                notifyGestureAnimationEnd();
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IMiuiGestureControlHelper impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IMiuiGestureControlHelper getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    MiuiAppTransitionAnimationSpec getSpec(String str, int i) throws RemoteException;

    void notifyGestureAnimationCancel() throws RemoteException;

    void notifyGestureAnimationEnd() throws RemoteException;

    void notifyGestureAnimationStart() throws RemoteException;

    void notifyGestureStartRecents() throws RemoteException;
}
