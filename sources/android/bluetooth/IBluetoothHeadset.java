package android.bluetooth;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IBluetoothHeadset extends IInterface {

    public static class Default implements IBluetoothHeadset {
        public List<BluetoothDevice> getConnectedDevices() throws RemoteException {
            return null;
        }

        public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) throws RemoteException {
            return null;
        }

        public int getConnectionState(BluetoothDevice device) throws RemoteException {
            return 0;
        }

        public boolean startVoiceRecognition(BluetoothDevice device) throws RemoteException {
            return false;
        }

        public boolean stopVoiceRecognition(BluetoothDevice device) throws RemoteException {
            return false;
        }

        public boolean isAudioConnected(BluetoothDevice device) throws RemoteException {
            return false;
        }

        public boolean sendVendorSpecificResultCode(BluetoothDevice device, String command, String arg) throws RemoteException {
            return false;
        }

        public boolean connect(BluetoothDevice device) throws RemoteException {
            return false;
        }

        public boolean disconnect(BluetoothDevice device) throws RemoteException {
            return false;
        }

        public boolean setPriority(BluetoothDevice device, int priority) throws RemoteException {
            return false;
        }

        public int getPriority(BluetoothDevice device) throws RemoteException {
            return 0;
        }

        public int getAudioState(BluetoothDevice device) throws RemoteException {
            return 0;
        }

        public boolean isAudioOn() throws RemoteException {
            return false;
        }

        public boolean connectAudio() throws RemoteException {
            return false;
        }

        public boolean disconnectAudio() throws RemoteException {
            return false;
        }

        public void setAudioRouteAllowed(boolean allowed) throws RemoteException {
        }

        public boolean getAudioRouteAllowed() throws RemoteException {
            return false;
        }

        public void setForceScoAudio(boolean forced) throws RemoteException {
        }

        public boolean startScoUsingVirtualVoiceCall() throws RemoteException {
            return false;
        }

        public boolean stopScoUsingVirtualVoiceCall() throws RemoteException {
            return false;
        }

        public void phoneStateChanged(int numActive, int numHeld, int callState, String number, int type, String name) throws RemoteException {
        }

        public void clccResponse(int index, int direction, int status, int mode, boolean mpty, String number, int type) throws RemoteException {
        }

        public boolean setActiveDevice(BluetoothDevice device) throws RemoteException {
            return false;
        }

        public BluetoothDevice getActiveDevice() throws RemoteException {
            return null;
        }

        public boolean isInbandRingingEnabled() throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IBluetoothHeadset {
        private static final String DESCRIPTOR = "android.bluetooth.IBluetoothHeadset";
        static final int TRANSACTION_clccResponse = 22;
        static final int TRANSACTION_connect = 8;
        static final int TRANSACTION_connectAudio = 14;
        static final int TRANSACTION_disconnect = 9;
        static final int TRANSACTION_disconnectAudio = 15;
        static final int TRANSACTION_getActiveDevice = 24;
        static final int TRANSACTION_getAudioRouteAllowed = 17;
        static final int TRANSACTION_getAudioState = 12;
        static final int TRANSACTION_getConnectedDevices = 1;
        static final int TRANSACTION_getConnectionState = 3;
        static final int TRANSACTION_getDevicesMatchingConnectionStates = 2;
        static final int TRANSACTION_getPriority = 11;
        static final int TRANSACTION_isAudioConnected = 6;
        static final int TRANSACTION_isAudioOn = 13;
        static final int TRANSACTION_isInbandRingingEnabled = 25;
        static final int TRANSACTION_phoneStateChanged = 21;
        static final int TRANSACTION_sendVendorSpecificResultCode = 7;
        static final int TRANSACTION_setActiveDevice = 23;
        static final int TRANSACTION_setAudioRouteAllowed = 16;
        static final int TRANSACTION_setForceScoAudio = 18;
        static final int TRANSACTION_setPriority = 10;
        static final int TRANSACTION_startScoUsingVirtualVoiceCall = 19;
        static final int TRANSACTION_startVoiceRecognition = 4;
        static final int TRANSACTION_stopScoUsingVirtualVoiceCall = 20;
        static final int TRANSACTION_stopVoiceRecognition = 5;

        private static class Proxy implements IBluetoothHeadset {
            public static IBluetoothHeadset sDefaultImpl;
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

            public List<BluetoothDevice> getConnectedDevices() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<BluetoothDevice> list = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getConnectedDevices();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(BluetoothDevice.CREATOR);
                    List<BluetoothDevice> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(states);
                    List<BluetoothDevice> list = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getDevicesMatchingConnectionStates(states);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(BluetoothDevice.CREATOR);
                    List<BluetoothDevice> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getConnectionState(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(3, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getConnectionState(device);
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

            public boolean startVoiceRecognition(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().startVoiceRecognition(device);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean stopVoiceRecognition(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().stopVoiceRecognition(device);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isAudioConnected(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isAudioConnected(device);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean sendVendorSpecificResultCode(BluetoothDevice device, String command, String arg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(command);
                    _data.writeString(arg);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().sendVendorSpecificResultCode(device, command, arg);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean connect(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().connect(device);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean disconnect(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().disconnect(device);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setPriority(BluetoothDevice device, int priority) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(priority);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setPriority(device, priority);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPriority(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(11, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getPriority(device);
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

            public int getAudioState(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(12, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getAudioState(device);
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

            public boolean isAudioOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isAudioOn();
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

            public boolean connectAudio() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(14, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().connectAudio();
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

            public boolean disconnectAudio() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().disconnectAudio();
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

            public void setAudioRouteAllowed(boolean allowed) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(allowed ? 1 : 0);
                    if (this.mRemote.transact(16, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAudioRouteAllowed(allowed);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getAudioRouteAllowed() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(17, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().getAudioRouteAllowed();
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

            public void setForceScoAudio(boolean forced) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(forced ? 1 : 0);
                    if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setForceScoAudio(forced);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean startScoUsingVirtualVoiceCall() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(19, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().startScoUsingVirtualVoiceCall();
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

            public boolean stopScoUsingVirtualVoiceCall() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(20, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().stopScoUsingVirtualVoiceCall();
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

            public void phoneStateChanged(int numActive, int numHeld, int callState, String number, int type, String name) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                String str;
                int i3;
                String str2;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(numActive);
                    } catch (Throwable th2) {
                        th = th2;
                        i = numHeld;
                        i2 = callState;
                        str = number;
                        i3 = type;
                        str2 = name;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(numHeld);
                        try {
                            _data.writeInt(callState);
                            try {
                                _data.writeString(number);
                                try {
                                    _data.writeInt(type);
                                } catch (Throwable th3) {
                                    th = th3;
                                    str2 = name;
                                    _data.recycle();
                                    throw th;
                                }
                            } catch (Throwable th4) {
                                th = th4;
                                i3 = type;
                                str2 = name;
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th5) {
                            th = th5;
                            str = number;
                            i3 = type;
                            str2 = name;
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeString(name);
                            try {
                                if (this.mRemote.transact(21, _data, null, 1) || Stub.getDefaultImpl() == null) {
                                    _data.recycle();
                                    return;
                                }
                                Stub.getDefaultImpl().phoneStateChanged(numActive, numHeld, callState, number, type, name);
                                _data.recycle();
                            } catch (Throwable th6) {
                                th = th6;
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th7) {
                            th = th7;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th8) {
                        th = th8;
                        i2 = callState;
                        str = number;
                        i3 = type;
                        str2 = name;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th9) {
                    th = th9;
                    int i4 = numActive;
                    i = numHeld;
                    i2 = callState;
                    str = number;
                    i3 = type;
                    str2 = name;
                    _data.recycle();
                    throw th;
                }
            }

            public void clccResponse(int index, int direction, int status, int mode, boolean mpty, String number, int type) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                int i3;
                String str;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(index);
                    } catch (Throwable th2) {
                        th = th2;
                        i = direction;
                        i2 = status;
                        i3 = mode;
                        str = number;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(direction);
                    } catch (Throwable th3) {
                        th = th3;
                        i2 = status;
                        i3 = mode;
                        str = number;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(status);
                        try {
                            _data.writeInt(mode);
                            _data.writeInt(mpty ? 1 : 0);
                        } catch (Throwable th4) {
                            th = th4;
                            str = number;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeString(number);
                            _data.writeInt(type);
                            if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().clccResponse(index, direction, status, mode, mpty, number, type);
                            _reply.recycle();
                            _data.recycle();
                        } catch (Throwable th5) {
                            th = th5;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        i3 = mode;
                        str = number;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    int i4 = index;
                    i = direction;
                    i2 = status;
                    i3 = mode;
                    str = number;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public boolean setActiveDevice(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(23, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setActiveDevice(device);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public BluetoothDevice getActiveDevice() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    BluetoothDevice bluetoothDevice = 24;
                    if (!this.mRemote.transact(24, _data, _reply, 0)) {
                        bluetoothDevice = Stub.getDefaultImpl();
                        if (bluetoothDevice != 0) {
                            bluetoothDevice = Stub.getDefaultImpl().getActiveDevice();
                            return bluetoothDevice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        bluetoothDevice = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(_reply);
                    } else {
                        bluetoothDevice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return bluetoothDevice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isInbandRingingEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(25, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isInbandRingingEnabled();
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

        public static IBluetoothHeadset asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IBluetoothHeadset)) {
                return new Proxy(obj);
            }
            return (IBluetoothHeadset) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getConnectedDevices";
                case 2:
                    return "getDevicesMatchingConnectionStates";
                case 3:
                    return "getConnectionState";
                case 4:
                    return "startVoiceRecognition";
                case 5:
                    return "stopVoiceRecognition";
                case 6:
                    return "isAudioConnected";
                case 7:
                    return "sendVendorSpecificResultCode";
                case 8:
                    return "connect";
                case 9:
                    return "disconnect";
                case 10:
                    return "setPriority";
                case 11:
                    return "getPriority";
                case 12:
                    return "getAudioState";
                case 13:
                    return "isAudioOn";
                case 14:
                    return "connectAudio";
                case 15:
                    return "disconnectAudio";
                case 16:
                    return "setAudioRouteAllowed";
                case 17:
                    return "getAudioRouteAllowed";
                case 18:
                    return "setForceScoAudio";
                case 19:
                    return "startScoUsingVirtualVoiceCall";
                case 20:
                    return "stopScoUsingVirtualVoiceCall";
                case 21:
                    return "phoneStateChanged";
                case 22:
                    return "clccResponse";
                case 23:
                    return "setActiveDevice";
                case 24:
                    return "getActiveDevice";
                case 25:
                    return "isInbandRingingEnabled";
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
                boolean _arg0 = false;
                BluetoothDevice _arg02;
                int _result;
                boolean _result2;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        List<BluetoothDevice> _result3 = getConnectedDevices();
                        reply.writeNoException();
                        parcel2.writeTypedList(_result3);
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        List<BluetoothDevice> _result4 = getDevicesMatchingConnectionStates(data.createIntArray());
                        reply.writeNoException();
                        parcel2.writeTypedList(_result4);
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        _result = getConnectionState(_arg02);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        _result2 = startVoiceRecognition(_arg02);
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        _result2 = stopVoiceRecognition(_arg02);
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        _result2 = isAudioConnected(_arg02);
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        boolean _result5 = sendVendorSpecificResultCode(_arg02, data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        _result2 = connect(_arg02);
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        _result2 = disconnect(_arg02);
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        boolean _result6 = setPriority(_arg02, data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        _result = getPriority(_arg02);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        _result = getAudioState(_arg02);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        _arg0 = isAudioOn();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        _arg0 = connectAudio();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        _arg0 = disconnectAudio();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        setAudioRouteAllowed(_arg0);
                        reply.writeNoException();
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        _arg0 = getAudioRouteAllowed();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        setForceScoAudio(_arg0);
                        reply.writeNoException();
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        _arg0 = startScoUsingVirtualVoiceCall();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        _arg0 = stopScoUsingVirtualVoiceCall();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        phoneStateChanged(data.readInt(), data.readInt(), data.readInt(), data.readString(), data.readInt(), data.readString());
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        clccResponse(data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt() != 0, data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        _result2 = setActiveDevice(_arg02);
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        BluetoothDevice _result7 = getActiveDevice();
                        reply.writeNoException();
                        if (_result7 != null) {
                            parcel2.writeInt(1);
                            _result7.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 25:
                        parcel.enforceInterface(descriptor);
                        _arg0 = isInbandRingingEnabled();
                        reply.writeNoException();
                        parcel2.writeInt(_arg0);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IBluetoothHeadset impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IBluetoothHeadset getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void clccResponse(int i, int i2, int i3, int i4, boolean z, String str, int i5) throws RemoteException;

    boolean connect(BluetoothDevice bluetoothDevice) throws RemoteException;

    boolean connectAudio() throws RemoteException;

    boolean disconnect(BluetoothDevice bluetoothDevice) throws RemoteException;

    boolean disconnectAudio() throws RemoteException;

    BluetoothDevice getActiveDevice() throws RemoteException;

    boolean getAudioRouteAllowed() throws RemoteException;

    int getAudioState(BluetoothDevice bluetoothDevice) throws RemoteException;

    List<BluetoothDevice> getConnectedDevices() throws RemoteException;

    int getConnectionState(BluetoothDevice bluetoothDevice) throws RemoteException;

    List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] iArr) throws RemoteException;

    int getPriority(BluetoothDevice bluetoothDevice) throws RemoteException;

    boolean isAudioConnected(BluetoothDevice bluetoothDevice) throws RemoteException;

    boolean isAudioOn() throws RemoteException;

    boolean isInbandRingingEnabled() throws RemoteException;

    void phoneStateChanged(int i, int i2, int i3, String str, int i4, String str2) throws RemoteException;

    boolean sendVendorSpecificResultCode(BluetoothDevice bluetoothDevice, String str, String str2) throws RemoteException;

    boolean setActiveDevice(BluetoothDevice bluetoothDevice) throws RemoteException;

    void setAudioRouteAllowed(boolean z) throws RemoteException;

    void setForceScoAudio(boolean z) throws RemoteException;

    boolean setPriority(BluetoothDevice bluetoothDevice, int i) throws RemoteException;

    boolean startScoUsingVirtualVoiceCall() throws RemoteException;

    boolean startVoiceRecognition(BluetoothDevice bluetoothDevice) throws RemoteException;

    boolean stopScoUsingVirtualVoiceCall() throws RemoteException;

    boolean stopVoiceRecognition(BluetoothDevice bluetoothDevice) throws RemoteException;
}
