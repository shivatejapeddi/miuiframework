package android.view;

import android.annotation.UnsupportedAppUsage;
import android.content.ClipData;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.MergedConfiguration;
import android.view.DisplayCutout.ParcelableWrapper;
import android.view.WindowManager.LayoutParams;
import java.util.List;

public interface IWindowSession extends IInterface {

    public static class Default implements IWindowSession {
        public int addToDisplay(IWindow window, int seq, LayoutParams attrs, int viewVisibility, int layerStackId, Rect outFrame, Rect outContentInsets, Rect outStableInsets, Rect outOutsets, ParcelableWrapper displayCutout, InputChannel outInputChannel, InsetsState insetsState) throws RemoteException {
            return 0;
        }

        public int addToDisplayWithoutInputChannel(IWindow window, int seq, LayoutParams attrs, int viewVisibility, int layerStackId, Rect outContentInsets, Rect outStableInsets, InsetsState insetsState) throws RemoteException {
            return 0;
        }

        public void remove(IWindow window) throws RemoteException {
        }

        public int relayout(IWindow window, int seq, LayoutParams attrs, int requestedWidth, int requestedHeight, int viewVisibility, int flags, long frameNumber, Rect outFrame, Rect outOverscanInsets, Rect outContentInsets, Rect outVisibleInsets, Rect outStableInsets, Rect outOutsets, Rect outBackdropFrame, ParcelableWrapper displayCutout, MergedConfiguration outMergedConfiguration, SurfaceControl outSurfaceControl, InsetsState insetsState) throws RemoteException {
            return 0;
        }

        public void prepareToReplaceWindows(IBinder appToken, boolean childrenOnly) throws RemoteException {
        }

        public boolean outOfMemory(IWindow window) throws RemoteException {
            return false;
        }

        public void setTransparentRegion(IWindow window, Region region) throws RemoteException {
        }

        public void setInsets(IWindow window, int touchableInsets, Rect contentInsets, Rect visibleInsets, Region touchableRegion) throws RemoteException {
        }

        public void getDisplayFrame(IWindow window, Rect outDisplayFrame) throws RemoteException {
        }

        public void finishDrawing(IWindow window) throws RemoteException {
        }

        public void setInTouchMode(boolean showFocus) throws RemoteException {
        }

        public boolean getInTouchMode() throws RemoteException {
            return false;
        }

        public boolean performHapticFeedback(int effectId, boolean always) throws RemoteException {
            return false;
        }

        public IBinder performDrag(IWindow window, int flags, SurfaceControl surface, int touchSource, float touchX, float touchY, float thumbCenterX, float thumbCenterY, ClipData data) throws RemoteException {
            return null;
        }

        public void reportDropResult(IWindow window, boolean consumed) throws RemoteException {
        }

        public void cancelDragAndDrop(IBinder dragToken, boolean skipAnimation) throws RemoteException {
        }

        public void dragRecipientEntered(IWindow window) throws RemoteException {
        }

        public void dragRecipientExited(IWindow window) throws RemoteException {
        }

        public void setWallpaperPosition(IBinder windowToken, float x, float y, float xstep, float ystep) throws RemoteException {
        }

        public void wallpaperOffsetsComplete(IBinder window) throws RemoteException {
        }

        public void setWallpaperDisplayOffset(IBinder windowToken, int x, int y) throws RemoteException {
        }

        public Bundle sendWallpaperCommand(IBinder window, String action, int x, int y, int z, Bundle extras, boolean sync) throws RemoteException {
            return null;
        }

        public void wallpaperCommandComplete(IBinder window, Bundle result) throws RemoteException {
        }

        public void onRectangleOnScreenRequested(IBinder token, Rect rectangle) throws RemoteException {
        }

        public IWindowId getWindowId(IBinder window) throws RemoteException {
            return null;
        }

        public void pokeDrawLock(IBinder window) throws RemoteException {
        }

        public boolean startMovingTask(IWindow window, float startX, float startY) throws RemoteException {
            return false;
        }

        public void finishMovingTask(IWindow window) throws RemoteException {
        }

        public void updatePointerIcon(IWindow window) throws RemoteException {
        }

        public void reparentDisplayContent(IWindow window, SurfaceControl sc, int displayId) throws RemoteException {
        }

        public void updateDisplayContentLocation(IWindow window, int x, int y, int displayId) throws RemoteException {
        }

        public void updateTapExcludeRegion(IWindow window, int regionId, Region region) throws RemoteException {
        }

        public void insetsModified(IWindow window, InsetsState state) throws RemoteException {
        }

        public void reportSystemGestureExclusionChanged(IWindow window, List<Rect> list) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IWindowSession {
        private static final String DESCRIPTOR = "android.view.IWindowSession";
        static final int TRANSACTION_addToDisplay = 1;
        static final int TRANSACTION_addToDisplayWithoutInputChannel = 2;
        static final int TRANSACTION_cancelDragAndDrop = 16;
        static final int TRANSACTION_dragRecipientEntered = 17;
        static final int TRANSACTION_dragRecipientExited = 18;
        static final int TRANSACTION_finishDrawing = 10;
        static final int TRANSACTION_finishMovingTask = 28;
        static final int TRANSACTION_getDisplayFrame = 9;
        static final int TRANSACTION_getInTouchMode = 12;
        static final int TRANSACTION_getWindowId = 25;
        static final int TRANSACTION_insetsModified = 33;
        static final int TRANSACTION_onRectangleOnScreenRequested = 24;
        static final int TRANSACTION_outOfMemory = 6;
        static final int TRANSACTION_performDrag = 14;
        static final int TRANSACTION_performHapticFeedback = 13;
        static final int TRANSACTION_pokeDrawLock = 26;
        static final int TRANSACTION_prepareToReplaceWindows = 5;
        static final int TRANSACTION_relayout = 4;
        static final int TRANSACTION_remove = 3;
        static final int TRANSACTION_reparentDisplayContent = 30;
        static final int TRANSACTION_reportDropResult = 15;
        static final int TRANSACTION_reportSystemGestureExclusionChanged = 34;
        static final int TRANSACTION_sendWallpaperCommand = 22;
        static final int TRANSACTION_setInTouchMode = 11;
        static final int TRANSACTION_setInsets = 8;
        static final int TRANSACTION_setTransparentRegion = 7;
        static final int TRANSACTION_setWallpaperDisplayOffset = 21;
        static final int TRANSACTION_setWallpaperPosition = 19;
        static final int TRANSACTION_startMovingTask = 27;
        static final int TRANSACTION_updateDisplayContentLocation = 31;
        static final int TRANSACTION_updatePointerIcon = 29;
        static final int TRANSACTION_updateTapExcludeRegion = 32;
        static final int TRANSACTION_wallpaperCommandComplete = 23;
        static final int TRANSACTION_wallpaperOffsetsComplete = 20;

        private static class Proxy implements IWindowSession {
            public static IWindowSession sDefaultImpl;
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

            public int addToDisplay(IWindow window, int seq, LayoutParams attrs, int viewVisibility, int layerStackId, Rect outFrame, Rect outContentInsets, Rect outStableInsets, Rect outOutsets, ParcelableWrapper displayCutout, InputChannel outInputChannel, InsetsState insetsState) throws RemoteException {
                Throwable th;
                Rect rect;
                Rect rect2;
                Rect rect3;
                Rect rect4;
                ParcelableWrapper parcelableWrapper;
                InputChannel inputChannel;
                InsetsState insetsState2;
                Parcel _reply;
                LayoutParams layoutParams = attrs;
                Parcel _data = Parcel.obtain();
                Parcel _reply2 = Parcel.obtain();
                try {
                    IBinder asBinder;
                    Parcel _reply3;
                    int addToDisplay;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (window != null) {
                        try {
                            asBinder = window.asBinder();
                        } catch (Throwable th2) {
                            th = th2;
                            rect = outFrame;
                            rect2 = outContentInsets;
                            rect3 = outStableInsets;
                            rect4 = outOutsets;
                            parcelableWrapper = displayCutout;
                            inputChannel = outInputChannel;
                            insetsState2 = insetsState;
                            _reply = _reply2;
                        }
                    } else {
                        asBinder = null;
                    }
                    _data.writeStrongBinder(asBinder);
                    _data.writeInt(seq);
                    if (layoutParams != null) {
                        _data.writeInt(1);
                        layoutParams.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(viewVisibility);
                    _data.writeInt(layerStackId);
                    if (!this.mRemote.transact(1, _data, _reply2, 0)) {
                        try {
                            if (Stub.getDefaultImpl() != null) {
                                _reply3 = _reply2;
                                try {
                                    addToDisplay = Stub.getDefaultImpl().addToDisplay(window, seq, attrs, viewVisibility, layerStackId, outFrame, outContentInsets, outStableInsets, outOutsets, displayCutout, outInputChannel, insetsState);
                                    _reply3.recycle();
                                    _data.recycle();
                                    return addToDisplay;
                                } catch (Throwable th3) {
                                    th = th3;
                                    rect = outFrame;
                                    rect2 = outContentInsets;
                                    rect3 = outStableInsets;
                                    rect4 = outOutsets;
                                    parcelableWrapper = displayCutout;
                                    inputChannel = outInputChannel;
                                    insetsState2 = insetsState;
                                    _reply = _reply3;
                                    _reply.recycle();
                                    _data.recycle();
                                    throw th;
                                }
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            rect = outFrame;
                            rect2 = outContentInsets;
                            rect3 = outStableInsets;
                            rect4 = outOutsets;
                            parcelableWrapper = displayCutout;
                            inputChannel = outInputChannel;
                            insetsState2 = insetsState;
                            _reply = _reply2;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    }
                    _reply3 = _reply2;
                    try {
                        _reply3.readException();
                        addToDisplay = _reply3.readInt();
                        if (_reply3.readInt() != 0) {
                            _reply = _reply3;
                            try {
                                outFrame.readFromParcel(_reply);
                            } catch (Throwable th5) {
                                th = th5;
                                rect2 = outContentInsets;
                                rect3 = outStableInsets;
                                rect4 = outOutsets;
                                parcelableWrapper = displayCutout;
                                inputChannel = outInputChannel;
                                insetsState2 = insetsState;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        }
                        rect = outFrame;
                        _reply = _reply3;
                        if (_reply.readInt() != 0) {
                            try {
                                outContentInsets.readFromParcel(_reply);
                            } catch (Throwable th6) {
                                th = th6;
                                rect3 = outStableInsets;
                                rect4 = outOutsets;
                                parcelableWrapper = displayCutout;
                                inputChannel = outInputChannel;
                                insetsState2 = insetsState;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        }
                        rect2 = outContentInsets;
                        if (_reply.readInt() != 0) {
                            try {
                                outStableInsets.readFromParcel(_reply);
                            } catch (Throwable th7) {
                                th = th7;
                                rect4 = outOutsets;
                                parcelableWrapper = displayCutout;
                                inputChannel = outInputChannel;
                                insetsState2 = insetsState;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        }
                        rect3 = outStableInsets;
                        if (_reply.readInt() != 0) {
                            try {
                                outOutsets.readFromParcel(_reply);
                            } catch (Throwable th8) {
                                th = th8;
                                parcelableWrapper = displayCutout;
                                inputChannel = outInputChannel;
                                insetsState2 = insetsState;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        }
                        rect4 = outOutsets;
                        if (_reply.readInt() != 0) {
                            try {
                                displayCutout.readFromParcel(_reply);
                            } catch (Throwable th9) {
                                th = th9;
                                inputChannel = outInputChannel;
                                insetsState2 = insetsState;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        }
                        parcelableWrapper = displayCutout;
                        if (_reply.readInt() != 0) {
                            try {
                                outInputChannel.readFromParcel(_reply);
                            } catch (Throwable th10) {
                                th = th10;
                                insetsState2 = insetsState;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        }
                        inputChannel = outInputChannel;
                        if (_reply.readInt() != 0) {
                            try {
                                insetsState.readFromParcel(_reply);
                            } catch (Throwable th11) {
                                th = th11;
                            }
                        } else {
                            insetsState2 = insetsState;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return addToDisplay;
                    } catch (Throwable th12) {
                        th = th12;
                        rect = outFrame;
                        rect2 = outContentInsets;
                        rect3 = outStableInsets;
                        rect4 = outOutsets;
                        parcelableWrapper = displayCutout;
                        inputChannel = outInputChannel;
                        insetsState2 = insetsState;
                        _reply = _reply3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th13) {
                    th = th13;
                    rect = outFrame;
                    rect2 = outContentInsets;
                    rect3 = outStableInsets;
                    rect4 = outOutsets;
                    parcelableWrapper = displayCutout;
                    inputChannel = outInputChannel;
                    insetsState2 = insetsState;
                    _reply = _reply2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public int addToDisplayWithoutInputChannel(IWindow window, int seq, LayoutParams attrs, int viewVisibility, int layerStackId, Rect outContentInsets, Rect outStableInsets, InsetsState insetsState) throws RemoteException {
                Throwable th;
                int i;
                Rect rect;
                Rect rect2;
                InsetsState insetsState2;
                int i2;
                LayoutParams layoutParams = attrs;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                    try {
                        _data.writeInt(seq);
                        if (layoutParams != null) {
                            _data.writeInt(1);
                            layoutParams.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            _data.writeInt(viewVisibility);
                        } catch (Throwable th2) {
                            th = th2;
                            i = layerStackId;
                            rect = outContentInsets;
                            rect2 = outStableInsets;
                            insetsState2 = insetsState;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        i2 = viewVisibility;
                        i = layerStackId;
                        rect = outContentInsets;
                        rect2 = outStableInsets;
                        insetsState2 = insetsState;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(layerStackId);
                        int _result;
                        if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            _result = _reply.readInt();
                            if (_reply.readInt() != 0) {
                                try {
                                    outContentInsets.readFromParcel(_reply);
                                } catch (Throwable th4) {
                                    th = th4;
                                    rect2 = outStableInsets;
                                    insetsState2 = insetsState;
                                    _reply.recycle();
                                    _data.recycle();
                                    throw th;
                                }
                            }
                            rect = outContentInsets;
                            if (_reply.readInt() != 0) {
                                try {
                                    outStableInsets.readFromParcel(_reply);
                                } catch (Throwable th5) {
                                    th = th5;
                                    insetsState2 = insetsState;
                                    _reply.recycle();
                                    _data.recycle();
                                    throw th;
                                }
                            }
                            rect2 = outStableInsets;
                            if (_reply.readInt() != 0) {
                                try {
                                    insetsState.readFromParcel(_reply);
                                } catch (Throwable th6) {
                                    th = th6;
                                }
                            } else {
                                insetsState2 = insetsState;
                            }
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        }
                        _result = Stub.getDefaultImpl().addToDisplayWithoutInputChannel(window, seq, attrs, viewVisibility, layerStackId, outContentInsets, outStableInsets, insetsState);
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    } catch (Throwable th7) {
                        th = th7;
                        rect = outContentInsets;
                        rect2 = outStableInsets;
                        insetsState2 = insetsState;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th8) {
                    th = th8;
                    int i3 = seq;
                    i2 = viewVisibility;
                    i = layerStackId;
                    rect = outContentInsets;
                    rect2 = outStableInsets;
                    insetsState2 = insetsState;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void remove(IWindow window) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().remove(window);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int relayout(IWindow window, int seq, LayoutParams attrs, int requestedWidth, int requestedHeight, int viewVisibility, int flags, long frameNumber, Rect outFrame, Rect outOverscanInsets, Rect outContentInsets, Rect outVisibleInsets, Rect outStableInsets, Rect outOutsets, Rect outBackdropFrame, ParcelableWrapper displayCutout, MergedConfiguration outMergedConfiguration, SurfaceControl outSurfaceControl, InsetsState insetsState) throws RemoteException {
                Throwable th;
                Rect rect;
                Rect rect2;
                Rect rect3;
                Rect rect4;
                Rect rect5;
                Rect rect6;
                Rect rect7;
                ParcelableWrapper parcelableWrapper;
                MergedConfiguration mergedConfiguration;
                SurfaceControl surfaceControl;
                Parcel _reply;
                InsetsState _reply2;
                LayoutParams layoutParams = attrs;
                Parcel _data = Parcel.obtain();
                Parcel _reply3 = Parcel.obtain();
                Parcel _data2;
                try {
                    IBinder asBinder;
                    Parcel _reply4;
                    int relayout;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (window != null) {
                        try {
                            asBinder = window.asBinder();
                        } catch (Throwable th2) {
                            th = th2;
                            rect = outFrame;
                            rect2 = outOverscanInsets;
                            rect3 = outContentInsets;
                            rect4 = outVisibleInsets;
                            rect5 = outStableInsets;
                            rect6 = outOutsets;
                            rect7 = outBackdropFrame;
                            parcelableWrapper = displayCutout;
                            mergedConfiguration = outMergedConfiguration;
                            surfaceControl = outSurfaceControl;
                            _reply = _reply3;
                            _data2 = _data;
                            _reply2 = insetsState;
                        }
                    } else {
                        asBinder = null;
                    }
                    _data.writeStrongBinder(asBinder);
                    _data.writeInt(seq);
                    if (layoutParams != null) {
                        _data.writeInt(1);
                        layoutParams.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(requestedWidth);
                    _data.writeInt(requestedHeight);
                    _data.writeInt(viewVisibility);
                    _data.writeInt(flags);
                    _data.writeLong(frameNumber);
                    if (!this.mRemote.transact(4, _data, _reply3, 0)) {
                        try {
                            if (Stub.getDefaultImpl() != null) {
                                _reply4 = _reply3;
                                _data2 = _data;
                                try {
                                    relayout = Stub.getDefaultImpl().relayout(window, seq, attrs, requestedWidth, requestedHeight, viewVisibility, flags, frameNumber, outFrame, outOverscanInsets, outContentInsets, outVisibleInsets, outStableInsets, outOutsets, outBackdropFrame, displayCutout, outMergedConfiguration, outSurfaceControl, insetsState);
                                    _reply4.recycle();
                                    _data2.recycle();
                                    return relayout;
                                } catch (Throwable th3) {
                                    th = th3;
                                    rect = outFrame;
                                    rect2 = outOverscanInsets;
                                    rect3 = outContentInsets;
                                    rect4 = outVisibleInsets;
                                    rect5 = outStableInsets;
                                    rect6 = outOutsets;
                                    rect7 = outBackdropFrame;
                                    parcelableWrapper = displayCutout;
                                    mergedConfiguration = outMergedConfiguration;
                                    surfaceControl = outSurfaceControl;
                                    _reply2 = insetsState;
                                    _reply = _reply4;
                                    _reply.recycle();
                                    _data2.recycle();
                                    throw th;
                                }
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            _data2 = _data;
                            rect = outFrame;
                            rect2 = outOverscanInsets;
                            rect3 = outContentInsets;
                            rect4 = outVisibleInsets;
                            rect5 = outStableInsets;
                            rect6 = outOutsets;
                            rect7 = outBackdropFrame;
                            parcelableWrapper = displayCutout;
                            mergedConfiguration = outMergedConfiguration;
                            surfaceControl = outSurfaceControl;
                            _reply = _reply3;
                            _reply2 = insetsState;
                            _reply.recycle();
                            _data2.recycle();
                            throw th;
                        }
                    }
                    _reply4 = _reply3;
                    _data2 = _data;
                    try {
                        _reply4.readException();
                        relayout = _reply4.readInt();
                        if (_reply4.readInt() != 0) {
                            _reply = _reply4;
                            try {
                                outFrame.readFromParcel(_reply);
                            } catch (Throwable th5) {
                                th = th5;
                                rect2 = outOverscanInsets;
                                rect3 = outContentInsets;
                                rect4 = outVisibleInsets;
                                rect5 = outStableInsets;
                                rect6 = outOutsets;
                                rect7 = outBackdropFrame;
                                parcelableWrapper = displayCutout;
                                mergedConfiguration = outMergedConfiguration;
                                surfaceControl = outSurfaceControl;
                                _reply2 = insetsState;
                                _reply.recycle();
                                _data2.recycle();
                                throw th;
                            }
                        }
                        rect = outFrame;
                        _reply = _reply4;
                        if (_reply.readInt() != 0) {
                            try {
                                outOverscanInsets.readFromParcel(_reply);
                            } catch (Throwable th6) {
                                th = th6;
                                rect3 = outContentInsets;
                                rect4 = outVisibleInsets;
                                rect5 = outStableInsets;
                                rect6 = outOutsets;
                                rect7 = outBackdropFrame;
                                parcelableWrapper = displayCutout;
                                mergedConfiguration = outMergedConfiguration;
                                surfaceControl = outSurfaceControl;
                                _reply2 = insetsState;
                                _reply.recycle();
                                _data2.recycle();
                                throw th;
                            }
                        }
                        rect2 = outOverscanInsets;
                        if (_reply.readInt() != 0) {
                            try {
                                outContentInsets.readFromParcel(_reply);
                            } catch (Throwable th7) {
                                th = th7;
                                rect4 = outVisibleInsets;
                                rect5 = outStableInsets;
                                rect6 = outOutsets;
                                rect7 = outBackdropFrame;
                                parcelableWrapper = displayCutout;
                                mergedConfiguration = outMergedConfiguration;
                                surfaceControl = outSurfaceControl;
                                _reply2 = insetsState;
                                _reply.recycle();
                                _data2.recycle();
                                throw th;
                            }
                        }
                        rect3 = outContentInsets;
                        if (_reply.readInt() != 0) {
                            try {
                                outVisibleInsets.readFromParcel(_reply);
                            } catch (Throwable th8) {
                                th = th8;
                                rect5 = outStableInsets;
                                rect6 = outOutsets;
                                rect7 = outBackdropFrame;
                                parcelableWrapper = displayCutout;
                                mergedConfiguration = outMergedConfiguration;
                                surfaceControl = outSurfaceControl;
                                _reply2 = insetsState;
                                _reply.recycle();
                                _data2.recycle();
                                throw th;
                            }
                        }
                        rect4 = outVisibleInsets;
                        if (_reply.readInt() != 0) {
                            try {
                                outStableInsets.readFromParcel(_reply);
                            } catch (Throwable th9) {
                                th = th9;
                                rect6 = outOutsets;
                                rect7 = outBackdropFrame;
                                parcelableWrapper = displayCutout;
                                mergedConfiguration = outMergedConfiguration;
                                surfaceControl = outSurfaceControl;
                                _reply2 = insetsState;
                                _reply.recycle();
                                _data2.recycle();
                                throw th;
                            }
                        }
                        rect5 = outStableInsets;
                        if (_reply.readInt() != 0) {
                            try {
                                outOutsets.readFromParcel(_reply);
                            } catch (Throwable th10) {
                                th = th10;
                                rect7 = outBackdropFrame;
                                parcelableWrapper = displayCutout;
                                mergedConfiguration = outMergedConfiguration;
                                surfaceControl = outSurfaceControl;
                                _reply2 = insetsState;
                                _reply.recycle();
                                _data2.recycle();
                                throw th;
                            }
                        }
                        rect6 = outOutsets;
                        if (_reply.readInt() != 0) {
                            try {
                                outBackdropFrame.readFromParcel(_reply);
                            } catch (Throwable th11) {
                                th = th11;
                                parcelableWrapper = displayCutout;
                                mergedConfiguration = outMergedConfiguration;
                                surfaceControl = outSurfaceControl;
                                _reply2 = insetsState;
                                _reply.recycle();
                                _data2.recycle();
                                throw th;
                            }
                        }
                        rect7 = outBackdropFrame;
                        if (_reply.readInt() != 0) {
                            try {
                                displayCutout.readFromParcel(_reply);
                            } catch (Throwable th12) {
                                th = th12;
                                mergedConfiguration = outMergedConfiguration;
                                surfaceControl = outSurfaceControl;
                                _reply2 = insetsState;
                                _reply.recycle();
                                _data2.recycle();
                                throw th;
                            }
                        }
                        parcelableWrapper = displayCutout;
                        if (_reply.readInt() != 0) {
                            try {
                                outMergedConfiguration.readFromParcel(_reply);
                            } catch (Throwable th13) {
                                th = th13;
                                surfaceControl = outSurfaceControl;
                                _reply2 = insetsState;
                                _reply.recycle();
                                _data2.recycle();
                                throw th;
                            }
                        }
                        mergedConfiguration = outMergedConfiguration;
                        if (_reply.readInt() != 0) {
                            try {
                                outSurfaceControl.readFromParcel(_reply);
                            } catch (Throwable th14) {
                                th = th14;
                                _reply2 = insetsState;
                                _reply.recycle();
                                _data2.recycle();
                                throw th;
                            }
                        }
                        surfaceControl = outSurfaceControl;
                        if (_reply.readInt() != 0) {
                            try {
                                insetsState.readFromParcel(_reply);
                            } catch (Throwable th15) {
                                th = th15;
                            }
                        } else {
                            _reply2 = insetsState;
                        }
                        _reply.recycle();
                        _data2.recycle();
                        return relayout;
                    } catch (Throwable th16) {
                        th = th16;
                        rect = outFrame;
                        rect2 = outOverscanInsets;
                        rect3 = outContentInsets;
                        rect4 = outVisibleInsets;
                        rect5 = outStableInsets;
                        rect6 = outOutsets;
                        rect7 = outBackdropFrame;
                        parcelableWrapper = displayCutout;
                        mergedConfiguration = outMergedConfiguration;
                        surfaceControl = outSurfaceControl;
                        _reply2 = insetsState;
                        _reply = _reply4;
                        _reply.recycle();
                        _data2.recycle();
                        throw th;
                    }
                } catch (Throwable th17) {
                    th = th17;
                    rect = outFrame;
                    rect2 = outOverscanInsets;
                    rect3 = outContentInsets;
                    rect4 = outVisibleInsets;
                    rect5 = outStableInsets;
                    rect6 = outOutsets;
                    rect7 = outBackdropFrame;
                    parcelableWrapper = displayCutout;
                    mergedConfiguration = outMergedConfiguration;
                    surfaceControl = outSurfaceControl;
                    _reply = _reply3;
                    _data2 = _data;
                    _reply2 = insetsState;
                    _reply.recycle();
                    _data2.recycle();
                    throw th;
                }
            }

            public void prepareToReplaceWindows(IBinder appToken, boolean childrenOnly) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(appToken);
                    _data.writeInt(childrenOnly ? 1 : 0);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().prepareToReplaceWindows(appToken, childrenOnly);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean outOfMemory(IWindow window) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().outOfMemory(window);
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

            public void setTransparentRegion(IWindow window, Region region) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                    if (region != null) {
                        _data.writeInt(1);
                        region.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setTransparentRegion(window, region);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setInsets(IWindow window, int touchableInsets, Rect contentInsets, Rect visibleInsets, Region touchableRegion) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                    _data.writeInt(touchableInsets);
                    if (contentInsets != null) {
                        _data.writeInt(1);
                        contentInsets.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (visibleInsets != null) {
                        _data.writeInt(1);
                        visibleInsets.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (touchableRegion != null) {
                        _data.writeInt(1);
                        touchableRegion.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setInsets(window, touchableInsets, contentInsets, visibleInsets, touchableRegion);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void getDisplayFrame(IWindow window, Rect outDisplayFrame) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() != 0) {
                            outDisplayFrame.readFromParcel(_reply);
                        }
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().getDisplayFrame(window, outDisplayFrame);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void finishDrawing(IWindow window) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().finishDrawing(window);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setInTouchMode(boolean showFocus) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(showFocus ? 1 : 0);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setInTouchMode(showFocus);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getInTouchMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().getInTouchMode();
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

            public boolean performHapticFeedback(int effectId, boolean always) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(effectId);
                    boolean _result = true;
                    _data.writeInt(always ? 1 : 0);
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().performHapticFeedback(effectId, always);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IBinder performDrag(IWindow window, int flags, SurfaceControl surface, int touchSource, float touchX, float touchY, float thumbCenterX, float thumbCenterY, ClipData data) throws RemoteException {
                Throwable th;
                SurfaceControl surfaceControl = surface;
                ClipData clipData = data;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                    try {
                        _data.writeInt(flags);
                        if (surfaceControl != null) {
                            _data.writeInt(1);
                            surfaceControl.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        _data.writeInt(touchSource);
                        _data.writeFloat(touchX);
                        _data.writeFloat(touchY);
                        _data.writeFloat(thumbCenterX);
                        _data.writeFloat(thumbCenterY);
                        if (clipData != null) {
                            _data.writeInt(1);
                            clipData.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            IBinder _result = _reply.readStrongBinder();
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        }
                        IBinder performDrag = Stub.getDefaultImpl().performDrag(window, flags, surface, touchSource, touchX, touchY, thumbCenterX, thumbCenterY, data);
                        _reply.recycle();
                        _data.recycle();
                        return performDrag;
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    int i = flags;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void reportDropResult(IWindow window, boolean consumed) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                    _data.writeInt(consumed ? 1 : 0);
                    if (this.mRemote.transact(15, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reportDropResult(window, consumed);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cancelDragAndDrop(IBinder dragToken, boolean skipAnimation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(dragToken);
                    _data.writeInt(skipAnimation ? 1 : 0);
                    if (this.mRemote.transact(16, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().cancelDragAndDrop(dragToken, skipAnimation);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void dragRecipientEntered(IWindow window) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                    if (this.mRemote.transact(17, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().dragRecipientEntered(window);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void dragRecipientExited(IWindow window) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                    if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().dragRecipientExited(window);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setWallpaperPosition(IBinder windowToken, float x, float y, float xstep, float ystep) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(windowToken);
                    _data.writeFloat(x);
                    _data.writeFloat(y);
                    _data.writeFloat(xstep);
                    _data.writeFloat(ystep);
                    if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setWallpaperPosition(windowToken, x, y, xstep, ystep);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void wallpaperOffsetsComplete(IBinder window) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window);
                    if (this.mRemote.transact(20, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().wallpaperOffsetsComplete(window);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setWallpaperDisplayOffset(IBinder windowToken, int x, int y) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(windowToken);
                    _data.writeInt(x);
                    _data.writeInt(y);
                    if (this.mRemote.transact(21, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setWallpaperDisplayOffset(windowToken, x, y);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bundle sendWallpaperCommand(IBinder window, String action, int x, int y, int z, Bundle extras, boolean sync) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                String str;
                Bundle bundle = extras;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeStrongBinder(window);
                        try {
                            _data.writeString(action);
                        } catch (Throwable th2) {
                            th = th2;
                            i = x;
                            i2 = y;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeInt(x);
                        } catch (Throwable th3) {
                            th = th3;
                            i2 = y;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        str = action;
                        i = x;
                        i2 = y;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(y);
                        _data.writeInt(z);
                        int i3 = 1;
                        if (bundle != null) {
                            _data.writeInt(1);
                            bundle.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (!sync) {
                            i3 = 0;
                        }
                        _data.writeInt(i3);
                        Bundle _result;
                        if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            if (_reply.readInt() != 0) {
                                _result = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
                            } else {
                                _result = null;
                            }
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        }
                        _result = Stub.getDefaultImpl().sendWallpaperCommand(window, action, x, y, z, extras, sync);
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    } catch (Throwable th5) {
                        th = th5;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    IBinder iBinder = window;
                    str = action;
                    i = x;
                    i2 = y;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void wallpaperCommandComplete(IBinder window, Bundle result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window);
                    if (result != null) {
                        _data.writeInt(1);
                        result.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(23, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().wallpaperCommandComplete(window, result);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onRectangleOnScreenRequested(IBinder token, Rect rectangle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (rectangle != null) {
                        _data.writeInt(1);
                        rectangle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(24, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onRectangleOnScreenRequested(token, rectangle);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IWindowId getWindowId(IBinder window) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window);
                    IWindowId iWindowId = 25;
                    if (!this.mRemote.transact(25, _data, _reply, 0)) {
                        iWindowId = Stub.getDefaultImpl();
                        if (iWindowId != 0) {
                            iWindowId = Stub.getDefaultImpl().getWindowId(window);
                            return iWindowId;
                        }
                    }
                    _reply.readException();
                    iWindowId = android.view.IWindowId.Stub.asInterface(_reply.readStrongBinder());
                    IWindowId _result = iWindowId;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void pokeDrawLock(IBinder window) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window);
                    if (this.mRemote.transact(26, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().pokeDrawLock(window);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean startMovingTask(IWindow window, float startX, float startY) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                    _data.writeFloat(startX);
                    _data.writeFloat(startY);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(27, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().startMovingTask(window, startX, startY);
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

            public void finishMovingTask(IWindow window) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                    if (this.mRemote.transact(28, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().finishMovingTask(window);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updatePointerIcon(IWindow window) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                    if (this.mRemote.transact(29, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updatePointerIcon(window);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reparentDisplayContent(IWindow window, SurfaceControl sc, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                    if (sc != null) {
                        _data.writeInt(1);
                        sc.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(displayId);
                    if (this.mRemote.transact(30, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reparentDisplayContent(window, sc, displayId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateDisplayContentLocation(IWindow window, int x, int y, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                    _data.writeInt(x);
                    _data.writeInt(y);
                    _data.writeInt(displayId);
                    if (this.mRemote.transact(31, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateDisplayContentLocation(window, x, y, displayId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateTapExcludeRegion(IWindow window, int regionId, Region region) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                    _data.writeInt(regionId);
                    if (region != null) {
                        _data.writeInt(1);
                        region.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(32, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateTapExcludeRegion(window, regionId, region);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void insetsModified(IWindow window, InsetsState state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                    if (state != null) {
                        _data.writeInt(1);
                        state.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(33, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().insetsModified(window, state);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportSystemGestureExclusionChanged(IWindow window, List<Rect> exclusionRects) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                    _data.writeTypedList(exclusionRects);
                    if (this.mRemote.transact(34, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().reportSystemGestureExclusionChanged(window, exclusionRects);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IWindowSession asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IWindowSession)) {
                return new Proxy(obj);
            }
            return (IWindowSession) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "addToDisplay";
                case 2:
                    return "addToDisplayWithoutInputChannel";
                case 3:
                    return "remove";
                case 4:
                    return "relayout";
                case 5:
                    return "prepareToReplaceWindows";
                case 6:
                    return "outOfMemory";
                case 7:
                    return "setTransparentRegion";
                case 8:
                    return "setInsets";
                case 9:
                    return "getDisplayFrame";
                case 10:
                    return "finishDrawing";
                case 11:
                    return "setInTouchMode";
                case 12:
                    return "getInTouchMode";
                case 13:
                    return "performHapticFeedback";
                case 14:
                    return "performDrag";
                case 15:
                    return "reportDropResult";
                case 16:
                    return "cancelDragAndDrop";
                case 17:
                    return "dragRecipientEntered";
                case 18:
                    return "dragRecipientExited";
                case 19:
                    return "setWallpaperPosition";
                case 20:
                    return "wallpaperOffsetsComplete";
                case 21:
                    return "setWallpaperDisplayOffset";
                case 22:
                    return "sendWallpaperCommand";
                case 23:
                    return "wallpaperCommandComplete";
                case 24:
                    return "onRectangleOnScreenRequested";
                case 25:
                    return "getWindowId";
                case 26:
                    return "pokeDrawLock";
                case 27:
                    return "startMovingTask";
                case 28:
                    return "finishMovingTask";
                case 29:
                    return "updatePointerIcon";
                case 30:
                    return "reparentDisplayContent";
                case 31:
                    return "updateDisplayContentLocation";
                case 32:
                    return "updateTapExcludeRegion";
                case 33:
                    return "insetsModified";
                case 34:
                    return "reportSystemGestureExclusionChanged";
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
            Parcel parcel2 = reply;
            String descriptor = DESCRIPTOR;
            Parcel parcel3;
            boolean z;
            if (i != 1598968902) {
                boolean z2 = false;
                Parcel parcel4;
                String descriptor2;
                IWindow _arg0;
                int _arg1;
                int _arg3;
                Rect _arg5;
                Rect _arg6;
                Rect rect;
                Rect _arg8;
                int _result;
                int _arg4;
                int readInt;
                boolean z3;
                Rect _arg9;
                IBinder _arg02;
                IWindow _arg03;
                switch (i) {
                    case 1:
                        LayoutParams _arg2;
                        i = 1;
                        parcel4 = parcel2;
                        descriptor2 = descriptor;
                        parcel3 = parcel;
                        parcel = parcel4;
                        parcel3.enforceInterface(descriptor2);
                        _arg0 = android.view.IWindow.Stub.asInterface(data.readStrongBinder());
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = (LayoutParams) LayoutParams.CREATOR.createFromParcel(parcel3);
                        } else {
                            _arg2 = null;
                        }
                        _arg3 = data.readInt();
                        int _arg42 = data.readInt();
                        _arg5 = new Rect();
                        _arg6 = new Rect();
                        rect = new Rect();
                        _arg8 = new Rect();
                        ParcelableWrapper _arg92 = new ParcelableWrapper();
                        InputChannel _arg10 = new InputChannel();
                        InsetsState _arg11 = new InsetsState();
                        InputChannel _arg102 = _arg10;
                        ParcelableWrapper _arg93 = _arg92;
                        Rect _arg82 = _arg8;
                        Rect _arg7 = rect;
                        Rect _arg62 = _arg6;
                        Rect _arg52 = _arg5;
                        _result = addToDisplay(_arg0, _arg1, _arg2, _arg3, _arg42, _arg5, _arg6, rect, _arg82, _arg93, _arg102, _arg11);
                        reply.writeNoException();
                        parcel.writeInt(_result);
                        parcel.writeInt(i);
                        _arg52.writeToParcel(parcel, i);
                        parcel.writeInt(i);
                        _arg62.writeToParcel(parcel, i);
                        parcel.writeInt(i);
                        _arg7.writeToParcel(parcel, i);
                        parcel.writeInt(i);
                        _arg82.writeToParcel(parcel, i);
                        parcel.writeInt(i);
                        _arg93.writeToParcel(parcel, i);
                        parcel.writeInt(i);
                        _arg102.writeToParcel(parcel, i);
                        parcel.writeInt(i);
                        _arg11.writeToParcel(parcel, i);
                        return i;
                    case 2:
                        LayoutParams _arg22;
                        i = 1;
                        parcel4 = parcel2;
                        descriptor2 = descriptor;
                        parcel3 = parcel;
                        parcel = parcel4;
                        parcel3.enforceInterface(descriptor2);
                        IWindow _arg04 = android.view.IWindow.Stub.asInterface(data.readStrongBinder());
                        int _arg12 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg22 = (LayoutParams) LayoutParams.CREATOR.createFromParcel(parcel3);
                        } else {
                            _arg22 = null;
                        }
                        _arg1 = data.readInt();
                        _arg4 = data.readInt();
                        rect = new Rect();
                        _arg8 = new Rect();
                        InsetsState _arg72 = new InsetsState();
                        Rect _arg63 = _arg8;
                        Rect _arg53 = rect;
                        _result = addToDisplayWithoutInputChannel(_arg04, _arg12, _arg22, _arg1, _arg4, rect, _arg8, _arg72);
                        reply.writeNoException();
                        parcel.writeInt(_result);
                        parcel.writeInt(i);
                        _arg53.writeToParcel(parcel, i);
                        parcel.writeInt(i);
                        _arg63.writeToParcel(parcel, i);
                        parcel.writeInt(i);
                        _arg72.writeToParcel(parcel, i);
                        return i;
                    case 3:
                        z = true;
                        parcel = parcel2;
                        data.enforceInterface(descriptor);
                        remove(android.view.IWindow.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return z;
                    case 4:
                        LayoutParams _arg23;
                        parcel.enforceInterface(descriptor);
                        IWindow _arg05 = android.view.IWindow.Stub.asInterface(data.readStrongBinder());
                        int _arg13 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg23 = (LayoutParams) LayoutParams.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg23 = null;
                        }
                        int readInt2 = data.readInt();
                        int readInt3 = data.readInt();
                        int readInt4 = data.readInt();
                        readInt = data.readInt();
                        long readLong = data.readLong();
                        Rect _arg83 = new Rect();
                        z3 = true;
                        _arg5 = _arg83;
                        _arg9 = new Rect();
                        String descriptor3 = descriptor;
                        Rect rect2 = _arg9;
                        Rect _arg103 = new Rect();
                        Rect rect3 = _arg103;
                        Rect _arg112 = new Rect();
                        Rect rect4 = _arg112;
                        Rect _arg122 = new Rect();
                        Rect rect5 = _arg122;
                        Rect _arg132 = new Rect();
                        Rect rect6 = _arg132;
                        Rect rect7 = new Rect();
                        Rect _arg14 = rect7;
                        ParcelableWrapper parcelableWrapper = new ParcelableWrapper();
                        ParcelableWrapper _arg15 = parcelableWrapper;
                        MergedConfiguration mergedConfiguration = new MergedConfiguration();
                        MergedConfiguration _arg16 = mergedConfiguration;
                        SurfaceControl surfaceControl = new SurfaceControl();
                        SurfaceControl _arg17 = surfaceControl;
                        InsetsState insetsState = new InsetsState();
                        InsetsState _arg18 = insetsState;
                        Rect _arg94 = _arg9;
                        Rect _arg84 = _arg83;
                        _result = relayout(_arg05, _arg13, _arg23, readInt2, readInt3, readInt4, readInt, readLong, _arg5, rect2, rect3, rect4, rect5, rect6, rect7, parcelableWrapper, mergedConfiguration, surfaceControl, insetsState);
                        reply.writeNoException();
                        parcel = reply;
                        parcel.writeInt(_result);
                        parcel.writeInt(1);
                        _arg84.writeToParcel(parcel, 1);
                        parcel.writeInt(1);
                        _arg94.writeToParcel(parcel, 1);
                        parcel.writeInt(1);
                        _arg103.writeToParcel(parcel, 1);
                        parcel.writeInt(1);
                        _arg112.writeToParcel(parcel, 1);
                        parcel.writeInt(1);
                        _arg122.writeToParcel(parcel, 1);
                        parcel.writeInt(1);
                        _arg132.writeToParcel(parcel, 1);
                        parcel.writeInt(1);
                        _arg14.writeToParcel(parcel, 1);
                        parcel.writeInt(1);
                        _arg15.writeToParcel(parcel, 1);
                        parcel.writeInt(1);
                        _arg16.writeToParcel(parcel, 1);
                        parcel.writeInt(1);
                        _arg17.writeToParcel(parcel, 1);
                        parcel.writeInt(1);
                        _arg18.writeToParcel(parcel, 1);
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            z2 = true;
                        }
                        prepareToReplaceWindows(_arg02, z2);
                        reply.writeNoException();
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        boolean _result2 = outOfMemory(android.view.IWindow.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 7:
                        Region _arg19;
                        parcel.enforceInterface(descriptor);
                        _arg03 = android.view.IWindow.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg19 = (Region) Region.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg19 = null;
                        }
                        setTransparentRegion(_arg03, _arg19);
                        reply.writeNoException();
                        return true;
                    case 8:
                        Region _arg43;
                        parcel.enforceInterface(descriptor);
                        IWindow _arg06 = android.view.IWindow.Stub.asInterface(data.readStrongBinder());
                        readInt = data.readInt();
                        if (data.readInt() != 0) {
                            rect = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            rect = null;
                        }
                        if (data.readInt() != 0) {
                            _arg6 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg6 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg43 = (Region) Region.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg43 = null;
                        }
                        setInsets(_arg06, readInt, rect, _arg6, _arg43);
                        reply.writeNoException();
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        _arg03 = android.view.IWindow.Stub.asInterface(data.readStrongBinder());
                        _arg9 = new Rect();
                        getDisplayFrame(_arg03, _arg9);
                        reply.writeNoException();
                        parcel2.writeInt(1);
                        _arg9.writeToParcel(parcel2, 1);
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        finishDrawing(android.view.IWindow.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            z2 = true;
                        }
                        setInTouchMode(z2);
                        reply.writeNoException();
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        boolean _result3 = getInTouchMode();
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        _result = data.readInt();
                        if (data.readInt() != 0) {
                            z2 = true;
                        }
                        z3 = performHapticFeedback(_result, z2);
                        reply.writeNoException();
                        parcel2.writeInt(z3);
                        return true;
                    case 14:
                        SurfaceControl _arg24;
                        ClipData _arg85;
                        parcel.enforceInterface(descriptor);
                        _arg0 = android.view.IWindow.Stub.asInterface(data.readStrongBinder());
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg24 = (SurfaceControl) SurfaceControl.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg24 = null;
                        }
                        _arg3 = data.readInt();
                        float _arg44 = data.readFloat();
                        float _arg54 = data.readFloat();
                        float _arg64 = data.readFloat();
                        float _arg73 = data.readFloat();
                        if (data.readInt() != 0) {
                            _arg85 = (ClipData) ClipData.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg85 = null;
                        }
                        _arg02 = performDrag(_arg0, _arg1, _arg24, _arg3, _arg44, _arg54, _arg64, _arg73, _arg85);
                        reply.writeNoException();
                        parcel2.writeStrongBinder(_arg02);
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        _arg03 = android.view.IWindow.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            z2 = true;
                        }
                        reportDropResult(_arg03, z2);
                        reply.writeNoException();
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            z2 = true;
                        }
                        cancelDragAndDrop(_arg02, z2);
                        reply.writeNoException();
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        dragRecipientEntered(android.view.IWindow.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        dragRecipientExited(android.view.IWindow.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        setWallpaperPosition(data.readStrongBinder(), data.readFloat(), data.readFloat(), data.readFloat(), data.readFloat());
                        reply.writeNoException();
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        wallpaperOffsetsComplete(data.readStrongBinder());
                        reply.writeNoException();
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        setWallpaperDisplayOffset(data.readStrongBinder(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 22:
                        Bundle _arg55;
                        parcel.enforceInterface(descriptor);
                        IBinder _arg07 = data.readStrongBinder();
                        String _arg110 = data.readString();
                        _arg1 = data.readInt();
                        _arg4 = data.readInt();
                        _arg3 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg55 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg55 = null;
                        }
                        Bundle _result4 = sendWallpaperCommand(_arg07, _arg110, _arg1, _arg4, _arg3, _arg55, data.readInt() != 0);
                        reply.writeNoException();
                        if (_result4 != null) {
                            parcel2.writeInt(1);
                            _result4.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 23:
                        Bundle _arg111;
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg111 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg111 = null;
                        }
                        wallpaperCommandComplete(_arg02, _arg111);
                        reply.writeNoException();
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg9 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg9 = null;
                        }
                        onRectangleOnScreenRequested(_arg02, _arg9);
                        reply.writeNoException();
                        return true;
                    case 25:
                        parcel.enforceInterface(descriptor);
                        IWindowId _result5 = getWindowId(data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeStrongBinder(_result5 != null ? _result5.asBinder() : null);
                        return true;
                    case 26:
                        parcel.enforceInterface(descriptor);
                        pokeDrawLock(data.readStrongBinder());
                        reply.writeNoException();
                        return true;
                    case 27:
                        parcel.enforceInterface(descriptor);
                        boolean _result6 = startMovingTask(android.view.IWindow.Stub.asInterface(data.readStrongBinder()), data.readFloat(), data.readFloat());
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return true;
                    case 28:
                        parcel.enforceInterface(descriptor);
                        finishMovingTask(android.view.IWindow.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 29:
                        parcel.enforceInterface(descriptor);
                        updatePointerIcon(android.view.IWindow.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 30:
                        SurfaceControl _arg113;
                        parcel.enforceInterface(descriptor);
                        _arg03 = android.view.IWindow.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg113 = (SurfaceControl) SurfaceControl.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg113 = null;
                        }
                        reparentDisplayContent(_arg03, _arg113, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 31:
                        parcel.enforceInterface(descriptor);
                        updateDisplayContentLocation(android.view.IWindow.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 32:
                        Region _arg25;
                        parcel.enforceInterface(descriptor);
                        _arg03 = android.view.IWindow.Stub.asInterface(data.readStrongBinder());
                        int _arg114 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg25 = (Region) Region.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg25 = null;
                        }
                        updateTapExcludeRegion(_arg03, _arg114, _arg25);
                        reply.writeNoException();
                        return true;
                    case 33:
                        InsetsState _arg115;
                        parcel.enforceInterface(descriptor);
                        _arg03 = android.view.IWindow.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg115 = (InsetsState) InsetsState.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg115 = null;
                        }
                        insetsModified(_arg03, _arg115);
                        reply.writeNoException();
                        return true;
                    case 34:
                        parcel.enforceInterface(descriptor);
                        reportSystemGestureExclusionChanged(android.view.IWindow.Stub.asInterface(data.readStrongBinder()), parcel.createTypedArrayList(Rect.CREATOR));
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            z = true;
            parcel3 = parcel;
            parcel2.writeString(descriptor);
            return z;
        }

        public static boolean setDefaultImpl(IWindowSession impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IWindowSession getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    int addToDisplay(IWindow iWindow, int i, LayoutParams layoutParams, int i2, int i3, Rect rect, Rect rect2, Rect rect3, Rect rect4, ParcelableWrapper parcelableWrapper, InputChannel inputChannel, InsetsState insetsState) throws RemoteException;

    int addToDisplayWithoutInputChannel(IWindow iWindow, int i, LayoutParams layoutParams, int i2, int i3, Rect rect, Rect rect2, InsetsState insetsState) throws RemoteException;

    void cancelDragAndDrop(IBinder iBinder, boolean z) throws RemoteException;

    void dragRecipientEntered(IWindow iWindow) throws RemoteException;

    void dragRecipientExited(IWindow iWindow) throws RemoteException;

    @UnsupportedAppUsage
    void finishDrawing(IWindow iWindow) throws RemoteException;

    void finishMovingTask(IWindow iWindow) throws RemoteException;

    void getDisplayFrame(IWindow iWindow, Rect rect) throws RemoteException;

    @UnsupportedAppUsage
    boolean getInTouchMode() throws RemoteException;

    IWindowId getWindowId(IBinder iBinder) throws RemoteException;

    void insetsModified(IWindow iWindow, InsetsState insetsState) throws RemoteException;

    void onRectangleOnScreenRequested(IBinder iBinder, Rect rect) throws RemoteException;

    boolean outOfMemory(IWindow iWindow) throws RemoteException;

    @UnsupportedAppUsage
    IBinder performDrag(IWindow iWindow, int i, SurfaceControl surfaceControl, int i2, float f, float f2, float f3, float f4, ClipData clipData) throws RemoteException;

    @UnsupportedAppUsage
    boolean performHapticFeedback(int i, boolean z) throws RemoteException;

    void pokeDrawLock(IBinder iBinder) throws RemoteException;

    void prepareToReplaceWindows(IBinder iBinder, boolean z) throws RemoteException;

    int relayout(IWindow iWindow, int i, LayoutParams layoutParams, int i2, int i3, int i4, int i5, long j, Rect rect, Rect rect2, Rect rect3, Rect rect4, Rect rect5, Rect rect6, Rect rect7, ParcelableWrapper parcelableWrapper, MergedConfiguration mergedConfiguration, SurfaceControl surfaceControl, InsetsState insetsState) throws RemoteException;

    @UnsupportedAppUsage
    void remove(IWindow iWindow) throws RemoteException;

    void reparentDisplayContent(IWindow iWindow, SurfaceControl surfaceControl, int i) throws RemoteException;

    void reportDropResult(IWindow iWindow, boolean z) throws RemoteException;

    void reportSystemGestureExclusionChanged(IWindow iWindow, List<Rect> list) throws RemoteException;

    Bundle sendWallpaperCommand(IBinder iBinder, String str, int i, int i2, int i3, Bundle bundle, boolean z) throws RemoteException;

    @UnsupportedAppUsage
    void setInTouchMode(boolean z) throws RemoteException;

    void setInsets(IWindow iWindow, int i, Rect rect, Rect rect2, Region region) throws RemoteException;

    @UnsupportedAppUsage
    void setTransparentRegion(IWindow iWindow, Region region) throws RemoteException;

    void setWallpaperDisplayOffset(IBinder iBinder, int i, int i2) throws RemoteException;

    void setWallpaperPosition(IBinder iBinder, float f, float f2, float f3, float f4) throws RemoteException;

    boolean startMovingTask(IWindow iWindow, float f, float f2) throws RemoteException;

    void updateDisplayContentLocation(IWindow iWindow, int i, int i2, int i3) throws RemoteException;

    void updatePointerIcon(IWindow iWindow) throws RemoteException;

    void updateTapExcludeRegion(IWindow iWindow, int i, Region region) throws RemoteException;

    @UnsupportedAppUsage
    void wallpaperCommandComplete(IBinder iBinder, Bundle bundle) throws RemoteException;

    @UnsupportedAppUsage
    void wallpaperOffsetsComplete(IBinder iBinder) throws RemoteException;
}
