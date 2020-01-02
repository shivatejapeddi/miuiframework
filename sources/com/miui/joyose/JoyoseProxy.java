package com.miui.joyose;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public abstract class JoyoseProxy {
    public static final String AUTHORITY = "com.xiaomi.joyose";
    public static final Uri CONTENT_URI = Uri.parse("content://com.xiaomi.joyose");

    public static long getJoyoseSupportSceneList(Context context) {
        if (context == null) {
            return 0;
        }
        long sceneList = 0;
        Cursor typeCursor = context.getContentResolver().query(Uri.withAppendedPath(CONTENT_URI, "scenemanager"), null, null, null, null);
        if (typeCursor != null) {
            if (typeCursor.getCount() == 1) {
                while (typeCursor.moveToNext()) {
                    sceneList = (long) typeCursor.getInt(0);
                }
            }
            typeCursor.close();
        }
        return sceneList;
    }

    public static boolean checkSceneWorkState(long scene) {
        return false;
    }
}
