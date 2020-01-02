package android.app.slice;

import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISliceManager extends IInterface {

    public static class Default implements ISliceManager {
        public void pinSlice(String pkg, Uri uri, SliceSpec[] specs, IBinder token) throws RemoteException {
        }

        public void unpinSlice(String pkg, Uri uri, IBinder token) throws RemoteException {
        }

        public boolean hasSliceAccess(String pkg) throws RemoteException {
            return false;
        }

        public SliceSpec[] getPinnedSpecs(Uri uri, String pkg) throws RemoteException {
            return null;
        }

        public Uri[] getPinnedSlices(String pkg) throws RemoteException {
            return null;
        }

        public byte[] getBackupPayload(int user) throws RemoteException {
            return null;
        }

        public void applyRestore(byte[] payload, int user) throws RemoteException {
        }

        public void grantSlicePermission(String callingPkg, String toPkg, Uri uri) throws RemoteException {
        }

        public void revokeSlicePermission(String callingPkg, String toPkg, Uri uri) throws RemoteException {
        }

        public int checkSlicePermission(Uri uri, String callingPkg, String pkg, int pid, int uid, String[] autoGrantPermissions) throws RemoteException {
            return 0;
        }

        public void grantPermissionFromUser(Uri uri, String pkg, String callingPkg, boolean allSlices) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ISliceManager {
        private static final String DESCRIPTOR = "android.app.slice.ISliceManager";
        static final int TRANSACTION_applyRestore = 7;
        static final int TRANSACTION_checkSlicePermission = 10;
        static final int TRANSACTION_getBackupPayload = 6;
        static final int TRANSACTION_getPinnedSlices = 5;
        static final int TRANSACTION_getPinnedSpecs = 4;
        static final int TRANSACTION_grantPermissionFromUser = 11;
        static final int TRANSACTION_grantSlicePermission = 8;
        static final int TRANSACTION_hasSliceAccess = 3;
        static final int TRANSACTION_pinSlice = 1;
        static final int TRANSACTION_revokeSlicePermission = 9;
        static final int TRANSACTION_unpinSlice = 2;

        private static class Proxy implements ISliceManager {
            public static ISliceManager sDefaultImpl;
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

            public void pinSlice(String pkg, Uri uri, SliceSpec[] specs, IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeTypedArray(specs, 0);
                    _data.writeStrongBinder(token);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().pinSlice(pkg, uri, specs, token);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unpinSlice(String pkg, Uri uri, IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(token);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unpinSlice(pkg, uri, token);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean hasSliceAccess(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().hasSliceAccess(pkg);
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

            public SliceSpec[] getPinnedSpecs(Uri uri, String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(pkg);
                    SliceSpec[] sliceSpecArr = this.mRemote;
                    if (!sliceSpecArr.transact(4, _data, _reply, 0)) {
                        sliceSpecArr = Stub.getDefaultImpl();
                        if (sliceSpecArr != null) {
                            sliceSpecArr = Stub.getDefaultImpl().getPinnedSpecs(uri, pkg);
                            return sliceSpecArr;
                        }
                    }
                    _reply.readException();
                    SliceSpec[] _result = (SliceSpec[]) _reply.createTypedArray(SliceSpec.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Uri[] getPinnedSlices(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    Uri[] uriArr = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        uriArr = Stub.getDefaultImpl();
                        if (uriArr != 0) {
                            uriArr = Stub.getDefaultImpl().getPinnedSlices(pkg);
                            return uriArr;
                        }
                    }
                    _reply.readException();
                    Uri[] _result = (Uri[]) _reply.createTypedArray(Uri.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] getBackupPayload(int user) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(user);
                    byte[] bArr = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        bArr = Stub.getDefaultImpl();
                        if (bArr != 0) {
                            bArr = Stub.getDefaultImpl().getBackupPayload(user);
                            return bArr;
                        }
                    }
                    _reply.readException();
                    bArr = _reply.createByteArray();
                    byte[] _result = bArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void applyRestore(byte[] payload, int user) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(payload);
                    _data.writeInt(user);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().applyRestore(payload, user);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void grantSlicePermission(String callingPkg, String toPkg, Uri uri) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    _data.writeString(toPkg);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().grantSlicePermission(callingPkg, toPkg, uri);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void revokeSlicePermission(String callingPkg, String toPkg, Uri uri) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    _data.writeString(toPkg);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().revokeSlicePermission(callingPkg, toPkg, uri);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int checkSlicePermission(Uri uri, String callingPkg, String pkg, int pid, int uid, String[] autoGrantPermissions) throws RemoteException {
                Throwable th;
                String str;
                int i;
                int i2;
                String[] strArr;
                Uri uri2 = uri;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (uri2 != null) {
                        _data.writeInt(1);
                        uri2.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeString(callingPkg);
                    } catch (Throwable th2) {
                        th = th2;
                        str = pkg;
                        i = pid;
                        i2 = uid;
                        strArr = autoGrantPermissions;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(pkg);
                    } catch (Throwable th3) {
                        th = th3;
                        i = pid;
                        i2 = uid;
                        strArr = autoGrantPermissions;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(pid);
                        try {
                            _data.writeInt(uid);
                        } catch (Throwable th4) {
                            th = th4;
                            strArr = autoGrantPermissions;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeStringArray(autoGrantPermissions);
                            if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                int _result = _reply.readInt();
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            int checkSlicePermission = Stub.getDefaultImpl().checkSlicePermission(uri, callingPkg, pkg, pid, uid, autoGrantPermissions);
                            _reply.recycle();
                            _data.recycle();
                            return checkSlicePermission;
                        } catch (Throwable th5) {
                            th = th5;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        i2 = uid;
                        strArr = autoGrantPermissions;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    String str2 = callingPkg;
                    str = pkg;
                    i = pid;
                    i2 = uid;
                    strArr = autoGrantPermissions;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void grantPermissionFromUser(Uri uri, String pkg, String callingPkg, boolean allSlices) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(pkg);
                    _data.writeString(callingPkg);
                    if (!allSlices) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().grantPermissionFromUser(uri, pkg, callingPkg, allSlices);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISliceManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ISliceManager)) {
                return new Proxy(obj);
            }
            return (ISliceManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "pinSlice";
                case 2:
                    return "unpinSlice";
                case 3:
                    return "hasSliceAccess";
                case 4:
                    return "getPinnedSpecs";
                case 5:
                    return "getPinnedSlices";
                case 6:
                    return "getBackupPayload";
                case 7:
                    return "applyRestore";
                case 8:
                    return "grantSlicePermission";
                case 9:
                    return "revokeSlicePermission";
                case 10:
                    return "checkSlicePermission";
                case 11:
                    return "grantPermissionFromUser";
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
                String _arg0;
                Uri _arg1;
                Uri _arg02;
                String _arg12;
                Uri _arg2;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg1 = null;
                        }
                        pinSlice(_arg0, _arg1, (SliceSpec[]) parcel.createTypedArray(SliceSpec.CREATOR), data.readStrongBinder());
                        reply.writeNoException();
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg1 = null;
                        }
                        unpinSlice(_arg0, _arg1, data.readStrongBinder());
                        reply.writeNoException();
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        boolean _result = hasSliceAccess(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        SliceSpec[] _result2 = getPinnedSpecs(_arg02, data.readString());
                        reply.writeNoException();
                        parcel2.writeTypedArray(_result2, 1);
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        Uri[] _result3 = getPinnedSlices(data.readString());
                        reply.writeNoException();
                        parcel2.writeTypedArray(_result3, 1);
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        byte[] _result4 = getBackupPayload(data.readInt());
                        reply.writeNoException();
                        parcel2.writeByteArray(_result4);
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        applyRestore(data.createByteArray(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg12 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        grantSlicePermission(_arg0, _arg12, _arg2);
                        reply.writeNoException();
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg12 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        revokeSlicePermission(_arg0, _arg12, _arg2);
                        reply.writeNoException();
                        return true;
                    case 10:
                        Uri _arg03;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        int _result5 = checkSlicePermission(_arg03, data.readString(), data.readString(), data.readInt(), data.readInt(), data.createStringArray());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        grantPermissionFromUser(_arg02, data.readString(), data.readString(), data.readInt() != 0);
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(ISliceManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ISliceManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void applyRestore(byte[] bArr, int i) throws RemoteException;

    int checkSlicePermission(Uri uri, String str, String str2, int i, int i2, String[] strArr) throws RemoteException;

    byte[] getBackupPayload(int i) throws RemoteException;

    Uri[] getPinnedSlices(String str) throws RemoteException;

    SliceSpec[] getPinnedSpecs(Uri uri, String str) throws RemoteException;

    void grantPermissionFromUser(Uri uri, String str, String str2, boolean z) throws RemoteException;

    void grantSlicePermission(String str, String str2, Uri uri) throws RemoteException;

    boolean hasSliceAccess(String str) throws RemoteException;

    void pinSlice(String str, Uri uri, SliceSpec[] sliceSpecArr, IBinder iBinder) throws RemoteException;

    void revokeSlicePermission(String str, String str2, Uri uri) throws RemoteException;

    void unpinSlice(String str, Uri uri, IBinder iBinder) throws RemoteException;
}
