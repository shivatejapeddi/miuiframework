package com.miui.internal.search;

import android.content.Context;

public interface BackgroundCheckable {
    boolean isChecked(Context context);

    void setChecked(Context context, boolean z);

    void toggle(Context context);
}
