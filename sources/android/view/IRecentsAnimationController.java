package android.view;

import android.annotation.UnsupportedAppUsage;
import android.app.ActivityManager.TaskSnapshot;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IRecentsAnimationController extends IInterface {

    public static class Default implements IRecentsAnimationController {
        public TaskSnapshot screenshotTask(int taskId) throws RemoteException {
            return null;
        }

        public void finish(boolean moveHomeToTop, boolean sendUserLeaveHint) throws RemoteException {
        }

        public void setInputConsumerEnabled(boolean enabled) throws RemoteException {
        }

        public void setAnimationTargetsBehindSystemBars(boolean behindSystemBars) throws RemoteException {
        }

        public void setSplitScreenMinimized(boolean minimized) throws RemoteException {
        }

        public void hideCurrentInputMethod() throws RemoteException {
        }

        public void setCancelWithDeferredScreenshot(boolean screenshot) throws RemoteException {
        }

        public void cleanupScreenshot() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IRecentsAnimationController {
        private static final String DESCRIPTOR = "android.view.IRecentsAnimationController";
        static final int TRANSACTION_cleanupScreenshot = 8;
        static final int TRANSACTION_finish = 2;
        static final int TRANSACTION_hideCurrentInputMethod = 6;
        static final int TRANSACTION_screenshotTask = 1;
        static final int TRANSACTION_setAnimationTargetsBehindSystemBars = 4;
        static final int TRANSACTION_setCancelWithDeferredScreenshot = 7;
        static final int TRANSACTION_setInputConsumerEnabled = 3;
        static final int TRANSACTION_setSplitScreenMinimized = 5;

        private static class Proxy implements IRecentsAnimationController {
            public static IRecentsAnimationController sDefaultImpl;
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

            public TaskSnapshot screenshotTask(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    TaskSnapshot taskSnapshot = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        taskSnapshot = Stub.getDefaultImpl();
                        if (taskSnapshot != 0) {
                            taskSnapshot = Stub.getDefaultImpl().screenshotTask(taskId);
                            return taskSnapshot;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        taskSnapshot = (TaskSnapshot) TaskSnapshot.CREATOR.createFromParcel(_reply);
                    } else {
                        taskSnapshot = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return taskSnapshot;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void finish(boolean moveHomeToTop, boolean sendUserLeaveHint) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    _data.writeInt(moveHomeToTop ? 1 : 0);
                    if (!sendUserLeaveHint) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().finish(moveHomeToTop, sendUserLeaveHint);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setInputConsumerEnabled(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setInputConsumerEnabled(enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAnimationTargetsBehindSystemBars(boolean behindSystemBars) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(behindSystemBars ? 1 : 0);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAnimationTargetsBehindSystemBars(behindSystemBars);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setSplitScreenMinimized(boolean minimized) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(minimized ? 1 : 0);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setSplitScreenMinimized(minimized);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void hideCurrentInputMethod() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().hideCurrentInputMethod();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setCancelWithDeferredScreenshot(boolean screenshot) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(screenshot ? 1 : 0);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setCancelWithDeferredScreenshot(screenshot);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cleanupScreenshot() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().cleanupScreenshot();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IRecentsAnimationController asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IRecentsAnimationController)) {
                return new Proxy(obj);
            }
            return (IRecentsAnimationController) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "screenshotTask";
                case 2:
                    return "finish";
                case 3:
                    return "setInputConsumerEnabled";
                case 4:
                    return "setAnimationTargetsBehindSystemBars";
                case 5:
                    return "setSplitScreenMinimized";
                case 6:
                    return "hideCurrentInputMethod";
                case 7:
                    return "setCancelWithDeferredScreenshot";
                case 8:
                    return "cleanupScreenshot";
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
                        TaskSnapshot _result = screenshotTask(data.readInt());
                        reply.writeNoException();
                        if (_result != null) {
                            reply.writeInt(1);
                            _result.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        boolean _arg02 = data.readInt() != 0;
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        finish(_arg02, _arg0);
                        reply.writeNoException();
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        setInputConsumerEnabled(_arg0);
                        reply.writeNoException();
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        setAnimationTargetsBehindSystemBars(_arg0);
                        reply.writeNoException();
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        setSplitScreenMinimized(_arg0);
                        reply.writeNoException();
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        hideCurrentInputMethod();
                        reply.writeNoException();
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        setCancelWithDeferredScreenshot(_arg0);
                        reply.writeNoException();
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        cleanupScreenshot();
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IRecentsAnimationController impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IRecentsAnimationController getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void cleanupScreenshot() throws RemoteException;

    @UnsupportedAppUsage
    void finish(boolean z, boolean z2) throws RemoteException;

    void hideCurrentInputMethod() throws RemoteException;

    @UnsupportedAppUsage
    TaskSnapshot screenshotTask(int i) throws RemoteException;

    @UnsupportedAppUsage
    void setAnimationTargetsBehindSystemBars(boolean z) throws RemoteException;

    void setCancelWithDeferredScreenshot(boolean z) throws RemoteException;

    @UnsupportedAppUsage
    void setInputConsumerEnabled(boolean z) throws RemoteException;

    void setSplitScreenMinimized(boolean z) throws RemoteException;
}
