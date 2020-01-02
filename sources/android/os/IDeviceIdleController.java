package android.os;

import android.annotation.UnsupportedAppUsage;

public interface IDeviceIdleController extends IInterface {

    public static class Default implements IDeviceIdleController {
        public void addPowerSaveWhitelistApp(String name) throws RemoteException {
        }

        public void removePowerSaveWhitelistApp(String name) throws RemoteException {
        }

        public void addPowerSaveWhitelistApps(String[] names) throws RemoteException {
        }

        public void removePowerSaveWhitelistApps(String[] names) throws RemoteException {
        }

        public void removeSystemPowerWhitelistApp(String name) throws RemoteException {
        }

        public void restoreSystemPowerWhitelistApp(String name) throws RemoteException {
        }

        public String[] getRemovedSystemPowerWhitelistApps() throws RemoteException {
            return null;
        }

        public String[] getSystemPowerWhitelistExceptIdle() throws RemoteException {
            return null;
        }

        public String[] getSystemPowerWhitelist() throws RemoteException {
            return null;
        }

        public String[] getUserPowerWhitelist() throws RemoteException {
            return null;
        }

        public String[] getFullPowerWhitelistExceptIdle() throws RemoteException {
            return null;
        }

        public String[] getFullPowerWhitelist() throws RemoteException {
            return null;
        }

        public int[] getAppIdWhitelistExceptIdle() throws RemoteException {
            return null;
        }

        public int[] getAppIdWhitelist() throws RemoteException {
            return null;
        }

        public int[] getAppIdUserWhitelist() throws RemoteException {
            return null;
        }

        public int[] getAppIdTempWhitelist() throws RemoteException {
            return null;
        }

        public boolean isPowerSaveWhitelistExceptIdleApp(String name) throws RemoteException {
            return false;
        }

        public boolean isPowerSaveWhitelistApp(String name) throws RemoteException {
            return false;
        }

        public void addPowerSaveTempWhitelistApp(String name, long duration, int userId, String reason) throws RemoteException {
        }

        public long addPowerSaveTempWhitelistAppForMms(String name, int userId, String reason) throws RemoteException {
            return 0;
        }

        public long addPowerSaveTempWhitelistAppForSms(String name, int userId, String reason) throws RemoteException {
            return 0;
        }

        public void exitIdle(String reason) throws RemoteException {
        }

        public boolean registerMaintenanceActivityListener(IMaintenanceActivityListener listener) throws RemoteException {
            return false;
        }

        public void unregisterMaintenanceActivityListener(IMaintenanceActivityListener listener) throws RemoteException {
        }

        public int setPreIdleTimeoutMode(int Mode) throws RemoteException {
            return 0;
        }

        public void resetPreIdleTimeoutMode() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IDeviceIdleController {
        private static final String DESCRIPTOR = "android.os.IDeviceIdleController";
        static final int TRANSACTION_addPowerSaveTempWhitelistApp = 19;
        static final int TRANSACTION_addPowerSaveTempWhitelistAppForMms = 20;
        static final int TRANSACTION_addPowerSaveTempWhitelistAppForSms = 21;
        static final int TRANSACTION_addPowerSaveWhitelistApp = 1;
        static final int TRANSACTION_addPowerSaveWhitelistApps = 3;
        static final int TRANSACTION_exitIdle = 22;
        static final int TRANSACTION_getAppIdTempWhitelist = 16;
        static final int TRANSACTION_getAppIdUserWhitelist = 15;
        static final int TRANSACTION_getAppIdWhitelist = 14;
        static final int TRANSACTION_getAppIdWhitelistExceptIdle = 13;
        static final int TRANSACTION_getFullPowerWhitelist = 12;
        static final int TRANSACTION_getFullPowerWhitelistExceptIdle = 11;
        static final int TRANSACTION_getRemovedSystemPowerWhitelistApps = 7;
        static final int TRANSACTION_getSystemPowerWhitelist = 9;
        static final int TRANSACTION_getSystemPowerWhitelistExceptIdle = 8;
        static final int TRANSACTION_getUserPowerWhitelist = 10;
        static final int TRANSACTION_isPowerSaveWhitelistApp = 18;
        static final int TRANSACTION_isPowerSaveWhitelistExceptIdleApp = 17;
        static final int TRANSACTION_registerMaintenanceActivityListener = 23;
        static final int TRANSACTION_removePowerSaveWhitelistApp = 2;
        static final int TRANSACTION_removePowerSaveWhitelistApps = 4;
        static final int TRANSACTION_removeSystemPowerWhitelistApp = 5;
        static final int TRANSACTION_resetPreIdleTimeoutMode = 26;
        static final int TRANSACTION_restoreSystemPowerWhitelistApp = 6;
        static final int TRANSACTION_setPreIdleTimeoutMode = 25;
        static final int TRANSACTION_unregisterMaintenanceActivityListener = 24;

        private static class Proxy implements IDeviceIdleController {
            public static IDeviceIdleController sDefaultImpl;
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

            public void addPowerSaveWhitelistApp(String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addPowerSaveWhitelistApp(name);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removePowerSaveWhitelistApp(String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removePowerSaveWhitelistApp(name);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addPowerSaveWhitelistApps(String[] names) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(names);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addPowerSaveWhitelistApps(names);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removePowerSaveWhitelistApps(String[] names) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(names);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removePowerSaveWhitelistApps(names);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeSystemPowerWhitelistApp(String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeSystemPowerWhitelistApp(name);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void restoreSystemPowerWhitelistApp(String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().restoreSystemPowerWhitelistApp(name);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getRemovedSystemPowerWhitelistApps() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String[] strArr = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getRemovedSystemPowerWhitelistApps();
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

            public String[] getSystemPowerWhitelistExceptIdle() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String[] strArr = 8;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getSystemPowerWhitelistExceptIdle();
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

            public String[] getSystemPowerWhitelist() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String[] strArr = 9;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getSystemPowerWhitelist();
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

            public String[] getUserPowerWhitelist() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String[] strArr = 10;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getUserPowerWhitelist();
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

            public String[] getFullPowerWhitelistExceptIdle() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String[] strArr = 11;
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getFullPowerWhitelistExceptIdle();
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

            public String[] getFullPowerWhitelist() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String[] strArr = 12;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getFullPowerWhitelist();
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

            public int[] getAppIdWhitelistExceptIdle() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int[] iArr = 13;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        iArr = Stub.getDefaultImpl();
                        if (iArr != 0) {
                            iArr = Stub.getDefaultImpl().getAppIdWhitelistExceptIdle();
                            return iArr;
                        }
                    }
                    _reply.readException();
                    iArr = _reply.createIntArray();
                    int[] _result = iArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] getAppIdWhitelist() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int[] iArr = 14;
                    if (!this.mRemote.transact(14, _data, _reply, 0)) {
                        iArr = Stub.getDefaultImpl();
                        if (iArr != 0) {
                            iArr = Stub.getDefaultImpl().getAppIdWhitelist();
                            return iArr;
                        }
                    }
                    _reply.readException();
                    iArr = _reply.createIntArray();
                    int[] _result = iArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] getAppIdUserWhitelist() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int[] iArr = 15;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        iArr = Stub.getDefaultImpl();
                        if (iArr != 0) {
                            iArr = Stub.getDefaultImpl().getAppIdUserWhitelist();
                            return iArr;
                        }
                    }
                    _reply.readException();
                    iArr = _reply.createIntArray();
                    int[] _result = iArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] getAppIdTempWhitelist() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int[] iArr = 16;
                    if (!this.mRemote.transact(16, _data, _reply, 0)) {
                        iArr = Stub.getDefaultImpl();
                        if (iArr != 0) {
                            iArr = Stub.getDefaultImpl().getAppIdTempWhitelist();
                            return iArr;
                        }
                    }
                    _reply.readException();
                    iArr = _reply.createIntArray();
                    int[] _result = iArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isPowerSaveWhitelistExceptIdleApp(String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(17, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isPowerSaveWhitelistExceptIdleApp(name);
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

            public boolean isPowerSaveWhitelistApp(String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(18, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isPowerSaveWhitelistApp(name);
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

            public void addPowerSaveTempWhitelistApp(String name, long duration, int userId, String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeLong(duration);
                    _data.writeInt(userId);
                    _data.writeString(reason);
                    if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addPowerSaveTempWhitelistApp(name, duration, userId, reason);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long addPowerSaveTempWhitelistAppForMms(String name, int userId, String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(userId);
                    _data.writeString(reason);
                    long j = 20;
                    if (!this.mRemote.transact(20, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().addPowerSaveTempWhitelistAppForMms(name, userId, reason);
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

            public long addPowerSaveTempWhitelistAppForSms(String name, int userId, String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(userId);
                    _data.writeString(reason);
                    long j = 21;
                    if (!this.mRemote.transact(21, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().addPowerSaveTempWhitelistAppForSms(name, userId, reason);
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

            public void exitIdle(String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(reason);
                    if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().exitIdle(reason);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean registerMaintenanceActivityListener(IMaintenanceActivityListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(23, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().registerMaintenanceActivityListener(listener);
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

            public void unregisterMaintenanceActivityListener(IMaintenanceActivityListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(24, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterMaintenanceActivityListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setPreIdleTimeoutMode(int Mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(Mode);
                    int i = 25;
                    if (!this.mRemote.transact(25, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().setPreIdleTimeoutMode(Mode);
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

            public void resetPreIdleTimeoutMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(26, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().resetPreIdleTimeoutMode();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IDeviceIdleController asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IDeviceIdleController)) {
                return new Proxy(obj);
            }
            return (IDeviceIdleController) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "addPowerSaveWhitelistApp";
                case 2:
                    return "removePowerSaveWhitelistApp";
                case 3:
                    return "addPowerSaveWhitelistApps";
                case 4:
                    return "removePowerSaveWhitelistApps";
                case 5:
                    return "removeSystemPowerWhitelistApp";
                case 6:
                    return "restoreSystemPowerWhitelistApp";
                case 7:
                    return "getRemovedSystemPowerWhitelistApps";
                case 8:
                    return "getSystemPowerWhitelistExceptIdle";
                case 9:
                    return "getSystemPowerWhitelist";
                case 10:
                    return "getUserPowerWhitelist";
                case 11:
                    return "getFullPowerWhitelistExceptIdle";
                case 12:
                    return "getFullPowerWhitelist";
                case 13:
                    return "getAppIdWhitelistExceptIdle";
                case 14:
                    return "getAppIdWhitelist";
                case 15:
                    return "getAppIdUserWhitelist";
                case 16:
                    return "getAppIdTempWhitelist";
                case 17:
                    return "isPowerSaveWhitelistExceptIdleApp";
                case 18:
                    return "isPowerSaveWhitelistApp";
                case 19:
                    return "addPowerSaveTempWhitelistApp";
                case 20:
                    return "addPowerSaveTempWhitelistAppForMms";
                case 21:
                    return "addPowerSaveTempWhitelistAppForSms";
                case 22:
                    return "exitIdle";
                case 23:
                    return "registerMaintenanceActivityListener";
                case 24:
                    return "unregisterMaintenanceActivityListener";
                case 25:
                    return "setPreIdleTimeoutMode";
                case 26:
                    return "resetPreIdleTimeoutMode";
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
                String[] _result;
                int[] _result2;
                boolean _result3;
                long _result4;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        addPowerSaveWhitelistApp(data.readString());
                        reply.writeNoException();
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        removePowerSaveWhitelistApp(data.readString());
                        reply.writeNoException();
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        addPowerSaveWhitelistApps(data.createStringArray());
                        reply.writeNoException();
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        removePowerSaveWhitelistApps(data.createStringArray());
                        reply.writeNoException();
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        removeSystemPowerWhitelistApp(data.readString());
                        reply.writeNoException();
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        restoreSystemPowerWhitelistApp(data.readString());
                        reply.writeNoException();
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        _result = getRemovedSystemPowerWhitelistApps();
                        reply.writeNoException();
                        parcel2.writeStringArray(_result);
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        _result = getSystemPowerWhitelistExceptIdle();
                        reply.writeNoException();
                        parcel2.writeStringArray(_result);
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        _result = getSystemPowerWhitelist();
                        reply.writeNoException();
                        parcel2.writeStringArray(_result);
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        _result = getUserPowerWhitelist();
                        reply.writeNoException();
                        parcel2.writeStringArray(_result);
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        _result = getFullPowerWhitelistExceptIdle();
                        reply.writeNoException();
                        parcel2.writeStringArray(_result);
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        _result = getFullPowerWhitelist();
                        reply.writeNoException();
                        parcel2.writeStringArray(_result);
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        _result2 = getAppIdWhitelistExceptIdle();
                        reply.writeNoException();
                        parcel2.writeIntArray(_result2);
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        _result2 = getAppIdWhitelist();
                        reply.writeNoException();
                        parcel2.writeIntArray(_result2);
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        _result2 = getAppIdUserWhitelist();
                        reply.writeNoException();
                        parcel2.writeIntArray(_result2);
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        _result2 = getAppIdTempWhitelist();
                        reply.writeNoException();
                        parcel2.writeIntArray(_result2);
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        _result3 = isPowerSaveWhitelistExceptIdleApp(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        _result3 = isPowerSaveWhitelistApp(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        addPowerSaveTempWhitelistApp(data.readString(), data.readLong(), data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        _result4 = addPowerSaveTempWhitelistAppForMms(data.readString(), data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeLong(_result4);
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        _result4 = addPowerSaveTempWhitelistAppForSms(data.readString(), data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeLong(_result4);
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        exitIdle(data.readString());
                        reply.writeNoException();
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        _result3 = registerMaintenanceActivityListener(android.os.IMaintenanceActivityListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        unregisterMaintenanceActivityListener(android.os.IMaintenanceActivityListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 25:
                        parcel.enforceInterface(descriptor);
                        int _result5 = setPreIdleTimeoutMode(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 26:
                        parcel.enforceInterface(descriptor);
                        resetPreIdleTimeoutMode();
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IDeviceIdleController impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IDeviceIdleController getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    @UnsupportedAppUsage
    void addPowerSaveTempWhitelistApp(String str, long j, int i, String str2) throws RemoteException;

    long addPowerSaveTempWhitelistAppForMms(String str, int i, String str2) throws RemoteException;

    long addPowerSaveTempWhitelistAppForSms(String str, int i, String str2) throws RemoteException;

    void addPowerSaveWhitelistApp(String str) throws RemoteException;

    void addPowerSaveWhitelistApps(String[] strArr) throws RemoteException;

    void exitIdle(String str) throws RemoteException;

    int[] getAppIdTempWhitelist() throws RemoteException;

    int[] getAppIdUserWhitelist() throws RemoteException;

    int[] getAppIdWhitelist() throws RemoteException;

    int[] getAppIdWhitelistExceptIdle() throws RemoteException;

    String[] getFullPowerWhitelist() throws RemoteException;

    String[] getFullPowerWhitelistExceptIdle() throws RemoteException;

    String[] getRemovedSystemPowerWhitelistApps() throws RemoteException;

    String[] getSystemPowerWhitelist() throws RemoteException;

    String[] getSystemPowerWhitelistExceptIdle() throws RemoteException;

    String[] getUserPowerWhitelist() throws RemoteException;

    boolean isPowerSaveWhitelistApp(String str) throws RemoteException;

    boolean isPowerSaveWhitelistExceptIdleApp(String str) throws RemoteException;

    boolean registerMaintenanceActivityListener(IMaintenanceActivityListener iMaintenanceActivityListener) throws RemoteException;

    void removePowerSaveWhitelistApp(String str) throws RemoteException;

    void removePowerSaveWhitelistApps(String[] strArr) throws RemoteException;

    void removeSystemPowerWhitelistApp(String str) throws RemoteException;

    void resetPreIdleTimeoutMode() throws RemoteException;

    void restoreSystemPowerWhitelistApp(String str) throws RemoteException;

    int setPreIdleTimeoutMode(int i) throws RemoteException;

    void unregisterMaintenanceActivityListener(IMaintenanceActivityListener iMaintenanceActivityListener) throws RemoteException;
}
