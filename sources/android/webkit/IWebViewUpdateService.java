package android.webkit;

import android.annotation.UnsupportedAppUsage;
import android.content.pm.PackageInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IWebViewUpdateService extends IInterface {

    public static class Default implements IWebViewUpdateService {
        public void notifyRelroCreationCompleted() throws RemoteException {
        }

        public WebViewProviderResponse waitForAndGetProvider() throws RemoteException {
            return null;
        }

        public String changeProviderAndSetting(String newProvider) throws RemoteException {
            return null;
        }

        public WebViewProviderInfo[] getValidWebViewPackages() throws RemoteException {
            return null;
        }

        public WebViewProviderInfo[] getAllWebViewPackages() throws RemoteException {
            return null;
        }

        public String getCurrentWebViewPackageName() throws RemoteException {
            return null;
        }

        public PackageInfo getCurrentWebViewPackage() throws RemoteException {
            return null;
        }

        public boolean isMultiProcessEnabled() throws RemoteException {
            return false;
        }

        public void enableMultiProcess(boolean enable) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IWebViewUpdateService {
        private static final String DESCRIPTOR = "android.webkit.IWebViewUpdateService";
        static final int TRANSACTION_changeProviderAndSetting = 3;
        static final int TRANSACTION_enableMultiProcess = 9;
        static final int TRANSACTION_getAllWebViewPackages = 5;
        static final int TRANSACTION_getCurrentWebViewPackage = 7;
        static final int TRANSACTION_getCurrentWebViewPackageName = 6;
        static final int TRANSACTION_getValidWebViewPackages = 4;
        static final int TRANSACTION_isMultiProcessEnabled = 8;
        static final int TRANSACTION_notifyRelroCreationCompleted = 1;
        static final int TRANSACTION_waitForAndGetProvider = 2;

        private static class Proxy implements IWebViewUpdateService {
            public static IWebViewUpdateService sDefaultImpl;
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

            public void notifyRelroCreationCompleted() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyRelroCreationCompleted();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public WebViewProviderResponse waitForAndGetProvider() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    WebViewProviderResponse webViewProviderResponse = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        webViewProviderResponse = Stub.getDefaultImpl();
                        if (webViewProviderResponse != 0) {
                            webViewProviderResponse = Stub.getDefaultImpl().waitForAndGetProvider();
                            return webViewProviderResponse;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        webViewProviderResponse = (WebViewProviderResponse) WebViewProviderResponse.CREATOR.createFromParcel(_reply);
                    } else {
                        webViewProviderResponse = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return webViewProviderResponse;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String changeProviderAndSetting(String newProvider) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(newProvider);
                    String str = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().changeProviderAndSetting(newProvider);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public WebViewProviderInfo[] getValidWebViewPackages() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    WebViewProviderInfo[] webViewProviderInfoArr = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        webViewProviderInfoArr = Stub.getDefaultImpl();
                        if (webViewProviderInfoArr != 0) {
                            webViewProviderInfoArr = Stub.getDefaultImpl().getValidWebViewPackages();
                            return webViewProviderInfoArr;
                        }
                    }
                    _reply.readException();
                    WebViewProviderInfo[] _result = (WebViewProviderInfo[]) _reply.createTypedArray(WebViewProviderInfo.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public WebViewProviderInfo[] getAllWebViewPackages() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    WebViewProviderInfo[] webViewProviderInfoArr = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        webViewProviderInfoArr = Stub.getDefaultImpl();
                        if (webViewProviderInfoArr != 0) {
                            webViewProviderInfoArr = Stub.getDefaultImpl().getAllWebViewPackages();
                            return webViewProviderInfoArr;
                        }
                    }
                    _reply.readException();
                    WebViewProviderInfo[] _result = (WebViewProviderInfo[]) _reply.createTypedArray(WebViewProviderInfo.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getCurrentWebViewPackageName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getCurrentWebViewPackageName();
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public PackageInfo getCurrentWebViewPackage() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    PackageInfo packageInfo = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        packageInfo = Stub.getDefaultImpl();
                        if (packageInfo != 0) {
                            packageInfo = Stub.getDefaultImpl().getCurrentWebViewPackage();
                            return packageInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        packageInfo = (PackageInfo) PackageInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        packageInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return packageInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isMultiProcessEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isMultiProcessEnabled();
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

            public void enableMultiProcess(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().enableMultiProcess(enable);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IWebViewUpdateService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IWebViewUpdateService)) {
                return new Proxy(obj);
            }
            return (IWebViewUpdateService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "notifyRelroCreationCompleted";
                case 2:
                    return "waitForAndGetProvider";
                case 3:
                    return "changeProviderAndSetting";
                case 4:
                    return "getValidWebViewPackages";
                case 5:
                    return "getAllWebViewPackages";
                case 6:
                    return "getCurrentWebViewPackageName";
                case 7:
                    return "getCurrentWebViewPackage";
                case 8:
                    return "isMultiProcessEnabled";
                case 9:
                    return "enableMultiProcess";
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
                boolean _arg0 = false;
                WebViewProviderInfo[] _result;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        notifyRelroCreationCompleted();
                        reply.writeNoException();
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        WebViewProviderResponse _result2 = waitForAndGetProvider();
                        reply.writeNoException();
                        if (_result2 != null) {
                            reply.writeInt(1);
                            _result2.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        String _result3 = changeProviderAndSetting(data.readString());
                        reply.writeNoException();
                        reply.writeString(_result3);
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        _result = getValidWebViewPackages();
                        reply.writeNoException();
                        reply.writeTypedArray(_result, 1);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        _result = getAllWebViewPackages();
                        reply.writeNoException();
                        reply.writeTypedArray(_result, 1);
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        String _result4 = getCurrentWebViewPackageName();
                        reply.writeNoException();
                        reply.writeString(_result4);
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        PackageInfo _result5 = getCurrentWebViewPackage();
                        reply.writeNoException();
                        if (_result5 != null) {
                            reply.writeInt(1);
                            _result5.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        _arg0 = isMultiProcessEnabled();
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        enableMultiProcess(_arg0);
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IWebViewUpdateService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IWebViewUpdateService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    String changeProviderAndSetting(String str) throws RemoteException;

    void enableMultiProcess(boolean z) throws RemoteException;

    WebViewProviderInfo[] getAllWebViewPackages() throws RemoteException;

    PackageInfo getCurrentWebViewPackage() throws RemoteException;

    @UnsupportedAppUsage
    String getCurrentWebViewPackageName() throws RemoteException;

    @UnsupportedAppUsage
    WebViewProviderInfo[] getValidWebViewPackages() throws RemoteException;

    boolean isMultiProcessEnabled() throws RemoteException;

    void notifyRelroCreationCompleted() throws RemoteException;

    WebViewProviderResponse waitForAndGetProvider() throws RemoteException;
}
