package android.telecom;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telecom.InCallService.VideoCall;
import com.android.internal.telephony.IccCardConstants;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public final class Call {
    @Deprecated
    public static final String AVAILABLE_PHONE_ACCOUNTS = "selectPhoneAccountAccounts";
    public static final String EVENT_HANDOVER_COMPLETE = "android.telecom.event.HANDOVER_COMPLETE";
    public static final String EVENT_HANDOVER_FAILED = "android.telecom.event.HANDOVER_FAILED";
    public static final String EVENT_HANDOVER_SOURCE_DISCONNECTED = "android.telecom.event.HANDOVER_SOURCE_DISCONNECTED";
    public static final String EVENT_REQUEST_HANDOVER = "android.telecom.event.REQUEST_HANDOVER";
    public static final String EXTRA_HANDOVER_EXTRAS = "android.telecom.extra.HANDOVER_EXTRAS";
    public static final String EXTRA_HANDOVER_PHONE_ACCOUNT_HANDLE = "android.telecom.extra.HANDOVER_PHONE_ACCOUNT_HANDLE";
    public static final String EXTRA_HANDOVER_VIDEO_STATE = "android.telecom.extra.HANDOVER_VIDEO_STATE";
    public static final String EXTRA_LAST_EMERGENCY_CALLBACK_TIME_MILLIS = "android.telecom.extra.LAST_EMERGENCY_CALLBACK_TIME_MILLIS";
    public static final String EXTRA_SILENT_RINGING_REQUESTED = "android.telecom.extra.SILENT_RINGING_REQUESTED";
    public static final String EXTRA_SUGGESTED_PHONE_ACCOUNTS = "android.telecom.extra.SUGGESTED_PHONE_ACCOUNTS";
    public static final int STATE_ACTIVE = 4;
    public static final int STATE_CONNECTING = 9;
    public static final int STATE_DIALING = 1;
    public static final int STATE_DISCONNECTED = 7;
    public static final int STATE_DISCONNECTING = 10;
    public static final int STATE_HOLDING = 3;
    public static final int STATE_NEW = 0;
    @SystemApi
    @Deprecated
    public static final int STATE_PRE_DIAL_WAIT = 8;
    public static final int STATE_PULLING_CALL = 11;
    public static final int STATE_RINGING = 2;
    public static final int STATE_SELECT_PHONE_ACCOUNT = 8;
    private final List<CallbackRecord<Callback>> mCallbackRecords = new CopyOnWriteArrayList();
    private String mCallingPackage;
    private List<String> mCannedTextResponses = null;
    private final List<Call> mChildren = new ArrayList();
    private boolean mChildrenCached;
    private final List<String> mChildrenIds = new ArrayList();
    private final List<Call> mConferenceableCalls = new ArrayList();
    private Details mDetails;
    private Bundle mExtras;
    private final InCallAdapter mInCallAdapter;
    private String mParentId = null;
    private final Phone mPhone;
    private String mRemainingPostDialSequence;
    private RttCall mRttCall;
    private int mState;
    private int mTargetSdkVersion;
    private final String mTelecomCallId;
    private final List<Call> mUnmodifiableChildren = Collections.unmodifiableList(this.mChildren);
    private final List<Call> mUnmodifiableConferenceableCalls = Collections.unmodifiableList(this.mConferenceableCalls);
    private VideoCallImpl mVideoCallImpl;

    public static abstract class Callback {
        public static final int HANDOVER_FAILURE_DEST_APP_REJECTED = 1;
        public static final int HANDOVER_FAILURE_NOT_SUPPORTED = 2;
        public static final int HANDOVER_FAILURE_ONGOING_EMERGENCY_CALL = 4;
        public static final int HANDOVER_FAILURE_UNKNOWN = 5;
        public static final int HANDOVER_FAILURE_USER_REJECTED = 3;

        @Retention(RetentionPolicy.SOURCE)
        public @interface HandoverFailureErrors {
        }

        public void onStateChanged(Call call, int state) {
        }

        public void onParentChanged(Call call, Call parent) {
        }

        public void onChildrenChanged(Call call, List<Call> list) {
        }

        public void onDetailsChanged(Call call, Details details) {
        }

        public void onCannedTextResponsesLoaded(Call call, List<String> list) {
        }

        public void onPostDialWait(Call call, String remainingPostDialSequence) {
        }

        public void onVideoCallChanged(Call call, VideoCall videoCall) {
        }

        public void onCallDestroyed(Call call) {
        }

        public void onConferenceableCallsChanged(Call call, List<Call> list) {
        }

        public void onConnectionEvent(Call call, String event, Bundle extras) {
        }

        public void onRttModeChanged(Call call, int mode) {
        }

        public void onRttStatusChanged(Call call, boolean enabled, RttCall rttCall) {
        }

        public void onRttRequest(Call call, int id) {
        }

        public void onRttInitiationFailure(Call call, int reason) {
        }

        public void onHandoverComplete(Call call) {
        }

        public void onHandoverFailed(Call call, int failureReason) {
        }
    }

    public static class Details {
        public static final int CAPABILITY_ADD_PARTICIPANT = 33554432;
        public static final int CAPABILITY_CANNOT_DOWNGRADE_VIDEO_TO_AUDIO = 4194304;
        public static final int CAPABILITY_CAN_PAUSE_VIDEO = 1048576;
        public static final int CAPABILITY_CAN_PULL_CALL = 8388608;
        public static final int CAPABILITY_CAN_SEND_RESPONSE_VIA_CONNECTION = 2097152;
        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 119305590)
        public static final int CAPABILITY_CAN_UPGRADE_TO_VIDEO = 524288;
        public static final int CAPABILITY_DISCONNECT_FROM_CONFERENCE = 8192;
        public static final int CAPABILITY_HOLD = 1;
        public static final int CAPABILITY_MANAGE_CONFERENCE = 128;
        public static final int CAPABILITY_MERGE_CONFERENCE = 4;
        public static final int CAPABILITY_MUTE = 64;
        public static final int CAPABILITY_RESPOND_VIA_TEXT = 32;
        public static final int CAPABILITY_SEPARATE_FROM_CONFERENCE = 4096;
        public static final int CAPABILITY_SPEED_UP_MT_AUDIO = 262144;
        public static final int CAPABILITY_SUPPORTS_RTT_REMOTE = 67108864;
        public static final int CAPABILITY_SUPPORTS_VT_LOCAL_BIDIRECTIONAL = 768;
        public static final int CAPABILITY_SUPPORTS_VT_LOCAL_RX = 256;
        public static final int CAPABILITY_SUPPORTS_VT_LOCAL_TX = 512;
        public static final int CAPABILITY_SUPPORTS_VT_REMOTE_BIDIRECTIONAL = 3072;
        public static final int CAPABILITY_SUPPORTS_VT_REMOTE_RX = 1024;
        public static final int CAPABILITY_SUPPORTS_VT_REMOTE_TX = 2048;
        public static final int CAPABILITY_SUPPORT_DEFLECT = 16777216;
        public static final int CAPABILITY_SUPPORT_HOLD = 2;
        public static final int CAPABILITY_SWAP_CONFERENCE = 8;
        public static final int CAPABILITY_UNUSED_1 = 16;
        public static final int DIRECTION_INCOMING = 0;
        public static final int DIRECTION_OUTGOING = 1;
        public static final int DIRECTION_UNKNOWN = -1;
        public static final int PROPERTY_ASSISTED_DIALING_USED = 512;
        public static final int PROPERTY_CONFERENCE = 1;
        public static final int PROPERTY_EMERGENCY_CALLBACK_MODE = 4;
        public static final int PROPERTY_ENTERPRISE_CALL = 32;
        public static final int PROPERTY_GENERIC_CONFERENCE = 2;
        public static final int PROPERTY_HAS_CDMA_VOICE_PRIVACY = 128;
        public static final int PROPERTY_HIGH_DEF_AUDIO = 16;
        public static final int PROPERTY_IS_EXTERNAL_CALL = 64;
        public static final int PROPERTY_NETWORK_IDENTIFIED_EMERGENCY_CALL = 2048;
        public static final int PROPERTY_RTT = 1024;
        public static final int PROPERTY_SELF_MANAGED = 256;
        public static final int PROPERTY_VOIP_AUDIO_MODE = 4096;
        public static final int PROPERTY_WIFI = 8;
        private final PhoneAccountHandle mAccountHandle;
        private final int mCallCapabilities;
        private final int mCallDirection;
        private final int mCallProperties;
        private final String mCallerDisplayName;
        private final int mCallerDisplayNamePresentation;
        private final long mConnectTimeMillis;
        private final long mCreationTimeMillis;
        private final DisconnectCause mDisconnectCause;
        private final Bundle mExtras;
        private final GatewayInfo mGatewayInfo;
        private final Uri mHandle;
        private final int mHandlePresentation;
        private final Bundle mIntentExtras;
        private final StatusHints mStatusHints;
        private final int mSupportedAudioRoutes = 15;
        private final String mTelecomCallId;
        private final int mVideoState;

        @Retention(RetentionPolicy.SOURCE)
        public @interface CallDirection {
        }

        public static boolean can(int capabilities, int capability) {
            return (capabilities & capability) == capability;
        }

        public boolean can(int capability) {
            return can(this.mCallCapabilities, capability);
        }

        public static String capabilitiesToString(int capabilities) {
            StringBuilder builder = new StringBuilder();
            builder.append("[Capabilities:");
            if (can(capabilities, 1)) {
                builder.append(" CAPABILITY_HOLD");
            }
            if (can(capabilities, 2)) {
                builder.append(" CAPABILITY_SUPPORT_HOLD");
            }
            if (can(capabilities, 4)) {
                builder.append(" CAPABILITY_MERGE_CONFERENCE");
            }
            if (can(capabilities, 8)) {
                builder.append(" CAPABILITY_SWAP_CONFERENCE");
            }
            if (can(capabilities, 32)) {
                builder.append(" CAPABILITY_RESPOND_VIA_TEXT");
            }
            if (can(capabilities, 64)) {
                builder.append(" CAPABILITY_MUTE");
            }
            if (can(capabilities, 128)) {
                builder.append(" CAPABILITY_MANAGE_CONFERENCE");
            }
            if (can(capabilities, 256)) {
                builder.append(" CAPABILITY_SUPPORTS_VT_LOCAL_RX");
            }
            if (can(capabilities, 512)) {
                builder.append(" CAPABILITY_SUPPORTS_VT_LOCAL_TX");
            }
            if (can(capabilities, 768)) {
                builder.append(" CAPABILITY_SUPPORTS_VT_LOCAL_BIDIRECTIONAL");
            }
            if (can(capabilities, 1024)) {
                builder.append(" CAPABILITY_SUPPORTS_VT_REMOTE_RX");
            }
            if (can(capabilities, 2048)) {
                builder.append(" CAPABILITY_SUPPORTS_VT_REMOTE_TX");
            }
            if (can(capabilities, 4194304)) {
                builder.append(" CAPABILITY_CANNOT_DOWNGRADE_VIDEO_TO_AUDIO");
            }
            if (can(capabilities, 3072)) {
                builder.append(" CAPABILITY_SUPPORTS_VT_REMOTE_BIDIRECTIONAL");
            }
            if (can(capabilities, 262144)) {
                builder.append(" CAPABILITY_SPEED_UP_MT_AUDIO");
            }
            if (can(capabilities, 524288)) {
                builder.append(" CAPABILITY_CAN_UPGRADE_TO_VIDEO");
            }
            if (can(capabilities, 1048576)) {
                builder.append(" CAPABILITY_CAN_PAUSE_VIDEO");
            }
            if (can(capabilities, 8388608)) {
                builder.append(" CAPABILITY_CAN_PULL_CALL");
            }
            if (can(capabilities, 33554432)) {
                builder.append(" CAPABILITY_ADD_PARTICIPANT");
            }
            if (can(capabilities, 16777216)) {
                builder.append(" CAPABILITY_SUPPORT_DEFLECT");
            }
            if (can(capabilities, 67108864)) {
                builder.append(" CAPABILITY_SUPPORTS_RTT_REMOTE");
            }
            builder.append("]");
            return builder.toString();
        }

        public static boolean hasProperty(int properties, int property) {
            return (properties & property) == property;
        }

        public boolean hasProperty(int property) {
            return hasProperty(this.mCallProperties, property);
        }

        public static String propertiesToString(int properties) {
            StringBuilder builder = new StringBuilder();
            builder.append("[Properties:");
            if (hasProperty(properties, 1)) {
                builder.append(" PROPERTY_CONFERENCE");
            }
            if (hasProperty(properties, 2)) {
                builder.append(" PROPERTY_GENERIC_CONFERENCE");
            }
            if (hasProperty(properties, 8)) {
                builder.append(" PROPERTY_WIFI");
            }
            if (hasProperty(properties, 16)) {
                builder.append(" PROPERTY_HIGH_DEF_AUDIO");
            }
            if (hasProperty(properties, 4)) {
                builder.append(" PROPERTY_EMERGENCY_CALLBACK_MODE");
            }
            if (hasProperty(properties, 64)) {
                builder.append(" PROPERTY_IS_EXTERNAL_CALL");
            }
            if (hasProperty(properties, 128)) {
                builder.append(" PROPERTY_HAS_CDMA_VOICE_PRIVACY");
            }
            if (hasProperty(properties, 512)) {
                builder.append(" PROPERTY_ASSISTED_DIALING_USED");
            }
            if (hasProperty(properties, 2048)) {
                builder.append(" PROPERTY_NETWORK_IDENTIFIED_EMERGENCY_CALL");
            }
            if (hasProperty(properties, 1024)) {
                builder.append(" PROPERTY_RTT");
            }
            if (hasProperty(properties, 4096)) {
                builder.append(" PROPERTY_VOIP_AUDIO_MODE");
            }
            builder.append("]");
            return builder.toString();
        }

        public String getTelecomCallId() {
            return this.mTelecomCallId;
        }

        public Uri getHandle() {
            return this.mHandle;
        }

        public int getHandlePresentation() {
            return this.mHandlePresentation;
        }

        public String getCallerDisplayName() {
            return this.mCallerDisplayName;
        }

        public int getCallerDisplayNamePresentation() {
            return this.mCallerDisplayNamePresentation;
        }

        public PhoneAccountHandle getAccountHandle() {
            return this.mAccountHandle;
        }

        public int getCallCapabilities() {
            return this.mCallCapabilities;
        }

        public int getCallProperties() {
            return this.mCallProperties;
        }

        public int getSupportedAudioRoutes() {
            return 15;
        }

        public DisconnectCause getDisconnectCause() {
            return this.mDisconnectCause;
        }

        public final long getConnectTimeMillis() {
            return this.mConnectTimeMillis;
        }

        public GatewayInfo getGatewayInfo() {
            return this.mGatewayInfo;
        }

        public int getVideoState() {
            return this.mVideoState;
        }

        public StatusHints getStatusHints() {
            return this.mStatusHints;
        }

        public Bundle getExtras() {
            return this.mExtras;
        }

        public Bundle getIntentExtras() {
            return this.mIntentExtras;
        }

        public long getCreationTimeMillis() {
            return this.mCreationTimeMillis;
        }

        public int getCallDirection() {
            return this.mCallDirection;
        }

        public boolean equals(Object o) {
            boolean z = false;
            if (!(o instanceof Details)) {
                return false;
            }
            Details d = (Details) o;
            if (Objects.equals(this.mHandle, d.mHandle) && Objects.equals(Integer.valueOf(this.mHandlePresentation), Integer.valueOf(d.mHandlePresentation)) && Objects.equals(this.mCallerDisplayName, d.mCallerDisplayName) && Objects.equals(Integer.valueOf(this.mCallerDisplayNamePresentation), Integer.valueOf(d.mCallerDisplayNamePresentation)) && Objects.equals(this.mAccountHandle, d.mAccountHandle) && Objects.equals(Integer.valueOf(this.mCallCapabilities), Integer.valueOf(d.mCallCapabilities)) && Objects.equals(Integer.valueOf(this.mCallProperties), Integer.valueOf(d.mCallProperties)) && Objects.equals(this.mDisconnectCause, d.mDisconnectCause) && Objects.equals(Long.valueOf(this.mConnectTimeMillis), Long.valueOf(d.mConnectTimeMillis)) && Objects.equals(this.mGatewayInfo, d.mGatewayInfo) && Objects.equals(Integer.valueOf(this.mVideoState), Integer.valueOf(d.mVideoState)) && Objects.equals(this.mStatusHints, d.mStatusHints) && Call.areBundlesEqual(this.mExtras, d.mExtras) && Call.areBundlesEqual(this.mIntentExtras, d.mIntentExtras) && Objects.equals(Long.valueOf(this.mCreationTimeMillis), Long.valueOf(d.mCreationTimeMillis)) && Objects.equals(Integer.valueOf(this.mCallDirection), Integer.valueOf(d.mCallDirection))) {
                z = true;
            }
            return z;
        }

        public int hashCode() {
            return Objects.hash(new Object[]{this.mHandle, Integer.valueOf(this.mHandlePresentation), this.mCallerDisplayName, Integer.valueOf(this.mCallerDisplayNamePresentation), this.mAccountHandle, Integer.valueOf(this.mCallCapabilities), Integer.valueOf(this.mCallProperties), this.mDisconnectCause, Long.valueOf(this.mConnectTimeMillis), this.mGatewayInfo, Integer.valueOf(this.mVideoState), this.mStatusHints, this.mExtras, this.mIntentExtras, Long.valueOf(this.mCreationTimeMillis), Integer.valueOf(this.mCallDirection)});
        }

        public Details(String telecomCallId, Uri handle, int handlePresentation, String callerDisplayName, int callerDisplayNamePresentation, PhoneAccountHandle accountHandle, int capabilities, int properties, DisconnectCause disconnectCause, long connectTimeMillis, GatewayInfo gatewayInfo, int videoState, StatusHints statusHints, Bundle extras, Bundle intentExtras, long creationTimeMillis, int callDirection) {
            this.mTelecomCallId = telecomCallId;
            this.mHandle = handle;
            this.mHandlePresentation = handlePresentation;
            this.mCallerDisplayName = callerDisplayName;
            this.mCallerDisplayNamePresentation = callerDisplayNamePresentation;
            this.mAccountHandle = accountHandle;
            this.mCallCapabilities = capabilities;
            this.mCallProperties = properties;
            this.mDisconnectCause = disconnectCause;
            this.mConnectTimeMillis = connectTimeMillis;
            this.mGatewayInfo = gatewayInfo;
            this.mVideoState = videoState;
            this.mStatusHints = statusHints;
            this.mExtras = extras;
            this.mIntentExtras = intentExtras;
            this.mCreationTimeMillis = creationTimeMillis;
            this.mCallDirection = callDirection;
        }

        public static Details createFromParcelableCall(ParcelableCall parcelableCall) {
            return new Details(parcelableCall.getId(), parcelableCall.getHandle(), parcelableCall.getHandlePresentation(), parcelableCall.getCallerDisplayName(), parcelableCall.getCallerDisplayNamePresentation(), parcelableCall.getAccountHandle(), parcelableCall.getCapabilities(), parcelableCall.getProperties(), parcelableCall.getDisconnectCause(), parcelableCall.getConnectTimeMillis(), parcelableCall.getGatewayInfo(), parcelableCall.getVideoState(), parcelableCall.getStatusHints(), parcelableCall.getExtras(), parcelableCall.getIntentExtras(), parcelableCall.getCreationTimeMillis(), parcelableCall.getCallDirection());
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("[id: ");
            sb.append(this.mTelecomCallId);
            sb.append(", pa: ");
            sb.append(this.mAccountHandle);
            sb.append(", hdl: ");
            sb.append(Log.piiHandle(this.mHandle));
            sb.append(", hdlPres: ");
            sb.append(this.mHandlePresentation);
            sb.append(", videoState: ");
            sb.append(VideoProfile.videoStateToString(this.mVideoState));
            sb.append(", caps: ");
            sb.append(capabilitiesToString(this.mCallCapabilities));
            sb.append(", props: ");
            sb.append(propertiesToString(this.mCallProperties));
            sb.append("]");
            return sb.toString();
        }
    }

    @SystemApi
    @Deprecated
    public static abstract class Listener extends Callback {
    }

    public static final class RttCall {
        private static final int READ_BUFFER_SIZE = 1000;
        public static final int RTT_MODE_FULL = 1;
        public static final int RTT_MODE_HCO = 2;
        public static final int RTT_MODE_INVALID = 0;
        public static final int RTT_MODE_VCO = 3;
        private final InCallAdapter mInCallAdapter;
        private char[] mReadBuffer = new char[1000];
        private InputStreamReader mReceiveStream;
        private int mRttMode;
        private final String mTelecomCallId;
        private OutputStreamWriter mTransmitStream;

        @Retention(RetentionPolicy.SOURCE)
        public @interface RttAudioMode {
        }

        public RttCall(String telecomCallId, InputStreamReader receiveStream, OutputStreamWriter transmitStream, int mode, InCallAdapter inCallAdapter) {
            this.mTelecomCallId = telecomCallId;
            this.mReceiveStream = receiveStream;
            this.mTransmitStream = transmitStream;
            this.mRttMode = mode;
            this.mInCallAdapter = inCallAdapter;
        }

        public int getRttAudioMode() {
            return this.mRttMode;
        }

        public void setRttMode(int mode) {
            this.mInCallAdapter.setRttMode(this.mTelecomCallId, mode);
        }

        public void write(String input) throws IOException {
            this.mTransmitStream.write(input);
            this.mTransmitStream.flush();
        }

        public String read() {
            try {
                int numRead = this.mReceiveStream.read(this.mReadBuffer, 0, 1000);
                if (numRead < 0) {
                    return null;
                }
                return new String(this.mReadBuffer, 0, numRead);
            } catch (IOException e) {
                Log.w((Object) this, "Exception encountered when reading from InputStreamReader: %s", e);
                return null;
            }
        }

        public String readImmediately() throws IOException {
            if (!this.mReceiveStream.ready()) {
                return null;
            }
            int numRead = this.mReceiveStream.read(this.mReadBuffer, 0, 1000);
            if (numRead < 0) {
                return null;
            }
            return new String(this.mReadBuffer, 0, numRead);
        }

        public void close() {
            try {
                this.mReceiveStream.close();
            } catch (IOException e) {
            }
            try {
                this.mTransmitStream.close();
            } catch (IOException e2) {
            }
        }
    }

    public String getRemainingPostDialSequence() {
        return this.mRemainingPostDialSequence;
    }

    public void answer(int videoState) {
        this.mInCallAdapter.answerCall(this.mTelecomCallId, videoState);
    }

    public void deflect(Uri address) {
        this.mInCallAdapter.deflectCall(this.mTelecomCallId, address);
    }

    public void reject(boolean rejectWithMessage, String textMessage) {
        this.mInCallAdapter.rejectCall(this.mTelecomCallId, rejectWithMessage, textMessage);
    }

    public void disconnect() {
        this.mInCallAdapter.disconnectCall(this.mTelecomCallId);
    }

    public void hold() {
        this.mInCallAdapter.holdCall(this.mTelecomCallId);
    }

    public void unhold() {
        this.mInCallAdapter.unholdCall(this.mTelecomCallId);
    }

    public void playDtmfTone(char digit) {
        this.mInCallAdapter.playDtmfTone(this.mTelecomCallId, digit);
    }

    public void stopDtmfTone() {
        this.mInCallAdapter.stopDtmfTone(this.mTelecomCallId);
    }

    public void postDialContinue(boolean proceed) {
        this.mInCallAdapter.postDialContinue(this.mTelecomCallId, proceed);
    }

    public void phoneAccountSelected(PhoneAccountHandle accountHandle, boolean setDefault) {
        this.mInCallAdapter.phoneAccountSelected(this.mTelecomCallId, accountHandle, setDefault);
    }

    public void conference(Call callToConferenceWith) {
        if (callToConferenceWith != null) {
            this.mInCallAdapter.conference(this.mTelecomCallId, callToConferenceWith.mTelecomCallId);
        }
    }

    public void splitFromConference() {
        this.mInCallAdapter.splitFromConference(this.mTelecomCallId);
    }

    public void mergeConference() {
        this.mInCallAdapter.mergeConference(this.mTelecomCallId);
    }

    public void swapConference() {
        this.mInCallAdapter.swapConference(this.mTelecomCallId);
    }

    public void pullExternalCall() {
        if (this.mDetails.hasProperty(64)) {
            this.mInCallAdapter.pullExternalCall(this.mTelecomCallId);
        }
    }

    public void sendCallEvent(String event, Bundle extras) {
        this.mInCallAdapter.sendCallEvent(this.mTelecomCallId, event, this.mTargetSdkVersion, extras);
    }

    public void sendRttRequest() {
        this.mInCallAdapter.sendRttRequest(this.mTelecomCallId);
    }

    public void respondToRttRequest(int id, boolean accept) {
        this.mInCallAdapter.respondToRttRequest(this.mTelecomCallId, id, accept);
    }

    public void handoverTo(PhoneAccountHandle toHandle, int videoState, Bundle extras) {
        this.mInCallAdapter.handoverTo(this.mTelecomCallId, toHandle, videoState, extras);
    }

    public void stopRtt() {
        this.mInCallAdapter.stopRtt(this.mTelecomCallId);
    }

    public final void putExtras(Bundle extras) {
        if (extras != null) {
            if (this.mExtras == null) {
                this.mExtras = new Bundle();
            }
            this.mExtras.putAll(extras);
            this.mInCallAdapter.putExtras(this.mTelecomCallId, extras);
        }
    }

    public final void putExtra(String key, boolean value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putBoolean(key, value);
        this.mInCallAdapter.putExtra(this.mTelecomCallId, key, value);
    }

    public final void putExtra(String key, int value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putInt(key, value);
        this.mInCallAdapter.putExtra(this.mTelecomCallId, key, value);
    }

    public final void putExtra(String key, String value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putString(key, value);
        this.mInCallAdapter.putExtra(this.mTelecomCallId, key, value);
    }

    public final void removeExtras(List<String> keys) {
        if (this.mExtras != null) {
            for (String key : keys) {
                this.mExtras.remove(key);
            }
            if (this.mExtras.size() == 0) {
                this.mExtras = null;
            }
        }
        this.mInCallAdapter.removeExtras(this.mTelecomCallId, keys);
    }

    public final void removeExtras(String... keys) {
        removeExtras(Arrays.asList(keys));
    }

    public Call getParent() {
        String str = this.mParentId;
        if (str != null) {
            return this.mPhone.internalGetCallByTelecomId(str);
        }
        return null;
    }

    public List<Call> getChildren() {
        if (!this.mChildrenCached) {
            this.mChildrenCached = true;
            this.mChildren.clear();
            for (String id : this.mChildrenIds) {
                Call call = this.mPhone.internalGetCallByTelecomId(id);
                if (call == null) {
                    this.mChildrenCached = false;
                } else {
                    this.mChildren.add(call);
                }
            }
        }
        return this.mUnmodifiableChildren;
    }

    public List<Call> getConferenceableCalls() {
        return this.mUnmodifiableConferenceableCalls;
    }

    public int getState() {
        return this.mState;
    }

    public List<String> getCannedTextResponses() {
        return this.mCannedTextResponses;
    }

    public VideoCall getVideoCall() {
        return this.mVideoCallImpl;
    }

    public Details getDetails() {
        return this.mDetails;
    }

    public RttCall getRttCall() {
        return this.mRttCall;
    }

    public boolean isRttActive() {
        return this.mRttCall != null && this.mDetails.hasProperty(1024);
    }

    public void registerCallback(Callback callback) {
        registerCallback(callback, new Handler());
    }

    public void registerCallback(Callback callback, Handler handler) {
        unregisterCallback(callback);
        if (callback != null && handler != null && this.mState != 7) {
            this.mCallbackRecords.add(new CallbackRecord(callback, handler));
        }
    }

    public void unregisterCallback(Callback callback) {
        if (callback != null && this.mState != 7) {
            for (CallbackRecord<Callback> record : this.mCallbackRecords) {
                if (record.getCallback() == callback) {
                    this.mCallbackRecords.remove(record);
                    return;
                }
            }
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Call [id: ");
        stringBuilder.append(this.mTelecomCallId);
        stringBuilder.append(", state: ");
        stringBuilder.append(stateToString(this.mState));
        stringBuilder.append(", details: ");
        stringBuilder.append(this.mDetails);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    private static String stateToString(int state) {
        switch (state) {
            case 0:
                return "NEW";
            case 1:
                return "DIALING";
            case 2:
                return "RINGING";
            case 3:
                return "HOLDING";
            case 4:
                return "ACTIVE";
            case 7:
                return "DISCONNECTED";
            case 8:
                return "SELECT_PHONE_ACCOUNT";
            case 9:
                return "CONNECTING";
            case 10:
                return "DISCONNECTING";
            default:
                Log.w((Object) Call.class, "Unknown state %d", Integer.valueOf(state));
                return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
        }
    }

    @SystemApi
    @Deprecated
    public void addListener(Listener listener) {
        registerCallback(listener);
    }

    @SystemApi
    @Deprecated
    public void removeListener(Listener listener) {
        unregisterCallback(listener);
    }

    Call(Phone phone, String telecomCallId, InCallAdapter inCallAdapter, String callingPackage, int targetSdkVersion) {
        this.mPhone = phone;
        this.mTelecomCallId = telecomCallId;
        this.mInCallAdapter = inCallAdapter;
        this.mState = 0;
        this.mCallingPackage = callingPackage;
        this.mTargetSdkVersion = targetSdkVersion;
    }

    Call(Phone phone, String telecomCallId, InCallAdapter inCallAdapter, int state, String callingPackage, int targetSdkVersion) {
        this.mPhone = phone;
        this.mTelecomCallId = telecomCallId;
        this.mInCallAdapter = inCallAdapter;
        this.mState = state;
        this.mCallingPackage = callingPackage;
        this.mTargetSdkVersion = targetSdkVersion;
    }

    /* Access modifiers changed, original: final */
    public final String internalGetCallId() {
        return this.mTelecomCallId;
    }

    /* Access modifiers changed, original: final */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x0187  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x018e  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x0195  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x019c  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x01a3  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x01ac  */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x01b5  */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x01c3  */
    /* JADX WARNING: Removed duplicated region for block: B:92:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x01d1  */
    public final void internalUpdate(android.telecom.ParcelableCall r28, java.util.Map<java.lang.String, android.telecom.Call> r29) {
        /*
        r27 = this;
        r0 = r27;
        r1 = r29;
        r2 = android.telecom.Call.Details.createFromParcelableCall(r28);
        r3 = r0.mDetails;
        r3 = java.util.Objects.equals(r3, r2);
        r4 = 1;
        r3 = r3 ^ r4;
        if (r3 == 0) goto L_0x0014;
    L_0x0012:
        r0.mDetails = r2;
    L_0x0014:
        r5 = 0;
        r6 = r0.mCannedTextResponses;
        if (r6 != 0) goto L_0x0035;
    L_0x0019:
        r6 = r28.getCannedSmsResponses();
        if (r6 == 0) goto L_0x0035;
    L_0x001f:
        r6 = r28.getCannedSmsResponses();
        r6 = r6.isEmpty();
        if (r6 != 0) goto L_0x0035;
        r6 = r28.getCannedSmsResponses();
        r6 = java.util.Collections.unmodifiableList(r6);
        r0.mCannedTextResponses = r6;
        r5 = 1;
    L_0x0035:
        r6 = r0.mCallingPackage;
        r7 = r0.mTargetSdkVersion;
        r8 = r28;
        r6 = r8.getVideoCallImpl(r6, r7);
        r7 = r28.isVideoCallProviderChanged();
        if (r7 == 0) goto L_0x004f;
    L_0x0045:
        r7 = r0.mVideoCallImpl;
        r7 = java.util.Objects.equals(r7, r6);
        if (r7 != 0) goto L_0x004f;
    L_0x004d:
        r7 = r4;
        goto L_0x0050;
    L_0x004f:
        r7 = 0;
    L_0x0050:
        if (r7 == 0) goto L_0x0054;
    L_0x0052:
        r0.mVideoCallImpl = r6;
    L_0x0054:
        r10 = r0.mVideoCallImpl;
        if (r10 == 0) goto L_0x0063;
    L_0x0058:
        r11 = r27.getDetails();
        r11 = r11.getVideoState();
        r10.setVideoState(r11);
    L_0x0063:
        r10 = r28.getState();
        r11 = r0.mState;
        if (r11 == r10) goto L_0x006d;
    L_0x006b:
        r11 = r4;
        goto L_0x006e;
    L_0x006d:
        r11 = 0;
    L_0x006e:
        if (r11 == 0) goto L_0x0072;
    L_0x0070:
        r0.mState = r10;
    L_0x0072:
        r12 = r28.getParentCallId();
        r13 = r0.mParentId;
        r13 = java.util.Objects.equals(r13, r12);
        r13 = r13 ^ r4;
        if (r13 == 0) goto L_0x0081;
    L_0x007f:
        r0.mParentId = r12;
    L_0x0081:
        r14 = r28.getChildCallIds();
        r15 = r0.mChildrenIds;
        r15 = java.util.Objects.equals(r14, r15);
        r15 = r15 ^ r4;
        if (r15 == 0) goto L_0x00a0;
    L_0x008e:
        r4 = r0.mChildrenIds;
        r4.clear();
        r4 = r0.mChildrenIds;
        r9 = r28.getChildCallIds();
        r4.addAll(r9);
        r4 = 0;
        r0.mChildrenCached = r4;
        goto L_0x00a1;
    L_0x00a0:
        r4 = 0;
    L_0x00a1:
        r9 = r28.getConferenceableCallIds();
        r4 = new java.util.ArrayList;
        r16 = r2;
        r2 = r9.size();
        r4.<init>(r2);
        r2 = r4;
        r4 = r9.iterator();
    L_0x00b5:
        r17 = r4.hasNext();
        if (r17 == 0) goto L_0x00db;
    L_0x00bb:
        r17 = r4.next();
        r18 = r4;
        r4 = r17;
        r4 = (java.lang.String) r4;
        r17 = r1.containsKey(r4);
        if (r17 == 0) goto L_0x00d6;
    L_0x00cb:
        r17 = r1.get(r4);
        r1 = r17;
        r1 = (android.telecom.Call) r1;
        r2.add(r1);
    L_0x00d6:
        r1 = r29;
        r4 = r18;
        goto L_0x00b5;
    L_0x00db:
        r1 = r0.mConferenceableCalls;
        r1 = java.util.Objects.equals(r1, r2);
        if (r1 != 0) goto L_0x00f0;
    L_0x00e3:
        r1 = r0.mConferenceableCalls;
        r1.clear();
        r1 = r0.mConferenceableCalls;
        r1.addAll(r2);
        r27.fireConferenceableCallsChanged();
    L_0x00f0:
        r1 = 0;
        r4 = 0;
        r17 = r28.getIsRttCallChanged();
        if (r17 == 0) goto L_0x0167;
    L_0x00f8:
        r17 = r1;
        r1 = r0.mDetails;
        r18 = r2;
        r2 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r1 = r1.hasProperty(r2);
        if (r1 == 0) goto L_0x0162;
    L_0x0106:
        r1 = r28.getParcelableRttCall();
        r2 = new java.io.InputStreamReader;
        r25 = r4;
        r4 = new android.os.ParcelFileDescriptor$AutoCloseInputStream;
        r26 = r6;
        r6 = r1.getReceiveStream();
        r4.<init>(r6);
        r6 = java.nio.charset.StandardCharsets.UTF_8;
        r2.<init>(r4, r6);
        r21 = r2;
        r2 = new java.io.OutputStreamWriter;
        r4 = new android.os.ParcelFileDescriptor$AutoCloseOutputStream;
        r6 = r1.getTransmitStream();
        r4.<init>(r6);
        r6 = java.nio.charset.StandardCharsets.UTF_8;
        r2.<init>(r4, r6);
        r22 = r2;
        r2 = new android.telecom.Call$RttCall;
        r4 = r0.mTelecomCallId;
        r23 = r1.getRttMode();
        r6 = r0.mInCallAdapter;
        r19 = r2;
        r20 = r4;
        r24 = r6;
        r19.<init>(r20, r21, r22, r23, r24);
        r4 = r0.mRttCall;
        if (r4 != 0) goto L_0x014f;
    L_0x0149:
        r4 = 1;
        r17 = r4;
        r4 = r25;
        goto L_0x015d;
    L_0x014f:
        r4 = r4.getRttAudioMode();
        r6 = r2.getRttAudioMode();
        if (r4 == r6) goto L_0x015b;
    L_0x0159:
        r4 = 1;
        goto L_0x015d;
    L_0x015b:
        r4 = r25;
    L_0x015d:
        r0.mRttCall = r2;
        r25 = r4;
        goto L_0x0185;
    L_0x0162:
        r25 = r4;
        r26 = r6;
        goto L_0x016f;
    L_0x0167:
        r17 = r1;
        r18 = r2;
        r25 = r4;
        r26 = r6;
    L_0x016f:
        r1 = r0.mRttCall;
        if (r1 == 0) goto L_0x0185;
    L_0x0173:
        r1 = r28.getParcelableRttCall();
        if (r1 != 0) goto L_0x0185;
    L_0x0179:
        r1 = r28.getIsRttCallChanged();
        if (r1 == 0) goto L_0x0185;
    L_0x017f:
        r1 = 1;
        r2 = 0;
        r0.mRttCall = r2;
        r17 = r1;
    L_0x0185:
        if (r11 == 0) goto L_0x018c;
    L_0x0187:
        r1 = r0.mState;
        r0.fireStateChanged(r1);
    L_0x018c:
        if (r3 == 0) goto L_0x0193;
    L_0x018e:
        r1 = r0.mDetails;
        r0.fireDetailsChanged(r1);
    L_0x0193:
        if (r5 == 0) goto L_0x019a;
    L_0x0195:
        r1 = r0.mCannedTextResponses;
        r0.fireCannedTextResponsesLoaded(r1);
    L_0x019a:
        if (r7 == 0) goto L_0x01a1;
    L_0x019c:
        r1 = r0.mVideoCallImpl;
        r0.fireVideoCallChanged(r1);
    L_0x01a1:
        if (r13 == 0) goto L_0x01aa;
    L_0x01a3:
        r1 = r27.getParent();
        r0.fireParentChanged(r1);
    L_0x01aa:
        if (r15 == 0) goto L_0x01b3;
    L_0x01ac:
        r1 = r27.getChildren();
        r0.fireChildrenChanged(r1);
    L_0x01b3:
        if (r17 == 0) goto L_0x01c1;
    L_0x01b5:
        r1 = r0.mRttCall;
        if (r1 == 0) goto L_0x01bb;
    L_0x01b9:
        r1 = 1;
        goto L_0x01bc;
    L_0x01bb:
        r1 = 0;
    L_0x01bc:
        r2 = r0.mRttCall;
        r0.fireOnIsRttChanged(r1, r2);
    L_0x01c1:
        if (r25 == 0) goto L_0x01cc;
    L_0x01c3:
        r1 = r0.mRttCall;
        r1 = r1.getRttAudioMode();
        r0.fireOnRttModeChanged(r1);
    L_0x01cc:
        r1 = r0.mState;
        r2 = 7;
        if (r1 != r2) goto L_0x01d4;
    L_0x01d1:
        r27.fireCallDestroyed();
    L_0x01d4:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.telecom.Call.internalUpdate(android.telecom.ParcelableCall, java.util.Map):void");
    }

    /* Access modifiers changed, original: final */
    public final void internalSetPostDialWait(String remaining) {
        this.mRemainingPostDialSequence = remaining;
        firePostDialWait(this.mRemainingPostDialSequence);
    }

    /* Access modifiers changed, original: final */
    public final void internalSetDisconnected() {
        if (this.mState != 7) {
            this.mState = 7;
            fireStateChanged(this.mState);
            fireCallDestroyed();
        }
    }

    /* Access modifiers changed, original: final */
    public final void internalOnConnectionEvent(String event, Bundle extras) {
        fireOnConnectionEvent(event, extras);
    }

    /* Access modifiers changed, original: final */
    public final void internalOnRttUpgradeRequest(int requestId) {
        for (CallbackRecord<Callback> record : this.mCallbackRecords) {
            Callback callback = (Callback) record.getCallback();
            record.getHandler().post(new -$$Lambda$Call$hgXdGxKbb9IRpCeFrYieOwUuElE(callback, this, requestId));
        }
    }

    /* Access modifiers changed, original: final */
    public final void internalOnRttInitiationFailure(int reason) {
        for (CallbackRecord<Callback> record : this.mCallbackRecords) {
            Callback callback = (Callback) record.getCallback();
            record.getHandler().post(new -$$Lambda$Call$JyYlHynNNc3DTrfrP5aXatJNft4(callback, this, reason));
        }
    }

    /* Access modifiers changed, original: final */
    public final void internalOnHandoverFailed(int error) {
        for (CallbackRecord<Callback> record : this.mCallbackRecords) {
            Callback callback = (Callback) record.getCallback();
            record.getHandler().post(new -$$Lambda$Call$aPdcAxyKfpxcuraTjET8ce3xApc(callback, this, error));
        }
    }

    /* Access modifiers changed, original: final */
    public final void internalOnHandoverComplete() {
        for (CallbackRecord<Callback> record : this.mCallbackRecords) {
            Callback callback = (Callback) record.getCallback();
            record.getHandler().post(new -$$Lambda$Call$bt1B6cq3ylYqEtzOXnJWMeJ-ojc(callback, this));
        }
    }

    private void fireStateChanged(final int newState) {
        for (CallbackRecord<Callback> record : this.mCallbackRecords) {
            final Callback callback = (Callback) record.getCallback();
            record.getHandler().post(new Runnable() {
                public void run() {
                    callback.onStateChanged(this, newState);
                }
            });
        }
    }

    private void fireParentChanged(final Call newParent) {
        for (CallbackRecord<Callback> record : this.mCallbackRecords) {
            final Callback callback = (Callback) record.getCallback();
            record.getHandler().post(new Runnable() {
                public void run() {
                    callback.onParentChanged(this, newParent);
                }
            });
        }
    }

    private void fireChildrenChanged(final List<Call> children) {
        for (CallbackRecord<Callback> record : this.mCallbackRecords) {
            final Callback callback = (Callback) record.getCallback();
            record.getHandler().post(new Runnable() {
                public void run() {
                    callback.onChildrenChanged(this, children);
                }
            });
        }
    }

    private void fireDetailsChanged(final Details details) {
        for (CallbackRecord<Callback> record : this.mCallbackRecords) {
            final Callback callback = (Callback) record.getCallback();
            record.getHandler().post(new Runnable() {
                public void run() {
                    callback.onDetailsChanged(this, details);
                }
            });
        }
    }

    private void fireCannedTextResponsesLoaded(final List<String> cannedTextResponses) {
        for (CallbackRecord<Callback> record : this.mCallbackRecords) {
            final Callback callback = (Callback) record.getCallback();
            record.getHandler().post(new Runnable() {
                public void run() {
                    callback.onCannedTextResponsesLoaded(this, cannedTextResponses);
                }
            });
        }
    }

    private void fireVideoCallChanged(final VideoCall videoCall) {
        for (CallbackRecord<Callback> record : this.mCallbackRecords) {
            final Callback callback = (Callback) record.getCallback();
            record.getHandler().post(new Runnable() {
                public void run() {
                    callback.onVideoCallChanged(this, videoCall);
                }
            });
        }
    }

    private void firePostDialWait(final String remainingPostDialSequence) {
        for (CallbackRecord<Callback> record : this.mCallbackRecords) {
            final Callback callback = (Callback) record.getCallback();
            record.getHandler().post(new Runnable() {
                public void run() {
                    callback.onPostDialWait(this, remainingPostDialSequence);
                }
            });
        }
    }

    private void fireCallDestroyed() {
        if (this.mCallbackRecords.isEmpty()) {
            this.mPhone.internalRemoveCall(this);
        }
        for (final CallbackRecord<Callback> record : this.mCallbackRecords) {
            final Callback callback = (Callback) record.getCallback();
            record.getHandler().post(new Runnable() {
                public void run() {
                    boolean isFinalRemoval = false;
                    RuntimeException toThrow = null;
                    try {
                        callback.onCallDestroyed(this);
                    } catch (RuntimeException e) {
                        toThrow = e;
                    }
                    synchronized (Call.this) {
                        Call.this.mCallbackRecords.remove(record);
                        if (Call.this.mCallbackRecords.isEmpty()) {
                            isFinalRemoval = true;
                        }
                    }
                    if (isFinalRemoval) {
                        Call.this.mPhone.internalRemoveCall(this);
                    }
                    if (toThrow != null) {
                        throw toThrow;
                    }
                }
            });
        }
    }

    private void fireConferenceableCallsChanged() {
        for (CallbackRecord<Callback> record : this.mCallbackRecords) {
            final Callback callback = (Callback) record.getCallback();
            record.getHandler().post(new Runnable() {
                public void run() {
                    callback.onConferenceableCallsChanged(this, Call.this.mUnmodifiableConferenceableCalls);
                }
            });
        }
    }

    private void fireOnConnectionEvent(String event, Bundle extras) {
        for (CallbackRecord<Callback> record : this.mCallbackRecords) {
            final Callback callback = (Callback) record.getCallback();
            final String str = event;
            final Bundle bundle = extras;
            record.getHandler().post(new Runnable() {
                public void run() {
                    callback.onConnectionEvent(this, str, bundle);
                }
            });
        }
    }

    private void fireOnIsRttChanged(boolean enabled, RttCall rttCall) {
        for (CallbackRecord<Callback> record : this.mCallbackRecords) {
            Callback callback = (Callback) record.getCallback();
            record.getHandler().post(new -$$Lambda$Call$5JdbCgV1DP_WhiljnHJKKAJdCu0(callback, this, enabled, rttCall));
        }
    }

    private void fireOnRttModeChanged(int mode) {
        for (CallbackRecord<Callback> record : this.mCallbackRecords) {
            Callback callback = (Callback) record.getCallback();
            record.getHandler().post(new -$$Lambda$Call$qjo4awib5yVZC_4Qe_hhqUSk7ho(callback, this, mode));
        }
    }

    private static boolean areBundlesEqual(Bundle bundle, Bundle newBundle) {
        boolean z = true;
        if (bundle == null || newBundle == null) {
            if (bundle != newBundle) {
                z = false;
            }
            return z;
        } else if (bundle.size() != newBundle.size()) {
            return false;
        } else {
            for (String key : bundle.keySet()) {
                if (key != null && !Objects.equals(bundle.get(key), newBundle.get(key))) {
                    return false;
                }
            }
            return true;
        }
    }
}
