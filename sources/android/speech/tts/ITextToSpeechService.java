package android.speech.tts;

import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.text.TextUtils;
import java.util.List;

public interface ITextToSpeechService extends IInterface {

    public static class Default implements ITextToSpeechService {
        public int speak(IBinder callingInstance, CharSequence text, int queueMode, Bundle params, String utteranceId) throws RemoteException {
            return 0;
        }

        public int synthesizeToFileDescriptor(IBinder callingInstance, CharSequence text, ParcelFileDescriptor fileDescriptor, Bundle params, String utteranceId) throws RemoteException {
            return 0;
        }

        public int playAudio(IBinder callingInstance, Uri audioUri, int queueMode, Bundle params, String utteranceId) throws RemoteException {
            return 0;
        }

        public int playSilence(IBinder callingInstance, long duration, int queueMode, String utteranceId) throws RemoteException {
            return 0;
        }

        public boolean isSpeaking() throws RemoteException {
            return false;
        }

        public int stop(IBinder callingInstance) throws RemoteException {
            return 0;
        }

        public String[] getLanguage() throws RemoteException {
            return null;
        }

        public String[] getClientDefaultLanguage() throws RemoteException {
            return null;
        }

        public int isLanguageAvailable(String lang, String country, String variant) throws RemoteException {
            return 0;
        }

        public String[] getFeaturesForLanguage(String lang, String country, String variant) throws RemoteException {
            return null;
        }

        public int loadLanguage(IBinder caller, String lang, String country, String variant) throws RemoteException {
            return 0;
        }

        public void setCallback(IBinder caller, ITextToSpeechCallback cb) throws RemoteException {
        }

        public List<Voice> getVoices() throws RemoteException {
            return null;
        }

        public int loadVoice(IBinder caller, String voiceName) throws RemoteException {
            return 0;
        }

        public String getDefaultVoiceNameFor(String lang, String country, String variant) throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ITextToSpeechService {
        private static final String DESCRIPTOR = "android.speech.tts.ITextToSpeechService";
        static final int TRANSACTION_getClientDefaultLanguage = 8;
        static final int TRANSACTION_getDefaultVoiceNameFor = 15;
        static final int TRANSACTION_getFeaturesForLanguage = 10;
        static final int TRANSACTION_getLanguage = 7;
        static final int TRANSACTION_getVoices = 13;
        static final int TRANSACTION_isLanguageAvailable = 9;
        static final int TRANSACTION_isSpeaking = 5;
        static final int TRANSACTION_loadLanguage = 11;
        static final int TRANSACTION_loadVoice = 14;
        static final int TRANSACTION_playAudio = 3;
        static final int TRANSACTION_playSilence = 4;
        static final int TRANSACTION_setCallback = 12;
        static final int TRANSACTION_speak = 1;
        static final int TRANSACTION_stop = 6;
        static final int TRANSACTION_synthesizeToFileDescriptor = 2;

        private static class Proxy implements ITextToSpeechService {
            public static ITextToSpeechService sDefaultImpl;
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

            public int speak(IBinder callingInstance, CharSequence text, int queueMode, Bundle params, String utteranceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callingInstance);
                    int i = 0;
                    if (text != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(text, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(queueMode);
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(utteranceId);
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().speak(callingInstance, text, queueMode, params, utteranceId);
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

            public int synthesizeToFileDescriptor(IBinder callingInstance, CharSequence text, ParcelFileDescriptor fileDescriptor, Bundle params, String utteranceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callingInstance);
                    int i = 0;
                    if (text != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(text, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (fileDescriptor != null) {
                        _data.writeInt(1);
                        fileDescriptor.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(utteranceId);
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().synthesizeToFileDescriptor(callingInstance, text, fileDescriptor, params, utteranceId);
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

            public int playAudio(IBinder callingInstance, Uri audioUri, int queueMode, Bundle params, String utteranceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callingInstance);
                    int i = 0;
                    if (audioUri != null) {
                        _data.writeInt(1);
                        audioUri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(queueMode);
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(utteranceId);
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().playAudio(callingInstance, audioUri, queueMode, params, utteranceId);
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

            public int playSilence(IBinder callingInstance, long duration, int queueMode, String utteranceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callingInstance);
                    _data.writeLong(duration);
                    _data.writeInt(queueMode);
                    _data.writeString(utteranceId);
                    int i = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().playSilence(callingInstance, duration, queueMode, utteranceId);
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

            public boolean isSpeaking() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isSpeaking();
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

            public int stop(IBinder callingInstance) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callingInstance);
                    int i = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().stop(callingInstance);
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

            public String[] getLanguage() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String[] strArr = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getLanguage();
                            return strArr;
                        }
                    }
                    _reply.readException();
                    strArr = _reply.createStringArray();
                    String[] _result = strArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getClientDefaultLanguage() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String[] strArr = 8;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getClientDefaultLanguage();
                            return strArr;
                        }
                    }
                    _reply.readException();
                    strArr = _reply.createStringArray();
                    String[] _result = strArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int isLanguageAvailable(String lang, String country, String variant) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(lang);
                    _data.writeString(country);
                    _data.writeString(variant);
                    int i = 9;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().isLanguageAvailable(lang, country, variant);
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

            public String[] getFeaturesForLanguage(String lang, String country, String variant) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(lang);
                    _data.writeString(country);
                    _data.writeString(variant);
                    String[] strArr = 10;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getFeaturesForLanguage(lang, country, variant);
                            return strArr;
                        }
                    }
                    _reply.readException();
                    strArr = _reply.createStringArray();
                    String[] _result = strArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int loadLanguage(IBinder caller, String lang, String country, String variant) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller);
                    _data.writeString(lang);
                    _data.writeString(country);
                    _data.writeString(variant);
                    int i = 11;
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().loadLanguage(caller, lang, country, variant);
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

            public void setCallback(IBinder caller, ITextToSpeechCallback cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setCallback(caller, cb);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<Voice> getVoices() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<Voice> list = 13;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getVoices();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(Voice.CREATOR);
                    List<Voice> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int loadVoice(IBinder caller, String voiceName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller);
                    _data.writeString(voiceName);
                    int i = 14;
                    if (!this.mRemote.transact(14, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().loadVoice(caller, voiceName);
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

            public String getDefaultVoiceNameFor(String lang, String country, String variant) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(lang);
                    _data.writeString(country);
                    _data.writeString(variant);
                    String str = 15;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getDefaultVoiceNameFor(lang, country, variant);
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ITextToSpeechService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ITextToSpeechService)) {
                return new Proxy(obj);
            }
            return (ITextToSpeechService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "speak";
                case 2:
                    return "synthesizeToFileDescriptor";
                case 3:
                    return "playAudio";
                case 4:
                    return "playSilence";
                case 5:
                    return "isSpeaking";
                case 6:
                    return "stop";
                case 7:
                    return "getLanguage";
                case 8:
                    return "getClientDefaultLanguage";
                case 9:
                    return "isLanguageAvailable";
                case 10:
                    return "getFeaturesForLanguage";
                case 11:
                    return "loadLanguage";
                case 12:
                    return "setCallback";
                case 13:
                    return "getVoices";
                case 14:
                    return "loadVoice";
                case 15:
                    return "getDefaultVoiceNameFor";
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
                IBinder _arg0;
                CharSequence _arg1;
                int _arg2;
                Bundle _arg3;
                int _result;
                String[] _result2;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg1 = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
                        } else {
                            _arg1 = null;
                        }
                        _arg2 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg3 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        _result = speak(_arg0, _arg1, _arg2, _arg3, data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 2:
                        ParcelFileDescriptor _arg22;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg1 = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
                        } else {
                            _arg1 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg22 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg22 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg3 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        _result = synthesizeToFileDescriptor(_arg0, _arg1, _arg22, _arg3, data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 3:
                        Uri _arg12;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg12 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        _arg2 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg3 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        _result = playAudio(_arg0, _arg12, _arg2, _arg3, data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        _result = playSilence(data.readStrongBinder(), data.readLong(), data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        boolean _result3 = isSpeaking();
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        int _result4 = stop(data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        _result2 = getLanguage();
                        reply.writeNoException();
                        parcel2.writeStringArray(_result2);
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        _result2 = getClientDefaultLanguage();
                        reply.writeNoException();
                        parcel2.writeStringArray(_result2);
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        int _result5 = isLanguageAvailable(data.readString(), data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        String[] _result6 = getFeaturesForLanguage(data.readString(), data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeStringArray(_result6);
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        int _result7 = loadLanguage(data.readStrongBinder(), data.readString(), data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        setCallback(data.readStrongBinder(), android.speech.tts.ITextToSpeechCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        List<Voice> _result8 = getVoices();
                        reply.writeNoException();
                        parcel2.writeTypedList(_result8);
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        int _result9 = loadVoice(data.readStrongBinder(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result9);
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        String _result10 = getDefaultVoiceNameFor(data.readString(), data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_result10);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(ITextToSpeechService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ITextToSpeechService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    String[] getClientDefaultLanguage() throws RemoteException;

    String getDefaultVoiceNameFor(String str, String str2, String str3) throws RemoteException;

    String[] getFeaturesForLanguage(String str, String str2, String str3) throws RemoteException;

    String[] getLanguage() throws RemoteException;

    List<Voice> getVoices() throws RemoteException;

    int isLanguageAvailable(String str, String str2, String str3) throws RemoteException;

    boolean isSpeaking() throws RemoteException;

    int loadLanguage(IBinder iBinder, String str, String str2, String str3) throws RemoteException;

    int loadVoice(IBinder iBinder, String str) throws RemoteException;

    int playAudio(IBinder iBinder, Uri uri, int i, Bundle bundle, String str) throws RemoteException;

    int playSilence(IBinder iBinder, long j, int i, String str) throws RemoteException;

    void setCallback(IBinder iBinder, ITextToSpeechCallback iTextToSpeechCallback) throws RemoteException;

    int speak(IBinder iBinder, CharSequence charSequence, int i, Bundle bundle, String str) throws RemoteException;

    int stop(IBinder iBinder) throws RemoteException;

    int synthesizeToFileDescriptor(IBinder iBinder, CharSequence charSequence, ParcelFileDescriptor parcelFileDescriptor, Bundle bundle, String str) throws RemoteException;
}
