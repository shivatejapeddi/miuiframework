package android.app;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IActivityController extends IInterface {

    public static class Default implements IActivityController {
        public boolean activityStarting(Intent intent, String pkg) throws RemoteException {
            return false;
        }

        public boolean activityResuming(String pkg) throws RemoteException {
            return false;
        }

        public boolean appCrashed(String processName, int pid, String shortMsg, String longMsg, long timeMillis, String stackTrace) throws RemoteException {
            return false;
        }

        public int appEarlyNotResponding(String processName, int pid, String annotation) throws RemoteException {
            return 0;
        }

        public int appNotResponding(String processName, int pid, String processStats) throws RemoteException {
            return 0;
        }

        public int systemNotResponding(String msg) throws RemoteException {
            return 0;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IActivityController {
        private static final String DESCRIPTOR = "android.app.IActivityController";
        static final int TRANSACTION_activityResuming = 2;
        static final int TRANSACTION_activityStarting = 1;
        static final int TRANSACTION_appCrashed = 3;
        static final int TRANSACTION_appEarlyNotResponding = 4;
        static final int TRANSACTION_appNotResponding = 5;
        static final int TRANSACTION_systemNotResponding = 6;

        private static class Proxy implements IActivityController {
            public static IActivityController sDefaultImpl;
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

            public boolean activityStarting(Intent intent, String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(pkg);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().activityStarting(intent, pkg);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean activityResuming(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().activityResuming(pkg);
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

            public boolean appCrashed(String processName, int pid, String shortMsg, String longMsg, long timeMillis, String stackTrace) throws RemoteException {
                Throwable th;
                int i;
                String str;
                String str2;
                String str3;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(processName);
                    } catch (Throwable th2) {
                        th = th2;
                        i = pid;
                        str = shortMsg;
                        str2 = longMsg;
                        str3 = stackTrace;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(pid);
                    } catch (Throwable th3) {
                        th = th3;
                        str = shortMsg;
                        str2 = longMsg;
                        str3 = stackTrace;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(shortMsg);
                        try {
                            _data.writeString(longMsg);
                            _data.writeLong(timeMillis);
                        } catch (Throwable th4) {
                            th = th4;
                            str3 = stackTrace;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        str2 = longMsg;
                        str3 = stackTrace;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(stackTrace);
                        boolean z = false;
                        if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            if (_reply.readInt() != 0) {
                                z = true;
                            }
                            boolean _result = z;
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        }
                        boolean appCrashed = Stub.getDefaultImpl().appCrashed(processName, pid, shortMsg, longMsg, timeMillis, stackTrace);
                        _reply.recycle();
                        _data.recycle();
                        return appCrashed;
                    } catch (Throwable th6) {
                        th = th6;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    String str4 = processName;
                    i = pid;
                    str = shortMsg;
                    str2 = longMsg;
                    str3 = stackTrace;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public int appEarlyNotResponding(String processName, int pid, String annotation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(processName);
                    _data.writeInt(pid);
                    _data.writeString(annotation);
                    int i = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().appEarlyNotResponding(processName, pid, annotation);
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

            public int appNotResponding(String processName, int pid, String processStats) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(processName);
                    _data.writeInt(pid);
                    _data.writeString(processStats);
                    int i = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().appNotResponding(processName, pid, processStats);
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

            public int systemNotResponding(String msg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(msg);
                    int i = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().systemNotResponding(msg);
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

        public static IActivityController asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IActivityController)) {
                return new Proxy(obj);
            }
            return (IActivityController) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "activityStarting";
                case 2:
                    return "activityResuming";
                case 3:
                    return "appCrashed";
                case 4:
                    return "appEarlyNotResponding";
                case 5:
                    return "appNotResponding";
                case 6:
                    return "systemNotResponding";
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
                int _result;
                switch (i) {
                    case 1:
                        Intent _arg0;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        boolean _result2 = activityStarting(_arg0, data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        boolean _result3 = activityResuming(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        boolean _result4 = appCrashed(data.readString(), data.readInt(), data.readString(), data.readString(), data.readLong(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        _result = appEarlyNotResponding(data.readString(), data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        _result = appNotResponding(data.readString(), data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        int _result5 = systemNotResponding(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IActivityController impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IActivityController getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    boolean activityResuming(String str) throws RemoteException;

    boolean activityStarting(Intent intent, String str) throws RemoteException;

    boolean appCrashed(String str, int i, String str2, String str3, long j, String str4) throws RemoteException;

    int appEarlyNotResponding(String str, int i, String str2) throws RemoteException;

    int appNotResponding(String str, int i, String str2) throws RemoteException;

    int systemNotResponding(String str) throws RemoteException;
}
