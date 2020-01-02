package miui.util;

import android.content.Context;
import android.net.Uri;
import android.os.VibrationEffect;
import android.os.VibrationEffect.Prebaked;
import android.os.Vibrator;
import android.util.Log;

public class VibrateUtils {
    private static String TAG = "VibrateUtils";

    public static void vibrate(Vibrator vibrator, boolean isUsePrebakedEffect, long[] pattern, int effectStrength, int defaultEffectStrength) {
        if (isUsePrebakedEffect) {
            VibrationEffect vibrationEffect = null;
            try {
                vibrationEffect = VibrationEffect.get((int) pattern[0]);
            } catch (IllegalArgumentException e) {
                Log.i(TAG, e.getMessage());
            }
            if (vibrationEffect != null) {
                if ((vibrationEffect instanceof Prebaked) && effectStrength != defaultEffectStrength) {
                    ((Prebaked) vibrationEffect).setEffectStrength(effectStrength);
                }
                vibrator.vibrate(vibrationEffect);
                return;
            }
        }
        if (pattern.length == 1) {
            vibrator.vibrate(pattern[0]);
        } else {
            vibrator.vibrate(pattern, -1);
        }
    }

    public static boolean vibrateExt(Vibrator vibrator, int effectId) {
        vibrator.vibrate(VibrationEffect.get(effectId));
        return true;
    }

    public static boolean vibrateExt(Vibrator vibrator, Uri uri, Context context) {
        VibrationEffect vibrationEffect = VibrationEffect.get(uri, context);
        if (vibrationEffect == null) {
            return false;
        }
        vibrator.vibrate(vibrationEffect);
        return true;
    }
}
