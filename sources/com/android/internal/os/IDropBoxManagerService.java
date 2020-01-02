package com.android.internal.os;

import android.annotation.UnsupportedAppUsage;
import android.os.Binder;
import android.os.DropBoxManager.Entry;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IDropBoxManagerService extends IInterface {

    public static class Default implements IDropBoxManagerService {
        public void add(Entry entry) throws RemoteException {
        }

        public boolean isTagEnabled(String tag) throws RemoteException {
            return false;
        }

        public Entry getNextEntry(String tag, long millis, String packageName) throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IDropBoxManagerService {
        private static final String DESCRIPTOR = "com.android.internal.os.IDropBoxManagerService";
        static final int TRANSACTION_add = 1;
        static final int TRANSACTION_getNextEntry = 3;
        static final int TRANSACTION_isTagEnabled = 2;

        private static class Proxy implements IDropBoxManagerService {
            public static IDropBoxManagerService sDefaultImpl;
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

            public void add(Entry entry) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (entry != null) {
                        _data.writeInt(1);
                        entry.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().add(entry);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isTagEnabled(String tag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(tag);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isTagEnabled(tag);
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

            public Entry getNextEntry(String tag, long millis, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(tag);
                    _data.writeLong(millis);
                    _data.writeString(packageName);
                    Entry entry = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        entry = Stub.getDefaultImpl();
                        if (entry != 0) {
                            entry = Stub.getDefaultImpl().getNextEntry(tag, millis, packageName);
                            return entry;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        entry = (Entry) Entry.CREATOR.createFromParcel(_reply);
                    } else {
                        entry = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return entry;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IDropBoxManagerService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IDropBoxManagerService)) {
                return new Proxy(obj);
            }
            return (IDropBoxManagerService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "add";
            }
            if (transactionCode == 2) {
                return "isTagEnabled";
            }
            if (transactionCode != 3) {
                return null;
            }
            return "getNextEntry";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                Entry _arg0;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (Entry) Entry.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                add(_arg0);
                reply.writeNoException();
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                boolean _result = isTagEnabled(data.readString());
                reply.writeNoException();
                reply.writeInt(_result);
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                Entry _result2 = getNextEntry(data.readString(), data.readLong(), data.readString());
                reply.writeNoException();
                if (_result2 != null) {
                    reply.writeInt(1);
                    _result2.writeToParcel(reply, 1);
                } else {
                    reply.writeInt(0);
                }
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IDropBoxManagerService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IDropBoxManagerService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void add(Entry entry) throws RemoteException;

    @UnsupportedAppUsage
    Entry getNextEntry(String str, long j, String str2) throws RemoteException;

    boolean isTagEnabled(String str) throws RemoteException;
}
