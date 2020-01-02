package android.app;

import android.accessibilityservice.IAccessibilityServiceClient;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.view.InputEvent;
import android.view.WindowAnimationFrameStats;
import android.view.WindowContentFrameStats;

public interface IUiAutomationConnection extends IInterface {

    public static class Default implements IUiAutomationConnection {
        public void connect(IAccessibilityServiceClient client, int flags) throws RemoteException {
        }

        public void disconnect() throws RemoteException {
        }

        public boolean injectInputEvent(InputEvent event, boolean sync) throws RemoteException {
            return false;
        }

        public void syncInputTransactions() throws RemoteException {
        }

        public boolean setRotation(int rotation) throws RemoteException {
            return false;
        }

        public Bitmap takeScreenshot(Rect crop, int rotation) throws RemoteException {
            return null;
        }

        public boolean clearWindowContentFrameStats(int windowId) throws RemoteException {
            return false;
        }

        public WindowContentFrameStats getWindowContentFrameStats(int windowId) throws RemoteException {
            return null;
        }

        public void clearWindowAnimationFrameStats() throws RemoteException {
        }

        public WindowAnimationFrameStats getWindowAnimationFrameStats() throws RemoteException {
            return null;
        }

        public void executeShellCommand(String command, ParcelFileDescriptor sink, ParcelFileDescriptor source) throws RemoteException {
        }

        public void grantRuntimePermission(String packageName, String permission, int userId) throws RemoteException {
        }

        public void revokeRuntimePermission(String packageName, String permission, int userId) throws RemoteException {
        }

        public void adoptShellPermissionIdentity(int uid, String[] permissions) throws RemoteException {
        }

        public void dropShellPermissionIdentity() throws RemoteException {
        }

        public void shutdown() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IUiAutomationConnection {
        private static final String DESCRIPTOR = "android.app.IUiAutomationConnection";
        static final int TRANSACTION_adoptShellPermissionIdentity = 14;
        static final int TRANSACTION_clearWindowAnimationFrameStats = 9;
        static final int TRANSACTION_clearWindowContentFrameStats = 7;
        static final int TRANSACTION_connect = 1;
        static final int TRANSACTION_disconnect = 2;
        static final int TRANSACTION_dropShellPermissionIdentity = 15;
        static final int TRANSACTION_executeShellCommand = 11;
        static final int TRANSACTION_getWindowAnimationFrameStats = 10;
        static final int TRANSACTION_getWindowContentFrameStats = 8;
        static final int TRANSACTION_grantRuntimePermission = 12;
        static final int TRANSACTION_injectInputEvent = 3;
        static final int TRANSACTION_revokeRuntimePermission = 13;
        static final int TRANSACTION_setRotation = 5;
        static final int TRANSACTION_shutdown = 16;
        static final int TRANSACTION_syncInputTransactions = 4;
        static final int TRANSACTION_takeScreenshot = 6;

        private static class Proxy implements IUiAutomationConnection {
            public static IUiAutomationConnection sDefaultImpl;
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

            public void connect(IAccessibilityServiceClient client, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    _data.writeInt(flags);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().connect(client, flags);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disconnect() throws RemoteException {
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
                    Stub.getDefaultImpl().disconnect();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean injectInputEvent(InputEvent event, boolean sync) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (event != null) {
                        _data.writeInt(1);
                        event.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(sync ? 1 : 0);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().injectInputEvent(event, sync);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void syncInputTransactions() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().syncInputTransactions();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setRotation(int rotation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(rotation);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().setRotation(rotation);
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

            public Bitmap takeScreenshot(Rect crop, int rotation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (crop != null) {
                        _data.writeInt(1);
                        crop.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(rotation);
                    Bitmap bitmap = this.mRemote;
                    if (!bitmap.transact(6, _data, _reply, 0)) {
                        bitmap = Stub.getDefaultImpl();
                        if (bitmap != null) {
                            bitmap = Stub.getDefaultImpl().takeScreenshot(crop, rotation);
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

            public boolean clearWindowContentFrameStats(int windowId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(windowId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().clearWindowContentFrameStats(windowId);
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

            public WindowContentFrameStats getWindowContentFrameStats(int windowId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(windowId);
                    WindowContentFrameStats windowContentFrameStats = 8;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        windowContentFrameStats = Stub.getDefaultImpl();
                        if (windowContentFrameStats != 0) {
                            windowContentFrameStats = Stub.getDefaultImpl().getWindowContentFrameStats(windowId);
                            return windowContentFrameStats;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        windowContentFrameStats = (WindowContentFrameStats) WindowContentFrameStats.CREATOR.createFromParcel(_reply);
                    } else {
                        windowContentFrameStats = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return windowContentFrameStats;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearWindowAnimationFrameStats() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearWindowAnimationFrameStats();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public WindowAnimationFrameStats getWindowAnimationFrameStats() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    WindowAnimationFrameStats windowAnimationFrameStats = 10;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        windowAnimationFrameStats = Stub.getDefaultImpl();
                        if (windowAnimationFrameStats != 0) {
                            windowAnimationFrameStats = Stub.getDefaultImpl().getWindowAnimationFrameStats();
                            return windowAnimationFrameStats;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        windowAnimationFrameStats = (WindowAnimationFrameStats) WindowAnimationFrameStats.CREATOR.createFromParcel(_reply);
                    } else {
                        windowAnimationFrameStats = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return windowAnimationFrameStats;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void executeShellCommand(String command, ParcelFileDescriptor sink, ParcelFileDescriptor source) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(command);
                    if (sink != null) {
                        _data.writeInt(1);
                        sink.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (source != null) {
                        _data.writeInt(1);
                        source.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().executeShellCommand(command, sink, source);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void grantRuntimePermission(String packageName, String permission, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(permission);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().grantRuntimePermission(packageName, permission, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void revokeRuntimePermission(String packageName, String permission, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(permission);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().revokeRuntimePermission(packageName, permission, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void adoptShellPermissionIdentity(int uid, String[] permissions) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeStringArray(permissions);
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().adoptShellPermissionIdentity(uid, permissions);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void dropShellPermissionIdentity() throws RemoteException {
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
                    Stub.getDefaultImpl().dropShellPermissionIdentity();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void shutdown() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(16, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().shutdown();
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IUiAutomationConnection asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IUiAutomationConnection)) {
                return new Proxy(obj);
            }
            return (IUiAutomationConnection) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "connect";
                case 2:
                    return "disconnect";
                case 3:
                    return "injectInputEvent";
                case 4:
                    return "syncInputTransactions";
                case 5:
                    return "setRotation";
                case 6:
                    return "takeScreenshot";
                case 7:
                    return "clearWindowContentFrameStats";
                case 8:
                    return "getWindowContentFrameStats";
                case 9:
                    return "clearWindowAnimationFrameStats";
                case 10:
                    return "getWindowAnimationFrameStats";
                case 11:
                    return "executeShellCommand";
                case 12:
                    return "grantRuntimePermission";
                case 13:
                    return "revokeRuntimePermission";
                case 14:
                    return "adoptShellPermissionIdentity";
                case 15:
                    return "dropShellPermissionIdentity";
                case 16:
                    return "shutdown";
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
                boolean _result;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        connect(android.accessibilityservice.IAccessibilityServiceClient.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        disconnect();
                        reply.writeNoException();
                        return true;
                    case 3:
                        InputEvent _arg0;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (InputEvent) InputEvent.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        boolean _result2 = injectInputEvent(_arg0, _arg1);
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        syncInputTransactions();
                        reply.writeNoException();
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        _result = setRotation(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 6:
                        Rect _arg02;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (Rect) Rect.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        Bitmap _result3 = takeScreenshot(_arg02, data.readInt());
                        reply.writeNoException();
                        if (_result3 != null) {
                            reply.writeInt(1);
                            _result3.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        _result = clearWindowContentFrameStats(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        WindowContentFrameStats _result4 = getWindowContentFrameStats(data.readInt());
                        reply.writeNoException();
                        if (_result4 != null) {
                            reply.writeInt(1);
                            _result4.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        clearWindowAnimationFrameStats();
                        reply.writeNoException();
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        WindowAnimationFrameStats _result5 = getWindowAnimationFrameStats();
                        reply.writeNoException();
                        if (_result5 != null) {
                            reply.writeInt(1);
                            _result5.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 11:
                        ParcelFileDescriptor _arg12;
                        ParcelFileDescriptor _arg2;
                        data.enforceInterface(descriptor);
                        String _arg03 = data.readString();
                        if (data.readInt() != 0) {
                            _arg12 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(data);
                        } else {
                            _arg12 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg2 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(data);
                        } else {
                            _arg2 = null;
                        }
                        executeShellCommand(_arg03, _arg12, _arg2);
                        reply.writeNoException();
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        grantRuntimePermission(data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        revokeRuntimePermission(data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 14:
                        data.enforceInterface(descriptor);
                        adoptShellPermissionIdentity(data.readInt(), data.createStringArray());
                        reply.writeNoException();
                        return true;
                    case 15:
                        data.enforceInterface(descriptor);
                        dropShellPermissionIdentity();
                        reply.writeNoException();
                        return true;
                    case 16:
                        data.enforceInterface(descriptor);
                        shutdown();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IUiAutomationConnection impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IUiAutomationConnection getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void adoptShellPermissionIdentity(int i, String[] strArr) throws RemoteException;

    void clearWindowAnimationFrameStats() throws RemoteException;

    boolean clearWindowContentFrameStats(int i) throws RemoteException;

    void connect(IAccessibilityServiceClient iAccessibilityServiceClient, int i) throws RemoteException;

    void disconnect() throws RemoteException;

    void dropShellPermissionIdentity() throws RemoteException;

    void executeShellCommand(String str, ParcelFileDescriptor parcelFileDescriptor, ParcelFileDescriptor parcelFileDescriptor2) throws RemoteException;

    WindowAnimationFrameStats getWindowAnimationFrameStats() throws RemoteException;

    WindowContentFrameStats getWindowContentFrameStats(int i) throws RemoteException;

    void grantRuntimePermission(String str, String str2, int i) throws RemoteException;

    boolean injectInputEvent(InputEvent inputEvent, boolean z) throws RemoteException;

    void revokeRuntimePermission(String str, String str2, int i) throws RemoteException;

    boolean setRotation(int i) throws RemoteException;

    void shutdown() throws RemoteException;

    void syncInputTransactions() throws RemoteException;

    Bitmap takeScreenshot(Rect rect, int i) throws RemoteException;
}
