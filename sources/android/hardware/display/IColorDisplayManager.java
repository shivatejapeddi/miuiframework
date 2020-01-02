package android.hardware.display;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IColorDisplayManager extends IInterface {

    public static class Default implements IColorDisplayManager {
        public boolean isDeviceColorManaged() throws RemoteException {
            return false;
        }

        public boolean setSaturationLevel(int saturationLevel) throws RemoteException {
            return false;
        }

        public boolean setAppSaturationLevel(String packageName, int saturationLevel) throws RemoteException {
            return false;
        }

        public boolean isSaturationActivated() throws RemoteException {
            return false;
        }

        public int getTransformCapabilities() throws RemoteException {
            return 0;
        }

        public boolean isNightDisplayActivated() throws RemoteException {
            return false;
        }

        public boolean setNightDisplayActivated(boolean activated) throws RemoteException {
            return false;
        }

        public int getNightDisplayColorTemperature() throws RemoteException {
            return 0;
        }

        public boolean setNightDisplayColorTemperature(int temperature) throws RemoteException {
            return false;
        }

        public int getNightDisplayAutoMode() throws RemoteException {
            return 0;
        }

        public int getNightDisplayAutoModeRaw() throws RemoteException {
            return 0;
        }

        public boolean setNightDisplayAutoMode(int autoMode) throws RemoteException {
            return false;
        }

        public Time getNightDisplayCustomStartTime() throws RemoteException {
            return null;
        }

        public boolean setNightDisplayCustomStartTime(Time time) throws RemoteException {
            return false;
        }

        public Time getNightDisplayCustomEndTime() throws RemoteException {
            return null;
        }

        public boolean setNightDisplayCustomEndTime(Time time) throws RemoteException {
            return false;
        }

        public int getColorMode() throws RemoteException {
            return 0;
        }

        public void setColorMode(int colorMode) throws RemoteException {
        }

        public boolean isDisplayWhiteBalanceEnabled() throws RemoteException {
            return false;
        }

        public boolean setDisplayWhiteBalanceEnabled(boolean enabled) throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IColorDisplayManager {
        private static final String DESCRIPTOR = "android.hardware.display.IColorDisplayManager";
        static final int TRANSACTION_getColorMode = 17;
        static final int TRANSACTION_getNightDisplayAutoMode = 10;
        static final int TRANSACTION_getNightDisplayAutoModeRaw = 11;
        static final int TRANSACTION_getNightDisplayColorTemperature = 8;
        static final int TRANSACTION_getNightDisplayCustomEndTime = 15;
        static final int TRANSACTION_getNightDisplayCustomStartTime = 13;
        static final int TRANSACTION_getTransformCapabilities = 5;
        static final int TRANSACTION_isDeviceColorManaged = 1;
        static final int TRANSACTION_isDisplayWhiteBalanceEnabled = 19;
        static final int TRANSACTION_isNightDisplayActivated = 6;
        static final int TRANSACTION_isSaturationActivated = 4;
        static final int TRANSACTION_setAppSaturationLevel = 3;
        static final int TRANSACTION_setColorMode = 18;
        static final int TRANSACTION_setDisplayWhiteBalanceEnabled = 20;
        static final int TRANSACTION_setNightDisplayActivated = 7;
        static final int TRANSACTION_setNightDisplayAutoMode = 12;
        static final int TRANSACTION_setNightDisplayColorTemperature = 9;
        static final int TRANSACTION_setNightDisplayCustomEndTime = 16;
        static final int TRANSACTION_setNightDisplayCustomStartTime = 14;
        static final int TRANSACTION_setSaturationLevel = 2;

        private static class Proxy implements IColorDisplayManager {
            public static IColorDisplayManager sDefaultImpl;
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

            public boolean isDeviceColorManaged() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
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
                    z = Stub.getDefaultImpl().isDeviceColorManaged();
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setSaturationLevel(int saturationLevel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(saturationLevel);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().setSaturationLevel(saturationLevel);
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

            public boolean setAppSaturationLevel(String packageName, int saturationLevel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(saturationLevel);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().setAppSaturationLevel(packageName, saturationLevel);
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

            public boolean isSaturationActivated() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isSaturationActivated();
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

            public int getTransformCapabilities() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getTransformCapabilities();
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

            public boolean isNightDisplayActivated() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isNightDisplayActivated();
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

            public boolean setNightDisplayActivated(boolean activated) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    _data.writeInt(activated ? 1 : 0);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setNightDisplayActivated(activated);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getNightDisplayColorTemperature() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 8;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getNightDisplayColorTemperature();
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

            public boolean setNightDisplayColorTemperature(int temperature) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(temperature);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().setNightDisplayColorTemperature(temperature);
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

            public int getNightDisplayAutoMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 10;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getNightDisplayAutoMode();
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

            public int getNightDisplayAutoModeRaw() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 11;
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getNightDisplayAutoModeRaw();
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

            public boolean setNightDisplayAutoMode(int autoMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(autoMode);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().setNightDisplayAutoMode(autoMode);
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

            public Time getNightDisplayCustomStartTime() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    Time time = 13;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        time = Stub.getDefaultImpl();
                        if (time != 0) {
                            time = Stub.getDefaultImpl().getNightDisplayCustomStartTime();
                            return time;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        time = (Time) Time.CREATOR.createFromParcel(_reply);
                    } else {
                        time = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return time;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setNightDisplayCustomStartTime(Time time) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (time != null) {
                        _data.writeInt(1);
                        time.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setNightDisplayCustomStartTime(time);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Time getNightDisplayCustomEndTime() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    Time time = 15;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        time = Stub.getDefaultImpl();
                        if (time != 0) {
                            time = Stub.getDefaultImpl().getNightDisplayCustomEndTime();
                            return time;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        time = (Time) Time.CREATOR.createFromParcel(_reply);
                    } else {
                        time = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return time;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setNightDisplayCustomEndTime(Time time) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (time != null) {
                        _data.writeInt(1);
                        time.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(16, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setNightDisplayCustomEndTime(time);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getColorMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 17;
                    if (!this.mRemote.transact(17, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getColorMode();
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

            public void setColorMode(int colorMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(colorMode);
                    if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setColorMode(colorMode);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isDisplayWhiteBalanceEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(19, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isDisplayWhiteBalanceEnabled();
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

            public boolean setDisplayWhiteBalanceEnabled(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    _data.writeInt(enabled ? 1 : 0);
                    if (this.mRemote.transact(20, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setDisplayWhiteBalanceEnabled(enabled);
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

        public static IColorDisplayManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IColorDisplayManager)) {
                return new Proxy(obj);
            }
            return (IColorDisplayManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "isDeviceColorManaged";
                case 2:
                    return "setSaturationLevel";
                case 3:
                    return "setAppSaturationLevel";
                case 4:
                    return "isSaturationActivated";
                case 5:
                    return "getTransformCapabilities";
                case 6:
                    return "isNightDisplayActivated";
                case 7:
                    return "setNightDisplayActivated";
                case 8:
                    return "getNightDisplayColorTemperature";
                case 9:
                    return "setNightDisplayColorTemperature";
                case 10:
                    return "getNightDisplayAutoMode";
                case 11:
                    return "getNightDisplayAutoModeRaw";
                case 12:
                    return "setNightDisplayAutoMode";
                case 13:
                    return "getNightDisplayCustomStartTime";
                case 14:
                    return "setNightDisplayCustomStartTime";
                case 15:
                    return "getNightDisplayCustomEndTime";
                case 16:
                    return "setNightDisplayCustomEndTime";
                case 17:
                    return "getColorMode";
                case 18:
                    return "setColorMode";
                case 19:
                    return "isDisplayWhiteBalanceEnabled";
                case 20:
                    return "setDisplayWhiteBalanceEnabled";
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
                boolean _arg0 = false;
                boolean _result;
                int _result2;
                Time _result3;
                Time _arg02;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        _arg0 = isDeviceColorManaged();
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        _result = setSaturationLevel(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        boolean _result4 = setAppSaturationLevel(data.readString(), data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        _arg0 = isSaturationActivated();
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        _result2 = getTransformCapabilities();
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        _arg0 = isNightDisplayActivated();
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        _result = setNightDisplayActivated(_arg0);
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        _result2 = getNightDisplayColorTemperature();
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        _result = setNightDisplayColorTemperature(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        _result2 = getNightDisplayAutoMode();
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        _result2 = getNightDisplayAutoModeRaw();
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        _result = setNightDisplayAutoMode(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        _result3 = getNightDisplayCustomStartTime();
                        reply.writeNoException();
                        if (_result3 != null) {
                            reply.writeInt(1);
                            _result3.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 14:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (Time) Time.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        _result = setNightDisplayCustomStartTime(_arg02);
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 15:
                        data.enforceInterface(descriptor);
                        _result3 = getNightDisplayCustomEndTime();
                        reply.writeNoException();
                        if (_result3 != null) {
                            reply.writeInt(1);
                            _result3.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 16:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (Time) Time.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        _result = setNightDisplayCustomEndTime(_arg02);
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 17:
                        data.enforceInterface(descriptor);
                        _result2 = getColorMode();
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 18:
                        data.enforceInterface(descriptor);
                        setColorMode(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 19:
                        data.enforceInterface(descriptor);
                        _arg0 = isDisplayWhiteBalanceEnabled();
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    case 20:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        _result = setDisplayWhiteBalanceEnabled(_arg0);
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IColorDisplayManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IColorDisplayManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    int getColorMode() throws RemoteException;

    int getNightDisplayAutoMode() throws RemoteException;

    int getNightDisplayAutoModeRaw() throws RemoteException;

    int getNightDisplayColorTemperature() throws RemoteException;

    Time getNightDisplayCustomEndTime() throws RemoteException;

    Time getNightDisplayCustomStartTime() throws RemoteException;

    int getTransformCapabilities() throws RemoteException;

    boolean isDeviceColorManaged() throws RemoteException;

    boolean isDisplayWhiteBalanceEnabled() throws RemoteException;

    boolean isNightDisplayActivated() throws RemoteException;

    boolean isSaturationActivated() throws RemoteException;

    boolean setAppSaturationLevel(String str, int i) throws RemoteException;

    void setColorMode(int i) throws RemoteException;

    boolean setDisplayWhiteBalanceEnabled(boolean z) throws RemoteException;

    boolean setNightDisplayActivated(boolean z) throws RemoteException;

    boolean setNightDisplayAutoMode(int i) throws RemoteException;

    boolean setNightDisplayColorTemperature(int i) throws RemoteException;

    boolean setNightDisplayCustomEndTime(Time time) throws RemoteException;

    boolean setNightDisplayCustomStartTime(Time time) throws RemoteException;

    boolean setSaturationLevel(int i) throws RemoteException;
}
