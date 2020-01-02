package android.os;

public interface IHardwarePropertiesManager extends IInterface {

    public static class Default implements IHardwarePropertiesManager {
        public float[] getDeviceTemperatures(String callingPackage, int type, int source) throws RemoteException {
            return null;
        }

        public CpuUsageInfo[] getCpuUsages(String callingPackage) throws RemoteException {
            return null;
        }

        public float[] getFanSpeeds(String callingPackage) throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IHardwarePropertiesManager {
        private static final String DESCRIPTOR = "android.os.IHardwarePropertiesManager";
        static final int TRANSACTION_getCpuUsages = 2;
        static final int TRANSACTION_getDeviceTemperatures = 1;
        static final int TRANSACTION_getFanSpeeds = 3;

        private static class Proxy implements IHardwarePropertiesManager {
            public static IHardwarePropertiesManager sDefaultImpl;
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

            public float[] getDeviceTemperatures(String callingPackage, int type, int source) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(type);
                    _data.writeInt(source);
                    float[] fArr = 1;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        fArr = Stub.getDefaultImpl();
                        if (fArr != 0) {
                            fArr = Stub.getDefaultImpl().getDeviceTemperatures(callingPackage, type, source);
                            return fArr;
                        }
                    }
                    _reply.readException();
                    fArr = _reply.createFloatArray();
                    float[] _result = fArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public CpuUsageInfo[] getCpuUsages(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    CpuUsageInfo[] cpuUsageInfoArr = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        cpuUsageInfoArr = Stub.getDefaultImpl();
                        if (cpuUsageInfoArr != 0) {
                            cpuUsageInfoArr = Stub.getDefaultImpl().getCpuUsages(callingPackage);
                            return cpuUsageInfoArr;
                        }
                    }
                    _reply.readException();
                    CpuUsageInfo[] _result = (CpuUsageInfo[]) _reply.createTypedArray(CpuUsageInfo.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public float[] getFanSpeeds(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    float[] fArr = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        fArr = Stub.getDefaultImpl();
                        if (fArr != 0) {
                            fArr = Stub.getDefaultImpl().getFanSpeeds(callingPackage);
                            return fArr;
                        }
                    }
                    _reply.readException();
                    fArr = _reply.createFloatArray();
                    float[] _result = fArr;
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

        public static IHardwarePropertiesManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IHardwarePropertiesManager)) {
                return new Proxy(obj);
            }
            return (IHardwarePropertiesManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "getDeviceTemperatures";
            }
            if (transactionCode == 2) {
                return "getCpuUsages";
            }
            if (transactionCode != 3) {
                return null;
            }
            return "getFanSpeeds";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                float[] _result = getDeviceTemperatures(data.readString(), data.readInt(), data.readInt());
                reply.writeNoException();
                reply.writeFloatArray(_result);
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                CpuUsageInfo[] _result2 = getCpuUsages(data.readString());
                reply.writeNoException();
                reply.writeTypedArray(_result2, 1);
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                float[] _result3 = getFanSpeeds(data.readString());
                reply.writeNoException();
                reply.writeFloatArray(_result3);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IHardwarePropertiesManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IHardwarePropertiesManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    CpuUsageInfo[] getCpuUsages(String str) throws RemoteException;

    float[] getDeviceTemperatures(String str, int i, int i2) throws RemoteException;

    float[] getFanSpeeds(String str) throws RemoteException;
}
