package android.app;

import android.app.ActivityManager.RunningTaskInfo;
import android.app.ActivityManager.TaskSnapshot;
import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ITaskStackListener extends IInterface {
    public static final int FORCED_RESIZEABLE_REASON_SECONDARY_DISPLAY = 2;
    public static final int FORCED_RESIZEABLE_REASON_SPLIT_SCREEN = 1;

    public static abstract class Stub extends Binder implements ITaskStackListener {
        private static final String DESCRIPTOR = "android.app.ITaskStackListener";
        static final int TRANSACTION_onActivityDismissingDockedStack = 8;
        static final int TRANSACTION_onActivityForcedResizable = 7;
        static final int TRANSACTION_onActivityLaunchOnSecondaryDisplayFailed = 9;
        static final int TRANSACTION_onActivityLaunchOnSecondaryDisplayRerouted = 10;
        static final int TRANSACTION_onActivityPinned = 2;
        static final int TRANSACTION_onActivityRequestedOrientationChanged = 15;
        static final int TRANSACTION_onActivityUnpinned = 3;
        static final int TRANSACTION_onBackPressedOnTaskRoot = 20;
        static final int TRANSACTION_onPinnedActivityRestartAttempt = 4;
        static final int TRANSACTION_onPinnedStackAnimationEnded = 6;
        static final int TRANSACTION_onPinnedStackAnimationStarted = 5;
        static final int TRANSACTION_onSizeCompatModeActivityChanged = 19;
        static final int TRANSACTION_onTaskCreated = 11;
        static final int TRANSACTION_onTaskDescriptionChanged = 14;
        static final int TRANSACTION_onTaskDisplayChanged = 21;
        static final int TRANSACTION_onTaskMovedToFront = 13;
        static final int TRANSACTION_onTaskProfileLocked = 17;
        static final int TRANSACTION_onTaskRemovalStarted = 16;
        static final int TRANSACTION_onTaskRemoved = 12;
        static final int TRANSACTION_onTaskSnapshotChanged = 18;
        static final int TRANSACTION_onTaskStackChanged = 1;

        private static class Proxy implements ITaskStackListener {
            public static ITaskStackListener sDefaultImpl;
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

            public void onTaskStackChanged() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onTaskStackChanged();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onActivityPinned(String packageName, int userId, int taskId, int stackId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeInt(taskId);
                    _data.writeInt(stackId);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onActivityPinned(packageName, userId, taskId, stackId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onActivityUnpinned() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onActivityUnpinned();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onPinnedActivityRestartAttempt(boolean clearedTask) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clearedTask ? 1 : 0);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onPinnedActivityRestartAttempt(clearedTask);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onPinnedStackAnimationStarted() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onPinnedStackAnimationStarted();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onPinnedStackAnimationEnded() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onPinnedStackAnimationEnded();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onActivityForcedResizable(String packageName, int taskId, int reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(taskId);
                    _data.writeInt(reason);
                    if (this.mRemote.transact(7, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onActivityForcedResizable(packageName, taskId, reason);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onActivityDismissingDockedStack() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(8, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onActivityDismissingDockedStack();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onActivityLaunchOnSecondaryDisplayFailed(RunningTaskInfo taskInfo, int requestedDisplayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (taskInfo != null) {
                        _data.writeInt(1);
                        taskInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(requestedDisplayId);
                    if (this.mRemote.transact(9, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onActivityLaunchOnSecondaryDisplayFailed(taskInfo, requestedDisplayId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onActivityLaunchOnSecondaryDisplayRerouted(RunningTaskInfo taskInfo, int requestedDisplayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (taskInfo != null) {
                        _data.writeInt(1);
                        taskInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(requestedDisplayId);
                    if (this.mRemote.transact(10, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onActivityLaunchOnSecondaryDisplayRerouted(taskInfo, requestedDisplayId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onTaskCreated(int taskId, ComponentName componentName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    if (componentName != null) {
                        _data.writeInt(1);
                        componentName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(11, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onTaskCreated(taskId, componentName);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onTaskRemoved(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    if (this.mRemote.transact(12, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onTaskRemoved(taskId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onTaskMovedToFront(RunningTaskInfo taskInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (taskInfo != null) {
                        _data.writeInt(1);
                        taskInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(13, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onTaskMovedToFront(taskInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onTaskDescriptionChanged(RunningTaskInfo taskInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (taskInfo != null) {
                        _data.writeInt(1);
                        taskInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(14, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onTaskDescriptionChanged(taskInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onActivityRequestedOrientationChanged(int taskId, int requestedOrientation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(requestedOrientation);
                    if (this.mRemote.transact(15, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onActivityRequestedOrientationChanged(taskId, requestedOrientation);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onTaskRemovalStarted(RunningTaskInfo taskInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (taskInfo != null) {
                        _data.writeInt(1);
                        taskInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(16, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onTaskRemovalStarted(taskInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onTaskProfileLocked(int taskId, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(17, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onTaskProfileLocked(taskId, userId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onTaskSnapshotChanged(int taskId, TaskSnapshot snapshot) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    if (snapshot != null) {
                        _data.writeInt(1);
                        snapshot.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(18, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onTaskSnapshotChanged(taskId, snapshot);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onSizeCompatModeActivityChanged(int displayId, IBinder activityToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    _data.writeStrongBinder(activityToken);
                    if (this.mRemote.transact(19, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onSizeCompatModeActivityChanged(displayId, activityToken);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onBackPressedOnTaskRoot(RunningTaskInfo taskInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (taskInfo != null) {
                        _data.writeInt(1);
                        taskInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(20, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onBackPressedOnTaskRoot(taskInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onTaskDisplayChanged(int taskId, int newDisplayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(newDisplayId);
                    if (this.mRemote.transact(21, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onTaskDisplayChanged(taskId, newDisplayId);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ITaskStackListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ITaskStackListener)) {
                return new Proxy(obj);
            }
            return (ITaskStackListener) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "onTaskStackChanged";
                case 2:
                    return "onActivityPinned";
                case 3:
                    return "onActivityUnpinned";
                case 4:
                    return "onPinnedActivityRestartAttempt";
                case 5:
                    return "onPinnedStackAnimationStarted";
                case 6:
                    return "onPinnedStackAnimationEnded";
                case 7:
                    return "onActivityForcedResizable";
                case 8:
                    return "onActivityDismissingDockedStack";
                case 9:
                    return "onActivityLaunchOnSecondaryDisplayFailed";
                case 10:
                    return "onActivityLaunchOnSecondaryDisplayRerouted";
                case 11:
                    return "onTaskCreated";
                case 12:
                    return "onTaskRemoved";
                case 13:
                    return "onTaskMovedToFront";
                case 14:
                    return "onTaskDescriptionChanged";
                case 15:
                    return "onActivityRequestedOrientationChanged";
                case 16:
                    return "onTaskRemovalStarted";
                case 17:
                    return "onTaskProfileLocked";
                case 18:
                    return "onTaskSnapshotChanged";
                case 19:
                    return "onSizeCompatModeActivityChanged";
                case 20:
                    return "onBackPressedOnTaskRoot";
                case 21:
                    return "onTaskDisplayChanged";
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
                RunningTaskInfo _arg0;
                int _arg02;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        onTaskStackChanged();
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        onActivityPinned(data.readString(), data.readInt(), data.readInt(), data.readInt());
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        onActivityUnpinned();
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        onPinnedActivityRestartAttempt(data.readInt() != 0);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        onPinnedStackAnimationStarted();
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        onPinnedStackAnimationEnded();
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        onActivityForcedResizable(data.readString(), data.readInt(), data.readInt());
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        onActivityDismissingDockedStack();
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (RunningTaskInfo) RunningTaskInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        onActivityLaunchOnSecondaryDisplayFailed(_arg0, data.readInt());
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (RunningTaskInfo) RunningTaskInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        onActivityLaunchOnSecondaryDisplayRerouted(_arg0, data.readInt());
                        return true;
                    case 11:
                        ComponentName _arg1;
                        data.enforceInterface(descriptor);
                        _arg02 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        onTaskCreated(_arg02, _arg1);
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        onTaskRemoved(data.readInt());
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (RunningTaskInfo) RunningTaskInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        onTaskMovedToFront(_arg0);
                        return true;
                    case 14:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (RunningTaskInfo) RunningTaskInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        onTaskDescriptionChanged(_arg0);
                        return true;
                    case 15:
                        data.enforceInterface(descriptor);
                        onActivityRequestedOrientationChanged(data.readInt(), data.readInt());
                        return true;
                    case 16:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (RunningTaskInfo) RunningTaskInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        onTaskRemovalStarted(_arg0);
                        return true;
                    case 17:
                        data.enforceInterface(descriptor);
                        onTaskProfileLocked(data.readInt(), data.readInt());
                        return true;
                    case 18:
                        TaskSnapshot _arg12;
                        data.enforceInterface(descriptor);
                        _arg02 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg12 = (TaskSnapshot) TaskSnapshot.CREATOR.createFromParcel(data);
                        } else {
                            _arg12 = null;
                        }
                        onTaskSnapshotChanged(_arg02, _arg12);
                        return true;
                    case 19:
                        data.enforceInterface(descriptor);
                        onSizeCompatModeActivityChanged(data.readInt(), data.readStrongBinder());
                        return true;
                    case 20:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (RunningTaskInfo) RunningTaskInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        onBackPressedOnTaskRoot(_arg0);
                        return true;
                    case 21:
                        data.enforceInterface(descriptor);
                        onTaskDisplayChanged(data.readInt(), data.readInt());
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(ITaskStackListener impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ITaskStackListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    public static class Default implements ITaskStackListener {
        public void onTaskStackChanged() throws RemoteException {
        }

        public void onActivityPinned(String packageName, int userId, int taskId, int stackId) throws RemoteException {
        }

        public void onActivityUnpinned() throws RemoteException {
        }

        public void onPinnedActivityRestartAttempt(boolean clearedTask) throws RemoteException {
        }

        public void onPinnedStackAnimationStarted() throws RemoteException {
        }

        public void onPinnedStackAnimationEnded() throws RemoteException {
        }

        public void onActivityForcedResizable(String packageName, int taskId, int reason) throws RemoteException {
        }

        public void onActivityDismissingDockedStack() throws RemoteException {
        }

        public void onActivityLaunchOnSecondaryDisplayFailed(RunningTaskInfo taskInfo, int requestedDisplayId) throws RemoteException {
        }

        public void onActivityLaunchOnSecondaryDisplayRerouted(RunningTaskInfo taskInfo, int requestedDisplayId) throws RemoteException {
        }

        public void onTaskCreated(int taskId, ComponentName componentName) throws RemoteException {
        }

        public void onTaskRemoved(int taskId) throws RemoteException {
        }

        public void onTaskMovedToFront(RunningTaskInfo taskInfo) throws RemoteException {
        }

        public void onTaskDescriptionChanged(RunningTaskInfo taskInfo) throws RemoteException {
        }

        public void onActivityRequestedOrientationChanged(int taskId, int requestedOrientation) throws RemoteException {
        }

        public void onTaskRemovalStarted(RunningTaskInfo taskInfo) throws RemoteException {
        }

        public void onTaskProfileLocked(int taskId, int userId) throws RemoteException {
        }

        public void onTaskSnapshotChanged(int taskId, TaskSnapshot snapshot) throws RemoteException {
        }

        public void onSizeCompatModeActivityChanged(int displayId, IBinder activityToken) throws RemoteException {
        }

        public void onBackPressedOnTaskRoot(RunningTaskInfo taskInfo) throws RemoteException {
        }

        public void onTaskDisplayChanged(int taskId, int newDisplayId) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    void onActivityDismissingDockedStack() throws RemoteException;

    void onActivityForcedResizable(String str, int i, int i2) throws RemoteException;

    void onActivityLaunchOnSecondaryDisplayFailed(RunningTaskInfo runningTaskInfo, int i) throws RemoteException;

    void onActivityLaunchOnSecondaryDisplayRerouted(RunningTaskInfo runningTaskInfo, int i) throws RemoteException;

    void onActivityPinned(String str, int i, int i2, int i3) throws RemoteException;

    void onActivityRequestedOrientationChanged(int i, int i2) throws RemoteException;

    void onActivityUnpinned() throws RemoteException;

    void onBackPressedOnTaskRoot(RunningTaskInfo runningTaskInfo) throws RemoteException;

    void onPinnedActivityRestartAttempt(boolean z) throws RemoteException;

    void onPinnedStackAnimationEnded() throws RemoteException;

    void onPinnedStackAnimationStarted() throws RemoteException;

    void onSizeCompatModeActivityChanged(int i, IBinder iBinder) throws RemoteException;

    void onTaskCreated(int i, ComponentName componentName) throws RemoteException;

    void onTaskDescriptionChanged(RunningTaskInfo runningTaskInfo) throws RemoteException;

    void onTaskDisplayChanged(int i, int i2) throws RemoteException;

    void onTaskMovedToFront(RunningTaskInfo runningTaskInfo) throws RemoteException;

    void onTaskProfileLocked(int i, int i2) throws RemoteException;

    void onTaskRemovalStarted(RunningTaskInfo runningTaskInfo) throws RemoteException;

    void onTaskRemoved(int i) throws RemoteException;

    void onTaskSnapshotChanged(int i, TaskSnapshot taskSnapshot) throws RemoteException;

    void onTaskStackChanged() throws RemoteException;
}
