package android.telecom;

import android.net.Uri;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.RemoteException;
import android.telecom.ConnectionRequest.Builder;
import android.telecom.Logging.Session.Info;
import android.telecom.RemoteConference.Callback;
import android.telecom.RemoteConnection.VideoProvider;
import com.android.internal.telecom.IConnectionService;
import com.android.internal.telecom.IConnectionServiceAdapter;
import com.android.internal.telecom.IVideoProvider;
import com.android.internal.telecom.RemoteServiceCallback;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

final class RemoteConnectionService {
    private static final RemoteConference NULL_CONFERENCE;
    private static final RemoteConnection NULL_CONNECTION;
    private final Map<String, RemoteConference> mConferenceById = new HashMap();
    private final Map<String, RemoteConnection> mConnectionById = new HashMap();
    private final DeathRecipient mDeathRecipient = new DeathRecipient() {
        public void binderDied() {
            for (RemoteConnection c : RemoteConnectionService.this.mConnectionById.values()) {
                c.setDestroyed();
            }
            for (RemoteConference c2 : RemoteConnectionService.this.mConferenceById.values()) {
                c2.setDestroyed();
            }
            RemoteConnectionService.this.mConnectionById.clear();
            RemoteConnectionService.this.mConferenceById.clear();
            RemoteConnectionService.this.mPendingConnections.clear();
            RemoteConnectionService.this.mOutgoingConnectionServiceRpc.asBinder().unlinkToDeath(RemoteConnectionService.this.mDeathRecipient, 0);
        }
    };
    private final ConnectionService mOurConnectionServiceImpl;
    private final IConnectionService mOutgoingConnectionServiceRpc;
    private final Set<RemoteConnection> mPendingConnections = new HashSet();
    private final ConnectionServiceAdapterServant mServant = new ConnectionServiceAdapterServant(this.mServantDelegate);
    private final IConnectionServiceAdapter mServantDelegate = new IConnectionServiceAdapter() {
        public void handleCreateConnectionComplete(String id, ConnectionRequest request, ParcelableConnection parcel, Info info) {
            RemoteConnection connection = RemoteConnectionService.this.findConnectionForAction(id, "handleCreateConnectionSuccessful");
            if (connection != RemoteConnectionService.NULL_CONNECTION && RemoteConnectionService.this.mPendingConnections.contains(connection)) {
                RemoteConnectionService.this.mPendingConnections.remove(connection);
                connection.setConnectionCapabilities(parcel.getConnectionCapabilities());
                connection.setConnectionProperties(parcel.getConnectionProperties());
                if (!(parcel.getHandle() == null && parcel.getState() == 6)) {
                    connection.setAddress(parcel.getHandle(), parcel.getHandlePresentation());
                }
                if (!(parcel.getCallerDisplayName() == null && parcel.getState() == 6)) {
                    connection.setCallerDisplayName(parcel.getCallerDisplayName(), parcel.getCallerDisplayNamePresentation());
                }
                if (parcel.getState() == 6) {
                    connection.setDisconnected(parcel.getDisconnectCause());
                } else {
                    connection.setState(parcel.getState());
                }
                List<RemoteConnection> conferenceable = new ArrayList();
                for (String confId : parcel.getConferenceableConnectionIds()) {
                    if (RemoteConnectionService.this.mConnectionById.containsKey(confId)) {
                        conferenceable.add((RemoteConnection) RemoteConnectionService.this.mConnectionById.get(confId));
                    }
                }
                connection.setConferenceableConnections(conferenceable);
                connection.setVideoState(parcel.getVideoState());
                if (connection.getState() == 6) {
                    connection.setDestroyed();
                }
                connection.setStatusHints(parcel.getStatusHints());
                connection.setIsVoipAudioMode(parcel.getIsVoipAudioMode());
                connection.setRingbackRequested(parcel.isRingbackRequested());
                connection.putExtras(parcel.getExtras());
            }
        }

        public void setActive(String callId, Info sessionInfo) {
            String str = "setActive";
            if (RemoteConnectionService.this.mConnectionById.containsKey(callId)) {
                RemoteConnectionService.this.findConnectionForAction(callId, str).setState(4);
            } else {
                RemoteConnectionService.this.findConferenceForAction(callId, str).setState(4);
            }
        }

        public void setRinging(String callId, Info sessionInfo) {
            RemoteConnectionService.this.findConnectionForAction(callId, "setRinging").setState(2);
        }

        public void setDialing(String callId, Info sessionInfo) {
            RemoteConnectionService.this.findConnectionForAction(callId, "setDialing").setState(3);
        }

        public void setPulling(String callId, Info sessionInfo) {
            RemoteConnectionService.this.findConnectionForAction(callId, "setPulling").setState(7);
        }

        public void setDisconnected(String callId, DisconnectCause disconnectCause, Info sessionInfo) {
            String str = "setDisconnected";
            if (RemoteConnectionService.this.mConnectionById.containsKey(callId)) {
                RemoteConnectionService.this.findConnectionForAction(callId, str).setDisconnected(disconnectCause);
            } else {
                RemoteConnectionService.this.findConferenceForAction(callId, str).setDisconnected(disconnectCause);
            }
        }

        public void setOnHold(String callId, Info sessionInfo) {
            String str = "setOnHold";
            if (RemoteConnectionService.this.mConnectionById.containsKey(callId)) {
                RemoteConnectionService.this.findConnectionForAction(callId, str).setState(5);
            } else {
                RemoteConnectionService.this.findConferenceForAction(callId, str).setState(5);
            }
        }

        public void setRingbackRequested(String callId, boolean ringing, Info sessionInfo) {
            RemoteConnectionService.this.findConnectionForAction(callId, "setRingbackRequested").setRingbackRequested(ringing);
        }

        public void setConnectionCapabilities(String callId, int connectionCapabilities, Info sessionInfo) {
            String str = "setConnectionCapabilities";
            if (RemoteConnectionService.this.mConnectionById.containsKey(callId)) {
                RemoteConnectionService.this.findConnectionForAction(callId, str).setConnectionCapabilities(connectionCapabilities);
            } else {
                RemoteConnectionService.this.findConferenceForAction(callId, str).setConnectionCapabilities(connectionCapabilities);
            }
        }

        public void setConnectionProperties(String callId, int connectionProperties, Info sessionInfo) {
            String str = "setConnectionProperties";
            if (RemoteConnectionService.this.mConnectionById.containsKey(callId)) {
                RemoteConnectionService.this.findConnectionForAction(callId, str).setConnectionProperties(connectionProperties);
            } else {
                RemoteConnectionService.this.findConferenceForAction(callId, str).setConnectionProperties(connectionProperties);
            }
        }

        public void setIsConferenced(String callId, String conferenceCallId, Info sessionInfo) {
            RemoteConference conference = "setIsConferenced";
            RemoteConnection connection = RemoteConnectionService.this.findConnectionForAction(callId, conference);
            if (connection == RemoteConnectionService.NULL_CONNECTION) {
                return;
            }
            if (conferenceCallId != null) {
                conference = RemoteConnectionService.this.findConferenceForAction(conferenceCallId, conference);
                if (conference != RemoteConnectionService.NULL_CONFERENCE) {
                    conference.addConnection(connection);
                }
            } else if (connection.getConference() != null) {
                connection.getConference().removeConnection(connection);
            }
        }

        public void setConferenceMergeFailed(String callId, Info sessionInfo) {
        }

        public void onPhoneAccountChanged(String callId, PhoneAccountHandle pHandle, Info sessionInfo) {
        }

        public void onConnectionServiceFocusReleased(Info sessionInfo) {
        }

        public void addConferenceCall(final String callId, ParcelableConference parcel, Info sessionInfo) {
            RemoteConference conference = new RemoteConference(callId, RemoteConnectionService.this.mOutgoingConnectionServiceRpc);
            for (String id : parcel.getConnectionIds()) {
                RemoteConnection c = (RemoteConnection) RemoteConnectionService.this.mConnectionById.get(id);
                if (c != null) {
                    conference.addConnection(c);
                }
            }
            if (conference.getConnections().size() == 0) {
                Log.d((Object) this, "addConferenceCall - skipping", new Object[0]);
                return;
            }
            conference.setState(parcel.getState());
            conference.setConnectionCapabilities(parcel.getConnectionCapabilities());
            conference.setConnectionProperties(parcel.getConnectionProperties());
            conference.putExtras(parcel.getExtras());
            RemoteConnectionService.this.mConferenceById.put(callId, conference);
            Bundle newExtras = new Bundle();
            newExtras.putString(Connection.EXTRA_ORIGINAL_CONNECTION_ID, callId);
            conference.putExtras(newExtras);
            conference.registerCallback(new Callback() {
                public void onDestroyed(RemoteConference c) {
                    RemoteConnectionService.this.mConferenceById.remove(callId);
                    RemoteConnectionService.this.maybeDisconnectAdapter();
                }
            });
            RemoteConnectionService.this.mOurConnectionServiceImpl.addRemoteConference(conference);
        }

        public void removeCall(String callId, Info sessionInfo) {
            String str = "removeCall";
            if (RemoteConnectionService.this.mConnectionById.containsKey(callId)) {
                RemoteConnectionService.this.findConnectionForAction(callId, str).setDestroyed();
            } else {
                RemoteConnectionService.this.findConferenceForAction(callId, str).setDestroyed();
            }
        }

        public void onPostDialWait(String callId, String remaining, Info sessionInfo) {
            RemoteConnectionService.this.findConnectionForAction(callId, "onPostDialWait").setPostDialWait(remaining);
        }

        public void onPostDialChar(String callId, char nextChar, Info sessionInfo) {
            RemoteConnectionService.this.findConnectionForAction(callId, "onPostDialChar").onPostDialChar(nextChar);
        }

        public void queryRemoteConnectionServices(RemoteServiceCallback callback, String callingPackage, Info sessionInfo) {
        }

        public void setVideoProvider(String callId, IVideoProvider videoProvider, Info sessionInfo) {
            String callingPackage = RemoteConnectionService.this.mOurConnectionServiceImpl.getApplicationContext().getOpPackageName();
            int targetSdkVersion = RemoteConnectionService.this.mOurConnectionServiceImpl.getApplicationInfo().targetSdkVersion;
            VideoProvider remoteVideoProvider = null;
            if (videoProvider != null) {
                remoteVideoProvider = new VideoProvider(videoProvider, callingPackage, targetSdkVersion);
            }
            RemoteConnectionService.this.findConnectionForAction(callId, "setVideoProvider").setVideoProvider(remoteVideoProvider);
        }

        public void setVideoState(String callId, int videoState, Info sessionInfo) {
            RemoteConnectionService.this.findConnectionForAction(callId, "setVideoState").setVideoState(videoState);
        }

        public void setIsVoipAudioMode(String callId, boolean isVoip, Info sessionInfo) {
            RemoteConnectionService.this.findConnectionForAction(callId, "setIsVoipAudioMode").setIsVoipAudioMode(isVoip);
        }

        public void setStatusHints(String callId, StatusHints statusHints, Info sessionInfo) {
            RemoteConnectionService.this.findConnectionForAction(callId, "setStatusHints").setStatusHints(statusHints);
        }

        public void setAddress(String callId, Uri address, int presentation, Info sessionInfo) {
            RemoteConnectionService.this.findConnectionForAction(callId, "setAddress").setAddress(address, presentation);
        }

        public void setCallerDisplayName(String callId, String callerDisplayName, int presentation, Info sessionInfo) {
            RemoteConnectionService.this.findConnectionForAction(callId, "setCallerDisplayName").setCallerDisplayName(callerDisplayName, presentation);
        }

        public IBinder asBinder() {
            throw new UnsupportedOperationException();
        }

        public final void setConferenceableConnections(String callId, List<String> conferenceableConnectionIds, Info sessionInfo) {
            List<RemoteConnection> conferenceable = new ArrayList();
            for (String id : conferenceableConnectionIds) {
                if (RemoteConnectionService.this.mConnectionById.containsKey(id)) {
                    conferenceable.add((RemoteConnection) RemoteConnectionService.this.mConnectionById.get(id));
                }
            }
            String id2 = "setConferenceableConnections";
            if (RemoteConnectionService.this.hasConnection(callId)) {
                RemoteConnectionService.this.findConnectionForAction(callId, id2).setConferenceableConnections(conferenceable);
            } else {
                RemoteConnectionService.this.findConferenceForAction(callId, id2).setConferenceableConnections(conferenceable);
            }
        }

        public void addExistingConnection(final String callId, ParcelableConnection connection, Info sessionInfo) {
            String str = callId;
            RemoteConnection remoteConnection = new RemoteConnection(str, RemoteConnectionService.this.mOutgoingConnectionServiceRpc, connection, RemoteConnectionService.this.mOurConnectionServiceImpl.getApplicationContext().getOpPackageName(), RemoteConnectionService.this.mOurConnectionServiceImpl.getApplicationInfo().targetSdkVersion);
            RemoteConnectionService.this.mConnectionById.put(callId, remoteConnection);
            remoteConnection.registerCallback(new RemoteConnection.Callback() {
                public void onDestroyed(RemoteConnection connection) {
                    RemoteConnectionService.this.mConnectionById.remove(callId);
                    RemoteConnectionService.this.maybeDisconnectAdapter();
                }
            });
            RemoteConnectionService.this.mOurConnectionServiceImpl.addRemoteExistingConnection(remoteConnection);
        }

        public void putExtras(String callId, Bundle extras, Info sessionInfo) {
            String str = "putExtras";
            if (RemoteConnectionService.this.hasConnection(callId)) {
                RemoteConnectionService.this.findConnectionForAction(callId, str).putExtras(extras);
            } else {
                RemoteConnectionService.this.findConferenceForAction(callId, str).putExtras(extras);
            }
        }

        public void removeExtras(String callId, List<String> keys, Info sessionInfo) {
            String str = "removeExtra";
            if (RemoteConnectionService.this.hasConnection(callId)) {
                RemoteConnectionService.this.findConnectionForAction(callId, str).removeExtras(keys);
            } else {
                RemoteConnectionService.this.findConferenceForAction(callId, str).removeExtras(keys);
            }
        }

        public void setAudioRoute(String callId, int audioRoute, String bluetoothAddress, Info sessionInfo) {
            RemoteConnectionService.this.hasConnection(callId);
        }

        public void onConnectionEvent(String callId, String event, Bundle extras, Info sessionInfo) {
            if (RemoteConnectionService.this.mConnectionById.containsKey(callId)) {
                RemoteConnectionService.this.findConnectionForAction(callId, "onConnectionEvent").onConnectionEvent(event, extras);
            }
        }

        public void onRttInitiationSuccess(String callId, Info sessionInfo) throws RemoteException {
            if (RemoteConnectionService.this.hasConnection(callId)) {
                RemoteConnectionService.this.findConnectionForAction(callId, "onRttInitiationSuccess").onRttInitiationSuccess();
                return;
            }
            Log.w((Object) this, "onRttInitiationSuccess called on a remote conference", new Object[0]);
        }

        public void onRttInitiationFailure(String callId, int reason, Info sessionInfo) throws RemoteException {
            if (RemoteConnectionService.this.hasConnection(callId)) {
                RemoteConnectionService.this.findConnectionForAction(callId, "onRttInitiationFailure").onRttInitiationFailure(reason);
                return;
            }
            Log.w((Object) this, "onRttInitiationFailure called on a remote conference", new Object[0]);
        }

        public void onRttSessionRemotelyTerminated(String callId, Info sessionInfo) throws RemoteException {
            if (RemoteConnectionService.this.hasConnection(callId)) {
                RemoteConnectionService.this.findConnectionForAction(callId, "onRttSessionRemotelyTerminated").onRttSessionRemotelyTerminated();
                return;
            }
            Log.w((Object) this, "onRttSessionRemotelyTerminated called on a remote conference", new Object[0]);
        }

        public void onRemoteRttRequest(String callId, Info sessionInfo) throws RemoteException {
            if (RemoteConnectionService.this.hasConnection(callId)) {
                RemoteConnectionService.this.findConnectionForAction(callId, "onRemoteRttRequest").onRemoteRttRequest();
                return;
            }
            Log.w((Object) this, "onRemoteRttRequest called on a remote conference", new Object[0]);
        }

        public void resetConnectionTime(String callId, Info sessionInfo) {
        }

        public void setConferenceState(String callId, boolean isConference, Info sessionInfo) {
        }
    };

    static {
        ConnectionRequest connectionRequest = (ConnectionRequest) null;
        String str = WifiEnterpriseConfig.EMPTY_VALUE;
        NULL_CONNECTION = new RemoteConnection(str, null, connectionRequest);
        NULL_CONFERENCE = new RemoteConference(str, null);
    }

    RemoteConnectionService(IConnectionService outgoingConnectionServiceRpc, ConnectionService ourConnectionServiceImpl) throws RemoteException {
        this.mOutgoingConnectionServiceRpc = outgoingConnectionServiceRpc;
        this.mOutgoingConnectionServiceRpc.asBinder().linkToDeath(this.mDeathRecipient, 0);
        this.mOurConnectionServiceImpl = ourConnectionServiceImpl;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[RemoteCS - ");
        stringBuilder.append(this.mOutgoingConnectionServiceRpc.asBinder().toString());
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    /* Access modifiers changed, original: final */
    public final RemoteConnection createRemoteConnection(PhoneAccountHandle connectionManagerPhoneAccount, ConnectionRequest request, boolean isIncoming) {
        final String id = UUID.randomUUID().toString();
        ConnectionRequest newRequest = new Builder().setAccountHandle(request.getAccountHandle()).setAddress(request.getAddress()).setExtras(request.getExtras()).setVideoState(request.getVideoState()).setRttPipeFromInCall(request.getRttPipeFromInCall()).setRttPipeToInCall(request.getRttPipeToInCall()).build();
        try {
            if (this.mConnectionById.isEmpty()) {
                this.mOutgoingConnectionServiceRpc.addConnectionServiceAdapter(this.mServant.getStub(), null);
            }
            RemoteConnection connection = new RemoteConnection(id, this.mOutgoingConnectionServiceRpc, newRequest);
            this.mPendingConnections.add(connection);
            this.mConnectionById.put(id, connection);
            this.mOutgoingConnectionServiceRpc.createConnection(connectionManagerPhoneAccount, id, newRequest, isIncoming, false, null);
            connection.registerCallback(new RemoteConnection.Callback() {
                public void onDestroyed(RemoteConnection connection) {
                    RemoteConnectionService.this.mConnectionById.remove(id);
                    RemoteConnectionService.this.maybeDisconnectAdapter();
                }
            });
            return connection;
        } catch (RemoteException e) {
            return RemoteConnection.failure(new DisconnectCause(1, e.toString()));
        }
    }

    private boolean hasConnection(String callId) {
        return this.mConnectionById.containsKey(callId);
    }

    private RemoteConnection findConnectionForAction(String callId, String action) {
        if (this.mConnectionById.containsKey(callId)) {
            return (RemoteConnection) this.mConnectionById.get(callId);
        }
        Log.w((Object) this, "%s - Cannot find Connection %s", action, callId);
        return NULL_CONNECTION;
    }

    private RemoteConference findConferenceForAction(String callId, String action) {
        if (this.mConferenceById.containsKey(callId)) {
            return (RemoteConference) this.mConferenceById.get(callId);
        }
        Log.w((Object) this, "%s - Cannot find Conference %s", action, callId);
        return NULL_CONFERENCE;
    }

    private void maybeDisconnectAdapter() {
        if (this.mConnectionById.isEmpty() && this.mConferenceById.isEmpty()) {
            try {
                this.mOutgoingConnectionServiceRpc.removeConnectionServiceAdapter(this.mServant.getStub(), null);
            } catch (RemoteException e) {
            }
        }
    }
}
