package android.content;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IClipboard extends IInterface {

    public static class Default implements IClipboard {
        public void setPrimaryClip(ClipData clip, String callingPackage, int userId) throws RemoteException {
        }

        public void clearPrimaryClip(String callingPackage, int userId) throws RemoteException {
        }

        public ClipData getPrimaryClip(String pkg, int userId) throws RemoteException {
            return null;
        }

        public ClipDescription getPrimaryClipDescription(String callingPackage, int userId) throws RemoteException {
            return null;
        }

        public boolean hasPrimaryClip(String callingPackage, int userId) throws RemoteException {
            return false;
        }

        public void addPrimaryClipChangedListener(IOnPrimaryClipChangedListener listener, String callingPackage, int userId) throws RemoteException {
        }

        public void removePrimaryClipChangedListener(IOnPrimaryClipChangedListener listener, String callingPackage, int userId) throws RemoteException {
        }

        public boolean hasClipboardText(String callingPackage, int userId) throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IClipboard {
        private static final String DESCRIPTOR = "android.content.IClipboard";
        static final int TRANSACTION_addPrimaryClipChangedListener = 6;
        static final int TRANSACTION_clearPrimaryClip = 2;
        static final int TRANSACTION_getPrimaryClip = 3;
        static final int TRANSACTION_getPrimaryClipDescription = 4;
        static final int TRANSACTION_hasClipboardText = 8;
        static final int TRANSACTION_hasPrimaryClip = 5;
        static final int TRANSACTION_removePrimaryClipChangedListener = 7;
        static final int TRANSACTION_setPrimaryClip = 1;

        private static class Proxy implements IClipboard {
            public static IClipboard sDefaultImpl;
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

            public void setPrimaryClip(ClipData clip, String callingPackage, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (clip != null) {
                        _data.writeInt(1);
                        clip.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPrimaryClip(clip, callingPackage, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearPrimaryClip(String callingPackage, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearPrimaryClip(callingPackage, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ClipData getPrimaryClip(String pkg, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(userId);
                    ClipData clipData = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        clipData = Stub.getDefaultImpl();
                        if (clipData != 0) {
                            clipData = Stub.getDefaultImpl().getPrimaryClip(pkg, userId);
                            return clipData;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        clipData = (ClipData) ClipData.CREATOR.createFromParcel(_reply);
                    } else {
                        clipData = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return clipData;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ClipDescription getPrimaryClipDescription(String callingPackage, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(userId);
                    ClipDescription clipDescription = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        clipDescription = Stub.getDefaultImpl();
                        if (clipDescription != 0) {
                            clipDescription = Stub.getDefaultImpl().getPrimaryClipDescription(callingPackage, userId);
                            return clipDescription;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        clipDescription = (ClipDescription) ClipDescription.CREATOR.createFromParcel(_reply);
                    } else {
                        clipDescription = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return clipDescription;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean hasPrimaryClip(String callingPackage, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().hasPrimaryClip(callingPackage, userId);
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

            public void addPrimaryClipChangedListener(IOnPrimaryClipChangedListener listener, String callingPackage, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    _data.writeString(callingPackage);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addPrimaryClipChangedListener(listener, callingPackage, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removePrimaryClipChangedListener(IOnPrimaryClipChangedListener listener, String callingPackage, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    _data.writeString(callingPackage);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removePrimaryClipChangedListener(listener, callingPackage, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean hasClipboardText(String callingPackage, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().hasClipboardText(callingPackage, userId);
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

        public static IClipboard asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IClipboard)) {
                return new Proxy(obj);
            }
            return (IClipboard) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "setPrimaryClip";
                case 2:
                    return "clearPrimaryClip";
                case 3:
                    return "getPrimaryClip";
                case 4:
                    return "getPrimaryClipDescription";
                case 5:
                    return "hasPrimaryClip";
                case 6:
                    return "addPrimaryClipChangedListener";
                case 7:
                    return "removePrimaryClipChangedListener";
                case 8:
                    return "hasClipboardText";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code != IBinder.INTERFACE_TRANSACTION) {
                boolean _result;
                switch (code) {
                    case 1:
                        ClipData _arg0;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ClipData) ClipData.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        setPrimaryClip(_arg0, data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        clearPrimaryClip(data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        ClipData _result2 = getPrimaryClip(data.readString(), data.readInt());
                        reply.writeNoException();
                        if (_result2 != null) {
                            reply.writeInt(1);
                            _result2.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        ClipDescription _result3 = getPrimaryClipDescription(data.readString(), data.readInt());
                        reply.writeNoException();
                        if (_result3 != null) {
                            reply.writeInt(1);
                            _result3.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        _result = hasPrimaryClip(data.readString(), data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        addPrimaryClipChangedListener(android.content.IOnPrimaryClipChangedListener.Stub.asInterface(data.readStrongBinder()), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        removePrimaryClipChangedListener(android.content.IOnPrimaryClipChangedListener.Stub.asInterface(data.readStrongBinder()), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        _result = hasClipboardText(data.readString(), data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IClipboard impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IClipboard getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void addPrimaryClipChangedListener(IOnPrimaryClipChangedListener iOnPrimaryClipChangedListener, String str, int i) throws RemoteException;

    void clearPrimaryClip(String str, int i) throws RemoteException;

    ClipData getPrimaryClip(String str, int i) throws RemoteException;

    ClipDescription getPrimaryClipDescription(String str, int i) throws RemoteException;

    boolean hasClipboardText(String str, int i) throws RemoteException;

    boolean hasPrimaryClip(String str, int i) throws RemoteException;

    void removePrimaryClipChangedListener(IOnPrimaryClipChangedListener iOnPrimaryClipChangedListener, String str, int i) throws RemoteException;

    void setPrimaryClip(ClipData clipData, String str, int i) throws RemoteException;
}
