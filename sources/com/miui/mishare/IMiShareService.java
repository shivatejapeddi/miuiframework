package com.miui.mishare;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IMiShareService extends IInterface {

    public static class Default implements IMiShareService {
        public int getState() throws RemoteException {
            return 0;
        }

        public void registerStateListener(IMiShareStateListener listener) throws RemoteException {
        }

        public void unregisterStateListener(IMiShareStateListener listener) throws RemoteException {
        }

        public void enable() throws RemoteException {
        }

        public void disable() throws RemoteException {
        }

        public void discover(IMiShareDiscoverCallback callback) throws RemoteException {
        }

        public void discoverWithIntent(IMiShareDiscoverCallback callback, Intent intent) throws RemoteException {
        }

        public void stopDiscover(IMiShareDiscoverCallback callback) throws RemoteException {
        }

        public void send(MiShareTask task) throws RemoteException {
        }

        public void cancel(MiShareTask task) throws RemoteException {
        }

        public void registerTaskStateListener(IMiShareTaskStateListener listener) throws RemoteException {
        }

        public void unregisterTaskStateListener(IMiShareTaskStateListener listener) throws RemoteException {
        }

        public void receive(MiShareTask task) throws RemoteException {
        }

        public void refuse(MiShareTask task) throws RemoteException {
        }

        public void getThumbnail(MiShareTask task, IThumbnailCallback callback) throws RemoteException {
        }

        public void registerScreenThrowListener(IScreenThrowListener listener) throws RemoteException {
        }

        public void unregisterScreenThrowListener() throws RemoteException {
        }

        public void openScreenThrow() throws RemoteException {
        }

        public void closeScreenThrow() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMiShareService {
        private static final String DESCRIPTOR = "com.miui.mishare.IMiShareService";
        static final int TRANSACTION_cancel = 10;
        static final int TRANSACTION_closeScreenThrow = 19;
        static final int TRANSACTION_disable = 5;
        static final int TRANSACTION_discover = 6;
        static final int TRANSACTION_discoverWithIntent = 7;
        static final int TRANSACTION_enable = 4;
        static final int TRANSACTION_getState = 1;
        static final int TRANSACTION_getThumbnail = 15;
        static final int TRANSACTION_openScreenThrow = 18;
        static final int TRANSACTION_receive = 13;
        static final int TRANSACTION_refuse = 14;
        static final int TRANSACTION_registerScreenThrowListener = 16;
        static final int TRANSACTION_registerStateListener = 2;
        static final int TRANSACTION_registerTaskStateListener = 11;
        static final int TRANSACTION_send = 9;
        static final int TRANSACTION_stopDiscover = 8;
        static final int TRANSACTION_unregisterScreenThrowListener = 17;
        static final int TRANSACTION_unregisterStateListener = 3;
        static final int TRANSACTION_unregisterTaskStateListener = 12;

        private static class Proxy implements IMiShareService {
            public static IMiShareService sDefaultImpl;
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

            public int getState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getState();
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerStateListener(IMiShareStateListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().registerStateListener(listener);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void unregisterStateListener(IMiShareStateListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().unregisterStateListener(listener);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void enable() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().enable();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void disable() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().disable();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void discover(IMiShareDiscoverCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().discover(callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void discoverWithIntent(IMiShareDiscoverCallback callback, Intent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(7, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().discoverWithIntent(callback, intent);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void stopDiscover(IMiShareDiscoverCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(8, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().stopDiscover(callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void send(MiShareTask task) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (task != null) {
                        _data.writeInt(1);
                        task.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(9, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().send(task);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void cancel(MiShareTask task) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (task != null) {
                        _data.writeInt(1);
                        task.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(10, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().cancel(task);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void registerTaskStateListener(IMiShareTaskStateListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(11, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().registerTaskStateListener(listener);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void unregisterTaskStateListener(IMiShareTaskStateListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(12, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().unregisterTaskStateListener(listener);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void receive(MiShareTask task) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (task != null) {
                        _data.writeInt(1);
                        task.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(13, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().receive(task);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void refuse(MiShareTask task) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (task != null) {
                        _data.writeInt(1);
                        task.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(14, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().refuse(task);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void getThumbnail(MiShareTask task, IThumbnailCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (task != null) {
                        _data.writeInt(1);
                        task.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(15, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().getThumbnail(task, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void registerScreenThrowListener(IScreenThrowListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(16, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().registerScreenThrowListener(listener);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void unregisterScreenThrowListener() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(17, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().unregisterScreenThrowListener();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void openScreenThrow() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(18, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().openScreenThrow();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void closeScreenThrow() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(19, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().closeScreenThrow();
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMiShareService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IMiShareService)) {
                return new Proxy(obj);
            }
            return (IMiShareService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getState";
                case 2:
                    return "registerStateListener";
                case 3:
                    return "unregisterStateListener";
                case 4:
                    return "enable";
                case 5:
                    return "disable";
                case 6:
                    return "discover";
                case 7:
                    return "discoverWithIntent";
                case 8:
                    return "stopDiscover";
                case 9:
                    return "send";
                case 10:
                    return "cancel";
                case 11:
                    return "registerTaskStateListener";
                case 12:
                    return "unregisterTaskStateListener";
                case 13:
                    return "receive";
                case 14:
                    return "refuse";
                case 15:
                    return "getThumbnail";
                case 16:
                    return "registerScreenThrowListener";
                case 17:
                    return "unregisterScreenThrowListener";
                case 18:
                    return "openScreenThrow";
                case 19:
                    return "closeScreenThrow";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code != IBinder.INTERFACE_TRANSACTION) {
                MiShareTask _arg0;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        int _result = getState();
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        registerStateListener(com.miui.mishare.IMiShareStateListener.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        unregisterStateListener(com.miui.mishare.IMiShareStateListener.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        enable();
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        disable();
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        discover(com.miui.mishare.IMiShareDiscoverCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 7:
                        Intent _arg1;
                        data.enforceInterface(descriptor);
                        IMiShareDiscoverCallback _arg02 = com.miui.mishare.IMiShareDiscoverCallback.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg1 = (Intent) Intent.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        discoverWithIntent(_arg02, _arg1);
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        stopDiscover(com.miui.mishare.IMiShareDiscoverCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (MiShareTask) MiShareTask.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        send(_arg0);
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (MiShareTask) MiShareTask.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        cancel(_arg0);
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        registerTaskStateListener(com.miui.mishare.IMiShareTaskStateListener.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        unregisterTaskStateListener(com.miui.mishare.IMiShareTaskStateListener.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (MiShareTask) MiShareTask.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        receive(_arg0);
                        return true;
                    case 14:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (MiShareTask) MiShareTask.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        refuse(_arg0);
                        return true;
                    case 15:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (MiShareTask) MiShareTask.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        getThumbnail(_arg0, com.miui.mishare.IThumbnailCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 16:
                        data.enforceInterface(descriptor);
                        registerScreenThrowListener(com.miui.mishare.IScreenThrowListener.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 17:
                        data.enforceInterface(descriptor);
                        unregisterScreenThrowListener();
                        return true;
                    case 18:
                        data.enforceInterface(descriptor);
                        openScreenThrow();
                        return true;
                    case 19:
                        data.enforceInterface(descriptor);
                        closeScreenThrow();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IMiShareService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IMiShareService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void cancel(MiShareTask miShareTask) throws RemoteException;

    void closeScreenThrow() throws RemoteException;

    void disable() throws RemoteException;

    void discover(IMiShareDiscoverCallback iMiShareDiscoverCallback) throws RemoteException;

    void discoverWithIntent(IMiShareDiscoverCallback iMiShareDiscoverCallback, Intent intent) throws RemoteException;

    void enable() throws RemoteException;

    int getState() throws RemoteException;

    void getThumbnail(MiShareTask miShareTask, IThumbnailCallback iThumbnailCallback) throws RemoteException;

    void openScreenThrow() throws RemoteException;

    void receive(MiShareTask miShareTask) throws RemoteException;

    void refuse(MiShareTask miShareTask) throws RemoteException;

    void registerScreenThrowListener(IScreenThrowListener iScreenThrowListener) throws RemoteException;

    void registerStateListener(IMiShareStateListener iMiShareStateListener) throws RemoteException;

    void registerTaskStateListener(IMiShareTaskStateListener iMiShareTaskStateListener) throws RemoteException;

    void send(MiShareTask miShareTask) throws RemoteException;

    void stopDiscover(IMiShareDiscoverCallback iMiShareDiscoverCallback) throws RemoteException;

    void unregisterScreenThrowListener() throws RemoteException;

    void unregisterStateListener(IMiShareStateListener iMiShareStateListener) throws RemoteException;

    void unregisterTaskStateListener(IMiShareTaskStateListener iMiShareTaskStateListener) throws RemoteException;
}
