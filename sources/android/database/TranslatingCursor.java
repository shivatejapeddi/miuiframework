package android.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.CancellationSignal;
import android.util.ArraySet;
import com.android.internal.util.ArrayUtils;
import java.util.Arrays;
import java.util.Objects;

public class TranslatingCursor extends CrossProcessCursorWrapper {
    private final int mAuxiliaryColumnIndex;
    private final Config mConfig;
    private final boolean mDropLast;
    private final ArraySet<Integer> mTranslateColumnIndices = new ArraySet();
    private final Translator mTranslator;

    public static class Config {
        public final String auxiliaryColumn;
        public final Uri baseUri;
        public final String[] translateColumns;

        public Config(Uri baseUri, String auxiliaryColumn, String... translateColumns) {
            this.baseUri = baseUri;
            this.auxiliaryColumn = auxiliaryColumn;
            this.translateColumns = translateColumns;
        }
    }

    public interface Translator {
        String translate(String str, int i, String str2, Cursor cursor);
    }

    public TranslatingCursor(Cursor cursor, Config config, Translator translator, boolean dropLast) {
        super(cursor);
        this.mConfig = (Config) Objects.requireNonNull(config);
        this.mTranslator = (Translator) Objects.requireNonNull(translator);
        this.mDropLast = dropLast;
        this.mAuxiliaryColumnIndex = cursor.getColumnIndexOrThrow(config.auxiliaryColumn);
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            if (ArrayUtils.contains(config.translateColumns, cursor.getColumnName(i))) {
                this.mTranslateColumnIndices.add(Integer.valueOf(i));
            }
        }
    }

    public int getColumnCount() {
        if (this.mDropLast) {
            return super.getColumnCount() - 1;
        }
        return super.getColumnCount();
    }

    public String[] getColumnNames() {
        if (this.mDropLast) {
            return (String[]) Arrays.copyOfRange(super.getColumnNames(), 0, super.getColumnCount() - 1);
        }
        return super.getColumnNames();
    }

    public static Cursor query(Config config, Translator translator, SQLiteQueryBuilder qb, SQLiteDatabase db, String[] projectionIn, String selection, String[] selectionArgs, String groupBy, String having, String sortOrder, String limit, CancellationSignal signal) {
        Config config2 = config;
        String[] projectionIn2 = projectionIn;
        boolean z = true;
        boolean requestedAuxiliaryColumn = ArrayUtils.isEmpty((Object[]) projectionIn) || ArrayUtils.contains((Object[]) projectionIn2, config2.auxiliaryColumn);
        boolean requestedTranslateColumns = ArrayUtils.isEmpty((Object[]) projectionIn) || ArrayUtils.containsAny(projectionIn2, config2.translateColumns);
        if (!requestedTranslateColumns) {
            return qb.query(db, projectionIn, selection, selectionArgs, groupBy, having, sortOrder, limit, signal);
        }
        if (!requestedAuxiliaryColumn) {
            projectionIn2 = (String[]) ArrayUtils.appendElement(String.class, projectionIn2, config2.auxiliaryColumn);
        }
        Cursor c = qb.query(db, projectionIn2, selection, selectionArgs, groupBy, having, sortOrder);
        if (requestedAuxiliaryColumn) {
            z = false;
        }
        Translator translator2 = translator;
        return new TranslatingCursor(c, config, translator, z);
    }

    public void fillWindow(int position, CursorWindow window) {
        DatabaseUtils.cursorFillWindow(this, position, window);
    }

    public CursorWindow getWindow() {
        return null;
    }

    public Cursor getWrappedCursor() {
        throw new UnsupportedOperationException("Returning underlying cursor risks leaking data");
    }

    public double getDouble(int columnIndex) {
        if (!ArrayUtils.contains(this.mTranslateColumnIndices, Integer.valueOf(columnIndex))) {
            return super.getDouble(columnIndex);
        }
        throw new IllegalArgumentException();
    }

    public float getFloat(int columnIndex) {
        if (!ArrayUtils.contains(this.mTranslateColumnIndices, Integer.valueOf(columnIndex))) {
            return super.getFloat(columnIndex);
        }
        throw new IllegalArgumentException();
    }

    public int getInt(int columnIndex) {
        if (!ArrayUtils.contains(this.mTranslateColumnIndices, Integer.valueOf(columnIndex))) {
            return super.getInt(columnIndex);
        }
        throw new IllegalArgumentException();
    }

    public long getLong(int columnIndex) {
        if (!ArrayUtils.contains(this.mTranslateColumnIndices, Integer.valueOf(columnIndex))) {
            return super.getLong(columnIndex);
        }
        throw new IllegalArgumentException();
    }

    public short getShort(int columnIndex) {
        if (!ArrayUtils.contains(this.mTranslateColumnIndices, Integer.valueOf(columnIndex))) {
            return super.getShort(columnIndex);
        }
        throw new IllegalArgumentException();
    }

    public String getString(int columnIndex) {
        if (ArrayUtils.contains(this.mTranslateColumnIndices, Integer.valueOf(columnIndex))) {
            return this.mTranslator.translate(super.getString(columnIndex), this.mAuxiliaryColumnIndex, getColumnName(columnIndex), this);
        }
        return super.getString(columnIndex);
    }

    public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {
        if (ArrayUtils.contains(this.mTranslateColumnIndices, Integer.valueOf(columnIndex))) {
            throw new IllegalArgumentException();
        }
        super.copyStringToBuffer(columnIndex, buffer);
    }

    public byte[] getBlob(int columnIndex) {
        if (!ArrayUtils.contains(this.mTranslateColumnIndices, Integer.valueOf(columnIndex))) {
            return super.getBlob(columnIndex);
        }
        throw new IllegalArgumentException();
    }

    public int getType(int columnIndex) {
        if (ArrayUtils.contains(this.mTranslateColumnIndices, Integer.valueOf(columnIndex))) {
            return 3;
        }
        return super.getType(columnIndex);
    }

    public boolean isNull(int columnIndex) {
        if (!ArrayUtils.contains(this.mTranslateColumnIndices, Integer.valueOf(columnIndex))) {
            return super.isNull(columnIndex);
        }
        return getString(columnIndex) == null;
    }
}
