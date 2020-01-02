package com.android.internal.util.dump;

import android.content.ComponentName;
import android.media.tv.TvContract.BaseTvColumns;

public class DumpUtils {
    public static void writeStringIfNotNull(DualDumpOutputStream proto, String idName, long id, String string) {
        if (string != null) {
            proto.write(idName, id, string);
        }
    }

    public static void writeComponentName(DualDumpOutputStream proto, String idName, long id, ComponentName component) {
        long token = proto.start(idName, id);
        proto.write(BaseTvColumns.COLUMN_PACKAGE_NAME, 1138166333441L, component.getPackageName());
        proto.write("class_name", 1138166333442L, component.getClassName());
        proto.end(token);
    }
}
