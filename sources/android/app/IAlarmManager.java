package android.app;

import android.annotation.UnsupportedAppUsage;
import android.app.AlarmManager.AlarmClockInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.WorkSource;

public interface IAlarmManager extends IInterface {

    public static class Default implements IAlarmManager {
        public void set(String callingPackage, int type, long triggerAtTime, long windowLength, long interval, int flags, PendingIntent operation, IAlarmListener listener, String listenerTag, WorkSource workSource, AlarmClockInfo alarmClock) throws RemoteException {
        }

        public boolean setTime(long millis) throws RemoteException {
            return false;
        }

        public void setTimeZone(String zone) throws RemoteException {
        }

        public void remove(PendingIntent operation, IAlarmListener listener) throws RemoteException {
        }

        public long getNextWakeFromIdleTime() throws RemoteException {
            return 0;
        }

        public AlarmClockInfo getNextAlarmClock(int userId) throws RemoteException {
            return null;
        }

        public long currentNetworkTimeMillis() throws RemoteException {
            return 0;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IAlarmManager {
        private static final String DESCRIPTOR = "android.app.IAlarmManager";
        static final int TRANSACTION_currentNetworkTimeMillis = 7;
        static final int TRANSACTION_getNextAlarmClock = 6;
        static final int TRANSACTION_getNextWakeFromIdleTime = 5;
        static final int TRANSACTION_remove = 4;
        static final int TRANSACTION_set = 1;
        static final int TRANSACTION_setTime = 2;
        static final int TRANSACTION_setTimeZone = 3;

        private static class Proxy implements IAlarmManager {
            public static IAlarmManager sDefaultImpl;
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

            public void set(String callingPackage, int type, long triggerAtTime, long windowLength, long interval, int flags, PendingIntent operation, IAlarmListener listener, String listenerTag, WorkSource workSource, AlarmClockInfo alarmClock) throws RemoteException {
                Throwable th;
                PendingIntent pendingIntent = operation;
                WorkSource workSource2 = workSource;
                AlarmClockInfo alarmClockInfo = alarmClock;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                Parcel _reply2;
                Parcel _data2;
                try {
                    int i;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(type);
                    _data.writeLong(triggerAtTime);
                    _data.writeLong(windowLength);
                    _data.writeLong(interval);
                    _data.writeInt(flags);
                    if (pendingIntent != null) {
                        try {
                            _data.writeInt(1);
                            pendingIntent.writeToParcel(_data, 0);
                        } catch (Throwable th2) {
                            th = th2;
                            _reply2 = _reply;
                            _data2 = _data;
                        }
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    _data.writeString(listenerTag);
                    if (workSource2 != null) {
                        _data.writeInt(1);
                        i = 0;
                        workSource2.writeToParcel(_data, 0);
                    } else {
                        i = 0;
                        _data.writeInt(0);
                    }
                    if (alarmClockInfo != null) {
                        _data.writeInt(1);
                        alarmClockInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(i);
                    }
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply2 = _reply;
                        _data2 = _data;
                        _reply2.readException();
                        _reply2.recycle();
                        _data2.recycle();
                        return;
                    }
                    _reply2 = _reply;
                    _data2 = _data;
                    try {
                        Stub.getDefaultImpl().set(callingPackage, type, triggerAtTime, windowLength, interval, flags, operation, listener, listenerTag, workSource, alarmClock);
                        _reply2.recycle();
                        _data2.recycle();
                    } catch (Throwable th3) {
                        th = th3;
                        _reply2.recycle();
                        _data2.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    _reply2 = _reply;
                    _data2 = _data;
                    _reply2.recycle();
                    _data2.recycle();
                    throw th;
                }
            }

            public boolean setTime(long millis) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(millis);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().setTime(millis);
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

            public void setTimeZone(String zone) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(zone);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setTimeZone(zone);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void remove(PendingIntent operation, IAlarmListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (operation != null) {
                        _data.writeInt(1);
                        operation.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().remove(operation, listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getNextWakeFromIdleTime() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    long j = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getNextWakeFromIdleTime();
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

            public AlarmClockInfo getNextAlarmClock(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    AlarmClockInfo alarmClockInfo = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        alarmClockInfo = Stub.getDefaultImpl();
                        if (alarmClockInfo != 0) {
                            alarmClockInfo = Stub.getDefaultImpl().getNextAlarmClock(userId);
                            return alarmClockInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        alarmClockInfo = (AlarmClockInfo) AlarmClockInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        alarmClockInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return alarmClockInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long currentNetworkTimeMillis() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    long j = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().currentNetworkTimeMillis();
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAlarmManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IAlarmManager)) {
                return new Proxy(obj);
            }
            return (IAlarmManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "set";
                case 2:
                    return "setTime";
                case 3:
                    return "setTimeZone";
                case 4:
                    return "remove";
                case 5:
                    return "getNextWakeFromIdleTime";
                case 6:
                    return "getNextAlarmClock";
                case 7:
                    return "currentNetworkTimeMillis";
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
            boolean z;
            if (i != 1598968902) {
                long _result;
                switch (i) {
                    case 1:
                        PendingIntent _arg6;
                        WorkSource _arg9;
                        AlarmClockInfo _arg10;
                        parcel.enforceInterface(descriptor);
                        String _arg0 = data.readString();
                        int _arg1 = data.readInt();
                        long _arg2 = data.readLong();
                        long _arg3 = data.readLong();
                        long _arg4 = data.readLong();
                        int _arg5 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg6 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg6 = null;
                        }
                        IAlarmListener _arg7 = android.app.IAlarmListener.Stub.asInterface(data.readStrongBinder());
                        String _arg8 = data.readString();
                        if (data.readInt() != 0) {
                            _arg9 = (WorkSource) WorkSource.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg9 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg10 = (AlarmClockInfo) AlarmClockInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg10 = null;
                        }
                        z = true;
                        Parcel descriptor2 = parcel2;
                        set(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8, _arg9, _arg10);
                        reply.writeNoException();
                        return z;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        boolean _result2 = setTime(data.readLong());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        setTimeZone(data.readString());
                        reply.writeNoException();
                        return true;
                    case 4:
                        PendingIntent _arg02;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        remove(_arg02, android.app.IAlarmListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        _result = getNextWakeFromIdleTime();
                        reply.writeNoException();
                        parcel2.writeLong(_result);
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        AlarmClockInfo _result3 = getNextAlarmClock(data.readInt());
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
                        _result = currentNetworkTimeMillis();
                        reply.writeNoException();
                        parcel2.writeLong(_result);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            z = true;
            parcel2.writeString(descriptor);
            return z;
        }

        public static boolean setDefaultImpl(IAlarmManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IAlarmManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    long currentNetworkTimeMillis() throws RemoteException;

    @UnsupportedAppUsage
    AlarmClockInfo getNextAlarmClock(int i) throws RemoteException;

    long getNextWakeFromIdleTime() throws RemoteException;

    void remove(PendingIntent pendingIntent, IAlarmListener iAlarmListener) throws RemoteException;

    @UnsupportedAppUsage
    void set(String str, int i, long j, long j2, long j3, int i2, PendingIntent pendingIntent, IAlarmListener iAlarmListener, String str2, WorkSource workSource, AlarmClockInfo alarmClockInfo) throws RemoteException;

    @UnsupportedAppUsage
    boolean setTime(long j) throws RemoteException;

    void setTimeZone(String str) throws RemoteException;
}
