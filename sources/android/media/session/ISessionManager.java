package android.media.session;

import android.content.ComponentName;
import android.content.pm.ParceledListSlice;
import android.media.IRemoteVolumeController;
import android.media.Session2Token;
import android.media.session.MediaSession.Token;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.KeyEvent;
import java.util.List;

public interface ISessionManager extends IInterface {

    public static class Default implements ISessionManager {
        public ISession createSession(String packageName, ISessionCallback sessionCb, String tag, Bundle sessionInfo, int userId) throws RemoteException {
            return null;
        }

        public void notifySession2Created(Session2Token sessionToken) throws RemoteException {
        }

        public List<Token> getSessions(ComponentName compName, int userId) throws RemoteException {
            return null;
        }

        public ParceledListSlice getSession2Tokens(int userId) throws RemoteException {
            return null;
        }

        public void dispatchMediaKeyEvent(String packageName, boolean asSystemService, KeyEvent keyEvent, boolean needWakeLock) throws RemoteException {
        }

        public boolean dispatchMediaKeyEventToSessionAsSystemService(String packageName, Token sessionToken, KeyEvent keyEvent) throws RemoteException {
            return false;
        }

        public void dispatchVolumeKeyEvent(String packageName, String opPackageName, boolean asSystemService, KeyEvent keyEvent, int stream, boolean musicOnly) throws RemoteException {
        }

        public void dispatchVolumeKeyEventToSessionAsSystemService(String packageName, String opPackageName, Token sessionToken, KeyEvent keyEvent) throws RemoteException {
        }

        public void dispatchAdjustVolume(String packageName, String opPackageName, int suggestedStream, int delta, int flags) throws RemoteException {
        }

        public void addSessionsListener(IActiveSessionsListener listener, ComponentName compName, int userId) throws RemoteException {
        }

        public void removeSessionsListener(IActiveSessionsListener listener) throws RemoteException {
        }

        public void addSession2TokensListener(ISession2TokensListener listener, int userId) throws RemoteException {
        }

        public void removeSession2TokensListener(ISession2TokensListener listener) throws RemoteException {
        }

        public void registerRemoteVolumeController(IRemoteVolumeController rvc) throws RemoteException {
        }

        public void unregisterRemoteVolumeController(IRemoteVolumeController rvc) throws RemoteException {
        }

        public boolean isGlobalPriorityActive() throws RemoteException {
            return false;
        }

        public void setCallback(ICallback callback) throws RemoteException {
        }

        public void setOnVolumeKeyLongPressListener(IOnVolumeKeyLongPressListener listener) throws RemoteException {
        }

        public void setOnMediaKeyListener(IOnMediaKeyListener listener) throws RemoteException {
        }

        public boolean isTrusted(String controllerPackageName, int controllerPid, int controllerUid) throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ISessionManager {
        private static final String DESCRIPTOR = "android.media.session.ISessionManager";
        static final int TRANSACTION_addSession2TokensListener = 12;
        static final int TRANSACTION_addSessionsListener = 10;
        static final int TRANSACTION_createSession = 1;
        static final int TRANSACTION_dispatchAdjustVolume = 9;
        static final int TRANSACTION_dispatchMediaKeyEvent = 5;
        static final int TRANSACTION_dispatchMediaKeyEventToSessionAsSystemService = 6;
        static final int TRANSACTION_dispatchVolumeKeyEvent = 7;
        static final int TRANSACTION_dispatchVolumeKeyEventToSessionAsSystemService = 8;
        static final int TRANSACTION_getSession2Tokens = 4;
        static final int TRANSACTION_getSessions = 3;
        static final int TRANSACTION_isGlobalPriorityActive = 16;
        static final int TRANSACTION_isTrusted = 20;
        static final int TRANSACTION_notifySession2Created = 2;
        static final int TRANSACTION_registerRemoteVolumeController = 14;
        static final int TRANSACTION_removeSession2TokensListener = 13;
        static final int TRANSACTION_removeSessionsListener = 11;
        static final int TRANSACTION_setCallback = 17;
        static final int TRANSACTION_setOnMediaKeyListener = 19;
        static final int TRANSACTION_setOnVolumeKeyLongPressListener = 18;
        static final int TRANSACTION_unregisterRemoteVolumeController = 15;

        private static class Proxy implements ISessionManager {
            public static ISessionManager sDefaultImpl;
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

            public ISession createSession(String packageName, ISessionCallback sessionCb, String tag, Bundle sessionInfo, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(sessionCb != null ? sessionCb.asBinder() : null);
                    _data.writeString(tag);
                    ISession iSession = 0;
                    if (sessionInfo != null) {
                        _data.writeInt(1);
                        sessionInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        iSession = Stub.getDefaultImpl();
                        if (iSession != 0) {
                            iSession = Stub.getDefaultImpl().createSession(packageName, sessionCb, tag, sessionInfo, userId);
                            return iSession;
                        }
                    }
                    _reply.readException();
                    iSession = android.media.session.ISession.Stub.asInterface(_reply.readStrongBinder());
                    ISession _result = iSession;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifySession2Created(Session2Token sessionToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (sessionToken != null) {
                        _data.writeInt(1);
                        sessionToken.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifySession2Created(sessionToken);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<Token> getSessions(ComponentName compName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (compName != null) {
                        _data.writeInt(1);
                        compName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    List<Token> list = this.mRemote;
                    if (!list.transact(3, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != null) {
                            list = Stub.getDefaultImpl().getSessions(compName, userId);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(Token.CREATOR);
                    List<Token> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getSession2Tokens(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    ParceledListSlice parceledListSlice = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().getSession2Tokens(userId);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void dispatchMediaKeyEvent(String packageName, boolean asSystemService, KeyEvent keyEvent, boolean needWakeLock) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    int i = 1;
                    _data.writeInt(asSystemService ? 1 : 0);
                    if (keyEvent != null) {
                        _data.writeInt(1);
                        keyEvent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!needWakeLock) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().dispatchMediaKeyEvent(packageName, asSystemService, keyEvent, needWakeLock);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean dispatchMediaKeyEventToSessionAsSystemService(String packageName, Token sessionToken, KeyEvent keyEvent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _result = true;
                    if (sessionToken != null) {
                        _data.writeInt(1);
                        sessionToken.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (keyEvent != null) {
                        _data.writeInt(1);
                        keyEvent.writeToParcel(_data, 0);
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
                    _result = Stub.getDefaultImpl().dispatchMediaKeyEventToSessionAsSystemService(packageName, sessionToken, keyEvent);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void dispatchVolumeKeyEvent(String packageName, String opPackageName, boolean asSystemService, KeyEvent keyEvent, int stream, boolean musicOnly) throws RemoteException {
                Throwable th;
                int i;
                String str;
                KeyEvent keyEvent2 = keyEvent;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    int i2;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(packageName);
                        try {
                            _data.writeString(opPackageName);
                            i2 = 1;
                            _data.writeInt(asSystemService ? 1 : 0);
                            if (keyEvent2 != null) {
                                _data.writeInt(1);
                                keyEvent2.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            i = stream;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        str = opPackageName;
                        i = stream;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(stream);
                        if (!musicOnly) {
                            i2 = 0;
                        }
                        _data.writeInt(i2);
                        try {
                            if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().dispatchVolumeKeyEvent(packageName, opPackageName, asSystemService, keyEvent, stream, musicOnly);
                            _reply.recycle();
                            _data.recycle();
                        } catch (Throwable th4) {
                            th = th4;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    String str2 = packageName;
                    str = opPackageName;
                    i = stream;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void dispatchVolumeKeyEventToSessionAsSystemService(String packageName, String opPackageName, Token sessionToken, KeyEvent keyEvent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(opPackageName);
                    if (sessionToken != null) {
                        _data.writeInt(1);
                        sessionToken.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (keyEvent != null) {
                        _data.writeInt(1);
                        keyEvent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().dispatchVolumeKeyEventToSessionAsSystemService(packageName, opPackageName, sessionToken, keyEvent);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void dispatchAdjustVolume(String packageName, String opPackageName, int suggestedStream, int delta, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(opPackageName);
                    _data.writeInt(suggestedStream);
                    _data.writeInt(delta);
                    _data.writeInt(flags);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().dispatchAdjustVolume(packageName, opPackageName, suggestedStream, delta, flags);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addSessionsListener(IActiveSessionsListener listener, ComponentName compName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (compName != null) {
                        _data.writeInt(1);
                        compName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addSessionsListener(listener, compName, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeSessionsListener(IActiveSessionsListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeSessionsListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addSession2TokensListener(ISession2TokensListener listener, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addSession2TokensListener(listener, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeSession2TokensListener(ISession2TokensListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeSession2TokensListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerRemoteVolumeController(IRemoteVolumeController rvc) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(rvc != null ? rvc.asBinder() : null);
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerRemoteVolumeController(rvc);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterRemoteVolumeController(IRemoteVolumeController rvc) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(rvc != null ? rvc.asBinder() : null);
                    if (this.mRemote.transact(15, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterRemoteVolumeController(rvc);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isGlobalPriorityActive() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(16, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isGlobalPriorityActive();
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

            public void setCallback(ICallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(17, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setCallback(callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setOnVolumeKeyLongPressListener(IOnVolumeKeyLongPressListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setOnVolumeKeyLongPressListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setOnMediaKeyListener(IOnMediaKeyListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setOnMediaKeyListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isTrusted(String controllerPackageName, int controllerPid, int controllerUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(controllerPackageName);
                    _data.writeInt(controllerPid);
                    _data.writeInt(controllerUid);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(20, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isTrusted(controllerPackageName, controllerPid, controllerUid);
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

        public static ISessionManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ISessionManager)) {
                return new Proxy(obj);
            }
            return (ISessionManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "createSession";
                case 2:
                    return "notifySession2Created";
                case 3:
                    return "getSessions";
                case 4:
                    return "getSession2Tokens";
                case 5:
                    return "dispatchMediaKeyEvent";
                case 6:
                    return "dispatchMediaKeyEventToSessionAsSystemService";
                case 7:
                    return "dispatchVolumeKeyEvent";
                case 8:
                    return "dispatchVolumeKeyEventToSessionAsSystemService";
                case 9:
                    return "dispatchAdjustVolume";
                case 10:
                    return "addSessionsListener";
                case 11:
                    return "removeSessionsListener";
                case 12:
                    return "addSession2TokensListener";
                case 13:
                    return "removeSession2TokensListener";
                case 14:
                    return "registerRemoteVolumeController";
                case 15:
                    return "unregisterRemoteVolumeController";
                case 16:
                    return "isGlobalPriorityActive";
                case 17:
                    return "setCallback";
                case 18:
                    return "setOnVolumeKeyLongPressListener";
                case 19:
                    return "setOnMediaKeyListener";
                case 20:
                    return "isTrusted";
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
                boolean _arg3 = false;
                String _arg2;
                String _arg0;
                KeyEvent _arg22;
                String _arg02;
                boolean _result;
                switch (i) {
                    case 1:
                        Bundle _arg32;
                        parcel.enforceInterface(descriptor);
                        String _arg03 = data.readString();
                        ISessionCallback _arg1 = android.media.session.ISessionCallback.Stub.asInterface(data.readStrongBinder());
                        _arg2 = data.readString();
                        if (data.readInt() != 0) {
                            _arg32 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg32 = null;
                        }
                        ISession _result2 = createSession(_arg03, _arg1, _arg2, _arg32, data.readInt());
                        reply.writeNoException();
                        parcel2.writeStrongBinder(_result2 != null ? _result2.asBinder() : null);
                        return true;
                    case 2:
                        Session2Token _arg04;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (Session2Token) Session2Token.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        notifySession2Created(_arg04);
                        reply.writeNoException();
                        return true;
                    case 3:
                        ComponentName _arg05;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg05 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg05 = null;
                        }
                        List<Token> _result3 = getSessions(_arg05, data.readInt());
                        reply.writeNoException();
                        parcel2.writeTypedList(_result3);
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        ParceledListSlice _result4 = getSession2Tokens(data.readInt());
                        reply.writeNoException();
                        if (_result4 != null) {
                            parcel2.writeInt(1);
                            _result4.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        boolean _arg12 = data.readInt() != 0;
                        if (data.readInt() != 0) {
                            _arg22 = (KeyEvent) KeyEvent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg22 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg3 = true;
                        }
                        dispatchMediaKeyEvent(_arg0, _arg12, _arg22, _arg3);
                        reply.writeNoException();
                        return true;
                    case 6:
                        Token _arg13;
                        KeyEvent _arg23;
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg13 = (Token) Token.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg23 = (KeyEvent) KeyEvent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg23 = null;
                        }
                        _result = dispatchMediaKeyEventToSessionAsSystemService(_arg02, _arg13, _arg23);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 7:
                        KeyEvent _arg33;
                        parcel.enforceInterface(descriptor);
                        String _arg06 = data.readString();
                        _arg2 = data.readString();
                        _result = data.readInt() != 0;
                        if (data.readInt() != 0) {
                            _arg33 = (KeyEvent) KeyEvent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg33 = null;
                        }
                        dispatchVolumeKeyEvent(_arg06, _arg2, _result, _arg33, data.readInt(), data.readInt() != 0);
                        reply.writeNoException();
                        return true;
                    case 8:
                        Token _arg24;
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        _arg0 = data.readString();
                        if (data.readInt() != 0) {
                            _arg24 = (Token) Token.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg24 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg22 = (KeyEvent) KeyEvent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg22 = null;
                        }
                        dispatchVolumeKeyEventToSessionAsSystemService(_arg02, _arg0, _arg24, _arg22);
                        reply.writeNoException();
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        dispatchAdjustVolume(data.readString(), data.readString(), data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 10:
                        ComponentName _arg14;
                        parcel.enforceInterface(descriptor);
                        IActiveSessionsListener _arg07 = android.media.session.IActiveSessionsListener.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg14 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg14 = null;
                        }
                        addSessionsListener(_arg07, _arg14, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        removeSessionsListener(android.media.session.IActiveSessionsListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        addSession2TokensListener(android.media.session.ISession2TokensListener.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        removeSession2TokensListener(android.media.session.ISession2TokensListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        registerRemoteVolumeController(android.media.IRemoteVolumeController.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        unregisterRemoteVolumeController(android.media.IRemoteVolumeController.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        _arg3 = isGlobalPriorityActive();
                        reply.writeNoException();
                        parcel2.writeInt(_arg3);
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        setCallback(android.media.session.ICallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        setOnVolumeKeyLongPressListener(android.media.session.IOnVolumeKeyLongPressListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        setOnMediaKeyListener(android.media.session.IOnMediaKeyListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        _result = isTrusted(data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(ISessionManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ISessionManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void addSession2TokensListener(ISession2TokensListener iSession2TokensListener, int i) throws RemoteException;

    void addSessionsListener(IActiveSessionsListener iActiveSessionsListener, ComponentName componentName, int i) throws RemoteException;

    ISession createSession(String str, ISessionCallback iSessionCallback, String str2, Bundle bundle, int i) throws RemoteException;

    void dispatchAdjustVolume(String str, String str2, int i, int i2, int i3) throws RemoteException;

    void dispatchMediaKeyEvent(String str, boolean z, KeyEvent keyEvent, boolean z2) throws RemoteException;

    boolean dispatchMediaKeyEventToSessionAsSystemService(String str, Token token, KeyEvent keyEvent) throws RemoteException;

    void dispatchVolumeKeyEvent(String str, String str2, boolean z, KeyEvent keyEvent, int i, boolean z2) throws RemoteException;

    void dispatchVolumeKeyEventToSessionAsSystemService(String str, String str2, Token token, KeyEvent keyEvent) throws RemoteException;

    ParceledListSlice getSession2Tokens(int i) throws RemoteException;

    List<Token> getSessions(ComponentName componentName, int i) throws RemoteException;

    boolean isGlobalPriorityActive() throws RemoteException;

    boolean isTrusted(String str, int i, int i2) throws RemoteException;

    void notifySession2Created(Session2Token session2Token) throws RemoteException;

    void registerRemoteVolumeController(IRemoteVolumeController iRemoteVolumeController) throws RemoteException;

    void removeSession2TokensListener(ISession2TokensListener iSession2TokensListener) throws RemoteException;

    void removeSessionsListener(IActiveSessionsListener iActiveSessionsListener) throws RemoteException;

    void setCallback(ICallback iCallback) throws RemoteException;

    void setOnMediaKeyListener(IOnMediaKeyListener iOnMediaKeyListener) throws RemoteException;

    void setOnVolumeKeyLongPressListener(IOnVolumeKeyLongPressListener iOnVolumeKeyLongPressListener) throws RemoteException;

    void unregisterRemoteVolumeController(IRemoteVolumeController iRemoteVolumeController) throws RemoteException;
}
