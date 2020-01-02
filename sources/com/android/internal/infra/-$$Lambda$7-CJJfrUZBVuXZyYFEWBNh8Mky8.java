package com.android.internal.infra;

import com.android.internal.infra.AbstractRemoteService.BasePendingRequest;
import java.util.function.BiConsumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$7-CJJfrUZBVuXZyYFEWBNh8Mky8 implements BiConsumer {
    public static final /* synthetic */ -$$Lambda$7-CJJfrUZBVuXZyYFEWBNh8Mky8 INSTANCE = new -$$Lambda$7-CJJfrUZBVuXZyYFEWBNh8Mky8();

    private /* synthetic */ -$$Lambda$7-CJJfrUZBVuXZyYFEWBNh8Mky8() {
    }

    public final void accept(Object obj, Object obj2) {
        ((AbstractRemoteService) obj).handlePendingRequest((BasePendingRequest) obj2);
    }
}
