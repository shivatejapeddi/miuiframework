package android.hardware.radio;

import android.graphics.Bitmap;
import android.hardware.radio.ProgramList.Filter;
import android.hardware.radio.RadioManager.BandConfig;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ITuner extends IInterface {

    public static class Default implements ITuner {
        public void close() throws RemoteException {
        }

        public boolean isClosed() throws RemoteException {
            return false;
        }

        public void setConfiguration(BandConfig config) throws RemoteException {
        }

        public BandConfig getConfiguration() throws RemoteException {
            return null;
        }

        public void setMuted(boolean mute) throws RemoteException {
        }

        public boolean isMuted() throws RemoteException {
            return false;
        }

        public void step(boolean directionDown, boolean skipSubChannel) throws RemoteException {
        }

        public void scan(boolean directionDown, boolean skipSubChannel) throws RemoteException {
        }

        public void tune(ProgramSelector selector) throws RemoteException {
        }

        public void cancel() throws RemoteException {
        }

        public void cancelAnnouncement() throws RemoteException {
        }

        public Bitmap getImage(int id) throws RemoteException {
            return null;
        }

        public boolean startBackgroundScan() throws RemoteException {
            return false;
        }

        public void startProgramListUpdates(Filter filter) throws RemoteException {
        }

        public void stopProgramListUpdates() throws RemoteException {
        }

        public boolean isConfigFlagSupported(int flag) throws RemoteException {
            return false;
        }

        public boolean isConfigFlagSet(int flag) throws RemoteException {
            return false;
        }

        public void setConfigFlag(int flag, boolean value) throws RemoteException {
        }

        public Map setParameters(Map parameters) throws RemoteException {
            return null;
        }

        public Map getParameters(List<String> list) throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ITuner {
        private static final String DESCRIPTOR = "android.hardware.radio.ITuner";
        static final int TRANSACTION_cancel = 10;
        static final int TRANSACTION_cancelAnnouncement = 11;
        static final int TRANSACTION_close = 1;
        static final int TRANSACTION_getConfiguration = 4;
        static final int TRANSACTION_getImage = 12;
        static final int TRANSACTION_getParameters = 20;
        static final int TRANSACTION_isClosed = 2;
        static final int TRANSACTION_isConfigFlagSet = 17;
        static final int TRANSACTION_isConfigFlagSupported = 16;
        static final int TRANSACTION_isMuted = 6;
        static final int TRANSACTION_scan = 8;
        static final int TRANSACTION_setConfigFlag = 18;
        static final int TRANSACTION_setConfiguration = 3;
        static final int TRANSACTION_setMuted = 5;
        static final int TRANSACTION_setParameters = 19;
        static final int TRANSACTION_startBackgroundScan = 13;
        static final int TRANSACTION_startProgramListUpdates = 14;
        static final int TRANSACTION_step = 7;
        static final int TRANSACTION_stopProgramListUpdates = 15;
        static final int TRANSACTION_tune = 9;

        private static class Proxy implements ITuner {
            public static ITuner sDefaultImpl;
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

            public void close() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().close();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isClosed() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isClosed();
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

            public void setConfiguration(BandConfig config) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (config != null) {
                        _data.writeInt(1);
                        config.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setConfiguration(config);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public BandConfig getConfiguration() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    BandConfig bandConfig = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        bandConfig = Stub.getDefaultImpl();
                        if (bandConfig != 0) {
                            bandConfig = Stub.getDefaultImpl().getConfiguration();
                            return bandConfig;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        bandConfig = (BandConfig) BandConfig.CREATOR.createFromParcel(_reply);
                    } else {
                        bandConfig = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return bandConfig;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setMuted(boolean mute) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mute ? 1 : 0);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setMuted(mute);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isMuted() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isMuted();
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

            public void step(boolean directionDown, boolean skipSubChannel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    _data.writeInt(directionDown ? 1 : 0);
                    if (!skipSubChannel) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().step(directionDown, skipSubChannel);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void scan(boolean directionDown, boolean skipSubChannel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    _data.writeInt(directionDown ? 1 : 0);
                    if (!skipSubChannel) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().scan(directionDown, skipSubChannel);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void tune(ProgramSelector selector) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (selector != null) {
                        _data.writeInt(1);
                        selector.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().tune(selector);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cancel() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().cancel();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cancelAnnouncement() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().cancelAnnouncement();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bitmap getImage(int id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(id);
                    Bitmap bitmap = 12;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        bitmap = Stub.getDefaultImpl();
                        if (bitmap != 0) {
                            bitmap = Stub.getDefaultImpl().getImage(id);
                            return bitmap;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        bitmap = (Bitmap) Bitmap.CREATOR.createFromParcel(_reply);
                    } else {
                        bitmap = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return bitmap;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean startBackgroundScan() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().startBackgroundScan();
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

            public void startProgramListUpdates(Filter filter) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (filter != null) {
                        _data.writeInt(1);
                        filter.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startProgramListUpdates(filter);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopProgramListUpdates() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(15, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().stopProgramListUpdates();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isConfigFlagSupported(int flag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flag);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(16, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isConfigFlagSupported(flag);
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

            public boolean isConfigFlagSet(int flag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flag);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(17, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isConfigFlagSet(flag);
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

            public void setConfigFlag(int flag, boolean value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flag);
                    _data.writeInt(value ? 1 : 0);
                    if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setConfigFlag(flag, value);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Map setParameters(Map parameters) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeMap(parameters);
                    Map map = 19;
                    if (!this.mRemote.transact(19, _data, _reply, 0)) {
                        map = Stub.getDefaultImpl();
                        if (map != 0) {
                            map = Stub.getDefaultImpl().setParameters(parameters);
                            return map;
                        }
                    }
                    _reply.readException();
                    map = getClass().getClassLoader();
                    HashMap _result = _reply.readHashMap(map);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Map getParameters(List<String> keys) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(keys);
                    Map map = 20;
                    if (!this.mRemote.transact(20, _data, _reply, 0)) {
                        map = Stub.getDefaultImpl();
                        if (map != 0) {
                            map = Stub.getDefaultImpl().getParameters(keys);
                            return map;
                        }
                    }
                    _reply.readException();
                    map = getClass().getClassLoader();
                    HashMap _result = _reply.readHashMap(map);
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

        public static ITuner asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ITuner)) {
                return new Proxy(obj);
            }
            return (ITuner) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "close";
                case 2:
                    return "isClosed";
                case 3:
                    return "setConfiguration";
                case 4:
                    return "getConfiguration";
                case 5:
                    return "setMuted";
                case 6:
                    return "isMuted";
                case 7:
                    return "step";
                case 8:
                    return "scan";
                case 9:
                    return "tune";
                case 10:
                    return "cancel";
                case 11:
                    return "cancelAnnouncement";
                case 12:
                    return "getImage";
                case 13:
                    return "startBackgroundScan";
                case 14:
                    return "startProgramListUpdates";
                case 15:
                    return "stopProgramListUpdates";
                case 16:
                    return "isConfigFlagSupported";
                case 17:
                    return "isConfigFlagSet";
                case 18:
                    return "setConfigFlag";
                case 19:
                    return "setParameters";
                case 20:
                    return "getParameters";
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
                boolean _arg0;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        close();
                        reply.writeNoException();
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        _arg1 = isClosed();
                        reply.writeNoException();
                        reply.writeInt(_arg1);
                        return true;
                    case 3:
                        BandConfig _arg02;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (BandConfig) BandConfig.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        setConfiguration(_arg02);
                        reply.writeNoException();
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        BandConfig _result = getConfiguration();
                        reply.writeNoException();
                        if (_result != null) {
                            reply.writeInt(1);
                            _result.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setMuted(_arg1);
                        reply.writeNoException();
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        _arg1 = isMuted();
                        reply.writeNoException();
                        reply.writeInt(_arg1);
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        _arg0 = data.readInt() != 0;
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        step(_arg0, _arg1);
                        reply.writeNoException();
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        _arg0 = data.readInt() != 0;
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        scan(_arg0, _arg1);
                        reply.writeNoException();
                        return true;
                    case 9:
                        ProgramSelector _arg03;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (ProgramSelector) ProgramSelector.CREATOR.createFromParcel(data);
                        } else {
                            _arg03 = null;
                        }
                        tune(_arg03);
                        reply.writeNoException();
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        cancel();
                        reply.writeNoException();
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        cancelAnnouncement();
                        reply.writeNoException();
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        Bitmap _result2 = getImage(data.readInt());
                        reply.writeNoException();
                        if (_result2 != null) {
                            reply.writeInt(1);
                            _result2.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        _arg1 = startBackgroundScan();
                        reply.writeNoException();
                        reply.writeInt(_arg1);
                        return true;
                    case 14:
                        Filter _arg04;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (Filter) Filter.CREATOR.createFromParcel(data);
                        } else {
                            _arg04 = null;
                        }
                        startProgramListUpdates(_arg04);
                        reply.writeNoException();
                        return true;
                    case 15:
                        data.enforceInterface(descriptor);
                        stopProgramListUpdates();
                        reply.writeNoException();
                        return true;
                    case 16:
                        data.enforceInterface(descriptor);
                        _arg0 = isConfigFlagSupported(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    case 17:
                        data.enforceInterface(descriptor);
                        _arg0 = isConfigFlagSet(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    case 18:
                        data.enforceInterface(descriptor);
                        int _arg05 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setConfigFlag(_arg05, _arg1);
                        reply.writeNoException();
                        return true;
                    case 19:
                        data.enforceInterface(descriptor);
                        Map _result3 = setParameters(data.readHashMap(getClass().getClassLoader()));
                        reply.writeNoException();
                        reply.writeMap(_result3);
                        return true;
                    case 20:
                        data.enforceInterface(descriptor);
                        Map _result4 = getParameters(data.createStringArrayList());
                        reply.writeNoException();
                        reply.writeMap(_result4);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(ITuner impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ITuner getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void cancel() throws RemoteException;

    void cancelAnnouncement() throws RemoteException;

    void close() throws RemoteException;

    BandConfig getConfiguration() throws RemoteException;

    Bitmap getImage(int i) throws RemoteException;

    Map getParameters(List<String> list) throws RemoteException;

    boolean isClosed() throws RemoteException;

    boolean isConfigFlagSet(int i) throws RemoteException;

    boolean isConfigFlagSupported(int i) throws RemoteException;

    boolean isMuted() throws RemoteException;

    void scan(boolean z, boolean z2) throws RemoteException;

    void setConfigFlag(int i, boolean z) throws RemoteException;

    void setConfiguration(BandConfig bandConfig) throws RemoteException;

    void setMuted(boolean z) throws RemoteException;

    Map setParameters(Map map) throws RemoteException;

    boolean startBackgroundScan() throws RemoteException;

    void startProgramListUpdates(Filter filter) throws RemoteException;

    void step(boolean z, boolean z2) throws RemoteException;

    void stopProgramListUpdates() throws RemoteException;

    void tune(ProgramSelector programSelector) throws RemoteException;
}
