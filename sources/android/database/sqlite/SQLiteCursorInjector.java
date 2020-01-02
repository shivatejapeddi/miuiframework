package android.database.sqlite;

import android.database.CursorWindow;
import miui.util.ReflectionUtils;

final class SQLiteCursorInjector {
    private static final String COUNT_FIELD_NAME = "mCount";

    private SQLiteCursorInjector() {
    }

    public static void middle_onMove_calib_count(SQLiteCursor thiz, CursorWindow cursorWindow, int oldPosition, int newPosition) {
        if (cursorWindow != null) {
            int windowEndPosition = cursorWindow.getStartPosition() + cursorWindow.getNumRows();
            if (windowEndPosition <= newPosition) {
                ReflectionUtils.trySetObjectField(thiz, COUNT_FIELD_NAME, Integer.valueOf(windowEndPosition));
            }
        }
    }

    public static void calibRowCount(SQLiteCursor thiz, CursorWindow cursorWindow, int oldPosition, int newPosition) {
        middle_onMove_calib_count(thiz, cursorWindow, oldPosition, newPosition);
    }
}
