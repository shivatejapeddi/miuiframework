package android.os;

public interface ISchedulingPolicyService extends IInterface {

    public static class Default implements ISchedulingPolicyService {
        public int requestPriority(int pid, int tid, int prio, boolean isForApp) throws RemoteException {
            return 0;
        }

        public int requestCpusetBoost(boolean enable, IBinder client) throws RemoteException {
            return 0;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ISchedulingPolicyService {
        private static final String DESCRIPTOR = "android.os.ISchedulingPolicyService";
        static final int TRANSACTION_requestCpusetBoost = 2;
        static final int TRANSACTION_requestPriority = 1;

        private static class Proxy implements ISchedulingPolicyService {
            public static ISchedulingPolicyService sDefaultImpl;
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

            public int requestPriority(int pid, int tid, int prio, boolean isForApp) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(pid);
                    _data.writeInt(tid);
                    _data.writeInt(prio);
                    int i = 0;
                    _data.writeInt(isForApp ? 1 : 0);
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().requestPriority(pid, tid, prio, isForApp);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int requestCpusetBoost(boolean enable, IBinder client) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    _data.writeStrongBinder(client);
                    int i = this.mRemote;
                    if (!i.transact(2, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().requestCpusetBoost(enable, client);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
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

        public static ISchedulingPolicyService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ISchedulingPolicyService)) {
                return new Proxy(obj);
            }
            return (ISchedulingPolicyService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "requestPriority";
            }
            if (transactionCode != 2) {
                return null;
            }
            return "requestCpusetBoost";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            boolean _arg0 = false;
            int _arg1;
            if (code == 1) {
                data.enforceInterface(descriptor);
                int _arg02 = data.readInt();
                _arg1 = data.readInt();
                int _arg2 = data.readInt();
                if (data.readInt() != 0) {
                    _arg0 = true;
                }
                int _result = requestPriority(_arg02, _arg1, _arg2, _arg0);
                reply.writeNoException();
                reply.writeInt(_result);
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = true;
                }
                _arg1 = requestCpusetBoost(_arg0, data.readStrongBinder());
                reply.writeNoException();
                reply.writeInt(_arg1);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(ISchedulingPolicyService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ISchedulingPolicyService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    int requestCpusetBoost(boolean z, IBinder iBinder) throws RemoteException;

    int requestPriority(int i, int i2, int i3, boolean z) throws RemoteException;
}
