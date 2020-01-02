package android.service.voice;

import android.app.assist.AssistContent;
import android.app.assist.AssistStructure;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.ThreadedRenderer;
import com.android.internal.app.IVoiceInteractionSessionShowCallback;

public interface IVoiceInteractionSession extends IInterface {

    public static class Default implements IVoiceInteractionSession {
        public void show(Bundle sessionArgs, int flags, IVoiceInteractionSessionShowCallback showCallback) throws RemoteException {
        }

        public void hide() throws RemoteException {
        }

        public void handleAssist(int taskId, IBinder activityId, Bundle assistData, AssistStructure structure, AssistContent content, int index, int count) throws RemoteException {
        }

        public void handleScreenshot(Bitmap screenshot) throws RemoteException {
        }

        public void taskStarted(Intent intent, int taskId) throws RemoteException {
        }

        public void taskFinished(Intent intent, int taskId) throws RemoteException {
        }

        public void closeSystemDialogs() throws RemoteException {
        }

        public void onLockscreenShown() throws RemoteException {
        }

        public void destroy() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IVoiceInteractionSession {
        private static final String DESCRIPTOR = "android.service.voice.IVoiceInteractionSession";
        static final int TRANSACTION_closeSystemDialogs = 7;
        static final int TRANSACTION_destroy = 9;
        static final int TRANSACTION_handleAssist = 3;
        static final int TRANSACTION_handleScreenshot = 4;
        static final int TRANSACTION_hide = 2;
        static final int TRANSACTION_onLockscreenShown = 8;
        static final int TRANSACTION_show = 1;
        static final int TRANSACTION_taskFinished = 6;
        static final int TRANSACTION_taskStarted = 5;

        private static class Proxy implements IVoiceInteractionSession {
            public static IVoiceInteractionSession sDefaultImpl;
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

            public void show(Bundle sessionArgs, int flags, IVoiceInteractionSessionShowCallback showCallback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (sessionArgs != null) {
                        _data.writeInt(1);
                        sessionArgs.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    _data.writeStrongBinder(showCallback != null ? showCallback.asBinder() : null);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().show(sessionArgs, flags, showCallback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void hide() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().hide();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void handleAssist(int taskId, IBinder activityId, Bundle assistData, AssistStructure structure, AssistContent content, int index, int count) throws RemoteException {
                Throwable th;
                int i;
                IBinder iBinder;
                Bundle bundle = assistData;
                AssistStructure assistStructure = structure;
                AssistContent assistContent = content;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(taskId);
                        try {
                            _data.writeStrongBinder(activityId);
                            if (bundle != null) {
                                _data.writeInt(1);
                                bundle.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            if (assistStructure != null) {
                                _data.writeInt(1);
                                assistStructure.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            if (assistContent != null) {
                                _data.writeInt(1);
                                assistContent.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            i = index;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        iBinder = activityId;
                        i = index;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(index);
                        _data.writeInt(count);
                        if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().handleAssist(taskId, activityId, assistData, structure, content, index, count);
                        _data.recycle();
                    } catch (Throwable th4) {
                        th = th4;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    int i2 = taskId;
                    iBinder = activityId;
                    i = index;
                    _data.recycle();
                    throw th;
                }
            }

            public void handleScreenshot(Bitmap screenshot) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (screenshot != null) {
                        _data.writeInt(1);
                        screenshot.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().handleScreenshot(screenshot);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void taskStarted(Intent intent, int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(taskId);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().taskStarted(intent, taskId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void taskFinished(Intent intent, int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(taskId);
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().taskFinished(intent, taskId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void closeSystemDialogs() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(7, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().closeSystemDialogs();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onLockscreenShown() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(8, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onLockscreenShown();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void destroy() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(9, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().destroy();
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IVoiceInteractionSession asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IVoiceInteractionSession)) {
                return new Proxy(obj);
            }
            return (IVoiceInteractionSession) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return ThreadedRenderer.OVERDRAW_PROPERTY_SHOW;
                case 2:
                    return "hide";
                case 3:
                    return "handleAssist";
                case 4:
                    return "handleScreenshot";
                case 5:
                    return "taskStarted";
                case 6:
                    return "taskFinished";
                case 7:
                    return "closeSystemDialogs";
                case 8:
                    return "onLockscreenShown";
                case 9:
                    return "destroy";
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
                Intent _arg0;
                switch (i) {
                    case 1:
                        Bundle _arg02;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        show(_arg02, data.readInt(), com.android.internal.app.IVoiceInteractionSessionShowCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        hide();
                        return true;
                    case 3:
                        Bundle _arg2;
                        AssistStructure _arg3;
                        AssistContent _arg4;
                        parcel.enforceInterface(descriptor);
                        int _arg03 = data.readInt();
                        IBinder _arg1 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg2 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg3 = (AssistStructure) AssistStructure.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg4 = (AssistContent) AssistContent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg4 = null;
                        }
                        handleAssist(_arg03, _arg1, _arg2, _arg3, _arg4, data.readInt(), data.readInt());
                        return true;
                    case 4:
                        Bitmap _arg04;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (Bitmap) Bitmap.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        handleScreenshot(_arg04);
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        taskStarted(_arg0, data.readInt());
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        taskFinished(_arg0, data.readInt());
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        closeSystemDialogs();
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        onLockscreenShown();
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        destroy();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IVoiceInteractionSession impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IVoiceInteractionSession getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void closeSystemDialogs() throws RemoteException;

    void destroy() throws RemoteException;

    void handleAssist(int i, IBinder iBinder, Bundle bundle, AssistStructure assistStructure, AssistContent assistContent, int i2, int i3) throws RemoteException;

    void handleScreenshot(Bitmap bitmap) throws RemoteException;

    void hide() throws RemoteException;

    void onLockscreenShown() throws RemoteException;

    void show(Bundle bundle, int i, IVoiceInteractionSessionShowCallback iVoiceInteractionSessionShowCallback) throws RemoteException;

    void taskFinished(Intent intent, int i) throws RemoteException;

    void taskStarted(Intent intent, int i) throws RemoteException;
}
