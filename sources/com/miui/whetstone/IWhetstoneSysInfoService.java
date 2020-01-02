package com.miui.whetstone;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IWhetstoneSysInfoService extends IInterface {

    public static class Default implements IWhetstoneSysInfoService {
        public long getSysInfo(String type) throws RemoteException {
            return 0;
        }

        public String[] getSysInfos(String[] type) throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IWhetstoneSysInfoService {
        private static final String DESCRIPTOR = "com.miui.whetstone.IWhetstoneSysInfoService";
        static final int TRANSACTION_getSysInfo = 1;
        static final int TRANSACTION_getSysInfos = 2;

        private static class Proxy implements IWhetstoneSysInfoService {
            public static IWhetstoneSysInfoService sDefaultImpl;
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

            public long getSysInfo(String type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(type);
                    long j = 1;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getSysInfo(type);
                            return j;
                        }
                    }
                    _reply.readException();
                    j = _reply.readLong();
                    long _result = j;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getSysInfos(String[] type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(type);
                    String[] strArr = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getSysInfos(type);
                            return strArr;
                        }
                    }
                    _reply.readException();
                    strArr = _reply.createStringArray();
                    String[] _result = strArr;
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

        public static IWhetstoneSysInfoService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IWhetstoneSysInfoService)) {
                return new Proxy(obj);
            }
            return (IWhetstoneSysInfoService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "getSysInfo";
            }
            if (transactionCode != 2) {
                return null;
            }
            return "getSysInfos";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                long _result = getSysInfo(data.readString());
                reply.writeNoException();
                reply.writeLong(_result);
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                String[] _result2 = getSysInfos(data.createStringArray());
                reply.writeNoException();
                reply.writeStringArray(_result2);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IWhetstoneSysInfoService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IWhetstoneSysInfoService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    long getSysInfo(String str) throws RemoteException;

    String[] getSysInfos(String[] strArr) throws RemoteException;
}
