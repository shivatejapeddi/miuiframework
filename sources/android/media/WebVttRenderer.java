package android.media;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.media.SubtitleController.Renderer;

public class WebVttRenderer extends Renderer {
    private final Context mContext;
    private WebVttRenderingWidget mRenderingWidget;

    @UnsupportedAppUsage
    public WebVttRenderer(Context context) {
        this.mContext = context;
    }

    public boolean supports(MediaFormat format) {
        String str = MediaFormat.KEY_MIME;
        if (format.containsKey(str)) {
            return format.getString(str).equals("text/vtt");
        }
        return false;
    }

    public SubtitleTrack createTrack(MediaFormat format) {
        if (this.mRenderingWidget == null) {
            this.mRenderingWidget = new WebVttRenderingWidget(this.mContext);
        }
        return new WebVttTrack(this.mRenderingWidget, format);
    }
}
