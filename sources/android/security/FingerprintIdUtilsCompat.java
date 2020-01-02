package android.security;

import android.content.Context;
import android.hardware.fingerprint.Fingerprint;
import android.hardware.fingerprint.FingerprintManager;
import android.provider.MiuiSettings;
import android.provider.Settings.Secure;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class FingerprintIdUtilsCompat {
    private static FingerprintManager mFingerprintMgr = null;

    public static FingerprintManager getService(Context context) {
        if (mFingerprintMgr == null) {
            mFingerprintMgr = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
        }
        return mFingerprintMgr;
    }

    public static int getSecondSpaceId(Context context) {
        return Secure.getIntForUser(context.getContentResolver(), MiuiSettings.Secure.SECOND_USER_ID, -10000, 0);
    }

    private static List<String> getAllFingerprintIds(Context context) {
        FingerprintManager mFingerprintMgr = getService(context);
        if (mFingerprintMgr == null) {
            return Collections.emptyList();
        }
        List<Fingerprint> fingers = mFingerprintMgr.getEnrolledFingerprints();
        List<String> ret = new ArrayList();
        if (fingers != null && fingers.size() > 0) {
            for (Fingerprint finger : fingers) {
                ret.add(Integer.toString(finger.getBiometricId()));
            }
        }
        return ret;
    }

    public static HashMap<String, Integer> getValidFingerPrintIds(HashMap<String, Integer> fingerPrintsId, Context context) {
        HashMap<String, Integer> map = new HashMap();
        if (fingerPrintsId == null || fingerPrintsId.isEmpty()) {
            return map;
        }
        List<String> allFingerprintIds = getAllFingerprintIds(context);
        if (allFingerprintIds == null || allFingerprintIds.isEmpty()) {
            return map;
        }
        Iterator<Entry<String, Integer>> it = fingerPrintsId.entrySet().iterator();
        while (it.hasNext()) {
            if (!allFingerprintIds.contains((String) ((Entry) it.next()).getKey())) {
                it.remove();
                FingerprintIdUtils.writeFingerprintIdsToXml(context, getSecondSpaceId(context), fingerPrintsId);
            }
        }
        return fingerPrintsId;
    }
}
