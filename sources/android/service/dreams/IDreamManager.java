package android.service.dreams;

import android.annotation.UnsupportedAppUsage;
import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IDreamManager extends IInterface {

    public static class Default implements IDreamManager {
        public void dream() throws RemoteException {
        }

        public void awaken() throws RemoteException {
        }

        public void setDreamComponents(ComponentName[] componentNames) throws RemoteException {
        }

        public ComponentName[] getDreamComponents() throws RemoteException {
            return null;
        }

        public ComponentName getDefaultDreamComponent() throws RemoteException {
            return null;
        }

        public void testDream(ComponentName componentName) throws RemoteException {
        }

        public boolean isDreaming() throws RemoteException {
            return false;
        }

        public void finishSelf(IBinder token, boolean immediate) throws RemoteException {
        }

        public void startDozing(IBinder token, int screenState, int screenBrightness) throws RemoteException {
        }

        public void stopDozing(IBinder token) throws RemoteException {
        }

        public void forceAmbientDisplayEnabled(boolean enabled) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IDreamManager {
        private static final String DESCRIPTOR = "android.service.dreams.IDreamManager";
        static final int TRANSACTION_awaken = 2;
        static final int TRANSACTION_dream = 1;
        static final int TRANSACTION_finishSelf = 8;
        static final int TRANSACTION_forceAmbientDisplayEnabled = 11;
        static final int TRANSACTION_getDefaultDreamComponent = 5;
        static final int TRANSACTION_getDreamComponents = 4;
        static final int TRANSACTION_isDreaming = 7;
        static final int TRANSACTION_setDreamComponents = 3;
        static final int TRANSACTION_startDozing = 9;
        static final int TRANSACTION_stopDozing = 10;
        static final int TRANSACTION_testDream = 6;

        private static class Proxy implements IDreamManager {
            public static IDreamManager sDefaultImpl;
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

            public void dream() throws RemoteException {
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
                    Stub.getDefaultImpl().dream();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void awaken() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().awaken();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setDreamComponents(ComponentName[] componentNames) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedArray(componentNames, 0);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setDreamComponents(componentNames);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ComponentName[] getDreamComponents() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ComponentName[] componentNameArr = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        componentNameArr = Stub.getDefaultImpl();
                        if (componentNameArr != 0) {
                            componentNameArr = Stub.getDefaultImpl().getDreamComponents();
                            return componentNameArr;
                        }
                    }
                    _reply.readException();
                    ComponentName[] _result = (ComponentName[]) _reply.createTypedArray(ComponentName.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ComponentName getDefaultDreamComponent() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ComponentName componentName = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        componentName = Stub.getDefaultImpl();
                        if (componentName != 0) {
                            componentName = Stub.getDefaultImpl().getDefaultDreamComponent();
                            return componentName;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        componentName = (ComponentName) ComponentName.CREATOR.createFromParcel(_reply);
                    } else {
                        componentName = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return componentName;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void testDream(ComponentName componentName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (componentName != null) {
                        _data.writeInt(1);
                        componentName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().testDream(componentName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isDreaming() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isDreaming();
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

            public void finishSelf(IBinder token, boolean immediate) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(immediate ? 1 : 0);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().finishSelf(token, immediate);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startDozing(IBinder token, int screenState, int screenBrightness) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(screenState);
                    _data.writeInt(screenBrightness);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startDozing(token, screenState, screenBrightness);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopDozing(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().stopDozing(token);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void forceAmbientDisplayEnabled(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().forceAmbientDisplayEnabled(enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IDreamManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IDreamManager)) {
                return new Proxy(obj);
            }
            return (IDreamManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "dream";
                case 2:
                    return "awaken";
                case 3:
                    return "setDreamComponents";
                case 4:
                    return "getDreamComponents";
                case 5:
                    return "getDefaultDreamComponent";
                case 6:
                    return "testDream";
                case 7:
                    return "isDreaming";
                case 8:
                    return "finishSelf";
                case 9:
                    return "startDozing";
                case 10:
                    return "stopDozing";
                case 11:
                    return "forceAmbientDisplayEnabled";
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
                        dream();
                        reply.writeNoException();
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        awaken();
                        reply.writeNoException();
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        setDreamComponents((ComponentName[]) data.createTypedArray(ComponentName.CREATOR));
                        reply.writeNoException();
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        ComponentName[] _result = getDreamComponents();
                        reply.writeNoException();
                        reply.writeTypedArray(_result, 1);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        ComponentName _result2 = getDefaultDreamComponent();
                        reply.writeNoException();
                        if (_result2 != null) {
                            reply.writeInt(1);
                            _result2.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 6:
                        ComponentName _arg02;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        testDream(_arg02);
                        reply.writeNoException();
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        _arg0 = isDreaming();
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        IBinder _arg03 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        finishSelf(_arg03, _arg0);
                        reply.writeNoException();
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        startDozing(data.readStrongBinder(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        stopDozing(data.readStrongBinder());
                        reply.writeNoException();
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        forceAmbientDisplayEnabled(_arg0);
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IDreamManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IDreamManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    @UnsupportedAppUsage
    void awaken() throws RemoteException;

    @UnsupportedAppUsage
    void dream() throws RemoteException;

    void finishSelf(IBinder iBinder, boolean z) throws RemoteException;

    void forceAmbientDisplayEnabled(boolean z) throws RemoteException;

    ComponentName getDefaultDreamComponent() throws RemoteException;

    ComponentName[] getDreamComponents() throws RemoteException;

    @UnsupportedAppUsage
    boolean isDreaming() throws RemoteException;

    @UnsupportedAppUsage
    void setDreamComponents(ComponentName[] componentNameArr) throws RemoteException;

    void startDozing(IBinder iBinder, int i, int i2) throws RemoteException;

    void stopDozing(IBinder iBinder) throws RemoteException;

    void testDream(ComponentName componentName) throws RemoteException;
}
