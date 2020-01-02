package android.view.accessibility;

import android.graphics.Region;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.MagnificationSpec;

public interface IAccessibilityInteractionConnection extends IInterface {

    public static abstract class Stub extends Binder implements IAccessibilityInteractionConnection {
        private static final String DESCRIPTOR = "android.view.accessibility.IAccessibilityInteractionConnection";
        static final int TRANSACTION_clearAccessibilityFocus = 7;
        static final int TRANSACTION_findAccessibilityNodeInfoByAccessibilityId = 1;
        static final int TRANSACTION_findAccessibilityNodeInfosByText = 3;
        static final int TRANSACTION_findAccessibilityNodeInfosByViewId = 2;
        static final int TRANSACTION_findFocus = 4;
        static final int TRANSACTION_focusSearch = 5;
        static final int TRANSACTION_notifyOutsideTouch = 8;
        static final int TRANSACTION_performAccessibilityAction = 6;

        private static class Proxy implements IAccessibilityInteractionConnection {
            public static IAccessibilityInteractionConnection sDefaultImpl;
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

            public void findAccessibilityNodeInfoByAccessibilityId(long accessibilityNodeId, Region bounds, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, MagnificationSpec spec, Bundle arguments) throws RemoteException {
                Throwable th;
                Region region = bounds;
                MagnificationSpec magnificationSpec = spec;
                Bundle bundle = arguments;
                Parcel _data = Parcel.obtain();
                Parcel _data2;
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(accessibilityNodeId);
                    if (region != null) {
                        try {
                            _data.writeInt(1);
                            region.writeToParcel(_data, 0);
                        } catch (Throwable th2) {
                            th = th2;
                            _data2 = _data;
                        }
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(interactionId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeInt(flags);
                    _data.writeInt(interrogatingPid);
                    _data.writeLong(interrogatingTid);
                    if (magnificationSpec != null) {
                        _data.writeInt(1);
                        magnificationSpec.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (bundle != null) {
                        _data.writeInt(1);
                        bundle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                        return;
                    }
                    _data2 = _data;
                    try {
                        Stub.getDefaultImpl().findAccessibilityNodeInfoByAccessibilityId(accessibilityNodeId, bounds, interactionId, callback, flags, interrogatingPid, interrogatingTid, spec, arguments);
                        _data2.recycle();
                    } catch (Throwable th3) {
                        th = th3;
                        _data2.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    _data2 = _data;
                    _data2.recycle();
                    throw th;
                }
            }

            public void findAccessibilityNodeInfosByViewId(long accessibilityNodeId, String viewId, Region bounds, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, MagnificationSpec spec) throws RemoteException {
                Region region = bounds;
                MagnificationSpec magnificationSpec = spec;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(accessibilityNodeId);
                    _data.writeString(viewId);
                    if (region != null) {
                        _data.writeInt(1);
                        region.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(interactionId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeInt(flags);
                    _data.writeInt(interrogatingPid);
                    _data.writeLong(interrogatingTid);
                    if (magnificationSpec != null) {
                        _data.writeInt(1);
                        magnificationSpec.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().findAccessibilityNodeInfosByViewId(accessibilityNodeId, viewId, bounds, interactionId, callback, flags, interrogatingPid, interrogatingTid, spec);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void findAccessibilityNodeInfosByText(long accessibilityNodeId, String text, Region bounds, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, MagnificationSpec spec) throws RemoteException {
                Region region = bounds;
                MagnificationSpec magnificationSpec = spec;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(accessibilityNodeId);
                    _data.writeString(text);
                    if (region != null) {
                        _data.writeInt(1);
                        region.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(interactionId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeInt(flags);
                    _data.writeInt(interrogatingPid);
                    _data.writeLong(interrogatingTid);
                    if (magnificationSpec != null) {
                        _data.writeInt(1);
                        magnificationSpec.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().findAccessibilityNodeInfosByText(accessibilityNodeId, text, bounds, interactionId, callback, flags, interrogatingPid, interrogatingTid, spec);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void findFocus(long accessibilityNodeId, int focusType, Region bounds, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, MagnificationSpec spec) throws RemoteException {
                Region region = bounds;
                MagnificationSpec magnificationSpec = spec;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(accessibilityNodeId);
                    _data.writeInt(focusType);
                    if (region != null) {
                        _data.writeInt(1);
                        region.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(interactionId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeInt(flags);
                    _data.writeInt(interrogatingPid);
                    _data.writeLong(interrogatingTid);
                    if (magnificationSpec != null) {
                        _data.writeInt(1);
                        magnificationSpec.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().findFocus(accessibilityNodeId, focusType, bounds, interactionId, callback, flags, interrogatingPid, interrogatingTid, spec);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void focusSearch(long accessibilityNodeId, int direction, Region bounds, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, MagnificationSpec spec) throws RemoteException {
                Region region = bounds;
                MagnificationSpec magnificationSpec = spec;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(accessibilityNodeId);
                    _data.writeInt(direction);
                    if (region != null) {
                        _data.writeInt(1);
                        region.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(interactionId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeInt(flags);
                    _data.writeInt(interrogatingPid);
                    _data.writeLong(interrogatingTid);
                    if (magnificationSpec != null) {
                        _data.writeInt(1);
                        magnificationSpec.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().focusSearch(accessibilityNodeId, direction, bounds, interactionId, callback, flags, interrogatingPid, interrogatingTid, spec);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void performAccessibilityAction(long accessibilityNodeId, int action, Bundle arguments, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid) throws RemoteException {
                Throwable th;
                Bundle bundle = arguments;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeLong(accessibilityNodeId);
                        _data.writeInt(action);
                        if (bundle != null) {
                            _data.writeInt(1);
                            bundle.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        _data.writeInt(interactionId);
                        _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                        _data.writeInt(flags);
                        _data.writeInt(interrogatingPid);
                        _data.writeLong(interrogatingTid);
                        if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().performAccessibilityAction(accessibilityNodeId, action, arguments, interactionId, callback, flags, interrogatingPid, interrogatingTid);
                        _data.recycle();
                    } catch (Throwable th2) {
                        th = th2;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    long j = accessibilityNodeId;
                    _data.recycle();
                    throw th;
                }
            }

            public void clearAccessibilityFocus() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(7, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().clearAccessibilityFocus();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void notifyOutsideTouch() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(8, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().notifyOutsideTouch();
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAccessibilityInteractionConnection asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IAccessibilityInteractionConnection)) {
                return new Proxy(obj);
            }
            return (IAccessibilityInteractionConnection) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "findAccessibilityNodeInfoByAccessibilityId";
                case 2:
                    return "findAccessibilityNodeInfosByViewId";
                case 3:
                    return "findAccessibilityNodeInfosByText";
                case 4:
                    return "findFocus";
                case 5:
                    return "focusSearch";
                case 6:
                    return "performAccessibilityAction";
                case 7:
                    return "clearAccessibilityFocus";
                case 8:
                    return "notifyOutsideTouch";
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
                long _arg0;
                int _arg5;
                String _arg1;
                Region _arg2;
                int _arg3;
                IAccessibilityInteractionConnectionCallback _arg4;
                int _arg6;
                long _arg7;
                MagnificationSpec _arg8;
                int _arg12;
                switch (i) {
                    case 1:
                        Region _arg13;
                        MagnificationSpec _arg72;
                        Bundle _arg82;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readLong();
                        if (data.readInt() != 0) {
                            _arg13 = (Region) Region.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        int _arg22 = data.readInt();
                        IAccessibilityInteractionConnectionCallback _arg32 = android.view.accessibility.IAccessibilityInteractionConnectionCallback.Stub.asInterface(data.readStrongBinder());
                        int _arg42 = data.readInt();
                        _arg5 = data.readInt();
                        long _arg62 = data.readLong();
                        if (data.readInt() != 0) {
                            _arg72 = (MagnificationSpec) MagnificationSpec.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg72 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg82 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg82 = null;
                        }
                        findAccessibilityNodeInfoByAccessibilityId(_arg0, _arg13, _arg22, _arg32, _arg42, _arg5, _arg62, _arg72, _arg82);
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readLong();
                        _arg1 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = (Region) Region.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        _arg3 = data.readInt();
                        _arg4 = android.view.accessibility.IAccessibilityInteractionConnectionCallback.Stub.asInterface(data.readStrongBinder());
                        _arg5 = data.readInt();
                        _arg6 = data.readInt();
                        _arg7 = data.readLong();
                        if (data.readInt() != 0) {
                            _arg8 = (MagnificationSpec) MagnificationSpec.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg8 = null;
                        }
                        findAccessibilityNodeInfosByViewId(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8);
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readLong();
                        _arg1 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = (Region) Region.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        _arg3 = data.readInt();
                        _arg4 = android.view.accessibility.IAccessibilityInteractionConnectionCallback.Stub.asInterface(data.readStrongBinder());
                        _arg5 = data.readInt();
                        _arg6 = data.readInt();
                        _arg7 = data.readLong();
                        if (data.readInt() != 0) {
                            _arg8 = (MagnificationSpec) MagnificationSpec.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg8 = null;
                        }
                        findAccessibilityNodeInfosByText(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8);
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readLong();
                        _arg12 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = (Region) Region.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        _arg3 = data.readInt();
                        _arg4 = android.view.accessibility.IAccessibilityInteractionConnectionCallback.Stub.asInterface(data.readStrongBinder());
                        _arg5 = data.readInt();
                        _arg6 = data.readInt();
                        _arg7 = data.readLong();
                        if (data.readInt() != 0) {
                            _arg8 = (MagnificationSpec) MagnificationSpec.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg8 = null;
                        }
                        findFocus(_arg0, _arg12, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8);
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readLong();
                        _arg12 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = (Region) Region.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        _arg3 = data.readInt();
                        _arg4 = android.view.accessibility.IAccessibilityInteractionConnectionCallback.Stub.asInterface(data.readStrongBinder());
                        _arg5 = data.readInt();
                        _arg6 = data.readInt();
                        _arg7 = data.readLong();
                        if (data.readInt() != 0) {
                            _arg8 = (MagnificationSpec) MagnificationSpec.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg8 = null;
                        }
                        focusSearch(_arg0, _arg12, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8);
                        return true;
                    case 6:
                        Bundle _arg23;
                        parcel.enforceInterface(descriptor);
                        long _arg02 = data.readLong();
                        _arg12 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg23 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg23 = null;
                        }
                        performAccessibilityAction(_arg02, _arg12, _arg23, data.readInt(), android.view.accessibility.IAccessibilityInteractionConnectionCallback.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readInt(), data.readLong());
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        clearAccessibilityFocus();
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        notifyOutsideTouch();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IAccessibilityInteractionConnection impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IAccessibilityInteractionConnection getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    public static class Default implements IAccessibilityInteractionConnection {
        public void findAccessibilityNodeInfoByAccessibilityId(long accessibilityNodeId, Region bounds, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, MagnificationSpec spec, Bundle arguments) throws RemoteException {
        }

        public void findAccessibilityNodeInfosByViewId(long accessibilityNodeId, String viewId, Region bounds, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, MagnificationSpec spec) throws RemoteException {
        }

        public void findAccessibilityNodeInfosByText(long accessibilityNodeId, String text, Region bounds, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, MagnificationSpec spec) throws RemoteException {
        }

        public void findFocus(long accessibilityNodeId, int focusType, Region bounds, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, MagnificationSpec spec) throws RemoteException {
        }

        public void focusSearch(long accessibilityNodeId, int direction, Region bounds, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, MagnificationSpec spec) throws RemoteException {
        }

        public void performAccessibilityAction(long accessibilityNodeId, int action, Bundle arguments, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid) throws RemoteException {
        }

        public void clearAccessibilityFocus() throws RemoteException {
        }

        public void notifyOutsideTouch() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    void clearAccessibilityFocus() throws RemoteException;

    void findAccessibilityNodeInfoByAccessibilityId(long j, Region region, int i, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i2, int i3, long j2, MagnificationSpec magnificationSpec, Bundle bundle) throws RemoteException;

    void findAccessibilityNodeInfosByText(long j, String str, Region region, int i, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i2, int i3, long j2, MagnificationSpec magnificationSpec) throws RemoteException;

    void findAccessibilityNodeInfosByViewId(long j, String str, Region region, int i, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i2, int i3, long j2, MagnificationSpec magnificationSpec) throws RemoteException;

    void findFocus(long j, int i, Region region, int i2, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i3, int i4, long j2, MagnificationSpec magnificationSpec) throws RemoteException;

    void focusSearch(long j, int i, Region region, int i2, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i3, int i4, long j2, MagnificationSpec magnificationSpec) throws RemoteException;

    void notifyOutsideTouch() throws RemoteException;

    void performAccessibilityAction(long j, int i, Bundle bundle, int i2, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i3, int i4, long j2) throws RemoteException;
}
