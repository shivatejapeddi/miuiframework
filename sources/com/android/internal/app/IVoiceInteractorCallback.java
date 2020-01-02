package com.android.internal.app;

import android.app.VoiceInteractor.PickOptionRequest.Option;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IVoiceInteractorCallback extends IInterface {

    public static class Default implements IVoiceInteractorCallback {
        public void deliverConfirmationResult(IVoiceInteractorRequest request, boolean confirmed, Bundle result) throws RemoteException {
        }

        public void deliverPickOptionResult(IVoiceInteractorRequest request, boolean finished, Option[] selections, Bundle result) throws RemoteException {
        }

        public void deliverCompleteVoiceResult(IVoiceInteractorRequest request, Bundle result) throws RemoteException {
        }

        public void deliverAbortVoiceResult(IVoiceInteractorRequest request, Bundle result) throws RemoteException {
        }

        public void deliverCommandResult(IVoiceInteractorRequest request, boolean finished, Bundle result) throws RemoteException {
        }

        public void deliverCancel(IVoiceInteractorRequest request) throws RemoteException {
        }

        public void destroy() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IVoiceInteractorCallback {
        private static final String DESCRIPTOR = "com.android.internal.app.IVoiceInteractorCallback";
        static final int TRANSACTION_deliverAbortVoiceResult = 4;
        static final int TRANSACTION_deliverCancel = 6;
        static final int TRANSACTION_deliverCommandResult = 5;
        static final int TRANSACTION_deliverCompleteVoiceResult = 3;
        static final int TRANSACTION_deliverConfirmationResult = 1;
        static final int TRANSACTION_deliverPickOptionResult = 2;
        static final int TRANSACTION_destroy = 7;

        private static class Proxy implements IVoiceInteractorCallback {
            public static IVoiceInteractorCallback sDefaultImpl;
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

            public void deliverConfirmationResult(IVoiceInteractorRequest request, boolean confirmed, Bundle result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(request != null ? request.asBinder() : null);
                    _data.writeInt(confirmed ? 1 : 0);
                    if (result != null) {
                        _data.writeInt(1);
                        result.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().deliverConfirmationResult(request, confirmed, result);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void deliverPickOptionResult(IVoiceInteractorRequest request, boolean finished, Option[] selections, Bundle result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(request != null ? request.asBinder() : null);
                    _data.writeInt(finished ? 1 : 0);
                    _data.writeTypedArray(selections, 0);
                    if (result != null) {
                        _data.writeInt(1);
                        result.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().deliverPickOptionResult(request, finished, selections, result);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void deliverCompleteVoiceResult(IVoiceInteractorRequest request, Bundle result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(request != null ? request.asBinder() : null);
                    if (result != null) {
                        _data.writeInt(1);
                        result.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().deliverCompleteVoiceResult(request, result);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void deliverAbortVoiceResult(IVoiceInteractorRequest request, Bundle result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(request != null ? request.asBinder() : null);
                    if (result != null) {
                        _data.writeInt(1);
                        result.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().deliverAbortVoiceResult(request, result);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void deliverCommandResult(IVoiceInteractorRequest request, boolean finished, Bundle result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(request != null ? request.asBinder() : null);
                    _data.writeInt(finished ? 1 : 0);
                    if (result != null) {
                        _data.writeInt(1);
                        result.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().deliverCommandResult(request, finished, result);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void deliverCancel(IVoiceInteractorRequest request) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(request != null ? request.asBinder() : null);
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().deliverCancel(request);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void destroy() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(7, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().destroy();
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IVoiceInteractorCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IVoiceInteractorCallback)) {
                return new Proxy(obj);
            }
            return (IVoiceInteractorCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "deliverConfirmationResult";
                case 2:
                    return "deliverPickOptionResult";
                case 3:
                    return "deliverCompleteVoiceResult";
                case 4:
                    return "deliverAbortVoiceResult";
                case 5:
                    return "deliverCommandResult";
                case 6:
                    return "deliverCancel";
                case 7:
                    return "destroy";
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
                boolean _arg1 = false;
                IVoiceInteractorRequest _arg0;
                Bundle _arg2;
                IVoiceInteractorRequest _arg02;
                Bundle _arg12;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        _arg0 = com.android.internal.app.IVoiceInteractorRequest.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        if (data.readInt() != 0) {
                            _arg2 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                        } else {
                            _arg2 = null;
                        }
                        deliverConfirmationResult(_arg0, _arg1, _arg2);
                        return true;
                    case 2:
                        Bundle _arg3;
                        data.enforceInterface(descriptor);
                        _arg0 = com.android.internal.app.IVoiceInteractorRequest.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        Option[] _arg22 = (Option[]) data.createTypedArray(Option.CREATOR);
                        if (data.readInt() != 0) {
                            _arg3 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                        } else {
                            _arg3 = null;
                        }
                        deliverPickOptionResult(_arg0, _arg1, _arg22, _arg3);
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        _arg02 = com.android.internal.app.IVoiceInteractorRequest.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg12 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                        } else {
                            _arg12 = null;
                        }
                        deliverCompleteVoiceResult(_arg02, _arg12);
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        _arg02 = com.android.internal.app.IVoiceInteractorRequest.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg12 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                        } else {
                            _arg12 = null;
                        }
                        deliverAbortVoiceResult(_arg02, _arg12);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        _arg0 = com.android.internal.app.IVoiceInteractorRequest.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        if (data.readInt() != 0) {
                            _arg2 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                        } else {
                            _arg2 = null;
                        }
                        deliverCommandResult(_arg0, _arg1, _arg2);
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        deliverCancel(com.android.internal.app.IVoiceInteractorRequest.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        destroy();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IVoiceInteractorCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IVoiceInteractorCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void deliverAbortVoiceResult(IVoiceInteractorRequest iVoiceInteractorRequest, Bundle bundle) throws RemoteException;

    void deliverCancel(IVoiceInteractorRequest iVoiceInteractorRequest) throws RemoteException;

    void deliverCommandResult(IVoiceInteractorRequest iVoiceInteractorRequest, boolean z, Bundle bundle) throws RemoteException;

    void deliverCompleteVoiceResult(IVoiceInteractorRequest iVoiceInteractorRequest, Bundle bundle) throws RemoteException;

    void deliverConfirmationResult(IVoiceInteractorRequest iVoiceInteractorRequest, boolean z, Bundle bundle) throws RemoteException;

    void deliverPickOptionResult(IVoiceInteractorRequest iVoiceInteractorRequest, boolean z, Option[] optionArr, Bundle bundle) throws RemoteException;

    void destroy() throws RemoteException;
}
