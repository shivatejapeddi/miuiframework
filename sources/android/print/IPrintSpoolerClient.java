package android.print;

import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IPrintSpoolerClient extends IInterface {

    public static class Default implements IPrintSpoolerClient {
        public void onPrintJobQueued(PrintJobInfo printJob) throws RemoteException {
        }

        public void onAllPrintJobsForServiceHandled(ComponentName printService) throws RemoteException {
        }

        public void onAllPrintJobsHandled() throws RemoteException {
        }

        public void onPrintJobStateChanged(PrintJobInfo printJob) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IPrintSpoolerClient {
        private static final String DESCRIPTOR = "android.print.IPrintSpoolerClient";
        static final int TRANSACTION_onAllPrintJobsForServiceHandled = 2;
        static final int TRANSACTION_onAllPrintJobsHandled = 3;
        static final int TRANSACTION_onPrintJobQueued = 1;
        static final int TRANSACTION_onPrintJobStateChanged = 4;

        private static class Proxy implements IPrintSpoolerClient {
            public static IPrintSpoolerClient sDefaultImpl;
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

            public void onPrintJobQueued(PrintJobInfo printJob) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (printJob != null) {
                        _data.writeInt(1);
                        printJob.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onPrintJobQueued(printJob);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onAllPrintJobsForServiceHandled(ComponentName printService) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (printService != null) {
                        _data.writeInt(1);
                        printService.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onAllPrintJobsForServiceHandled(printService);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onAllPrintJobsHandled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onAllPrintJobsHandled();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onPrintJobStateChanged(PrintJobInfo printJob) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (printJob != null) {
                        _data.writeInt(1);
                        printJob.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onPrintJobStateChanged(printJob);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPrintSpoolerClient asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IPrintSpoolerClient)) {
                return new Proxy(obj);
            }
            return (IPrintSpoolerClient) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onPrintJobQueued";
            }
            if (transactionCode == 2) {
                return "onAllPrintJobsForServiceHandled";
            }
            if (transactionCode == 3) {
                return "onAllPrintJobsHandled";
            }
            if (transactionCode != 4) {
                return null;
            }
            return "onPrintJobStateChanged";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            PrintJobInfo _arg0;
            if (code == 1) {
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (PrintJobInfo) PrintJobInfo.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                onPrintJobQueued(_arg0);
                return true;
            } else if (code == 2) {
                ComponentName _arg02;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg02 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                } else {
                    _arg02 = null;
                }
                onAllPrintJobsForServiceHandled(_arg02);
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                onAllPrintJobsHandled();
                return true;
            } else if (code == 4) {
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (PrintJobInfo) PrintJobInfo.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                onPrintJobStateChanged(_arg0);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IPrintSpoolerClient impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IPrintSpoolerClient getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onAllPrintJobsForServiceHandled(ComponentName componentName) throws RemoteException;

    void onAllPrintJobsHandled() throws RemoteException;

    void onPrintJobQueued(PrintJobInfo printJobInfo) throws RemoteException;

    void onPrintJobStateChanged(PrintJobInfo printJobInfo) throws RemoteException;
}
