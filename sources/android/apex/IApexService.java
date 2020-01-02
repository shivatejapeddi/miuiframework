package android.apex;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IApexService extends IInterface {

    public static class Default implements IApexService {
        public boolean submitStagedSession(int session_id, int[] child_session_ids, ApexInfoList packages) throws RemoteException {
            return false;
        }

        public boolean markStagedSessionReady(int session_id) throws RemoteException {
            return false;
        }

        public void markStagedSessionSuccessful(int session_id) throws RemoteException {
        }

        public ApexSessionInfo[] getSessions() throws RemoteException {
            return null;
        }

        public ApexSessionInfo getStagedSessionInfo(int session_id) throws RemoteException {
            return null;
        }

        public ApexInfo[] getActivePackages() throws RemoteException {
            return null;
        }

        public ApexInfo[] getAllPackages() throws RemoteException {
            return null;
        }

        public void abortActiveSession() throws RemoteException {
        }

        public void unstagePackages(List<String> list) throws RemoteException {
        }

        public ApexInfo getActivePackage(String package_name) throws RemoteException {
            return null;
        }

        public void activatePackage(String package_path) throws RemoteException {
        }

        public void deactivatePackage(String package_path) throws RemoteException {
        }

        public void preinstallPackages(List<String> list) throws RemoteException {
        }

        public void postinstallPackages(List<String> list) throws RemoteException {
        }

        public boolean stagePackage(String package_tmp_path) throws RemoteException {
            return false;
        }

        public boolean stagePackages(List<String> list) throws RemoteException {
            return false;
        }

        public void rollbackActiveSession() throws RemoteException {
        }

        public void resumeRollbackIfNeeded() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IApexService {
        private static final String DESCRIPTOR = "android.apex.IApexService";
        static final int TRANSACTION_abortActiveSession = 8;
        static final int TRANSACTION_activatePackage = 11;
        static final int TRANSACTION_deactivatePackage = 12;
        static final int TRANSACTION_getActivePackage = 10;
        static final int TRANSACTION_getActivePackages = 6;
        static final int TRANSACTION_getAllPackages = 7;
        static final int TRANSACTION_getSessions = 4;
        static final int TRANSACTION_getStagedSessionInfo = 5;
        static final int TRANSACTION_markStagedSessionReady = 2;
        static final int TRANSACTION_markStagedSessionSuccessful = 3;
        static final int TRANSACTION_postinstallPackages = 14;
        static final int TRANSACTION_preinstallPackages = 13;
        static final int TRANSACTION_resumeRollbackIfNeeded = 18;
        static final int TRANSACTION_rollbackActiveSession = 17;
        static final int TRANSACTION_stagePackage = 15;
        static final int TRANSACTION_stagePackages = 16;
        static final int TRANSACTION_submitStagedSession = 1;
        static final int TRANSACTION_unstagePackages = 9;

        private static class Proxy implements IApexService {
            public static IApexService sDefaultImpl;
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

            public boolean submitStagedSession(int session_id, int[] child_session_ids, ApexInfoList packages) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(session_id);
                    _data.writeIntArray(child_session_ids);
                    boolean _result = false;
                    if (this.mRemote.transact(1, _data, _reply, _result) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() != 0) {
                            _result = true;
                        }
                        if (_reply.readInt() != 0) {
                            packages.readFromParcel(_reply);
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().submitStagedSession(session_id, child_session_ids, packages);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean markStagedSessionReady(int session_id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(session_id);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().markStagedSessionReady(session_id);
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

            public void markStagedSessionSuccessful(int session_id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(session_id);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().markStagedSessionSuccessful(session_id);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ApexSessionInfo[] getSessions() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ApexSessionInfo[] apexSessionInfoArr = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        apexSessionInfoArr = Stub.getDefaultImpl();
                        if (apexSessionInfoArr != 0) {
                            apexSessionInfoArr = Stub.getDefaultImpl().getSessions();
                            return apexSessionInfoArr;
                        }
                    }
                    _reply.readException();
                    ApexSessionInfo[] _result = (ApexSessionInfo[]) _reply.createTypedArray(ApexSessionInfo.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ApexSessionInfo getStagedSessionInfo(int session_id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(session_id);
                    ApexSessionInfo apexSessionInfo = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        apexSessionInfo = Stub.getDefaultImpl();
                        if (apexSessionInfo != 0) {
                            apexSessionInfo = Stub.getDefaultImpl().getStagedSessionInfo(session_id);
                            return apexSessionInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        apexSessionInfo = (ApexSessionInfo) ApexSessionInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        apexSessionInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return apexSessionInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ApexInfo[] getActivePackages() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ApexInfo[] apexInfoArr = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        apexInfoArr = Stub.getDefaultImpl();
                        if (apexInfoArr != 0) {
                            apexInfoArr = Stub.getDefaultImpl().getActivePackages();
                            return apexInfoArr;
                        }
                    }
                    _reply.readException();
                    ApexInfo[] _result = (ApexInfo[]) _reply.createTypedArray(ApexInfo.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ApexInfo[] getAllPackages() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ApexInfo[] apexInfoArr = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        apexInfoArr = Stub.getDefaultImpl();
                        if (apexInfoArr != 0) {
                            apexInfoArr = Stub.getDefaultImpl().getAllPackages();
                            return apexInfoArr;
                        }
                    }
                    _reply.readException();
                    ApexInfo[] _result = (ApexInfo[]) _reply.createTypedArray(ApexInfo.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void abortActiveSession() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().abortActiveSession();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unstagePackages(List<String> active_package_paths) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(active_package_paths);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unstagePackages(active_package_paths);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ApexInfo getActivePackage(String package_name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(package_name);
                    ApexInfo apexInfo = 10;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        apexInfo = Stub.getDefaultImpl();
                        if (apexInfo != 0) {
                            apexInfo = Stub.getDefaultImpl().getActivePackage(package_name);
                            return apexInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        apexInfo = (ApexInfo) ApexInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        apexInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return apexInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void activatePackage(String package_path) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(package_path);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().activatePackage(package_path);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void deactivatePackage(String package_path) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(package_path);
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().deactivatePackage(package_path);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void preinstallPackages(List<String> package_tmp_paths) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(package_tmp_paths);
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().preinstallPackages(package_tmp_paths);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void postinstallPackages(List<String> package_tmp_paths) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(package_tmp_paths);
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().postinstallPackages(package_tmp_paths);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean stagePackage(String package_tmp_path) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(package_tmp_path);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().stagePackage(package_tmp_path);
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

            public boolean stagePackages(List<String> package_tmp_paths) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(package_tmp_paths);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(16, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().stagePackages(package_tmp_paths);
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

            public void rollbackActiveSession() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(17, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().rollbackActiveSession();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void resumeRollbackIfNeeded() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().resumeRollbackIfNeeded();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IApexService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IApexService)) {
                return new Proxy(obj);
            }
            return (IApexService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code != IBinder.INTERFACE_TRANSACTION) {
                boolean _result;
                ApexInfo[] _result2;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        int _arg0 = data.readInt();
                        int[] _arg1 = data.createIntArray();
                        ApexInfoList _arg2 = new ApexInfoList();
                        boolean _result3 = submitStagedSession(_arg0, _arg1, _arg2);
                        reply.writeNoException();
                        reply.writeInt(_result3);
                        reply.writeInt(1);
                        _arg2.writeToParcel(reply, 1);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        _result = markStagedSessionReady(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        markStagedSessionSuccessful(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        ApexSessionInfo[] _result4 = getSessions();
                        reply.writeNoException();
                        reply.writeTypedArray(_result4, 1);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        ApexSessionInfo _result5 = getStagedSessionInfo(data.readInt());
                        reply.writeNoException();
                        if (_result5 != null) {
                            reply.writeInt(1);
                            _result5.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        _result2 = getActivePackages();
                        reply.writeNoException();
                        reply.writeTypedArray(_result2, 1);
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        _result2 = getAllPackages();
                        reply.writeNoException();
                        reply.writeTypedArray(_result2, 1);
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        abortActiveSession();
                        reply.writeNoException();
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        unstagePackages(data.createStringArrayList());
                        reply.writeNoException();
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        ApexInfo _result6 = getActivePackage(data.readString());
                        reply.writeNoException();
                        if (_result6 != null) {
                            reply.writeInt(1);
                            _result6.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        activatePackage(data.readString());
                        reply.writeNoException();
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        deactivatePackage(data.readString());
                        reply.writeNoException();
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        preinstallPackages(data.createStringArrayList());
                        reply.writeNoException();
                        return true;
                    case 14:
                        data.enforceInterface(descriptor);
                        postinstallPackages(data.createStringArrayList());
                        reply.writeNoException();
                        return true;
                    case 15:
                        data.enforceInterface(descriptor);
                        _result = stagePackage(data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 16:
                        data.enforceInterface(descriptor);
                        _result = stagePackages(data.createStringArrayList());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 17:
                        data.enforceInterface(descriptor);
                        rollbackActiveSession();
                        reply.writeNoException();
                        return true;
                    case 18:
                        data.enforceInterface(descriptor);
                        resumeRollbackIfNeeded();
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IApexService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IApexService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void abortActiveSession() throws RemoteException;

    void activatePackage(String str) throws RemoteException;

    void deactivatePackage(String str) throws RemoteException;

    ApexInfo getActivePackage(String str) throws RemoteException;

    ApexInfo[] getActivePackages() throws RemoteException;

    ApexInfo[] getAllPackages() throws RemoteException;

    ApexSessionInfo[] getSessions() throws RemoteException;

    ApexSessionInfo getStagedSessionInfo(int i) throws RemoteException;

    boolean markStagedSessionReady(int i) throws RemoteException;

    void markStagedSessionSuccessful(int i) throws RemoteException;

    void postinstallPackages(List<String> list) throws RemoteException;

    void preinstallPackages(List<String> list) throws RemoteException;

    void resumeRollbackIfNeeded() throws RemoteException;

    void rollbackActiveSession() throws RemoteException;

    boolean stagePackage(String str) throws RemoteException;

    boolean stagePackages(List<String> list) throws RemoteException;

    boolean submitStagedSession(int i, int[] iArr, ApexInfoList apexInfoList) throws RemoteException;

    void unstagePackages(List<String> list) throws RemoteException;
}
