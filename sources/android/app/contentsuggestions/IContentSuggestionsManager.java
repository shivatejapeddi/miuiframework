package android.app.contentsuggestions;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.internal.os.IResultReceiver;

public interface IContentSuggestionsManager extends IInterface {

    public static class Default implements IContentSuggestionsManager {
        public void provideContextImage(int userId, int taskId, Bundle imageContextRequestExtras) throws RemoteException {
        }

        public void suggestContentSelections(int userId, SelectionsRequest request, ISelectionsCallback callback) throws RemoteException {
        }

        public void classifyContentSelections(int userId, ClassificationsRequest request, IClassificationsCallback callback) throws RemoteException {
        }

        public void notifyInteraction(int userId, String requestId, Bundle interaction) throws RemoteException {
        }

        public void isEnabled(int userId, IResultReceiver receiver) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IContentSuggestionsManager {
        private static final String DESCRIPTOR = "android.app.contentsuggestions.IContentSuggestionsManager";
        static final int TRANSACTION_classifyContentSelections = 3;
        static final int TRANSACTION_isEnabled = 5;
        static final int TRANSACTION_notifyInteraction = 4;
        static final int TRANSACTION_provideContextImage = 1;
        static final int TRANSACTION_suggestContentSelections = 2;

        private static class Proxy implements IContentSuggestionsManager {
            public static IContentSuggestionsManager sDefaultImpl;
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

            public void provideContextImage(int userId, int taskId, Bundle imageContextRequestExtras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(taskId);
                    if (imageContextRequestExtras != null) {
                        _data.writeInt(1);
                        imageContextRequestExtras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().provideContextImage(userId, taskId, imageContextRequestExtras);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void suggestContentSelections(int userId, SelectionsRequest request, ISelectionsCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().suggestContentSelections(userId, request, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void classifyContentSelections(int userId, ClassificationsRequest request, IClassificationsCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().classifyContentSelections(userId, request, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void notifyInteraction(int userId, String requestId, Bundle interaction) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(requestId);
                    if (interaction != null) {
                        _data.writeInt(1);
                        interaction.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().notifyInteraction(userId, requestId, interaction);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void isEnabled(int userId, IResultReceiver receiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().isEnabled(userId, receiver);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IContentSuggestionsManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IContentSuggestionsManager)) {
                return new Proxy(obj);
            }
            return (IContentSuggestionsManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "provideContextImage";
            }
            if (transactionCode == 2) {
                return "suggestContentSelections";
            }
            if (transactionCode == 3) {
                return "classifyContentSelections";
            }
            if (transactionCode == 4) {
                return "notifyInteraction";
            }
            if (transactionCode != 5) {
                return null;
            }
            return "isEnabled";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            int _arg0;
            Bundle _arg2;
            if (code == 1) {
                data.enforceInterface(descriptor);
                _arg0 = data.readInt();
                int _arg1 = data.readInt();
                if (data.readInt() != 0) {
                    _arg2 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                } else {
                    _arg2 = null;
                }
                provideContextImage(_arg0, _arg1, _arg2);
                return true;
            } else if (code == 2) {
                SelectionsRequest _arg12;
                data.enforceInterface(descriptor);
                _arg0 = data.readInt();
                if (data.readInt() != 0) {
                    _arg12 = (SelectionsRequest) SelectionsRequest.CREATOR.createFromParcel(data);
                } else {
                    _arg12 = null;
                }
                suggestContentSelections(_arg0, _arg12, android.app.contentsuggestions.ISelectionsCallback.Stub.asInterface(data.readStrongBinder()));
                return true;
            } else if (code == 3) {
                ClassificationsRequest _arg13;
                data.enforceInterface(descriptor);
                _arg0 = data.readInt();
                if (data.readInt() != 0) {
                    _arg13 = (ClassificationsRequest) ClassificationsRequest.CREATOR.createFromParcel(data);
                } else {
                    _arg13 = null;
                }
                classifyContentSelections(_arg0, _arg13, android.app.contentsuggestions.IClassificationsCallback.Stub.asInterface(data.readStrongBinder()));
                return true;
            } else if (code == 4) {
                data.enforceInterface(descriptor);
                _arg0 = data.readInt();
                String _arg14 = data.readString();
                if (data.readInt() != 0) {
                    _arg2 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                } else {
                    _arg2 = null;
                }
                notifyInteraction(_arg0, _arg14, _arg2);
                return true;
            } else if (code == 5) {
                data.enforceInterface(descriptor);
                isEnabled(data.readInt(), com.android.internal.os.IResultReceiver.Stub.asInterface(data.readStrongBinder()));
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IContentSuggestionsManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IContentSuggestionsManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void classifyContentSelections(int i, ClassificationsRequest classificationsRequest, IClassificationsCallback iClassificationsCallback) throws RemoteException;

    void isEnabled(int i, IResultReceiver iResultReceiver) throws RemoteException;

    void notifyInteraction(int i, String str, Bundle bundle) throws RemoteException;

    void provideContextImage(int i, int i2, Bundle bundle) throws RemoteException;

    void suggestContentSelections(int i, SelectionsRequest selectionsRequest, ISelectionsCallback iSelectionsCallback) throws RemoteException;
}
