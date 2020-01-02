package android.net.util;

import android.net.util.DnsUtils.SortableAddress;
import java.util.List;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$DnsUtils$GlRZOd_k4dipl4wcKx5eyR_B_sU implements Consumer {
    private final /* synthetic */ List f$0;

    public /* synthetic */ -$$Lambda$DnsUtils$GlRZOd_k4dipl4wcKx5eyR_B_sU(List list) {
        this.f$0 = list;
    }

    public final void accept(Object obj) {
        this.f$0.add(((SortableAddress) obj).address);
    }
}
