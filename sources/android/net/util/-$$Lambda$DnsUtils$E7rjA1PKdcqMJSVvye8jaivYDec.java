package android.net.util;

import android.net.Network;
import android.net.util.DnsUtils.SortableAddress;
import java.net.InetAddress;
import java.util.List;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$DnsUtils$E7rjA1PKdcqMJSVvye8jaivYDec implements Consumer {
    private final /* synthetic */ List f$0;
    private final /* synthetic */ Network f$1;

    public /* synthetic */ -$$Lambda$DnsUtils$E7rjA1PKdcqMJSVvye8jaivYDec(List list, Network network) {
        this.f$0 = list;
        this.f$1 = network;
    }

    public final void accept(Object obj) {
        this.f$0.add(new SortableAddress((InetAddress) obj, DnsUtils.findSrcAddress(this.f$1, (InetAddress) obj)));
    }
}
