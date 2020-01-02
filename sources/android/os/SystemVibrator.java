package android.os;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.media.AudioAttributes;
import android.os.IVibratorService.Stub;
import android.util.Log;

public class SystemVibrator extends Vibrator {
    private static final String TAG = "Vibrator";
    private final IVibratorService mService = Stub.asInterface(ServiceManager.getService(Context.VIBRATOR_SERVICE));
    private final Binder mToken = new Binder();

    @UnsupportedAppUsage
    public SystemVibrator(Context context) {
        super(context);
    }

    public boolean hasVibrator() {
        IVibratorService iVibratorService = this.mService;
        if (iVibratorService == null) {
            Log.w(TAG, "Failed to vibrate; no vibrator service.");
            return false;
        }
        try {
            return iVibratorService.hasVibrator();
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean hasAmplitudeControl() {
        IVibratorService iVibratorService = this.mService;
        if (iVibratorService == null) {
            Log.w(TAG, "Failed to check amplitude control; no vibrator service.");
            return false;
        }
        try {
            return iVibratorService.hasAmplitudeControl();
        } catch (RemoteException e) {
            return false;
        }
    }

    public void vibrate(int uid, String opPkg, VibrationEffect effect, String reason, AudioAttributes attributes) {
        IVibratorService iVibratorService = this.mService;
        String str = TAG;
        if (iVibratorService == null) {
            Log.w(str, "Failed to vibrate; no vibrator service.");
            return;
        }
        try {
            iVibratorService.vibrate(uid, opPkg, effect, usageForAttributes(attributes), reason, this.mToken);
        } catch (RemoteException e) {
            Log.w(str, "Failed to vibrate.", e);
        }
    }

    private static int usageForAttributes(AudioAttributes attributes) {
        return attributes != null ? attributes.getUsage() : 0;
    }

    public void cancel() {
        IVibratorService iVibratorService = this.mService;
        if (iVibratorService != null) {
            try {
                iVibratorService.cancelVibrate(this.mToken);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed to cancel vibration.", e);
            }
        }
    }
}
