package com.android.internal.infra;

import java.util.function.BiConsumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$EbzSql2RHkXox5Myj8A-7kLC4_A implements BiConsumer {
    public static final /* synthetic */ -$$Lambda$EbzSql2RHkXox5Myj8A-7kLC4_A INSTANCE = new -$$Lambda$EbzSql2RHkXox5Myj8A-7kLC4_A();

    private /* synthetic */ -$$Lambda$EbzSql2RHkXox5Myj8A-7kLC4_A() {
    }

    public final void accept(Object obj, Object obj2) {
        ((AbstractRemoteService) obj).handlePendingRequest((MyAsyncPendingRequest) obj2);
    }
}
