package com.android.ims.internal;

import android.annotation.UnsupportedAppUsage;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.CallQuality;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsConferenceState;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ImsStreamMediaProfile;
import android.telephony.ims.ImsSuppServiceNotification;

public interface IImsCallSessionListener extends IInterface {

    public static class Default implements IImsCallSessionListener {
        public void callSessionProgressing(IImsCallSession session, ImsStreamMediaProfile profile) throws RemoteException {
        }

        public void callSessionStarted(IImsCallSession session, ImsCallProfile profile) throws RemoteException {
        }

        public void callSessionStartFailed(IImsCallSession session, ImsReasonInfo reasonInfo) throws RemoteException {
        }

        public void callSessionTerminated(IImsCallSession session, ImsReasonInfo reasonInfo) throws RemoteException {
        }

        public void callSessionHeld(IImsCallSession session, ImsCallProfile profile) throws RemoteException {
        }

        public void callSessionHoldFailed(IImsCallSession session, ImsReasonInfo reasonInfo) throws RemoteException {
        }

        public void callSessionHoldReceived(IImsCallSession session, ImsCallProfile profile) throws RemoteException {
        }

        public void callSessionResumed(IImsCallSession session, ImsCallProfile profile) throws RemoteException {
        }

        public void callSessionResumeFailed(IImsCallSession session, ImsReasonInfo reasonInfo) throws RemoteException {
        }

        public void callSessionResumeReceived(IImsCallSession session, ImsCallProfile profile) throws RemoteException {
        }

        public void callSessionMergeStarted(IImsCallSession session, IImsCallSession newSession, ImsCallProfile profile) throws RemoteException {
        }

        public void callSessionMergeComplete(IImsCallSession session) throws RemoteException {
        }

        public void callSessionMergeFailed(IImsCallSession session, ImsReasonInfo reasonInfo) throws RemoteException {
        }

        public void callSessionUpdated(IImsCallSession session, ImsCallProfile profile) throws RemoteException {
        }

        public void callSessionUpdateFailed(IImsCallSession session, ImsReasonInfo reasonInfo) throws RemoteException {
        }

        public void callSessionUpdateReceived(IImsCallSession session, ImsCallProfile profile) throws RemoteException {
        }

        public void callSessionConferenceExtended(IImsCallSession session, IImsCallSession newSession, ImsCallProfile profile) throws RemoteException {
        }

        public void callSessionConferenceExtendFailed(IImsCallSession session, ImsReasonInfo reasonInfo) throws RemoteException {
        }

        public void callSessionConferenceExtendReceived(IImsCallSession session, IImsCallSession newSession, ImsCallProfile profile) throws RemoteException {
        }

        public void callSessionInviteParticipantsRequestDelivered(IImsCallSession session) throws RemoteException {
        }

        public void callSessionInviteParticipantsRequestFailed(IImsCallSession session, ImsReasonInfo reasonInfo) throws RemoteException {
        }

        public void callSessionRemoveParticipantsRequestDelivered(IImsCallSession session) throws RemoteException {
        }

        public void callSessionRemoveParticipantsRequestFailed(IImsCallSession session, ImsReasonInfo reasonInfo) throws RemoteException {
        }

        public void callSessionConferenceStateUpdated(IImsCallSession session, ImsConferenceState state) throws RemoteException {
        }

        public void callSessionUssdMessageReceived(IImsCallSession session, int mode, String ussdMessage) throws RemoteException {
        }

        public void callSessionHandover(IImsCallSession session, int srcAccessTech, int targetAccessTech, ImsReasonInfo reasonInfo) throws RemoteException {
        }

        public void callSessionHandoverFailed(IImsCallSession session, int srcAccessTech, int targetAccessTech, ImsReasonInfo reasonInfo) throws RemoteException {
        }

        public void callSessionMayHandover(IImsCallSession session, int srcAccessTech, int targetAccessTech) throws RemoteException {
        }

        public void callSessionTtyModeReceived(IImsCallSession session, int mode) throws RemoteException {
        }

        public void callSessionMultipartyStateChanged(IImsCallSession session, boolean isMultiParty) throws RemoteException {
        }

        public void callSessionSuppServiceReceived(IImsCallSession session, ImsSuppServiceNotification suppSrvNotification) throws RemoteException {
        }

        public void callSessionRttModifyRequestReceived(IImsCallSession session, ImsCallProfile callProfile) throws RemoteException {
        }

        public void callSessionRttModifyResponseReceived(int status) throws RemoteException {
        }

        public void callSessionRttMessageReceived(String rttMessage) throws RemoteException {
        }

        public void callSessionRttAudioIndicatorChanged(ImsStreamMediaProfile profile) throws RemoteException {
        }

        public void callQualityChanged(CallQuality callQuality) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IImsCallSessionListener {
        private static final String DESCRIPTOR = "com.android.ims.internal.IImsCallSessionListener";
        static final int TRANSACTION_callQualityChanged = 36;
        static final int TRANSACTION_callSessionConferenceExtendFailed = 18;
        static final int TRANSACTION_callSessionConferenceExtendReceived = 19;
        static final int TRANSACTION_callSessionConferenceExtended = 17;
        static final int TRANSACTION_callSessionConferenceStateUpdated = 24;
        static final int TRANSACTION_callSessionHandover = 26;
        static final int TRANSACTION_callSessionHandoverFailed = 27;
        static final int TRANSACTION_callSessionHeld = 5;
        static final int TRANSACTION_callSessionHoldFailed = 6;
        static final int TRANSACTION_callSessionHoldReceived = 7;
        static final int TRANSACTION_callSessionInviteParticipantsRequestDelivered = 20;
        static final int TRANSACTION_callSessionInviteParticipantsRequestFailed = 21;
        static final int TRANSACTION_callSessionMayHandover = 28;
        static final int TRANSACTION_callSessionMergeComplete = 12;
        static final int TRANSACTION_callSessionMergeFailed = 13;
        static final int TRANSACTION_callSessionMergeStarted = 11;
        static final int TRANSACTION_callSessionMultipartyStateChanged = 30;
        static final int TRANSACTION_callSessionProgressing = 1;
        static final int TRANSACTION_callSessionRemoveParticipantsRequestDelivered = 22;
        static final int TRANSACTION_callSessionRemoveParticipantsRequestFailed = 23;
        static final int TRANSACTION_callSessionResumeFailed = 9;
        static final int TRANSACTION_callSessionResumeReceived = 10;
        static final int TRANSACTION_callSessionResumed = 8;
        static final int TRANSACTION_callSessionRttAudioIndicatorChanged = 35;
        static final int TRANSACTION_callSessionRttMessageReceived = 34;
        static final int TRANSACTION_callSessionRttModifyRequestReceived = 32;
        static final int TRANSACTION_callSessionRttModifyResponseReceived = 33;
        static final int TRANSACTION_callSessionStartFailed = 3;
        static final int TRANSACTION_callSessionStarted = 2;
        static final int TRANSACTION_callSessionSuppServiceReceived = 31;
        static final int TRANSACTION_callSessionTerminated = 4;
        static final int TRANSACTION_callSessionTtyModeReceived = 29;
        static final int TRANSACTION_callSessionUpdateFailed = 15;
        static final int TRANSACTION_callSessionUpdateReceived = 16;
        static final int TRANSACTION_callSessionUpdated = 14;
        static final int TRANSACTION_callSessionUssdMessageReceived = 25;

        private static class Proxy implements IImsCallSessionListener {
            public static IImsCallSessionListener sDefaultImpl;
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

            public void callSessionProgressing(IImsCallSession session, ImsStreamMediaProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().callSessionProgressing(session, profile);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void callSessionStarted(IImsCallSession session, ImsCallProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().callSessionStarted(session, profile);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void callSessionStartFailed(IImsCallSession session, ImsReasonInfo reasonInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    if (reasonInfo != null) {
                        _data.writeInt(1);
                        reasonInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().callSessionStartFailed(session, reasonInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void callSessionTerminated(IImsCallSession session, ImsReasonInfo reasonInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    if (reasonInfo != null) {
                        _data.writeInt(1);
                        reasonInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().callSessionTerminated(session, reasonInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void callSessionHeld(IImsCallSession session, ImsCallProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().callSessionHeld(session, profile);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void callSessionHoldFailed(IImsCallSession session, ImsReasonInfo reasonInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    if (reasonInfo != null) {
                        _data.writeInt(1);
                        reasonInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().callSessionHoldFailed(session, reasonInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void callSessionHoldReceived(IImsCallSession session, ImsCallProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(7, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().callSessionHoldReceived(session, profile);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void callSessionResumed(IImsCallSession session, ImsCallProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(8, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().callSessionResumed(session, profile);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void callSessionResumeFailed(IImsCallSession session, ImsReasonInfo reasonInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    if (reasonInfo != null) {
                        _data.writeInt(1);
                        reasonInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(9, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().callSessionResumeFailed(session, reasonInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void callSessionResumeReceived(IImsCallSession session, ImsCallProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(10, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().callSessionResumeReceived(session, profile);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void callSessionMergeStarted(IImsCallSession session, IImsCallSession newSession, ImsCallProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    _data.writeStrongBinder(newSession != null ? newSession.asBinder() : null);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(11, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().callSessionMergeStarted(session, newSession, profile);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void callSessionMergeComplete(IImsCallSession session) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    if (this.mRemote.transact(12, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().callSessionMergeComplete(session);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void callSessionMergeFailed(IImsCallSession session, ImsReasonInfo reasonInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    if (reasonInfo != null) {
                        _data.writeInt(1);
                        reasonInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(13, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().callSessionMergeFailed(session, reasonInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void callSessionUpdated(IImsCallSession session, ImsCallProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(14, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().callSessionUpdated(session, profile);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void callSessionUpdateFailed(IImsCallSession session, ImsReasonInfo reasonInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    if (reasonInfo != null) {
                        _data.writeInt(1);
                        reasonInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(15, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().callSessionUpdateFailed(session, reasonInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void callSessionUpdateReceived(IImsCallSession session, ImsCallProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(16, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().callSessionUpdateReceived(session, profile);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void callSessionConferenceExtended(IImsCallSession session, IImsCallSession newSession, ImsCallProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    _data.writeStrongBinder(newSession != null ? newSession.asBinder() : null);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(17, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().callSessionConferenceExtended(session, newSession, profile);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void callSessionConferenceExtendFailed(IImsCallSession session, ImsReasonInfo reasonInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    if (reasonInfo != null) {
                        _data.writeInt(1);
                        reasonInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(18, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().callSessionConferenceExtendFailed(session, reasonInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void callSessionConferenceExtendReceived(IImsCallSession session, IImsCallSession newSession, ImsCallProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    _data.writeStrongBinder(newSession != null ? newSession.asBinder() : null);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(19, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().callSessionConferenceExtendReceived(session, newSession, profile);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void callSessionInviteParticipantsRequestDelivered(IImsCallSession session) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    if (this.mRemote.transact(20, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().callSessionInviteParticipantsRequestDelivered(session);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void callSessionInviteParticipantsRequestFailed(IImsCallSession session, ImsReasonInfo reasonInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    if (reasonInfo != null) {
                        _data.writeInt(1);
                        reasonInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(21, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().callSessionInviteParticipantsRequestFailed(session, reasonInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void callSessionRemoveParticipantsRequestDelivered(IImsCallSession session) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    if (this.mRemote.transact(22, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().callSessionRemoveParticipantsRequestDelivered(session);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void callSessionRemoveParticipantsRequestFailed(IImsCallSession session, ImsReasonInfo reasonInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    if (reasonInfo != null) {
                        _data.writeInt(1);
                        reasonInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(23, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().callSessionRemoveParticipantsRequestFailed(session, reasonInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void callSessionConferenceStateUpdated(IImsCallSession session, ImsConferenceState state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    if (state != null) {
                        _data.writeInt(1);
                        state.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(24, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().callSessionConferenceStateUpdated(session, state);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void callSessionUssdMessageReceived(IImsCallSession session, int mode, String ussdMessage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    _data.writeInt(mode);
                    _data.writeString(ussdMessage);
                    if (this.mRemote.transact(25, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().callSessionUssdMessageReceived(session, mode, ussdMessage);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void callSessionHandover(IImsCallSession session, int srcAccessTech, int targetAccessTech, ImsReasonInfo reasonInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    _data.writeInt(srcAccessTech);
                    _data.writeInt(targetAccessTech);
                    if (reasonInfo != null) {
                        _data.writeInt(1);
                        reasonInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(26, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().callSessionHandover(session, srcAccessTech, targetAccessTech, reasonInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void callSessionHandoverFailed(IImsCallSession session, int srcAccessTech, int targetAccessTech, ImsReasonInfo reasonInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    _data.writeInt(srcAccessTech);
                    _data.writeInt(targetAccessTech);
                    if (reasonInfo != null) {
                        _data.writeInt(1);
                        reasonInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(27, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().callSessionHandoverFailed(session, srcAccessTech, targetAccessTech, reasonInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void callSessionMayHandover(IImsCallSession session, int srcAccessTech, int targetAccessTech) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    _data.writeInt(srcAccessTech);
                    _data.writeInt(targetAccessTech);
                    if (this.mRemote.transact(28, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().callSessionMayHandover(session, srcAccessTech, targetAccessTech);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void callSessionTtyModeReceived(IImsCallSession session, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    _data.writeInt(mode);
                    if (this.mRemote.transact(29, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().callSessionTtyModeReceived(session, mode);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void callSessionMultipartyStateChanged(IImsCallSession session, boolean isMultiParty) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    _data.writeInt(isMultiParty ? 1 : 0);
                    if (this.mRemote.transact(30, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().callSessionMultipartyStateChanged(session, isMultiParty);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void callSessionSuppServiceReceived(IImsCallSession session, ImsSuppServiceNotification suppSrvNotification) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    if (suppSrvNotification != null) {
                        _data.writeInt(1);
                        suppSrvNotification.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(31, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().callSessionSuppServiceReceived(session, suppSrvNotification);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void callSessionRttModifyRequestReceived(IImsCallSession session, ImsCallProfile callProfile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    if (callProfile != null) {
                        _data.writeInt(1);
                        callProfile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(32, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().callSessionRttModifyRequestReceived(session, callProfile);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void callSessionRttModifyResponseReceived(int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status);
                    if (this.mRemote.transact(33, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().callSessionRttModifyResponseReceived(status);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void callSessionRttMessageReceived(String rttMessage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(rttMessage);
                    if (this.mRemote.transact(34, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().callSessionRttMessageReceived(rttMessage);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void callSessionRttAudioIndicatorChanged(ImsStreamMediaProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(35, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().callSessionRttAudioIndicatorChanged(profile);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void callQualityChanged(CallQuality callQuality) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (callQuality != null) {
                        _data.writeInt(1);
                        callQuality.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(36, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().callQualityChanged(callQuality);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IImsCallSessionListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IImsCallSessionListener)) {
                return new Proxy(obj);
            }
            return (IImsCallSessionListener) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "callSessionProgressing";
                case 2:
                    return "callSessionStarted";
                case 3:
                    return "callSessionStartFailed";
                case 4:
                    return "callSessionTerminated";
                case 5:
                    return "callSessionHeld";
                case 6:
                    return "callSessionHoldFailed";
                case 7:
                    return "callSessionHoldReceived";
                case 8:
                    return "callSessionResumed";
                case 9:
                    return "callSessionResumeFailed";
                case 10:
                    return "callSessionResumeReceived";
                case 11:
                    return "callSessionMergeStarted";
                case 12:
                    return "callSessionMergeComplete";
                case 13:
                    return "callSessionMergeFailed";
                case 14:
                    return "callSessionUpdated";
                case 15:
                    return "callSessionUpdateFailed";
                case 16:
                    return "callSessionUpdateReceived";
                case 17:
                    return "callSessionConferenceExtended";
                case 18:
                    return "callSessionConferenceExtendFailed";
                case 19:
                    return "callSessionConferenceExtendReceived";
                case 20:
                    return "callSessionInviteParticipantsRequestDelivered";
                case 21:
                    return "callSessionInviteParticipantsRequestFailed";
                case 22:
                    return "callSessionRemoveParticipantsRequestDelivered";
                case 23:
                    return "callSessionRemoveParticipantsRequestFailed";
                case 24:
                    return "callSessionConferenceStateUpdated";
                case 25:
                    return "callSessionUssdMessageReceived";
                case 26:
                    return "callSessionHandover";
                case 27:
                    return "callSessionHandoverFailed";
                case 28:
                    return "callSessionMayHandover";
                case 29:
                    return "callSessionTtyModeReceived";
                case 30:
                    return "callSessionMultipartyStateChanged";
                case 31:
                    return "callSessionSuppServiceReceived";
                case 32:
                    return "callSessionRttModifyRequestReceived";
                case 33:
                    return "callSessionRttModifyResponseReceived";
                case 34:
                    return "callSessionRttMessageReceived";
                case 35:
                    return "callSessionRttAudioIndicatorChanged";
                case 36:
                    return "callQualityChanged";
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
                IImsCallSession _arg0;
                ImsCallProfile _arg1;
                ImsReasonInfo _arg12;
                IImsCallSession _arg13;
                ImsCallProfile _arg2;
                int _arg14;
                int _arg22;
                ImsReasonInfo _arg3;
                switch (code) {
                    case 1:
                        ImsStreamMediaProfile _arg15;
                        data.enforceInterface(descriptor);
                        _arg0 = com.android.ims.internal.IImsCallSession.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg15 = (ImsStreamMediaProfile) ImsStreamMediaProfile.CREATOR.createFromParcel(data);
                        } else {
                            _arg15 = null;
                        }
                        callSessionProgressing(_arg0, _arg15);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        _arg0 = com.android.ims.internal.IImsCallSession.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg1 = (ImsCallProfile) ImsCallProfile.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        callSessionStarted(_arg0, _arg1);
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        _arg0 = com.android.ims.internal.IImsCallSession.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg12 = (ImsReasonInfo) ImsReasonInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg12 = null;
                        }
                        callSessionStartFailed(_arg0, _arg12);
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        _arg0 = com.android.ims.internal.IImsCallSession.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg12 = (ImsReasonInfo) ImsReasonInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg12 = null;
                        }
                        callSessionTerminated(_arg0, _arg12);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        _arg0 = com.android.ims.internal.IImsCallSession.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg1 = (ImsCallProfile) ImsCallProfile.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        callSessionHeld(_arg0, _arg1);
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        _arg0 = com.android.ims.internal.IImsCallSession.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg12 = (ImsReasonInfo) ImsReasonInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg12 = null;
                        }
                        callSessionHoldFailed(_arg0, _arg12);
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        _arg0 = com.android.ims.internal.IImsCallSession.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg1 = (ImsCallProfile) ImsCallProfile.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        callSessionHoldReceived(_arg0, _arg1);
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        _arg0 = com.android.ims.internal.IImsCallSession.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg1 = (ImsCallProfile) ImsCallProfile.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        callSessionResumed(_arg0, _arg1);
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        _arg0 = com.android.ims.internal.IImsCallSession.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg12 = (ImsReasonInfo) ImsReasonInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg12 = null;
                        }
                        callSessionResumeFailed(_arg0, _arg12);
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        _arg0 = com.android.ims.internal.IImsCallSession.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg1 = (ImsCallProfile) ImsCallProfile.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        callSessionResumeReceived(_arg0, _arg1);
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        _arg0 = com.android.ims.internal.IImsCallSession.Stub.asInterface(data.readStrongBinder());
                        _arg13 = com.android.ims.internal.IImsCallSession.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg2 = (ImsCallProfile) ImsCallProfile.CREATOR.createFromParcel(data);
                        } else {
                            _arg2 = null;
                        }
                        callSessionMergeStarted(_arg0, _arg13, _arg2);
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        callSessionMergeComplete(com.android.ims.internal.IImsCallSession.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        _arg0 = com.android.ims.internal.IImsCallSession.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg12 = (ImsReasonInfo) ImsReasonInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg12 = null;
                        }
                        callSessionMergeFailed(_arg0, _arg12);
                        return true;
                    case 14:
                        data.enforceInterface(descriptor);
                        _arg0 = com.android.ims.internal.IImsCallSession.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg1 = (ImsCallProfile) ImsCallProfile.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        callSessionUpdated(_arg0, _arg1);
                        return true;
                    case 15:
                        data.enforceInterface(descriptor);
                        _arg0 = com.android.ims.internal.IImsCallSession.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg12 = (ImsReasonInfo) ImsReasonInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg12 = null;
                        }
                        callSessionUpdateFailed(_arg0, _arg12);
                        return true;
                    case 16:
                        data.enforceInterface(descriptor);
                        _arg0 = com.android.ims.internal.IImsCallSession.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg1 = (ImsCallProfile) ImsCallProfile.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        callSessionUpdateReceived(_arg0, _arg1);
                        return true;
                    case 17:
                        data.enforceInterface(descriptor);
                        _arg0 = com.android.ims.internal.IImsCallSession.Stub.asInterface(data.readStrongBinder());
                        _arg13 = com.android.ims.internal.IImsCallSession.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg2 = (ImsCallProfile) ImsCallProfile.CREATOR.createFromParcel(data);
                        } else {
                            _arg2 = null;
                        }
                        callSessionConferenceExtended(_arg0, _arg13, _arg2);
                        return true;
                    case 18:
                        data.enforceInterface(descriptor);
                        _arg0 = com.android.ims.internal.IImsCallSession.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg12 = (ImsReasonInfo) ImsReasonInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg12 = null;
                        }
                        callSessionConferenceExtendFailed(_arg0, _arg12);
                        return true;
                    case 19:
                        data.enforceInterface(descriptor);
                        _arg0 = com.android.ims.internal.IImsCallSession.Stub.asInterface(data.readStrongBinder());
                        _arg13 = com.android.ims.internal.IImsCallSession.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg2 = (ImsCallProfile) ImsCallProfile.CREATOR.createFromParcel(data);
                        } else {
                            _arg2 = null;
                        }
                        callSessionConferenceExtendReceived(_arg0, _arg13, _arg2);
                        return true;
                    case 20:
                        data.enforceInterface(descriptor);
                        callSessionInviteParticipantsRequestDelivered(com.android.ims.internal.IImsCallSession.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 21:
                        data.enforceInterface(descriptor);
                        _arg0 = com.android.ims.internal.IImsCallSession.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg12 = (ImsReasonInfo) ImsReasonInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg12 = null;
                        }
                        callSessionInviteParticipantsRequestFailed(_arg0, _arg12);
                        return true;
                    case 22:
                        data.enforceInterface(descriptor);
                        callSessionRemoveParticipantsRequestDelivered(com.android.ims.internal.IImsCallSession.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 23:
                        data.enforceInterface(descriptor);
                        _arg0 = com.android.ims.internal.IImsCallSession.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg12 = (ImsReasonInfo) ImsReasonInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg12 = null;
                        }
                        callSessionRemoveParticipantsRequestFailed(_arg0, _arg12);
                        return true;
                    case 24:
                        ImsConferenceState _arg16;
                        data.enforceInterface(descriptor);
                        _arg0 = com.android.ims.internal.IImsCallSession.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg16 = (ImsConferenceState) ImsConferenceState.CREATOR.createFromParcel(data);
                        } else {
                            _arg16 = null;
                        }
                        callSessionConferenceStateUpdated(_arg0, _arg16);
                        return true;
                    case 25:
                        data.enforceInterface(descriptor);
                        callSessionUssdMessageReceived(com.android.ims.internal.IImsCallSession.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readString());
                        return true;
                    case 26:
                        data.enforceInterface(descriptor);
                        _arg0 = com.android.ims.internal.IImsCallSession.Stub.asInterface(data.readStrongBinder());
                        _arg14 = data.readInt();
                        _arg22 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg3 = (ImsReasonInfo) ImsReasonInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg3 = null;
                        }
                        callSessionHandover(_arg0, _arg14, _arg22, _arg3);
                        return true;
                    case 27:
                        data.enforceInterface(descriptor);
                        _arg0 = com.android.ims.internal.IImsCallSession.Stub.asInterface(data.readStrongBinder());
                        _arg14 = data.readInt();
                        _arg22 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg3 = (ImsReasonInfo) ImsReasonInfo.CREATOR.createFromParcel(data);
                        } else {
                            _arg3 = null;
                        }
                        callSessionHandoverFailed(_arg0, _arg14, _arg22, _arg3);
                        return true;
                    case 28:
                        data.enforceInterface(descriptor);
                        callSessionMayHandover(com.android.ims.internal.IImsCallSession.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readInt());
                        return true;
                    case 29:
                        data.enforceInterface(descriptor);
                        callSessionTtyModeReceived(com.android.ims.internal.IImsCallSession.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        return true;
                    case 30:
                        data.enforceInterface(descriptor);
                        callSessionMultipartyStateChanged(com.android.ims.internal.IImsCallSession.Stub.asInterface(data.readStrongBinder()), data.readInt() != 0);
                        return true;
                    case 31:
                        ImsSuppServiceNotification _arg17;
                        data.enforceInterface(descriptor);
                        _arg0 = com.android.ims.internal.IImsCallSession.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg17 = (ImsSuppServiceNotification) ImsSuppServiceNotification.CREATOR.createFromParcel(data);
                        } else {
                            _arg17 = null;
                        }
                        callSessionSuppServiceReceived(_arg0, _arg17);
                        return true;
                    case 32:
                        data.enforceInterface(descriptor);
                        _arg0 = com.android.ims.internal.IImsCallSession.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg1 = (ImsCallProfile) ImsCallProfile.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        callSessionRttModifyRequestReceived(_arg0, _arg1);
                        return true;
                    case 33:
                        data.enforceInterface(descriptor);
                        callSessionRttModifyResponseReceived(data.readInt());
                        return true;
                    case 34:
                        data.enforceInterface(descriptor);
                        callSessionRttMessageReceived(data.readString());
                        return true;
                    case 35:
                        ImsStreamMediaProfile _arg02;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (ImsStreamMediaProfile) ImsStreamMediaProfile.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        callSessionRttAudioIndicatorChanged(_arg02);
                        return true;
                    case 36:
                        CallQuality _arg03;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (CallQuality) CallQuality.CREATOR.createFromParcel(data);
                        } else {
                            _arg03 = null;
                        }
                        callQualityChanged(_arg03);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IImsCallSessionListener impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IImsCallSessionListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void callQualityChanged(CallQuality callQuality) throws RemoteException;

    void callSessionConferenceExtendFailed(IImsCallSession iImsCallSession, ImsReasonInfo imsReasonInfo) throws RemoteException;

    void callSessionConferenceExtendReceived(IImsCallSession iImsCallSession, IImsCallSession iImsCallSession2, ImsCallProfile imsCallProfile) throws RemoteException;

    void callSessionConferenceExtended(IImsCallSession iImsCallSession, IImsCallSession iImsCallSession2, ImsCallProfile imsCallProfile) throws RemoteException;

    @UnsupportedAppUsage
    void callSessionConferenceStateUpdated(IImsCallSession iImsCallSession, ImsConferenceState imsConferenceState) throws RemoteException;

    @UnsupportedAppUsage
    void callSessionHandover(IImsCallSession iImsCallSession, int i, int i2, ImsReasonInfo imsReasonInfo) throws RemoteException;

    @UnsupportedAppUsage
    void callSessionHandoverFailed(IImsCallSession iImsCallSession, int i, int i2, ImsReasonInfo imsReasonInfo) throws RemoteException;

    @UnsupportedAppUsage
    void callSessionHeld(IImsCallSession iImsCallSession, ImsCallProfile imsCallProfile) throws RemoteException;

    @UnsupportedAppUsage
    void callSessionHoldFailed(IImsCallSession iImsCallSession, ImsReasonInfo imsReasonInfo) throws RemoteException;

    @UnsupportedAppUsage
    void callSessionHoldReceived(IImsCallSession iImsCallSession, ImsCallProfile imsCallProfile) throws RemoteException;

    @UnsupportedAppUsage
    void callSessionInviteParticipantsRequestDelivered(IImsCallSession iImsCallSession) throws RemoteException;

    @UnsupportedAppUsage
    void callSessionInviteParticipantsRequestFailed(IImsCallSession iImsCallSession, ImsReasonInfo imsReasonInfo) throws RemoteException;

    void callSessionMayHandover(IImsCallSession iImsCallSession, int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    void callSessionMergeComplete(IImsCallSession iImsCallSession) throws RemoteException;

    @UnsupportedAppUsage
    void callSessionMergeFailed(IImsCallSession iImsCallSession, ImsReasonInfo imsReasonInfo) throws RemoteException;

    @UnsupportedAppUsage
    void callSessionMergeStarted(IImsCallSession iImsCallSession, IImsCallSession iImsCallSession2, ImsCallProfile imsCallProfile) throws RemoteException;

    @UnsupportedAppUsage
    void callSessionMultipartyStateChanged(IImsCallSession iImsCallSession, boolean z) throws RemoteException;

    @UnsupportedAppUsage
    void callSessionProgressing(IImsCallSession iImsCallSession, ImsStreamMediaProfile imsStreamMediaProfile) throws RemoteException;

    void callSessionRemoveParticipantsRequestDelivered(IImsCallSession iImsCallSession) throws RemoteException;

    void callSessionRemoveParticipantsRequestFailed(IImsCallSession iImsCallSession, ImsReasonInfo imsReasonInfo) throws RemoteException;

    @UnsupportedAppUsage
    void callSessionResumeFailed(IImsCallSession iImsCallSession, ImsReasonInfo imsReasonInfo) throws RemoteException;

    @UnsupportedAppUsage
    void callSessionResumeReceived(IImsCallSession iImsCallSession, ImsCallProfile imsCallProfile) throws RemoteException;

    @UnsupportedAppUsage
    void callSessionResumed(IImsCallSession iImsCallSession, ImsCallProfile imsCallProfile) throws RemoteException;

    void callSessionRttAudioIndicatorChanged(ImsStreamMediaProfile imsStreamMediaProfile) throws RemoteException;

    void callSessionRttMessageReceived(String str) throws RemoteException;

    void callSessionRttModifyRequestReceived(IImsCallSession iImsCallSession, ImsCallProfile imsCallProfile) throws RemoteException;

    void callSessionRttModifyResponseReceived(int i) throws RemoteException;

    @UnsupportedAppUsage
    void callSessionStartFailed(IImsCallSession iImsCallSession, ImsReasonInfo imsReasonInfo) throws RemoteException;

    @UnsupportedAppUsage
    void callSessionStarted(IImsCallSession iImsCallSession, ImsCallProfile imsCallProfile) throws RemoteException;

    @UnsupportedAppUsage
    void callSessionSuppServiceReceived(IImsCallSession iImsCallSession, ImsSuppServiceNotification imsSuppServiceNotification) throws RemoteException;

    @UnsupportedAppUsage
    void callSessionTerminated(IImsCallSession iImsCallSession, ImsReasonInfo imsReasonInfo) throws RemoteException;

    @UnsupportedAppUsage
    void callSessionTtyModeReceived(IImsCallSession iImsCallSession, int i) throws RemoteException;

    void callSessionUpdateFailed(IImsCallSession iImsCallSession, ImsReasonInfo imsReasonInfo) throws RemoteException;

    void callSessionUpdateReceived(IImsCallSession iImsCallSession, ImsCallProfile imsCallProfile) throws RemoteException;

    @UnsupportedAppUsage
    void callSessionUpdated(IImsCallSession iImsCallSession, ImsCallProfile imsCallProfile) throws RemoteException;

    void callSessionUssdMessageReceived(IImsCallSession iImsCallSession, int i, String str) throws RemoteException;
}
