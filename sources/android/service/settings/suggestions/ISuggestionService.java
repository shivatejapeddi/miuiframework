package android.service.settings.suggestions;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface ISuggestionService extends IInterface {

    public static class Default implements ISuggestionService {
        public List<Suggestion> getSuggestions() throws RemoteException {
            return null;
        }

        public void dismissSuggestion(Suggestion suggestion) throws RemoteException {
        }

        public void launchSuggestion(Suggestion suggestion) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ISuggestionService {
        private static final String DESCRIPTOR = "android.service.settings.suggestions.ISuggestionService";
        static final int TRANSACTION_dismissSuggestion = 3;
        static final int TRANSACTION_getSuggestions = 2;
        static final int TRANSACTION_launchSuggestion = 4;

        private static class Proxy implements ISuggestionService {
            public static ISuggestionService sDefaultImpl;
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

            public List<Suggestion> getSuggestions() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<Suggestion> list = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getSuggestions();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(Suggestion.CREATOR);
                    List<Suggestion> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void dismissSuggestion(Suggestion suggestion) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (suggestion != null) {
                        _data.writeInt(1);
                        suggestion.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().dismissSuggestion(suggestion);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void launchSuggestion(Suggestion suggestion) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (suggestion != null) {
                        _data.writeInt(1);
                        suggestion.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().launchSuggestion(suggestion);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISuggestionService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ISuggestionService)) {
                return new Proxy(obj);
            }
            return (ISuggestionService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 2) {
                return "getSuggestions";
            }
            if (transactionCode == 3) {
                return "dismissSuggestion";
            }
            if (transactionCode != 4) {
                return null;
            }
            return "launchSuggestion";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            Suggestion _arg0;
            if (code == 2) {
                data.enforceInterface(descriptor);
                List<Suggestion> _result = getSuggestions();
                reply.writeNoException();
                reply.writeTypedList(_result);
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (Suggestion) Suggestion.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                dismissSuggestion(_arg0);
                reply.writeNoException();
                return true;
            } else if (code == 4) {
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (Suggestion) Suggestion.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                launchSuggestion(_arg0);
                reply.writeNoException();
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(ISuggestionService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ISuggestionService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void dismissSuggestion(Suggestion suggestion) throws RemoteException;

    List<Suggestion> getSuggestions() throws RemoteException;

    void launchSuggestion(Suggestion suggestion) throws RemoteException;
}
