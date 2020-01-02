package android.media.midi;

import android.app.Service;
import android.content.Intent;
import android.media.midi.IMidiManager.Stub;
import android.media.midi.MidiDeviceServer.Callback;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;

public abstract class MidiDeviceService extends Service {
    public static final String SERVICE_INTERFACE = "android.media.midi.MidiDeviceService";
    private static final String TAG = "MidiDeviceService";
    private final Callback mCallback = new Callback() {
        public void onDeviceStatusChanged(MidiDeviceServer server, MidiDeviceStatus status) {
            MidiDeviceService.this.onDeviceStatusChanged(status);
        }

        public void onClose() {
            MidiDeviceService.this.onClose();
        }
    };
    private MidiDeviceInfo mDeviceInfo;
    private IMidiManager mMidiManager;
    private MidiDeviceServer mServer;

    public abstract MidiReceiver[] onGetInputPortReceivers();

    public void onCreate() {
        String str = TAG;
        this.mMidiManager = Stub.asInterface(ServiceManager.getService("midi"));
        MidiDeviceServer server;
        try {
            MidiDeviceInfo deviceInfo = this.mMidiManager.getServiceDeviceInfo(getPackageName(), getClass().getName());
            if (deviceInfo == null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Could not find MidiDeviceInfo for MidiDeviceService ");
                stringBuilder.append(this);
                Log.e(str, stringBuilder.toString());
                return;
            }
            this.mDeviceInfo = deviceInfo;
            MidiReceiver[] inputPortReceivers = onGetInputPortReceivers();
            if (inputPortReceivers == null) {
                inputPortReceivers = new MidiReceiver[0];
            }
            server = new MidiDeviceServer(this.mMidiManager, inputPortReceivers, deviceInfo, this.mCallback);
            this.mServer = server;
        } catch (RemoteException e) {
            Log.e(str, "RemoteException in IMidiManager.getServiceDeviceInfo");
            server = null;
        }
    }

    public final MidiReceiver[] getOutputPortReceivers() {
        MidiDeviceServer midiDeviceServer = this.mServer;
        if (midiDeviceServer == null) {
            return null;
        }
        return midiDeviceServer.getOutputPortReceivers();
    }

    public final MidiDeviceInfo getDeviceInfo() {
        return this.mDeviceInfo;
    }

    public void onDeviceStatusChanged(MidiDeviceStatus status) {
    }

    public void onClose() {
    }

    public IBinder onBind(Intent intent) {
        if (SERVICE_INTERFACE.equals(intent.getAction())) {
            MidiDeviceServer midiDeviceServer = this.mServer;
            if (midiDeviceServer != null) {
                return midiDeviceServer.getBinderInterface().asBinder();
            }
        }
        return null;
    }
}
