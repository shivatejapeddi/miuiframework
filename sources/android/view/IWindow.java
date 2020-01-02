package android.view;

import android.graphics.Rect;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.MergedConfiguration;
import android.view.DisplayCutout.ParcelableWrapper;
import com.android.internal.os.IResultReceiver;

public interface IWindow extends IInterface {

    public static class Default implements IWindow {
        public void executeCommand(String command, String parameters, ParcelFileDescriptor descriptor) throws RemoteException {
        }

        public void resized(Rect frame, Rect overscanInsets, Rect contentInsets, Rect visibleInsets, Rect stableInsets, Rect outsets, boolean reportDraw, MergedConfiguration newMergedConfiguration, Rect backDropFrame, boolean forceLayout, boolean alwaysConsumeSystemBars, int displayId, ParcelableWrapper displayCutout) throws RemoteException {
        }

        public void insetsChanged(InsetsState insetsState) throws RemoteException {
        }

        public void insetsControlChanged(InsetsState insetsState, InsetsSourceControl[] activeControls) throws RemoteException {
        }

        public void moved(int newX, int newY) throws RemoteException {
        }

        public void dispatchAppVisibility(boolean visible) throws RemoteException {
        }

        public void dispatchGetNewSurface() throws RemoteException {
        }

        public void windowFocusChanged(boolean hasFocus, boolean inTouchMode) throws RemoteException {
        }

        public void closeSystemDialogs(String reason) throws RemoteException {
        }

        public void dispatchWallpaperOffsets(float x, float y, float xStep, float yStep, boolean sync) throws RemoteException {
        }

        public void dispatchWallpaperCommand(String action, int x, int y, int z, Bundle extras, boolean sync) throws RemoteException {
        }

        public void dispatchDragEvent(DragEvent event) throws RemoteException {
        }

        public void updatePointerIcon(float x, float y) throws RemoteException {
        }

        public void dispatchSystemUiVisibilityChanged(int seq, int globalVisibility, int localValue, int localChanges) throws RemoteException {
        }

        public void dispatchWindowShown() throws RemoteException {
        }

        public void requestAppKeyboardShortcuts(IResultReceiver receiver, int deviceId) throws RemoteException {
        }

        public void dispatchPointerCaptureChanged(boolean hasCapture) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IWindow {
        private static final String DESCRIPTOR = "android.view.IWindow";
        static final int TRANSACTION_closeSystemDialogs = 9;
        static final int TRANSACTION_dispatchAppVisibility = 6;
        static final int TRANSACTION_dispatchDragEvent = 12;
        static final int TRANSACTION_dispatchGetNewSurface = 7;
        static final int TRANSACTION_dispatchPointerCaptureChanged = 17;
        static final int TRANSACTION_dispatchSystemUiVisibilityChanged = 14;
        static final int TRANSACTION_dispatchWallpaperCommand = 11;
        static final int TRANSACTION_dispatchWallpaperOffsets = 10;
        static final int TRANSACTION_dispatchWindowShown = 15;
        static final int TRANSACTION_executeCommand = 1;
        static final int TRANSACTION_insetsChanged = 3;
        static final int TRANSACTION_insetsControlChanged = 4;
        static final int TRANSACTION_moved = 5;
        static final int TRANSACTION_requestAppKeyboardShortcuts = 16;
        static final int TRANSACTION_resized = 2;
        static final int TRANSACTION_updatePointerIcon = 13;
        static final int TRANSACTION_windowFocusChanged = 8;

        private static class Proxy implements IWindow {
            public static IWindow sDefaultImpl;
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

            public void executeCommand(String command, String parameters, ParcelFileDescriptor descriptor) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(command);
                    _data.writeString(parameters);
                    if (descriptor != null) {
                        _data.writeInt(1);
                        descriptor.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().executeCommand(command, parameters, descriptor);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void resized(Rect frame, Rect overscanInsets, Rect contentInsets, Rect visibleInsets, Rect stableInsets, Rect outsets, boolean reportDraw, MergedConfiguration newMergedConfiguration, Rect backDropFrame, boolean forceLayout, boolean alwaysConsumeSystemBars, int displayId, ParcelableWrapper displayCutout) throws RemoteException {
                Throwable th;
                Rect rect = frame;
                Rect rect2 = overscanInsets;
                Rect rect3 = contentInsets;
                Rect rect4 = visibleInsets;
                Rect rect5 = stableInsets;
                Rect rect6 = outsets;
                MergedConfiguration mergedConfiguration = newMergedConfiguration;
                Rect rect7 = backDropFrame;
                ParcelableWrapper parcelableWrapper = displayCutout;
                Parcel _data = Parcel.obtain();
                Parcel _data2;
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (rect != null) {
                        try {
                            _data.writeInt(1);
                            rect.writeToParcel(_data, 0);
                        } catch (Throwable th2) {
                            th = th2;
                            _data2 = _data;
                        }
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
                    if (rect4 != null) {
                        _data.writeInt(1);
                        rect4.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (rect5 != null) {
                        _data.writeInt(1);
                        rect5.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (rect6 != null) {
                        _data.writeInt(1);
                        rect6.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(reportDraw ? 1 : 0);
                    if (mergedConfiguration != null) {
                        _data.writeInt(1);
                        mergedConfiguration.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (rect7 != null) {
                        _data.writeInt(1);
                        rect7.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(forceLayout ? 1 : 0);
                    _data.writeInt(alwaysConsumeSystemBars ? 1 : 0);
                    _data.writeInt(displayId);
                    if (parcelableWrapper != null) {
                        _data.writeInt(1);
                        parcelableWrapper.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                        return;
                    }
                    _data2 = _data;
                    try {
                        Stub.getDefaultImpl().resized(frame, overscanInsets, contentInsets, visibleInsets, stableInsets, outsets, reportDraw, newMergedConfiguration, backDropFrame, forceLayout, alwaysConsumeSystemBars, displayId, displayCutout);
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

            public void insetsChanged(InsetsState insetsState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (insetsState != null) {
                        _data.writeInt(1);
                        insetsState.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().insetsChanged(insetsState);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void insetsControlChanged(InsetsState insetsState, InsetsSourceControl[] activeControls) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (insetsState != null) {
                        _data.writeInt(1);
                        insetsState.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeTypedArray(activeControls, 0);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().insetsControlChanged(insetsState, activeControls);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void moved(int newX, int newY) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(newX);
                    _data.writeInt(newY);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().moved(newX, newY);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void dispatchAppVisibility(boolean visible) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(visible ? 1 : 0);
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().dispatchAppVisibility(visible);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void dispatchGetNewSurface() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(7, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().dispatchGetNewSurface();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void windowFocusChanged(boolean hasFocus, boolean inTouchMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    _data.writeInt(hasFocus ? 1 : 0);
                    if (inTouchMode) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(8, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().windowFocusChanged(hasFocus, inTouchMode);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void closeSystemDialogs(String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(reason);
                    if (this.mRemote.transact(9, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().closeSystemDialogs(reason);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void dispatchWallpaperOffsets(float x, float y, float xStep, float yStep, boolean sync) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeFloat(x);
                    _data.writeFloat(y);
                    _data.writeFloat(xStep);
                    _data.writeFloat(yStep);
                    _data.writeInt(sync ? 1 : 0);
                    if (this.mRemote.transact(10, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().dispatchWallpaperOffsets(x, y, xStep, yStep, sync);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void dispatchWallpaperCommand(String action, int x, int y, int z, Bundle extras, boolean sync) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                int i3;
                Bundle bundle = extras;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(action);
                    } catch (Throwable th2) {
                        th = th2;
                        i = x;
                        i2 = y;
                        i3 = z;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(x);
                        try {
                            _data.writeInt(y);
                        } catch (Throwable th3) {
                            th = th3;
                            i3 = z;
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeInt(z);
                            int i4 = 0;
                            if (bundle != null) {
                                _data.writeInt(1);
                                bundle.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            if (sync) {
                                i4 = 1;
                            }
                            _data.writeInt(i4);
                        } catch (Throwable th4) {
                            th = th4;
                            _data.recycle();
                            throw th;
                        }
                        try {
                            if (this.mRemote.transact(11, _data, null, 1) || Stub.getDefaultImpl() == null) {
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().dispatchWallpaperCommand(action, x, y, z, extras, sync);
                            _data.recycle();
                        } catch (Throwable th5) {
                            th = th5;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        i2 = y;
                        i3 = z;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    String str = action;
                    i = x;
                    i2 = y;
                    i3 = z;
                    _data.recycle();
                    throw th;
                }
            }

            public void dispatchDragEvent(DragEvent event) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (event != null) {
                        _data.writeInt(1);
                        event.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(12, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().dispatchDragEvent(event);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void updatePointerIcon(float x, float y) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeFloat(x);
                    _data.writeFloat(y);
                    if (this.mRemote.transact(13, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().updatePointerIcon(x, y);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void dispatchSystemUiVisibilityChanged(int seq, int globalVisibility, int localValue, int localChanges) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(seq);
                    _data.writeInt(globalVisibility);
                    _data.writeInt(localValue);
                    _data.writeInt(localChanges);
                    if (this.mRemote.transact(14, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().dispatchSystemUiVisibilityChanged(seq, globalVisibility, localValue, localChanges);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void dispatchWindowShown() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(15, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().dispatchWindowShown();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void requestAppKeyboardShortcuts(IResultReceiver receiver, int deviceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    _data.writeInt(deviceId);
                    if (this.mRemote.transact(16, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().requestAppKeyboardShortcuts(receiver, deviceId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void dispatchPointerCaptureChanged(boolean hasCapture) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(hasCapture ? 1 : 0);
                    if (this.mRemote.transact(17, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().dispatchPointerCaptureChanged(hasCapture);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IWindow asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IWindow)) {
                return new Proxy(obj);
            }
            return (IWindow) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "executeCommand";
                case 2:
                    return "resized";
                case 3:
                    return "insetsChanged";
                case 4:
                    return "insetsControlChanged";
                case 5:
                    return "moved";
                case 6:
                    return "dispatchAppVisibility";
                case 7:
                    return "dispatchGetNewSurface";
                case 8:
                    return "windowFocusChanged";
                case 9:
                    return "closeSystemDialogs";
                case 10:
                    return "dispatchWallpaperOffsets";
                case 11:
                    return "dispatchWallpaperCommand";
                case 12:
                    return "dispatchDragEvent";
                case 13:
                    return "updatePointerIcon";
                case 14:
                    return "dispatchSystemUiVisibilityChanged";
                case 15:
                    return "dispatchWindowShown";
                case 16:
                    return "requestAppKeyboardShortcuts";
                case 17:
                    return "dispatchPointerCaptureChanged";
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
                Parcel parcel2;
                InsetsState _arg0;
                switch (i) {
                    case 1:
                        ParcelFileDescriptor _arg2;
                        parcel2 = parcel;
                        parcel2.enforceInterface(descriptor);
                        String _arg02 = data.readString();
                        String _arg12 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(parcel2);
                        } else {
                            _arg2 = null;
                        }
                        executeCommand(_arg02, _arg12, _arg2);
                        return true;
                    case 2:
                        Rect _arg03;
                        Rect _arg13;
                        Rect _arg22;
                        Rect _arg3;
                        Rect _arg4;
                        Rect _arg5;
                        MergedConfiguration _arg7;
                        Rect _arg8;
                        ParcelableWrapper _arg122;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg13 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg22 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg22 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg3 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg4 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg4 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg5 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg5 = null;
                        }
                        boolean _arg6 = data.readInt() != 0;
                        if (data.readInt() != 0) {
                            _arg7 = (MergedConfiguration) MergedConfiguration.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg7 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg8 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg8 = null;
                        }
                        boolean _arg9 = data.readInt() != 0;
                        boolean _arg10 = data.readInt() != 0;
                        int _arg11 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg122 = (ParcelableWrapper) ParcelableWrapper.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg122 = null;
                        }
                        parcel2 = parcel;
                        resized(_arg03, _arg13, _arg22, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8, _arg9, _arg10, _arg11, _arg122);
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (InsetsState) InsetsState.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        insetsChanged(_arg0);
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (InsetsState) InsetsState.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        insetsControlChanged(_arg0, (InsetsSourceControl[]) parcel.createTypedArray(InsetsSourceControl.CREATOR));
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        moved(data.readInt(), data.readInt());
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        dispatchAppVisibility(_arg1);
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        dispatchGetNewSurface();
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        boolean _arg04 = data.readInt() != 0;
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        windowFocusChanged(_arg04, _arg1);
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        closeSystemDialogs(data.readString());
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        dispatchWallpaperOffsets(data.readFloat(), data.readFloat(), data.readFloat(), data.readFloat(), data.readInt() != 0);
                        return true;
                    case 11:
                        Bundle _arg42;
                        parcel.enforceInterface(descriptor);
                        String _arg05 = data.readString();
                        int _arg14 = data.readInt();
                        int _arg23 = data.readInt();
                        int _arg32 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg42 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg42 = null;
                        }
                        dispatchWallpaperCommand(_arg05, _arg14, _arg23, _arg32, _arg42, data.readInt() != 0);
                        return true;
                    case 12:
                        DragEvent _arg06;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg06 = (DragEvent) DragEvent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg06 = null;
                        }
                        dispatchDragEvent(_arg06);
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        updatePointerIcon(data.readFloat(), data.readFloat());
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        dispatchSystemUiVisibilityChanged(data.readInt(), data.readInt(), data.readInt(), data.readInt());
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        dispatchWindowShown();
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        requestAppKeyboardShortcuts(com.android.internal.os.IResultReceiver.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        dispatchPointerCaptureChanged(_arg1);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IWindow impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IWindow getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void closeSystemDialogs(String str) throws RemoteException;

    void dispatchAppVisibility(boolean z) throws RemoteException;

    void dispatchDragEvent(DragEvent dragEvent) throws RemoteException;

    void dispatchGetNewSurface() throws RemoteException;

    void dispatchPointerCaptureChanged(boolean z) throws RemoteException;

    void dispatchSystemUiVisibilityChanged(int i, int i2, int i3, int i4) throws RemoteException;

    void dispatchWallpaperCommand(String str, int i, int i2, int i3, Bundle bundle, boolean z) throws RemoteException;

    void dispatchWallpaperOffsets(float f, float f2, float f3, float f4, boolean z) throws RemoteException;

    void dispatchWindowShown() throws RemoteException;

    void executeCommand(String str, String str2, ParcelFileDescriptor parcelFileDescriptor) throws RemoteException;

    void insetsChanged(InsetsState insetsState) throws RemoteException;

    void insetsControlChanged(InsetsState insetsState, InsetsSourceControl[] insetsSourceControlArr) throws RemoteException;

    void moved(int i, int i2) throws RemoteException;

    void requestAppKeyboardShortcuts(IResultReceiver iResultReceiver, int i) throws RemoteException;

    void resized(Rect rect, Rect rect2, Rect rect3, Rect rect4, Rect rect5, Rect rect6, boolean z, MergedConfiguration mergedConfiguration, Rect rect7, boolean z2, boolean z3, int i, ParcelableWrapper parcelableWrapper) throws RemoteException;

    void updatePointerIcon(float f, float f2) throws RemoteException;

    void windowFocusChanged(boolean z, boolean z2) throws RemoteException;
}
