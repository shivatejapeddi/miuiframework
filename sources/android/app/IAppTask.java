package android.app;

import android.annotation.UnsupportedAppUsage;
import android.app.ActivityManager.RecentTaskInfo;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IAppTask extends IInterface {

    public static class Default implements IAppTask {
        public void finishAndRemoveTask() throws RemoteException {
        }

        public RecentTaskInfo getTaskInfo() throws RemoteException {
            return null;
        }

        public void moveToFront(IApplicationThread appThread, String callingPackage) throws RemoteException {
        }

        public int startActivity(IBinder whoThread, String callingPackage, Intent intent, String resolvedType, Bundle options) throws RemoteException {
            return 0;
        }

        public void setExcludeFromRecents(boolean exclude) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IAppTask {
        private static final String DESCRIPTOR = "android.app.IAppTask";
        static final int TRANSACTION_finishAndRemoveTask = 1;
        static final int TRANSACTION_getTaskInfo = 2;
        static final int TRANSACTION_moveToFront = 3;
        static final int TRANSACTION_setExcludeFromRecents = 5;
        static final int TRANSACTION_startActivity = 4;

        private static class Proxy implements IAppTask {
            public static IAppTask sDefaultImpl;
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

            public void finishAndRemoveTask() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().finishAndRemoveTask();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public RecentTaskInfo getTaskInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    RecentTaskInfo recentTaskInfo = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        recentTaskInfo = Stub.getDefaultImpl();
                        if (recentTaskInfo != 0) {
                            recentTaskInfo = Stub.getDefaultImpl().getTaskInfo();
                            return recentTaskInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        recentTaskInfo = (RecentTaskInfo) RecentTaskInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        recentTaskInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return recentTaskInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void moveToFront(IApplicationThread appThread, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(appThread != null ? appThread.asBinder() : null);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().moveToFront(appThread, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int startActivity(IBinder whoThread, String callingPackage, Intent intent, String resolvedType, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(whoThread);
                    _data.writeString(callingPackage);
                    int i = 0;
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().startActivity(whoThread, callingPackage, intent, resolvedType, options);
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

            public void setExcludeFromRecents(boolean exclude) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(exclude ? 1 : 0);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setExcludeFromRecents(exclude);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAppTask asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IAppTask)) {
                return new Proxy(obj);
            }
            return (IAppTask) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "finishAndRemoveTask";
            }
            if (transactionCode == 2) {
                return "getTaskInfo";
            }
            if (transactionCode == 3) {
                return "moveToFront";
            }
            if (transactionCode == 4) {
                return "startActivity";
            }
            if (transactionCode != 5) {
                return null;
            }
            return "setExcludeFromRecents";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            Parcel parcel2 = reply;
            String descriptor = DESCRIPTOR;
            if (i != 1) {
                boolean z = false;
                if (i == 2) {
                    parcel.enforceInterface(descriptor);
                    RecentTaskInfo _result = getTaskInfo();
                    reply.writeNoException();
                    if (_result != null) {
                        parcel2.writeInt(1);
                        _result.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                } else if (i == 3) {
                    parcel.enforceInterface(descriptor);
                    moveToFront(android.app.IApplicationThread.Stub.asInterface(data.readStrongBinder()), data.readString());
                    reply.writeNoException();
                    return true;
                } else if (i == 4) {
                    Intent _arg2;
                    Bundle _arg4;
                    parcel.enforceInterface(descriptor);
                    IBinder _arg0 = data.readStrongBinder();
                    String _arg1 = data.readString();
                    if (data.readInt() != 0) {
                        _arg2 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                    } else {
                        _arg2 = null;
                    }
                    String _arg3 = data.readString();
                    if (data.readInt() != 0) {
                        _arg4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                    } else {
                        _arg4 = null;
                    }
                    int _result2 = startActivity(_arg0, _arg1, _arg2, _arg3, _arg4);
                    reply.writeNoException();
                    parcel2.writeInt(_result2);
                    return true;
                } else if (i == 5) {
                    parcel.enforceInterface(descriptor);
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    setExcludeFromRecents(z);
                    reply.writeNoException();
                    return true;
                } else if (i != 1598968902) {
                    return super.onTransact(code, data, reply, flags);
                } else {
                    parcel2.writeString(descriptor);
                    return true;
                }
            }
            parcel.enforceInterface(descriptor);
            finishAndRemoveTask();
            reply.writeNoException();
            return true;
        }

        public static boolean setDefaultImpl(IAppTask impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IAppTask getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void finishAndRemoveTask() throws RemoteException;

    @UnsupportedAppUsage
    RecentTaskInfo getTaskInfo() throws RemoteException;

    void moveToFront(IApplicationThread iApplicationThread, String str) throws RemoteException;

    void setExcludeFromRecents(boolean z) throws RemoteException;

    int startActivity(IBinder iBinder, String str, Intent intent, String str2, Bundle bundle) throws RemoteException;
}
