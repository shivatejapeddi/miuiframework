package android.bluetooth.le;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IScannerCallback extends IInterface {

    public static abstract class Stub extends Binder implements IScannerCallback {
        private static final String DESCRIPTOR = "android.bluetooth.le.IScannerCallback";
        static final int TRANSACTION_onBatchScanResults = 3;
        static final int TRANSACTION_onFoundOrLost = 4;
        static final int TRANSACTION_onScanManagerErrorCallback = 5;
        static final int TRANSACTION_onScanResult = 2;
        static final int TRANSACTION_onScannerRegistered = 1;

        private static class Proxy implements IScannerCallback {
            public static IScannerCallback sDefaultImpl;
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

            public void onScannerRegistered(int status, int scannerId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status);
                    _data.writeInt(scannerId);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onScannerRegistered(status, scannerId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onScanResult(ScanResult scanResult) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (scanResult != null) {
                        _data.writeInt(1);
                        scanResult.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onScanResult(scanResult);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onBatchScanResults(List<ScanResult> batchResults) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(batchResults);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onBatchScanResults(batchResults);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onFoundOrLost(boolean onFound, ScanResult scanResult) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(onFound ? 1 : 0);
                    if (scanResult != null) {
                        _data.writeInt(1);
                        scanResult.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onFoundOrLost(onFound, scanResult);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onScanManagerErrorCallback(int errorCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(errorCode);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onScanManagerErrorCallback(errorCode);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IScannerCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IScannerCallback)) {
                return new Proxy(obj);
            }
            return (IScannerCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onScannerRegistered";
            }
            if (transactionCode == 2) {
                return "onScanResult";
            }
            if (transactionCode == 3) {
                return "onBatchScanResults";
            }
            if (transactionCode == 4) {
                return "onFoundOrLost";
            }
            if (transactionCode != 5) {
                return null;
            }
            return "onScanManagerErrorCallback";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                onScannerRegistered(data.readInt(), data.readInt());
                return true;
            } else if (code == 2) {
                ScanResult _arg0;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (ScanResult) ScanResult.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                onScanResult(_arg0);
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                onBatchScanResults(data.createTypedArrayList(ScanResult.CREATOR));
                return true;
            } else if (code == 4) {
                ScanResult _arg1;
                data.enforceInterface(descriptor);
                boolean _arg02 = data.readInt() != 0;
                if (data.readInt() != 0) {
                    _arg1 = (ScanResult) ScanResult.CREATOR.createFromParcel(data);
                } else {
                    _arg1 = null;
                }
                onFoundOrLost(_arg02, _arg1);
                return true;
            } else if (code == 5) {
                data.enforceInterface(descriptor);
                onScanManagerErrorCallback(data.readInt());
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IScannerCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IScannerCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    public static class Default implements IScannerCallback {
        public void onScannerRegistered(int status, int scannerId) throws RemoteException {
        }

        public void onScanResult(ScanResult scanResult) throws RemoteException {
        }

        public void onBatchScanResults(List<ScanResult> list) throws RemoteException {
        }

        public void onFoundOrLost(boolean onFound, ScanResult scanResult) throws RemoteException {
        }

        public void onScanManagerErrorCallback(int errorCode) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    void onBatchScanResults(List<ScanResult> list) throws RemoteException;

    void onFoundOrLost(boolean z, ScanResult scanResult) throws RemoteException;

    void onScanManagerErrorCallback(int i) throws RemoteException;

    void onScanResult(ScanResult scanResult) throws RemoteException;

    void onScannerRegistered(int i, int i2) throws RemoteException;
}
