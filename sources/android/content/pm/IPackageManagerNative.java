package android.content.pm;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IPackageManagerNative extends IInterface {
    public static final int LOCATION_PRODUCT = 4;
    public static final int LOCATION_SYSTEM = 1;
    public static final int LOCATION_VENDOR = 2;

    public static class Default implements IPackageManagerNative {
        public String[] getNamesForUids(int[] uids) throws RemoteException {
            return null;
        }

        public String getInstallerForPackage(String packageName) throws RemoteException {
            return null;
        }

        public long getVersionCodeForPackage(String packageName) throws RemoteException {
            return 0;
        }

        public boolean[] isAudioPlaybackCaptureAllowed(String[] packageNames) throws RemoteException {
            return null;
        }

        public int getLocationFlags(String packageName) throws RemoteException {
            return 0;
        }

        public int getTargetSdkVersionForPackage(String packageName) throws RemoteException {
            return 0;
        }

        public String getModuleMetadataPackageName() throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IPackageManagerNative {
        private static final String DESCRIPTOR = "android.content.pm.IPackageManagerNative";
        static final int TRANSACTION_getInstallerForPackage = 2;
        static final int TRANSACTION_getLocationFlags = 5;
        static final int TRANSACTION_getModuleMetadataPackageName = 7;
        static final int TRANSACTION_getNamesForUids = 1;
        static final int TRANSACTION_getTargetSdkVersionForPackage = 6;
        static final int TRANSACTION_getVersionCodeForPackage = 3;
        static final int TRANSACTION_isAudioPlaybackCaptureAllowed = 4;

        private static class Proxy implements IPackageManagerNative {
            public static IPackageManagerNative sDefaultImpl;
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

            public String[] getNamesForUids(int[] uids) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(uids);
                    String[] strArr = 1;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getNamesForUids(uids);
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

            public String getInstallerForPackage(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    String str = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getInstallerForPackage(packageName);
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

            public long getVersionCodeForPackage(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    long j = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getVersionCodeForPackage(packageName);
                            return j;
                        }
                    }
                    _reply.readException();
                    j = _reply.readLong();
                    long _result = j;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean[] isAudioPlaybackCaptureAllowed(String[] packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(packageNames);
                    boolean[] zArr = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        zArr = Stub.getDefaultImpl();
                        if (zArr != 0) {
                            zArr = Stub.getDefaultImpl().isAudioPlaybackCaptureAllowed(packageNames);
                            return zArr;
                        }
                    }
                    _reply.readException();
                    zArr = _reply.createBooleanArray();
                    boolean[] _result = zArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getLocationFlags(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    int i = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getLocationFlags(packageName);
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

            public int getTargetSdkVersionForPackage(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    int i = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getTargetSdkVersionForPackage(packageName);
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

            public String getModuleMetadataPackageName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getModuleMetadataPackageName();
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPackageManagerNative asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IPackageManagerNative)) {
                return new Proxy(obj);
            }
            return (IPackageManagerNative) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getNamesForUids";
                case 2:
                    return "getInstallerForPackage";
                case 3:
                    return "getVersionCodeForPackage";
                case 4:
                    return "isAudioPlaybackCaptureAllowed";
                case 5:
                    return "getLocationFlags";
                case 6:
                    return "getTargetSdkVersionForPackage";
                case 7:
                    return "getModuleMetadataPackageName";
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
                int _result;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        String[] _result2 = getNamesForUids(data.createIntArray());
                        reply.writeNoException();
                        reply.writeStringArray(_result2);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        String _result3 = getInstallerForPackage(data.readString());
                        reply.writeNoException();
                        reply.writeString(_result3);
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        long _result4 = getVersionCodeForPackage(data.readString());
                        reply.writeNoException();
                        reply.writeLong(_result4);
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        boolean[] _result5 = isAudioPlaybackCaptureAllowed(data.createStringArray());
                        reply.writeNoException();
                        reply.writeBooleanArray(_result5);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        _result = getLocationFlags(data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        _result = getTargetSdkVersionForPackage(data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        String _result6 = getModuleMetadataPackageName();
                        reply.writeNoException();
                        reply.writeString(_result6);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IPackageManagerNative impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IPackageManagerNative getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    String getInstallerForPackage(String str) throws RemoteException;

    int getLocationFlags(String str) throws RemoteException;

    String getModuleMetadataPackageName() throws RemoteException;

    String[] getNamesForUids(int[] iArr) throws RemoteException;

    int getTargetSdkVersionForPackage(String str) throws RemoteException;

    long getVersionCodeForPackage(String str) throws RemoteException;

    boolean[] isAudioPlaybackCaptureAllowed(String[] strArr) throws RemoteException;
}
