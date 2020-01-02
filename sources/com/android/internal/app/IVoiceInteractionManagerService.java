package com.android.internal.app;

import android.annotation.UnsupportedAppUsage;
import android.content.ComponentName;
import android.content.Intent;
import android.hardware.soundtrigger.IRecognitionStatusCallback;
import android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel;
import android.hardware.soundtrigger.SoundTrigger.ModuleProperties;
import android.hardware.soundtrigger.SoundTrigger.RecognitionConfig;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.service.voice.IVoiceInteractionService;
import android.service.voice.IVoiceInteractionSession;
import java.util.List;

public interface IVoiceInteractionManagerService extends IInterface {

    public static class Default implements IVoiceInteractionManagerService {
        public void showSession(IVoiceInteractionService service, Bundle sessionArgs, int flags) throws RemoteException {
        }

        public boolean deliverNewSession(IBinder token, IVoiceInteractionSession session, IVoiceInteractor interactor) throws RemoteException {
            return false;
        }

        public boolean showSessionFromSession(IBinder token, Bundle sessionArgs, int flags) throws RemoteException {
            return false;
        }

        public boolean hideSessionFromSession(IBinder token) throws RemoteException {
            return false;
        }

        public int startVoiceActivity(IBinder token, Intent intent, String resolvedType) throws RemoteException {
            return 0;
        }

        public int startAssistantActivity(IBinder token, Intent intent, String resolvedType) throws RemoteException {
            return 0;
        }

        public void setKeepAwake(IBinder token, boolean keepAwake) throws RemoteException {
        }

        public void closeSystemDialogs(IBinder token) throws RemoteException {
        }

        public void finish(IBinder token) throws RemoteException {
        }

        public void setDisabledShowContext(int flags) throws RemoteException {
        }

        public int getDisabledShowContext() throws RemoteException {
            return 0;
        }

        public int getUserDisabledShowContext() throws RemoteException {
            return 0;
        }

        public KeyphraseSoundModel getKeyphraseSoundModel(int keyphraseId, String bcp47Locale) throws RemoteException {
            return null;
        }

        public int updateKeyphraseSoundModel(KeyphraseSoundModel model) throws RemoteException {
            return 0;
        }

        public int deleteKeyphraseSoundModel(int keyphraseId, String bcp47Locale) throws RemoteException {
            return 0;
        }

        public ModuleProperties getDspModuleProperties(IVoiceInteractionService service) throws RemoteException {
            return null;
        }

        public boolean isEnrolledForKeyphrase(IVoiceInteractionService service, int keyphraseId, String bcp47Locale) throws RemoteException {
            return false;
        }

        public int startRecognition(IVoiceInteractionService service, int keyphraseId, String bcp47Locale, IRecognitionStatusCallback callback, RecognitionConfig recognitionConfig) throws RemoteException {
            return 0;
        }

        public int stopRecognition(IVoiceInteractionService service, int keyphraseId, IRecognitionStatusCallback callback) throws RemoteException {
            return 0;
        }

        public ComponentName getActiveServiceComponentName() throws RemoteException {
            return null;
        }

        public boolean showSessionForActiveService(Bundle args, int sourceFlags, IVoiceInteractionSessionShowCallback showCallback, IBinder activityToken) throws RemoteException {
            return false;
        }

        public void hideCurrentSession() throws RemoteException {
        }

        public void launchVoiceAssistFromKeyguard() throws RemoteException {
        }

        public boolean isSessionRunning() throws RemoteException {
            return false;
        }

        public boolean activeServiceSupportsAssist() throws RemoteException {
            return false;
        }

        public boolean activeServiceSupportsLaunchFromKeyguard() throws RemoteException {
            return false;
        }

        public void onLockscreenShown() throws RemoteException {
        }

        public void registerVoiceInteractionSessionListener(IVoiceInteractionSessionListener listener) throws RemoteException {
        }

        public void getActiveServiceSupportedActions(List<String> list, IVoiceActionCheckCallback callback) throws RemoteException {
        }

        public void setUiHints(IVoiceInteractionService service, Bundle hints) throws RemoteException {
        }

        public void requestDirectActions(IBinder token, int taskId, IBinder assistToken, RemoteCallback cancellationCallback, RemoteCallback callback) throws RemoteException {
        }

        public void performDirectAction(IBinder token, String actionId, Bundle arguments, int taskId, IBinder assistToken, RemoteCallback cancellationCallback, RemoteCallback resultCallback) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IVoiceInteractionManagerService {
        private static final String DESCRIPTOR = "com.android.internal.app.IVoiceInteractionManagerService";
        static final int TRANSACTION_activeServiceSupportsAssist = 25;
        static final int TRANSACTION_activeServiceSupportsLaunchFromKeyguard = 26;
        static final int TRANSACTION_closeSystemDialogs = 8;
        static final int TRANSACTION_deleteKeyphraseSoundModel = 15;
        static final int TRANSACTION_deliverNewSession = 2;
        static final int TRANSACTION_finish = 9;
        static final int TRANSACTION_getActiveServiceComponentName = 20;
        static final int TRANSACTION_getActiveServiceSupportedActions = 29;
        static final int TRANSACTION_getDisabledShowContext = 11;
        static final int TRANSACTION_getDspModuleProperties = 16;
        static final int TRANSACTION_getKeyphraseSoundModel = 13;
        static final int TRANSACTION_getUserDisabledShowContext = 12;
        static final int TRANSACTION_hideCurrentSession = 22;
        static final int TRANSACTION_hideSessionFromSession = 4;
        static final int TRANSACTION_isEnrolledForKeyphrase = 17;
        static final int TRANSACTION_isSessionRunning = 24;
        static final int TRANSACTION_launchVoiceAssistFromKeyguard = 23;
        static final int TRANSACTION_onLockscreenShown = 27;
        static final int TRANSACTION_performDirectAction = 32;
        static final int TRANSACTION_registerVoiceInteractionSessionListener = 28;
        static final int TRANSACTION_requestDirectActions = 31;
        static final int TRANSACTION_setDisabledShowContext = 10;
        static final int TRANSACTION_setKeepAwake = 7;
        static final int TRANSACTION_setUiHints = 30;
        static final int TRANSACTION_showSession = 1;
        static final int TRANSACTION_showSessionForActiveService = 21;
        static final int TRANSACTION_showSessionFromSession = 3;
        static final int TRANSACTION_startAssistantActivity = 6;
        static final int TRANSACTION_startRecognition = 18;
        static final int TRANSACTION_startVoiceActivity = 5;
        static final int TRANSACTION_stopRecognition = 19;
        static final int TRANSACTION_updateKeyphraseSoundModel = 14;

        private static class Proxy implements IVoiceInteractionManagerService {
            public static IVoiceInteractionManagerService sDefaultImpl;
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

            public void showSession(IVoiceInteractionService service, Bundle sessionArgs, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(service != null ? service.asBinder() : null);
                    if (sessionArgs != null) {
                        _data.writeInt(1);
                        sessionArgs.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().showSession(service, sessionArgs, flags);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean deliverNewSession(IBinder token, IVoiceInteractionSession session, IVoiceInteractor interactor) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    IBinder iBinder = null;
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    if (interactor != null) {
                        iBinder = interactor.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().deliverNewSession(token, session, interactor);
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

            public boolean showSessionFromSession(IBinder token, Bundle sessionArgs, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _result = true;
                    if (sessionArgs != null) {
                        _data.writeInt(1);
                        sessionArgs.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().showSessionFromSession(token, sessionArgs, flags);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean hideSessionFromSession(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().hideSessionFromSession(token);
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

            public int startVoiceActivity(IBinder token, Intent intent, String resolvedType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    int i = this.mRemote;
                    if (!i.transact(5, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().startVoiceActivity(token, intent, resolvedType);
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

            public int startAssistantActivity(IBinder token, Intent intent, String resolvedType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    int i = this.mRemote;
                    if (!i.transact(6, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().startAssistantActivity(token, intent, resolvedType);
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

            public void setKeepAwake(IBinder token, boolean keepAwake) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(keepAwake ? 1 : 0);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setKeepAwake(token, keepAwake);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void closeSystemDialogs(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().closeSystemDialogs(token);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void finish(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().finish(token);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setDisabledShowContext(int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setDisabledShowContext(flags);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getDisabledShowContext() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 11;
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getDisabledShowContext();
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

            public int getUserDisabledShowContext() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 12;
                    if (!this.mRemote.transact(12, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getUserDisabledShowContext();
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

            public KeyphraseSoundModel getKeyphraseSoundModel(int keyphraseId, String bcp47Locale) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(keyphraseId);
                    _data.writeString(bcp47Locale);
                    KeyphraseSoundModel keyphraseSoundModel = 13;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        keyphraseSoundModel = Stub.getDefaultImpl();
                        if (keyphraseSoundModel != 0) {
                            keyphraseSoundModel = Stub.getDefaultImpl().getKeyphraseSoundModel(keyphraseId, bcp47Locale);
                            return keyphraseSoundModel;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        keyphraseSoundModel = (KeyphraseSoundModel) KeyphraseSoundModel.CREATOR.createFromParcel(_reply);
                    } else {
                        keyphraseSoundModel = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return keyphraseSoundModel;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int updateKeyphraseSoundModel(KeyphraseSoundModel model) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (model != null) {
                        _data.writeInt(1);
                        model.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(14, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().updateKeyphraseSoundModel(model);
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

            public int deleteKeyphraseSoundModel(int keyphraseId, String bcp47Locale) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(keyphraseId);
                    _data.writeString(bcp47Locale);
                    int i = 15;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().deleteKeyphraseSoundModel(keyphraseId, bcp47Locale);
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

            public ModuleProperties getDspModuleProperties(IVoiceInteractionService service) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(service != null ? service.asBinder() : null);
                    ModuleProperties moduleProperties = 16;
                    if (!this.mRemote.transact(16, _data, _reply, 0)) {
                        moduleProperties = Stub.getDefaultImpl();
                        if (moduleProperties != 0) {
                            moduleProperties = Stub.getDefaultImpl().getDspModuleProperties(service);
                            return moduleProperties;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        moduleProperties = (ModuleProperties) ModuleProperties.CREATOR.createFromParcel(_reply);
                    } else {
                        moduleProperties = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return moduleProperties;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isEnrolledForKeyphrase(IVoiceInteractionService service, int keyphraseId, String bcp47Locale) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(service != null ? service.asBinder() : null);
                    _data.writeInt(keyphraseId);
                    _data.writeString(bcp47Locale);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(17, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isEnrolledForKeyphrase(service, keyphraseId, bcp47Locale);
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

            public int startRecognition(IVoiceInteractionService service, int keyphraseId, String bcp47Locale, IRecognitionStatusCallback callback, RecognitionConfig recognitionConfig) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IBinder iBinder = null;
                    _data.writeStrongBinder(service != null ? service.asBinder() : null);
                    _data.writeInt(keyphraseId);
                    _data.writeString(bcp47Locale);
                    if (callback != null) {
                        iBinder = callback.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    if (recognitionConfig != null) {
                        _data.writeInt(1);
                        recognitionConfig.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(18, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().startRecognition(service, keyphraseId, bcp47Locale, callback, recognitionConfig);
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

            public int stopRecognition(IVoiceInteractionService service, int keyphraseId, IRecognitionStatusCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IBinder iBinder = null;
                    _data.writeStrongBinder(service != null ? service.asBinder() : null);
                    _data.writeInt(keyphraseId);
                    if (callback != null) {
                        iBinder = callback.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    int i = 19;
                    if (!this.mRemote.transact(19, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().stopRecognition(service, keyphraseId, callback);
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

            public ComponentName getActiveServiceComponentName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ComponentName componentName = 20;
                    if (!this.mRemote.transact(20, _data, _reply, 0)) {
                        componentName = Stub.getDefaultImpl();
                        if (componentName != 0) {
                            componentName = Stub.getDefaultImpl().getActiveServiceComponentName();
                            return componentName;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        componentName = (ComponentName) ComponentName.CREATOR.createFromParcel(_reply);
                    } else {
                        componentName = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return componentName;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean showSessionForActiveService(Bundle args, int sourceFlags, IVoiceInteractionSessionShowCallback showCallback, IBinder activityToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (args != null) {
                        _data.writeInt(1);
                        args.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(sourceFlags);
                    _data.writeStrongBinder(showCallback != null ? showCallback.asBinder() : null);
                    _data.writeStrongBinder(activityToken);
                    if (this.mRemote.transact(21, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().showSessionForActiveService(args, sourceFlags, showCallback, activityToken);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void hideCurrentSession() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().hideCurrentSession();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void launchVoiceAssistFromKeyguard() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(23, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().launchVoiceAssistFromKeyguard();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isSessionRunning() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(24, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isSessionRunning();
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

            public boolean activeServiceSupportsAssist() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(25, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().activeServiceSupportsAssist();
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

            public boolean activeServiceSupportsLaunchFromKeyguard() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(26, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().activeServiceSupportsLaunchFromKeyguard();
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

            public void onLockscreenShown() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(27, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onLockscreenShown();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerVoiceInteractionSessionListener(IVoiceInteractionSessionListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(28, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerVoiceInteractionSessionListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void getActiveServiceSupportedActions(List<String> voiceActions, IVoiceActionCheckCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(voiceActions);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(29, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().getActiveServiceSupportedActions(voiceActions, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setUiHints(IVoiceInteractionService service, Bundle hints) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(service != null ? service.asBinder() : null);
                    if (hints != null) {
                        _data.writeInt(1);
                        hints.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(30, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setUiHints(service, hints);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestDirectActions(IBinder token, int taskId, IBinder assistToken, RemoteCallback cancellationCallback, RemoteCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(taskId);
                    _data.writeStrongBinder(assistToken);
                    if (cancellationCallback != null) {
                        _data.writeInt(1);
                        cancellationCallback.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (callback != null) {
                        _data.writeInt(1);
                        callback.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(31, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().requestDirectActions(token, taskId, assistToken, cancellationCallback, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void performDirectAction(IBinder token, String actionId, Bundle arguments, int taskId, IBinder assistToken, RemoteCallback cancellationCallback, RemoteCallback resultCallback) throws RemoteException {
                Throwable th;
                String str;
                Bundle bundle = arguments;
                RemoteCallback remoteCallback = cancellationCallback;
                RemoteCallback remoteCallback2 = resultCallback;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeStrongBinder(token);
                        try {
                            _data.writeString(actionId);
                            if (bundle != null) {
                                _data.writeInt(1);
                                bundle.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            _data.writeInt(taskId);
                            _data.writeStrongBinder(assistToken);
                            if (remoteCallback != null) {
                                _data.writeInt(1);
                                remoteCallback.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            if (remoteCallback2 != null) {
                                _data.writeInt(1);
                                remoteCallback2.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            if (this.mRemote.transact(32, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().performDirectAction(token, actionId, arguments, taskId, assistToken, cancellationCallback, resultCallback);
                            _reply.recycle();
                            _data.recycle();
                        } catch (Throwable th2) {
                            th = th2;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        str = actionId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    IBinder iBinder = token;
                    str = actionId;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IVoiceInteractionManagerService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IVoiceInteractionManagerService)) {
                return new Proxy(obj);
            }
            return (IVoiceInteractionManagerService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "showSession";
                case 2:
                    return "deliverNewSession";
                case 3:
                    return "showSessionFromSession";
                case 4:
                    return "hideSessionFromSession";
                case 5:
                    return "startVoiceActivity";
                case 6:
                    return "startAssistantActivity";
                case 7:
                    return "setKeepAwake";
                case 8:
                    return "closeSystemDialogs";
                case 9:
                    return "finish";
                case 10:
                    return "setDisabledShowContext";
                case 11:
                    return "getDisabledShowContext";
                case 12:
                    return "getUserDisabledShowContext";
                case 13:
                    return "getKeyphraseSoundModel";
                case 14:
                    return "updateKeyphraseSoundModel";
                case 15:
                    return "deleteKeyphraseSoundModel";
                case 16:
                    return "getDspModuleProperties";
                case 17:
                    return "isEnrolledForKeyphrase";
                case 18:
                    return "startRecognition";
                case 19:
                    return "stopRecognition";
                case 20:
                    return "getActiveServiceComponentName";
                case 21:
                    return "showSessionForActiveService";
                case 22:
                    return "hideCurrentSession";
                case 23:
                    return "launchVoiceAssistFromKeyguard";
                case 24:
                    return "isSessionRunning";
                case 25:
                    return "activeServiceSupportsAssist";
                case 26:
                    return "activeServiceSupportsLaunchFromKeyguard";
                case 27:
                    return "onLockscreenShown";
                case 28:
                    return "registerVoiceInteractionSessionListener";
                case 29:
                    return "getActiveServiceSupportedActions";
                case 30:
                    return "setUiHints";
                case 31:
                    return "requestDirectActions";
                case 32:
                    return "performDirectAction";
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
                boolean _arg1 = false;
                IVoiceInteractionService _arg0;
                Bundle _arg12;
                boolean _result;
                IBinder _arg02;
                Intent _arg13;
                int _result2;
                int _result3;
                int _arg14;
                IBinder _arg2;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        _arg0 = android.service.voice.IVoiceInteractionService.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg12 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        showSession(_arg0, _arg12, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        _result = deliverNewSession(data.readStrongBinder(), android.service.voice.IVoiceInteractionSession.Stub.asInterface(data.readStrongBinder()), com.android.internal.app.IVoiceInteractor.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg12 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        _result = showSessionFromSession(_arg02, _arg12, data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        boolean _result4 = hideSessionFromSession(data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg13 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        _result2 = startVoiceActivity(_arg02, _arg13, data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg13 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        _result2 = startAssistantActivity(_arg02, _arg13, data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        IBinder _arg03 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setKeepAwake(_arg03, _arg1);
                        reply.writeNoException();
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        closeSystemDialogs(data.readStrongBinder());
                        reply.writeNoException();
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        finish(data.readStrongBinder());
                        reply.writeNoException();
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        setDisabledShowContext(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        _result3 = getDisabledShowContext();
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        _result3 = getUserDisabledShowContext();
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        KeyphraseSoundModel _result5 = getKeyphraseSoundModel(data.readInt(), data.readString());
                        reply.writeNoException();
                        if (_result5 != null) {
                            parcel2.writeInt(1);
                            _result5.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 14:
                        KeyphraseSoundModel _arg04;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (KeyphraseSoundModel) KeyphraseSoundModel.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        int _result6 = updateKeyphraseSoundModel(_arg04);
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        int _result7 = deleteKeyphraseSoundModel(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        ModuleProperties _result8 = getDspModuleProperties(android.service.voice.IVoiceInteractionService.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        if (_result8 != null) {
                            parcel2.writeInt(1);
                            _result8.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        _result = isEnrolledForKeyphrase(android.service.voice.IVoiceInteractionService.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 18:
                        RecognitionConfig _arg4;
                        parcel.enforceInterface(descriptor);
                        IVoiceInteractionService _arg05 = android.service.voice.IVoiceInteractionService.Stub.asInterface(data.readStrongBinder());
                        _arg14 = data.readInt();
                        String _arg22 = data.readString();
                        IRecognitionStatusCallback _arg3 = android.hardware.soundtrigger.IRecognitionStatusCallback.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg4 = (RecognitionConfig) RecognitionConfig.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg4 = null;
                        }
                        _result3 = startRecognition(_arg05, _arg14, _arg22, _arg3, _arg4);
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        _result2 = stopRecognition(android.service.voice.IVoiceInteractionService.Stub.asInterface(data.readStrongBinder()), data.readInt(), android.hardware.soundtrigger.IRecognitionStatusCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        ComponentName _result9 = getActiveServiceComponentName();
                        reply.writeNoException();
                        if (_result9 != null) {
                            parcel2.writeInt(1);
                            _result9.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 21:
                        Bundle _arg06;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg06 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg06 = null;
                        }
                        boolean _result10 = showSessionForActiveService(_arg06, data.readInt(), com.android.internal.app.IVoiceInteractionSessionShowCallback.Stub.asInterface(data.readStrongBinder()), data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeInt(_result10);
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        hideCurrentSession();
                        reply.writeNoException();
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        launchVoiceAssistFromKeyguard();
                        reply.writeNoException();
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        _arg1 = isSessionRunning();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 25:
                        parcel.enforceInterface(descriptor);
                        _arg1 = activeServiceSupportsAssist();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 26:
                        parcel.enforceInterface(descriptor);
                        _arg1 = activeServiceSupportsLaunchFromKeyguard();
                        reply.writeNoException();
                        parcel2.writeInt(_arg1);
                        return true;
                    case 27:
                        parcel.enforceInterface(descriptor);
                        onLockscreenShown();
                        reply.writeNoException();
                        return true;
                    case 28:
                        parcel.enforceInterface(descriptor);
                        registerVoiceInteractionSessionListener(com.android.internal.app.IVoiceInteractionSessionListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 29:
                        parcel.enforceInterface(descriptor);
                        getActiveServiceSupportedActions(data.createStringArrayList(), com.android.internal.app.IVoiceActionCheckCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 30:
                        parcel.enforceInterface(descriptor);
                        _arg0 = android.service.voice.IVoiceInteractionService.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg12 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        setUiHints(_arg0, _arg12);
                        reply.writeNoException();
                        return true;
                    case 31:
                        RemoteCallback _arg32;
                        RemoteCallback _arg42;
                        parcel.enforceInterface(descriptor);
                        IBinder _arg07 = data.readStrongBinder();
                        _arg14 = data.readInt();
                        _arg2 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg32 = (RemoteCallback) RemoteCallback.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg32 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg42 = (RemoteCallback) RemoteCallback.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg42 = null;
                        }
                        requestDirectActions(_arg07, _arg14, _arg2, _arg32, _arg42);
                        reply.writeNoException();
                        return true;
                    case 32:
                        Bundle _arg23;
                        RemoteCallback _arg5;
                        RemoteCallback _arg6;
                        parcel.enforceInterface(descriptor);
                        _arg2 = data.readStrongBinder();
                        String _arg15 = data.readString();
                        if (data.readInt() != 0) {
                            _arg23 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg23 = null;
                        }
                        int _arg33 = data.readInt();
                        IBinder _arg43 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg5 = (RemoteCallback) RemoteCallback.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg5 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg6 = (RemoteCallback) RemoteCallback.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg6 = null;
                        }
                        performDirectAction(_arg2, _arg15, _arg23, _arg33, _arg43, _arg5, _arg6);
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IVoiceInteractionManagerService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IVoiceInteractionManagerService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    boolean activeServiceSupportsAssist() throws RemoteException;

    boolean activeServiceSupportsLaunchFromKeyguard() throws RemoteException;

    void closeSystemDialogs(IBinder iBinder) throws RemoteException;

    int deleteKeyphraseSoundModel(int i, String str) throws RemoteException;

    boolean deliverNewSession(IBinder iBinder, IVoiceInteractionSession iVoiceInteractionSession, IVoiceInteractor iVoiceInteractor) throws RemoteException;

    void finish(IBinder iBinder) throws RemoteException;

    ComponentName getActiveServiceComponentName() throws RemoteException;

    void getActiveServiceSupportedActions(List<String> list, IVoiceActionCheckCallback iVoiceActionCheckCallback) throws RemoteException;

    int getDisabledShowContext() throws RemoteException;

    ModuleProperties getDspModuleProperties(IVoiceInteractionService iVoiceInteractionService) throws RemoteException;

    @UnsupportedAppUsage
    KeyphraseSoundModel getKeyphraseSoundModel(int i, String str) throws RemoteException;

    int getUserDisabledShowContext() throws RemoteException;

    void hideCurrentSession() throws RemoteException;

    boolean hideSessionFromSession(IBinder iBinder) throws RemoteException;

    boolean isEnrolledForKeyphrase(IVoiceInteractionService iVoiceInteractionService, int i, String str) throws RemoteException;

    boolean isSessionRunning() throws RemoteException;

    void launchVoiceAssistFromKeyguard() throws RemoteException;

    void onLockscreenShown() throws RemoteException;

    void performDirectAction(IBinder iBinder, String str, Bundle bundle, int i, IBinder iBinder2, RemoteCallback remoteCallback, RemoteCallback remoteCallback2) throws RemoteException;

    void registerVoiceInteractionSessionListener(IVoiceInteractionSessionListener iVoiceInteractionSessionListener) throws RemoteException;

    void requestDirectActions(IBinder iBinder, int i, IBinder iBinder2, RemoteCallback remoteCallback, RemoteCallback remoteCallback2) throws RemoteException;

    void setDisabledShowContext(int i) throws RemoteException;

    void setKeepAwake(IBinder iBinder, boolean z) throws RemoteException;

    void setUiHints(IVoiceInteractionService iVoiceInteractionService, Bundle bundle) throws RemoteException;

    void showSession(IVoiceInteractionService iVoiceInteractionService, Bundle bundle, int i) throws RemoteException;

    boolean showSessionForActiveService(Bundle bundle, int i, IVoiceInteractionSessionShowCallback iVoiceInteractionSessionShowCallback, IBinder iBinder) throws RemoteException;

    boolean showSessionFromSession(IBinder iBinder, Bundle bundle, int i) throws RemoteException;

    int startAssistantActivity(IBinder iBinder, Intent intent, String str) throws RemoteException;

    int startRecognition(IVoiceInteractionService iVoiceInteractionService, int i, String str, IRecognitionStatusCallback iRecognitionStatusCallback, RecognitionConfig recognitionConfig) throws RemoteException;

    int startVoiceActivity(IBinder iBinder, Intent intent, String str) throws RemoteException;

    int stopRecognition(IVoiceInteractionService iVoiceInteractionService, int i, IRecognitionStatusCallback iRecognitionStatusCallback) throws RemoteException;

    int updateKeyphraseSoundModel(KeyphraseSoundModel keyphraseSoundModel) throws RemoteException;
}
