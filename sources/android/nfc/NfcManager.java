package android.nfc;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.util.Log;

public final class NfcManager {
    private NfcAdapter mAdapter;
    private Context mContext = null;

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public NfcManager(Context context) {
        context = context.getApplicationContext();
        if (context != null) {
            NfcAdapter adapter;
            try {
                adapter = NfcAdapter.getNfcAdapter(context);
            } catch (UnsupportedOperationException e) {
                adapter = null;
            }
            this.mAdapter = adapter;
            this.mContext = context;
            return;
        }
        throw new IllegalArgumentException("context not associated with any application (using a mock context?)");
    }

    public NfcAdapter getDefaultAdapter() {
        if (this.mAdapter == null) {
            NfcAdapter adapter;
            Log.w("getDefaultAdapter", "Try get adapter again");
            try {
                adapter = NfcAdapter.getNfcAdapter(this.mContext);
            } catch (UnsupportedOperationException e) {
                adapter = null;
            }
            this.mAdapter = adapter;
        }
        return this.mAdapter;
    }
}
