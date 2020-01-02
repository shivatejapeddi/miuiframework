package com.android.internal.os;

import android.util.Pair;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$BinderCallsStats$Vota0PqfoPWckjXH35wE48myGdk implements Consumer {
    private final /* synthetic */ List f$0;

    public /* synthetic */ -$$Lambda$BinderCallsStats$Vota0PqfoPWckjXH35wE48myGdk(List list) {
        this.f$0 = list;
    }

    public final void accept(Object obj) {
        this.f$0.add(Pair.create((String) ((Entry) obj).getKey(), (Integer) ((Entry) obj).getValue()));
    }
}
