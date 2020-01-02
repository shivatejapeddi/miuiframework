package android.content;

import android.graphics.ImageDecoder;
import android.graphics.ImageDecoder.ImageInfo;
import android.graphics.ImageDecoder.OnHeaderDecodedListener;
import android.graphics.ImageDecoder.Source;
import android.os.CancellationSignal;
import android.util.Size;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ContentResolver$RVw7W0M7r0cGmbYi8rAG5GKxq4M implements OnHeaderDecodedListener {
    private final /* synthetic */ int f$0;
    private final /* synthetic */ CancellationSignal f$1;
    private final /* synthetic */ Size f$2;

    public /* synthetic */ -$$Lambda$ContentResolver$RVw7W0M7r0cGmbYi8rAG5GKxq4M(int i, CancellationSignal cancellationSignal, Size size) {
        this.f$0 = i;
        this.f$1 = cancellationSignal;
        this.f$2 = size;
    }

    public final void onHeaderDecoded(ImageDecoder imageDecoder, ImageInfo imageInfo, Source source) {
        ContentResolver.lambda$loadThumbnail$1(this.f$0, this.f$1, this.f$2, imageDecoder, imageInfo, source);
    }
}
