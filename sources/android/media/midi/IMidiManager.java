package android.media.midi;

import android.bluetooth.BluetoothDevice;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IMidiManager extends IInterface {

    public static class Default implements IMidiManager {
        public MidiDeviceInfo[] getDevices() throws RemoteException {
            return null;
        }

        public void registerListener(IBinder clientToken, IMidiDeviceListener listener) throws RemoteException {
        }

        public void unregisterListener(IBinder clientToken, IMidiDeviceListener listener) throws RemoteException {
        }

        public void openDevice(IBinder clientToken, MidiDeviceInfo device, IMidiDeviceOpenCallback callback) throws RemoteException {
        }

        public void openBluetoothDevice(IBinder clientToken, BluetoothDevice bluetoothDevice, IMidiDeviceOpenCallback callback) throws RemoteException {
        }

        public void closeDevice(IBinder clientToken, IBinder deviceToken) throws RemoteException {
        }

        public MidiDeviceInfo registerDeviceServer(IMidiDeviceServer server, int numInputPorts, int numOutputPorts, String[] inputPortNames, String[] outputPortNames, Bundle properties, int type) throws RemoteException {
            return null;
        }

        public void unregisterDeviceServer(IMidiDeviceServer server) throws RemoteException {
        }

        public MidiDeviceInfo getServiceDeviceInfo(String packageName, String className) throws RemoteException {
            return null;
        }

        public MidiDeviceStatus getDeviceStatus(MidiDeviceInfo deviceInfo) throws RemoteException {
            return null;
        }

        public void setDeviceStatus(IMidiDeviceServer server, MidiDeviceStatus status) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMidiManager {
        private static final String DESCRIPTOR = "android.media.midi.IMidiManager";
        static final int TRANSACTION_closeDevice = 6;
        static final int TRANSACTION_getDeviceStatus = 10;
        static final int TRANSACTION_getDevices = 1;
        static final int TRANSACTION_getServiceDeviceInfo = 9;
        static final int TRANSACTION_openBluetoothDevice = 5;
        static final int TRANSACTION_openDevice = 4;
        static final int TRANSACTION_registerDeviceServer = 7;
        static final int TRANSACTION_registerListener = 2;
        static final int TRANSACTION_setDeviceStatus = 11;
        static final int TRANSACTION_unregisterDeviceServer = 8;
        static final int TRANSACTION_unregisterListener = 3;

        private static class Proxy implements IMidiManager {
            public static IMidiManager sDefaultImpl;
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

            public MidiDeviceInfo[] getDevices() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    MidiDeviceInfo[] midiDeviceInfoArr = 1;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        midiDeviceInfoArr = Stub.getDefaultImpl();
                        if (midiDeviceInfoArr != 0) {
                            midiDeviceInfoArr = Stub.getDefaultImpl().getDevices();
                            return midiDeviceInfoArr;
                        }
                    }
                    _reply.readException();
                    MidiDeviceInfo[] _result = (MidiDeviceInfo[]) _reply.createTypedArray(MidiDeviceInfo.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerListener(IBinder clientToken, IMidiDeviceListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(clientToken);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerListener(clientToken, listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterListener(IBinder clientToken, IMidiDeviceListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(clientToken);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterListener(clientToken, listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void openDevice(IBinder clientToken, MidiDeviceInfo device, IMidiDeviceOpenCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(clientToken);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().openDevice(clientToken, device, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void openBluetoothDevice(IBinder clientToken, BluetoothDevice bluetoothDevice, IMidiDeviceOpenCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(clientToken);
                    if (bluetoothDevice != null) {
                        _data.writeInt(1);
                        bluetoothDevice.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().openBluetoothDevice(clientToken, bluetoothDevice, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void closeDevice(IBinder clientToken, IBinder deviceToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(clientToken);
                    _data.writeStrongBinder(deviceToken);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().closeDevice(clientToken, deviceToken);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public MidiDeviceInfo registerDeviceServer(IMidiDeviceServer server, int numInputPorts, int numOutputPorts, String[] inputPortNames, String[] outputPortNames, Bundle properties, int type) throws RemoteException {
                Throwable th;
                int i;
                String[] strArr;
                String[] strArr2;
                Bundle bundle = properties;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(server != null ? server.asBinder() : null);
                    try {
                        _data.writeInt(numInputPorts);
                    } catch (Throwable th2) {
                        th = th2;
                        i = numOutputPorts;
                        strArr = inputPortNames;
                        strArr2 = outputPortNames;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(numOutputPorts);
                        try {
                            _data.writeStringArray(inputPortNames);
                        } catch (Throwable th3) {
                            th = th3;
                            strArr2 = outputPortNames;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeStringArray(outputPortNames);
                            if (bundle != null) {
                                _data.writeInt(1);
                                bundle.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            _data.writeInt(type);
                            MidiDeviceInfo _result;
                            if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                if (_reply.readInt() != 0) {
                                    _result = (MidiDeviceInfo) MidiDeviceInfo.CREATOR.createFromParcel(_reply);
                                } else {
                                    _result = null;
                                }
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            _result = Stub.getDefaultImpl().registerDeviceServer(server, numInputPorts, numOutputPorts, inputPortNames, outputPortNames, properties, type);
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        } catch (Throwable th4) {
                            th = th4;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        strArr = inputPortNames;
                        strArr2 = outputPortNames;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    int i2 = numInputPorts;
                    i = numOutputPorts;
                    strArr = inputPortNames;
                    strArr2 = outputPortNames;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void unregisterDeviceServer(IMidiDeviceServer server) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(server != null ? server.asBinder() : null);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterDeviceServer(server);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public MidiDeviceInfo getServiceDeviceInfo(String packageName, String className) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(className);
                    MidiDeviceInfo midiDeviceInfo = 9;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        midiDeviceInfo = Stub.getDefaultImpl();
                        if (midiDeviceInfo != 0) {
                            midiDeviceInfo = Stub.getDefaultImpl().getServiceDeviceInfo(packageName, className);
                            return midiDeviceInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        midiDeviceInfo = (MidiDeviceInfo) MidiDeviceInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        midiDeviceInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return midiDeviceInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public MidiDeviceStatus getDeviceStatus(MidiDeviceInfo deviceInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (deviceInfo != null) {
                        _data.writeInt(1);
                        deviceInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    MidiDeviceStatus midiDeviceStatus = this.mRemote;
                    if (!midiDeviceStatus.transact(10, _data, _reply, 0)) {
                        midiDeviceStatus = Stub.getDefaultImpl();
                        if (midiDeviceStatus != null) {
                            midiDeviceStatus = Stub.getDefaultImpl().getDeviceStatus(deviceInfo);
                            return midiDeviceStatus;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        midiDeviceStatus = (MidiDeviceStatus) MidiDeviceStatus.CREATOR.createFromParcel(_reply);
                    } else {
                        midiDeviceStatus = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return midiDeviceStatus;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setDeviceStatus(IMidiDeviceServer server, MidiDeviceStatus status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(server != null ? server.asBinder() : null);
                    if (status != null) {
                        _data.writeInt(1);
                        status.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setDeviceStatus(server, status);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMidiManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IMidiManager)) {
                return new Proxy(obj);
            }
            return (IMidiManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getDevices";
                case 2:
                    return "registerListener";
                case 3:
                    return "unregisterListener";
                case 4:
                    return "openDevice";
                case 5:
                    return "openBluetoothDevice";
                case 6:
                    return "closeDevice";
                case 7:
                    return "registerDeviceServer";
                case 8:
                    return "unregisterDeviceServer";
                case 9:
                    return "getServiceDeviceInfo";
                case 10:
                    return "getDeviceStatus";
                case 11:
                    return "setDeviceStatus";
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
                IBinder _arg0;
                MidiDeviceInfo _result;
                MidiDeviceStatus _result2;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        MidiDeviceInfo[] _result3 = getDevices();
                        reply.writeNoException();
                        parcel2.writeTypedArray(_result3, 1);
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        registerListener(data.readStrongBinder(), android.media.midi.IMidiDeviceListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        unregisterListener(data.readStrongBinder(), android.media.midi.IMidiDeviceListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 4:
                        MidiDeviceInfo _arg1;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg1 = (MidiDeviceInfo) MidiDeviceInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg1 = null;
                        }
                        openDevice(_arg0, _arg1, android.media.midi.IMidiDeviceOpenCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 5:
                        BluetoothDevice _arg12;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg12 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        openBluetoothDevice(_arg0, _arg12, android.media.midi.IMidiDeviceOpenCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        closeDevice(data.readStrongBinder(), data.readStrongBinder());
                        reply.writeNoException();
                        return true;
                    case 7:
                        Bundle _arg5;
                        parcel.enforceInterface(descriptor);
                        IMidiDeviceServer _arg02 = android.media.midi.IMidiDeviceServer.Stub.asInterface(data.readStrongBinder());
                        int _arg13 = data.readInt();
                        int _arg2 = data.readInt();
                        String[] _arg3 = data.createStringArray();
                        String[] _arg4 = data.createStringArray();
                        if (data.readInt() != 0) {
                            _arg5 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg5 = null;
                        }
                        _result = registerDeviceServer(_arg02, _arg13, _arg2, _arg3, _arg4, _arg5, data.readInt());
                        reply.writeNoException();
                        if (_result != null) {
                            parcel2.writeInt(1);
                            _result.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        unregisterDeviceServer(android.media.midi.IMidiDeviceServer.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        MidiDeviceInfo _result4 = getServiceDeviceInfo(data.readString(), data.readString());
                        reply.writeNoException();
                        if (_result4 != null) {
                            parcel2.writeInt(1);
                            _result4.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _result = (MidiDeviceInfo) MidiDeviceInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _result = null;
                        }
                        _result2 = getDeviceStatus(_result);
                        reply.writeNoException();
                        if (_result2 != null) {
                            parcel2.writeInt(1);
                            _result2.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        IMidiDeviceServer _arg03 = android.media.midi.IMidiDeviceServer.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _result2 = (MidiDeviceStatus) MidiDeviceStatus.CREATOR.createFromParcel(parcel);
                        } else {
                            _result2 = null;
                        }
                        setDeviceStatus(_arg03, _result2);
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IMidiManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IMidiManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void closeDevice(IBinder iBinder, IBinder iBinder2) throws RemoteException;

    MidiDeviceStatus getDeviceStatus(MidiDeviceInfo midiDeviceInfo) throws RemoteException;

    MidiDeviceInfo[] getDevices() throws RemoteException;

    MidiDeviceInfo getServiceDeviceInfo(String str, String str2) throws RemoteException;

    void openBluetoothDevice(IBinder iBinder, BluetoothDevice bluetoothDevice, IMidiDeviceOpenCallback iMidiDeviceOpenCallback) throws RemoteException;

    void openDevice(IBinder iBinder, MidiDeviceInfo midiDeviceInfo, IMidiDeviceOpenCallback iMidiDeviceOpenCallback) throws RemoteException;

    MidiDeviceInfo registerDeviceServer(IMidiDeviceServer iMidiDeviceServer, int i, int i2, String[] strArr, String[] strArr2, Bundle bundle, int i3) throws RemoteException;

    void registerListener(IBinder iBinder, IMidiDeviceListener iMidiDeviceListener) throws RemoteException;

    void setDeviceStatus(IMidiDeviceServer iMidiDeviceServer, MidiDeviceStatus midiDeviceStatus) throws RemoteException;

    void unregisterDeviceServer(IMidiDeviceServer iMidiDeviceServer) throws RemoteException;

    void unregisterListener(IBinder iBinder, IMidiDeviceListener iMidiDeviceListener) throws RemoteException;
}
