package com.android.internal.util;

import android.graphics.ImageDecoder;
import android.graphics.ImageDecoder.ImageInfo;
import android.graphics.ImageDecoder.OnHeaderDecodedListener;
import android.graphics.ImageDecoder.Source;
import android.util.Size;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ImageUtils$rnRZcgsdC1BtH9FpHTN2Kf_FXwE implements OnHeaderDecodedListener {
    private final /* synthetic */ Size f$0;

    public /* synthetic */ -$$Lambda$ImageUtils$rnRZcgsdC1BtH9FpHTN2Kf_FXwE(Size size) {
        this.f$0 = size;
    }

    public final void onHeaderDecoded(ImageDecoder imageDecoder, ImageInfo imageInfo, Source source) {
        ImageUtils.lambda$loadThumbnail$1(this.f$0, imageDecoder, imageInfo, source);
    }
}
