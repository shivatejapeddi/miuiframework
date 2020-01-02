package android.bluetooth;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.bluetooth.BluetoothProfile.ServiceListener;
import android.bluetooth.IBluetoothStateChangeCallback.Stub;
import android.content.ComponentName;
import android.content.Context;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.R;
import com.android.internal.annotations.GuardedBy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public final class BluetoothHeadset implements BluetoothProfile {
    @UnsupportedAppUsage
    public static final String ACTION_ACTIVE_DEVICE_CHANGED = "android.bluetooth.headset.profile.action.ACTIVE_DEVICE_CHANGED";
    public static final String ACTION_AUDIO_STATE_CHANGED = "android.bluetooth.headset.profile.action.AUDIO_STATE_CHANGED";
    public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED";
    public static final String ACTION_HF_INDICATORS_VALUE_CHANGED = "android.bluetooth.headset.action.HF_INDICATORS_VALUE_CHANGED";
    public static final String ACTION_HF_TWSP_BATTERY_STATE_CHANGED = "android.bluetooth.headset.action.HF_TWSP_BATTERY_STATE_CHANGED";
    public static final String ACTION_VENDOR_SPECIFIC_HEADSET_EVENT = "android.bluetooth.headset.action.VENDOR_SPECIFIC_HEADSET_EVENT";
    public static final int AT_CMD_TYPE_ACTION = 4;
    public static final int AT_CMD_TYPE_BASIC = 3;
    public static final int AT_CMD_TYPE_READ = 0;
    public static final int AT_CMD_TYPE_SET = 2;
    public static final int AT_CMD_TYPE_TEST = 1;
    private static final boolean DBG = true;
    public static final String EXTRA_HF_INDICATORS_IND_ID = "android.bluetooth.headset.extra.HF_INDICATORS_IND_ID";
    public static final String EXTRA_HF_INDICATORS_IND_VALUE = "android.bluetooth.headset.extra.HF_INDICATORS_IND_VALUE";
    public static final String EXTRA_HF_TWSP_BATTERY_LEVEL = "android.bluetooth.headset.extra.HF_TWSP_BATTERY_LEVEL";
    public static final String EXTRA_HF_TWSP_BATTERY_STATE = "android.bluetooth.headset.extra.HF_TWSP_BATTERY_STATE";
    public static final String EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_ARGS = "android.bluetooth.headset.extra.VENDOR_SPECIFIC_HEADSET_EVENT_ARGS";
    public static final String EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_CMD = "android.bluetooth.headset.extra.VENDOR_SPECIFIC_HEADSET_EVENT_CMD";
    public static final String EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_CMD_TYPE = "android.bluetooth.headset.extra.VENDOR_SPECIFIC_HEADSET_EVENT_CMD_TYPE";
    private static final int MESSAGE_HEADSET_SERVICE_CONNECTED = 100;
    private static final int MESSAGE_HEADSET_SERVICE_DISCONNECTED = 101;
    public static final int STATE_AUDIO_CONNECTED = 12;
    public static final int STATE_AUDIO_CONNECTING = 11;
    public static final int STATE_AUDIO_DISCONNECTED = 10;
    private static final String TAG = "BluetoothHeadset";
    private static final boolean VDBG = false;
    public static final String VENDOR_RESULT_CODE_COMMAND_ANDROID = "+ANDROID";
    public static final String VENDOR_SPECIFIC_HEADSET_EVENT_COMPANY_ID_CATEGORY = "android.bluetooth.headset.intent.category.companyid";
    public static final String VENDOR_SPECIFIC_HEADSET_EVENT_IPHONEACCEV = "+IPHONEACCEV";
    public static final int VENDOR_SPECIFIC_HEADSET_EVENT_IPHONEACCEV_BATTERY_LEVEL = 1;
    public static final String VENDOR_SPECIFIC_HEADSET_EVENT_XAPL = "+XAPL";
    public static final String VENDOR_SPECIFIC_HEADSET_EVENT_XEVENT = "+XEVENT";
    public static final String VENDOR_SPECIFIC_HEADSET_EVENT_XEVENT_BATTERY_LEVEL = "BATTERY";
    private BluetoothAdapter mAdapter;
    private final IBluetoothStateChangeCallback mBluetoothStateChangeCallback = new Stub() {
        public void onBluetoothStateChange(boolean up) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onBluetoothStateChange: up=");
            stringBuilder.append(up);
            Log.d(BluetoothHeadset.TAG, stringBuilder.toString());
            if (up) {
                BluetoothHeadset.this.doBind();
            } else {
                BluetoothHeadset.this.doUnbind();
            }
        }
    };
    private final IBluetoothProfileServiceConnection mConnection = new IBluetoothProfileServiceConnection.Stub() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d(BluetoothHeadset.TAG, "Proxy object connected");
            try {
                BluetoothHeadset.this.mServiceLock.writeLock().lock();
                BluetoothHeadset.this.mService = IBluetoothHeadset.Stub.asInterface(Binder.allowBlocking(service));
                BluetoothHeadset.this.mHandler.sendMessage(BluetoothHeadset.this.mHandler.obtainMessage(100));
            } finally {
                BluetoothHeadset.this.mServiceLock.writeLock().unlock();
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.d(BluetoothHeadset.TAG, "Proxy object disconnected");
            try {
                BluetoothHeadset.this.mServiceLock.writeLock().lock();
                BluetoothHeadset.this.mService = null;
                BluetoothHeadset.this.mHandler.sendMessage(BluetoothHeadset.this.mHandler.obtainMessage(101));
            } finally {
                BluetoothHeadset.this.mServiceLock.writeLock().unlock();
            }
        }
    };
    private Context mContext;
    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            int i = msg.what;
            if (i != 100) {
                if (i == 101 && BluetoothHeadset.this.mServiceListener != null) {
                    BluetoothHeadset.this.mServiceListener.onServiceDisconnected(1);
                }
            } else if (BluetoothHeadset.this.mServiceListener != null) {
                BluetoothHeadset.this.mServiceListener.onServiceConnected(1, BluetoothHeadset.this);
            }
        }
    };
    @GuardedBy({"mServiceLock"})
    private IBluetoothHeadset mService;
    private ServiceListener mServiceListener;
    private final ReentrantReadWriteLock mServiceLock = new ReentrantReadWriteLock();

    BluetoothHeadset(Context context, ServiceListener l) {
        this.mContext = context;
        this.mServiceListener = l;
        this.mAdapter = BluetoothAdapter.getDefaultAdapter();
        IBluetoothManager mgr = this.mAdapter.getBluetoothManager();
        if (mgr != null) {
            try {
                mgr.registerStateChangeCallback(this.mBluetoothStateChangeCallback);
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
            }
        }
        doBind();
    }

    private boolean doBind() {
        synchronized (this.mConnection) {
            if (this.mService == null) {
                Log.d(TAG, "Binding service...");
                try {
                    boolean bindBluetoothProfileService = this.mAdapter.getBluetoothManager().bindBluetoothProfileService(1, this.mConnection);
                    return bindBluetoothProfileService;
                } catch (RemoteException e) {
                    Log.e(TAG, "Unable to bind HeadsetService", e);
                    return false;
                }
            }
        }
    }

    private void doUnbind() {
        synchronized (this.mConnection) {
            WriteLock writeLock;
            try {
                this.mAdapter.getBluetoothManager().unbindBluetoothProfileService(1, this.mConnection);
                this.mServiceLock.writeLock().lock();
                this.mService = null;
                writeLock = this.mServiceLock.writeLock();
            } catch (RemoteException e) {
                try {
                    Log.e(TAG, "Unable to unbind HeadsetService", e);
                    this.mServiceLock.writeLock().lock();
                    this.mService = null;
                    writeLock = this.mServiceLock.writeLock();
                } catch (Throwable th) {
                    this.mServiceLock.writeLock().lock();
                    this.mService = null;
                    this.mServiceLock.writeLock().unlock();
                }
            }
            writeLock.unlock();
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void close() {
        IBluetoothManager mgr = this.mAdapter.getBluetoothManager();
        if (mgr != null) {
            try {
                mgr.unregisterStateChangeCallback(this.mBluetoothStateChangeCallback);
            } catch (RemoteException re) {
                Log.e(TAG, "", re);
            }
        }
        this.mServiceListener = null;
        doUnbind();
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        close();
    }

    @SystemApi
    public boolean connect(BluetoothDevice device) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("connect(");
        stringBuilder.append(device);
        stringBuilder.append(")");
        log(stringBuilder.toString());
        IBluetoothHeadset service = this.mService;
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

    @SystemApi
    public boolean disconnect(BluetoothDevice device) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("disconnect(");
        stringBuilder.append(device);
        stringBuilder.append(")");
        log(stringBuilder.toString());
        IBluetoothHeadset service = this.mService;
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
        String str;
        List<BluetoothDevice> arrayList;
        try {
            this.mServiceLock.readLock().lock();
            IBluetoothHeadset service = this.mService;
            str = TAG;
            if (service != null) {
                if (isEnabled()) {
                    str = service.getConnectedDevices();
                    return arrayList;
                }
            }
            if (service == null) {
                Log.w(str, "Proxy not attached to service");
            }
            ArrayList arrayList2 = new ArrayList();
            this.mServiceLock.readLock().unlock();
            return arrayList2;
        } catch (RemoteException e) {
            Log.e(str, Log.getStackTraceString(new Throwable()));
            arrayList = new ArrayList();
            return arrayList;
        } finally {
            arrayList = this.mServiceLock.readLock();
            arrayList.unlock();
        }
    }

    public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) {
        IBluetoothHeadset service = this.mService;
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
        IBluetoothHeadset service = this.mService;
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

    @SystemApi
    public boolean setPriority(BluetoothDevice device, int priority) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("setPriority(");
        stringBuilder.append(device);
        stringBuilder.append(", ");
        stringBuilder.append(priority);
        stringBuilder.append(")");
        log(stringBuilder.toString());
        IBluetoothHeadset service = this.mService;
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

    @UnsupportedAppUsage
    public int getPriority(BluetoothDevice device) {
        IBluetoothHeadset service = this.mService;
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
        IBluetoothHeadset service = this.mService;
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
        IBluetoothHeadset service = this.mService;
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

    public boolean isAudioConnected(BluetoothDevice device) {
        String str;
        IBluetoothHeadset service = this.mService;
        try {
            this.mServiceLock.readLock().lock();
            str = TAG;
            if (service != null) {
                if (isEnabled() && isValidDevice(device)) {
                    str = service.isAudioConnected(device);
                    this.mServiceLock.readLock().unlock();
                    return str;
                }
            }
        } catch (RemoteException e) {
            Log.e(str, Log.getStackTraceString(new Throwable()));
        } catch (Throwable th) {
            this.mServiceLock.readLock().unlock();
        }
        if (service == null) {
            Log.w(str, "Proxy not attached to service");
        }
        this.mServiceLock.readLock().unlock();
        return false;
    }

    public static boolean isBluetoothVoiceDialingEnabled(Context context) {
        return context.getResources().getBoolean(R.bool.config_bluetooth_sco_off_call);
    }

    @UnsupportedAppUsage
    public int getAudioState(BluetoothDevice device) {
        IBluetoothHeadset service = this.mService;
        String str = TAG;
        if (service == null || isDisabled()) {
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
        return 10;
    }

    public void setAudioRouteAllowed(boolean allowed) {
        IBluetoothHeadset service = this.mService;
        String str = TAG;
        if (service == null || !isEnabled()) {
            Log.w(str, "Proxy not attached to service");
            Log.d(str, Log.getStackTraceString(new Throwable()));
            return;
        }
        try {
            service.setAudioRouteAllowed(allowed);
        } catch (RemoteException e) {
            Log.e(str, e.toString());
        }
    }

    public boolean getAudioRouteAllowed() {
        IBluetoothHeadset service = this.mService;
        String str = TAG;
        if (service == null || !isEnabled()) {
            Log.w(str, "Proxy not attached to service");
            Log.d(str, Log.getStackTraceString(new Throwable()));
        } else {
            try {
                str = service.getAudioRouteAllowed();
                return str;
            } catch (RemoteException e) {
                Log.e(str, e.toString());
            }
        }
        return false;
    }

    public void setForceScoAudio(boolean forced) {
        IBluetoothHeadset service = this.mService;
        String str = TAG;
        if (service == null || !isEnabled()) {
            Log.w(str, "Proxy not attached to service");
            Log.d(str, Log.getStackTraceString(new Throwable()));
            return;
        }
        try {
            service.setForceScoAudio(forced);
        } catch (RemoteException e) {
            Log.e(str, e.toString());
        }
    }

    public boolean isAudioOn() {
        IBluetoothHeadset service = this.mService;
        String str = TAG;
        if (service != null && isEnabled()) {
            try {
                str = service.isAudioOn();
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
    public boolean connectAudio() {
        IBluetoothHeadset service = this.mService;
        String str = TAG;
        if (service == null || !isEnabled()) {
            Log.w(str, "Proxy not attached to service");
            Log.d(str, Log.getStackTraceString(new Throwable()));
        } else {
            try {
                str = service.connectAudio();
                return str;
            } catch (RemoteException e) {
                Log.e(str, e.toString());
            }
        }
        return false;
    }

    @UnsupportedAppUsage
    public boolean disconnectAudio() {
        IBluetoothHeadset service = this.mService;
        String str = TAG;
        if (service == null || !isEnabled()) {
            Log.w(str, "Proxy not attached to service");
            Log.d(str, Log.getStackTraceString(new Throwable()));
        } else {
            try {
                str = service.disconnectAudio();
                return str;
            } catch (RemoteException e) {
                Log.e(str, e.toString());
            }
        }
        return false;
    }

    @UnsupportedAppUsage
    public boolean startScoUsingVirtualVoiceCall() {
        log("startScoUsingVirtualVoiceCall()");
        IBluetoothHeadset service = this.mService;
        String str = TAG;
        if (service == null || !isEnabled()) {
            Log.w(str, "Proxy not attached to service");
            Log.d(str, Log.getStackTraceString(new Throwable()));
        } else {
            try {
                str = service.startScoUsingVirtualVoiceCall();
                return str;
            } catch (RemoteException e) {
                Log.e(str, e.toString());
            }
        }
        return false;
    }

    @UnsupportedAppUsage
    public boolean stopScoUsingVirtualVoiceCall() {
        log("stopScoUsingVirtualVoiceCall()");
        IBluetoothHeadset service = this.mService;
        String str = TAG;
        if (service == null || !isEnabled()) {
            Log.w(str, "Proxy not attached to service");
            Log.d(str, Log.getStackTraceString(new Throwable()));
        } else {
            try {
                str = service.stopScoUsingVirtualVoiceCall();
                return str;
            } catch (RemoteException e) {
                Log.e(str, e.toString());
            }
        }
        return false;
    }

    @UnsupportedAppUsage
    public void phoneStateChanged(int numActive, int numHeld, int callState, String number, int type, String name) {
        IBluetoothHeadset service = this.mService;
        String str = TAG;
        if (service == null || !isEnabled()) {
            Log.w(str, "Proxy not attached to service");
            Log.d(str, Log.getStackTraceString(new Throwable()));
            return;
        }
        try {
            service.phoneStateChanged(numActive, numHeld, callState, number, type, name);
        } catch (RemoteException e) {
            Log.e(str, e.toString());
        }
    }

    public void clccResponse(int index, int direction, int status, int mode, boolean mpty, String number, int type) {
        IBluetoothHeadset service = this.mService;
        String str = TAG;
        if (service == null || !isEnabled()) {
            Log.w(str, "Proxy not attached to service");
            Log.d(str, Log.getStackTraceString(new Throwable()));
            return;
        }
        try {
            service.clccResponse(index, direction, status, mode, mpty, number, type);
        } catch (RemoteException e) {
            Log.e(str, e.toString());
        }
    }

    public boolean sendVendorSpecificResultCode(BluetoothDevice device, String command, String arg) {
        log("sendVendorSpecificResultCode()");
        if (command != null) {
            IBluetoothHeadset service = this.mService;
            String str = TAG;
            if (service != null && isEnabled() && isValidDevice(device)) {
                try {
                    str = service.sendVendorSpecificResultCode(device, command, arg);
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
        throw new IllegalArgumentException("command is null");
    }

    @UnsupportedAppUsage
    public boolean setActiveDevice(BluetoothDevice device) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("setActiveDevice: ");
        stringBuilder.append(device);
        String stringBuilder2 = stringBuilder.toString();
        String str = TAG;
        Log.d(str, stringBuilder2);
        IBluetoothHeadset service = this.mService;
        if (service != null && isEnabled() && (device == null || isValidDevice(device))) {
            try {
                str = service.setActiveDevice(device);
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
    public BluetoothDevice getActiveDevice() {
        IBluetoothHeadset service = this.mService;
        String str = TAG;
        if (service != null && isEnabled()) {
            try {
                str = service.getActiveDevice();
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

    public boolean isInbandRingingEnabled() {
        log("isInbandRingingEnabled()");
        IBluetoothHeadset service = this.mService;
        String str = TAG;
        if (service != null && isEnabled()) {
            try {
                str = service.isInbandRingingEnabled();
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

    public static boolean isInbandRingingSupported(Context context) {
        return context.getResources().getBoolean(R.bool.config_bluetooth_hfp_inband_ringing_support);
    }

    @UnsupportedAppUsage
    private boolean isEnabled() {
        return this.mAdapter.getState() == 12;
    }

    private boolean isDisabled() {
        return this.mAdapter.getState() == 10;
    }

    private static boolean isValidDevice(BluetoothDevice device) {
        return device != null && BluetoothAdapter.checkBluetoothAddress(device.getAddress());
    }

    private static void log(String msg) {
        Log.d(TAG, msg);
    }
}
