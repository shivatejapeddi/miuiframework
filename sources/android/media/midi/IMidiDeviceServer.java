package android.media.midi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.io.FileDescriptor;

public interface IMidiDeviceServer extends IInterface {

    public static class Default implements IMidiDeviceServer {
        public FileDescriptor openInputPort(IBinder token, int portNumber) throws RemoteException {
            return null;
        }

        public FileDescriptor openOutputPort(IBinder token, int portNumber) throws RemoteException {
            return null;
        }

        public void closePort(IBinder token) throws RemoteException {
        }

        public void closeDevice() throws RemoteException {
        }

        public int connectPorts(IBinder token, FileDescriptor fd, int outputPortNumber) throws RemoteException {
            return 0;
        }

        public MidiDeviceInfo getDeviceInfo() throws RemoteException {
            return null;
        }

        public void setDeviceInfo(MidiDeviceInfo deviceInfo) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMidiDeviceServer {
        private static final String DESCRIPTOR = "android.media.midi.IMidiDeviceServer";
        static final int TRANSACTION_closeDevice = 4;
        static final int TRANSACTION_closePort = 3;
        static final int TRANSACTION_connectPorts = 5;
        static final int TRANSACTION_getDeviceInfo = 6;
        static final int TRANSACTION_openInputPort = 1;
        static final int TRANSACTION_openOutputPort = 2;
        static final int TRANSACTION_setDeviceInfo = 7;

        private static class Proxy implements IMidiDeviceServer {
            public static IMidiDeviceServer sDefaultImpl;
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

            public FileDescriptor openInputPort(IBinder token, int portNumber) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(portNumber);
                    FileDescriptor fileDescriptor = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        fileDescriptor = Stub.getDefaultImpl();
                        if (fileDescriptor != 0) {
                            fileDescriptor = Stub.getDefaultImpl().openInputPort(token, portNumber);
                            return fileDescriptor;
                        }
                    }
                    _reply.readException();
                    fileDescriptor = _reply.readRawFileDescriptor();
                    FileDescriptor _result = fileDescriptor;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public FileDescriptor openOutputPort(IBinder token, int portNumber) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(portNumber);
                    FileDescriptor fileDescriptor = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        fileDescriptor = Stub.getDefaultImpl();
                        if (fileDescriptor != 0) {
                            fileDescriptor = Stub.getDefaultImpl().openOutputPort(token, portNumber);
                            return fileDescriptor;
                        }
                    }
                    _reply.readException();
                    fileDescriptor = _reply.readRawFileDescriptor();
                    FileDescriptor _result = fileDescriptor;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void closePort(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().closePort(token);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void closeDevice() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().closeDevice();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public int connectPorts(IBinder token, FileDescriptor fd, int outputPortNumber) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeRawFileDescriptor(fd);
                    _data.writeInt(outputPortNumber);
                    int i = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().connectPorts(token, fd, outputPortNumber);
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

            public MidiDeviceInfo getDeviceInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    MidiDeviceInfo midiDeviceInfo = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        midiDeviceInfo = Stub.getDefaultImpl();
                        if (midiDeviceInfo != 0) {
                            midiDeviceInfo = Stub.getDefaultImpl().getDeviceInfo();
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

            public void setDeviceInfo(MidiDeviceInfo deviceInfo) throws RemoteException {
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
                        Stub.getDefaultImpl().setDeviceInfo(deviceInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMidiDeviceServer asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IMidiDeviceServer)) {
                return new Proxy(obj);
            }
            return (IMidiDeviceServer) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "openInputPort";
                case 2:
                    return "openOutputPort";
                case 3:
                    return "closePort";
                case 4:
                    return "closeDevice";
                case 5:
                    return "connectPorts";
                case 6:
                    return "getDeviceInfo";
                case 7:
                    return "setDeviceInfo";
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
                FileDescriptor _result;
                MidiDeviceInfo _result2;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        _result = openInputPort(data.readStrongBinder(), data.readInt());
                        reply.writeNoException();
                        reply.writeRawFileDescriptor(_result);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        _result = openOutputPort(data.readStrongBinder(), data.readInt());
                        reply.writeNoException();
                        reply.writeRawFileDescriptor(_result);
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        closePort(data.readStrongBinder());
                        reply.writeNoException();
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        closeDevice();
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        int _result3 = connectPorts(data.readStrongBinder(), data.readRawFileDescriptor(), data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result3);
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        _result2 = getDeviceInfo();
                        reply.writeNoException();
                        if (_result2 != null) {
                            reply.writeInt(1);
                            _result2.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _result2 = (MidiDeviceInfo) MidiDeviceInfo.CREATOR.createFromParcel(data);
                        } else {
                            _result2 = null;
                        }
                        setDeviceInfo(_result2);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IMidiDeviceServer impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IMidiDeviceServer getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void closeDevice() throws RemoteException;

    void closePort(IBinder iBinder) throws RemoteException;

    int connectPorts(IBinder iBinder, FileDescriptor fileDescriptor, int i) throws RemoteException;

    MidiDeviceInfo getDeviceInfo() throws RemoteException;

    FileDescriptor openInputPort(IBinder iBinder, int i) throws RemoteException;

    FileDescriptor openOutputPort(IBinder iBinder, int i) throws RemoteException;

    void setDeviceInfo(MidiDeviceInfo midiDeviceInfo) throws RemoteException;
}
