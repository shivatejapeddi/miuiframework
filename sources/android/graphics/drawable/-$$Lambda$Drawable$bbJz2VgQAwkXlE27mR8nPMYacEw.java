package android.graphics.drawable;

import android.graphics.ImageDecoder;
import android.graphics.ImageDecoder.ImageInfo;
import android.graphics.ImageDecoder.OnHeaderDecodedListener;
import android.graphics.ImageDecoder.Source;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$Drawable$bbJz2VgQAwkXlE27mR8nPMYacEw implements OnHeaderDecodedListener {
    public static final /* synthetic */ -$$Lambda$Drawable$bbJz2VgQAwkXlE27mR8nPMYacEw INSTANCE = new -$$Lambda$Drawable$bbJz2VgQAwkXlE27mR8nPMYacEw();

    private /* synthetic */ -$$Lambda$Drawable$bbJz2VgQAwkXlE27mR8nPMYacEw() {
    }

    public final void onHeaderDecoded(ImageDecoder imageDecoder, ImageInfo imageInfo, Source source) {
        Drawable.lambda$getBitmapDrawable$1(imageDecoder, imageInfo, source);
    }
}
