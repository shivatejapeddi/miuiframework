package miui.os;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IMiuiInit extends IInterface {

    public static class Default implements IMiuiInit {
        public boolean initCustEnvironment(String custVariant, IMiuiInitObserver obs) throws RemoteException {
            return false;
        }

        public String[] getCustVariants() throws RemoteException {
            return null;
        }

        public void installPreinstallApp() throws RemoteException {
        }

        public void doFactoryReset(boolean keepUserApps) throws RemoteException {
        }

        public boolean isPreinstalledPackage(String pkg) throws RemoteException {
            return false;
        }

        public boolean isPreinstalledPAIPackage(String pkg) throws RemoteException {
            return false;
        }

        public String getMiuiChannelPath(String pkg) throws RemoteException {
            return null;
        }

        public void removeFromPreinstallList(String pkg) throws RemoteException {
        }

        public void removeFromPreinstallPAIList(String pkg) throws RemoteException {
        }

        public void writePreinstallPAIPackage(String pkg) throws RemoteException {
        }

        public void copyPreinstallPAITrackingFile(String type, String fileName, String content) throws RemoteException {
        }

        public String getMiuiPreinstallAppPath(String pkg) throws RemoteException {
            return null;
        }

        public void setRestrictAspect(String pkg, boolean restrict) throws RemoteException {
        }

        public boolean isRestrictAspect(String pkg) throws RemoteException {
            return false;
        }

        public float getAspectRatio(String pkg) throws RemoteException {
            return 0.0f;
        }

        public int getDefaultAspectType(String pkg) throws RemoteException {
            return 0;
        }

        public int getNotchConfig(String pkg) throws RemoteException {
            return 0;
        }

        public int getPreinstalledAppVersion(String pkg) throws RemoteException {
            return 0;
        }

        public void setNotchSpecialMode(String pkg, boolean special) throws RemoteException {
        }

        public void setCutoutMode(String pkg, int mode) throws RemoteException {
        }

        public int getCutoutMode(String pkg) throws RemoteException {
            return 0;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMiuiInit {
        private static final String DESCRIPTOR = "miui.os.IMiuiInit";
        static final int TRANSACTION_copyPreinstallPAITrackingFile = 11;
        static final int TRANSACTION_doFactoryReset = 4;
        static final int TRANSACTION_getAspectRatio = 15;
        static final int TRANSACTION_getCustVariants = 2;
        static final int TRANSACTION_getCutoutMode = 21;
        static final int TRANSACTION_getDefaultAspectType = 16;
        static final int TRANSACTION_getMiuiChannelPath = 7;
        static final int TRANSACTION_getMiuiPreinstallAppPath = 12;
        static final int TRANSACTION_getNotchConfig = 17;
        static final int TRANSACTION_getPreinstalledAppVersion = 18;
        static final int TRANSACTION_initCustEnvironment = 1;
        static final int TRANSACTION_installPreinstallApp = 3;
        static final int TRANSACTION_isPreinstalledPAIPackage = 6;
        static final int TRANSACTION_isPreinstalledPackage = 5;
        static final int TRANSACTION_isRestrictAspect = 14;
        static final int TRANSACTION_removeFromPreinstallList = 8;
        static final int TRANSACTION_removeFromPreinstallPAIList = 9;
        static final int TRANSACTION_setCutoutMode = 20;
        static final int TRANSACTION_setNotchSpecialMode = 19;
        static final int TRANSACTION_setRestrictAspect = 13;
        static final int TRANSACTION_writePreinstallPAIPackage = 10;

        private static class Proxy implements IMiuiInit {
            public static IMiuiInit sDefaultImpl;
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

            public boolean initCustEnvironment(String custVariant, IMiuiInitObserver obs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(custVariant);
                    _data.writeStrongBinder(obs != null ? obs.asBinder() : null);
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
                    z = Stub.getDefaultImpl().initCustEnvironment(custVariant, obs);
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getCustVariants() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String[] strArr = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getCustVariants();
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

            public void installPreinstallApp() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().installPreinstallApp();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void doFactoryReset(boolean keepUserApps) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(keepUserApps ? 1 : 0);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().doFactoryReset(keepUserApps);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isPreinstalledPackage(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isPreinstalledPackage(pkg);
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

            public boolean isPreinstalledPAIPackage(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isPreinstalledPAIPackage(pkg);
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

            public String getMiuiChannelPath(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    String str = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getMiuiChannelPath(pkg);
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

            public void removeFromPreinstallList(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeFromPreinstallList(pkg);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeFromPreinstallPAIList(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeFromPreinstallPAIList(pkg);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void writePreinstallPAIPackage(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().writePreinstallPAIPackage(pkg);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void copyPreinstallPAITrackingFile(String type, String fileName, String content) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(type);
                    _data.writeString(fileName);
                    _data.writeString(content);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().copyPreinstallPAITrackingFile(type, fileName, content);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getMiuiPreinstallAppPath(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    String str = 12;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getMiuiPreinstallAppPath(pkg);
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

            public void setRestrictAspect(String pkg, boolean restrict) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(restrict ? 1 : 0);
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setRestrictAspect(pkg, restrict);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isRestrictAspect(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(14, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isRestrictAspect(pkg);
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

            public float getAspectRatio(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    float f = 15;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        f = Stub.getDefaultImpl();
                        if (f != 0) {
                            f = Stub.getDefaultImpl().getAspectRatio(pkg);
                            return f;
                        }
                    }
                    _reply.readException();
                    f = _reply.readFloat();
                    int _result = f;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getDefaultAspectType(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    int i = 16;
                    if (!this.mRemote.transact(16, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getDefaultAspectType(pkg);
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

            public int getNotchConfig(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    int i = 17;
                    if (!this.mRemote.transact(17, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getNotchConfig(pkg);
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

            public int getPreinstalledAppVersion(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    int i = 18;
                    if (!this.mRemote.transact(18, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getPreinstalledAppVersion(pkg);
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

            public void setNotchSpecialMode(String pkg, boolean special) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(special ? 1 : 0);
                    if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setNotchSpecialMode(pkg, special);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setCutoutMode(String pkg, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(mode);
                    if (this.mRemote.transact(20, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setCutoutMode(pkg, mode);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public int getCutoutMode(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    int i = 21;
                    if (!this.mRemote.transact(21, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getCutoutMode(pkg);
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMiuiInit asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IMiuiInit)) {
                return new Proxy(obj);
            }
            return (IMiuiInit) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "initCustEnvironment";
                case 2:
                    return "getCustVariants";
                case 3:
                    return "installPreinstallApp";
                case 4:
                    return "doFactoryReset";
                case 5:
                    return "isPreinstalledPackage";
                case 6:
                    return "isPreinstalledPAIPackage";
                case 7:
                    return "getMiuiChannelPath";
                case 8:
                    return "removeFromPreinstallList";
                case 9:
                    return "removeFromPreinstallPAIList";
                case 10:
                    return "writePreinstallPAIPackage";
                case 11:
                    return "copyPreinstallPAITrackingFile";
                case 12:
                    return "getMiuiPreinstallAppPath";
                case 13:
                    return "setRestrictAspect";
                case 14:
                    return "isRestrictAspect";
                case 15:
                    return "getAspectRatio";
                case 16:
                    return "getDefaultAspectType";
                case 17:
                    return "getNotchConfig";
                case 18:
                    return "getPreinstalledAppVersion";
                case 19:
                    return "setNotchSpecialMode";
                case 20:
                    return "setCutoutMode";
                case 21:
                    return "getCutoutMode";
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
                boolean _result;
                String _result2;
                int _result3;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        boolean _result4 = initCustEnvironment(data.readString(), miui.os.IMiuiInitObserver.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        String[] _result5 = getCustVariants();
                        reply.writeNoException();
                        reply.writeStringArray(_result5);
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        installPreinstallApp();
                        reply.writeNoException();
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        doFactoryReset(_arg1);
                        reply.writeNoException();
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        _result = isPreinstalledPackage(data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        _result = isPreinstalledPAIPackage(data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        _result2 = getMiuiChannelPath(data.readString());
                        reply.writeNoException();
                        reply.writeString(_result2);
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        removeFromPreinstallList(data.readString());
                        reply.writeNoException();
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        removeFromPreinstallPAIList(data.readString());
                        reply.writeNoException();
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        writePreinstallPAIPackage(data.readString());
                        reply.writeNoException();
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        copyPreinstallPAITrackingFile(data.readString(), data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        _result2 = getMiuiPreinstallAppPath(data.readString());
                        reply.writeNoException();
                        reply.writeString(_result2);
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        _result2 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setRestrictAspect(_result2, _arg1);
                        reply.writeNoException();
                        return true;
                    case 14:
                        data.enforceInterface(descriptor);
                        _result = isRestrictAspect(data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 15:
                        data.enforceInterface(descriptor);
                        float _result6 = getAspectRatio(data.readString());
                        reply.writeNoException();
                        reply.writeFloat(_result6);
                        return true;
                    case 16:
                        data.enforceInterface(descriptor);
                        _result3 = getDefaultAspectType(data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result3);
                        return true;
                    case 17:
                        data.enforceInterface(descriptor);
                        _result3 = getNotchConfig(data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result3);
                        return true;
                    case 18:
                        data.enforceInterface(descriptor);
                        _result3 = getPreinstalledAppVersion(data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result3);
                        return true;
                    case 19:
                        data.enforceInterface(descriptor);
                        _result2 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setNotchSpecialMode(_result2, _arg1);
                        reply.writeNoException();
                        return true;
                    case 20:
                        data.enforceInterface(descriptor);
                        setCutoutMode(data.readString(), data.readInt());
                        return true;
                    case 21:
                        data.enforceInterface(descriptor);
                        _result3 = getCutoutMode(data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result3);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IMiuiInit impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IMiuiInit getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void copyPreinstallPAITrackingFile(String str, String str2, String str3) throws RemoteException;

    void doFactoryReset(boolean z) throws RemoteException;

    float getAspectRatio(String str) throws RemoteException;

    String[] getCustVariants() throws RemoteException;

    int getCutoutMode(String str) throws RemoteException;

    int getDefaultAspectType(String str) throws RemoteException;

    String getMiuiChannelPath(String str) throws RemoteException;

    String getMiuiPreinstallAppPath(String str) throws RemoteException;

    int getNotchConfig(String str) throws RemoteException;

    int getPreinstalledAppVersion(String str) throws RemoteException;

    boolean initCustEnvironment(String str, IMiuiInitObserver iMiuiInitObserver) throws RemoteException;

    void installPreinstallApp() throws RemoteException;

    boolean isPreinstalledPAIPackage(String str) throws RemoteException;

    boolean isPreinstalledPackage(String str) throws RemoteException;

    boolean isRestrictAspect(String str) throws RemoteException;

    void removeFromPreinstallList(String str) throws RemoteException;

    void removeFromPreinstallPAIList(String str) throws RemoteException;

    void setCutoutMode(String str, int i) throws RemoteException;

    void setNotchSpecialMode(String str, boolean z) throws RemoteException;

    void setRestrictAspect(String str, boolean z) throws RemoteException;

    void writePreinstallPAIPackage(String str) throws RemoteException;
}
