package android.hardware.hdmi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IHdmiControlService extends IInterface {

    public static class Default implements IHdmiControlService {
        public int[] getSupportedTypes() throws RemoteException {
            return null;
        }

        public HdmiDeviceInfo getActiveSource() throws RemoteException {
            return null;
        }

        public void oneTouchPlay(IHdmiControlCallback callback) throws RemoteException {
        }

        public void queryDisplayStatus(IHdmiControlCallback callback) throws RemoteException {
        }

        public void addHotplugEventListener(IHdmiHotplugEventListener listener) throws RemoteException {
        }

        public void removeHotplugEventListener(IHdmiHotplugEventListener listener) throws RemoteException {
        }

        public void addDeviceEventListener(IHdmiDeviceEventListener listener) throws RemoteException {
        }

        public void deviceSelect(int deviceId, IHdmiControlCallback callback) throws RemoteException {
        }

        public void portSelect(int portId, IHdmiControlCallback callback) throws RemoteException {
        }

        public void sendKeyEvent(int deviceType, int keyCode, boolean isPressed) throws RemoteException {
        }

        public void sendVolumeKeyEvent(int deviceType, int keyCode, boolean isPressed) throws RemoteException {
        }

        public List<HdmiPortInfo> getPortInfo() throws RemoteException {
            return null;
        }

        public boolean canChangeSystemAudioMode() throws RemoteException {
            return false;
        }

        public boolean getSystemAudioMode() throws RemoteException {
            return false;
        }

        public int getPhysicalAddress() throws RemoteException {
            return 0;
        }

        public void setSystemAudioMode(boolean enabled, IHdmiControlCallback callback) throws RemoteException {
        }

        public void addSystemAudioModeChangeListener(IHdmiSystemAudioModeChangeListener listener) throws RemoteException {
        }

        public void removeSystemAudioModeChangeListener(IHdmiSystemAudioModeChangeListener listener) throws RemoteException {
        }

        public void setArcMode(boolean enabled) throws RemoteException {
        }

        public void setProhibitMode(boolean enabled) throws RemoteException {
        }

        public void setSystemAudioVolume(int oldIndex, int newIndex, int maxIndex) throws RemoteException {
        }

        public void setSystemAudioMute(boolean mute) throws RemoteException {
        }

        public void setInputChangeListener(IHdmiInputChangeListener listener) throws RemoteException {
        }

        public List<HdmiDeviceInfo> getInputDevices() throws RemoteException {
            return null;
        }

        public List<HdmiDeviceInfo> getDeviceList() throws RemoteException {
            return null;
        }

        public void powerOffRemoteDevice(int logicalAddress, int powerStatus) throws RemoteException {
        }

        public void powerOnRemoteDevice(int logicalAddress, int powerStatus) throws RemoteException {
        }

        public void askRemoteDeviceToBecomeActiveSource(int physicalAddress) throws RemoteException {
        }

        public void sendVendorCommand(int deviceType, int targetAddress, byte[] params, boolean hasVendorId) throws RemoteException {
        }

        public void addVendorCommandListener(IHdmiVendorCommandListener listener, int deviceType) throws RemoteException {
        }

        public void sendStandby(int deviceType, int deviceId) throws RemoteException {
        }

        public void setHdmiRecordListener(IHdmiRecordListener callback) throws RemoteException {
        }

        public void startOneTouchRecord(int recorderAddress, byte[] recordSource) throws RemoteException {
        }

        public void stopOneTouchRecord(int recorderAddress) throws RemoteException {
        }

        public void startTimerRecording(int recorderAddress, int sourceType, byte[] recordSource) throws RemoteException {
        }

        public void clearTimerRecording(int recorderAddress, int sourceType, byte[] recordSource) throws RemoteException {
        }

        public void sendMhlVendorCommand(int portId, int offset, int length, byte[] data) throws RemoteException {
        }

        public void addHdmiMhlVendorCommandListener(IHdmiMhlVendorCommandListener listener) throws RemoteException {
        }

        public void setStandbyMode(boolean isStandbyModeOn) throws RemoteException {
        }

        public void reportAudioStatus(int deviceType, int volume, int maxVolume, boolean isMute) throws RemoteException {
        }

        public void setSystemAudioModeOnForAudioOnlySource() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IHdmiControlService {
        private static final String DESCRIPTOR = "android.hardware.hdmi.IHdmiControlService";
        static final int TRANSACTION_addDeviceEventListener = 7;
        static final int TRANSACTION_addHdmiMhlVendorCommandListener = 38;
        static final int TRANSACTION_addHotplugEventListener = 5;
        static final int TRANSACTION_addSystemAudioModeChangeListener = 17;
        static final int TRANSACTION_addVendorCommandListener = 30;
        static final int TRANSACTION_askRemoteDeviceToBecomeActiveSource = 28;
        static final int TRANSACTION_canChangeSystemAudioMode = 13;
        static final int TRANSACTION_clearTimerRecording = 36;
        static final int TRANSACTION_deviceSelect = 8;
        static final int TRANSACTION_getActiveSource = 2;
        static final int TRANSACTION_getDeviceList = 25;
        static final int TRANSACTION_getInputDevices = 24;
        static final int TRANSACTION_getPhysicalAddress = 15;
        static final int TRANSACTION_getPortInfo = 12;
        static final int TRANSACTION_getSupportedTypes = 1;
        static final int TRANSACTION_getSystemAudioMode = 14;
        static final int TRANSACTION_oneTouchPlay = 3;
        static final int TRANSACTION_portSelect = 9;
        static final int TRANSACTION_powerOffRemoteDevice = 26;
        static final int TRANSACTION_powerOnRemoteDevice = 27;
        static final int TRANSACTION_queryDisplayStatus = 4;
        static final int TRANSACTION_removeHotplugEventListener = 6;
        static final int TRANSACTION_removeSystemAudioModeChangeListener = 18;
        static final int TRANSACTION_reportAudioStatus = 40;
        static final int TRANSACTION_sendKeyEvent = 10;
        static final int TRANSACTION_sendMhlVendorCommand = 37;
        static final int TRANSACTION_sendStandby = 31;
        static final int TRANSACTION_sendVendorCommand = 29;
        static final int TRANSACTION_sendVolumeKeyEvent = 11;
        static final int TRANSACTION_setArcMode = 19;
        static final int TRANSACTION_setHdmiRecordListener = 32;
        static final int TRANSACTION_setInputChangeListener = 23;
        static final int TRANSACTION_setProhibitMode = 20;
        static final int TRANSACTION_setStandbyMode = 39;
        static final int TRANSACTION_setSystemAudioMode = 16;
        static final int TRANSACTION_setSystemAudioModeOnForAudioOnlySource = 41;
        static final int TRANSACTION_setSystemAudioMute = 22;
        static final int TRANSACTION_setSystemAudioVolume = 21;
        static final int TRANSACTION_startOneTouchRecord = 33;
        static final int TRANSACTION_startTimerRecording = 35;
        static final int TRANSACTION_stopOneTouchRecord = 34;

        private static class Proxy implements IHdmiControlService {
            public static IHdmiControlService sDefaultImpl;
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

            public int[] getSupportedTypes() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int[] iArr = 1;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        iArr = Stub.getDefaultImpl();
                        if (iArr != 0) {
                            iArr = Stub.getDefaultImpl().getSupportedTypes();
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

            public HdmiDeviceInfo getActiveSource() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    HdmiDeviceInfo hdmiDeviceInfo = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        hdmiDeviceInfo = Stub.getDefaultImpl();
                        if (hdmiDeviceInfo != 0) {
                            hdmiDeviceInfo = Stub.getDefaultImpl().getActiveSource();
                            return hdmiDeviceInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        hdmiDeviceInfo = (HdmiDeviceInfo) HdmiDeviceInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        hdmiDeviceInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return hdmiDeviceInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void oneTouchPlay(IHdmiControlCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().oneTouchPlay(callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void queryDisplayStatus(IHdmiControlCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().queryDisplayStatus(callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addHotplugEventListener(IHdmiHotplugEventListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addHotplugEventListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeHotplugEventListener(IHdmiHotplugEventListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeHotplugEventListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addDeviceEventListener(IHdmiDeviceEventListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addDeviceEventListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void deviceSelect(int deviceId, IHdmiControlCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(deviceId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().deviceSelect(deviceId, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void portSelect(int portId, IHdmiControlCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(portId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().portSelect(portId, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendKeyEvent(int deviceType, int keyCode, boolean isPressed) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(deviceType);
                    _data.writeInt(keyCode);
                    _data.writeInt(isPressed ? 1 : 0);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().sendKeyEvent(deviceType, keyCode, isPressed);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendVolumeKeyEvent(int deviceType, int keyCode, boolean isPressed) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(deviceType);
                    _data.writeInt(keyCode);
                    _data.writeInt(isPressed ? 1 : 0);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().sendVolumeKeyEvent(deviceType, keyCode, isPressed);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<HdmiPortInfo> getPortInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<HdmiPortInfo> list = 12;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getPortInfo();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(HdmiPortInfo.CREATOR);
                    List<HdmiPortInfo> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean canChangeSystemAudioMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().canChangeSystemAudioMode();
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

            public boolean getSystemAudioMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(14, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().getSystemAudioMode();
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

            public int getPhysicalAddress() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 15;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getPhysicalAddress();
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

            public void setSystemAudioMode(boolean enabled, IHdmiControlCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(16, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setSystemAudioMode(enabled, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addSystemAudioModeChangeListener(IHdmiSystemAudioModeChangeListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(17, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addSystemAudioModeChangeListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeSystemAudioModeChangeListener(IHdmiSystemAudioModeChangeListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeSystemAudioModeChangeListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setArcMode(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setArcMode(enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setProhibitMode(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    if (this.mRemote.transact(20, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setProhibitMode(enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setSystemAudioVolume(int oldIndex, int newIndex, int maxIndex) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(oldIndex);
                    _data.writeInt(newIndex);
                    _data.writeInt(maxIndex);
                    if (this.mRemote.transact(21, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setSystemAudioVolume(oldIndex, newIndex, maxIndex);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setSystemAudioMute(boolean mute) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mute ? 1 : 0);
                    if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setSystemAudioMute(mute);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setInputChangeListener(IHdmiInputChangeListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(23, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setInputChangeListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<HdmiDeviceInfo> getInputDevices() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<HdmiDeviceInfo> list = 24;
                    if (!this.mRemote.transact(24, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getInputDevices();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(HdmiDeviceInfo.CREATOR);
                    List<HdmiDeviceInfo> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<HdmiDeviceInfo> getDeviceList() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<HdmiDeviceInfo> list = 25;
                    if (!this.mRemote.transact(25, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getDeviceList();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(HdmiDeviceInfo.CREATOR);
                    List<HdmiDeviceInfo> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void powerOffRemoteDevice(int logicalAddress, int powerStatus) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(logicalAddress);
                    _data.writeInt(powerStatus);
                    if (this.mRemote.transact(26, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().powerOffRemoteDevice(logicalAddress, powerStatus);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void powerOnRemoteDevice(int logicalAddress, int powerStatus) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(logicalAddress);
                    _data.writeInt(powerStatus);
                    if (this.mRemote.transact(27, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().powerOnRemoteDevice(logicalAddress, powerStatus);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void askRemoteDeviceToBecomeActiveSource(int physicalAddress) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(physicalAddress);
                    if (this.mRemote.transact(28, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().askRemoteDeviceToBecomeActiveSource(physicalAddress);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendVendorCommand(int deviceType, int targetAddress, byte[] params, boolean hasVendorId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(deviceType);
                    _data.writeInt(targetAddress);
                    _data.writeByteArray(params);
                    _data.writeInt(hasVendorId ? 1 : 0);
                    if (this.mRemote.transact(29, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().sendVendorCommand(deviceType, targetAddress, params, hasVendorId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addVendorCommandListener(IHdmiVendorCommandListener listener, int deviceType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    _data.writeInt(deviceType);
                    if (this.mRemote.transact(30, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addVendorCommandListener(listener, deviceType);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendStandby(int deviceType, int deviceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(deviceType);
                    _data.writeInt(deviceId);
                    if (this.mRemote.transact(31, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().sendStandby(deviceType, deviceId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setHdmiRecordListener(IHdmiRecordListener callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(32, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setHdmiRecordListener(callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startOneTouchRecord(int recorderAddress, byte[] recordSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(recorderAddress);
                    _data.writeByteArray(recordSource);
                    if (this.mRemote.transact(33, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startOneTouchRecord(recorderAddress, recordSource);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopOneTouchRecord(int recorderAddress) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(recorderAddress);
                    if (this.mRemote.transact(34, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().stopOneTouchRecord(recorderAddress);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startTimerRecording(int recorderAddress, int sourceType, byte[] recordSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(recorderAddress);
                    _data.writeInt(sourceType);
                    _data.writeByteArray(recordSource);
                    if (this.mRemote.transact(35, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startTimerRecording(recorderAddress, sourceType, recordSource);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearTimerRecording(int recorderAddress, int sourceType, byte[] recordSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(recorderAddress);
                    _data.writeInt(sourceType);
                    _data.writeByteArray(recordSource);
                    if (this.mRemote.transact(36, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearTimerRecording(recorderAddress, sourceType, recordSource);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendMhlVendorCommand(int portId, int offset, int length, byte[] data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(portId);
                    _data.writeInt(offset);
                    _data.writeInt(length);
                    _data.writeByteArray(data);
                    if (this.mRemote.transact(37, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().sendMhlVendorCommand(portId, offset, length, data);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addHdmiMhlVendorCommandListener(IHdmiMhlVendorCommandListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(38, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addHdmiMhlVendorCommandListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setStandbyMode(boolean isStandbyModeOn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isStandbyModeOn ? 1 : 0);
                    if (this.mRemote.transact(39, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setStandbyMode(isStandbyModeOn);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportAudioStatus(int deviceType, int volume, int maxVolume, boolean isMute) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(deviceType);
                    _data.writeInt(volume);
                    _data.writeInt(maxVolume);
                    _data.writeInt(isMute ? 1 : 0);
                    if (this.mRemote.transact(40, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reportAudioStatus(deviceType, volume, maxVolume, isMute);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setSystemAudioModeOnForAudioOnlySource() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(41, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setSystemAudioModeOnForAudioOnlySource();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IHdmiControlService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IHdmiControlService)) {
                return new Proxy(obj);
            }
            return (IHdmiControlService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getSupportedTypes";
                case 2:
                    return "getActiveSource";
                case 3:
                    return "oneTouchPlay";
                case 4:
                    return "queryDisplayStatus";
                case 5:
                    return "addHotplugEventListener";
                case 6:
                    return "removeHotplugEventListener";
                case 7:
                    return "addDeviceEventListener";
                case 8:
                    return "deviceSelect";
                case 9:
                    return "portSelect";
                case 10:
                    return "sendKeyEvent";
                case 11:
                    return "sendVolumeKeyEvent";
                case 12:
                    return "getPortInfo";
                case 13:
                    return "canChangeSystemAudioMode";
                case 14:
                    return "getSystemAudioMode";
                case 15:
                    return "getPhysicalAddress";
                case 16:
                    return "setSystemAudioMode";
                case 17:
                    return "addSystemAudioModeChangeListener";
                case 18:
                    return "removeSystemAudioModeChangeListener";
                case 19:
                    return "setArcMode";
                case 20:
                    return "setProhibitMode";
                case 21:
                    return "setSystemAudioVolume";
                case 22:
                    return "setSystemAudioMute";
                case 23:
                    return "setInputChangeListener";
                case 24:
                    return "getInputDevices";
                case 25:
                    return "getDeviceList";
                case 26:
                    return "powerOffRemoteDevice";
                case 27:
                    return "powerOnRemoteDevice";
                case 28:
                    return "askRemoteDeviceToBecomeActiveSource";
                case 29:
                    return "sendVendorCommand";
                case 30:
                    return "addVendorCommandListener";
                case 31:
                    return "sendStandby";
                case 32:
                    return "setHdmiRecordListener";
                case 33:
                    return "startOneTouchRecord";
                case 34:
                    return "stopOneTouchRecord";
                case 35:
                    return "startTimerRecording";
                case 36:
                    return "clearTimerRecording";
                case 37:
                    return "sendMhlVendorCommand";
                case 38:
                    return "addHdmiMhlVendorCommandListener";
                case 39:
                    return "setStandbyMode";
                case 40:
                    return "reportAudioStatus";
                case 41:
                    return "setSystemAudioModeOnForAudioOnlySource";
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
                boolean _arg2 = false;
                int _arg0;
                int _arg1;
                List<HdmiDeviceInfo> _result;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        int[] _result2 = getSupportedTypes();
                        reply.writeNoException();
                        reply.writeIntArray(_result2);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        HdmiDeviceInfo _result3 = getActiveSource();
                        reply.writeNoException();
                        if (_result3 != null) {
                            reply.writeInt(1);
                            _result3.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        oneTouchPlay(android.hardware.hdmi.IHdmiControlCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        queryDisplayStatus(android.hardware.hdmi.IHdmiControlCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        addHotplugEventListener(android.hardware.hdmi.IHdmiHotplugEventListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        removeHotplugEventListener(android.hardware.hdmi.IHdmiHotplugEventListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        addDeviceEventListener(android.hardware.hdmi.IHdmiDeviceEventListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        deviceSelect(data.readInt(), android.hardware.hdmi.IHdmiControlCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        portSelect(data.readInt(), android.hardware.hdmi.IHdmiControlCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        sendKeyEvent(_arg0, _arg1, _arg2);
                        reply.writeNoException();
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        sendVolumeKeyEvent(_arg0, _arg1, _arg2);
                        reply.writeNoException();
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        List<HdmiPortInfo> _result4 = getPortInfo();
                        reply.writeNoException();
                        reply.writeTypedList(_result4);
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        _arg2 = canChangeSystemAudioMode();
                        reply.writeNoException();
                        reply.writeInt(_arg2);
                        return true;
                    case 14:
                        data.enforceInterface(descriptor);
                        _arg2 = getSystemAudioMode();
                        reply.writeNoException();
                        reply.writeInt(_arg2);
                        return true;
                    case 15:
                        data.enforceInterface(descriptor);
                        int _result5 = getPhysicalAddress();
                        reply.writeNoException();
                        reply.writeInt(_result5);
                        return true;
                    case 16:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        setSystemAudioMode(_arg2, android.hardware.hdmi.IHdmiControlCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 17:
                        data.enforceInterface(descriptor);
                        addSystemAudioModeChangeListener(android.hardware.hdmi.IHdmiSystemAudioModeChangeListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 18:
                        data.enforceInterface(descriptor);
                        removeSystemAudioModeChangeListener(android.hardware.hdmi.IHdmiSystemAudioModeChangeListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 19:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        setArcMode(_arg2);
                        reply.writeNoException();
                        return true;
                    case 20:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        setProhibitMode(_arg2);
                        reply.writeNoException();
                        return true;
                    case 21:
                        data.enforceInterface(descriptor);
                        setSystemAudioVolume(data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 22:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        setSystemAudioMute(_arg2);
                        reply.writeNoException();
                        return true;
                    case 23:
                        data.enforceInterface(descriptor);
                        setInputChangeListener(android.hardware.hdmi.IHdmiInputChangeListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 24:
                        data.enforceInterface(descriptor);
                        _result = getInputDevices();
                        reply.writeNoException();
                        reply.writeTypedList(_result);
                        return true;
                    case 25:
                        data.enforceInterface(descriptor);
                        _result = getDeviceList();
                        reply.writeNoException();
                        reply.writeTypedList(_result);
                        return true;
                    case 26:
                        data.enforceInterface(descriptor);
                        powerOffRemoteDevice(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 27:
                        data.enforceInterface(descriptor);
                        powerOnRemoteDevice(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 28:
                        data.enforceInterface(descriptor);
                        askRemoteDeviceToBecomeActiveSource(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 29:
                        data.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        _arg1 = data.readInt();
                        byte[] _arg22 = data.createByteArray();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        sendVendorCommand(_arg0, _arg1, _arg22, _arg2);
                        reply.writeNoException();
                        return true;
                    case 30:
                        data.enforceInterface(descriptor);
                        addVendorCommandListener(android.hardware.hdmi.IHdmiVendorCommandListener.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 31:
                        data.enforceInterface(descriptor);
                        sendStandby(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 32:
                        data.enforceInterface(descriptor);
                        setHdmiRecordListener(android.hardware.hdmi.IHdmiRecordListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 33:
                        data.enforceInterface(descriptor);
                        startOneTouchRecord(data.readInt(), data.createByteArray());
                        reply.writeNoException();
                        return true;
                    case 34:
                        data.enforceInterface(descriptor);
                        stopOneTouchRecord(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 35:
                        data.enforceInterface(descriptor);
                        startTimerRecording(data.readInt(), data.readInt(), data.createByteArray());
                        reply.writeNoException();
                        return true;
                    case 36:
                        data.enforceInterface(descriptor);
                        clearTimerRecording(data.readInt(), data.readInt(), data.createByteArray());
                        reply.writeNoException();
                        return true;
                    case 37:
                        data.enforceInterface(descriptor);
                        sendMhlVendorCommand(data.readInt(), data.readInt(), data.readInt(), data.createByteArray());
                        reply.writeNoException();
                        return true;
                    case 38:
                        data.enforceInterface(descriptor);
                        addHdmiMhlVendorCommandListener(android.hardware.hdmi.IHdmiMhlVendorCommandListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 39:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        setStandbyMode(_arg2);
                        reply.writeNoException();
                        return true;
                    case 40:
                        data.enforceInterface(descriptor);
                        _arg0 = data.readInt();
                        _arg1 = data.readInt();
                        int _arg23 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        reportAudioStatus(_arg0, _arg1, _arg23, _arg2);
                        reply.writeNoException();
                        return true;
                    case 41:
                        data.enforceInterface(descriptor);
                        setSystemAudioModeOnForAudioOnlySource();
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IHdmiControlService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IHdmiControlService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void addDeviceEventListener(IHdmiDeviceEventListener iHdmiDeviceEventListener) throws RemoteException;

    void addHdmiMhlVendorCommandListener(IHdmiMhlVendorCommandListener iHdmiMhlVendorCommandListener) throws RemoteException;

    void addHotplugEventListener(IHdmiHotplugEventListener iHdmiHotplugEventListener) throws RemoteException;

    void addSystemAudioModeChangeListener(IHdmiSystemAudioModeChangeListener iHdmiSystemAudioModeChangeListener) throws RemoteException;

    void addVendorCommandListener(IHdmiVendorCommandListener iHdmiVendorCommandListener, int i) throws RemoteException;

    void askRemoteDeviceToBecomeActiveSource(int i) throws RemoteException;

    boolean canChangeSystemAudioMode() throws RemoteException;

    void clearTimerRecording(int i, int i2, byte[] bArr) throws RemoteException;

    void deviceSelect(int i, IHdmiControlCallback iHdmiControlCallback) throws RemoteException;

    HdmiDeviceInfo getActiveSource() throws RemoteException;

    List<HdmiDeviceInfo> getDeviceList() throws RemoteException;

    List<HdmiDeviceInfo> getInputDevices() throws RemoteException;

    int getPhysicalAddress() throws RemoteException;

    List<HdmiPortInfo> getPortInfo() throws RemoteException;

    int[] getSupportedTypes() throws RemoteException;

    boolean getSystemAudioMode() throws RemoteException;

    void oneTouchPlay(IHdmiControlCallback iHdmiControlCallback) throws RemoteException;

    void portSelect(int i, IHdmiControlCallback iHdmiControlCallback) throws RemoteException;

    void powerOffRemoteDevice(int i, int i2) throws RemoteException;

    void powerOnRemoteDevice(int i, int i2) throws RemoteException;

    void queryDisplayStatus(IHdmiControlCallback iHdmiControlCallback) throws RemoteException;

    void removeHotplugEventListener(IHdmiHotplugEventListener iHdmiHotplugEventListener) throws RemoteException;

    void removeSystemAudioModeChangeListener(IHdmiSystemAudioModeChangeListener iHdmiSystemAudioModeChangeListener) throws RemoteException;

    void reportAudioStatus(int i, int i2, int i3, boolean z) throws RemoteException;

    void sendKeyEvent(int i, int i2, boolean z) throws RemoteException;

    void sendMhlVendorCommand(int i, int i2, int i3, byte[] bArr) throws RemoteException;

    void sendStandby(int i, int i2) throws RemoteException;

    void sendVendorCommand(int i, int i2, byte[] bArr, boolean z) throws RemoteException;

    void sendVolumeKeyEvent(int i, int i2, boolean z) throws RemoteException;

    void setArcMode(boolean z) throws RemoteException;

    void setHdmiRecordListener(IHdmiRecordListener iHdmiRecordListener) throws RemoteException;

    void setInputChangeListener(IHdmiInputChangeListener iHdmiInputChangeListener) throws RemoteException;

    void setProhibitMode(boolean z) throws RemoteException;

    void setStandbyMode(boolean z) throws RemoteException;

    void setSystemAudioMode(boolean z, IHdmiControlCallback iHdmiControlCallback) throws RemoteException;

    void setSystemAudioModeOnForAudioOnlySource() throws RemoteException;

    void setSystemAudioMute(boolean z) throws RemoteException;

    void setSystemAudioVolume(int i, int i2, int i3) throws RemoteException;

    void startOneTouchRecord(int i, byte[] bArr) throws RemoteException;

    void startTimerRecording(int i, int i2, byte[] bArr) throws RemoteException;

    void stopOneTouchRecord(int i) throws RemoteException;
}
