package android.content.om;

import android.annotation.UnsupportedAppUsage;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IOverlayManager extends IInterface {

    public static class Default implements IOverlayManager {
        public Map getAllOverlays(int userId) throws RemoteException {
            return null;
        }

        public List getOverlayInfosForTarget(String targetPackageName, int userId) throws RemoteException {
            return null;
        }

        public OverlayInfo getOverlayInfo(String packageName, int userId) throws RemoteException {
            return null;
        }

        public boolean setEnabled(String packageName, boolean enable, int userId) throws RemoteException {
            return false;
        }

        public boolean setEnabledExclusive(String packageName, boolean enable, int userId) throws RemoteException {
            return false;
        }

        public boolean setEnabledExclusiveInCategory(String packageName, int userId) throws RemoteException {
            return false;
        }

        public boolean setPriority(String packageName, String newParentPackageName, int userId) throws RemoteException {
            return false;
        }

        public boolean setHighestPriority(String packageName, int userId) throws RemoteException {
            return false;
        }

        public boolean setLowestPriority(String packageName, int userId) throws RemoteException {
            return false;
        }

        public String[] getDefaultOverlayPackages() throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IOverlayManager {
        private static final String DESCRIPTOR = "android.content.om.IOverlayManager";
        static final int TRANSACTION_getAllOverlays = 1;
        static final int TRANSACTION_getDefaultOverlayPackages = 10;
        static final int TRANSACTION_getOverlayInfo = 3;
        static final int TRANSACTION_getOverlayInfosForTarget = 2;
        static final int TRANSACTION_setEnabled = 4;
        static final int TRANSACTION_setEnabledExclusive = 5;
        static final int TRANSACTION_setEnabledExclusiveInCategory = 6;
        static final int TRANSACTION_setHighestPriority = 8;
        static final int TRANSACTION_setLowestPriority = 9;
        static final int TRANSACTION_setPriority = 7;

        private static class Proxy implements IOverlayManager {
            public static IOverlayManager sDefaultImpl;
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

            public Map getAllOverlays(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    Map map = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        map = Stub.getDefaultImpl();
                        if (map != 0) {
                            map = Stub.getDefaultImpl().getAllOverlays(userId);
                            return map;
                        }
                    }
                    _reply.readException();
                    map = getClass().getClassLoader();
                    HashMap _result = _reply.readHashMap(map);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List getOverlayInfosForTarget(String targetPackageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(targetPackageName);
                    _data.writeInt(userId);
                    List list = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getOverlayInfosForTarget(targetPackageName, userId);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = getClass().getClassLoader();
                    ArrayList _result = _reply.readArrayList(list);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public OverlayInfo getOverlayInfo(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    OverlayInfo overlayInfo = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        overlayInfo = Stub.getDefaultImpl();
                        if (overlayInfo != 0) {
                            overlayInfo = Stub.getDefaultImpl().getOverlayInfo(packageName, userId);
                            return overlayInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        overlayInfo = (OverlayInfo) OverlayInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        overlayInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return overlayInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setEnabled(String packageName, boolean enable, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _result = true;
                    _data.writeInt(enable ? 1 : 0);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setEnabled(packageName, enable, userId);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setEnabledExclusive(String packageName, boolean enable, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _result = true;
                    _data.writeInt(enable ? 1 : 0);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setEnabledExclusive(packageName, enable, userId);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setEnabledExclusiveInCategory(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().setEnabledExclusiveInCategory(packageName, userId);
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

            public boolean setPriority(String packageName, String newParentPackageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(newParentPackageName);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().setPriority(packageName, newParentPackageName, userId);
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

            public boolean setHighestPriority(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().setHighestPriority(packageName, userId);
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

            public boolean setLowestPriority(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().setLowestPriority(packageName, userId);
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

            public String[] getDefaultOverlayPackages() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String[] strArr = 10;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getDefaultOverlayPackages();
                            return strArr;
                        }
                    }
                    _reply.readException();
                    strArr = _reply.createStringArray();
                    String[] _result = strArr;
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

        public static IOverlayManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IOverlayManager)) {
                return new Proxy(obj);
            }
            return (IOverlayManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getAllOverlays";
                case 2:
                    return "getOverlayInfosForTarget";
                case 3:
                    return "getOverlayInfo";
                case 4:
                    return "setEnabled";
                case 5:
                    return "setEnabledExclusive";
                case 6:
                    return "setEnabledExclusiveInCategory";
                case 7:
                    return "setPriority";
                case 8:
                    return "setHighestPriority";
                case 9:
                    return "setLowestPriority";
                case 10:
                    return "getDefaultOverlayPackages";
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
                boolean _arg1 = false;
                String _arg0;
                boolean _result;
                boolean _result2;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        Map _result3 = getAllOverlays(data.readInt());
                        reply.writeNoException();
                        reply.writeMap(_result3);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        List _result4 = getOverlayInfosForTarget(data.readString(), data.readInt());
                        reply.writeNoException();
                        reply.writeList(_result4);
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        OverlayInfo _result5 = getOverlayInfo(data.readString(), data.readInt());
                        reply.writeNoException();
                        if (_result5 != null) {
                            reply.writeInt(1);
                            _result5.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        _result = setEnabled(_arg0, _arg1, data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        _result = setEnabledExclusive(_arg0, _arg1, data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        _result2 = setEnabledExclusiveInCategory(data.readString(), data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        _result = setPriority(data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        _result2 = setHighestPriority(data.readString(), data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        _result2 = setLowestPriority(data.readString(), data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        String[] _result6 = getDefaultOverlayPackages();
                        reply.writeNoException();
                        reply.writeStringArray(_result6);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IOverlayManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IOverlayManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    @UnsupportedAppUsage
    Map getAllOverlays(int i) throws RemoteException;

    String[] getDefaultOverlayPackages() throws RemoteException;

    @UnsupportedAppUsage
    OverlayInfo getOverlayInfo(String str, int i) throws RemoteException;

    List getOverlayInfosForTarget(String str, int i) throws RemoteException;

    boolean setEnabled(String str, boolean z, int i) throws RemoteException;

    boolean setEnabledExclusive(String str, boolean z, int i) throws RemoteException;

    boolean setEnabledExclusiveInCategory(String str, int i) throws RemoteException;

    boolean setHighestPriority(String str, int i) throws RemoteException;

    boolean setLowestPriority(String str, int i) throws RemoteException;

    boolean setPriority(String str, String str2, int i) throws RemoteException;
}
