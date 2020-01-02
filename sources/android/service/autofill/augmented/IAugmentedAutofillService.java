package android.service.autofill.augmented;

import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillValue;

public interface IAugmentedAutofillService extends IInterface {

    public static abstract class Stub extends Binder implements IAugmentedAutofillService {
        private static final String DESCRIPTOR = "android.service.autofill.augmented.IAugmentedAutofillService";
        static final int TRANSACTION_onConnected = 1;
        static final int TRANSACTION_onDestroyAllFillWindowsRequest = 4;
        static final int TRANSACTION_onDisconnected = 2;
        static final int TRANSACTION_onFillRequest = 3;

        private static class Proxy implements IAugmentedAutofillService {
            public static IAugmentedAutofillService sDefaultImpl;
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

            public void onConnected(boolean debug, boolean verbose) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    _data.writeInt(debug ? 1 : 0);
                    if (verbose) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onConnected(debug, verbose);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onDisconnected() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onDisconnected();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onFillRequest(int sessionId, IBinder autofillManagerClient, int taskId, ComponentName activityComponent, AutofillId focusedId, AutofillValue focusedValue, long requestTime, IFillCallback callback) throws RemoteException {
                Throwable th;
                ComponentName componentName = activityComponent;
                AutofillId autofillId = focusedId;
                AutofillValue autofillValue = focusedValue;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(sessionId);
                        _data.writeStrongBinder(autofillManagerClient);
                        _data.writeInt(taskId);
                        if (componentName != null) {
                            _data.writeInt(1);
                            componentName.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (autofillId != null) {
                            _data.writeInt(1);
                            autofillId.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (autofillValue != null) {
                            _data.writeInt(1);
                            autofillValue.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        _data.writeLong(requestTime);
                        _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                        if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().onFillRequest(sessionId, autofillManagerClient, taskId, activityComponent, focusedId, focusedValue, requestTime, callback);
                        _data.recycle();
                    } catch (Throwable th2) {
                        th = th2;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    int i = sessionId;
                    _data.recycle();
                    throw th;
                }
            }

            public void onDestroyAllFillWindowsRequest() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onDestroyAllFillWindowsRequest();
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAugmentedAutofillService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IAugmentedAutofillService)) {
                return new Proxy(obj);
            }
            return (IAugmentedAutofillService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onConnected";
            }
            if (transactionCode == 2) {
                return "onDisconnected";
            }
            if (transactionCode == 3) {
                return "onFillRequest";
            }
            if (transactionCode != 4) {
                return null;
            }
            return "onDestroyAllFillWindowsRequest";
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
                boolean _arg1 = false;
                boolean _arg0 = data.readInt() != 0;
                if (data.readInt() != 0) {
                    _arg1 = true;
                }
                onConnected(_arg0, _arg1);
                return true;
            } else if (i == 2) {
                parcel2 = reply;
                parcel.enforceInterface(descriptor);
                onDisconnected();
                return true;
            } else if (i == 3) {
                ComponentName _arg3;
                AutofillId _arg4;
                AutofillValue _arg5;
                parcel2 = reply;
                parcel.enforceInterface(descriptor);
                int _arg02 = data.readInt();
                IBinder _arg12 = data.readStrongBinder();
                int _arg2 = data.readInt();
                if (data.readInt() != 0) {
                    _arg3 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                } else {
                    _arg3 = null;
                }
                if (data.readInt() != 0) {
                    _arg4 = (AutofillId) AutofillId.CREATOR.createFromParcel(parcel);
                } else {
                    _arg4 = null;
                }
                if (data.readInt() != 0) {
                    _arg5 = (AutofillValue) AutofillValue.CREATOR.createFromParcel(parcel);
                } else {
                    _arg5 = null;
                }
                onFillRequest(_arg02, _arg12, _arg2, _arg3, _arg4, _arg5, data.readLong(), android.service.autofill.augmented.IFillCallback.Stub.asInterface(data.readStrongBinder()));
                return true;
            } else if (i == 4) {
                parcel2 = reply;
                parcel.enforceInterface(descriptor);
                onDestroyAllFillWindowsRequest();
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IAugmentedAutofillService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IAugmentedAutofillService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    public static class Default implements IAugmentedAutofillService {
        public void onConnected(boolean debug, boolean verbose) throws RemoteException {
        }

        public void onDisconnected() throws RemoteException {
        }

        public void onFillRequest(int sessionId, IBinder autofillManagerClient, int taskId, ComponentName activityComponent, AutofillId focusedId, AutofillValue focusedValue, long requestTime, IFillCallback callback) throws RemoteException {
        }

        public void onDestroyAllFillWindowsRequest() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    void onConnected(boolean z, boolean z2) throws RemoteException;

    void onDestroyAllFillWindowsRequest() throws RemoteException;

    void onDisconnected() throws RemoteException;

    void onFillRequest(int i, IBinder iBinder, int i2, ComponentName componentName, AutofillId autofillId, AutofillValue autofillValue, long j, IFillCallback iFillCallback) throws RemoteException;
}
