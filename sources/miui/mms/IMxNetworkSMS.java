package miui.mms;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IMxNetworkSMS extends IInterface {

    public static class Default implements IMxNetworkSMS {
        public boolean isMxOnline(String address) throws RemoteException {
            return false;
        }

        public String[] sendNetwokSMS(String address, String body, long threadId, boolean mSendByMx, int slotId) throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMxNetworkSMS {
        private static final String DESCRIPTOR = "miui.mms.IMxNetworkSMS";
        static final int TRANSACTION_isMxOnline = 1;
        static final int TRANSACTION_sendNetwokSMS = 2;

        private static class Proxy implements IMxNetworkSMS {
            public static IMxNetworkSMS sDefaultImpl;
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

            public boolean isMxOnline(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
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
                    z = Stub.getDefaultImpl().isMxOnline(address);
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] sendNetwokSMS(String address, String body, long threadId, boolean mSendByMx, int slotId) throws RemoteException {
                Throwable th;
                long j;
                int i;
                String str;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(address);
                        try {
                            _data.writeString(body);
                        } catch (Throwable th2) {
                            th = th2;
                            j = threadId;
                            i = slotId;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeLong(threadId);
                            _data.writeInt(mSendByMx ? 1 : 0);
                        } catch (Throwable th3) {
                            th = th3;
                            i = slotId;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeInt(slotId);
                            try {
                                if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                    _reply.readException();
                                    String[] _result = _reply.createStringArray();
                                    _reply.recycle();
                                    _data.recycle();
                                    return _result;
                                }
                                String[] sendNetwokSMS = Stub.getDefaultImpl().sendNetwokSMS(address, body, threadId, mSendByMx, slotId);
                                _reply.recycle();
                                _data.recycle();
                                return sendNetwokSMS;
                            } catch (Throwable th4) {
                                th = th4;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th5) {
                            th = th5;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        str = body;
                        j = threadId;
                        i = slotId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    String str2 = address;
                    str = body;
                    j = threadId;
                    i = slotId;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMxNetworkSMS asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IMxNetworkSMS)) {
                return new Proxy(obj);
            }
            return (IMxNetworkSMS) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "isMxOnline";
            }
            if (transactionCode != 2) {
                return null;
            }
            return "sendNetwokSMS";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            Parcel parcel2 = reply;
            String descriptor = DESCRIPTOR;
            if (i == 1) {
                parcel.enforceInterface(descriptor);
                boolean _result = isMxOnline(data.readString());
                reply.writeNoException();
                parcel2.writeInt(_result);
                return true;
            } else if (i == 2) {
                parcel.enforceInterface(descriptor);
                String[] _result2 = sendNetwokSMS(data.readString(), data.readString(), data.readLong(), data.readInt() != 0, data.readInt());
                reply.writeNoException();
                parcel2.writeStringArray(_result2);
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(code, data, reply, flags);
            } else {
                parcel2.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IMxNetworkSMS impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IMxNetworkSMS getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    boolean isMxOnline(String str) throws RemoteException;

    String[] sendNetwokSMS(String str, String str2, long j, boolean z, int i) throws RemoteException;
}
