package android.service.vr;

import android.annotation.UnsupportedAppUsage;
import android.app.Vr2dDisplayProperties;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IVrManager extends IInterface {

    public static class Default implements IVrManager {
        public void registerListener(IVrStateCallbacks cb) throws RemoteException {
        }

        public void unregisterListener(IVrStateCallbacks cb) throws RemoteException {
        }

        public void registerPersistentVrStateListener(IPersistentVrStateCallbacks cb) throws RemoteException {
        }

        public void unregisterPersistentVrStateListener(IPersistentVrStateCallbacks cb) throws RemoteException {
        }

        public boolean getVrModeState() throws RemoteException {
            return false;
        }

        public boolean getPersistentVrModeEnabled() throws RemoteException {
            return false;
        }

        public void setPersistentVrModeEnabled(boolean enabled) throws RemoteException {
        }

        public void setVr2dDisplayProperties(Vr2dDisplayProperties vr2dDisplayProperties) throws RemoteException {
        }

        public int getVr2dDisplayId() throws RemoteException {
            return 0;
        }

        public void setAndBindCompositor(String componentName) throws RemoteException {
        }

        public void setStandbyEnabled(boolean standby) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IVrManager {
        private static final String DESCRIPTOR = "android.service.vr.IVrManager";
        static final int TRANSACTION_getPersistentVrModeEnabled = 6;
        static final int TRANSACTION_getVr2dDisplayId = 9;
        static final int TRANSACTION_getVrModeState = 5;
        static final int TRANSACTION_registerListener = 1;
        static final int TRANSACTION_registerPersistentVrStateListener = 3;
        static final int TRANSACTION_setAndBindCompositor = 10;
        static final int TRANSACTION_setPersistentVrModeEnabled = 7;
        static final int TRANSACTION_setStandbyEnabled = 11;
        static final int TRANSACTION_setVr2dDisplayProperties = 8;
        static final int TRANSACTION_unregisterListener = 2;
        static final int TRANSACTION_unregisterPersistentVrStateListener = 4;

        private static class Proxy implements IVrManager {
            public static IVrManager sDefaultImpl;
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

            public void registerListener(IVrStateCallbacks cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerListener(cb);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterListener(IVrStateCallbacks cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterListener(cb);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerPersistentVrStateListener(IPersistentVrStateCallbacks cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerPersistentVrStateListener(cb);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterPersistentVrStateListener(IPersistentVrStateCallbacks cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterPersistentVrStateListener(cb);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getVrModeState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().getVrModeState();
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

            public boolean getPersistentVrModeEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().getPersistentVrModeEnabled();
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

            public void setPersistentVrModeEnabled(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPersistentVrModeEnabled(enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setVr2dDisplayProperties(Vr2dDisplayProperties vr2dDisplayProperties) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (vr2dDisplayProperties != null) {
                        _data.writeInt(1);
                        vr2dDisplayProperties.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setVr2dDisplayProperties(vr2dDisplayProperties);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getVr2dDisplayId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 9;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getVr2dDisplayId();
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

            public void setAndBindCompositor(String componentName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(componentName);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAndBindCompositor(componentName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setStandbyEnabled(boolean standby) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(standby ? 1 : 0);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setStandbyEnabled(standby);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IVrManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IVrManager)) {
                return new Proxy(obj);
            }
            return (IVrManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "registerListener";
                case 2:
                    return "unregisterListener";
                case 3:
                    return "registerPersistentVrStateListener";
                case 4:
                    return "unregisterPersistentVrStateListener";
                case 5:
                    return "getVrModeState";
                case 6:
                    return "getPersistentVrModeEnabled";
                case 7:
                    return "setPersistentVrModeEnabled";
                case 8:
                    return "setVr2dDisplayProperties";
                case 9:
                    return "getVr2dDisplayId";
                case 10:
                    return "setAndBindCompositor";
                case 11:
                    return "setStandbyEnabled";
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
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        registerListener(android.service.vr.IVrStateCallbacks.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        unregisterListener(android.service.vr.IVrStateCallbacks.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        registerPersistentVrStateListener(android.service.vr.IPersistentVrStateCallbacks.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        unregisterPersistentVrStateListener(android.service.vr.IPersistentVrStateCallbacks.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        _arg0 = getVrModeState();
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        _arg0 = getPersistentVrModeEnabled();
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        setPersistentVrModeEnabled(_arg0);
                        reply.writeNoException();
                        return true;
                    case 8:
                        Vr2dDisplayProperties _arg02;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (Vr2dDisplayProperties) Vr2dDisplayProperties.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        setVr2dDisplayProperties(_arg02);
                        reply.writeNoException();
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        int _result = getVr2dDisplayId();
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        setAndBindCompositor(data.readString());
                        reply.writeNoException();
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        setStandbyEnabled(_arg0);
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IVrManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IVrManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    boolean getPersistentVrModeEnabled() throws RemoteException;

    @UnsupportedAppUsage
    int getVr2dDisplayId() throws RemoteException;

    @UnsupportedAppUsage
    boolean getVrModeState() throws RemoteException;

    void registerListener(IVrStateCallbacks iVrStateCallbacks) throws RemoteException;

    void registerPersistentVrStateListener(IPersistentVrStateCallbacks iPersistentVrStateCallbacks) throws RemoteException;

    void setAndBindCompositor(String str) throws RemoteException;

    void setPersistentVrModeEnabled(boolean z) throws RemoteException;

    void setStandbyEnabled(boolean z) throws RemoteException;

    void setVr2dDisplayProperties(Vr2dDisplayProperties vr2dDisplayProperties) throws RemoteException;

    void unregisterListener(IVrStateCallbacks iVrStateCallbacks) throws RemoteException;

    void unregisterPersistentVrStateListener(IPersistentVrStateCallbacks iPersistentVrStateCallbacks) throws RemoteException;
}
