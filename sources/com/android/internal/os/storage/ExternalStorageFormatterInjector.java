package com.android.internal.os.storage;

import android.content.Context;
import miui.R;
import miui.app.ProgressDialog;

class ExternalStorageFormatterInjector {
    ExternalStorageFormatterInjector() {
    }

    static ProgressDialog createProgressDialog(Context context) {
        return new ProgressDialog(context, R.style.Theme_DayNight_Dialog_Alert);
    }
}
