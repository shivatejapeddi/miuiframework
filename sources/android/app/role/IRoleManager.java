package android.app.role;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.telephony.IFinancialSmsCallback;
import java.util.List;

public interface IRoleManager extends IInterface {

    public static class Default implements IRoleManager {
        public boolean isRoleAvailable(String roleName) throws RemoteException {
            return false;
        }

        public boolean isRoleHeld(String roleName, String packageName) throws RemoteException {
            return false;
        }

        public List<String> getRoleHoldersAsUser(String roleName, int userId) throws RemoteException {
            return null;
        }

        public void addRoleHolderAsUser(String roleName, String packageName, int flags, int userId, RemoteCallback callback) throws RemoteException {
        }

        public void removeRoleHolderAsUser(String roleName, String packageName, int flags, int userId, RemoteCallback callback) throws RemoteException {
        }

        public void clearRoleHoldersAsUser(String roleName, int flags, int userId, RemoteCallback callback) throws RemoteException {
        }

        public void addOnRoleHoldersChangedListenerAsUser(IOnRoleHoldersChangedListener listener, int userId) throws RemoteException {
        }

        public void removeOnRoleHoldersChangedListenerAsUser(IOnRoleHoldersChangedListener listener, int userId) throws RemoteException {
        }

        public void setRoleNamesFromController(List<String> list) throws RemoteException {
        }

        public boolean addRoleHolderFromController(String roleName, String packageName) throws RemoteException {
            return false;
        }

        public boolean removeRoleHolderFromController(String roleName, String packageName) throws RemoteException {
            return false;
        }

        public List<String> getHeldRolesFromController(String packageName) throws RemoteException {
            return null;
        }

        public String getDefaultSmsPackage(int userId) throws RemoteException {
            return null;
        }

        public void getSmsMessagesForFinancialApp(String callingPkg, Bundle params, IFinancialSmsCallback callback) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IRoleManager {
        private static final String DESCRIPTOR = "android.app.role.IRoleManager";
        static final int TRANSACTION_addOnRoleHoldersChangedListenerAsUser = 7;
        static final int TRANSACTION_addRoleHolderAsUser = 4;
        static final int TRANSACTION_addRoleHolderFromController = 10;
        static final int TRANSACTION_clearRoleHoldersAsUser = 6;
        static final int TRANSACTION_getDefaultSmsPackage = 13;
        static final int TRANSACTION_getHeldRolesFromController = 12;
        static final int TRANSACTION_getRoleHoldersAsUser = 3;
        static final int TRANSACTION_getSmsMessagesForFinancialApp = 14;
        static final int TRANSACTION_isRoleAvailable = 1;
        static final int TRANSACTION_isRoleHeld = 2;
        static final int TRANSACTION_removeOnRoleHoldersChangedListenerAsUser = 8;
        static final int TRANSACTION_removeRoleHolderAsUser = 5;
        static final int TRANSACTION_removeRoleHolderFromController = 11;
        static final int TRANSACTION_setRoleNamesFromController = 9;

        private static class Proxy implements IRoleManager {
            public static IRoleManager sDefaultImpl;
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

            public boolean isRoleAvailable(String roleName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(roleName);
                    boolean z = false;
                    if (this.mRemote.transact(1, _data, _reply, z) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() != 0) {
                            z = true;
                        }
                        boolean _result = z;
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    z = Stub.getDefaultImpl().isRoleAvailable(roleName);
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isRoleHeld(String roleName, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(roleName);
                    _data.writeString(packageName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isRoleHeld(roleName, packageName);
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

            public List<String> getRoleHoldersAsUser(String roleName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(roleName);
                    _data.writeInt(userId);
                    List<String> list = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getRoleHoldersAsUser(roleName, userId);
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

            public void addRoleHolderAsUser(String roleName, String packageName, int flags, int userId, RemoteCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(roleName);
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    if (callback != null) {
                        _data.writeInt(1);
                        callback.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addRoleHolderAsUser(roleName, packageName, flags, userId, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeRoleHolderAsUser(String roleName, String packageName, int flags, int userId, RemoteCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(roleName);
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    if (callback != null) {
                        _data.writeInt(1);
                        callback.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeRoleHolderAsUser(roleName, packageName, flags, userId, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearRoleHoldersAsUser(String roleName, int flags, int userId, RemoteCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(roleName);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    if (callback != null) {
                        _data.writeInt(1);
                        callback.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearRoleHoldersAsUser(roleName, flags, userId, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addOnRoleHoldersChangedListenerAsUser(IOnRoleHoldersChangedListener listener, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addOnRoleHoldersChangedListenerAsUser(listener, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeOnRoleHoldersChangedListenerAsUser(IOnRoleHoldersChangedListener listener, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeOnRoleHoldersChangedListenerAsUser(listener, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setRoleNamesFromController(List<String> roleNames) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(roleNames);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setRoleNamesFromController(roleNames);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean addRoleHolderFromController(String roleName, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(roleName);
                    _data.writeString(packageName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().addRoleHolderFromController(roleName, packageName);
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

            public boolean removeRoleHolderFromController(String roleName, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(roleName);
                    _data.writeString(packageName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().removeRoleHolderFromController(roleName, packageName);
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

            public List<String> getHeldRolesFromController(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    List<String> list = 12;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getHeldRolesFromController(packageName);
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

            public String getDefaultSmsPackage(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    String str = 13;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getDefaultSmsPackage(userId);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void getSmsMessagesForFinancialApp(String callingPkg, Bundle params, IFinancialSmsCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().getSmsMessagesForFinancialApp(callingPkg, params, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IRoleManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IRoleManager)) {
                return new Proxy(obj);
            }
            return (IRoleManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "isRoleAvailable";
                case 2:
                    return "isRoleHeld";
                case 3:
                    return "getRoleHoldersAsUser";
                case 4:
                    return "addRoleHolderAsUser";
                case 5:
                    return "removeRoleHolderAsUser";
                case 6:
                    return "clearRoleHoldersAsUser";
                case 7:
                    return "addOnRoleHoldersChangedListenerAsUser";
                case 8:
                    return "removeOnRoleHoldersChangedListenerAsUser";
                case 9:
                    return "setRoleNamesFromController";
                case 10:
                    return "addRoleHolderFromController";
                case 11:
                    return "removeRoleHolderFromController";
                case 12:
                    return "getHeldRolesFromController";
                case 13:
                    return "getDefaultSmsPackage";
                case 14:
                    return "getSmsMessagesForFinancialApp";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            Parcel parcel2 = reply;
            String descriptor = DESCRIPTOR;
            if (i != 1598968902) {
                boolean _result;
                String _arg0;
                String _arg1;
                int _arg2;
                int _arg3;
                RemoteCallback _arg4;
                String _arg02;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        boolean _result2 = isRoleAvailable(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        _result = isRoleHeld(data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        List<String> _result3 = getRoleHoldersAsUser(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeStringList(_result3);
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg1 = data.readString();
                        _arg2 = data.readInt();
                        _arg3 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg4 = (RemoteCallback) RemoteCallback.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg4 = null;
                        }
                        addRoleHolderAsUser(_arg0, _arg1, _arg2, _arg3, _arg4);
                        reply.writeNoException();
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg1 = data.readString();
                        _arg2 = data.readInt();
                        _arg3 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg4 = (RemoteCallback) RemoteCallback.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg4 = null;
                        }
                        removeRoleHolderAsUser(_arg0, _arg1, _arg2, _arg3, _arg4);
                        reply.writeNoException();
                        return true;
                    case 6:
                        RemoteCallback _arg32;
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        int _arg12 = data.readInt();
                        int _arg22 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg32 = (RemoteCallback) RemoteCallback.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg32 = null;
                        }
                        clearRoleHoldersAsUser(_arg02, _arg12, _arg22, _arg32);
                        reply.writeNoException();
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        addOnRoleHoldersChangedListenerAsUser(android.app.role.IOnRoleHoldersChangedListener.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        removeOnRoleHoldersChangedListenerAsUser(android.app.role.IOnRoleHoldersChangedListener.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        setRoleNamesFromController(data.createStringArrayList());
                        reply.writeNoException();
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        _result = addRoleHolderFromController(data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        _result = removeRoleHolderFromController(data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        List<String> _result4 = getHeldRolesFromController(data.readString());
                        reply.writeNoException();
                        parcel2.writeStringList(_result4);
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        String _result5 = getDefaultSmsPackage(data.readInt());
                        reply.writeNoException();
                        parcel2.writeString(_result5);
                        return true;
                    case 14:
                        Bundle _arg13;
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg13 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        getSmsMessagesForFinancialApp(_arg02, _arg13, android.telephony.IFinancialSmsCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IRoleManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IRoleManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void addOnRoleHoldersChangedListenerAsUser(IOnRoleHoldersChangedListener iOnRoleHoldersChangedListener, int i) throws RemoteException;

    void addRoleHolderAsUser(String str, String str2, int i, int i2, RemoteCallback remoteCallback) throws RemoteException;

    boolean addRoleHolderFromController(String str, String str2) throws RemoteException;

    void clearRoleHoldersAsUser(String str, int i, int i2, RemoteCallback remoteCallback) throws RemoteException;

    String getDefaultSmsPackage(int i) throws RemoteException;

    List<String> getHeldRolesFromController(String str) throws RemoteException;

    List<String> getRoleHoldersAsUser(String str, int i) throws RemoteException;

    void getSmsMessagesForFinancialApp(String str, Bundle bundle, IFinancialSmsCallback iFinancialSmsCallback) throws RemoteException;

    boolean isRoleAvailable(String str) throws RemoteException;

    boolean isRoleHeld(String str, String str2) throws RemoteException;

    void removeOnRoleHoldersChangedListenerAsUser(IOnRoleHoldersChangedListener iOnRoleHoldersChangedListener, int i) throws RemoteException;

    void removeRoleHolderAsUser(String str, String str2, int i, int i2, RemoteCallback remoteCallback) throws RemoteException;

    boolean removeRoleHolderFromController(String str, String str2) throws RemoteException;

    void setRoleNamesFromController(List<String> list) throws RemoteException;
}
