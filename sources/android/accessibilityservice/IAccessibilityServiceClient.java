package android.accessibilityservice;

import android.graphics.Region;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

public interface IAccessibilityServiceClient extends IInterface {

    public static abstract class Stub extends Binder implements IAccessibilityServiceClient {
        private static final String DESCRIPTOR = "android.accessibilityservice.IAccessibilityServiceClient";
        static final int TRANSACTION_clearAccessibilityCache = 5;
        static final int TRANSACTION_init = 1;
        static final int TRANSACTION_onAccessibilityButtonAvailabilityChanged = 13;
        static final int TRANSACTION_onAccessibilityButtonClicked = 12;
        static final int TRANSACTION_onAccessibilityEvent = 2;
        static final int TRANSACTION_onFingerprintCapturingGesturesChanged = 10;
        static final int TRANSACTION_onFingerprintGesture = 11;
        static final int TRANSACTION_onGesture = 4;
        static final int TRANSACTION_onInterrupt = 3;
        static final int TRANSACTION_onKeyEvent = 6;
        static final int TRANSACTION_onMagnificationChanged = 7;
        static final int TRANSACTION_onPerformGestureResult = 9;
        static final int TRANSACTION_onSoftKeyboardShowModeChanged = 8;

        private static class Proxy implements IAccessibilityServiceClient {
            public static IAccessibilityServiceClient sDefaultImpl;
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

            public void init(IAccessibilityServiceConnection connection, int connectionId, IBinder windowToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(connection != null ? connection.asBinder() : null);
                    _data.writeInt(connectionId);
                    _data.writeStrongBinder(windowToken);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().init(connection, connectionId, windowToken);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onAccessibilityEvent(AccessibilityEvent event, boolean serviceWantsEvent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    if (event != null) {
                        _data.writeInt(1);
                        event.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (serviceWantsEvent) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onAccessibilityEvent(event, serviceWantsEvent);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onInterrupt() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onInterrupt();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onGesture(int gesture) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(gesture);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onGesture(gesture);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void clearAccessibilityCache() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().clearAccessibilityCache();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onKeyEvent(KeyEvent event, int sequence) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (event != null) {
                        _data.writeInt(1);
                        event.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(sequence);
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onKeyEvent(event, sequence);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onMagnificationChanged(int displayId, Region region, float scale, float centerX, float centerY) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    if (region != null) {
                        _data.writeInt(1);
                        region.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeFloat(scale);
                    _data.writeFloat(centerX);
                    _data.writeFloat(centerY);
                    if (this.mRemote.transact(7, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onMagnificationChanged(displayId, region, scale, centerX, centerY);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onSoftKeyboardShowModeChanged(int showMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(showMode);
                    if (this.mRemote.transact(8, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onSoftKeyboardShowModeChanged(showMode);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onPerformGestureResult(int sequence, boolean completedSuccessfully) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sequence);
                    _data.writeInt(completedSuccessfully ? 1 : 0);
                    if (this.mRemote.transact(9, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onPerformGestureResult(sequence, completedSuccessfully);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onFingerprintCapturingGesturesChanged(boolean capturing) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(capturing ? 1 : 0);
                    if (this.mRemote.transact(10, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onFingerprintCapturingGesturesChanged(capturing);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onFingerprintGesture(int gesture) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(gesture);
                    if (this.mRemote.transact(11, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onFingerprintGesture(gesture);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onAccessibilityButtonClicked() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(12, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onAccessibilityButtonClicked();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onAccessibilityButtonAvailabilityChanged(boolean available) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(available ? 1 : 0);
                    if (this.mRemote.transact(13, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onAccessibilityButtonAvailabilityChanged(available);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAccessibilityServiceClient asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IAccessibilityServiceClient)) {
                return new Proxy(obj);
            }
            return (IAccessibilityServiceClient) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "init";
                case 2:
                    return "onAccessibilityEvent";
                case 3:
                    return "onInterrupt";
                case 4:
                    return "onGesture";
                case 5:
                    return "clearAccessibilityCache";
                case 6:
                    return "onKeyEvent";
                case 7:
                    return "onMagnificationChanged";
                case 8:
                    return "onSoftKeyboardShowModeChanged";
                case 9:
                    return "onPerformGestureResult";
                case 10:
                    return "onFingerprintCapturingGesturesChanged";
                case 11:
                    return "onFingerprintGesture";
                case 12:
                    return "onAccessibilityButtonClicked";
                case 13:
                    return "onAccessibilityButtonAvailabilityChanged";
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
                boolean _arg1 = false;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        init(android.accessibilityservice.IAccessibilityServiceConnection.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readStrongBinder());
                        return true;
                    case 2:
                        AccessibilityEvent _arg0;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (AccessibilityEvent) AccessibilityEvent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        onAccessibilityEvent(_arg0, _arg1);
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        onInterrupt();
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        onGesture(data.readInt());
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        clearAccessibilityCache();
                        return true;
                    case 6:
                        KeyEvent _arg02;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (KeyEvent) KeyEvent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        onKeyEvent(_arg02, data.readInt());
                        return true;
                    case 7:
                        Region _arg12;
                        parcel.enforceInterface(descriptor);
                        int _arg03 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg12 = (Region) Region.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        onMagnificationChanged(_arg03, _arg12, data.readFloat(), data.readFloat(), data.readFloat());
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        onSoftKeyboardShowModeChanged(data.readInt());
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        int _arg04 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        onPerformGestureResult(_arg04, _arg1);
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        onFingerprintCapturingGesturesChanged(_arg1);
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        onFingerprintGesture(data.readInt());
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        onAccessibilityButtonClicked();
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        onAccessibilityButtonAvailabilityChanged(_arg1);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IAccessibilityServiceClient impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IAccessibilityServiceClient getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    public static class Default implements IAccessibilityServiceClient {
        public void init(IAccessibilityServiceConnection connection, int connectionId, IBinder windowToken) throws RemoteException {
        }

        public void onAccessibilityEvent(AccessibilityEvent event, boolean serviceWantsEvent) throws RemoteException {
        }

        public void onInterrupt() throws RemoteException {
        }

        public void onGesture(int gesture) throws RemoteException {
        }

        public void clearAccessibilityCache() throws RemoteException {
        }

        public void onKeyEvent(KeyEvent event, int sequence) throws RemoteException {
        }

        public void onMagnificationChanged(int displayId, Region region, float scale, float centerX, float centerY) throws RemoteException {
        }

        public void onSoftKeyboardShowModeChanged(int showMode) throws RemoteException {
        }

        public void onPerformGestureResult(int sequence, boolean completedSuccessfully) throws RemoteException {
        }

        public void onFingerprintCapturingGesturesChanged(boolean capturing) throws RemoteException {
        }

        public void onFingerprintGesture(int gesture) throws RemoteException {
        }

        public void onAccessibilityButtonClicked() throws RemoteException {
        }

        public void onAccessibilityButtonAvailabilityChanged(boolean available) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    void clearAccessibilityCache() throws RemoteException;

    void init(IAccessibilityServiceConnection iAccessibilityServiceConnection, int i, IBinder iBinder) throws RemoteException;

    void onAccessibilityButtonAvailabilityChanged(boolean z) throws RemoteException;

    void onAccessibilityButtonClicked() throws RemoteException;

    void onAccessibilityEvent(AccessibilityEvent accessibilityEvent, boolean z) throws RemoteException;

    void onFingerprintCapturingGesturesChanged(boolean z) throws RemoteException;

    void onFingerprintGesture(int i) throws RemoteException;

    void onGesture(int i) throws RemoteException;

    void onInterrupt() throws RemoteException;

    void onKeyEvent(KeyEvent keyEvent, int i) throws RemoteException;

    void onMagnificationChanged(int i, Region region, float f, float f2, float f3) throws RemoteException;

    void onPerformGestureResult(int i, boolean z) throws RemoteException;

    void onSoftKeyboardShowModeChanged(int i) throws RemoteException;
}
