package android.media.midi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IMidiDeviceListener extends IInterface {

    public static class Default implements IMidiDeviceListener {
        public void onDeviceAdded(MidiDeviceInfo device) throws RemoteException {
        }

        public void onDeviceRemoved(MidiDeviceInfo device) throws RemoteException {
        }

        public void onDeviceStatusChanged(MidiDeviceStatus status) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMidiDeviceListener {
        private static final String DESCRIPTOR = "android.media.midi.IMidiDeviceListener";
        static final int TRANSACTION_onDeviceAdded = 1;
        static final int TRANSACTION_onDeviceRemoved = 2;
        static final int TRANSACTION_onDeviceStatusChanged = 3;

        private static class Proxy implements IMidiDeviceListener {
            public static IMidiDeviceListener sDefaultImpl;
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

            public void onDeviceAdded(MidiDeviceInfo device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onDeviceAdded(device);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onDeviceRemoved(MidiDeviceInfo device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onDeviceRemoved(device);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onDeviceStatusChanged(MidiDeviceStatus status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (status != null) {
                        _data.writeInt(1);
                        status.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onDeviceStatusChanged(status);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMidiDeviceListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IMidiDeviceListener)) {
                return new Proxy(obj);
            }
            return (IMidiDeviceListener) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onDeviceAdded";
            }
            if (transactionCode == 2) {
                return "onDeviceRemoved";
            }
            if (transactionCode != 3) {
                return null;
            }
            return "onDeviceStatusChanged";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            MidiDeviceInfo _arg0;
            if (code == 1) {
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (MidiDeviceInfo) MidiDeviceInfo.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                onDeviceAdded(_arg0);
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (MidiDeviceInfo) MidiDeviceInfo.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                onDeviceRemoved(_arg0);
                return true;
            } else if (code == 3) {
                MidiDeviceStatus _arg02;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg02 = (MidiDeviceStatus) MidiDeviceStatus.CREATOR.createFromParcel(data);
                } else {
                    _arg02 = null;
                }
                onDeviceStatusChanged(_arg02);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IMidiDeviceListener impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IMidiDeviceListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onDeviceAdded(MidiDeviceInfo midiDeviceInfo) throws RemoteException;

    void onDeviceRemoved(MidiDeviceInfo midiDeviceInfo) throws RemoteException;

    void onDeviceStatusChanged(MidiDeviceStatus midiDeviceStatus) throws RemoteException;
}
