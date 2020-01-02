package com.android.ims.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.provider.Telephony.BaseMmsColumns;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsStreamMediaProfile;
import android.telephony.ims.aidl.IImsCallSessionListener;
import miui.util.HapticFeedbackUtil;

public interface IImsCallSession extends IInterface {

    public static class Default implements IImsCallSession {
        public void close() throws RemoteException {
        }

        public String getCallId() throws RemoteException {
            return null;
        }

        public ImsCallProfile getCallProfile() throws RemoteException {
            return null;
        }

        public ImsCallProfile getLocalCallProfile() throws RemoteException {
            return null;
        }

        public ImsCallProfile getRemoteCallProfile() throws RemoteException {
            return null;
        }

        public String getProperty(String name) throws RemoteException {
            return null;
        }

        public int getState() throws RemoteException {
            return 0;
        }

        public boolean isInCall() throws RemoteException {
            return false;
        }

        public void setListener(IImsCallSessionListener listener) throws RemoteException {
        }

        public void setMute(boolean muted) throws RemoteException {
        }

        public void start(String callee, ImsCallProfile profile) throws RemoteException {
        }

        public void startConference(String[] participants, ImsCallProfile profile) throws RemoteException {
        }

        public void accept(int callType, ImsStreamMediaProfile profile) throws RemoteException {
        }

        public void deflect(String deflectNumber) throws RemoteException {
        }

        public void reject(int reason) throws RemoteException {
        }

        public void terminate(int reason) throws RemoteException {
        }

        public void hold(ImsStreamMediaProfile profile) throws RemoteException {
        }

        public void resume(ImsStreamMediaProfile profile) throws RemoteException {
        }

        public void merge() throws RemoteException {
        }

        public void update(int callType, ImsStreamMediaProfile profile) throws RemoteException {
        }

        public void extendToConference(String[] participants) throws RemoteException {
        }

        public void inviteParticipants(String[] participants) throws RemoteException {
        }

        public void removeParticipants(String[] participants) throws RemoteException {
        }

        public void sendDtmf(char c, Message result) throws RemoteException {
        }

        public void startDtmf(char c) throws RemoteException {
        }

        public void stopDtmf() throws RemoteException {
        }

        public void sendUssd(String ussdMessage) throws RemoteException {
        }

        public IImsVideoCallProvider getVideoCallProvider() throws RemoteException {
            return null;
        }

        public boolean isMultiparty() throws RemoteException {
            return false;
        }

        public void sendRttModifyRequest(ImsCallProfile toProfile) throws RemoteException {
        }

        public void sendRttModifyResponse(boolean status) throws RemoteException {
        }

        public void sendRttMessage(String rttMessage) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IImsCallSession {
        private static final String DESCRIPTOR = "com.android.ims.internal.IImsCallSession";
        static final int TRANSACTION_accept = 13;
        static final int TRANSACTION_close = 1;
        static final int TRANSACTION_deflect = 14;
        static final int TRANSACTION_extendToConference = 21;
        static final int TRANSACTION_getCallId = 2;
        static final int TRANSACTION_getCallProfile = 3;
        static final int TRANSACTION_getLocalCallProfile = 4;
        static final int TRANSACTION_getProperty = 6;
        static final int TRANSACTION_getRemoteCallProfile = 5;
        static final int TRANSACTION_getState = 7;
        static final int TRANSACTION_getVideoCallProvider = 28;
        static final int TRANSACTION_hold = 17;
        static final int TRANSACTION_inviteParticipants = 22;
        static final int TRANSACTION_isInCall = 8;
        static final int TRANSACTION_isMultiparty = 29;
        static final int TRANSACTION_merge = 19;
        static final int TRANSACTION_reject = 15;
        static final int TRANSACTION_removeParticipants = 23;
        static final int TRANSACTION_resume = 18;
        static final int TRANSACTION_sendDtmf = 24;
        static final int TRANSACTION_sendRttMessage = 32;
        static final int TRANSACTION_sendRttModifyRequest = 30;
        static final int TRANSACTION_sendRttModifyResponse = 31;
        static final int TRANSACTION_sendUssd = 27;
        static final int TRANSACTION_setListener = 9;
        static final int TRANSACTION_setMute = 10;
        static final int TRANSACTION_start = 11;
        static final int TRANSACTION_startConference = 12;
        static final int TRANSACTION_startDtmf = 25;
        static final int TRANSACTION_stopDtmf = 26;
        static final int TRANSACTION_terminate = 16;
        static final int TRANSACTION_update = 20;

        private static class Proxy implements IImsCallSession {
            public static IImsCallSession sDefaultImpl;
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

            public void close() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().close();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getCallId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getCallId();
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

            public ImsCallProfile getCallProfile() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ImsCallProfile imsCallProfile = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        imsCallProfile = Stub.getDefaultImpl();
                        if (imsCallProfile != 0) {
                            imsCallProfile = Stub.getDefaultImpl().getCallProfile();
                            return imsCallProfile;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        imsCallProfile = (ImsCallProfile) ImsCallProfile.CREATOR.createFromParcel(_reply);
                    } else {
                        imsCallProfile = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return imsCallProfile;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ImsCallProfile getLocalCallProfile() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ImsCallProfile imsCallProfile = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        imsCallProfile = Stub.getDefaultImpl();
                        if (imsCallProfile != 0) {
                            imsCallProfile = Stub.getDefaultImpl().getLocalCallProfile();
                            return imsCallProfile;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        imsCallProfile = (ImsCallProfile) ImsCallProfile.CREATOR.createFromParcel(_reply);
                    } else {
                        imsCallProfile = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return imsCallProfile;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ImsCallProfile getRemoteCallProfile() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ImsCallProfile imsCallProfile = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        imsCallProfile = Stub.getDefaultImpl();
                        if (imsCallProfile != 0) {
                            imsCallProfile = Stub.getDefaultImpl().getRemoteCallProfile();
                            return imsCallProfile;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        imsCallProfile = (ImsCallProfile) ImsCallProfile.CREATOR.createFromParcel(_reply);
                    } else {
                        imsCallProfile = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return imsCallProfile;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getProperty(String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    String str = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getProperty(name);
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

            public int getState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getState();
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

            public boolean isInCall() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isInCall();
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

            public void setListener(IImsCallSessionListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setMute(boolean muted) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(muted ? 1 : 0);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setMute(muted);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void start(String callee, ImsCallProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callee);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().start(callee, profile);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startConference(String[] participants, ImsCallProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(participants);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startConference(participants, profile);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void accept(int callType, ImsStreamMediaProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(callType);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().accept(callType, profile);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void deflect(String deflectNumber) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(deflectNumber);
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().deflect(deflectNumber);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reject(int reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(reason);
                    if (this.mRemote.transact(15, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reject(reason);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void terminate(int reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(reason);
                    if (this.mRemote.transact(16, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().terminate(reason);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void hold(ImsStreamMediaProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(17, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().hold(profile);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void resume(ImsStreamMediaProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().resume(profile);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void merge() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().merge();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void update(int callType, ImsStreamMediaProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(callType);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(20, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().update(callType, profile);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void extendToConference(String[] participants) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(participants);
                    if (this.mRemote.transact(21, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().extendToConference(participants);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void inviteParticipants(String[] participants) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(participants);
                    if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().inviteParticipants(participants);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeParticipants(String[] participants) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(participants);
                    if (this.mRemote.transact(23, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeParticipants(participants);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendDtmf(char c, Message result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(c);
                    if (result != null) {
                        _data.writeInt(1);
                        result.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(24, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().sendDtmf(c, result);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startDtmf(char c) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(c);
                    if (this.mRemote.transact(25, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startDtmf(c);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopDtmf() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(26, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().stopDtmf();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendUssd(String ussdMessage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(ussdMessage);
                    if (this.mRemote.transact(27, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().sendUssd(ussdMessage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IImsVideoCallProvider getVideoCallProvider() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IImsVideoCallProvider iImsVideoCallProvider = 28;
                    if (!this.mRemote.transact(28, _data, _reply, 0)) {
                        iImsVideoCallProvider = Stub.getDefaultImpl();
                        if (iImsVideoCallProvider != 0) {
                            iImsVideoCallProvider = Stub.getDefaultImpl().getVideoCallProvider();
                            return iImsVideoCallProvider;
                        }
                    }
                    _reply.readException();
                    iImsVideoCallProvider = com.android.ims.internal.IImsVideoCallProvider.Stub.asInterface(_reply.readStrongBinder());
                    IImsVideoCallProvider _result = iImsVideoCallProvider;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isMultiparty() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(29, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isMultiparty();
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

            public void sendRttModifyRequest(ImsCallProfile toProfile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (toProfile != null) {
                        _data.writeInt(1);
                        toProfile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(30, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().sendRttModifyRequest(toProfile);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendRttModifyResponse(boolean status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status ? 1 : 0);
                    if (this.mRemote.transact(31, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().sendRttModifyResponse(status);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendRttMessage(String rttMessage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(rttMessage);
                    if (this.mRemote.transact(32, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().sendRttMessage(rttMessage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IImsCallSession asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IImsCallSession)) {
                return new Proxy(obj);
            }
            return (IImsCallSession) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "close";
                case 2:
                    return "getCallId";
                case 3:
                    return "getCallProfile";
                case 4:
                    return "getLocalCallProfile";
                case 5:
                    return "getRemoteCallProfile";
                case 6:
                    return "getProperty";
                case 7:
                    return "getState";
                case 8:
                    return "isInCall";
                case 9:
                    return "setListener";
                case 10:
                    return "setMute";
                case 11:
                    return BaseMmsColumns.START;
                case 12:
                    return "startConference";
                case 13:
                    return "accept";
                case 14:
                    return "deflect";
                case 15:
                    return "reject";
                case 16:
                    return "terminate";
                case 17:
                    return HapticFeedbackUtil.EFFECT_KEY_HOLD;
                case 18:
                    return "resume";
                case 19:
                    return "merge";
                case 20:
                    return "update";
                case 21:
                    return "extendToConference";
                case 22:
                    return "inviteParticipants";
                case 23:
                    return "removeParticipants";
                case 24:
                    return "sendDtmf";
                case 25:
                    return "startDtmf";
                case 26:
                    return "stopDtmf";
                case 27:
                    return "sendUssd";
                case 28:
                    return "getVideoCallProvider";
                case 29:
                    return "isMultiparty";
                case 30:
                    return "sendRttModifyRequest";
                case 31:
                    return "sendRttModifyResponse";
                case 32:
                    return "sendRttMessage";
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
                boolean _arg0 = false;
                String _result;
                ImsCallProfile _result2;
                int _result3;
                ImsStreamMediaProfile _arg1;
                ImsStreamMediaProfile _arg02;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        close();
                        reply.writeNoException();
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        _result = getCallId();
                        reply.writeNoException();
                        reply.writeString(_result);
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        _result2 = getCallProfile();
                        reply.writeNoException();
                        if (_result2 != null) {
                            reply.writeInt(1);
                            _result2.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        _result2 = getLocalCallProfile();
                        reply.writeNoException();
                        if (_result2 != null) {
                            reply.writeInt(1);
                            _result2.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        _result2 = getRemoteCallProfile();
                        reply.writeNoException();
                        if (_result2 != null) {
                            reply.writeInt(1);
                            _result2.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        String _result4 = getProperty(data.readString());
                        reply.writeNoException();
                        reply.writeString(_result4);
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        _result3 = getState();
                        reply.writeNoException();
                        reply.writeInt(_result3);
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        _arg0 = isInCall();
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        setListener(android.telephony.ims.aidl.IImsCallSessionListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        setMute(_arg0);
                        reply.writeNoException();
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        _result = data.readString();
                        if (data.readInt() != 0) {
                            _result2 = (ImsCallProfile) ImsCallProfile.CREATOR.createFromParcel(data);
                        } else {
                            _result2 = null;
                        }
                        start(_result, _result2);
                        reply.writeNoException();
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        String[] _arg03 = data.createStringArray();
                        if (data.readInt() != 0) {
                            _result2 = (ImsCallProfile) ImsCallProfile.CREATOR.createFromParcel(data);
                        } else {
                            _result2 = null;
                        }
                        startConference(_arg03, _result2);
                        reply.writeNoException();
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        _result3 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = (ImsStreamMediaProfile) ImsStreamMediaProfile.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        accept(_result3, _arg1);
                        reply.writeNoException();
                        return true;
                    case 14:
                        data.enforceInterface(descriptor);
                        deflect(data.readString());
                        reply.writeNoException();
                        return true;
                    case 15:
                        data.enforceInterface(descriptor);
                        reject(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 16:
                        data.enforceInterface(descriptor);
                        terminate(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 17:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (ImsStreamMediaProfile) ImsStreamMediaProfile.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        hold(_arg02);
                        reply.writeNoException();
                        return true;
                    case 18:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (ImsStreamMediaProfile) ImsStreamMediaProfile.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        resume(_arg02);
                        reply.writeNoException();
                        return true;
                    case 19:
                        data.enforceInterface(descriptor);
                        merge();
                        reply.writeNoException();
                        return true;
                    case 20:
                        data.enforceInterface(descriptor);
                        _result3 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg1 = (ImsStreamMediaProfile) ImsStreamMediaProfile.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        update(_result3, _arg1);
                        reply.writeNoException();
                        return true;
                    case 21:
                        data.enforceInterface(descriptor);
                        extendToConference(data.createStringArray());
                        reply.writeNoException();
                        return true;
                    case 22:
                        data.enforceInterface(descriptor);
                        inviteParticipants(data.createStringArray());
                        reply.writeNoException();
                        return true;
                    case 23:
                        data.enforceInterface(descriptor);
                        removeParticipants(data.createStringArray());
                        reply.writeNoException();
                        return true;
                    case 24:
                        Message _arg12;
                        data.enforceInterface(descriptor);
                        char _arg04 = (char) data.readInt();
                        if (data.readInt() != 0) {
                            _arg12 = (Message) Message.CREATOR.createFromParcel(data);
                        } else {
                            _arg12 = null;
                        }
                        sendDtmf(_arg04, _arg12);
                        reply.writeNoException();
                        return true;
                    case 25:
                        data.enforceInterface(descriptor);
                        startDtmf((char) data.readInt());
                        reply.writeNoException();
                        return true;
                    case 26:
                        data.enforceInterface(descriptor);
                        stopDtmf();
                        reply.writeNoException();
                        return true;
                    case 27:
                        data.enforceInterface(descriptor);
                        sendUssd(data.readString());
                        reply.writeNoException();
                        return true;
                    case 28:
                        data.enforceInterface(descriptor);
                        IImsVideoCallProvider _result5 = getVideoCallProvider();
                        reply.writeNoException();
                        reply.writeStrongBinder(_result5 != null ? _result5.asBinder() : null);
                        return true;
                    case 29:
                        data.enforceInterface(descriptor);
                        _arg0 = isMultiparty();
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    case 30:
                        ImsCallProfile _arg05;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg05 = (ImsCallProfile) ImsCallProfile.CREATOR.createFromParcel(data);
                        } else {
                            _arg05 = null;
                        }
                        sendRttModifyRequest(_arg05);
                        reply.writeNoException();
                        return true;
                    case 31:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        sendRttModifyResponse(_arg0);
                        reply.writeNoException();
                        return true;
                    case 32:
                        data.enforceInterface(descriptor);
                        sendRttMessage(data.readString());
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IImsCallSession impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IImsCallSession getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void accept(int i, ImsStreamMediaProfile imsStreamMediaProfile) throws RemoteException;

    void close() throws RemoteException;

    void deflect(String str) throws RemoteException;

    void extendToConference(String[] strArr) throws RemoteException;

    String getCallId() throws RemoteException;

    ImsCallProfile getCallProfile() throws RemoteException;

    ImsCallProfile getLocalCallProfile() throws RemoteException;

    String getProperty(String str) throws RemoteException;

    ImsCallProfile getRemoteCallProfile() throws RemoteException;

    int getState() throws RemoteException;

    IImsVideoCallProvider getVideoCallProvider() throws RemoteException;

    void hold(ImsStreamMediaProfile imsStreamMediaProfile) throws RemoteException;

    void inviteParticipants(String[] strArr) throws RemoteException;

    boolean isInCall() throws RemoteException;

    boolean isMultiparty() throws RemoteException;

    void merge() throws RemoteException;

    void reject(int i) throws RemoteException;

    void removeParticipants(String[] strArr) throws RemoteException;

    void resume(ImsStreamMediaProfile imsStreamMediaProfile) throws RemoteException;

    void sendDtmf(char c, Message message) throws RemoteException;

    void sendRttMessage(String str) throws RemoteException;

    void sendRttModifyRequest(ImsCallProfile imsCallProfile) throws RemoteException;

    void sendRttModifyResponse(boolean z) throws RemoteException;

    void sendUssd(String str) throws RemoteException;

    void setListener(IImsCallSessionListener iImsCallSessionListener) throws RemoteException;

    void setMute(boolean z) throws RemoteException;

    void start(String str, ImsCallProfile imsCallProfile) throws RemoteException;

    void startConference(String[] strArr, ImsCallProfile imsCallProfile) throws RemoteException;

    void startDtmf(char c) throws RemoteException;

    void stopDtmf() throws RemoteException;

    void terminate(int i) throws RemoteException;

    void update(int i, ImsStreamMediaProfile imsStreamMediaProfile) throws RemoteException;
}
