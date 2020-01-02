package android.hardware.input;

import android.annotation.UnsupportedAppUsage;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.InputDevice;
import android.view.InputEvent;
import android.view.InputMonitor;
import android.view.PointerIcon;

public interface IInputManager extends IInterface {

    public static class Default implements IInputManager {
        public InputDevice getInputDevice(int deviceId) throws RemoteException {
            return null;
        }

        public int[] getInputDeviceIds() throws RemoteException {
            return null;
        }

        public boolean isInputDeviceEnabled(int deviceId) throws RemoteException {
            return false;
        }

        public void enableInputDevice(int deviceId) throws RemoteException {
        }

        public void disableInputDevice(int deviceId) throws RemoteException {
        }

        public boolean hasKeys(int deviceId, int sourceMask, int[] keyCodes, boolean[] keyExists) throws RemoteException {
            return false;
        }

        public void tryPointerSpeed(int speed) throws RemoteException {
        }

        public boolean injectInputEvent(InputEvent ev, int mode) throws RemoteException {
            return false;
        }

        public TouchCalibration getTouchCalibrationForInputDevice(String inputDeviceDescriptor, int rotation) throws RemoteException {
            return null;
        }

        public void setTouchCalibrationForInputDevice(String inputDeviceDescriptor, int rotation, TouchCalibration calibration) throws RemoteException {
        }

        public KeyboardLayout[] getKeyboardLayouts() throws RemoteException {
            return null;
        }

        public KeyboardLayout[] getKeyboardLayoutsForInputDevice(InputDeviceIdentifier identifier) throws RemoteException {
            return null;
        }

        public KeyboardLayout getKeyboardLayout(String keyboardLayoutDescriptor) throws RemoteException {
            return null;
        }

        public String getCurrentKeyboardLayoutForInputDevice(InputDeviceIdentifier identifier) throws RemoteException {
            return null;
        }

        public void setCurrentKeyboardLayoutForInputDevice(InputDeviceIdentifier identifier, String keyboardLayoutDescriptor) throws RemoteException {
        }

        public String[] getEnabledKeyboardLayoutsForInputDevice(InputDeviceIdentifier identifier) throws RemoteException {
            return null;
        }

        public void addKeyboardLayoutForInputDevice(InputDeviceIdentifier identifier, String keyboardLayoutDescriptor) throws RemoteException {
        }

        public void removeKeyboardLayoutForInputDevice(InputDeviceIdentifier identifier, String keyboardLayoutDescriptor) throws RemoteException {
        }

        public void registerInputDevicesChangedListener(IInputDevicesChangedListener listener) throws RemoteException {
        }

        public int isInTabletMode() throws RemoteException {
            return 0;
        }

        public void registerTabletModeChangedListener(ITabletModeChangedListener listener) throws RemoteException {
        }

        public void vibrate(int deviceId, long[] pattern, int repeat, IBinder token) throws RemoteException {
        }

        public void cancelVibrate(int deviceId, IBinder token) throws RemoteException {
        }

        public void setPointerIconType(int typeId) throws RemoteException {
        }

        public void setCustomPointerIcon(PointerIcon icon) throws RemoteException {
        }

        public void requestPointerCapture(IBinder windowToken, boolean enabled) throws RemoteException {
        }

        public InputMonitor monitorGestureInput(String name, int displayId) throws RemoteException {
            return null;
        }

        public void switchTouchSensitiveMode(boolean modeOn) throws RemoteException {
        }

        public void switchTouchStylusMode(boolean modeOn) throws RemoteException {
        }

        public void switchTouchWakeupMode(boolean modeOn) throws RemoteException {
        }

        public void switchTouchCoverMode(boolean modeOn) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IInputManager {
        private static final String DESCRIPTOR = "android.hardware.input.IInputManager";
        static final int TRANSACTION_addKeyboardLayoutForInputDevice = 17;
        static final int TRANSACTION_cancelVibrate = 23;
        static final int TRANSACTION_disableInputDevice = 5;
        static final int TRANSACTION_enableInputDevice = 4;
        static final int TRANSACTION_getCurrentKeyboardLayoutForInputDevice = 14;
        static final int TRANSACTION_getEnabledKeyboardLayoutsForInputDevice = 16;
        static final int TRANSACTION_getInputDevice = 1;
        static final int TRANSACTION_getInputDeviceIds = 2;
        static final int TRANSACTION_getKeyboardLayout = 13;
        static final int TRANSACTION_getKeyboardLayouts = 11;
        static final int TRANSACTION_getKeyboardLayoutsForInputDevice = 12;
        static final int TRANSACTION_getTouchCalibrationForInputDevice = 9;
        static final int TRANSACTION_hasKeys = 6;
        static final int TRANSACTION_injectInputEvent = 8;
        static final int TRANSACTION_isInTabletMode = 20;
        static final int TRANSACTION_isInputDeviceEnabled = 3;
        static final int TRANSACTION_monitorGestureInput = 27;
        static final int TRANSACTION_registerInputDevicesChangedListener = 19;
        static final int TRANSACTION_registerTabletModeChangedListener = 21;
        static final int TRANSACTION_removeKeyboardLayoutForInputDevice = 18;
        static final int TRANSACTION_requestPointerCapture = 26;
        static final int TRANSACTION_setCurrentKeyboardLayoutForInputDevice = 15;
        static final int TRANSACTION_setCustomPointerIcon = 25;
        static final int TRANSACTION_setPointerIconType = 24;
        static final int TRANSACTION_setTouchCalibrationForInputDevice = 10;
        static final int TRANSACTION_switchTouchCoverMode = 31;
        static final int TRANSACTION_switchTouchSensitiveMode = 28;
        static final int TRANSACTION_switchTouchStylusMode = 29;
        static final int TRANSACTION_switchTouchWakeupMode = 30;
        static final int TRANSACTION_tryPointerSpeed = 7;
        static final int TRANSACTION_vibrate = 22;

        private static class Proxy implements IInputManager {
            public static IInputManager sDefaultImpl;
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

            public InputDevice getInputDevice(int deviceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(deviceId);
                    InputDevice inputDevice = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        inputDevice = Stub.getDefaultImpl();
                        if (inputDevice != 0) {
                            inputDevice = Stub.getDefaultImpl().getInputDevice(deviceId);
                            return inputDevice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        inputDevice = (InputDevice) InputDevice.CREATOR.createFromParcel(_reply);
                    } else {
                        inputDevice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return inputDevice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] getInputDeviceIds() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int[] iArr = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        iArr = Stub.getDefaultImpl();
                        if (iArr != 0) {
                            iArr = Stub.getDefaultImpl().getInputDeviceIds();
                            return iArr;
                        }
                    }
                    _reply.readException();
                    iArr = _reply.createIntArray();
                    int[] _result = iArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isInputDeviceEnabled(int deviceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(deviceId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isInputDeviceEnabled(deviceId);
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

            public void enableInputDevice(int deviceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(deviceId);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().enableInputDevice(deviceId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disableInputDevice(int deviceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(deviceId);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().disableInputDevice(deviceId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean hasKeys(int deviceId, int sourceMask, int[] keyCodes, boolean[] keyExists) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(deviceId);
                    _data.writeInt(sourceMask);
                    _data.writeIntArray(keyCodes);
                    if (keyExists == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(keyExists.length);
                    }
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().hasKeys(deviceId, sourceMask, keyCodes, keyExists);
                            return z;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z2 = true;
                    }
                    z = z2;
                    _reply.readBooleanArray(keyExists);
                    _reply.recycle();
                    _data.recycle();
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void tryPointerSpeed(int speed) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(speed);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().tryPointerSpeed(speed);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean injectInputEvent(InputEvent ev, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (ev != null) {
                        _data.writeInt(1);
                        ev.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(mode);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().injectInputEvent(ev, mode);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public TouchCalibration getTouchCalibrationForInputDevice(String inputDeviceDescriptor, int rotation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(inputDeviceDescriptor);
                    _data.writeInt(rotation);
                    TouchCalibration touchCalibration = 9;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        touchCalibration = Stub.getDefaultImpl();
                        if (touchCalibration != 0) {
                            touchCalibration = Stub.getDefaultImpl().getTouchCalibrationForInputDevice(inputDeviceDescriptor, rotation);
                            return touchCalibration;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        touchCalibration = (TouchCalibration) TouchCalibration.CREATOR.createFromParcel(_reply);
                    } else {
                        touchCalibration = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return touchCalibration;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setTouchCalibrationForInputDevice(String inputDeviceDescriptor, int rotation, TouchCalibration calibration) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(inputDeviceDescriptor);
                    _data.writeInt(rotation);
                    if (calibration != null) {
                        _data.writeInt(1);
                        calibration.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setTouchCalibrationForInputDevice(inputDeviceDescriptor, rotation, calibration);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public KeyboardLayout[] getKeyboardLayouts() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    KeyboardLayout[] keyboardLayoutArr = 11;
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        keyboardLayoutArr = Stub.getDefaultImpl();
                        if (keyboardLayoutArr != 0) {
                            keyboardLayoutArr = Stub.getDefaultImpl().getKeyboardLayouts();
                            return keyboardLayoutArr;
                        }
                    }
                    _reply.readException();
                    KeyboardLayout[] _result = (KeyboardLayout[]) _reply.createTypedArray(KeyboardLayout.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public KeyboardLayout[] getKeyboardLayoutsForInputDevice(InputDeviceIdentifier identifier) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (identifier != null) {
                        _data.writeInt(1);
                        identifier.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    KeyboardLayout[] keyboardLayoutArr = this.mRemote;
                    if (!keyboardLayoutArr.transact(12, _data, _reply, 0)) {
                        keyboardLayoutArr = Stub.getDefaultImpl();
                        if (keyboardLayoutArr != null) {
                            keyboardLayoutArr = Stub.getDefaultImpl().getKeyboardLayoutsForInputDevice(identifier);
                            return keyboardLayoutArr;
                        }
                    }
                    _reply.readException();
                    KeyboardLayout[] _result = (KeyboardLayout[]) _reply.createTypedArray(KeyboardLayout.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public KeyboardLayout getKeyboardLayout(String keyboardLayoutDescriptor) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(keyboardLayoutDescriptor);
                    KeyboardLayout keyboardLayout = 13;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        keyboardLayout = Stub.getDefaultImpl();
                        if (keyboardLayout != 0) {
                            keyboardLayout = Stub.getDefaultImpl().getKeyboardLayout(keyboardLayoutDescriptor);
                            return keyboardLayout;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        keyboardLayout = (KeyboardLayout) KeyboardLayout.CREATOR.createFromParcel(_reply);
                    } else {
                        keyboardLayout = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return keyboardLayout;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getCurrentKeyboardLayoutForInputDevice(InputDeviceIdentifier identifier) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (identifier != null) {
                        _data.writeInt(1);
                        identifier.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    String str = this.mRemote;
                    if (!str.transact(14, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != null) {
                            str = Stub.getDefaultImpl().getCurrentKeyboardLayoutForInputDevice(identifier);
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

            public void setCurrentKeyboardLayoutForInputDevice(InputDeviceIdentifier identifier, String keyboardLayoutDescriptor) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (identifier != null) {
                        _data.writeInt(1);
                        identifier.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(keyboardLayoutDescriptor);
                    if (this.mRemote.transact(15, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setCurrentKeyboardLayoutForInputDevice(identifier, keyboardLayoutDescriptor);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getEnabledKeyboardLayoutsForInputDevice(InputDeviceIdentifier identifier) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (identifier != null) {
                        _data.writeInt(1);
                        identifier.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    String[] strArr = this.mRemote;
                    if (!strArr.transact(16, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != null) {
                            strArr = Stub.getDefaultImpl().getEnabledKeyboardLayoutsForInputDevice(identifier);
                            return strArr;
                        }
                    }
                    _reply.readException();
                    strArr = _reply.createStringArray();
                    String[] _result = strArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addKeyboardLayoutForInputDevice(InputDeviceIdentifier identifier, String keyboardLayoutDescriptor) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (identifier != null) {
                        _data.writeInt(1);
                        identifier.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(keyboardLayoutDescriptor);
                    if (this.mRemote.transact(17, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addKeyboardLayoutForInputDevice(identifier, keyboardLayoutDescriptor);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeKeyboardLayoutForInputDevice(InputDeviceIdentifier identifier, String keyboardLayoutDescriptor) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (identifier != null) {
                        _data.writeInt(1);
                        identifier.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(keyboardLayoutDescriptor);
                    if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeKeyboardLayoutForInputDevice(identifier, keyboardLayoutDescriptor);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerInputDevicesChangedListener(IInputDevicesChangedListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerInputDevicesChangedListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int isInTabletMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 20;
                    if (!this.mRemote.transact(20, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().isInTabletMode();
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

            public void registerTabletModeChangedListener(ITabletModeChangedListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(21, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerTabletModeChangedListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void vibrate(int deviceId, long[] pattern, int repeat, IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(deviceId);
                    _data.writeLongArray(pattern);
                    _data.writeInt(repeat);
                    _data.writeStrongBinder(token);
                    if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().vibrate(deviceId, pattern, repeat, token);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cancelVibrate(int deviceId, IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(deviceId);
                    _data.writeStrongBinder(token);
                    if (this.mRemote.transact(23, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().cancelVibrate(deviceId, token);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPointerIconType(int typeId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(typeId);
                    if (this.mRemote.transact(24, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPointerIconType(typeId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setCustomPointerIcon(PointerIcon icon) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (icon != null) {
                        _data.writeInt(1);
                        icon.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(25, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setCustomPointerIcon(icon);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestPointerCapture(IBinder windowToken, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(windowToken);
                    _data.writeInt(enabled ? 1 : 0);
                    if (this.mRemote.transact(26, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().requestPointerCapture(windowToken, enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public InputMonitor monitorGestureInput(String name, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(displayId);
                    InputMonitor inputMonitor = 27;
                    if (!this.mRemote.transact(27, _data, _reply, 0)) {
                        inputMonitor = Stub.getDefaultImpl();
                        if (inputMonitor != 0) {
                            inputMonitor = Stub.getDefaultImpl().monitorGestureInput(name, displayId);
                            return inputMonitor;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        inputMonitor = (InputMonitor) InputMonitor.CREATOR.createFromParcel(_reply);
                    } else {
                        inputMonitor = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return inputMonitor;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void switchTouchSensitiveMode(boolean modeOn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(modeOn ? 1 : 0);
                    if (this.mRemote.transact(28, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().switchTouchSensitiveMode(modeOn);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void switchTouchStylusMode(boolean modeOn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(modeOn ? 1 : 0);
                    if (this.mRemote.transact(29, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().switchTouchStylusMode(modeOn);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void switchTouchWakeupMode(boolean modeOn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(modeOn ? 1 : 0);
                    if (this.mRemote.transact(30, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().switchTouchWakeupMode(modeOn);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void switchTouchCoverMode(boolean modeOn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(modeOn ? 1 : 0);
                    if (this.mRemote.transact(31, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().switchTouchCoverMode(modeOn);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IInputManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IInputManager)) {
                return new Proxy(obj);
            }
            return (IInputManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getInputDevice";
                case 2:
                    return "getInputDeviceIds";
                case 3:
                    return "isInputDeviceEnabled";
                case 4:
                    return "enableInputDevice";
                case 5:
                    return "disableInputDevice";
                case 6:
                    return "hasKeys";
                case 7:
                    return "tryPointerSpeed";
                case 8:
                    return "injectInputEvent";
                case 9:
                    return "getTouchCalibrationForInputDevice";
                case 10:
                    return "setTouchCalibrationForInputDevice";
                case 11:
                    return "getKeyboardLayouts";
                case 12:
                    return "getKeyboardLayoutsForInputDevice";
                case 13:
                    return "getKeyboardLayout";
                case 14:
                    return "getCurrentKeyboardLayoutForInputDevice";
                case 15:
                    return "setCurrentKeyboardLayoutForInputDevice";
                case 16:
                    return "getEnabledKeyboardLayoutsForInputDevice";
                case 17:
                    return "addKeyboardLayoutForInputDevice";
                case 18:
                    return "removeKeyboardLayoutForInputDevice";
                case 19:
                    return "registerInputDevicesChangedListener";
                case 20:
                    return "isInTabletMode";
                case 21:
                    return "registerTabletModeChangedListener";
                case 22:
                    return "vibrate";
                case 23:
                    return "cancelVibrate";
                case 24:
                    return "setPointerIconType";
                case 25:
                    return "setCustomPointerIcon";
                case 26:
                    return "requestPointerCapture";
                case 27:
                    return "monitorGestureInput";
                case 28:
                    return "switchTouchSensitiveMode";
                case 29:
                    return "switchTouchStylusMode";
                case 30:
                    return "switchTouchWakeupMode";
                case 31:
                    return "switchTouchCoverMode";
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
                int _arg02;
                int _arg1;
                InputDeviceIdentifier _arg03;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        InputDevice _result = getInputDevice(data.readInt());
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
                        int[] _result2 = getInputDeviceIds();
                        reply.writeNoException();
                        reply.writeIntArray(_result2);
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        boolean _result3 = isInputDeviceEnabled(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result3);
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        enableInputDevice(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        disableInputDevice(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 6:
                        boolean[] _arg3;
                        data.enforceInterface(descriptor);
                        _arg02 = data.readInt();
                        _arg1 = data.readInt();
                        int[] _arg2 = data.createIntArray();
                        int _arg3_length = data.readInt();
                        if (_arg3_length < 0) {
                            _arg3 = null;
                        } else {
                            _arg3 = new boolean[_arg3_length];
                        }
                        boolean _result4 = hasKeys(_arg02, _arg1, _arg2, _arg3);
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        reply.writeBooleanArray(_arg3);
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        tryPointerSpeed(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 8:
                        InputEvent _arg04;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (InputEvent) InputEvent.CREATOR.createFromParcel(data);
                        } else {
                            _arg04 = null;
                        }
                        boolean _result5 = injectInputEvent(_arg04, data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result5);
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        TouchCalibration _result6 = getTouchCalibrationForInputDevice(data.readString(), data.readInt());
                        reply.writeNoException();
                        if (_result6 != null) {
                            reply.writeInt(1);
                            _result6.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 10:
                        TouchCalibration _arg22;
                        data.enforceInterface(descriptor);
                        String _arg05 = data.readString();
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg22 = (TouchCalibration) TouchCalibration.CREATOR.createFromParcel(data);
                        } else {
                            _arg22 = null;
                        }
                        setTouchCalibrationForInputDevice(_arg05, _arg1, _arg22);
                        reply.writeNoException();
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        KeyboardLayout[] _result7 = getKeyboardLayouts();
                        reply.writeNoException();
                        reply.writeTypedArray(_result7, 1);
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (InputDeviceIdentifier) InputDeviceIdentifier.CREATOR.createFromParcel(data);
                        } else {
                            _arg03 = null;
                        }
                        KeyboardLayout[] _result8 = getKeyboardLayoutsForInputDevice(_arg03);
                        reply.writeNoException();
                        reply.writeTypedArray(_result8, 1);
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        KeyboardLayout _result9 = getKeyboardLayout(data.readString());
                        reply.writeNoException();
                        if (_result9 != null) {
                            reply.writeInt(1);
                            _result9.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 14:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (InputDeviceIdentifier) InputDeviceIdentifier.CREATOR.createFromParcel(data);
                        } else {
                            _arg03 = null;
                        }
                        String _result10 = getCurrentKeyboardLayoutForInputDevice(_arg03);
                        reply.writeNoException();
                        reply.writeString(_result10);
                        return true;
                    case 15:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (InputDeviceIdentifier) InputDeviceIdentifier.CREATOR.createFromParcel(data);
                        } else {
                            _arg03 = null;
                        }
                        setCurrentKeyboardLayoutForInputDevice(_arg03, data.readString());
                        reply.writeNoException();
                        return true;
                    case 16:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (InputDeviceIdentifier) InputDeviceIdentifier.CREATOR.createFromParcel(data);
                        } else {
                            _arg03 = null;
                        }
                        String[] _result11 = getEnabledKeyboardLayoutsForInputDevice(_arg03);
                        reply.writeNoException();
                        reply.writeStringArray(_result11);
                        return true;
                    case 17:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (InputDeviceIdentifier) InputDeviceIdentifier.CREATOR.createFromParcel(data);
                        } else {
                            _arg03 = null;
                        }
                        addKeyboardLayoutForInputDevice(_arg03, data.readString());
                        reply.writeNoException();
                        return true;
                    case 18:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (InputDeviceIdentifier) InputDeviceIdentifier.CREATOR.createFromParcel(data);
                        } else {
                            _arg03 = null;
                        }
                        removeKeyboardLayoutForInputDevice(_arg03, data.readString());
                        reply.writeNoException();
                        return true;
                    case 19:
                        data.enforceInterface(descriptor);
                        registerInputDevicesChangedListener(android.hardware.input.IInputDevicesChangedListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 20:
                        data.enforceInterface(descriptor);
                        _arg02 = isInTabletMode();
                        reply.writeNoException();
                        reply.writeInt(_arg02);
                        return true;
                    case 21:
                        data.enforceInterface(descriptor);
                        registerTabletModeChangedListener(android.hardware.input.ITabletModeChangedListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 22:
                        data.enforceInterface(descriptor);
                        vibrate(data.readInt(), data.createLongArray(), data.readInt(), data.readStrongBinder());
                        reply.writeNoException();
                        return true;
                    case 23:
                        data.enforceInterface(descriptor);
                        cancelVibrate(data.readInt(), data.readStrongBinder());
                        reply.writeNoException();
                        return true;
                    case 24:
                        data.enforceInterface(descriptor);
                        setPointerIconType(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 25:
                        PointerIcon _arg06;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg06 = (PointerIcon) PointerIcon.CREATOR.createFromParcel(data);
                        } else {
                            _arg06 = null;
                        }
                        setCustomPointerIcon(_arg06);
                        reply.writeNoException();
                        return true;
                    case 26:
                        data.enforceInterface(descriptor);
                        IBinder _arg07 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        requestPointerCapture(_arg07, _arg0);
                        reply.writeNoException();
                        return true;
                    case 27:
                        data.enforceInterface(descriptor);
                        InputMonitor _result12 = monitorGestureInput(data.readString(), data.readInt());
                        reply.writeNoException();
                        if (_result12 != null) {
                            reply.writeInt(1);
                            _result12.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 28:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        switchTouchSensitiveMode(_arg0);
                        reply.writeNoException();
                        return true;
                    case 29:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        switchTouchStylusMode(_arg0);
                        reply.writeNoException();
                        return true;
                    case 30:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        switchTouchWakeupMode(_arg0);
                        reply.writeNoException();
                        return true;
                    case 31:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        switchTouchCoverMode(_arg0);
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IInputManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IInputManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void addKeyboardLayoutForInputDevice(InputDeviceIdentifier inputDeviceIdentifier, String str) throws RemoteException;

    void cancelVibrate(int i, IBinder iBinder) throws RemoteException;

    void disableInputDevice(int i) throws RemoteException;

    void enableInputDevice(int i) throws RemoteException;

    String getCurrentKeyboardLayoutForInputDevice(InputDeviceIdentifier inputDeviceIdentifier) throws RemoteException;

    String[] getEnabledKeyboardLayoutsForInputDevice(InputDeviceIdentifier inputDeviceIdentifier) throws RemoteException;

    InputDevice getInputDevice(int i) throws RemoteException;

    int[] getInputDeviceIds() throws RemoteException;

    KeyboardLayout getKeyboardLayout(String str) throws RemoteException;

    KeyboardLayout[] getKeyboardLayouts() throws RemoteException;

    KeyboardLayout[] getKeyboardLayoutsForInputDevice(InputDeviceIdentifier inputDeviceIdentifier) throws RemoteException;

    TouchCalibration getTouchCalibrationForInputDevice(String str, int i) throws RemoteException;

    boolean hasKeys(int i, int i2, int[] iArr, boolean[] zArr) throws RemoteException;

    @UnsupportedAppUsage
    boolean injectInputEvent(InputEvent inputEvent, int i) throws RemoteException;

    int isInTabletMode() throws RemoteException;

    boolean isInputDeviceEnabled(int i) throws RemoteException;

    InputMonitor monitorGestureInput(String str, int i) throws RemoteException;

    void registerInputDevicesChangedListener(IInputDevicesChangedListener iInputDevicesChangedListener) throws RemoteException;

    void registerTabletModeChangedListener(ITabletModeChangedListener iTabletModeChangedListener) throws RemoteException;

    void removeKeyboardLayoutForInputDevice(InputDeviceIdentifier inputDeviceIdentifier, String str) throws RemoteException;

    void requestPointerCapture(IBinder iBinder, boolean z) throws RemoteException;

    void setCurrentKeyboardLayoutForInputDevice(InputDeviceIdentifier inputDeviceIdentifier, String str) throws RemoteException;

    void setCustomPointerIcon(PointerIcon pointerIcon) throws RemoteException;

    void setPointerIconType(int i) throws RemoteException;

    void setTouchCalibrationForInputDevice(String str, int i, TouchCalibration touchCalibration) throws RemoteException;

    void switchTouchCoverMode(boolean z) throws RemoteException;

    void switchTouchSensitiveMode(boolean z) throws RemoteException;

    void switchTouchStylusMode(boolean z) throws RemoteException;

    void switchTouchWakeupMode(boolean z) throws RemoteException;

    void tryPointerSpeed(int i) throws RemoteException;

    void vibrate(int i, long[] jArr, int i2, IBinder iBinder) throws RemoteException;
}
