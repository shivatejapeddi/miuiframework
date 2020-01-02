package com.android.internal.app;

import android.content.ComponentName;
import android.hardware.soundtrigger.IRecognitionStatusCallback;
import android.hardware.soundtrigger.SoundTrigger.GenericSoundModel;
import android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel;
import android.hardware.soundtrigger.SoundTrigger.RecognitionConfig;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.RemoteException;

public interface ISoundTriggerService extends IInterface {

    public static class Default implements ISoundTriggerService {
        public GenericSoundModel getSoundModel(ParcelUuid soundModelId) throws RemoteException {
            return null;
        }

        public void updateSoundModel(GenericSoundModel soundModel) throws RemoteException {
        }

        public void deleteSoundModel(ParcelUuid soundModelId) throws RemoteException {
        }

        public int startRecognition(ParcelUuid soundModelId, IRecognitionStatusCallback callback, RecognitionConfig config) throws RemoteException {
            return 0;
        }

        public int stopRecognition(ParcelUuid soundModelId, IRecognitionStatusCallback callback) throws RemoteException {
            return 0;
        }

        public int loadGenericSoundModel(GenericSoundModel soundModel) throws RemoteException {
            return 0;
        }

        public int loadKeyphraseSoundModel(KeyphraseSoundModel soundModel) throws RemoteException {
            return 0;
        }

        public int startRecognitionForService(ParcelUuid soundModelId, Bundle params, ComponentName callbackIntent, RecognitionConfig config) throws RemoteException {
            return 0;
        }

        public int stopRecognitionForService(ParcelUuid soundModelId) throws RemoteException {
            return 0;
        }

        public int unloadSoundModel(ParcelUuid soundModelId) throws RemoteException {
            return 0;
        }

        public boolean isRecognitionActive(ParcelUuid parcelUuid) throws RemoteException {
            return false;
        }

        public int getModelState(ParcelUuid soundModelId) throws RemoteException {
            return 0;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ISoundTriggerService {
        private static final String DESCRIPTOR = "com.android.internal.app.ISoundTriggerService";
        static final int TRANSACTION_deleteSoundModel = 3;
        static final int TRANSACTION_getModelState = 12;
        static final int TRANSACTION_getSoundModel = 1;
        static final int TRANSACTION_isRecognitionActive = 11;
        static final int TRANSACTION_loadGenericSoundModel = 6;
        static final int TRANSACTION_loadKeyphraseSoundModel = 7;
        static final int TRANSACTION_startRecognition = 4;
        static final int TRANSACTION_startRecognitionForService = 8;
        static final int TRANSACTION_stopRecognition = 5;
        static final int TRANSACTION_stopRecognitionForService = 9;
        static final int TRANSACTION_unloadSoundModel = 10;
        static final int TRANSACTION_updateSoundModel = 2;

        private static class Proxy implements ISoundTriggerService {
            public static ISoundTriggerService sDefaultImpl;
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

            public GenericSoundModel getSoundModel(ParcelUuid soundModelId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    GenericSoundModel genericSoundModel = 0;
                    if (soundModelId != null) {
                        _data.writeInt(1);
                        soundModelId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        genericSoundModel = Stub.getDefaultImpl();
                        if (genericSoundModel != 0) {
                            genericSoundModel = Stub.getDefaultImpl().getSoundModel(soundModelId);
                            return genericSoundModel;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        genericSoundModel = (GenericSoundModel) GenericSoundModel.CREATOR.createFromParcel(_reply);
                    } else {
                        genericSoundModel = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return genericSoundModel;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateSoundModel(GenericSoundModel soundModel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (soundModel != null) {
                        _data.writeInt(1);
                        soundModel.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateSoundModel(soundModel);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void deleteSoundModel(ParcelUuid soundModelId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (soundModelId != null) {
                        _data.writeInt(1);
                        soundModelId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().deleteSoundModel(soundModelId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int startRecognition(ParcelUuid soundModelId, IRecognitionStatusCallback callback, RecognitionConfig config) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    if (soundModelId != null) {
                        _data.writeInt(1);
                        soundModelId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (config != null) {
                        _data.writeInt(1);
                        config.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().startRecognition(soundModelId, callback, config);
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

            public int stopRecognition(ParcelUuid soundModelId, IRecognitionStatusCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (soundModelId != null) {
                        _data.writeInt(1);
                        soundModelId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    int i = this.mRemote;
                    if (!i.transact(5, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().stopRecognition(soundModelId, callback);
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

            public int loadGenericSoundModel(GenericSoundModel soundModel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (soundModel != null) {
                        _data.writeInt(1);
                        soundModel.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(6, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().loadGenericSoundModel(soundModel);
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

            public int loadKeyphraseSoundModel(KeyphraseSoundModel soundModel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (soundModel != null) {
                        _data.writeInt(1);
                        soundModel.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(7, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().loadKeyphraseSoundModel(soundModel);
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

            public int startRecognitionForService(ParcelUuid soundModelId, Bundle params, ComponentName callbackIntent, RecognitionConfig config) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    if (soundModelId != null) {
                        _data.writeInt(1);
                        soundModelId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (callbackIntent != null) {
                        _data.writeInt(1);
                        callbackIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (config != null) {
                        _data.writeInt(1);
                        config.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().startRecognitionForService(soundModelId, params, callbackIntent, config);
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

            public int stopRecognitionForService(ParcelUuid soundModelId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (soundModelId != null) {
                        _data.writeInt(1);
                        soundModelId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(9, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().stopRecognitionForService(soundModelId);
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

            public int unloadSoundModel(ParcelUuid soundModelId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (soundModelId != null) {
                        _data.writeInt(1);
                        soundModelId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(10, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().unloadSoundModel(soundModelId);
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

            public boolean isRecognitionActive(ParcelUuid parcelUuid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (parcelUuid != null) {
                        _data.writeInt(1);
                        parcelUuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isRecognitionActive(parcelUuid);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getModelState(ParcelUuid soundModelId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (soundModelId != null) {
                        _data.writeInt(1);
                        soundModelId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(12, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getModelState(soundModelId);
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISoundTriggerService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ISoundTriggerService)) {
                return new Proxy(obj);
            }
            return (ISoundTriggerService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getSoundModel";
                case 2:
                    return "updateSoundModel";
                case 3:
                    return "deleteSoundModel";
                case 4:
                    return "startRecognition";
                case 5:
                    return "stopRecognition";
                case 6:
                    return "loadGenericSoundModel";
                case 7:
                    return "loadKeyphraseSoundModel";
                case 8:
                    return "startRecognitionForService";
                case 9:
                    return "stopRecognitionForService";
                case 10:
                    return "unloadSoundModel";
                case 11:
                    return "isRecognitionActive";
                case 12:
                    return "getModelState";
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
                ParcelUuid _arg0;
                GenericSoundModel _arg02;
                int _result;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        GenericSoundModel _result2 = getSoundModel(_arg0);
                        reply.writeNoException();
                        if (_result2 != null) {
                            reply.writeInt(1);
                            _result2.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (GenericSoundModel) GenericSoundModel.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        updateSoundModel(_arg02);
                        reply.writeNoException();
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        deleteSoundModel(_arg0);
                        reply.writeNoException();
                        return true;
                    case 4:
                        RecognitionConfig _arg2;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        IRecognitionStatusCallback _arg1 = android.hardware.soundtrigger.IRecognitionStatusCallback.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg2 = (RecognitionConfig) RecognitionConfig.CREATOR.createFromParcel(data);
                        } else {
                            _arg2 = null;
                        }
                        int _result3 = startRecognition(_arg0, _arg1, _arg2);
                        reply.writeNoException();
                        reply.writeInt(_result3);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        int _result4 = stopRecognition(_arg0, android.hardware.soundtrigger.IRecognitionStatusCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (GenericSoundModel) GenericSoundModel.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        _result = loadGenericSoundModel(_arg02);
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 7:
                        KeyphraseSoundModel _arg03;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (KeyphraseSoundModel) KeyphraseSoundModel.CREATOR.createFromParcel(data);
                        } else {
                            _arg03 = null;
                        }
                        _result = loadKeyphraseSoundModel(_arg03);
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 8:
                        Bundle _arg12;
                        ComponentName _arg22;
                        RecognitionConfig _arg3;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg12 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                        } else {
                            _arg12 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg22 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                        } else {
                            _arg22 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg3 = (RecognitionConfig) RecognitionConfig.CREATOR.createFromParcel(data);
                        } else {
                            _arg3 = null;
                        }
                        int _result5 = startRecognitionForService(_arg0, _arg12, _arg22, _arg3);
                        reply.writeNoException();
                        reply.writeInt(_result5);
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        _result = stopRecognitionForService(_arg0);
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        _result = unloadSoundModel(_arg0);
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        boolean _result6 = isRecognitionActive(_arg0);
                        reply.writeNoException();
                        reply.writeInt(_result6);
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        _result = getModelState(_arg0);
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(ISoundTriggerService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ISoundTriggerService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void deleteSoundModel(ParcelUuid parcelUuid) throws RemoteException;

    int getModelState(ParcelUuid parcelUuid) throws RemoteException;

    GenericSoundModel getSoundModel(ParcelUuid parcelUuid) throws RemoteException;

    boolean isRecognitionActive(ParcelUuid parcelUuid) throws RemoteException;

    int loadGenericSoundModel(GenericSoundModel genericSoundModel) throws RemoteException;

    int loadKeyphraseSoundModel(KeyphraseSoundModel keyphraseSoundModel) throws RemoteException;

    int startRecognition(ParcelUuid parcelUuid, IRecognitionStatusCallback iRecognitionStatusCallback, RecognitionConfig recognitionConfig) throws RemoteException;

    int startRecognitionForService(ParcelUuid parcelUuid, Bundle bundle, ComponentName componentName, RecognitionConfig recognitionConfig) throws RemoteException;

    int stopRecognition(ParcelUuid parcelUuid, IRecognitionStatusCallback iRecognitionStatusCallback) throws RemoteException;

    int stopRecognitionForService(ParcelUuid parcelUuid) throws RemoteException;

    int unloadSoundModel(ParcelUuid parcelUuid) throws RemoteException;

    void updateSoundModel(GenericSoundModel genericSoundModel) throws RemoteException;
}
