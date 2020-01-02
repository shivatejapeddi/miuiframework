package android.location;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IGnssMeasurementsListener extends IInterface {

    public static abstract class Stub extends Binder implements IGnssMeasurementsListener {
        private static final String DESCRIPTOR = "android.location.IGnssMeasurementsListener";
        static final int TRANSACTION_onGnssMeasurementsReceived = 1;
        static final int TRANSACTION_onStatusChanged = 2;

        private static class Proxy implements IGnssMeasurementsListener {
            public static IGnssMeasurementsListener sDefaultImpl;
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

            public void onGnssMeasurementsReceived(GnssMeasurementsEvent event) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (event != null) {
                        _data.writeInt(1);
                        event.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onGnssMeasurementsReceived(event);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onStatusChanged(int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onStatusChanged(status);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IGnssMeasurementsListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IGnssMeasurementsListener)) {
                return new Proxy(obj);
            }
            return (IGnssMeasurementsListener) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onGnssMeasurementsReceived";
            }
            if (transactionCode != 2) {
                return null;
            }
            return "onStatusChanged";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                GnssMeasurementsEvent _arg0;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (GnssMeasurementsEvent) GnssMeasurementsEvent.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                onGnssMeasurementsReceived(_arg0);
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                onStatusChanged(data.readInt());
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IGnssMeasurementsListener impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IGnssMeasurementsListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    public static class Default implements IGnssMeasurementsListener {
        public void onGnssMeasurementsReceived(GnssMeasurementsEvent event) throws RemoteException {
        }

        public void onStatusChanged(int status) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    void onGnssMeasurementsReceived(GnssMeasurementsEvent gnssMeasurementsEvent) throws RemoteException;

    void onStatusChanged(int i) throws RemoteException;
}
