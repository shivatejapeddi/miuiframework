package android.service.contentsuggestions;

import android.app.contentsuggestions.ClassificationsRequest;
import android.app.contentsuggestions.IClassificationsCallback;
import android.app.contentsuggestions.ISelectionsCallback;
import android.app.contentsuggestions.SelectionsRequest;
import android.graphics.GraphicBuffer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IContentSuggestionsService extends IInterface {

    public static abstract class Stub extends Binder implements IContentSuggestionsService {
        private static final String DESCRIPTOR = "android.service.contentsuggestions.IContentSuggestionsService";
        static final int TRANSACTION_classifyContentSelections = 3;
        static final int TRANSACTION_notifyInteraction = 4;
        static final int TRANSACTION_provideContextImage = 1;
        static final int TRANSACTION_suggestContentSelections = 2;

        private static class Proxy implements IContentSuggestionsService {
            public static IContentSuggestionsService sDefaultImpl;
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

            public void provideContextImage(int taskId, GraphicBuffer contextImage, int colorSpaceId, Bundle imageContextRequestExtras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    if (contextImage != null) {
                        _data.writeInt(1);
                        contextImage.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(colorSpaceId);
                    if (imageContextRequestExtras != null) {
                        _data.writeInt(1);
                        imageContextRequestExtras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().provideContextImage(taskId, contextImage, colorSpaceId, imageContextRequestExtras);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void suggestContentSelections(SelectionsRequest request, ISelectionsCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
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
                        Stub.getDefaultImpl().suggestContentSelections(request, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void classifyContentSelections(ClassificationsRequest request, IClassificationsCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
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
                        Stub.getDefaultImpl().classifyContentSelections(request, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void notifyInteraction(String requestId, Bundle interaction) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
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
                        Stub.getDefaultImpl().notifyInteraction(requestId, interaction);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IContentSuggestionsService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IContentSuggestionsService)) {
                return new Proxy(obj);
            }
            return (IContentSuggestionsService) iin;
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
            if (transactionCode != 4) {
                return null;
            }
            return "notifyInteraction";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                GraphicBuffer _arg1;
                Bundle _arg3;
                data.enforceInterface(descriptor);
                int _arg0 = data.readInt();
                if (data.readInt() != 0) {
                    _arg1 = (GraphicBuffer) GraphicBuffer.CREATOR.createFromParcel(data);
                } else {
                    _arg1 = null;
                }
                int _arg2 = data.readInt();
                if (data.readInt() != 0) {
                    _arg3 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                } else {
                    _arg3 = null;
                }
                provideContextImage(_arg0, _arg1, _arg2, _arg3);
                return true;
            } else if (code == 2) {
                SelectionsRequest _arg02;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg02 = (SelectionsRequest) SelectionsRequest.CREATOR.createFromParcel(data);
                } else {
                    _arg02 = null;
                }
                suggestContentSelections(_arg02, android.app.contentsuggestions.ISelectionsCallback.Stub.asInterface(data.readStrongBinder()));
                return true;
            } else if (code == 3) {
                ClassificationsRequest _arg03;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg03 = (ClassificationsRequest) ClassificationsRequest.CREATOR.createFromParcel(data);
                } else {
                    _arg03 = null;
                }
                classifyContentSelections(_arg03, android.app.contentsuggestions.IClassificationsCallback.Stub.asInterface(data.readStrongBinder()));
                return true;
            } else if (code == 4) {
                Bundle _arg12;
                data.enforceInterface(descriptor);
                String _arg04 = data.readString();
                if (data.readInt() != 0) {
                    _arg12 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                } else {
                    _arg12 = null;
                }
                notifyInteraction(_arg04, _arg12);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IContentSuggestionsService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IContentSuggestionsService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    public static class Default implements IContentSuggestionsService {
        public void provideContextImage(int taskId, GraphicBuffer contextImage, int colorSpaceId, Bundle imageContextRequestExtras) throws RemoteException {
        }

        public void suggestContentSelections(SelectionsRequest request, ISelectionsCallback callback) throws RemoteException {
        }

        public void classifyContentSelections(ClassificationsRequest request, IClassificationsCallback callback) throws RemoteException {
        }

        public void notifyInteraction(String requestId, Bundle interaction) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    void classifyContentSelections(ClassificationsRequest classificationsRequest, IClassificationsCallback iClassificationsCallback) throws RemoteException;

    void notifyInteraction(String str, Bundle bundle) throws RemoteException;

    void provideContextImage(int i, GraphicBuffer graphicBuffer, int i2, Bundle bundle) throws RemoteException;

    void suggestContentSelections(SelectionsRequest selectionsRequest, ISelectionsCallback iSelectionsCallback) throws RemoteException;
}
