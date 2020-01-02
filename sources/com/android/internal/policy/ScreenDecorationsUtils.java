package com.android.internal.policy;

import android.content.res.Resources;
import com.android.internal.R;

public class ScreenDecorationsUtils {
    public static float getWindowCornerRadius(Resources resources) {
        if (!supportsRoundedCornersOnWindows(resources)) {
            return 0.0f;
        }
        float defaultRadius = resources.getDimension(2.4429778E-38f);
        float topRadius = resources.getDimension(2.4429783E-38f);
        if (topRadius == 0.0f) {
            topRadius = defaultRadius;
        }
        float bottomRadius = resources.getDimension(2.442978E-38f);
        if (bottomRadius == 0.0f) {
            bottomRadius = defaultRadius;
        }
        return Math.min(topRadius, bottomRadius);
    }

    public static boolean supportsRoundedCornersOnWindows(Resources resources) {
        return resources.getBoolean(R.bool.config_supportsRoundedCornersOnWindows);
    }
}
