package com.android.internal.app;

import android.app.VoiceInteractor.PickOptionRequest.Option;
import android.app.VoiceInteractor.Prompt;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ICancellationSignal;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IVoiceInteractor extends IInterface {

    public static class Default implements IVoiceInteractor {
        public IVoiceInteractorRequest startConfirmation(String callingPackage, IVoiceInteractorCallback callback, Prompt prompt, Bundle extras) throws RemoteException {
            return null;
        }

        public IVoiceInteractorRequest startPickOption(String callingPackage, IVoiceInteractorCallback callback, Prompt prompt, Option[] options, Bundle extras) throws RemoteException {
            return null;
        }

        public IVoiceInteractorRequest startCompleteVoice(String callingPackage, IVoiceInteractorCallback callback, Prompt prompt, Bundle extras) throws RemoteException {
            return null;
        }

        public IVoiceInteractorRequest startAbortVoice(String callingPackage, IVoiceInteractorCallback callback, Prompt prompt, Bundle extras) throws RemoteException {
            return null;
        }

        public IVoiceInteractorRequest startCommand(String callingPackage, IVoiceInteractorCallback callback, String command, Bundle extras) throws RemoteException {
            return null;
        }

        public boolean[] supportsCommands(String callingPackage, String[] commands) throws RemoteException {
            return null;
        }

        public void notifyDirectActionsChanged(int taskId, IBinder assistToken) throws RemoteException {
        }

        public void setKillCallback(ICancellationSignal callback) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IVoiceInteractor {
        private static final String DESCRIPTOR = "com.android.internal.app.IVoiceInteractor";
        static final int TRANSACTION_notifyDirectActionsChanged = 7;
        static final int TRANSACTION_setKillCallback = 8;
        static final int TRANSACTION_startAbortVoice = 4;
        static final int TRANSACTION_startCommand = 5;
        static final int TRANSACTION_startCompleteVoice = 3;
        static final int TRANSACTION_startConfirmation = 1;
        static final int TRANSACTION_startPickOption = 2;
        static final int TRANSACTION_supportsCommands = 6;

        private static class Proxy implements IVoiceInteractor {
            public static IVoiceInteractor sDefaultImpl;
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

            public IVoiceInteractorRequest startConfirmation(String callingPackage, IVoiceInteractorCallback callback, Prompt prompt, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    IVoiceInteractorRequest iVoiceInteractorRequest = 0;
                    if (prompt != null) {
                        _data.writeInt(1);
                        prompt.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        iVoiceInteractorRequest = Stub.getDefaultImpl();
                        if (iVoiceInteractorRequest != 0) {
                            iVoiceInteractorRequest = Stub.getDefaultImpl().startConfirmation(callingPackage, callback, prompt, extras);
                            return iVoiceInteractorRequest;
                        }
                    }
                    _reply.readException();
                    iVoiceInteractorRequest = com.android.internal.app.IVoiceInteractorRequest.Stub.asInterface(_reply.readStrongBinder());
                    IVoiceInteractorRequest _result = iVoiceInteractorRequest;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IVoiceInteractorRequest startPickOption(String callingPackage, IVoiceInteractorCallback callback, Prompt prompt, Option[] options, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    IVoiceInteractorRequest iVoiceInteractorRequest = 0;
                    if (prompt != null) {
                        _data.writeInt(1);
                        prompt.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeTypedArray(options, 0);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        iVoiceInteractorRequest = Stub.getDefaultImpl();
                        if (iVoiceInteractorRequest != 0) {
                            iVoiceInteractorRequest = Stub.getDefaultImpl().startPickOption(callingPackage, callback, prompt, options, extras);
                            return iVoiceInteractorRequest;
                        }
                    }
                    _reply.readException();
                    iVoiceInteractorRequest = com.android.internal.app.IVoiceInteractorRequest.Stub.asInterface(_reply.readStrongBinder());
                    IVoiceInteractorRequest _result = iVoiceInteractorRequest;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IVoiceInteractorRequest startCompleteVoice(String callingPackage, IVoiceInteractorCallback callback, Prompt prompt, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    IVoiceInteractorRequest iVoiceInteractorRequest = 0;
                    if (prompt != null) {
                        _data.writeInt(1);
                        prompt.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        iVoiceInteractorRequest = Stub.getDefaultImpl();
                        if (iVoiceInteractorRequest != 0) {
                            iVoiceInteractorRequest = Stub.getDefaultImpl().startCompleteVoice(callingPackage, callback, prompt, extras);
                            return iVoiceInteractorRequest;
                        }
                    }
                    _reply.readException();
                    iVoiceInteractorRequest = com.android.internal.app.IVoiceInteractorRequest.Stub.asInterface(_reply.readStrongBinder());
                    IVoiceInteractorRequest _result = iVoiceInteractorRequest;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IVoiceInteractorRequest startAbortVoice(String callingPackage, IVoiceInteractorCallback callback, Prompt prompt, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    IVoiceInteractorRequest iVoiceInteractorRequest = 0;
                    if (prompt != null) {
                        _data.writeInt(1);
                        prompt.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        iVoiceInteractorRequest = Stub.getDefaultImpl();
                        if (iVoiceInteractorRequest != 0) {
                            iVoiceInteractorRequest = Stub.getDefaultImpl().startAbortVoice(callingPackage, callback, prompt, extras);
                            return iVoiceInteractorRequest;
                        }
                    }
                    _reply.readException();
                    iVoiceInteractorRequest = com.android.internal.app.IVoiceInteractorRequest.Stub.asInterface(_reply.readStrongBinder());
                    IVoiceInteractorRequest _result = iVoiceInteractorRequest;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IVoiceInteractorRequest startCommand(String callingPackage, IVoiceInteractorCallback callback, String command, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeString(command);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    IVoiceInteractorRequest iVoiceInteractorRequest = this.mRemote;
                    if (!iVoiceInteractorRequest.transact(5, _data, _reply, 0)) {
                        iVoiceInteractorRequest = Stub.getDefaultImpl();
                        if (iVoiceInteractorRequest != null) {
                            iVoiceInteractorRequest = Stub.getDefaultImpl().startCommand(callingPackage, callback, command, extras);
                            return iVoiceInteractorRequest;
                        }
                    }
                    _reply.readException();
                    iVoiceInteractorRequest = com.android.internal.app.IVoiceInteractorRequest.Stub.asInterface(_reply.readStrongBinder());
                    IVoiceInteractorRequest _result = iVoiceInteractorRequest;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean[] supportsCommands(String callingPackage, String[] commands) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeStringArray(commands);
                    boolean[] zArr = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        zArr = Stub.getDefaultImpl();
                        if (zArr != 0) {
                            zArr = Stub.getDefaultImpl().supportsCommands(callingPackage, commands);
                            return zArr;
                        }
                    }
                    _reply.readException();
                    zArr = _reply.createBooleanArray();
                    boolean[] _result = zArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyDirectActionsChanged(int taskId, IBinder assistToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeStrongBinder(assistToken);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyDirectActionsChanged(taskId, assistToken);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setKillCallback(ICancellationSignal callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setKillCallback(callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IVoiceInteractor asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IVoiceInteractor)) {
                return new Proxy(obj);
            }
            return (IVoiceInteractor) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "startConfirmation";
                case 2:
                    return "startPickOption";
                case 3:
                    return "startCompleteVoice";
                case 4:
                    return "startAbortVoice";
                case 5:
                    return "startCommand";
                case 6:
                    return "supportsCommands";
                case 7:
                    return "notifyDirectActionsChanged";
                case 8:
                    return "setKillCallback";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            Parcel parcel2 = reply;
            String descriptor = DESCRIPTOR;
            if (i != 1598968902) {
                IBinder iBinder = null;
                String _arg0;
                IVoiceInteractorCallback _arg1;
                Prompt _arg2;
                Bundle _arg3;
                IVoiceInteractorRequest _result;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg1 = com.android.internal.app.IVoiceInteractorCallback.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg2 = (Prompt) Prompt.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg3 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        _result = startConfirmation(_arg0, _arg1, _arg2, _arg3);
                        reply.writeNoException();
                        if (_result != null) {
                            iBinder = _result.asBinder();
                        }
                        parcel2.writeStrongBinder(iBinder);
                        return true;
                    case 2:
                        Prompt _arg22;
                        Bundle _arg4;
                        parcel.enforceInterface(descriptor);
                        String _arg02 = data.readString();
                        IVoiceInteractorCallback _arg12 = com.android.internal.app.IVoiceInteractorCallback.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg22 = (Prompt) Prompt.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg22 = null;
                        }
                        Option[] _arg32 = (Option[]) parcel.createTypedArray(Option.CREATOR);
                        if (data.readInt() != 0) {
                            _arg4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg4 = null;
                        }
                        IVoiceInteractorRequest _result2 = startPickOption(_arg02, _arg12, _arg22, _arg32, _arg4);
                        reply.writeNoException();
                        if (_result2 != null) {
                            iBinder = _result2.asBinder();
                        }
                        parcel2.writeStrongBinder(iBinder);
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg1 = com.android.internal.app.IVoiceInteractorCallback.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg2 = (Prompt) Prompt.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg3 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        _result = startCompleteVoice(_arg0, _arg1, _arg2, _arg3);
                        reply.writeNoException();
                        if (_result != null) {
                            iBinder = _result.asBinder();
                        }
                        parcel2.writeStrongBinder(iBinder);
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg1 = com.android.internal.app.IVoiceInteractorCallback.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg2 = (Prompt) Prompt.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg3 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        _result = startAbortVoice(_arg0, _arg1, _arg2, _arg3);
                        reply.writeNoException();
                        if (_result != null) {
                            iBinder = _result.asBinder();
                        }
                        parcel2.writeStrongBinder(iBinder);
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg1 = com.android.internal.app.IVoiceInteractorCallback.Stub.asInterface(data.readStrongBinder());
                        String _arg23 = data.readString();
                        if (data.readInt() != 0) {
                            _arg3 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        _result = startCommand(_arg0, _arg1, _arg23, _arg3);
                        reply.writeNoException();
                        if (_result != null) {
                            iBinder = _result.asBinder();
                        }
                        parcel2.writeStrongBinder(iBinder);
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        boolean[] _result3 = supportsCommands(data.readString(), data.createStringArray());
                        reply.writeNoException();
                        parcel2.writeBooleanArray(_result3);
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        notifyDirectActionsChanged(data.readInt(), data.readStrongBinder());
                        reply.writeNoException();
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        setKillCallback(android.os.ICancellationSignal.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IVoiceInteractor impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IVoiceInteractor getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void notifyDirectActionsChanged(int i, IBinder iBinder) throws RemoteException;

    void setKillCallback(ICancellationSignal iCancellationSignal) throws RemoteException;

    IVoiceInteractorRequest startAbortVoice(String str, IVoiceInteractorCallback iVoiceInteractorCallback, Prompt prompt, Bundle bundle) throws RemoteException;

    IVoiceInteractorRequest startCommand(String str, IVoiceInteractorCallback iVoiceInteractorCallback, String str2, Bundle bundle) throws RemoteException;

    IVoiceInteractorRequest startCompleteVoice(String str, IVoiceInteractorCallback iVoiceInteractorCallback, Prompt prompt, Bundle bundle) throws RemoteException;

    IVoiceInteractorRequest startConfirmation(String str, IVoiceInteractorCallback iVoiceInteractorCallback, Prompt prompt, Bundle bundle) throws RemoteException;

    IVoiceInteractorRequest startPickOption(String str, IVoiceInteractorCallback iVoiceInteractorCallback, Prompt prompt, Option[] optionArr, Bundle bundle) throws RemoteException;

    boolean[] supportsCommands(String str, String[] strArr) throws RemoteException;
}
