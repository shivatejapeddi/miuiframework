package miui.util;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;

public class FingerprintHelper {
    public static boolean isFingerprintHardwareDetected(Context context) {
        FingerprintManager fingerprintMgr = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
        return fingerprintMgr == null ? false : fingerprintMgr.isHardwareDetected();
    }
}
