package android.media.tv;

import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.InputChannel;
import java.util.List;

public interface ITvInputClient extends IInterface {

    public static class Default implements ITvInputClient {
        public void onSessionCreated(String inputId, IBinder token, InputChannel channel, int seq) throws RemoteException {
        }

        public void onSessionReleased(int seq) throws RemoteException {
        }

        public void onSessionEvent(String name, Bundle args, int seq) throws RemoteException {
        }

        public void onChannelRetuned(Uri channelUri, int seq) throws RemoteException {
        }

        public void onTracksChanged(List<TvTrackInfo> list, int seq) throws RemoteException {
        }

        public void onTrackSelected(int type, String trackId, int seq) throws RemoteException {
        }

        public void onVideoAvailable(int seq) throws RemoteException {
        }

        public void onVideoUnavailable(int reason, int seq) throws RemoteException {
        }

        public void onContentAllowed(int seq) throws RemoteException {
        }

        public void onContentBlocked(String rating, int seq) throws RemoteException {
        }

        public void onLayoutSurface(int left, int top, int right, int bottom, int seq) throws RemoteException {
        }

        public void onTimeShiftStatusChanged(int status, int seq) throws RemoteException {
        }

        public void onTimeShiftStartPositionChanged(long timeMs, int seq) throws RemoteException {
        }

        public void onTimeShiftCurrentPositionChanged(long timeMs, int seq) throws RemoteException {
        }

        public void onTuned(int seq, Uri channelUri) throws RemoteException {
        }

        public void onRecordingStopped(Uri recordedProgramUri, int seq) throws RemoteException {
        }

        public void onError(int error, int seq) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ITvInputClient {
        private static final String DESCRIPTOR = "android.media.tv.ITvInputClient";
        static final int TRANSACTION_onChannelRetuned = 4;
        static final int TRANSACTION_onContentAllowed = 9;
        static final int TRANSACTION_onContentBlocked = 10;
        static final int TRANSACTION_onError = 17;
        static final int TRANSACTION_onLayoutSurface = 11;
        static final int TRANSACTION_onRecordingStopped = 16;
        static final int TRANSACTION_onSessionCreated = 1;
        static final int TRANSACTION_onSessionEvent = 3;
        static final int TRANSACTION_onSessionReleased = 2;
        static final int TRANSACTION_onTimeShiftCurrentPositionChanged = 14;
        static final int TRANSACTION_onTimeShiftStartPositionChanged = 13;
        static final int TRANSACTION_onTimeShiftStatusChanged = 12;
        static final int TRANSACTION_onTrackSelected = 6;
        static final int TRANSACTION_onTracksChanged = 5;
        static final int TRANSACTION_onTuned = 15;
        static final int TRANSACTION_onVideoAvailable = 7;
        static final int TRANSACTION_onVideoUnavailable = 8;

        private static class Proxy implements ITvInputClient {
            public static ITvInputClient sDefaultImpl;
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

            public void onSessionCreated(String inputId, IBinder token, InputChannel channel, int seq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(inputId);
                    _data.writeStrongBinder(token);
                    if (channel != null) {
                        _data.writeInt(1);
                        channel.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(seq);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onSessionCreated(inputId, token, channel, seq);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onSessionReleased(int seq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(seq);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onSessionReleased(seq);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onSessionEvent(String name, Bundle args, int seq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    if (args != null) {
                        _data.writeInt(1);
                        args.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(seq);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onSessionEvent(name, args, seq);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onChannelRetuned(Uri channelUri, int seq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (channelUri != null) {
                        _data.writeInt(1);
                        channelUri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(seq);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onChannelRetuned(channelUri, seq);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onTracksChanged(List<TvTrackInfo> tracks, int seq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(tracks);
                    _data.writeInt(seq);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onTracksChanged(tracks, seq);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onTrackSelected(int type, String trackId, int seq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(trackId);
                    _data.writeInt(seq);
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onTrackSelected(type, trackId, seq);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onVideoAvailable(int seq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(seq);
                    if (this.mRemote.transact(7, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onVideoAvailable(seq);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onVideoUnavailable(int reason, int seq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(reason);
                    _data.writeInt(seq);
                    if (this.mRemote.transact(8, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onVideoUnavailable(reason, seq);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onContentAllowed(int seq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(seq);
                    if (this.mRemote.transact(9, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onContentAllowed(seq);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onContentBlocked(String rating, int seq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(rating);
                    _data.writeInt(seq);
                    if (this.mRemote.transact(10, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onContentBlocked(rating, seq);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onLayoutSurface(int left, int top, int right, int bottom, int seq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(left);
                    _data.writeInt(top);
                    _data.writeInt(right);
                    _data.writeInt(bottom);
                    _data.writeInt(seq);
                    if (this.mRemote.transact(11, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onLayoutSurface(left, top, right, bottom, seq);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onTimeShiftStatusChanged(int status, int seq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status);
                    _data.writeInt(seq);
                    if (this.mRemote.transact(12, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onTimeShiftStatusChanged(status, seq);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onTimeShiftStartPositionChanged(long timeMs, int seq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(timeMs);
                    _data.writeInt(seq);
                    if (this.mRemote.transact(13, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onTimeShiftStartPositionChanged(timeMs, seq);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onTimeShiftCurrentPositionChanged(long timeMs, int seq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(timeMs);
                    _data.writeInt(seq);
                    if (this.mRemote.transact(14, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onTimeShiftCurrentPositionChanged(timeMs, seq);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onTuned(int seq, Uri channelUri) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(seq);
                    if (channelUri != null) {
                        _data.writeInt(1);
                        channelUri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(15, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onTuned(seq, channelUri);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onRecordingStopped(Uri recordedProgramUri, int seq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (recordedProgramUri != null) {
                        _data.writeInt(1);
                        recordedProgramUri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(seq);
                    if (this.mRemote.transact(16, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onRecordingStopped(recordedProgramUri, seq);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onError(int error, int seq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(error);
                    _data.writeInt(seq);
                    if (this.mRemote.transact(17, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onError(error, seq);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ITvInputClient asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ITvInputClient)) {
                return new Proxy(obj);
            }
            return (ITvInputClient) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "onSessionCreated";
                case 2:
                    return "onSessionReleased";
                case 3:
                    return "onSessionEvent";
                case 4:
                    return "onChannelRetuned";
                case 5:
                    return "onTracksChanged";
                case 6:
                    return "onTrackSelected";
                case 7:
                    return "onVideoAvailable";
                case 8:
                    return "onVideoUnavailable";
                case 9:
                    return "onContentAllowed";
                case 10:
                    return "onContentBlocked";
                case 11:
                    return "onLayoutSurface";
                case 12:
                    return "onTimeShiftStatusChanged";
                case 13:
                    return "onTimeShiftStartPositionChanged";
                case 14:
                    return "onTimeShiftCurrentPositionChanged";
                case 15:
                    return "onTuned";
                case 16:
                    return "onRecordingStopped";
                case 17:
                    return "onError";
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
                String _arg0;
                Uri _arg02;
                switch (i) {
                    case 1:
                        InputChannel _arg2;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        IBinder _arg1 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg2 = (InputChannel) InputChannel.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        onSessionCreated(_arg0, _arg1, _arg2, data.readInt());
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        onSessionReleased(data.readInt());
                        return true;
                    case 3:
                        Bundle _arg12;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        if (data.readInt() != 0) {
                            _arg12 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        onSessionEvent(_arg0, _arg12, data.readInt());
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        onChannelRetuned(_arg02, data.readInt());
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        onTracksChanged(parcel.createTypedArrayList(TvTrackInfo.CREATOR), data.readInt());
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        onTrackSelected(data.readInt(), data.readString(), data.readInt());
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        onVideoAvailable(data.readInt());
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        onVideoUnavailable(data.readInt(), data.readInt());
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        onContentAllowed(data.readInt());
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        onContentBlocked(data.readString(), data.readInt());
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        onLayoutSurface(data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt());
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        onTimeShiftStatusChanged(data.readInt(), data.readInt());
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        onTimeShiftStartPositionChanged(data.readLong(), data.readInt());
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        onTimeShiftCurrentPositionChanged(data.readLong(), data.readInt());
                        return true;
                    case 15:
                        Uri _arg13;
                        parcel.enforceInterface(descriptor);
                        int _arg03 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg13 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        onTuned(_arg03, _arg13);
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        onRecordingStopped(_arg02, data.readInt());
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        onError(data.readInt(), data.readInt());
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(ITvInputClient impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ITvInputClient getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onChannelRetuned(Uri uri, int i) throws RemoteException;

    void onContentAllowed(int i) throws RemoteException;

    void onContentBlocked(String str, int i) throws RemoteException;

    void onError(int i, int i2) throws RemoteException;

    void onLayoutSurface(int i, int i2, int i3, int i4, int i5) throws RemoteException;

    void onRecordingStopped(Uri uri, int i) throws RemoteException;

    void onSessionCreated(String str, IBinder iBinder, InputChannel inputChannel, int i) throws RemoteException;

    void onSessionEvent(String str, Bundle bundle, int i) throws RemoteException;

    void onSessionReleased(int i) throws RemoteException;

    void onTimeShiftCurrentPositionChanged(long j, int i) throws RemoteException;

    void onTimeShiftStartPositionChanged(long j, int i) throws RemoteException;

    void onTimeShiftStatusChanged(int i, int i2) throws RemoteException;

    void onTrackSelected(int i, String str, int i2) throws RemoteException;

    void onTracksChanged(List<TvTrackInfo> list, int i) throws RemoteException;

    void onTuned(int i, Uri uri) throws RemoteException;

    void onVideoAvailable(int i) throws RemoteException;

    void onVideoUnavailable(int i, int i2) throws RemoteException;
}
