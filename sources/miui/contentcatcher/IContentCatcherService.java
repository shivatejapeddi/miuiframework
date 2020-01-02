package miui.contentcatcher;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import miui.contentcatcher.sdk.ClientToken;
import miui.contentcatcher.sdk.Content;
import miui.contentcatcher.sdk.DecorateContentParam;
import miui.contentcatcher.sdk.Token;
import miui.contentcatcher.sdk.data.PageConfig;
import miui.contentcatcher.sdk.injector.IContentDecorateCallback;
import miui.contentcatcher.sdk.listener.IContentListenerCallback;

public interface IContentCatcherService extends IInterface {

    public static class Default implements IContentCatcherService {
        public PageConfig getPageConfig(Token token) throws RemoteException {
            return null;
        }

        public void registerContentInjector(Token token, IContentDecorateCallback callback) throws RemoteException {
        }

        public void unregisterContentInjector(Token token) throws RemoteException {
        }

        public void onContentCatched(Content content) throws RemoteException {
        }

        public void registerContentListener(ClientToken token, IContentListenerCallback callback) throws RemoteException {
        }

        public void unregisterContentListener(ClientToken token) throws RemoteException {
        }

        public void decorateContent(ClientToken listenerToken, Token targetInjectorToken, DecorateContentParam params) throws RemoteException {
        }

        public void updateConfig(String[] configs) throws RemoteException {
        }

        public void updateClientConfig(String target, String jobTag, boolean enable) throws RemoteException {
        }

        public void updateJobValidity(String jobTag, String packageName, boolean enable) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IContentCatcherService {
        private static final String DESCRIPTOR = "miui.contentcatcher.IContentCatcherService";
        static final int TRANSACTION_decorateContent = 7;
        static final int TRANSACTION_getPageConfig = 1;
        static final int TRANSACTION_onContentCatched = 4;
        static final int TRANSACTION_registerContentInjector = 2;
        static final int TRANSACTION_registerContentListener = 5;
        static final int TRANSACTION_unregisterContentInjector = 3;
        static final int TRANSACTION_unregisterContentListener = 6;
        static final int TRANSACTION_updateClientConfig = 9;
        static final int TRANSACTION_updateConfig = 8;
        static final int TRANSACTION_updateJobValidity = 10;

        private static class Proxy implements IContentCatcherService {
            public static IContentCatcherService sDefaultImpl;
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

            public PageConfig getPageConfig(Token token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    PageConfig pageConfig = 0;
                    if (token != null) {
                        _data.writeInt(1);
                        token.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        pageConfig = Stub.getDefaultImpl();
                        if (pageConfig != 0) {
                            pageConfig = Stub.getDefaultImpl().getPageConfig(token);
                            return pageConfig;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        pageConfig = (PageConfig) PageConfig.CREATOR.createFromParcel(_reply);
                    } else {
                        pageConfig = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return pageConfig;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerContentInjector(Token token, IContentDecorateCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (token != null) {
                        _data.writeInt(1);
                        token.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerContentInjector(token, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterContentInjector(Token token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (token != null) {
                        _data.writeInt(1);
                        token.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterContentInjector(token);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onContentCatched(Content content) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (content != null) {
                        _data.writeInt(1);
                        content.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onContentCatched(content);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerContentListener(ClientToken token, IContentListenerCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (token != null) {
                        _data.writeInt(1);
                        token.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerContentListener(token, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterContentListener(ClientToken token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (token != null) {
                        _data.writeInt(1);
                        token.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterContentListener(token);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void decorateContent(ClientToken listenerToken, Token targetInjectorToken, DecorateContentParam params) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (listenerToken != null) {
                        _data.writeInt(1);
                        listenerToken.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (targetInjectorToken != null) {
                        _data.writeInt(1);
                        targetInjectorToken.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().decorateContent(listenerToken, targetInjectorToken, params);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateConfig(String[] configs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(configs);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateConfig(configs);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateClientConfig(String target, String jobTag, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(target);
                    _data.writeString(jobTag);
                    _data.writeInt(enable ? 1 : 0);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateClientConfig(target, jobTag, enable);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateJobValidity(String jobTag, String packageName, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(jobTag);
                    _data.writeString(packageName);
                    _data.writeInt(enable ? 1 : 0);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateJobValidity(jobTag, packageName, enable);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IContentCatcherService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IContentCatcherService)) {
                return new Proxy(obj);
            }
            return (IContentCatcherService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getPageConfig";
                case 2:
                    return "registerContentInjector";
                case 3:
                    return "unregisterContentInjector";
                case 4:
                    return "onContentCatched";
                case 5:
                    return "registerContentListener";
                case 6:
                    return "unregisterContentListener";
                case 7:
                    return "decorateContent";
                case 8:
                    return "updateConfig";
                case 9:
                    return "updateClientConfig";
                case 10:
                    return "updateJobValidity";
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
                boolean _arg2 = false;
                Token _arg0;
                Token _arg02;
                ClientToken _arg03;
                String _arg04;
                String _arg1;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Token) Token.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        PageConfig _result = getPageConfig(_arg0);
                        reply.writeNoException();
                        if (_result != null) {
                            reply.writeInt(1);
                            _result.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (Token) Token.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        registerContentInjector(_arg02, miui.contentcatcher.sdk.injector.IContentDecorateCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (Token) Token.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        unregisterContentInjector(_arg02);
                        reply.writeNoException();
                        return true;
                    case 4:
                        Content _arg05;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg05 = (Content) Content.CREATOR.createFromParcel(data);
                        } else {
                            _arg05 = null;
                        }
                        onContentCatched(_arg05);
                        reply.writeNoException();
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (ClientToken) ClientToken.CREATOR.createFromParcel(data);
                        } else {
                            _arg03 = null;
                        }
                        registerContentListener(_arg03, miui.contentcatcher.sdk.listener.IContentListenerCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (ClientToken) ClientToken.CREATOR.createFromParcel(data);
                        } else {
                            _arg03 = null;
                        }
                        unregisterContentListener(_arg03);
                        reply.writeNoException();
                        return true;
                    case 7:
                        DecorateContentParam _arg22;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (ClientToken) ClientToken.CREATOR.createFromParcel(data);
                        } else {
                            _arg03 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg0 = (Token) Token.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg22 = (DecorateContentParam) DecorateContentParam.CREATOR.createFromParcel(data);
                        } else {
                            _arg22 = null;
                        }
                        decorateContent(_arg03, _arg0, _arg22);
                        reply.writeNoException();
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        updateConfig(data.createStringArray());
                        reply.writeNoException();
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        _arg04 = data.readString();
                        _arg1 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        updateClientConfig(_arg04, _arg1, _arg2);
                        reply.writeNoException();
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        _arg04 = data.readString();
                        _arg1 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        updateJobValidity(_arg04, _arg1, _arg2);
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IContentCatcherService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IContentCatcherService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void decorateContent(ClientToken clientToken, Token token, DecorateContentParam decorateContentParam) throws RemoteException;

    PageConfig getPageConfig(Token token) throws RemoteException;

    void onContentCatched(Content content) throws RemoteException;

    void registerContentInjector(Token token, IContentDecorateCallback iContentDecorateCallback) throws RemoteException;

    void registerContentListener(ClientToken clientToken, IContentListenerCallback iContentListenerCallback) throws RemoteException;

    void unregisterContentInjector(Token token) throws RemoteException;

    void unregisterContentListener(ClientToken clientToken) throws RemoteException;

    void updateClientConfig(String str, String str2, boolean z) throws RemoteException;

    void updateConfig(String[] strArr) throws RemoteException;

    void updateJobValidity(String str, String str2, boolean z) throws RemoteException;
}
