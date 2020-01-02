package android.service.contentcapture;

import android.content.ComponentName;
import android.content.ContentCaptureOptions;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.contentcapture.ContentCaptureCondition;
import java.util.List;

public interface IContentCaptureServiceCallback extends IInterface {

    public static class Default implements IContentCaptureServiceCallback {
        public void setContentCaptureWhitelist(List<String> list, List<ComponentName> list2) throws RemoteException {
        }

        public void setContentCaptureConditions(String packageName, List<ContentCaptureCondition> list) throws RemoteException {
        }

        public void disableSelf() throws RemoteException {
        }

        public void writeSessionFlush(int sessionId, ComponentName app, FlushMetrics flushMetrics, ContentCaptureOptions options, int flushReason) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IContentCaptureServiceCallback {
        private static final String DESCRIPTOR = "android.service.contentcapture.IContentCaptureServiceCallback";
        static final int TRANSACTION_disableSelf = 3;
        static final int TRANSACTION_setContentCaptureConditions = 2;
        static final int TRANSACTION_setContentCaptureWhitelist = 1;
        static final int TRANSACTION_writeSessionFlush = 4;

        private static class Proxy implements IContentCaptureServiceCallback {
            public static IContentCaptureServiceCallback sDefaultImpl;
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

            public void setContentCaptureWhitelist(List<String> packages, List<ComponentName> activities) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(packages);
                    _data.writeTypedList(activities);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setContentCaptureWhitelist(packages, activities);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setContentCaptureConditions(String packageName, List<ContentCaptureCondition> conditions) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeTypedList(conditions);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setContentCaptureConditions(packageName, conditions);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void disableSelf() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().disableSelf();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void writeSessionFlush(int sessionId, ComponentName app, FlushMetrics flushMetrics, ContentCaptureOptions options, int flushReason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    if (app != null) {
                        _data.writeInt(1);
                        app.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (flushMetrics != null) {
                        _data.writeInt(1);
                        flushMetrics.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flushReason);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().writeSessionFlush(sessionId, app, flushMetrics, options, flushReason);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IContentCaptureServiceCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IContentCaptureServiceCallback)) {
                return new Proxy(obj);
            }
            return (IContentCaptureServiceCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "setContentCaptureWhitelist";
            }
            if (transactionCode == 2) {
                return "setContentCaptureConditions";
            }
            if (transactionCode == 3) {
                return "disableSelf";
            }
            if (transactionCode != 4) {
                return null;
            }
            return "writeSessionFlush";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            String descriptor = DESCRIPTOR;
            Parcel parcel2;
            if (i == 1) {
                parcel2 = reply;
                parcel.enforceInterface(descriptor);
                setContentCaptureWhitelist(data.createStringArrayList(), parcel.createTypedArrayList(ComponentName.CREATOR));
                return true;
            } else if (i == 2) {
                parcel2 = reply;
                parcel.enforceInterface(descriptor);
                setContentCaptureConditions(data.readString(), parcel.createTypedArrayList(ContentCaptureCondition.CREATOR));
                return true;
            } else if (i == 3) {
                parcel2 = reply;
                parcel.enforceInterface(descriptor);
                disableSelf();
                return true;
            } else if (i == 4) {
                ComponentName _arg1;
                FlushMetrics _arg2;
                ContentCaptureOptions _arg3;
                parcel2 = reply;
                parcel.enforceInterface(descriptor);
                int _arg0 = data.readInt();
                if (data.readInt() != 0) {
                    _arg1 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                } else {
                    _arg1 = null;
                }
                if (data.readInt() != 0) {
                    _arg2 = (FlushMetrics) FlushMetrics.CREATOR.createFromParcel(parcel);
                } else {
                    _arg2 = null;
                }
                if (data.readInt() != 0) {
                    _arg3 = (ContentCaptureOptions) ContentCaptureOptions.CREATOR.createFromParcel(parcel);
                } else {
                    _arg3 = null;
                }
                writeSessionFlush(_arg0, _arg1, _arg2, _arg3, data.readInt());
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IContentCaptureServiceCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IContentCaptureServiceCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void disableSelf() throws RemoteException;

    void setContentCaptureConditions(String str, List<ContentCaptureCondition> list) throws RemoteException;

    void setContentCaptureWhitelist(List<String> list, List<ComponentName> list2) throws RemoteException;

    void writeSessionFlush(int i, ComponentName componentName, FlushMetrics flushMetrics, ContentCaptureOptions contentCaptureOptions, int i2) throws RemoteException;
}
