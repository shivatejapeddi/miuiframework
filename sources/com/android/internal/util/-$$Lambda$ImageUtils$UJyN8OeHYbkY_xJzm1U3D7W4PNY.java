package com.android.internal.util;

import android.content.ContentProviderClient;
import android.net.Uri;
import android.os.Bundle;
import java.util.concurrent.Callable;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ImageUtils$UJyN8OeHYbkY_xJzm1U3D7W4PNY implements Callable {
    private final /* synthetic */ ContentProviderClient f$0;
    private final /* synthetic */ Uri f$1;
    private final /* synthetic */ Bundle f$2;

    public /* synthetic */ -$$Lambda$ImageUtils$UJyN8OeHYbkY_xJzm1U3D7W4PNY(ContentProviderClient contentProviderClient, Uri uri, Bundle bundle) {
        this.f$0 = contentProviderClient;
        this.f$1 = uri;
        this.f$2 = bundle;
    }

    public final Object call() {
        return this.f$0.openTypedAssetFile(this.f$1, "image/*", this.f$2, null);
    }
}
