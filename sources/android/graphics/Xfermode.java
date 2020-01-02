package android.graphics;

import android.annotation.UnsupportedAppUsage;
import android.graphics.PorterDuff.Mode;

public class Xfermode {
    static final int DEFAULT = Mode.SRC_OVER.nativeInt;
    @UnsupportedAppUsage
    int porterDuffMode = DEFAULT;
}
