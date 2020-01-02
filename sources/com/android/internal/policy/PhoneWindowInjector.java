package com.android.internal.policy;

import android.view.View;
import android.view.WindowInsets;

class PhoneWindowInjector {
    private static int sLastInsetBottom;

    private PhoneWindowInjector() {
    }

    static void onNavigationBarColorChange(PhoneWindow window) {
        if (window.getNavigationBarColor() == -1) {
            window.addExtraFlags(64);
        } else {
            window.clearExtraFlags(64);
        }
    }

    static void requestApplyInsetsIfNeeded(View content, WindowInsets insets) {
        if (content != null && insets != null && insets.getSystemWindowInsetBottom() != sLastInsetBottom) {
            sLastInsetBottom = insets.getSystemWindowInsetBottom();
            content.requestApplyInsets();
        }
    }
}
