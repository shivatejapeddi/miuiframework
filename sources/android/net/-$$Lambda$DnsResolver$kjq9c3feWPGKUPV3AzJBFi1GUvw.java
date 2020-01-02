package android.net;

import android.net.DnsResolver.Callback;
import android.net.DnsResolver.DnsException;
import android.system.ErrnoException;
import android.system.OsConstants;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$DnsResolver$kjq9c3feWPGKUPV3AzJBFi1GUvw implements Runnable {
    private final /* synthetic */ Callback f$0;

    public /* synthetic */ -$$Lambda$DnsResolver$kjq9c3feWPGKUPV3AzJBFi1GUvw(Callback callback) {
        this.f$0 = callback;
    }

    public final void run() {
        this.f$0.onError(new DnsException(1, new ErrnoException("resNetworkQuery", OsConstants.ENONET)));
    }
}
