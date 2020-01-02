package com.android.internal.policy;

import android.annotation.UnsupportedAppUsage;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.text.TextUtils;

public interface IKeyguardService extends IInterface {

    public static class Default implements IKeyguardService {
        public void setOccluded(boolean isOccluded, boolean animate) throws RemoteException {
        }

        public void addStateMonitorCallback(IKeyguardStateCallback callback) throws RemoteException {
        }

        public void verifyUnlock(IKeyguardExitCallback callback) throws RemoteException {
        }

        public void dismiss(IKeyguardDismissCallback callback, CharSequence message) throws RemoteException {
        }

        public void onDreamingStarted() throws RemoteException {
        }

        public void onDreamingStopped() throws RemoteException {
        }

        public void onStartedGoingToSleep(int reason) throws RemoteException {
        }

        public void onFinishedGoingToSleep(int reason, boolean cameraGestureTriggered) throws RemoteException {
        }

        public void onStartedWakingUp() throws RemoteException {
        }

        public void onFinishedWakingUp() throws RemoteException {
        }

        public void onScreenTurningOn(IKeyguardDrawnCallback callback) throws RemoteException {
        }

        public void onScreenTurnedOn() throws RemoteException {
        }

        public void onScreenTurningOff() throws RemoteException {
        }

        public void onScreenTurnedOff() throws RemoteException {
        }

        public void setKeyguardEnabled(boolean enabled) throws RemoteException {
        }

        public void onSystemReady() throws RemoteException {
        }

        public void doKeyguardTimeout(Bundle options) throws RemoteException {
        }

        public void setSwitchingUser(boolean switching) throws RemoteException {
        }

        public void setCurrentUser(int userId) throws RemoteException {
        }

        public void onBootCompleted() throws RemoteException {
        }

        public void OnDoubleClickHome() throws RemoteException {
        }

        public void startKeyguardExitAnimation(long startTime, long fadeoutDuration) throws RemoteException {
        }

        public void onShortPowerPressedGoHome() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IKeyguardService {
        private static final String DESCRIPTOR = "com.android.internal.policy.IKeyguardService";
        static final int TRANSACTION_OnDoubleClickHome = 21;
        static final int TRANSACTION_addStateMonitorCallback = 2;
        static final int TRANSACTION_dismiss = 4;
        static final int TRANSACTION_doKeyguardTimeout = 17;
        static final int TRANSACTION_onBootCompleted = 20;
        static final int TRANSACTION_onDreamingStarted = 5;
        static final int TRANSACTION_onDreamingStopped = 6;
        static final int TRANSACTION_onFinishedGoingToSleep = 8;
        static final int TRANSACTION_onFinishedWakingUp = 10;
        static final int TRANSACTION_onScreenTurnedOff = 14;
        static final int TRANSACTION_onScreenTurnedOn = 12;
        static final int TRANSACTION_onScreenTurningOff = 13;
        static final int TRANSACTION_onScreenTurningOn = 11;
        static final int TRANSACTION_onShortPowerPressedGoHome = 23;
        static final int TRANSACTION_onStartedGoingToSleep = 7;
        static final int TRANSACTION_onStartedWakingUp = 9;
        static final int TRANSACTION_onSystemReady = 16;
        static final int TRANSACTION_setCurrentUser = 19;
        static final int TRANSACTION_setKeyguardEnabled = 15;
        static final int TRANSACTION_setOccluded = 1;
        static final int TRANSACTION_setSwitchingUser = 18;
        static final int TRANSACTION_startKeyguardExitAnimation = 22;
        static final int TRANSACTION_verifyUnlock = 3;

        private static class Proxy implements IKeyguardService {
            public static IKeyguardService sDefaultImpl;
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

            public void setOccluded(boolean isOccluded, boolean animate) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    _data.writeInt(isOccluded ? 1 : 0);
                    if (animate) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setOccluded(isOccluded, animate);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void addStateMonitorCallback(IKeyguardStateCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().addStateMonitorCallback(callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void verifyUnlock(IKeyguardExitCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().verifyUnlock(callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void dismiss(IKeyguardDismissCallback callback, CharSequence message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (message != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(message, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().dismiss(callback, message);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onDreamingStarted() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onDreamingStarted();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onDreamingStopped() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onDreamingStopped();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onStartedGoingToSleep(int reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(reason);
                    if (this.mRemote.transact(7, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onStartedGoingToSleep(reason);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onFinishedGoingToSleep(int reason, boolean cameraGestureTriggered) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(reason);
                    _data.writeInt(cameraGestureTriggered ? 1 : 0);
                    if (this.mRemote.transact(8, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onFinishedGoingToSleep(reason, cameraGestureTriggered);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onStartedWakingUp() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(9, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onStartedWakingUp();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onFinishedWakingUp() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(10, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onFinishedWakingUp();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onScreenTurningOn(IKeyguardDrawnCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(11, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onScreenTurningOn(callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onScreenTurnedOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(12, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onScreenTurnedOn();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onScreenTurningOff() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(13, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onScreenTurningOff();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onScreenTurnedOff() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(14, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onScreenTurnedOff();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setKeyguardEnabled(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    if (this.mRemote.transact(15, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setKeyguardEnabled(enabled);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onSystemReady() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(16, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onSystemReady();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void doKeyguardTimeout(Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(17, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().doKeyguardTimeout(options);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setSwitchingUser(boolean switching) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(switching ? 1 : 0);
                    if (this.mRemote.transact(18, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setSwitchingUser(switching);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setCurrentUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(19, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setCurrentUser(userId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onBootCompleted() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(20, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onBootCompleted();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void OnDoubleClickHome() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(21, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().OnDoubleClickHome();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void startKeyguardExitAnimation(long startTime, long fadeoutDuration) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(startTime);
                    _data.writeLong(fadeoutDuration);
                    if (this.mRemote.transact(22, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().startKeyguardExitAnimation(startTime, fadeoutDuration);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onShortPowerPressedGoHome() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(23, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onShortPowerPressedGoHome();
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IKeyguardService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IKeyguardService)) {
                return new Proxy(obj);
            }
            return (IKeyguardService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "setOccluded";
                case 2:
                    return "addStateMonitorCallback";
                case 3:
                    return "verifyUnlock";
                case 4:
                    return "dismiss";
                case 5:
                    return "onDreamingStarted";
                case 6:
                    return "onDreamingStopped";
                case 7:
                    return "onStartedGoingToSleep";
                case 8:
                    return "onFinishedGoingToSleep";
                case 9:
                    return "onStartedWakingUp";
                case 10:
                    return "onFinishedWakingUp";
                case 11:
                    return "onScreenTurningOn";
                case 12:
                    return "onScreenTurnedOn";
                case 13:
                    return "onScreenTurningOff";
                case 14:
                    return "onScreenTurnedOff";
                case 15:
                    return "setKeyguardEnabled";
                case 16:
                    return "onSystemReady";
                case 17:
                    return "doKeyguardTimeout";
                case 18:
                    return "setSwitchingUser";
                case 19:
                    return "setCurrentUser";
                case 20:
                    return "onBootCompleted";
                case 21:
                    return "OnDoubleClickHome";
                case 22:
                    return "startKeyguardExitAnimation";
                case 23:
                    return "onShortPowerPressedGoHome";
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
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        boolean _arg0 = data.readInt() != 0;
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setOccluded(_arg0, _arg1);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        addStateMonitorCallback(com.android.internal.policy.IKeyguardStateCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        verifyUnlock(com.android.internal.policy.IKeyguardExitCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 4:
                        CharSequence _arg12;
                        data.enforceInterface(descriptor);
                        IKeyguardDismissCallback _arg02 = com.android.internal.policy.IKeyguardDismissCallback.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg12 = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data);
                        } else {
                            _arg12 = null;
                        }
                        dismiss(_arg02, _arg12);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        onDreamingStarted();
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        onDreamingStopped();
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        onStartedGoingToSleep(data.readInt());
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        int _arg03 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        onFinishedGoingToSleep(_arg03, _arg1);
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        onStartedWakingUp();
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        onFinishedWakingUp();
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        onScreenTurningOn(com.android.internal.policy.IKeyguardDrawnCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        onScreenTurnedOn();
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        onScreenTurningOff();
                        return true;
                    case 14:
                        data.enforceInterface(descriptor);
                        onScreenTurnedOff();
                        return true;
                    case 15:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setKeyguardEnabled(_arg1);
                        return true;
                    case 16:
                        data.enforceInterface(descriptor);
                        onSystemReady();
                        return true;
                    case 17:
                        Bundle _arg04;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                        } else {
                            _arg04 = null;
                        }
                        doKeyguardTimeout(_arg04);
                        return true;
                    case 18:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setSwitchingUser(_arg1);
                        return true;
                    case 19:
                        data.enforceInterface(descriptor);
                        setCurrentUser(data.readInt());
                        return true;
                    case 20:
                        data.enforceInterface(descriptor);
                        onBootCompleted();
                        return true;
                    case 21:
                        data.enforceInterface(descriptor);
                        OnDoubleClickHome();
                        return true;
                    case 22:
                        data.enforceInterface(descriptor);
                        startKeyguardExitAnimation(data.readLong(), data.readLong());
                        return true;
                    case 23:
                        data.enforceInterface(descriptor);
                        onShortPowerPressedGoHome();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IKeyguardService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IKeyguardService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void OnDoubleClickHome() throws RemoteException;

    void addStateMonitorCallback(IKeyguardStateCallback iKeyguardStateCallback) throws RemoteException;

    void dismiss(IKeyguardDismissCallback iKeyguardDismissCallback, CharSequence charSequence) throws RemoteException;

    @UnsupportedAppUsage
    void doKeyguardTimeout(Bundle bundle) throws RemoteException;

    void onBootCompleted() throws RemoteException;

    void onDreamingStarted() throws RemoteException;

    void onDreamingStopped() throws RemoteException;

    void onFinishedGoingToSleep(int i, boolean z) throws RemoteException;

    void onFinishedWakingUp() throws RemoteException;

    void onScreenTurnedOff() throws RemoteException;

    void onScreenTurnedOn() throws RemoteException;

    void onScreenTurningOff() throws RemoteException;

    void onScreenTurningOn(IKeyguardDrawnCallback iKeyguardDrawnCallback) throws RemoteException;

    void onShortPowerPressedGoHome() throws RemoteException;

    void onStartedGoingToSleep(int i) throws RemoteException;

    void onStartedWakingUp() throws RemoteException;

    void onSystemReady() throws RemoteException;

    void setCurrentUser(int i) throws RemoteException;

    @UnsupportedAppUsage
    void setKeyguardEnabled(boolean z) throws RemoteException;

    void setOccluded(boolean z, boolean z2) throws RemoteException;

    void setSwitchingUser(boolean z) throws RemoteException;

    void startKeyguardExitAnimation(long j, long j2) throws RemoteException;

    void verifyUnlock(IKeyguardExitCallback iKeyguardExitCallback) throws RemoteException;
}
