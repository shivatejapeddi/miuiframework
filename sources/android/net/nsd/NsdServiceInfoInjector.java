package android.net.nsd;

import android.util.Log;

public class NsdServiceInfoInjector {
    private static final String TAG = "NsdServiceInfo";

    public static void setTxtRecord(int txtLen, byte[] txtRecord, NsdServiceInfo nsdServiceInfo) {
        String str = TAG;
        if (txtLen < 2) {
            Log.w(str, "txtRecord < 2");
        } else if (txtRecord == null) {
            Log.w(str, "txtRecord is null");
        } else {
            if (txtRecord.length != txtLen) {
                Log.w(str, "txtRecord.length != txtLen");
            }
            int i = 0;
            while (i < txtLen) {
                byte length = txtRecord[i];
                int start = i + 1;
                if (length > txtLen - start) {
                    Log.w(str, String.format("invalid length: %d", new Object[]{Byte.valueOf(length)}));
                    break;
                }
                byte[] buf = new byte[length];
                try {
                    System.arraycopy(txtRecord, start, buf, 0, length);
                    String[] a = new String(buf).split("=");
                    if (a.length == 2) {
                        nsdServiceInfo.setAttribute(a[0], a[1].getBytes());
                    }
                    i = start + length;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
