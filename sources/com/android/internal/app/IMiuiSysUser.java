package com.android.internal.app;

import android.content.ComponentName;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IMiuiSysUser extends IInterface {

    public static class Default implements IMiuiSysUser {
        public void notifyAMProcStart(long startUsedTime, int pid, int uid, String processName, ComponentName name, String reason) throws RemoteException {
        }

        public void notifyAMProcDied(int pid, String packageName) throws RemoteException {
        }

        public void notifyAMDestroyActivity(int pid, int identify) throws RemoteException {
        }

        public void notifyAMPauseActivity(int pid, int identify) throws RemoteException {
        }

        public void notifyAMResumeActivity(ComponentName name, int pid, int identify) throws RemoteException {
        }

        public void notifyAMRestartActivity(ComponentName name, int pid, int identify) throws RemoteException {
        }

        public void notifyAMCreateActivity(ComponentName name, int pid, int identify) throws RemoteException {
        }

        public void notifyEvent(String eventTag, Bundle data) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMiuiSysUser {
        private static final String DESCRIPTOR = "com.android.internal.app.IMiuiSysUser";
        static final int TRANSACTION_notifyAMCreateActivity = 7;
        static final int TRANSACTION_notifyAMDestroyActivity = 3;
        static final int TRANSACTION_notifyAMPauseActivity = 4;
        static final int TRANSACTION_notifyAMProcDied = 2;
        static final int TRANSACTION_notifyAMProcStart = 1;
        static final int TRANSACTION_notifyAMRestartActivity = 6;
        static final int TRANSACTION_notifyAMResumeActivity = 5;
        static final int TRANSACTION_notifyEvent = 8;

        private static class Proxy implements IMiuiSysUser {
            public static IMiuiSysUser sDefaultImpl;
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

            public void notifyAMProcStart(long startUsedTime, int pid, int uid, String processName, ComponentName name, String reason) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                String str;
                ComponentName componentName = name;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeLong(startUsedTime);
                    } catch (Throwable th2) {
                        th = th2;
                        i = pid;
                        i2 = uid;
                        str = processName;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(pid);
                        try {
                            _data.writeInt(uid);
                        } catch (Throwable th3) {
                            th = th3;
                            str = processName;
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeString(processName);
                            if (componentName != null) {
                                _data.writeInt(1);
                                componentName.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            _data.writeString(reason);
                            if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().notifyAMProcStart(startUsedTime, pid, uid, processName, name, reason);
                            _data.recycle();
                        } catch (Throwable th4) {
                            th = th4;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        i2 = uid;
                        str = processName;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    long j = startUsedTime;
                    i = pid;
                    i2 = uid;
                    str = processName;
                    _data.recycle();
                    throw th;
                }
            }

            public void notifyAMProcDied(int pid, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(pid);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().notifyAMProcDied(pid, packageName);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void notifyAMDestroyActivity(int pid, int identify) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(pid);
                    _data.writeInt(identify);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().notifyAMDestroyActivity(pid, identify);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void notifyAMPauseActivity(int pid, int identify) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(pid);
                    _data.writeInt(identify);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().notifyAMPauseActivity(pid, identify);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void notifyAMResumeActivity(ComponentName name, int pid, int identify) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (name != null) {
                        _data.writeInt(1);
                        name.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(pid);
                    _data.writeInt(identify);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().notifyAMResumeActivity(name, pid, identify);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void notifyAMRestartActivity(ComponentName name, int pid, int identify) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (name != null) {
                        _data.writeInt(1);
                        name.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(pid);
                    _data.writeInt(identify);
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().notifyAMRestartActivity(name, pid, identify);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void notifyAMCreateActivity(ComponentName name, int pid, int identify) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (name != null) {
                        _data.writeInt(1);
                        name.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(pid);
                    _data.writeInt(identify);
                    if (this.mRemote.transact(7, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().notifyAMCreateActivity(name, pid, identify);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void notifyEvent(String eventTag, Bundle data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(eventTag);
                    if (data != null) {
                        _data.writeInt(1);
                        data.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(8, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().notifyEvent(eventTag, data);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMiuiSysUser asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IMiuiSysUser)) {
                return new Proxy(obj);
            }
            return (IMiuiSysUser) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "notifyAMProcStart";
                case 2:
                    return "notifyAMProcDied";
                case 3:
                    return "notifyAMDestroyActivity";
                case 4:
                    return "notifyAMPauseActivity";
                case 5:
                    return "notifyAMResumeActivity";
                case 6:
                    return "notifyAMRestartActivity";
                case 7:
                    return "notifyAMCreateActivity";
                case 8:
                    return "notifyEvent";
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
            String descriptor = DESCRIPTOR;
            if (i != 1598968902) {
                ComponentName _arg0;
                switch (i) {
                    case 1:
                        ComponentName _arg4;
                        parcel.enforceInterface(descriptor);
                        long _arg02 = data.readLong();
                        int _arg1 = data.readInt();
                        int _arg2 = data.readInt();
                        String _arg3 = data.readString();
                        if (data.readInt() != 0) {
                            _arg4 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg4 = null;
                        }
                        notifyAMProcStart(_arg02, _arg1, _arg2, _arg3, _arg4, data.readString());
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        notifyAMProcDied(data.readInt(), data.readString());
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        notifyAMDestroyActivity(data.readInt(), data.readInt());
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        notifyAMPauseActivity(data.readInt(), data.readInt());
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        notifyAMResumeActivity(_arg0, data.readInt(), data.readInt());
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        notifyAMRestartActivity(_arg0, data.readInt(), data.readInt());
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        notifyAMCreateActivity(_arg0, data.readInt(), data.readInt());
                        return true;
                    case 8:
                        Bundle _arg12;
                        parcel.enforceInterface(descriptor);
                        String _arg03 = data.readString();
                        if (data.readInt() != 0) {
                            _arg12 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        notifyEvent(_arg03, _arg12);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IMiuiSysUser impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IMiuiSysUser getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void notifyAMCreateActivity(ComponentName componentName, int i, int i2) throws RemoteException;

    void notifyAMDestroyActivity(int i, int i2) throws RemoteException;

    void notifyAMPauseActivity(int i, int i2) throws RemoteException;

    void notifyAMProcDied(int i, String str) throws RemoteException;

    void notifyAMProcStart(long j, int i, int i2, String str, ComponentName componentName, String str2) throws RemoteException;

    void notifyAMRestartActivity(ComponentName componentName, int i, int i2) throws RemoteException;

    void notifyAMResumeActivity(ComponentName componentName, int i, int i2) throws RemoteException;

    void notifyEvent(String str, Bundle bundle) throws RemoteException;
}
