package android.media;

import android.media.VolumeShaper.Configuration;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.UserHandle;

public interface IRingtonePlayer extends IInterface {

    public static class Default implements IRingtonePlayer {
        public void play(IBinder token, Uri uri, AudioAttributes aa, float volume, boolean looping) throws RemoteException {
        }

        public void playWithVolumeShaping(IBinder token, Uri uri, AudioAttributes aa, float volume, boolean looping, Configuration volumeShaperConfig) throws RemoteException {
        }

        public void stop(IBinder token) throws RemoteException {
        }

        public boolean isPlaying(IBinder token) throws RemoteException {
            return false;
        }

        public void setPlaybackProperties(IBinder token, float volume, boolean looping) throws RemoteException {
        }

        public void playAsync(Uri uri, UserHandle user, boolean looping, AudioAttributes aa) throws RemoteException {
        }

        public void stopAsync() throws RemoteException {
        }

        public String getTitle(Uri uri) throws RemoteException {
            return null;
        }

        public ParcelFileDescriptor openRingtone(Uri uri) throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IRingtonePlayer {
        private static final String DESCRIPTOR = "android.media.IRingtonePlayer";
        static final int TRANSACTION_getTitle = 8;
        static final int TRANSACTION_isPlaying = 4;
        static final int TRANSACTION_openRingtone = 9;
        static final int TRANSACTION_play = 1;
        static final int TRANSACTION_playAsync = 6;
        static final int TRANSACTION_playWithVolumeShaping = 2;
        static final int TRANSACTION_setPlaybackProperties = 5;
        static final int TRANSACTION_stop = 3;
        static final int TRANSACTION_stopAsync = 7;

        private static class Proxy implements IRingtonePlayer {
            public static IRingtonePlayer sDefaultImpl;
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

            public void play(IBinder token, Uri uri, AudioAttributes aa, float volume, boolean looping) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    int i = 0;
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (aa != null) {
                        _data.writeInt(1);
                        aa.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeFloat(volume);
                    if (looping) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().play(token, uri, aa, volume, looping);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void playWithVolumeShaping(IBinder token, Uri uri, AudioAttributes aa, float volume, boolean looping, Configuration volumeShaperConfig) throws RemoteException {
                Throwable th;
                float f;
                Uri uri2 = uri;
                AudioAttributes audioAttributes = aa;
                Configuration configuration = volumeShaperConfig;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeStrongBinder(token);
                        if (uri2 != null) {
                            _data.writeInt(1);
                            uri2.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (audioAttributes != null) {
                            _data.writeInt(1);
                            audioAttributes.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        f = volume;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeFloat(volume);
                        _data.writeInt(looping ? 1 : 0);
                        if (configuration != null) {
                            _data.writeInt(1);
                            configuration.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().playWithVolumeShaping(token, uri, aa, volume, looping, volumeShaperConfig);
                        _data.recycle();
                    } catch (Throwable th4) {
                        th = th4;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    IBinder iBinder = token;
                    f = volume;
                    _data.recycle();
                    throw th;
                }
            }

            public void stop(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().stop(token);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public boolean isPlaying(IBinder token) throws RemoteException {
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
                            z = Stub.getDefaultImpl().isPlaying(token);
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

            public void setPlaybackProperties(IBinder token, float volume, boolean looping) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeFloat(volume);
                    _data.writeInt(looping ? 1 : 0);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setPlaybackProperties(token, volume, looping);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void playAsync(Uri uri, UserHandle user, boolean looping, AudioAttributes aa) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(looping ? 1 : 0);
                    if (aa != null) {
                        _data.writeInt(1);
                        aa.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().playAsync(uri, user, looping, aa);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void stopAsync() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(7, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().stopAsync();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public String getTitle(Uri uri) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    String str = this.mRemote;
                    if (!str.transact(8, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != null) {
                            str = Stub.getDefaultImpl().getTitle(uri);
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

            public ParcelFileDescriptor openRingtone(Uri uri) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    ParcelFileDescriptor parcelFileDescriptor = this.mRemote;
                    if (!parcelFileDescriptor.transact(9, _data, _reply, 0)) {
                        parcelFileDescriptor = Stub.getDefaultImpl();
                        if (parcelFileDescriptor != null) {
                            parcelFileDescriptor = Stub.getDefaultImpl().openRingtone(uri);
                            return parcelFileDescriptor;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parcelFileDescriptor = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(_reply);
                    } else {
                        parcelFileDescriptor = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parcelFileDescriptor;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IRingtonePlayer asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IRingtonePlayer)) {
                return new Proxy(obj);
            }
            return (IRingtonePlayer) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "play";
                case 2:
                    return "playWithVolumeShaping";
                case 3:
                    return "stop";
                case 4:
                    return "isPlaying";
                case 5:
                    return "setPlaybackProperties";
                case 6:
                    return "playAsync";
                case 7:
                    return "stopAsync";
                case 8:
                    return "getTitle";
                case 9:
                    return "openRingtone";
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
                boolean _arg2 = false;
                Uri _arg0;
                switch (i) {
                    case 1:
                        Uri _arg1;
                        AudioAttributes _arg22;
                        parcel.enforceInterface(descriptor);
                        IBinder _arg02 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg1 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg1 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg22 = (AudioAttributes) AudioAttributes.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg22 = null;
                        }
                        play(_arg02, _arg1, _arg22, data.readFloat(), data.readInt() != 0);
                        return true;
                    case 2:
                        Uri _arg12;
                        AudioAttributes _arg23;
                        Configuration _arg5;
                        parcel.enforceInterface(descriptor);
                        IBinder _arg03 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg12 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg23 = (AudioAttributes) AudioAttributes.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg23 = null;
                        }
                        float _arg3 = data.readFloat();
                        boolean _arg4 = data.readInt() != 0;
                        if (data.readInt() != 0) {
                            _arg5 = (Configuration) Configuration.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg5 = null;
                        }
                        playWithVolumeShaping(_arg03, _arg12, _arg23, _arg3, _arg4, _arg5);
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        stop(data.readStrongBinder());
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        boolean _result = isPlaying(data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        IBinder _arg04 = data.readStrongBinder();
                        float _arg13 = data.readFloat();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        setPlaybackProperties(_arg04, _arg13, _arg2);
                        return true;
                    case 6:
                        UserHandle _arg14;
                        AudioAttributes _arg32;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg14 = (UserHandle) UserHandle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg14 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        if (data.readInt() != 0) {
                            _arg32 = (AudioAttributes) AudioAttributes.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg32 = null;
                        }
                        playAsync(_arg0, _arg14, _arg2, _arg32);
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        stopAsync();
                        return true;
                    case 8:
                        Uri _arg05;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg05 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg05 = null;
                        }
                        String _result2 = getTitle(_arg05);
                        reply.writeNoException();
                        parcel2.writeString(_result2);
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        ParcelFileDescriptor _result3 = openRingtone(_arg0);
                        reply.writeNoException();
                        if (_result3 != null) {
                            parcel2.writeInt(1);
                            _result3.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IRingtonePlayer impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IRingtonePlayer getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    String getTitle(Uri uri) throws RemoteException;

    boolean isPlaying(IBinder iBinder) throws RemoteException;

    ParcelFileDescriptor openRingtone(Uri uri) throws RemoteException;

    void play(IBinder iBinder, Uri uri, AudioAttributes audioAttributes, float f, boolean z) throws RemoteException;

    void playAsync(Uri uri, UserHandle userHandle, boolean z, AudioAttributes audioAttributes) throws RemoteException;

    void playWithVolumeShaping(IBinder iBinder, Uri uri, AudioAttributes audioAttributes, float f, boolean z, Configuration configuration) throws RemoteException;

    void setPlaybackProperties(IBinder iBinder, float f, boolean z) throws RemoteException;

    void stop(IBinder iBinder) throws RemoteException;

    void stopAsync() throws RemoteException;
}
