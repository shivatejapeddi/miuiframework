package android.database.sqlite;

import android.annotation.UnsupportedAppUsage;
import android.database.AbstractWindowedCursor;
import android.database.CursorWindow;
import android.database.DatabaseUtils;
import android.os.StrictMode;
import android.util.Log;
import com.android.internal.util.Preconditions;
import java.util.HashMap;
import java.util.Map;

public class SQLiteCursor extends AbstractWindowedCursor {
    static final int NO_COUNT = -1;
    static final String TAG = "SQLiteCursor";
    private Map<String, Integer> mColumnNameMap;
    private final String[] mColumns;
    private int mCount;
    private int mCursorWindowCapacity;
    private final SQLiteCursorDriver mDriver;
    @UnsupportedAppUsage
    private final String mEditTable;
    private boolean mFillWindowForwardOnly;
    @UnsupportedAppUsage
    private final SQLiteQuery mQuery;
    private final Throwable mStackTrace;

    @Deprecated
    public SQLiteCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
        this(driver, editTable, query);
    }

    public SQLiteCursor(SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
        this.mCount = -1;
        if (query != null) {
            if (StrictMode.vmSqliteObjectLeaksEnabled()) {
                this.mStackTrace = new DatabaseObjectNotClosedException().fillInStackTrace();
            } else {
                this.mStackTrace = null;
            }
            this.mDriver = driver;
            this.mEditTable = editTable;
            this.mColumnNameMap = null;
            this.mQuery = query;
            this.mColumns = query.getColumnNames();
            return;
        }
        throw new IllegalArgumentException("query object cannot be null");
    }

    public SQLiteDatabase getDatabase() {
        return this.mQuery.getDatabase();
    }

    public boolean onMove(int oldPosition, int newPosition) {
        if (this.mWindow == null || newPosition < this.mWindow.getStartPosition() || newPosition >= this.mWindow.getStartPosition() + this.mWindow.getNumRows()) {
            fillWindow(newPosition);
            SQLiteCursorInjector.calibRowCount(this, this.mWindow, oldPosition, newPosition);
        }
        return true;
    }

    public int getCount() {
        if (this.mCount == -1) {
            fillWindow(0);
        }
        return this.mCount;
    }

    @UnsupportedAppUsage
    private void fillWindow(int requiredPos) {
        String str = TAG;
        clearOrCreateWindow(getDatabase().getPath());
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("requiredPos cannot be negative, but was ");
            stringBuilder.append(requiredPos);
            Preconditions.checkArgumentNonnegative(requiredPos, stringBuilder.toString());
            if (this.mCount == -1) {
                this.mCount = this.mQuery.fillWindow(this.mWindow, requiredPos, requiredPos, true);
                this.mCursorWindowCapacity = this.mWindow.getNumRows();
                if (Log.isLoggable(str, 3)) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("received count(*) from native_fill_window: ");
                    stringBuilder.append(this.mCount);
                    Log.d(str, stringBuilder.toString());
                    return;
                }
                return;
            }
            int startPos;
            if (this.mFillWindowForwardOnly) {
                startPos = requiredPos;
            } else {
                startPos = DatabaseUtils.cursorPickFillWindowStartPosition(requiredPos, this.mCursorWindowCapacity);
            }
            this.mQuery.fillWindow(this.mWindow, startPos, requiredPos, false);
        } catch (RuntimeException ex) {
            closeWindow();
            throw ex;
        }
    }

    public int getColumnIndex(String columnName) {
        if (this.mColumnNameMap == null) {
            String[] columns = this.mColumns;
            int columnCount = columns.length;
            HashMap<String, Integer> map = new HashMap(columnCount, 1.0f);
            for (int i = 0; i < columnCount; i++) {
                map.put(columns[i], Integer.valueOf(i));
            }
            this.mColumnNameMap = map;
        }
        int periodIndex = columnName.lastIndexOf(46);
        if (periodIndex != -1) {
            Exception e = new Exception();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("requesting column name with table name -- ");
            stringBuilder.append(columnName);
            Log.e(TAG, stringBuilder.toString(), e);
            columnName = columnName.substring(periodIndex + 1);
        }
        Integer i2 = (Integer) this.mColumnNameMap.get(columnName);
        if (i2 != null) {
            return i2.intValue();
        }
        return -1;
    }

    public String[] getColumnNames() {
        return this.mColumns;
    }

    public void deactivate() {
        super.deactivate();
        this.mDriver.cursorDeactivated();
    }

    public void close() {
        super.close();
        synchronized (this) {
            this.mQuery.close();
            this.mDriver.cursorClosed();
        }
    }

    public boolean requery() {
        if (isClosed()) {
            return false;
        }
        synchronized (this) {
            if (this.mQuery.getDatabase().isOpen()) {
                if (this.mWindow != null) {
                    this.mWindow.clear();
                }
                this.mPos = -1;
                this.mCount = -1;
                this.mDriver.cursorRequeried(this);
                try {
                    return super.requery();
                } catch (IllegalStateException e) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("requery() failed ");
                    stringBuilder.append(e.getMessage());
                    Log.w(TAG, stringBuilder.toString(), e);
                    return false;
                }
            }
            return false;
        }
    }

    public void setWindow(CursorWindow window) {
        super.setWindow(window);
        this.mCount = -1;
    }

    public void setSelectionArguments(String[] selectionArgs) {
        this.mDriver.setBindArguments(selectionArgs);
    }

    public void setFillWindowForwardOnly(boolean fillWindowForwardOnly) {
        this.mFillWindowForwardOnly = fillWindowForwardOnly;
    }

    /* Access modifiers changed, original: protected */
    public void finalize() {
        try {
            if (this.mWindow != null) {
                if (this.mStackTrace != null) {
                    String sql = this.mQuery.getSql();
                    int len = sql.length();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Finalizing a Cursor that has not been deactivated or closed. database = ");
                    stringBuilder.append(this.mQuery.getDatabase().getLabel());
                    stringBuilder.append(", table = ");
                    stringBuilder.append(this.mEditTable);
                    stringBuilder.append(", query = ");
                    int i = 1000;
                    if (len <= 1000) {
                        i = len;
                    }
                    stringBuilder.append(sql.substring(0, i));
                    StrictMode.onSqliteObjectLeaked(stringBuilder.toString(), this.mStackTrace);
                }
                close();
            }
            super.finalize();
        } catch (Throwable th) {
            super.finalize();
        }
    }
}
