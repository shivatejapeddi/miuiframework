package android.media.session;

import android.content.ComponentName;
import android.media.session.MediaSession.Token;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.KeyEvent;

public interface ICallback extends IInterface {

    public static class Default implements ICallback {
        public void onMediaKeyEventDispatchedToMediaSession(KeyEvent event, Token sessionToken) throws RemoteException {
        }

        public void onMediaKeyEventDispatchedToMediaButtonReceiver(KeyEvent event, ComponentName mediaButtonReceiver) throws RemoteException {
        }

        public void onAddressedPlayerChangedToMediaSession(Token sessionToken) throws RemoteException {
        }

        public void onAddressedPlayerChangedToMediaButtonReceiver(ComponentName mediaButtonReceiver) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ICallback {
        private static final String DESCRIPTOR = "android.media.session.ICallback";
        static final int TRANSACTION_onAddressedPlayerChangedToMediaButtonReceiver = 4;
        static final int TRANSACTION_onAddressedPlayerChangedToMediaSession = 3;
        static final int TRANSACTION_onMediaKeyEventDispatchedToMediaButtonReceiver = 2;
        static final int TRANSACTION_onMediaKeyEventDispatchedToMediaSession = 1;

        private static class Proxy implements ICallback {
            public static ICallback sDefaultImpl;
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

            public void onMediaKeyEventDispatchedToMediaSession(KeyEvent event, Token sessionToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (event != null) {
                        _data.writeInt(1);
                        event.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (sessionToken != null) {
                        _data.writeInt(1);
                        sessionToken.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onMediaKeyEventDispatchedToMediaSession(event, sessionToken);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onMediaKeyEventDispatchedToMediaButtonReceiver(KeyEvent event, ComponentName mediaButtonReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (event != null) {
                        _data.writeInt(1);
                        event.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (mediaButtonReceiver != null) {
                        _data.writeInt(1);
                        mediaButtonReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onMediaKeyEventDispatchedToMediaButtonReceiver(event, mediaButtonReceiver);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onAddressedPlayerChangedToMediaSession(Token sessionToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (sessionToken != null) {
                        _data.writeInt(1);
                        sessionToken.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onAddressedPlayerChangedToMediaSession(sessionToken);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onAddressedPlayerChangedToMediaButtonReceiver(ComponentName mediaButtonReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (mediaButtonReceiver != null) {
                        _data.writeInt(1);
                        mediaButtonReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onAddressedPlayerChangedToMediaButtonReceiver(mediaButtonReceiver);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ICallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ICallback)) {
                return new Proxy(obj);
            }
            return (ICallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onMediaKeyEventDispatchedToMediaSession";
            }
            if (transactionCode == 2) {
                return "onMediaKeyEventDispatchedToMediaButtonReceiver";
            }
            if (transactionCode == 3) {
                return "onAddressedPlayerChangedToMediaSession";
            }
            if (transactionCode != 4) {
                return null;
            }
            return "onAddressedPlayerChangedToMediaButtonReceiver";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            KeyEvent _arg0;
            if (code == 1) {
                Token _arg1;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (KeyEvent) KeyEvent.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                if (data.readInt() != 0) {
                    _arg1 = (Token) Token.CREATOR.createFromParcel(data);
                } else {
                    _arg1 = null;
                }
                onMediaKeyEventDispatchedToMediaSession(_arg0, _arg1);
                return true;
            } else if (code == 2) {
                ComponentName _arg12;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (KeyEvent) KeyEvent.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                if (data.readInt() != 0) {
                    _arg12 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                } else {
                    _arg12 = null;
                }
                onMediaKeyEventDispatchedToMediaButtonReceiver(_arg0, _arg12);
                return true;
            } else if (code == 3) {
                Token _arg02;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg02 = (Token) Token.CREATOR.createFromParcel(data);
                } else {
                    _arg02 = null;
                }
                onAddressedPlayerChangedToMediaSession(_arg02);
                return true;
            } else if (code == 4) {
                ComponentName _arg03;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg03 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                } else {
                    _arg03 = null;
                }
                onAddressedPlayerChangedToMediaButtonReceiver(_arg03);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(ICallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ICallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onAddressedPlayerChangedToMediaButtonReceiver(ComponentName componentName) throws RemoteException;

    void onAddressedPlayerChangedToMediaSession(Token token) throws RemoteException;

    void onMediaKeyEventDispatchedToMediaButtonReceiver(KeyEvent keyEvent, ComponentName componentName) throws RemoteException;

    void onMediaKeyEventDispatchedToMediaSession(KeyEvent keyEvent, Token token) throws RemoteException;
}
