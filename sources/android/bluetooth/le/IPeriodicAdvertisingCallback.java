package android.bluetooth.le;

import android.bluetooth.BluetoothDevice;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IPeriodicAdvertisingCallback extends IInterface {

    public static class Default implements IPeriodicAdvertisingCallback {
        public void onSyncEstablished(int syncHandle, BluetoothDevice device, int advertisingSid, int skip, int timeout, int status) throws RemoteException {
        }

        public void onPeriodicAdvertisingReport(PeriodicAdvertisingReport report) throws RemoteException {
        }

        public void onSyncLost(int syncHandle) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IPeriodicAdvertisingCallback {
        private static final String DESCRIPTOR = "android.bluetooth.le.IPeriodicAdvertisingCallback";
        static final int TRANSACTION_onPeriodicAdvertisingReport = 2;
        static final int TRANSACTION_onSyncEstablished = 1;
        static final int TRANSACTION_onSyncLost = 3;

        private static class Proxy implements IPeriodicAdvertisingCallback {
            public static IPeriodicAdvertisingCallback sDefaultImpl;
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

            public void onSyncEstablished(int syncHandle, BluetoothDevice device, int advertisingSid, int skip, int timeout, int status) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                int i3;
                int i4;
                BluetoothDevice bluetoothDevice = device;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(syncHandle);
                        if (bluetoothDevice != null) {
                            _data.writeInt(1);
                            bluetoothDevice.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        i = advertisingSid;
                        i2 = skip;
                        i3 = timeout;
                        i4 = status;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(advertisingSid);
                        try {
                            _data.writeInt(skip);
                            try {
                                _data.writeInt(timeout);
                                try {
                                    _data.writeInt(status);
                                } catch (Throwable th3) {
                                    th = th3;
                                    _data.recycle();
                                    throw th;
                                }
                            } catch (Throwable th4) {
                                th = th4;
                                i4 = status;
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th5) {
                            th = th5;
                            i3 = timeout;
                            i4 = status;
                            _data.recycle();
                            throw th;
                        }
                        try {
                            if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().onSyncEstablished(syncHandle, device, advertisingSid, skip, timeout, status);
                            _data.recycle();
                        } catch (Throwable th6) {
                            th = th6;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th7) {
                        th = th7;
                        i2 = skip;
                        i3 = timeout;
                        i4 = status;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th8) {
                    th = th8;
                    int i5 = syncHandle;
                    i = advertisingSid;
                    i2 = skip;
                    i3 = timeout;
                    i4 = status;
                    _data.recycle();
                    throw th;
                }
            }

            public void onPeriodicAdvertisingReport(PeriodicAdvertisingReport report) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (report != null) {
                        _data.writeInt(1);
                        report.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onPeriodicAdvertisingReport(report);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onSyncLost(int syncHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(syncHandle);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onSyncLost(syncHandle);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPeriodicAdvertisingCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IPeriodicAdvertisingCallback)) {
                return new Proxy(obj);
            }
            return (IPeriodicAdvertisingCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onSyncEstablished";
            }
            if (transactionCode == 2) {
                return "onPeriodicAdvertisingReport";
            }
            if (transactionCode != 3) {
                return null;
            }
            return "onSyncLost";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            String descriptor = DESCRIPTOR;
            Parcel parcel2;
            if (i == 1) {
                BluetoothDevice _arg1;
                parcel2 = reply;
                parcel.enforceInterface(descriptor);
                int _arg0 = data.readInt();
                if (data.readInt() != 0) {
                    _arg1 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                } else {
                    _arg1 = null;
                }
                onSyncEstablished(_arg0, _arg1, data.readInt(), data.readInt(), data.readInt(), data.readInt());
                return true;
            } else if (i == 2) {
                PeriodicAdvertisingReport _arg02;
                parcel2 = reply;
                parcel.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg02 = (PeriodicAdvertisingReport) PeriodicAdvertisingReport.CREATOR.createFromParcel(parcel);
                } else {
                    _arg02 = null;
                }
                onPeriodicAdvertisingReport(_arg02);
                return true;
            } else if (i == 3) {
                parcel2 = reply;
                parcel.enforceInterface(descriptor);
                onSyncLost(data.readInt());
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IPeriodicAdvertisingCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IPeriodicAdvertisingCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onPeriodicAdvertisingReport(PeriodicAdvertisingReport periodicAdvertisingReport) throws RemoteException;

    void onSyncEstablished(int i, BluetoothDevice bluetoothDevice, int i2, int i3, int i4, int i5) throws RemoteException;

    void onSyncLost(int i) throws RemoteException;
}
