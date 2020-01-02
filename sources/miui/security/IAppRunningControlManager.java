package miui.security;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IAppRunningControlManager extends IInterface {

    public static class Default implements IAppRunningControlManager {
        public void setDisallowRunningList(List<String> list, Intent intent) throws RemoteException {
        }

        public Intent getBlockActivityIntent(String packageName, Intent intent, boolean fromActivity, int requestCode) throws RemoteException {
            return null;
        }

        public void setBlackListEnable(boolean isEnable) throws RemoteException {
        }

        public boolean matchRule(String pkgName, int wakeType) throws RemoteException {
            return false;
        }

        public List<String> getNotDisallowList() throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IAppRunningControlManager {
        private static final String DESCRIPTOR = "miui.security.IAppRunningControlManager";
        static final int TRANSACTION_getBlockActivityIntent = 2;
        static final int TRANSACTION_getNotDisallowList = 5;
        static final int TRANSACTION_matchRule = 4;
        static final int TRANSACTION_setBlackListEnable = 3;
        static final int TRANSACTION_setDisallowRunningList = 1;

        private static class Proxy implements IAppRunningControlManager {
            public static IAppRunningControlManager sDefaultImpl;
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

            public void setDisallowRunningList(List<String> list, Intent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(list);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setDisallowRunningList(list, intent);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Intent getBlockActivityIntent(String packageName, Intent intent, boolean fromActivity, int requestCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    int i = 1;
                    Intent intent2 = 0;
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!fromActivity) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeInt(requestCode);
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        intent2 = Stub.getDefaultImpl();
                        if (intent2 != 0) {
                            intent2 = Stub.getDefaultImpl().getBlockActivityIntent(packageName, intent, fromActivity, requestCode);
                            return intent2;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        intent2 = (Intent) Intent.CREATOR.createFromParcel(_reply);
                    } else {
                        intent2 = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return intent2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setBlackListEnable(boolean isEnable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isEnable ? 1 : 0);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setBlackListEnable(isEnable);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean matchRule(String pkgName, int wakeType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeInt(wakeType);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().matchRule(pkgName, wakeType);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getNotDisallowList() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<String> list = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getNotDisallowList();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createStringArrayList();
                    List<String> _result = list;
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

        public static IAppRunningControlManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IAppRunningControlManager)) {
                return new Proxy(obj);
            }
            return (IAppRunningControlManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "setDisallowRunningList";
            }
            if (transactionCode == 2) {
                return "getBlockActivityIntent";
            }
            if (transactionCode == 3) {
                return "setBlackListEnable";
            }
            if (transactionCode == 4) {
                return "matchRule";
            }
            if (transactionCode != 5) {
                return null;
            }
            return "getNotDisallowList";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            List<String> _result;
            if (code != 1) {
                boolean z = false;
                if (code == 2) {
                    Intent _arg1;
                    data.enforceInterface(descriptor);
                    String _arg0 = data.readString();
                    if (data.readInt() != 0) {
                        _arg1 = (Intent) Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    Intent _result2 = getBlockActivityIntent(_arg0, _arg1, data.readInt() != 0, data.readInt());
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                } else if (code == 3) {
                    data.enforceInterface(descriptor);
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    setBlackListEnable(z);
                    reply.writeNoException();
                    return true;
                } else if (code == 4) {
                    data.enforceInterface(descriptor);
                    boolean _result3 = matchRule(data.readString(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                } else if (code == 5) {
                    data.enforceInterface(descriptor);
                    _result = getNotDisallowList();
                    reply.writeNoException();
                    reply.writeStringList(_result);
                    return true;
                } else if (code != IBinder.INTERFACE_TRANSACTION) {
                    return super.onTransact(code, data, reply, flags);
                } else {
                    reply.writeString(descriptor);
                    return true;
                }
            }
            Intent _arg12;
            data.enforceInterface(descriptor);
            _result = data.createStringArrayList();
            if (data.readInt() != 0) {
                _arg12 = (Intent) Intent.CREATOR.createFromParcel(data);
            } else {
                _arg12 = null;
            }
            setDisallowRunningList(_result, _arg12);
            reply.writeNoException();
            return true;
        }

        public static boolean setDefaultImpl(IAppRunningControlManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IAppRunningControlManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    Intent getBlockActivityIntent(String str, Intent intent, boolean z, int i) throws RemoteException;

    List<String> getNotDisallowList() throws RemoteException;

    boolean matchRule(String str, int i) throws RemoteException;

    void setBlackListEnable(boolean z) throws RemoteException;

    void setDisallowRunningList(List<String> list, Intent intent) throws RemoteException;
}
