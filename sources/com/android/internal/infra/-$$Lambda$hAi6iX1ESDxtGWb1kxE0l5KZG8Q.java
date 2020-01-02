package com.android.internal.infra;

import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$hAi6iX1ESDxtGWb1kxE0l5KZG8Q implements Consumer {
    public static final /* synthetic */ -$$Lambda$hAi6iX1ESDxtGWb1kxE0l5KZG8Q INSTANCE = new -$$Lambda$hAi6iX1ESDxtGWb1kxE0l5KZG8Q();

    private /* synthetic */ -$$Lambda$hAi6iX1ESDxtGWb1kxE0l5KZG8Q() {
    }

    public final void accept(Object obj) {
        ((AbstractRemoteService) obj).handleUnbind();
    }
}
