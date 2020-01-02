package android.media.session;

import android.content.Intent;
import android.media.Rating;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ResultReceiver;

public interface ISessionCallback extends IInterface {

    public static class Default implements ISessionCallback {
        public void onCommand(String packageName, int pid, int uid, ISessionControllerCallback caller, String command, Bundle args, ResultReceiver cb) throws RemoteException {
        }

        public void onMediaButton(String packageName, int pid, int uid, Intent mediaButtonIntent, int sequenceNumber, ResultReceiver cb) throws RemoteException {
        }

        public void onMediaButtonFromController(String packageName, int pid, int uid, ISessionControllerCallback caller, Intent mediaButtonIntent) throws RemoteException {
        }

        public void onPrepare(String packageName, int pid, int uid, ISessionControllerCallback caller) throws RemoteException {
        }

        public void onPrepareFromMediaId(String packageName, int pid, int uid, ISessionControllerCallback caller, String mediaId, Bundle extras) throws RemoteException {
        }

        public void onPrepareFromSearch(String packageName, int pid, int uid, ISessionControllerCallback caller, String query, Bundle extras) throws RemoteException {
        }

        public void onPrepareFromUri(String packageName, int pid, int uid, ISessionControllerCallback caller, Uri uri, Bundle extras) throws RemoteException {
        }

        public void onPlay(String packageName, int pid, int uid, ISessionControllerCallback caller) throws RemoteException {
        }

        public void onPlayFromMediaId(String packageName, int pid, int uid, ISessionControllerCallback caller, String mediaId, Bundle extras) throws RemoteException {
        }

        public void onPlayFromSearch(String packageName, int pid, int uid, ISessionControllerCallback caller, String query, Bundle extras) throws RemoteException {
        }

        public void onPlayFromUri(String packageName, int pid, int uid, ISessionControllerCallback caller, Uri uri, Bundle extras) throws RemoteException {
        }

        public void onSkipToTrack(String packageName, int pid, int uid, ISessionControllerCallback caller, long id) throws RemoteException {
        }

        public void onPause(String packageName, int pid, int uid, ISessionControllerCallback caller) throws RemoteException {
        }

        public void onStop(String packageName, int pid, int uid, ISessionControllerCallback caller) throws RemoteException {
        }

        public void onNext(String packageName, int pid, int uid, ISessionControllerCallback caller) throws RemoteException {
        }

        public void onPrevious(String packageName, int pid, int uid, ISessionControllerCallback caller) throws RemoteException {
        }

        public void onFastForward(String packageName, int pid, int uid, ISessionControllerCallback caller) throws RemoteException {
        }

        public void onRewind(String packageName, int pid, int uid, ISessionControllerCallback caller) throws RemoteException {
        }

        public void onSeekTo(String packageName, int pid, int uid, ISessionControllerCallback caller, long pos) throws RemoteException {
        }

        public void onRate(String packageName, int pid, int uid, ISessionControllerCallback caller, Rating rating) throws RemoteException {
        }

        public void onSetPlaybackSpeed(String packageName, int pid, int uid, ISessionControllerCallback caller, float speed) throws RemoteException {
        }

        public void onCustomAction(String packageName, int pid, int uid, ISessionControllerCallback caller, String action, Bundle args) throws RemoteException {
        }

        public void onAdjustVolume(String packageName, int pid, int uid, ISessionControllerCallback caller, int direction) throws RemoteException {
        }

        public void onSetVolumeTo(String packageName, int pid, int uid, ISessionControllerCallback caller, int value) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ISessionCallback {
        private static final String DESCRIPTOR = "android.media.session.ISessionCallback";
        static final int TRANSACTION_onAdjustVolume = 23;
        static final int TRANSACTION_onCommand = 1;
        static final int TRANSACTION_onCustomAction = 22;
        static final int TRANSACTION_onFastForward = 17;
        static final int TRANSACTION_onMediaButton = 2;
        static final int TRANSACTION_onMediaButtonFromController = 3;
        static final int TRANSACTION_onNext = 15;
        static final int TRANSACTION_onPause = 13;
        static final int TRANSACTION_onPlay = 8;
        static final int TRANSACTION_onPlayFromMediaId = 9;
        static final int TRANSACTION_onPlayFromSearch = 10;
        static final int TRANSACTION_onPlayFromUri = 11;
        static final int TRANSACTION_onPrepare = 4;
        static final int TRANSACTION_onPrepareFromMediaId = 5;
        static final int TRANSACTION_onPrepareFromSearch = 6;
        static final int TRANSACTION_onPrepareFromUri = 7;
        static final int TRANSACTION_onPrevious = 16;
        static final int TRANSACTION_onRate = 20;
        static final int TRANSACTION_onRewind = 18;
        static final int TRANSACTION_onSeekTo = 19;
        static final int TRANSACTION_onSetPlaybackSpeed = 21;
        static final int TRANSACTION_onSetVolumeTo = 24;
        static final int TRANSACTION_onSkipToTrack = 12;
        static final int TRANSACTION_onStop = 14;

        private static class Proxy implements ISessionCallback {
            public static ISessionCallback sDefaultImpl;
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

            public void onCommand(String packageName, int pid, int uid, ISessionControllerCallback caller, String command, Bundle args, ResultReceiver cb) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                String str;
                Bundle bundle = args;
                ResultReceiver resultReceiver = cb;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(packageName);
                    } catch (Throwable th2) {
                        th = th2;
                        i = pid;
                        i2 = uid;
                        str = command;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(pid);
                    } catch (Throwable th3) {
                        th = th3;
                        i2 = uid;
                        str = command;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(uid);
                        _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                        try {
                            _data.writeString(command);
                            if (bundle != null) {
                                _data.writeInt(1);
                                bundle.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            if (resultReceiver != null) {
                                _data.writeInt(1);
                                resultReceiver.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().onCommand(packageName, pid, uid, caller, command, args, cb);
                            _data.recycle();
                        } catch (Throwable th4) {
                            th = th4;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        str = command;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    String str2 = packageName;
                    i = pid;
                    i2 = uid;
                    str = command;
                    _data.recycle();
                    throw th;
                }
            }

            public void onMediaButton(String packageName, int pid, int uid, Intent mediaButtonIntent, int sequenceNumber, ResultReceiver cb) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                int i3;
                Intent intent = mediaButtonIntent;
                ResultReceiver resultReceiver = cb;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(packageName);
                    } catch (Throwable th2) {
                        th = th2;
                        i = pid;
                        i2 = uid;
                        i3 = sequenceNumber;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(pid);
                    } catch (Throwable th3) {
                        th = th3;
                        i2 = uid;
                        i3 = sequenceNumber;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(uid);
                        if (intent != null) {
                            _data.writeInt(1);
                            intent.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            _data.writeInt(sequenceNumber);
                            if (resultReceiver != null) {
                                _data.writeInt(1);
                                resultReceiver.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            try {
                                if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                                    _data.recycle();
                                    return;
                                }
                                Stub.getDefaultImpl().onMediaButton(packageName, pid, uid, mediaButtonIntent, sequenceNumber, cb);
                                _data.recycle();
                            } catch (Throwable th4) {
                                th = th4;
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th5) {
                            th = th5;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        i3 = sequenceNumber;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    String str = packageName;
                    i = pid;
                    i2 = uid;
                    i3 = sequenceNumber;
                    _data.recycle();
                    throw th;
                }
            }

            public void onMediaButtonFromController(String packageName, int pid, int uid, ISessionControllerCallback caller, Intent mediaButtonIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    if (mediaButtonIntent != null) {
                        _data.writeInt(1);
                        mediaButtonIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onMediaButtonFromController(packageName, pid, uid, caller, mediaButtonIntent);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onPrepare(String packageName, int pid, int uid, ISessionControllerCallback caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onPrepare(packageName, pid, uid, caller);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onPrepareFromMediaId(String packageName, int pid, int uid, ISessionControllerCallback caller, String mediaId, Bundle extras) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                String str;
                Bundle bundle = extras;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(packageName);
                    } catch (Throwable th2) {
                        th = th2;
                        i = pid;
                        i2 = uid;
                        str = mediaId;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(pid);
                        try {
                            _data.writeInt(uid);
                            _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                        } catch (Throwable th3) {
                            th = th3;
                            str = mediaId;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        i2 = uid;
                        str = mediaId;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(mediaId);
                        if (bundle != null) {
                            _data.writeInt(1);
                            bundle.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().onPrepareFromMediaId(packageName, pid, uid, caller, mediaId, extras);
                            _data.recycle();
                        } catch (Throwable th5) {
                            th = th5;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    String str2 = packageName;
                    i = pid;
                    i2 = uid;
                    str = mediaId;
                    _data.recycle();
                    throw th;
                }
            }

            public void onPrepareFromSearch(String packageName, int pid, int uid, ISessionControllerCallback caller, String query, Bundle extras) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                String str;
                Bundle bundle = extras;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(packageName);
                    } catch (Throwable th2) {
                        th = th2;
                        i = pid;
                        i2 = uid;
                        str = query;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(pid);
                        try {
                            _data.writeInt(uid);
                            _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                        } catch (Throwable th3) {
                            th = th3;
                            str = query;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        i2 = uid;
                        str = query;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(query);
                        if (bundle != null) {
                            _data.writeInt(1);
                            bundle.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().onPrepareFromSearch(packageName, pid, uid, caller, query, extras);
                            _data.recycle();
                        } catch (Throwable th5) {
                            th = th5;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    String str2 = packageName;
                    i = pid;
                    i2 = uid;
                    str = query;
                    _data.recycle();
                    throw th;
                }
            }

            public void onPrepareFromUri(String packageName, int pid, int uid, ISessionControllerCallback caller, Uri uri, Bundle extras) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                Uri uri2 = uri;
                Bundle bundle = extras;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(packageName);
                    } catch (Throwable th2) {
                        th = th2;
                        i = pid;
                        i2 = uid;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(pid);
                    } catch (Throwable th3) {
                        th = th3;
                        i2 = uid;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(uid);
                        _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                        if (uri2 != null) {
                            _data.writeInt(1);
                            uri2.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (bundle != null) {
                            _data.writeInt(1);
                            bundle.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            if (this.mRemote.transact(7, _data, null, 1) || Stub.getDefaultImpl() == null) {
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().onPrepareFromUri(packageName, pid, uid, caller, uri, extras);
                            _data.recycle();
                        } catch (Throwable th4) {
                            th = th4;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    String str = packageName;
                    i = pid;
                    i2 = uid;
                    _data.recycle();
                    throw th;
                }
            }

            public void onPlay(String packageName, int pid, int uid, ISessionControllerCallback caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    if (this.mRemote.transact(8, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onPlay(packageName, pid, uid, caller);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onPlayFromMediaId(String packageName, int pid, int uid, ISessionControllerCallback caller, String mediaId, Bundle extras) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                String str;
                Bundle bundle = extras;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(packageName);
                    } catch (Throwable th2) {
                        th = th2;
                        i = pid;
                        i2 = uid;
                        str = mediaId;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(pid);
                        try {
                            _data.writeInt(uid);
                            _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                        } catch (Throwable th3) {
                            th = th3;
                            str = mediaId;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        i2 = uid;
                        str = mediaId;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(mediaId);
                        if (bundle != null) {
                            _data.writeInt(1);
                            bundle.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            if (this.mRemote.transact(9, _data, null, 1) || Stub.getDefaultImpl() == null) {
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().onPlayFromMediaId(packageName, pid, uid, caller, mediaId, extras);
                            _data.recycle();
                        } catch (Throwable th5) {
                            th = th5;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    String str2 = packageName;
                    i = pid;
                    i2 = uid;
                    str = mediaId;
                    _data.recycle();
                    throw th;
                }
            }

            public void onPlayFromSearch(String packageName, int pid, int uid, ISessionControllerCallback caller, String query, Bundle extras) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                String str;
                Bundle bundle = extras;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(packageName);
                    } catch (Throwable th2) {
                        th = th2;
                        i = pid;
                        i2 = uid;
                        str = query;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(pid);
                        try {
                            _data.writeInt(uid);
                            _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                        } catch (Throwable th3) {
                            th = th3;
                            str = query;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        i2 = uid;
                        str = query;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(query);
                        if (bundle != null) {
                            _data.writeInt(1);
                            bundle.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            if (this.mRemote.transact(10, _data, null, 1) || Stub.getDefaultImpl() == null) {
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().onPlayFromSearch(packageName, pid, uid, caller, query, extras);
                            _data.recycle();
                        } catch (Throwable th5) {
                            th = th5;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    String str2 = packageName;
                    i = pid;
                    i2 = uid;
                    str = query;
                    _data.recycle();
                    throw th;
                }
            }

            public void onPlayFromUri(String packageName, int pid, int uid, ISessionControllerCallback caller, Uri uri, Bundle extras) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                Uri uri2 = uri;
                Bundle bundle = extras;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(packageName);
                    } catch (Throwable th2) {
                        th = th2;
                        i = pid;
                        i2 = uid;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(pid);
                    } catch (Throwable th3) {
                        th = th3;
                        i2 = uid;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(uid);
                        _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                        if (uri2 != null) {
                            _data.writeInt(1);
                            uri2.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (bundle != null) {
                            _data.writeInt(1);
                            bundle.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            if (this.mRemote.transact(11, _data, null, 1) || Stub.getDefaultImpl() == null) {
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().onPlayFromUri(packageName, pid, uid, caller, uri, extras);
                            _data.recycle();
                        } catch (Throwable th4) {
                            th = th4;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    String str = packageName;
                    i = pid;
                    i2 = uid;
                    _data.recycle();
                    throw th;
                }
            }

            public void onSkipToTrack(String packageName, int pid, int uid, ISessionControllerCallback caller, long id) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                long j;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(packageName);
                    } catch (Throwable th2) {
                        th = th2;
                        i = pid;
                        i2 = uid;
                        j = id;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(pid);
                        try {
                            _data.writeInt(uid);
                            _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                            try {
                                _data.writeLong(id);
                            } catch (Throwable th3) {
                                th = th3;
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            j = id;
                            _data.recycle();
                            throw th;
                        }
                        try {
                            if (this.mRemote.transact(12, _data, null, 1) || Stub.getDefaultImpl() == null) {
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().onSkipToTrack(packageName, pid, uid, caller, id);
                            _data.recycle();
                        } catch (Throwable th5) {
                            th = th5;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        i2 = uid;
                        j = id;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    String str = packageName;
                    i = pid;
                    i2 = uid;
                    j = id;
                    _data.recycle();
                    throw th;
                }
            }

            public void onPause(String packageName, int pid, int uid, ISessionControllerCallback caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    if (this.mRemote.transact(13, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onPause(packageName, pid, uid, caller);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onStop(String packageName, int pid, int uid, ISessionControllerCallback caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    if (this.mRemote.transact(14, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onStop(packageName, pid, uid, caller);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onNext(String packageName, int pid, int uid, ISessionControllerCallback caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    if (this.mRemote.transact(15, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onNext(packageName, pid, uid, caller);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onPrevious(String packageName, int pid, int uid, ISessionControllerCallback caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    if (this.mRemote.transact(16, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onPrevious(packageName, pid, uid, caller);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onFastForward(String packageName, int pid, int uid, ISessionControllerCallback caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    if (this.mRemote.transact(17, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onFastForward(packageName, pid, uid, caller);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onRewind(String packageName, int pid, int uid, ISessionControllerCallback caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    if (this.mRemote.transact(18, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onRewind(packageName, pid, uid, caller);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onSeekTo(String packageName, int pid, int uid, ISessionControllerCallback caller, long pos) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                long j;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(packageName);
                    } catch (Throwable th2) {
                        th = th2;
                        i = pid;
                        i2 = uid;
                        j = pos;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(pid);
                        try {
                            _data.writeInt(uid);
                            _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                            try {
                                _data.writeLong(pos);
                            } catch (Throwable th3) {
                                th = th3;
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            j = pos;
                            _data.recycle();
                            throw th;
                        }
                        try {
                            if (this.mRemote.transact(19, _data, null, 1) || Stub.getDefaultImpl() == null) {
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().onSeekTo(packageName, pid, uid, caller, pos);
                            _data.recycle();
                        } catch (Throwable th5) {
                            th = th5;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        i2 = uid;
                        j = pos;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    String str = packageName;
                    i = pid;
                    i2 = uid;
                    j = pos;
                    _data.recycle();
                    throw th;
                }
            }

            public void onRate(String packageName, int pid, int uid, ISessionControllerCallback caller, Rating rating) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    if (rating != null) {
                        _data.writeInt(1);
                        rating.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(20, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onRate(packageName, pid, uid, caller, rating);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onSetPlaybackSpeed(String packageName, int pid, int uid, ISessionControllerCallback caller, float speed) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeFloat(speed);
                    if (this.mRemote.transact(21, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onSetPlaybackSpeed(packageName, pid, uid, caller, speed);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onCustomAction(String packageName, int pid, int uid, ISessionControllerCallback caller, String action, Bundle args) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                String str;
                Bundle bundle = args;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(packageName);
                    } catch (Throwable th2) {
                        th = th2;
                        i = pid;
                        i2 = uid;
                        str = action;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(pid);
                        try {
                            _data.writeInt(uid);
                            _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                        } catch (Throwable th3) {
                            th = th3;
                            str = action;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        i2 = uid;
                        str = action;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(action);
                        if (bundle != null) {
                            _data.writeInt(1);
                            bundle.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            if (this.mRemote.transact(22, _data, null, 1) || Stub.getDefaultImpl() == null) {
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().onCustomAction(packageName, pid, uid, caller, action, args);
                            _data.recycle();
                        } catch (Throwable th5) {
                            th = th5;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    String str2 = packageName;
                    i = pid;
                    i2 = uid;
                    str = action;
                    _data.recycle();
                    throw th;
                }
            }

            public void onAdjustVolume(String packageName, int pid, int uid, ISessionControllerCallback caller, int direction) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeInt(direction);
                    if (this.mRemote.transact(23, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onAdjustVolume(packageName, pid, uid, caller, direction);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onSetVolumeTo(String packageName, int pid, int uid, ISessionControllerCallback caller, int value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeInt(value);
                    if (this.mRemote.transact(24, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onSetVolumeTo(packageName, pid, uid, caller, value);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISessionCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ISessionCallback)) {
                return new Proxy(obj);
            }
            return (ISessionCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "onCommand";
                case 2:
                    return "onMediaButton";
                case 3:
                    return "onMediaButtonFromController";
                case 4:
                    return "onPrepare";
                case 5:
                    return "onPrepareFromMediaId";
                case 6:
                    return "onPrepareFromSearch";
                case 7:
                    return "onPrepareFromUri";
                case 8:
                    return "onPlay";
                case 9:
                    return "onPlayFromMediaId";
                case 10:
                    return "onPlayFromSearch";
                case 11:
                    return "onPlayFromUri";
                case 12:
                    return "onSkipToTrack";
                case 13:
                    return "onPause";
                case 14:
                    return "onStop";
                case 15:
                    return "onNext";
                case 16:
                    return "onPrevious";
                case 17:
                    return "onFastForward";
                case 18:
                    return "onRewind";
                case 19:
                    return "onSeekTo";
                case 20:
                    return "onRate";
                case 21:
                    return "onSetPlaybackSpeed";
                case 22:
                    return "onCustomAction";
                case 23:
                    return "onAdjustVolume";
                case 24:
                    return "onSetVolumeTo";
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
                int _arg1;
                String _arg0;
                int _arg12;
                Intent _arg3;
                String _arg02;
                int _arg13;
                ISessionControllerCallback _arg32;
                ISessionControllerCallback _arg33;
                String _arg4;
                Bundle _arg5;
                Uri _arg42;
                switch (i) {
                    case 1:
                        Bundle _arg52;
                        ResultReceiver _arg6;
                        parcel.enforceInterface(descriptor);
                        String _arg03 = data.readString();
                        _arg1 = data.readInt();
                        int _arg2 = data.readInt();
                        ISessionControllerCallback _arg34 = android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                        String _arg43 = data.readString();
                        if (data.readInt() != 0) {
                            _arg52 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg52 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg6 = (ResultReceiver) ResultReceiver.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg6 = null;
                        }
                        onCommand(_arg03, _arg1, _arg2, _arg34, _arg43, _arg52, _arg6);
                        return true;
                    case 2:
                        ResultReceiver _arg53;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg12 = data.readInt();
                        _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg3 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        int _arg44 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg53 = (ResultReceiver) ResultReceiver.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg53 = null;
                        }
                        onMediaButton(_arg0, _arg12, _arg1, _arg3, _arg44, _arg53);
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        _arg13 = data.readInt();
                        _arg12 = data.readInt();
                        _arg32 = android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg3 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        onMediaButtonFromController(_arg02, _arg13, _arg12, _arg32, _arg3);
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        onPrepare(data.readString(), data.readInt(), data.readInt(), android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg12 = data.readInt();
                        _arg1 = data.readInt();
                        _arg33 = android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                        _arg4 = data.readString();
                        if (data.readInt() != 0) {
                            _arg5 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg5 = null;
                        }
                        onPrepareFromMediaId(_arg0, _arg12, _arg1, _arg33, _arg4, _arg5);
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg12 = data.readInt();
                        _arg1 = data.readInt();
                        _arg33 = android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                        _arg4 = data.readString();
                        if (data.readInt() != 0) {
                            _arg5 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg5 = null;
                        }
                        onPrepareFromSearch(_arg0, _arg12, _arg1, _arg33, _arg4, _arg5);
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg12 = data.readInt();
                        _arg1 = data.readInt();
                        _arg33 = android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg42 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg42 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg5 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg5 = null;
                        }
                        onPrepareFromUri(_arg0, _arg12, _arg1, _arg33, _arg42, _arg5);
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        onPlay(data.readString(), data.readInt(), data.readInt(), android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg12 = data.readInt();
                        _arg1 = data.readInt();
                        _arg33 = android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                        _arg4 = data.readString();
                        if (data.readInt() != 0) {
                            _arg5 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg5 = null;
                        }
                        onPlayFromMediaId(_arg0, _arg12, _arg1, _arg33, _arg4, _arg5);
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg12 = data.readInt();
                        _arg1 = data.readInt();
                        _arg33 = android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                        _arg4 = data.readString();
                        if (data.readInt() != 0) {
                            _arg5 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg5 = null;
                        }
                        onPlayFromSearch(_arg0, _arg12, _arg1, _arg33, _arg4, _arg5);
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg12 = data.readInt();
                        _arg1 = data.readInt();
                        _arg33 = android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg42 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg42 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg5 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg5 = null;
                        }
                        onPlayFromUri(_arg0, _arg12, _arg1, _arg33, _arg42, _arg5);
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        onSkipToTrack(data.readString(), data.readInt(), data.readInt(), android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder()), data.readLong());
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        onPause(data.readString(), data.readInt(), data.readInt(), android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        onStop(data.readString(), data.readInt(), data.readInt(), android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        onNext(data.readString(), data.readInt(), data.readInt(), android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        onPrevious(data.readString(), data.readInt(), data.readInt(), android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        onFastForward(data.readString(), data.readInt(), data.readInt(), android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        onRewind(data.readString(), data.readInt(), data.readInt(), android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        onSeekTo(data.readString(), data.readInt(), data.readInt(), android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder()), data.readLong());
                        return true;
                    case 20:
                        Rating _arg45;
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        _arg13 = data.readInt();
                        _arg12 = data.readInt();
                        _arg32 = android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg45 = (Rating) Rating.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg45 = null;
                        }
                        onRate(_arg02, _arg13, _arg12, _arg32, _arg45);
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        onSetPlaybackSpeed(data.readString(), data.readInt(), data.readInt(), android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder()), data.readFloat());
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg12 = data.readInt();
                        _arg1 = data.readInt();
                        _arg33 = android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                        _arg4 = data.readString();
                        if (data.readInt() != 0) {
                            _arg5 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg5 = null;
                        }
                        onCustomAction(_arg0, _arg12, _arg1, _arg33, _arg4, _arg5);
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        onAdjustVolume(data.readString(), data.readInt(), data.readInt(), android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        onSetVolumeTo(data.readString(), data.readInt(), data.readInt(), android.media.session.ISessionControllerCallback.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(ISessionCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ISessionCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onAdjustVolume(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback, int i3) throws RemoteException;

    void onCommand(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback, String str2, Bundle bundle, ResultReceiver resultReceiver) throws RemoteException;

    void onCustomAction(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback, String str2, Bundle bundle) throws RemoteException;

    void onFastForward(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback) throws RemoteException;

    void onMediaButton(String str, int i, int i2, Intent intent, int i3, ResultReceiver resultReceiver) throws RemoteException;

    void onMediaButtonFromController(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback, Intent intent) throws RemoteException;

    void onNext(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback) throws RemoteException;

    void onPause(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback) throws RemoteException;

    void onPlay(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback) throws RemoteException;

    void onPlayFromMediaId(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback, String str2, Bundle bundle) throws RemoteException;

    void onPlayFromSearch(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback, String str2, Bundle bundle) throws RemoteException;

    void onPlayFromUri(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback, Uri uri, Bundle bundle) throws RemoteException;

    void onPrepare(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback) throws RemoteException;

    void onPrepareFromMediaId(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback, String str2, Bundle bundle) throws RemoteException;

    void onPrepareFromSearch(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback, String str2, Bundle bundle) throws RemoteException;

    void onPrepareFromUri(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback, Uri uri, Bundle bundle) throws RemoteException;

    void onPrevious(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback) throws RemoteException;

    void onRate(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback, Rating rating) throws RemoteException;

    void onRewind(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback) throws RemoteException;

    void onSeekTo(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback, long j) throws RemoteException;

    void onSetPlaybackSpeed(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback, float f) throws RemoteException;

    void onSetVolumeTo(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback, int i3) throws RemoteException;

    void onSkipToTrack(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback, long j) throws RemoteException;

    void onStop(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback) throws RemoteException;
}
