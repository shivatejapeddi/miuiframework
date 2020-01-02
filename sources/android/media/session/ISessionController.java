package android.media.session;

import android.app.PendingIntent;
import android.content.pm.ParceledListSlice;
import android.media.MediaMetadata;
import android.media.Rating;
import android.media.session.MediaController.PlaybackInfo;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.speech.tts.TextToSpeech.Engine;
import android.text.TextUtils;
import android.view.KeyEvent;

public interface ISessionController extends IInterface {

    public static class Default implements ISessionController {
        public void sendCommand(String packageName, ISessionControllerCallback caller, String command, Bundle args, ResultReceiver cb) throws RemoteException {
        }

        public boolean sendMediaButton(String packageName, ISessionControllerCallback caller, KeyEvent mediaButton) throws RemoteException {
            return false;
        }

        public void registerCallback(String packageName, ISessionControllerCallback cb) throws RemoteException {
        }

        public void unregisterCallback(ISessionControllerCallback cb) throws RemoteException {
        }

        public String getPackageName() throws RemoteException {
            return null;
        }

        public String getTag() throws RemoteException {
            return null;
        }

        public Bundle getSessionInfo() throws RemoteException {
            return null;
        }

        public PendingIntent getLaunchPendingIntent() throws RemoteException {
            return null;
        }

        public long getFlags() throws RemoteException {
            return 0;
        }

        public PlaybackInfo getVolumeAttributes() throws RemoteException {
            return null;
        }

        public void adjustVolume(String packageName, String opPackageName, ISessionControllerCallback caller, int direction, int flags) throws RemoteException {
        }

        public void setVolumeTo(String packageName, String opPackageName, ISessionControllerCallback caller, int value, int flags) throws RemoteException {
        }

        public void prepare(String packageName, ISessionControllerCallback caller) throws RemoteException {
        }

        public void prepareFromMediaId(String packageName, ISessionControllerCallback caller, String mediaId, Bundle extras) throws RemoteException {
        }

        public void prepareFromSearch(String packageName, ISessionControllerCallback caller, String string, Bundle extras) throws RemoteException {
        }

        public void prepareFromUri(String packageName, ISessionControllerCallback caller, Uri uri, Bundle extras) throws RemoteException {
        }

        public void play(String packageName, ISessionControllerCallback caller) throws RemoteException {
        }

        public void playFromMediaId(String packageName, ISessionControllerCallback caller, String mediaId, Bundle extras) throws RemoteException {
        }

        public void playFromSearch(String packageName, ISessionControllerCallback caller, String string, Bundle extras) throws RemoteException {
        }

        public void playFromUri(String packageName, ISessionControllerCallback caller, Uri uri, Bundle extras) throws RemoteException {
        }

        public void skipToQueueItem(String packageName, ISessionControllerCallback caller, long id) throws RemoteException {
        }

        public void pause(String packageName, ISessionControllerCallback caller) throws RemoteException {
        }

        public void stop(String packageName, ISessionControllerCallback caller) throws RemoteException {
        }

        public void next(String packageName, ISessionControllerCallback caller) throws RemoteException {
        }

        public void previous(String packageName, ISessionControllerCallback caller) throws RemoteException {
        }

        public void fastForward(String packageName, ISessionControllerCallback caller) throws RemoteException {
        }

        public void rewind(String packageName, ISessionControllerCallback caller) throws RemoteException {
        }

        public void seekTo(String packageName, ISessionControllerCallback caller, long pos) throws RemoteException {
        }

        public void rate(String packageName, ISessionControllerCallback caller, Rating rating) throws RemoteException {
        }

        public void setPlaybackSpeed(String packageName, ISessionControllerCallback caller, float speed) throws RemoteException {
        }

        public void sendCustomAction(String packageName, ISessionControllerCallback caller, String action, Bundle args) throws RemoteException {
        }

        public MediaMetadata getMetadata() throws RemoteException {
            return null;
        }

        public PlaybackState getPlaybackState() throws RemoteException {
            return null;
        }

        public ParceledListSlice getQueue() throws RemoteException {
            return null;
        }

        public CharSequence getQueueTitle() throws RemoteException {
            return null;
        }

        public Bundle getExtras() throws RemoteException {
            return null;
        }

        public int getRatingType() throws RemoteException {
            return 0;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ISessionController {
        private static final String DESCRIPTOR = "android.media.session.ISessionController";
        static final int TRANSACTION_adjustVolume = 11;
        static final int TRANSACTION_fastForward = 26;
        static final int TRANSACTION_getExtras = 36;
        static final int TRANSACTION_getFlags = 9;
        static final int TRANSACTION_getLaunchPendingIntent = 8;
        static final int TRANSACTION_getMetadata = 32;
        static final int TRANSACTION_getPackageName = 5;
        static final int TRANSACTION_getPlaybackState = 33;
        static final int TRANSACTION_getQueue = 34;
        static final int TRANSACTION_getQueueTitle = 35;
        static final int TRANSACTION_getRatingType = 37;
        static final int TRANSACTION_getSessionInfo = 7;
        static final int TRANSACTION_getTag = 6;
        static final int TRANSACTION_getVolumeAttributes = 10;
        static final int TRANSACTION_next = 24;
        static final int TRANSACTION_pause = 22;
        static final int TRANSACTION_play = 17;
        static final int TRANSACTION_playFromMediaId = 18;
        static final int TRANSACTION_playFromSearch = 19;
        static final int TRANSACTION_playFromUri = 20;
        static final int TRANSACTION_prepare = 13;
        static final int TRANSACTION_prepareFromMediaId = 14;
        static final int TRANSACTION_prepareFromSearch = 15;
        static final int TRANSACTION_prepareFromUri = 16;
        static final int TRANSACTION_previous = 25;
        static final int TRANSACTION_rate = 29;
        static final int TRANSACTION_registerCallback = 3;
        static final int TRANSACTION_rewind = 27;
        static final int TRANSACTION_seekTo = 28;
        static final int TRANSACTION_sendCommand = 1;
        static final int TRANSACTION_sendCustomAction = 31;
        static final int TRANSACTION_sendMediaButton = 2;
        static final int TRANSACTION_setPlaybackSpeed = 30;
        static final int TRANSACTION_setVolumeTo = 12;
        static final int TRANSACTION_skipToQueueItem = 21;
        static final int TRANSACTION_stop = 23;
        static final int TRANSACTION_unregisterCallback = 4;

        private static class Proxy implements ISessionController {
            public static ISessionController sDefaultImpl;
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

            public void sendCommand(String packageName, ISessionControllerCallback caller, String command, Bundle args, ResultReceiver cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeString(command);
                    if (args != null) {
                        _data.writeInt(1);
                        args.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (cb != null) {
                        _data.writeInt(1);
                        cb.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().sendCommand(packageName, caller, command, args, cb);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean sendMediaButton(String packageName, ISessionControllerCallback caller, KeyEvent mediaButton) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    boolean _result = true;
                    if (mediaButton != null) {
                        _data.writeInt(1);
                        mediaButton.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().sendMediaButton(packageName, caller, mediaButton);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerCallback(String packageName, ISessionControllerCallback cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerCallback(packageName, cb);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterCallback(ISessionControllerCallback cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterCallback(cb);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getPackageName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getPackageName();
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

            public String getTag() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getTag();
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

            public Bundle getSessionInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    Bundle bundle = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        bundle = Stub.getDefaultImpl();
                        if (bundle != 0) {
                            bundle = Stub.getDefaultImpl().getSessionInfo();
                            return bundle;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        bundle = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return bundle;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public PendingIntent getLaunchPendingIntent() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    PendingIntent pendingIntent = 8;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        pendingIntent = Stub.getDefaultImpl();
                        if (pendingIntent != 0) {
                            pendingIntent = Stub.getDefaultImpl().getLaunchPendingIntent();
                            return pendingIntent;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        pendingIntent = (PendingIntent) PendingIntent.CREATOR.createFromParcel(_reply);
                    } else {
                        pendingIntent = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return pendingIntent;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getFlags() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    long j = 9;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getFlags();
                            return j;
                        }
                    }
                    _reply.readException();
                    j = _reply.readLong();
                    long _result = j;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public PlaybackInfo getVolumeAttributes() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    PlaybackInfo playbackInfo = 10;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        playbackInfo = Stub.getDefaultImpl();
                        if (playbackInfo != 0) {
                            playbackInfo = Stub.getDefaultImpl().getVolumeAttributes();
                            return playbackInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        playbackInfo = (PlaybackInfo) PlaybackInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        playbackInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return playbackInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void adjustVolume(String packageName, String opPackageName, ISessionControllerCallback caller, int direction, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(opPackageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeInt(direction);
                    _data.writeInt(flags);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().adjustVolume(packageName, opPackageName, caller, direction, flags);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setVolumeTo(String packageName, String opPackageName, ISessionControllerCallback caller, int value, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(opPackageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeInt(value);
                    _data.writeInt(flags);
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setVolumeTo(packageName, opPackageName, caller, value, flags);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void prepare(String packageName, ISessionControllerCallback caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().prepare(packageName, caller);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void prepareFromMediaId(String packageName, ISessionControllerCallback caller, String mediaId, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeString(mediaId);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().prepareFromMediaId(packageName, caller, mediaId, extras);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void prepareFromSearch(String packageName, ISessionControllerCallback caller, String string, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeString(string);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(15, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().prepareFromSearch(packageName, caller, string, extras);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void prepareFromUri(String packageName, ISessionControllerCallback caller, Uri uri, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(16, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().prepareFromUri(packageName, caller, uri, extras);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void play(String packageName, ISessionControllerCallback caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    if (this.mRemote.transact(17, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().play(packageName, caller);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void playFromMediaId(String packageName, ISessionControllerCallback caller, String mediaId, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeString(mediaId);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().playFromMediaId(packageName, caller, mediaId, extras);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void playFromSearch(String packageName, ISessionControllerCallback caller, String string, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeString(string);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().playFromSearch(packageName, caller, string, extras);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void playFromUri(String packageName, ISessionControllerCallback caller, Uri uri, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(20, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().playFromUri(packageName, caller, uri, extras);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void skipToQueueItem(String packageName, ISessionControllerCallback caller, long id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeLong(id);
                    if (this.mRemote.transact(21, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().skipToQueueItem(packageName, caller, id);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void pause(String packageName, ISessionControllerCallback caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().pause(packageName, caller);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stop(String packageName, ISessionControllerCallback caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    if (this.mRemote.transact(23, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().stop(packageName, caller);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void next(String packageName, ISessionControllerCallback caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    if (this.mRemote.transact(24, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().next(packageName, caller);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void previous(String packageName, ISessionControllerCallback caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    if (this.mRemote.transact(25, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().previous(packageName, caller);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void fastForward(String packageName, ISessionControllerCallback caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    if (this.mRemote.transact(26, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().fastForward(packageName, caller);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void rewind(String packageName, ISessionControllerCallback caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    if (this.mRemote.transact(27, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().rewind(packageName, caller);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void seekTo(String packageName, ISessionControllerCallback caller, long pos) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeLong(pos);
                    if (this.mRemote.transact(28, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().seekTo(packageName, caller, pos);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void rate(String packageName, ISessionControllerCallback caller, Rating rating) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    if (rating != null) {
                        _data.writeInt(1);
                        rating.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(29, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().rate(packageName, caller, rating);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPlaybackSpeed(String packageName, ISessionControllerCallback caller, float speed) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeFloat(speed);
                    if (this.mRemote.transact(30, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPlaybackSpeed(packageName, caller, speed);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendCustomAction(String packageName, ISessionControllerCallback caller, String action, Bundle args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeString(action);
                    if (args != null) {
                        _data.writeInt(1);
                        args.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(31, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().sendCustomAction(packageName, caller, action, args);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public MediaMetadata getMetadata() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    MediaMetadata mediaMetadata = 32;
                    if (!this.mRemote.transact(32, _data, _reply, 0)) {
                        mediaMetadata = Stub.getDefaultImpl();
                        if (mediaMetadata != 0) {
                            mediaMetadata = Stub.getDefaultImpl().getMetadata();
                            return mediaMetadata;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        mediaMetadata = (MediaMetadata) MediaMetadata.CREATOR.createFromParcel(_reply);
                    } else {
                        mediaMetadata = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return mediaMetadata;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public PlaybackState getPlaybackState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    PlaybackState playbackState = 33;
                    if (!this.mRemote.transact(33, _data, _reply, 0)) {
                        playbackState = Stub.getDefaultImpl();
                        if (playbackState != 0) {
                            playbackState = Stub.getDefaultImpl().getPlaybackState();
                            return playbackState;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        playbackState = (PlaybackState) PlaybackState.CREATOR.createFromParcel(_reply);
                    } else {
                        playbackState = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return playbackState;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getQueue() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ParceledListSlice parceledListSlice = 34;
                    if (!this.mRemote.transact(34, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().getQueue();
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

            public CharSequence getQueueTitle() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    CharSequence charSequence = 35;
                    if (!this.mRemote.transact(35, _data, _reply, 0)) {
                        charSequence = Stub.getDefaultImpl();
                        if (charSequence != 0) {
                            charSequence = Stub.getDefaultImpl().getQueueTitle();
                            return charSequence;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        charSequence = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(_reply);
                    } else {
                        charSequence = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return charSequence;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bundle getExtras() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    Bundle bundle = 36;
                    if (!this.mRemote.transact(36, _data, _reply, 0)) {
                        bundle = Stub.getDefaultImpl();
                        if (bundle != 0) {
                            bundle = Stub.getDefaultImpl().getExtras();
                            return bundle;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        bundle = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return bundle;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getRatingType() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 37;
                    if (!this.mRemote.transact(37, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getRatingType();
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

        public static ISessionController asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ISessionController)) {
                return new Proxy(obj);
            }
            return (ISessionController) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "sendCommand";
                case 2:
                    return "sendMediaButton";
                case 3:
                    return "registerCallback";
                case 4:
                    return "unregisterCallback";
                case 5:
                    return "getPackageName";
                case 6:
                    return "getTag";
                case 7:
                    return "getSessionInfo";
                case 8:
                    return "getLaunchPendingIntent";
                case 9:
                    return "getFlags";
                case 10:
                    return "getVolumeAttributes";
                case 11:
                    return "adjustVolume";
                case 12:
                    return "setVolumeTo";
                case 13:
                    return "prepare";
                case 14:
                    return "prepareFromMediaId";
                case 15:
                    return "prepareFromSearch";
                case 16:
                    return "prepareFromUri";
                case 17:
                    return "play";
                case 18:
                    return "playFromMediaId";
                case 19:
                    return "playFromSearch";
                case 20:
                    return "playFromUri";
                case 21:
                    return "skipToQueueItem";
                case 22:
                    return "pause";
                case 23:
                    return "stop";
                case 24:
                    return "next";
                case 25:
                    return "previous";
                case 26:
                    return "fastForward";
                case 27:
                    return "rewind";
                case 28:
                    return "seekTo";
                case 29:
                    return Engine.KEY_PARAM_RATE;
                case 30:
                    return "setPlaybackSpeed";
                case 31:
                    return "sendCustomAction";
                case 32:
                    return "getMetadata";
                case 33:
                    return "getPlaybackState";
                case 34:
                    return "getQueue";
                case 35:
                    return "getQueueTitle";
                case 36:
                    return "getExtras";
                case 37:
                    return "getRatingType";
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
                String _arg0;
                ISessionControllerCallback _arg1;
                Bundle _result;
                String _arg2;
                Bundle _arg3;
                Uri _arg22;
                switch (i) {
                    case 1:
                        Bundle _arg32;
                        ResultReceiver _arg4;
                        parcel.enforceInterface(descriptor);
                        String _arg02 = data.readString();
                        ISessionControllerCallback _arg12 = android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                        String _arg23 = data.readString();
                        if (data.readInt() != 0) {
                            _arg32 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg32 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg4 = (ResultReceiver) ResultReceiver.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg4 = null;
                        }
                        sendCommand(_arg02, _arg12, _arg23, _arg32, _arg4);
                        reply.writeNoException();
                        return true;
                    case 2:
                        KeyEvent _arg24;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg1 = android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg24 = (KeyEvent) KeyEvent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg24 = null;
                        }
                        boolean _result2 = sendMediaButton(_arg0, _arg1, _arg24);
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        registerCallback(data.readString(), android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        unregisterCallback(android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        _arg0 = getPackageName();
                        reply.writeNoException();
                        parcel2.writeString(_arg0);
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        _arg0 = getTag();
                        reply.writeNoException();
                        parcel2.writeString(_arg0);
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        _result = getSessionInfo();
                        reply.writeNoException();
                        if (_result != null) {
                            parcel2.writeInt(1);
                            _result.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        PendingIntent _result3 = getLaunchPendingIntent();
                        reply.writeNoException();
                        if (_result3 != null) {
                            parcel2.writeInt(1);
                            _result3.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        long _result4 = getFlags();
                        reply.writeNoException();
                        parcel2.writeLong(_result4);
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        PlaybackInfo _result5 = getVolumeAttributes();
                        reply.writeNoException();
                        if (_result5 != null) {
                            parcel2.writeInt(1);
                            _result5.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        adjustVolume(data.readString(), data.readString(), android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        setVolumeTo(data.readString(), data.readString(), android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        prepare(data.readString(), android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg1 = android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                        _arg2 = data.readString();
                        if (data.readInt() != 0) {
                            _arg3 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        prepareFromMediaId(_arg0, _arg1, _arg2, _arg3);
                        reply.writeNoException();
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg1 = android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                        _arg2 = data.readString();
                        if (data.readInt() != 0) {
                            _arg3 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        prepareFromSearch(_arg0, _arg1, _arg2, _arg3);
                        reply.writeNoException();
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg1 = android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg22 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg22 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg3 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        prepareFromUri(_arg0, _arg1, _arg22, _arg3);
                        reply.writeNoException();
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        play(data.readString(), android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg1 = android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                        _arg2 = data.readString();
                        if (data.readInt() != 0) {
                            _arg3 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        playFromMediaId(_arg0, _arg1, _arg2, _arg3);
                        reply.writeNoException();
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg1 = android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                        _arg2 = data.readString();
                        if (data.readInt() != 0) {
                            _arg3 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        playFromSearch(_arg0, _arg1, _arg2, _arg3);
                        reply.writeNoException();
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg1 = android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg22 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg22 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg3 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        playFromUri(_arg0, _arg1, _arg22, _arg3);
                        reply.writeNoException();
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        skipToQueueItem(data.readString(), android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder()), data.readLong());
                        reply.writeNoException();
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        pause(data.readString(), android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        stop(data.readString(), android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        next(data.readString(), android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 25:
                        parcel.enforceInterface(descriptor);
                        previous(data.readString(), android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 26:
                        parcel.enforceInterface(descriptor);
                        fastForward(data.readString(), android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 27:
                        parcel.enforceInterface(descriptor);
                        rewind(data.readString(), android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 28:
                        parcel.enforceInterface(descriptor);
                        seekTo(data.readString(), android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder()), data.readLong());
                        reply.writeNoException();
                        return true;
                    case 29:
                        Rating _arg25;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg1 = android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg25 = (Rating) Rating.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg25 = null;
                        }
                        rate(_arg0, _arg1, _arg25);
                        reply.writeNoException();
                        return true;
                    case 30:
                        parcel.enforceInterface(descriptor);
                        setPlaybackSpeed(data.readString(), android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder()), data.readFloat());
                        reply.writeNoException();
                        return true;
                    case 31:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg1 = android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                        _arg2 = data.readString();
                        if (data.readInt() != 0) {
                            _arg3 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        sendCustomAction(_arg0, _arg1, _arg2, _arg3);
                        reply.writeNoException();
                        return true;
                    case 32:
                        parcel.enforceInterface(descriptor);
                        MediaMetadata _result6 = getMetadata();
                        reply.writeNoException();
                        if (_result6 != null) {
                            parcel2.writeInt(1);
                            _result6.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 33:
                        parcel.enforceInterface(descriptor);
                        PlaybackState _result7 = getPlaybackState();
                        reply.writeNoException();
                        if (_result7 != null) {
                            parcel2.writeInt(1);
                            _result7.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 34:
                        parcel.enforceInterface(descriptor);
                        ParceledListSlice _result8 = getQueue();
                        reply.writeNoException();
                        if (_result8 != null) {
                            parcel2.writeInt(1);
                            _result8.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 35:
                        parcel.enforceInterface(descriptor);
                        CharSequence _result9 = getQueueTitle();
                        reply.writeNoException();
                        if (_result9 != null) {
                            parcel2.writeInt(1);
                            TextUtils.writeToParcel(_result9, parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 36:
                        parcel.enforceInterface(descriptor);
                        _result = getExtras();
                        reply.writeNoException();
                        if (_result != null) {
                            parcel2.writeInt(1);
                            _result.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 37:
                        parcel.enforceInterface(descriptor);
                        int _result10 = getRatingType();
                        reply.writeNoException();
                        parcel2.writeInt(_result10);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(ISessionController impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ISessionController getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void adjustVolume(String str, String str2, ISessionControllerCallback iSessionControllerCallback, int i, int i2) throws RemoteException;

    void fastForward(String str, ISessionControllerCallback iSessionControllerCallback) throws RemoteException;

    Bundle getExtras() throws RemoteException;

    long getFlags() throws RemoteException;

    PendingIntent getLaunchPendingIntent() throws RemoteException;

    MediaMetadata getMetadata() throws RemoteException;

    String getPackageName() throws RemoteException;

    PlaybackState getPlaybackState() throws RemoteException;

    ParceledListSlice getQueue() throws RemoteException;

    CharSequence getQueueTitle() throws RemoteException;

    int getRatingType() throws RemoteException;

    Bundle getSessionInfo() throws RemoteException;

    String getTag() throws RemoteException;

    PlaybackInfo getVolumeAttributes() throws RemoteException;

    void next(String str, ISessionControllerCallback iSessionControllerCallback) throws RemoteException;

    void pause(String str, ISessionControllerCallback iSessionControllerCallback) throws RemoteException;

    void play(String str, ISessionControllerCallback iSessionControllerCallback) throws RemoteException;

    void playFromMediaId(String str, ISessionControllerCallback iSessionControllerCallback, String str2, Bundle bundle) throws RemoteException;

    void playFromSearch(String str, ISessionControllerCallback iSessionControllerCallback, String str2, Bundle bundle) throws RemoteException;

    void playFromUri(String str, ISessionControllerCallback iSessionControllerCallback, Uri uri, Bundle bundle) throws RemoteException;

    void prepare(String str, ISessionControllerCallback iSessionControllerCallback) throws RemoteException;

    void prepareFromMediaId(String str, ISessionControllerCallback iSessionControllerCallback, String str2, Bundle bundle) throws RemoteException;

    void prepareFromSearch(String str, ISessionControllerCallback iSessionControllerCallback, String str2, Bundle bundle) throws RemoteException;

    void prepareFromUri(String str, ISessionControllerCallback iSessionControllerCallback, Uri uri, Bundle bundle) throws RemoteException;

    void previous(String str, ISessionControllerCallback iSessionControllerCallback) throws RemoteException;

    void rate(String str, ISessionControllerCallback iSessionControllerCallback, Rating rating) throws RemoteException;

    void registerCallback(String str, ISessionControllerCallback iSessionControllerCallback) throws RemoteException;

    void rewind(String str, ISessionControllerCallback iSessionControllerCallback) throws RemoteException;

    void seekTo(String str, ISessionControllerCallback iSessionControllerCallback, long j) throws RemoteException;

    void sendCommand(String str, ISessionControllerCallback iSessionControllerCallback, String str2, Bundle bundle, ResultReceiver resultReceiver) throws RemoteException;

    void sendCustomAction(String str, ISessionControllerCallback iSessionControllerCallback, String str2, Bundle bundle) throws RemoteException;

    boolean sendMediaButton(String str, ISessionControllerCallback iSessionControllerCallback, KeyEvent keyEvent) throws RemoteException;

    void setPlaybackSpeed(String str, ISessionControllerCallback iSessionControllerCallback, float f) throws RemoteException;

    void setVolumeTo(String str, String str2, ISessionControllerCallback iSessionControllerCallback, int i, int i2) throws RemoteException;

    void skipToQueueItem(String str, ISessionControllerCallback iSessionControllerCallback, long j) throws RemoteException;

    void stop(String str, ISessionControllerCallback iSessionControllerCallback) throws RemoteException;

    void unregisterCallback(ISessionControllerCallback iSessionControllerCallback) throws RemoteException;
}
