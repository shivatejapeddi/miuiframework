package android.media.tv;

import android.hardware.hdmi.HdmiDeviceInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.InputChannel;

public interface ITvInputService extends IInterface {

    public static class Default implements ITvInputService {
        public void registerCallback(ITvInputServiceCallback callback) throws RemoteException {
        }

        public void unregisterCallback(ITvInputServiceCallback callback) throws RemoteException {
        }

        public void createSession(InputChannel channel, ITvInputSessionCallback callback, String inputId) throws RemoteException {
        }

        public void createRecordingSession(ITvInputSessionCallback callback, String inputId) throws RemoteException {
        }

        public void notifyHardwareAdded(TvInputHardwareInfo hardwareInfo) throws RemoteException {
        }

        public void notifyHardwareRemoved(TvInputHardwareInfo hardwareInfo) throws RemoteException {
        }

        public void notifyHdmiDeviceAdded(HdmiDeviceInfo deviceInfo) throws RemoteException {
        }

        public void notifyHdmiDeviceRemoved(HdmiDeviceInfo deviceInfo) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ITvInputService {
        private static final String DESCRIPTOR = "android.media.tv.ITvInputService";
        static final int TRANSACTION_createRecordingSession = 4;
        static final int TRANSACTION_createSession = 3;
        static final int TRANSACTION_notifyHardwareAdded = 5;
        static final int TRANSACTION_notifyHardwareRemoved = 6;
        static final int TRANSACTION_notifyHdmiDeviceAdded = 7;
        static final int TRANSACTION_notifyHdmiDeviceRemoved = 8;
        static final int TRANSACTION_registerCallback = 1;
        static final int TRANSACTION_unregisterCallback = 2;

        private static class Proxy implements ITvInputService {
            public static ITvInputService sDefaultImpl;
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

            public void registerCallback(ITvInputServiceCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().registerCallback(callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void unregisterCallback(ITvInputServiceCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().unregisterCallback(callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void createSession(InputChannel channel, ITvInputSessionCallback callback, String inputId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (channel != null) {
                        _data.writeInt(1);
                        channel.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeString(inputId);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().createSession(channel, callback, inputId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void createRecordingSession(ITvInputSessionCallback callback, String inputId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeString(inputId);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().createRecordingSession(callback, inputId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void notifyHardwareAdded(TvInputHardwareInfo hardwareInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (hardwareInfo != null) {
                        _data.writeInt(1);
                        hardwareInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().notifyHardwareAdded(hardwareInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void notifyHardwareRemoved(TvInputHardwareInfo hardwareInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (hardwareInfo != null) {
                        _data.writeInt(1);
                        hardwareInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().notifyHardwareRemoved(hardwareInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void notifyHdmiDeviceAdded(HdmiDeviceInfo deviceInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (deviceInfo != null) {
                        _data.writeInt(1);
                        deviceInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(7, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().notifyHdmiDeviceAdded(deviceInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void notifyHdmiDeviceRemoved(HdmiDeviceInfo deviceInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (deviceInfo != null) {
                        _data.writeInt(1);
                        deviceInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(8, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().notifyHdmiDeviceRemoved(deviceInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ITvInputService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ITvInputService)) {
                return new Proxy(obj);
            }
            return (ITvInputService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "registerCallback";
                case 2:
                    return "unregisterCallback";
                case 3:
                    return "createSession";
                case 4:
                    return "createRecordingSession";
                case 5:
                    return "notifyHardwareAdded";
                case 6:
                    return "notifyHardwareRemoved";
                case 7:
                    return "notifyHdmiDeviceAdded";
                case 8:
                    return "notifyHdmiDeviceRemoved";
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
                TvInputHardwareInfo _arg0;
                HdmiDeviceInfo _arg02;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        registerCallback(android.media.tv.ITvInputServiceCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        unregisterCallback(android.media.tv.ITvInputServiceCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 3:
                        InputChannel _arg03;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (InputChannel) InputChannel.CREATOR.createFromParcel(data);
                        } else {
                            _arg03 = null;
                        }
                        createSession(_arg03, android.media.tv.ITvInputSessionCallback.Stub.asInterface(data.readStrongBinder()), data.readString());
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        createRecordingSession(android.media.tv.ITvInputSessionCallback.Stub.asInterface(data.readStrongBinder()), data.readString());
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (TvInputHardwareInfo) TvInputHardwareInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        notifyHardwareAdded(_arg0);
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (TvInputHardwareInfo) TvInputHardwareInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        notifyHardwareRemoved(_arg0);
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (HdmiDeviceInfo) HdmiDeviceInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        notifyHdmiDeviceAdded(_arg02);
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (HdmiDeviceInfo) HdmiDeviceInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        notifyHdmiDeviceRemoved(_arg02);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(ITvInputService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ITvInputService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void createRecordingSession(ITvInputSessionCallback iTvInputSessionCallback, String str) throws RemoteException;

    void createSession(InputChannel inputChannel, ITvInputSessionCallback iTvInputSessionCallback, String str) throws RemoteException;

    void notifyHardwareAdded(TvInputHardwareInfo tvInputHardwareInfo) throws RemoteException;

    void notifyHardwareRemoved(TvInputHardwareInfo tvInputHardwareInfo) throws RemoteException;

    void notifyHdmiDeviceAdded(HdmiDeviceInfo hdmiDeviceInfo) throws RemoteException;

    void notifyHdmiDeviceRemoved(HdmiDeviceInfo hdmiDeviceInfo) throws RemoteException;

    void registerCallback(ITvInputServiceCallback iTvInputServiceCallback) throws RemoteException;

    void unregisterCallback(ITvInputServiceCallback iTvInputServiceCallback) throws RemoteException;
}
