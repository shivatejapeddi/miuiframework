package android.security;

import android.os.ServiceManager;
import android.util.Log;
import com.android.internal.widget.ILockSettings.Stub;
import com.android.internal.widget.LockPatternUtils;
import com.android.internal.widget.LockPatternView.Cell;
import java.util.List;

public class PrivacyLockPatternUtils {
    private static final String TAG = "PrivacyLockPatternUtils";

    public static boolean checkPrivacyPasswordPattern(List<Cell> pattern, String filename, int userId) {
        try {
            return Stub.asInterface(ServiceManager.getService("lock_settings")).checkPrivacyPasswordPattern(LockPatternUtils.patternToString(pattern), filename, userId);
        } catch (Exception e) {
            Log.i(TAG, "CheckPrivacyPasswordPattern error", e);
            return true;
        }
    }

    public static void savePrivacyPasswordPattern(List<Cell> pattern, String filename, int userId) {
        try {
            Stub.asInterface(ServiceManager.getService("lock_settings")).savePrivacyPasswordPattern(LockPatternUtils.patternToString(pattern), filename, userId);
        } catch (Exception e) {
            Log.e(TAG, "savePrivacyPasswordPattern error", e);
        }
    }
}
