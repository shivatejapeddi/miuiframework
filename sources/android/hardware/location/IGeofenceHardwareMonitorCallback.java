package android.hardware.location;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IGeofenceHardwareMonitorCallback extends IInterface {

    public static abstract class Stub extends Binder implements IGeofenceHardwareMonitorCallback {
        private static final String DESCRIPTOR = "android.hardware.location.IGeofenceHardwareMonitorCallback";
        static final int TRANSACTION_onMonitoringSystemChange = 1;

        private static class Proxy implements IGeofenceHardwareMonitorCallback {
            public static IGeofenceHardwareMonitorCallback sDefaultImpl;
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

            public void onMonitoringSystemChange(GeofenceHardwareMonitorEvent event) throws RemoteException {
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
                        Stub.getDefaultImpl().onMonitoringSystemChange(event);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IGeofenceHardwareMonitorCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IGeofenceHardwareMonitorCallback)) {
                return new Proxy(obj);
            }
            return (IGeofenceHardwareMonitorCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode != 1) {
                return null;
            }
            return "onMonitoringSystemChange";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                GeofenceHardwareMonitorEvent _arg0;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (GeofenceHardwareMonitorEvent) GeofenceHardwareMonitorEvent.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                onMonitoringSystemChange(_arg0);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IGeofenceHardwareMonitorCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IGeofenceHardwareMonitorCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    public static class Default implements IGeofenceHardwareMonitorCallback {
        public void onMonitoringSystemChange(GeofenceHardwareMonitorEvent event) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    void onMonitoringSystemChange(GeofenceHardwareMonitorEvent geofenceHardwareMonitorEvent) throws RemoteException;
}
