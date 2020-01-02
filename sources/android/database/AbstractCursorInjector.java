package android.database;

final class AbstractCursorInjector {
    private AbstractCursorInjector() {
    }

    public static void before_moveToPosition(AbstractCursor thiz, int position) {
        if (position < thiz.getCount() && position >= 0 && position != thiz.mPos) {
            thiz.onMove(thiz.mPos, position);
        }
    }

    public static void calibRowCountForReadRow(AbstractCursor thiz, int position) {
        before_moveToPosition(thiz, position);
    }

    public static boolean checkPosition(AbstractCursor thiz, int position) {
        int count = thiz.getCount();
        if (position < count) {
            return true;
        }
        thiz.mPos = count;
        return false;
    }
}
