package com.android.internal.telecom;

import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.telecom.CallAudioState;
import android.telecom.ConnectionRequest;
import android.telecom.Logging.Session.Info;
import android.telecom.PhoneAccountHandle;
import android.telephony.ims.ImsCallProfile;
import miui.util.HapticFeedbackUtil;

public interface IConnectionService extends IInterface {

    public static class Default implements IConnectionService {
        public void addConnectionServiceAdapter(IConnectionServiceAdapter adapter, Info sessionInfo) throws RemoteException {
        }

        public void removeConnectionServiceAdapter(IConnectionServiceAdapter adapter, Info sessionInfo) throws RemoteException {
        }

        public void createConnection(PhoneAccountHandle connectionManagerPhoneAccount, String callId, ConnectionRequest request, boolean isIncoming, boolean isUnknown, Info sessionInfo) throws RemoteException {
        }

        public void createConnectionComplete(String callId, Info sessionInfo) throws RemoteException {
        }

        public void createConnectionFailed(PhoneAccountHandle connectionManagerPhoneAccount, String callId, ConnectionRequest request, boolean isIncoming, Info sessionInfo) throws RemoteException {
        }

        public void abort(String callId, Info sessionInfo) throws RemoteException {
        }

        public void answerVideo(String callId, int videoState, Info sessionInfo) throws RemoteException {
        }

        public void answer(String callId, Info sessionInfo) throws RemoteException {
        }

        public void deflect(String callId, Uri address, Info sessionInfo) throws RemoteException {
        }

        public void reject(String callId, Info sessionInfo) throws RemoteException {
        }

        public void rejectWithMessage(String callId, String message, Info sessionInfo) throws RemoteException {
        }

        public void disconnect(String callId, Info sessionInfo) throws RemoteException {
        }

        public void silence(String callId, Info sessionInfo) throws RemoteException {
        }

        public void hold(String callId, Info sessionInfo) throws RemoteException {
        }

        public void unhold(String callId, Info sessionInfo) throws RemoteException {
        }

        public void onCallAudioStateChanged(String activeCallId, CallAudioState callAudioState, Info sessionInfo) throws RemoteException {
        }

        public void playDtmfTone(String callId, char digit, Info sessionInfo) throws RemoteException {
        }

        public void stopDtmfTone(String callId, Info sessionInfo) throws RemoteException {
        }

        public void conference(String conferenceCallId, String callId, Info sessionInfo) throws RemoteException {
        }

        public void splitFromConference(String callId, Info sessionInfo) throws RemoteException {
        }

        public void mergeConference(String conferenceCallId, Info sessionInfo) throws RemoteException {
        }

        public void swapConference(String conferenceCallId, Info sessionInfo) throws RemoteException {
        }

        public void onPostDialContinue(String callId, boolean proceed, Info sessionInfo) throws RemoteException {
        }

        public void pullExternalCall(String callId, Info sessionInfo) throws RemoteException {
        }

        public void sendCallEvent(String callId, String event, Bundle extras, Info sessionInfo) throws RemoteException {
        }

        public void onExtrasChanged(String callId, Bundle extras, Info sessionInfo) throws RemoteException {
        }

        public void startRtt(String callId, ParcelFileDescriptor fromInCall, ParcelFileDescriptor toInCall, Info sessionInfo) throws RemoteException {
        }

        public void stopRtt(String callId, Info sessionInfo) throws RemoteException {
        }

        public void respondToRttUpgradeRequest(String callId, ParcelFileDescriptor fromInCall, ParcelFileDescriptor toInCall, Info sessionInfo) throws RemoteException {
        }

        public void addParticipantWithConference(String callId, String recipients) throws RemoteException {
        }

        public void connectionServiceFocusLost(Info sessionInfo) throws RemoteException {
        }

        public void connectionServiceFocusGained(Info sessionInfo) throws RemoteException {
        }

        public void handoverFailed(String callId, ConnectionRequest request, int error, Info sessionInfo) throws RemoteException {
        }

        public void handoverComplete(String callId, Info sessionInfo) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IConnectionService {
        private static final String DESCRIPTOR = "com.android.internal.telecom.IConnectionService";
        static final int TRANSACTION_abort = 6;
        static final int TRANSACTION_addConnectionServiceAdapter = 1;
        static final int TRANSACTION_addParticipantWithConference = 30;
        static final int TRANSACTION_answer = 8;
        static final int TRANSACTION_answerVideo = 7;
        static final int TRANSACTION_conference = 19;
        static final int TRANSACTION_connectionServiceFocusGained = 32;
        static final int TRANSACTION_connectionServiceFocusLost = 31;
        static final int TRANSACTION_createConnection = 3;
        static final int TRANSACTION_createConnectionComplete = 4;
        static final int TRANSACTION_createConnectionFailed = 5;
        static final int TRANSACTION_deflect = 9;
        static final int TRANSACTION_disconnect = 12;
        static final int TRANSACTION_handoverComplete = 34;
        static final int TRANSACTION_handoverFailed = 33;
        static final int TRANSACTION_hold = 14;
        static final int TRANSACTION_mergeConference = 21;
        static final int TRANSACTION_onCallAudioStateChanged = 16;
        static final int TRANSACTION_onExtrasChanged = 26;
        static final int TRANSACTION_onPostDialContinue = 23;
        static final int TRANSACTION_playDtmfTone = 17;
        static final int TRANSACTION_pullExternalCall = 24;
        static final int TRANSACTION_reject = 10;
        static final int TRANSACTION_rejectWithMessage = 11;
        static final int TRANSACTION_removeConnectionServiceAdapter = 2;
        static final int TRANSACTION_respondToRttUpgradeRequest = 29;
        static final int TRANSACTION_sendCallEvent = 25;
        static final int TRANSACTION_silence = 13;
        static final int TRANSACTION_splitFromConference = 20;
        static final int TRANSACTION_startRtt = 27;
        static final int TRANSACTION_stopDtmfTone = 18;
        static final int TRANSACTION_stopRtt = 28;
        static final int TRANSACTION_swapConference = 22;
        static final int TRANSACTION_unhold = 15;

        private static class Proxy implements IConnectionService {
            public static IConnectionService sDefaultImpl;
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

            public void addConnectionServiceAdapter(IConnectionServiceAdapter adapter, Info sessionInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(adapter != null ? adapter.asBinder() : null);
                    if (sessionInfo != null) {
                        _data.writeInt(1);
                        sessionInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().addConnectionServiceAdapter(adapter, sessionInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void removeConnectionServiceAdapter(IConnectionServiceAdapter adapter, Info sessionInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(adapter != null ? adapter.asBinder() : null);
                    if (sessionInfo != null) {
                        _data.writeInt(1);
                        sessionInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().removeConnectionServiceAdapter(adapter, sessionInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void createConnection(PhoneAccountHandle connectionManagerPhoneAccount, String callId, ConnectionRequest request, boolean isIncoming, boolean isUnknown, Info sessionInfo) throws RemoteException {
                Throwable th;
                PhoneAccountHandle phoneAccountHandle = connectionManagerPhoneAccount;
                ConnectionRequest connectionRequest = request;
                Info info = sessionInfo;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (phoneAccountHandle != null) {
                        _data.writeInt(1);
                        connectionManagerPhoneAccount.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeString(callId);
                        if (connectionRequest != null) {
                            _data.writeInt(1);
                            connectionRequest.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        _data.writeInt(isIncoming ? 1 : 0);
                        _data.writeInt(isUnknown ? 1 : 0);
                        if (info != null) {
                            _data.writeInt(1);
                            info.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().createConnection(connectionManagerPhoneAccount, callId, request, isIncoming, isUnknown, sessionInfo);
                        _data.recycle();
                    } catch (Throwable th3) {
                        th = th3;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    String str = callId;
                    _data.recycle();
                    throw th;
                }
            }

            public void createConnectionComplete(String callId, Info sessionInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (sessionInfo != null) {
                        _data.writeInt(1);
                        sessionInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().createConnectionComplete(callId, sessionInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void createConnectionFailed(PhoneAccountHandle connectionManagerPhoneAccount, String callId, ConnectionRequest request, boolean isIncoming, Info sessionInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (connectionManagerPhoneAccount != null) {
                        _data.writeInt(1);
                        connectionManagerPhoneAccount.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callId);
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(isIncoming ? 1 : 0);
                    if (sessionInfo != null) {
                        _data.writeInt(1);
                        sessionInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().createConnectionFailed(connectionManagerPhoneAccount, callId, request, isIncoming, sessionInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void abort(String callId, Info sessionInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (sessionInfo != null) {
                        _data.writeInt(1);
                        sessionInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().abort(callId, sessionInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void answerVideo(String callId, int videoState, Info sessionInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeInt(videoState);
                    if (sessionInfo != null) {
                        _data.writeInt(1);
                        sessionInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(7, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().answerVideo(callId, videoState, sessionInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void answer(String callId, Info sessionInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (sessionInfo != null) {
                        _data.writeInt(1);
                        sessionInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(8, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().answer(callId, sessionInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void deflect(String callId, Uri address, Info sessionInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (address != null) {
                        _data.writeInt(1);
                        address.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (sessionInfo != null) {
                        _data.writeInt(1);
                        sessionInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(9, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().deflect(callId, address, sessionInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void reject(String callId, Info sessionInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (sessionInfo != null) {
                        _data.writeInt(1);
                        sessionInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(10, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().reject(callId, sessionInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void rejectWithMessage(String callId, String message, Info sessionInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeString(message);
                    if (sessionInfo != null) {
                        _data.writeInt(1);
                        sessionInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(11, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().rejectWithMessage(callId, message, sessionInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void disconnect(String callId, Info sessionInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (sessionInfo != null) {
                        _data.writeInt(1);
                        sessionInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(12, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().disconnect(callId, sessionInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void silence(String callId, Info sessionInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (sessionInfo != null) {
                        _data.writeInt(1);
                        sessionInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(13, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().silence(callId, sessionInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void hold(String callId, Info sessionInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (sessionInfo != null) {
                        _data.writeInt(1);
                        sessionInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(14, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().hold(callId, sessionInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void unhold(String callId, Info sessionInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (sessionInfo != null) {
                        _data.writeInt(1);
                        sessionInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(15, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().unhold(callId, sessionInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onCallAudioStateChanged(String activeCallId, CallAudioState callAudioState, Info sessionInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(activeCallId);
                    if (callAudioState != null) {
                        _data.writeInt(1);
                        callAudioState.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (sessionInfo != null) {
                        _data.writeInt(1);
                        sessionInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(16, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onCallAudioStateChanged(activeCallId, callAudioState, sessionInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void playDtmfTone(String callId, char digit, Info sessionInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeInt(digit);
                    if (sessionInfo != null) {
                        _data.writeInt(1);
                        sessionInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(17, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().playDtmfTone(callId, digit, sessionInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void stopDtmfTone(String callId, Info sessionInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (sessionInfo != null) {
                        _data.writeInt(1);
                        sessionInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(18, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().stopDtmfTone(callId, sessionInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void conference(String conferenceCallId, String callId, Info sessionInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(conferenceCallId);
                    _data.writeString(callId);
                    if (sessionInfo != null) {
                        _data.writeInt(1);
                        sessionInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(19, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().conference(conferenceCallId, callId, sessionInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void splitFromConference(String callId, Info sessionInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (sessionInfo != null) {
                        _data.writeInt(1);
                        sessionInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(20, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().splitFromConference(callId, sessionInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void mergeConference(String conferenceCallId, Info sessionInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(conferenceCallId);
                    if (sessionInfo != null) {
                        _data.writeInt(1);
                        sessionInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(21, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().mergeConference(conferenceCallId, sessionInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void swapConference(String conferenceCallId, Info sessionInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(conferenceCallId);
                    if (sessionInfo != null) {
                        _data.writeInt(1);
                        sessionInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(22, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().swapConference(conferenceCallId, sessionInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onPostDialContinue(String callId, boolean proceed, Info sessionInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeInt(proceed ? 1 : 0);
                    if (sessionInfo != null) {
                        _data.writeInt(1);
                        sessionInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(23, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onPostDialContinue(callId, proceed, sessionInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void pullExternalCall(String callId, Info sessionInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (sessionInfo != null) {
                        _data.writeInt(1);
                        sessionInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(24, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().pullExternalCall(callId, sessionInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void sendCallEvent(String callId, String event, Bundle extras, Info sessionInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeString(event);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (sessionInfo != null) {
                        _data.writeInt(1);
                        sessionInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(25, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().sendCallEvent(callId, event, extras, sessionInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onExtrasChanged(String callId, Bundle extras, Info sessionInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (sessionInfo != null) {
                        _data.writeInt(1);
                        sessionInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(26, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onExtrasChanged(callId, extras, sessionInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void startRtt(String callId, ParcelFileDescriptor fromInCall, ParcelFileDescriptor toInCall, Info sessionInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (fromInCall != null) {
                        _data.writeInt(1);
                        fromInCall.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (toInCall != null) {
                        _data.writeInt(1);
                        toInCall.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (sessionInfo != null) {
                        _data.writeInt(1);
                        sessionInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(27, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().startRtt(callId, fromInCall, toInCall, sessionInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void stopRtt(String callId, Info sessionInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (sessionInfo != null) {
                        _data.writeInt(1);
                        sessionInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(28, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().stopRtt(callId, sessionInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void respondToRttUpgradeRequest(String callId, ParcelFileDescriptor fromInCall, ParcelFileDescriptor toInCall, Info sessionInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (fromInCall != null) {
                        _data.writeInt(1);
                        fromInCall.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (toInCall != null) {
                        _data.writeInt(1);
                        toInCall.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (sessionInfo != null) {
                        _data.writeInt(1);
                        sessionInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(29, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().respondToRttUpgradeRequest(callId, fromInCall, toInCall, sessionInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void addParticipantWithConference(String callId, String recipients) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeString(recipients);
                    if (this.mRemote.transact(30, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().addParticipantWithConference(callId, recipients);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void connectionServiceFocusLost(Info sessionInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (sessionInfo != null) {
                        _data.writeInt(1);
                        sessionInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(31, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().connectionServiceFocusLost(sessionInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void connectionServiceFocusGained(Info sessionInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (sessionInfo != null) {
                        _data.writeInt(1);
                        sessionInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(32, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().connectionServiceFocusGained(sessionInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void handoverFailed(String callId, ConnectionRequest request, int error, Info sessionInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(error);
                    if (sessionInfo != null) {
                        _data.writeInt(1);
                        sessionInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(33, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().handoverFailed(callId, request, error, sessionInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void handoverComplete(String callId, Info sessionInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (sessionInfo != null) {
                        _data.writeInt(1);
                        sessionInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(34, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().handoverComplete(callId, sessionInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IConnectionService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IConnectionService)) {
                return new Proxy(obj);
            }
            return (IConnectionService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "addConnectionServiceAdapter";
                case 2:
                    return "removeConnectionServiceAdapter";
                case 3:
                    return "createConnection";
                case 4:
                    return "createConnectionComplete";
                case 5:
                    return "createConnectionFailed";
                case 6:
                    return "abort";
                case 7:
                    return "answerVideo";
                case 8:
                    return "answer";
                case 9:
                    return "deflect";
                case 10:
                    return "reject";
                case 11:
                    return "rejectWithMessage";
                case 12:
                    return "disconnect";
                case 13:
                    return "silence";
                case 14:
                    return HapticFeedbackUtil.EFFECT_KEY_HOLD;
                case 15:
                    return "unhold";
                case 16:
                    return "onCallAudioStateChanged";
                case 17:
                    return "playDtmfTone";
                case 18:
                    return "stopDtmfTone";
                case 19:
                    return ImsCallProfile.EXTRA_CONFERENCE;
                case 20:
                    return "splitFromConference";
                case 21:
                    return "mergeConference";
                case 22:
                    return "swapConference";
                case 23:
                    return "onPostDialContinue";
                case 24:
                    return "pullExternalCall";
                case 25:
                    return "sendCallEvent";
                case 26:
                    return "onExtrasChanged";
                case 27:
                    return "startRtt";
                case 28:
                    return "stopRtt";
                case 29:
                    return "respondToRttUpgradeRequest";
                case 30:
                    return "addParticipantWithConference";
                case 31:
                    return "connectionServiceFocusLost";
                case 32:
                    return "connectionServiceFocusGained";
                case 33:
                    return "handoverFailed";
                case 34:
                    return "handoverComplete";
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
                boolean _arg1 = false;
                IConnectionServiceAdapter _arg0;
                Info _arg12;
                boolean _arg3;
                String _arg02;
                Info _arg2;
                String _arg13;
                Info _arg32;
                ParcelFileDescriptor _arg14;
                ParcelFileDescriptor _arg22;
                Info _arg03;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        _arg0 = com.android.internal.telecom.IConnectionServiceAdapter.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg12 = (Info) Info.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        addConnectionServiceAdapter(_arg0, _arg12);
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        _arg0 = com.android.internal.telecom.IConnectionServiceAdapter.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg12 = (Info) Info.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        removeConnectionServiceAdapter(_arg0, _arg12);
                        return true;
                    case 3:
                        PhoneAccountHandle _arg04;
                        ConnectionRequest _arg23;
                        Info _arg5;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        String _arg15 = data.readString();
                        if (data.readInt() != 0) {
                            _arg23 = (ConnectionRequest) ConnectionRequest.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg23 = null;
                        }
                        _arg3 = data.readInt() != 0;
                        boolean _arg4 = data.readInt() != 0;
                        if (data.readInt() != 0) {
                            _arg5 = (Info) Info.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg5 = null;
                        }
                        createConnection(_arg04, _arg15, _arg23, _arg3, _arg4, _arg5);
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg12 = (Info) Info.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        createConnectionComplete(_arg02, _arg12);
                        return true;
                    case 5:
                        PhoneAccountHandle _arg05;
                        ConnectionRequest _arg24;
                        Info _arg42;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg05 = (PhoneAccountHandle) PhoneAccountHandle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg05 = null;
                        }
                        String _arg16 = data.readString();
                        if (data.readInt() != 0) {
                            _arg24 = (ConnectionRequest) ConnectionRequest.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg24 = null;
                        }
                        _arg3 = data.readInt() != 0;
                        if (data.readInt() != 0) {
                            _arg42 = (Info) Info.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg42 = null;
                        }
                        createConnectionFailed(_arg05, _arg16, _arg24, _arg3, _arg42);
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg12 = (Info) Info.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        abort(_arg02, _arg12);
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        int _arg17 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = (Info) Info.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        answerVideo(_arg02, _arg17, _arg2);
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg12 = (Info) Info.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        answer(_arg02, _arg12);
                        return true;
                    case 9:
                        Uri _arg18;
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg18 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg18 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg2 = (Info) Info.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        deflect(_arg02, _arg18, _arg2);
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg12 = (Info) Info.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        reject(_arg02, _arg12);
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        _arg13 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = (Info) Info.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        rejectWithMessage(_arg02, _arg13, _arg2);
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg12 = (Info) Info.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        disconnect(_arg02, _arg12);
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg12 = (Info) Info.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        silence(_arg02, _arg12);
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg12 = (Info) Info.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        hold(_arg02, _arg12);
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg12 = (Info) Info.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        unhold(_arg02, _arg12);
                        return true;
                    case 16:
                        CallAudioState _arg19;
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg19 = (CallAudioState) CallAudioState.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg19 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg2 = (Info) Info.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        onCallAudioStateChanged(_arg02, _arg19, _arg2);
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        char _arg110 = (char) data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = (Info) Info.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        playDtmfTone(_arg02, _arg110, _arg2);
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg12 = (Info) Info.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        stopDtmfTone(_arg02, _arg12);
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        _arg13 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = (Info) Info.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        conference(_arg02, _arg13, _arg2);
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg12 = (Info) Info.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        splitFromConference(_arg02, _arg12);
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg12 = (Info) Info.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        mergeConference(_arg02, _arg12);
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg12 = (Info) Info.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        swapConference(_arg02, _arg12);
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        _arg13 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        if (data.readInt() != 0) {
                            _arg2 = (Info) Info.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        onPostDialContinue(_arg13, _arg1, _arg2);
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg12 = (Info) Info.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        pullExternalCall(_arg02, _arg12);
                        return true;
                    case 25:
                        Bundle _arg25;
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        _arg13 = data.readString();
                        if (data.readInt() != 0) {
                            _arg25 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg25 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg32 = (Info) Info.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg32 = null;
                        }
                        sendCallEvent(_arg02, _arg13, _arg25, _arg32);
                        return true;
                    case 26:
                        Bundle _arg111;
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg111 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg111 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg2 = (Info) Info.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        onExtrasChanged(_arg02, _arg111, _arg2);
                        return true;
                    case 27:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg14 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg14 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg22 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg22 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg32 = (Info) Info.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg32 = null;
                        }
                        startRtt(_arg02, _arg14, _arg22, _arg32);
                        return true;
                    case 28:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg12 = (Info) Info.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        stopRtt(_arg02, _arg12);
                        return true;
                    case 29:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg14 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg14 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg22 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg22 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg32 = (Info) Info.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg32 = null;
                        }
                        respondToRttUpgradeRequest(_arg02, _arg14, _arg22, _arg32);
                        return true;
                    case 30:
                        parcel.enforceInterface(descriptor);
                        addParticipantWithConference(data.readString(), data.readString());
                        return true;
                    case 31:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (Info) Info.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        connectionServiceFocusLost(_arg03);
                        return true;
                    case 32:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (Info) Info.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        connectionServiceFocusGained(_arg03);
                        return true;
                    case 33:
                        ConnectionRequest _arg112;
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg112 = (ConnectionRequest) ConnectionRequest.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg112 = null;
                        }
                        int _arg26 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg32 = (Info) Info.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg32 = null;
                        }
                        handoverFailed(_arg02, _arg112, _arg26, _arg32);
                        return true;
                    case 34:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readString();
                        if (data.readInt() != 0) {
                            _arg12 = (Info) Info.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        handoverComplete(_arg02, _arg12);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IConnectionService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IConnectionService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void abort(String str, Info info) throws RemoteException;

    void addConnectionServiceAdapter(IConnectionServiceAdapter iConnectionServiceAdapter, Info info) throws RemoteException;

    void addParticipantWithConference(String str, String str2) throws RemoteException;

    void answer(String str, Info info) throws RemoteException;

    void answerVideo(String str, int i, Info info) throws RemoteException;

    void conference(String str, String str2, Info info) throws RemoteException;

    void connectionServiceFocusGained(Info info) throws RemoteException;

    void connectionServiceFocusLost(Info info) throws RemoteException;

    void createConnection(PhoneAccountHandle phoneAccountHandle, String str, ConnectionRequest connectionRequest, boolean z, boolean z2, Info info) throws RemoteException;

    void createConnectionComplete(String str, Info info) throws RemoteException;

    void createConnectionFailed(PhoneAccountHandle phoneAccountHandle, String str, ConnectionRequest connectionRequest, boolean z, Info info) throws RemoteException;

    void deflect(String str, Uri uri, Info info) throws RemoteException;

    void disconnect(String str, Info info) throws RemoteException;

    void handoverComplete(String str, Info info) throws RemoteException;

    void handoverFailed(String str, ConnectionRequest connectionRequest, int i, Info info) throws RemoteException;

    void hold(String str, Info info) throws RemoteException;

    void mergeConference(String str, Info info) throws RemoteException;

    void onCallAudioStateChanged(String str, CallAudioState callAudioState, Info info) throws RemoteException;

    void onExtrasChanged(String str, Bundle bundle, Info info) throws RemoteException;

    void onPostDialContinue(String str, boolean z, Info info) throws RemoteException;

    void playDtmfTone(String str, char c, Info info) throws RemoteException;

    void pullExternalCall(String str, Info info) throws RemoteException;

    void reject(String str, Info info) throws RemoteException;

    void rejectWithMessage(String str, String str2, Info info) throws RemoteException;

    void removeConnectionServiceAdapter(IConnectionServiceAdapter iConnectionServiceAdapter, Info info) throws RemoteException;

    void respondToRttUpgradeRequest(String str, ParcelFileDescriptor parcelFileDescriptor, ParcelFileDescriptor parcelFileDescriptor2, Info info) throws RemoteException;

    void sendCallEvent(String str, String str2, Bundle bundle, Info info) throws RemoteException;

    void silence(String str, Info info) throws RemoteException;

    void splitFromConference(String str, Info info) throws RemoteException;

    void startRtt(String str, ParcelFileDescriptor parcelFileDescriptor, ParcelFileDescriptor parcelFileDescriptor2, Info info) throws RemoteException;

    void stopDtmfTone(String str, Info info) throws RemoteException;

    void stopRtt(String str, Info info) throws RemoteException;

    void swapConference(String str, Info info) throws RemoteException;

    void unhold(String str, Info info) throws RemoteException;
}
