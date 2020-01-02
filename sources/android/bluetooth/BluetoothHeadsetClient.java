package android.bluetooth;

import android.annotation.UnsupportedAppUsage;
import android.bluetooth.BluetoothProfile.ServiceListener;
import android.bluetooth.IBluetoothHeadsetClient.Stub;
import android.content.Context;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public final class BluetoothHeadsetClient implements BluetoothProfile {
    public static final String ACTION_AG_EVENT = "android.bluetooth.headsetclient.profile.action.AG_EVENT";
    public static final String ACTION_AUDIO_STATE_CHANGED = "android.bluetooth.headsetclient.profile.action.AUDIO_STATE_CHANGED";
    public static final String ACTION_CALL_CHANGED = "android.bluetooth.headsetclient.profile.action.AG_CALL_CHANGED";
    public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.headsetclient.profile.action.CONNECTION_STATE_CHANGED";
    public static final String ACTION_LAST_VTAG = "android.bluetooth.headsetclient.profile.action.LAST_VTAG";
    public static final String ACTION_RESULT = "android.bluetooth.headsetclient.profile.action.RESULT";
    public static final int ACTION_RESULT_ERROR = 1;
    public static final int ACTION_RESULT_ERROR_BLACKLISTED = 6;
    public static final int ACTION_RESULT_ERROR_BUSY = 3;
    public static final int ACTION_RESULT_ERROR_CME = 7;
    public static final int ACTION_RESULT_ERROR_DELAYED = 5;
    public static final int ACTION_RESULT_ERROR_NO_ANSWER = 4;
    public static final int ACTION_RESULT_ERROR_NO_CARRIER = 2;
    public static final int ACTION_RESULT_OK = 0;
    public static final int CALL_ACCEPT_HOLD = 1;
    public static final int CALL_ACCEPT_NONE = 0;
    public static final int CALL_ACCEPT_TERMINATE = 2;
    public static final int CME_CORPORATE_PERSONALIZATION_PIN_REQUIRED = 46;
    public static final int CME_CORPORATE_PERSONALIZATION_PUK_REQUIRED = 47;
    public static final int CME_DIAL_STRING_TOO_LONG = 26;
    public static final int CME_EAP_NOT_SUPPORTED = 49;
    public static final int CME_EMERGENCY_SERVICE_ONLY = 32;
    public static final int CME_HIDDEN_KEY_REQUIRED = 48;
    public static final int CME_INCORRECT_PARAMETERS = 50;
    public static final int CME_INCORRECT_PASSWORD = 16;
    public static final int CME_INVALID_CHARACTER_IN_DIAL_STRING = 27;
    public static final int CME_INVALID_CHARACTER_IN_TEXT_STRING = 25;
    public static final int CME_INVALID_INDEX = 21;
    public static final int CME_MEMORY_FAILURE = 23;
    public static final int CME_MEMORY_FULL = 20;
    public static final int CME_NETWORK_PERSONALIZATION_PIN_REQUIRED = 40;
    public static final int CME_NETWORK_PERSONALIZATION_PUK_REQUIRED = 41;
    public static final int CME_NETWORK_SUBSET_PERSONALIZATION_PIN_REQUIRED = 42;
    public static final int CME_NETWORK_SUBSET_PERSONALIZATION_PUK_REQUIRED = 43;
    public static final int CME_NETWORK_TIMEOUT = 31;
    public static final int CME_NOT_FOUND = 22;
    public static final int CME_NOT_SUPPORTED_FOR_VOIP = 34;
    public static final int CME_NO_CONNECTION_TO_PHONE = 1;
    public static final int CME_NO_NETWORK_SERVICE = 30;
    public static final int CME_NO_SIMULTANOUS_VOIP_CS_CALLS = 33;
    public static final int CME_OPERATION_NOT_ALLOWED = 3;
    public static final int CME_OPERATION_NOT_SUPPORTED = 4;
    public static final int CME_PHFSIM_PIN_REQUIRED = 6;
    public static final int CME_PHFSIM_PUK_REQUIRED = 7;
    public static final int CME_PHONE_FAILURE = 0;
    public static final int CME_PHSIM_PIN_REQUIRED = 5;
    public static final int CME_SERVICE_PROVIDER_PERSONALIZATION_PIN_REQUIRED = 44;
    public static final int CME_SERVICE_PROVIDER_PERSONALIZATION_PUK_REQUIRED = 45;
    public static final int CME_SIM_BUSY = 14;
    public static final int CME_SIM_FAILURE = 13;
    public static final int CME_SIM_NOT_INSERTED = 10;
    public static final int CME_SIM_PIN2_REQUIRED = 17;
    public static final int CME_SIM_PIN_REQUIRED = 11;
    public static final int CME_SIM_PUK2_REQUIRED = 18;
    public static final int CME_SIM_PUK_REQUIRED = 12;
    public static final int CME_SIM_WRONG = 15;
    public static final int CME_SIP_RESPONSE_CODE = 35;
    public static final int CME_TEXT_STRING_TOO_LONG = 24;
    private static final boolean DBG = true;
    public static final String EXTRA_AG_FEATURE_3WAY_CALLING = "android.bluetooth.headsetclient.extra.EXTRA_AG_FEATURE_3WAY_CALLING";
    public static final String EXTRA_AG_FEATURE_ACCEPT_HELD_OR_WAITING_CALL = "android.bluetooth.headsetclient.extra.EXTRA_AG_FEATURE_ACCEPT_HELD_OR_WAITING_CALL";
    public static final String EXTRA_AG_FEATURE_ATTACH_NUMBER_TO_VT = "android.bluetooth.headsetclient.extra.EXTRA_AG_FEATURE_ATTACH_NUMBER_TO_VT";
    public static final String EXTRA_AG_FEATURE_ECC = "android.bluetooth.headsetclient.extra.EXTRA_AG_FEATURE_ECC";
    public static final String EXTRA_AG_FEATURE_MERGE = "android.bluetooth.headsetclient.extra.EXTRA_AG_FEATURE_MERGE";
    public static final String EXTRA_AG_FEATURE_MERGE_AND_DETACH = "android.bluetooth.headsetclient.extra.EXTRA_AG_FEATURE_MERGE_AND_DETACH";
    public static final String EXTRA_AG_FEATURE_REJECT_CALL = "android.bluetooth.headsetclient.extra.EXTRA_AG_FEATURE_REJECT_CALL";
    public static final String EXTRA_AG_FEATURE_RELEASE_AND_ACCEPT = "android.bluetooth.headsetclient.extra.EXTRA_AG_FEATURE_RELEASE_AND_ACCEPT";
    public static final String EXTRA_AG_FEATURE_RELEASE_HELD_OR_WAITING_CALL = "android.bluetooth.headsetclient.extra.EXTRA_AG_FEATURE_RELEASE_HELD_OR_WAITING_CALL";
    public static final String EXTRA_AG_FEATURE_RESPONSE_AND_HOLD = "android.bluetooth.headsetclient.extra.EXTRA_AG_FEATURE_RESPONSE_AND_HOLD";
    public static final String EXTRA_AG_FEATURE_VOICE_RECOGNITION = "android.bluetooth.headsetclient.extra.EXTRA_AG_FEATURE_VOICE_RECOGNITION";
    public static final String EXTRA_AUDIO_WBS = "android.bluetooth.headsetclient.extra.AUDIO_WBS";
    public static final String EXTRA_BATTERY_LEVEL = "android.bluetooth.headsetclient.extra.BATTERY_LEVEL";
    public static final String EXTRA_CALL = "android.bluetooth.headsetclient.extra.CALL";
    public static final String EXTRA_CME_CODE = "android.bluetooth.headsetclient.extra.CME_CODE";
    public static final String EXTRA_IN_BAND_RING = "android.bluetooth.headsetclient.extra.IN_BAND_RING";
    public static final String EXTRA_NETWORK_ROAMING = "android.bluetooth.headsetclient.extra.NETWORK_ROAMING";
    public static final String EXTRA_NETWORK_SIGNAL_STRENGTH = "android.bluetooth.headsetclient.extra.NETWORK_SIGNAL_STRENGTH";
    public static final String EXTRA_NETWORK_STATUS = "android.bluetooth.headsetclient.extra.NETWORK_STATUS";
    public static final String EXTRA_NUMBER = "android.bluetooth.headsetclient.extra.NUMBER";
    public static final String EXTRA_OPERATOR_NAME = "android.bluetooth.headsetclient.extra.OPERATOR_NAME";
    public static final String EXTRA_RESULT_CODE = "android.bluetooth.headsetclient.extra.RESULT_CODE";
    public static final String EXTRA_SUBSCRIBER_INFO = "android.bluetooth.headsetclient.extra.SUBSCRIBER_INFO";
    public static final String EXTRA_VOICE_RECOGNITION = "android.bluetooth.headsetclient.extra.VOICE_RECOGNITION";
    public static final int STATE_AUDIO_CONNECTED = 2;
    public static final int STATE_AUDIO_CONNECTING = 1;
    public static final int STATE_AUDIO_DISCONNECTED = 0;
    private static final String TAG = "BluetoothHeadsetClient";
    private static final boolean VDBG = false;
    private BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
    private final BluetoothProfileConnector<IBluetoothHeadsetClient> mProfileConnector = new BluetoothProfileConnector(this, 16, TAG, IBluetoothHeadsetClient.class.getName()) {
        public IBluetoothHeadsetClient getServiceInterface(IBinder service) {
            return Stub.asInterface(Binder.allowBlocking(service));
        }
    };

    BluetoothHeadsetClient(Context context, ServiceListener listener) {
        this.mProfileConnector.connect(context, listener);
    }

    /* Access modifiers changed, original: 0000 */
    public void close() {
        this.mProfileConnector.disconnect();
    }

    private IBluetoothHeadsetClient getService() {
        return (IBluetoothHeadsetClient) this.mProfileConnector.getService();
    }

    @UnsupportedAppUsage
    public boolean connect(BluetoothDevice device) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("connect(");
        stringBuilder.append(device);
        stringBuilder.append(")");
        log(stringBuilder.toString());
        IBluetoothHeadsetClient service = getService();
        String str = TAG;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                str = service.connect(device);
                return str;
            } catch (RemoteException e) {
                Log.e(str, Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
        }
        return false;
    }

    @UnsupportedAppUsage
    public boolean disconnect(BluetoothDevice device) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("disconnect(");
        stringBuilder.append(device);
        stringBuilder.append(")");
        log(stringBuilder.toString());
        IBluetoothHeadsetClient service = getService();
        String str = TAG;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                str = service.disconnect(device);
                return str;
            } catch (RemoteException e) {
                Log.e(str, Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
        }
        return false;
    }

    public List<BluetoothDevice> getConnectedDevices() {
        IBluetoothHeadsetClient service = getService();
        String str = TAG;
        if (service == null || !isEnabled()) {
            if (service == null) {
                Log.w(str, "Proxy not attached to service");
            }
            return new ArrayList();
        }
        try {
            str = service.getConnectedDevices();
            return str;
        } catch (RemoteException e) {
            Log.e(str, Log.getStackTraceString(new Throwable()));
            return new ArrayList();
        }
    }

    public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) {
        IBluetoothHeadsetClient service = getService();
        String str = TAG;
        if (service == null || !isEnabled()) {
            if (service == null) {
                Log.w(str, "Proxy not attached to service");
            }
            return new ArrayList();
        }
        try {
            str = service.getDevicesMatchingConnectionStates(states);
            return str;
        } catch (RemoteException e) {
            Log.e(str, Log.getStackTraceString(new Throwable()));
            return new ArrayList();
        }
    }

    public int getConnectionState(BluetoothDevice device) {
        IBluetoothHeadsetClient service = getService();
        String str = TAG;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                str = service.getConnectionState(device);
                return str;
            } catch (RemoteException e) {
                Log.e(str, Log.getStackTraceString(new Throwable()));
                return 0;
            }
        }
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
        }
        return 0;
    }

    public boolean setPriority(BluetoothDevice device, int priority) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("setPriority(");
        stringBuilder.append(device);
        stringBuilder.append(", ");
        stringBuilder.append(priority);
        stringBuilder.append(")");
        log(stringBuilder.toString());
        IBluetoothHeadsetClient service = getService();
        String str = TAG;
        if (service == null || !isEnabled() || !isValidDevice(device)) {
            if (service == null) {
                Log.w(str, "Proxy not attached to service");
            }
            return false;
        } else if (priority != 0 && priority != 100) {
            return false;
        } else {
            try {
                str = service.setPriority(device, priority);
                return str;
            } catch (RemoteException e) {
                Log.e(str, Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
    }

    public int getPriority(BluetoothDevice device) {
        IBluetoothHeadsetClient service = getService();
        String str = TAG;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                str = service.getPriority(device);
                return str;
            } catch (RemoteException e) {
                Log.e(str, Log.getStackTraceString(new Throwable()));
                return 0;
            }
        }
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
        }
        return 0;
    }

    public boolean startVoiceRecognition(BluetoothDevice device) {
        log("startVoiceRecognition()");
        IBluetoothHeadsetClient service = getService();
        String str = TAG;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                str = service.startVoiceRecognition(device);
                return str;
            } catch (RemoteException e) {
                Log.e(str, Log.getStackTraceString(new Throwable()));
            }
        }
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
        }
        return false;
    }

    public boolean stopVoiceRecognition(BluetoothDevice device) {
        log("stopVoiceRecognition()");
        IBluetoothHeadsetClient service = getService();
        String str = TAG;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                str = service.stopVoiceRecognition(device);
                return str;
            } catch (RemoteException e) {
                Log.e(str, Log.getStackTraceString(new Throwable()));
            }
        }
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
        }
        return false;
    }

    public List<BluetoothHeadsetClientCall> getCurrentCalls(BluetoothDevice device) {
        log("getCurrentCalls()");
        IBluetoothHeadsetClient service = getService();
        String str = TAG;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                str = service.getCurrentCalls(device);
                return str;
            } catch (RemoteException e) {
                Log.e(str, Log.getStackTraceString(new Throwable()));
            }
        }
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
        }
        return null;
    }

    public Bundle getCurrentAgEvents(BluetoothDevice device) {
        log("getCurrentCalls()");
        IBluetoothHeadsetClient service = getService();
        String str = TAG;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                str = service.getCurrentAgEvents(device);
                return str;
            } catch (RemoteException e) {
                Log.e(str, Log.getStackTraceString(new Throwable()));
            }
        }
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
        }
        return null;
    }

    @UnsupportedAppUsage
    public boolean acceptCall(BluetoothDevice device, int flag) {
        log("acceptCall()");
        IBluetoothHeadsetClient service = getService();
        String str = TAG;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                str = service.acceptCall(device, flag);
                return str;
            } catch (RemoteException e) {
                Log.e(str, Log.getStackTraceString(new Throwable()));
            }
        }
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
        }
        return false;
    }

    public boolean holdCall(BluetoothDevice device) {
        log("holdCall()");
        IBluetoothHeadsetClient service = getService();
        String str = TAG;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                str = service.holdCall(device);
                return str;
            } catch (RemoteException e) {
                Log.e(str, Log.getStackTraceString(new Throwable()));
            }
        }
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
        }
        return false;
    }

    @UnsupportedAppUsage
    public boolean rejectCall(BluetoothDevice device) {
        log("rejectCall()");
        IBluetoothHeadsetClient service = getService();
        String str = TAG;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                str = service.rejectCall(device);
                return str;
            } catch (RemoteException e) {
                Log.e(str, Log.getStackTraceString(new Throwable()));
            }
        }
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
        }
        return false;
    }

    public boolean terminateCall(BluetoothDevice device, BluetoothHeadsetClientCall call) {
        log("terminateCall()");
        IBluetoothHeadsetClient service = getService();
        String str = TAG;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                str = service.terminateCall(device, call);
                return str;
            } catch (RemoteException e) {
                Log.e(str, Log.getStackTraceString(new Throwable()));
            }
        }
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
        }
        return false;
    }

    public boolean enterPrivateMode(BluetoothDevice device, int index) {
        log("enterPrivateMode()");
        IBluetoothHeadsetClient service = getService();
        String str = TAG;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                str = service.enterPrivateMode(device, index);
                return str;
            } catch (RemoteException e) {
                Log.e(str, Log.getStackTraceString(new Throwable()));
            }
        }
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
        }
        return false;
    }

    public boolean explicitCallTransfer(BluetoothDevice device) {
        log("explicitCallTransfer()");
        IBluetoothHeadsetClient service = getService();
        String str = TAG;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                str = service.explicitCallTransfer(device);
                return str;
            } catch (RemoteException e) {
                Log.e(str, Log.getStackTraceString(new Throwable()));
            }
        }
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
        }
        return false;
    }

    public BluetoothHeadsetClientCall dial(BluetoothDevice device, String number) {
        log("dial()");
        IBluetoothHeadsetClient service = getService();
        String str = TAG;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                str = service.dial(device, number);
                return str;
            } catch (RemoteException e) {
                Log.e(str, Log.getStackTraceString(new Throwable()));
            }
        }
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
        }
        return null;
    }

    public boolean sendDTMF(BluetoothDevice device, byte code) {
        log("sendDTMF()");
        IBluetoothHeadsetClient service = getService();
        String str = TAG;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                str = service.sendDTMF(device, code);
                return str;
            } catch (RemoteException e) {
                Log.e(str, Log.getStackTraceString(new Throwable()));
            }
        }
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
        }
        return false;
    }

    public boolean getLastVoiceTagNumber(BluetoothDevice device) {
        log("getLastVoiceTagNumber()");
        IBluetoothHeadsetClient service = getService();
        String str = TAG;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                str = service.getLastVoiceTagNumber(device);
                return str;
            } catch (RemoteException e) {
                Log.e(str, Log.getStackTraceString(new Throwable()));
            }
        }
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
        }
        return false;
    }

    @UnsupportedAppUsage
    public int getAudioState(BluetoothDevice device) {
        IBluetoothHeadsetClient service = getService();
        String str = TAG;
        if (service == null || !isEnabled()) {
            Log.w(str, "Proxy not attached to service");
            Log.d(str, Log.getStackTraceString(new Throwable()));
        } else {
            try {
                str = service.getAudioState(device);
                return str;
            } catch (RemoteException e) {
                Log.e(str, e.toString());
            }
        }
        return 0;
    }

    public void setAudioRouteAllowed(BluetoothDevice device, boolean allowed) {
        IBluetoothHeadsetClient service = getService();
        String str = TAG;
        if (service == null || !isEnabled()) {
            Log.w(str, "Proxy not attached to service");
            Log.d(str, Log.getStackTraceString(new Throwable()));
            return;
        }
        try {
            service.setAudioRouteAllowed(device, allowed);
        } catch (RemoteException e) {
            Log.e(str, e.toString());
        }
    }

    public boolean getAudioRouteAllowed(BluetoothDevice device) {
        IBluetoothHeadsetClient service = getService();
        String str = TAG;
        if (service == null || !isEnabled()) {
            Log.w(str, "Proxy not attached to service");
            Log.d(str, Log.getStackTraceString(new Throwable()));
        } else {
            try {
                str = service.getAudioRouteAllowed(device);
                return str;
            } catch (RemoteException e) {
                Log.e(str, e.toString());
            }
        }
        return false;
    }

    public boolean connectAudio(BluetoothDevice device) {
        IBluetoothHeadsetClient service = getService();
        String str = TAG;
        if (service == null || !isEnabled()) {
            Log.w(str, "Proxy not attached to service");
            Log.d(str, Log.getStackTraceString(new Throwable()));
        } else {
            try {
                str = service.connectAudio(device);
                return str;
            } catch (RemoteException e) {
                Log.e(str, e.toString());
            }
        }
        return false;
    }

    public boolean disconnectAudio(BluetoothDevice device) {
        IBluetoothHeadsetClient service = getService();
        String str = TAG;
        if (service == null || !isEnabled()) {
            Log.w(str, "Proxy not attached to service");
            Log.d(str, Log.getStackTraceString(new Throwable()));
        } else {
            try {
                str = service.disconnectAudio(device);
                return str;
            } catch (RemoteException e) {
                Log.e(str, e.toString());
            }
        }
        return false;
    }

    public Bundle getCurrentAgFeatures(BluetoothDevice device) {
        IBluetoothHeadsetClient service = getService();
        String str = TAG;
        if (service == null || !isEnabled()) {
            Log.w(str, "Proxy not attached to service");
            Log.d(str, Log.getStackTraceString(new Throwable()));
        } else {
            try {
                str = service.getCurrentAgFeatures(device);
                return str;
            } catch (RemoteException e) {
                Log.e(str, e.toString());
            }
        }
        return null;
    }

    private boolean isEnabled() {
        return this.mAdapter.getState() == 12;
    }

    private static boolean isValidDevice(BluetoothDevice device) {
        return device != null && BluetoothAdapter.checkBluetoothAddress(device.getAddress());
    }

    private static void log(String msg) {
        Log.d(TAG, msg);
    }
}
