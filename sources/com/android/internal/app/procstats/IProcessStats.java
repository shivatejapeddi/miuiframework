package com.android.internal.app.procstats;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface IProcessStats extends IInterface {

    public static class Default implements IProcessStats {
        public byte[] getCurrentStats(List<ParcelFileDescriptor> list) throws RemoteException {
            return null;
        }

        public ParcelFileDescriptor getStatsOverTime(long minTime) throws RemoteException {
            return null;
        }

        public int getCurrentMemoryState() throws RemoteException {
            return 0;
        }

        public long getCommittedStats(long highWaterMarkMs, int section, boolean doAggregate, List<ParcelFileDescriptor> list) throws RemoteException {
            return 0;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IProcessStats {
        private static final String DESCRIPTOR = "com.android.internal.app.procstats.IProcessStats";
        static final int TRANSACTION_getCommittedStats = 4;
        static final int TRANSACTION_getCurrentMemoryState = 3;
        static final int TRANSACTION_getCurrentStats = 1;
        static final int TRANSACTION_getStatsOverTime = 2;

        private static class Proxy implements IProcessStats {
            public static IProcessStats sDefaultImpl;
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

            public byte[] getCurrentStats(List<ParcelFileDescriptor> historic) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    byte[] bArr = 1;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        bArr = Stub.getDefaultImpl();
                        if (bArr != 0) {
                            bArr = Stub.getDefaultImpl().getCurrentStats(historic);
                            return bArr;
                        }
                    }
                    _reply.readException();
                    bArr = _reply.createByteArray();
                    byte[] _result = bArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParcelFileDescriptor getStatsOverTime(long minTime) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(minTime);
                    ParcelFileDescriptor parcelFileDescriptor = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        parcelFileDescriptor = Stub.getDefaultImpl();
                        if (parcelFileDescriptor != 0) {
                            parcelFileDescriptor = Stub.getDefaultImpl().getStatsOverTime(minTime);
                            return parcelFileDescriptor;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parcelFileDescriptor = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(_reply);
                    } else {
                        parcelFileDescriptor = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parcelFileDescriptor;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCurrentMemoryState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getCurrentMemoryState();
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

            public long getCommittedStats(long highWaterMarkMs, int section, boolean doAggregate, List<ParcelFileDescriptor> committedStats) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(highWaterMarkMs);
                    _data.writeInt(section);
                    _data.writeInt(doAggregate ? 1 : 0);
                    long j = this.mRemote;
                    if (!j.transact(4, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != null) {
                            j = Stub.getDefaultImpl().getCommittedStats(highWaterMarkMs, section, doAggregate, committedStats);
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IProcessStats asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IProcessStats)) {
                return new Proxy(obj);
            }
            return (IProcessStats) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "getCurrentStats";
            }
            if (transactionCode == 2) {
                return "getStatsOverTime";
            }
            if (transactionCode == 3) {
                return "getCurrentMemoryState";
            }
            if (transactionCode != 4) {
                return null;
            }
            return "getCommittedStats";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                byte[] _result = getCurrentStats(new ArrayList());
                reply.writeNoException();
                reply.writeByteArray(_result);
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                ParcelFileDescriptor _result2 = getStatsOverTime(data.readLong());
                reply.writeNoException();
                if (_result2 != null) {
                    reply.writeInt(1);
                    _result2.writeToParcel(reply, 1);
                } else {
                    reply.writeInt(0);
                }
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                int _result3 = getCurrentMemoryState();
                reply.writeNoException();
                reply.writeInt(_result3);
                return true;
            } else if (code == 4) {
                data.enforceInterface(descriptor);
                long _result4 = getCommittedStats(data.readLong(), data.readInt(), data.readInt() != 0, new ArrayList());
                reply.writeNoException();
                reply.writeLong(_result4);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IProcessStats impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IProcessStats getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    long getCommittedStats(long j, int i, boolean z, List<ParcelFileDescriptor> list) throws RemoteException;

    int getCurrentMemoryState() throws RemoteException;

    byte[] getCurrentStats(List<ParcelFileDescriptor> list) throws RemoteException;

    ParcelFileDescriptor getStatsOverTime(long j) throws RemoteException;
}
