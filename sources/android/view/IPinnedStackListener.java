package android.view;

import android.content.pm.ParceledListSlice;
import android.graphics.Rect;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IPinnedStackListener extends IInterface {

    public static class Default implements IPinnedStackListener {
        public void onListenerRegistered(IPinnedStackController controller) throws RemoteException {
        }

        public void onMovementBoundsChanged(Rect insetBounds, Rect normalBounds, Rect animatingBounds, boolean fromImeAdjustment, boolean fromShelfAdjustment, int displayRotation) throws RemoteException {
        }

        public void onImeVisibilityChanged(boolean imeVisible, int imeHeight) throws RemoteException {
        }

        public void onShelfVisibilityChanged(boolean shelfVisible, int shelfHeight) throws RemoteException {
        }

        public void onMinimizedStateChanged(boolean isMinimized) throws RemoteException {
        }

        public void onActionsChanged(ParceledListSlice actions) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IPinnedStackListener {
        private static final String DESCRIPTOR = "android.view.IPinnedStackListener";
        static final int TRANSACTION_onActionsChanged = 6;
        static final int TRANSACTION_onImeVisibilityChanged = 3;
        static final int TRANSACTION_onListenerRegistered = 1;
        static final int TRANSACTION_onMinimizedStateChanged = 5;
        static final int TRANSACTION_onMovementBoundsChanged = 2;
        static final int TRANSACTION_onShelfVisibilityChanged = 4;

        private static class Proxy implements IPinnedStackListener {
            public static IPinnedStackListener sDefaultImpl;
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

            public void onListenerRegistered(IPinnedStackController controller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(controller != null ? controller.asBinder() : null);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onListenerRegistered(controller);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onMovementBoundsChanged(Rect insetBounds, Rect normalBounds, Rect animatingBounds, boolean fromImeAdjustment, boolean fromShelfAdjustment, int displayRotation) throws RemoteException {
                Throwable th;
                Rect rect = insetBounds;
                Rect rect2 = normalBounds;
                Rect rect3 = animatingBounds;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    if (rect != null) {
                        _data.writeInt(1);
                        insetBounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (rect2 != null) {
                        _data.writeInt(1);
                        rect2.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (rect3 != null) {
                        _data.writeInt(1);
                        rect3.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(fromImeAdjustment ? 1 : 0);
                    if (fromShelfAdjustment) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    try {
                        _data.writeInt(displayRotation);
                    } catch (Throwable th2) {
                        th = th2;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().onMovementBoundsChanged(insetBounds, normalBounds, animatingBounds, fromImeAdjustment, fromShelfAdjustment, displayRotation);
                        _data.recycle();
                    } catch (Throwable th3) {
                        th = th3;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    int i2 = displayRotation;
                    _data.recycle();
                    throw th;
                }
            }

            public void onImeVisibilityChanged(boolean imeVisible, int imeHeight) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(imeVisible ? 1 : 0);
                    _data.writeInt(imeHeight);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onImeVisibilityChanged(imeVisible, imeHeight);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onShelfVisibilityChanged(boolean shelfVisible, int shelfHeight) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(shelfVisible ? 1 : 0);
                    _data.writeInt(shelfHeight);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onShelfVisibilityChanged(shelfVisible, shelfHeight);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onMinimizedStateChanged(boolean isMinimized) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isMinimized ? 1 : 0);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onMinimizedStateChanged(isMinimized);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onActionsChanged(ParceledListSlice actions) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (actions != null) {
                        _data.writeInt(1);
                        actions.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onActionsChanged(actions);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPinnedStackListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IPinnedStackListener)) {
                return new Proxy(obj);
            }
            return (IPinnedStackListener) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "onListenerRegistered";
                case 2:
                    return "onMovementBoundsChanged";
                case 3:
                    return "onImeVisibilityChanged";
                case 4:
                    return "onShelfVisibilityChanged";
                case 5:
                    return "onMinimizedStateChanged";
                case 6:
                    return "onActionsChanged";
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
                boolean _arg0 = false;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        onListenerRegistered(android.view.IPinnedStackController.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 2:
                        Rect _arg02;
                        Rect _arg1;
                        Rect _arg2;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg1 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg1 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg2 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        onMovementBoundsChanged(_arg02, _arg1, _arg2, data.readInt() != 0, data.readInt() != 0, data.readInt());
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        onImeVisibilityChanged(_arg0, data.readInt());
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        onShelfVisibilityChanged(_arg0, data.readInt());
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        onMinimizedStateChanged(_arg0);
                        return true;
                    case 6:
                        ParceledListSlice _arg03;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        onActionsChanged(_arg03);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IPinnedStackListener impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IPinnedStackListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onActionsChanged(ParceledListSlice parceledListSlice) throws RemoteException;

    void onImeVisibilityChanged(boolean z, int i) throws RemoteException;

    void onListenerRegistered(IPinnedStackController iPinnedStackController) throws RemoteException;

    void onMinimizedStateChanged(boolean z) throws RemoteException;

    void onMovementBoundsChanged(Rect rect, Rect rect2, Rect rect3, boolean z, boolean z2, int i) throws RemoteException;

    void onShelfVisibilityChanged(boolean z, int i) throws RemoteException;
}
