package android.location;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IGpsGeofenceHardware extends IInterface {

    public static class Default implements IGpsGeofenceHardware {
        public boolean isHardwareGeofenceSupported() throws RemoteException {
            return false;
        }

        public boolean addCircularHardwareGeofence(int geofenceId, double latitude, double longitude, double radius, int lastTransition, int monitorTransition, int notificationResponsiveness, int unknownTimer) throws RemoteException {
            return false;
        }

        public boolean removeHardwareGeofence(int geofenceId) throws RemoteException {
            return false;
        }

        public boolean pauseHardwareGeofence(int geofenceId) throws RemoteException {
            return false;
        }

        public boolean resumeHardwareGeofence(int geofenceId, int monitorTransition) throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IGpsGeofenceHardware {
        private static final String DESCRIPTOR = "android.location.IGpsGeofenceHardware";
        static final int TRANSACTION_addCircularHardwareGeofence = 2;
        static final int TRANSACTION_isHardwareGeofenceSupported = 1;
        static final int TRANSACTION_pauseHardwareGeofence = 4;
        static final int TRANSACTION_removeHardwareGeofence = 3;
        static final int TRANSACTION_resumeHardwareGeofence = 5;

        private static class Proxy implements IGpsGeofenceHardware {
            public static IGpsGeofenceHardware sDefaultImpl;
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

            public boolean isHardwareGeofenceSupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    if (this.mRemote.transact(1, _data, _reply, z) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() != 0) {
                            z = true;
                        }
                        boolean _result = z;
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    z = Stub.getDefaultImpl().isHardwareGeofenceSupported();
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean addCircularHardwareGeofence(int geofenceId, double latitude, double longitude, double radius, int lastTransition, int monitorTransition, int notificationResponsiveness, int unknownTimer) throws RemoteException {
                Throwable th;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(geofenceId);
                        _data.writeDouble(latitude);
                        _data.writeDouble(longitude);
                        _data.writeDouble(radius);
                        _data.writeInt(lastTransition);
                        _data.writeInt(monitorTransition);
                        _data.writeInt(notificationResponsiveness);
                        _data.writeInt(unknownTimer);
                        boolean z = false;
                        if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            if (_reply.readInt() != 0) {
                                z = true;
                            }
                            boolean _result = z;
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        }
                        boolean addCircularHardwareGeofence = Stub.getDefaultImpl().addCircularHardwareGeofence(geofenceId, latitude, longitude, radius, lastTransition, monitorTransition, notificationResponsiveness, unknownTimer);
                        _reply.recycle();
                        _data.recycle();
                        return addCircularHardwareGeofence;
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    int i = geofenceId;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public boolean removeHardwareGeofence(int geofenceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(geofenceId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().removeHardwareGeofence(geofenceId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean pauseHardwareGeofence(int geofenceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(geofenceId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().pauseHardwareGeofence(geofenceId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean resumeHardwareGeofence(int geofenceId, int monitorTransition) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(geofenceId);
                    _data.writeInt(monitorTransition);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().resumeHardwareGeofence(geofenceId, monitorTransition);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IGpsGeofenceHardware asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IGpsGeofenceHardware)) {
                return new Proxy(obj);
            }
            return (IGpsGeofenceHardware) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "isHardwareGeofenceSupported";
            }
            if (transactionCode == 2) {
                return "addCircularHardwareGeofence";
            }
            if (transactionCode == 3) {
                return "removeHardwareGeofence";
            }
            if (transactionCode == 4) {
                return "pauseHardwareGeofence";
            }
            if (transactionCode != 5) {
                return null;
            }
            return "resumeHardwareGeofence";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            Parcel parcel2 = reply;
            String descriptor = DESCRIPTOR;
            boolean z;
            boolean _result;
            boolean _result2;
            if (i == 1) {
                z = true;
                parcel.enforceInterface(descriptor);
                _result = isHardwareGeofenceSupported();
                reply.writeNoException();
                parcel2.writeInt(_result);
                return z;
            } else if (i == 2) {
                parcel.enforceInterface(descriptor);
                z = true;
                _result = addCircularHardwareGeofence(data.readInt(), data.readDouble(), data.readDouble(), data.readDouble(), data.readInt(), data.readInt(), data.readInt(), data.readInt());
                reply.writeNoException();
                parcel2.writeInt(_result);
                return z;
            } else if (i == 3) {
                parcel.enforceInterface(descriptor);
                _result2 = removeHardwareGeofence(data.readInt());
                reply.writeNoException();
                parcel2.writeInt(_result2);
                return true;
            } else if (i == 4) {
                parcel.enforceInterface(descriptor);
                _result2 = pauseHardwareGeofence(data.readInt());
                reply.writeNoException();
                parcel2.writeInt(_result2);
                return true;
            } else if (i == 5) {
                parcel.enforceInterface(descriptor);
                boolean _result3 = resumeHardwareGeofence(data.readInt(), data.readInt());
                reply.writeNoException();
                parcel2.writeInt(_result3);
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(code, data, reply, flags);
            } else {
                parcel2.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IGpsGeofenceHardware impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IGpsGeofenceHardware getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    boolean addCircularHardwareGeofence(int i, double d, double d2, double d3, int i2, int i3, int i4, int i5) throws RemoteException;

    boolean isHardwareGeofenceSupported() throws RemoteException;

    boolean pauseHardwareGeofence(int i) throws RemoteException;

    boolean removeHardwareGeofence(int i) throws RemoteException;

    boolean resumeHardwareGeofence(int i, int i2) throws RemoteException;
}
