package android.os.statistics;

import android.os.Parcel;
import android.util.Log;

public class ParcelUtils {
    private static final int MAX_STRING_ARRAY_LENGTH = 100;
    private static final String TAG = "ParcelUtils";

    public static void writeStringArray(Parcel des, String[] s) {
        if (des != null) {
            if (s == null) {
                des.writeInt(-1);
            } else if (s.length > 100) {
                des.writeInt(0);
                des.writeStringArray(new String[0]);
                Log.e(TAG, "array is too long, write failed!!!");
            } else {
                des.writeInt(s.length);
                des.writeStringArray(s);
            }
        }
    }

    public static String[] readStringArray(Parcel src) {
        if (src == null) {
            return null;
        }
        int length = src.readInt();
        if (length < 0 || length > 100) {
            return null;
        }
        try {
            String[] res = new String[length];
            src.readStringArray(res);
            return res;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return null;
        }
    }
}
