package android.accessibilityservice;

import android.content.pm.ParceledListSlice;
import android.graphics.Region;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.accessibility.AccessibilityWindowInfo;
import android.view.accessibility.IAccessibilityInteractionConnectionCallback;
import java.util.List;

public interface IAccessibilityServiceConnection extends IInterface {

    public static class Default implements IAccessibilityServiceConnection {
        public void setServiceInfo(AccessibilityServiceInfo info) throws RemoteException {
        }

        public String[] findAccessibilityNodeInfoByAccessibilityId(int accessibilityWindowId, long accessibilityNodeId, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, long threadId, Bundle arguments) throws RemoteException {
            return null;
        }

        public String[] findAccessibilityNodeInfosByText(int accessibilityWindowId, long accessibilityNodeId, String text, int interactionId, IAccessibilityInteractionConnectionCallback callback, long threadId) throws RemoteException {
            return null;
        }

        public String[] findAccessibilityNodeInfosByViewId(int accessibilityWindowId, long accessibilityNodeId, String viewId, int interactionId, IAccessibilityInteractionConnectionCallback callback, long threadId) throws RemoteException {
            return null;
        }

        public String[] findFocus(int accessibilityWindowId, long accessibilityNodeId, int focusType, int interactionId, IAccessibilityInteractionConnectionCallback callback, long threadId) throws RemoteException {
            return null;
        }

        public String[] focusSearch(int accessibilityWindowId, long accessibilityNodeId, int direction, int interactionId, IAccessibilityInteractionConnectionCallback callback, long threadId) throws RemoteException {
            return null;
        }

        public boolean performAccessibilityAction(int accessibilityWindowId, long accessibilityNodeId, int action, Bundle arguments, int interactionId, IAccessibilityInteractionConnectionCallback callback, long threadId) throws RemoteException {
            return false;
        }

        public AccessibilityWindowInfo getWindow(int windowId) throws RemoteException {
            return null;
        }

        public List<AccessibilityWindowInfo> getWindows() throws RemoteException {
            return null;
        }

        public AccessibilityServiceInfo getServiceInfo() throws RemoteException {
            return null;
        }

        public boolean performGlobalAction(int action) throws RemoteException {
            return false;
        }

        public void disableSelf() throws RemoteException {
        }

        public void setOnKeyEventResult(boolean handled, int sequence) throws RemoteException {
        }

        public float getMagnificationScale(int displayId) throws RemoteException {
            return 0.0f;
        }

        public float getMagnificationCenterX(int displayId) throws RemoteException {
            return 0.0f;
        }

        public float getMagnificationCenterY(int displayId) throws RemoteException {
            return 0.0f;
        }

        public Region getMagnificationRegion(int displayId) throws RemoteException {
            return null;
        }

        public boolean resetMagnification(int displayId, boolean animate) throws RemoteException {
            return false;
        }

        public boolean setMagnificationScaleAndCenter(int displayId, float scale, float centerX, float centerY, boolean animate) throws RemoteException {
            return false;
        }

        public void setMagnificationCallbackEnabled(int displayId, boolean enabled) throws RemoteException {
        }

        public boolean setSoftKeyboardShowMode(int showMode) throws RemoteException {
            return false;
        }

        public int getSoftKeyboardShowMode() throws RemoteException {
            return 0;
        }

        public void setSoftKeyboardCallbackEnabled(boolean enabled) throws RemoteException {
        }

        public boolean isAccessibilityButtonAvailable() throws RemoteException {
            return false;
        }

        public void sendGesture(int sequence, ParceledListSlice gestureSteps) throws RemoteException {
        }

        public boolean isFingerprintGestureDetectionAvailable() throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IAccessibilityServiceConnection {
        private static final String DESCRIPTOR = "android.accessibilityservice.IAccessibilityServiceConnection";
        static final int TRANSACTION_disableSelf = 12;
        static final int TRANSACTION_findAccessibilityNodeInfoByAccessibilityId = 2;
        static final int TRANSACTION_findAccessibilityNodeInfosByText = 3;
        static final int TRANSACTION_findAccessibilityNodeInfosByViewId = 4;
        static final int TRANSACTION_findFocus = 5;
        static final int TRANSACTION_focusSearch = 6;
        static final int TRANSACTION_getMagnificationCenterX = 15;
        static final int TRANSACTION_getMagnificationCenterY = 16;
        static final int TRANSACTION_getMagnificationRegion = 17;
        static final int TRANSACTION_getMagnificationScale = 14;
        static final int TRANSACTION_getServiceInfo = 10;
        static final int TRANSACTION_getSoftKeyboardShowMode = 22;
        static final int TRANSACTION_getWindow = 8;
        static final int TRANSACTION_getWindows = 9;
        static final int TRANSACTION_isAccessibilityButtonAvailable = 24;
        static final int TRANSACTION_isFingerprintGestureDetectionAvailable = 26;
        static final int TRANSACTION_performAccessibilityAction = 7;
        static final int TRANSACTION_performGlobalAction = 11;
        static final int TRANSACTION_resetMagnification = 18;
        static final int TRANSACTION_sendGesture = 25;
        static final int TRANSACTION_setMagnificationCallbackEnabled = 20;
        static final int TRANSACTION_setMagnificationScaleAndCenter = 19;
        static final int TRANSACTION_setOnKeyEventResult = 13;
        static final int TRANSACTION_setServiceInfo = 1;
        static final int TRANSACTION_setSoftKeyboardCallbackEnabled = 23;
        static final int TRANSACTION_setSoftKeyboardShowMode = 21;

        private static class Proxy implements IAccessibilityServiceConnection {
            public static IAccessibilityServiceConnection sDefaultImpl;
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

            public void setServiceInfo(AccessibilityServiceInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setServiceInfo(info);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] findAccessibilityNodeInfoByAccessibilityId(int accessibilityWindowId, long accessibilityNodeId, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, long threadId, Bundle arguments) throws RemoteException {
                Throwable th;
                int i;
                Bundle bundle = arguments;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(accessibilityWindowId);
                        _data.writeLong(accessibilityNodeId);
                        try {
                            _data.writeInt(interactionId);
                            _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                            _data.writeInt(flags);
                            _data.writeLong(threadId);
                            if (bundle != null) {
                                _data.writeInt(1);
                                bundle.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                String[] _result = _reply.createStringArray();
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            String[] findAccessibilityNodeInfoByAccessibilityId = Stub.getDefaultImpl().findAccessibilityNodeInfoByAccessibilityId(accessibilityWindowId, accessibilityNodeId, interactionId, callback, flags, threadId, arguments);
                            _reply.recycle();
                            _data.recycle();
                            return findAccessibilityNodeInfoByAccessibilityId;
                        } catch (Throwable th2) {
                            th = th2;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        i = interactionId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    int i2 = accessibilityWindowId;
                    i = interactionId;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public String[] findAccessibilityNodeInfosByText(int accessibilityWindowId, long accessibilityNodeId, String text, int interactionId, IAccessibilityInteractionConnectionCallback callback, long threadId) throws RemoteException {
                Throwable th;
                long j;
                String str;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(accessibilityWindowId);
                    } catch (Throwable th2) {
                        th = th2;
                        j = accessibilityNodeId;
                        str = text;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeLong(accessibilityNodeId);
                    } catch (Throwable th3) {
                        th = th3;
                        str = text;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(text);
                        _data.writeInt(interactionId);
                        _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                        _data.writeLong(threadId);
                        if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            String[] _result = _reply.createStringArray();
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        }
                        String[] findAccessibilityNodeInfosByText = Stub.getDefaultImpl().findAccessibilityNodeInfosByText(accessibilityWindowId, accessibilityNodeId, text, interactionId, callback, threadId);
                        _reply.recycle();
                        _data.recycle();
                        return findAccessibilityNodeInfosByText;
                    } catch (Throwable th4) {
                        th = th4;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    int i = accessibilityWindowId;
                    j = accessibilityNodeId;
                    str = text;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public String[] findAccessibilityNodeInfosByViewId(int accessibilityWindowId, long accessibilityNodeId, String viewId, int interactionId, IAccessibilityInteractionConnectionCallback callback, long threadId) throws RemoteException {
                Throwable th;
                long j;
                String str;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(accessibilityWindowId);
                    } catch (Throwable th2) {
                        th = th2;
                        j = accessibilityNodeId;
                        str = viewId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeLong(accessibilityNodeId);
                    } catch (Throwable th3) {
                        th = th3;
                        str = viewId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(viewId);
                        _data.writeInt(interactionId);
                        _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                        _data.writeLong(threadId);
                        if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            String[] _result = _reply.createStringArray();
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        }
                        String[] findAccessibilityNodeInfosByViewId = Stub.getDefaultImpl().findAccessibilityNodeInfosByViewId(accessibilityWindowId, accessibilityNodeId, viewId, interactionId, callback, threadId);
                        _reply.recycle();
                        _data.recycle();
                        return findAccessibilityNodeInfosByViewId;
                    } catch (Throwable th4) {
                        th = th4;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    int i = accessibilityWindowId;
                    j = accessibilityNodeId;
                    str = viewId;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public String[] findFocus(int accessibilityWindowId, long accessibilityNodeId, int focusType, int interactionId, IAccessibilityInteractionConnectionCallback callback, long threadId) throws RemoteException {
                Throwable th;
                long j;
                int i;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(accessibilityWindowId);
                    } catch (Throwable th2) {
                        th = th2;
                        j = accessibilityNodeId;
                        i = focusType;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeLong(accessibilityNodeId);
                    } catch (Throwable th3) {
                        th = th3;
                        i = focusType;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(focusType);
                        _data.writeInt(interactionId);
                        _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                        _data.writeLong(threadId);
                        if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            String[] _result = _reply.createStringArray();
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        }
                        String[] findFocus = Stub.getDefaultImpl().findFocus(accessibilityWindowId, accessibilityNodeId, focusType, interactionId, callback, threadId);
                        _reply.recycle();
                        _data.recycle();
                        return findFocus;
                    } catch (Throwable th4) {
                        th = th4;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    int i2 = accessibilityWindowId;
                    j = accessibilityNodeId;
                    i = focusType;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public String[] focusSearch(int accessibilityWindowId, long accessibilityNodeId, int direction, int interactionId, IAccessibilityInteractionConnectionCallback callback, long threadId) throws RemoteException {
                Throwable th;
                long j;
                int i;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(accessibilityWindowId);
                    } catch (Throwable th2) {
                        th = th2;
                        j = accessibilityNodeId;
                        i = direction;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeLong(accessibilityNodeId);
                    } catch (Throwable th3) {
                        th = th3;
                        i = direction;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(direction);
                        _data.writeInt(interactionId);
                        _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                        _data.writeLong(threadId);
                        if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            String[] _result = _reply.createStringArray();
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        }
                        String[] focusSearch = Stub.getDefaultImpl().focusSearch(accessibilityWindowId, accessibilityNodeId, direction, interactionId, callback, threadId);
                        _reply.recycle();
                        _data.recycle();
                        return focusSearch;
                    } catch (Throwable th4) {
                        th = th4;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    int i2 = accessibilityWindowId;
                    j = accessibilityNodeId;
                    i = direction;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public boolean performAccessibilityAction(int accessibilityWindowId, long accessibilityNodeId, int action, Bundle arguments, int interactionId, IAccessibilityInteractionConnectionCallback callback, long threadId) throws RemoteException {
                Throwable th;
                int i;
                Bundle bundle = arguments;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(accessibilityWindowId);
                        _data.writeLong(accessibilityNodeId);
                        try {
                            _data.writeInt(action);
                            boolean _result = true;
                            if (bundle != null) {
                                _data.writeInt(1);
                                bundle.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            _data.writeInt(interactionId);
                            _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                            _data.writeLong(threadId);
                            if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                if (_reply.readInt() == 0) {
                                    _result = false;
                                }
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            _result = Stub.getDefaultImpl().performAccessibilityAction(accessibilityWindowId, accessibilityNodeId, action, arguments, interactionId, callback, threadId);
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        } catch (Throwable th2) {
                            th = th2;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        i = action;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    int i2 = accessibilityWindowId;
                    i = action;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public AccessibilityWindowInfo getWindow(int windowId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(windowId);
                    AccessibilityWindowInfo accessibilityWindowInfo = 8;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        accessibilityWindowInfo = Stub.getDefaultImpl();
                        if (accessibilityWindowInfo != 0) {
                            accessibilityWindowInfo = Stub.getDefaultImpl().getWindow(windowId);
                            return accessibilityWindowInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        accessibilityWindowInfo = (AccessibilityWindowInfo) AccessibilityWindowInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        accessibilityWindowInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return accessibilityWindowInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<AccessibilityWindowInfo> getWindows() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<AccessibilityWindowInfo> list = 9;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getWindows();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(AccessibilityWindowInfo.CREATOR);
                    List<AccessibilityWindowInfo> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public AccessibilityServiceInfo getServiceInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    AccessibilityServiceInfo accessibilityServiceInfo = 10;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        accessibilityServiceInfo = Stub.getDefaultImpl();
                        if (accessibilityServiceInfo != 0) {
                            accessibilityServiceInfo = Stub.getDefaultImpl().getServiceInfo();
                            return accessibilityServiceInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        accessibilityServiceInfo = (AccessibilityServiceInfo) AccessibilityServiceInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        accessibilityServiceInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return accessibilityServiceInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean performGlobalAction(int action) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(action);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().performGlobalAction(action);
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

            public void disableSelf() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().disableSelf();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setOnKeyEventResult(boolean handled, int sequence) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(handled ? 1 : 0);
                    _data.writeInt(sequence);
                    if (this.mRemote.transact(13, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setOnKeyEventResult(handled, sequence);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public float getMagnificationScale(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    float f = 14;
                    if (!this.mRemote.transact(14, _data, _reply, 0)) {
                        f = Stub.getDefaultImpl();
                        if (f != 0) {
                            f = Stub.getDefaultImpl().getMagnificationScale(displayId);
                            return f;
                        }
                    }
                    _reply.readException();
                    f = _reply.readFloat();
                    int _result = f;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public float getMagnificationCenterX(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    float f = 15;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        f = Stub.getDefaultImpl();
                        if (f != 0) {
                            f = Stub.getDefaultImpl().getMagnificationCenterX(displayId);
                            return f;
                        }
                    }
                    _reply.readException();
                    f = _reply.readFloat();
                    int _result = f;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public float getMagnificationCenterY(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    float f = 16;
                    if (!this.mRemote.transact(16, _data, _reply, 0)) {
                        f = Stub.getDefaultImpl();
                        if (f != 0) {
                            f = Stub.getDefaultImpl().getMagnificationCenterY(displayId);
                            return f;
                        }
                    }
                    _reply.readException();
                    f = _reply.readFloat();
                    int _result = f;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Region getMagnificationRegion(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    Region region = 17;
                    if (!this.mRemote.transact(17, _data, _reply, 0)) {
                        region = Stub.getDefaultImpl();
                        if (region != 0) {
                            region = Stub.getDefaultImpl().getMagnificationRegion(displayId);
                            return region;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        region = (Region) Region.CREATOR.createFromParcel(_reply);
                    } else {
                        region = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return region;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean resetMagnification(int displayId, boolean animate) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    boolean _result = true;
                    _data.writeInt(animate ? 1 : 0);
                    if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().resetMagnification(displayId, animate);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setMagnificationScaleAndCenter(int displayId, float scale, float centerX, float centerY, boolean animate) throws RemoteException {
                Throwable th;
                float f;
                float f2;
                float f3;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(displayId);
                    } catch (Throwable th2) {
                        th = th2;
                        f = scale;
                        f2 = centerX;
                        f3 = centerY;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeFloat(scale);
                    } catch (Throwable th3) {
                        th = th3;
                        f2 = centerX;
                        f3 = centerY;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        boolean _result;
                        _data.writeFloat(centerX);
                        try {
                            _data.writeFloat(centerY);
                            _result = true;
                            _data.writeInt(animate ? 1 : 0);
                        } catch (Throwable th4) {
                            th = th4;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                if (_reply.readInt() == 0) {
                                    _result = false;
                                }
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            _result = Stub.getDefaultImpl().setMagnificationScaleAndCenter(displayId, scale, centerX, centerY, animate);
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
                        f3 = centerY;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    int i = displayId;
                    f = scale;
                    f2 = centerX;
                    f3 = centerY;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void setMagnificationCallbackEnabled(int displayId, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    _data.writeInt(enabled ? 1 : 0);
                    if (this.mRemote.transact(20, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setMagnificationCallbackEnabled(displayId, enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setSoftKeyboardShowMode(int showMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(showMode);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(21, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().setSoftKeyboardShowMode(showMode);
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

            public int getSoftKeyboardShowMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 22;
                    if (!this.mRemote.transact(22, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getSoftKeyboardShowMode();
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

            public void setSoftKeyboardCallbackEnabled(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    if (this.mRemote.transact(23, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setSoftKeyboardCallbackEnabled(enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isAccessibilityButtonAvailable() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(24, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isAccessibilityButtonAvailable();
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

            public void sendGesture(int sequence, ParceledListSlice gestureSteps) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sequence);
                    if (gestureSteps != null) {
                        _data.writeInt(1);
                        gestureSteps.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(25, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().sendGesture(sequence, gestureSteps);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isFingerprintGestureDetectionAvailable() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(26, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isFingerprintGestureDetectionAvailable();
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAccessibilityServiceConnection asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IAccessibilityServiceConnection)) {
                return new Proxy(obj);
            }
            return (IAccessibilityServiceConnection) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "setServiceInfo";
                case 2:
                    return "findAccessibilityNodeInfoByAccessibilityId";
                case 3:
                    return "findAccessibilityNodeInfosByText";
                case 4:
                    return "findAccessibilityNodeInfosByViewId";
                case 5:
                    return "findFocus";
                case 6:
                    return "focusSearch";
                case 7:
                    return "performAccessibilityAction";
                case 8:
                    return "getWindow";
                case 9:
                    return "getWindows";
                case 10:
                    return "getServiceInfo";
                case 11:
                    return "performGlobalAction";
                case 12:
                    return "disableSelf";
                case 13:
                    return "setOnKeyEventResult";
                case 14:
                    return "getMagnificationScale";
                case 15:
                    return "getMagnificationCenterX";
                case 16:
                    return "getMagnificationCenterY";
                case 17:
                    return "getMagnificationRegion";
                case 18:
                    return "resetMagnification";
                case 19:
                    return "setMagnificationScaleAndCenter";
                case 20:
                    return "setMagnificationCallbackEnabled";
                case 21:
                    return "setSoftKeyboardShowMode";
                case 22:
                    return "getSoftKeyboardShowMode";
                case 23:
                    return "setSoftKeyboardCallbackEnabled";
                case 24:
                    return "isAccessibilityButtonAvailable";
                case 25:
                    return "sendGesture";
                case 26:
                    return "isFingerprintGestureDetectionAvailable";
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
            if (i != 1598968902) {
                boolean _arg1 = false;
                int _arg0;
                long _arg12;
                int _arg2;
                String[] _result;
                boolean _result2;
                float _result3;
                int _arg02;
                int _result4;
                switch (i) {
                    case 1:
                        AccessibilityServiceInfo _arg03;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (AccessibilityServiceInfo) AccessibilityServiceInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        setServiceInfo(_arg03);
                        reply.writeNoException();
                        return true;
                    case 2:
                        Bundle _arg6;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        _arg12 = data.readLong();
                        _arg2 = data.readInt();
                        IAccessibilityInteractionConnectionCallback _arg3 = android.view.accessibility.IAccessibilityInteractionConnectionCallback.Stub.asInterface(data.readStrongBinder());
                        int _arg4 = data.readInt();
                        long _arg5 = data.readLong();
                        if (data.readInt() != 0) {
                            _arg6 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg6 = null;
                        }
                        _result = findAccessibilityNodeInfoByAccessibilityId(_arg0, _arg12, _arg2, _arg3, _arg4, _arg5, _arg6);
                        reply.writeNoException();
                        parcel2.writeStringArray(_result);
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        _result = findAccessibilityNodeInfosByText(data.readInt(), data.readLong(), data.readString(), data.readInt(), android.view.accessibility.IAccessibilityInteractionConnectionCallback.Stub.asInterface(data.readStrongBinder()), data.readLong());
                        reply.writeNoException();
                        parcel2.writeStringArray(_result);
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        _result = findAccessibilityNodeInfosByViewId(data.readInt(), data.readLong(), data.readString(), data.readInt(), android.view.accessibility.IAccessibilityInteractionConnectionCallback.Stub.asInterface(data.readStrongBinder()), data.readLong());
                        reply.writeNoException();
                        parcel2.writeStringArray(_result);
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        _result = findFocus(data.readInt(), data.readLong(), data.readInt(), data.readInt(), android.view.accessibility.IAccessibilityInteractionConnectionCallback.Stub.asInterface(data.readStrongBinder()), data.readLong());
                        reply.writeNoException();
                        parcel2.writeStringArray(_result);
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        _result = focusSearch(data.readInt(), data.readLong(), data.readInt(), data.readInt(), android.view.accessibility.IAccessibilityInteractionConnectionCallback.Stub.asInterface(data.readStrongBinder()), data.readLong());
                        reply.writeNoException();
                        parcel2.writeStringArray(_result);
                        return true;
                    case 7:
                        Bundle _arg32;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        _arg12 = data.readLong();
                        _arg2 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg32 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg32 = null;
                        }
                        _arg1 = performAccessibilityAction(_arg0, _arg12, _arg2, _arg32, data.readInt(), android.view.accessibility.IAccessibilityInteractionConnectionCallback.Stub.asInterface(data.readStrongBinder()), data.readLong());
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        AccessibilityWindowInfo _result5 = getWindow(data.readInt());
                        reply.writeNoException();
                        if (_result5 != null) {
                            parcel2.writeInt(1);
                            _result5.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        List<AccessibilityWindowInfo> _result6 = getWindows();
                        reply.writeNoException();
                        parcel2.writeTypedList(_result6);
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        AccessibilityServiceInfo _result7 = getServiceInfo();
                        reply.writeNoException();
                        if (_result7 != null) {
                            parcel2.writeInt(1);
                            _result7.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        _result2 = performGlobalAction(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        disableSelf();
                        reply.writeNoException();
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setOnKeyEventResult(_arg1, data.readInt());
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        _result3 = getMagnificationScale(data.readInt());
                        reply.writeNoException();
                        parcel2.writeFloat(_result3);
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        _result3 = getMagnificationCenterX(data.readInt());
                        reply.writeNoException();
                        parcel2.writeFloat(_result3);
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        _result3 = getMagnificationCenterY(data.readInt());
                        reply.writeNoException();
                        parcel2.writeFloat(_result3);
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        Region _result8 = getMagnificationRegion(data.readInt());
                        reply.writeNoException();
                        if (_result8 != null) {
                            parcel2.writeInt(1);
                            _result8.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        boolean _result9 = resetMagnification(_arg02, _arg1);
                        reply.writeNoException();
                        parcel2.writeInt(_result9);
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        _arg1 = setMagnificationScaleAndCenter(data.readInt(), data.readFloat(), data.readFloat(), data.readFloat(), data.readInt() != 0);
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setMagnificationCallbackEnabled(_arg02, _arg1);
                        reply.writeNoException();
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        _result2 = setSoftKeyboardShowMode(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        _result4 = getSoftKeyboardShowMode();
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setSoftKeyboardCallbackEnabled(_arg1);
                        reply.writeNoException();
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        _arg1 = isAccessibilityButtonAvailable();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 25:
                        ParceledListSlice _arg13;
                        parcel.enforceInterface(descriptor);
                        _result4 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg13 = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        sendGesture(_result4, _arg13);
                        reply.writeNoException();
                        return true;
                    case 26:
                        parcel.enforceInterface(descriptor);
                        _arg1 = isFingerprintGestureDetectionAvailable();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IAccessibilityServiceConnection impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IAccessibilityServiceConnection getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void disableSelf() throws RemoteException;

    String[] findAccessibilityNodeInfoByAccessibilityId(int i, long j, int i2, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i3, long j2, Bundle bundle) throws RemoteException;

    String[] findAccessibilityNodeInfosByText(int i, long j, String str, int i2, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, long j2) throws RemoteException;

    String[] findAccessibilityNodeInfosByViewId(int i, long j, String str, int i2, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, long j2) throws RemoteException;

    String[] findFocus(int i, long j, int i2, int i3, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, long j2) throws RemoteException;

    String[] focusSearch(int i, long j, int i2, int i3, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, long j2) throws RemoteException;

    float getMagnificationCenterX(int i) throws RemoteException;

    float getMagnificationCenterY(int i) throws RemoteException;

    Region getMagnificationRegion(int i) throws RemoteException;

    float getMagnificationScale(int i) throws RemoteException;

    AccessibilityServiceInfo getServiceInfo() throws RemoteException;

    int getSoftKeyboardShowMode() throws RemoteException;

    AccessibilityWindowInfo getWindow(int i) throws RemoteException;

    List<AccessibilityWindowInfo> getWindows() throws RemoteException;

    boolean isAccessibilityButtonAvailable() throws RemoteException;

    boolean isFingerprintGestureDetectionAvailable() throws RemoteException;

    boolean performAccessibilityAction(int i, long j, int i2, Bundle bundle, int i3, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, long j2) throws RemoteException;

    boolean performGlobalAction(int i) throws RemoteException;

    boolean resetMagnification(int i, boolean z) throws RemoteException;

    void sendGesture(int i, ParceledListSlice parceledListSlice) throws RemoteException;

    void setMagnificationCallbackEnabled(int i, boolean z) throws RemoteException;

    boolean setMagnificationScaleAndCenter(int i, float f, float f2, float f3, boolean z) throws RemoteException;

    void setOnKeyEventResult(boolean z, int i) throws RemoteException;

    void setServiceInfo(AccessibilityServiceInfo accessibilityServiceInfo) throws RemoteException;

    void setSoftKeyboardCallbackEnabled(boolean z) throws RemoteException;

    boolean setSoftKeyboardShowMode(int i) throws RemoteException;
}
