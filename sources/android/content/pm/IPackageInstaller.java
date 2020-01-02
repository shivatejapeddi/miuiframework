package android.content.pm;

import android.annotation.UnsupportedAppUsage;
import android.content.IntentSender;
import android.content.pm.PackageInstaller.SessionInfo;
import android.content.pm.PackageInstaller.SessionParams;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IPackageInstaller extends IInterface {

    public static class Default implements IPackageInstaller {
        public int createSession(SessionParams params, String installerPackageName, int userId) throws RemoteException {
            return 0;
        }

        public void updateSessionAppIcon(int sessionId, Bitmap appIcon) throws RemoteException {
        }

        public void updateSessionAppLabel(int sessionId, String appLabel) throws RemoteException {
        }

        public void abandonSession(int sessionId) throws RemoteException {
        }

        public IPackageInstallerSession openSession(int sessionId) throws RemoteException {
            return null;
        }

        public SessionInfo getSessionInfo(int sessionId) throws RemoteException {
            return null;
        }

        public ParceledListSlice getAllSessions(int userId) throws RemoteException {
            return null;
        }

        public ParceledListSlice getMySessions(String installerPackageName, int userId) throws RemoteException {
            return null;
        }

        public ParceledListSlice getStagedSessions() throws RemoteException {
            return null;
        }

        public void registerCallback(IPackageInstallerCallback callback, int userId) throws RemoteException {
        }

        public void unregisterCallback(IPackageInstallerCallback callback) throws RemoteException {
        }

        public void uninstall(VersionedPackage versionedPackage, String callerPackageName, int flags, IntentSender statusReceiver, int userId) throws RemoteException {
        }

        public void installExistingPackage(String packageName, int installFlags, int installReason, IntentSender statusReceiver, int userId, List<String> list) throws RemoteException {
        }

        public void setPermissionsResult(int sessionId, boolean accepted) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IPackageInstaller {
        private static final String DESCRIPTOR = "android.content.pm.IPackageInstaller";
        static final int TRANSACTION_abandonSession = 4;
        static final int TRANSACTION_createSession = 1;
        static final int TRANSACTION_getAllSessions = 7;
        static final int TRANSACTION_getMySessions = 8;
        static final int TRANSACTION_getSessionInfo = 6;
        static final int TRANSACTION_getStagedSessions = 9;
        static final int TRANSACTION_installExistingPackage = 13;
        static final int TRANSACTION_openSession = 5;
        static final int TRANSACTION_registerCallback = 10;
        static final int TRANSACTION_setPermissionsResult = 14;
        static final int TRANSACTION_uninstall = 12;
        static final int TRANSACTION_unregisterCallback = 11;
        static final int TRANSACTION_updateSessionAppIcon = 2;
        static final int TRANSACTION_updateSessionAppLabel = 3;

        private static class Proxy implements IPackageInstaller {
            public static IPackageInstaller sDefaultImpl;
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

            public int createSession(SessionParams params, String installerPackageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(installerPackageName);
                    _data.writeInt(userId);
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().createSession(params, installerPackageName, userId);
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

            public void updateSessionAppIcon(int sessionId, Bitmap appIcon) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    if (appIcon != null) {
                        _data.writeInt(1);
                        appIcon.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateSessionAppIcon(sessionId, appIcon);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateSessionAppLabel(int sessionId, String appLabel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    _data.writeString(appLabel);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateSessionAppLabel(sessionId, appLabel);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void abandonSession(int sessionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().abandonSession(sessionId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IPackageInstallerSession openSession(int sessionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    IPackageInstallerSession iPackageInstallerSession = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        iPackageInstallerSession = Stub.getDefaultImpl();
                        if (iPackageInstallerSession != 0) {
                            iPackageInstallerSession = Stub.getDefaultImpl().openSession(sessionId);
                            return iPackageInstallerSession;
                        }
                    }
                    _reply.readException();
                    iPackageInstallerSession = android.content.pm.IPackageInstallerSession.Stub.asInterface(_reply.readStrongBinder());
                    IPackageInstallerSession _result = iPackageInstallerSession;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public SessionInfo getSessionInfo(int sessionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    SessionInfo sessionInfo = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        sessionInfo = Stub.getDefaultImpl();
                        if (sessionInfo != 0) {
                            sessionInfo = Stub.getDefaultImpl().getSessionInfo(sessionId);
                            return sessionInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        sessionInfo = (SessionInfo) SessionInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        sessionInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return sessionInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getAllSessions(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    ParceledListSlice parceledListSlice = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().getAllSessions(userId);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getMySessions(String installerPackageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(installerPackageName);
                    _data.writeInt(userId);
                    ParceledListSlice parceledListSlice = 8;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().getMySessions(installerPackageName, userId);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getStagedSessions() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ParceledListSlice parceledListSlice = 9;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().getStagedSessions();
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerCallback(IPackageInstallerCallback callback, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerCallback(callback, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterCallback(IPackageInstallerCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterCallback(callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void uninstall(VersionedPackage versionedPackage, String callerPackageName, int flags, IntentSender statusReceiver, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (versionedPackage != null) {
                        _data.writeInt(1);
                        versionedPackage.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackageName);
                    _data.writeInt(flags);
                    if (statusReceiver != null) {
                        _data.writeInt(1);
                        statusReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().uninstall(versionedPackage, callerPackageName, flags, statusReceiver, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void installExistingPackage(String packageName, int installFlags, int installReason, IntentSender statusReceiver, int userId, List<String> whiteListedPermissions) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                List<String> list;
                int i3;
                IntentSender intentSender = statusReceiver;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(packageName);
                        try {
                            _data.writeInt(installFlags);
                        } catch (Throwable th2) {
                            th = th2;
                            i = installReason;
                            i2 = userId;
                            list = whiteListedPermissions;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeInt(installReason);
                            if (intentSender != null) {
                                _data.writeInt(1);
                                intentSender.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            try {
                                _data.writeInt(userId);
                            } catch (Throwable th3) {
                                th = th3;
                                list = whiteListedPermissions;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                            try {
                                _data.writeStringList(whiteListedPermissions);
                                if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                    _reply.readException();
                                    _reply.recycle();
                                    _data.recycle();
                                    return;
                                }
                                Stub.getDefaultImpl().installExistingPackage(packageName, installFlags, installReason, statusReceiver, userId, whiteListedPermissions);
                                _reply.recycle();
                                _data.recycle();
                            } catch (Throwable th4) {
                                th = th4;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th5) {
                            th = th5;
                            i2 = userId;
                            list = whiteListedPermissions;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        i3 = installFlags;
                        i = installReason;
                        i2 = userId;
                        list = whiteListedPermissions;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    String str = packageName;
                    i3 = installFlags;
                    i = installReason;
                    i2 = userId;
                    list = whiteListedPermissions;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void setPermissionsResult(int sessionId, boolean accepted) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    _data.writeInt(accepted ? 1 : 0);
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPermissionsResult(sessionId, accepted);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPackageInstaller asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IPackageInstaller)) {
                return new Proxy(obj);
            }
            return (IPackageInstaller) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "createSession";
                case 2:
                    return "updateSessionAppIcon";
                case 3:
                    return "updateSessionAppLabel";
                case 4:
                    return "abandonSession";
                case 5:
                    return "openSession";
                case 6:
                    return "getSessionInfo";
                case 7:
                    return "getAllSessions";
                case 8:
                    return "getMySessions";
                case 9:
                    return "getStagedSessions";
                case 10:
                    return "registerCallback";
                case 11:
                    return "unregisterCallback";
                case 12:
                    return "uninstall";
                case 13:
                    return "installExistingPackage";
                case 14:
                    return "setPermissionsResult";
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
                boolean _arg1 = false;
                String _arg12;
                int _arg2;
                switch (i) {
                    case 1:
                        SessionParams _arg0;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (SessionParams) SessionParams.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        int _result = createSession(_arg0, data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 2:
                        Bitmap _arg13;
                        parcel.enforceInterface(descriptor);
                        int _arg02 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg13 = (Bitmap) Bitmap.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        updateSessionAppIcon(_arg02, _arg13);
                        reply.writeNoException();
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        updateSessionAppLabel(data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        abandonSession(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        IPackageInstallerSession _result2 = openSession(data.readInt());
                        reply.writeNoException();
                        parcel2.writeStrongBinder(_result2 != null ? _result2.asBinder() : null);
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        SessionInfo _result3 = getSessionInfo(data.readInt());
                        reply.writeNoException();
                        if (_result3 != null) {
                            parcel2.writeInt(1);
                            _result3.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        ParceledListSlice _result4 = getAllSessions(data.readInt());
                        reply.writeNoException();
                        if (_result4 != null) {
                            parcel2.writeInt(1);
                            _result4.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        ParceledListSlice _result5 = getMySessions(data.readString(), data.readInt());
                        reply.writeNoException();
                        if (_result5 != null) {
                            parcel2.writeInt(1);
                            _result5.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        ParceledListSlice _result6 = getStagedSessions();
                        reply.writeNoException();
                        if (_result6 != null) {
                            parcel2.writeInt(1);
                            _result6.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        registerCallback(android.content.pm.IPackageInstallerCallback.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        unregisterCallback(android.content.pm.IPackageInstallerCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 12:
                        VersionedPackage _arg03;
                        IntentSender _arg3;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (VersionedPackage) VersionedPackage.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        _arg12 = data.readString();
                        _arg2 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg3 = (IntentSender) IntentSender.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        uninstall(_arg03, _arg12, _arg2, _arg3, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 13:
                        IntentSender _arg32;
                        parcel.enforceInterface(descriptor);
                        _arg12 = data.readString();
                        _arg2 = data.readInt();
                        int _arg22 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg32 = (IntentSender) IntentSender.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg32 = null;
                        }
                        installExistingPackage(_arg12, _arg2, _arg22, _arg32, data.readInt(), data.createStringArrayList());
                        reply.writeNoException();
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        int _arg04 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setPermissionsResult(_arg04, _arg1);
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IPackageInstaller impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IPackageInstaller getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void abandonSession(int i) throws RemoteException;

    int createSession(SessionParams sessionParams, String str, int i) throws RemoteException;

    ParceledListSlice getAllSessions(int i) throws RemoteException;

    ParceledListSlice getMySessions(String str, int i) throws RemoteException;

    SessionInfo getSessionInfo(int i) throws RemoteException;

    ParceledListSlice getStagedSessions() throws RemoteException;

    void installExistingPackage(String str, int i, int i2, IntentSender intentSender, int i3, List<String> list) throws RemoteException;

    IPackageInstallerSession openSession(int i) throws RemoteException;

    void registerCallback(IPackageInstallerCallback iPackageInstallerCallback, int i) throws RemoteException;

    void setPermissionsResult(int i, boolean z) throws RemoteException;

    @UnsupportedAppUsage
    void uninstall(VersionedPackage versionedPackage, String str, int i, IntentSender intentSender, int i2) throws RemoteException;

    void unregisterCallback(IPackageInstallerCallback iPackageInstallerCallback) throws RemoteException;

    void updateSessionAppIcon(int i, Bitmap bitmap) throws RemoteException;

    void updateSessionAppLabel(int i, String str) throws RemoteException;
}
