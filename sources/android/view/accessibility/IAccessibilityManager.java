package android.view.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.accessibilityservice.IAccessibilityServiceClient;
import android.annotation.UnsupportedAppUsage;
import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.IWindow;
import java.util.List;

public interface IAccessibilityManager extends IInterface {

    public static class Default implements IAccessibilityManager {
        public void interrupt(int userId) throws RemoteException {
        }

        public void sendAccessibilityEvent(AccessibilityEvent uiEvent, int userId) throws RemoteException {
        }

        public long addClient(IAccessibilityManagerClient client, int userId) throws RemoteException {
            return 0;
        }

        public List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(int userId) throws RemoteException {
            return null;
        }

        public List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(int feedbackType, int userId) throws RemoteException {
            return null;
        }

        public int addAccessibilityInteractionConnection(IWindow windowToken, IAccessibilityInteractionConnection connection, String packageName, int userId) throws RemoteException {
            return 0;
        }

        public void removeAccessibilityInteractionConnection(IWindow windowToken) throws RemoteException {
        }

        public void setPictureInPictureActionReplacingConnection(IAccessibilityInteractionConnection connection) throws RemoteException {
        }

        public void registerUiTestAutomationService(IBinder owner, IAccessibilityServiceClient client, AccessibilityServiceInfo info, int flags) throws RemoteException {
        }

        public void unregisterUiTestAutomationService(IAccessibilityServiceClient client) throws RemoteException {
        }

        public void temporaryEnableAccessibilityStateUntilKeyguardRemoved(ComponentName service, boolean touchExplorationEnabled) throws RemoteException {
        }

        public IBinder getWindowToken(int windowId, int userId) throws RemoteException {
            return null;
        }

        public void notifyAccessibilityButtonClicked(int displayId) throws RemoteException {
        }

        public void notifyAccessibilityButtonVisibilityChanged(boolean available) throws RemoteException {
        }

        public void performAccessibilityShortcut() throws RemoteException {
        }

        public String getAccessibilityShortcutService() throws RemoteException {
            return null;
        }

        public boolean sendFingerprintGesture(int gestureKeyCode) throws RemoteException {
            return false;
        }

        public int getAccessibilityWindowId(IBinder windowToken) throws RemoteException {
            return 0;
        }

        public long getRecommendedTimeoutMillis() throws RemoteException {
            return 0;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IAccessibilityManager {
        private static final String DESCRIPTOR = "android.view.accessibility.IAccessibilityManager";
        static final int TRANSACTION_addAccessibilityInteractionConnection = 6;
        static final int TRANSACTION_addClient = 3;
        static final int TRANSACTION_getAccessibilityShortcutService = 16;
        static final int TRANSACTION_getAccessibilityWindowId = 18;
        static final int TRANSACTION_getEnabledAccessibilityServiceList = 5;
        static final int TRANSACTION_getInstalledAccessibilityServiceList = 4;
        static final int TRANSACTION_getRecommendedTimeoutMillis = 19;
        static final int TRANSACTION_getWindowToken = 12;
        static final int TRANSACTION_interrupt = 1;
        static final int TRANSACTION_notifyAccessibilityButtonClicked = 13;
        static final int TRANSACTION_notifyAccessibilityButtonVisibilityChanged = 14;
        static final int TRANSACTION_performAccessibilityShortcut = 15;
        static final int TRANSACTION_registerUiTestAutomationService = 9;
        static final int TRANSACTION_removeAccessibilityInteractionConnection = 7;
        static final int TRANSACTION_sendAccessibilityEvent = 2;
        static final int TRANSACTION_sendFingerprintGesture = 17;
        static final int TRANSACTION_setPictureInPictureActionReplacingConnection = 8;
        static final int TRANSACTION_temporaryEnableAccessibilityStateUntilKeyguardRemoved = 11;
        static final int TRANSACTION_unregisterUiTestAutomationService = 10;

        private static class Proxy implements IAccessibilityManager {
            public static IAccessibilityManager sDefaultImpl;
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

            public void interrupt(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().interrupt(userId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void sendAccessibilityEvent(AccessibilityEvent uiEvent, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (uiEvent != null) {
                        _data.writeInt(1);
                        uiEvent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().sendAccessibilityEvent(uiEvent, userId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public long addClient(IAccessibilityManagerClient client, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    _data.writeInt(userId);
                    long j = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().addClient(client, userId);
                            return j;
                        }
                    }
                    _reply.readException();
                    j = _reply.readLong();
                    long _result = j;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    List<AccessibilityServiceInfo> list = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getInstalledAccessibilityServiceList(userId);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(AccessibilityServiceInfo.CREATOR);
                    List<AccessibilityServiceInfo> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(int feedbackType, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(feedbackType);
                    _data.writeInt(userId);
                    List<AccessibilityServiceInfo> list = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getEnabledAccessibilityServiceList(feedbackType, userId);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(AccessibilityServiceInfo.CREATOR);
                    List<AccessibilityServiceInfo> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int addAccessibilityInteractionConnection(IWindow windowToken, IAccessibilityInteractionConnection connection, String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IBinder iBinder = null;
                    _data.writeStrongBinder(windowToken != null ? windowToken.asBinder() : null);
                    if (connection != null) {
                        iBinder = connection.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    int i = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().addAccessibilityInteractionConnection(windowToken, connection, packageName, userId);
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

            public void removeAccessibilityInteractionConnection(IWindow windowToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(windowToken != null ? windowToken.asBinder() : null);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeAccessibilityInteractionConnection(windowToken);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPictureInPictureActionReplacingConnection(IAccessibilityInteractionConnection connection) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(connection != null ? connection.asBinder() : null);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPictureInPictureActionReplacingConnection(connection);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerUiTestAutomationService(IBinder owner, IAccessibilityServiceClient client, AccessibilityServiceInfo info, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(owner);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerUiTestAutomationService(owner, client, info, flags);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterUiTestAutomationService(IAccessibilityServiceClient client) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterUiTestAutomationService(client);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void temporaryEnableAccessibilityStateUntilKeyguardRemoved(ComponentName service, boolean touchExplorationEnabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!touchExplorationEnabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().temporaryEnableAccessibilityStateUntilKeyguardRemoved(service, touchExplorationEnabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IBinder getWindowToken(int windowId, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(windowId);
                    _data.writeInt(userId);
                    IBinder iBinder = 12;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        iBinder = Stub.getDefaultImpl();
                        if (iBinder != 0) {
                            iBinder = Stub.getDefaultImpl().getWindowToken(windowId, userId);
                            return iBinder;
                        }
                    }
                    _reply.readException();
                    iBinder = _reply.readStrongBinder();
                    IBinder _result = iBinder;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyAccessibilityButtonClicked(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyAccessibilityButtonClicked(displayId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyAccessibilityButtonVisibilityChanged(boolean available) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(available ? 1 : 0);
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyAccessibilityButtonVisibilityChanged(available);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void performAccessibilityShortcut() throws RemoteException {
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
                    Stub.getDefaultImpl().performAccessibilityShortcut();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getAccessibilityShortcutService() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 16;
                    if (!this.mRemote.transact(16, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getAccessibilityShortcutService();
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean sendFingerprintGesture(int gestureKeyCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(gestureKeyCode);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(17, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().sendFingerprintGesture(gestureKeyCode);
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

            public int getAccessibilityWindowId(IBinder windowToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(windowToken);
                    int i = 18;
                    if (!this.mRemote.transact(18, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getAccessibilityWindowId(windowToken);
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

            public long getRecommendedTimeoutMillis() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    long j = 19;
                    if (!this.mRemote.transact(19, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getRecommendedTimeoutMillis();
                            return j;
                        }
                    }
                    _reply.readException();
                    j = _reply.readLong();
                    long _result = j;
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

        public static IAccessibilityManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IAccessibilityManager)) {
                return new Proxy(obj);
            }
            return (IAccessibilityManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "interrupt";
                case 2:
                    return "sendAccessibilityEvent";
                case 3:
                    return "addClient";
                case 4:
                    return "getInstalledAccessibilityServiceList";
                case 5:
                    return "getEnabledAccessibilityServiceList";
                case 6:
                    return "addAccessibilityInteractionConnection";
                case 7:
                    return "removeAccessibilityInteractionConnection";
                case 8:
                    return "setPictureInPictureActionReplacingConnection";
                case 9:
                    return "registerUiTestAutomationService";
                case 10:
                    return "unregisterUiTestAutomationService";
                case 11:
                    return "temporaryEnableAccessibilityStateUntilKeyguardRemoved";
                case 12:
                    return "getWindowToken";
                case 13:
                    return "notifyAccessibilityButtonClicked";
                case 14:
                    return "notifyAccessibilityButtonVisibilityChanged";
                case 15:
                    return "performAccessibilityShortcut";
                case 16:
                    return "getAccessibilityShortcutService";
                case 17:
                    return "sendFingerprintGesture";
                case 18:
                    return "getAccessibilityWindowId";
                case 19:
                    return "getRecommendedTimeoutMillis";
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
                        interrupt(data.readInt());
                        return true;
                    case 2:
                        AccessibilityEvent _arg02;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (AccessibilityEvent) AccessibilityEvent.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        sendAccessibilityEvent(_arg02, data.readInt());
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        long _result = addClient(android.view.accessibility.IAccessibilityManagerClient.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        reply.writeLong(_result);
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        List<AccessibilityServiceInfo> _result2 = getInstalledAccessibilityServiceList(data.readInt());
                        reply.writeNoException();
                        reply.writeTypedList(_result2);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        List<AccessibilityServiceInfo> _result3 = getEnabledAccessibilityServiceList(data.readInt(), data.readInt());
                        reply.writeNoException();
                        reply.writeTypedList(_result3);
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        int _result4 = addAccessibilityInteractionConnection(android.view.IWindow.Stub.asInterface(data.readStrongBinder()), android.view.accessibility.IAccessibilityInteractionConnection.Stub.asInterface(data.readStrongBinder()), data.readString(), data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        removeAccessibilityInteractionConnection(android.view.IWindow.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        setPictureInPictureActionReplacingConnection(android.view.accessibility.IAccessibilityInteractionConnection.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 9:
                        AccessibilityServiceInfo _arg2;
                        data.enforceInterface(descriptor);
                        IBinder _arg03 = data.readStrongBinder();
                        IAccessibilityServiceClient _arg1 = android.accessibilityservice.IAccessibilityServiceClient.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg2 = (AccessibilityServiceInfo) AccessibilityServiceInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg2 = null;
                        }
                        registerUiTestAutomationService(_arg03, _arg1, _arg2, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        unregisterUiTestAutomationService(android.accessibilityservice.IAccessibilityServiceClient.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 11:
                        ComponentName _arg04;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                        } else {
                            _arg04 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        temporaryEnableAccessibilityStateUntilKeyguardRemoved(_arg04, _arg0);
                        reply.writeNoException();
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        IBinder _result5 = getWindowToken(data.readInt(), data.readInt());
                        reply.writeNoException();
                        reply.writeStrongBinder(_result5);
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        notifyAccessibilityButtonClicked(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 14:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        notifyAccessibilityButtonVisibilityChanged(_arg0);
                        reply.writeNoException();
                        return true;
                    case 15:
                        data.enforceInterface(descriptor);
                        performAccessibilityShortcut();
                        reply.writeNoException();
                        return true;
                    case 16:
                        data.enforceInterface(descriptor);
                        String _result6 = getAccessibilityShortcutService();
                        reply.writeNoException();
                        reply.writeString(_result6);
                        return true;
                    case 17:
                        data.enforceInterface(descriptor);
                        boolean _result7 = sendFingerprintGesture(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result7);
                        return true;
                    case 18:
                        data.enforceInterface(descriptor);
                        int _result8 = getAccessibilityWindowId(data.readStrongBinder());
                        reply.writeNoException();
                        reply.writeInt(_result8);
                        return true;
                    case 19:
                        data.enforceInterface(descriptor);
                        long _result9 = getRecommendedTimeoutMillis();
                        reply.writeNoException();
                        reply.writeLong(_result9);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IAccessibilityManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IAccessibilityManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    int addAccessibilityInteractionConnection(IWindow iWindow, IAccessibilityInteractionConnection iAccessibilityInteractionConnection, String str, int i) throws RemoteException;

    long addClient(IAccessibilityManagerClient iAccessibilityManagerClient, int i) throws RemoteException;

    String getAccessibilityShortcutService() throws RemoteException;

    int getAccessibilityWindowId(IBinder iBinder) throws RemoteException;

    @UnsupportedAppUsage
    List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(int i, int i2) throws RemoteException;

    List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(int i) throws RemoteException;

    long getRecommendedTimeoutMillis() throws RemoteException;

    IBinder getWindowToken(int i, int i2) throws RemoteException;

    void interrupt(int i) throws RemoteException;

    void notifyAccessibilityButtonClicked(int i) throws RemoteException;

    void notifyAccessibilityButtonVisibilityChanged(boolean z) throws RemoteException;

    void performAccessibilityShortcut() throws RemoteException;

    void registerUiTestAutomationService(IBinder iBinder, IAccessibilityServiceClient iAccessibilityServiceClient, AccessibilityServiceInfo accessibilityServiceInfo, int i) throws RemoteException;

    void removeAccessibilityInteractionConnection(IWindow iWindow) throws RemoteException;

    void sendAccessibilityEvent(AccessibilityEvent accessibilityEvent, int i) throws RemoteException;

    boolean sendFingerprintGesture(int i) throws RemoteException;

    void setPictureInPictureActionReplacingConnection(IAccessibilityInteractionConnection iAccessibilityInteractionConnection) throws RemoteException;

    void temporaryEnableAccessibilityStateUntilKeyguardRemoved(ComponentName componentName, boolean z) throws RemoteException;

    void unregisterUiTestAutomationService(IAccessibilityServiceClient iAccessibilityServiceClient) throws RemoteException;
}
