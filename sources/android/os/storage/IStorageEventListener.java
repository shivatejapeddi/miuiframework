package android.os.storage;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IStorageEventListener extends IInterface {

    public static class Default implements IStorageEventListener {
        public void onUsbMassStorageConnectionChanged(boolean connected) throws RemoteException {
        }

        public void onStorageStateChanged(String path, String oldState, String newState) throws RemoteException {
        }

        public void onVolumeStateChanged(VolumeInfo vol, int oldState, int newState) throws RemoteException {
        }

        public void onVolumeRecordChanged(VolumeRecord rec) throws RemoteException {
        }

        public void onVolumeForgotten(String fsUuid) throws RemoteException {
        }

        public void onDiskScanned(DiskInfo disk, int volumeCount) throws RemoteException {
        }

        public void onDiskDestroyed(DiskInfo disk) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IStorageEventListener {
        private static final String DESCRIPTOR = "android.os.storage.IStorageEventListener";
        static final int TRANSACTION_onDiskDestroyed = 7;
        static final int TRANSACTION_onDiskScanned = 6;
        static final int TRANSACTION_onStorageStateChanged = 2;
        static final int TRANSACTION_onUsbMassStorageConnectionChanged = 1;
        static final int TRANSACTION_onVolumeForgotten = 5;
        static final int TRANSACTION_onVolumeRecordChanged = 4;
        static final int TRANSACTION_onVolumeStateChanged = 3;

        private static class Proxy implements IStorageEventListener {
            public static IStorageEventListener sDefaultImpl;
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

            public void onUsbMassStorageConnectionChanged(boolean connected) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(connected ? 1 : 0);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onUsbMassStorageConnectionChanged(connected);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onStorageStateChanged(String path, String oldState, String newState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    _data.writeString(oldState);
                    _data.writeString(newState);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onStorageStateChanged(path, oldState, newState);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onVolumeStateChanged(VolumeInfo vol, int oldState, int newState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (vol != null) {
                        _data.writeInt(1);
                        vol.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(oldState);
                    _data.writeInt(newState);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onVolumeStateChanged(vol, oldState, newState);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onVolumeRecordChanged(VolumeRecord rec) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (rec != null) {
                        _data.writeInt(1);
                        rec.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onVolumeRecordChanged(rec);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onVolumeForgotten(String fsUuid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(fsUuid);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onVolumeForgotten(fsUuid);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onDiskScanned(DiskInfo disk, int volumeCount) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (disk != null) {
                        _data.writeInt(1);
                        disk.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(volumeCount);
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onDiskScanned(disk, volumeCount);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onDiskDestroyed(DiskInfo disk) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (disk != null) {
                        _data.writeInt(1);
                        disk.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(7, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onDiskDestroyed(disk);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IStorageEventListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IStorageEventListener)) {
                return new Proxy(obj);
            }
            return (IStorageEventListener) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "onUsbMassStorageConnectionChanged";
                case 2:
                    return "onStorageStateChanged";
                case 3:
                    return "onVolumeStateChanged";
                case 4:
                    return "onVolumeRecordChanged";
                case 5:
                    return "onVolumeForgotten";
                case 6:
                    return "onDiskScanned";
                case 7:
                    return "onDiskDestroyed";
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
                DiskInfo _arg0;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        onUsbMassStorageConnectionChanged(data.readInt() != 0);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        onStorageStateChanged(data.readString(), data.readString(), data.readString());
                        return true;
                    case 3:
                        VolumeInfo _arg02;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (VolumeInfo) VolumeInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        onVolumeStateChanged(_arg02, data.readInt(), data.readInt());
                        return true;
                    case 4:
                        VolumeRecord _arg03;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (VolumeRecord) VolumeRecord.CREATOR.createFromParcel(data);
                        } else {
                            _arg03 = null;
                        }
                        onVolumeRecordChanged(_arg03);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        onVolumeForgotten(data.readString());
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (DiskInfo) DiskInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        onDiskScanned(_arg0, data.readInt());
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (DiskInfo) DiskInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        onDiskDestroyed(_arg0);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IStorageEventListener impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IStorageEventListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onDiskDestroyed(DiskInfo diskInfo) throws RemoteException;

    void onDiskScanned(DiskInfo diskInfo, int i) throws RemoteException;

    void onStorageStateChanged(String str, String str2, String str3) throws RemoteException;

    void onUsbMassStorageConnectionChanged(boolean z) throws RemoteException;

    void onVolumeForgotten(String str) throws RemoteException;

    void onVolumeRecordChanged(VolumeRecord volumeRecord) throws RemoteException;

    void onVolumeStateChanged(VolumeInfo volumeInfo, int i, int i2) throws RemoteException;
}
