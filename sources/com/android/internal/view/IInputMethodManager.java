package com.android.internal.view;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodSubtype;
import java.util.List;

public interface IInputMethodManager extends IInterface {

    public static class Default implements IInputMethodManager {
        public void addClient(IInputMethodClient client, IInputContext inputContext, int untrustedDisplayId) throws RemoteException {
        }

        public List<InputMethodInfo> getInputMethodList(int userId) throws RemoteException {
            return null;
        }

        public List<InputMethodInfo> getEnabledInputMethodList(int userId) throws RemoteException {
            return null;
        }

        public List<InputMethodSubtype> getEnabledInputMethodSubtypeList(String imiId, boolean allowsImplicitlySelectedSubtypes) throws RemoteException {
            return null;
        }

        public InputMethodSubtype getLastInputMethodSubtype() throws RemoteException {
            return null;
        }

        public boolean showSoftInput(IInputMethodClient client, int flags, ResultReceiver resultReceiver) throws RemoteException {
            return false;
        }

        public boolean hideSoftInput(IInputMethodClient client, int flags, ResultReceiver resultReceiver) throws RemoteException {
            return false;
        }

        public InputBindResult startInputOrWindowGainedFocus(int startInputReason, IInputMethodClient client, IBinder windowToken, int startInputFlags, int softInputMode, int windowFlags, EditorInfo attribute, IInputContext inputContext, int missingMethodFlags, int unverifiedTargetSdkVersion) throws RemoteException {
            return null;
        }

        public void showInputMethodPickerFromClient(IInputMethodClient client, int auxiliarySubtypeMode) throws RemoteException {
        }

        public void showInputMethodPickerFromSystem(IInputMethodClient client, int auxiliarySubtypeMode, int displayId) throws RemoteException {
        }

        public void showInputMethodAndSubtypeEnablerFromClient(IInputMethodClient client, String topId) throws RemoteException {
        }

        public boolean isInputMethodPickerShownForTest() throws RemoteException {
            return false;
        }

        public InputMethodSubtype getCurrentInputMethodSubtype() throws RemoteException {
            return null;
        }

        public void setAdditionalInputMethodSubtypes(String id, InputMethodSubtype[] subtypes) throws RemoteException {
        }

        public int getInputMethodWindowVisibleHeight() throws RemoteException {
            return 0;
        }

        public void reportActivityView(IInputMethodClient parentClient, int childDisplayId, float[] matrixValues) throws RemoteException {
        }

        public boolean isTokenValid(IBinder token) throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IInputMethodManager {
        private static final String DESCRIPTOR = "com.android.internal.view.IInputMethodManager";
        static final int TRANSACTION_addClient = 1;
        static final int TRANSACTION_getCurrentInputMethodSubtype = 13;
        static final int TRANSACTION_getEnabledInputMethodList = 3;
        static final int TRANSACTION_getEnabledInputMethodSubtypeList = 4;
        static final int TRANSACTION_getInputMethodList = 2;
        static final int TRANSACTION_getInputMethodWindowVisibleHeight = 15;
        static final int TRANSACTION_getLastInputMethodSubtype = 5;
        static final int TRANSACTION_hideSoftInput = 7;
        static final int TRANSACTION_isInputMethodPickerShownForTest = 12;
        static final int TRANSACTION_isTokenValid = 17;
        static final int TRANSACTION_reportActivityView = 16;
        static final int TRANSACTION_setAdditionalInputMethodSubtypes = 14;
        static final int TRANSACTION_showInputMethodAndSubtypeEnablerFromClient = 11;
        static final int TRANSACTION_showInputMethodPickerFromClient = 9;
        static final int TRANSACTION_showInputMethodPickerFromSystem = 10;
        static final int TRANSACTION_showSoftInput = 6;
        static final int TRANSACTION_startInputOrWindowGainedFocus = 8;

        private static class Proxy implements IInputMethodManager {
            public static IInputMethodManager sDefaultImpl;
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

            public void addClient(IInputMethodClient client, IInputContext inputContext, int untrustedDisplayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IBinder iBinder = null;
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    if (inputContext != null) {
                        iBinder = inputContext.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    _data.writeInt(untrustedDisplayId);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addClient(client, inputContext, untrustedDisplayId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<InputMethodInfo> getInputMethodList(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    List<InputMethodInfo> list = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getInputMethodList(userId);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(InputMethodInfo.CREATOR);
                    List<InputMethodInfo> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<InputMethodInfo> getEnabledInputMethodList(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    List<InputMethodInfo> list = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getEnabledInputMethodList(userId);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(InputMethodInfo.CREATOR);
                    List<InputMethodInfo> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<InputMethodSubtype> getEnabledInputMethodSubtypeList(String imiId, boolean allowsImplicitlySelectedSubtypes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(imiId);
                    _data.writeInt(allowsImplicitlySelectedSubtypes ? 1 : 0);
                    List<InputMethodSubtype> list = this.mRemote;
                    if (!list.transact(4, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != null) {
                            list = Stub.getDefaultImpl().getEnabledInputMethodSubtypeList(imiId, allowsImplicitlySelectedSubtypes);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(InputMethodSubtype.CREATOR);
                    List<InputMethodSubtype> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public InputMethodSubtype getLastInputMethodSubtype() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    InputMethodSubtype inputMethodSubtype = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        inputMethodSubtype = Stub.getDefaultImpl();
                        if (inputMethodSubtype != 0) {
                            inputMethodSubtype = Stub.getDefaultImpl().getLastInputMethodSubtype();
                            return inputMethodSubtype;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        inputMethodSubtype = (InputMethodSubtype) InputMethodSubtype.CREATOR.createFromParcel(_reply);
                    } else {
                        inputMethodSubtype = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return inputMethodSubtype;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean showSoftInput(IInputMethodClient client, int flags, ResultReceiver resultReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    _data.writeInt(flags);
                    boolean _result = true;
                    if (resultReceiver != null) {
                        _data.writeInt(1);
                        resultReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().showSoftInput(client, flags, resultReceiver);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean hideSoftInput(IInputMethodClient client, int flags, ResultReceiver resultReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    _data.writeInt(flags);
                    boolean _result = true;
                    if (resultReceiver != null) {
                        _data.writeInt(1);
                        resultReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().hideSoftInput(client, flags, resultReceiver);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public InputBindResult startInputOrWindowGainedFocus(int startInputReason, IInputMethodClient client, IBinder windowToken, int startInputFlags, int softInputMode, int windowFlags, EditorInfo attribute, IInputContext inputContext, int missingMethodFlags, int unverifiedTargetSdkVersion) throws RemoteException {
                Throwable th;
                EditorInfo editorInfo = attribute;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(startInputReason);
                        IBinder iBinder = null;
                        _data.writeStrongBinder(client != null ? client.asBinder() : null);
                        _data.writeStrongBinder(windowToken);
                        _data.writeInt(startInputFlags);
                        _data.writeInt(softInputMode);
                        _data.writeInt(windowFlags);
                        if (editorInfo != null) {
                            _data.writeInt(1);
                            editorInfo.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (inputContext != null) {
                            iBinder = inputContext.asBinder();
                        }
                        _data.writeStrongBinder(iBinder);
                        _data.writeInt(missingMethodFlags);
                        _data.writeInt(unverifiedTargetSdkVersion);
                        InputBindResult _result;
                        if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            if (_reply.readInt() != 0) {
                                _result = (InputBindResult) InputBindResult.CREATOR.createFromParcel(_reply);
                            } else {
                                _result = null;
                            }
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        }
                        _result = Stub.getDefaultImpl().startInputOrWindowGainedFocus(startInputReason, client, windowToken, startInputFlags, softInputMode, windowFlags, attribute, inputContext, missingMethodFlags, unverifiedTargetSdkVersion);
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    int i = startInputReason;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void showInputMethodPickerFromClient(IInputMethodClient client, int auxiliarySubtypeMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    _data.writeInt(auxiliarySubtypeMode);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().showInputMethodPickerFromClient(client, auxiliarySubtypeMode);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void showInputMethodPickerFromSystem(IInputMethodClient client, int auxiliarySubtypeMode, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    _data.writeInt(auxiliarySubtypeMode);
                    _data.writeInt(displayId);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().showInputMethodPickerFromSystem(client, auxiliarySubtypeMode, displayId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void showInputMethodAndSubtypeEnablerFromClient(IInputMethodClient client, String topId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    _data.writeString(topId);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().showInputMethodAndSubtypeEnablerFromClient(client, topId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isInputMethodPickerShownForTest() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isInputMethodPickerShownForTest();
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

            public InputMethodSubtype getCurrentInputMethodSubtype() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    InputMethodSubtype inputMethodSubtype = 13;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        inputMethodSubtype = Stub.getDefaultImpl();
                        if (inputMethodSubtype != 0) {
                            inputMethodSubtype = Stub.getDefaultImpl().getCurrentInputMethodSubtype();
                            return inputMethodSubtype;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        inputMethodSubtype = (InputMethodSubtype) InputMethodSubtype.CREATOR.createFromParcel(_reply);
                    } else {
                        inputMethodSubtype = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return inputMethodSubtype;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAdditionalInputMethodSubtypes(String id, InputMethodSubtype[] subtypes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(id);
                    _data.writeTypedArray(subtypes, 0);
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAdditionalInputMethodSubtypes(id, subtypes);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getInputMethodWindowVisibleHeight() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 15;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getInputMethodWindowVisibleHeight();
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

            public void reportActivityView(IInputMethodClient parentClient, int childDisplayId, float[] matrixValues) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(parentClient != null ? parentClient.asBinder() : null);
                    _data.writeInt(childDisplayId);
                    _data.writeFloatArray(matrixValues);
                    if (this.mRemote.transact(16, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reportActivityView(parentClient, childDisplayId, matrixValues);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isTokenValid(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(17, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isTokenValid(token);
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

        public static IInputMethodManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IInputMethodManager)) {
                return new Proxy(obj);
            }
            return (IInputMethodManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "addClient";
                case 2:
                    return "getInputMethodList";
                case 3:
                    return "getEnabledInputMethodList";
                case 4:
                    return "getEnabledInputMethodSubtypeList";
                case 5:
                    return "getLastInputMethodSubtype";
                case 6:
                    return "showSoftInput";
                case 7:
                    return "hideSoftInput";
                case 8:
                    return "startInputOrWindowGainedFocus";
                case 9:
                    return "showInputMethodPickerFromClient";
                case 10:
                    return "showInputMethodPickerFromSystem";
                case 11:
                    return "showInputMethodAndSubtypeEnablerFromClient";
                case 12:
                    return "isInputMethodPickerShownForTest";
                case 13:
                    return "getCurrentInputMethodSubtype";
                case 14:
                    return "setAdditionalInputMethodSubtypes";
                case 15:
                    return "getInputMethodWindowVisibleHeight";
                case 16:
                    return "reportActivityView";
                case 17:
                    return "isTokenValid";
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
            boolean z;
            if (i != 1598968902) {
                List<InputMethodInfo> _result;
                boolean _arg1;
                InputMethodSubtype _result2;
                IInputMethodClient _arg0;
                int _arg12;
                ResultReceiver _arg2;
                boolean _result3;
                switch (i) {
                    case 1:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        addClient(com.android.internal.view.IInputMethodClient.Stub.asInterface(data.readStrongBinder()), com.android.internal.view.IInputContext.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        return z;
                    case 2:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result = getInputMethodList(data.readInt());
                        reply.writeNoException();
                        parcel2.writeTypedList(_result);
                        return z;
                    case 3:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _result = getEnabledInputMethodList(data.readInt());
                        reply.writeNoException();
                        parcel2.writeTypedList(_result);
                        return z;
                    case 4:
                        _arg1 = false;
                        z = true;
                        parcel.enforceInterface(descriptor);
                        String _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = z;
                        }
                        List<InputMethodSubtype> _result4 = getEnabledInputMethodSubtypeList(_arg02, _arg1);
                        reply.writeNoException();
                        parcel2.writeTypedList(_result4);
                        return z;
                    case 5:
                        i = 1;
                        parcel.enforceInterface(descriptor);
                        _result2 = getLastInputMethodSubtype();
                        reply.writeNoException();
                        if (_result2 != null) {
                            parcel2.writeInt(i);
                            _result2.writeToParcel(parcel2, i);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return i;
                    case 6:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg0 = com.android.internal.view.IInputMethodClient.Stub.asInterface(data.readStrongBinder());
                        _arg12 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = (ResultReceiver) ResultReceiver.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        _result3 = showSoftInput(_arg0, _arg12, _arg2);
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return z;
                    case 7:
                        z = true;
                        parcel.enforceInterface(descriptor);
                        _arg0 = com.android.internal.view.IInputMethodClient.Stub.asInterface(data.readStrongBinder());
                        _arg12 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = (ResultReceiver) ResultReceiver.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        _result3 = hideSoftInput(_arg0, _arg12, _arg2);
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return z;
                    case 8:
                        EditorInfo _arg6;
                        parcel.enforceInterface(descriptor);
                        int _arg03 = data.readInt();
                        IInputMethodClient _arg13 = com.android.internal.view.IInputMethodClient.Stub.asInterface(data.readStrongBinder());
                        IBinder _arg22 = data.readStrongBinder();
                        int _arg3 = data.readInt();
                        int _arg4 = data.readInt();
                        int _arg5 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg6 = (EditorInfo) EditorInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg6 = null;
                        }
                        i = 0;
                        i = 1;
                        InputBindResult _result5 = startInputOrWindowGainedFocus(_arg03, _arg13, _arg22, _arg3, _arg4, _arg5, _arg6, com.android.internal.view.IInputContext.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result5 != null) {
                            parcel2.writeInt(i);
                            _result5.writeToParcel(parcel2, i);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return i;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        showInputMethodPickerFromClient(com.android.internal.view.IInputMethodClient.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        showInputMethodPickerFromSystem(com.android.internal.view.IInputMethodClient.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        showInputMethodAndSubtypeEnablerFromClient(com.android.internal.view.IInputMethodClient.Stub.asInterface(data.readStrongBinder()), data.readString());
                        reply.writeNoException();
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        boolean _result6 = isInputMethodPickerShownForTest();
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        _result2 = getCurrentInputMethodSubtype();
                        reply.writeNoException();
                        if (_result2 != null) {
                            parcel2.writeInt(1);
                            _result2.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        setAdditionalInputMethodSubtypes(data.readString(), (InputMethodSubtype[]) parcel.createTypedArray(InputMethodSubtype.CREATOR));
                        reply.writeNoException();
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        int _result7 = getInputMethodWindowVisibleHeight();
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        reportActivityView(com.android.internal.view.IInputMethodClient.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.createFloatArray());
                        reply.writeNoException();
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        _arg1 = isTokenValid(data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            z = true;
            parcel2.writeString(descriptor);
            return z;
        }

        public static boolean setDefaultImpl(IInputMethodManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IInputMethodManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void addClient(IInputMethodClient iInputMethodClient, IInputContext iInputContext, int i) throws RemoteException;

    InputMethodSubtype getCurrentInputMethodSubtype() throws RemoteException;

    List<InputMethodInfo> getEnabledInputMethodList(int i) throws RemoteException;

    List<InputMethodSubtype> getEnabledInputMethodSubtypeList(String str, boolean z) throws RemoteException;

    List<InputMethodInfo> getInputMethodList(int i) throws RemoteException;

    int getInputMethodWindowVisibleHeight() throws RemoteException;

    InputMethodSubtype getLastInputMethodSubtype() throws RemoteException;

    boolean hideSoftInput(IInputMethodClient iInputMethodClient, int i, ResultReceiver resultReceiver) throws RemoteException;

    boolean isInputMethodPickerShownForTest() throws RemoteException;

    boolean isTokenValid(IBinder iBinder) throws RemoteException;

    void reportActivityView(IInputMethodClient iInputMethodClient, int i, float[] fArr) throws RemoteException;

    void setAdditionalInputMethodSubtypes(String str, InputMethodSubtype[] inputMethodSubtypeArr) throws RemoteException;

    void showInputMethodAndSubtypeEnablerFromClient(IInputMethodClient iInputMethodClient, String str) throws RemoteException;

    void showInputMethodPickerFromClient(IInputMethodClient iInputMethodClient, int i) throws RemoteException;

    void showInputMethodPickerFromSystem(IInputMethodClient iInputMethodClient, int i, int i2) throws RemoteException;

    boolean showSoftInput(IInputMethodClient iInputMethodClient, int i, ResultReceiver resultReceiver) throws RemoteException;

    InputBindResult startInputOrWindowGainedFocus(int i, IInputMethodClient iInputMethodClient, IBinder iBinder, int i2, int i3, int i4, EditorInfo editorInfo, IInputContext iInputContext, int i5, int i6) throws RemoteException;
}
