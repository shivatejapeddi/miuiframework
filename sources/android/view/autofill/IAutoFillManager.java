package android.view.autofill;

import android.content.ComponentName;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.service.autofill.UserData;
import com.android.internal.os.IResultReceiver;
import java.util.List;

public interface IAutoFillManager extends IInterface {

    public static class Default implements IAutoFillManager {
        public void addClient(IAutoFillManagerClient client, ComponentName componentName, int userId, IResultReceiver result) throws RemoteException {
        }

        public void removeClient(IAutoFillManagerClient client, int userId) throws RemoteException {
        }

        public void startSession(IBinder activityToken, IBinder appCallback, AutofillId autoFillId, Rect bounds, AutofillValue value, int userId, boolean hasCallback, int flags, ComponentName componentName, boolean compatMode, IResultReceiver result) throws RemoteException {
        }

        public void getFillEventHistory(IResultReceiver result) throws RemoteException {
        }

        public void restoreSession(int sessionId, IBinder activityToken, IBinder appCallback, IResultReceiver result) throws RemoteException {
        }

        public void updateSession(int sessionId, AutofillId id, Rect bounds, AutofillValue value, int action, int flags, int userId) throws RemoteException {
        }

        public void setAutofillFailure(int sessionId, List<AutofillId> list, int userId) throws RemoteException {
        }

        public void finishSession(int sessionId, int userId) throws RemoteException {
        }

        public void cancelSession(int sessionId, int userId) throws RemoteException {
        }

        public void setAuthenticationResult(Bundle data, int sessionId, int authenticationId, int userId) throws RemoteException {
        }

        public void setHasCallback(int sessionId, int userId, boolean hasIt) throws RemoteException {
        }

        public void disableOwnedAutofillServices(int userId) throws RemoteException {
        }

        public void isServiceSupported(int userId, IResultReceiver result) throws RemoteException {
        }

        public void isServiceEnabled(int userId, String packageName, IResultReceiver result) throws RemoteException {
        }

        public void onPendingSaveUi(int operation, IBinder token) throws RemoteException {
        }

        public void getUserData(IResultReceiver result) throws RemoteException {
        }

        public void getUserDataId(IResultReceiver result) throws RemoteException {
        }

        public void setUserData(UserData userData) throws RemoteException {
        }

        public void isFieldClassificationEnabled(IResultReceiver result) throws RemoteException {
        }

        public void getAutofillServiceComponentName(IResultReceiver result) throws RemoteException {
        }

        public void getAvailableFieldClassificationAlgorithms(IResultReceiver result) throws RemoteException {
        }

        public void getDefaultFieldClassificationAlgorithm(IResultReceiver result) throws RemoteException {
        }

        public void setAugmentedAutofillWhitelist(List<String> list, List<ComponentName> list2, IResultReceiver result) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IAutoFillManager {
        private static final String DESCRIPTOR = "android.view.autofill.IAutoFillManager";
        static final int TRANSACTION_addClient = 1;
        static final int TRANSACTION_cancelSession = 9;
        static final int TRANSACTION_disableOwnedAutofillServices = 12;
        static final int TRANSACTION_finishSession = 8;
        static final int TRANSACTION_getAutofillServiceComponentName = 20;
        static final int TRANSACTION_getAvailableFieldClassificationAlgorithms = 21;
        static final int TRANSACTION_getDefaultFieldClassificationAlgorithm = 22;
        static final int TRANSACTION_getFillEventHistory = 4;
        static final int TRANSACTION_getUserData = 16;
        static final int TRANSACTION_getUserDataId = 17;
        static final int TRANSACTION_isFieldClassificationEnabled = 19;
        static final int TRANSACTION_isServiceEnabled = 14;
        static final int TRANSACTION_isServiceSupported = 13;
        static final int TRANSACTION_onPendingSaveUi = 15;
        static final int TRANSACTION_removeClient = 2;
        static final int TRANSACTION_restoreSession = 5;
        static final int TRANSACTION_setAugmentedAutofillWhitelist = 23;
        static final int TRANSACTION_setAuthenticationResult = 10;
        static final int TRANSACTION_setAutofillFailure = 7;
        static final int TRANSACTION_setHasCallback = 11;
        static final int TRANSACTION_setUserData = 18;
        static final int TRANSACTION_startSession = 3;
        static final int TRANSACTION_updateSession = 6;

        private static class Proxy implements IAutoFillManager {
            public static IAutoFillManager sDefaultImpl;
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

            public void addClient(IAutoFillManagerClient client, ComponentName componentName, int userId, IResultReceiver result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    if (componentName != null) {
                        _data.writeInt(1);
                        componentName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    _data.writeStrongBinder(result != null ? result.asBinder() : null);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().addClient(client, componentName, userId, result);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void removeClient(IAutoFillManagerClient client, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().removeClient(client, userId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void startSession(IBinder activityToken, IBinder appCallback, AutofillId autoFillId, Rect bounds, AutofillValue value, int userId, boolean hasCallback, int flags, ComponentName componentName, boolean compatMode, IResultReceiver result) throws RemoteException {
                Throwable th;
                AutofillId autofillId = autoFillId;
                Rect rect = bounds;
                AutofillValue autofillValue = value;
                ComponentName componentName2 = componentName;
                Parcel _data = Parcel.obtain();
                Parcel _data2;
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    _data.writeStrongBinder(appCallback);
                    int i = 0;
                    if (autofillId != null) {
                        try {
                            _data.writeInt(1);
                            autofillId.writeToParcel(_data, 0);
                        } catch (Throwable th2) {
                            th = th2;
                            _data2 = _data;
                        }
                    } else {
                        _data.writeInt(0);
                    }
                    if (rect != null) {
                        _data.writeInt(1);
                        rect.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (autofillValue != null) {
                        _data.writeInt(1);
                        autofillValue.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    _data.writeInt(hasCallback ? 1 : 0);
                    _data.writeInt(flags);
                    if (componentName2 != null) {
                        _data.writeInt(1);
                        componentName2.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (compatMode) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeStrongBinder(result != null ? result.asBinder() : null);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                        return;
                    }
                    _data2 = _data;
                    try {
                        Stub.getDefaultImpl().startSession(activityToken, appCallback, autoFillId, bounds, value, userId, hasCallback, flags, componentName, compatMode, result);
                        _data2.recycle();
                    } catch (Throwable th3) {
                        th = th3;
                        _data2.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    _data2 = _data;
                    _data2.recycle();
                    throw th;
                }
            }

            public void getFillEventHistory(IResultReceiver result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(result != null ? result.asBinder() : null);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().getFillEventHistory(result);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void restoreSession(int sessionId, IBinder activityToken, IBinder appCallback, IResultReceiver result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    _data.writeStrongBinder(activityToken);
                    _data.writeStrongBinder(appCallback);
                    _data.writeStrongBinder(result != null ? result.asBinder() : null);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().restoreSession(sessionId, activityToken, appCallback, result);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void updateSession(int sessionId, AutofillId id, Rect bounds, AutofillValue value, int action, int flags, int userId) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                AutofillId autofillId = id;
                Rect rect = bounds;
                AutofillValue autofillValue = value;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(sessionId);
                        if (autofillId != null) {
                            _data.writeInt(1);
                            autofillId.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (rect != null) {
                            _data.writeInt(1);
                            rect.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (autofillValue != null) {
                            _data.writeInt(1);
                            autofillValue.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            _data.writeInt(action);
                        } catch (Throwable th2) {
                            th = th2;
                            i = flags;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        i2 = action;
                        i = flags;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(flags);
                        _data.writeInt(userId);
                        if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().updateSession(sessionId, id, bounds, value, action, flags, userId);
                        _data.recycle();
                    } catch (Throwable th4) {
                        th = th4;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    int i3 = sessionId;
                    i2 = action;
                    i = flags;
                    _data.recycle();
                    throw th;
                }
            }

            public void setAutofillFailure(int sessionId, List<AutofillId> ids, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    _data.writeTypedList(ids);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(7, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setAutofillFailure(sessionId, ids, userId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void finishSession(int sessionId, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(8, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().finishSession(sessionId, userId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void cancelSession(int sessionId, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(9, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().cancelSession(sessionId, userId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setAuthenticationResult(Bundle data, int sessionId, int authenticationId, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (data != null) {
                        _data.writeInt(1);
                        data.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(sessionId);
                    _data.writeInt(authenticationId);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(10, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setAuthenticationResult(data, sessionId, authenticationId, userId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setHasCallback(int sessionId, int userId, boolean hasIt) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    _data.writeInt(userId);
                    _data.writeInt(hasIt ? 1 : 0);
                    if (this.mRemote.transact(11, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setHasCallback(sessionId, userId, hasIt);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void disableOwnedAutofillServices(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(12, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().disableOwnedAutofillServices(userId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void isServiceSupported(int userId, IResultReceiver result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeStrongBinder(result != null ? result.asBinder() : null);
                    if (this.mRemote.transact(13, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().isServiceSupported(userId, result);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void isServiceEnabled(int userId, String packageName, IResultReceiver result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(result != null ? result.asBinder() : null);
                    if (this.mRemote.transact(14, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().isServiceEnabled(userId, packageName, result);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onPendingSaveUi(int operation, IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(operation);
                    _data.writeStrongBinder(token);
                    if (this.mRemote.transact(15, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onPendingSaveUi(operation, token);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void getUserData(IResultReceiver result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(result != null ? result.asBinder() : null);
                    if (this.mRemote.transact(16, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().getUserData(result);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void getUserDataId(IResultReceiver result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(result != null ? result.asBinder() : null);
                    if (this.mRemote.transact(17, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().getUserDataId(result);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setUserData(UserData userData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (userData != null) {
                        _data.writeInt(1);
                        userData.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(18, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setUserData(userData);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void isFieldClassificationEnabled(IResultReceiver result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(result != null ? result.asBinder() : null);
                    if (this.mRemote.transact(19, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().isFieldClassificationEnabled(result);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void getAutofillServiceComponentName(IResultReceiver result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(result != null ? result.asBinder() : null);
                    if (this.mRemote.transact(20, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().getAutofillServiceComponentName(result);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void getAvailableFieldClassificationAlgorithms(IResultReceiver result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(result != null ? result.asBinder() : null);
                    if (this.mRemote.transact(21, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().getAvailableFieldClassificationAlgorithms(result);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void getDefaultFieldClassificationAlgorithm(IResultReceiver result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(result != null ? result.asBinder() : null);
                    if (this.mRemote.transact(22, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().getDefaultFieldClassificationAlgorithm(result);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setAugmentedAutofillWhitelist(List<String> packages, List<ComponentName> activities, IResultReceiver result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(packages);
                    _data.writeTypedList(activities);
                    _data.writeStrongBinder(result != null ? result.asBinder() : null);
                    if (this.mRemote.transact(23, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setAugmentedAutofillWhitelist(packages, activities, result);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAutoFillManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IAutoFillManager)) {
                return new Proxy(obj);
            }
            return (IAutoFillManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "addClient";
                case 2:
                    return "removeClient";
                case 3:
                    return "startSession";
                case 4:
                    return "getFillEventHistory";
                case 5:
                    return "restoreSession";
                case 6:
                    return "updateSession";
                case 7:
                    return "setAutofillFailure";
                case 8:
                    return "finishSession";
                case 9:
                    return "cancelSession";
                case 10:
                    return "setAuthenticationResult";
                case 11:
                    return "setHasCallback";
                case 12:
                    return "disableOwnedAutofillServices";
                case 13:
                    return "isServiceSupported";
                case 14:
                    return "isServiceEnabled";
                case 15:
                    return "onPendingSaveUi";
                case 16:
                    return "getUserData";
                case 17:
                    return "getUserDataId";
                case 18:
                    return "setUserData";
                case 19:
                    return "isFieldClassificationEnabled";
                case 20:
                    return "getAutofillServiceComponentName";
                case 21:
                    return "getAvailableFieldClassificationAlgorithms";
                case 22:
                    return "getDefaultFieldClassificationAlgorithm";
                case 23:
                    return "setAugmentedAutofillWhitelist";
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
            String descriptor = DESCRIPTOR;
            if (i != 1598968902) {
                boolean _arg2 = false;
                switch (i) {
                    case 1:
                        ComponentName _arg1;
                        parcel.enforceInterface(descriptor);
                        IAutoFillManagerClient _arg0 = android.view.autofill.IAutoFillManagerClient.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg1 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg1 = null;
                        }
                        addClient(_arg0, _arg1, data.readInt(), com.android.internal.os.IResultReceiver.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        removeClient(android.view.autofill.IAutoFillManagerClient.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        return true;
                    case 3:
                        AutofillId _arg22;
                        Rect _arg3;
                        AutofillValue _arg4;
                        ComponentName _arg8;
                        parcel.enforceInterface(descriptor);
                        IBinder _arg02 = data.readStrongBinder();
                        IBinder _arg12 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg22 = (AutofillId) AutofillId.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg22 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg3 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg4 = (AutofillValue) AutofillValue.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg4 = null;
                        }
                        int _arg5 = data.readInt();
                        boolean _arg6 = data.readInt() != 0;
                        int _arg7 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg8 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg8 = null;
                        }
                        startSession(_arg02, _arg12, _arg22, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8, data.readInt() != 0, com.android.internal.os.IResultReceiver.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        getFillEventHistory(com.android.internal.os.IResultReceiver.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        restoreSession(data.readInt(), data.readStrongBinder(), data.readStrongBinder(), com.android.internal.os.IResultReceiver.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 6:
                        AutofillId _arg13;
                        Rect _arg23;
                        AutofillValue _arg32;
                        parcel.enforceInterface(descriptor);
                        int _arg03 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg13 = (AutofillId) AutofillId.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg23 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg23 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg32 = (AutofillValue) AutofillValue.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg32 = null;
                        }
                        updateSession(_arg03, _arg13, _arg23, _arg32, data.readInt(), data.readInt(), data.readInt());
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        setAutofillFailure(data.readInt(), parcel.createTypedArrayList(AutofillId.CREATOR), data.readInt());
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        finishSession(data.readInt(), data.readInt());
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        cancelSession(data.readInt(), data.readInt());
                        return true;
                    case 10:
                        Bundle _arg04;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        setAuthenticationResult(_arg04, data.readInt(), data.readInt(), data.readInt());
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        int _arg05 = data.readInt();
                        int _arg14 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        setHasCallback(_arg05, _arg14, _arg2);
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        disableOwnedAutofillServices(data.readInt());
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        isServiceSupported(data.readInt(), com.android.internal.os.IResultReceiver.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        isServiceEnabled(data.readInt(), data.readString(), com.android.internal.os.IResultReceiver.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        onPendingSaveUi(data.readInt(), data.readStrongBinder());
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        getUserData(com.android.internal.os.IResultReceiver.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        getUserDataId(com.android.internal.os.IResultReceiver.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 18:
                        UserData _arg06;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg06 = (UserData) UserData.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg06 = null;
                        }
                        setUserData(_arg06);
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        isFieldClassificationEnabled(com.android.internal.os.IResultReceiver.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        getAutofillServiceComponentName(com.android.internal.os.IResultReceiver.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        getAvailableFieldClassificationAlgorithms(com.android.internal.os.IResultReceiver.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        getDefaultFieldClassificationAlgorithm(com.android.internal.os.IResultReceiver.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        setAugmentedAutofillWhitelist(data.createStringArrayList(), parcel.createTypedArrayList(ComponentName.CREATOR), com.android.internal.os.IResultReceiver.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IAutoFillManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IAutoFillManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void addClient(IAutoFillManagerClient iAutoFillManagerClient, ComponentName componentName, int i, IResultReceiver iResultReceiver) throws RemoteException;

    void cancelSession(int i, int i2) throws RemoteException;

    void disableOwnedAutofillServices(int i) throws RemoteException;

    void finishSession(int i, int i2) throws RemoteException;

    void getAutofillServiceComponentName(IResultReceiver iResultReceiver) throws RemoteException;

    void getAvailableFieldClassificationAlgorithms(IResultReceiver iResultReceiver) throws RemoteException;

    void getDefaultFieldClassificationAlgorithm(IResultReceiver iResultReceiver) throws RemoteException;

    void getFillEventHistory(IResultReceiver iResultReceiver) throws RemoteException;

    void getUserData(IResultReceiver iResultReceiver) throws RemoteException;

    void getUserDataId(IResultReceiver iResultReceiver) throws RemoteException;

    void isFieldClassificationEnabled(IResultReceiver iResultReceiver) throws RemoteException;

    void isServiceEnabled(int i, String str, IResultReceiver iResultReceiver) throws RemoteException;

    void isServiceSupported(int i, IResultReceiver iResultReceiver) throws RemoteException;

    void onPendingSaveUi(int i, IBinder iBinder) throws RemoteException;

    void removeClient(IAutoFillManagerClient iAutoFillManagerClient, int i) throws RemoteException;

    void restoreSession(int i, IBinder iBinder, IBinder iBinder2, IResultReceiver iResultReceiver) throws RemoteException;

    void setAugmentedAutofillWhitelist(List<String> list, List<ComponentName> list2, IResultReceiver iResultReceiver) throws RemoteException;

    void setAuthenticationResult(Bundle bundle, int i, int i2, int i3) throws RemoteException;

    void setAutofillFailure(int i, List<AutofillId> list, int i2) throws RemoteException;

    void setHasCallback(int i, int i2, boolean z) throws RemoteException;

    void setUserData(UserData userData) throws RemoteException;

    void startSession(IBinder iBinder, IBinder iBinder2, AutofillId autofillId, Rect rect, AutofillValue autofillValue, int i, boolean z, int i2, ComponentName componentName, boolean z2, IResultReceiver iResultReceiver) throws RemoteException;

    void updateSession(int i, AutofillId autofillId, Rect rect, AutofillValue autofillValue, int i2, int i3, int i4) throws RemoteException;
}
